package fi.heks.vectortek;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.*;

public class VeelhoekTek extends TekenObjectTek 
{
	public Polygon basisPolygon;
	Color vulkleur;
	Color lijnkleur;
	boolean isOmlijnd, isGevuld;
	double[] exactePuntenX, exactePuntenY;

	public VeelhoekTek(DataInputStream inv) 
	{
		isGevuld = false;
		isOmlijnd = false;
		basisPolygon = new Polygon();
		try 
		{
			int gevuld = inv.readByte();
			if (gevuld != 0) 
			{
				isGevuld = true;
				int r = inv.readByte() + 128;
				int g = inv.readByte() + 128;
				int b = inv.readByte() + 128;
				vulkleur = new Color(r, g, b);
			}
			int omlijnd = inv.readByte();
			if (omlijnd != 0) 
			{
				isOmlijnd = true;
				int r = inv.readByte() + 128;
				int g = inv.readByte() + 128;
				int b = inv.readByte() + 128;
				lijnkleur = new Color(r, g, b);
			}
			int n = inv.readByte();
			for (int j = 0; j < n; j++) {
				int x = inv.readByte() * 2 + 254;
				int y = inv.readByte() * 2 + 254;
				basisPolygon.addPoint(x, y);
			}
		} catch (IOException io) 
		{}
	}

	public Hashtable<String,Object> getState()
	{
		Hashtable<String,Object> h = new Hashtable<String,Object>();
		h.put("soort", "Veelhoek");
		h.put("lijnkleur", new String("rgb(" + lijnkleur.getRed()+ "," + lijnkleur.getGreen() + "," + lijnkleur.getBlue() + ")"));
		h.put("vulkleur", new String("rgb(" + vulkleur.getRed()+ "," + vulkleur.getGreen() + "," + vulkleur.getBlue() + ")"));
		h.put("omlijnd", new Boolean(isOmlijnd));
		h.put("gevuld", new Boolean(isGevuld));
		h.put("puntenx", basisPolygon.xpoints);
		h.put("punteny", basisPolygon.ypoints);
		
		return h;
	}
	
	public void paint(Graphics gr) 
	{
		Graphics g = (Graphics2D) gr;
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (isGevuld && vulkleur != null) 
		{
			g.setColor(vulkleur);
			g.fillPolygon(basisPolygon);
		}

		if (isOmlijnd && lijnkleur != null) 
		{
			g.setColor(lijnkleur);
			g.drawPolygon(basisPolygon);
		}
	}

	public boolean contains(int x, int y) 
	{
		return (basisPolygon.contains(x, y));
	}

	public void zetVulkleur(Color c) 
	{
		vulkleur = c;
		isGevuld = true;
	}

	public void zetLijnkleur(Color c) 
	{
		lijnkleur = c;
		isOmlijnd = true;
	}

	public void zetPolygon() 
	{
		Polygon p = new Polygon();
		for (int j = 0; j < basisPolygon.npoints; j++) 
		{
			p.addPoint(basisPolygon.xpoints[j], basisPolygon.ypoints[j]);
		}
		basisPolygon = p;
	}

	public void verplaats(int dx, int dy) 
	{
		for (int j = 0; j < basisPolygon.npoints; j++) 
		{
			basisPolygon.xpoints[j] += dx;
			basisPolygon.ypoints[j] += dy;

			if (exactePuntenX != null) 
			{
				exactePuntenX[j] += dx;
				exactePuntenY[j] += dy;
			}
		}
		zetPolygon();
	}

	public void schaal(double factorX, double factorY) 
	{
		if (exactePuntenX == null) 
		{
			exactePuntenX = new double[basisPolygon.npoints];
			exactePuntenY = new double[basisPolygon.npoints];
			for (int j = 0; j < basisPolygon.npoints; j++) 
			{
				exactePuntenX[j] = basisPolygon.xpoints[j];
				exactePuntenY[j] = basisPolygon.ypoints[j];
			}
		}
		for (int j = 0; j < basisPolygon.npoints; j++) 
		{
			exactePuntenX[j] = factorX * exactePuntenX[j];
			exactePuntenY[j] = factorY * exactePuntenY[j];
			basisPolygon.xpoints[j] = ((int) exactePuntenX[j]);
			basisPolygon.ypoints[j] = ((int) exactePuntenY[j]);
		}
		zetPolygon();
	}

	public void draai(double dh) 
	{
		if (exactePuntenX == null) 
		{
			exactePuntenX = new double[basisPolygon.npoints];
			exactePuntenY = new double[basisPolygon.npoints];
			for (int j = 0; j < basisPolygon.npoints; j++) 
			{
				exactePuntenX[j] = basisPolygon.xpoints[j];
				exactePuntenY[j] = basisPolygon.ypoints[j];
			}
		}
		double cos = Math.cos(dh * Math.PI / 180);
		double sin = Math.sin(dh * Math.PI / 180);

		for (int j = 0; j < basisPolygon.npoints; j++) 
		{
			double x = exactePuntenX[j] - ((Tekening) getParent()).breedte / 2;
			double y = exactePuntenY[j] - ((Tekening) getParent()).hoogte / 2;
			exactePuntenX[j] = cos * x + sin * y + ((Tekening) getParent()).breedte / 2;
			exactePuntenY[j] = cos * y - sin * x + ((Tekening) getParent()).hoogte / 2;
			basisPolygon.xpoints[j] = ((int) exactePuntenX[j] + 1) / 2 * 2;
			basisPolygon.ypoints[j] = ((int) exactePuntenY[j] + 1) / 2 * 2;
		}
		zetPolygon();
	}
}
