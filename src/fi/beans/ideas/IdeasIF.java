// $Id: IdeasIF.java 3735 2011-01-19 12:58:02Z wim $
package fi.beans.ideas;

/**
 * Interface naar de IDEAS exercise assistant.
 * Informatica heeft de IDEAS service draaien. Een implementatie geeft een 
 * interface daar naar toe. 
 * @author wim
 *
 */
public interface IdeasIF {

	/**
	 * Constant voor lineaire vergelijkingen.
	 */
	public static final String MATH_LINEQ = "math.lineq";
	/**
	 * Constant voor hogere machts vergelijkingen.
	 */
	public static final String MATH_HIGHERDEGREE = "math.higherdegree";
	/**
	 * Constant voor kwadratische vergelijkingen zonder abc-formule.
	 */
	public static final String MATH_QUADREQ_NO_ABC = "math.quadreq-no-abc";
	
	/**
	 * Constant voor kwadratische vergelijkingen met benaderingen.
	 */
	public static final String MATH_QUADREQ_WITH_APPROX = "math.quadreq-with-approx";
	/**
	 * Constant voor kwadratische vergelijkingen.
	 */
	public static final String MATH_QUADREQ = "math.quadreq";
	/**
	 * Constant voor 'bordjes' vergelijkingen.
	 */
	public static final String MATH_COVERUP = "math.coverup";
	/**
	 * Constant voor lineaire ongelijkheden .
	 */
	public static final String MATH_LININEQ = "math.linineq";
	/**
	 * Constant voor kwadratische ongelijkheden.
	 */
	public static final String MATH_QUADRINEQ = "math.quadrineq";
	/**
	 * Constant voor hogeregraads ongelijkheden.
	 */
	public static final String MATH_INEQHIGHERDEGREE = "math.ineqhigherdegree";
	/**
	 * Bepaal de afleiding van een expressie binnen de default strategie.
	 * @param expr een expressie string
	 * @return array met rules.
	 */
	public RuleIF[] getDerivation(String expr);
	/**
	 * Bepaal de afleiding van een expressie binnen de default strategie.
	 * @param expr een expressie ingepakt in een RuleIF
	 * @return array met rules.
	 */
	public RuleIF[] getDerivation(RuleIF expr);
	/**
	 * Bepaal de afleiding van een expressie binnen een strategie.
	 * @param expr een expressie string
	 * @return array met rules.
	 */
	public RuleIF[] getDerivation(String expr, String strategie);
	/**
	 * Bepaal de afleiding van een expressie binnen een strategie.
	 * @param rule een expressie string
	 * @param strategie MATH_LINEQ of MATH_HIGHERDEGREE
	 * @return array met rules.
	 */
	public RuleIF[] getDerivation(RuleIF rule, String strategie);
			

	
	
	/** 
	 * Bepaal de <em>allfirsts</em> van een expressie 
	 * 
	 * @param expr String
	 * @return array met rules.
	 */
	public RuleIF[] getAllFirsts(String expr);		
	/** 
	 * Bepaal de <em>allfirsts</em> van een expressie 
	 * 
	 * @param rule RuleIF
	 * @param strategie MATH_LINEQ of MATH_HIGHERDEGREE
	 * @return array met rules.
	 */
	public RuleIF[] getAllFirsts(RuleIF rule, String strategie);
	/** 
	 * Bepaal de <em>allfirsts</em> van een expressie 
	 * 
	 * @param rule RuleIF
	 * @return array met rules.
	 */
	public RuleIF[] getAllFirsts(RuleIF rule);
	/** 
	 * Bepaal de <em>allfirsts</em> van een expressie 
	 * 
	 * @param expr String
	 * @param strategie MATH_LINEQ of MATH_HIGHERDEGREE
	 * @return array met rules.
	 */
	public RuleIF[] getAllFirsts(String expr, String strategie);

	
	
	
	/** 
	 * Bepaal de <em>onefirst</em> van een expressie 
	 * 
	 * @param expr String
	 * @return een rule 
	 */
	public RuleIF   getOneFirst(String expr);
	/** 
	 * Bepaal de <em>onefirst</em> van een expressie 
	 * 
	 * @param expr RuleIF
	 * @return een rule 
	 */
	public RuleIF   getOneFirst(RuleIF expr);
	/** 
	 * Bepaal de <em>onefirst</em> van een expressie 
	 * 
	 * @param expr String
	 * @param strategie MATH_LINEQ of MATH_HIGHERDEGREE
	 * @return een rule 
	 */
	public RuleIF   getOneFirst(String expr, String strategie);
	/** 
	 * Bepaal de <em>onefirst</em> van een expressie 
	 * 
	 * @param expr RuleIF
	 * @param strategie MATH_LINEQ of MATH_HIGHERDEGREE
	 * @return een rule 
	 */
	public RuleIF   getOneFirst(RuleIF expr, String strategie);

	/**
	 * Zet de default strategie
	 * @param strategie
	 */
	public void setStrategie(String strategie);
	
	/**
	 * Geef de default strategie
	 * @return strategie
	 */
	public String getStrategie();

	/**
	 * Vind 'buggy rules'.
	 * @param expr laatste goede vergelijking
	 * @param input (foute) studenteninput 
	 * @return array met rules waarvan de ID de rule-id is.
	 */
	public RuleIF[] findBuggyRules(RuleIF expr, RuleIF input);
	/**
	 * Vind 'buggy rules'.
	 * @param expr
	 * @param input
	 * @param stategie
	 * @return
	 */
	public RuleIF[] findBuggyRules(RuleIF expr, RuleIF input, String stategie);

	public RuleIF diagnose(RuleIF expr, RuleIF input);
	public RuleIF diagnose(RuleIF expr, RuleIF input, String strategie);
	public RuleIF diagnose(String expr, String input);
	public RuleIF diagnose(String expr, String input, String strategie);
	
	public Exercise[] getExerciseList();
	
	public RuleIF[] getRuleList();
	public RuleIF[] getRuleList(String strategie);
	
	public RuleIF[] getRulesInfo();
	public RuleIF[] getRulesInfo(String strategie);
	
	public RuleIF[] getExamples();
	public RuleIF[] getExamples(String strategie);

	
	public String EVAL = "eval";
	public String NUMERIC = "numeric";
	public String SOLVE = "solve";
	/**
	 *  Interface to a CAS. Not quite IDEAS.
	 * @param how 	command	
	 * @param args	arguments
	 * @return result of command.
	 */
	public RuleIF interpret(String how, RuleIF[] args);
	
	/**
	 * Shortcut. Evaluate arg.
	 * @param arg
	 * @return result of evaluation
	 * @see #interpret(String, RuleIF[])
	 */
	public RuleIF interpret(String arg);
	/**
	 * Interface to a CAS. 
	 * @param how command
	 * @param arg Single string argument
	 * @return result of command
	 */
	public RuleIF interpret(String how, String arg);
}
