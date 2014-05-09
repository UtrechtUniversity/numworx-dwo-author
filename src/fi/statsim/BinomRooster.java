package fi.statsim;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BinomRooster extends JPanel {

	BinomTrekking binomTrekking;
	
	public BinomRooster (BinomTrekking binomTrekking) {
		this.binomTrekking = binomTrekking;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int a=this.getHeight()-20;
		if (this.getWidth()<this.getHeight())
			a=this.getWidth()-20;
		
		int b=binomTrekking.maxCount;
		int c=a/binomTrekking.maxCount;
		g.setColor(Color.lightGray);
		for (int i=0;i<binomTrekking.maxCount;i++) {
			g.drawLine(10+i*c, 10+a, 10+i*c, 10+a-b*c);
			g.drawLine(10,10+a-i*c,10+b*c,10+a-i*c);
			b--;
		}
			
	}
}