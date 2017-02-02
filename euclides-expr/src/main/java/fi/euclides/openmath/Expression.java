package fi.euclides.openmath;

import java.util.HashMap;

import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.expr.Complex;
import fi.euclides.expr.Coord;
import fi.euclides.expr.DestroyDependency;
import fi.euclides.expr.Div;
import fi.euclides.expr.If;
import fi.euclides.expr.InterpretException;
import fi.euclides.expr.List;
import fi.euclides.expr.Logic1;
import fi.euclides.expr.Minus;
import fi.euclides.expr.Mul;
import fi.euclides.expr.Not;
import fi.euclides.expr.Power;
import fi.euclides.expr.Ratio;
import fi.euclides.expr.Relation1;
import fi.euclides.expr.Som;
import fi.euclides.expr.SomN;
import fi.euclides.expr.Transc1;
import fi.euclides.expr.UMinus;
import fi.euclides.expr.Visible;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.AfstandHandler;
import fi.euclides.proof.Const;
import fi.euclides.proof.FlipFlop;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.proof.MidpointTester;
import fi.euclides.proof.OppHandler;
import fi.euclides.util.DComparator;
import fi.euclides.util.DefaultAdapter;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMFloat;
import nl.tue.win.riaca.openmath.lang.OMInteger;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMString;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.tue.win.riaca.openmath.lang.OMVariable;

public class Expression implements OMConstants {

	public final  Som    SOM = new SomN();
	public final  Const  CONST = new Const();
	public final  FlipFlop FLIP_FLOP = new FlipFlop();
	public final  Minus  MINUS = new Minus();
	public final  Div    DIV   = new Div();
	public final  Mul    MUL   = new Mul();
	public final  UMinus UMINUS = new UMinus();
	public final  OppHandler AREA = new OppHandler("", true);

	public final  Coord X = new Coord(Coord.xKey);
	public final  Coord Y = new Coord(Coord.yKey);
	
	public final LabelDelegate LAMBDA = new Lambda();
	public final Relation1 LEQ = new Relation1("\u2264", Relation1.leq);
	public final Relation1 GEQ = new Relation1(">=", Relation1.geq);
	public final Relation1 LT = new Relation1("<", Relation1.lt);
	public final Relation1 GT = new Relation1(">", Relation1.gt);
	public final Relation1 NEQ = new Relation1("<>", Relation1.neq);
	
	public final HashMap<String,LabelDelegate> symbolmap = new HashMap<String,LabelDelegate>();
	public static final OMObject TRUE = new OMSymbol("logic1","true");
	public static final OMObject FALSE = new OMSymbol("logic1", "false");
	{
// tests on values: > < <= >= !=
		symbolmap.put(Expression.nm(RELATION1_LEQ), LEQ);
		symbolmap.put(Expression.nm(RELATION1_GEQ), GEQ);
		symbolmap.put(Expression.nm(RELATION1_LT), LT);
		symbolmap.put(Expression.nm(RELATION1_GT), GT);
		symbolmap.put(Expression.nm(RELATION1_NEQ), NEQ);
		
		symbolmap.put(Expression.nm(ARITH1_PLUS), SOM);
		symbolmap.put(Expression.nm(ARITH1_MINUS), MINUS);
		symbolmap.put(Expression.nm(ARITH1_TIMES), MUL);
		symbolmap.put(Expression.nm(ARITH1_DIVIDE), DIV);
		symbolmap.put(Expression.nm(NUMS1_RATIONAL), DIV);
		symbolmap.put(Expression.nm(ARITH1_UNARYMINUS), UMINUS);
		
		symbolmap.put(Expression.nm(LOGIC1_OR), Logic1.OR());
		symbolmap.put(Expression.nm(LOGIC1_AND), Logic1.AND());
		symbolmap.put(Expression.nm(EUCLIDES_X), X);
		symbolmap.put(Expression.nm(EUCLIDES_Y), Y);
		symbolmap.put(Expression.nm(LOGIC1_NOT), new Not());
		symbolmap.put(Expression.nm(ARITH1_ROOT), Power.ROOT);
		symbolmap.put(Expression.nm(ARITH1_POWER), Power.POWER);
		symbolmap.put(Expression.nm(EUCLIDES_SQRT), Power.ROOT);
		symbolmap.put(Expression.nm(TRANSC1_SIN), Transc1.SIN);
		symbolmap.put(Expression.nm(TRANSC1_TAN), Transc1.TAN);
		symbolmap.put(Expression.nm(TRANSC1_EXP), Transc1.EXP);
		symbolmap.put(Expression.nm(TRANSC1_LN),  Transc1.LN);
		symbolmap.put(Expression.nm(TRANSC1_COS), Transc1.COS);

		symbolmap.put("transc1.arcsin", Transc1.ARCSIN);
		symbolmap.put("transc1.arctan", Transc1.ARCTAN);
		symbolmap.put("transc1.log",  Transc1.LOG);
		symbolmap.put("transc1.arccos", Transc1.ARCCOS);
		symbolmap.put("transc1.cot", Transc1.COT);
		symbolmap.put("transc1.csc", Transc1.CSC);
		symbolmap.put("transc1.sec", Transc1.SEC);
		
		symbolmap.put("arith1.abs", Transc1.ABS);
		symbolmap.put("complex1.conjugate", Transc1.CONJ);
		symbolmap.put("complex1.argument" , Transc1.ARGUMENT);
		symbolmap.put("complex1.real", Transc1.REAL);
		symbolmap.put("complex1.imaginary" , Transc1.IMAG);
		symbolmap.put(Expression.nm(Popcorn.COMPLEX1_COMPLEX_CARTESIAN), Complex.INSTANCE);
		symbolmap.put(Expression.nm(PROG1_IF), new If());
		symbolmap.put("plangeo7.area", AREA);
		symbolmap.put("euclides.ratio", Ratio.INSTANCE);
		symbolmap.put(Expression.nm(LIST1_LIST), List.INSTANCE);
		symbolmap.put("linalg2.vector", List.INSTANCE);
		symbolmap.put("euclides.visible", new Visible());
	}

