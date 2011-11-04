package fi.javalogoweb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import logotekenap.Rekenblad;
import logotekenap.Tekenblad;

import fi.javalogoweb.schuifobjects.SchuifVeld;
import fi.javalogoweb.expressies.*;

public class VarCComponent extends CommandComponent implements ActionListener
{
	
	public VarCComponent(int x, int y, int b, int h, SchuifVeld sv)
	{	super(x,y,b,h,sv);
		commandString = "";
		kommaString = " = ";
		haakjeString = "";
		
		gc1 = new GetalComponent(locationGc1,2,fm.stringWidth("variabele"),21);
		//gc1.zetInstelbaar(true);
		gc1.zetWaarde(new BasisExpressie("variabele"));
		gc1.addActionListener(this);
		add(gc1,0);
		
		gc2 = new GetalComponent(locationGc2,2,fm.stringWidth("0"),21);
		//gc2.zetInstelbaar(true);
		gc2.zetWaarde(0);
		gc2.addActionListener(this);
		add(gc2,0);
				
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
	{	double value = gc2.geefWaarde();
		if(Double.isNaN(value))value = varSet.getExpressionValue(gc2.geefExpressie());
		if(Double.isNaN(value))return false;
	
		String s1 = gc1.geefExpressie().toString();
		String s2 = gc2.geefExpressie().toString();
		
		varSet.setVar((gc1.geefExpressie()).toString(), gc2.geefExpressie());
		traceKleur = tb.varAanpassing(s1,Expressie.format(value));
		if(traceKleur)schuifveld.tekenOpnieuw();
		return traceKleur;
	}
	
	public boolean reken(Rekenblad rb, VarSet varSet)
	{	double value = gc2.geefWaarde();
		if(Double.isNaN(value))value = varSet.getExpressionValue(gc2.geefExpressie());
		if(Double.isNaN(value))return false;
	
		String s1 = gc1.geefExpressie().toString();
		String s2 = gc2.geefExpressie().toString();
		
		varSet.setVar((gc1.geefExpressie()).toString(), gc2.geefExpressie());
		traceKleur = rb.varAanpassing(s1,Expressie.format(value));
		if(traceKleur)schuifveld.tekenOpnieuw();
		return traceKleur;
	}
	
	public String getCode(String tab)
	{	String s = tab + gc1.geefTekst() + " = " + gc2.geefTekst() +  "\n";
		return s;
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		schuifveld.tekenOpnieuw();
	}
}
