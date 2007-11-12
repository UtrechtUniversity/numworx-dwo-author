package fi.heks;

import java.awt.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;


public class SomContainer extends ScContainer implements ActionListener
{
	private Applet eigenaar;
	private AppletUtil au;
	private GetalComponent term1,term2,uitkomst;
	private ScLabel plusMinLabel, isLabel;
	private ScContainer goedFoutContainer;
	private Tekening goedFoutTeken;
	private Som huidigeSom;
	private boolean nagekeken;
	private ActionListener actionListener;
	private int soort;
	private boolean rechtsInvullen;
	private int vaknr;
	
	public SomContainer(int x, int y, int b, int h, Applet applet, int srt, boolean rechtsinv)
	{	super(x,y,b,h);
		eigenaar = applet;
		au = new AppletUtil(eigenaar);
		soort = srt;
		rechtsInvullen = rechtsinv;
		
		int hg = h*4/5;
		
		term1 = new GetalComponent(0,0,2*hg,h);
		term1.addActionListener(this);
		add(term1);
		
		plusMinLabel = new ScLabel(2*hg,0,hg,h,"");
		add(plusMinLabel);
		
		term2 = new GetalComponent(3*hg,0,2*hg,h);
		term2.addActionListener(this);
		add(term2);
		
		isLabel = new ScLabel(5*hg,0,hg,h,"=");
		add(isLabel);
		
		uitkomst = new GetalComponent(6*hg,0,2*hg,h);
		uitkomst.addActionListener(this);
		uitkomst.zetBekend(false);
		uitkomst.zetInstelbaar(true);
		add(uitkomst);
		
		goedFoutContainer = new ScContainer(8*hg,0,h,h);
		add(goedFoutContainer);
		
		//huidigeSom = new Som(soort);
		//zetSom(huidigeSom);
	}
	
	public void zetSom(Som som)
	{	if(!rechtsInvullen)vaknr = (int)(3*Math.random());
		else vaknr=2;
		zetSom(som,vaknr);
	}
	
	public void zetSom(Som som, int vaknr)
	{	nagekeken = false;
		huidigeSom = som;
		goedFoutContainer.removeAll();
		this.vaknr = vaknr;
		if(vaknr==0)
		{	uitkomst.zetBekend(true);
			uitkomst.zetInstelbaar(false);
			uitkomst.setForeground(Color.black);
			
			term2.zetBekend(true);
			term2.zetInstelbaar(false);
			term2.setForeground(Color.black);
			
			term1.zetBekend(false);
			if(!term1.isInstelbaar())term1.zetInstelbaar(true);
			term1.setForeground(new Color(150,0,0));
			
			term2.zetWaarde(som.geefTerm2());
			uitkomst.zetWaarde(som.geefUitkomst());
			String op = som.geefOperator();
			plusMinLabel.setLabel(op);
		}
		else if(vaknr==1)
		{	uitkomst.zetBekend(true);
			uitkomst.zetInstelbaar(false);
			uitkomst.setForeground(Color.black);
			
			term1.zetBekend(true);
			term1.zetInstelbaar(false);
			term1.setForeground(Color.black);
			
			term2.zetBekend(false);
			if(!term2.isInstelbaar())term2.zetInstelbaar(true);
			term2.setForeground(new Color(150,0,0));
			
			term1.zetWaarde(som.geefTerm1());
			uitkomst.zetWaarde(som.geefUitkomst());
			String op = som.geefOperator();
			plusMinLabel.setLabel(op);
		}
		else if(vaknr==2)
		{	term1.zetBekend(true);
			term1.zetInstelbaar(false);
			term1.setForeground(Color.black);
			
			term2.zetBekend(true);
			term2.zetInstelbaar(false);
			term2.setForeground(Color.black);
			
			uitkomst.zetBekend(false);
			if(!uitkomst.isInstelbaar())uitkomst.zetInstelbaar(true);
			if(!rechtsInvullen)	uitkomst.setForeground(new Color(150,0,0));
			
			term1.zetWaarde(som.geefTerm1());
			term2.zetWaarde(som.geefTerm2());
			String op = som.geefOperator();
			plusMinLabel.setLabel(op);
			
		
		}
	}
	
