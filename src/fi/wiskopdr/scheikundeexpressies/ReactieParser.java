package fi.wiskopdr.scheikundeexpressies;

import java.util.Vector;

import fi.beans.stringutils.StringUtils;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.AantalSign;
import fi.wiskopdr.expressies.Abs;
import fi.wiskopdr.expressies.Aftrekking;
import fi.wiskopdr.expressies.ArcCosinus;
import fi.wiskopdr.expressies.ArcSinus;
import fi.wiskopdr.expressies.ArcTangens;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Bin;
import fi.wiskopdr.expressies.BinomCDF;
import fi.wiskopdr.expressies.BinomPDF;
import fi.wiskopdr.expressies.Conjug;
import fi.wiskopdr.expressies.Cosinus;
import fi.wiskopdr.expressies.DecRound;
import fi.wiskopdr.expressies.DecRoundStrict;
import fi.wiskopdr.expressies.Deling;
import fi.wiskopdr.expressies.Diff;
import fi.wiskopdr.expressies.DiffPartial;
import fi.wiskopdr.expressies.Differentiaal;
import fi.wiskopdr.expressies.E;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.Faculteit;
import fi.wiskopdr.expressies.GCD;
import fi.wiskopdr.expressies.Integraal;
import fi.wiskopdr.expressies.InvNorm;
import fi.wiskopdr.expressies.Limiet;
import fi.wiskopdr.expressies.Ln;
import fi.wiskopdr.expressies.Log;
import fi.wiskopdr.expressies.Macht;
import fi.wiskopdr.expressies.Max;
import fi.wiskopdr.expressies.Min;
import fi.wiskopdr.expressies.NdeLog;
import fi.wiskopdr.expressies.NdeWortel;
import fi.wiskopdr.expressies.NormalCDF;
import fi.wiskopdr.expressies.Optelling;
import fi.wiskopdr.expressies.PI;
import fi.wiskopdr.expressies.PoissonCDF;
import fi.wiskopdr.expressies.PoissonPDF;
import fi.wiskopdr.expressies.Primitieve;
import fi.wiskopdr.expressies.Prv;
import fi.wiskopdr.expressies.SigRound;
import fi.wiskopdr.expressies.SigRoundStandard;
import fi.wiskopdr.expressies.Sigma;
import fi.wiskopdr.expressies.Sinus;
import fi.wiskopdr.expressies.Tangens;
import fi.wiskopdr.expressies.Vermenigvuldiging;
import fi.wiskopdr.expressies.Wortel;
import fi.wiskopdr.formuleobjects.FormuleParser;


public class ReactieParser {

	public ReactieParser()
	{
		
	}
	
