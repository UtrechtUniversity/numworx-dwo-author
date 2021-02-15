package nl.numworx.geodefiner.common;

import fi.euclides.event.Tracker;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class LocusModelFX extends LocusModelF {

	class XCoord extends LabelValue {
		
		XCoord() {
			super("xx");
		}

		@Override
		public Destroyable[] createDepend() {
			return new Destroyable[1];
		}

		@Override
		public void update(Observable observable, Object arg) {
			if(arg == Destroyable.DESTROY 
			   || arg == Destroyable.VISIBLE)
						return;
			Numbers xc = source.getX();
			Numbers ox = mapper.getO().getX();
			Numbers ux = mapper.getU().getX();
			Numbers value = Numbers.div(Numbers.sub(xc, ox),Numbers.sub(ux, ox));
			setStringValue((Label) observable, value);					
		}

		@Override
		public String getSymbolicValue(Label l) {
			return "x";
		}

	}

	AbstractViewer tracker;
	class HSegment extends Segment implements PointOnAlgorithm<Lijn>, Observer {

		{ 
			tracker.addObserver(this);
		}
		
		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
			super.update(o, arg);
		}

		@Override
		public boolean isDefined() {
			return true;
		}

		@Override
		public PointOnAlgorithm<Lijn> getAlgo() {
			return this;
		}

		@Override
		public void destroy() {
			tracker.deleteObserver(this);
			super.destroy();
		}

		@Override
		public Numbers getX1n() {
			return tracker.clipLeft();
		}

		@Override
		public Numbers getX2n() {
			return tracker.clipRight();
		}
		@Override
		public Numbers getY1n() {
			return Numbers.ONE;
		}

		@Override
		public Numbers getY2n() {
			return Numbers.ONE;
		}

		@Override
		public Numbers getDYn() {
			return Numbers.ZERO;
		}

		@Override
		public void recalc(Lijn on, FreePoint punt, double x, double y) {
			recalc(on, punt, Numbers.createDouble(x), Numbers.ONE);
		}

		@Override
		public void recalc(Lijn on, FreePoint punt, Numbers x, Numbers y) {
			punt.setXY(x, Numbers.ONE);
		}

		@Override
		public void update(Lijn on, FreePoint punt) {
			punt.setXY(punt.getX(), Numbers.ONE);
		}
		
	}
	
	public LocusModelFX(Label f, Tracker viewer) {
		super(f, viewer);
	}

	/**
	 * Optimalisatie O-U is horizontaal
	 */
	@Override
	protected void createX(Tracker tracker) {
		this.tracker = tracker.adapt(AbstractViewer.class);
		Punt O = mapper.getO();
		Punt U = mapper.getU();
		O.addObserver(this); //changes in O propagates to U
		U.addObserver(this);
		xas = new HSegment();
		xas.addObserver(this);
		source = xas.pointOn(Numbers.ZERO, Numbers.ONE);
		LabelDelegate coordX = new XCoord();
		Destroyable depend[] = coordX.createDepend();
		depend[0] = source;
		x = coordX.define(depend);
	}
	
	@Override
	protected void createY(Label f, Tracker tracker) {
		super.createY(f, tracker);
		output.add(mapper.getU());
		output.add(mapper.getO());
		output.add(xas);
	}

	@Override
	public void destroy() {
		Punt O = mapper.getO();
		Punt U = mapper.getU();
		if(O!=null) O.deleteObserver(this);
		if(U!=null) U.deleteObserver(this);
		xas.deleteObserver(this);
		x.destroy();
		source.destroy();
		if (dest != null) dest.destroy();
	}

	public void update(Observable observable, Object arg) {
		if(arg == Label.DESTROY) {
			for(Destroyable d: output) {
				d.deleteObserver(this);
			}
		}
		if(output.contains(observable))
		{	setChanged();
			notifyObservers(arg);
		}
		if(arg == Label.DESTROY) {
			output.clear();
			destroy();
		}
	}

}
