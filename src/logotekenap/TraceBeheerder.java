package logotekenap;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import fi.javalogoweb.JavaLogoSchuifVeld;
import fi.javalogoweb.JavaLogoWeb;

public class TraceBeheerder extends JPanel implements ActionListener,Runnable, ItemListener
{
	private JButton stapKnop,terugKnop,loopKnop,beginKnop,traceKnop;
	private JCheckBox showVariables;
	private JTextField methodeVeld;
	private int maxAantalStappen,aantalStappen,aantalStappenTekening;
	private Tekenblad tb;
	private JavaLogoSchuifVeld jlsveld;
	private boolean loopAan,traceAan;
	private Thread loop;
	
	public TraceBeheerder(Tekenblad tb, JavaLogoSchuifVeld v)
	{	
		setLayout(null);
		makeGUI();
		aantalStappen = 0;
		maxAantalStappen = 0;
		aantalStappenTekening = 1;
		this.tb = tb;
		jlsveld = v;
		loopAan = false;
		traceAan = false;
		naarBegin();
	}
	
	public void makeGUI()
	{
		beginKnop = new JButton(JavaLogoWeb.rb.getString("beginKnopLabel"));
		beginKnop.setBounds(0,5,60,23);
		beginKnop.setFont(JavaLogoWeb.boldfont);
		beginKnop.setMargin(new Insets(0,0,0,0));
		beginKnop.addActionListener(this);
		add(beginKnop);
		stapKnop = new JButton(JavaLogoWeb.rb.getString("stapKnopLabel"));
		stapKnop.setBounds(70,5,60,23);
		stapKnop.setFont(JavaLogoWeb.boldfont);
		stapKnop.setMargin(new Insets(0,0,0,0));
		stapKnop.addActionListener(this);
		add(stapKnop);
		terugKnop = new JButton(JavaLogoWeb.rb.getString("terugKnopLabel"));
		terugKnop.setBounds(140,5,60,23);
		terugKnop.setFont(JavaLogoWeb.boldfont);
		terugKnop.setMargin(new Insets(0,0,0,0));
		terugKnop.addActionListener(this);
		add(terugKnop);
		methodeVeld = new JTextField("",15);
		methodeVeld.setBounds(210,5,160,23);
		methodeVeld.setFont(JavaLogoWeb.defaultfont);
		methodeVeld.setMargin(new Insets(0,0,0,0));
		add(methodeVeld);
		loopKnop = new JButton(JavaLogoWeb.rb.getString("loopKnopLabel"));
		loopKnop.setBounds(300,5,40,20);
		loopKnop.setMargin(new Insets(0,0,0,0));
		loopKnop.addActionListener(this);
		//add(loopKnop);
		traceKnop = new JButton(JavaLogoWeb.rb.getString("traceOnLabel"));
		traceKnop.setBounds(0,32,200,23);
		traceKnop.setFont(JavaLogoWeb.boldfont);
		traceKnop.setMargin(new Insets(0,0,0,0));
		traceKnop.addActionListener(this);
		add(traceKnop);

		showVariables = new JCheckBox(JavaLogoWeb.rb.getString("showVarLabel"));
		showVariables.setOpaque(false);
		showVariables.addItemListener(this);
		showVariables.setEnabled(true);
		showVariables.setSelected(false);
		showVariables.setBounds(210, 32, 160, 23);
		showVariables.setFont(JavaLogoWeb.boldfont);
		add(showVariables);
	}
	
	public void naarBegin()
	{	methodeVeld.setVisible(false);
		beginKnop.setVisible(false);
		stapKnop.setVisible(false);
		loopKnop.setVisible(false);
		terugKnop.setVisible(false);
		showVariables.setVisible(false);
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
	{	
		aantalStappen++;
		boolean traceStap = false;
		if(aantalStappen == maxAantalStappen && traceAan)
		{	
			tb.tekenCursor();
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
		{	
			loopAan=false;
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
				loopKnop.setText(JavaLogoWeb.rb.getString("stopKnopLabel"));
			}
		
			else
			{	loopAan=false;
				loop = null;
				loopKnop.setText(JavaLogoWeb.rb.getString("loopKnopLabel"));
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
				traceKnop.setText(JavaLogoWeb.rb.getString("traceOffLabel"));
				methodeVeld.setVisible(true);
				beginKnop.setVisible(true);
				stapKnop.setVisible(true);
				terugKnop.setVisible(true);
				loopKnop.setVisible(true);
				showVariables.setVisible(true);
			}
			else
			{	traceAan = false;
				loopAan = false;
				//rg.setEnableAll(true);
				tb.tekenOpnieuw();
				showVariables.setSelected(false);
				showVariables.setVisible(false);
				jlsveld.setVartracing(false);
				methodeVeld.setVisible(false);
				beginKnop.setVisible(false);
				stapKnop.setVisible(false);
				loopKnop.setVisible(false);
				terugKnop.setVisible(false);
				traceKnop.setText(JavaLogoWeb.rb.getString("traceOnLabel"));
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
		loopKnop.setText("loop");
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

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		boolean vartrace = ( e.getStateChange() == ItemEvent.SELECTED );
		jlsveld.setVartracing(vartrace);
	}
}