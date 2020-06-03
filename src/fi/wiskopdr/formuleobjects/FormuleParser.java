package fi.wiskopdr.formuleobjects;

import java.util.ArrayList;
import java.util.Hashtable;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.WiskOpdr;
import fi.beans.stringutils.*;

public class FormuleParser
{		
	private static boolean woordFormule = false;
	private static boolean tweeHoofdletterVariabele = false;
	private static boolean significantie = false;
	private static boolean diffOperatoren = false;
	
	public FormuleParser()
	{	
	}
	
	public static void zetWoordFormule(boolean b)
	{
		woordFormule = b;
	}
	
	public static void zetTweeHoofdletterVariabele(boolean b)
    {
	    tweeHoofdletterVariabele = b;
    }
	
	public static void zetSignificantie(boolean b)
    {
	    significantie = b;
    }
	
	public static void zetDiffOperatoren(boolean b)
	{
		diffOperatoren = b;
	}
	
	public static boolean isWoordFormule()
	{
		return woordFormule;
	}
	
	public static boolean isTweeHoofdletterVariabele()
    {
        return tweeHoofdletterVariabele;
    }
	
	public static boolean isDiffOperatoren()
	{
		return diffOperatoren;
	}
	
	public static VergelijkingMeerv parseVergelijking(String s)
	{
		return parseVergelijking(s, null);
	}
	
	//teveel variabelen (>7) laat parser vastlopen. Vaak wordt geen expressie bedoeld
	public static boolean expressieVerdacht(String exprString)
	{
	    boolean verdacht = false;
	    int varTeller = 0;
	    for(int i=0 ; i < exprString.length() ; i++)
	    {
	        char c = exprString.charAt(i);
	        if(Character.isLetter(c))
	        {   varTeller++;
    	        for(int j=0 ; j<i ; j++)
    	        {
    	            if(exprString.charAt(j)==c)
    	               varTeller--;
    	            break;
    	        } 
	        }
	        if(varTeller > 20)
            {
                verdacht = true;
                break;
            }
	        System.out.println("tellerVar: "+varTeller);
	    }
	    return verdacht;
	}
	
	public static VergelijkingMeerv parseVergelijking(String s, FunctieMVDefSet fds) //FunctieDefSet
	{	//Functie.setFunctieDefSet(fds);
		FunctieMV.setFunctieMVDefSet(fds);
		try
		{
			s = s.substring(2,s.length()-1);
			if(s.length()==0)return null;
			String[] vergelijkingStrings = StringUtils.split(s,WiskOpdr.rb.getString("ofLabel"));
			Vergelijking[] vergelijkingen = new Vergelijking[vergelijkingStrings.length];
			
			for(int j=0 ; j<vergelijkingStrings.length; j++) 
			{	int index1 = vergelijkingStrings[j].indexOf("[");
		    	int index2 = vergelijkingStrings[j].indexOf("]");
				if(index1>-1 && index2>index1)
				{	
					String[] expressieStrings = StringUtils.split(vergelijkingStrings[j].substring(index1+1,index2),":");
					if(expressieStrings.length==3)
					{	Expressie e1 = parse(schoon(formuleString("$f" + expressieStrings[0] + "@")));
			    		Expressie e2 = parse(schoon(formuleString("$f" + expressieStrings[1] + "@")));
			    		Expressie e3 = parse(schoon(formuleString("$f" + expressieStrings[2] + "@")));
			    		if(e1!=null && e2!=null && e3!=null && e1.isWaarde() && e2.isWaarde() && e3.isWaarde())
				    	{	vergelijkingen[j] = new Vergelijking(new BasisExpressie("Q?(Q)"),new Vermenigvuldiging(e1,new Optelling(e2,e3)), "=");
				    		//System.out.println(vergelijkingen[j].toString());
				    	}
					}
					
				}
			}
			
			String[][] tekenParen = {{"<","<"},{"<","\u2264"},{"\u2264","<"},{"\u2264","\u2264"},{">",">"},{"\u2265",">"},{">","\u2265"},{"\u2265","\u2265"}};
			Vergelijking ongDubbel = null;
			boolean[] splitOngDubbel = new boolean[vergelijkingStrings.length];
	    	for(int j=0 ; j<vergelijkingStrings.length ; j++) 
			{	for(int i = 0; i < 8 && vergelijkingen[j]==null; i++)
			    { 	int index1 = vergelijkingStrings[j].indexOf(tekenParen[i][0]);
			    	int index2 = vergelijkingStrings[j].indexOf(tekenParen[i][1],index1+1);
					if(index1>0 && index2>0)
					{	String s1 = vergelijkingStrings[j].substring(0,index1);
						String s2 = vergelijkingStrings[j].substring(index1+1,index2);
						String s3 = vergelijkingStrings[j].substring(index2+1);
						Expressie e1 = parse(schoon(formuleString("$f" + s1 + "@")));
			    		Expressie e2 = parse(schoon(formuleString("$f" + s2 + "@")));
			    		Expressie e3 = parse(schoon(formuleString("$f" + s3 + "@")));
				    	if(e1!=null && e2!=null && e3!=null && e1.isWaarde() && e2.isVar() && e3.isWaarde())
				    	{	vergelijkingen[j] = new Vergelijking(e2,new Vermenigvuldiging(new BasisExpressie(i),new Optelling(e1,e3)), "~");
				    		//System.out.println(vergelijkingen[j].toString());
				    	}
					}
			    }
			}
			
			String[] vergTekens = {"=", ">", "<", "\u2264", "\u2265","\u2248"};
			
			for(int i=0 ; i<vergelijkingStrings.length; i++)
		    {	boolean split = false;
			    for(int j=0 ; j<vergTekens.length && !split  && vergelijkingen[i]==null; j++)
			    {	String[] expressieStrings  = StringUtils.split(vergelijkingStrings[i],vergTekens[j]);
			    	if(expressieStrings.length==2)
			    	{	//if(expressieStrings[1].trim().equals(WiskOpdr.rb.getString("antwoordModelGeen"))) expressieStrings[1] = "0.1234567";
				    	if(expressieStrings[1].trim().equals("geen") || expressieStrings[1].trim().equals("none")) expressieStrings[1] = "0.1234567";
				    	if(expressieStrings[1].trim().equals("alles") || expressieStrings[1].trim().equals("all")) expressieStrings[1] = "0.7654321";
			    		Expressie e1 = null;
			    		Expressie e2 = null;
			    		String[] eindoplStrings  = StringUtils.split(expressieStrings[1],"::");
			    		int aantalEO = eindoplStrings.length;
			    		if(aantalEO>1)
			    		{ 	Expressie[] eindoplossingen = new Expressie[aantalEO];
			    			for(int k=0 ; k<aantalEO ; k++)
						    {	eindoplossingen[k] = parse(schoon(formuleString("$f" + eindoplStrings[k] + "@")));
						    	if(eindoplossingen[k]==null)
						    	{	eindoplossingen=null;
						    		break;
						    	}
						    }
			    			e1 = parse(schoon(formuleString("$f" + expressieStrings[0] + "@")));
			    			
			    			if(e1==null || eindoplossingen==null) 
					    	{	split = false;
					    	}
					    	else 
					    	{	split = true;
					    		vergelijkingen[i] = new Vergelijking(e1,eindoplossingen, vergTekens[j]);
					    	}
			    		}
			    		else
			    		{
				    		e1 = parse(schoon(formuleString("$f" + expressieStrings[0] + "@")));
					    	e2 = parse(schoon(formuleString("$f" + expressieStrings[1] + "@")));
					    	//System.out.println(e2.toString());
			    		
					    	if(e1==null || e2==null) 
					    	{	split = false;
					    	}
					    	else 
					    	{	split = true;
					    		vergelijkingen[i] = new Vergelijking(e1,e2, vergTekens[j]);
					    	}
			    		}
			    	}
			    }
			    if(!split && vergelijkingen[i]==null)return null;
			}
			//Functie.setFunctieDefSet(null);
			FunctieMV.setFunctieMVDefSet(null);
			return new VergelijkingMeerv(vergelijkingen); 
			
		}
		catch(Exception e)
		{	//Functie.setFunctieDefSet(null);
			FunctieMV.setFunctieMVDefSet(null);
			return null;
			
		}
	}
	

