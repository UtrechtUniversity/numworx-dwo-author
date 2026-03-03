package fi.wiskopdr;

import java.util.*;

import fi.beans.stringutils.StringUtils;
import fi.wiskopdr.expressies.Expressie;

public class VariableCollection
{
	Vector<Variable> variables;
	
	public VariableCollection()
	{	variables = new Vector<>();
	}
	
	public boolean setVariables(String s)
	{	
		//bv a1 wordt a?(1)
		String sNieuw = "";
		for(int i=0 ; i<s.length()-1; i++)
	    {	char char1 = s.charAt(i);
	    	char char2 = s.charAt(i+1);
	    	if(Character.isLetter(char1) && Character.isDigit(char2)) 
	    	{	sNieuw = sNieuw+char1+"?("+char2+")";
	    		i++;
	    	}
	    	else if(i==s.length()-2)sNieuw = sNieuw+char1+char2;
	    	else sNieuw = sNieuw+char1;
	    	//System.out.println(sNieuw);
	    }
		s=sNieuw;
		
		StringTokenizer tokenizer = new StringTokenizer(s,";\n");
		while(tokenizer.hasMoreTokens())
	    {	String tok = tokenizer.nextToken();
			try
			{	int index = Math.max(tok.indexOf("="), tok.indexOf('~'));
				if(index>0)
				{	String name = tok.substring(0,index);
					if(Character.isLetter(name.charAt(0)))
					{	setVariable(tok);
					}
					else return true;
				}
				else return false;
			}
			catch(Exception e)
			{	return false ;
			}
		}
		return true;
	}	
	
	public void setVariable(String s)
	{	s = s.trim();
		int index = Math.max(s.indexOf("="),s.indexOf('~'));
		String name = s.substring(0,index);
		name = name.trim();
		//String[] nameParts = StringUtils.split(name, "_");
		//if(nameParts.length==2) name = nameParts[0] + "?(" + nameParts[1] + ")";
		
		for(int i=0 ; i<variables.size(); i++)
	    {	Variable v = (Variable)variables.elementAt(i);
	    	if(v.getName().equals(name))
	    	{	variables.removeElementAt(i);
	    		break;
	    	}
	    }
		String valueString = s.substring(index+1);
		valueString = valueString.trim();
		Variable var = new Variable(name);
		var.setDraw(s.charAt(index)=='~');
		var.setValues(valueString);
		variables.addElement(var);
	}
		
	public Variable[] getVariables()
	{	Variable[] vars = new Variable[variables.size()];
		for(int i=0 ; i<variables.size(); i++)
	    {	vars[i] = (Variable)variables.elementAt(i);
	    }
	    return vars;
	}
	
	public Variable getVariable(String name)
	{	Variable[] vars = getVariables();
		for(int i=0 ; i<vars.length; i++)
		{	if(vars[i].getName().equals(name))
			{	return vars[i];
			}
		}
		return null;
	}
	
	public String[] getVariableNames()
	{	String[] names = new String[variables.size()];
		for (int i = 0; i<variables.size(); i++) 
		{	names[i] = ((Variable)variables.elementAt(i)).getName();
		}
		return names;
	}
	
	
	
	public Hashtable<String, Integer> getRandomValues()
	{	Hashtable<String, Integer> h = new Hashtable<>();
		for (int i = 0; i<variables.size(); i++) 
		{	Variable var = (Variable)variables.elementAt(i);
			String varName = var.getName();
			int[] values = var.getValues();
			int randNr = (int)(Math.random()* values.length);
			int value = values[randNr];
			var.draw(value);
			for (int j = i+1; j<variables.size(); j++) 
			{	((Variable)variables.elementAt(j)).substitueer(value,varName);
		    }
			h.put(varName,new Integer(value));
		}
		return h;
	}
	
	public boolean checkBorders()
	{	boolean bordersOK = true;
		for(int i=0 ; i<variables.size(); i++)
		{	bordersOK = bordersOK && ((Variable)variables.elementAt(i)).checkBorders();
		}
		return bordersOK;
	}
}
