package fi.euclides.model;

import fi.euclides.model.math.Exact;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.IncidentHandler; // TODO deze methode verplaatsen

public class CirkelLijnSnijpunt extends PuntOp2<Lijn,Cirkel> {

	public static final String TYPE = "Pcl";

	public CirkelLijnSnijpunt(Lijn lijn, Cirkel cirkel, Numbers x, Numbers y) {
		super(lijn, cirkel);
		setXY(x,y);
		setOther();		
		recalc();
	}


	protected void setOther() {
		if(lijn1 instanceof Bissectrice)
		{
			Bissectrice b  = (Bissectrice) lijn1;
			if(incident(b.getP2(),lijn2)) other = b.getP2();
		} else if(lijn1 instanceof PuntenLijn)
		{
			setOtherFromDepend(lijn1);
		} else if(lijn1 instanceof LijnPuntCombi) {
			LijnPuntCombi lp = (LijnPuntCombi)lijn1;
			if(incident(lp.getPunt(), lijn2)) other = lp.getPunt();
		} 
		if(other == null && lijn2.isr2c())
		{
			if(lijn2 instanceof Cirkel3)
			{
				setOtherFromDepend(lijn2);
			}
			
			Punt p = lijn2.getRadius();
			if(IncidentHandler.incident(p, lijn1))
				other = p;
		}
		
	}

	private void setOtherFromDepend(Cirkel d) {
		Punt p[] = (Punt[])d.getDepend();
		for (int i = 0; i < p.length; i++) {
			if(IncidentHandler.incident(p[i], lijn1))
				other = p[i];
		}
	}

	private void setOtherFromDepend(Lijn d) {
		Punt p[] = (Punt[])d.getDepend();
		for (int i = 0; i < p.length; i++) {
			if(incident(p[i], lijn2))
				other = p[i];
		}
	}

/*
	public static Coordinates intersect
		(PrimitiveLineObject l, PrimitiveCircleObject c)
	// compute the intersection coordinates of a line with a circle
	{	double x=c.getX(),y=c.getY(),r=c.getR();
		double d=(x-l.X1)*l.DY-(y-l.Y1)*l.DX;
		if (Math.abs(d)>r+1e-10) return null;
		x-=d*l.DY; y+=d*l.DX;
		double h=r*r-d*d;
		if (h>0) h=Math.sqrt(h);
		else h=0;
		return new Coordinates(x+h*l.DX,y+h*l.DY,x-h*l.DX,y-h*l.DY);
	}


 */	
	
	public CirkelLijnSnijpunt() {
	}

	protected void recalc() {
		if(other != null)
		{
			recalcOther();
			return;
		}
		
		double r = lijn2.getR();
		Numbers x = lijn2.getCenter().getX();
		Numbers y = lijn2.getCenter().getY();
		Numbers d = Numbers.sub(
				Numbers.mul(Numbers.sub(x,lijn1.getX1n()), lijn1.getDYn()),
				Numbers.mul(Numbers.sub(y, lijn1.getY1n()), lijn1.getDXn())
		);
		
		Numbers n = Numbers.add(Numbers.sqr(lijn1.getDXn()), Numbers.sqr(lijn1.getDYn()));
		n = Numbers.sqrt(n);
		d = Numbers.div(d, n);
		if( Math.abs(d.doubleValue())>r+1e-10)
		{
			setDefined(false);
			notifyObservers();
			return;
		}
		x = Numbers.sub(x, Numbers.div(Numbers.mul(d,lijn1.getDYn()), n));
		y = Numbers.add(y, Numbers.div(Numbers.mul(d, lijn1.getDXn()), n));
		Numbers h = Numbers.sub(lijn2.getR2n(),Numbers.sqr(d));
		if(h instanceof Exact || h.doubleValue()>0){
			h = Numbers.sqrt(h);
		}
		else { // inexact... complex?
			h  = Numbers.createDouble(0.0);
		}
		Numbers hn = Numbers.div(h,n);
		setFusedXY(
				Numbers.add(x, Numbers.mul(hn, lijn1.getDXn())),
				Numbers.add(y, Numbers.mul(hn, lijn1.getDYn())),
				Numbers.sub(x, Numbers.mul(hn, lijn1.getDXn())),
				Numbers.sub(y, Numbers.mul(hn, lijn1.getDYn()))
		);
		setDefined(lijn1.contains(this));
	
	}

	private void recalcOther() {
		setDefined(lijn2.isDefined() && lijn1.isDefined() /*&& other.isDefined()*/);
		if(!isDefined())
			return;
		Numbers c1x = lijn2.getCenter().getX();
		Numbers c1y = lijn2.getCenter().getY();
		Numbers ox = other.getX();
		Numbers oy = other.getY();
		Numbers ldx = lijn1.getDYn();
		Numbers ldy = Numbers.neg(lijn1.getDXn());
		spiegeling(c1x,c1y,ox,oy,ldx,ldy);
		setDefined(lijn1.contains(this));
	}

	public String key() {
		return TYPE;
	}

}
