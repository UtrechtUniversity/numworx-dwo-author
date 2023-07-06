package fi.javalogoweb;

import fi.javalogoweb.expressies.*;
import fi.javalogoweb.formuleobjects.*;

class BooleanParameter extends TAParameter
{
	private VergelijkingMeerv waarde;
	private boolean value;
	/**
	 * true if 'waarde' is a syntactically correct expression, given that all variables exist
	 */
	private boolean isValid;
	/**
	 * true if 'waarde' can be calculated, that is: all its variables exist and have valid numerical values, 
	 * no division by zero, etc.
	 * NOTE: When editing, this var is equal to isValid, because we don't know about the vars yet.
	 * At runtime, isCorrect may turn false, because there are missing vars. This will be used in executing (no)
	 * and painting the CC (it will turn red) 
	 */
	// private boolean isCorrect;  defined in superclass

	public BooleanParameter()
	{
		setParameter("0=00");
	}

	@Override
	void setParameter(String s)
	{
		parameterText = s.trim();
		isValid = true;				// will be set to false when parsing fails...
		isCorrect = true;			// we assume variables to be ok until the program runs
		try
		{
			waarde = FormuleParser.parseVergelijking("$f"+parameterText+"@");
		}
		catch(NumberFormatException ex)
		{	
			isValid = false;
			isCorrect = false;
		}
		if ( waarde == null )
		{
			isValid = false;
			isCorrect = false;
		}
	}

	@Override
	String getParameterText()
	{
		if ( isValid )
		{
			return waarde.toString();
		} else
		{
			return parameterText;
		}
	}

	@Override
	boolean isCorrect(VarSet varSet)
	{
		//System.out.println(parameterText+" ---- "+isValid+" C: "+isCorrect+" ==== "+waarde);
		if ( isValid )
		{
			// Peter, kan dit falen???
			VergelijkingMeerv w = varSet.getSubstEquation(waarde);
			value = w.isOplossing(new BasisExpressie(1.212131415),"q");		// Huhhh??? 1.212131415,q???
			isCorrect = true;
		}
		else 
		{
			isCorrect = false;
		}
		return isCorrect;
	}
		

	/**
	 * @return true if (1, edit-time) expression is well-formed (2, runtime) is computable with the current VarSet
	 */
	@Override
	boolean isCorrect()
	{
		return isCorrect;
	}
	
	/**
	 * Get the value. This is only guaranteed to yield a valid result if the parameter expression
	 * has been tested for correctness with the current Varset
	 * 
	 * @return the numeric value
	 */
	boolean getValue()
	{
		return value;
	}

}
