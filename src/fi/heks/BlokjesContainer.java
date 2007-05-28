package fi.heks;

import java.awt.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;
import java.applet.*;


public class BlokjesContainer extends ScContainer
{
	private boolean veranderd;
	private boolean alsGetal;
	
	private Applet eigenaar;
	private AppletUtil au;
	private int aantalBlokjes;
	private Tekening[] blokjes;
	private boolean[] soorten;
	private Tekening plusblokje,minblokje;
	private GetalComponent getalPlus, getalMin;
	
	public BlokjesContainer(int x, int y, int b, int h, Applet applet)
	{	super(x,y,b,h);
		eigenaar = applet;
		au = new AppletUtil(eigenaar);
		
		aantalBlokjes = 0;
		blokjes = new Tekening[20];
		soorten = new boolean[500];
		
		plusblokje = new Tekening(0,0,40,40,au,"blokjePlus.gif");
		plusblokje.setVisible(false);
		add(plusblokje);
		
		minblokje = new Tekening(50,0,40,40,au,"blokjeMin.gif");
		minblokje.setVisible(false);
		add(minblokje);
		
		getalPlus = new GetalComponent(5,35,30,25);
		getalPlus.setVisible(false);
		getalPlus.zetWaarde(0);
		add(getalPlus);
		
		getalMin = new GetalComponent(55,35,30,25);
		getalMin.setVisible(false);
		getalMin.zetWaarde(0);
		add(getalMin);
		
		veranderd = true;
		alsGetal = false;
	}
	
	public void toonAlsGetal(boolean bool)
	{	alsGetal = bool;
	}
	
	public void voegBlokjeToe(boolean bool)
	{	
		int x = (int)((aantalBlokjes%5)*(relb/5));
		int y = (int)(aantalBlokjes/5*(relb/5));
		int b = (int)(20.0/110*relb);
		int h = (int)(20.0/110*relb);
		
		if(bool)
		{	getalPlus.verhoog();System.out.println("test");
			if(aantalBlokjes<20 && !alsGetal)
			{	blokjes[aantalBlokjes] = new Tekening(x,y,b,h,au,"blokjePlus.gif");
				blokjes[aantalBlokjes].schaal(schaal);
				add(blokjes[aantalBlokjes]);
			}
			else if(aantalBlokjes==20)
			{	plusblokje.setVisible(true);
				getalPlus.setVisible(true);
				minblokje.setVisible(true);
				getalMin.setVisible(true);
				for(int i=0 ; i<20 ; i++)
				{	blokjes[i].setVisible(false);
				}
			}
			if(alsGetal && getalPlus.geefWaarde()==1)
			{	
				plusblokje.setVisible(true);
				getalPlus.setVisible(true);
			}
		}
		else
		{	getalMin.verhoog();
			if(aantalBlokjes<20 && !alsGetal)
			{	blokjes[aantalBlokjes] = new Tekening(x,y,b,h,au,"blokjeMin.gif");
				blokjes[aantalBlokjes].schaal(schaal);
				add(blokjes[aantalBlokjes]);
			}
			else if(aantalBlokjes==20)
			{	plusblokje.setVisible(true);
				getalPlus.setVisible(true);
				minblokje.setVisible(true);
				getalMin.setVisible(true);
				for(int i=0 ; i<20 ; i++)
				{	blokjes[i].setVisible(false);
				}
			}
			if(alsGetal && getalMin.geefWaarde()==1)
			{	minblokje.setVisible(true);
				getalMin.setVisible(true);
			}
		}
		soorten[aantalBlokjes]=bool;
		if(aantalBlokjes<500)aantalBlokjes++;
		veranderd = true;
		repaint();
	}
	
	public void verwijderBlokje()
	{	aantalBlokjes--;
		if(soorten[aantalBlokjes])getalPlus.verlaag();
		else getalMin.verlaag();
		if(aantalBlokjes==20 && !alsGetal)
		{	plusblokje.setVisible(false);
			getalPlus.setVisible(false);
			minblokje.setVisible(false);
			getalMin.setVisible(false);
			for(int i=0 ; i<20 ; i++)
			{	blokjes[i].setVisible(true);
			}
		}
		else if(aantalBlokjes<20)
		{	if(blokjes[aantalBlokjes]!=null)remove(blokjes[aantalBlokjes]);
		}
		if(alsGetal && getalMin.geefWaarde()==0)
		{	minblokje.setVisible(false);
			getalMin.setVisible(false);
		}
		if(alsGetal && getalPlus.geefWaarde()==0)
		{	plusblokje.setVisible(false);
			getalPlus.setVisible(false);
		}
		veranderd = true;
		repaint();
	}
	
	public void removeAll()
	{	int n = aantalBlokjes ;
		for(int i=0 ; i<n ; i++)
		{	verwijderBlokje();
		}
		
		//aantalBlokjes = 0;
		veranderd = true;
		//super.removeAll();
	}

}
