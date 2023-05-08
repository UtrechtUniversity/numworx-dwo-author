package fi.euclides.proof;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DComparator;

public class VierOpEenCirkel extends LabelTester {

	public static final String TYPE = "4"; //$NON-NLS-1$
	public String getSubKey() { return TYPE; }
	
	private static final double EPS = 0.0001;

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#command()
	 */
	public void command() {
		Vector select = getModel().getSelect();
		if(select.size() == 4)
		{
			try {
				Punt p1 = (Punt) select.firstElement();
				Punt p4 = (Punt) select.lastElement();
				Punt p2 = (Punt) select.elementAt(1);
				Punt p3 = (Punt) select.elementAt(2);
				Punt[] punten = new Punt[]{p1,p2,p3,p4};
				addLabel(punten);
			} catch (ClassCastException e) {
				setStatus("Selecteer alleen punten");
			}			
		} else 
			setStatus("Selecteer eerst 4 punten");
	}

	public VierOpEenCirkel() {
		super(TYPE);
	}

	public Destroyable[] createDepend() {
		return new Punt[4];
	}

	protected boolean test(Label l) {
		Punt[] punten = (Punt[])l.getDepend();
		Numbers d = cyclic(punten[0], punten[1], punten[2], punten[3]);
		return setState(l, d, EPS);
	}
/**
 * test p[1..4] voldoen aan de constante hoek stelling.
 * @param p1
 * @param p2
 * @param p3
 * @param p4
 * @return
 */
	public static Numbers cyclic(Punt p1, Punt p2, Punt p3, Punt p4) {
		Numbers dx13 = Numbers.sub(p3.getX(), p1.getX());
		Numbers dy13 = Numbers.sub(p3.getY(), p1.getY());
		Numbers dx23 = Numbers.sub(p3.getX(), p2.getX());
		Numbers dy23 = Numbers.sub(p3.getY(), p2.getY());
		Numbers x123 = Numbers.add(Numbers.mul(dx13, dx23), Numbers.mul(dy13, dy23));
		Numbers y123 = Numbers.sub(Numbers.mul(dx13, dy23), Numbers.mul(dx23, dy13));

		Numbers dx14 = Numbers.sub(p4.getX(), p1.getX());
		Numbers dy14 = Numbers.sub(p4.getY(), p1.getY());
		Numbers dx24 = Numbers.sub(p4.getX(), p2.getX());
		Numbers dy24 = Numbers.sub(p4.getY(), p2.getY());
		Numbers x124 = Numbers.add(Numbers.mul(dx14, dx24), Numbers.mul(dy14, dy24));
		Numbers y124 = Numbers.sub(Numbers.mul(dx14, dy24), Numbers.mul(dy14, dx24));

		Numbers d1 = Numbers.mul(x123, y124);
		Numbers d2 = Numbers.mul(x124, y123);
		
		return Numbers.sub(d1,d2);
		
	}

	public boolean define(Label label) {
		if(test(label))
		{
			Punt[] pp = (Punt[])label.getDepend();
			DComparator.sort(pp);
			Punt p1 = pp[0];
			Punt p2 = pp[1];
			Punt p3 = pp[2];
			Punt p4 = pp[3];
			label.register(this);
				label.setX(Numbers.div(Numbers.add(p2.getX(), p3.getX()), Numbers.TWO));
			label.setY(p2.getY());
			label.setString(s(p1) +" "+s(p2) +" "+s(p3) + " " + s(p4) + Messages.getString("VierOpEenCirkel.4")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			return true;
		}
		return false;
	}

}
