package fi.beans.ideas;

import java.applet.Applet;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class IdeasClient extends AbstractIdeas {

	class ExceptionRule extends AbstractRule {

		private Exception e;
		ExceptionRule(Exception e) {
			this.e = e;
		}

		public String getExpr() {
			return e.toString();
		}

		public String getId() {
			return EXCEPTION;
		}

		public boolean isException() {
			return true;
		}

	}
	
	public static String ID = "id";
	public static String EXPRESSION = "expr";
	static String NAME = "name";
	static String READY = "ready";
	static String CONTEXT = "context";
	static String PREFIX = "prefix";
	static String ARG = "argument";
	IdeasRPCIF stub;

		public static final String STUBS[] = {
			"/servlet/fi.servlet.ideas.IdeasServlet",
			"/servlet/IdeasDwoServlet",
			"/servlet/IdeasDwoTestServlet",
			"/servlet/IdeasServlet"
		};
	
		public static final int DEFAULT = 0; // is 2 op www/www-dev is 1 op dwo.fi.uu.nl
		public static final int IDEAS_DWO = 1;
		public static final int IDEAS_DWO_TEST = 2;
		public static final int IDEAS = 3;
		
		public static final String STUB = STUBS[DEFAULT];
		public static final String DOMAIN = "domain";
		public static final String DESCRIPTION = "description";
		public static final String STATUS = "status";
		private static final String IDEAS_SERVLET = "IDEAS";
		
		public IdeasClient(URL u) {
			stub = new IdeasRPCClient(u);
		}
		
		public IdeasClient(URL base, int version) throws MalformedURLException
		{
			this(new URL(base, STUBS[version]));
		}
		
		public IdeasClient(IdeasRPCIF stub)
		{
			this.stub = stub;
		}
		
		public IdeasClient(Applet applet)
		{
			this(applet, DEFAULT);
		}
		
		public IdeasClient(Applet applet, int version)
		{
			try {
				String spec = applet.getParameter(IDEAS_SERVLET);
				if( spec == null)
					spec = STUBS[version];
				URL  u = new URL(fix(applet.getCodeBase()), spec);
				stub = new IdeasRPCClient(u);
			} catch (MalformedURLException e) {
// Alleen 1.4.2: geen constructor, gebruik initcause.
				throw (IllegalArgumentException)new IllegalArgumentException().initCause(e);
			}			
		}
		private URL fix(URL codeBase) {
			if(codeBase == null || !codeBase.getProtocol().startsWith("http"))
				try {
					codeBase = new URL("http://ws.fisme.science.uu.nl/javaclasses/");
				} catch (MalformedURLException e) {
				}
			return codeBase;
		}

		public RuleIF[] getDerivation(RuleIF expr, String strategie)
		{
			try {
				Vector v = stub.getDerivation(toHashtable(expr), strategie);
				RuleIF[] result = importRules(v);
				return result;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		/**
		 * Utility Method. Converteer een RuleIF naar een Hashtable
		 * @param expr RuleIF
		 * @return a Hashtable
		 */
		static public Hashtable toHashtable(RuleIF expr) {
			Hashtable result = new Hashtable();
			if(expr == null)
				return result;
			put(result, EXPRESSION, expr.getExpr());
			put(result, ID, expr.getId());
			put(result, NAME, expr.getName());
			put(result, ARG, expr.getArgument());
			if(expr.isReady())
				put(result, READY, Boolean.TRUE);
			if(expr.isException())
				put(result, RuleIF.EXCEPTION, Boolean.TRUE);
			if(expr.getContext()!= null) {
				Map context = expr.getContext();
				if(!(context instanceof Hashtable))
				{
					context = new Hashtable(context);
				}
				put(result, CONTEXT, context);
			}
			if(!"".equals(expr.getPrefix()))
				put(result, PREFIX, expr.getPrefix());
			// more to follow....
			return result;
		}
		
		
		public static void put(Hashtable h, Object key, Object value) {
			if(value != null)
				h.put(key, value);
			
		}

		public static Vector toVector(RuleIF[] rules)
		{
			Vector result = new Vector();		
			if(rules != null)
				for (int i = 0; i < rules.length; i++) {
					RuleIF rule = rules[i];
					Hashtable map = toHashtable(rule);
					result.add(map);
				} 
			return result;
		}
		
		private RuleIF[] importRules(Vector v) {
			RuleIF[] result = new RuleIF[v.size()];
			for (int i = 0; i < result.length; i++) {
				Hashtable rule = (Hashtable) v.get(i);
				result[i] = toRuleIF(rule);
			}
			return result;
		}
		
		/**
		 * Utility method. Conversie naar een RuleIF
		 * @param rule Hashtable
		 * @return a Rule
		 */
		static public RuleIF toRuleIF(Hashtable rule) {
			String id = (String) rule.get(ID);
			String e = (String) rule.get(EXPRESSION);
			boolean b  = Boolean.TRUE.equals(rule.get(RuleIF.EXCEPTION));
			Rule r = new Rule(id,e, b);
			r.name = (String) rule.get(NAME);
			r.ready = Boolean.TRUE.equals(rule.get(READY));
			r.context = (Map) rule.get(CONTEXT);
			r.prefix = (String) rule.get(PREFIX);
			r.argument = (String) rule.get(ARG);
			if (r.prefix == null)
				r.prefix = "";
			return r;
		}
		
		public RuleIF[] getAllFirsts(RuleIF expr, String strategie) {
			try {
				Vector v = stub.getAllFirsts(toHashtable(expr), strategie);
				RuleIF[] result = importRules(v);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public RuleIF getOneFirst(RuleIF expr, String strategie) {
			try {
				return toRuleIF(stub.getOneFirst(toHashtable(expr), strategie));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}

		public RuleIF[] findBuggyRules(RuleIF expr, RuleIF input,
				String strategie) {
			try {
				Vector v = stub.findBuggyRules(toHashtable(expr), toHashtable(input), strategie);
				return importRules(v);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public RuleIF diagnose(RuleIF expr, RuleIF input, String strategie) {
			try {
				return toRuleIF(stub.diagnose(toHashtable(expr), toHashtable(input), strategie));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public Exercise[] getExerciseList() {
			try {
				return toExerciseList(stub.getExerciseList());
			} catch(Exception e)
			{
				return null;
			}
		}

		private Exercise[] toExerciseList(Vector exerciseList) {
			Exercise[] result = new Exercise[exerciseList.size()];
			Enumeration e = exerciseList.elements();
			int i=0;
			while (e.hasMoreElements()) {
				Hashtable h = (Hashtable) e.nextElement();
				result[i++] = new Exercise((String)h.get(DOMAIN),
										 (String)h.get(ID),
										 (String)h.get(DESCRIPTION),
										 (String)h.get(STATUS));
				
			}
			return result;
		}

		public RuleIF[] getRuleList(String strategie) {
			Vector v;
			try {
				v = stub.getRuleList(strategie);
				return importRules(v);
			} catch (Exception e) {
				e.printStackTrace();
				return new RuleIF[] { new ExceptionRule(e) };
			}
		}
// TODO 
		public RuleIF[] getRulesInfo(String strategie) {
			Vector v;
			try {
				v = stub.getRuleInfo(strategie);
				return importRules(v);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public RuleIF[] getExamples(String strategie) {
			Vector v;
			try {
				v = stub.getExamples(strategie);
				return importRules(v);
			} catch (Exception e) {
				e.printStackTrace();
				return new RuleIF[] { new ExceptionRule(e) };
			}
		}
		
		public RuleIF interpret(String how, RuleIF[] args)
		{
			Hashtable result;
			try {
				result = stub.interpret(how, toVector(args));
				return toRuleIF(result);
			} catch (Exception e)
			{
				return new ExceptionRule(e);
			}
			
		}
}
