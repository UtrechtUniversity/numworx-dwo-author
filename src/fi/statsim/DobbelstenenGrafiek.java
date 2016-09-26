package fi.statsim;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DobbelstenenGrafiek extends JPanel{

	private Dobbelstenen dobbelstenen;
	boolean displaySom;
	
	public DobbelstenenGrafiek (Dobbelstenen dobbelstenen) {
		this.dobbelstenen = dobbelstenen;
	}
	
	public void paintComponent(Graphics g) {
		this.setBackground(Color.white);
		super.paintComponent(g);
		g.drawLine(50,20,50,this.getHeight()-30);
		g.drawLine(50,this.getHeight()-30,this.getWidth()-10,this.getHeight()-30);
		
		int a=0;
		if (displaySom==true) {
			for (int i=0;i<19;i++) {
				if (dobbelstenen.ogenSom[i]>a)
					a=dobbelstenen.ogenSom[i];
			}
			if (a==0)
				a=1;
		} else {
			a=dobbelstenen.maxCount/2;
			if (a==0)
				a=1;
		}
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
			g.drawLine(45,(this.getHeight()-40)-(this.getHeight()-50)*(i*c)/(a)+10, 50, (this.getHeight()-40)-(this.getHeight()-50)*(i*c)/(a)+10);
			String s=(i*c)+"";
			g.drawString(s, 25,(this.getHeight()-40)-(this.getHeight()-50)*(i*c)/(a)+15);
		}
		int numBars=0;
		int skipNum=0;
		String ogenString="";
		if (dobbelstenen.eenDobbelsteenRadio.isSelected()) {
			numBars=6;
			ogenString=StatSim.rb.getString("eyes");
		}
		if (dobbelstenen.tweeDobbelstenenRadio.isSelected()) {
			numBars=11;
			skipNum=1;
			ogenString=StatSim.rb.getString("sumEyes");
		}
		if (dobbelstenen.drieDobbelstenenRadio.isSelected()) {
			numBars=16;
			skipNum=2;
			ogenString=StatSim.rb.getString("sumEyes");
		}
		
		g.drawString(ogenString, 5, this.getHeight()-10);
		g.drawString(StatSim.rb.getString("frequency"), 5, 10);
		for (int i=0;i<numBars;i++) {
			g.setColor(Color.RED);
			if (displaySom==true) {
				g.fillRect(50+i*(this.getWidth()-60)/numBars+5, 20+(a-dobbelstenen.ogenSom[i+1+skipNum])*(this.getHeight()-50)/a, (this.getWidth()-60)/numBars-10, dobbelstenen.ogenSom[i+1+skipNum]*(this.getHeight()-50)/a);
				g.setColor(Color.black);
				g.drawRect(50+i*(this.getWidth()-60)/numBars+5, 20+(a-dobbelstenen.ogenSom[i+1+skipNum])*(this.getHeight()-50)/a, (this.getWidth()-60)/numBars-10, dobbelstenen.ogenSom[i+1+skipNum]*(this.getHeight()-50)/a);
			} else {
				g.fillRect(50+i*(this.getWidth()-60)/numBars+5, 20+(a-dobbelstenen.ogen[i+1+skipNum])*(this.getHeight()-50)/a, (this.getWidth()-60)/numBars-10, dobbelstenen.ogen[i+1+skipNum]*(this.getHeight()-50)/a);
				g.setColor(Color.black);
				g.drawRect(50+i*(this.getWidth()-60)/numBars+5, 20+(a-dobbelstenen.ogen[i+1+skipNum])*(this.getHeight()-50)/a, (this.getWidth()-60)/numBars-10, dobbelstenen.ogen[i+1+skipNum]*(this.getHeight()-50)/a);				
			}
			g.drawLine(50+i*(this.getWidth()-60)/numBars+(this.getWidth()-60)/(numBars*2), this.getHeight()-30, 50+i*(this.getWidth()-60)/numBars+(this.getWidth()-60)/(numBars*2), this.getHeight()-25);
			g.drawString(i+1+skipNum+"",50+i*(this.getWidth()-60)/numBars+(this.getWidth()-60)/(numBars*2), this.getHeight()-10);
			if (displaySom==true)
				g.drawString(dobbelstenen.ogenSom[i+1+skipNum]+"",50+i*(this.getWidth()-60)/numBars+(this.getWidth()-60)/(numBars*2), (a-dobbelstenen.ogenSom[i+1+skipNum])*(this.getHeight()-50)/a+10);
			else
				g.drawString(dobbelstenen.ogen[i+1+skipNum]+"",50+i*(this.getWidth()-60)/numBars+(this.getWidth()-60)/(numBars*2), (a-dobbelstenen.ogen[i+1+skipNum])*(this.getHeight()-50)/a+10);
		}
		
	}
}
