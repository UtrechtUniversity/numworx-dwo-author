package fi.statsim;

import java.awt.Graphics;

import javax.swing.JPanel;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class PaintPanel extends JPanel{
	
	private Munten munten;
	
	public PaintPanel(Munten munten) {
		this.munten = munten;
	}

	public void paintComponent(Graphics g) {
		this.setBackground(Color.white);
		super.paintComponent(g);
		//g.drawString(munten.getText(),100,100);
		if (munten.eenMuntRadio.isSelected()==true) {
			for (int i=1;i<munten.muntCount;i++) {
				g.drawLine(i*(this.getWidth()-90)/munten.maxCount+80, (int) (munten.percentageMunt[i-1]*(this.getHeight()-50))+5, (i+1)*(this.getWidth()-90)/munten.maxCount+80,(int) (munten.percentageMunt[i]*(this.getHeight()-50))+5);	
			}
			g.drawLine(80, 5, 80, this.getHeight()-45);
			g.drawLine(80, (this.getHeight()-40)/2, this.getWidth()-10, (this.getHeight()-40)/2);
			g.drawLine(80, this.getHeight()-45, this.getWidth()-10, this.getHeight()-45);
			for (int i=5;i>=0;i--) {
				g.drawString(i*20+"%",40,(5-i)*(this.getHeight()-50)/5+10);
				g.drawLine(75,(5-i)*(this.getHeight()-50)/5+5,80,(5-i)*(this.getHeight()-50)/5+5);
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
				g.drawLine((this.getWidth()-90)*(i*c)/(a)+80, (this.getHeight()-50), (this.getWidth()-90)*(i*c)/(a)+80, (this.getHeight()-45));
				String s=(i*c)+"";
				g.drawString(s, (this.getWidth()-90)*(i*c)/(a)+70, (this.getHeight()-25));
			}
			Graphics2D g2 = (Graphics2D) g;
			AffineTransform orig = g2.getTransform();
			AffineTransform at = new AffineTransform();
			at.setToRotation(-Math.PI / 2.0, getWidth() / 2.0, getHeight() / 2.0);
			g2.setTransform(at);
			g2.setColor(Color.black);
			g2.drawString(StatSim.rb.getString("percentageHeads"),200,-90);
			g2.setTransform(orig);
		} else {
			g.drawLine(80,this.getHeight()-45,this.getWidth()-60,this.getHeight()-45);
			g.drawLine(80, 5, 80, this.getHeight()-45);
			g.drawLine(80+(this.getWidth()-90)/6,this.getHeight()-45,80+(this.getWidth()-90)/6,this.getHeight()-40);
			g.drawString(StatSim.rb.getString("noHeads"),80+(this.getWidth()-90)/6-30,this.getHeight()-25);
			g.drawLine(80+ (this.getWidth()-90)*3/6, this.getHeight()-45,80+(this.getWidth()-90)*3/6,this.getHeight()-40);
			g.drawString(StatSim.rb.getString("oneHeads"),80+(this.getWidth()-90)*3/6-25,this.getHeight()-25);
			g.drawLine(80+(this.getWidth()-90)*5/6, this.getHeight()-45, 80+(this.getWidth()-90)*5/6, this.getHeight()-40);
			g.drawString(StatSim.rb.getString("twoHeads"),80+(this.getWidth()-90)*5/6-30,this.getHeight()-25);
			
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
				g.drawLine(75,(this.getHeight()-50)-(this.getHeight()-50)*(i*c)/(a)+5, 80, (this.getHeight()-50)-(this.getHeight()-50)*(i*c)/(a)+5);
				String s=(i*c)+"";
				g.drawString(s, 50,(this.getHeight()-50)-(this.getHeight()-50)*(i*c)/(a)+10);
			}
			Graphics2D g2 = (Graphics2D) g;
			AffineTransform orig = g2.getTransform();
			AffineTransform at = new AffineTransform();
			at.setToRotation(-Math.PI / 2.0, getWidth() / 2.0, getHeight() / 2.0);
			g2.setTransform(at);
			g2.setColor(Color.black);
			g2.drawString(StatSim.rb.getString("frequency"),200,-70);
			g2.setTransform(orig);
			
			g.setColor(Color.red);
			g.fillRect(85, 5+(this.getHeight()-50)-munten.geenKop*(this.getHeight()-50)/a, (this.getWidth()-90)/3-10, munten.geenKop*(this.getHeight()-50)/a);
			g.setColor(Color.black);
			g.drawRect(85, 5+(this.getHeight()-50)-munten.geenKop*(this.getHeight()-50)/a, (this.getWidth()-90)/3-10, munten.geenKop*(this.getHeight()-50)/a);
			g.setColor(Color.yellow);
			g.fillRect(85+(this.getWidth()-90)*1/3, 5+(this.getHeight()-50)-munten.eenKop*(this.getHeight()-50)/a, (this.getWidth()-90)/3-10, munten.eenKop*(this.getHeight()-50)/a);
			g.setColor(Color.black);
			g.drawRect(85+(this.getWidth()-90)*1/3, 5+(this.getHeight()-50)-munten.eenKop*(this.getHeight()-50)/a, (this.getWidth()-90)/3-10, munten.eenKop*(this.getHeight()-50)/a);
			g.setColor(Color.green);
			g.fillRect(85+(this.getWidth()-90)*2/3, 5+(this.getHeight()-50)-munten.tweeKop*(this.getHeight()-50)/a, (this.getWidth()-90)/3-10, munten.tweeKop*(this.getHeight()-50)/a);
			g.setColor(Color.black);
			g.drawRect(85+(this.getWidth()-90)*2/3, 5+(this.getHeight()-50)-munten.tweeKop*(this.getHeight()-50)/a, (this.getWidth()-90)/3-10, munten.tweeKop*(this.getHeight()-50)/a);
		}	
	}
}
