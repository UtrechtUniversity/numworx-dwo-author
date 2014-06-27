package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

import fi.wiskopdr.WiskOpdr;


public class FormuleTeken extends FormuleElement
{	
	private FontMetrics fm;
	
	private String teken;
	private char character;
	private boolean selected = false;
	private boolean functieTeken = false;
	private static boolean maalteken=false;
	
	public FormuleTeken(char tk)
	{	character = tk;
		if(tk=='+' || tk=='=' || tk=='<' || tk=='>' || tk=='\u2264' || tk=='\u2265' || tk=='\u2248')
		{	teken = " "+tk+" ";
		}
		else if(tk=='*')
		{	teken = null;
		}
		else if(tk=='\u00d7')
		{	teken = null;
		}
		else if(tk=='-')
		{	teken = null;
		}
		else if(tk==':')
		{	teken = null;
		}
		/*else if(tk=='2')
		{	teken = null;
		}*/
		else if(tk=='z')
		{	teken = null;
		}
		else if(tk=='y')
		{	teken = null;
		}
		else if(tk=='\u3008')
		{	teken = null;
		}
		else if(tk=='\u3009')
		{	teken = null;
		}
		else if(tk=='[')
		{	teken = null;
		}
		else if(tk==']')
		{	teken = null;
		}
		else if(tk=='\u2220')
		{	teken = null;
		}
		else teken = ""+tk;
		
		/*if(Character.isLetter(tk))
		{	setFont(new Font(defaultFont.getName(),Font.ITALIC,defaultFont.getSize()));
		}
		else
		{	setFont(defaultFont);
		}
		fm = getFontMetrics(getFont());
		
		if(teken!=null)
		{	setSize(fm.stringWidth(teken),fm.getAscent()+fm.getDescent());
		}
		else if(character=='*')
		{	setSize(fm.getAscent()/3,fm.getAscent()+fm.getDescent());
		}
		ashoogte = fm.getAscent()/2;*/
		selected = false;
		setOpaque(false);
	}
	
	public static void zetMaalTeken(boolean b)
	{	maalteken = b;
	}
	
	public int getCorrItalic()
	{
		
		//System.out.println("" + fm.getStringBounds(teken,getGraphics()).getWidth());
		//System.out.println("" + fm.stringWidth(teken));
		return (int)Math.round((fm.getStringBounds(teken,getGraphics()).getWidth() - fm.stringWidth(teken)));
	}
	public void setFont(Font f)
	{	if(Character.isLetter(character))
		{	boolean bold = f.getStyle()==Font.BOLD;
			if(functieTeken)
			{	if(bold)super.setFont(new Font(f.getName(),Font.BOLD,f.getSize()));
				else super.setFont(new Font(f.getName(),Font.PLAIN,f.getSize()));
			}
			else 
			{	if(bold) super.setFont(new Font(f.getName(),Font.ITALIC+Font.BOLD,f.getSize()));
				else  super.setFont(new Font(f.getName(),Font.ITALIC,f.getSize()));
			}
		}
		else
		{	super.setFont(f);
		}
		fm = getFontMetrics(getFont());
		if(teken!=null)
		{	setSize(fm.stringWidth(teken),fm.getAscent()+fm.getDescent());
		}
		//else if(character=='*')
		//{	setSize(fm.getAscent()/2,fm.getAscent()+fm.getDescent());
		//}
		else if(character=='*' || character=='\u00d7')
		{	if(maalteken)
			{	setSize(fm.getAscent()/2+7,fm.getAscent()+fm.getDescent()); // aanpassing maalteken
			}
			else
			{	setSize(fm.getAscent()/2,fm.getAscent()+fm.getDescent());
			}

			//setSize(fm.getAscent()/2,fm.getAscent()+fm.getDescent());
		}
		else if(character=='-')
		{	setSize(fm.getAscent(),fm.getAscent()+fm.getDescent());
		}
		else if(character==':')
		{	setSize(fm.getAscent(),fm.getAscent()+fm.getDescent());
		}
		/*else if(character=='2')
		{	setSize(fm.stringWidth("2"),fm.getAscent()+fm.getDescent());
		}*/
		else if(character=='z')
		{	setSize(fm.stringWidth("2"),fm.getAscent()+fm.getDescent());
		}
		else if(character=='y')
		{	setSize(fm.stringWidth("y"),fm.getAscent()+fm.getDescent());
		}
		else if(character=='\u3008' || character=='\u3009' || character=='[' || character==']')
		{	setSize(fm.getAscent()/2,fm.getAscent()+fm.getDescent());
		}
		else if(character=='\u2220')
		{	setSize(fm.getAscent(),fm.getAscent()+fm.getDescent());
		}
		ashoogte = fm.getAscent()/2;
		
	}
	
