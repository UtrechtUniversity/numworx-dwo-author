package fi.spot_problems_dwo.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class FormuleButton extends JPanel implements MouseListener	
{	
	private Image im;
	private Graphics gIm;
	
	protected String code;
	private Font defaultfont = new Font("SansSerif", Font.PLAIN, 16);
	private FontMetrics fm;
	protected Color bgColor = new Color(210,210,210);	
	protected Color fgColor = Color.black;
	protected Color bgUseColor = bgColor;
	protected Color fgUseColor = fgColor;
	private Color vlakkleur;
	private int kubusNummer;
	private Rectangle[] zijvlakken;
	protected boolean focus = false;
	protected boolean actief = false;
	
	
	protected ActionListener actionListener = null;

		
	public FormuleButton(String s)
	{	code = s;
		addMouseListener(this);
		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);
	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
  
	
	public void zetActief(boolean b)
	{	actief = b;
		repaint();
	}
		
	public void setBackground(Color c)
	{	//bgColor = c;
		//bgUseColor = c;
	}
	
	public void paint(Graphics g)
	{	{ 	
			//if(im==null)
			//{	im = createImage(getSize().width,getSize().height);
  			//	gIm = im.getGraphics();
			//}
			gIm = g;
			gIm.setColor(getBackground());
			gIm.fillRect(0,0,getSize().width,getSize().height);
			paintBuffer(gIm);
			//g.drawImage(im, 0, 0, null);
  		}
	}
	
/*	
	public void update(Graphics g)
	{	paint(g);
	}
*/	
	public void paintBuffer(Graphics g)
	{	
		g.setColor(fgColor);
		{	g.setColor(bgColor);
		int red = bgColor.getRed();
		int green = bgColor.getGreen();
		int blue = bgColor.getBlue();
		for(int i=0 ; i<10 ; i++)
		{
			g.setColor(new Color(red + i*(245-red)/10,green + i*(245-green)/10,blue + i*(245-blue)/10));
			//g.setColor(new Color(200+5*i,200+5*i,200+5*i));
			g.fillRect(0,getHeight()-(i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
		}
		if(focus)
		{	if(actief)g.setColor(bgColor.darker());
			else g.setColor(bgColor.brighter());
			g.drawLine(0,0,getSize().width-1,0);
			g.drawLine(0,0,0,getSize().height-1);
			if(actief)g.setColor(bgColor.brighter());
			else g.setColor(bgColor.darker());
			g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
			g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
		}
		}
		int b = getSize().width;
		int h = getSize().height;
		g.setColor(Color.black);
		if(code.equals("wortel"))
		{	g.drawLine(3,2*h/4,h/4,h-3);
			g.drawLine(4,2*h/4,h/4+1,h-3);
			g.drawLine(h/4+1,h-3,h/2,3);
			g.drawLine(h/2,3,b-3,3);
            g.setColor(Color.white);
            g.fillRect(h/2+2,6,4,10);
            g.setColor(Color.gray);
			g.drawRect(h/2+2,6,4,10);
		}
		else if(code.equals("wortelbewerk"))
        {   g.drawLine(3,2*h/4,h/4,h-3);
            g.drawLine(4,2*h/4,h/4+1,h-3);
            g.drawLine(h/4+1,h-3,h/2,3);
            g.drawLine(h/2,3,b-3,3);
           
        }
		else if(code.equals("macht"))
		{	g.setColor(Color.white);
            g.fillRect(6,6,4,10);
            g.fillRect(13,3,3,6);
            g.setColor(Color.gray);
            g.drawRect(6,6,4,10);
			g.drawRect(13,3,3,6);
		
		}
		else if(code.equals("subscript"))
		{	g.setColor(Color.white);
            g.fillRect(6,3,4,10);
            g.fillRect(13,10,3,6);
            g.setColor(Color.gray);
            g.drawRect(6,3,4,10);
			g.drawRect(13,10,3,6);
		
		}
		else if(code.equals("kwadraat"))
		{	g.setColor(Color.white);
            g.fillRect(6,6,4,10);
            g.setColor(Color.gray);
            g.drawRect(6,6,4,10);
            g.setColor(Color.black);
            g.setFont(new Font("SansSerif",Font.PLAIN, 8));
			g.drawString("2",13,8);
		}
		else if(code.equals("breuk"))
		{	g.setColor(Color.white);
            g.fillRect(8,3,4,5);
            g.fillRect(8,12,4,5);
            g.setColor(Color.gray);
            g.drawRect(8,3,4,5);
            g.drawRect(8,12,4,5);
            g.setColor(Color.black);
			g.drawLine(7,10,13,10);
			
		}
		else if(code.equals("bin"))
		{	
			int hoogte = 20;
			int breedte = 20;
			 h =20;
			int hh = h/2;
			 b = h/6;
			int bb = b/2;
			
			int c = 3;
			int d = 2;
			
			g.drawLine(c+b, d, c+b-bb, d+bb);
			g.drawLine(c+b-bb, d+bb, c, d+hh-b);
			g.drawLine(c, d+hh-b, c, hoogte-hh+b-d);		
			g.drawLine(c+b-bb, hoogte-bb-d, c, hoogte-hh+b-d);
			g.drawLine(c+b, hoogte-d, c+b-bb, hoogte-bb-d);
			
			g.drawLine(breedte-b-1-c, d, breedte-b+bb-1-c, d+bb);
			g.drawLine(breedte-b+bb-1-c, d+bb, breedte-1-c, d+hh-b);
			g.drawLine(breedte-1-c, d+hh-b, breedte-1-c, hoogte-hh+b-d);		
			g.drawLine(breedte-b+bb-1-c, hoogte-bb-d, breedte-1-c, hoogte-hh+b-d);
			g.drawLine(breedte-b-1-c, hoogte-d, breedte-b+bb-1-c, hoogte-bb-d);
			g.setColor(Color.white);
            g.fillRect(8,3,4,5);
            g.fillRect(8,12,4,5);
            g.setColor(Color.gray);
            g.drawRect(8,3,4,5);
            g.drawRect(8,12,4,5);
            g.setColor(Color.black);
			
			
		}
		else if(code.equals("haakjes"))
		{	g.drawString("(",3,15);
			g.drawString(")",13,15);
            g.setColor(Color.white);
            g.fillRect(8,5,4,10);
            g.setColor(Color.gray);
            g.drawRect(8,5,4,10);
            
		}
		if(code.equals("ndewortel"))
		{	g.drawLine(3,2*h/4+3,h/4,h-3);
			g.drawLine(4,2*h/4+3,h/4+1,h-3);
			g.drawLine(h/4+1,h-3,h/2,3);
			g.drawLine(h/2,3,b-3,3);
			g.drawRect(h/2+2,6,4,10);
			g.drawRect(3,3,3,6);
		}
		if(code.equals("formule"))
		{	g.drawString("F",7,15);
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	actief = true;
		repaint();
	}
	
	public void mouseReleased(MouseEvent e) 
 	{	actief = false;
		if ( isEnabled() )
 		{	if (actionListener != null)
 			{	actionListener.actionPerformed( new ActionEvent(this, 0, "knop") );
 			}
 		}
		repaint();
 	}  
	
	public void mouseEntered(MouseEvent e)
	{	focus = true;
		setCursor(new Cursor(Cursor.HAND_CURSOR ));
		repaint();
	}
	public void mouseExited(MouseEvent e)
	{	focus = false;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
		repaint();
	}
	public void mouseClicked(MouseEvent e){;}
	
}