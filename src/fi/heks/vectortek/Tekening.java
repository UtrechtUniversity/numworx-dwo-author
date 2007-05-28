package fi.heks.vectortek;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;


public class Tekening extends ScContainer   
{	
	int breedte;
	int hoogte;
	
	public int aantalTekenObj;
	public TekenObjectTek[] to;
	
	public Tekening(int x, int y, int b, int h, AppletUtil au,String naam)
	{	super();
		relx = x;
		rely = y; 
		relb = b;
		relh = h;
		
		aantalTekenObj = 0;
		to = new TekenObjectTek[250];
		leesFile(au,naam);
		setBounds(x,y,b,h);
	}
	
	private void leesFile( AppletUtil au, String naam)
	{	DataInputStream invoer;
		try
		{	
			invoer = new DataInputStream(au.getStream("resources/" + naam));
			breedte = (invoer.readByte()+128)*2;
			hoogte = (invoer.readByte()+128)*2;
			setSize(breedte,hoogte);
			aantalTekenObj = invoer.readByte();
			for(int i=0 ; i<aantalTekenObj ; i++)
			{	int srt = invoer.readByte();
				if(srt==0)to[i] = new LijnstukTek(invoer);
				else if(srt==3)to[i] = new VeelhoekTek(invoer);
				else if(srt==4)to[i] = new KrommeTek(invoer);
				else if(srt==5)to[i] = new VulKrommeTek(invoer);
				to[i].setSize(breedte,hoogte);
				add(to[i],0);
			}
		}
		catch(IOException io){}
	}
	
	public boolean contains(int x, int y)
	{	int lx = getLocation().x;
		int ly = getLocation().y;
		for(int i=0 ; i<aantalTekenObj ; i++)
		{	if(to[i].contains(x-lx,y-ly))return true;
		}
		return false;
	}
	
	public void setSize(int b, int h)
	{	double bd = b;
		double hd = h;
		double fx = bd/breedte;
		double fy = hd/hoogte;
		breedte = b; 
		hoogte = h;
		for(int i=0 ; i<aantalTekenObj ; i++)
		{	to[i].schaal(fx, fy);
			to[i].setSize(breedte,hoogte);
		}
		super.setSize(b,h);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	double bd = b;
		double hd = h;
		double fx = bd/breedte;
		double fy = hd/hoogte;
		breedte = b; 
		hoogte = h;
		for(int i=0 ; i<aantalTekenObj ; i++)
		{	to[i].schaal(fx, fy);
			to[i].setSize(breedte,hoogte);
		}
		super.setBounds(x,y,b,h);
	}
	
	public void schaal(double factorX, double factorY)
	{	
		breedte = (int)(factorX*relb); 
		hoogte = (int)(factorY*relh); 
		for(int i=0 ; i<aantalTekenObj ; i++)
		{	to[i].schaal(factorX, factorY);
			to[i].setSize(breedte,hoogte);
		}
		super.setSize(breedte,hoogte);
	}
	
	public void schaal(double factor)
	{	schaal = factor;
		int x = (int)(schaal*relx);
		int y = (int)(schaal*rely);
		int b = (int)(schaal*relb);
		int h = (int)(schaal*relh);
		setBounds(x,y,b,h);
		
	}
	
	public void draai(double h)
	{	for(int i=0 ; i<aantalTekenObj ; i++)
		{	to[i].draai(h);
		}
	}
}
