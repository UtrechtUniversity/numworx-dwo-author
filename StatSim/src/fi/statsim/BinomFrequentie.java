package fi.statsim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class BinomFrequentie extends JPanel{

	private BinomTrekking binomTrekking;
	
	public BinomFrequentie(BinomTrekking binomTrekking) {
		this.binomTrekking = binomTrekking;
	}
	public void paintComponent(Graphics g) {
		this.setBackground(Color.white);
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform orig = g2.getTransform();
		AffineTransform at = new AffineTransform();
	    at.setToRotation(-Math.PI / 2.0, getWidth() / 2.0, getHeight() / 2.0);
	    g2.setTransform(at);
	    g2.setColor(Color.black);
		g2.drawString(StatSim.rb.getString("frequency"),80,-30);
		g2.setTransform(orig);
		
		g.drawString (StatSim.rb.getString("so"),90,90);
		g.drawString (StatSim.rb.getString("notso"),150,90);
		
		g.setColor(Color.yellow);
		g.fillRect(65, 10+60-(int)60*(binomTrekking.totaal)/(int)(binomTrekking.maxCount), 60, (int)60*(binomTrekking.totaal)/(int)(binomTrekking.maxCount));
		g.setColor(Color.black);
		g.drawRect(65, 10+60-(int)60*(binomTrekking.totaal)/(int)(binomTrekking.maxCount), 60, (int)60*(binomTrekking.totaal)/(int)(binomTrekking.maxCount));
		
		g.setColor(Color.red);
		g.fillRect(135, 10+60-60*(binomTrekking.trekkingCount-binomTrekking.totaal)/(int)(binomTrekking.maxCount), 60, 60*(binomTrekking.trekkingCount-binomTrekking.totaal)/(int)(binomTrekking.maxCount));
		g.setColor(Color.black);
		g.drawRect(135, 10+60-60*(binomTrekking.trekkingCount-binomTrekking.totaal)/(int)(binomTrekking.maxCount), 60, 60*(binomTrekking.trekkingCount-binomTrekking.totaal)/(int)(binomTrekking.maxCount));
		
		g.drawLine(55, 10, 55, 70);
		g.drawLine(50,10,55,10);
		g.drawLine(50, 70, 55, 70);
		
		g.drawString("0", 40, 75);
		g.drawString((int)(binomTrekking.maxCount)+ "",25,15);
	}
}
