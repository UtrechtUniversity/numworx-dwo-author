package nl.numworx.geodefiner.common;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Volgpunt extends Punt implements FreePoint, Observer {

	@Override
	public void update(Observable observable, Object arg) {
		if(arg == DESTROY) {
			destroy();
			
		}
		if(observable == p[0]) {
			setXY(Numbers.add(p[0].getX(), dx), Numbers.add(p[0].getY(), dy));
		}
	}

	private Punt[] p = new Punt[1];
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
		return p[0].isDefined();
	}

	@Override
	public void moveTo(Numbers x, Numbers y) {
		if(isFree()) {
			dx = Numbers.sub(x, p[0].getX());
			dy = Numbers.sub(y, p[0].getY());
			super.moveTo(x, y);
		}
	}

	@Override
	public String key() {
		return "PP";
	}

	public void setDxy(Numbers dx2, Numbers dy2) {
		dx = dx2;
		dy = dy2;
		update(p[0], null);
	}

}
