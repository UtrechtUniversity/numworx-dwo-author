package fi.wiskopdr.expressies;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.*;
import java.util.*;
import java.util.List;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.repr.AbstractConverter;
import fi.wiskopdr.expressies.repr.MPReduce;
import fi.wiskopdr.expressies.repr.MPReduceConverter;
import fi.wiskopdr.expressies.repr.MPReduceParser;
import fi.wiskopdr.expressies.repr.MathematicaConverter;
import fi.wiskopdr.expressies.repr.ParseException;
import fi.wiskopdr.expressies.repr.StrictConverter;
import fi.wiskopdr.expressies.repr.StringConverter;
import fi.wiskopdr.formuleobjects.*;
import fi.beans.ideas.AbstractRule;
import fi.beans.ideas.IdeasIF;
import fi.beans.ideas.RuleIF;
import fi.beans.stringutils.StringUtils;



public class Expressie 
{	
	public Expressie kind1, kind2, kind3, kind4;
	public String operatorString;
	boolean isVeelterm;
	boolean isProdukt;
	boolean isBasis;

	static DecimalFormatSymbols dfs;
	public static DecimalFormat df;
	public static DecimalFormat dfe;
	public static DecimalFormat df3;
	public static FontMetrics fm;
	
	private static Hashtable casEvalStrings = new Hashtable();
	
	static boolean hoekGraden;
	
	public Expressie()
	{	dfs = new DecimalFormatSymbols();
		if(WiskOpdr.language.toString().equals("nl")) dfs.setDecimalSeparator(',');
		else dfs.setDecimalSeparator('.');
		if(WiskOpdr.language.toString().equals("nl")) dfs.setGroupingSeparator(' ');
		else dfs.setGroupingSeparator(' ');
		df = new DecimalFormat("0.##########", dfs);
		dfe = new DecimalFormat("0.##########E0", dfs);
		df3 = new DecimalFormat("0.###", dfs);
	}
	public static void zetHoekGraden(boolean b)
  	{	hoekGraden=b;
	}
	public void zetMaat(FontMetrics fm)
  	{
	}
	public void teken(Graphics g, int x, int y)
  	{ 
	}
	
	public Expressie geefDiff(BasisExpressie e)
	{	return null;
	}
	
	public double geefWaarde()
	{	return Double.NaN;
	}
	
	public Complex geefWaardeComplex()
	{	return null;
	}
	
	public double geefWaarde(double subst)
	{	return Double.NaN;
	}
	
	public Complex geefWaardeComplex(Complex subst)
	{	return null;
	}
	
	public double geefWaarde(double[] subst, String[] vars)
	{	return Double.NaN;
	}
	
	public Complex geefWaardeComplex(Complex[] subst, String[] vars)
	{	return null;
	}
	
	public Expressie substitueer(double subst, String var)
	{	return null;
	}
	
	public Expressie substitueer(Expressie subst, String var)
	{	return null;
	}
	
	public boolean isWaarde(double subst)
	{	return true;
	}
	public String geefVarNaam()
	{	return null;
	}
	
	public boolean isVar()
	{	return this instanceof BasisExpressie;
	}
	
	public boolean isWaarde()
	{	return !Double.isNaN(geefWaarde());
	}
	
	public String toString()
	{
		return visit(StringConverter.getInstance()).toString();
	}
	
	public String toStringStrikt()
	{
		return visit(StrictConverter.getInstance()).toString();
	}
    
    public String toStringCAS()
    {
    	return visit(MathematicaConverter.getInstance()).toString();
    }
	
	public static Expressie evalWithCAS(Expressie e)
	{
		if(e instanceof Diff)
		{	Expressie diff = ((Diff)e).evalDiff();
			if(diff!=null)return diff;
		}
		if(isCasLocal())
			return evalWithReduce(e);
		if(isCasIdeas())
			return evalWithIdeas(e.toStringStrikt());
		return evalWithCAS(e.toStringCAS());
	}
	
	public static boolean isCasLocal() {
		return WiskOpdr.CAS_LOCAL .equals( WiskOpdr.doCAS)
		;
	}
	public static boolean isCasIdeas() {
		return WiskOpdr.CAS_IDEAS .equals( WiskOpdr.doCAS);
	}
	
