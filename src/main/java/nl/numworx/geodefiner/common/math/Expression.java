package nl.numworx.geodefiner.common.math;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import nl.numworx.geodefiner.common.CheckObjectList;
import nl.numworx.geodefiner.common.Definitions;
import nl.numworx.geodefiner.common.GroupOf;
import nl.numworx.geodefiner.common.Polygon;
import nl.numworx.geodefiner.common.State;
import nl.numworx.geodefiner.common.Volgpunt;
import nl.numworx.geodefiner.common.index.ListSelector;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMInteger;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.expr.DestroyDependency;
import fi.euclides.expr.InterpretException;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.formuleobjects.TokenMgrError;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.model.OpObject;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp2;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.OMConstants;
import fi.euclides.proof.HoekHandler;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.DComparator;

public class Expression extends fi.euclides.openmath.Expression {

	public final Checked CHECKED = new Checked();
	public CheckObjectList object;
	@Override
	public Destroyable interpret(OMObject o, Label l, NameMapper mapper) {
		
		if(GEODEFINER_CHECKED.isSame(o)) {
			CHECKED.define(l);
			return l;
		}
		if(GEODEFINER_OBJECT.isSame(o)) {
			if(object != null) 
				return object;
		}
		return super.interpret(o, l, mapper);
	}



	static class FunctionalMapper implements NameMapper {
		final NameMapper mapper;
		final Map<String, Destroyable> function;
		
		public FunctionalMapper(NameMapper mapper, Vector<OMVariable> variables,
				Destroyable[] arguments) {
			this.mapper = mapper;
			function = new TreeMap<String, Destroyable>();
			int size = Math.min(arguments.length, variables.size());
			for(int i = 0; i < size; i++) {
				function.put(variables.get(i).getName(), arguments[i]);
			}
		}

		@Override
		public Destroyable fromString(String name) {
			if(function.containsKey(name))
				return function.get(name);
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
			for(Map.Entry<String, Destroyable> entry: function.entrySet()) {
				if(entry.getValue() == destroyable) return entry.getKey();
			}
			return mapper.toString(destroyable);
		}

		@Override
		public void rename(Destroyable p, String name) {
		}

	}

	public Expression() {};

	@Override
	public void setAllTracker(Tracker tracker) {
		super.setAllTracker(tracker);
		LabelDelegate value = new HoekHandler();
		symbolmap.put("geodefiner.angle", value);
		value = new Phi();
		symbolmap.put("geodefiner.phi", value);
		install(new Rnd());
		install(new Rnq());
		value = new Equals();
		symbolmap.put("geodefiner.equals", value);
		symbolmap.put("relation1.approx", value);
		value = new Eq();
		symbolmap.put("relation1.eq", value);
		Abs abs = new Abs();
		put(OMConstants.ARITH1_ABS, abs);
		Faculteit fac = new Faculteit();
		symbolmap.put("integer1.factorial", fac);
		symbolmap.put("arith1.sum", new Sigma());
		Prv prv = new Prv();
		symbolmap.put("wiskopdr.prv", prv);
		MinMax m = MinMax.MAX();symbolmap.put("minmax1.max", m);
		 m = MinMax.MIN();symbolmap.put("minmax1.min", m);
		toc = new ToComplex();
		toc.setTracker(tracker);
		symbolmap.put("combinat1.binomial", new Binomial());
		install(new BinomCDF());
		install(new BinomPDF());
		install(new InvNorm());
		install(new NormalCDF());
		install(new PoissonCDF());
		install(new PoissonPDF());
		install(new AantalSign());
		install(CHECKED);
		install(new Exists());
		install(new Dragging(tracker));
		GCD gcd = new GCD();
		symbolmap.put("arith1.gcd", gcd);

		for(LabelDelegate delegate: symbolmap.values())
			delegate.setTracker(tracker);
	}

	private void install(LabelDelegate value) {
		symbolmap.put("geodefiner."+value.getSubKey(), value);		
	}

	LabelValue toc;
	public void copy(OMApplication oma, NameMapper mapper, Destroyable[] depend) {
		int size = oma.getLength()-1;
		for (int i = 0; i < size; i++) {
			OMObject o = oma.getElementAt(i+1);
			Destroyable result = null;
			try {
				depend[i] = result = interpret(o, new Label(), mapper);
			} catch (ArrayStoreException e) {
				if(depend instanceof Label[]) {
					if(result instanceof Punt || result instanceof Segment) {
						Label l = toc.define(new Destroyable[] { result });
						depend[i] = l;
						continue;
					}
				}
				throw new InterpretException("Wrong type", e);
			}
		}
	}

	
	Punt createPunt(Destroyable[] depend, NameMapper mapper) {
		Destroyable arg0 = depend[0];
		Destroyable arg1;
		Punt p = null;
		if(depend[1] == null) {
			LabelDelegate d = CONST;
			Label l = d.define(Label.EMPTY);
			l.setValue(Numbers.ZERO);l.setString("0");
			arg1 = l;
		} else 
			arg1 = depend[1];
		if(arg0 instanceof Label && arg1 instanceof Label) {
//$P := point(1,2)
			Label ix = (Label) arg0; // toNumber(object)
			Label iy = (Label) arg1;
			p = new Coordinaten(ix, iy, mapper.getO(), mapper.getU());
			if(depend[2] instanceof OpObject) {
//$P := point(1, 2, $lijn)
				Destroyable on = depend[2];
				OpObject op = (OpObject) on;
				p.destroy();
				DestroyDependency dd = new DestroyDependency(p);
				ix.addObserver(dd);
				iy.addObserver(dd);
				p = op.pointOn(p.getX(), p.getY());
			} else {

			}
		} else {
//			model.clearSelection();
//			model.toggle(arg0);
//			model.toggle(arg1);
//			p = model.buildPunt(Numbers.ZERO, Numbers.ZERO);
//			model.clearSelection();
//			if ( p instanceof PuntOp2 && depend.length == 3)
//			{	PuntOp2 p2 = (PuntOp2)p;
//				Destroyable arg2 = depend[2];
//				if(arg2 instanceof Label) {
//					byte b = (byte) ((Label) arg2).value.doubleValue();
//					p2.setFuse(b);
//					p2.update(arg0, arg1);
//				}
//			}
//			if (p instanceof VrijPunt) { 
//				p.destroy();
//				p = null;
//			}
		}
		if(p == null) {
			throw new InterpretException("wrong point");
		}
		return p;		
	}
	
	
	
