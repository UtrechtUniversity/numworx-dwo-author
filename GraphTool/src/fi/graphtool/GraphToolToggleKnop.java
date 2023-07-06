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
import java.util.Hashtable;

import javax.swing.JToggleButton;

import fi.wiskopdr.WiskOpdr;

public class GraphToolToggleKnop extends JToggleButton implements MouseListener{

	public static final String IMAGE = "I";
	
	
	protected String code;
	private Font defaultfont = new Font("SansSerif", Font.PLAIN, 16);
	private FontMetrics fm;
	protected Color bgColor = new Color(200,200,200);		
	protected Color fgColor = Color.black;
	protected boolean focus = false;
	protected boolean actief = false;
	protected boolean focusable = true;
	
	private static String[] imageNames = 
		{	"teken_penknop.gif",
			"teken_gumknop.gif",
			"teken_cursorknop.gif",
			"teken_puntenknop.gif",
			"teken_lijnenknop.gif",
			"teken_krommeknop.gif",
			"teken_extrapoleerknop.gif"
		};
		
	private static Hashtable images;
	
	protected ActionListener actionListener = null;
		
		
	public GraphToolToggleKnop(String s)
	{
		code = s;
		addMouseListener(this);
		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);
		setBorder(null);
		if(images==null)
		{	images = new Hashtable();
			WiskOpdr.loadImages(images,imageNames);
		}
	}

	public void setFocusable(boolean b)
	{	focusable = b;
	}
	
	
	public static Image getImage(String name)
	{	return(Image)images.get(name);
	}
	
	public void zetActief(boolean b)
	{	actief = b;
		repaint();
	}
	
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
			if(!focusable || focus || actief)
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
		
	}
	
	public void mousePressed(MouseEvent e)
	{	actief = !actief;
		repaint();
	}
	
	public void mouseReleased(MouseEvent e) 
 	{	if ( isEnabled() )
 		{	produceAction("knop");
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
		getParent().repaint();
	}
	
	public void mouseClicked(MouseEvent e){;}
	
	//	ActionProducer
	
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
