package fi.euclides.proof;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Triangle;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.math.FloatingPoint;
import fi.euclides.model.math.Numbers;

public class Equidistant extends LabelTester implements Comparator<Numbers>  {

	public static final String TYPE = "="; //$NON-NLS-1$
	public String getSubKey() { return TYPE; }
	
	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		Vector select = getModel().getSelect();
		int size = select.size();
		Destroyable[] depend = createDepend();
		if(size == 2)
		{
			Object o1 = select.firstElement();
			Object o4 = select.lastElement();
			if(o1 instanceof Segment && o4 instanceof Segment ||
			   o1 instanceof Cirkel  && o4 instanceof Cirkel  ||
			   o1 instanceof Triangle && o4 instanceof Triangle
			)
			{
				depend[0] = (Destroyable) o1;
				depend[1] = (Destroyable) o4;
				swap(depend);
				addLabel(depend);
				return;
			} else if(o1 instanceof Label && o4 instanceof Label)
			{
				depend[0] = (Destroyable) o1;
				depend[1] = (Destroyable) o4;
				swap(depend);
				addLabel(depend);
				return;
			}
		} else if(size == 4)
		{
			depend[0] = (Destroyable) select.firstElement();
			depend[1] = (Destroyable) select.elementAt(1);
			depend[2] = (Destroyable) select.elementAt(2);
			depend[3] = (Destroyable) select.lastElement();
			if(depend[0] instanceof Punt && 
					depend[1] instanceof Punt && 
					depend[2] instanceof Punt && 
					depend[3] instanceof Punt )
			{	swap(depend);
				addLabel(depend);
				return;
			}
			
		} else if(size == 3)
		{
			depend[0] = (Destroyable) select.firstElement();
			depend[1] = (Destroyable) select.elementAt(1);
			depend[2] = depend[1];
			depend[3] = (Destroyable) select.lastElement();
			if(depend[0] instanceof Punt &&
				depend[1] instanceof Punt &&
				depend[3] instanceof Punt
			) {
				swap(depend);
				addLabel(depend);
				return;
			}
		}
		setStatus(string);
	}

	private void swap(Destroyable[] d) {
		Destroyable tmp;
		if(d[0].getIndex()>d[1].getIndex())
		{
			tmp=d[0];d[0]=d[1];d[1]=tmp;
		}
		if(d[2]==null)
			return;
		if(d[2].getIndex()>d[3].getIndex())
		{
			tmp=d[2];d[2]=d[3];d[3]=tmp;
		}
		if(d[0].getIndex()>d[2].getIndex() 
		|| d[0] == d[2] && d[1].getIndex()>d[3].getIndex()		
		) {
			tmp=d[0];d[0]=d[2];d[2]=tmp;
			tmp=d[3];d[3]=d[1];d[1]=tmp;
		}
	}

	/**
	 * @param o1
	 * @param o4
	 * @param l 
	 * @return
	 */
	private boolean test(Segment o1, Segment o4, Label l) {
		return test(o1.getP1(),  o1.getP2(),  o4.getP1(),  o4.getP2(), l);
	}
	private boolean test(Cirkel o1, Cirkel o4, Label l) {
		return test(o1.getRadius(),  o1.getRadius2(),  o4.getRadius(),  o4.getRadius2(), l);
	}

	boolean test(Punt p1, Punt p2, Punt p3, Punt p4, Label l)
	{
		Numbers dx1 = Numbers.sub(p1.getX(),p2.getX());
		Numbers dy1 = Numbers.sub(p1.getY(),p2.getY());
		Numbers dx2 = Numbers.sub(p3.getX(),p4.getX());
		Numbers dy2 = Numbers.sub(p3.getY(),p4.getY());
		Numbers d1  = Numbers.add(Numbers.sqr(dx1),Numbers.sqr(dy1));
		Numbers d2  = Numbers.add(Numbers.sqr(dx2),Numbers.sqr(dy2));
//System.out.println(p1 + " " + p2 + " " + d1 + " " + p3 + " " + p4 + " " + d2);
		return setState(l, Numbers.sub(d1, d2), 0.000001);
	}
	
	boolean test(Triangle a, Triangle b, Label l)
	{ // SSS test
		Punt[] pa = (Punt[]) a.getDepend();
		Punt[] pb = (Punt[]) b.getDepend();
		Numbers[] da = new Numbers[3];
		Numbers[] db = new Numbers[3];
		lengths(pa, da); lengths(pb, db);
		Arrays.sort(da, this);
		Arrays.sort(db, this);
		da[0] = Numbers.sqr(Numbers.sub(da[0], db[0]));
		da[1] = Numbers.sqr(Numbers.sub(da[1], db[1]));
		da[2] = Numbers.sqr(Numbers.sub(da[2], db[2]));
		return setState(l, Numbers.add(Numbers.add(da[0], da[1]), da[2]), 0.00001);
	}
	
	/**
	 * @param ta
	 * @param da
	 */
	static void lengths(Punt[] ta, Numbers[] da) {
		da[0] = Numbers.add( 
				Numbers.sqr(Numbers.sub(ta[0].getX(),ta[1].getX()))
			  , Numbers.sqr(Numbers.sub(ta[0].getY(),ta[1].getY()))
			  );
		da[1] = Numbers.add( 
				Numbers.sqr(Numbers.sub(ta[2].getX(),ta[1].getX()))
			  , Numbers.sqr(Numbers.sub(ta[2].getY(),ta[1].getY()))
			  );
		da[2] = Numbers.add( 
				Numbers.sqr(Numbers.sub(ta[0].getX(),ta[2].getX()))
			  , Numbers.sqr(Numbers.sub(ta[0].getY(),ta[2].getY()))
			  );
	}

	
	
	public Equidistant() {
		super(Messages.getString("Equidistant.9")); //$NON-NLS-1$
	}

