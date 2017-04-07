package fi.euclides.openmath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.model.Codec;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus.LocusModel;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Complex;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.expr.Coord;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class LocusModelF extends Observable implements LocusModel, Observer, NameMapper {

	protected Coordinaten dest;
	protected PuntOp<Lijn> source;
	protected NameMapper mapper;
	protected Label x,y,f;
	protected PuntenLijn xas;
	private String F;
	protected List<Destroyable> output;
	
//	public LocusModelF() {
//		Punt O = new VrijPunt(Numbers.ZERO, Numbers.ZERO);
//		Punt U = new VrijPunt(Numbers.ONE, Numbers.ZERO);
//		xas = new PuntenLijn(U,O);
//		source = xas.pointOn(Numbers.ZERO, Numbers.ZERO);
//		source.addObserver(this);
//		dest = new Coordinaten();
//		x = new Label();
//		x.setValue(Numbers.ZERO);
//		y = new Label();
//	}
	
	
	public static List<Destroyable> varsOf(Label f) {
		OMObject obj = f.adapt(OMObject.class);
		if(obj != null) {
			NameMapper mapper = f.getRegistered().getTracker().getMapper();
			return varsOf(obj, mapper, Collections.EMPTY_SET, new ArrayList<Destroyable> ());
		}		
		return Collections.EMPTY_LIST;
	}
	
	protected static List<Destroyable> varsOf(Object obj, NameMapper mapper, Set<String> bindvars, ArrayList<Destroyable> output) {
		if(obj instanceof OMVariable) {
			String name = ((OMVariable) obj).getName();
			if(bindvars.contains(name))return output;
			Destroyable var = mapper.fromString(name);
			if(var != null) {
				output.add(var);
			}
			return output;
		}
		if(obj instanceof OMApplication) {
			OMApplication oma = (OMApplication) obj;
			for(Object item : oma.getElements())
			{
				varsOf(item, mapper, bindvars, output);
			}
			return output;
		}
		if(obj instanceof OMBinding) {
			OMBinding binding = (OMBinding) obj;
			Vector<OMVariable> vars = binding.getVariables();
			bindvars = new HashSet<String>(bindvars);
			for (OMVariable omVariable : vars) {
				bindvars.add(omVariable.getName());
			}
			return varsOf(binding.getBody(), mapper, bindvars, output);		
		}
// Float/Integer/attributtion		
		return output;
	}


	public LocusModelF(Label f, Tracker tracker) {
		mapper = tracker.getMapper();
		createX(tracker);
		createY(f, tracker);
		createDest();
	}


	protected void createDest() {
		dest = new Coordinaten(x, y, mapper.getO(), mapper.getU()) {

			@Override
			public Numbers getCy() {
				if(super.getCy() instanceof Complex) return Numbers.NaN;
				return super.getCy();
			}

			@Override
			public boolean isDefined() {
				return super.isDefined() && !getCy().isNaN();
			} };
	}


	protected void createY(Label f, Tracker tracker) {
		this.f = f;
		this.output = varsOf(f);
		F = mapper.toString(f);
		OMVariable fvar, xvar;
		xvar = new OMVariable("x");
		fvar = new OMVariable(F);
		OMApplication oma = new OMApplication();
		oma.addElement(fvar);oma.addElement(xvar);	
		Expression expression = tracker.adapt(Expression.class);
		if(expression == null) 
			expression = new Expression(tracker);
		y = new Label();
		y = (Label) expression.interpret(oma, y, this);

		for(Destroyable i: output) i.addObserver(this);
	}


	protected void createX(Tracker tracker) {
		Punt O = mapper.getO();
		Punt U = mapper.getU();
		O.addObserver(this);
		U.addObserver(this);
		xas = new PuntenLijn(U,O);
		source = xas.pointOn(O.getX(), O.getY());
		y = new Label();
		LabelDelegate coordX = tracker.getRegistered(Coord.xKey);
		Destroyable depend[] = coordX.createDepend();
		depend[0] = source;
		x = coordX.define(depend);
	}

	public Punt getDest() {
		return dest;
	}

	public PuntOp<Lijn> getSource() {
		return source;
	}


	public void destroy() {
		xas.getP1().deleteObserver(this);
		xas.getP2().deleteObserver(this);
		xas.destroy(); // will destroy source, x, y and dest
	}

	public void update(Observable observable, Object arg) {
		if(observable == xas.getP1() || observable == xas.getP2())
		{
			setChanged();
		}
		if(output.contains(observable))
			setChanged();

		notifyObservers(arg);
	}

	public Destroyable fromString(String name) {
		if ("x".equals(name))
			return x;
		if(F.equals(name))
			return f;
		return mapper.fromString(name);
	}

	public Punt getO() {
		return mapper.getO();
	}

	public Punt getU() {
		return mapper.getU();
	}

	public String toString(Destroyable destroyable) {
		if(destroyable == x) return "x";
		return mapper.toString(destroyable);
	}

	public Destroyable[] getDepend() {
		return new Label[] { f };
	}

	public void writeModel(Codec codec) throws IOException {
		codec.writeDestroyable(f);
	}

	public void rename(Destroyable p, String name) {
		
	}
}
