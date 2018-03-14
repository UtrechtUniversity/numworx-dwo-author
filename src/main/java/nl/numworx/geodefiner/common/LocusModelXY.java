package nl.numworx.geodefiner.common;

import java.io.IOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.expr.Coord;
import fi.euclides.model.Codec;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.Locus.LocusModel;
import fi.euclides.model.math.Complex;
import fi.euclides.model.math.Numbers;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Segment;
import fi.euclides.openmath.Expression;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.proof.Const;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class LocusModelXY extends Observable implements LocusModel, Observer, NameMapper {

	private static final Label ZERO = new Label(); 
	static { 
		ZERO.value = Numbers.ZERO;
		ZERO.setState(Label.EXACT);
		ZERO.registered = new Const(); // geen tracker
		ZERO.setString("0");
	}
	private NameMapper mapper;
	private PuntenLijn xas;
	private PuntOp<Lijn> source;
	private Label y1, y2;
	private Label x;
	private Label fx,fy;
	private Set<Destroyable> output = Collections.newSetFromMap(new IdentityHashMap<Destroyable, Boolean>());
	private Coordinaten dest;
	private Label interval;

	public LocusModelXY(Label fx, Label fy, Label interval, Tracker tracker) {
		output.add(fx);
		output.add(fy);
		mapper = tracker.getMapper();
		Punt O = mapper.getO();
		Punt U = mapper.getU();
		if(interval != null) {
			Destroyable[] depend = interval.getDepend();
			output.add(interval);
			this.interval = interval;
			Label min = (Label) depend[0];
			Label max = (Label) depend[1];
			Coordinaten Pmax = new Coordinaten(max, ZERO, O, U);
			Coordinaten Pmin = new Coordinaten(min, ZERO, O, U);
			xas = new Segment(Pmin, Pmax);
			output.add(Pmax);
			output.add(Pmin);
		} else {
			output.add(O);
			output.add(U);
			xas = new PuntenLijn(U,O);
		}
		source = xas.pointOn(O.getX(), O.getY());
		y1 = new Label();
		y2 = new Label();
		LabelDelegate coordX = tracker.getRegistered(Coord.xKey);
		Destroyable depend[] = coordX.createDepend();
		depend[0] = source;
		x = coordX.define(depend);
		this.fx = fx;
		this.output .addAll( LocusModelF.varsOf(fx) );
		this.fy = fy;
		this.output. addAll( LocusModelF.varsOf(fy));
		for(Destroyable i: output) i.addObserver(this);
		OMVariable fvar, xvar;
		xvar = new OMVariable("x");
		fvar = new OMVariable("%fx");
		OMApplication oma = new OMApplication();
		oma.addElement(fvar);oma.addElement(xvar);	
		Expression expression = tracker.adapt(Expression.class);
		y1 = (Label) expression.interpret(oma, y1, this);
		oma = new OMApplication();
		fvar = new OMVariable("%fy");
		oma.addElement(fvar); oma.addElement(xvar);
		y2 = (Label) expression.interpret(oma, y2, this);
		dest = new Coordinaten(y1, y2, O, U){
			@Override
			public Numbers getCy() {
				if(super.getCy() instanceof Complex) return Numbers.NaN;
				return super.getCy();
			}

			@Override
			public Numbers getCx() {
				if (super.getCx() instanceof Complex) return Numbers.NaN;
				return super.getCx();
			}

			@Override
			public boolean isDefined() {
				return super.isDefined() && !getCy().isNaN() &&!getCx().isNaN();
			} };
	}

	@Override
	public Punt getDest() {
		return dest;
	}

	@Override
	public PuntOp<?> getSource() {
		return source;
	}

	@Override
	public void destroy() {
		for(Destroyable i: output) {
			i.deleteObserver(this);
		}
		xas.destroy();
	}

	@Override
	public void writeModel(Codec codec) throws IOException {
		codec.writeNumber(Numbers.TWO);
		codec.write(getDepend());
	}

	@Override
	public Destroyable[] getDepend() {
		return new Label[] { fx, fy , interval};
	}

	@Override
	public void update(Observable observable, Object arg) {
		if (output.contains(observable))
			setChanged();

		notifyObservers(arg);
	}

	@Override
	public Destroyable fromString(String name) {
		if( "%fx".equals(name)) return fx;
		if( "%fy".equals(name)) return fy;
		if ("x".equals(name))
			return x;
		return mapper.fromString(name);
	}

	@Override
	public Punt getO() {
		return mapper.getO();
	}

	@Override
	public Punt getU() {
		return mapper.getU();
	}

	@Override
	public String toString(Destroyable destroyable) {
		if(destroyable == x) return "x";
		return mapper.toString(destroyable);
	}

	@Override
	public void rename(Destroyable p, String name) {
	}

}
