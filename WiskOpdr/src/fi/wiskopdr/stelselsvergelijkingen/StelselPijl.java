package fi.wiskopdr.stelselsvergelijkingen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

public class StelselPijl extends JLabel{
	
	int xBegin, xEind;
	int hoogte = 20;
	
	public StelselPijl(int x1, int x2)
	{
		xBegin = x1;
		xEind = x2;
		setLayout(null);
		setSize(Math.max(5, Math.abs(xBegin - xEind)), hoogte);
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.gray);
		if(xBegin < xEind)
		{
			g.drawLine(0, 0, xEind - xBegin, hoogte);
		}
		else
			g.drawLine(xBegin - xEind, 0, 0, hoogte);
		
	}
	
	public void zetBeginX(int x)
	{
		xBegin = x;
	}
	
	public void zetEindX(int x)
	{
		xEind = x;
		setSize(Math.max(5, Math.abs(xBegin - xEind)), hoogte);
		repaint();
	}

}
