package fi.euclides.proof;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.event.Tracker;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.PuntOp2;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;

public class PointOnObject extends LabelTester {

	static final double EPS = 0.0001;
	public static final String TYPE = "O"; //$NON-NLS-1$
	public String getSubKey() { return TYPE; }
	
	PointOnObject() {
		super(" "); //$NON-NLS-1$
	}

	/**
	 * @param p
	 * @param l
	 * @return
	 */
	static final Numbers EPSn = Numbers.createDouble(EPS);
	
	static Numbers bracket(Punt p, Lijn l) {
		Numbers x1 = p.getX();
		Numbers x2 = l.getX1n();
		Numbers x3 = l.getX2n();
		Numbers y1 = p.getY();
		Numbers y2 = l.getY1n();
		Numbers y3 = l.getY2n();
	
	if(l instanceof Segment)
	{
		if(x1.doubleValue() > Math.max(x2.doubleValue(), x3.doubleValue())+EPS) {return EPSn;}
		if(x1.doubleValue() < Math.min(x2.doubleValue(), x3.doubleValue())-EPS) {return EPSn;}
		if(y1.doubleValue() > Math.max(y2.doubleValue(), y3.doubleValue())+EPS) {return EPSn;}
		if(y1.doubleValue() < Math.min(y2.doubleValue(), y3.doubleValue())-EPS) {return EPSn;}
	}
		
	// bereken bracket [ p1 p2 p3 ] = 
	// [ x1 x2 x3 ]
	// [ y1 y2 y3 ]
	// [ 1  1  1  ]
	
	Numbers d = 
		Numbers.add(Numbers.sub(Numbers.mul(x1, Numbers.sub(y2,y3)) ,Numbers.mul(y1,Numbers.sub(x2,x3))) , 
		Numbers.sub(Numbers.mul(x2,y3),Numbers.mul(y2,x3)));
		return Numbers.abs(d);
	}

//	private void check(Numbers d, Punt p, Destroyable l) {
//		if (setState(null, d, EPS))
//		{
//			Label label = createLabel(p, l, getTracker());
//			setState(label,d,EPS);
//			add(label);
//		}
//		else 
//			nee();
//	}

//	/**
//	 * DONE deze naar LabelTester
//	 * @see LabelTester#addLabel(Destroyable[])
//	 * 
//	 * @param label
//	 */
//	private void add(Label label) {
//		getModel().add(label);
//		setStatus(Messages.getString("PointOnObject.2") + label.getString()); //$NON-NLS-1$
//	}

	/**
	 * @param p
	 * @param l
	 * @return
	 */
	public static Label createLabel(Punt p, Destroyable l, Tracker t) {
		Label label = new Label();
		label.setDepend(new Destroyable[] { p, l });
		label.setX(p.getXd()+10);
		label.setY(p.getY());
		label.setString(Messages.getString("PointOnObject.3") + t.getMapper().toString(p) +Messages.getString("PointOnObject.4") + (l.key().startsWith("c")?Messages.getString("PointOnObject.6"):Messages.getString("PointOnObject.7"))+ t.getMapper().toString(l));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		label.register(t.getRegistered(TYPE));
		return label;
	}

//	/**
//	 * 
//	 */
//	private void nee() {
//		setStatus(Messages.getString("PointOnObject.8")); //$NON-NLS-1$
//	}

	
	
	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		Vector select = getModel().getSelect();
		if(select.size() == 2) {
			Destroyable o1 = (Destroyable) select.firstElement();
			Destroyable o2 = (Destroyable) select.lastElement();
			Destroyable[] depend = new Destroyable[] { o1, o2 };
			addLabel(depend);
			return;
//			if(o1 instanceof Punt && o2 instanceof Lijn )
//			{
//				puntOpLijn((Punt)o1, (Lijn)o2);
//				return;
//			} else if (o1 instanceof Lijn && o2 instanceof Punt) {
//				puntOpLijn((Punt)o2, (Lijn)o1);
//				return;
//			} else if (o1 instanceof Punt && o2 instanceof Cirkel) {
//				puntOpCirkel((Punt)o1, (Cirkel) o2);
//				return;
//			} else if (o1 instanceof Cirkel && o2 instanceof Punt) {
//				puntOpCirkel((Punt)o2, (Cirkel) o1);
//				return;
//			}
		} 
		setStatus("Selecteer een punt en een lijn of cirkel");
	}

//	private void puntOpCirkel(Punt o1, Cirkel o2) {
//		if(PuntOp2.incident(o1, o2))
//		{
//			Label label = createLabel(o1, o2, getTracker());
//			label.setState (Label.CONFIGURATION);
//			label.value = Numbers.ZERO;
//			add(label);
//			return;
//		}
//		Numbers d = bracket(o1, o2);
//		check ( d, o1, o2);
//	}

	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static Numbers bracket(Punt o1, Cirkel o2) {
		Numbers x = Numbers.sub(o1.getX(),o2.getCenter().getX());
		Numbers y = Numbers.sub(o1.getY(),o2.getCenter().getY());
		final Numbers r2 = o2.getR2n();
		Numbers d = Numbers.add(Numbers.sqr(x),Numbers.sqr(y));
		d = Numbers.sub(d, r2);
		return Numbers.abs(d);
	}

	/**
	 * @param l
	 */
	protected boolean test(Label l) {
		Numbers d;
		Destroyable depend[] = l.getDepend();
		if(depend[1]instanceof Lijn)
			d = bracket((Punt)depend[0], (Lijn)depend[1]);
		else if(depend[1] instanceof Cirkel)
			d = bracket((Punt)depend[0], (Cirkel)depend[1]);
		else if(depend[1] instanceof Locus) 
			d = bracket((Punt)depend[0], (Locus)depend[1]);
		else if(depend[1] instanceof Kegelsnede2) 
			d = bracket((Punt)depend[0], (Kegelsnede2)depend[1]);
		else	
			d = EPSn;
		return setState(l, d, EPS);
	}

	static Numbers bracket(Punt punt, Kegelsnede2 k) {
		return k.incident(punt);
	}

	static Numbers bracket(Punt punt, Locus locus) {
		if(punt == locus.getDest())
			return Numbers.ZERO;
		if(punt instanceof PuntOp && ((PuntOp<?>) punt).getOp() == locus)
			return Numbers.ZERO;
		
		return EPSn;
	}

	/**
	 * @return
	 */
	public Destroyable[] createDepend() {
		return new Destroyable[2];
	}

	public boolean define(Label label) {
		if(label.getDepend()[1] instanceof Punt)
		{
// Swap, Punt als eerste...
			Destroyable[] dd = label.getDepend();
			Destroyable d = dd[0];
			dd[0] = dd[1];
			dd[1] = d;
		}
// DONE stukjes uit: puntOpLijn/Cirkel...
		if(test(label))
		{
			Destroyable[] dd = label.getDepend();
			Punt p = (Punt)dd[0];
			Destroyable l = dd[1];
			if(l instanceof Lijn && IncidentHandler.incident(p, (Lijn)l)
			|| l instanceof Cirkel && PuntOp2.incident(p, (Cirkel)l)				
			) {
				label.setState(Label.CONFIGURATION);
				label.value=Numbers.ZERO;
			}
			label.setX(p.getXd()+10);
			label.setY(p.getY());
			label.setString(Messages.getString("PointOnObject.9") + s(p) +Messages.getString("PointOnObject.10") + (l.key().startsWith("c")?Messages.getString("PointOnObject.12"):Messages.getString("PointOnObject.13"))+ s(l));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			label.register(this);			
			return true;
		}
		return false;
	}

}
