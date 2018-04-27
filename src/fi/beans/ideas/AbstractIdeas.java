package fi.beans.ideas;
/**
 * Abstracte class. Superclass voor IdeasClient en Ideas
 * @see IdeasClient
 * @see fi.servlet.ideas.Ideas
 * @author wim
 *
 */
public abstract class AbstractIdeas implements IdeasIF {

	protected String strategie = MATH_LINEQ;

	final public RuleIF[] getDerivation(String expr) {
		return getDerivation(expr, strategie);
	}

	final public RuleIF[] getDerivation(RuleIF expr) {
		return getDerivation(expr, strategie);
	}

	final public RuleIF[] getDerivation(String expr, String strategie)
	{
		return getDerivation(toRuleIF(expr), strategie);
		
	}
	
	static private class R extends AbstractRule
	{
		private ThreadLocal<String> e = new ThreadLocal<String>();

		private R() {

		}
		private RuleIF setExpr(String expr)
		{
			e.set(expr);
			return this;
		}
		
		public String getExpr() {
			return (String) e.get();
		}		
	}
		
	static private R RULE = new R();
	static private R INPUT = new R();
	
	protected RuleIF toRuleIF(String expr) {
		return RULE.setExpr(expr);
	}
	protected RuleIF toInputRuleIF(String input) {
	    return INPUT.setExpr(input);
	}
	
	/**
	 * @return the strategie
	 */
	public String getStrategie() {
		return strategie;
	}

	/**
	 * @param strategie the strategie to set
	 */
	public void setStrategie(String strategie) {
		this.strategie = strategie;
	}

	public RuleIF[] getAllFirsts(RuleIF rule) {
		return getAllFirsts(rule, getStrategie());
	}

	public RuleIF[] getAllFirsts(String expr, String strategie) {
		return getAllFirsts(toRuleIF(expr), strategie);
	}

	public RuleIF[] getAllFirsts(String expr) {
		return getAllFirsts(expr, getStrategie());
	}

	public RuleIF getOneFirst(String expr) {
		return getOneFirst(toRuleIF(expr));
	}

	public RuleIF getOneFirst(RuleIF expr) {
		
		return getOneFirst(expr, strategie);
	}

	public RuleIF getOneFirst(String expr, String strategie) {
		return getOneFirst(toRuleIF(expr), strategie);
	}
	
	public RuleIF[] findBuggyRules(RuleIF vgl, RuleIF input) {
		return findBuggyRules(vgl, input, strategie);
	}

	public abstract RuleIF diagnose(RuleIF vgl, RuleIF input, String strategie); 
	
	public RuleIF diagnose(RuleIF vgl, RuleIF input) {
		return diagnose(vgl, input, strategie);
	}
	
	public RuleIF diagnose(String vgl, String input, String strategie) {
		return diagnose(toRuleIF(vgl), toInputRuleIF(input), strategie);
	}
	
	public RuleIF diagnose(String vgl, String input) {
		return diagnose(toRuleIF(vgl), toInputRuleIF(input), strategie);
	}
	
	public abstract RuleIF[] getRuleList(String strategie);
	public RuleIF[] getRuleList() {
		return getRuleList(strategie);
	}
	public abstract RuleIF[] getRulesInfo(String strategie);
	public RuleIF[] getRulesInfo() {
		return getRulesInfo(strategie);
	}
	
	public abstract RuleIF[] getExamples(String strategie);
	public RuleIF[] getExamples() {
		return getExamples(strategie);
	}
	
	public abstract RuleIF interpret(String how, RuleIF[] args);
	
	public RuleIF interpret(String arg) {
		return interpret(EVAL, new RuleIF[] { toRuleIF(arg) });
	}
	
	public RuleIF interpret(String how, String arg) 
	{
		return interpret(how, new RuleIF[] { toRuleIF(arg) });
	}
}
