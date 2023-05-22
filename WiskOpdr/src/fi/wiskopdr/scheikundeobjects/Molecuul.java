package fi.wiskopdr.scheikundeobjects;

import java.util.Vector;

import fi.wiskopdr.expressies.*;


public class Molecuul {

	AtoomDeel[] atoomDelen;
	SamengesteldIonDeel[] samengesteldeIonDelen; //werkt niet, moet ook nog een coefficient bij...
	int lading;
	
	public Molecuul(AtoomDeel[] delen, int lading)
	{
		atoomDelen = delen;
		samengesteldeIonDelen = null;
		this.lading = lading;
	}
	
	public Molecuul(SamengesteldIonDeel[] ionDelen, int lading)
	{
		atoomDelen = null;
		samengesteldeIonDelen = ionDelen;
		this.lading = lading;
	}
	
	public Expressie geefAantalAtoomElementen(String naam)
	{
		if(samengesteldeIonDelen == null)
		{	Expressie aantalElementen = new BasisExpressie(0);
			for(int i = 0; i < atoomDelen.length; i++)
			{
				if(atoomDelen[i].atoomNaam.equals(naam))
					aantalElementen = new Optelling(aantalElementen, atoomDelen[i].aantalElementen);
			}
			return aantalElementen;
		}
		else
		{
			Expressie aantalElementen = new BasisExpressie(0);
			for(int i = 0; i < samengesteldeIonDelen.length; i++)
			{
				aantalElementen = new Optelling(aantalElementen, new Vermenigvuldiging(samengesteldeIonDelen[i].coefficient, samengesteldeIonDelen[i].geefAantalAtoomElementen(naam)));
			}
			return aantalElementen;
		}
	}
	
	public boolean isGelijkwaardig(Molecuul m)
	{
		boolean gelijkWaardig = isGelijkwaardigZonderLading(m);
		return gelijkWaardig && m.lading == lading;
	}
	
	public boolean isGelijkwaardigZonderLading(Molecuul m)
	{
		if(samengesteldeIonDelen == null)
		{	if(m.atoomDelen == null || atoomDelen.length != m.atoomDelen.length)
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
		}
		else
		{
			if(m.samengesteldeIonDelen == null || samengesteldeIonDelen.length != m.samengesteldeIonDelen.length)
				return false;
			Vector<SamengesteldIonDeel> a1 = new Vector<SamengesteldIonDeel>();
			Vector<SamengesteldIonDeel> a2 = new Vector<SamengesteldIonDeel>();
			for(int i = 0; i < samengesteldeIonDelen.length; i++)
			{
				a1.add(m.samengesteldeIonDelen[i]);
				a2.add(samengesteldeIonDelen[i]);
			}
			for(int i = 0; i < samengesteldeIonDelen.length; i++)
			{
				for(int j = 0; j < a1.size(); j++)
				{
					if(samengesteldeIonDelen[i].isGelijkwaardig(a1.get(j)))
					{
						a1.remove(j);
						break;
					}
				}
			}
			if(a1.size() > 0)
				return false;
		}
		
		return true;
		
	}
	
	public String toString()
	{
		String s = "";
		
		if(samengesteldeIonDelen == null)
		{	for(int i = 0; i < atoomDelen.length; i++)
			{
				s = s + atoomDelen[i].toString();
			}
			if(lading != 0)
				s = s + "$m" + lading + "@@"; //waarom twee apestaartjes??
			return s;
		}
		else
		{
			for(int i = 0; i < samengesteldeIonDelen.length; i++)
			{
				s = s + samengesteldeIonDelen[i].toString();
			}
			if(lading != 0)
				s = s + "$m" + lading + "@@";
			return s;
		}
	}
}
