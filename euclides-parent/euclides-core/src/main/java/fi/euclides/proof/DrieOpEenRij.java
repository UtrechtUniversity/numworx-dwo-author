package fi.euclides.proof;

import java.util.Enumeration;
import java.util.Vector;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DComparator;
import fi.euclides.util.Messages;

public class DrieOpEenRij extends LabelTester  {

	public static final String TYPE = "3"; //$NON-NLS-1$
	public String getSubKey() { return TYPE; }
	public static final double EPS = 0.0001;

	public DrieOpEenRij() {
		super(" "); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		Vector select = getModel().getSelect();
		if(select.size() == 3)
		{
			try {
				Punt p1 = (Punt) select.firstElement();
				Punt p3 = (Punt) select.lastElement();
				Punt p2 = (Punt) select.elementAt(1);

				Punt[] pp = new Punt[]{p1,p2,p3};
				
				addLabel(pp);
				
			} catch (ClassCastException e) {
				setStatus("Selecteer alleen punten");
			}
		} else 
			setStatus("Selecteer eerst 3 punten");
	}
	public static Numbers bracketn(Punt p1, Punt p2, Punt p3) {
		Numbers x1 = p1.getX();
		Numbers x2 = p2.getX();
		Numbers x3 = p3.getX();
		Numbers y1 = p1.getY();
		Numbers y2 = p2.getY();
		Numbers y3 = p3.getY();
			
	// bereken bracket [ p1 p2 p3 ] = 
	// [ x1 x2 x3 ]
	// [ y1 y2 y3 ]
	// [ 1  1  1  ]
	
		Numbers d = 
			Numbers.add(Numbers.sub(Numbers.mul(x1, Numbers.sub(y2,y3)) ,Numbers.mul(y1,Numbers.sub(x2,x3))) , 
				Numbers.sub(Numbers.mul(x2,y3),Numbers.mul(y2,x3)));
		return d;
	}

	/**
	 * @return
	 */
	public Destroyable[] createDepend() {
		return new Punt[3];
	}

	/**
	 * @param l
	 */
	protected boolean test(Label l) {
		Punt[] punten = (Punt[])l.getDepend();
		Numbers d = bracketn(punten[0], punten[1], punten[2]);
		return setState(l, d, EPS);
	}

	public boolean define(Label l) {
		boolean result = test(l);
		if(result)
		{
			Punt[] pp = (Punt[]) l.getDepend();
			DComparator.sort(pp);
			Punt p1 = pp[0];
			Punt p2 = pp[1];
			Punt p3 = pp[2];
			l.register(this);
 			l.setX(p2.getXd()+10);
			l.setY(p2.getY());
			l.setString(s(p1) +" "+s(p2) +" "+s(p3) + Messages.getString("DrieOpEenRij.4")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(check(p1,p2,p3))
				l.setState(Label.CONFIGURATION);
		}
		return result;
	}

	/**
	 * Geval p1,p2,p3 triviaal collineair.
	 * 	P1 is midpoint p2,p3, met permutaties.
	 *  TODO, 
	 *  p1,p2,p3 liggen op een gemeenschappelijke lijn.
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	private static boolean checkM(Punt p1, Punt p2, Punt p3) {
		return	MidpointTester.check(p1, p2, p3) ||
				MidpointTester.check(p2, p1, p3) ||
				MidpointTester.check(p3, p2, p1)
		;
	}
	
	private static boolean check(Punt p1, Punt p2, Punt p3)
	{
		boolean result = checkM(p1, p2, p3);
		if(!result)
		{
			Vector v = new Vector();
			addLines(v, p1.getDepend());
			addLines(v, p2.getDepend());
			addLines(v, p3.getDepend());
			Enumeration e = v.elements();
			while (!result && e.hasMoreElements()) {
				Lijn lijn = (Lijn) e.nextElement();
				result = IncidentHandler.incident(p1, lijn) && IncidentHandler.incident(p2, lijn) && IncidentHandler.incident(p3, lijn);
			}
		}			
		return result;
	}

	private static void addLines(Vector v, Destroyable[] d) {
		for (int i = 0; i < d.length; i++) {
			Destroyable destroyable = d[i];
			if(destroyable instanceof Lijn && !v.contains(destroyable))
				v.addElement(destroyable);
		}
	}
}
