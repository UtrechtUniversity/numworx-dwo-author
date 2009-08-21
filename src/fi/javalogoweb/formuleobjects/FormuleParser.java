package fi.javalogoweb.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;
import java.util.Vector;
import fi.javalogoweb.expressies.*;
//import fi.wiskopdr.WiskOpdr;
//import fi.beans.stringutils.*;

public class FormuleParser
{		
	public FormuleParser()
	{	
	}
		
	public static VergelijkingMeerv parseVergelijking(String s)
	{	try
		{
			s = s.substring(2,s.length()-1);
			if(s.length()==0)return null;
			String[] vergelijkingStrings = null;//StringUtils.split(s,WiskOpdr.rb.getString("ofLabel"));
			Vergelijking[] vergelijkingen = new Vergelijking[vergelijkingStrings.length];
			
			for(int j=0 ; j<vergelijkingStrings.length; j++) 
			{	int index1 = vergelijkingStrings[j].indexOf("[");
		    	int index2 = vergelijkingStrings[j].indexOf("]");
				if(index1>-1 && index2>index1)
				{	
					String[] expressieStrings = null;//StringUtils.split(vergelijkingStrings[j].substring(index1+1,index2),":");
					if(expressieStrings.length==3)
					{	Expressie e1 = parse(schoon(formuleString("$f" + expressieStrings[0] + "@")));
			    		Expressie e2 = parse(schoon(formuleString("$f" + expressieStrings[1] + "@")));
			    		Expressie e3 = parse(schoon(formuleString("$f" + expressieStrings[2] + "@")));
			    		if(e1!=null && e2!=null && e3!=null && e1.isWaarde() && e2.isWaarde() && e3.isWaarde())
				    	{	vergelijkingen[j] = new Vergelijking(new BasisExpressie("Q"),new Vermenigvuldiging(e1,new Optelling(e2,e3)), "=");
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
			    {	String[] expressieStrings  = null;//StringUtils.split(vergelijkingStrings[i],vergTekens[j]);
			    	if(expressieStrings.length==2)
			    	{	if(expressieStrings[1].equals("geen")) expressieStrings[1] = "0.1234567";
			    		//System.out.println(expressieStrings[1]);
			    		Expressie e1 = parse(schoon(formuleString("$f" + expressieStrings[0] + "@")));
				    	Expressie e2 = parse(schoon(formuleString("$f" + expressieStrings[1] + "@")));
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
			    if(!split && vergelijkingen[i]==null)return null;
			}
			return new VergelijkingMeerv(vergelijkingen); 
			
		}
		catch(Exception e)
		{
			return null;
		}
	}
	

	public static String formuleString(String s)
	{	s = "(" + s.substring(2,s.length()-1) + ")";
		
		
		int n = s.indexOf("$n");
		while(n>-1)
		{	int index$ = n-1;
			int niv = 0;
			while(!(s.charAt(index$)=='$' && niv==0))
			{	if(s.charAt(index$)=='$')niv--;
				else if(s.charAt(index$)=='@')niv++;
				index$--;
			}
			int indexAt = n+2;
			niv = 0;
			while(!(s.charAt(indexAt)=='@' && niv==0))
			{	if(s.charAt(indexAt)=='$')niv++;
				else if(s.charAt(indexAt)=='@')niv--;
				indexAt++;
			}
			if(s.charAt(index$+1)=='b')s = s.substring(0,n) + ")/(" + s.substring(n+2,indexAt) + ")" + s.substring(indexAt+1);
			else if(s.charAt(index$+1)=='o')s = s.substring(0,n) + ")+(" + s.substring(n+2,indexAt) + ")" + s.substring(indexAt+1);
			else if(s.charAt(index$+1)=='a')s = s.substring(0,n) + ")-(" + s.substring(n+2,indexAt) + ")" + s.substring(indexAt+1);
			else if(s.charAt(index$+1)=='v')s = s.substring(0,n) + ")*(" + s.substring(n+2,indexAt) + ")" + s.substring(indexAt+1);
			else if(s.charAt(index$+1)=='p')s = s.substring(0,n) + ")^(" + s.substring(n+2,indexAt) + ")" + s.substring(indexAt+1);
			else if(s.charAt(index$+1)=='W')s = s.substring(0,n) + ")|(" + s.substring(n+2,indexAt) + ")" + s.substring(indexAt+1);
			else if(s.charAt(index$+1)=='L')s = s.substring(0,n) + ")~(" + s.substring(n+2,indexAt) + ")" + s.substring(indexAt+1);
			n = s.indexOf("$n");
		}
		
		n = s.indexOf("$w");
		while(n>-1)
		{	s = s.substring(0,n) + "sqrt(" + s.substring(n+2);
			n = s.indexOf("$w");
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
		n = s.indexOf("@");
		while(n>-1)
		{	s = s.substring(0,n) + ")" + s.substring(n+1);
			n = s.indexOf("@");
		}
		n = s.indexOf("·");
		while(n>-1)
		{	s = s.substring(0,n) + "*" + s.substring(n+1);
			n = s.indexOf("·");
		}
		return s;	
	}
	
	public static String schoon(String s)
	{	int index = 0;
		s = s.replace(',','.');
		s = s.replace(':','/');
		
		while(index >-1)
		{	index = s.indexOf(" ");
			//if(index>0 && index<s.length()-1 && Character.isDigit(s.charAt(index-1)) && Character.isDigit(s.charAt(index+1)))
			//{	s = s.substring(0,index) + "+" + s.substring(index+1);
			//}
			//else 
			if(index >-1)s = s.substring(0,index) + s.substring(index+1);
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
			
			int tel = index+2;
			while(tel<s.length() && Character.isDigit(s.charAt(tel)))
			{	tel++;
			}
			if(index >-1 && tel>index+2) s = s.substring(0,index) + "(" + s.substring(index+1,tel) + ")" + s.substring(tel);
			
			if(index >-1 && index+2<s.length() && Character.isLetter(s.charAt(index+2))) 
			{	tel = index+3;
				s = s.substring(0,index) + "(-1)" + s.substring(index+2);
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
		/*for(int i=0 ; i<s.length()-1 ; i++)
		{	if((Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)) || s.charAt(i)==')') && (Character.isLetter(s.charAt(i+1)) || s.charAt(i+1)=='('))
			{	s = s.substring(0,i+1) + '*' +  s.substring(i+1);
			}
			else if(s.charAt(i)==')' && Character.isDigit(s.charAt(i+1)))
			{	s = s.substring(0,i+1) + '*' +  s.substring(i+1);
			}
		}*/
		index = 0;
		while(index >-1)
		{	index = s.indexOf("s*q*r*t*");
			if(index >-1)s = s.substring(0,index) + "sqrt" + s.substring(index+8);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("a*r*c*s*i*n*");
			if(index >-1)s = s.substring(0,index) + "arcsin" + s.substring(index+12);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("a*r*c*s*i*n");
			if(index >-1)s = s.substring(0,index) + "arcsin" + s.substring(index+11);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("a*r*c*c*o*s*");
			if(index >-1)s = s.substring(0,index) + "cos" + s.substring(index+12);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("a*r*c*c*o*s");
			if(index >-1)s = s.substring(0,index) + "cos" + s.substring(index+11);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("a*r*c*t*a*n*");
			if(index >-1)s = s.substring(0,index) + "tan" + s.substring(index+12);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("a*r*c*t*a*n");
			if(index >-1)s = s.substring(0,index) + "tan" + s.substring(index+11);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("s*i*n*");
			if(index >-1)s = s.substring(0,index) + "sin" + s.substring(index+6);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("s*i*n");
			if(index >-1)s = s.substring(0,index) + "sin" + s.substring(index+5);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("c*o*s*");
			if(index >-1)s = s.substring(0,index) + "cos" + s.substring(index+6);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("c*o*s");
			if(index >-1)s = s.substring(0,index) + "cos" + s.substring(index+5);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("t*a*n*");
			if(index >-1)s = s.substring(0,index) + "tan" + s.substring(index+6);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("t*a*n");
			if(index >-1)s = s.substring(0,index) + "tan" + s.substring(index+5);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("l*o*g*");
			if(index >-1)s = s.substring(0,index) + "log" + s.substring(index+6);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("l*o*g");
			if(index >-1)s = s.substring(0,index) + "log" + s.substring(index+5);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("l*n*");
			if(index >-1)s = s.substring(0,index) + "ln" + s.substring(index+4);
		}
		index = 0;
		while(index >-1)
		{	index = s.indexOf("l*n");
			if(index >-1)s = s.substring(0,index) + "ln" + s.substring(index+3);
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
		
		return s;	
	}
	
	public static String pel(String s)
	{	boolean pelbaar = true;
		while(pelbaar)
		{	if(s.charAt(0)=='(' && s.charAt(s.length()-1)==')')
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
		return s;
	}
	
	public static Expressie parse(String s)
	{	Expressie exp = null;
		//verwijder overbodige haakjes
		try
		{
		boolean pelbaar = true;
		while(pelbaar)
		{	if(s.charAt(0)=='(' && s.charAt(s.length()-1)==')')
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
		
		//is het een letter?		
		boolean startMetLetter = true;
		startMetLetter = Character.isLetter(s.charAt(0));
		boolean basisString = true;
		for(int i=1 ; i<s.length() ; i++)
		{	basisString = startMetLetter && (Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)));
			if(!basisString) break;
		}
		
		//if(s.length()==1 && Character.isLetter(s.charAt(0)))
		if(basisString)
		{	if(s.length()==1 && s.charAt(0)=='e')exp = new E();
			else if(s.length()==1 && s.charAt(0)=='\u03C0')exp = new PI();
			else exp = new BasisExpressie(s);
			return exp;
		}
		
		//is het een getal?
		boolean isGetal = true;
		try
		{	Double d = Double.valueOf(s);
		}
		catch(NumberFormatException nfe)
		{	isGetal = false;
		}
		if(isGetal)
		{	exp = new BasisExpressie(Double.valueOf(s).doubleValue());
			return exp;
		}
		
		//is het een optelling, aftrekking, enz?
		/*char[] operatoren = {'+','-','*','/','^','|'};
		for(int j=0 ; j<operatoren.length ; j++)
		{	int niv = 0;
			for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(s.charAt(i)==operatoren[j] && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					if(j==0)return new Optelling(e1,e2);
					else if(j==1)return new Aftrekking(e1,e2);
					else if(j==2)return new Vermenigvuldiging(e1,e2);
					else if(j==3)return new Deling(e1,e2);
					else if(j==4)return new Macht(e1,e2);
					else if(j==5)return new NdeWortel(e1,e2);
					return exp;
				}
			}
		}*/
		
		int niv = 0;
			for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(s.charAt(i)=='+' && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Optelling(e1,e2);
					
				}
				//else return exp;
			}
		
		niv = 0;
			for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(s.charAt(i)=='-' && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Aftrekking(e1,e2);
					
				}
				//else return exp;
			}
		niv = 0;
		if(s.length()>4)
		{	for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(i<s.length()-4 && s.substring(i,i+4).equals("*sin") && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Vermenigvuldiging(e1,e2);
					
				}
				//else return exp;
			}
		}
		niv = 0;
		if(s.length()>4)
		{	for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(i<s.length()-4 && s.substring(i,i+4).equals("*cos") && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Vermenigvuldiging(e1,e2);
					
				}
				//else return exp;
			}
		}
		niv = 0;
		if(s.length()>4)
		{	for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(i<s.length()-4 && s.substring(i,i+4).equals("*tan") && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Vermenigvuldiging(e1,e2);
					
				}
				//else return exp;
			}
		}
		
		niv = 0;
		if(s.length()>7)
		{	for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(i<s.length()-7 && s.substring(i,i+7).equals("*arcsin") && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Vermenigvuldiging(e1,e2);
					
				}
				//else return exp;
			}
		}
		niv = 0;
		if(s.length()>7)
		{	for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(i<s.length()-7 && s.substring(i,i+7).equals("*arccos") && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Vermenigvuldiging(e1,e2);
					
				}
				//else return exp;
			}
		}
		niv = 0;
		if(s.length()>7)
		{	for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(i<s.length()-7 && s.substring(i,i+7).equals("*arctan") && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Vermenigvuldiging(e1,e2);
					
				}
				//else return exp;
			}
		} 
		 
		niv = 0;
		if(s.length()>4)
		{	for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(i<s.length()-4 && s.substring(i,i+4).equals("*log") && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Vermenigvuldiging(e1,e2);
					
				}
				//else return exp;
			}
		}
		niv = 0;
		if(s.length()>3)
		{	for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(i<s.length()-3 && s.substring(i,i+3).equals("*ln") && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Vermenigvuldiging(e1,e2);
					
				}
				//else return exp;
			}
		}
		
		
		
		niv = 0;
		if(s.length()>4 && s.substring(0,4).equals("sin^"))
		{	for(int i=4 ; i<s.length() ; i++)
			{	if(s.charAt(i)=='(')
				{	niv++;
				}
				else if(s.charAt(i)==')')
				{	niv--;
				}
				else if(s.substring(i,i+1).equals("*") && niv==0)
				{	
					Expressie e1 = parse(s.substring(4,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Macht(new Sinus(e2),e1);
				}
				
			}
			return null;
		}
		
		niv = 0;
		if(s.length()>4 && s.substring(0,4).equals("cos^"))
		{	for(int i=4 ; i<s.length() ; i++)
			{	if(s.charAt(i)=='(')
				{	niv++;
				}
				else if(s.charAt(i)==')')
				{	niv--;
				}
				else if(s.substring(i,i+1).equals("*") && niv==0)
				{	
					Expressie e1 = parse(s.substring(4,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Macht(new Cosinus(e2),e1);
				}
				
			}
			return null;
		}
		
		niv = 0;
		if(s.length()>4 && s.substring(0,4).equals("log^"))
		{	for(int i=4 ; i<s.length() ; i++)
			{	if(s.charAt(i)=='(')
				{	niv++;
				}
				else if(s.charAt(i)==')')
				{	niv--;
				}
				else if(s.substring(i,i+1).equals("*") && niv==0)
				{	
					Expressie e1 = parse(s.substring(4,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Macht(new Log(e2),e1);
				}
				
			}
			return null;
		}
		
		niv = 0;
		if(s.length()>3 && s.substring(0,3).equals("ln^"))
		{	for(int i=3 ; i<s.length() ; i++)
			{	if(s.charAt(i)=='(')
				{	niv++;
				}
				else if(s.charAt(i)==')')
				{	niv--;
				}
				else if(s.substring(i,i+1).equals("*") && niv==0)
				{	
					Expressie e1 = parse(s.substring(3,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Macht(new Ln(e2),e1);
				}
				
			}
			return null;
		}
		
		if(s.length()>6 && s.substring(0,6).equals("arcsin") && s.charAt(6) != '(')
		{	Expressie e = parse(s.substring(6));
			if(e==null)return null;
			return new ArcSinus(e);
		}
		
		if(s.length()>6 && s.substring(0,6).equals("arccos") && s.charAt(6) != '(')
		{	Expressie e = parse(s.substring(6));
			if(e==null)return null;
			return new ArcCosinus(e);
		}
		
		if(s.length()>6 && s.substring(0,6).equals("arctan") && s.charAt(6) != '(')
		{	Expressie e = parse(s.substring(6));
			if(e==null)return null;
			return new ArcTangens(e);
		}
		
		if(s.length()>3 && s.substring(0,3).equals("sin") && s.charAt(3) != '(')
		{	Expressie e = parse(s.substring(3));
			if(e==null)return null;
			return new Sinus(e);
		}
		
		if(s.length()>3 && s.substring(0,3).equals("cos") && s.charAt(3) != '(')
		{	Expressie e = parse(s.substring(3));
			if(e==null)return null;
			return new Cosinus(e);
		}
		
		if(s.length()>3 && s.substring(0,3).equals("tan") && s.charAt(3) != '(')
		{	Expressie e = parse(s.substring(3));
			if(e==null)return null;
			return new Tangens(e);
		}
		
		if(s.length()>3 && s.substring(0,3).equals("log") && s.charAt(3) != '(')
		{	Expressie e = parse(s.substring(3));
			if(e==null)return null;
			return new Log(e);
		}
		
		if(s.length()>2 && s.substring(0,2).equals("ln") && s.charAt(2) != '(')
		{	Expressie e = parse(s.substring(2));
			if(e==null)return null;
			return new Ln(e);
		}
		
		
		
		 niv = 0;
			for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(s.charAt(i)=='*' && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Vermenigvuldiging(e1,e2);
					
				}
				
			}
		
		 niv = 0;
			for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(s.charAt(i)=='/' && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Deling(e1,e2);
					
				}
			}
		
		niv = 0;
			for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(s.charAt(i)=='^' && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new Macht(e1,e2);
					
				}
			}
			
		niv = 0;
			for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(s.charAt(i)=='|' && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new NdeWortel(e1,e2);
					
				}
			}
			
		niv = 0;
			for(int i=s.length()-1 ; i>-1 ; i--)
			{	if(s.charAt(i)==')')
				{	niv++;
				}
				else if(s.charAt(i)=='(')
				{	niv--;
				}
				else if(s.charAt(i)=='~' && niv==0)
				{	Expressie e1 = parse(s.substring(0,i));
					Expressie e2 = parse(s.substring(i+1));
					if(e1==null || e2==null)return null;
					return new NdeLog(e1,e2);
					
				}
			}
		
		
		//is het een wortel
		if(s.substring(0,4).equals("sqrt"))
		{	Expressie e = parse(s.substring(4,s.length()));
			if(e==null)return null;
			return new Wortel(e);
		}
//		is het een arcsinus
		if(s.length()>7 && s.substring(0,7).equals("arcsin("))
		{	Expressie e = parse(s.substring(6,s.length()));
			if(e==null)return null;
			return new ArcSinus(e);
		}
		//is het een arccosinus
		if(s.length()>7 && s.substring(0,7).equals("arccos("))
		{	Expressie e = parse(s.substring(6,s.length()));
			if(e==null)return null;
			return new ArcCosinus(e);
		}
		//is het een arctangens
		if(s.length()>7 && s.substring(0,7).equals("arctan("))
		{	Expressie e = parse(s.substring(6,s.length()));
			if(e==null)return null;
			return new ArcTangens(e);
		}
		//is het een sinus
		if(s.length()>4 && s.substring(0,4).equals("sin("))
		{	Expressie e = parse(s.substring(3,s.length()));
			if(e==null)return null;
			return new Sinus(e);
		}
		//is het een cosinus
		if(s.length()>4 && s.substring(0,4).equals("cos("))
		{	Expressie e = parse(s.substring(3,s.length()));
			if(e==null)return null;
			return new Cosinus(e);
		}
		//is het een tangens
		if(s.length()>4 && s.substring(0,4).equals("tan("))
		{	Expressie e = parse(s.substring(3,s.length()));
			if(e==null)return null;
			return new Tangens(e);
		}
		//is het een log
		if(s.length()>4 && s.substring(0,4).equals("log("))
		{	Expressie e = parse(s.substring(3,s.length()));
			if(e==null)return null;
			return new Log(e);
		}
		//is het een ln
		if(s.length()>3 && s.substring(0,3).equals("ln("))
		{	Expressie e = parse(s.substring(2,s.length()));
			if(e==null)return null;
			return new Ln(e);
		}/**/
		}
		catch(Exception e)
		{}
		return exp;
		//return s;
	}
	
	public static Expressie geefExpressie(String codeString)
	{	return parse(schoon(formuleString(codeString)));
	}
	
	
}

