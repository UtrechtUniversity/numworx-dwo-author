package fi.wiskopdr.expressies.repr;

import fi.beans.stringutils.StringUtils;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.Vermenigvuldiging;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Deling;
import fi.wiskopdr.expressies.Expressie;

public class StrictConverter extends AbstractConverter {

	StrictConverter() {
	}

	private static StrictConverter _instance = new StrictConverter();
	
	public static StrictConverter getInstance() { return _instance; }
	
	@Override
	public Object expressie(Expressie expressie) {
		return expressie.toStringStrikt();
	}

	
	@Override
	public Object aftrekking(Object s1, Object s2) {
		if( s2 instanceof Veelterm)
			s2 = "$h" + s2 + "@";
		return new Veelterm( "$a" + s1 + "$n" + s2 + "@@" );
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.StringConverter#macht(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object macht(Object s1, Object s2) {
		if(s1 instanceof Veelterm || s1.toString().contains("@")) s1 = "$h" + s1 + "@"; // is overhodig, maar wel true
		return "$p" + s1 + "$n" + s2 + "@@";
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#optelling(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object optelling(Object s1, Object s2) {
		if(s1 instanceof Number && s2 instanceof Breuk) 
			return s1.toString() + s2;
//		if(kind2 instanceof Deling && kind1 instanceof BasisExpressie && kind2.kind1 instanceof BasisExpressie && kind2.kind2 instanceof BasisExpressie)
//		{	int getal,teller,noemer;
//			boolean integerBreuk = true;
//			try
//			{	getal = Integer.parseInt(((BasisExpressie)kind1).basisString);
//				teller = Integer.parseInt(((BasisExpressie)kind2.kind1).basisString);
//				noemer = Integer.parseInt(((BasisExpressie)kind2.kind2).basisString);
//			}
//			catch(NumberFormatException e)
//			{	integerBreuk = false;
//			}
//			if(integerBreuk)
//			{	isVeelterm = false;
//				return kind1.toString() + kind2.toString();
//			}
//		}
		return "$o" + s1 + "$n" + s2 + "@@";
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.StringConverter#vermenigvuldiging(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object vermenigvuldiging(Object s1, Object s2) {
		if( s1 instanceof Veelterm) s1 = "$h" +s1 + "@";
		if( s2 instanceof Veelterm) s2 = "$h" +s2 + "@";
		return "$v" + s1 + "$n" + s2 + "@@";
	}
	
	@Override
	public Object deling(Object s1, Object s2) {
		String r = "$b" + s1 + "$n" + s2 + "@@";
		if(s1 instanceof Number && s2 instanceof Number) {
			return new Breuk(s1, s2, r);
		}
		return r;
	}

	@Override
	public Object abs(Object child) {
		return "$r" + child + "@";
	}

	@Override
	public Object arccos(Object kind1) {
		return "arccos" + "$h" + kind1 + "@";
	}

	@Override
	public Object arcsin(Object kind1) {
		return "arcsin" + "$h" + kind1 + "@";
	}

	@Override
	public Object arctan(Object kind1) {
		return "arctan" + "$h" + kind1 + "@";
	}

	@Override
	public Object bin(Object kind1, Object kind2) {
		return "$y" + kind1 + "$n" + kind2 + "@@";
	}

	@Override
	public Object binomcdf(Object kind1, Object kind2, Object kind3) {
		return "binomcdf"  + "$h" +  kind1 + "_" + kind2 + "_" + kind3 + "@";
	}

	@Override
	public Object binompdf(Object kind1, Object kind2, Object kind3) {
		return "binompdf"  + "$h" +  kind1.toString() + "_" + kind2.toString() + "_" + kind3.toString() + "@";
	}

	@Override
	public Object aantalsign(Object kind1) {
		return "sgf" + "$h" + kind1 + "@";
	}

	@Override
	public Object fac(Object kind1) {
		return kind1 + "!";
	}

	@Override
	public Object conjug(Object kind1) {
		return "$c" + kind1 + "@";
	}

	@Override
	public Object cosecans(Object kind1) {
		return "csc" + "$h" + kind1 + "@";
	}

	@Override
	public Object cosinus(Object kind1) {
		return "cos" + "$h" + kind1 + "@";
	}

	@Override
	public Object cotangens(Object kind1) {
		return "cot" + "$h" + kind1 + "@";
	}

	@Override
	public Object decround(Object kind1, Object kind2) {
		return "rnd" + "$h" + kind1 + "_" + kind2 + "@";
	}

	@Override
	public Object diff(Object kind1, Object kind2) {
		return "$d" + kind1 + "$n" + kind2 + "@@";
	}
	
	public Object differentiaal(Object kind1)	{
		return "$g" + kind1 + "@";
	}

	@Override
	public Object gcd(Object kind1, Object kind2) {
		return "gcd"  + "$h" +  kind1.toString() + "_" + kind2.toString() + "@";
	}

	@Override
	public Object integrate(Object kind1, Object kind2, Object kind3,
			Object kind4, String _) {
				return "$i" + kind1 + "$n" + kind2 + "$k" + kind3 + "$l" + kind4 + "@@@@";
			}

	@Override
	public Object invNorm(Object kind1, Object kind2, Object kind3) {
	return "invNorm"  + "$h" +  kind1 + "_" + kind2 + "_" + kind3 + "@";	}

