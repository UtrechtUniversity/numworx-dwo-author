package fi.wiskopdr.scheikundeobjects;

import java.util.Vector;


public class Molecuul {

	AtoomDeel[] atoomDelen;
	int lading;
	
	public Molecuul(AtoomDeel[] delen, int lading)
	{
		atoomDelen = delen;
		this.lading = lading;
	}
	
	public int geefAantalAtoomElementen(String naam)
	{
		int aantalElementen = 0;
		for(int i = 0; i < atoomDelen.length; i++)
		{
			if(atoomDelen[i].atoomNaam.equals(naam))
				aantalElementen += atoomDelen[i].aantalElementen;
		}
		return aantalElementen;
	}
	
	public boolean isGelijkwaardig(Molecuul m)
	{
		boolean gelijkWaardig = isGelijkwaardigZonderLading(m);
		return gelijkWaardig && m.lading == lading;
	}
	
	public boolean isGelijkwaardigZonderLading(Molecuul m)
	{
		if(atoomDelen.length != m.atoomDelen.length)
			return false;
		Vector<AtoomDeel> a1 = new Vector<AtoomDeel>();
		Vector<AtoomDeel> a2 = new Vector<AtoomDeel>();
		for(int i = 0; i < atoomDelen.length; i++)
		{
			a1.add(m.atoomDelen[i]);
			a2.add(atoomDelen[i]);
		}
		for(int i = 0; i < atoomDelen.length; i++)
		{
			for(int j = 0; j < a1.size(); j++)
			{
				if(atoomDelen[i].isGelijkwaardig(a1.get(j)))
				{
					a1.remove(j);
					break;
				}
			}
		}
		if(a1.size() > 0)
			return false;
		
		return true;
		
	}
	
	public String toString()
	{
		String s = "";
		
		for(int i = 0; i < atoomDelen.length; i++)
		{
			s = s + atoomDelen[i].toString();
		}
		if(lading != 0)
			s = s + "$m" + lading + "@@";
		return s;
	}
}
