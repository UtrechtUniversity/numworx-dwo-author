package fi.euclides.event;

import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.MP;
import fi.euclides.model.SegmentVisitor;
import fi.euclides.model.Triangle;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;


/**
 * Decorator pattern
 * @author Wim
 *
 */
public class HitTester implements Visitor, SegmentVisitor  {

	//private static HitTester instance;
	
	private Visitor v;
	protected double lastx;
	protected double lasty;
	protected double marge = 5;
	 
	public void visitCirkel(Cirkel c) {
		// FIXME deze moet /max(r,1);
		double r = c.getR();
		double x = c.getCenter().getXd()-lastx;
		double y = c.getCenter().getYd()-lasty;
		double r2 =  Math.hypot(x,y);
		boolean done = Math.abs(r2-r) < marge;
		if(done)
			call(c);
	}

	public void visitBoog(Boog b) {
		//visitCirkel(b); // FIXME
		double r = b.getR();
		double x = b.getCenter().getXd()-lastx;
		double y = b.getCenter().getYd()-lasty;
		double r2 = Math.hypot(x,y);
		boolean done = Math.abs(r2-r) < marge;
		if(!done) return;
		double h = Math.atan2(y, -x);
		double s = b.getStart();
		double l = b.length();
		if(l >= 0) {
			if(h < s) h += Math.PI * 2.0;
			done = s <= h && h <= s+l;
		} else {
			if(h > s) h -= Math.PI * 2.0;
			done = s+l <= h && h <= s;
		}
		if(done)
			call(b);
	}
	

	MP tmp;
	boolean tmpdone;
	public void visitLijn(Lijn lijn) {
		double x1 = lijn.getX1();
		double y1 = lijn.getY1();
		double dx = lijn.getDX();
		double dy = lijn.getDY();
		double uit = dx * (lasty-y1) - dy * (lastx - x1);
		double len = dx * dx + dy * dy;
		double d = (uit * uit / len );
//System.out.println(d + " " + uit + " " + len);
		if ( (d) <= (marge*marge) )
		{
			if(tmp != null)
			{
				if(!tmpdone)
					call(tmp);
				tmpdone=true;
			} else
			{
				if(lijn instanceof Ray)
				{
					if(dx > 0 && lastx < x1)
					{
						return;
					}
					if(dx < 0 && lastx > x1)
					{
						return;
					}
					if(dy > 0 && lasty < y1)
					{
						return;
					}
					if(dy < 0 && lasty > y1)
					{
						return;
					}
				}
				call(lijn);
			}
		}
		
	}

	public void visitPunt(Punt p) {
		if(Math.abs(p.getXd()-lastx)<=marge &&
				Math.abs(p.getYd()-lasty)<=marge
		)
		{	
			v.visitPunt(p);
		}
	}

	public void setVisitor(Visitor v) {
		this.v = v;
	}

	public void setXY(double lastx, double lasty) {
		this.lastx = lastx;
		this.lasty = lasty;		
	}

	public double getX() {
	  return lastx;
	}
	public double getY() {
	  return lasty;
	}
	
	public void visitSegment(Segment s) {
		double maxX = s.getX1();
		double minX = s.getX2();
		if(minX>maxX) { double t= maxX; maxX=minX; minX=t; }

		double maxY = s.getY1();
		double minY = s.getY2();
		if(minY>maxY) { double t= maxY; maxY=minY; minY=t; }
		if(
				lastx > minX-marge &&
				lastx < maxX+marge &&
				lasty > minY-marge &&
				lasty < maxY+marge
		)		
				visitLijn(s);		
	}

	public void visitLabel(Label label) {
		double x = -label.getXd()+lastx;
		double y = -label.getYd()+lasty;
		if( x > 0 && x < 10  && y < 0 && y > -10)
		{	
			call(label);
		}
	}

	public void visitMP(MP locus) {
		tmp = locus;
		tmpdone=false;
		locus.visitSegments(this);
		tmp = null;
	}

	public void visitTriangle(Triangle t) {
		visitMP(t);		
	}

	public void visitKegelsnede(Kegelsnede2 k) {
		visitMP(k);		
	}

	public void visitLocus(Locus l) {
		visitMP(l);		
	}

	public Numbers clipBottom() {
		return Numbers.createDouble(lasty+10);
	}

	public Numbers clipLeft() {
		return Numbers.createDouble(lastx-10);
	}

	public Numbers clipRight() {
		return Numbers.createDouble(lastx+10);
	}

	public Numbers clipTop() {
		return Numbers.createDouble(lasty-10);
	}

//	/**
//	 * @return the instance
//	 * @deprecated use getTracker().getHitTester();
//	 */
//	public static HitTester getInstance() {
//		if(instance == null)
//			instance = new HitTester();
//		return instance;
//	}
//
//	/**
//	 * @param instance the instance to set
//	 * @deprecated
//	 */
//	public static void setInstance(HitTester instance) {
//		HitTester.instance = instance;
//	}

	public HitTester copy() {
		return new HitTester();
	}

	public void done() {
	}

	protected void call(Destroyable d) {
		d.visit(v);
	}
}
