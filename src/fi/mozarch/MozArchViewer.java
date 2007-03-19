package fi.mozarch;

import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class MozArchViewer extends TekenApplet 
{	private Vlakdeel[] vlakdelen;
	private int aantal,aantalVlakdelen;
	double trek, trekx=0,treky=0, zijde;
	int[] volgorde;
	
	public void initialiseer()
	{	achtergrondkleur(255,255,200);
		maakMuisActieMogelijk();
		
		aantalVlakdelen = 0;
		aantal = 10;
		zijde = 40;
		vlakdelen = new Vlakdeel[1000];
		volgorde = new int[1000];
		
		String startFile = getParameter("stfile");
		if(startFile!=null && !startFile.equals(""))zetVlakdelen(startFile);
 	}
	
	public void tekenprogramma()
	{	schaal(0.7);
		for(int i=aantalVlakdelen-1 ; i>-1 ; i--)
		{	if(volgorde[i]!=-1 && !vlakdelen[volgorde[i]].nieuw)
				tekenVlakdeel(vlakdelen[volgorde[i]]);
		}
	}
	
	void tekenVlakdeel(Vlakdeel vd)
	{	int len = vd.hoekpunten.length;
		int num = vd.beginnummer;
		
		penUit(); stap(vd.draaipunt.x, vd.draaipunt.y); 
		links(vd.orientatie);
		vulAan(vd.kleur);
		for(int i=0 ; i<len ; i++)
		{	stap(vd.hoekpunten[(i+1+num)%len].x - vd.hoekpunten[(i+num)%len].x , vd.hoekpunten[(i+1+num)%len].y - vd.hoekpunten[(i+num)%len].y);
			vd.hoekpunten[(i+1+num)%len].tekenpunt = new Punt(geefPunt());
		}
		vulUit();
		vd.tekenvlak = geefVlak();
		penAan();
		for(int i=0 ; i<len ; i++)
		{	if((i+1+num)%len==0 || (i+num)%len==0)penUit();
			stap(vd.hoekpunten[(i+1+num)%len].x - vd.hoekpunten[(i+num)%len].x , vd.hoekpunten[(i+1+num)%len].y - vd.hoekpunten[(i+num)%len].y);
			penAan();
		}
		rechts(vd.orientatie);
		penUit();
		stap(-vd.draaipunt.x, -vd.draaipunt.y);
		tb.zetStart();
	}	
	
	public void zetVlakdelen(String s)
	{	Base64InputStream invoer;
		try
		{	ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
			invoer = new Base64InputStream(bais);
			aantalVlakdelen = invoer.readShort();
			vlakdelen = new Vlakdeel[1000];
			volgorde = new int[1000];
			for(int i=0 ; i<aantalVlakdelen ; i++)
			{	int aantalPunten = invoer.readShort();
				double posx = invoer.readDouble()+100;
				double posy = invoer.readDouble();
				double orientatie = invoer.readDouble();
				boolean nieuw = invoer.readBoolean();
				int beginnummer = invoer.readShort();
				int rood = invoer.readByte()+128;
				int groen = invoer.readByte()+128;
				int blauw = invoer.readByte()+128;
				volgorde[i] = invoer.readShort();
				Color c = new Color(rood,groen,blauw);
				vlakdelen[i] = new Vlakdeel(this,aantalPunten,posx,posy,c);
				vlakdelen[i].orientatie = orientatie;
				vlakdelen[i].beginnummer = beginnummer;
				vlakdelen[i].nieuw = nieuw;
				for(int j=0 ; j<aantalPunten+1 ; j++)
				{	double x = invoer.readDouble();
					double y = invoer.readDouble();
					vlakdelen[i].hoekpunten[j] = new HoekpuntMoz(x,y);
				}
			}
		}
		catch(IOException io){}
	}

}
