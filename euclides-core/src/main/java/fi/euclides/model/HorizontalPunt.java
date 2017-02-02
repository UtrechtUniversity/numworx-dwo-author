package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class HorizontalPunt extends Punt implements FreePoint {

	@Override
	public String key() {
			return "PH";
	}
	
	private Punt op;
	private boolean free = true;
	private Numbers distance;

	public HorizontalPunt(Numbers x, Numbers y, Punt o) {
		super(x, o.getY());
		distance = Numbers.sub(x, o.getX());
		setOp(o);
	}

	@Override
	public void read(Codec codec) throws IOException {
		Numbers dx = (codec.readNumber());
		setOp(codec.readPunt());
		distance = Numbers.sub(x, op.getX());
		setY(op.getY());
	}

	private void setOp(Punt readPunt) {
		op = readPunt;
		op.addObserver(this);
	}

	@Override
	public void write(Codec codec) throws IOException {
		codec.writeNumber(getX());
		codec.writePunt(op);
	}

	public void setFree(boolean free) {
		this.free = free;
	}
	
	public boolean isFree() {
		return free;
	}

	public void setDistance(Numbers distance) {
		Numbers old = this.distance;
		this.distance = distance;
		if(old == null || !old.equals(distance))
			setChanged();
		notifyObservers();
	}
	
	@Override
	public void setXY(Numbers x, Numbers y) { // alleen via move! of restore?
		setDistance(Numbers.sub(x, op.getX()));
	}
	
	@Override
	public <T> T adapt(Class<T> clz) {
		if (clz == FreePoint.class) return (T) this;
		return super.adapt(clz);
	}

	@Override
	public boolean isDefined() {
		return op.isDefined();
	}

	@Override
	public Numbers getY() {
		return op.getY();
	}

	@Override
	public Numbers getX() {
		return Numbers.add(distance, op.getX());
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(arg == DESTROY)
			destroy();
		if(arg == null) {
			setChanged(); notifyObservers();
		}
	}

	@Override
	public void moveTo(Numbers x, Numbers y) {
		// TODO Auto-generated method stub
		super.moveTo(x, y);
	}

	public Numbers getDistance() {
		return distance;
	}

}
