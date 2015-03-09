package fi.wiskopdr.expressies;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.expressies.repr.AbstractConverter;

public class Functie extends Expressie  
{	
	public static FunctieDefSet functieDefSet = new FunctieDefSet();
	
	public static void setFunctieDefSet(FunctieDefSet functieDefSet)
	{	Functie.functieDefSet = functieDefSet;
		if(Functie.functieDefSet==null)
			Functie.functieDefSet = new FunctieDefSet();
	}
	
	public static FunctieDefSet getFunctieDefSet()
	{	return functieDefSet;
	}
	
	private String functieNaam;
	private String functieVariabele;
	private Expressie functieExpressie;
	
	public Functie(String functieNaam, Expressie e1 )
	{	this.functieNaam = functieNaam;
		if(functieNaam!=null && functieDefSet.functieExpressies.containsKey(functieNaam))
			functieExpressie = functieDefSet.functieExpressies.get(functieNaam);
		if(functieNaam!=null && functieDefSet.functieVariabelen.containsKey(functieNaam))
			functieVariabele = functieDefSet.functieVariabelen.get(functieNaam);
		
		kind1 = e1;
		isVeelterm = false;
		isProdukt = false;
		isBasis = false;
	}
	
	public String geefFunctieNaam()
	{	return functieNaam;
	}
	
	public Expressie geefDiff(BasisExpressie basisExp)
	{	if(kind1!=null)
		{	return new Vermenigvuldiging(kind1.geefDiff(basisExp),functieExpressie.geefDiff(new BasisExpressie(functieVariabele)).substitueer(kind1, functieVariabele));
		}
		return null;	
	}
	
	public double geefWaarde()
	{	return functieExpressie.substitueer(kind1.geefWaarde(), functieVariabele).geefWaarde();
	}
	
	public double geefWaarde(double subst)
	{	return functieExpressie.substitueer(kind1.geefWaarde(subst), functieVariabele).geefWaarde();
	}
	
	public Complex geefWaardeComplex()
	{	//Complex c1 = kind1.geefWaardeComplex();
		//if(c1==null) return null;
		//return Complex.sin(c1);
		return functieExpressie.substitueer(kind1.geefWaardeComplex().getReal(), functieVariabele).geefWaardeComplex();
		// nog niet goed
	}
	
	public Complex geefWaardeComplex(Complex subst)
	{	//return Complex.sin(kind1.geefWaardeComplex(subst));
		return functieExpressie.substitueer(kind1.geefWaardeComplex(subst).getReal(), functieVariabele).geefWaardeComplex();
		// nog niet goed
	}
	
	public double geefWaarde(double[] subst, String[] vars)
	{	return functieExpressie.substitueer(kind1.geefWaarde(subst,vars), functieVariabele).geefWaarde();
	}
	
	public Expressie substitueer(double subst, String var)
	{	//return new Sinus(kind1.substitueer(subst,var));
		return functieExpressie.substitueer(kind1.substitueer(subst,var), functieVariabele);
	}
	
	public Expressie substitueer(Expressie subst, String var)
	{	//return new Sinus(kind1.substitueer(subst,var));
		return functieExpressie.substitueer(kind1.substitueer(subst,var), functieVariabele);
	}
	
	public Expressie vervangDifferentialen(String var)
	{	return new Functie(functieNaam, kind1.vervangDifferentialen(var));
	}
	
	public Expressie vervangDiffs(Expressie subst, String var)
	{
		return new Functie(functieNaam, kind1.vervangDiffs(subst, var));
	}
		
	public boolean isWaarde(double subst)
	{	return kind1.isWaarde(subst);
	}
	
	public String geefVarNaam()
	{	String s1 = kind1.geefVarNaam();
		if(s1!=null)return s1;
		return null;
	}
	
	public String toString()
	{	return functieNaam + "$h" + kind1.toString() + "@";
	}
	
	public String toStringStrikt()
	{	return functieNaam + "$h" + kind1.toStringStrikt() + "@";
	}
    
    public String toStringCAS()
    {   return functieNaam + "[" + kind1.toStringCAS() + "]";
    }
    
    public Object visit(AbstractConverter converter) {
    	//return converter.sinus(kind1.visit(converter));
    	return functieExpressie.substitueer(kind1, functieVariabele).visit(converter);
    	//lijkt me ?
    }

}
