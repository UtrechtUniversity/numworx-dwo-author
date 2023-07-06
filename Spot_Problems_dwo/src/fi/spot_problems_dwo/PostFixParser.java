package fi.spot_problems_dwo;

public class PostFixParser
{	// applet owning the parser
    Spot_Problems_dwo parent;
    // applet's language table
    LookUpTable table;
    // string to be parsed
    // string for progress
	String s, t;
	// tracking parenthesis
	int parOpen;
	// looking ahead for inserting *
	static int NUMBER = 0;
    static int VARIABLE = 1;
    static int TERM = 2;
    int lastTerm = 0;
    boolean multInserted = false;
    // constructor
    public PostFixParser(Spot_Problems_dwo p)
    {   parent = p;
        table = parent.languageTable;
    }
    // start parsing the string input
	public String parseString(String input)
	{   // no parentheses yet
	    parOpen = 0;
	    // replace all , by .
		StringBuffer tmp = new StringBuffer(input);
		for(int i = 0; i < tmp.length(); i++)
		{	if (tmp.charAt(i) == ',')
		        tmp.setCharAt(i, '.');
		}
		// whole string
		s = tmp.toString();
		// nothing done yet
		t = "";
		// skip leading blanks
		skipspaces();
		// initialize as null
		String result = null;
		// check for no input
		if (s.length() == 0)
		{	parent.parserLabel.setText(
		        table.lookUp("nothingText"));
		    return result;
		}
        // get parsed result if any
		result = inString();
// not necessary?
// in case of errors string is never finished
		// check if string was finished
		skipspaces();
	 	if (s.length() > 0)
	 	{	// parent.errorLabel.setText(
	 	    //     Table.lookUp("leftParText"));
	 	    result = null;
		}
	 	return result;
	} 	// parseString

// Syntax directed programming: 
// INSTRING ==	TERM |
//				TERM {[+|-] TERM}
	private String inString()
    {	String term = getTerm();
        if (term == null)
        {   // error already generated
            return term;
        }    
		while (true)
		{	// delete leading blanks
		    skipspaces();
			if (s.length() == 0)
			{   // nothing left, no error!
			    return term;
			}    
			Character ch = nextChar();
            // check for unknown symbols
            if (!(isAllowed(ch.charValue())))
            {   String temp = "";
   			    if (s.length() > 1)
   			        temp = s.substring(1);
   			    parent.parserLabel.setText(
    		        table.lookUp("unknownText") + t + " ? " + ch.charValue() + " ? " + temp);		    			    
                return null;    
            }    
            // there must follow operator or )
            if (!(isOperator(ch.charValue())))
            {   parent.parserLabel.setText(
                    table.lookUp("opExpectedText") + t + " ? " + s);
                return null;    
            }    
            // too many ))
            if ((ch.charValue() == ')') && (parOpen == 0))
            {   parent.parserLabel.setText(
                    table.lookUp("rightTooMuchText") + t + " ? " + s);
                return null;    
            }    
			// here the operator must be + or -
			if ((ch.charValue() != '+') && (ch.charValue() != '-'))
			{   // return result 
			    return term;
			}
			//  now ch is  + or -
			consumeChar();
			String nextTerm = getTerm();
			// error in nextTerm
			if (nextTerm == null)
			{   // error message generated elsewhere
			    return null;
            }			    
			if (ch.charValue() == '+')
			{	term = term + nextTerm + "[+]";
			} else 	// ch.charValue() == '-'
			{	term = term + nextTerm + "[-]";
			}
		} // end of while
	} 	// inString



// with ^:
// TERM == SUBFACTOR |
//		   SUBFACTOR {[*|/] SUBFACTOR}
	private String getTerm()
    {	        
        // find the first factor
		String subFactor = getSubFactor();
        if (subFactor == null)
        {   // no error message
            return subFactor;
        }    		
		while (true)
		{	// delete leading blanks
		    skipspaces();
		    // exit if nothing left
			if (s.length() == 0)
			{	// nothing left, no error! 
                return subFactor;			    
			}
    		// find next character
			Character ch = nextChar();
			// check for illegal characters
            if (!(isAllowed(ch.charValue())))
            {   String temp = "";
   			    if (s.length() > 1)
   			        temp = s.substring(1);
   			    parent.parserLabel.setText(
    		        table.lookUp("unknownText") + t + " ? " + ch.charValue() + " ? " + temp);		    			    
                return null;    
            }    
            
            // there must follow an operator or )
            if (!(isOperator(ch.charValue())))
            {   parent.parserLabel.setText(
                    table.lookUp("opExpectedText") + t + " ? " + s);
                return null;    
            }    
            
            // too many ))
            if ((ch.charValue() == ')') && (parOpen == 0))
            {   parent.parserLabel.setText(
                    table.lookUp("rightTooMuchText") + t + " ? " + s);
                return null;    
            }    
            
			// here the operator must be / or *
			if ((ch.charValue() != '/') && 
			    (ch.charValue() != '*') )
			{ 	// return result
			    return subFactor;
			}
			// ch is * or /
      	    consumeChar();
			String nextSubFactor = getSubFactor();
			// error in nextSubFactor
			if (nextSubFactor == null)
			{   
			    return null;
			}    
			if ((ch.charValue() == '*'))
			{	subFactor = subFactor + nextSubFactor + "[*]";
			    multInserted = false;
			}
			else // (ch.charValue() == '/')
			{	subFactor = subFactor + nextSubFactor + "[/]";
			}
		} // end of while
	} // getTerm




// SUBFACTOR == FACTOR |
//              FACTOR (^) FACTOR
	private String getSubFactor()
    {	// find the first factor
		String factor = getFactor();
        if (factor == null)
        {   // error message
            return factor;
        }    
		while (true)
		{	// delete leading blanks
		    skipspaces();
		    // exit if nothing left
			if (s.length() == 0)
			{	// nothing left, no error! 
			    return factor;
			}
    		// find next character
			Character ch = nextChar();
            // check for illegal characters
            if (!(isAllowed(ch.charValue())))
            {   String temp = "";
   			    if (s.length() > 1)
   			        temp = s.substring(1);
   			    parent.parserLabel.setText(
    		        table.lookUp("unknownText") + t + " ? " + ch.charValue() + " ? " + temp);		    			    
                return null;    
            }    
            // check for inserting * at this level!
            if ((lastTerm == NUMBER) &&
                ((ch.charValue() == 'n') ||
                 (ch.charValue() == 'N') ||
                 (ch.charValue() == '(')
                )
               )
            {   s = "*" + s;
                ch = nextChar();
            }
            if ((lastTerm == VARIABLE) &&
                (ch.charValue() == '(')
               )
            {   s = "*" + s;
                ch = nextChar();
            }
            if ((lastTerm == TERM) &&
                ((ch.charValue() == '(') ||
                 (ch.charValue() == 'n') ||
                 (ch.charValue() == 'N') 
                ) 
               )
            {   s = "*" + s;
                ch = nextChar();
            }
                
            
//hier aanvullen     
                
            // there must follow operator or )            
            if (!(isOperator(ch.charValue()))
               )
            {   parent.parserLabel.setText(
                    table.lookUp("opExpectedText") + t + " ? " + s);
                return null;    
            }    
            
            // too many ))
            if ((ch.charValue() == ')') && (parOpen == 0))
            {   parent.parserLabel.setText(
                    table.lookUp("rightTooMuchText") + t + " ? " + s);
                return null;    
            }    
            
			// here operator must be ^
			if (ch.charValue() != '^')
			{ 	// return result
			    return factor;
			}
			// ch is  ^
			consumeChar();
			String nextFactor = getFactor();
			// error in nextFactor
			if (nextFactor == null)
			{   
			    return null;
			}    
			factor = factor + nextFactor + "[^]";
		} // end of while
	} // getSubFactor





// Syntax directed programming: expressie (inString) bestaat uit termen
// FACTOR == POSGETAL |
//			 -POSGETAL |
//           n |
//           - n |   
//           N |
//           - N |   
//			 (INSTRING) |
//			 -(INSTRING)
	private String getFactor()
	{	boolean negatief = false;
		Character ch;
		String result = null;
		// skip leading blanks
		skipspaces();
		// read first character
		ch = nextChar();
		if (ch == null)
		{   
			    parent.parserLabel.setText(
			        table.lookUp("missingText") + t + " ? " + s);
		    return result;
		}    
		// check for - sign
    	if ( ch.charValue() == '-' )
    	{	// delete minus sign
		    consumeChar();
	        // set flagg
			negatief = true;
			skipspaces();
			// read first character again
			ch = nextChar();
			if (ch == null)
    		{   // situation *-	
    		    return result; 
    		}    
		}
		
    	// now check for digit or .
		if ( isCijfer(ch.charValue()) )
		{	// nodig??
		    skipspaces();
		    // this consumes number
		    result = posgetal();
		    if (result == null)
		    {   //error message generated by posgetal()
		        return result;
    		}
    		result = "[" + result + "]";
    		// remember
    		lastTerm = NUMBER;
    	}
    	else if (ch.charValue() == 'N')
    	{   skipspaces();
    	    result = "[n]";
    	    consumeChar();
    	    // remember
    	    lastTerm = VARIABLE;
    	}    
    	else if (ch.charValue() == 'n')
    	{   skipspaces();
    	    result = "[n]";
    	    consumeChar();
    	    // remember
    	    lastTerm = VARIABLE;
    	}    
		else // there must follow (
 
		{	
// overbodig????		    
//		    skipspaces();
//			ch = nextChar();
			if ( ch.charValue() != '(' )
			{	if (isOperator(ch.charValue()))
    			{       			    
    			    parent.parserLabel.setText(
	    		        table.lookUp("missingText") + t + " ? " + s);		    			    
	    		}        
                else			    
    			{   
    			    String temp = "";
    			    if (s.length() > 1)
    			        temp = s.substring(1);
    			    parent.parserLabel.setText(
	    		        table.lookUp("unknownText") + t + " ? " + ch.charValue() + " ? " + temp);		    			    
	    		}        

			    
			    return null;
			}
            // consume (
			consumeChar();
			parOpen++;
			// consume the expression after (
			result = inString();
			if (result == null)
			{   return null;
			}
			skipspaces();
			// next we must have )
			ch = nextChar();
			if (ch == null)
			{   parent.parserLabel.setText(
			        table.lookUp("rightParText") + t + " ? " + s);
			    return null;
			}    
			if (ch.charValue() != ')')
			{	parent.parserLabel.setText(
			        table.lookUp("rightParText") + t + " ? " + s);
				return null;
			}
			// consume )
			consumeChar();
			parOpen--;
			// remember
			lastTerm = TERM;
		} // ()
		if (negatief == true)
		{   double d = -1;
			result = result + "[" + d + "][*]";
		}	
		return result;	
	} 	// factor

