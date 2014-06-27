package fi.beans.ideas;

import java.util.Map;

public abstract class AbstractRule implements RuleIF {

	public String getExpr() {
		return null;
	}

	public String getId() {
		return null;
	}

	public boolean isException() {
		return false;
	}
	public boolean isReady() {
		return false;
	}
	public String getName() { 
		return null;
	}
	public Map getContext() {
		return null;
	}
	public String getPrefix() { 
		return "";
	}
	
	public String getArgument() {
		return null;
	}
	
}