	@Override
	public Object limit(Object kind1, Object kind2, Object kind3, Object kind4) {
		return "$T" + kind1 + "$n" + kind2 + "$k" + kind3 + "$l" + kind4 + "@@@@";
	}

	@Override
	public Object ln(Object kind1) {
		return "ln" + "$h" + kind1 + "@";
	}

	@Override
	public Object log(Object kind1) {
		return "log" + "$h" + kind1 + "@";
	}

	@Override
	public Object max(Object kind1, Object kind2) {
		return "max"  + "$h" +  kind1.toString() + "_" + kind2.toString() + "@";
	}

	@Override
	public Object min(Object kind1, Object kind2) {
		return "min"  + "$h" +  kind1.toString() + "_" + kind2.toString() + "@";
	}

	@Override
	public Object decroundstrict(Object kind1, Object kind2) {
		return "rnq" + "$h" + kind1 + "_" + kind2 + "@";
	}

	@Override
	public Object ndelog(Object kind1, Object kind2) {
		return "$L" + kind1 + "$n" +kind2 + "@@";
	}

	@Override
	public Object normalcdf(Object kind1, Object kind2, Object kind3, Object kind4) {
		return "normalcdf"  + "$h" +  kind1 + "_" + kind2 + "_" + kind3 + "_" + kind4 + "@";
	}

	@Override
	public Object poissoncdf(Object kind1, Object kind2) {
		return "poissoncdf"  + "$h" +  kind1 + "_" + kind2 + "@";
	}

	@Override
	public Object poissonpdf(Object kind1, Object kind2) {
		return "poissonpdf"  + "$h" +  kind1 + "_" + kind2 + "@";
	}

	@Override
	public Object secans(Object kind1) {
		return "sec" + "$h" + kind1 + "@";
	}

	@Override
	public Object root(Object s1, Object s2) {
		return "$W" + s1 + "$n" + s2 + "@@";
	}

	@Override
	public Object primitieve(Object kind1, Object kind2, String _) {
		return "$P" + kind1 + "$n" + kind2  + "@@";
	}

	@Override
	public Object prv(Object kind1, Object kind2, Object kind3, Object kind4) {
		return "$q" + kind1 + "$n" + kind2 + "$k" + kind3 + "$l" + kind4 + "@@@@";
	}

	@Override
	public Object sigma(Object kind1, Object kind2, Object kind3, Object kind4) {
		return "$S" + kind1 + "$n" + kind2 + "$k" + kind3 + "$l" + kind4 + "@@@@";
	}

	@Override
	public Object siground(Object kind1, Object kind2, Object kind3) {
		return "rns" + "$h" + kind1 + "_" + kind2 + "_" + kind3 + "@";
	}

	@Override
	public Object sigroundstandard(Object kind1, Object kind2) {
		return "rns" + "$h" + kind1 + "_" + kind2 + "@";
	}

	@Override
	public Object sinus(Object kind1) {
		return "sin" + "$h" + kind1 + "@";
	}

	@Override
	public Object tangens(Object kind1) {
		return "tan" + "$h" + kind1 + "@";
	}

	@Override
	public Object wortel(Object kind1) {
		return "$w" + kind1 + "@";
	}

	@Override
	public Object diffpartial(Object kind1, Object kind2) {
		return "$D" + kind1 + "$n" + kind2 + "@@";
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#basis(java.lang.String)
	 */
	@Override
	public Object basis(String basisString) {
		String basisStringUit = StringUtils.replaceStr(basisString,"?(","$s");
		basisStringUit = StringUtils.replaceStr(basisStringUit,")","@");
		double waarde = Double.NaN;
		try { 
			waarde = Double.parseDouble(basisStringUit);
			// looks like a double
		} catch (Exception e) {} 
		
		
		if(!Double.isNaN(waarde) && (!Algebra.withinLongRange((long)waarde) || basisString.indexOf('E')>-1)){
        	if("0".equals(basisString.substring(basisString.indexOf('E')+1)))basisStringUit = basisString.substring(0,basisString.indexOf('E'));
			else 
				basisStringUit = StringUtils.replaceStr(basisString,"E","*$p10$n") + "@@";
		}
		
		if(WiskOpdr.language.toString().equals("nl"))basisStringUit = basisStringUit.replace('.',',');
        
        
		 
        return super.basis(basisStringUit);

	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#vergelijking(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public Object vergelijking(Object visit, String vergelijkingsTeken,
			Object visit2) {
		// TODO Auto-generated method stub
		return super.vergelijking(visit, vergelijkingsTeken, visit2);
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#vergelijking(java.lang.Object, java.lang.String, java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public Object vergelijking(Object visit, String teken1, Object visit2,
			String teken2, Object visit3) {
		return visit + teken1 + visit2 + teken2 + visit3;
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#vergelijkingmeerv(java.lang.Object[])
	 */
	@Override
	public Object vergelijkingmeerv(Object[] objects) {
		if(objects.length == 1)
			return objects[0];
		String string = "  " + WiskOpdr.rb.getString("ofLabel") + "  ";
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < objects.length; i++) {
			builder.append(objects[i]);
			builder.append(string);
		}
		builder.setLength(builder.length()-string.length());
		return builder;
	}
	
}
