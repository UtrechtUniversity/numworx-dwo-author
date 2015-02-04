package fi.wiskopdr.scheikundeobjects;

public class ReactieVergelijking {

	ReactieExpressie expressie1, expressie2;
	int pijl;
	public static int AFLOPEND = 0; 
	public static int EVENWICHTSREACTIE = 1;
	
	public ReactieVergelijking(ReactieExpressie e1, ReactieExpressie e2)
	{
		expressie1 = e1;
		expressie2 = e2;
		pijl = AFLOPEND;
	}
	
	public ReactieVergelijking(ReactieExpressie e1, ReactieExpressie e2, int pijl)
	{
		expressie1 = e1;
		expressie2 = e2;
		this.pijl = pijl;
	}
	
	public boolean elementenBalansKlopt()
	{
		String[] atomenLinks = expressie1.atoomNamen;
		String[] atomenRechts = expressie2.atoomNamen;
		
		//Komen links en rechts even veel atoomsoorten voor?
		if(atomenLinks.length != atomenRechts.length)
			return false;
		
		//Komen links en rechts dezelfde atoomsoorten voor?
		for(int i = 0; i < atomenLinks.length; i++)
		{
			String soort = atomenLinks[i];
			boolean komtRechtsVoor = false;
			for(int j = 0; j < atomenRechts.length; j++)
			{
				if(atomenRechts[j].equals(soort))
				{	
					komtRechtsVoor = true;
					if(!elementenBalansKlopt(soort))
						return false;
					break;
				}
			}
			if(!komtRechtsVoor)
				return false;
		}
		
		return true;
		
	}
	
	public boolean elementenBalansKlopt(String atoomNaam)
	{
		int aantalElementen1 = expressie1.geefAantalAtoomElementen(atoomNaam);
		int aantalElementen2 = expressie2.geefAantalAtoomElementen(atoomNaam);
		return aantalElementen1 == aantalElementen2;
	}
	
	public boolean ladingenBalansKlopt()
	{
		int lading1 = expressie1.geefTotaleLading();
		int lading2 = expressie2.geefTotaleLading();
		return lading1 == lading2;
	}
	
	public boolean isGelijkwaardig(ReactieVergelijking vergelijking)
	{
		ReactieExpressie e1 = vergelijking.expressie1;
		ReactieExpressie e2 = vergelijking.expressie2;
		
		if(vergelijking.pijl != pijl)
			return false;
		
		if(e1.isGelijkwaardig(expressie1) && e2.isGelijkwaardig(expressie2))
			return true;
		else if(pijl == EVENWICHTSREACTIE)
		{
			if(e1.isGelijkwaardig(expressie2) && e2.isGelijkwaardig(expressie1))
				return true;
		}
		return false;		
				
	}
	
	public boolean isGelijkwaardigMoleculen(ReactieVergelijking vergelijking)
	{
		ReactieExpressie e1 = vergelijking.expressie1;
		ReactieExpressie e2 = vergelijking.expressie2;
		if(e1.isGelijkwaardigMoleculen(expressie1) && e2.isGelijkwaardigMoleculen(expressie2))
			return true;
		else if(pijl == EVENWICHTSREACTIE)
		{
			if(e1.isGelijkwaardigMoleculen(expressie2) && e2.isGelijkwaardigMoleculen(expressie1))
				return true;
		}
		return false;
	}
	
	public boolean isGelijkwaardigMoleculenLading(ReactieVergelijking vergelijking)
	{
		ReactieExpressie e1 = vergelijking.expressie1;
		ReactieExpressie e2 = vergelijking.expressie2;
		if(e1.isGelijkwaardigMoleculenLading(expressie1) && e2.isGelijkwaardigMoleculenLading(expressie2))
			return true;
		else if(pijl == EVENWICHTSREACTIE)
		{
			if(e1.isGelijkwaardigMoleculenLading(expressie2) && e2.isGelijkwaardigMoleculenLading(expressie1))
				return true;
		}
		return false;
	}
	
	public boolean isGelijkwaardigPijl(ReactieVergelijking vergelijking)
	{
		return vergelijking.pijl == pijl;
	}
	
	public boolean kanVereenvoudigd(ReactieVergelijking vergelijking)
	{
		ReactieExpressie e1 = vergelijking.expressie1;
		ReactieExpressie e2 = vergelijking.expressie2;
		
		if(e1.isGelijkwaardigMoleculenLading(expressie1))
		{
			double factor1 = e1.vereenvoudigFactor(expressie1);
			double factor2 = e2.vereenvoudigFactor(expressie2);
			if(factor1 != -999 && factor1 == factor2)
				return true;
		}
		else if(pijl == EVENWICHTSREACTIE && e1.isGelijkwaardigMoleculenLading(expressie2))
		{
			double factor1 = e1.vereenvoudigFactor(expressie2);
			double factor2 = e2.vereenvoudigFactor(expressie1);
			if(factor1 != -999 && factor1 == factor2)
				return true;
		}
		return false;
		 
	}
	
	public String toString()
	{
		if(pijl == AFLOPEND)
			return expressie1.toString() + ">" + expressie2.toString();
		else //if(pijl == EVENWICHTSREACTIE)
			return expressie1.toString() + "=" + expressie2.toString();
		
	}
	
}
