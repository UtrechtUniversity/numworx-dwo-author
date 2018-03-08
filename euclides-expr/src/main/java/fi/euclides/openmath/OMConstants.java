package fi.euclides.openmath;

import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;

public interface OMConstants {
	OMSymbol LOGIC1_AND = new OMSymbol("logic1","and");
	OMSymbol LOGIC1_OR     = new OMSymbol("logic1", "or");
	OMSymbol LOGIC1_NOT     = new OMSymbol("logic1", "not");
	OMSymbol IMPLIES = new OMSymbol("logic1", "implies");
	OMSymbol ASSERTION = new OMSymbol("plangeo1", "assertion");
	OMSymbol CONFIGURATION = new OMSymbol("plangeo1", "configuration");
	OMSymbol ARE_ON_CIRCLE = new OMSymbol("plangeo3", "are_on_circle");
	OMSymbol LENGTH = new OMSymbol("plangeo7", "length");
	OMSymbol ARE_ON_LINE = new OMSymbol("plangeo1", "are_on_line");
	OMSymbol RELATION1_EQ = new OMSymbol("relation1", "eq");
	OMSymbol RELATION1_APPROX = new OMSymbol("relation1", "approx");
	OMSymbol RELATION1_NEQ = new OMSymbol("relation1", "neq");
	OMSymbol RELATION1_LT = new OMSymbol("relation1", "lt");
	OMSymbol RELATION1_LEQ = new OMSymbol("relation1", "leq");
	OMSymbol RELATION1_GT = new OMSymbol("relation1", "gt");
	OMSymbol RELATION1_GEQ = new OMSymbol("relation1", "geq");
	OMSymbol IS_MIDPOINT = new OMSymbol("plangeo3", "is_midpoint");
	OMSymbol INCIDENT = new OMSymbol("plangeo1", "incident");
	OMSymbol PERPENDICULAR = new OMSymbol("plangeo3", "perpendicular");
	OMSymbol PARALLEL = new OMSymbol("plangeo3", "parallel");
    OMSymbol POINT = new OMSymbol("plangeo1", "point");
	OMSymbol LINE = new OMSymbol("plangeo1", "line");
	OMSymbol CIRCLE = new OMSymbol("plangeo3", "circle");
	OMObject RADIUS  = new OMSymbol("plangeo3", "radius");
	OMSymbol SEGMENT = new OMSymbol("plangeo2", "segment");
	OMSymbol CENTER = new OMSymbol("plangeo3", "center");
	OMSymbol DISTANCE = new OMSymbol("plangeo3", "distance");
	OMObject CORNER = new OMSymbol("plangeo2", "corner");
	OMSymbol BISECTOR = new OMSymbol("plangeo7", "bisector");
	OMSymbol LOCUS = new OMSymbol("plangeoX", "locus");
	OMSymbol CONIC = new OMSymbol("plangeo6", "conic");
	OMObject RADIUS_OF = new OMSymbol("plangeo3", "radius_of");
	OMSymbol ARITH1_PLUS = new OMSymbol("arith1", "plus");
	OMSymbol ARITH1_MINUS = new OMSymbol("arith1", "minus");
	OMSymbol ARITH1_UNARYMINUS = new OMSymbol("arith1", "unary_minus");
	
	OMSymbol SIMILAR = new OMSymbol("plangeo8","are_similar");
	OMSymbol CONGRUENT = new OMSymbol("plangeo8","are_congruent");

	OMSymbol TRIANGLE = new OMSymbol("plangeo7", "triangle");

	public static final OMSymbol EUCLIDES_X = new OMSymbol("euclides", "x");
	public static final OMSymbol EUCLIDES_Y = new OMSymbol("euclides", "y");
	public static final OMSymbol TRANSC1_COS = new OMSymbol("transc1", "cos");
	public static final OMSymbol TRANSC1_SIN = new OMSymbol("transc1", "sin");
	public static final OMSymbol TRANSC1_TAN = new OMSymbol("transc1", "tan");
	public static final OMSymbol TRANSC1_EXP = new OMSymbol("transc1", "exp");
	public static final OMSymbol TRANSC1_LN = new OMSymbol("transc1", "ln");
	public static final OMSymbol ARITH1_ROOT = new OMSymbol("arith1", "root");
	public static final OMSymbol EUCLIDES_SQRT = new OMSymbol("euclides", "sqrt");

	public static final OMObject NUMS1_PI = new OMSymbol("nums1", "pi");
	public static final OMObject NUMS1_I = new OMSymbol("nums1", "i");
	public static final OMObject NUMS1_E = new OMSymbol("nums1", "e");
	public static final OMObject NUMS1_INFINITY = new OMSymbol("nums1", "infinity");

	  public final static OMSymbol ARITH1_TIMES = new OMSymbol("arith1", "times");
	  public final static OMSymbol ARITH1_DIVIDE = new OMSymbol("arith1", "divide");
	  public final static OMSymbol ARITH1_POWER = new OMSymbol("arith1", "power");
	  public final static OMSymbol NUMS1_RATIONAL = new OMSymbol("nums1", "rational");
	  public final static OMSymbol PROG1_ASSIGN = new OMSymbol("prog1", "assignment");
	  public final static OMSymbol FNS1_LAMBDA = new OMSymbol("fns1", "lambda");

	  public final static OMSymbol PROG1_IF = new OMSymbol("prog1", "if");
	  public final static OMSymbol INTERVAL1_INTERVAL = new OMSymbol("interval1", "interval");
	  OMSymbol ARITH1_ABS = new OMSymbol("arith1", "abs" );
	  
	  OMSymbol FNS1_IDENTITY = new OMSymbol("fns1", "identity");
	  OMSymbol LIST1_LIST = new OMSymbol("list1", "list");
	  OMSymbol LIST1_MAP  = new OMSymbol("list1", "map");
	  OMSymbol LIST1_SUCHTHAT = new OMSymbol("list1", "suchthat");
	  OMSymbol LIST2_LIST_SELECTOR = new OMSymbol("list2", "list_selector");
	  OMSymbol LIST2_SIZE = new OMSymbol("list2", "size");
	    
	  OMSymbol GEODEFINER_CHECKED = new OMSymbol("geodefiner", "checked");
	  OMSymbol GEODEFINER_OBJECT = new OMSymbol("geodefiner", "object");

}
