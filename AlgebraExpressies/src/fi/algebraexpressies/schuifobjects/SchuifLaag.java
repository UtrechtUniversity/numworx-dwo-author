package fi.algebraexpressies.schuifobjects;


import java.awt.*;

import javax.swing.*;

public class SchuifLaag extends JPanel //Container
{
	boolean actief; 
	
	public SchuifLaag(int x, int y, int b, int h)
	{	setBounds(x,y,b,h);
		setLayout(null);
		actief = false;
		
		setOpaque(false);		
	}
	
	public void zetActief(boolean b)
	{	actief = b;
	}
	
/*	
	public void paint(Graphics g)
	{	if(actief)super.paint(g);
	}
*/	
}
