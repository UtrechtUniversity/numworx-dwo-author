package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Coordinaten extends Punt implements Observer, FreePoint {

	final public static String TYPE = "Pou";
	private Punt o, u;
	private Label cx, 
	cy;
	private boolean free;
	
	public Coordinaten() {
	}

	
	/* (non-Javadoc)
	 * @see fi.euclides.model.Punt#key()
	 */
	public String key() {
		return TYPE;
	}

	public Coordinaten(Label x, Label y, Punt o, Punt u) {
		this();
		setCx(x);
		setCy(y);
		setO(o);
		setU(u);
		recalc();
	}
	
	public Destroyable[] getDepend() {
		return new Destroyable[] { 
				cx, cy, o, u
		};
	}
	
	public void update(Observable observable, Object arg) {
		if(arg == DESTROY)
		{
			destroy();
		} else if(arg != VISIBLE)
		{
			recalc();
		}
	}

	private void recalc() {
		Numbers ux = Numbers.sub(u.getX(), o.getX());
		Numbers vx = Numbers.sub(u.getY(), o.getY());
		Numbers uy = Numbers.neg(vx);
		Numbers vy = ux;
// this.cx,this.cy COMPLEX	
		Numbers cx = Numbers.sub(Numbers.real(this.cx.value), Numbers.imag(this.cy.value));
		Numbers cy = Numbers.add(Numbers.real(this.cy.value), Numbers.imag(this.cx.value));
		Numbers x = Numbers.add(Numbers.mul(cx, ux), Numbers.mul(cy, vx));
		Numbers y = Numbers.add(Numbers.mul(cx, uy), Numbers.mul(cy, vy));
		
		setXY(Numbers.add(x, o.getX()),
			  Numbers.sub(o.getY(), y));
	}

	/**
	 * @return the o
	 */
	public Punt getO() {
		return o;
	}

	/**
	 * @param o the o to set
	 */
	public void setO(Punt o) {
		this.o = o;
		if(o!=null)o.addObserver(this);
	}

	/**
	 * @return the u
	 */
	public Punt getU() {
		return u;
	}

	/**
	 * @param u the u to set
	 */
	public void setU(Punt u) {
		this.u = u;
		if(u!=null)u.addObserver(this);
	}

	/**
	 * @return the cx
	 */
	public Numbers getCx() {
		return cx.value;
	}

	/**
	 * @param cx the cx to set
	 */
	public void setCx(Numbers cx) {
		this.cx.setValue(cx);
		this.cx.register(this.cx.registered.getTracker().getRegistered("1"));
		this.cx.setState(Label.CONSTANT);
		this.cx.setString(Numbers.toString(cx));
		this.cx.getRegistered().define(this.cx);
		setChanged();
	}

	/**
	 * @return the cy
	 */
	public Numbers getCy() {
		return cy.value;
	}

	/**
	 * @param cy the cy to set
	 */
	public void setCy(Numbers cy) {
		this.cy.setValue(cy);
		this.cy.register(this.cy.registered.getTracker().getRegistered("1"));
		this.cy.setState(Label.CONSTANT);
		this.cy.setString(Numbers.toString(cy));
		this.cy.getRegistered().define(this.cy);
		setChanged();
	}


	/* (non-Javadoc)
	 * @see fi.euclides.model.Punt#read(fi.euclides.model.Codec)
	 */
	public void read(Codec codec) throws IOException {
		super.read(codec);
		setCx((Label)codec.readDestroyable());
		setCy((Label)codec.readDestroyable());
		setO(codec.readPunt());
		setU(codec.readPunt());
	}


	public void setCx(Label cx) {
		if(this.cx != null) 
			this.cx.deleteObserver(this);
		this.cx = cx;
		cx.addObserver(this);
	}
	
	public void setCy(Label cy) {
		if(this.cy != null)
			this.cy.deleteObserver(this);
		this.cy = cy;
		cy.addObserver(this);
	}


	/* (non-Javadoc)
	 * @see fi.euclides.model.Punt#write(fi.euclides.model.Codec)
	 */
	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.writeDestroyable(cx);
		codec.writeDestroyable(cy);
		codec.writePunt(o);
		codec.writePunt(u);
	}

	public void setFree(boolean free) {
		this.free = free;
		if(!free) {
			recalc(); // jump to official position
		}
	}

	public boolean isFree() {
		return free;
	}


	@Override
	public <T> T adapt(Class<T> clz) {
		if (clz == FreePoint.class) return (T) this;
		return super.adapt(clz);
	}
	
	
}
