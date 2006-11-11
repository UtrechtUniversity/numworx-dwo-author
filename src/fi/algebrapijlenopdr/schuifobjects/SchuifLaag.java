package fi.algebrapijlenopdr.schuifobjects;


import java.awt.*;

public class SchuifLaag extends Container
{
	boolean actief; 
	
	public SchuifLaag(int x, int y, int b, int h)
	{	setBounds(x,y,b,h);
		setLayout(null);
		actief = false;
	}
	
	public void zetActief(boolean b)
	{	actief = b;
	}
	
	public void paint(Graphics g)
	{	if(actief)super.paint(g);
	}
	
}
