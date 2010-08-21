package fi.binomverdeling;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Slider	extends JComponent implements MouseListener, MouseMotionListener {
	private Image im;
	private Graphics gIm;
	private boolean resize;
	protected ActionListener actionListener = null;
	
	private int lengte;
	private int stand;
	private int minimum = -2;
	private int maximum;
	private int muisStartX, muisStartY;
	private Polygon schuifKnop;
	private boolean raak;
	
	private boolean showLine = true;
	
	private Color knopColor = Color.red;
	
	private boolean enabled = true;
	
	public Slider(int aantalPix, int beginst)
	{	lengte = aantalPix;
		maximum = lengte;
		stand = beginst;
		addMouseListener(this);
		addMouseMotionListener(this);
		setSize(lengte + 10, 13);
	}
	
	public void zetLengte(int aantalPix)
	{	lengte = aantalPix;
		maximum = lengte;
		setSize(lengte + 10, 13);
		resize = true;
		repaint();
	}
		
	public void zetShowLine(boolean b)
	{	showLine = b;
	}	
	
	public void zetKnopColor(Color c)
	{	knopColor = c;
	}
		
	public void zetEnabled(boolean b)
	{	enabled = b;
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
	
		if (enabled)
		{
			g.setColor(knopColor);
			g.fillOval(5 + stand - 3, 2, 6, 6);
			g.setColor(Color.black);
			g.drawOval(5 + stand - 3, 2, 6, 6);
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
	{	return stand;
	}
	
	public void zetStand(int std)
	{	if (std > maximum)
			stand = maximum;
		else if (std < minimum)
			stand = minimum;
		else 
			stand = std;
		repaint();
	}
	
	public void setMaximum(int max)
	{	maximum = max;
	}

	public void setMinimum(int min)
	{	minimum = min - 2;
	}

	public int getMaximum()
	{	return maximum;
	}
	
	public int getMinimum()
	{	return this.minimum;
	}
	
	public void mousePressed(MouseEvent e)
	{	raak = enabled && (new Rectangle(stand, 0, 10, 20)).contains(e.getX(), e.getY());
		muisStartX = e.getX();
		muisStartY = e.getY();
		if (raak && actionListener != null)
		{	actionListener.actionPerformed(new ActionEvent(this, 0, "start"));
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{	if (!raak && enabled && 
			(new Rectangle(stand, 0, 10, 20).contains(e.getX(), e.getY())))
		{	raak = true;
			muisStartX = e.getX();
			if (raak && actionListener != null)
			{	actionListener.actionPerformed(new ActionEvent(this, 0, "start"));
			}
		}
		if (raak)
		{	int x = e.getX();
			int dx = x - muisStartX;
			stand = stand + dx;
			if (stand > maximum) 
			{	stand = maximum;
			}
			else if (stand < minimum) 
			{	stand = minimum;
			}
			if (x < 5 || x > lengte + 20)
			{	raak = false;
			}
			repaint();
			if (actionListener != null)
 			{	actionListener.actionPerformed(new ActionEvent(this, 0, "verschoven"));
 			}
			muisStartX = x;
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{	if (actionListener != null)
		{	actionListener.actionPerformed( new ActionEvent(this, 0, "stop") );
		}
	}
	public void mouseClicked(MouseEvent e)
	{}
	public void mouseExited(MouseEvent e)
	{	//raak = false;
	}
	public void mouseEntered(MouseEvent e)
	{}
	public void mouseMoved(MouseEvent e)
	{	//if (new Rectangle(stand, 0, 10, 20).contains(e.getX(), e.getY()))
		//	setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		//else
		//	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}
