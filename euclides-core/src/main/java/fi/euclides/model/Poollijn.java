package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Poollijn.CirkelStrategy;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class Poollijn extends Lijn {
	
	public static class Punt1 extends Punt2 {

		public Punt1() {
		}

		public Punt1(Lijn l) {
			super(l);
		}

		public Numbers getX() {
			return op.getX1n();
		}

		public Numbers getY() {

			return op.getY1n();
		}

		public String key() {
			return "P''";
		}
		
	}
	
	
	public static final String TYPE = "lPC";
	private Punt punt;
	
	abstract class Strategy extends PuntenLijn {
		abstract Destroyable getDestroyable();
		
	};
	class CirkelStrategy extends Strategy{
		CirkelStrategy(Cirkel c) {
			super();
			setP1(new VrijPunt(Numbers.ZERO, Numbers.ZERO));
			setP2(new VrijPunt(Numbers.ZERO, Numbers.ZERO));
			this.c = c;
			update(c, null);
			getP1().addObserver(this);
			getP2().addObserver(this);
			addObserver(Poollijn.this);
		}

		Cirkel c;

		public void update(Observable o, Object arg) {
			if(o == c || o == punt)
			{
				// vergelijking van de cirkel
				// ( x - cx ) ^ 2 + ( y - cy ) ^ 2 = r^2;
				// vergelijking van de lijn:
				// ( x - cx ) * ( px - cx ) + ( y - cy ) * (py - cy ) = r^2
				Punt center = c.getCenter();
				Numbers r2  = c.getR2n();
				Numbers cx = Numbers.sub(punt.getX(), center.getX());
				Numbers cy = Numbers.sub(punt.getY(), center.getY());
				
				Numbers y,x;
				y = Numbers.add(r2, Numbers.mul(cx, center.x));
				y = Numbers.div(y, cy);
				y = Numbers.add(y, center.y);
				
				x = Numbers.add(r2, Numbers.mul(cy, center.y));
				x = Numbers.div(x, cx);
				x = Numbers.add(x, center.x);
				
				getP1().setXY(Numbers.ZERO, y);
				getP2().setXY(x, Numbers.ZERO);
				if(Math.abs(cx.doubleValue()) < 0.00001)
					getP2().setXY(1.0, y.doubleValue());
				if(Math.abs(cy.doubleValue()) < 0.00001)
					getP1().setXY(x.doubleValue(),1.0);
				notifyObservers();
			} else if(o == getP1() || o == getP2())
			{
				setChanged();
			}
		}

		Destroyable getDestroyable() {
			return c;
		}
	}

	class ConicStrategy extends Strategy {
		public ConicStrategy(Kegelsnede2 k) {
			super();
			this.k = k;
			setP1(new VrijPunt(Numbers.ZERO, Numbers.ZERO));
			setP2(new VrijPunt(Numbers.ZERO, Numbers.ZERO));
			update(k, null);
			getP1().addObserver(this);
			getP2().addObserver(this);
			addObserver(Poollijn.this);
		}

		Kegelsnede2 k;
		public void update(Observable o, Object arg) {
			if(o == k || o == punt)
			{
				// vergelijking van de kegelsnede
				// (x,y,1) A (x,y,1) = 0;
				// vergelijking van de lijn:
				// (px,py,1) A (x,y,1) = 0
				//Punt punt = new Punt(Numbers.ZERO, Numbers.ZERO);
			
				Numbers[] pl = k.getPolarLine(punt);
				
				Numbers y,x;
				y = Numbers.div(pl[2], pl[1]);  y = Numbers.neg(y);
				x = Numbers.div(pl[2], pl[0]);  x = Numbers.neg(x);
				getP1().setXY(Numbers.ZERO, y);
				getP2().setXY(x, Numbers.ZERO);
				if(Math.abs(pl[0].doubleValue()) < 0.00001)
					getP2().setXY(1.0, y.doubleValue());
				if(Math.abs(pl[1].doubleValue()) < 0.00001)
					getP1().setXY(x.doubleValue(),1.0);
				notifyObservers();
			} else if(o == getP1() || o == getP2())
			{
				setChanged();
			}
		}
		Destroyable getDestroyable() {
			return k;
		}
		
	}
	
	private Strategy strategy;
	
	public Poollijn() {
	}
	
	public Poollijn(Punt p, Destroyable c) {
		setPunt(p);
		setDestroyable(c);
	}

	private void setDestroyable(Destroyable lijn) {
		if(lijn instanceof Cirkel)
			strategy = new CirkelStrategy((Cirkel) lijn);
		else if(lijn instanceof Kegelsnede2)
			strategy = new ConicStrategy((Kegelsnede2) lijn);
		lijn.addObserver(this);
	}

	public String key() {
		return TYPE;
	}
	
	public void setPunt(Punt punt) {
		this.punt = punt;
		punt.addObserver(this);
	}

	public void update(Observable o, Object arg) {
		if(arg == DESTROY)
		{
			destroy();
			return;
		}
		if(o == strategy)
			setChanged();
		else if(o == punt || o == strategy.getDestroyable())
		{
			strategy.update(o, null);
			notifyObservers();
		}
	}

	public double getX1() {
		return strategy.getX1();
	}

	public double getX2() {
		return strategy.getX2();
	}

	public double getY1() {
		return strategy.getY1();
	}

	public double getY2() {
		return strategy.getY2();
	}

	public Numbers getX1n() {
		return strategy.getX1n();
	}

	public Numbers getX2n() {
		return strategy.getX2n();
	}

	public double getDX() {
		return strategy.getDX();
	}

	public Numbers getY1n() {
		return strategy.getY1n();
	}

	public Numbers getDXn() {
		return strategy.getDXn();
	}

	public Numbers getY2n() {
		return strategy.getY2n();
	}

	public double getDY() {
		return strategy.getDY();
	}

	public Numbers getDYn() {
		return strategy.getDYn();
	}

//	public void recalc(Punt punt, double x, double y) {
//		strategy.getAlgo().recalc(strategy, punt, x, y);
//	}

	public boolean incident(Destroyable other) {
		if(other == punt)
		{
			return punt.incident(strategy.getDestroyable()) || strategy.getDestroyable().incident(punt);
		}
		return super.incident(other) || strategy.incident(other);
	}

	public boolean isDefined() {
		return punt.isDefined() && strategy.isDefined() && strategy.getDestroyable().isDefined();
	}

	public void write(Codec codec) throws IOException {
		codec.write(getDepend());
	}

	public Destroyable[] getDepend() {
		return new Destroyable[] { punt, strategy.getDestroyable() };
	}
	
	public void read(Codec codec) throws IOException {
		setPunt(codec.readPunt());
		setDestroyable(codec.readDestroyable());
	}

	public Destroyable[] getImage(Destroyable mirror) {
		if(incident(punt))
			return super.getImage(mirror, punt, new Punt2(this));
		return super.getImage(mirror, new Punt1(this), new Punt2(this)); // FIXME 2 punten toevoegen
	}

	
}