	public Expression(Tracker tracker) {
		setAllTracker(tracker);
	}

	public void setAllTracker(Tracker tracker) {
		for(LabelDelegate delegate: symbolmap.values())
			delegate.setTracker(tracker);
		CONST.setTracker(tracker);
		LAMBDA.setTracker(tracker);
	}

	@Deprecated
	public Expression() {
	}
	
	public void copy(OMApplication oma, NameMapper mapper, Destroyable[] depend) {
		int size = oma.getLength()-1;
		for (int i = 0; i < size; i++) {
			OMObject o = oma.getElementAt(i+1);
			try {
				depend[i] = interpret(o, new Label(), mapper);
			} catch (ArrayStoreException e) {
				throw new InterpretException("Wrong type", e);
			}
		}
	}
	
	public  Label apply(Label l, LabelDelegate operator, OMApplication oma, NameMapper mapper) {
		l.register(operator);
		Destroyable[] depend = operator.createDepend(oma.getLength()-1);
		copy(oma, mapper, depend);
		l.setDepend(depend);
		operator.define(l);
		return l;
	}

	public  Destroyable copyOMA(Label l, OMObject o, NameMapper mapper) {
			OMApplication oma = (OMApplication)o;
			OMObject func = oma.firstElement();
	// arith1 functions: root/power, abs
	// transc1 functions sin/cos/tan en a
	// relation functions: and or not
	// prog1 function: if, while, assign, block, local_var
	// constants: pi, e, infinity
	// x/y of point/vector
	// complex: real/img arg, r
			if(func instanceof OMSymbol)
			{
				LabelDelegate delegate = symbolmap.get(nm((OMSymbol) func));
				if(delegate != null)
					return apply(l, delegate, oma, mapper);
			}
			if(func.isSame(PROG1_ASSIGN))
			{
				String name = ((OMVariable) oma.getElementAt(1)).getName();
				Destroyable d = null;
				try {
					d = mapper.fromString(name); // catch not found exception
				} catch(Exception e) {}
				Label k = l;
				if(d instanceof Label)
				{
					k = (Label)d; // pas op voor recursie $aap := $aap + 1
					// remove observers
				} else if(d != null)
					throw new InterpretException("Wrong variable type:" + name);
				Destroyable v = interpret(oma.getElementAt(2), k, mapper);
				if(v == l)
				{
					mapper.rename(l, name);
				}
				return v;
			}
			
			
	// functions on destroyables: distance, area, angle, ratio
			if(func.isSame(LENGTH)||func.isSame(DISTANCE))
			{
				LabelDelegate afstand = CONST.getTracker().getRegistered(AfstandHandler.TYPE);
				l.register(afstand);
				Destroyable[] depend = afstand.createDepend();
				copy(oma, mapper, depend);
				if(func.isSame(LENGTH))
					depend[1] = depend[0];
				depend[2] = mapper.getO();
				depend[3] = mapper.getU();
				l.setDepend(depend);
				afstand.define(l);
				return l;
			}
			if(func.isSame(CENTER))
			{
				LabelDelegate center = CONST.getTracker().getRegistered(MidpointTester.TYPE);
				l.register(center);
				Destroyable[] depend = center.createDepend();
				copy(oma, mapper, depend);
				Destroyable tmp = depend[0];
				depend[0] = depend[1];
				depend[1] = tmp;
				l.setDepend(depend);
				center.define(l);
				return l;
			}
			if(func.isSame(TRIANGLE))
			{
				// Triangle constructor
				Punt depend[] = new Punt[3];
				copy(oma, mapper, depend);
				DComparator.sort(depend);			
				return new Triangle(depend);
			}
			if(func.isSame(FNS1_IDENTITY)) {
				OMObject arg = oma.getElementAt(1);
				return interpret(arg, l, mapper);
			}
			
			if(func instanceof OMVariable)
			{
				OMVariable var = (OMVariable) func;
				Label f = (Label) mapper.fromString(var.getName());
				if(f == null) throw new InterpretException("Unknown: " + var.getName());
				OMObject om = f.getAdapter().adapt(OMObject.class);
				if(om instanceof OMBinding) {
					OMBinding binding = (OMBinding) om;
					binding = (OMBinding) binding.copy();
					o = binding.getBody();
					for(int i = 1; i < oma.getLength(); i++)
					{
						o = binding.betaReduce(binding.firstVariable(), oma.getElementAt(i));
					}
				} else {
					oma = (OMApplication) oma.copy();
					oma.setElementAt(om, 0);
					o = oma;
				}	
				final Destroyable interpret = interpret(o, l, mapper);
				f.addObserver(new DestroyDependency(interpret));
				return interpret;
			}
			if(func instanceof OMBinding)
			{
				OMBinding binding = (OMBinding) func;
				o = binding.getBody();
				for(int i = 1; i < oma.getLength(); i++)
				{
					o = binding.betaReduce(binding.firstVariable(), oma.getElementAt(i));
				}
				return interpret(o, l, mapper);
			}
			l.setString(o.toString());
			return l;
		}

