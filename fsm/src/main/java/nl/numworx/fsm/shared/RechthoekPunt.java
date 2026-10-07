package nl.numworx.fsm.shared;

import java.io.IOException;

import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.persist.CreateUtil;
import fi.euclides.util.Observable;

public class RechthoekPunt extends Punt {
	
	private Punt[] depend = new Punt[2];

	public RechthoekPunt() {
	}

	public RechthoekPunt(Punt a, Punt b) {
		super(a.getX(), b.getY());
		depend[0] = a;
		depend[1] = b;
		observe();
	}
	
	public static final String TYPE = "PR"; 

	static class Creator implements CreateUtil.Creator {

		@Override
		public Destroyable create() {
			return new RechthoekPunt();
		}
	}

	public static void addCreator() {
		CreateUtil.buildmap.put(TYPE, new Creator());
	}

	private void observe() {
		depend[0].addObserver(this);
		depend[1].addObserver(this);
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
	public Numbers getY() {
		return depend[1].getY();
	}

	@Override
	public Numbers getX() {
		return depend[0].getX();
	}

	@Override
	public void read(Codec codec) throws IOException {
		super.read(codec);
		codec.read(depend);
		observe();
	}

	@Override
	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.write(depend);
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
			setXY(getX(), getY());
		}
	}

	@Override
	public Punt[] getDepend() {
		return depend;
	}


}
