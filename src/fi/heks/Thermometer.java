package fi.heks;

import java.awt.*;
import fi.heks.scobjects.*;

public class Thermometer extends ScComponent
{
	private int temp;
	
	public Thermometer(int x, int y, int b, int h)
	{	super(x,y,b,h);
		//setBackground(Color.white);
		temp = 0;
	}
	
	public void paint(Graphics g)
	{	
		double schaal = 1.0*getSize().height/300;
		Font f = new Font("SansSerif", Font.PLAIN, (int)(schaal*12));
		g.setColor(Color.black);
		FontMetrics fm = g.getFontMetrics();
		g.setFont(f);
		
		g.setColor(Color.red);
		g.fillOval((int)(schaal*20),(int)(schaal*280),(int)(schaal*19),(int)(schaal*19));
		g.setColor(Color.black);
		g.setColor(Color.white);
		g.fillOval((int)(schaal*25),0,(int)(schaal*9),(int)(schaal*9));
		
		g.setColor(Color.black);
		g.drawOval((int)(schaal*20),(int)(schaal*280),(int)(schaal*19),(int)(schaal*19));
		g.drawOval((int)(schaal*25),0,(int)(schaal*9),(int)(schaal*9));
		
		g.setColor(Color.white);
		g.fillRect((int)(schaal*25),(int)(schaal*5),(int)(schaal*10),(int)(schaal*275));
		
		int h = 190 + 5*temp;
				
		g.setColor(Color.red);
		g.fillRect((int)(schaal*26),(int)(schaal*(300-h)),(int)(schaal*9),(int)(schaal*(h-15)));

		g.setColor(Color.black);
		g.drawLine((int)(schaal*25),(int)(schaal*5),(int)(schaal*25),(int)(schaal*280));
		g.drawLine((int)(schaal*34),(int)(schaal*5),(int)(schaal*34),(int)(schaal*280));

		
		g.setColor(Color.black);
		for(int i=-20 ; i<21 ; i++)
		{	if(i%5==0)
			g.drawLine((int)(schaal*22),(int)(schaal*(110+5*i)),(int)(schaal*34),(int)(schaal*(110+5*i)));
			else
			g.drawLine((int)(schaal*25),(int)(schaal*(110+5*i)),(int)(schaal*34),(int)(schaal*(110+5*i)));	
			
		}
		
		for(int i=-20 ; i<21 ; i+=5)
		{	String s = Integer.toString(i)+"°";
			int sw = fm.stringWidth(s);
			int sh = fm.getHeight();
			g.drawString(Integer.toString(i)+"°",(int)(schaal*(19-sw)),(int)(schaal*(110-5*i+sh/2)));
		}
		
		
		
		//g.drawString(Integer.toString(temp)+"°",0,getSize().height);
	}
	public int geefTemp()
	{	return temp;
		
	}
	public void zetTemp(int t)
	{	temp = t;
		repaint();
	}
	public void tempPlus()
	{	temp++;
		repaint();
	}
	public void tempMin()
	{	temp--;
		repaint();
	}
	
	
}
