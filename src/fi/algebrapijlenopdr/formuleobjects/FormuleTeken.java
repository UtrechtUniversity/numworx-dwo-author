package fi.algebrapijlenopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class FormuleTeken extends FormuleElement
{	
	private FontMetrics fm;
	
	private String teken;
	private char character;
	private boolean selected = false;
	private boolean functieTeken = false;
	
	public FormuleTeken(char tk)
	{	character = tk;
		if(tk=='+' || tk=='=')
		{	teken = " "+tk+" ";
		}
		else if(tk=='*')
		{	teken = null;
		}
		else if(tk=='-')
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
	}
	
	public void setFont(Font f)
	{	if(Character.isLetter(character))
		{	super.setFont(new Font(f.getName(),Font.ITALIC,f.getSize()));
		}
		else
		{	super.setFont(f);
		}
		fm = getFontMetrics(getFont());
		if(teken!=null)
		{	setSize(fm.stringWidth(teken),fm.getAscent()+fm.getDescent());
		}
		else if(character=='*')
		{	setSize(fm.getAscent()/2,fm.getAscent()+fm.getDescent());
		}
		else if(character=='-')
		{	setSize(fm.getAscent(),fm.getAscent()+fm.getDescent());
		}
		ashoogte = fm.getAscent()/2;
		
	}
	
	public void paint(Graphics g)
	{	if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
	}
	
	public void paint(Graphics g, int x, int y)
	{	if(selected)g.setColor(Color.white);
		else g.setColor(Color.black);
		
		g.setFont(getFont());
		if(teken!=null)g.drawString(teken,x,y+fm.getAscent());
		else if(character=='*')
		{	g.drawLine(x+fm.getAscent()/4,y+5*fm.getAscent()/8,x+fm.getAscent()/4+1,y+5*fm.getAscent()/8);
			g.drawLine(x+fm.getAscent()/4,y+5*fm.getAscent()/8+1,x+fm.getAscent()/4+1,y+5*fm.getAscent()/8+1);
		}
		else if(character=='-')
		{	g.drawLine(x+fm.getAscent()/4,y+5*fm.getAscent()/8,x+fm.getAscent()/4+fm.getAscent()/2,y+5*fm.getAscent()/8);
			
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

