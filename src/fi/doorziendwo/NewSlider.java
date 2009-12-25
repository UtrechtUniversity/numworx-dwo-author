package fi.doorziendwo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class NewSlider	extends JPanel implements MouseListener, MouseMotionListener
{	private Image im;
	private Graphics gIm;
	private boolean resize;
	protected ActionListener actionListener = null;
	
	private int lengte;
	private int stand;
	private int minimum=0;
	private int muisStartX, muisStartY;
	private Polygon schuifKnop;
	private boolean raak;
	
	public NewSlider(int aantalPix, int beginst)
	{	lengte = aantalPix;
		stand = beginst;
		addMouseListener(this);
		addMouseMotionListener(this);
		setSize(lengte+10,13);
	}
	
	public void zetLengte(int aantalPix)
	{	lengte = aantalPix;
		setSize(lengte+10,13);
		resize = true;
		repaint();
	}
		
	public void paintComponent(Graphics g)
	{	{ 	tekenSlider(g);
			
  		}
	}
	
	
	
	public void tekenSlider(Graphics g)
	{	g.setColor(Color.black);
		
		g.drawLine(5,5,lengte+5,5);
		g.setColor(Color.red);
		g.fillOval(5+stand-4, 1, 8, 8);
		g.setColor(Color.black);
		g.drawOval(5+stand-4, 1, 8, 8);
		
	
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
	
	public int geeflengte()
	{	return lengte;
	}
	
	public void zetStand(int std)
	{	if(std>lengte)stand = lengte;
		else if(std<minimum)stand = minimum;
		else stand = std;
		repaint();
	}
	
	public void mousePressed(MouseEvent e)
	{	raak = (new Rectangle(stand,0,10,20)).contains(e.getX(), e.getY());
		muisStartX = e.getX();
		muisStartY = e.getY();
		if (raak && actionListener != null)
		{	actionListener.actionPerformed( new ActionEvent(this, 0, "start") );
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(!raak && new Rectangle(stand,0,10,20).contains(e.getX(), e.getY()))
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
	
	public void mouseReleased(MouseEvent e)
	{	//if (actionListener != null)
		//{	actionListener.actionPerformed( new ActionEvent(this, 0, "stop") );
		//}
	}
	public void mouseClicked(MouseEvent e){;}
	public void mouseExited(MouseEvent e)
	{	//raak = false;
	}
	public void mouseEntered(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
}
