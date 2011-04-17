package fi.heks.scobjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ScTekstContainer extends ScContainer
{	
	private String tekst;
	private int aantalRegels;
	private int regelHoogte;
	private ScLabel[] regels;
	private String[] deelteksten;
	
	public ScTekstContainer(int x, int y, int b, int regelH, int aantalR, String tkst)
	{	super(x,y,b,aantalR*regelH);
		
		aantalRegels = aantalR;
		regelHoogte = regelH;
		tekst = tkst;
				
		maakDeelteksten();
		regels = new ScLabel[aantalRegels];
		for(int i=0 ; i<aantalRegels ; i++)
		{	regels[i] = new ScLabel(0, i*regelHoogte, b, regelHoogte, deelteksten[i]);
			add(regels[i]);
		}
	}
	
	
	
	private void maakDeelteksten()
	{	deelteksten = new String[aantalRegels];
		int startIndex = 0;
		for(int i=0 ; i<aantalRegels ; i++)
		{	int plaatsScheiding = tekst.indexOf("/",startIndex);
			if(plaatsScheiding==-1)plaatsScheiding = tekst.length();
			deelteksten[i] = tekst.substring(startIndex,plaatsScheiding);
			if(plaatsScheiding==tekst.length())break;
			startIndex = plaatsScheiding+1;
		}
	}
	
	public void setText(String tekstregel, int labelnummer)
	{	regels[labelnummer].setLabel(tekstregel);
	}
	
	public void setText(String tekst)
	{	this.tekst = tekst;
		maakDeelteksten();
		for(int i=0 ; i<aantalRegels ; i++)
		{	regels[i].setLabel(deelteksten[i]);
		}
	}
	
	public void lijnUit(int soort)
	{	for(int i=0 ; i<aantalRegels ; i++)
		{	regels[i].lijnUit(soort);
		}
	}
		
}
