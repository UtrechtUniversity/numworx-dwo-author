package fi.javalogoweb;

import java.awt.*;
import java.awt.event.*;

public class ScrollSlider	extends Panel implements MouseListener, MouseMotionListener
{	private Image im;
	private Graphics gIm;
	private boolean resize;
	protected ActionListener actionListener = null;
	
	private int lengte;
	private int stand;
	private int minimum=-2;
	private int muisStartX, muisStartY;
	private Polygon schuifKnop;
	private boolean raak;
	private boolean focus;
	
	private boolean horizontaal = true;
	private static Image scrollknop0, scrollknop1, scrollknop2;
	private double factor = 1;
	
	public ScrollSlider(int aantalPix, int beginst, boolean hor)
	{	horizontaal = hor;
		lengte = aantalPix;
		stand = beginst;
		addMouseListener(this);
		addMouseMotionListener(this);
		if (horizontaal) setSize(lengte+10,10);
		else setSize(20,lengte+10);
	}
	
	public static void zetScrollPlaatjes(Image sk0, Image sk1, Image sk2)
	{	scrollknop0 = sk0;
		scrollknop1 = sk1;
		scrollknop2 = sk2;
	}
	
	public void zetLengte(int aantalPix)
	{	lengte = aantalPix;
		if (horizontaal) setSize(lengte+10,10);
		else setSize(20,lengte+10);
		resize = true;
		repaint();
	}
		
	public void paint(Graphics g)
	{	{ 	if(im==null || resize)
			{	im = createImage(getSize().width,getSize().height);
  				gIm = im.getGraphics();
			}
			gIm.setColor(getBackground());
			gIm.fillRect(0,0,getSize().width,getSize().height);
			tekenSlider(gIm);
			g.drawImage(im, 0, 0, null);
  		}
	}
	
	public void update(Graphics g)
	{	paint(g);
	}
	
	public void tekenSlider(Graphics g)
	{	g.setColor(Color.black);
		
		if(horizontaal) 
		{	g.drawLine(5,5,lengte+5,5);
			g.setColor(Color.red);
			g.fillOval(5+stand-3, 2, 6, 6);
			g.setColor(Color.black);
			g.drawOval(5+stand-3, 2, 6, 6);
		}
		else
		{	g.setColor(Color.black);
			g.drawRect(8,5,3,lengte);
			g.setColor(Color.red);
			g.fillOval(2, 5+stand-3, 16, 6);
			g.setColor(Color.black);
			g.drawOval(2, 5+stand-3, 16, 6);
			if(raak)g.drawImage(scrollknop2,1,stand+2,null);
			else if(focus) g.drawImage(scrollknop1,1,stand+2,null);
			else g.drawImage(scrollknop0,1,stand+2,null);
		}
		
	
		/*g.drawRect(5,7,lengte,6);
		schuifKnop = new Polygon();
		schuifKnop.addPoint(5+stand,0);
		schuifKnop.addPoint(5+stand+3,5);
		schuifKnop.addPoint(5+stand+3,15);
		schuifKnop.addPoint(5+stand,20);
		schuifKnop.addPoint(5+stand-3,15);
		schuifKnop.addPoint(5+stand-3,5);
		g.fillPolygon(schuifKnop);
		g.drawPolygon(schuifKnop);*/
	
	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
	
	public int geefStand()
	{	return (int)(factor*stand);
	}
	
	public void zetStand(int std)
	{	if(std>lengte)
		{	factor = 1.0*std/lengte;
			stand = lengte;
		}
		else if(std<minimum)
		{	stand = minimum;
			factor=1;
		}
		else 
		{	stand = std;
			factor=1;
		}
		repaint();
	}
	
	public void mousePressed(MouseEvent e)
	{	if(horizontaal) raak = contains(e.getX(), e.getY());
		else  raak = contains(e.getX(), e.getY());
		muisStartX = e.getX();
		muisStartY = e.getY();
		if (raak && actionListener != null)
		{	actionListener.actionPerformed( new ActionEvent(this, 0, "start") );
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(horizontaal) 
		{
			if(!raak && new Rectangle(stand,0,10,20).contains(e.getX(), e.getY()))
			{	raak = true;
				muisStartX = e.getX();
				if (raak && actionListener != null)
				{	actionListener.actionPerformed( new ActionEvent(this, 0, "start") );
				}
			}
			if(raak)
			{	int x = e.getX();
				int dx = x - muisStartX;
				stand = stand + dx;
				if(stand>lengte) 
				{	stand = lengte;
				}
				else if(stand<minimum) 
				{	stand = minimum;
				}
				if(x<5 || x>lengte+20)
				{	raak = false;
				}
				repaint();
				if (actionListener != null)
	 			{	actionListener.actionPerformed( new ActionEvent(this, 0, "verschoven") );
	 			}
				muisStartX = x;
			}
		}
		else
		{
			if(!raak && contains(e.getX(), e.getY()))
			{	raak = true;
				muisStartY = e.getY();
				if (raak && actionListener != null)
				{	actionListener.actionPerformed( new ActionEvent(this, 0, "start") );
				}
			}
			if(raak)
			{	int y = e.getY();
				int dy = y - muisStartY;
				stand = stand + dy;
				if(stand>lengte) 
				{	stand = lengte;
				}
				else if(stand<minimum) 
				{	stand = minimum;
				}
				if(y<5 || y>lengte+20)
				{	raak = false;
				}
				repaint();
				if (actionListener != null)
	 			{	actionListener.actionPerformed( new ActionEvent(this, 0, "verschoven") );
	 			}
				muisStartY = y;
			}
		}
	}
	
	public boolean contains(int x, int y)
	{
		return new Rectangle(0,stand,20,35).contains(x, y);
	}
	
	public void mouseReleased(MouseEvent e)
	{	raak = false;
		repaint();
	}
	public void mouseClicked(MouseEvent e){;}
	public void mouseExited(MouseEvent e)
	{	raak = false;
		focus = false;
		repaint();
	}
	public void mouseEntered(MouseEvent e)
	{	focus = true;
		repaint();
	}
	public void mouseMoved(MouseEvent e){;}
}
