package fi.wiskopdr.scheikundeobjects;

import java.util.Vector;

import fi.wiskopdr.expressies.*;

/*
 * Het deel van een reactievergelijking voor of na de pijl. 
 * Bestaat uit een optelling van verschillende moleculen.
 */
public class ReactieExpressie {

	Molecuul[] moleculen;
	String[] atoomNamen;
	//double[] aantallen;
	Expressie[] aantallen;
	
	public ReactieExpressie(Molecuul[] moleculen, Expressie[] aantallen)
	{
		this.moleculen = moleculen;
		this.aantallen = aantallen;
		bepaalAtoomNamen();
	}
	
	public ReactieExpressie(Molecuul molecuul, Expressie aantal)
	{
		moleculen = new Molecuul[1];
		aantallen = new Expressie[1];
		moleculen[0] = molecuul;
		aantallen[0] = aantal;
		bepaalAtoomNamen();
	}
	
	public ReactieExpressie (ReactieExpressie expressie1, ReactieExpressie expressie2)
	{
		moleculen = new Molecuul[expressie1.moleculen.length + expressie2.moleculen.length];
		aantallen = new Expressie[moleculen.length];
		for(int i = 0; i < expressie1.moleculen.length; i++)
		{
			moleculen[i] = expressie1.moleculen[i];
			aantallen[i] = expressie1.aantallen[i];
		}
		for(int j = expressie1.moleculen.length; j < moleculen.length; j++)
		{
			moleculen[j] = expressie2.moleculen[j - expressie1.moleculen.length];
			aantallen[j] = expressie2.aantallen[j - expressie1.moleculen.length];
		}
		bepaalAtoomNamen();
	}
	
	public void bepaalAtoomNamen()
	{
		Vector<String> v = new Vector<String>();
		for(int i = 0; i < moleculen.length; i++)
		{
			if(moleculen[i].samengesteldeIonDelen == null)
			{
				for(int j = 0; j < moleculen[i].atoomDelen.length; j++)
				{	String s = moleculen[i].atoomDelen[j].atoomNaam;
					if(!v.contains(s) && !s.equals("e"))
						v.add(s);
				}
			}
			else
			{
				for(int j = 0; j < moleculen[i].samengesteldeIonDelen.length; j++)
				{
					Vector<String> v2 = moleculen[i].samengesteldeIonDelen[j].bepaalAtoomNamen();
					for(int k = 0; k < v2.size(); k++)
					{
						String s = v2.elementAt(k);
						if(!v.contains(s) && !s.equals("e"))
							v.add(s);
					}
				}
				
			}
		}
		atoomNamen = new String[v.size()];
		for(int i = 0; i < atoomNamen.length; i++)
			atoomNamen[i] = v.get(i);
	}
	
	/*
	 * Bepaal voor deze expressie het aantal elementen van een atoomsoort
	 */
	public Expressie geefAantalAtoomElementen(String naam)
	{
		Expressie aantalElementen = new BasisExpressie(0);
		//int aantalElementen = 0;
		for(int i = 0; i < moleculen.length; i++)
		{
			aantalElementen = new Optelling(aantalElementen, new Vermenigvuldiging(aantallen[i], moleculen[i].geefAantalAtoomElementen(naam)));
			//aantalElementen += aantallen[i] * moleculen[i].geefAantalAtoomElementen(naam);
		}
		return aantalElementen;
	}
	
	/*
	 * Bepaal voor deze expressie de totale lading van een atoomsoort 
	 */
	public Expressie geefTotaleLading()
	{
		Expressie totaleLading = new BasisExpressie(0);
		//int totaleLading = 0;
		for(int i = 0; i < moleculen.length; i++)
		{
			totaleLading = new Optelling(totaleLading, new Vermenigvuldiging(aantallen[i], new BasisExpressie(moleculen[i].lading)));
			//totaleLading += aantallen[i] * moleculen[i].lading;
		}
		return totaleLading;
	}
	
