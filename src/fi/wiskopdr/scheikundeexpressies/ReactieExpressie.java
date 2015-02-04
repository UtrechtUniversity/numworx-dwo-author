package fi.wiskopdr.scheikundeexpressies;

import java.util.Vector;

/*
 * Het deel van een reactievergelijking voor of na de pijl. 
 * Bestaat uit een optelling van verschillende moleculen.
 */
public class ReactieExpressie {

	Molecuul[] moleculen;
	String[] atoomNamen;
	double[] aantallen;
	
	public ReactieExpressie(Molecuul[] moleculen, double[] aantallen)
	{
		this.moleculen = moleculen;
		this.aantallen = aantallen;
		bepaalAtoomNamen();
	}
	
	public ReactieExpressie(Molecuul molecuul, double aantal)
	{
		moleculen = new Molecuul[1];
		aantallen = new double[1];
		moleculen[0] = molecuul;
		aantallen[0] = aantal;
		bepaalAtoomNamen();
	}
	
	public ReactieExpressie (ReactieExpressie expressie1, ReactieExpressie expressie2)
	{
		moleculen = new Molecuul[expressie1.moleculen.length + expressie2.moleculen.length];
		aantallen = new double[moleculen.length];
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
			for(int j = 0; j < moleculen[i].atoomDelen.length; j++)
			{	String s = moleculen[i].atoomDelen[j].atoomNaam;
				if(!v.contains(s))
					v.add(s);
			
			}
		}
		atoomNamen = new String[v.size()];
		for(int i = 0; i < atoomNamen.length; i++)
			atoomNamen[i] = v.get(i);
	}
	
	/*
	 * Bepaal voor deze expressie het aantal elementen van een atoomsoort
	 */
	public int geefAantalAtoomElementen(String naam)
	{
		int aantalElementen = 0;
		for(int i = 0; i < moleculen.length; i++)
		{
			aantalElementen += aantallen[i] * moleculen[i].geefAantalAtoomElementen(naam);
		}
		return aantalElementen;
	}
	
	/*
	 * Bepaal voor deze expressie de totale lading van een atoomsoort 
	 */
	public int geefTotaleLading()
	{
		int totaleLading = 0;
		for(int i = 0; i < moleculen.length; i++)
		{
			totaleLading += aantallen[i] * moleculen[i].lading;
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
					molecuulKomtVoor =true;
					if(aantallen[i] != exp.aantallen[j])
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
		if(exp.moleculen.length != moleculen.length)
			return false;
		
		for(int i = 0; i < moleculen.length; i++)
		{	boolean molecuulKomtVoor = false;
			for(int j = 0; j < exp.moleculen.length; j++)
			{
				if(moleculen[i].isGelijkwaardigZonderLading(exp.moleculen[j]))
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
		if(exp.moleculen.length != moleculen.length)
			return false;
		
		for(int i = 0; i < moleculen.length; i++)
		{	boolean molecuulKomtVoor = false;
			for(int j = 0; j < exp.moleculen.length; j++)
			{
				if(moleculen[i].isGelijkwaardig(exp.moleculen[j]))
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
