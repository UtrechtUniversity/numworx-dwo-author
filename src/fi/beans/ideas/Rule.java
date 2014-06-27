package fi.beans.ideas;

import java.util.HashMap;
import java.util.Map;

class Rule implements RuleIF {
	private String id, expr;
	private boolean exception;
	String name, argument; 
	boolean ready;
	Map context;
	String prefix = "";
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the ready
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the expr
	 */
	public String getExpr() {
		return expr;
	}


	/**
	 * @param id
	 * @param expr
	 */
	public Rule(String id, String expressie) {
		this.id = id;
		this.expr = expressie;
	}

	/**
	 * @param id
	 * @param expr
	 * @param exception
	 */
	public Rule(String id, String expr, boolean exception) {
		this.id = id;
		this.expr = expr;
		this.exception = exception;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expr == null) ? 0 : expr.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Rule other = (Rule) obj;
		if (expr == null) {
			if (other.expr != null)
				return false;
		} else if (!expr.equals(other.expr))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the exception
	 */
	public boolean isException() {
		return exception;
	}

	/**
	 * @return the context
	 */
	public Map getContext() {
		return context;
	}
	
	public String getPrefix() {
		return prefix ;
	}

	public String getArgument() {
		return argument;
	}
	
}
