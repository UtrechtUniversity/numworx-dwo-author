package fi.grafiek3dtest.expressies;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;

import fi.beans.stringutils.*;
import fi.grafiek3dtest.Grafiek3DTest;

public class BasisExpressie extends Expressie  
{	String 	basisString;
	double waarde;
	
	public BasisExpressie()
	{
	}
	
	public BasisExpressie(String s)
	{	super();
		s = s.replace(',','.');
		basisString = s;
		isVeelterm = false;
		isProdukt = false;
		isBasis = true;
		waarde = geefW();
	}

	public BasisExpressie(double d)
	{	super();
		waarde = d;
		basisString = df.format(d);
		if(!Algebra.withinLongRange((long)waarde))basisString = dfe.format(d);
		//if(Math.abs(1.0/waarde)>10000000000.0)basisString = dfe.format(d);
		isVeelterm = false;
		isProdukt = false;
		isBasis = true;
	}
	
	public double geefW()
	{	double waarde = Double.NaN;
		try
		{	waarde = Double.valueOf(basisString).doubleValue();
		}
		catch(NumberFormatException e)
		{	
		}
		return waarde;
	}
	
	public boolean isVar()
	{	return Character.isLetter(basisString.charAt(0));
	}
	
	public double geefWaarde()
	{	return waarde;
	}
	
	public Complex geefWaardeComplex()
	{	if(!Double.isNaN(waarde)) return new Complex(waarde);
		else if(Double.isNaN(waarde) &&  basisString.equals("i"))return new Complex(0,1);
		else return null;
	}

	public double geefWaarde(double subst)
	{	if(Double.isNaN(waarde))return subst;
		else return waarde;
	}
	
	public Complex geefWaardeComplex(Complex subst)
	{	if(basisString.equals("i")) return new Complex(0,1);
		else if(Double.isNaN(waarde))return new Complex(subst);
		else return new Complex(waarde);
	}
	
	public double geefWaarde(double[] subst, String[] vars)
	{	if(!Double.isNaN(waarde))return waarde;
		else 
		{	for(int i=0 ; i<vars.length ; i++)
			{	if(basisString.equals(vars[i]))
				{	return subst[i];
				}
			}
			return Double.NaN;
		}
	}
	
	public Complex geefWaardeComplex(Complex[] subst, String[] vars)
	{	if(!Double.isNaN(waarde))return new Complex(waarde);
		else 
		{	for(int i=0 ; i<vars.length ; i++)
			{	if(basisString.equals("i") &&  basisString.equals(vars[i]))
				{	return new Complex(0,1);
				}
				else if(basisString.equals(vars[i]))
				{	return new Complex(subst[i]);
					
				}
			}
			return null;
		}
	}
	
	public Expressie substitueer(double subst, String var)
	{	if(basisString.equals(var))
		{	return new BasisExpressie(subst);
		}
		else return new BasisExpressie(basisString);
	}
	
	public Expressie substitueer(Expressie subst, String var)
	{	if(basisString.equals(var))
		{	return subst;
		}
		else return new BasisExpressie(basisString);
	}
	
	public boolean isWaarde(double subst)
	{	if(Double.isNaN(geefWaarde()))return false;
		return true;
	}
	
	public String geefVarNaam()
	{	if(Double.isNaN(geefWaarde()))return basisString;
		return null;
	}
	
	public Vector geefVarNamen()
	{	Vector v = new Vector();
		if(Double.isNaN(waarde))v.addElement(basisString);
		return v;
	}
	
	public void setScientificNotation(boolean b, int macht, int signf)
	{
		int digits = basisString.length();
		int intDigits = digits;
		if(basisString.indexOf(',')>-1){
			intDigits = basisString.substring(0,basisString.indexOf(',')).length();
			digits -=1;
		}
		else if(basisString.indexOf('.')>-1){
			intDigits = basisString.substring(0,basisString.indexOf('.')).length();
			digits -=1;
		}
		int fracDigits = digits-intDigits;
		if(signf<intDigits-macht)macht = intDigits-signf;
		
		String formatString = "";
		for(int i=0 ; i<intDigits-macht-1 ; i++ ) formatString = formatString + "0";
		if(signf-intDigits+macht>0)formatString = formatString + "0.";
		else formatString = formatString + "0";
		for(int i=0 ; i<signf-intDigits+macht ; i++ ) formatString = formatString + "0";
		if(macht!=0)formatString = formatString + "E0";
		DecimalFormat dfee = new DecimalFormat(formatString);
		basisString = dfee.format(waarde);
	}
	
	public String toString()
	{	
		String basisStringUit = StringUtils.replaceStr(basisString,"?(","$s");
		basisStringUit = StringUtils.replaceStr(basisStringUit,")","@");
		
		if(!Double.isNaN(waarde) && (!Algebra.withinLongRange((long)waarde) || basisString.indexOf('E')>-1))basisStringUit = StringUtils.replaceStr(basisString,"E","*$p10$n") + "@@";
		//if(!Double.isNaN(waarde) && (Math.abs(1.0/waarde)>10000000000.0))basisStringUit = StringUtils.replaceStr(basisString,"E","*$p10$n") + "@@";
        
        if(Grafiek3DTest.language.toString().equals("nl"))basisStringUit = basisStringUit.replace('.',',');
        
        /*
        if(isWaarde())
        {
        	String[] delen = StringUtils.split(basisStringUit, ",");
        	String deel0Nieuw;
        	for(int i = 0 ; i<delen[0].length() ; i++)
        	{
        		if(i>1 && i%3==0 && i<delen[0].length()-1)
        		{	
        			String sKop = delen[0].substring(0,delen[0].length()-i);
        			String sStaart = delen[0].substring(delen[0].length()-i);
        			if(sStaart.length()>0)deel0Nieuw = sKop+" "+sStaart;
        			else delen[0] = sKop;
        			i++;
        		}
        	}
        	if(delen.length>1) basisStringUit = delen[0] +","+ delen[1];
    		else basisStringUit = delen[0];
        }
        */
		 
        return basisStringUit;
	}
	
	public String toStringStrikt()
	{	
		String basisStringUit = StringUtils.replaceStr(basisString,"?(","$s");
        basisStringUit = StringUtils.replaceStr(basisStringUit,")","@");
        
        if(!Double.isNaN(waarde) && (!Algebra.withinLongRange((long)waarde) || basisString.indexOf('E')>-1))basisStringUit = StringUtils.replaceStr(basisString,"E","*$p10$n") + "@@";
		//if(!Double.isNaN(waarde) && (Math.abs(1.0/waarde)>10000000000.0))basisStringUit = StringUtils.replaceStr(basisString,"E","*$p10$n") + "@@";
        
        if(Grafiek3DTest.language.toString().equals("nl"))basisStringUit = basisStringUit.replace('.',',');
        return basisStringUit;
		//basisString = basisString.replace('.',',');
		//if(isWaarde())
	    //{
	    	/*
	    	 for(int i = 0 ; i<basisString.length() ; i++)
	    	{
	    		if(i>0 && i%3==0 && i<basisString.length()-1)
	    		{	
	    			String sKop = basisString.substring(0,basisString.length()-1-i);
	    			String sStaart = basisString.substring(basisString.length()-1-i);
	    			basisString = sKop+" "+sStaart;
	    		}
	    	}
	    	*/
	    //}
		//return basisString;
	}
    
    public String toStringCAS()
    {   basisString = basisString.replace(',','.');
    	if(basisString.equals("\u221e")) return("Infinity");
    	if(basisString.equals("-\u221e")) return("-Infinity");
        return basisString;
    }

}
