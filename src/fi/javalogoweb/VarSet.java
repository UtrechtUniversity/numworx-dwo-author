package fi.javalogoweb;

import java.util.*;
import fi.javalogoweb.expressies.*;

public class VarSet 
{
	private Hashtable variabelen;
	private Vector varNamen;
	
	public VarSet()
	{	variabelen = new Hashtable();
		varNamen = new Vector();
	}
	
	public void setVar(String varName, Expressie e)
	{	double value = getExpressionValue(e);
		if(!Double.isNaN(value) && !variabelen.containsKey(varName))
		{	varNamen.addElement(varName);
		}
		variabelen.put(varName, new Double(value));
	}
	
	public double getValue(String varName)
	{	return ((Double)variabelen.get(varName)).doubleValue();
	}
	
	public double getExpressionValue(Expressie e)
	{	for(int i=0 ; i<varNamen.size(); i++)
		{	double d = ((Double)variabelen.get((String)varNamen.elementAt(i))).doubleValue();
			System.out.println(""+d);
			System.out.println(e.toString());
			e = e.substitueer(d,(String)varNamen.elementAt(i));
			System.out.println(e.toString());
			
		}
		return e.geefWaarde();
	}

}
