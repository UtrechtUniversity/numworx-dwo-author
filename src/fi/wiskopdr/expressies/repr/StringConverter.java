package fi.wiskopdr.expressies.repr;

import fi.wiskopdr.expressies.Vermenigvuldiging;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Cosinus;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.Ln;
import fi.wiskopdr.expressies.Log;
import fi.wiskopdr.expressies.Sinus;
import fi.wiskopdr.expressies.Tangens;

public class StringConverter extends StrictConverter {
	
	private static StringConverter _instance = new StringConverter();
	
	protected StringConverter() {}
	
	public static StringConverter getInstance() {
		return _instance;
	}

	@Override
	public Object expressie(Expressie expressie) {
		return expressie.toString();
	}

	@Override
	public Object aftrekking(Object s1, Object s2) {
		if("0".equals(s1.toString())) s1 = "";
		if( s2 instanceof Veelterm)
			s2 = "$h" + s2 + "@";
		return new Veelterm( s1 + "-" + s2 );
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#macht(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object macht(Object s1, Object s2) {
		String s11 = s1.toString();
		if(s11 .startsWith("sin$h") )
		{	
			s11 = s11.substring(3);
			return "sin" + "$m" + s2 + "@" + s11 ;
		}
		if(s11 .startsWith("cos$h")) // TODO just like sin
		{	
			s11 = s11.substring(3);
			return "cos" + "$m" + s2 + "@" + s11 ;
		}
		if(s11 .startsWith("tan$h"))
		{	
			s11 = s11.substring(3);
			return "tan" + "$m" + s2 + "@" + s11 ;
		}
		if(s11 .startsWith("log$h"))
		{	
			s11 = s11.substring(3);
			return "log" + "$m" + s2 + "@" + s11 ;
		}
		if(s11 .startsWith("ln$h"))
		{	
			s11 = s11.substring(2);
			return "ln" + "$m" + s2 + "@" + s11 ;
		}
		if(s1 instanceof Veelterm || s11.contains("@") ) s1 = "$h" + s1 + "@";
		return s1 + "$m" + s2 + "@";
	}

	
	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#optelling(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object optelling(Object s1, Object s2) {
		if(s1 instanceof Number && s2 instanceof Breuk)
			return s1.toString() + s2;
		return new Veelterm ( s1 + "+" + s2 );
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.expressies.repr.AbstractConverter#vermenigvuldiging(java.lang.Object, java.lang.Object)
	 */
	private Object vermenigvuldiging(Object s1, Object s2, String op) {
		if (s1 instanceof Veelterm) s1 = "$h" + s1 + "@";
		if (s2 instanceof Veelterm) s2 = "$h" + s2 + "@";
		return s1 + op + s2;
	}
	
	public Object vermenigvuldiging(Vermenigvuldiging expr) {
		return vermenigvuldiging( expr.kind1.visit(this), expr.kind2.visit(this), expr.operator());
	}
	
	
}