/**
 * @param l
 * @return
 */
protected boolean test(Label l) {
	boolean  b;
	final Destroyable[] depend = l.getDepend();
	if(depend[0] instanceof Label)
	{
		final Label label0 = (Label) depend[0];
		final Label label1 = (Label) depend[1];
		if(label0.getState() == Label.HOEK && label1.getState() == Label.HOEK )
		{
			if(label0.getAdapter().adapt(Numbers[].class) == null)
				label0.getRegistered().define(label0);
			if(label1.getAdapter().adapt(Numbers[].class) == null)
				label1.getRegistered().define(label1);
			Numbers[] extra0 = label0.getAdapter().adapt(Numbers[].class);			
			Numbers[] extra1 = label1.getAdapter().adapt(Numbers[].class);
			Numbers mul = Numbers.mul(extra0[0], extra1[1]);
			double f = mul.doubleValue();
			f = Math.max(0.0001, Math.abs(f)*0.0001); // mul explodeerd, gebruik relatieve error
			b = setState(l, Numbers.sub( mul, Numbers.mul(extra0[1], extra1[0])), f);
		} else {
// infinity equals infinity but inf-inf = inf, not ZERO
		b = ( label0.value instanceof FloatingPoint || label1.value instanceof FloatingPoint ) &&
			label0.value.doubleValue() == label1.value.doubleValue();
		if(!b)
		{	b = setState(l, Numbers.sub(label0.value, label1.value), 0.00001);
			if(Double.isNaN(label0.value.doubleValue()))
					b = true; // FIXME a HACK
		} else {
			l.value = Numbers.createDouble(0.0);
			l.setState(Label.INEXACT);
		}}
// TODO same subkey?
// expr == afstand? FIXME nu even uitgezet!
// 
//		b &= label0.getSubKey().equals(label1.getSubKey());
// text not always same, if using OMNumber.
		//b = (label0).getString().equals((label1).getString());
	} else
	if(depend[0] instanceof Triangle)
	{
		b = test((Triangle)depend[0], (Triangle)depend[1], l);
	} else
	if(depend[0] instanceof Segment)
		b = test((Segment)depend[0], (Segment)depend[1], l);
	else if (depend[0] instanceof Cirkel)
		b = test((Cirkel)depend[0], (Cirkel)depend[1], l);
	else 
		b = test((Punt)depend[0], (Punt)depend[1],(Punt)depend[2], (Punt)depend[3], l);
	return b;
}

/**
 * @return
 */
public Destroyable[] createDepend() {
	return new Destroyable[4];
}

public boolean define(Label l) {
	Destroyable[] depend = l.getDepend();
	swap(depend);
	if(test(l))
	{
		if(depend[0] instanceof Segment )
		{
			l.setString( s(depend[0]) + Messages.getString("Equidistant.1") + s(depend[1]) + Messages.getString("Equidistant.2"));
			l.setX(((Lijn) depend[0]).getX1n());
			l.setY(((Lijn) depend[1]).getY1n());
		} 
		else if ( depend[0] instanceof Cirkel)
		{
			Cirkel c1 = (Cirkel) depend[0];
			Cirkel c2 = (Cirkel) depend[1];
			l.setString( s(c1) + Messages.getString("Equidistant.1") + s(c2) + Messages.getString("Equidistant.10"));
			Numbers x1 = c1.getCenter().getX();
			Numbers x2 = c2.getCenter().getX();
			Numbers y1 = c1.getCenter().getY();
			Numbers y2 = c2.getCenter().getY();		
			l.setX(Numbers.div(Numbers.add(x1,x2), Numbers.TWO) );
			l.setY(Numbers.div(Numbers.add(y1,y2), Numbers.TWO) );
		}
		
		else if(depend[0] instanceof Label ) {
				Label l1 = (Label)depend[0];
				Label l4 = (Label)depend[1];
				if(l1.equals(l4))
					return false;
				LabelValue lv1 = (LabelValue) l1.getRegistered();
				LabelValue lv2 = (LabelValue) l4.getRegistered();
				l.setString(lv1.getSymbolicValue(l1) + Messages.getString("Equidistant.3") + lv2.getSymbolicValue(l4) + Messages.getString("Equidistant.4")); //$NON-NLS-1$ //$NON-NLS-2$
				l.setX(l1.getX());
				l.setY(l4.getY());
		} else if (depend[0] instanceof Punt)
		{
			Destroyable o1 = depend[0];
			Destroyable o2 = depend[1];
			Destroyable o3 = depend[2];
			Destroyable o4 = depend[3];
			if(o1==o3&&o2==o4) //XY==XY considered FALSE
				return false;
			l.setString(s(o1) + s(o2) + Messages.getString("Equidistant.5") + s(o3) + s(o4)); //$NON-NLS-1$
			l.setX(((Punt) o1).getX());
			l.setY(((Punt) o1).getY());	
		} else if(depend[0] instanceof Triangle) {
			if(depend[0].equals(depend[1]))
				return false;
			l.setString( s(depend[0]) + " ≅ " + s(depend[1]));
		}
		return true;
	}
	return false;
}

public int compare(Numbers n1, Numbers n2)
{
	return Double.compare(n1.doubleValue(), n2.doubleValue());
}

}
