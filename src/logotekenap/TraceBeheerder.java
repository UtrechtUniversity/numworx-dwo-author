package logotekenap;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TraceBeheerder extends Panel implements ActionListener,Runnable
{
	private Button stapKnop,terugKnop,loopKnop,beginKnop,traceKnop;
	private TextField methodeVeld;
	private int maxAantalStappen,aantalStappen,aantalStappenTekening;
	private Regelaar rg;
	private Tekenblad tb;
	private AnimatieBeheerder ab;
	private MuisBeheerder mb;
	private boolean loopAan,traceAan;
	private Thread loop;
	
	public TraceBeheerder(Tekenblad tb, Regelaar rg)
	{	setLayout(null);
		setBackground(getBackground());
		beginKnop = new Button("begin");
		beginKnop.setBounds(20,5,40,20);
		beginKnop.addActionListener(this);
		add(beginKnop);
		stapKnop = new Button("stap");
		stapKnop.setBounds(70,5,40,20);
		stapKnop.addActionListener(this);
		add(stapKnop);
		terugKnop = new Button("terug");
		terugKnop.setBounds(120,5,40,20);
		terugKnop.addActionListener(this);
		add(terugKnop);
		methodeVeld = new TextField("",15);
		methodeVeld.setBounds(170,5,120,20);
		add(methodeVeld);
		loopKnop = new Button("loop");
		loopKnop.setBounds(300,5,40,20);
		loopKnop.addActionListener(this);
		//add(loopKnop);
		traceKnop = new Button("trace aanschakelen");
		traceKnop.setBounds(130,30,120,25);
		traceKnop.addActionListener(this);
		add(traceKnop);
		
		
		aantalStappen = 0;
		maxAantalStappen = 0;
		aantalStappenTekening = 1;
		this.tb = tb;
		this.rg = rg;
		loopAan = false;
		traceAan = false;
		naarBegin();
		
	}
	public void naarBegin()
	{	methodeVeld.setVisible(false);
		beginKnop.setVisible(false);
		stapKnop.setVisible(false);
		loopKnop.setVisible(false);
		terugKnop.setVisible(false);
	}
	//-------------------------------------------------------------------------------------------
	//de AnimatieBeheerder en Muisbeheerder maken zich met deze methoden bekend 
	//-------------------------------------------------------------------------------------------
	public void meldAnimatieBeheerder(AnimatieBeheerder ab)
	{	this.ab = ab;
	}
	public void meldMuisBeheerder(MuisBeheerder mb)
	{	this.mb = mb;
	}
	//-------------------------------------------------------------------------------------------
	// het Tekenblad vraagt hiermee op of de Tracefunctie aanstaat 
	//-------------------------------------------------------------------------------------------
	public boolean geefTraceStatus()
	{	return traceAan;
	}
	//-------------------------------------------------------------------------------------------
	//de AnimatieBeheerder kan de TraceKnop hiermee disabelen  
	//-------------------------------------------------------------------------------------------
	public void setEnableTraceKnop(boolean b)
	{	traceKnop.setEnabled(b);
	}
	//-------------------------------------------------------------------------------------------
	//het TekenApplet geeft bij het doorlopen van tekenprogramma() de namen van de uitgevoerde
	//stappen (tekenopdrachten) door aan TraceBeheerder.
	//Wanneer het aantal stappen gelijk is aan maxAantalStappen, dan wordt het tot dan toe 
	//voltooide deel van de tekening op het image via de methode tekenTraceImage()op Tekenblad 
	//gezet . De variabele maxAantalStappen wordt met de stapKnop (of met de loopKnop
	//in een Thread) steeds met een verhoogd, waardoor de tekening stap voor stap wordt opgebouwd.
	//met de terugKnop wordt maxAantalStappen telkens een verlaagd, waardoor de tekening stap voor
	//stap terugloopt
	//-------------------------------------------------------------------------------------------
	public boolean volgendeMethode(String naam)
	{	aantalStappen++;
		boolean traceStap = false;
		if(aantalStappen == maxAantalStappen && traceAan)
		{	tb.tekenCursor();
			tb.tekenTraceImage();
			if(!loopAan)methodeVeld.setText(naam);
			else methodeVeld.setText("");
			traceStap = true;
		}
		aantalStappenTekening = aantalStappen;
		if(!traceAan)naarBegin();
		return traceStap;
	}
	//-------------------------------------------------------------------------------------------
	//afhandeling van de knopacties, en het starten van de loopdraad 
	//-------------------------------------------------------------------------------------------
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == stapKnop)
		{	loopAan=false;
			maxAantalStappen++;
			aantalStappen = 0;
			tb.tekenOpnieuw();
		}
		if(e.getSource() == terugKnop)
		{	loopAan=false;
			maxAantalStappen--;
			if(maxAantalStappen<0)maxAantalStappen=0;
			aantalStappen = 0;
			tb.tekenOpnieuw();
		}
		if(e.getSource() == loopKnop)
		{	if(loop==null)
			{	loopAan=true;
				loop = new Thread(this);
				loop.start();
				loopKnop.setLabel("stop");
			}
		
			else
			{	loopAan=false;
				loop = null;
				loopKnop.setLabel("loop");
			}
		}
		if(e.getSource() == beginKnop)
		{	loopAan=false;
			maxAantalStappen = 1;
			aantalStappen = 0;
			tb.tekenOpnieuw();
			produceAction("changed");
		}
		if(e.getSource() == traceKnop)
		{	if(!traceAan)
			{	traceAan = true;
				//rg.setEnableAll(false);
				if(ab!=null)ab.setEnableAnimatieKnop(false);
				if(mb!=null)mb.setEnableMuisActie(false);
				traceKnop.setLabel("trace uitschakelen");
				methodeVeld.setVisible(true);
				beginKnop.setVisible(true);
				stapKnop.setVisible(true);
				terugKnop.setVisible(true);
				loopKnop.setVisible(true);
				
			}
			else
			{	traceAan = false;
				loopAan = false;
				//rg.setEnableAll(true);
				if(ab!=null)ab.setEnableAnimatieKnop(true);
				if(mb!=null)mb.setEnableMuisActie(true);
				tb.tekenOpnieuw();
				methodeVeld.setVisible(false);
				beginKnop.setVisible(false);
				stapKnop.setVisible(false);
				loopKnop.setVisible(false);
				terugKnop.setVisible(false);
				traceKnop.setLabel("trace aanschakelen");
				produceAction("changed");
			}
				
			maxAantalStappen = 1;
			aantalStappen = 0;
			tb.tekenOpnieuw();
			repaint();
			
		}
	}
	public void run()
	{	while(loopAan && aantalStappenTekening>maxAantalStappen)
		{	maxAantalStappen++;
			aantalStappen = 0;
			tb.tekenOpnieuw();
			try	
			{   loop.sleep(100);
			}
    		catch(InterruptedException e) {   }
		}
		loopAan=false;
		loop = null;
		loopKnop.setLabel("loop");
		if(aantalStappenTekening<maxAantalStappen)maxAantalStappen = 0; 
	}
	
//	ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//end ActionProducer
}