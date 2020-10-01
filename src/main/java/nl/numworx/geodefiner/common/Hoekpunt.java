package nl.numworx.geodefiner.common;

import java.io.IOException;

import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.persist.CreateUtil;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Hoekpunt extends Punt implements Observer {
	
	public static final String TYPE = "PH"; 

	static class Creator implements CreateUtil.Creator {

		@Override
		public Destroyable create() {
			return new Hoekpunt();
		}
	}

	static void addCreator() {
		CreateUtil.buildmap.put(TYPE, new Creator());
	}

	private Punt[] depend = new Punt[2];
	Numbers sin, cos;
	
	public Hoekpunt() {
	}

	public Hoekpunt(Punt p1, Punt p2, Numbers cos, Numbers sin) {
		depend[0] = p1;
		depend[1] = p2;
		this.sin = sin;
		this.cos = cos;
		observe();
		calculate();
		
	}

	public Hoekpunt(Punt p1, Punt p2, Numbers hoek) {
		depend[0] = p1;
		depend[1] = p2;
		double d = hoek.doubleValue();
		sin = Numbers.createDouble(Math.sin(d));
		cos = Numbers.createDouble(Math.cos(d));
		observe();
		calculate();
	}


	private void observe() {
		depend[0].addObserver(this);
		depend[1].addObserver(this);
	}


	private void calculate() {
		Numbers x0 = depend[0].getX();
		Numbers y0 = depend[0].getY();
		Numbers x1 = depend[1].getX();
		Numbers y1 = depend[1].getY();
		y1 = Numbers.sub(y1, y0);
		x1 = Numbers.sub(x1, x0);
		Numbers y2 = Numbers.sub(Numbers.mul(y1, cos), Numbers.mul(x1, sin));
		Numbers x2 = Numbers.add(Numbers.mul(x1, cos), Numbers.mul(y1, sin));
		y2 = Numbers.add(y2, y0);
		x2 = Numbers.add(x2, x0);
		setXY(x2,y2);
	}


	@Override
	public boolean isDefined() {
		return depend[0].isDefined() && depend[1].isDefined();
	}


	@Override
	public String key() {
		return TYPE;
	}


	@Override
	public void read(Codec codec) throws IOException {
		super.read(codec);
		codec.read(depend);
		cos = codec.readNumber();
		sin = codec.readNumber();
		observe();
	}

	@Override
	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.write(depend);
		codec.writeNumber(cos);
		codec.writeNumber(sin);
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
		if((observable == depend[0]||observable == depend[1]) && observable != null) {
			calculate();
		}
	}

}
