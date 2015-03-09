package fi.wiskopdr.expressies;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.expressies.repr.AbstractConverter;

public class FunctieDefSet  
{	
	public HashMap<String,Expressie> functieExpressies = new HashMap<String,Expressie>();
	public HashMap<String,String> functieVariabelen = new HashMap<String,String>();
	public HashMap<String,String> functieNamenSubst = new HashMap<String,String>();
	
	public void addFunctieExpressie(String functieString)
	{
		String[] functieDelen = functieString.split("=");
		Expressie functieExpressie = FormuleParser.geefExpressie("$f"+functieDelen[1]);
		String functieNaam = functieDelen[0].substring(0, functieDelen[1].indexOf('('));
		String functieVariabele = functieDelen[0].substring(functieDelen[1].indexOf('(')+1, functieDelen[1].indexOf('(')+2);
		addFunctieExpressie(functieNaam, functieVariabele, functieExpressie);
	}
	
	public void addFunctieExpressie(String functieNaam, String functieVariabele, Expressie functieExpressie)
	{	functieExpressies.put(functieNaam, functieExpressie);
		functieExpressies.put(functieNaam+"'", functieExpressie.geefDiff(new BasisExpressie(functieVariabele)));
		functieVariabelen.put(functieNaam, functieVariabele);
		functieVariabelen.put(functieNaam+"'", functieVariabele);
		
		String fn = functieNaam;
		String fnn = "";
		for(int j=0 ; j<fn.length() ; j++)
		{	fnn = fnn + fn.charAt(j);
			fnn = fnn + '*';
		}
		functieNamenSubst.put(functieNaam, fnn);
		functieNamenSubst.put(functieNaam+"'", fnn+"'*");
		//System.out.println("**"+functieNamenSubst.toString());
	}
	
	public void removeFunctieExpressie(String functieNaam)
	{	functieExpressies.remove(functieNaam);
		functieVariabelen.remove(functieNaam);
		functieNamenSubst.remove(functieNaam);
	}
	
	public void removeAll()
	{	functieExpressies.clear();
		functieVariabelen.clear();
		functieNamenSubst.clear();
	}
	
	public String[] geefFunctieNamen()
	{	return functieExpressies.keySet().toArray(new String[0]);
	}
	
	public String[] geefFunctieNamenSubst()
	{	return functieNamenSubst.values().toArray(new String[0]);
	}
	
	public String geefFunctieVariabele(String functieNaam)
	{	if(functieNaam!=null && functieVariabelen.containsKey(functieNaam))
			return functieVariabelen.get(functieNaam);
		return null;
	}
	
	public Expressie geefFunctieExpressie(String functieNaam)
	{	if(functieNaam!=null && functieExpressies.containsKey(functieNaam))
			return functieExpressies.get(functieNaam);
		return null;
	}
	
	
}
