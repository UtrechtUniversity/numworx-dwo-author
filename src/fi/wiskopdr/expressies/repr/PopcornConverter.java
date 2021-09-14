package fi.wiskopdr.expressies.repr;

import java.math.BigInteger;
import java.util.ArrayList;

import fi.wiskopdr.expressies.Expressie;

public class PopcornConverter extends AbstractConverter {

	private static PopcornConverter _instance = new PopcornConverter();
	
	public static PopcornConverter getInstance() {
		return _instance;
	}
	
	private PopcornConverter() {
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#basis(java.lang.String)
	 */
	@Override
	public Object basis(String basisString) {
		if("-\u221e".equals(basisString))
			return "-infinity";
		if("\u221e".equals(basisString))
			return "infinity";
		if("\u03c0".equals(basisString))
			return "pi";
		if("i".equals(basisString)) return "i"; // nums1.i not $i
		if("e".equals(basisString)) return "e"; // nums1.e
		basisString = basisString.replace(',','.'); // decimale komma
		try {
			return new BigInteger(basisString); // looks Integer
		} catch(Exception oops) {}
		try {
			new Double(basisString);    // looks Double
			return basisString;			// prevent conversion errors 
		} catch(Exception oops) {}
		return "$" + basisString;		// variable
	}

	
	@Override
	public Object abs(Object visit) {
		return "abs("+visit+")";
	}

	@Override
	public Object expressie(Expressie expressie) {
		return "moreerrors.unexpected!(\"" + expressie + "\")";
	}

	@Override
	public Object arccos(Object kind1) {
		return "arccos(" + kind1 + ")";
	}

	@Override
	public Object arcsin(Object kind1) {
		return "arcsin(" + kind1 + ")";
	}

	@Override
	public Object arctan(Object kind1) {
		return "arctan(" + kind1 + ")";
	}

	@Override
	public Object bin(Object kind1, Object kind2) {
		return "binomial("+kind1+"," + kind2 + ")";
	}

	@Override
	public Object binomcdf(Object kind1, Object kind2, Object kind3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object binompdf(Object kind1, Object kind2, Object kind3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object conjug(Object kind1) {
		return "complex1.conjugate(" + kind1 + ")";
	}

	@Override
	public Object cosecans(Object kind1) {
		return "csc(" + kind1 + ")";
	}

	@Override
	public Object cosinus(Object kind1) {
		return "cos("+kind1+")";
	}

	@Override
	public Object cotangens(Object kind1) {
		return "cot(" + kind1 + ")";
	}

	@Override
	public Object decround(Object kind1, Object kind2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object diff(Object kind1, Object kind2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object differentiaal(Object kind1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object fac(Object kind1) {
		return "factorial(" + kind1 + ")";
	}

	@Override
	public Object gcd(Object kind1, Object kind2) {
		return "gcd("+kind1+"," +kind2 + ")";
	}

	@Override
	public Object integrate(Object kind1, Object kind2, Object kind3,
			Object kind4, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object invNorm(Object kind1, Object kind2, Object kind3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object limit(Object kind1, Object kind2, Object kind3, Object kind4) {
		String up = "limit1.both_sides";
		if("2".equals(kind4.toString()))
			up = "limit1.below";  // naar boven
		else if("1".equals(kind4.toString()))
			up = "limit1.above"; // naar beneden;
		// 
		return "limit1.limit(" + kind3 + "," + up + ",lambda[" + kind2 + "->" + kind1 + "])";
	}

	@Override
	public Object ln(Object kind1) {
		return "ln(" + kind1 + ")";
	}

	@Override
	public Object log(Object kind1) {
		return "log(10," + kind1 + ")";
	}

	@Override
	public Object max(Object kind1, Object kind2) {
		return "max(" +kind1 + "," + kind2 + ")";
	}

	@Override
	public Object min(Object kind1, Object kind2) {
		return "min(" +kind1 + "," + kind2 + ")";
	}

	@Override
	public Object ndelog(Object kind1, Object kind2) {
		return "log(" + kind2 + "," + kind1 + ")";
	}

	@Override
	public Object root(Object kind1, Object kind2) {
		return "root("+kind1 +"," + kind2 +")";
	}

	@Override
	public Object normalcdf(Object kind1, Object kind2, Object kind3,
			Object kind4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object poissoncdf(Object kind1, Object kind2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object poissonpdf(Object kind1, Object kind2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object primitieve(Object kind1, Object kind2, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object prv(Object kind1, Object kind2, Object kind3, Object kind4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object secans(Object kind1) {
		return "sec(" + kind1 + ")";
	}

	@Override
	public Object sigma(Object kind1, Object kind2, Object kind3, Object kind4) {
		// TODO sum( kind3..kind4, lambda( $kind2 -> kind1 ) )
		return "sum(interval1.integer_interval(" + kind3 + "," + kind4 + "),lambda[" + kind2 + "->" + kind1 + "])";
	}

	@Override
	public Object siground(Object kind1, Object kind2, Object kind3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object sigroundstandard(Object kind1, Object kind2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object sinus(Object kind1) {
		return "sin("+kind1+")";
	}

	@Override
	public Object tangens(Object kind1) {
		return "tan("+kind1+")";
	}

	@Override
	public Object wortel(Object kind1) {
		return root(kind1, "2");
	}

	@Override
	public Object aantalsign(Object visit) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object vergelijking(Object visit, String vergelijkingsTeken,
			Object visit2) {
		if ("\u2265".equals(vergelijkingsTeken))
			vergelijkingsTeken = ">=";
		if ("\u2264".equals(vergelijkingsTeken))
			vergelijkingsTeken = "<=";
		return  super.vergelijking(visit, vergelijkingsTeken, visit2) ;
	}

	@Override
	public Object vectorExpr(ArrayList<Object> kinderen)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object matrix(ArrayList<ArrayList<Object>> kinderen)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
