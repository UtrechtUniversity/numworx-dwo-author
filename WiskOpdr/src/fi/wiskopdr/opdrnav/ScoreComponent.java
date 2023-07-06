package fi.wiskopdr.opdrnav;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import fi.wiskopdr.WiskOpdr;

public class ScoreComponent extends JComponent
{	
	private int score = 0;
	private int size = 12;
	private boolean correctieView;
	
	public ScoreComponent(int size)
	{	this.size = size;
	}
	public void paintComponent(Graphics gr)
	{	Graphics g;
	    /*if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
	    {     g = (Graphics2D)gr;
	          ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    }
	    else */
		g=gr;
	
		if(correctieView) {
			Polygon p = new Polygon();
			p.addPoint(0, getHeight());
			p.addPoint(getWidth(), 0);
			p.addPoint(getWidth(), getHeight());
	
			g.setColor(new Color(255,150,0,128));
			g.fillPolygon(p);
		}
		
		g.setColor(Color.black);
		
		Font f = new Font("SansSerif", Font.PLAIN,(size-1)/2);
		g.setFont(f);
		String s = Integer.toString(score);
		int lengte = g.getFontMetrics().stringWidth(s);
		if(score>0)g.drawString(s,(getWidth()-lengte)/2,3*size/5);
	}
	
	public void zetScore(int sc)
	{	score = sc;
		repaint();
	}
	
	public void zetCorrectieView(boolean b)
	{	correctieView = b;
		repaint();
	}
	
	public int geefScore()
	{	return score;
	}
	
}