	private String posgetal()
	{	String result = null;
	    String tmpString = "";
	    int i;
/*
// check, nodig???
		if ( isCijfer(s.charAt(0)) == false)
		{	parent.errorLabel.setText(Table.lookUp("numberErrorText") + t + " ? " + s);
		    return result;
		}
*/		
		// maak een substring die het volgende getal bevat
		for(i = 0; ( (i < s.length()) &&
		             (isCijfer(s.charAt(i))) ); i++);
/*
// check, nodig???
		if (i == 0)
		{	parent.errorLabel.setText(Table.lookUp("numberErrorText") + t + " ? " + s);            
            return result;
		}
*/		
		Double d;
		try
		{	tmpString = s.substring(0, i );
			// this could generate NumberFormatException
			d = Double.valueOf(tmpString);
			// if not, reconvert to string
			result = "" + d.doubleValue();
		}
		catch (NumberFormatException e) 
		{   // else error message
		    parent.parserLabel.setText(table.lookUp("numberErrorText") + t + " ? " + s);
		}
        // check for double ..
        int k = 0;
        int j = tmpString.indexOf('.');
        // only if there is a .
        if (j >= 0)
        {   k = tmpString.lastIndexOf('.');
            if (j != k)
            {
                // error message
    		    parent.parserLabel.setText(table.lookUp("numberErrorText") + t + " ? " + s);                
    		    return null;
            }
        }
        // consume number		
        t = t + tmpString;
	    s = s.substring(i);
		return result;
	} 	// posgetal

