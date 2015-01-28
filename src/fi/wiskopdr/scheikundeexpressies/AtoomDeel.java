package fi.wiskopdr.scheikundeexpressies;


/*
 * De klasse specificeert een atoomdeel in een molecuul, met (afgekorte) naam, 
 * lading en aantal deeltjes van dit atoom in het molecuul.
 */
public class AtoomDeel {

	String atoomNaam;
	int aantalElementen;
	
	public AtoomDeel(String s, int aantal)
	{	
		atoomNaam = s;
		aantalElementen = aantal;
	}
	
	public boolean isGelijkwaardig(AtoomDeel a)
	{
		if(atoomNaam.equals(a.atoomNaam) && aantalElementen == a.aantalElementen)
			return true;
		return false;
	}
	
	public String toString()
	{
		return atoomNaam + "$s" + aantalElementen + "@";
		
	}
}