	public static String formuleString(String s)
	{
		String in = s;
		
		s = "(" + s.substring(2,s.length()-1) + ")";
		
		int n = s.indexOf("$n");
		while (n>-1)
		{
			int index$ = n - 1;
			int niv = 0;
			while (!(s.charAt(index$) == '$' && niv == 0))
			{
				if (s.charAt(index$) == '$')
					niv--;
				else if (s.charAt(index$) == '@')
					niv++;
				index$--;
			}
			int indexAt = n + 2;
			niv = 0;
			while (!(s.charAt(indexAt) == '@' && niv == 0))
			{
				if (s.charAt(indexAt) == '$')
					niv++;
				else if (s.charAt(indexAt) == '@')
					niv--;
				indexAt++;
			}
			int k = s.indexOf("$k");
			int indexAtk = k + 2;
			if (k > n && k < indexAt)
			{
				niv = 0;
				while (!(s.charAt(indexAtk) == '@' && niv == 0))
				{
					if (s.charAt(indexAtk) == '$')
						niv++;
					else if (s.charAt(indexAtk) == '@')
						niv--;
					indexAtk++;
				}
			}

			int l = s.indexOf("$l");
			int indexAtl = l + 2;
			if (l > k && l < indexAtk)
			{
				indexAtl = l + 2;
				niv = 0;
				while (!(s.charAt(indexAtl) == '@' && niv == 0))
				{
					if (s.charAt(indexAtl) == '$')
						niv++;
					else if (s.charAt(indexAtl) == '@')
						niv--;
					indexAtl++;
				}
			}
			
			if (s.charAt(index$+1)=='b')
			{
				if (Character.isDigit(s.charAt(index$-1))||Character.isWhitespace(s.charAt(index$-1)))
				{ // special case getal$b .. @ -> (getal + $b ... )@
					int digit = index$-1;while(Character.isDigit(s.charAt(digit-1))||Character.isWhitespace(s.charAt(digit-1))) digit--;
					s = s.substring(0,digit) + "(" + s.substring(digit, n) + ")/(" + s.substring(n+2,indexAt) + "))" + s.substring(indexAt+1);
				}
				else
					s = s.substring(0,n) + ")/(" + s.substring(n+2,indexAt) + ")" + s.substring(indexAt+1);
			}
			else if (s.charAt(index$ + 1) == 'o')
				s = s.substring(0, n) + ")+(" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'a')
				s = s.substring(0, n) + ")-(" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'v')
				s = s.substring(0, n) + ")*(" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'p')
				s = s.substring(0, n) + ")^(" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'W')
				s = s.substring(0, n) + ")|(" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'L')
				s = s.substring(0, n) + ")~(" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'i')
				s = s.substring(0, n) + "_" + s.substring(n + 2, k) + "_" + s.substring(k + 2, l) + "_"
					+ s.substring(l + 2, indexAtl) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'q')
				s = s.substring(0, n) + "_" + s.substring(n + 2, k) + "_" + s.substring(k + 2, l) + "_"
					+ s.substring(l + 2, indexAtl) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'y')
				s = s.substring(0, n) + "_" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'd')
				s = s.substring(0, n) + "_" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'D')
				s = s.substring(0, n) + "_" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'P')
				s = s.substring(0, n) + "_" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'T')
				s = s.substring(0, n) + "_" + s.substring(n + 2, k) + "_" + s.substring(k + 2, l) + "_"
					+ s.substring(l + 2, indexAtl) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'S')
				s = s.substring(0, n) + "_" + s.substring(n + 2, k) + "_" + s.substring(k + 2, l) + "_"
					+ s.substring(l + 2, indexAtl) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'Y')
				s = s.substring(0, n) + "(" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
			else if (s.charAt(index$ + 1) == 'M')
				s = s.substring(0, n) + "(" + s.substring(n + 2, indexAt) + ")" + s.substring(indexAt + 1);
            
			n = s.indexOf("$n");
		}
		
		n = s.indexOf("$w");
		while(n>-1)
		{	s = s.substring(0,n) + "sqrt(" + s.substring(n+2);
			n = s.indexOf("$w");
		}
		n = s.indexOf("$r");
		while(n>-1)
		{	s = s.substring(0,n) + "abs(" + s.substring(n+2);
			n = s.indexOf("$r");
		}
		n = s.indexOf("$c");
        while(n>-1)
        {   s = s.substring(0,n) + "conjug(" + s.substring(n+2);
            n = s.indexOf("$c");
        }
		n = s.indexOf("$y");
		while(n>-1)
		{
			s = s.substring(0,n) + "(bin(" + s.substring(n+2);
			n = s.indexOf("$y");
		}
		n = s.indexOf("$Y");
		while (n>-1)
		{
			s = s.substring(0,n) + "vector(" + s.substring(n+2);
			n = s.indexOf("$Y");
		}
		n = s.indexOf("$M"); // matrix
		while (n > -1)
		{
			s = s.substring(0,n) + "matrix(" + s.substring(n+2);
			n = s.indexOf("$M");
		}
		n = s.indexOf("$k"); // kolommen in rij van matrix
		while (n > -1)
		{	s = s.substring(0,n) + "(" + s.substring(n+2);
			n = s.indexOf("$k");
		}
		n = s.indexOf("$d");
		while(n>-1)
		{	s = s.substring(0,n) + "(dif(" + s.substring(n+2);
			n = s.indexOf("$d");
			
		}
		n = s.indexOf("$D");
		while(n>-1)
		{	s = s.substring(0,n) + "(difpar(" + s.substring(n+2);
			n = s.indexOf("$D");
			
		}
		n = s.indexOf("$g");
		while(n>-1)
		{
			s = s.substring(0,n) + "(differentiaal(" + s.substring(n+2);
			n = s.indexOf("$g");
		}
		n = s.indexOf("$P");
		while(n>-1)
		{	s = s.substring(0,n) + "(prm(" + s.substring(n+2);
			n = s.indexOf("$P");
			
		}
		n = s.indexOf("$T");
		while(n>-1)
		{	s = s.substring(0,n) + "(lim(" + s.substring(n+2);
			n = s.indexOf("$T");
			
		}
		n = s.indexOf("$S");
        while(n>-1)
        {   s = s.substring(0,n) + "(sig(" + s.substring(n+2);
            n = s.indexOf("$S");
            
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
		n = s.indexOf("$h");
		while(n>-1)
		{	s = s.substring(0,n) + "(" + s.substring(n+2);
			n = s.indexOf("$h");
		}
		n = s.indexOf("$b");
		while(n>-1)
		{	s = s.substring(0,n) + "((" + s.substring(n+2);
			n = s.indexOf("$b");
		}
		n = s.indexOf("$o");
		while(n>-1)
		{	s = s.substring(0,n) + "((" + s.substring(n+2);
			n = s.indexOf("$o");
		}
		n = s.indexOf("$a");
		while(n>-1)
		{	s = s.substring(0,n) + "((" + s.substring(n+2);
			n = s.indexOf("$a");
		}
		n = s.indexOf("$v");
		while(n>-1)
		{	s = s.substring(0,n) + "((" + s.substring(n+2);
			n = s.indexOf("$v");
		}
		n = s.indexOf("$p");
		while(n>-1)
		{	s = s.substring(0,n) + "((" + s.substring(n+2);
			n = s.indexOf("$p");
		}
		n = s.indexOf("$W");
		while(n>-1)
		{	s = s.substring(0,n) + "((" + s.substring(n+2);
			n = s.indexOf("$W");
		}
		n = s.indexOf("$L");
		while(n>-1)
		{	s = s.substring(0,n) + "((" + s.substring(n+2);
			n = s.indexOf("$L");
		}
		n = s.indexOf("$i");
		while(n>-1)
		{	s = s.substring(0,n) + "(int(" + s.substring(n+2);
			n = s.indexOf("$i");
		}
		n = s.indexOf("$q");
        while(n>-1)
        {   s = s.substring(0,n) + "(prv(" + s.substring(n+2);
            n = s.indexOf("$q");
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
		
		while (n>-1)
		{
			s = s.substring(0,n) + "*" + s.substring(n+1);
			n = s.indexOf("\u00d7");
		}
		
		//System.out.println("FormuleParser.formuleString(" + in + ") = " + s);
		
		return s;
	}
	
	public static String vervangFunctieScheidingstekens(String s, String functie)
	{	String s1 = s;
		String s2 = "";
		int ind = s.indexOf(functie + "(");
		int len = functie.length()+1;
		
		while(ind>-1) 
		{	int start = ind + len;
			s1 = s.substring(0, ind+len);
			s2 = s.substring(ind+len);
			int niv = 0;
			for(int i=0 ; i<s2.length() ; i++)
			{	if(s2.charAt(i)==')') niv++;
				else if(s2.charAt(i)=='(') niv--;
				else if(s2.charAt(i)==',' && niv==0) s2 = s2.substring(0,i)+"_"+s2.substring(i+1);
				if(niv>0)break;
			}
			s = s1 + s2;
			ind = s.indexOf(functie + "(", start );
		}
		return s;
	}
	
	public static String schoon(String s)
	{	return schoon(s,false);
	}
	
	public static String schoon(String s, boolean woordformule)
	{
		String in = s;
		// in: (2*vector((x)((1((1)/(2)))x)))
		
		s = vervangFunctieScheidingstekens(s,"normalcdf");
		s = vervangFunctieScheidingstekens(s,"invNorm");
		s = vervangFunctieScheidingstekens(s,"invnorm");
		s = vervangFunctieScheidingstekens(s,"binomcdf");
		s = vervangFunctieScheidingstekens(s,"binompdf");
		s = vervangFunctieScheidingstekens(s,"poissoncdf");
		s = vervangFunctieScheidingstekens(s,"poissonpdf");
		s = vervangFunctieScheidingstekens(s,"gcd");
		s = vervangFunctieScheidingstekens(s,"min");
		s = vervangFunctieScheidingstekens(s,"max");
		
		String[] functieNamen = FunctieMV.getFunctieMVDefSet().geefFunctieMVNamen();
        for(int i = 0 ; i<functieNamen.length ; i++)
        {	s = vervangFunctieScheidingstekens(s,functieNamen[i]);
        }
		
	
		s = s.replace(',','.');
		s = s.replace(':','/');
		s = s.replace('\u00f7', '/');
		s = s.replace('\u00b0',' ');
		
		int index = 0;
		while(index >-1)
		{	index = s.indexOf(" ");
			//if(index>0 && index<s.length()-1 && Character.isDigit(s.charAt(index-1)) && Character.isDigit(s.charAt(index+1)))
			//{	s = s.substring(0,index) + "+" + s.substring(index+1);
			//}
			//else 
			if(index >-1)s = s.substring(0,index) + s.substring(index+1);
		}
		
		//voor hoeken
		index = 0;
		int teller = 0;
		while(index >-1 && index>teller)
		{	index = s.substring(teller).indexOf("\u2220");
			if(index >-1 && s.substring(index+teller+1).length()>0)
			{
				if(Character.isLetter(s.charAt(index+teller+1)) && Character.isUpperCase(s.charAt(index+teller+1)))
				s = s.substring(0,index+teller) + "(" + "\u2220" + s.charAt(index+teller+1) + ")" + s.substring(index+teller+2);
			}
			teller = index+2;
		}
		
		//vervangt -- door +
		index = 0;
		while(index >-1)
		{	index = s.indexOf("--");
			if(index >-1)s = s.substring(0,index) + "+" + s.substring(index+2);
		}
		
		
		/*index = 0;
		while(index >-1)
		{	index = s.indexOf("*-");
			
			int tel = index+2;
			while(tel<s.length() && Character.isDigit(s.charAt(tel)))
			{	tel++;
			}
			
			if(index >-1 && tel>index+2)s = s.substring(0,index) + "(" + s.substring(index+1,tel) + ")" + s.substring(tel);
		}*/
		
		//vervangt *-6 door *(-6)
		index = 0;
		while(index >-1)
		{	index = s.indexOf("*-");
			//System.out.println("s1: "+s);
			int tel = index+2;
			while(tel<s.length() && (Character.isDigit(s.charAt(tel)) || s.charAt(tel)=='.'))
			{	tel++;
			}
			
			if(index >-1 && tel>index+2 && s.charAt(tel)=='^') 
				s = s.substring(0,index) + "(-1)" + s.substring(index+2);
			
			else if(index >-1 && tel>index+2) 
				s = s.substring(0,index) + "(" + s.substring(index+1,tel) + ")" + s.substring(tel);
			
			else if(index >-1 && index+2<s.length() && Character.isLetter(s.charAt(index+2))) 
			{	tel = index+3;
				s = s.substring(0,index) + "(-1)" + s.substring(index+2);
			}	
			else if(index >-1 && index+2<s.length())s = s.substring(0,index) + "(-1)" + s.substring(index+2);
			
			//System.out.println("s2: "+s);
		}
		
		//vervangt /-6 door /(-6)
		index = 0;
		int check = 1000;
		while(index >-1 && check>0)
		{	
			check--;
			index = s.indexOf("/-");
			
			int tel = index+2;
			while(tel<s.length() && (Character.isDigit(s.charAt(tel)) || s.charAt(tel)=='.'))
			{	tel++;
			}
			
			if(index >-1 && tel>index+2)s = s.substring(0,index) + "/(" + s.substring(index+1,tel) + ")" + s.substring(tel);
			
			else if(index >-1 && index+2<s.length()) // && (Character.isLetter(s.charAt(index+2))
			{	tel = index+3;
				s = s.substring(0,index) + "(-1)/" + s.substring(index+2);
			}	
			
		}
		
		//een breuk constructie als "2((1)/(2))" wordt vervangen door 2+1/2
		int start = 0;
		index = s.indexOf(")/(",start);
		while(index >-1)
		{	
			start = index+3;
			int telmin = index-1;
			while(telmin>0 && Character.isDigit(s.charAt(telmin)))
			{	telmin--;
			}
			int telmax = index+3;
			while(telmax<s.length() && Character.isDigit(s.charAt(telmax)))
			{	telmax++;
			}
			if(s.charAt(telmin)=='(' 
			   && s.charAt(telmin-1)=='('
			   && Character.isDigit(s.charAt(telmin-2))
			   && s.charAt(telmax)==')'
			   && s.charAt(telmax+1)==')')
			{	int telminmin = telmin-3;
				while(telminmin>0 && Character.isDigit(s.charAt(telminmin)))
				{	telminmin--;
				}
				s = s.substring(0,telminmin+1) + "(" + s.substring(telminmin+1,telmin-1) + "+" +  s.substring(telmin-1,telmax+2) + ")" + s.substring(telmax+2);
			}
			index = s.indexOf(")/(",start);
		}
		if(!(woordformule || FormuleParser.woordFormule)&& tweeHoofdletterVariabele)
        {   for(int i=0 ; i<s.length()-1 ; i++)
            {  	char c0 = s.charAt(i);
        		char c1 = s.charAt(i+1);
        		boolean isUpperCasePair = Character.isUpperCase(c0) && Character.isUpperCase(c1);
        		if(!isUpperCasePair && ((Character.isLetter(c0) || Character.isDigit(c0) || c0==')') && (Character.isLetter(c1) || c1=='(')))
                {   s = s.substring(0,i+1) + '*' +  s.substring(i+1);
                }
                else if(c0==')' && Character.isDigit(c1))
                {   s = s.substring(0,i+1) + '*' +  s.substring(i+1);
                }
            }
        }
		else if(!(woordformule || FormuleParser.woordFormule))
		{	for(int i=0 ; i<s.length()-1 ; i++)
			{	if((Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)) || s.charAt(i)==')') && (Character.isLetter(s.charAt(i+1)) || s.charAt(i+1)=='(' || s.charAt(i+1)=='\u2220'))
				{	s = s.substring(0,i+1) + '*' +  s.substring(i+1);
				}
				else if(s.charAt(i)==')' && Character.isDigit(s.charAt(i+1)))
				{	s = s.substring(0,i+1) + '*' +  s.substring(i+1);
				}
			}
		}
		
		String[] fMetMaalBasis 	= {
		 		"s*q*r*t*", 
				"r*n*d*", 
				"r*n*s*", 
				"s*g*f*", 
				"r*n*q*", 
				"a*b*s*", 
				"c*o*n*j*u*g*", 
				"b*i*n*o*m*c*d*f*",
				"b*i*n*o*m*p*d*f*",
				"p*o*i*s*s*o*n*c*d*f*",
				"p*o*i*s*s*o*n*p*d*f*",
				"b*i*n*",
				"d*i*f*p*a*r*",
				"d*i*f*f*e*r*e*n*t*i*a*a*l*",
				"d*i*f*",
				"d*i*f*",
				"p*r*m*",
				"s*i*g*",
				"l*i*m*",
				"i*n*t*",
				"g*c*d*",
				"m*i*n*",
				"m*a*x*",
				"n*o*r*m*a*l*c*d*f*",
				"i*n*v*N*o*r*m*",
				"i*n*v*n*o*r*m*",
				"p*r*v*",
				"a*r*c*s*i*n*",
				"a*r*c*s*i*n",
				"a*r*c*c*o*s*",
				"a*r*c*c*o*s",
				"a*r*c*t*a*n*",
				"a*r*c*t*a*n",
				"s*i*n*",
				"s*i*n",
				"c*o*s*",
				"c*o*s",
				"t*a*n*",
				"t*a*n",
				"l*o*g*",
				"l*o*g",
				"l*n*",
				"l*n",
				"v*e*c*t*o*r*",
				"m*a*t*r*i*x*"
				};
		//String[] fMetMaalFunctie = Functie.getFunctieDefSet().geefFunctieNamenSubst();
		String[] fMetMaalFunctie = FunctieMV.getFunctieMVDefSet().geefFunctieMVNamenSubst();
		String[] fMetMaal = new String[fMetMaalBasis.length + fMetMaalFunctie.length];
		for(int i=0 ; i<fMetMaalBasis.length ; i++)
		{	fMetMaal[i] = fMetMaalBasis[i];
		}
		for(int i=0 ; i<fMetMaalFunctie.length ; i++)
		{	fMetMaal[i+fMetMaalBasis.length] = fMetMaalFunctie[i];
		}
		//String[] fMetMaal = fMetMaalBasis;
		for (int i = 0; i < fMetMaal.length; i++)
		{
			int fLength = fMetMaal[i].length();
			String fZonderMaal = StringUtils.replaceStr(fMetMaal[i], "*", "");
			index = 0;
			while (index > -1)
			{
				index = s.indexOf(fMetMaal[i]);
				if (index > -1)
					s = s.substring(0, index) + fZonderMaal + s.substring(index + fLength); // bijv. (s*q*r*t*(5)) -> (sqrt(5))
			}
		}
		
		index = 0;
		while(index >-1)
		{	index = s.indexOf("invnorm");
			if(index >-1)s = s.substring(0,index) + "invNorm" + s.substring(index+7);
		}
		
		
		index = 0;
		while(index >-1)
		{	index = s.indexOf("(-");
			if(index >-1)s = s.substring(0,index) + "(0-" + s.substring(index+2);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("+-");
			if(index >-1)s = s.substring(0,index) + "-" + s.substring(index+2);
		}
		
		String[] gfs = {"sin","cos","tan","log","ln"};
		for (int j = 0; j < gfs.length; j++)
		{
			index = 0;
			while (index > -1)
			{
				int lf = gfs[j].length() + 2;
				index = s.indexOf(gfs[j] + "^(", index);
				if (index == -1)
					break;
				boolean scheidingGepaseerd = false;
				int niv = 1;
				for (int i = index + lf; i < s.length(); i++)
				{
					if (s.charAt(i) == '(')
					{
						niv++;
					}
					else if (s.charAt(i) == ')')
					{
						niv--;
					}
					if (niv == 0 && s.charAt(i) == ')' && scheidingGepaseerd)
					{
						s = s.substring(0, index) + "(" + s.substring(index, i + 1) + ")" + s.substring(i + 1);
						// System.out.println("correctie");
						break;
					}
					if (!scheidingGepaseerd)
						scheidingGepaseerd = (s.length() > i + 3 && niv == 0 && s.substring(i, i + 3).equals(")*("));
				}
				if (index > -1)
					index = index + lf;
			}/**/	
		}
		
		//System.out.println("FormuleParser.schoon(" + in + ", woordformule = " + woordformule + ") = " + s);

		return s; // uit: (2*vector((x)*(((1+((1)/(2))))*x)))
	}
	
	/**
	 * Pel de omsluitende haken eraf.
	 * 
	 * @param s
	 * @return
	 */
	public static String pel(String s)
	{
		String in = s;
		
		boolean pelbaar = true;
		
		while (pelbaar)
		{
			if (s.length() > 0 && s.charAt(0)=='(' && s.charAt(s.length()-1)==')')
			{
				pelbaar = true;
			}
			else
				pelbaar = false;
			if (pelbaar)
			{
				int niv = 0;
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
			if (pelbaar)
				s = s.substring(1,s.length()-1);
		}
		
//		System.out.println("FormuleParser.pel(" + in + ") = " + s);
		
		return s;
	}
	
	public static Expressie[] splitExpressieParameters(String string, char scheidingsChar, int aantal)
	{	Expressie[] expressies = splitExpressieParameters(string, scheidingsChar);
		if(expressies!=null && expressies.length==aantal) 
			return expressies;
		return null;
	}
	
	public static Expressie[] splitExpressieParameters(String string, char scheidingsChar)
	{	
		int lev=1;
	    int index1 = -1;
	    int index2 = -1;
	    int index3 = -1;
	    for (int i=0 ; i<string.length() ; i++)
	    {   if(i>0 && lev==0) break;
	        if(string.charAt(i) == '(') lev++;
	        if(string.charAt(i) == ')') lev--;
	        if(index1==-1 && lev==1 && string.charAt(i)==scheidingsChar) index1 = i;
	        else if(index2==-1 && lev==1 && string.charAt(i)==scheidingsChar) index2 = i;
	        else if(index3==-1 && lev==1 && string.charAt(i)==scheidingsChar) index3 = i;
	    }
	    if(index1==-1)
	    {	Expressie[] expressies = new Expressie[1];
    		expressies[0] = parse(string);
    		if(expressies[0]==null)return null;
            else return expressies;
	    }
	    if(index2==-1)
        { 	Expressie[] expressies = new Expressie[2];
        	expressies[0] = parse(string.substring(0,index1));
        	expressies[1] = parse(string.substring(index1+1));
            if(expressies[0]==null || expressies[1]==null)return null;
            else return expressies;
        }
	    else if(index3==-1)
	    {   Expressie[] expressies = new Expressie[3];
	    	expressies[0] = parse(string.substring(0,index1));
	    	expressies[1] = parse(string.substring(index1+1,index2));
	    	expressies[2] = parse(string.substring(index2+1));
	    	if(expressies[0]==null || expressies[1]==null || expressies[2]==null)return null;
	    	else return expressies;
	    }
	    else 
	    {   Expressie[] expressies = new Expressie[4];
	    	expressies[0] = parse(string.substring(0,index1));
	    	expressies[1] = parse(string.substring(index1+1,index2));
	    	expressies[2] = parse(string.substring(index2+1,index3));
	    	expressies[3] = parse(string.substring(index3+1));
	    	if(expressies[0]==null || expressies[1]==null || expressies[2]==null || expressies[3]==null)return null;
	    	else return expressies;
	    }
	}
	
	public static Expressie parse(String s)
	{	return parse(s,false);
	}
	
	/**
	 * Bijv. (sqrt(5)) -> Wortel(5)
	 * @param s
	 * @param woordformule
	 * @return
	 */
	public static Expressie parse(String s, boolean woordformule)
	{
		//System.out.println("FormuleParser.parse(" + s + ", woordformule = " + woordformule + ")");
		
		Expressie exp = null;
		// verwijder overbodige haakjes
		try
		{
			boolean pelbaar = true;
			while (pelbaar)
			{
				if (s.length() > 0 && s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')')
				{
					pelbaar = true;
				}
				else
					pelbaar = false;
				if (pelbaar)
				{
					int niv = 0;
					int minNiv = 0;
					for (int i = 0; i < s.length() - 1; i++)
					{
						if (s.charAt(i) == '(')
						{
							niv++;

						}
						else if (s.charAt(i) == ')')
						{
							niv--;
							if (niv < 1)
							{
								pelbaar = false;
								break;
							}
						}
					}
				}
				if (pelbaar)
					s = s.substring(1, s.length() - 1);
			}
			if (s.length() > 0 && s.charAt(0) == '-')
				s = '0' + s;
			if (s.length() > 0 && s.charAt(0) == '+')
				s = s.substring(1);

			if (woordformule || FormuleParser.woordFormule)
			{
				boolean startMetLetter = true;
				startMetLetter = Character.isLetter(s.charAt(0));
				boolean basisString = true;
				for (int i = 1; i < s.length(); i++)
				{
					basisString = startMetLetter && (Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)));
					if (!basisString)
						break;
				}

				if (basisString)
				{
					if (s.length() == 1 && s.charAt(0) == 'e')
						exp = new E();
					else if (s.length() == 1 && s.charAt(0) == '\u03C0')
						exp = new PI();
					else
						exp = new BasisExpressie(s);
					return exp;
				}
			}
			else if (FormuleParser.tweeHoofdletterVariabele)
			{
				boolean upperCasePair = s.length() == 2 && Character.isUpperCase(s.charAt(0))
					&& Character.isUpperCase(s.charAt(1));
				if (upperCasePair)
					return new BasisExpressie(s);
				else if (s.length() == 1 && Character.isLetter(s.charAt(0)))
				{
					if (s.charAt(0) == 'e')
						exp = new E();
					else if (s.charAt(0) == '\u03C0')
						exp = new PI();
					else
						exp = new BasisExpressie(s);
					return exp;
				}
			}
			else
			{
				//hoek
				if (s.length() == 2 && '\u2220'==s.charAt(0) && Character.isLetter(s.charAt(1)) && Character.isUpperCase(s.charAt(1)))
				{
					exp = new BasisExpressie(s);
					return exp;
				}
				// is het een letter?
				if (s.length() == 1 && Character.isLetter(s.charAt(0)))
				{
					if (s.charAt(0) == 'e')
						exp = new E();
					else if (s.charAt(0) == '\u03C0')
						exp = new PI();

					// else if(s.charAt(0) == 'd' && diffOperatoren)
					// { exp = new Differentiaal(null);
					// System.out.println("differentiaal gemaakt");
					// }
					else
						exp = new BasisExpressie(s);
					return exp;
				}
			}
			// is het een getal?
			boolean isGetal = true;
			try
			{
				Double d = Double.valueOf(s);
			}
			catch (NumberFormatException nfe)
			{
				isGetal = false;
			}
			if (isGetal)
			{
				exp = new BasisExpressie(Double.valueOf(s).doubleValue());
				if ("MW".equals(WiskOpdr.deployVariant) || significantie)
					exp = new BasisExpressie(s);
				return exp;
			}
			// is + of - oneidig?
			if (s.equals("\u221e"))
			{
				double d = Double.POSITIVE_INFINITY;
				exp = new BasisExpressie(s);
			}

			if (s.equals("-\u221e"))
			{
				double d = Double.NEGATIVE_INFINITY;
				exp = new BasisExpressie(s);
			}

			if (diffOperatoren && s.charAt(0) == 'd' && (s.charAt(1) == '*')) // (als
																				// charAt(1)
																				// i
																				// is,
																				// dan
																				// heb
																				// je
																				// een
																				// diff)
			{ // begin met een d, en dan ofwel een haakje openen die hoort bij
				// het haakje sluiten
				// helemaal achteraan, of één teken, of een
				// subscript-constructie.
				if (s.length() == 3)
				{
					return new Differentiaal(parse(s.substring(2, s.length())));
				}
				boolean isDifferentiaal = true;
				if (s.charAt(2) == '(')
				{
					int niv = 1;
					for (int i = 3; i < s.length(); i++)
					{
						if (s.charAt(i) == '(')
							niv++;
						else if (s.charAt(i) == ')')
							niv--;
						if (niv == 0 && i < s.length() - 1)
						{
							isDifferentiaal = false;
							break;
						}
					}
				}
				else if (s.length() > 4 && s.charAt(3) == '$' && s.charAt(4) == 's')// subscript
				{
					int niv = 1;
					for (int i = 4; i < s.length(); i++)
					{
						if (s.charAt(i) == '$')
							niv++;
						else if (s.charAt(i) == '@')
							niv--;
						if (niv == 0 && i < s.length() - 1)
						{
							isDifferentiaal = false;
							break;
						}
					}
				}
				else
					isDifferentiaal = false;

				if (isDifferentiaal)
				{
					Expressie e1 = parse(s.substring(2, s.length()));
					// System.out.println("geeft differentiaal met kind " +
					// e1.toString());
					return new Differentiaal(e1);
				}

			}

			int niv = 0;
			for (int i = s.length() - 1; i > -1; i--)
			{
				if (s.charAt(i) == ')')
				{
					niv++;
				}
				else if (s.charAt(i) == '(')
				{
					niv--;
				}
				else if (s.charAt(i) == '+' && niv == 0)
				{
					Expressie e1 = parse(s.substring(0, i));
					Expressie e2 = parse(s.substring(i + 1));
					if (e1 == null || e2 == null)
						return null;
					return new Optelling(e1, e2);

				}
				// else return exp;
			}

			niv = 0;
			for (int i = s.length() - 1; i > -1; i--)
			{
				if (s.charAt(i) == ')')
				{
					niv++;
				}
				else if (s.charAt(i) == '(')
				{
					niv--;
				}
				else if (s.charAt(i) == '-' && niv == 0)
				{ // Expressie e1 = new BasisExpressie(0);
					// if(i!=0)
					Expressie e1 = parse(s.substring(0, i));
					Expressie e2 = parse(s.substring(i + 1));
					if (e1 == null || e2 == null)
						return null;
					return new Aftrekking(e1, e2);

				}
				// else return exp;
			}

			String[] maalFnct =
				{ "*sin", "*cos", "*tan", "*arcsin", "*arccos", "*arctan", "*log", "*ln" };
			for (int j = 0; j < maalFnct.length; j++)
			{
				int maalFnctLength = maalFnct[j].length();
				niv = 0;
				if (s.length() > maalFnctLength)
				{
					for (int i = s.length() - 1; i > -1; i--)
					{
						if (s.charAt(i) == ')')
						{
							niv++;
						}
						else if (s.charAt(i) == '(')
						{
							niv--;
						}
						else if (i < s.length() - maalFnctLength
							&& s.substring(i, i + maalFnctLength).equals(maalFnct[j]) && niv == 0)
						{
							Expressie e1 = parse(s.substring(0, i));
							Expressie e2 = parse(s.substring(i + 1));
							if (e1 == null || e2 == null)
								return null;
							return new Vermenigvuldiging(e1, e2);
						}
					}
				}
			}

			niv = 0;
			if (s.length() > 4 && s.substring(0, 4).equals("sin^"))
			{
				for (int i = 4; i < s.length(); i++)
				{
					if (s.charAt(i) == '(')
					{
						niv++;
					}
					else if (s.charAt(i) == ')')
					{
						niv--;
					}
					else if (s.substring(i, i + 1).equals("*") && niv == 0)
					{
						Expressie e1 = parse(s.substring(4, i));
						Expressie e2 = parse(s.substring(i + 1));
						if (e1 == null || e2 == null)
							return null;
						if("(0-1)".equals(s.substring(4, i)))
							return new ArcSinus(e2);
						return new Macht(new Sinus(e2), e1);
					}

				}
				return null;
			}

			niv = 0;
			if (s.length() > 4 && s.substring(0, 4).equals("cos^"))
			{
				for (int i = 4; i < s.length(); i++)
				{
					if (s.charAt(i) == '(')
					{
						niv++;
					}
					else if (s.charAt(i) == ')')
					{
						niv--;
					}
					else if (s.substring(i, i + 1).equals("*") && niv == 0)
					{
						Expressie e1 = parse(s.substring(4, i));
						Expressie e2 = parse(s.substring(i + 1));
						if (e1 == null || e2 == null)
							return null;
						if("(0-1)".equals(s.substring(4, i)))
							return new ArcCosinus(e2);
						return new Macht(new Cosinus(e2), e1);
					}

				}
				return null;
			}

			niv = 0;
			if (s.length() > 4 && s.substring(0, 4).equals("tan^"))
			{
				for (int i = 4; i < s.length(); i++)
				{
					if (s.charAt(i) == '(')
					{
						niv++;
					}
					else if (s.charAt(i) == ')')
					{
						niv--;
					}
					else if (s.substring(i, i + 1).equals("*") && niv == 0)
					{
						Expressie e1 = parse(s.substring(4, i));
						Expressie e2 = parse(s.substring(i + 1));
						if (e1 == null || e2 == null)
							return null;
						if("(0-1)".equals(s.substring(4, i)))
							return new ArcTangens(e2);
						return new Macht(new Tangens(e2), e1);
					}

				}
				return null;
			}

			niv = 0;
			if (s.length() > 4 && s.substring(0, 4).equals("log^"))
			{
				for (int i = 4; i < s.length(); i++)
				{
					if (s.charAt(i) == '(')
					{
						niv++;
					}
					else if (s.charAt(i) == ')')
					{
						niv--;
					}
					else if (s.substring(i, i + 1).equals("*") && niv == 0)
					{
						Expressie e1 = parse(s.substring(4, i));
						Expressie e2 = parse(s.substring(i + 1));
						if (e1 == null || e2 == null)
							return null;
						return new Macht(new Log(e2), e1);
					}

				}
				return null;
			}

			niv = 0;
			if (s.length() > 3 && s.substring(0, 3).equals("ln^"))
			{
				for (int i = 3; i < s.length(); i++)
				{
					if (s.charAt(i) == '(')
					{
						niv++;
					}
					else if (s.charAt(i) == ')')
					{
						niv--;
					}
					else if (s.substring(i, i + 1).equals("*") && niv == 0)
					{
						Expressie e1 = parse(s.substring(3, i));
						Expressie e2 = parse(s.substring(i + 1));
						if (e1 == null || e2 == null)
							return null;
						return new Macht(new Ln(e2), e1);
					}

				}
				return null;
			}

			// System.out.println("expressieString: "+s);

			if (s.length() > 6 && s.substring(0, 6).equals("arcsin") && s.charAt(6) != '(')
			{
				Expressie e = parse(s.substring(6));
				if (e == null)
					return null;
				return new ArcSinus(e);
			}

			if (s.length() > 6 && s.substring(0, 6).equals("arccos") && s.charAt(6) != '(')
			{
				Expressie e = parse(s.substring(6));
				if (e == null)
					return null;
				return new ArcCosinus(e);
			}

			if (s.length() > 6 && s.substring(0, 6).equals("arctan") && s.charAt(6) != '(')
			{
				Expressie e = parse(s.substring(6));
				if (e == null)
					return null;
				return new ArcTangens(e);
			}

			if (s.length() > 3 && s.substring(0, 3).equals("sin") && s.charAt(3) != '(')
			{
				Expressie e = parse(s.substring(3));
				if (e == null)
					return null;
				return new Sinus(e);
			}

			if (s.length() > 3 && s.substring(0, 3).equals("cos") && s.charAt(3) != '(')
			{
				Expressie e = parse(s.substring(3));
				if (e == null)
					return null;
				return new Cosinus(e);
			}

			if (s.length() > 3 && s.substring(0, 3).equals("tan") && s.charAt(3) != '(')
			{
				Expressie e = parse(s.substring(3));
				if (e == null)
					return null;
				return new Tangens(e);
			}

			if (s.length() > 3 && s.substring(0, 3).equals("log") && s.charAt(3) != '(')
			{
				Expressie e = parse(s.substring(3));
				if (e == null)
					return null;
				return new Log(e);
			}

			// boolean lnMetAbs = false;
			// if(s.length()>5 && s.substring(0,5).equals("lnabs"))
			// { lnMetAbs=true;
			// }

			if (s.length() > 2 && s.substring(0, 2).equals("ln") && s.charAt(2) != '(')// &&
																						// !lnMetAbs)
			{
				Expressie e = parse(s.substring(2));
				if (e == null)
					return null;
				return new Ln(e);
			}

			niv = 0;
			for (int i = s.length() - 1; i > -1; i--)
			{
				if (s.charAt(i) == ')')
				{
					niv++;
				}
				else if (s.charAt(i) == '(')
				{
					niv--;
				}
				else if (s.charAt(i) == '*' && niv == 0)
				{
					Expressie e1 = parse(s.substring(0, i));
					Expressie e2 = parse(s.substring(i + 1));
					if (e1 == null || e2 == null)
						return null;
					if (diffOperatoren && e1 instanceof Deling && e1.kind2 instanceof Differentiaal
						&& e1.kind1.toString().equals("d"))// geval: d/dx (f(x))
						return new Diff(e2, e1.kind2.kind1);
					else if (diffOperatoren && e1 instanceof Vermenigvuldiging && e1.kind2.toString().equals("d"))// geval:
																													// iets*dx
					{
						return new Vermenigvuldiging(e1.kind1, new Differentiaal(e2));
					}

					else
						return new Vermenigvuldiging(e1, e2);

				}

			}

			niv = 0;
			for (int i = s.length() - 1; i > -1; i--)
			{
				if (s.charAt(i) == ')')
				{
					niv++;
				}
				else if (s.charAt(i) == '(')
				{
					niv--;
				}
				else if (s.charAt(i) == '/' && niv == 0)
				{
					Expressie e1 = parse(s.substring(0, i));
					Expressie e2 = parse(s.substring(i + 1));
					if (e1 == null || e2 == null)
					{
						return null;
					}
					if (diffOperatoren && e1 instanceof Differentiaal && e2 instanceof Differentiaal)
					{
						return new Diff(e1.kind1, e2.kind1);
					}
					else
						return new Deling(e1, e2);

				}
			}

			niv = 0;
			/*
			 * for(int i=s.length()-1 ; i>-1 ; i--) { if(s.charAt(i)==')') {
			 * niv++; } else if(s.charAt(i)=='(') { niv--; } else
			 * if(s.charAt(i)=='^' && niv==0) { Expressie e1 =
			 * parse(s.substring(0,i)); Expressie e2 = parse(s.substring(i+1));
			 * if(e1==null || e2==null)return null; return new Macht(e1,e2);
			 * 
			 * } }
			 */
			for (int i = 0; i < s.length(); i++)
			{
				if (s.charAt(i) == ')')
				{
					niv++;
				}
				else if (s.charAt(i) == '(')
				{
					niv--;
				}
				else if (s.charAt(i) == '^' && niv == 0)
				{
					Expressie e1 = parse(s.substring(0, i));
					Expressie e2 = parse(s.substring(i + 1));
					if (e1 == null || e2 == null)
						return null;
					return new Macht(e1, e2);

				}
			}

			niv = 0;
			for (int i = s.length() - 1; i > -1; i--)
			{
				if (s.charAt(i) == ')')
				{
					niv++;
				}
				else if (s.charAt(i) == '(')
				{
					niv--;
				}
				else if (s.charAt(i) == '|' && niv == 0)
				{
					Expressie e1 = parse(s.substring(0, i));
					Expressie e2 = parse(s.substring(i + 1));
					if (e1 == null || e2 == null)
						return null;
					return new NdeWortel(e1, e2);

				}
			}

			niv = 0;
			for (int i = s.length() - 1; i > -1; i--)
			{
				if (s.charAt(i) == ')')
				{
					niv++;
				}
				else if (s.charAt(i) == '(')
				{
					niv--;
				}
				else if (s.charAt(i) == '~' && niv == 0)
				{
					Expressie e1 = parse(s.substring(0, i));
					Expressie e2 = parse(s.substring(i + 1));
					if (e1 == null || e2 == null)
						return null;
					return new NdeLog(e1, e2);

				}
			}
			niv = 0;
			for (int i = s.length() - 1; i > -1; i--)
			{
				if (s.charAt(i) == ')')
				{
					niv++;
				}
				else if (s.charAt(i) == '(')
				{
					niv--;
				}
				else if (s.charAt(i) == '?' && niv == 0)
				{
					Expressie e1 = parse(s.substring(0, i));
					Expressie e2 = parse(s.substring(i + 1));
					if (e1 == null || e2 == null)
						return null;
					return new BasisExpressie(s.substring(0, i) + "?" + s.substring(i + 1));
				}
			}

			/*
			 * String[] functieNamen =
			 * Functie.getFunctieDefSet().geefFunctieNamen(); String[]
			 * functieNamen = FunctieMV.getFunctieMVDefSet().geefFunctieNamen();
			 * for(int i = 0 ; i<functieNamen.length ; i++) { String functieNaam
			 * = functieNamen[i]; if(s.length()>functieNaam.length() &&
			 * s.substring(0,functieNaam.length()).equals(functieNaam) &&
			 * s.charAt(functieNaam.length())=='(') { Expressie e =
			 * parse(s.substring(functieNaam.length(),s.length()));
			 * if(e==null)return null; return new Functie(functieNaam,e); } }
			 */

			String[] functieNamen = FunctieMV.getFunctieMVDefSet().geefFunctieMVNamen();
			for (int i = 0; i < functieNamen.length; i++)
			{
				String functieNaam = functieNamen[i];
				// System.out.println("parseString:"+s);
				// System.out.println("functieNaam:"+functieNaam);
				// System.out.println("fit:"+(s.length()>functieNaam.length() &&
				// s.substring(0,functieNaam.length()).equals(functieNaam) &&
				// s.charAt(functieNaam.length())=='('));
				if (s.length() > functieNaam.length() && s.substring(0, functieNaam.length()).equals(functieNaam)
					&& s.charAt(functieNaam.length()) == '(')
				{ // Expressie e =
					// parse(s.substring(functieNaam.length(),s.length()));
					int aantalVar = FunctieMV.getFunctieMVDefSet().geefFunctieMVVariabele(functieNaam).length;
					String string = s.substring(functieNaam.length() + 1, s.length() - 1);
					// System.out.println("splitString:"+string);
					Expressie[] expressies = splitExpressieParameters(string, '_', aantalVar);
					// System.out.println("parseFunctie:"+expressies.toString());
					boolean parseOK = true;
					for (int j = 0; j < expressies.length; j++)
						parseOK = parseOK && expressies[j] != null;
					if (parseOK == false)
						return null;
					return new FunctieMV(functieNaam, expressies);
				}
			}

			// is het een wortel
			if (s.length() > 4 && s.substring(0, 4).equals("sqrt"))
			{
				Expressie e = parse(s.substring(4, s.length()));
				if (e == null)
					return null;
				return new Wortel(e);
			}
			else if (s.length() > 6 && s.substring(0, 6).equals("conjug"))
			{
				Expressie e = parse(s.substring(6, s.length()));
				if (e == null)
					return null;
				return new Conjug(e);
			}
			else if (s.length() > 8 && s.substring(0, 8).equals("binomcdf"))
			{
				String string = s.substring(9, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 3);
				if (expressies == null)
					return null;
				return new BinomCDF(expressies[0], expressies[1], expressies[2]);
			}
			else if (s.length() > 8 && s.substring(0, 8).equals("binompdf"))
			{
				String string = s.substring(9, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 3);
				if (expressies == null)
					return null;
				return new BinomPDF(expressies[0], expressies[1], expressies[2]);
			}
			else if (s.length() > 10 && s.substring(0, 10).equals("poissoncdf"))
			{
				String string = s.substring(11, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new PoissonCDF(expressies[0], expressies[1]);
			}
			else if (s.length() > 10 && s.substring(0, 10).equals("poissonpdf"))
			{
				String string = s.substring(11, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new PoissonPDF(expressies[0], expressies[1]);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("bin"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new Bin(expressies[0], expressies[1]);
			}
			else if (s.length() > 6 && s.substring(0, 6).equals("difpar"))
			{
				String string = s.substring(7, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new DiffPartial(expressies[0], expressies[1]);
			}
			else if (s.length() > 13 && s.substring(0, 13).equals("differentiaal"))
			{
				Expressie e = parse(s.substring(13, s.length()));
				if (e == null)
					return null;
				return new Differentiaal(e);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("dif"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new Diff(expressies[0], expressies[1]);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("prm"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new Primitieve(expressies[0], expressies[1]);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("lim"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 4);
				if (expressies == null)
					return null;
				return new Limiet(expressies[0], expressies[1], expressies[2], expressies[3]);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("sig"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 4);
				if (expressies == null)
					return null;
				return new Sigma(expressies[0], expressies[1], expressies[2], expressies[3]);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("rnd"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new DecRound(expressies[0], expressies[1]);
			}
			else if (s.length() > 4 && s.substring(0, 4).equals("sgf("))
			{
				Expressie e = parse(s.substring(4, s.length() - 1));
				if (e == null)
					return null;
				return new AantalSign(new BasisExpressie(s.substring(4, s.length() - 1)));
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("rns"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_');
				if (expressies == null)
					return null;
				if (expressies.length == 2)
					return new SigRoundStandard(expressies[0], expressies[1]);
				if (expressies.length == 3)
					return new SigRound(expressies[0], expressies[1], expressies[2]);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("rnq"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new DecRoundStrict(expressies[0], expressies[1]);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("int"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 4);
				if (expressies == null)
					return null;
				return new Integraal(expressies[0], expressies[1], expressies[2], expressies[3]);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("gcd"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new GCD(expressies[0], expressies[1]);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("min"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new Min(expressies[0], expressies[1]);
			}
			else if (s.length() > 3 && s.substring(0, 3).equals("max"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 2);
				if (expressies == null)
					return null;
				return new Max(expressies[0], expressies[1]);
			}
			else if (s.length() > 9 && s.substring(0, 9).equals("normalcdf"))
			{
				String string = s.substring(10, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 4);
				if (expressies == null)
					return null;
				return new NormalCDF(expressies[0], expressies[1], expressies[2], expressies[3]);
			}
			else if (s.length() > 7 && s.substring(0, 7).equals("invNorm"))
			{
				String string = s.substring(8, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 3);
				if (expressies == null)
					return null;
				return new InvNorm(expressies[0], expressies[1], expressies[2]);
			}

			else if (s.length() > 3 && s.substring(0, 3).equals("prv"))
			{
				String string = s.substring(4, s.length() - 1);
				Expressie[] expressies = splitExpressieParameters(string, '_', 4);
				if (expressies == null)
					return null;
				return new Prv(expressies[0], expressies[1], expressies[2], expressies[3]);
			}
			// is het een arcsinus
			else if (s.length() > 7 && s.substring(0, 7).equals("arcsin("))
			{
				Expressie e = parse(s.substring(6, s.length()));
				if (e == null)
					return null;
				return new ArcSinus(e);
			}
			// is het een arccosinus
			else if (s.length() > 7 && s.substring(0, 7).equals("arccos("))
			{
				Expressie e = parse(s.substring(6, s.length()));
				if (e == null)
					return null;
				return new ArcCosinus(e);
			}
			// is het een arctangens
			else if (s.length() > 7 && s.substring(0, 7).equals("arctan("))
			{
				Expressie e = parse(s.substring(6, s.length()));
				if (e == null)
					return null;
				return new ArcTangens(e);
			}
			// is het een sinus
			else if (s.length() > 4 && s.substring(0, 4).equals("sin("))
			{
				Expressie e = parse(s.substring(3, s.length()));
				if (e == null)
					return null;
				return new Sinus(e);
			}
			// is het een cosinus
			else if (s.length() > 4 && s.substring(0, 4).equals("cos("))
			{
				Expressie e = parse(s.substring(3, s.length()));
				if (e == null)
					return null;
				return new Cosinus(e);
			}
			// is het een tangens
			else if (s.length() > 4 && s.substring(0, 4).equals("tan("))
			{
				Expressie e = parse(s.substring(3, s.length()));
				if (e == null)
					return null;
				return new Tangens(e);
			}
			// is het een log
			else if (s.length() > 4 && s.substring(0, 4).equals("log("))
			{
				Expressie e = parse(s.substring(3, s.length()));
				if (e == null)
					return null;
				return new Log(e);
			}
			// is het een ln
			else if (s.length() > 3 && s.substring(0, 3).equals("ln("))
			{
				Expressie e = parse(s.substring(2, s.length()));
				if (e == null)
					return null;
				return new Ln(e);
			}

			// else if(s.length()>5 && s.substring(0,5).equals("lnabs"))
			// { Expressie e = parse(s.substring(2,s.length()));
			// if(e==null)return null;
			// return new Ln(e);
			// }
			/**/

			else if (s.length() > 3 && s.substring(0, 3).equals("abs"))
			{
				Expressie e = parse(s.substring(3, s.length()));
				if (e == null)
					return null;
				return new Abs(e);
			}
			// is het een ln
			else if (s.length() > 0 && s.charAt(s.length() - 1) == ('!'))
			{
				Expressie e = parse(s.substring(0, s.length() - 1));
				if (e == null)
					return null;
				// System.out.println(e.toString());
				return new Faculteit(e);
			} /**/
			else if (s.length() > 7 && s.substring(0, 7).equals("vector("))
			{
				ArrayList<Expressie> list = parseVectorKinderen(s.substring(7, s.length() - 1));
				if (list == null)
					return null;
				return new VectorExpr(list);
			}
			else if (s.length() > 7 && s.substring(0, 7).equals("matrix("))
			{
				ArrayList<ArrayList<Expressie>> list = parseMatrixKinderen(s.substring(7, s.length() - 1));
				if (list == null)
					return null;
				return new Matrix(list);
			}
		}
		catch (Exception e)
		{
		}

		return exp;
	}
	
	/**
	 * Haal de vector kinderen uit de gegeven string.
	 * @param s
	 * @return
	 */
	private static ArrayList<Expressie> parseVectorKinderen(String s)
	{
//		System.out.println("FormuleParser.parseVectorKinderen(" + s + ")");
		
		ArrayList<Expressie> kinderen = new ArrayList<Expressie>();
		
		ArrayList result = geefVectorKind(s); 
		Expressie kind = (Expressie) result.get(0);
		s = (String) result.get(1); // string zonder kind
		
		while (kind != null && kind.toString() != "")
		{
			kinderen.add(kind);

			if (!s.equals(""))
			{
				result = geefVectorKind(s);
				kind = (Expressie) result.get(0);
				s = (String) result.get(1); // string zonder kind
			}
			else
				break;
		}
		
		return kinderen;
	}

	/**
	 * Haal de matrix-rij kinderen uit de gegeven string.
	 * @param s de matrix-rij
	 * @return
	 */
	private static ArrayList<Expressie> parseMatrixRijKinderen(String s)
	{
//		System.out.println("FormuleParser.parseMatrixRijKinderen(" + s + ")");
		
		ArrayList<Expressie> kinderen = new ArrayList<Expressie>();
		
		ArrayList result = geefMatrixRijKind(s); 
		Expressie kind = (Expressie) result.get(0);
		s = (String) result.get(1); // string zonder kind
		
		while (kind != null && kind.toString() != "")
		{
			kinderen.add(kind);

			if (!s.equals(""))
			{
				result = geefMatrixRijKind(s);
				kind = (Expressie) result.get(0);
				s = (String) result.get(1); // string zonder kind
			}
			else
				break;
		}
		
		return kinderen;
	}

	/**
	 * Haal de matrix kinderen uit de gegeven string.
	 * @param s
	 * @return
	 */
	private static ArrayList<ArrayList<Expressie>> parseMatrixKinderen(String s)
	{
		//System.out.println("FormuleParser.parseMatrixKinderen(" + s + ")");
		
		ArrayList<ArrayList<Expressie>> kinderen = new ArrayList<ArrayList<Expressie>>();
		
		ArrayList result = geefMatrixRij(s); 
		ArrayList<Expressie> rij = (ArrayList<Expressie>) result.get(0);
		s = (String) result.get(1); // string zonder rij
		
		while (rij != null && rij.toString() != "")
		{
			kinderen.add(rij);

			if (!s.equals(""))
			{
				result = geefMatrixRij(s);
				rij = (ArrayList<Expressie>) result.get(0);
				s = (String) result.get(1); // string zonder rij
			}
			else
				break;
		}
		
		return kinderen;
	}

	/**
	 * Kinderen zijn te onderscheiden doordat ze omsloten zijn door haken '(' en ')',
	 * haken gescheiden met '*'.
	 * Retourneert in een arraylist de kindexpressie 
	 * en de string zonder het kind t.b.v. verder parsen.
	 *  
	 * @param s
	 * @return
	 */
	private static ArrayList<Object> geefVectorKind(String s)
	{
		ArrayList<Object> result = new ArrayList<Object>();
		
		int beginIndex = s.indexOf("(");
		int endIndex = getIndexSluithaak(s, beginIndex);
		String kindString = s.substring(beginIndex + 1, endIndex);
		s = s.substring(s.indexOf(kindString) + kindString.length() + 1); // + 1 voor sluithaak ')'
		// vectorkind-scheidingsteken '*'
		int asterixIndex = s.indexOf("*"); 
		if (asterixIndex > -1)
			s = s.substring(asterixIndex + 1); // +1 voor '('
		
		Expressie kind = parse(kindString); // in kindString staat geen formule ("1+1"), parse maakt er een Expressie van (Optelling)
		result.add(kind); // het geparste kind
		result.add(s); // string zonder kind
		
		return result;
	}

	/**
	 * Kinderen zijn te onderscheiden doordat ze omsloten zijn door haken '(' en ')',
	 * haken gescheiden met '*'.
	 * Retourneert in een arraylist de kindexpressie 
	 * en de string zonder het kind t.b.v. verder parsen.
	 *  
	 * @param s
	 * @return
	 */
	private static ArrayList<Object> geefMatrixRijKind(String s)
	{
		ArrayList<Object> result = new ArrayList<Object>();
		
		int beginIndex = s.indexOf("(");
		int endIndex = getIndexSluithaak(s, beginIndex);
		String kindString = s.substring(beginIndex + 1, endIndex);
		s = s.substring(s.indexOf(kindString) + kindString.length() + 1); // + 1 voor sluithaak ')'
		// vectorkind-scheidingsteken '*'
		int asterixIndex = s.indexOf("*"); 
		if (asterixIndex > -1)
			s = s.substring(asterixIndex + 1); // +1 voor '('
		
		if ("".equals(kindString))
			kindString = "0"; // als niets is ingevuld, dan vullen we 0 in
		Expressie kind = parse(kindString); // in kindString staat geen formule ("1+1"), parse maakt er een Expressie van (Optelling)
		result.add(kind); // het geparste kind
		result.add(s); // string zonder kind
		
		return result;
	}

	/**
	 * Kinderen binnen rijen zijn te onderscheiden doordat ze omsloten zijn door 
	 * haken '(' en ')', haken gescheiden met '*'. 
	 * Rijen zijn omsloten door haken en gescheiden met '*'. 
	 * Retourneert in een arraylist de rij-arraylist 
	 * en de string zonder de rij t.b.v. verder parsen.
	 *  
	 * @param s
	 * @return
	 */
	private static ArrayList<Object> geefMatrixRij(String s)
	{
		ArrayList<Object> result = new ArrayList<Object>();
		
		int beginIndex = s.indexOf("(");
		int endIndex = getIndexSluithaak(s, beginIndex);
		String rijString = s.substring(beginIndex + 1, endIndex);
		s = s.substring(s.indexOf(rijString) + rijString.length() + 1); // + 1 voor sluithaak ')'
		// vectorkind-scheidingsteken '*'
		int asterixIndex = s.indexOf("*"); 
		if (asterixIndex > -1)
			s = s.substring(asterixIndex + 1); // +1 voor '('
		
		ArrayList<Expressie> rij = parseMatrixRijKinderen(rijString);
		result.add(rij); // rij-arraylist
		result.add(s); // string zonder rij
		
		return result;
	}

	/**
	 * Retourneert de index van de sluithaak die hoort bij de
	 * openingshaak met de gegeven beginIndex in de string.
	 * 
	 * @param s
	 * @param beginIndex
	 * @return
	 */
	private static int getIndexSluithaak(String s, int beginIndex)
	{
		int endIndex = -1;
		// houdt bij of er na beginIndex haakjes worden geopend die nog niet gesloten zijn
		int haakjesGeopendCount = 0;
		int i = beginIndex + 1;
		
		while (i < s.length())
		{
			if (s.charAt(i) == ')')
			{
				if (haakjesGeopendCount == 0)
				{
					// sluithaak gevonden
					endIndex = i;
					break;
				}
				else
					haakjesGeopendCount--;
			}
			else
			{
				if (s.charAt(i) == '(')
					haakjesGeopendCount++;
			}

			i++;
		}
		
		return endIndex;
	}

	public static Expressie geefExpressie(String codeString)
	{
//		System.out.println("FormuleParser.geefExpressie(" + codeString + ")");
		
		return parse(schoon(formuleString(codeString)));
	}
	
	public static Expressie geefExpressie(String codeString, FunctieMVDefSet fds) //FunctieDefSet
	{	
//		System.out.println("----- FormuleParser.geefExpressie(" + codeString + ")");
    	
		FunctieMV.setFunctieMVDefSet(fds);
		Expressie e = parse(schoon(formuleString(codeString)));

//		System.out.println("----- FormuleParser.geefExpressie(" + codeString + ") = " + e);
		
		FunctieMV.setFunctieMVDefSet(null);
		return e;
	}
	
	public static Expressie geefExpressie(String codeString, boolean woordformule)
	{	return parse(schoon(formuleString(codeString),woordformule),woordformule);
	}
	
	public static Expressie geefExpressie(String codeString, boolean woordformule, FunctieMVDefSet fds) //FunctieDefSet
	{	//Functie.setFunctieDefSet(fds);
		FunctieMV.setFunctieMVDefSet(fds);
		Expressie e = parse(schoon(formuleString(codeString),woordformule),woordformule);
		//Functie.setFunctieDefSet(null);
		FunctieMV.setFunctieMVDefSet(null);
		return e;
	}
	
	public static String randomizeString(String s, String[] randomVars, Hashtable randomValues) throws Exception
	{	for(int i=s.length()-1 ; i>-1; i--)
		{	if(s.charAt(i)=='@')
			{	int index = s.substring(0,i).lastIndexOf("$f");
				String formString = s.substring(index,i+1);
				for(int j=formString.length()-1 ; j>-1; j--)
				{	if(formString.charAt(j)=='\u00A9')
					{	int index1 = formString.substring(0,j).lastIndexOf("\u00A9");
						String parseString = formString.substring(index1+1,j);
						parseString = substitueerRandom(parseString, randomVars, randomValues, false);
						formString = ""+formString.substring(0,index1)+parseString+formString.substring(j+1);
						j=index1;
					}	
				}
				for(int j=formString.length()-1 ; j>-1; j--)
				{	if(formString.charAt(j)=='#')
					{	int index1 = formString.substring(0,j).lastIndexOf("#");
						String parseString = formString.substring(index1+1,j);
						parseString = substitueerRandom(parseString, randomVars, randomValues);
						formString = ""+formString.substring(0,index1)+parseString+formString.substring(j+1);
						j=index1;
					}	
				}		
				s = ""+s.substring(0,index)+formString+s.substring(i+1);
				i=index;
			}
		}
		return s;
	}
	
	public static String randomizeTekstVakString(String tekst, String[] randomVars, Hashtable randomValues) throws Exception
	{	for(int i=tekst.length()-1 ; i>-1; i--)
		{	if(tekst.charAt(i)=='@')
			{	int index = tekst.substring(0,i).lastIndexOf("$f");
				
				int indexF = tekst.substring(0,i).lastIndexOf("$f");
				int indexA = tekst.substring(0,i).lastIndexOf("$A");
				int indexV = tekst.substring(0,i).lastIndexOf("$V");
				int indexH = tekst.substring(0,i).lastIndexOf("$H");
				int indexI = tekst.lastIndexOf("$I", i);
				index = Math.max(indexF, indexA);
				index = Math.max(index, indexV);
				index = Math.max(index, indexH);
				index = Math.max(index, indexI);
				
				String formString = tekst.substring(index,i+1);
				for(int j=formString.length()-1 ; j>-1; j--)
				{	if(formString.charAt(j)=='#')
					{	int index1 = formString.substring(0,j).lastIndexOf("#");
					    if(index1 >= 0) {
					    	String parseString = formString.substring(index1+1,j);
					    	parseString = FormuleParser.substitueerRandom(parseString, randomVars, randomValues);
					    	formString = ""+formString.substring(0,index1)+parseString+formString.substring(j+1);
					    	j=index1;
					    } else {
					    	break;
					    }
					}	
				}	
				for(int j=formString.length()-1 ; j>-1; j--)
				{	if(formString.charAt(j)=='\u00A9')
					{	int index1 = formString.substring(0,j).lastIndexOf("\u00A9");
						String parseString = formString.substring(index1+1,j);
						parseString = FormuleParser.substitueerRandom(parseString, randomVars, randomValues,false);
						formString = ""+formString.substring(0,index1)+parseString+formString.substring(j+1);
						j=index1;
					}	
				}
				tekst = ""+tekst.substring(0,index)+formString+tekst.substring(i+1);
				
				i=index;
			}
		}
		return tekst;
	}
	
	public static String substitueerRandom(String formString, String[] varnamen, Hashtable waarden)
	{	return substitueerRandom(formString,  varnamen,  waarden, true);
	}
	
	public static String substitueerRandom(String formString, String[] varnamen, Hashtable waarden, boolean breukenGemengd)
	{	String sNieuw = null;
		String s1Nieuw = null;
		String s2Nieuw = null;
		Expressie e = null;
		Expressie e1 = null;
		Expressie e2 = null;
		boolean parseable = true;
		int n = formString.indexOf("=");
		if(n>-1)
		{	FormuleParser p = new FormuleParser();
			String s1 = formString.substring(0,n);
			e1 = geefExpressie("$f" + s1 + "@");
			if(e1==null)parseable = false;
			if(parseable)
			{	for(int j=0 ; j<varnamen.length; j++)
				{	int value = ((Number)waarden.get(varnamen[j])).intValue();
					e1 = e1.substitueer(value,varnamen[j]);
				}
				e1 = Algebra.herleidMild(e1, breukenGemengd);
				s1Nieuw = e1.toString();
			}
			else 
			{	s1Nieuw = s1;
			}
			
			parseable = true;
			String s2 = formString.substring(n+1);
			e2 = p.parse(p.schoon(p.formuleString("$f" + s2 + "@")));
			if(e2==null)parseable = false;
			if(parseable)
			{	for(int j=0 ; j<varnamen.length; j++)
				{	int value = ((Number)waarden.get(varnamen[j])).intValue();
					e2 = e2.substitueer(value,varnamen[j]);
				}
				e2 = Algebra.herleidMild(e2, breukenGemengd);			
				s2Nieuw = e2.toString();
			}
			else 
			{	s2Nieuw = s2;
			}
			sNieuw = s1Nieuw + "=" + s2Nieuw;
		}
		else 
		{	String s = formString;
			//System.out.println("voor: "+formString);
			FormuleParser p = new FormuleParser();
			
			e = p.parse(p.schoon(p.formuleString("$f" + s + "@")));
			//System.out.println("na: "+e);
			if(e==null)parseable = false;
			if(parseable)
			{	for(int j=0 ; j<varnamen.length; j++)
				{	int value = ((Number)waarden.get(varnamen[j])).intValue();
					e = e.substitueer(value,varnamen[j]);
				}
				e = Algebra.herleidMild(e, breukenGemengd);
				sNieuw = e.toString();
			}
			else 
			{	sNieuw = s;
			}
		}
		
		//System.out.println(formString);
		//System.out.println(sNieuw);
		return sNieuw;
	}
	
	
	
}

