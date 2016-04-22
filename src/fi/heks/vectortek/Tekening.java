package fi.heks.vectortek;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;

import java.util.*;

import fi.heks.*;

public class Tekening extends ScContainer 
{
	int breedte;
	int hoogte;

	public int aantalTekenObj;
	public TekenObjectTek[] to;
	
	boolean log = false;
	String srtStr = "";

	public Tekening(int x, int y, int b, int h, Object owner, String naam) 
	{
		super();
		relx = x;
		rely = y;
		relb = b;
		relh = h;

		aantalTekenObj = 0;
		to = new TekenObjectTek[250];
		if (owner instanceof AppletUtil)
			leesFile((AppletUtil) owner, naam);
		else if (owner instanceof HeksInteractiePanel)
			leesFile((HeksInteractiePanel) owner, naam);

		setBounds(x, y, b, h);
	}

	public Tekening(int x, int y, int b, int h, Object owner, String naam, boolean log) 
	{
		super();
		relx = x;
		rely = y;
		relb = b;
		relh = h;

		this.log = log;
		
		aantalTekenObj = 0;
		to = new TekenObjectTek[250];
		if (owner instanceof AppletUtil)
			leesFile((AppletUtil) owner, naam);
		else if (owner instanceof HeksInteractiePanel)
			leesFile((HeksInteractiePanel) owner, naam);
		setBounds(x, y, b, h);

	}
	
	
	private void leesFile(AppletUtil au, String naam) 
	{
		DataInputStream invoer;
		try 
		{
			invoer = new DataInputStream(au.getStream("resources/" + naam));
			breedte = (invoer.readByte() + 128) * 2;
			hoogte = (invoer.readByte() + 128) * 2;
			setSize(breedte, hoogte);
			aantalTekenObj = invoer.readByte();
int lCnt = 0; int vCnt = 0; int kCnt = 0; int vkCnt = 0;		
			for (int i = 0; i < aantalTekenObj; i++) 
			{
				int srt = invoer.readByte();
				if (srt == 0)
				{	to[i] = new LijnstukTek(invoer);
					lCnt++;
				}
				else if (srt == 3)
				{	to[i] = new VeelhoekTek(invoer);
					vCnt++;
				}
				else if (srt == 4)
				{	to[i] = new KrommeTek(invoer);
					kCnt++;
				}
				else if (srt == 5)
				{	to[i] = new VulKrommeTek(invoer);
					vkCnt++;
				}
				to[i].setSize(breedte, hoogte);
				add(to[i], 0);
			}
srtStr = "" + lCnt + "," + vCnt + "," + kCnt + "," + vkCnt;  			
//if (log)
//System.out.println("" + naam + " aantalTekenObj " + aantalTekenObj + " " + srtStr);
			
		} 
		catch (IOException io) 
		{}
	}

	private void leesFile(HeksInteractiePanel heip, String naam) 
	{
		DataInputStream invoer;
		try 
		{
			invoer = new DataInputStream(heip.getStream("resources/" + naam));
			breedte = (invoer.readByte() + 128) * 2;
			hoogte = (invoer.readByte() + 128) * 2;
			setSize(breedte, hoogte);
			aantalTekenObj = invoer.readByte();
int lCnt = 0; int vCnt = 0; int kCnt = 0; int vkCnt = 0;		
			for (int i = 0; i < aantalTekenObj; i++) 
			{
				int srt = invoer.readByte();
				if (srt == 0)
				{	to[i] = new LijnstukTek(invoer);
					lCnt++;
				}
				else if (srt == 3)
				{	to[i] = new VeelhoekTek(invoer);
					vCnt++;
				}
				else if (srt == 4)
				{	to[i] = new KrommeTek(invoer);
					kCnt++;
				}
				else if (srt == 5)
				{	to[i] = new VulKrommeTek(invoer);
					vkCnt++;
				}
				to[i].setSize(breedte, hoogte);
				add(to[i], 0);
			}
srtStr = "" + lCnt + "," + vCnt + "," + kCnt + "," + vkCnt;  			
//if (log)
//System.out.println("" + naam + " aantalTekenObj " + aantalTekenObj + " " + srtStr);
			
		} 
		catch (IOException io) 
		{}
	}

	public Hashtable<String,Object> getState()
	{
		Hashtable<String,Object> h = new Hashtable<String,Object>();
		h.put("breedte", breedte);
		h.put("hoogte", hoogte);
		h.put("aantaltekenobj", aantalTekenObj);
		ArrayList<Hashtable<String,Object>> tekenobj = new ArrayList<Hashtable<String,Object>>();
		for (int tCnt = 0; tCnt < to.length; tCnt++)
		{
			TekenObjectTek tot = to[tCnt];
			if (to[tCnt] instanceof LijnstukTek)
			{	tekenobj.add(((LijnstukTek)to[tCnt]).getState());
			}
			else if (to[tCnt] instanceof VeelhoekTek)
			{	tekenobj.add(((VeelhoekTek)to[tCnt]).getState());
			}
			else if (to[tCnt] instanceof KrommeTek)
			{	tekenobj.add(((KrommeTek) to[tCnt]).getState());
			}
			else if (to[tCnt] instanceof VulKrommeTek)
			{	tekenobj.add(((VulKrommeTek)to[tCnt]).getState());
			}
		
		}
		h.put("tekenobj", tekenobj);
		
		return h;
	}
	
	public boolean contains(int x, int y) 
	{
		int lx = getLocation().x;
		int ly = getLocation().y;
		for (int i = 0; i < aantalTekenObj; i++) 
		{
			if (to[i].contains(x - lx, y - ly))
				return true;
		}
		return false;
	}

	public void setSize(int b, int h) 
	{
		double bd = b;
		double hd = h;
		double fx = bd / breedte;
		double fy = hd / hoogte;
		breedte = b;
		hoogte = h;
		for (int i = 0; i < aantalTekenObj; i++) 
		{
			to[i].schaal(fx, fy);
			to[i].setSize(breedte, hoogte);
		}
		super.setSize(b, h);
	}

	public void setBounds(int x, int y, int b, int h) 
	{
		double bd = b;
		double hd = h;
		double fx = bd / breedte;
		double fy = hd / hoogte;
		breedte = b;
		hoogte = h;
		for (int i = 0; i < aantalTekenObj; i++) 
		{
			to[i].schaal(fx, fy);
			to[i].setSize(breedte, hoogte);
		}
		super.setBounds(x, y, b, h);
	}

	public void schaal(double factorX, double factorY) 
	{
		breedte = (int) (factorX * relb);
		hoogte = (int) (factorY * relh);
		for (int i = 0; i < aantalTekenObj; i++) 
		{
			to[i].schaal(factorX, factorY);
			to[i].setSize(breedte, hoogte);
		}
		super.setSize(breedte, hoogte);
	}

	public void schaal(double factor) 
	{
		schaal = factor;
		int x = (int) (schaal * relx);
		int y = (int) (schaal * rely);
		int b = (int) (schaal * relb);
		int h = (int) (schaal * relh);
		setBounds(x, y, b, h);

	}

	public void draai(double h) 
	{
		for (int i = 0; i < aantalTekenObj; i++) 
		{
			to[i].draai(h);
		}
	}
}