	public static ReactieVergelijking parseVergelijking(String s)
	{	try
		{
			s = s.substring(2,s.length()-1);
			if(s.length()==0)return null;
			
			String[] expressieStrings = StringUtils.split(s, "=");
			if(expressieStrings.length != 2)
				return null;
			ReactieExpressie e1 = parse(formuleString("$f" + expressieStrings[0] + "@"));
			ReactieExpressie e2 = parse(formuleString("$f" + expressieStrings[1] + "@"));
			if(e1 == null || e2 == null)
				return null;
			else
				return new ReactieVergelijking(e1, e2);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static ReactieExpressie parse(String s)
	{	ReactieExpressie exp = null;
		//verwijder overbodige haakjes
		try
		{
		boolean pelbaar = true;
		while(pelbaar)
		{	if(s.length()>0 && s.charAt(0)=='(' && s.charAt(s.length()-1)==')')
			{	pelbaar = true;
			}
			else pelbaar = false;
			if(pelbaar)
			{	int niv = 0;
				int minNiv = 0;
				for(int i=0 ; i<s.length()-1 ; i++)
				{	if(s.charAt(i)=='(')
					{	niv++;
						
					}
					else if(s.charAt(i)==')')
					{	niv--;
						if(niv<1)
						{	pelbaar = false;
							break;
						}
					}
				}
			}
			if(pelbaar)s = s.substring(1,s.length()-1);
		}
		if(s.length()>0 && s.charAt(0)=='-')s = '0' + s;
		if(s.length()>0 && s.charAt(0)=='+')s = s.substring(1);
		
		//maaltekens verwijderen
		for(int i = s.length() - 1; i > - 1; i--)
		{
			if(s.charAt(i) == '*')
				s = s.substring(0, i - 1) + s.substring(i + 1);
		}
		
		//Expressie splitsen in verschillende moleculen. 
		//Er kunnen plusjes in de ladingen zitten. Dus alleen plus op niveau 0 telt mee.
		int niv = 0;
		for(int i = 0; i < s.length(); i++)
		{
			if(s.charAt(i)==')')
			{	niv++;
			}
			else if(s.charAt(i)=='(')
			{	niv--;
			}
			if(s.charAt(i) == '$')
				niv++;
			else if(s.charAt(i) == '@')
				niv--;
			
			if(niv == 0 && s.charAt(i) == '+')
			{
				return new ReactieExpressie(parse(s.substring(0, i)), parse(s.substring(i + 1)));
			}
			
		}
		
		//Nu is het een enkele molecuulexpressie (combinatie van coëfficiënt en molecuul). 
		//bepalen wat het molecuul en het aantal moeten worden. 
		int startIndex = 0;
		for(int i = 0; i < s.length(); i++)
		{
			if(Character.isUpperCase(s.charAt(i)))
			{
				startIndex = i;
				break;
			}
		}
		String aantalString = s.substring(0, startIndex);
		String molecuulString = s.substring(startIndex);
		
		//aantal van het molecuul bepalen
		try
		{
			Double d = Double.valueOf(aantalString);
		}
		catch(NumberFormatException nfe)
		{
			aantalString = "1";
		}
		double aantal = Double.valueOf(aantalString);
		
		//molecuul zelf ontleden
		//Eerst de lading bepalen. Als het molecuul een lading heeft, zit er een macht in, anders niet. 
		int lading = 0;
		if(molecuulString.contains("^"))
		{
			//ladingstring is het deel achter het dakje. Daarom begint en eindigt de ladingstring met een haakje. 
			String ladingString = molecuulString.substring(molecuulString.indexOf('^') + 1);
			ladingString = ladingString.substring(1, ladingString.length() - 1);
			if(ladingString.endsWith("-"))
			{
				ladingString = ladingString.substring(0, ladingString.length() - 1);
				try{
					if(ladingString.length() > 0)
						lading = - Integer.parseInt(ladingString);
					else
						lading = -1;
				}
				catch(Exception e)
				{
					System.out.println("catch bij negatieve lading");
					return null;
				}
			}
			else if(ladingString.endsWith("+"))
			{
				ladingString = ladingString.substring(0, ladingString.length() - 1);
				try{
					if(ladingString.length() > 0)
						lading = Integer.parseInt(ladingString);
					else
						lading = 1;
				}
				catch(Exception e)
				{
					System.out.println("catch bij positieve lading");
					return null;
				}
			}
			else
				System.out.println("lading eindigt niet op + of -");
			molecuulString = molecuulString.substring(0, molecuulString.indexOf('^'));
		}
		
		
		//Vector<AtoomDeel> atoomDelen = new Vector<AtoomDeel>();
		int aantalAtoomDelen = 0;
		for(int i = 0; i < molecuulString.length(); i++)
		{
			if(Character.isUpperCase(molecuulString.charAt(i)))
			{
				aantalAtoomDelen++;
			}
		}
		String[] atoomDeelStrings = new String[aantalAtoomDelen];
		String deelString = "";
		int teller = 0;
		for(int i = 0; i < molecuulString.length(); i++)
		{
			if(Character.isUpperCase(molecuulString.charAt(i)))
			{
				if(!deelString.equals(""))
				{
					atoomDeelStrings[teller] = deelString;
					teller++;
				}
				deelString = "" + molecuulString.charAt(i);
			}
			else
				deelString = deelString + molecuulString.charAt(i);
		}
		atoomDeelStrings[teller] = deelString;
		
		AtoomDeel[] atoomdelen = new AtoomDeel[atoomDeelStrings.length];
		for(int i = 0; i < atoomDeelStrings.length; i++)
		{
			String atoomNaam = "";
			int aantalElementen = 1;
			int getalBeginIndex = atoomDeelStrings[i].length();
			for (int j = 0; j < atoomDeelStrings[i].length(); j++)
			{
				if(Character.isLetter(atoomDeelStrings[i].charAt(j)))
				{
					atoomNaam = atoomNaam + atoomDeelStrings[i].charAt(j);
				}
				else
				{	getalBeginIndex = j;
					break;
				}
			}
			if(getalBeginIndex < atoomDeelStrings[i].length())
			{
				try{
					aantalElementen = (int) Double.parseDouble(atoomDeelStrings[i].substring(getalBeginIndex + 2, atoomDeelStrings[i].length() - 1)); //"?(" voor getal en ")" na getal weglaten.
				}
				catch(NumberFormatException nfe)
				{
					return null;
				}
			}
			atoomdelen[i] = new AtoomDeel(atoomNaam, aantalElementen);
		}
		
		exp = new ReactieExpressie(new Molecuul(atoomdelen, lading), aantal);
	
		return exp;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static String formuleString(String s)
	{	s = "(" + s.substring(2,s.length()-1) + ")";
		
		int n = s.indexOf(" ");
		while(n>-1)
		{
			s = s.substring(0,n)+s.substring(n+1);
			n = s.indexOf(" ");
		}
		n = s.indexOf("$s");
        while(n>-1)
        {   s = s.substring(0,n) + "?(" + s.substring(n+2);
            n = s.indexOf("$s");
        }
        n = s.indexOf("$m");
		while(n>-1)
		{	s = s.substring(0,n) + "^(" + s.substring(n+2);
			n = s.indexOf("$m");
		}
		n = s.indexOf("@");
		while(n>-1)
		{	s = s.substring(0,n) + ")" + s.substring(n+1);
			n = s.indexOf("@");
		}
		n = s.indexOf("\u00B7");
		while(n>-1)
		{	s = s.substring(0,n) + "*" + s.substring(n+1);
			n = s.indexOf("\u00B7");
		}
		n = s.indexOf("\u00d7");
		while(n>-1)
		{	s = s.substring(0,n) + "*" + s.substring(n+1);
			n = s.indexOf("\u00d7");
		}
		return s;	
	}
	
}
