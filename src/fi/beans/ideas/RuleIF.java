// $Id: RuleIF.java 4118 2011-05-03 10:11:28Z velth101 $
package fi.beans.ideas;

import java.util.Map;

/**
 * Interface naar de state van IDEAS. Een rule bestaat uit een 
 * expressie, een identifier, een location en een context.
 * @author wim
 * @mock.generate
 */
public interface RuleIF {
	/**
	 * Constant voor een runtime-exception.
	 */
	public static final String EXCEPTION = "exception";
	/**
	 * Constant voor een IDEAS error
	 */
	public static final String ERROR = "error";
	/**
	 * Geef de <em>ruleId</em> van een afleiding.
	 * Mogelijke ids zijn {@link #EXCEPTION} en {@link #ERROR}.
	 * @return String
	 */
	public String getId();
	/**
	 * Geeft de expressie in fi.beans.mathparser format.
	 * @return String
	 */
	public String getExpr();
	/**
	 * Is deze rule een exceptie/error?
	 * @return boolean
	 */
	public boolean isException();

	public String getName();
	public boolean isReady();
	public Map getContext();
	public String getPrefix();
	public String getArgument();
	
}
