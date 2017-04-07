package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class PuntOp<T extends Destroyable> extends Punt implements Observer, FreePoint {

	protected T op;
	protected PointOnAlgorithm<T> pon;
	private boolean free = true;

	public String key()
	{
		return pon.key();
	}

	public void read(Codec codec) throws IOException
	{
		super.read(codec);
		T op = (T) codec.readDestroyable();
		if(op != null) {
			setOp(op);
			setOb((OpObject<T>) op);
		}
	}
	
	public void setOb(OpObject<T> op) {
			pon = op.getAlgo();
	}
		
	public void write(Codec codec) throws IOException
	{
		super.write(codec);
		codec.writeDestroyable(getOp());
	}
	/**
	 * @return the op
	 */
	public T getOp() {
		return op;
	}

	/**
	 * @param op the op to set
	 */
	public void setOp(T op) {
		this.op = op;
		op.addObserver(this);
	}

	/**
	 * @deprecated gebruik Numbers
	 * @param x
	 * @param y
	 * @param op
	 */
	public PuntOp(double x, double y, T op) {
		super(x, y);
		setOp(op);
	}

	/**
	 * @deprecated use specialized version
	 * @param x
	 * @param y
	 * @param op
	 */
	public PuntOp(Numbers x, Numbers y, T op) {
		super(x,y);
		setOp(op);
	}
				
	public PuntOp() {
	}

	public PuntOp(Numbers x, Numbers y, T o, PointOnAlgorithm<T> ob)
	{
		this(x, y, o);
		pon = ob;
		recalc(x, y);
	}

	public PuntOp(OpObject<T> dummy) {
		setOb(dummy);
	}

	/**
	 * Not deprecated gebruik recalc(Numbers, Numbers)
	 * @param x
	 * @param y
	 */
	protected void recalc(double x, double y)
	{
		pon.recalc(op, this, x, y);
	}
	
	public void update(Observable observable, Object arg) {
		if(arg == DESTROY)
			destroy();
		else if (observable == op && arg != VISIBLE){
			setDefined(op.isDefined());
			//recalc(getX(), getY());
			pon.update(op, this);
			notifyObservers();
		}
	}

	/* TODO move met Numbers
	 * @see euclides.Punt#move(double, double)
	 */
	public void moveTo(Numbers dx, Numbers dy) {
		recalc(dx, dy);
		notifyObservers();
	}

	 protected void recalc(Numbers x, Numbers y) {
			pon.recalc(op, this, x, y);
	}

	 public Destroyable[] getDepend() {
		return new Destroyable[] {op};
	 }

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#incident(fi.euclides.model.Destroyable)
	 */
	public boolean incident(Destroyable other) {
		return super.incident(other) || other == op;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
	
	@Override
	public boolean isFree() {
		return free;
	}

	@Override
	public <T> T adapt(Class<T> clz) {
		if (clz == FreePoint.class) return (T) this;
		return super.adapt(clz);
	}

	 
}
