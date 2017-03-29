package fi.javalogoweb3d.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;


//import fi.beans.wnwidgets.*;

import fi.javalogoweb3d.JavaLogoWeb3d;

public class FormuleButton extends JButton implements MouseListener	
{	
	public static final String IMAGE = "I";
	
	private Image im;
	private Graphics gIm;
	
	protected String code;
	private Font defaultfont = new Font("SansSerif", Font.PLAIN, 13);
	private FontMetrics fm;
	protected Color bgColor = new Color(200,200,200);	
	protected Color fgColor = Color.black;
	protected Color bgUseColor = bgColor;
	protected Color fgUseColor = fgColor;
	private Color vlakkleur;
	private int kubusNummer;
	private Rectangle[] zijvlakken;
	protected boolean focus = false;
	protected boolean actief = false;
	protected boolean focusable = true;
	private Image popupButtonImage;

/*	
	private static String[] imageNames = 
	{	
		"formuleknop.gif",
		"linkknop.gif",
		"imageknop.gif",
		"grafiekknop.gif",
		"appletknop.gif",	
		"rmknop.gif",
		"geogebra.gif",
		"reseticon.gif",
		
		"wnformbutton.gif",
		"wnformbuttonrood.gif",
		"wnformbuttonoranje.gif"//,
		//"wngrafiekbutton.png",
		//"wngeogebrabutton.png",
		//"wntekstvakbutton.png"
	};
*/	
//	private static Hashtable images;
	
	
	protected ActionListener actionListener = null;
	
	public static int EDITORKNOP = 0;
	public static int BEWERKINGSKNOP = 1;
	public static int NAVIGATIEKNOP = 2;
	public static int TABLETKNOP = 3;
	public static int MEERKNOP = 4;
	
	private int soort = 0;

	public FormuleButton()
	{
		this("",0);
	}
	
	public FormuleButton(String s)
	{
		this(s,0);
	}
	public FormuleButton(String s, int soort)
	{	code = s;
		this.soort = soort;
		addMouseListener(this);
		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);
		UIManager.put("ToolTip.background", new ColorUIResource(new Color(255,255,230)));
		//setToolTipText(s);
		setBorder(null);
//		if(images==null)
//		{	images = new Hashtable();
//		}
		if(soort==BEWERKINGSKNOP)
			bgColor = new Color(255,150,150);
		
	}
	
	public void setPopupButtonImage(Image image)
	{
	    popupButtonImage = image;
	}
	
//	public static Image getImage(String name)
//	{	return(Image)images.get(name);
//	}
	
	public void setFocusable(boolean b)
	{	focusable = b;
	}
	
	public String getCode()
	{	return code;
	}
	
	public void setCode(String s)
	{	 code = s;
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
	
	
	public void paintComponent(Graphics g)
	{	
	    if(popupButtonImage!=null){
	        g.drawImage(popupButtonImage,0,0,getWidth(), getHeight(),getParent());
	        return;
	    }
		g.setColor(fgColor);
		{	g.setColor(bgColor);
			//g.fillRect(0,0,getSize().width,getSize().height);
			
			int red = bgColor.getRed();
			int green = bgColor.getGreen();
			int blue = bgColor.getBlue();
			for(int i=0 ; i<10 ; i++)
			{
				g.setColor(new Color(red + i*(245-red)/10,green + i*(245-green)/10,blue + i*(245-blue)/10));
				//g.setColor(new Color(200+5*i,200+5*i,200+5*i));
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
		if(soort==BEWERKINGSKNOP)
		{	if(actief)g.setColor(bgColor.darker());
			else g.setColor(bgColor.brighter());
			g.drawLine(0,0,getSize().width-1,0);
			g.drawLine(0,0,0,getSize().height-1);
			if(focus)
			{	g.drawLine(1,1,getSize().width-2,1);
				g.drawLine(1,1,1,getSize().height-2);
			}
			if(actief)g.setColor(bgColor.brighter());
			else g.setColor(bgColor.darker());
			g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
			g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
			if(focus)
			{	g.drawLine(getSize().width-2,1,getSize().width-2,getSize().height-2);
				g.drawLine(1,getSize().height-2,getSize().width-2,getSize().height-2);
			}
		}
		paintCode(g);
		
		
		
		
		
	}
	
	public void paintCode(Graphics g)
	{
		int b = getSize().width;
		int h = getSize().height;
		g.setColor(Color.black);
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
 			{	actionListener.actionPerformed(new ActionEvent(this, 0, "knop"));
System.out.println("formuleButton knop"); 			
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