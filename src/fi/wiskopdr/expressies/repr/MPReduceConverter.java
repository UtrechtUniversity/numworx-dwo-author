package fi.wiskopdr.expressies.repr;

import java.math.BigInteger;

import fi.wiskopdr.expressies.Expressie;

public class MPReduceConverter extends AbstractConverter {

	private static AbstractConverter _instance = new MPReduceConverter();

	
	
	@Override
	public Object abs(Object visit) {
		return "abs(" + visit + ")";
	}

	@Override
	public Object expressie(Expressie expressie) {
		return "NOT_IMPLEMENTED";
	}

	public static AbstractConverter getInstance() {
		return _instance ;
	}

	@Override
	public Object arccos(Object kind1) {
		return "acos(" + kind1 + ")";
	}

	@Override
	public Object arcsin(Object kind1) {
		return "asin(" + kind1 + ")";
	}

	@Override
	public Object arctan(Object kind1) {
		return "atan(" + kind1 + ")";
	}

	@Override
	public Object bin(Object kind1, Object kind2) {
		return "binomial(" + kind1 + "," + kind2 + ")";
	}

	@Override
	public Object binomcdf(Object kind1, Object kind2, Object kind3) {
		return null;
	}

	@Override
	public Object binompdf(Object kind1, Object kind2, Object kind3) {
		return null;
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
		return super.basis(basisString.replace(',', '.')); // decimal comma to decimal point
	}

	@Override
	public Object fac(Object kind1) {
		return "factorial(" + kind1 + ")";
	}

	@Override
	public Object gcd(Object kind1, Object kind2) {
		return "gcd(" + kind1 +"," + kind2 + ")";
	}

	@Override
	public Object integrate(Object kind1, Object kind2, Object kind3,
			Object kind4, String var) {
//		return "int(" + kind1 + "," + kind4 + "," + kind2 + "," + kind3 + ")";
		return prv ( primitieve(kind1, kind4, var), kind2, kind3, var);
	}

	@Override
	public Object invNorm(Object kind1, Object kind2, Object kind3) {
		return null;
	}

	@Override
	public Object limit(Object kind1, Object kind2, Object kind3, Object kind4) {
		String up = "";
		if("2".equals(kind4.toString()))
			up = "!-";  // naar boven
		else if("1".equals(kind4.toString()))
			up = "!+"; // naar beneden;
		return "limit" +up+ "(" + kind1 + "," + kind2 + "," + kind3 + ")";
	}

	@Override
	public Object ln(Object kind1) {
		return "log(" + kind1 + ")";
	}

	@Override
	public Object log(Object kind1) {
		return "logb(" + kind1 + ",10)";
	}

	@Override
	public Object max(Object kind1, Object kind2) {
		return "max(" + kind1 + "," + kind2 + ")";
	}

	@Override
	public Object min(Object kind1, Object kind2) {
		return "min(" + kind1 + "," + kind2 + ")";
	}

	@Override
	public Object ndelog(Object kind1, Object kind2) { // TODO controle volgorde
		return "logb(" + kind1 + "," + kind2 + ")";
	}

	@Override
	public Object root(Object kind1, Object kind2) {
		return "(" + kind1 + ")^/(" + kind2 + ")";
	}

	@Override
	public Object normalcdf(Object kind1, Object kind2, Object kind3,
			Object kind4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object conjug(Object kind1) {
		return "conjugate(" + kind1 + ")";
	}

	@Override
	public Object cosecans(Object kind1) {
		return "csc(" + kind1 + ")";
	}

	@Override
	public Object cosinus(Object kind1) {
		return "cos(" + kind1 + ")";
	}

	@Override
	public Object cotangens(Object kind1) {
		return "cot(" + kind1 + ")";
	}

	@Override
	public Object decround(Object kind1, Object kind2) {
		if(kind2 instanceof Veelterm) {
			try { 
				kind2 = new BigInteger(kind2.toString());
			} catch(Exception e) {}
		}
		
		if(kind2 instanceof BigInteger) {
			switch(((BigInteger) kind2).signum()) {
				case 0: return "round(" + kind1 + ")";
				case +1: 
						String ten = "(10^(" + kind2 + "))";
						return "(round(" + ten +"*" + kind1 + ")/" + ten + ")";
				case -1:
						ten = "(10^(" + ((BigInteger) kind2).abs() + "))";
						return "(" + ten + "*round(" + kind1 + "/" + ten + "))";
			}
			
		}
		
		return kind1; // FIXME niet goed....
	}

	@Override
	public Object diff(Object kind1, Object kind2) {
		return "df(" + kind1 + "," + kind2 + ")";
	}
	
	public Object differentiaal(Object kind1)	{
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
	public Object primitieve(Object kind1, Object kind2, String var) {
		// TODO if(!kind2.equals(var))
		return "int(" + kind1 + "," + kind2 + ")";
	}

	@Override
	public Object prv(Object kind1, Object kind2, Object kind3, Object var) {
//		return aftrekking( 
//				"sub(" + var + "=" + kind3 + "," + kind1 +")",
//				"sub(" + var + "=" + kind2 + "," + kind1 +")");
		return "prv(" + kind1 + "," + kind2 +"," + kind3 +"," + var +")";
	}

	@Override
	public Object secans(Object kind1) {
		return "sec(" + kind1 + ")";
	}

	@Override
	public Object sigma(Object kind1, Object kind2, Object kind3, Object kind4) {
		return "sum("+kind1 +"," + kind2 +"," + kind3 +"," + kind4 + ")";
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
		return "sin(" + kind1 + ")";
	}

	@Override
	public Object tangens(Object kind1) {
		return "tan(" + kind1 + ")";
	}

	@Override
	public Object wortel(Object kind1) {
		return "sqrt(" + kind1 + ")";
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
		return "trigsimp("  + super.vergelijking(visit, vergelijkingsTeken, visit2) + ")";
	}

}
