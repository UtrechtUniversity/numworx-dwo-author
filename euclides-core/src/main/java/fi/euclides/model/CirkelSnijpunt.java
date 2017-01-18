package fi.euclides.model;

import fi.euclides.model.math.Exact;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.JMath;

public class CirkelSnijpunt extends PuntOp2<Cirkel,Cirkel> {


	public static final String TYPE = "Pcc";

	public CirkelSnijpunt(Cirkel cirkel1, Cirkel lijn2, Numbers x, Numbers y) {
		super(cirkel1, lijn2);
		setOther();
		setXY(x,y);
		recalc();
	}

	protected void setOther() {
// TODO deze code is niet 100% omdat er altijd een punt kan zijn dat dmv 'bewijs' incident is
//      met cirkel1 en lijn2
		if(lijn1.isr2c() && incident(lijn1.getRadius(), lijn2))
			other = lijn1.getRadius();
		else if(lijn2.isr2c() && incident(lijn2.getRadius(), lijn1))
			other = lijn2.getRadius();
	}
	
	public CirkelSnijpunt() {
		super();
	}

	
	private void recalcOther() {
		setDefined(lijn1.isDefined() && lijn2.isDefined() && other.isDefined());
		if(!isDefined())
			return;
		Numbers c1x = lijn1.getCenter().getX();
		Numbers c1y = lijn1.getCenter().getY();
		Numbers ox  = other.getX();
		Numbers oy  = other.getY();		
		final Numbers ldxn = Numbers.sub(lijn2.getCenter().getX(), c1x);
		final Numbers ldyn = Numbers.sub(lijn2.getCenter().getY(), c1y);
// TODO commom code		
		spiegeling(c1x, c1y, ox, oy, ldxn, ldyn);
		
	}

	protected void recalc() {
		if(other != null)
		{
			recalcOther();
			return;
		}
		
		
		Numbers dx, dy;
		dx = Numbers.sub(lijn2.getCenter().getX(), lijn1.getCenter().getX());
		dy = Numbers.sub(lijn2.getCenter().getY(), lijn1.getCenter().getY());
		double r  = JMath.hypot(dx.doubleValue(),dy.doubleValue());
// overlap?
		if(r*2 > lijn1.getD()+lijn2.getD()+2.0e-10)
		{
			setDefined(false);
			return;
		}
// cirkels waarvan de middens bijna gelijk zijn.
		if( (dx.equals(Numbers.ZERO) && dy.equals(Numbers.ZERO)) || 
				(!(dx instanceof Exact) || !(dy instanceof Exact) ) && r <= 1.0e-10)
		{
			setDefined(false);
			setXY(lijn1.getCenter().getX(), lijn1.getCenter().getY());
			return;
		}
		Numbers r2 = Numbers.add(Numbers.sqr(dx), Numbers.sqr(dy));
		Numbers lt = Numbers.add(r2, lijn1.getR2n());
		        lt = Numbers.sub(lt, lijn2.getR2n());
		Numbers r1 = Numbers.sqrt(r2);
		lt = Numbers.div(lt, Numbers.TWO);
		lt = Numbers.div(lt, r1);
		//double l=lt.doubleValue(); // (r*r+lijn1.getR()*lijn1.getR()-lijn2.getR()*lijn2.getR())/(2*r);
		dx = Numbers.div(dx, r1); dy = Numbers.div(dy,r1);
		Numbers x = lijn1.getCenter().getX();
		x = Numbers.add(x,Numbers.mul(lt, dx));
		Numbers y = lijn1.getCenter().getY();
		y = Numbers.add(y, Numbers.mul(lt, dy));
		Numbers h = Numbers.sub(lijn1.getR2n(), Numbers.sqr(lt));
//		double x=lijn1.getX()+lijn1.getR()+l*dx.doubleValue() ,
//			y=lijn1.getY()+lijn1.getR()+l*dy.doubleValue(),
//			h=lijn1.getR()*lijn1.getR()-l*l;
		if (h.doubleValue()<-1e-10) {
			setDefined(false);
			return;
		}
		if (h.doubleValue()<0) h=Numbers.ZERO;
		else h=Numbers.sqrt(h);
		setDefined(true);

		Numbers p1xn = Numbers.add(x, Numbers.mul(h, dy));
		Numbers p1yn = Numbers.sub(y, Numbers.mul(h, dx));
		Numbers p2xn = Numbers.sub(x, Numbers.mul(h, dy));
		Numbers p2yn = Numbers.add(y, Numbers.mul(h, dx));
		setFusedXY(p1xn, p1yn, p2xn, p2yn);
		
		//return new Coordinates(x+h*dy,y-h*dx,x-h*dy,y+h*dx);
			

	}

	public String key() {
		return TYPE;
	}

}