    // skip any block of PREFIX spaces in inputstring
	private void skipspaces()
	{	boolean notready = true;
		while ( notready )
		{	if ( s.length() == 0 )
			{	notready = false;
			}
			else
			{	if ( s.charAt(0) == ' ' )
					s = s.substring(1);
				else
					notready = false;
			}
		}
	} 	// skipspaces

    // true if ch is a digit or a '.'
	private boolean isCijfer(char ch)
	{	return ((ch == '1') ||
				(ch == '2') ||
				(ch == '3') ||
				(ch == '4') ||
				(ch == '5') ||
				(ch == '6') ||
				(ch == '7') ||
				(ch == '8') ||
				(ch == '9') ||
				(ch == '0') ||
				(ch == '.')
			   );
	} 	// isCijfer
    
    private boolean isAllowed(char ch)
    {   return ((ch == 'n') ||
				(ch == 'N') ||
				(ch == '(') ||
				isCijfer(ch) ||
				isOperator(ch));
    }    
    
    // return next character wrapped!, null if there are none left
	private Character nextChar()
	{	Character result = null;
	    if (s.length() == 0)
		{	
		    parent.parserLabel.setText(
			        table.lookUp("missingText") + t + " ? " + s);		    
		}
		else 
		    result = new Character(s.charAt(0));
		return result;
	}


	private boolean isOperator(char ch)
	{   return ((ch == '+') ||
    			(ch == '-') ||
			    (ch == '*') ||
			    (ch == '/') ||
			    (ch == '^') ||
//			    (ch == '(') ||
			    (ch == ')'));
	} 	//isOperator


	private void consumeChar()
	{	t = t + s.charAt(0);
	    s = s.substring(1);
	}	// consumeChar

} // class PostFixParser
