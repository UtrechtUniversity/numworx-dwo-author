package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import fi.wiskopdr.WiskOpdr;

public class KlaarKnop extends JButton implements MouseListener	
{	
	public static final String IMAGE = "I";
	
	private Image im;
	private Graphics gIm;
	
	protected String code;
	private Font defaultfont = new Font("SansSerif", Font.PLAIN, 13);
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
	protected boolean focusable = true;
	
	private static String[] imageNames = 
	{	
		"formuleknop.gif",
		"linkknop.gif",
		"imageknop.gif",
		"grafiekknop.gif",
		"appletknop.gif",	
		"rmknop.gif",
	};
	
	private static Hashtable images;
	
	
	protected ActionListener actionListener = null;

		
	public KlaarKnop(String s)
	{	code = s;
		addMouseListener(this);
		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);
		UIManager.put("ToolTip.background", new ColorUIResource(new Color(255,255,230)));
		//setToolTipText(s);
		setBorder(null);
		if(images==null)
		{	images = new Hashtable();
			WiskOpdr.loadImages(images,imageNames);
		}
		setOpaque(false);
	}
	
	public static Image getImage(String name)
	{	return(Image)images.get(name);
	}
	
	public void setFocusable(boolean b)
	{	focusable = b;
	}
	
	public String getCode()
	{	return code;
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
	{	bgColor = c;
		//bgUseColor = c;
	}
	
	/*public void paint(Graphics g)
	{	{ 	if(im==null)
			{	im = createImage(getSize().width,getSize().height);
  				gIm = im.getGraphics();
			}
			gIm.setColor(getBackground());
			gIm.fillRect(0,0,getSize().width,getSize().height);
			paintBuffer(gIm);
			g.drawImage(im, 0, 0, null);
  		}
	}
	
	public void update(Graphics g)
	{	paint(g);
	}*/
	
	public void paintComponent(Graphics g)
	{	
		g = (Graphics2D)g;
	    ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    super.paintComponent(g);   
	    g.setColor(new Color(0,153,255));
	    g.fillRoundRect(0,0,getSize().width, getSize().height,40,40);
	    g.setColor(new Color(245,250,255));
	    g.fillRoundRect(6,6,getSize().width-12, getSize().height-12,30,30);
	    g.setColor(Color.gray);
	    g.drawRoundRect(0,0,getSize().width-1, getSize().height-1,40,40);
	    g.drawRoundRect(6,6,getSize().width-1-12, getSize().height-1-12,30,30);
	    
	    g.setColor(Color.black);
	    g.setFont(WiskOpdr.tekstFont);
	    FontMetrics fm = g.getFontMetrics();
	    int woordlengte = fm.stringWidth(code);
	    int x = getWidth()/2 - woordlengte/2;
	    int y = getHeight()/2 + fm.getAscent()/2;
	    g.drawString(code, x, y);
	}
	
	public void mousePressed(MouseEvent e)
	{	actief = true;
		//if(focusable)
			repaint();
	}
	
	public void mouseReleased(MouseEvent e) 
 	{	actief = false;
		if ( isEnabled() )
 		{	if (actionListener != null)
 			{	actionListener.actionPerformed( new ActionEvent(this, 0, "knop") );
 			}
 		}
		//if(focusable)
		repaint();
 	}  
	
	public void mouseEntered(MouseEvent e)
	{	focus = true;
		setCursor(new Cursor(Cursor.HAND_CURSOR ));
		if(focusable)repaint();
	}
	public void mouseExited(MouseEvent e)
	{	focus = false;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
		if(focusable)repaint();
	}
	public void mouseClicked(MouseEvent e){;}
	
}