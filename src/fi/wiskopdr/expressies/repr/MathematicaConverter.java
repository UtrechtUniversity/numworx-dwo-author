package fi.wiskopdr.expressies.repr;

import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Expressie;

public class MathematicaConverter extends AbstractConverter {

	private MathematicaConverter() {
	}

	private static MathematicaConverter _instance = new MathematicaConverter();
	
	public static MathematicaConverter getInstance() {
		return _instance;
	}
	
	@Override
	public Object expressie(Expressie expressie) {
		return expressie.toStringCAS();
	}

	@Override
	public Object abs(Object arg) {
		return "Abs" + "[" +arg + "]";
	}

	@Override
	public Object arccos(Object kind1) {
		return "ArcCos" + "[" + kind1 + "]";
	}

	@Override
	public Object arcsin(Object kind1) {
		return "ArcSin" + "[" + kind1 + "]";
	}

	@Override
	public Object arctan(Object kind1) {
		return "ArcTan" + "[" + kind1 + "]";
	}

	@Override
	public Object bin(Object kind1, Object kind2) {
		return "Binomial" + "[" + kind1 + "," + kind2 + "]";
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

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#basis(java.lang.String)
	 */
	@Override
	public Object basis(String basisString) {
		if("e".equals(basisString))
			return "E";
		else if("\u221e".equals(basisString))
			return "Infinity";
		else if("-\u221e".equals(basisString))
			return "-Infinity";
		if("\u03c0".equals(basisString))
			return "Pi";
		return super.basis(basisString);
	}

	@Override
	public Object fac(Object kind1) {
		return "Fac[" + kind1 + "]";
	}

	@Override
	public Object gcd(Object kind1, Object kind2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object integrate(Object kind1, Object kind2, Object kind3,
			Object kind4, String var) {
	   	if(!kind4.equals(var))
    	{
    		return "Integrate[" + kind1 + "*D[" +  kind4 + "," + var + "]" + ",{" + var + "," + kind2 + "," + kind3 + "}]";
    	}
    	return "Integrate[" + kind1 + ",{" + kind4 + "," + kind2 + "," + kind3 + "}]";//"$n" + kind3.toString() + 
	}

	@Override
	public Object invNorm(Object kind1, Object kind2, Object kind3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object limit(Object kind1, Object kind2, Object kind3, Object kind4) {
		   String direction = kind4.toString();
		if(direction.equals("1"))return "Limit[" + kind1 + "," + kind2+ "->" + kind3 + ",Direction->-1" + "]";
	    if(direction.equals("2"))return "Limit[" + kind1+ "," + kind2+ "->" + kind3 + ",Direction->1" + "]";
	    else return "Limit[" + kind1 + "," + kind2+ "->" + kind3 + "]";
	}

	@Override
	public Object ln(Object kind1) {
		return "Log[" + kind1 + "]";
	}

	@Override
	public Object log(Object kind1) {
		return "Log" + "["+ "10," + kind1 + "]";
	}

	public Object macht(Object s1, Object s2) {
		if(true || 
				s1 instanceof Veelterm || 
				s1.toString().contains("[")||
				s1.toString().contains("^")) 
			s1 = "(" + s1 + ")";
		return s1 + "^(" + s2 + ")";
	}
	
	@Override
	public Object max(Object kind1, Object kind2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object min(Object kind1, Object kind2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object ndelog(Object kind1, Object kind2) {
		// FIXME plain wrong!
		return "Log" + "[" + kind2 + "," + kind1 + "]";	}

	@Override
	public Object root(Object kind1, Object kind2) {
        return "(" + kind1 + ")^(1/(" + kind2 + "))";
	}

	@Override
	public Object normalcdf(Object kind1, Object kind2, Object kind3,
			Object kind4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object conjug(Object kind1) {
		return "Conjugate" + "[" + kind1 + "]";
	}

	@Override
	public Object cosecans(Object kind1) {
		return "Csc" + "[" + kind1 + "]";
	}

	@Override
	public Object cosinus(Object kind1) {
		return "Cos" + "[" + kind1 + "]";
	}

	@Override
	public Object cotangens(Object kind1) {
		return "Cot" + "[" + kind1 + "]";
	}

	@Override
	public Object decround(Object kind1, Object kind2) {
		return "N" + "[" + kind1 + "," + kind2 + "]";
	}


	@Override
	public Object diff(Object kind1, Object kind2) {
		return "D[" + kind1 + "," + kind2 + "]";
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
    	if(!kind2.equals(var))
    	{	//TODO
    		return "Integrate[" + kind1 + "*D[" +  kind2 + "," + var + "]" + "," + var +"]";
    	}
    	return "Integrate[" + kind1 + "," + kind2 + "]";//"$n" + kind3.toString() + 
	}

	@Override
	public Object prv(Object kind1, Object kind2, Object kind3, Object var) {
		return aftrekking("Function[" + var + "," + kind1 + "][" + kind3 +"]", 
				"Function[" + var + "," + kind1 + "][" + kind2 +"]");
	}

	@Override
	public Object secans(Object kind1) {
		return "Sec" + "[" + kind1 + "]";
	}

	@Override
	public Object sigma(Object kind1, Object kind2, Object kind3, Object kind4) {
		return "Sum[" + kind1 + ",{" + kind2 + "," + kind3 + "," + kind4 + "}]";
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
		return "Sin" + "[" + kind1 + "]";
	}

	@Override
	public Object tangens(Object kind1) {
		return "Tan" + "[" + kind1 + "]";
	}

	@Override
	public Object wortel(Object kind1) {
		return "Sqrt" + "[" + kind1 + "]";
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#optelling(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object optelling(Object s1, Object s2) {
		if(s1 instanceof Number && s2 instanceof Breuk)
		{
			return "(" + super.optelling(s1, s2) + ")";
		}
		return super.optelling(s1, s2);
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#deling(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object deling(Object s1, Object s2) {
		Object r =  super.deling(s1, s2);
		if(s1 instanceof Number && s2 instanceof Number) 
			r = new Breuk(s1, s2, r.toString());
		return r;
	}

	@Override
	public Object aantalsign(Object kind1) {
		return "Sgf[" + kind1 + "]"; //TODO
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#vergelijking(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public Object vergelijking(Object visit, String vergelijkingsTeken,
			Object visit2) {
		if ("=".equals(vergelijkingsTeken))
			vergelijkingsTeken = "==";
		if ("\u2265".equals(vergelijkingsTeken))
			vergelijkingsTeken = ">=";
		if ("\u2264".equals(vergelijkingsTeken))
			vergelijkingsTeken = "<=";
		return "Simplify[ " + super.vergelijking(visit, vergelijkingsTeken, visit2) + "]";
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#vergelijking(java.lang.Object, java.lang.String, java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public Object vergelijking(Object visit, String teken1, Object visit2,
			String teken2, Object visit3) {
		return vergelijking(visit,  teken1, visit2) +  " && "  +
			   vergelijking(visit2, teken2, visit3);
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#vergelijkingmeerv(java.lang.Object[])
	 */
	@Override
	public Object vergelijkingmeerv(Object[] objects) {
		if(objects.length == 1)
			return objects[0];
		String string = "||";
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < objects.length; i++) {
			builder.append("("); builder.append(objects[i]);
			builder.append(")"); builder.append(string);
		}
		builder.setLength(builder.length()-string.length());
		return builder;
	}

	

}
