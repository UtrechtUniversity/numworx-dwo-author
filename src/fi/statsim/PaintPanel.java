package fi.statsim;

import java.awt.Graphics;

import javax.swing.JPanel;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class PaintPanel extends JPanel{
	
	private StatSimInteractiePanel munten;
	
	public PaintPanel(StatSimInteractiePanel munten) {
		this.munten = munten;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//g.drawString(munten.getText(),100,100);
		if (munten.eenMuntRadio.isSelected()==true) {
			for (int i=1;i<munten.muntCount;i++) {
				g.drawLine(i*5*100/munten.maxCount+80, (int) (munten.percentageMunt[i-1]*300)+5, (i+1)*5*100/munten.maxCount+80,(int) (munten.percentageMunt[i]*300)+5);	
			}
			g.drawLine(80, 5, 80, 305);
			g.drawLine(80, 155, 580, 155);
			g.drawLine(80, 305, 580, 305);
			for (int i=5;i>=0;i--) {
				g.drawString(i*20+"%",40,(5-i)*300/5+10);
				g.drawLine(75,(5-i)*300/5+5,80,(5-i)*300/5+5);
			}
			int a=munten.maxCount;
			int b=0;
			int c=0;
			while (true) {
				if (a/(2*Math.pow(10, b))<7) {
					c =2*(int)Math.pow(10, b);
					break;
				}
				if (a/(3*Math.pow(10, b))<7) {
					c =3*(int)Math.pow(10, b);
					break;
				}
				if (a/(5*Math.pow(10, b))<7) {
					c =5*(int)Math.pow(10, b);
					break;
				}
				if (a/(10*Math.pow(10, b))<7) {
					c =10*(int)Math.pow(10, b);
					break;
				}
				b++;
			}
			System.out.println(c);
			int numMarks=a/c;
			for (int i=0;i<=numMarks;i++) {
				g.drawLine(500*(i*c)/(a)+80, 300, 500*(i*c)/(a)+80, 305);
				String s=(i*c)+"";
				g.drawString(s, 500*(i*c)/(a)+70, 325);
			}
			Graphics2D g2 = (Graphics2D) g;
			AffineTransform orig = g2.getTransform();
			AffineTransform at = new AffineTransform();
			at.setToRotation(-Math.PI / 2.0, getWidth() / 2.0, getHeight() / 2.0);
			g2.setTransform(at);
			g2.setColor(Color.black);
			g2.drawString("Percentage kop",200,-90);
			g2.setTransform(orig);
		} else {
			g.drawLine(80,305,530,305);
			g.drawLine(80, 5, 80, 305);
			g.drawLine(80+450/6,305,80+450/6,310);
			g.drawString("Geen kop",80+450/6-30,325);
			g.drawLine(80+ 450*3/6, 305,80+450*3/6,310);
			g.drawString("Een kop",80+450*3/6-25,325);
			g.drawLine(80+450*5/6, 305, 80+450*5/6, 310);
			g.drawString("Twee kop",80+450*5/6-30,325);
			
			int a;			
			if (Double.parseDouble(munten.kansOpKopText.getText())>0.75 || Double.parseDouble(munten.kansOpKopText.getText())<0.25)
				a=munten.maxCount;
			else
				a=munten.maxCount*3/4;
			int b=0;
			int c=0;
			while (true) {
				if (a/(2*Math.pow(10, b))<7) {
					c =2*(int)Math.pow(10, b);
					break;
				}
				if (a/(3*Math.pow(10, b))<7) {
					c =3*(int)Math.pow(10, b);
					break;
				}
				if (a/(5*Math.pow(10, b))<7) {
					c =5*(int)Math.pow(10, b);
					break;
				}
				if (a/(10*Math.pow(10, b))<7) {
					c =10*(int)Math.pow(10, b);
					break;
				}
				b++;
			}
			System.out.println(c);
			int numMarks=a/c;
			for (int i=0;i<=numMarks;i++) {
				g.drawLine(75,300-300*(i*c)/(a)+5, 80, 300-300*(i*c)/(a)+5);
				String s=(i*c)+"";
				g.drawString(s, 50,300-300*(i*c)/(a)+10);
			}
			Graphics2D g2 = (Graphics2D) g;
			AffineTransform orig = g2.getTransform();
			AffineTransform at = new AffineTransform();
			at.setToRotation(-Math.PI / 2.0, getWidth() / 2.0, getHeight() / 2.0);
			g2.setTransform(at);
			g2.setColor(Color.black);
			g2.drawString("Frequentie",200,-70);
			g2.setTransform(orig);
			
			g.setColor(Color.red);
			g.fillRect(85, 5+300-munten.geenKop*300/a, 450/3-10, munten.geenKop*300/a);
			g.setColor(Color.black);
			g.drawRect(85, 5+300-munten.geenKop*300/a, 450/3-10, munten.geenKop*300/a);
			g.setColor(Color.yellow);
			g.fillRect(85+150, 5+300-munten.eenKop*300/a, 450/3-10, munten.eenKop*300/a);
			g.setColor(Color.black);
			g.drawRect(85+150, 5+300-munten.eenKop*300/a, 450/3-10, munten.eenKop*300/a);
			g.setColor(Color.green);
			g.fillRect(85+300, 5+300-munten.tweeKop*300/a, 450/3-10, munten.tweeKop*300/a);
			g.setColor(Color.black);
			g.drawRect(85+300, 5+300-munten.tweeKop*300/a, 450/3-10, munten.tweeKop*300/a);
		}
		
	}
}