	private static Expressie evalWithReduce(Expressie e) {
		try {
			String command = e.visit(MPReduceConverter.getInstance()).toString();
			String result = MPReduce.evaluate(command);
			Reader reader = new StringReader(result);
			return (Expressie) new fi.wiskopdr.expressies.repr.MPReduceParser(reader).start();
		} catch (ParseException e1) {
			e1.printStackTrace();
			return null;
		} catch (Throwable e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	private static Expressie evalWithIdeas(String evalCommand)
	{
		Expressie expr = (Expressie) casEvalStrings.get(evalCommand);
		if(expr != null)
			return expr;
		RuleIF result = WiskOpdr.ideas.interpret(evalCommand);
		if (result.isException())
			return null;
		expr =  FormuleParser.geefExpressie("$f" + result.getExpr() + "@");
		if(expr != null)
			casEvalStrings.put(evalCommand, expr);
		return expr;
	}
	
	static private class Rule extends AbstractRule {
		private String expr;
		public String getExpr() { return expr; };
		private Rule(String expr) { this.expr = expr; }
	}
	
	/**
	 * Los vergelijking op via ideas servlet.
	 * @expr vergelijking.toStringStrikt()
	 * @arg  bijv. "x"
	 */
	static private VergelijkingMeerv solveWithIdeas(String expr, String arg) {
		RuleIF[] args = new RuleIF[] { new Rule(expr), new Rule(arg) };
		RuleIF result = WiskOpdr.ideas.interpret(IdeasIF.SOLVE, args);
		expr = result.getExpr();
// FIXME zou FormuleParser.parseVergelijking moeten zijn.
		StringTokenizer st = new StringTokenizer(expr, "\u2228");
		Vergelijking[] v = new Vergelijking[st.countTokens()];
		
		for(int i=0 ; i<v.length ; i++)
		{	expr = st.nextToken();
			int index = expr.indexOf("=") + 1;
			String var = index >= 2 ? expr.substring(0,index-1) : arg;
			expr = expr.substring(index);
			Expressie ps = FormuleParser.geefExpressie("@f" +var.trim() +"@");
			Expressie es = FormuleParser.geefExpressie("$f"+expr.trim()+"@");
			v[i] = new Vergelijking(ps,es);
		}
		return new VergelijkingMeerv(v);
	}
	
	
	/**
	 * Bereken de (double) waarde van een Expressie via een CAS.
	 * @param e
	 * @return waarde
	 */
	public static double geefWaardeViaIdeas(Expressie e)
	{
		RuleIF result = WiskOpdr.ideas.interpret(IdeasIF.NUMERIC, e.toStringStrikt());
		if(result.isException())
			return Double.NaN;
		return Double.parseDouble(result.getExpr());
	}
	
	/**
	 * Evalueer expressie mbv Mathematica.
	 * Wordt twee keer gebruikt
	 * @param evalCommand Mathematica input
	 * @return result
	 * @deprecated niet altijd mathematica aanwezig.
	 */
	public static Expressie evalWithCAS(String evalCommand)
	{	
       	Expressie e = null;
       	String s = "";
       	
    	if(casEvalStrings.containsKey(evalCommand)) s = (String)casEvalStrings.get(evalCommand);
    	else
    	{	//System.out.println(evalCommand);
       	
	        try
	        {   WiskOpdr.phrasebook.eval("ClearAll[x]");
	            s = WiskOpdr.phrasebook.eval("InputForm[" + evalCommand + "]");
	            //s = WiskOpdr.phrasebook.eval(evalCommand);
	            
	            //System.out.println(s);
	        }
	        catch(Exception ex)
	        {}
	        casEvalStrings.put(evalCommand, s);
    	}
       	/*
		evalCommand = StringUtils.replaceStr(evalCommand,"+","%2B");
		evalCommand = StringUtils.replaceStr(evalCommand,"/","%2F");
	
		String s = "";
		try
		{   URLConnection con;
	        URL u = new URL("http://www.fi.uu.nl/servlet/mathshell/mathshell?input=InputForm[" + evalCommand + "]&native=on");
	        con = u.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        if (in != null) 
	        {   s = "";
	            String tmp = "";
	            while ((tmp = in.readLine()) != null) {
	                s += tmp;
	            }
	            in.close();
	            System.out.println(s);
	            int index1 = s.indexOf("<pre>");
	            int index2 = s.indexOf("</pre>");
	            s = s.substring(index1+5, index2).trim();
	            System.out.println(s);
	            
	        }
	    }
		catch(Exception ex)
		{}*/
		
		s = s.substring(0,s.length()-1);
		s = s.replace('[','(');
		s = s.replace(']',')');
		s = StringUtils.replaceStr(s,"Pi","\u03C0");
		s = StringUtils.replaceStr(s,"E","e");
		s = StringUtils.replaceStr(s,"Log","ln");
		s = StringUtils.replaceStr(s,"Sin","sin");
		s = StringUtils.replaceStr(s,"Cos","cos");
		s = StringUtils.replaceStr(s,"Tan","tan");
		s = StringUtils.replaceStr(s,"Arc","arc");
		s = StringUtils.replaceStr(s,"Sqrt","sqrt");
		
		System.out.println("$f"+s+"@");
		e = FormuleParser.parse(FormuleParser.schoon(FormuleParser.formuleString("$f"+s+"@")));
		return e;
	}
	
	public static VergelijkingMeerv solve(Vergelijking vgl) {
		return solve(vgl, vgl.geefVarNaam());
	}
	
	public static VergelijkingMeerv solve(Vergelijking vgl, String arg) {
		if(isCasLocal())
			return solveWithReduce(vgl, arg);
		if(isCasIdeas())
			return solveWithIdeas(vgl.toStringStrikt(), arg);
		return solveWithCAS(vgl.geefExpLinks().toStringCAS() + "==" + vgl.geefExpRechts().toStringCAS(), arg);
		
	}
	
	
	private static VergelijkingMeerv solveWithReduce(Vergelijking vgl,
			String arg) {
			try {
				Object visit = vgl.visit(MPReduceConverter.getInstance());
				String command = "on fullroots $ off arbvars $ solve(" + visit + "," + arg  + ")";
				String result;
				result = MPReduce.evaluate(command); // delen met geogebra
				Object obj = new MPReduceParser(new StringReader(result)).start();
				Vergelijking[] oplossingen;
				if(obj instanceof List)
				{	List list = (List)obj;
					if( list.isEmpty())
						oplossingen = new Vergelijking[] {
							new Vergelijking(new BasisExpressie(arg), new BasisExpressie("0.1234567")) };
					else
						oplossingen = (Vergelijking[]) list.toArray(new Vergelijking[list.size()]);
				} else {
					oplossingen = new Vergelijking[] { (Vergelijking) obj };
					
				}
				return new VergelijkingMeerv(oplossingen);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}			
	}
	private static VergelijkingMeerv solveWithCAS(String evalCommand, String arg)
	{	
       	VergelijkingMeerv v = null;
       	String s = "";
       	
    	if(casEvalStrings.containsKey(evalCommand)) s = (String)casEvalStrings.get(evalCommand);
    	else
    	{	//System.out.println(evalCommand);
       	
	        try
	        {   //System.out.println(s);
	        	s = WiskOpdr.phrasebook.eval("InputForm[" + arg+"/."+"Solve[" + evalCommand + "," + arg + "]" + "]");
	            //System.out.println(s);
	        }
	        catch(Exception ex)
	        {ex.printStackTrace();}
	        //casEvalStrings.put(evalCommand, s);
    	}
    	
    	String[] oplossingen = StringUtils.split(s.substring(1,s.length()-2), ",");
    	
    	for(int i=0 ; i<oplossingen.length ; i++)
		{	s = oplossingen[i];
			s = s.replace('[','(');
			s = s.replace(']',')');
			s = StringUtils.replaceStr(s,"Pi","\u03C0");
			s = StringUtils.replaceStr(s,"E","e");
			s = StringUtils.replaceStr(s,"I","i");
			s = StringUtils.replaceStr(s,"Log","ln");
			s = StringUtils.replaceStr(s,"Sin","sin");
			s = StringUtils.replaceStr(s,"Cos","cos");
			s = StringUtils.replaceStr(s,"Tan","tan");
			s = StringUtils.replaceStr(s,"Arc","arc");
			s = StringUtils.replaceStr(s,"Sqrt","sqrt");
			oplossingen[i] = s;
			//System.out.println(oplossingen[i]);
		}
		Expressie[] es = new Expressie[oplossingen.length];
		
		Vergelijking[] vs = new Vergelijking[oplossingen.length];
		for(int i=0 ; i<es.length ; i++)
		{	
			es[i] = FormuleParser.parse(FormuleParser.schoon(FormuleParser.formuleString("$f"+oplossingen[i].trim()+"@")));
			//System.out.println(oplossingen[i]);
			//System.out.println(es[i].toString());
			vs[i] = new Vergelijking(new BasisExpressie(arg),es[i]);
		}
		v = new VergelijkingMeerv(vs);
		return v;
	}

	public Object visit(AbstractConverter converter) {
		return converter.expressie(this);
	}
	
	public Expressie vervangDifferentialen(String diffVar) {
		
		return null;
	}
	
	public Expressie vervangDiffs(Expressie subst, String var) {
		return null;
	}
	
}
