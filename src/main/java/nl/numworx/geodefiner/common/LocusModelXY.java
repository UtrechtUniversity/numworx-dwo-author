package nl.numworx.geodefiner.common;

import java.io.IOException;
import java.util.List;

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
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.openmath.Expression;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class LocusModelXY extends Observable implements LocusModel, Observer, NameMapper {

	private NameMapper mapper;
	private PuntenLijn xas;
	private PuntOp<Lijn> source;
	private Label y1, y2;
	private Label x;
	private Label fx,fy;
	private List<Destroyable> output1, output2;
	private String FX, FY;
	private Coordinaten dest;

	public LocusModelXY(Label fx, Label fy, Label interval, Tracker tracker) {
		mapper = tracker.getMapper();
		Punt O = mapper.getO();
		Punt U = mapper.getU();
		O.addObserver(this);
		U.addObserver(this);
		xas = new PuntenLijn(U,O);
		source = xas.pointOn(O.getX(), O.getY());
		y1 = new Label();
		y2 = new Label();
		LabelDelegate coordX = tracker.getRegistered(Coord.xKey);
		Destroyable depend[] = coordX.createDepend();
		depend[0] = source;
		x = coordX.define(depend);
		this.fx = fx;
		this.output1 = LocusModelF.varsOf(fx);
		for(Destroyable i: output1) i.addObserver(this);
		this.fy = fy;
		this.output2 = LocusModelF.varsOf(fy);
		for(Destroyable i: output2) i.addObserver(this);
		
		FX = mapper.toString(fx);
		FY = mapper.toString(fy);
		OMVariable fvar, xvar;
		xvar = new OMVariable("x");
		fvar = new OMVariable(FX);
		OMApplication oma = new OMApplication();
		oma.addElement(fvar);oma.addElement(xvar);	
		Expression expression = new Expression(tracker);
		y1 = (Label) expression.interpret(oma, y1, this);
		oma = new OMApplication();
		fvar = new OMVariable(FY);
		oma.addElement(fvar); oma.addElement(xvar);
		y2 = (Label) expression.interpret(oma, y2, this);
		dest = new Coordinaten(y1, y2, O, U);
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
//TODO
	}

	@Override
	public void writeModel(Codec codec) throws IOException {
		codec.write(getDepend());
	}

	@Override
	public Destroyable[] getDepend() {
		return new Label[] { fx, fy };
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(observable == xas.getP1() || observable == xas.getP2())
		{
			setChanged();
		}
		if(output1.contains(observable)||output2.contains(observable))
			setChanged();

		notifyObservers(arg);
	}

	@Override
	public Destroyable fromString(String name) {
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
		// TODO Auto-generated method stub		
	}

}
