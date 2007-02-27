package fi.geomalgebra;

import java.awt.*;
import java.awt.event.*;

public class Slider	extends Container implements MouseListener, MouseMotionListener
{	private Image im;
	private Graphics gIm;
	
	protected ActionListener actionListener = null;
	
	private int lengte;
	private int stand;
	private int muisStartX, muisStartY;
	private Polygon schuifKnop;
	private boolean raak;
	
	public Slider(int aantalPix, int beginst)
	{	lengte = aantalPix;
		stand = beginst;
		addMouseListener(this);
		addMouseMotionListener(this);
		setSize(lengte+20,20);
	}
		
	public void paint(Graphics g)
	{	{ 	if(im==null)
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
		g.drawRect(10,7,lengte,6);
		schuifKnop = new Polygon();
		schuifKnop.addPoint(10+stand,0);
		schuifKnop.addPoint(10+stand+3,5);
		schuifKnop.addPoint(10+stand+3,15);
		schuifKnop.addPoint(10+stand,20);
		schuifKnop.addPoint(10+stand-3,15);
		schuifKnop.addPoint(10+stand-3,5);
		g.setColor(new Color(255,100,100));
		g.fillPolygon(schuifKnop);
		g.setColor(Color.black);
		g.drawPolygon(schuifKnop);
	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
	
	public int geefStand()
	{	return stand;
	}
	
	public void zetStand(int std)
	{	if(std>lengte)stand = lengte;
		else if(std<0)stand = 0;
		else stand = std;
		repaint();
	}
	
	public void mousePressed(MouseEvent e)
	{	raak = (new Rectangle(stand+5,0,10,20)).contains(e.getX(), e.getY());
		muisStartX = e.getX();
		muisStartY = e.getY();
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(!raak && new Rectangle(stand+5,0,10,20).contains(e.getX(), e.getY()))
		{	raak = true;
			muisStartX = e.getX();
		}
		if(raak)
		{	int x = e.getX();
			int dx = x - muisStartX;
			stand = stand + dx;
			if(stand>lengte) 
			{	stand = lengte;
			}
			else if(stand<0) 
			{	stand = 0;
			}
			if(x<10 || x>lengte+20)
			{	raak = false;
			}
			repaint();
			if (actionListener != null)
 			{	actionListener.actionPerformed( new ActionEvent(this, 0, "verschoven") );
 			}
			muisStartX = x;
		}
	}
	
	public void mouseReleased(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
}