	//public void vernieuw()
	//{	huidigeSom = new Som(soort);
	//	zetSom(huidigeSom);		
	//	//repaint();

	//}
	
	public void setState(Hashtable h)
	{	int vaknr = ((Integer)h.get("vaknr")).intValue();
		boolean nagekeken = ((Boolean)h.get("nagekeken")).booleanValue();
		Hashtable somState = (Hashtable)h.get("somState");
		boolean ingevuld = ((Boolean)h.get("ingevuld")).booleanValue();
		int antwoord = ((Integer)h.get("antwoord")).intValue();
		
		this.vaknr = vaknr;
		this.nagekeken = nagekeken;
		huidigeSom.setState(somState);
		zetSom(huidigeSom,vaknr);
		if(ingevuld)
		{	if(vaknr == 0)
			{	term1.zetWaarde(antwoord);
			}
			else if(vaknr == 1)
			{	term2.zetWaarde(antwoord);
			}
			else if(vaknr == 2)
			{	uitkomst.zetWaarde(antwoord);
			}
		}
		if(nagekeken)evalueer();
		
	}
	
	public Hashtable getState()
	{	int vaknr = 0;
		boolean nagekeken = false;
		Hashtable somState = null;
		boolean ingevuld = false;
		int antwoord = 0;
				
		vaknr = this.vaknr;
		nagekeken = this.nagekeken;
		if(vaknr == 0)
		{	ingevuld = term1.isBekend();
			antwoord = term1.geefWaarde();
		}
		else if(vaknr == 1)
		{	ingevuld = term2.isBekend();
			antwoord = term2.geefWaarde();
		}
		else if(vaknr == 2)
		{	ingevuld = uitkomst.isBekend();
			antwoord = uitkomst.geefWaarde();
		}
		somState = huidigeSom.getState();
		
		Hashtable h = new Hashtable();
	    h.put("vaknr", new Integer(vaknr));
	    h.put("nagekeken", new Boolean(nagekeken));
	    h.put("somState", somState);
	    h.put("ingevuld", new Boolean(ingevuld));
	    h.put("antwoord", new Integer(antwoord));
	    return h;
	}
	
	public int geefTerm1()
	{	return huidigeSom.geefTerm1();
	}
	
	public int geefTerm2()
	{	return huidigeSom.geefTerm2();
	}
	
	public int geefUitkomst()
	{	return huidigeSom.geefUitkomst();
	}
	
	public boolean evalueer()
	{	nagekeken = true;
		if((vaknr==2  &&  uitkomst.geefWaarde() == huidigeSom.geefUitkomst())	//&& term1.isBekend()
		   || (vaknr==1 &&  term2.geefWaarde() == huidigeSom.geefTerm2())		//&& term1.isBekend()
		   || (vaknr==0 &&  term1.geefWaarde() == huidigeSom.geefTerm1()))		//&& term1.isBekend()
		{	//goedkrul.setVisible(true);
			goedFoutContainer.removeAll();
			if(Heks.rb.getLocale().toString().equals("nl")) goedFoutTeken = new Tekening(0,0,(int)relh,(int)relh,au,"goedkrul.gif");
            else goedFoutTeken = new Tekening(0,0,(int)relh,(int)relh,au,"goedkrul_en.gif");
            goedFoutTeken.schaal(schaal);
			goedFoutContainer.add(goedFoutTeken,0);
			goedFoutContainer.repaint();
			return true;
		}
		else
		{	//foutkruis.setVisible(true);
			goedFoutContainer.removeAll();
			goedFoutTeken = new Tekening(0,0,(int)relh,(int)relh,au,"foutkruis.gif");
			goedFoutTeken.schaal(schaal);
			goedFoutContainer.add(goedFoutTeken,0);
			goedFoutContainer.repaint();
			return false;
		}
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("vulin") && goedFoutTeken!=null)
		{	goedFoutContainer.remove(goedFoutTeken);
			goedFoutTeken = null;
			repaint();
		}
		if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""+this));
		}
	}
	
}
