package logotekenap;

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;



public class TekenApplet extends Applet 
{
	private Regelaar rg;
	public Tekenblad tb;
	private AnimatieBeheerder ab;
	private MuisBeheerder mb;
	private TraceBeheerder trb;
	
	private boolean initializing;
	
	//-----------------------------------------------------------------------------------------
	// initalisatie
	//-----------------------------------------------------------------------------------------
	public void init()
	{	//tb = new Tekenblad(this);
		rg = new Regelaar(this);
		this.setLayout(new BorderLayout(0,0));
		initializing = true;
		initialiseer();							// wordt geimplementeerd in leerlingprogramma
		initializing = false;
		add(tb,"Center");
		add(rg,"East");
		
		if(trb!=null)							//
		{	tb.meldTraceBeheerder(trb);			//
		}										//
		if(trb!=null && ab!=null)				//
		{	trb.meldAnimatieBeheerder(ab);		//
			ab.meldTraceBeheerder(trb);			//de verschillende objecten leren elkaar kennen
		}										//
		if(mb!=null && ab!=null)				//
		{	mb.meldAnimatieBeheerder(ab);		// 
		}										//
		if(trb!=null && mb!=null)				//
		{	trb.meldMuisBeheerder(mb);			//
		}										//
	}	
	public void stop()
	{	if(animatieLopend())onderbreekAnimatie();		
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden kunnen alleen worden gebruikt in  "initialiseer()" van leerling-applet
	//-------------------------------------------------------------------------------------------
	public void maakAnimatieMogelijk()					
	{	if(initializing)						 
		{	ab = new AnimatieBeheerder(this);	
			add(ab,"North");					
		}										
	}											
	public void maakMuisActieMogelijk()
	{	if(initializing) 
		{	mb = new MuisBeheerder(this);
			tb.addMouseListener(mb);
			tb.addMouseMotionListener(mb);
		}
	}
	public void maakTraceMogelijk()
	{	if(initializing) 
		{	trb = new TraceBeheerder(tb,rg);
			add(trb,"South");
		}
	}
	//-------------------------------------------------------------------------------------------
	//deze methode wordt gebruikt in de "initialiseer()" van leerling-applet en doorgegeven aan
	//de regelaar
	//-------------------------------------------------------------------------------------------
	public void maakZichtbaar(Component com)
	{	rg.maakZichtbaar(com);
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de muishandler en  doorgegeven aan MuisBeheerder mb
	//-------------------------------------------------------------------------------------------
	public int geefSleepdx(){return mb.geefSleepdx();}
	public int geefSleepdy(){return mb.geefSleepdy();}
	public int geefDrukx(){return mb.geefDrukx();}
	public int geefDruky(){return mb.geefDruky();}
	public int geefX(){return mb.geefX();}
	public int geefY(){return mb.geefY();}

	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de animatiehandler en doorgegeven aan AnimatieBeheerder ab
	//-------------------------------------------------------------------------------------------
	public void pauze(int millisec){ab.pauze(millisec);}
	public boolean animatieLopend(){if (ab!=null)return ab.animatieLopend();else return false;}
	public void onderbreekAnimatie(){ab.onderbreekAnimatie();}
	public void beginAnimatie(){ab.beginAnimatie();}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de animatiehandler en muishandlers en doogegeven aan 
	//Tekenblad
	//-------------------------------------------------------------------------------------------
	public void tekenOpnieuw(){tb.tekenOpnieuw();}
	public void tekenErbij(){tb.tekenErbij();}
	
  	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "initialiseer" en doorgegeven aan Tekenblad tb (of 
	//Matrix2d) 
	//-------------------------------------------------------------------------------------------
	public void schaal(double s)
	{	tb.mat.schaal(s);
	}
	public void achtergrondkleur(String kl)
	{	tb.achtergrondkleur(kl);
	}
	public void achtergrondkleur(int r, int g, int b)
	{	tb.achtergrondkleur(r, g, b);
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "tekenprogramma()" en doorgegeven aan Tekenblad tb (of 
	//Matrix2d) 
	//-------------------------------------------------------------------------------------------
 	public void links(double dHoek)
	{	tb.mat.draai(dHoek);
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("links("+Integer.toString((int)Math.rint(dHoek))+")");
	}
  	public void rechts(double dHoek)
	{	tb.mat.draai(-dHoek);		
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("rechts("+Integer.toString((int)Math.rint(dHoek))+")");
	}
	public void vooruit(double dy)
	{	tb.naarVolgendPunt(0,-dy);	
		if(trb!=null)
		trb.volgendeMethode("vooruit("+Integer.toString((int)Math.rint(dy))+")");
	}
	public void stapy(double dy)
	{	tb.naarVolgendPunt(0,-dy);		
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("stapy("+Integer.toString((int)Math.rint(dy))+")");
	}
	public void stapx(double dx)
	{	tb.naarVolgendPunt(dx,0);		
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("stapx("+Integer.toString((int)Math.rint(dx))+")");
	}
	public void stap(double dx,double dy)
	{	tb.naarVolgendPunt(dx,-dy);
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("stap("+Integer.toString((int)Math.rint(dx))+","+Integer.toString((int)Math.rint(dy))+")");
	}
	public void penAan()
	{	tb.penAan();							
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("penAan()");
	}
	public void penAan(String kl)
	{	tb.penAan(kl);				
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("penAan("+kl+")");
	}
	public void penAan(int r, int g, int b)
	{	tb.penAan(r, g, b);	
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("penAan("+Integer.toString(r)+Integer.toString(g)+Integer.toString(b)+")");
	}
	public void penUit()
	{	tb.penUit();							
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("penUit()");
	}
	public void vulAan()
	{	tb.vulAan();							
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("vulAan()");
	}
	public void vulAan(String kl)
	{	tb.vulAan(kl);				
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("vulAan("+kl+")");
	}
	public void vulAan(int r, int g, int b)
	{	tb.vulAan(r, g, b);	
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("vulAan("+Integer.toString(r)+Integer.toString(g)+Integer.toString(b)+")");
	}
	public void vulUit()
	{	tb.vulUit();							
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("vulUit()");
	}
	public void schrijf(String s)
	{	tb.schrijf(s);							
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("schrijf()");
	}
	public void schrijf(String s, Font f)
	{	tb.schrijf(s,f);							
		if(trb!=null && trb.geefTraceStatus())
		trb.volgendeMethode("schrijf()");
	}
	
	public Polygon geefVlak()
	{	return tb.geefVlak();
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden geimplemeteerd in het leerlingenprogramma
	//-------------------------------------------------------------------------------------------
	public void tekenprogramma(){}
	public void initialiseer(){}
	public void animatie(){}
	public void muisSleepActie(){}
	public void muisDrukActie(){}
	public void muisLosActie(){}
	public void invoerVarActie(InvoerVariabele iv){}
	public void schuifInvoerVarActie(SchuifInvoerVariabele iv){}
}		
	












