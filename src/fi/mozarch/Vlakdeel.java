package fi.mozarch;


import java.awt.Polygon;
import java.awt.Color;
import java.applet.Applet;

public class Vlakdeel
{	
	public int aantalPunten;
	public HoekpuntMoz[] hoekpunten;
	public Punt sleeppunt, draaipunt;
	public double orientatie;
	public Polygon tekenvlak;
	public Color kleur;
	public int beginnummer;
	public int aantalHoekpuntenVast;
	public Applet eigenaar;
	public boolean nieuw;
	
	public double positiex, positiey;
	//public boolean tekenbaar;
	
	public int fractielType = 0;
	
	public Vlakdeel(Applet ap, int aantal, double positiex, double positiey, Color kl)
	{	
		this.positiex = positiex;
		this.positiey = positiey;
		
		nieuw = true;
		aantalPunten = aantal;
		hoekpunten = new HoekpuntMoz[aantal + 1];
		draaipunt = new Punt(positiex, positiey);
		sleeppunt = new Punt(0, 0);
		tekenvlak = new Polygon();
		kleur = kl;
		eigenaar = ap;
		beginnummer = 0;
		aantalHoekpuntenVast = 0;
		orientatie = 0;
		//tekenbaar = false;
		
//System.out.println("hoekpunten = " + hoekpunten.length);		
	}
	
	public void klikVast(int hoekn, int vlakdeeln, int hoeknVlakdeel)
	{	beginnummer = hoekn;
		if (hoekpunten[hoekn].aantalVastgeklikt == 0)
			aantalHoekpuntenVast++;
		hoekpunten[hoekn].maakVast(vlakdeeln, hoeknVlakdeel);
		draaipunt = new Punt(hoekpunten[hoekn].tekenpunt.x - 0.5 * eigenaar.getSize().width,
				            -hoekpunten[hoekn].tekenpunt.y + 0.5 * eigenaar.getSize().height);
  	}
	
	public void klikLos(int hoekn, int vlakdeeln, int hoeknVlakdeel)
	{	hoekpunten[hoekn].maakLos(vlakdeeln, hoeknVlakdeel);
		if (!hoekpunten[hoekn].vast)
		{	aantalHoekpuntenVast-- ;
		}
		if (aantalHoekpuntenVast == 0)
		{	beginnummer = 0;
			draaipunt = new Punt(hoekpunten[0].tekenpunt.x - 0.5 * eigenaar.getSize().width,
					            -hoekpunten[0].tekenpunt.y + 0.5 * eigenaar.getSize().height);
		}
	}
	
	public void klikVastDraai(int hoekn, int vlakdeeln, int hoeknVlakdeel)
	{	if (hoekpunten[hoekn].aantalVastgeklikt == 0)
			aantalHoekpuntenVast++;
		hoekpunten[hoekn].maakVast(vlakdeeln, hoeknVlakdeel);
	}
	
	public void klikAllesLos()
	{	beginnummer = 0;
		aantalHoekpuntenVast = 0;
		draaipunt = new Punt(hoekpunten[0].tekenpunt.x - 0.5 * eigenaar.getSize().width,
				            -hoekpunten[0].tekenpunt.y + 0.5 * eigenaar.getSize().height);
		for (int i = 1; i < hoekpunten.length - 1; i++)
		{	hoekpunten[i].aantalVastgeklikt = 0;
		}
	}
}

class HoekpuntMoz
{
	public Punt tekenpunt;
	public double x, y;
	public int aantalVastgeklikt;
	public int[] vlakdeelnummers;
	public int[] hoeknummersVlakdeel;
	public boolean vast;
	
	public HoekpuntMoz(double x, double y)
	{	this.x = x;
		this.y = y;
		aantalVastgeklikt = 0;
		vlakdeelnummers = new int[30];
		hoeknummersVlakdeel = new int[30];
		vast = false;
	}
	
	void maakVast(int vlakdeeln, int hoeknVlakdeel)
	{	if (!zitVast(vlakdeeln, hoeknVlakdeel))
		{	vlakdeelnummers[aantalVastgeklikt] = vlakdeeln;
			hoeknummersVlakdeel[aantalVastgeklikt] = hoeknVlakdeel;
			aantalVastgeklikt++;
			vast = true;
		}
	}
	
	void maakLos(int vlakdeeln, int hoeknVlakdeel)
	{	for (int i = 0; i < aantalVastgeklikt; i++)
		{	if ((vlakdeelnummers[i] == vlakdeeln) && (hoeknummersVlakdeel[i] == hoeknVlakdeel))
			{	vlakdeelnummers[i] = vlakdeelnummers[aantalVastgeklikt - 1];
				hoeknummersVlakdeel[i] = hoeknummersVlakdeel[aantalVastgeklikt - 1];
				aantalVastgeklikt--;
			}
		}
		if (aantalVastgeklikt < 1)
			vast = false;
	}
	
	boolean zitVast(int vlakdeeln, int hoeknVlakdeel)
	{	boolean b = false;
		for (int i = 0; i < aantalVastgeklikt; i++)
		{	if (vlakdeelnummers[i] == vlakdeeln && hoeknummersVlakdeel[i] == hoeknVlakdeel)
			b = true;
		}
		return b;
	}
}