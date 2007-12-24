package fi.javalogoweb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import logotekenap.Tekenblad;

import fi.javalogoweb.schuifobjects.SchuifVeld;

public class VulUitCComponent extends CommandComponent implements ActionListener
{
	private GetalComponent gc;
	
	public VulUitCComponent(int x, int y, int b, int h, SchuifVeld sv)
	{	super(x,y,b,h,sv);
		commandString = "vulUit ( ";
		kommaString = null;
		haakjeString = ") ";
		
		zetMaat();
	}
	
	public void paint(Graphics g)
	{	g.setColor(new Color(240,240,240));
		if(traceKleur)g.setColor(traceActiveColor);
		g.fillRect(0,0,getSize().width-1,getSize().height-1);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
		g.drawRect(1,1,getSize().width-3,getSize().height-3);
		if(caretUp)
		{	g.drawLine(2,2,getSize().width-3,2);
			g.drawLine(2,3,getSize().width-3,3);
		}
		if(caretDown)
		{	g.drawLine(2,getSize().height-3,getSize().width-3,getSize().height-3);
			g.drawLine(2,getSize().height-4,getSize().width-3,getSize().height-4);
		}
		if(label!=null)g.drawString(label,20,18);
		super.paint(g);
	}
	
	public boolean teken(Tekenblad tb, VarSet varSet)
	{	traceKleur = tb.vulUit();
		if(traceKleur)schuifveld.tekenOpnieuw();
		return traceKleur;
	}
	
	public String getCode(String tab)
	{	String s = tab + "vulUit()"  + "\n";
		return s;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		schuifveld.tekenOpnieuw();
	}
}