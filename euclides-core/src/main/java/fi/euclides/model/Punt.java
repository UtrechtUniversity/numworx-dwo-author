package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

abstract public class Punt extends Destroyable {
	
	public static final String TYPE = "P";

	/* (non-Javadoc)
	 * @see euclides.Destroyable#visit(euclides.Visitor)
	 */
	public void visit(Visitor v) {
		v.visitPunt(this);
	}
	
	public boolean isFree() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#trail()
	 */
	public Destroyable trail() {
		return new VrijPunt(getX(), getY());
	}

	Numbers x, y;
	boolean defined = true;
	/**
	 * 
	 */
	public Punt() {
	}

	/**
	 * @param x
	 * @param y
	 */
	public Punt(double x, double y) {
		this.x = Numbers.createDouble(x);
		this.y = Numbers.createDouble(y);
	}
	
	public Punt(Numbers x, Numbers y)
	{
		this.x = x;
		this.y = y;
	}
	

	public boolean isDefined() {
		return defined;
	}

	public void setDefined(boolean defined) {
		if(this.defined!=defined)
			setChanged();
		this.defined = defined;
	}
	
	final public double getXd() {
		return getX().doubleValue();
	}

	/**
	 * @deprecated gebruik Numbers
	 * @param x
	 */
	public void setX(double x) {
		if(this.x == null || this.x.doubleValue() != x)
			setChanged();
		this.x = Numbers.createDouble(x);
	}
	
	final public double getYd() {
		return getY().doubleValue();
	}
	
	/**
	 * @deprecated gebruik Numbers
	 * @param y
	 */
	public void setY(double y) {
		if(this.y == null || y != this.y.doubleValue())
			setChanged();
		this.y = Numbers.createDouble(y);
	}

	public void setX(Numbers x) {
		if(this.x == null || !this.x.equals(x))
		{	setChanged();
		}
		this.x = x;
	}
	
	public void setY(Numbers y) {
		if(this.y == null || !this.y.equals(y))
		{	setChanged();
		}
		this.y = y;
	}
	
	/**
	 * @deprecated gebruik Numbers
	 */
	public void setXY(double x, double y)
	{
		setX(x);
		setY(y);
		notifyObservers();
	}

	public void setXY(Numbers x, Numbers y)
	{
		setX(x);
		setY(y);
		notifyObservers();
	}
	
	public void moveTo(Numbers x, Numbers y)
	{
		if(isFree())
			setXY(x, y);
	}
	
	public String key() {
		return TYPE;
	}

	public void read(Codec codec) throws IOException {
		setX(codec.readNumber());
		setY(codec.readNumber());
	}

	public void write(Codec codec) throws IOException {
		codec.writeNumber(getX());
		codec.writeNumber(getY());
	}

	public Numbers getY() {
		return y;
	}

	public Numbers getX() {
		return x;
	}

	public void update(Observable observable, Object arg) {
	}
	
	public Destroyable[] getImage(Destroyable mirror)
	{
		return new Destroyable[] { new SpiegelPunt( mirror, this) };
	}
	
	public Punt getImage(Destroyable mirror, Destroyable orig)
	{
		return new SpiegelPunt(mirror, this, orig);
	}
	
	public void forceChanged() {
		setChanged();
		notifyObservers();
	}
}