	@Override
	public Destroyable copyOMA(Label l, OMObject o, NameMapper mapper) {
		OMApplication oma = (OMApplication)o;
		OMObject func = oma.firstElement();
		Model model = toc.getTracker().getModel();
		if(Definitions.POINT.isSame(func)) {
			Destroyable depend[] = new Destroyable[3];
			copy(oma, mapper, depend);
			return createPunt(depend, mapper);
		}
		if(Definitions.LINE.isSame(func)) {
			Punt depend[] = new Punt[2];
			copy(oma, mapper, depend);
			return new PuntenLijn(depend[0], depend[1]);
		}
		if(Definitions.SEGMENT.isSame(func)) {
			Punt depend[] = new Punt[2];
			copy(oma, mapper, depend);
			return new Segment(depend[0], depend[1]);
		}
		if(Definitions.HALFLINE.isSame(func)) {
			Punt depend[] = new Punt[2];
			copy(oma, mapper, depend);
			return new Ray(depend[0], depend[1]);
		}
		if (Definitions.CIRCLE.isSame(func)) {
			Destroyable depend[] = new Destroyable[oma.getLength()-1];
			copy(oma, mapper, depend);
			return model.createCirkel(depend);
		}
		if (Definitions.ARC.isSame(func)) {
			Destroyable depend[] = new Destroyable[oma.getLength()-1];
			copy(oma, mapper, depend);
			return model.createBoog(depend);
		}
		if (Definitions.POLYGON.isSame(func)) {
			Punt depend[] = new Punt[oma.getLength()-1];
			copy(oma, mapper, depend);
			if(depend.length == 3) {
				DComparator.sort(depend);			
				return new Triangle(depend);
			}
			return new Polygon(depend);
		}
		if (OMConstants.LIST1_MAP.isSame(func)) {
			Destroyable depend[] = new Destroyable[oma.getLength()-1];
			copy(oma, mapper, depend);
			return new GroupOf(depend, this, mapper);
		}
		if (Definitions.TEXT.isSame(func)) {
			Destroyable depend[] = new Destroyable[2];
			copy(oma, mapper, depend);
			if(depend[1] instanceof Punt && depend[0] instanceof Label) {
				Label t = (Label) depend[0];
				t.setP(/*new Volgpunt*/((Punt) depend[1])); // XXX Geen Volgpunt, want je kan niet met de hand dx/dy zetten, dus ook niet via ModelUI.install
				depend[1].addObserver(new DestroyDependency(t));
				if( "".equals(t.getSubKey())) {
					String plain = t.getString();
					if(plain.contains("{") && plain.contains("}"))
					{
						plain = plain.replace("{", "\",").replace("}",",\"");
						FormuleParser parser = new FormuleParser("[\""+plain+"\"]");
						try {
							OMObject obj = parser.bracket();
							depend[0] = interpret(obj, t, mapper);
						} catch (ParseException e) {
							// log.fine(e.toString())
							;
						} catch (TokenMgrError tme) {							
						}
					}
				}
				return depend[0];

			}
		}
		
		if (OMConstants.LIST2_LIST_SELECTOR.isSame(func)) {
			Destroyable depend[] = new Destroyable[2];
			copy(oma, mapper, depend);
			if (depend[0] instanceof Groep && oma.getElementAt(2) instanceof OMInteger) {
				OMInteger i = (OMInteger) oma.getElementAt(2);
				int index = i.intValue();
				return new ListSelector((Groep)depend[0], index).get();
				
			} else
			if(depend[0] instanceof Groep && depend[1] instanceof Label) {
				return new ListSelector((Groep)depend[0], (Label)depend[1]).get();
			}
			// map(f,1..n)_i === f(i)
			// [a1,a2,a3,a4]_i === a_i
			// f_i == f(i)
			
		}
		if ( func instanceof OMVariable) {
			OMVariable var = (OMVariable) func;
			Label f = (Label) mapper.fromString(var.getName());
			if(f == null) throw new InterpretException("Unknown: " + var.getName());
			OMObject om = f.getAdapter().adapt(OMObject.class);
			if(om instanceof OMBinding) {
				OMBinding binding = (OMBinding) om;
				o = binding.getBody();
				Destroyable depend[] = new Destroyable[oma.getLength()-1];
				copy(oma, mapper, depend);
				mapper = new FunctionalMapper(mapper, binding.getVariables(), depend);				
			} else {
				oma = (OMApplication) oma.copy();
				oma.setElementAt(om, 0);
				o = oma;
			}	
			final Destroyable interpret = interpret(o, l, mapper);
			f.addObserver(new DestroyDependency(interpret));
			return interpret;
			
		}
		
		
		return super.copyOMA(l, o, mapper);
	}

	public void setObject(CheckObjectList checkObjectList) {
		this.object = checkObjectList;
	}

}
