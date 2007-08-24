package fi.javalogoweb;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import logotekenap.Tekenblad;
import fi.javalogoweb.expressies.BasisExpressie;
import fi.javalogoweb.schuifobjects.SchuifVeld;

public class DeeltaakCComponent  extends CommandComponent implements ActionListener
{
	private ProgrammaComponent deeltaakContainer;
	private DeeltaakCComponent parent;
	
	public DeeltaakCComponent(int x, int y, int b, int h, SchuifVeld sv)
	{	super(x,y,b,h,sv);
		commandString = "deeltaak1";
		kommaString = "";
		haakjeString = "";
		
		gc1 = new GetalComponent(locationGc1,2,fm.stringWidth("0"),21);
		gc1.setFont(new Font("SansSerif", Font.BOLD, 12));
		gc1.zetInstelbaar(true);
		gc1.zetTekst("0");
		gc1.addActionListener(this);
		//add(gc1,0);
		
		gc2 = new GetalComponent(locationGc2,2,fm.stringWidth("0"),21);
		gc2.zetInstelbaar(true);
		gc2.zetWaarde(0);
		gc2.addActionListener(this);
		//add(gc2,0);
		setDtcParent(this);
				
		zetMaat();
	}
	
	public void setDtcParent(DeeltaakCComponent dtc)
	{	parent = dtc;
	}
	
	public DeeltaakCComponent getDtcParent()
	{	return parent;
	}
	
	public ProgrammaComponent geefProgrammaComponent()
	{	return deeltaakContainer;
	}
	
	public void zetDeeltaakContainer(ProgrammaComponent pc)
	{	deeltaakContainer = pc;
	}
	
	public void addDeeltaakContainer()
	{	((JavaLogoSchuifVeld)schuifveld).add(deeltaakContainer,0);
		
	}
	
	public void removeDeeltaakContainer()
	{	((JavaLogoSchuifVeld)schuifveld).remove(deeltaakContainer);
		
	}
	
	public void setCommandName(String s)
	{	commandString = s;	
	}
	
	public String getCommandName()
	{	return commandString ;	
	}
	
	public void paint(Graphics g)
	{	g.setColor(new Color(240,240,240));
		if(traceKleur)g.setColor(traceActiveColor);
		g.fillRect(0,0,getSize().width-1,getSize().height-1);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
		g.drawRect(1,1,getSize().width-3,getSize().height-3);
		g.drawRect(5,1,getSize().width-11,getSize().height-1);
		g.drawRect(6,1,getSize().width-13,getSize().height-3);
		if(caretUp)g.drawLine(2,2,getSize().width-3,2);
		if(caretDown)g.drawLine(2,getSize().height-3,getSize().width-3,getSize().height-3);
		if(label!=null)g.drawString(label,20,18);
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		super.paint(g);
	}
	
	public boolean teken(Tekenblad tb, VarSet varSet)
	{	traceKleur  = deeltaakContainer.teken(tb, varSet);
		return traceKleur;
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		schuifveld.tekenOpnieuw();
	}
}