package fi.algebrapijlenopdr.schuifobjects;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;


public class SchuifComponent extends Container implements MouseListener, MouseMotionListener
{	
	public SchuifVeld schuifveld;
	protected int startx = 0;
	protected int starty = 0;
	
	public SchuifComponent(SchuifVeld sv)
	{	setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		schuifveld = sv;
	}
	
	public SchuifComponent(int x, int y, int b, int h, SchuifVeld sv)
	{	setBounds(x,y,b,h);
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		schuifveld = sv;
	}
	
	/*public void plaatsOpGrid()
	{	int x = getLocation().x+300;
		int y = getLocation().y+300;
		int ex = x%10;
		int ey = y%10;
		if(ex<5)verplaats(-ex,0);
		else verplaats(10-ex,0);
		if(ey<5)verplaats(0,-ey);
		else verplaats(0,10-ey);
	}*/
	
	public void mousePressed(MouseEvent e)
	{	schuifveld.start();
		startx = e.getX();
		starty = e.getY();
		schuifveld.zetSchuiver(this);
	}
	
	public void mouseDragged(MouseEvent e)
	{	int dx = e.getX() - startx;
		int dy =  e.getY() - starty;
		int x = getLocation().x + dx;
		int y = getLocation().y + dy;
		
		if(schuifveld.isGesloten())
		{	int b = getSize().width;
			int h = getSize().height;
			int bp = schuifveld.getSize().width;
			int hp = schuifveld.getSize().height;
			if(x < 0)x = 0;
			if(x > bp-b)x = bp-b;
			if(y < 0)y = 0;
			if(y > hp-h)y = hp-h;
		}
		setLocation(x,y);
	}
	
	public void mouseReleased(MouseEvent e)
	{	schuifveld.losSchuiver(this);
	}
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
}
