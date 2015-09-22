package fi.binomverdeling;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Gekopieerd uit Normale Verdeling, aangepast door Manu: getMinimums toegevoegd.
 */
public class DoubleSlider extends JComponent implements MouseListener, 
						 						        MouseMotionListener
{	private Image im;
	private Graphics gIm;
	private boolean resize;
	protected ActionListener actionListener = null;
	
	private int lengte;
	
	private int standLinks, standRechts;
	
	private int minimumLinks = -2;
	private int maximumLinks;

	private int minimumRechts;
	private int maximumRechts; 
	
	private int muisStartX, muisStartY;

	private Polygon schuifKnop;
	
	private boolean raakLinks, raakRechts;
	
	private boolean linksEnabled = true;
	private boolean rechtsEnabled = true;
	
	private boolean showLine = true;
	
	private Color knopColor = Color.red;
	
	private Color knopLinksColor = Color.orange;
	private Color knopRechtsColor = Color.magenta;
	
	int pixDis = 10;
	
	public DoubleSlider(int aantalPix, int beginLinks, int beginRechts)
	{	lengte = aantalPix;
		standLinks = beginLinks;
		standRechts = beginRechts;
		// minimumLinks = -2; blijft zo
		maximumLinks = standRechts - pixDis;		
		minimumRechts = standLinks + pixDis - 2;
		maximumRechts = lengte;

		addMouseListener(this);
		addMouseMotionListener(this);
		setSize(lengte + 10, 13);
	}
	
	public void zetLengte(int aantalPix)
	{	lengte = aantalPix;
		maximumRechts = lengte;
		setSize(lengte + 10, 13);
		resize = true;
		repaint();
	}
	
	public int getMinimumRechts() {
		return this.minimumRechts;
	}
	
	public int getMinimumLinks() {
		return this.minimumLinks;
	}
	
	public int getLengte()
	{	return lengte;
	}		
	
	public void zetShowLine(boolean b)
	{	showLine = b;
	}	
	
	public void zetLinksEnabled(boolean b)
	{	linksEnabled = b;
	}

	public void zetRechtsEnabled(boolean b)
	{	rechtsEnabled = b;
	}
	
	public void zetKnopLinksColor(Color c)
	{	knopLinksColor = c;
	}

	public void zetKnopRechtsColor(Color c)
	{	knopRechtsColor = c;
	}
		
	public void paintComponent(Graphics g)
	{	
		{ 	if (im == null || resize)
			{	im = createImage(getSize().width, getSize().height);
  				gIm = im.getGraphics();
			}
			gIm.setColor(getBackground());
			//gIm.fillRect(0, 0, getSize().width, getSize().height);
			//tekenSlider(gIm);
			//g.drawImage(im, 0, 0, null);
			
			tekenSlider(g);
  		}
	}
	
	public void update(Graphics g)
	{	paint(g);
	}
	
	public void tekenSlider(Graphics g)
	{	g.setColor(Color.black);
		if (showLine)
			g.drawLine(5, 5, lengte + 5, 5);
		
		if (linksEnabled)
		{
			g.setColor(knopLinksColor);
			g.fillOval(5 + standLinks - 3, 2, 6, 6);
			g.setColor(Color.black);
			g.drawOval(5 + standLinks - 3, 2, 6, 6);
		}
		
		if (rechtsEnabled)
		{
			g.setColor(knopRechtsColor);
			g.fillOval(5 + standRechts - 3, 2, 6, 6);
			g.setColor(Color.black);
			g.drawOval(5 + standRechts - 3, 2, 6, 6);
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
	
	public int geefStandLinks()
	{	return standLinks;
	}

	public int geefStandRechts()
	{	return standRechts;
	}
	
	public void zetStandLinks(int std)
	{	if (std > maximumLinks)
			standLinks = maximumLinks;
		else if (std < minimumLinks)
			standLinks = minimumLinks;
		else 
			standLinks = std;
			
		minimumRechts = standLinks + pixDis;	
		
		repaint();
		
	}

	public void zetStandRechts(int std)
	{	
		if (std > maximumRechts)
			standRechts = maximumRechts;
		else if (std < minimumRechts)
			standRechts = minimumRechts;
		else 
			standRechts = std;
			
		maximumLinks = standRechts - pixDis;					
		
		repaint();
	}
	
	public void zetMaximumLinks(int max)
	{	maximumLinks = max;
	}

	public void zetMinimumRechts(int min)
	{	minimumRechts = min - 2;
	}
	
	public void mousePressed(MouseEvent e)
	{	
		raakLinks = linksEnabled && 
					(new Rectangle(standLinks, 0, 10, 20)).contains(e.getX(), e.getY());
		if (raakLinks && actionListener != null)
		{	actionListener.actionPerformed(new ActionEvent(this, 0, "startLinks"));
		}
		
		raakRechts = rechtsEnabled && 
					 (new Rectangle(standRechts, 0, 10, 20)).contains(e.getX(), e.getY());
		if (raakRechts && actionListener != null)
		{	actionListener.actionPerformed(new ActionEvent(this, 0, "startRechts"));
		}
		
		muisStartX = e.getX();
//		muisStartY = e.getY();
		
	}
	
	public void mouseDragged(MouseEvent e)
	{	
		if (!raakLinks && linksEnabled && 
			(new Rectangle(standLinks, 0, 10, 20)).contains(e.getX(), e.getY()))
		{	raakLinks = true;
			muisStartX = e.getX();
			if (raakLinks && actionListener != null)
			{	actionListener.actionPerformed(new ActionEvent(this, 0, "startLinks"));
			}
		}
		
		if (!raakRechts && rechtsEnabled && 
			(new Rectangle(standRechts, 0, 10, 20)).contains(e.getX(), e.getY()))
		{	raakRechts = true;
			muisStartX = e.getX();
			if (raakRechts && actionListener != null)
			{	actionListener.actionPerformed(new ActionEvent(this, 0, "startRechts"));
			}
		}
		
		if (raakLinks)
		{	int x = e.getX();
			int dx = x - muisStartX;
			standLinks = standLinks + dx;
			if (standLinks > maximumLinks) 
			{	standLinks = maximumLinks;
			}
			else if (standLinks < minimumLinks) 
			{	standLinks = minimumLinks;
			}
			minimumRechts = standLinks + pixDis;
			
			if (x < 5 || x > lengte + 20)
			{	raakLinks = false;
			}
			repaint();
			if (actionListener != null)
 			{	actionListener.actionPerformed(new ActionEvent(this, 0, "verschovenLinks"));
 			}
			muisStartX = x;
		}

		if (raakRechts)
		{	int x = e.getX();
			int dx = x - muisStartX;
			standRechts = standRechts + dx;
			if (standRechts > maximumRechts) 
			{	standRechts = maximumRechts;
			}
			else if (standRechts < minimumRechts) 
			{	standRechts = minimumRechts;
			}
			
			maximumLinks = standRechts - pixDis;		
			
			if (x < 5 || x > lengte + 20)
			{	raakRechts = false;
			}
			repaint();
			if (actionListener != null)
 			{	actionListener.actionPerformed(new ActionEvent(this, 0, "verschovenRechts"));
 			}
			muisStartX = x;
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{	
		if (actionListener != null)
		{	actionListener.actionPerformed( new ActionEvent(this, 0, "stop") );
		}
	}
	public void mouseClicked(MouseEvent e)
	{}
	public void mouseExited(MouseEvent e)
	{}
	public void mouseEntered(MouseEvent e)
	{}
	public void mouseMoved(MouseEvent e)
	{}
}