	public void paintComponent(Graphics g)
	{	
		
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
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
	
	public void paint(Graphics g, int x, int y)
	{
		paint(g, x, y, Color.black);
	}
	
	public void paint(Graphics g, int x, int y, Color fgColor)
	{	
		if(selected)g.setColor(Color.white);
		else g.setColor(fgColor);
		
		g.setFont(getFont());
		if(teken!=null)g.drawString(teken,x,y+fm.getAscent());
		else if(character=='*' || character=='\u00d7')
		{	if(maalteken)
			{	g.drawLine(x+fm.getAscent()/4+1,y+5*fm.getAscent()/8-1,x+fm.getAscent()/4+6,y+5*fm.getAscent()/8+4);
				g.drawLine(x+fm.getAscent()/4+1,y+5*fm.getAscent()/8+4,x+fm.getAscent()/4+6,y+5*fm.getAscent()/8-1);
			}
			else
			{	g.drawLine(x+fm.getAscent()/4,y+5*fm.getAscent()/8,x+fm.getAscent()/4+1,y+5*fm.getAscent()/8);
				g.drawLine(x+fm.getAscent()/4,y+5*fm.getAscent()/8+1,x+fm.getAscent()/4+1,y+5*fm.getAscent()/8+1);
			}

			
			//g.drawLine(x+fm.getAscent()/4,y+5*fm.getAscent()/8,x+fm.getAscent()/4+1,y+5*fm.getAscent()/8);
			//g.drawLine(x+fm.getAscent()/4,y+5*fm.getAscent()/8+1,x+fm.getAscent()/4+1,y+5*fm.getAscent()/8+1);
		}
		
		else if(character=='-')
		{	g.drawLine(x+fm.getAscent()/4,y+5*fm.getAscent()/8,x+fm.getAscent()/4+fm.getAscent()/2,y+5*fm.getAscent()/8);
			if(getFont().isBold())g.drawLine(x+fm.getAscent()/4,y+5*fm.getAscent()/8+1,x+fm.getAscent()/4+fm.getAscent()/2,y+5*fm.getAscent()/8+1);
		}
		else if(character==':')
		{	g.drawLine(x+fm.getAscent()/2,y+5*fm.getAscent()/8-2,x+fm.getAscent()/2+1,y+5*fm.getAscent()/8-2);
			g.drawLine(x+fm.getAscent()/2,y+5*fm.getAscent()/8-1,x+fm.getAscent()/2+1,y+5*fm.getAscent()/8-1);
			g.drawLine(x+fm.getAscent()/2,y+5*fm.getAscent()/8+4,x+fm.getAscent()/2+1,y+5*fm.getAscent()/8+4);
			g.drawLine(x+fm.getAscent()/2,y+5*fm.getAscent()/8+5,x+fm.getAscent()/2+1,y+5*fm.getAscent()/8+5);
			
		}
		/*else if(character=='2')
		{	g.drawString("2", x,y+fm.getAscent());
			g.drawLine(x+1,y+fm.getAscent()-1,x+1+fm.getAscent()/5,y+fm.getAscent()-2-fm.getAscent()/5);
		}*/
		else if(character=='z')
		{	g.drawString("z", x,y+fm.getAscent());
			//boolean b = getFont().getSize()==12 && (getFont().getName().equals("SansSerif") || getFont().getName().equals("Arial"));
			//g.drawLine(x-1,y+fm.getAscent()-1,x+fm.getAscent()/3,y+fm.getAscent()-2-fm.getAscent()/3);
		}
		else if(character=='y')
		{	g.drawString("y", x,y+fm.getAscent());
			boolean b = getFont().getSize()==12 && (getFont().getName().equals("SansSerif") || getFont().getName().equals("Arial"));
			if(b)g.drawLine(x+fm.getAscent()/3-1,y+fm.getAscent()-2,x+1,y+fm.getAscent()-2-fm.getAscent()/3);
		}
		else if(character=='\u3008')
		{	g.drawLine(x+3*fm.getAscent()/8,y,x+fm.getAscent()/8,y+(fm.getAscent()/2+fm.getDescent()/2));
			g.drawLine(x+3*fm.getAscent()/8,y+fm.getAscent()+fm.getDescent(),x+fm.getAscent()/8,y+(fm.getAscent()/2+fm.getDescent()/2));
		}
		else if(character=='\u3009')
		{	g.drawLine(x+fm.getAscent()/8,y,x+3*fm.getAscent()/8,y+(fm.getAscent()/2+fm.getDescent()/2));
			g.drawLine(x+fm.getAscent()/8,y+fm.getAscent()+fm.getDescent(),x+3*fm.getAscent()/8,y+(fm.getAscent()/2+fm.getDescent()/2));
		}
		else if(character=='[')
		{	g.drawLine(x+3*fm.getAscent()/8,y,x+fm.getAscent()/8,y);
			g.drawLine(x+3*fm.getAscent()/8,y+fm.getAscent()+fm.getDescent()-1,x+fm.getAscent()/8,y+fm.getAscent()+fm.getDescent()-1);
			g.drawLine(x+fm.getAscent()/8,y,x+fm.getAscent()/8,y+fm.getAscent()+fm.getDescent()-1);
		}
		else if(character==']')
		{	g.drawLine(x+3*fm.getAscent()/8,y,x+fm.getAscent()/8,y);
			g.drawLine(x+3*fm.getAscent()/8,y+fm.getAscent()+fm.getDescent()-1,x+fm.getAscent()/8,y+fm.getAscent()+fm.getDescent()-1);
			g.drawLine(x+3*fm.getAscent()/8,y,x+3*fm.getAscent()/8,y+fm.getAscent()+fm.getDescent()-1);
		}
		else if(character=='\u2220')
		{	g.drawLine(x+fm.getAscent()/4,y+fm.getAscent()-1,x+fm.getAscent()/4+fm.getAscent()/2,y+fm.getAscent()-1);
			g.drawLine(x+fm.getAscent()/4,y+fm.getAscent()-1,x+fm.getAscent()/4+fm.getAscent()/2,y+4*fm.getAscent()/8);
		}
	}
	
	public char geefChar()
	{	return character;
	}
	
	public void zetFunctieTeken(boolean b)
	{	functieTeken = b;
		super.setFont(new Font(getFont().getName(),Font.PLAIN,getFont().getSize()));
		fm = getFontMetrics(getFont());
	}
	
	public void setSelected(boolean b)
	{	if(selected!=b)
		{	selected = b;
			repaint();
		}
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public String toString()
	{	return "" + character;
	}
}

