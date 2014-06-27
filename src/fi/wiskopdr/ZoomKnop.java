package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

import fi.wiskopdr.formuleobjects.FormuleButton;

public class ZoomKnop extends FormuleButton implements MouseListener	
{		private Image im;
	private Graphics gIm;		protected String code;
	private Font defaultfont = new Font("SansSerif", Font.PLAIN, 16);	private FontMetrics fm;
	protected Color bgColor = new Color(200,200,200);		
	protected Color fgColor = Color.black;	protected boolean focus = false;
	protected boolean actief = false;	
	private static String[] imageNames = 
	{	"zoominknop.gif",
		"zoomuitknop.gif",
		"zoominxknop.gif",
		"zoomuitxknop.gif",
		"zoominyknop.gif",
		"zoomuityknop.gif",
	};
	
	private static Hashtable images;
				public ZoomKnop(String s)
	{	code = s;
		addMouseListener(this);		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);
		setBorder(null);
		if(images==null)
		{	images = new Hashtable();
			WiskOpdr.loadImages(images,imageNames);
		}	}
	
	public static Image getImage(String name)
	{	return(Image)images.get(name);
	}
		
	public void zetActief(boolean b)
	{	actief = b;
		repaint();
	}			public void setBackground(Color c)
	{	//bgColor = c;
		//bgUseColor = c;	}
	
	public void paintComponent(Graphics g)
	{	g.setColor(fgColor);
		{	g.setColor(bgColor);
			int red = bgColor.getRed();
			int green = bgColor.getGreen();
			int blue = bgColor.getBlue();
			for(int i=0 ; i<10 ; i++)
			{
				g.setColor(new Color(red + i*(245-red)/10,green + i*(245-green)/10,blue + i*(245-blue)/10));
				g.fillRect(0,getHeight()-(i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
			}
			if(!focusable || focus)
			{	if(actief)g.setColor(bgColor.darker());
				else g.setColor(bgColor.brighter());
				g.drawLine(0,0,getSize().width-1,0);
				g.drawLine(0,0,0,getSize().height-1);
				if(actief)g.setColor(bgColor.brighter());
				else g.setColor(bgColor.darker());
				g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
				g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
			}
		}		int b = getSize().width;
		int h = getSize().height;		g.setColor(Color.black);
		if(code.equals("zoominx"))		{	g.drawImage(getImage("zoominxknop.gif"),1,1,null);
		}		else if(code.equals("zoomuitx"))		{	g.drawImage(getImage("zoomuitxknop.gif"),1,1,null);
		}		else if(code.equals("zoominy"))		{	g.drawImage(getImage("zoominyknop.gif"),1,1,null);
		}		else if(code.equals("zoomuity"))		{	g.drawImage(getImage("zoomuityknop.gif"),1,1,null);
		}		else if(code.equals("zoomin"))		{	g.drawImage(getImage("zoominknop.gif"),1,1,null);
		}		else if(code.equals("zoomuit"))		{	g.drawImage(getImage("zoomuitknop.gif"),1,1,null);
		}		else if(code.equals("standaard"))		{	g.drawLine(4,10,16,10);
			g.drawLine(10,4,10,16);
		}
	}
	
	public void mousePressed(MouseEvent e)	{	actief = true;		repaint();
	}	
	public void mouseReleased(MouseEvent e) 
 	{	actief = false;		if ( isEnabled() )
 		{	produceAction("knop");
 		}		repaint();
 	}  
	
	public void mouseEntered(MouseEvent e)	{	focus = true;
		setCursor(new Cursor(Cursor.HAND_CURSOR ));
		repaint();
		produceAction("focus");	}
	
	public void mouseExited(MouseEvent e)
	{	focus = false;		setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
		getParent().repaint();
	}
	
	public void mouseClicked(MouseEvent e){;}
	
	//	ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//
}