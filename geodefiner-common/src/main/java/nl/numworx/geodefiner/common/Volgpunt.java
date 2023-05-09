package nl.numworx.geodefiner.common;

import java.io.IOException;

import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;
import fi.euclides.persist.CreateUtil;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Volgpunt extends Punt implements FreePoint, Observer {

	public static final String TYPE = "PP";

	static class Creator implements CreateUtil.Creator {

		@Override
		public Destroyable create() {
			return new Volgpunt();
		}
	}

	static void addCreator() {
		CreateUtil.buildmap.put(TYPE, new Creator());
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	protected void destroy(Observable observable) {
		if(observable != null) 
			observable.deleteObserver(this);
		destroy();	
	}
	
	@Override
	public void update(Observable observable, Object arg) {
		if(arg == DESTROY) {
			destroy(observable);
			return;
		}
		if(observable == p[0] && observable != null) {
			setXY(Numbers.add(p[0].getX(), dx), Numbers.add(p[0].getY(), dy));
		}
	}

	private final Punt[] p = new Punt[1];
	private boolean free = true;
	private Numbers dx = Numbers.ZERO;
	private Numbers dy = Numbers.ZERO;
	
	public boolean isFree() {
		return free;
	}

	public Volgpunt(Punt p) {
		super(p.getX(), p.getY());
		p.addObserver(this);
		this.p[0] = p;
	}

	Volgpunt() {
	}

	public Numbers getDx() {
		return dx;
	}

	public void setDx(Numbers dx) {
		this.dx = dx;
	}

	public Numbers getDy() {
		return dy;
	}

	public void setDy(Numbers dy) {
		this.dy = dy;
	}

	@Override
	public void setFree(boolean free) {
		this.free = free;
	}

	@Override
	public Destroyable[] getDepend() {
		return p;
	}

	@Override
	public boolean isDefined() {
		return p[0] != null && p[0].isDefined();
	}

	@Override
	public void moveTo(Numbers x, Numbers y) {
		if(isFree() && p[0] != null) {
			dx = Numbers.sub(x, p[0].getX());
			dy = Numbers.sub(y, p[0].getY());
			super.moveTo(x, y);
		}
	}

	@Override
	public String key() {
		return TYPE;
	}

	public void setDxy(Numbers dx2, Numbers dy2) {
		dx = dx2;
		dy = dy2;
		update(p[0], null);
	}

	@Override
	public void read(Codec codec) throws IOException {
		super.read(codec);
		setP(codec.readPunt());
		dx = codec.readNumber();
		dy = codec.readNumber();
	}

	private void setP(Punt readPunt) {
		p[0] = readPunt;
		if(readPunt != null)
			readPunt.addObserver(this);
	}
	public Punt getP() {
		return p[0];
	}

	@Override
	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.writeDestroyable(p[0]);
		codec.writeNumber(dx);
		codec.writeNumber(dy);
	}

}
