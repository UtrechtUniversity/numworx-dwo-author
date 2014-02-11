package fi.statsim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class FrequentieClass extends JPanel{

	private StatSimInteractiePanel munten;
	
	public FrequentieClass(StatSimInteractiePanel munten) {
		this.munten = munten;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform orig = g2.getTransform();
		AffineTransform at = new AffineTransform();
	    at.setToRotation(-Math.PI / 2.0, getWidth() / 2.0, getHeight() / 2.0);
	    g2.setTransform(at);
	    g2.setColor(Color.black);
		g2.drawString("Frequentie",80,-30);
		g2.setTransform(orig);
		
		g.drawString ("Kop",90,90);
		g.drawString ("Munt",150,90);
		
		g.setColor(Color.yellow);
		g.fillRect(65, 10+60-(int)60*(munten.muntCount-munten.totaalmunt)/(int)(munten.maxCount*0.75), 60, (int)60*(munten.muntCount-munten.totaalmunt)/(int)(munten.maxCount*0.75));
		g.setColor(Color.black);
		g.drawRect(65, 10+60-(int)60*(munten.muntCount-munten.totaalmunt)/(int)(munten.maxCount*0.75), 60, (int)60*(munten.muntCount-munten.totaalmunt)/(int)(munten.maxCount*0.75));
		
		g.setColor(Color.red);
		g.fillRect(135, 10+60-60*(munten.totaalmunt)/(int)(munten.maxCount*0.75), 60, 60*munten.totaalmunt/(int)(munten.maxCount*0.75));
		g.setColor(Color.black);
		g.drawRect(135, 10+60-60*(munten.totaalmunt)/(int)(munten.maxCount*0.75), 60, 60*munten.totaalmunt/(int)(munten.maxCount*0.75));
		
		g.drawLine(55, 10, 55, 70);
		g.drawLine(50,10,55,10);
		g.drawLine(50, 70, 55, 70);
		
		g.drawString("0", 40, 75);
		g.drawString((int)(munten.maxCount*0.75)+ "",25,15);
	}
}
