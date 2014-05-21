package fi.statsim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class BinomRooster extends JPanel {

	BinomTrekking binomTrekking;
	
	public BinomRooster (BinomTrekking binomTrekking) {
		this.binomTrekking = binomTrekking;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int a=this.getHeight()-20;
		int d=0;
		if (this.getWidth()<this.getHeight()) {
			a=this.getWidth()-20;
			d=this.getHeight()-a-20;
		}
		
		int b=binomTrekking.maxCount;
		double c=a/binomTrekking.maxCount;
		g.setColor(Color.lightGray);
		for (int i=0;i<binomTrekking.maxCount;i++) {
			g.drawLine((int)(10+i*c), 10+a+d, (int)(10+i*c), (int)(10+a-b*c)+d);
			g.drawLine(10,10+a-(int)(i*c)+d,10+(int)(b*c),10+a-(int)(i*c)+d);
			b--;
		}
		b=binomTrekking.maxCount;
		for (int i=0;i<binomTrekking.maxCount;i++) {
			for (int j=0;j<b;j++) {
				g.fillRect(10+(int)(c*j)-1, 10+a-(int)(c*i)-1+d, 3, 3);
			}
			b--;
		}
		double x=0;
		double y=a;
		double x1=0;
		double y1=0;
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(3));
		String s="";
		for (int i=0;i<binomTrekking.trekkingCount;i++) {
			if (binomTrekking.trekkingenGeschiedenis[i]==true) {
				y1=y-c;
				x1=x;
				s=s+ "W";
			} else {
				y1=y;
				x1=x+c;
				s=s+"N";
			}
			g2.drawLine((int)x+10, (int)y+10+d, (int)x1+10, (int)y1+10+d);
			x=x1;
			y=y1;
		}
		g2.drawString(s, a/3+10, a/3-10+d);
	}
}