package fi.wiskopdr.tekstobjects;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.util.Locale;

import javax.swing.*;

import fi.wiskopdr.WiskOpdr;

public class TekstTeken extends JComponent implements TekstElement
{
	private int ashoogte;
	private TekstVak tekstVak;
		
	private FontMetrics fm;
	
	private String teken;
	private char character;
	private boolean selected = false;
	boolean underlined = false;
	
	public TekstTeken(char tk)
	{	character = tk;
		
		//if(tk=='*')
		//{	teken = null;
		//}
		//else 
			if(tk=='\n')
		{	teken = "";
		}
		else teken = ""+tk;
		
		selected = false;
		
		//setOpaque(false);
	}
	
	public void setChar(char tk)
	{	character = tk;
		teken = ""+tk;
	}
	
	public void setFont(Font f)
	{	
		{	super.setFont(f);
		}
		fm = getFontMetrics(getFont());
		if(teken!=null)
		{	setSize(fm.stringWidth(teken),fm.getAscent()+fm.getDescent());
		}
		//else if(character=='*')
		//{	setSize(fm.getAscent()/3,fm.getAscent()+fm.getDescent());
		//}
		ashoogte = fm.getAscent();///2;
		
	}
	
	public void paintComponent(Graphics g)
	{	if(selected)
		{	g.setColor(new Color(0,0,0,128));
			g.fillRect(0,0,getSize().width,getSize().height);
		}
	
	}
	
	public void setLocation(int x, int y)
	{
	    /*boolean fa = WiskOpdr.language.equals(new Locale("fa"));
	    if(fa)
	    {
	        Container parent = getParent();
	        int xNew = parent.getWidth()-x-getWidth();
	        super.setLocation(xNew,y);
	    }
	    else*/
	    {
	        super.setLocation(x,y);
	    }
	}
	static RenderingHints.Key KEY_TEXT_LCD_CONTRAST;
	static Object VALUE_TEXT_ANTIALIAS_LCD_HRGB;
	static {
		// if 1.6
		//KEY_TEXT_LDC_CONTRAST = RenderingHints.KEY_TEXT_LCD_CONTRAST;
		//VALUE_TEXT_ANTIALIAS_LCD_HRGB = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
		try {
			KEY_TEXT_LCD_CONTRAST = (Key) RenderingHints.class.getField("KEY_TEXT_LCD_CONTRAST").get(null);
			VALUE_TEXT_ANTIALIAS_LCD_HRGB = RenderingHints.class.getField("VALUE_TEXT_ANTIALIAS_LCD_HRGB").get(null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// Ignore if <=1.5
		}
	}
	
	public void setUnderlined(boolean b)
	{
		underlined = b;
	}
	
	public void paint(Graphics gr, int x, int y)
	{	
		Graphics g ;
	    //if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
	    {   g = (Graphics2D)gr;
	    	if(System.getProperty("java.specification.version").equals("1.6") || System.getProperty("java.specification.version").equals("1.7"))
			{	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		        ((Graphics2D)g).setRenderingHint(KEY_TEXT_LCD_CONTRAST, new Integer(100));
		    }
	    	else
	    	{	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    	}
	     }
	   // else 
	    //g=gr;
		if(selected)g.setColor(Color.white);
		else g.setColor(getForeground());
		g.setFont(getFont());
		if(teken!=null)g.drawString(teken,x,y+fm.getAscent());
		if(underlined)g.drawLine(x,y+fm.getAscent()+1,x+getWidth(),y+fm.getAscent()+1);
		//else if(character=='*')
		//{	g.drawLine(x+fm.getAscent()/6,y+5*fm.getAscent()/8,x+fm.getAscent()/6,y+5*fm.getAscent()/8);
		//}
	}
	
	public char geefChar()
	{	return character;
	}
	
	//public void zetFunctieTeken(boolean b)
	//{	functieTeken = b;
	//	super.setFont(new Font(getFont().getName(),Font.PLAIN,getFont().getSize()));
	//	fm = getFontMetrics(getFont());
	//}
	
	public void setSelected(boolean b)
	{	if(selected!=b)
		{	selected = b;
			repaint();
		}
	}
	
	public boolean isSpatie()
	{	return character==' ';
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public String toString()
	{	return "" + character;
	}
	
	// implementation TekstElement
 	public Component add( Component comp ) 
	{	
		return comp;
	}
	public Component add( Component comp, int index ) 
	{	
		return comp;
	}
	
	public void zetMaat()
  	{
	}
	
	public void setEditable(boolean b)
	{	
	}
	
	public void setSelectable(boolean b)
	{	
	}
	public void neemFocus(String richting, TekstElement fe)
	{
	}
	public void neemFocus(String richting)
	{
	}
	
	public TekstVak getTekstVak()
	{
		return tekstVak;
	}
	
	public int getAsHoogte()
	{
		return ashoogte;
	}
//
}
