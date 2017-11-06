package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class SpiegelPunt extends Punt implements Visitor, Observer {

	public static final String TYPE = "Px";
	private Destroyable mirror;
	private Punt punt;
	public  Destroyable orig;
	
	/**
	 * @param mirror
	 * @param punt
	 */
	public SpiegelPunt(Destroyable mirror, Punt punt) {
		setMirror( mirror );
		setPunt( punt );
		orig = punt;
		update(this, null);
	}

	public SpiegelPunt() {
	}


	public SpiegelPunt(Destroyable mirror, Punt punt, Destroyable orig) {
		this(mirror, punt);
		this.orig = orig;
		orig.addObserver(this);
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Punt#key()
	 */
	public String key() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Punt#move(double, double)
	 */
	public void moveTo(Numbers dx, Numbers dy) {
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Punt#read(fi.euclides.model.Codec)
	 */
	public void read(Codec codec) throws IOException {
		super.read(codec);
		Destroyable d = codec.readDestroyable();
		if(d!=null) setMirror(d);
		Punt p = codec.readPunt();
		if(p!=null) setPunt(p);
		orig = codec.readDestroyable();
		if(orig != null) orig.addObserver(this);
	}

	public Destroyable[] getDepend() 
	{
		return new Destroyable[] { mirror, punt, orig };
	}
	
	
	/* (non-Javadoc)
	 * @see fi.euclides.model.Punt#write(fi.euclides.model.Codec)
	 */
	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.writeDestroyable(mirror);
		codec.writePunt(punt);
		codec.writeDestroyable(orig);
	}

	public void visitCirkel(Cirkel c) {
		Numbers x1 = c.getCenter().getX();
		Numbers y1 = c.getCenter().getY();
		Numbers dx = Numbers.sub(punt.getX(),x1);
		Numbers dy = Numbers.sub(punt.getY(),y1);
		Numbers d = Numbers.add(Numbers.sqr(dx), Numbers.sqr(dy));
		setDefined(!d.equals(Numbers.ZERO));
		Numbers d1 = Numbers.div(c.getR2n(), d);
		setXY(Numbers.add(x1 , Numbers.mul(d1,dx)), 
			  Numbers.add(y1 , Numbers.mul(d1,dy)));
	}
	
	public void visitBoog(Boog b) {
		visitCirkel(b.base);
	}

	public void visitLabel(Label vector) {
		Numbers[] v;
		Numbers x0 = punt.getX();
		Numbers y0 = punt.getY();
		v = vector.getAdapter().adapt(Numbers[].class);
		if(v == null)
		{
			vector.getRegistered().define(vector);
			v = vector.getAdapter().adapt(Numbers[].class);
		}
		switch(vector.getState())
		{
		case Label.VECTOR:		    
			setXY(Numbers.add(x0, v[0]), Numbers.add(y0, v[1]));
			break;
		case Label.HOEK:
			Numbers hyp = Numbers.hypot(v[0], v[1]);
// TODO find center: Corner, Snijpunt of 2 lines
			Punt center = vector.getRegistered().getModel().getO();
			x0 = Numbers.sub(x0, center.getX());
			y0 = Numbers.sub( center.getY(), y0);
			Numbers x = Numbers.sub(Numbers.mul(x0, v[0]), Numbers.mul(y0, v[1]));
			Numbers y = Numbers.add(Numbers.mul(x0, v[1]), Numbers.mul(y0, v[0]));
			//y = Numbers.neg(y);
			x = Numbers.div(x, hyp);
			y = Numbers.div(y, hyp);
			x = Numbers.add(x, center.getX());
			y = Numbers.sub(center.getY(), y);
			setXY(x,y);
			break;
		default:
			setXY(x0, y0);
		} 

	}

	public void visitLijn(Lijn l) {
		Numbers x1 = l.getX1n();
		Numbers y1 = l.getY1n();
		Numbers x = Numbers.sub(punt.getX(),x1);
		Numbers y = Numbers.sub(punt.getY(),y1);
		final Numbers ldxn = l.getDXn();
		final Numbers ldyn = l.getDYn();
		Numbers d = Numbers.add(Numbers.mul(x, ldxn) , Numbers.mul(y , ldyn));
		Numbers hl = Numbers.add(Numbers.sqr(ldxn), Numbers.sqr(ldyn));
		d = Numbers.div(d , hl);
		setXY( Numbers.sub(Numbers.mul(Numbers.add(x1 , Numbers.mul(d , ldxn)),Numbers.TWO),punt.getX()), 
			   Numbers.sub(Numbers.mul(Numbers.add(y1 , Numbers.mul(d , ldyn)),Numbers.TWO),punt.getY())
		);
	}

	public void visitPunt(Punt p) {
		setXY( Numbers.sub(Numbers.mul(Numbers.TWO, p.getX()), punt.getX()),
				Numbers.sub(Numbers.mul(Numbers.TWO, p.getY()), punt.getY()));
		//setXY( 2*p.getX()-punt.getX(), 2*p.getY()-punt.getY());
	}

	public void visitSegment(Segment s) {
		visitLijn(s);
	}

	public void update(Observable observable, Object arg) {
		if(arg == DESTROY)
		{
			destroy();
			return;
		}
		setDefined(mirror.isDefined() && punt.isDefined());
		if(isDefined())
			mirror.visit(this);
	}

	/**
	 * @return the mirror
	 */
	public Destroyable getMirror() {
		return mirror;
	}

	/**
	 * @param mirror the mirror to set
	 */
	public void setMirror(Destroyable mirror) {
		this.mirror = mirror;
		mirror.addObserver(this);
	}

	/**
	 * @return the punt
	 */
	public Punt getPunt() {
		return punt;
	}

	/**
	 * @param punt the punt to set
	 */
	public void setPunt(Punt punt) {
		this.punt = punt;
		punt.addObserver(this);
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#destroy()
	 */
	public void destroy() {
		punt.deleteObserver(this);
		mirror.deleteObserver(this);
		orig.deleteObserver(this);
		super.destroy();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean same(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SpiegelPunt other = (SpiegelPunt) obj;
		if (mirror == null) {
			if (other.mirror != null)
				return false;
		} else if (!mirror.equals(other.mirror))
			return false;
		if (punt == null) {
			if (other.punt != null)
				return false;
		} else if (!punt.equals(other.punt))
			return false;
		return true;
	}

	public void visitTriangle(Triangle t) {
	}
	public void visitKegelsnede(Kegelsnede2 k) {
	}
	public void visitLocus(Locus l) {
	}

}
