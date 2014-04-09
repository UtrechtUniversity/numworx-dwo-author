package fi.statsim;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BinomGrafiek extends JPanel {

	BinomTrekking binomTrekking;
	int[] binomVerdeling;
	
	public BinomGrafiek (BinomTrekking binomTrekking) {
		this.binomTrekking = binomTrekking;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		binomVerdeling = new int[binomTrekking.maxCount];
		for (int i=0;i<binomTrekking.experiment;i++) {
			binomVerdeling[binomTrekking.trekkingen[i]]++;
		}
		int maxHeight=0;
		for (int i=0;i<binomTrekking.maxCount;i++) {
			if (binomVerdeling[i]>maxHeight) {
				maxHeight=binomVerdeling[i];
			}
		}
		if (maxHeight<10) maxHeight=10;
	
		int d=maxHeight;
		int e=0;
		int f=0;
		while (true) {
			if (d/(1*Math.pow(10, e))<7) {
				f =1*(int)Math.pow(10, e);
				break;
			}
			if (d/(2*Math.pow(10, e))<7) {
				f =2*(int)Math.pow(10, e);
				break;
			}
			if (d/(3*Math.pow(10, e))<7) {
				f =3*(int)Math.pow(10, e);
				break;
			}
			if (d/(5*Math.pow(10, e))<7) {
				f =5*(int)Math.pow(10, e);
				break;
			}
			//if (d/(10*Math.pow(10, e))<7) {
			//	f =10*(int)Math.pow(10, e);
			//	break;
			//}
			e++;
		}
		System.out.println(f);
		int numMarks1=d/f;
		
		for (int i=0;i<=numMarks1;i++) {
			if (maxHeight>0) {
				g.drawLine(45,this.getHeight()-40-(i*f*(getHeight()-50)/d),50,this.getHeight()-40-(i*f*(getHeight()-50)/d));
				g.drawString(i*f+"", 25,this.getHeight()-40-(i*f*(getHeight()-50)/d));
			}
		}
		
		for (int i=0;i<binomTrekking.maxCount;i++) {
			if (maxHeight>0) {
				g.setColor(Color.red);
				g.fillRect(50+(i*(this.getWidth()-60)/binomTrekking.maxCount), (maxHeight-binomVerdeling[i])*(this.getHeight()-50)/maxHeight+10, ((this.getWidth()-60)/binomTrekking.maxCount), (binomVerdeling[i])*(this.getHeight()-50)/maxHeight);
				g.setColor(Color.black);
				g.drawRect(50+(i*(this.getWidth()-60)/binomTrekking.maxCount), (maxHeight-binomVerdeling[i])*(this.getHeight()-50)/maxHeight+10, ((this.getWidth()-60)/binomTrekking.maxCount), (binomVerdeling[i])*(this.getHeight()-50)/maxHeight);
			}
		}
		
		g.drawLine(50,10,50,this.getHeight()-40);
		g.drawLine(50,this.getHeight()-40,this.getWidth()-10,this.getHeight()-40);
		
		int a=binomTrekking.maxCount;
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
			g.drawLine((this.getWidth()-60)*(i*c)/(a)+50, this.getHeight()-40, (this.getWidth()-60)*(i*c)/(a)+50, this.getHeight()-35);
			String s=(i*c)+"";
			g.drawString(s, (this.getWidth()-60)*(i*c)/(a)+45, this.getHeight()-20);
		}
	}
}
