package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class CirkelRadius extends Cirkel {

	public static String TYPE = "cr";
	
	static class RPunt extends Punt {
		private Punt center, u, o;
		private Label r;

		@Override
		public void addObserver(Observer observer) {
		}

		@Override
		public boolean isFree() {
			return center.isFree();
		}

		@Override
		public void deleteObserver(Observer observer) {
		}

		@Override
		public boolean isDefined() {
			return center.isDefined() && r.isDefined();
		}
		
		private Numbers getScale() {
			Numbers x = Numbers.sub(u.getX(),o.getX());
			Numbers  y = Numbers.sub(u.getY(),o.getY());
			Numbers result = Numbers.hypot(x,y);
			return result;
		}
		
		@Override
		public Numbers getY() {
			return center.getY();
		}
		@Override
		public Numbers getX() {
			return Numbers.add(center.getX(), Numbers.mul(r.value, getScale()));
		}

		RPunt(Punt center, Label r, Punt o, Punt u) {
			this.center = center;
			this.r = r;
			this.o = o;
			this.u = u;
		}
	}
	
	public CirkelRadius() {
	}

	Label radius;
	private Punt o,u;
	
	public CirkelRadius(Punt center, Label radius, Punt o, Punt u) {
		super(center, new RPunt(center, radius, o, u));
		this.radius = radius;
		this.o = o;
		this.u = u;
		radius.addObserver(this);
		o.addObserver(this);
		u.addObserver(this);
	}

	@Override
	public Destroyable[] getDepend() {
		return new Destroyable[] { getCenter(), radius, o, u };
	}

	@Override
	public String key() {
		return TYPE;
	}

	@Override
	public void read(Codec codec) throws IOException {
		Punt center = codec.readPunt();
		radius = (Label) codec.readDestroyable();
		o = codec.readPunt();
		u = codec.readPunt();
		setCenter(center);
		setRadius(new RPunt(center, radius, o, u));
		setRadius2(center);
		radius.addObserver(this);
		o.addObserver(this);
		u.addObserver(this);
		recalc();
	}

	@Override
	public void write(Codec codec) throws IOException {
		codec.write(getDepend());
	}

}
