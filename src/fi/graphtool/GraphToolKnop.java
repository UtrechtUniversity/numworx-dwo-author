package fi.graphtool;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleButton;

public class GraphToolKnop extends FormuleButton implements MouseListener	{

	protected String code;
	private int type;
	private Font defaultfont = new Font("SansSerif", Font.PLAIN, 16);
	private FontMetrics fm;
	protected Color bgColor = new Color(200,200,200);		
	protected Color fgColor = Color.black;
	protected boolean focus = false;
	protected boolean actief = false;
	
	private static String[] imageNames = 
		{	"zoominknop.gif",
			"zoomuitknop.gif",
			"zoominxknop.gif",
			"zoomuitxknop.gif",
			"zoominyknop.gif",
			"zoomuityknop.gif",
			"teken_wisknop.gif",
			"reseticon.gif",
			"pijllinks.gif",
			"pijlrechts.gif"
		};
		
		private static Hashtable images;
	
	public GraphToolKnop(String s, int i)
	{
		type = i;
		code = s;
		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);
		setBorder(null);
		if(images==null)
		{	images = new Hashtable();
			WiskOpdr.loadImages(images,imageNames);
		}
	}
	
	public static Image getImage(String name)
	{	return(Image)images.get(name);
	}
	
	public void zetActief(boolean b)
	{	actief = b;
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{	{	g.setColor(bgColor);
			int red = bgColor.getRed();
			int green = bgColor.getGreen();
			int blue = bgColor.getBlue();
			if(type == 0)
				for(int i=0 ; i<10 ; i++)
				{
					g.setColor(new Color(red + i*(245-red)/10,green + i*(245-green)/10,blue + i*(245-blue)/10));
					g.fillRect(0,getHeight()-(i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
				}
			else if(type == 1)
				for(int i = 0; i < 10; i++)
				{	g.setColor(new Color(red + i*(245-red)/20, green + i*(245-green)/20, blue + i*(245-blue)/20));
					g.fillRect(0,getHeight()-(i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
				}
			else
				for(int i = 0; i < 10; i++)
				{	g.setColor(new Color((245+red)/2 + i*(245-red)/20, (245+green)/2 + i*(245-green)/20, (245+blue)/2 + i*(245-blue)/20));
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
		}
		int b = getSize().width;
		int h = getSize().height;
		g.setColor(Color.black);
		if(code.endsWith(".gif"))
		{	g.drawImage(getImage(code), 1, 1, null);
		}
		
		else if(code.equals("standaard"))
		{	g.drawLine(4,10,16,10);
			g.drawLine(10,4,10,16);
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	actief = true;
		repaint();
	}
	
	public void mouseReleased(MouseEvent e) 
 	{	actief = false;
		if ( isEnabled() )
 		{	produceAction("knopje");
 		}
		repaint();
 	}  
	
	public void mouseEntered(MouseEvent e)
	{	focus = true;
		setCursor(new Cursor(Cursor.HAND_CURSOR ));
		repaint();
		produceAction("focus");
	}
	
	public void mouseExited(MouseEvent e)
	{	focus = false;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
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