	public boolean isGelijkwaardig(ReactieExpressie exp)
	{
		if(exp.moleculen.length != moleculen.length)
			return false;
		
		for(int i = 0; i < moleculen.length; i++)
		{	boolean molecuulKomtVoor = false;
			for(int j = 0; j < exp.moleculen.length; j++)
			{
				if(moleculen[i].isGelijkwaardig(exp.moleculen[j]))
				{
					molecuulKomtVoor = true;
					if(!Double.isNaN(aantallen[i].geefWaarde()) && !Double.isNaN(exp.aantallen[i].geefWaarde()))
					{	if(aantallen[i].geefWaarde() != exp.aantallen[i].geefWaarde())
							return false;
					}
					else if(!Double.isNaN(aantallen[i].geefWaarde()) || !Double.isNaN(exp.aantallen[i].geefWaarde()))
						return false;
					else if(aantallen[i].substitueer(0.54321, "n").geefWaarde() != exp.aantallen[i].substitueer(0.54321, "n").geefWaarde())
						return false;
					break;
				}
			}
			if(!molecuulKomtVoor)
				return false;
		}
		return true;
	}
	
	public boolean isGelijkwaardigMoleculen(ReactieExpressie exp)
	{
		//Elektronen buiten beschouwing laten, die tellen niet mee voor moleculen, maar alleen voor ladingen.
		Molecuul[] moleculen1 = moleculenZonderElektronen(exp.moleculen);
		Molecuul[] moleculen2 = moleculenZonderElektronen(moleculen);
		
		if(moleculen1.length != moleculen2.length)
			return false;
		
		for(int i = 0; i < moleculen2.length; i++)
		{	boolean molecuulKomtVoor = false;
			for(int j = 0; j < moleculen1.length; j++)
			{
				if(moleculen2[i].isGelijkwaardigZonderLading(moleculen1[j]))
				{
					molecuulKomtVoor = true;
					break;
				}
			}
			if(!molecuulKomtVoor)
				return false;
		}
		return true;
	}
	
	public boolean isGelijkwaardigMoleculenLading(ReactieExpressie exp)
	{
		Molecuul[] moleculen1 = moleculenZonderElektronen(exp.moleculen);
		Molecuul[] moleculen2 = moleculenZonderElektronen(moleculen);
		//Molecuul[] moleculen1 = exp.moleculen;
		//Molecuul[] moleculen2 = moleculen;
				
		if(moleculen1.length != moleculen2.length)
			return false;
		
		for(int i = 0; i < moleculen2.length; i++)
		{	boolean molecuulKomtVoor = false;
			for(int j = 0; j < moleculen1.length; j++)
			{
				if(moleculen2[i].isGelijkwaardig(moleculen1[j]))
				{
					molecuulKomtVoor = true;
					break;
				}
			}
			if(!molecuulKomtVoor)
				return false;
		}
		return true;
	}
	
	public Molecuul[] moleculenZonderElektronen(Molecuul[] moleculen)
	{
		int verwijderIndex = -1;
		for(int i = 0; i < moleculen.length; i++)
		{
			Molecuul m = moleculen[i];
			if(m.atoomDelen != null && m.atoomDelen.length == 1 && m.atoomDelen[0].atoomNaam.equals("e"))
			{
				verwijderIndex = i;
				break;
			}
		}
		if(verwijderIndex == -1)
			return moleculen;
		Molecuul[] moleculen1 = new Molecuul[moleculen.length - 1];
		if(verwijderIndex > -1)
		{
			for(int i = 0; i < verwijderIndex; i++)
				moleculen1[i] = moleculen[i];
			for(int i = verwijderIndex + 1; i < moleculen.length; i++)
				moleculen1[i-1] = moleculen[i];
		}
		return moleculen1;
	}
	
	
	public double vereenvoudigFactor(ReactieExpressie exp)
	{
		if(exp.aantallen.length != aantallen.length)
			return -999;
		if(aantallen.length == 0)
			return -999;
		try{
			double factor = new Deling(aantallen[0], exp.aantallen[0]).geefWaarde();
			for(int i = 1; i < aantallen.length; i++)
			{
				double factor2 = new Deling(aantallen[i], exp.aantallen[i]).geefWaarde();
				if(factor2 != factor)
					return -999;
			}
			return factor;
		}
		catch(Exception e){
			return -999;
		}
	}
	
	public String toString()
	{
		String s = "";
		for(int i = 0; i < moleculen.length; i++)
		{
			s = s + aantallen[i] + moleculen[i] + "+";
		}
		s = s.substring(0, s.length() - 1);
		return s;
	}
}
