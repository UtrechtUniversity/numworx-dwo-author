package fi.javalogoweb;

import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;

import logotekenap.Tekenblad;

import fi.javalogoweb.schuifobjects.SchuifVeld;

public class VooruitCComponent extends CommandComponent implements ActionListener
{
	
	private CommandInvulVak commandInvulVak;
	
	
	
	
	
	public VooruitCComponent(int x, int y, int b, int h, SchuifVeld sv)
	{	super(x,y,b,h,sv);
		
		commandString = "vooruit (";
		kommaString = null;
		haakjeString = ") ";
		
		gc1 = new GetalComponent(locationGc1,2,fm.stringWidth("0"),21);
		gc1.zetInstelbaar(true);
		gc1.zetWaarde(0);
		gc1.addActionListener(this);
		add(gc1,0);
				
		zetMaat();
	}
	
	
	
	public void paint(Graphics g)
	{	g.setFont(font);
		g.setColor(new Color(240,240,240));
		g.fillRect(0,0,getSize().width-1,getSize().height-1);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
		g.drawRect(1,1,getSize().width-3,getSize().height-3);
		if(caretUp)g.drawLine(2,2,getSize().width-3,2);
		if(caretDown)g.drawLine(2,getSize().height-3,getSize().width-3,getSize().height-3);
		
		super.paint(g);
	}
	
	public void teken(Tekenblad tb, VarSet varSet)
	{	double value = gc1.geefWaarde();
		if(Double.isNaN(value))value = varSet.getExpressionValue(gc1.geefExpressie());
		if(Double.isNaN(value))return;
		tb.naarVolgendPunt(0, -value);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		//schuifveld.tekenOpnieuw();
	}
	
	
}
