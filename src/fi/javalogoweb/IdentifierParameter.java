package fi.javalogoweb;

public class IdentifierParameter extends TAParameter
{

	/**
	 * An indentifier always starts with a default name, such as 'variabele' or 'deeltaak2'
	 * Because this name is supplied by the programmer of TA, we assume it to be correct :)
	 * 
	 * @param the default name of this parameter's owner
	 */
	public IdentifierParameter(String s)
	{
		setParameter(s);
	}

	@Override
	void setParameter(String s)
	{
    	if ( s == null ) s = "";
		parameterText = s.trim();	
		isCorrect = isIdentifier(parameterText);
	}
	
    /**
     * Check if string s is an identidier using the standard Unicode rules, supplied in class Character
     * 
     * @param s		string to be tested
     * @return		true, if correct
     */
    private boolean isIdentifier(String s)
    {
    	if ( s.equals("")) return false;
    	if ( !Character.isUnicodeIdentifierStart(s.charAt(0)) ) return false;
    	for ( int i = 1; i < s.length(); i++)
    	{
    		if ( !Character.isUnicodeIdentifierPart(s.charAt(i)) ) return false;
    	}
    	// s passed all tests (literally)
    	return true;
    }


	@Override
	String getParameterText()
	{
		return parameterText;
	}

	@Override
	boolean isCorrect(VarSet varSet)
	{
		// identifier is independent of VarSet
		return isCorrect;
	}

	@Override
	boolean isCorrect()
	{
		return isCorrect;
	}

	boolean isEmpty()
	{
		return ( parameterText.equals(""));
	}
}
