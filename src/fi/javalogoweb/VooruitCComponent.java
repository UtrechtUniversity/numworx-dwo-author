package fi.javalogoweb;

import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;

import logotekenap.Tekenblad;

import fi.javalogoweb.schuifobjects.SchuifVeld;

public class VooruitCComponent extends CommandComponent implements ActionListener
{
	
	private CommandInvulVak commandInvulVak;
	private boolean editing;
	private int startx, starty, dx, dy;
	private MouseEvent me;
	
	
	
	
	public VooruitCComponent(int x, int y, int b, int h, SchuifVeld sv)
	{	super(x,y,b,h,sv);
		
		commandString = "vooruit (";
		kommaString = null;
		haakjeString = ") ";
		
		gc1 = new GetalComponent(locationGc1,2,fm.stringWidth("0"),21);
		//gc1.zetInstelbaar(true);
		gc1.zetWaarde(0);
		gc1.addActionListener(this);
		add(gc1,0);
				
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
		super.paint(g);
	}
	
	public boolean teken(Tekenblad tb, VarSet varSet)
	{	double value = gc1.geefWaarde();
		if(Double.isNaN(value))value = varSet.getExpressionValue(gc1.geefExpressie());
		if(Double.isNaN(value))return false;
		traceKleur = tb.vooruit(value);
		if(traceKleur)schuifveld.tekenOpnieuw();
		return traceKleur;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		//schuifveld.tekenOpnieuw();
	}
	
	/*public void mousePressed(MouseEvent e)
	{	requestFocus();
		startx=e.getX()+getLocation().x;
		starty=e.getY()+getLocation().y;
		dx = 0;
		dy = 0;
		me = e;
		editing = true;
		
	}
	
	public void mouseDragged(MouseEvent e)
	{	dx = e.getX()+getLocation().x-startx;
		dy = e.getY()+getLocation().y-starty;
		System.out.println("dx = "+dx);
		System.out.println("dy = "+dx);
		if(dx*dx+dy*dy>=20) 
		{	
			super.mousePressed(me);
			super.mouseDragged(e);
			editing = false;
		}
		else editing = true;
		
	}
	
	public void mouseReleased(MouseEvent e)
	{	if(editing) 
		{	gc1.vulIn();
			editing = false;
		}
		else
		{ 	super.mouseReleased(e);
		}

	}*/
	
}
