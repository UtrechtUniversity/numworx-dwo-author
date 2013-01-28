package fi.javalogoweb.schuifobjects;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import fi.beans.appletutil.*;
import javax.swing.JPanel;


public class SchuifComponent extends JPanel implements MouseListener, MouseMotionListener
{	
	public SchuifVeld schuifveld;
	private int startx = 0;
	private int starty = 0;
			 
	public SchuifComponent(int x, int y, int b, int h, SchuifVeld sv)
	{	setBounds(x,y,b,h);
		setLayout(null);
		if(sv!=null)
		{	addMouseListener(this);
			addMouseMotionListener(this);
			schuifveld = sv;
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	schuifveld.begin();
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
	
	//public void schaal(double s)
	//{	
	//	relx = 1.0*getLocation().x/schaal;
	//	rely = 1.0*getLocation().y/schaal;
	//	super.schaal(s);
	//}
}
