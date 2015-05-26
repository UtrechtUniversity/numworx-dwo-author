package fi.wiskopdr.scheikundeobjects;

import fi.wiskopdr.expressies.*;


/*
 * De klasse specificeert een atoomdeel in een molecuul, met (afgekorte) naam, 
 * lading en aantal deeltjes van dit atoom in het molecuul.
 */
public class AtoomDeel {

	String atoomNaam;
	Expressie aantalElementen;
	
	public AtoomDeel(String s, Expressie aantal)
	{	
		atoomNaam = s;
		aantalElementen = aantal;
	}
	
	public boolean isGelijkwaardig(AtoomDeel a)
	{
		boolean aantalElementenGelijk = false;
		if(aantalElementen.isWaarde() && a.aantalElementen.isWaarde())
			aantalElementenGelijk = aantalElementen.geefWaarde() == a.aantalElementen.geefWaarde();
		else
		{
			aantalElementenGelijk = aantalElementen.substitueer(0.54321, "n").geefWaarde() == a.aantalElementen.substitueer(0.54321, "n").geefWaarde();
		}
		if(atoomNaam.equals(a.atoomNaam) && aantalElementenGelijk)
			return true;
		return false;
	}
	
	public String toString()
	{
		return atoomNaam + "$s" + aantalElementen + "@";
		
	}
}