	public  Destroyable interpret(OMObject o, Label l, NameMapper mapper) {
			if(o instanceof OMVariable)
			{
				String name = ((OMVariable) o).getName();
				Destroyable var = mapper.fromString(name);
				if(var == null) throw new InterpretException("Unknown: " + name);
				return var;
			}
			if (o instanceof OMApplication)
			{
				return copyOMA(l, o, mapper);
			}
			if ( o instanceof OMInteger)
			{
				String n = ((OMInteger) o).getInteger();
				DefaultAdapter.getDefault(l).put(OMObject.class, o);
				l.setString(n);
				l.setValue(Numbers.valueOf(n));
				CONST.define(l);
				return l;
			}
			if(o instanceof OMFloat)
			{
				String n = ((OMFloat)o).getFloat();
				DefaultAdapter.getDefault(l).put(OMObject.class, o);
				l.setString(n);
	//valueOf is Locale-dependent
				double value = Double.parseDouble(n);
				l.setValue(Numbers.createDouble(value));
				CONST.define(l);
				return l;
			}
			if(o instanceof OMSymbol)
			{
				DefaultAdapter.getDefault(l).put(OMObject.class, o);
				if(o.isSame(NUMS1_PI))
				{
					l.setString("\u03c0");
					l.setValue(Numbers.PI);
					CONST.define(l);
					return l;
				} 
				if(o.isSame(NUMS1_E))
				{
					l.setString("e");
					l.setValue(Numbers.createDouble(Math.E));
					CONST.define(l);
					return l;
				}
				if(o.isSame(NUMS1_INFINITY))
				{
					l.setString("∞");
					l.setValue(Numbers.createDouble(Double.POSITIVE_INFINITY));
					CONST.define(l);
					return l;
				}
				if(o.isSame(NUMS1_I))
				{
					l.register(CONST);
					l.setState(Label.CONSTANT);
					l.setString("i");
					l.setValue(Numbers.createComplex(Numbers.ZERO, Numbers.ONE));
					CONST.define(l);
					return l;
				}
				if(o.isSame(TRUE) ||o.isSame(FALSE))
				{
					l.setString(((OMSymbol) o).getName());
					l.setValue(o.isSame(TRUE)? Numbers.ZERO:Numbers.ONE);
					FLIP_FLOP.define(l);
					return l;
				}			
				else 
				{	// TODO wat moeten we hier mee?
					OMSymbol s = (OMSymbol) o;
					l.register(LAMBDA);
					l.setString(s.getCd() + "." + s.getName());
					l.setState(Label.EXPR);
				}
				
			}
			if(o instanceof OMBinding)
			{
				l.register(LAMBDA);
				l.setState(Label.EXPR);
				DefaultAdapter.getDefault(l).put(OMObject.class, o);
				return l;
			}
			if(o instanceof OMString)
			{
				l.register(Label.DEFAULT); // FIXME Euclides overschrijft key ""
				l.setString(((OMString)o).getString());
				return l;
			}
	// OMError, OMAttribution, OMForeign, OMByteArray, OMReference
			l.register(LAMBDA);
			DefaultAdapter.getDefault(l).put(OMObject.class, o);
			l.setState(Label.EXPR);
			return l;
		}

	public  Label stringExpr(String string, Label l, NameMapper mapper) {
		try {
			l.setString(string);
			OMObject o = new Popcorn(string).start();
			Label ll = (Label) interpret(o, l, mapper);
			return ll;
		} catch (ParseException e) {
			throw new InterpretException(e.getMessage(), e);
		}
	}

	public static String nm(OMSymbol s)
	{
		return s.getCd() + "." + s.getName();
	}

	// Add functions to interpreter
	public void put(OMSymbol s, LabelDelegate delegate) {
		symbolmap.put(nm(s), delegate);
	}
	
}
