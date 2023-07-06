package fi.spot_problems_dwo.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import fi.spot_problems_dwo.wiskopdr.formuleobjects.*;
import fi.spot_problems_dwo.wiskopdr.expressies.*;

public class PijlVak extends FormuleElement
{	
	private Font font = new Font("Serif",Font.PLAIN,16);
	private FontMetrics fm;
	private String operator;
	private ActionListener actionListener = null;

	FormuleVak formuleVak;
	
	public PijlVak(String op)
	{	setLayout(null);
		operator = op;
		
		super.setFont(font);
		fm = getFontMetrics(getFont());
					
		setSize((fm.getAscent() + fm.getDescent())/2 + fm.getAscent()/4 + fm.stringWidth("  "+operator+"   "),5*(fm.getAscent() + fm.getDescent())/2);
		ashoogte = 5*(fm.getAscent() + fm.getDescent())/4;
		
		formuleVak = new FormuleVak();
		formuleVak.setLocation((fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+" "),(fm.getAscent() + fm.getDescent())/2);
		if(!op.equals("haakjes") && !op.equals("herleid") && !op.equals("gelijkwaardig") && !op.equals("ontbind") && !op.equals("splits") && !op.equals("wortel"))
			add(formuleVak);
		
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		formuleVak.setFont(f);
	}
	
	public void paint(Graphics g)
	{	zetMaat();
		g.setColor(Color.black);
		g.setFont(getFont());
		int h = ashoogte;
		g.drawArc(-3*h/2,0,2*h,2*h,(int)(180*Math.PI/4),(int)(-180*Math.PI/2));
		g.drawLine(0,getSize().height-4,2,getSize().height-12);
		g.drawLine(0,getSize().height-4,7,getSize().height-4);
		if(operator.equals("*"))
		{	g.drawLine((fm.getAscent() + fm.getDescent())/2+10,ashoogte-fm.getAscent()/4,(7*fm.getAscent()/4 + fm.getDescent())/2+10,ashoogte+fm.getAscent()/4);
			g.drawLine((fm.getAscent() + fm.getDescent())/2+10,ashoogte+fm.getAscent()/4,(7*fm.getAscent()/4 + fm.getDescent())/2+10,ashoogte-fm.getAscent()/4);
		
		}
		else if(operator.equals("haakjes"))
		{	g.drawString(WiskOpdr.rb.getString("haakjesLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent());
			g.drawString(WiskOpdr.rb.getString("haakjesLabel1"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("herleid"))
		{	g.drawString(WiskOpdr.rb.getString("herleidLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent()+10);
			g.drawString(WiskOpdr.rb.getString("herleidLabel1"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("ontbind"))
		{	g.drawString(WiskOpdr.rb.getString("ontbindLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent()+10);
			g.drawString("",(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("splits"))
		{	g.drawString(WiskOpdr.rb.getString("splitsLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent()+10);
			g.drawString("",(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("wortel"))
		{	g.drawString(WiskOpdr.rb.getString("wortelLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent()+10);
			g.drawString("",(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("gelijkwaardig"))
		{	g.drawString(WiskOpdr.rb.getString("gelijkwaardigLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent());
			g.drawString(WiskOpdr.rb.getString("gelijkwaardigLabel1"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else
		{	g.drawString("  "+operator+" ",(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent()/2);
		}
		super.paint(g);
	}
	
	public void zetExpressie(String s)
	{	formuleVak.vulVak("$f" + s + "@");
	}
	
	public void zetMaat()
	{	int b = (fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+"   ") + formuleVak.getSize().width;
		setSize(b, getSize().height);
		formuleVak.setLocation((fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+" "),ashoogte-formuleVak.ashoogte-fm.getDescent()/2);
	}
	
	public String geefExpressieString()
	{	Expressie e = formuleVak.geefExpressie();
		if(e==null)return null;
		String s = e.toStringStrikt();
		return s;
	}
	
	public String geefOperator()
	{	return operator;
	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
	
}

