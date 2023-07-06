package fi.spot_problems_dwo.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fi.beans.base64code.*;
import fi.spot_problems_dwo.wiskopdr.tekstobjects.*;
import fi.spot_problems_dwo.wiskopdr.formuleobjects.*;
import fi.spot_problems_dwo.wiskopdr.expressies.*;
import fi.spot_problems_dwo.wiskopdr.opdrnav.*;

public class MyOpdrEditContainer extends OpdrContainer implements ActionListener, ItemListener, TextListener
{
	private TekstEditor titelEditor, tekstEditor, randomVarEditor,randomVarShow;
	private FormuleEditor antwoordvak;
	private Label titelLabel, tekstLabel, randVarLabel, antwoordLabel;
	private Checkbox gelijkwaardigCB, herleidingCB, exactCB, stappenCB;
	private Label ScoringLabel, puntenLabel, checkTotaalLabel;
	private TextField gelijkwaardigPV, herleidingPV, exactPV;
	private Choice herleidingsKeuze;
	private int lastChangedTextField;
	
	private int puntenGelijkwaardig = 10;
	private int puntenHerleiding = 0;
	private int puntenExact = 0;
	
	private boolean	herleiding;
	private boolean	exact;
	private boolean	stappen = true;
	
	private int soortHerleiding = 0;
		
	private String[] herleidingItems;

	
	public MyOpdrEditContainer()
	{	setLayout(null);
		super.setSize(770,480); //voor dwo
		
		titelLabel = new Label("Titel van de opdracht:");
		titelLabel.setBounds(30,20,300,20);
		add(titelLabel);
		
		titelEditor = new TekstEditor(false,false);
		titelEditor.setBounds(30,40,300,60);
		add(titelEditor);
		
		tekstLabel = new Label("Tekst voor de opdracht");
		tekstLabel.setBounds(30,110,300,20);
		add(tekstLabel);
		
		tekstEditor = new TekstEditor(true,true);
		tekstEditor.setBounds(30,130,300,250);
		add(tekstEditor);
		
		randVarLabel = new Label("Variabelen voor random parameters:");
		randVarLabel.setBounds(350,20,400,20);
		add(randVarLabel);
		
		randomVarEditor = new TekstEditor(true,false);
		randomVarEditor.setBounds(350,40,400,140);
		add(randomVarEditor);
		
		antwoordLabel = new Label("Juiste antwoord:");
		antwoordLabel.setBounds(350,190,400,20);
		add(antwoordLabel);
				
		antwoordvak = new FormuleEditor(true);
		antwoordvak.setBounds(350,210,400,170);
		add(antwoordvak);
		
		ScoringLabel = new Label("Scoring van het antwoord:");
		ScoringLabel.setBounds(360,385,160,20);
		add(ScoringLabel);
		
		puntenLabel = new Label("Punten:");
		puntenLabel.setBounds(520,385,100,20);
		add(puntenLabel);
		
		checkTotaalLabel = new Label("Let op! Totaal 10");
		checkTotaalLabel.setForeground(Color.red);
		checkTotaalLabel.setBounds(620,385,160,20);
		checkTotaalLabel.setVisible(false);
		add(checkTotaalLabel);
		
		gelijkwaardigCB = new Checkbox("Gelijkwaardig");
		gelijkwaardigCB.setEnabled(false);
		gelijkwaardigCB.setBounds(360,410,120,20);
		gelijkwaardigCB.addItemListener(this);
		gelijkwaardigCB.setState(true);
		add(gelijkwaardigCB);
		
		herleidingCB = new Checkbox("Herleiding");
		herleidingCB.setBounds(360,435,120,20);
		herleidingCB.addItemListener(this);
		add(herleidingCB);
		
		exactCB = new Checkbox("Exact");
		exactCB.setBounds(360,460,120,20);
		exactCB.addItemListener(this);
		add(exactCB,0);
		
		stappenCB = new Checkbox("Stappen mogelijk");
		stappenCB.setBounds(530,190,300,20);
		stappenCB.addItemListener(this);
		stappenCB.setState(true);
		add(stappenCB,0);
		
		gelijkwaardigPV = new TextField("10");
		gelijkwaardigPV.setBounds(520,410,30,20);
		//gelijkwaardigPV.addActionListener(this);
		gelijkwaardigPV.addTextListener(this);
		add(gelijkwaardigPV);
		
		herleidingPV = new TextField("0");
		herleidingPV.setBounds(520,435,30,20);
		herleidingPV.addTextListener(this);
		herleidingPV.setVisible(false);
		add(herleidingPV);
		
		exactPV = new TextField("0");
		exactPV.setBounds(520,460,30,20);
		exactPV.addTextListener(this);
		exactPV.setVisible(false);
		add(exactPV);
		
		herleidingsKeuze = new Choice();
		herleidingsKeuze.setBounds(570,435,170,20);
		herleidingsKeuze.addItemListener(this);
		herleidingsKeuze.setVisible(false);
		add(herleidingsKeuze);
		
		herleidingItems = new String [7];
		herleidingItems[0] = "Geen";
		herleidingItems[1] = "Veelterm herleid zonder haakjes";
		herleidingItems[2] = "Als één macht";
		herleidingItems[3] = "Zonder gebroken of negatieve exponenten";
		herleidingItems[4] = "Als één breuk";
		herleidingItems[5] = "Als één log";
		herleidingItems[6] = "Met alleen enkelvoudige log's (bv log(x)+log(3))";
		for (int i = 0; i<herleidingItems.length; i++) 
		{	herleidingsKeuze.addItem(herleidingItems[i]);
	    }
		
		
		
	}
	
	//public void zetOpdracht(String s)
	//{	setEditState(s);
	//}
		
	public void setEditState(String s)
	{	if(s==null || s.equals(""))return;
		Object o = StringCodeObject.decodeStringToObject(s);
		Hashtable h = (Hashtable)o;
		
		String titel = "titel";
		String tekst = "tekst";
		String randVarString = "";
		String antwoordString = "$f@";
		boolean herleiding = false;
		boolean exact = false;
		boolean stappen = true;
		int soortHerleiding = 0;
		int puntenGelijkwaardig = 10;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		
		if(h.containsKey("titel")) titel = (String)h.get("titel");
		if(h.containsKey("tekst")) tekst = (String)h.get("tekst");
		if(h.containsKey("randVarString")) randVarString = (String)h.get("randVarString");
		if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
		if(h.containsKey("herleiding")) herleiding = ((Boolean)h.get("herleiding")).booleanValue();
		if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
		if(h.containsKey("stappen")) stappen = ((Boolean)h.get("stappen")).booleanValue();
		if(h.containsKey("soortHerleiding")) soortHerleiding = ((Integer)h.get("soortHerleiding")).intValue();
		if(h.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)h.get("puntenGelijkwaardig")).intValue();
		if(h.containsKey("puntenHerleiding")) puntenHerleiding = ((Integer)h.get("puntenHerleiding")).intValue();
		if(h.containsKey("puntenExact")) puntenExact = ((Integer)h.get("puntenExact")).intValue();
		
		titelEditor.zetTekst(titel);
		tekstEditor.zetTekst(tekst);
		randomVarEditor.zetTekst(randVarString);
		antwoordvak.geefFormuleVak().vulVak(antwoordString);
		this.herleiding = herleiding;
		this.exact = exact;
		this.soortHerleiding = soortHerleiding;
		this.puntenGelijkwaardig = puntenGelijkwaardig;
		this.puntenHerleiding = puntenHerleiding;
		this.puntenExact = puntenExact;
		
		gelijkwaardigPV.setText(""+puntenGelijkwaardig);
		
		herleidingCB.setState(herleiding);
		herleidingPV.setVisible(herleiding);
		herleidingPV.setText(""+puntenHerleiding);
		
		herleidingsKeuze.setVisible(herleiding);
		herleidingsKeuze.select(soortHerleiding);
		
		exactCB.setState(exact);
		exactPV.setVisible(exact);
		exactPV.setText(""+puntenExact);
		
		stappenCB.setState(stappen);
		
	}
	
	public String getEditState()
	{	String titel = null;
		String tekst = null;
		String randVarString = null;
		String antwoordString = null;
		boolean herleiding = false;
		boolean exact = false;
		boolean stappen = false;
		int soortHerleiding = 0;
		int puntenGelijkwaardig = 10;
		int puntenHerleiding = 0;
		int puntenExact = 0; 
		
	
		titel = titelEditor.getText();
		tekst = tekstEditor.getText();
		randVarString = randomVarEditor.getText();
		antwoordString = antwoordvak.geefFormuleVak().toString();
		herleiding = this.herleiding;
		exact = this.exact;
		stappen = this.stappen;
		soortHerleiding = this.soortHerleiding;
		puntenGelijkwaardig = this.puntenGelijkwaardig;
		puntenHerleiding = this.puntenHerleiding;
		puntenExact = this.puntenExact;
				
		Hashtable h = new Hashtable();
		h.put("titel",titel);
		h.put("tekst",tekst);
		h.put("randVarString",randVarString);
		h.put("antwoordString",antwoordString);
		h.put("herleiding",new Boolean(herleiding));
		h.put("exact",new Boolean(exact));
		h.put("stappen",new Boolean(stappen));
		h.put("soortHerleiding",new Integer(soortHerleiding));
		h.put("puntenGelijkwaardig",new Integer(puntenGelijkwaardig));
		h.put("puntenHerleiding",new Integer(puntenHerleiding));
		h.put("puntenExact",new Integer(puntenExact));

		String s = StringCodeObject.encodeObjectToString(h);
	    return s;
	}
	
	public void destroy()
	{	
		remove(antwoordvak);
		antwoordvak.destroy();
		antwoordvak = null;
		
		
		remove(tekstEditor);
		tekstEditor.destroy();
		tekstEditor = null;
	}
	
	
	public void textValueChanged(TextEvent e)
	{	int puntenGelijkwaardig = 0;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		
		if(e.getSource()==gelijkwaardigPV)
		{	try
			{	puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
				this.puntenGelijkwaardig = puntenGelijkwaardig;
			}	
			catch(Exception ex)
			{	}
		}
		if(e.getSource()==herleidingPV)
		{	try
			{	puntenHerleiding = Integer.parseInt(herleidingPV.getText());
				this.puntenHerleiding = puntenHerleiding;
			}	
			catch(Exception ex)
			{	}
		}
		if(e.getSource()==exactPV)
		{	try
			{	puntenExact = Integer.parseInt(exactPV.getText());
				this.puntenExact = puntenExact;
			}	
			catch(Exception ex)
			{	}
		}
		boolean b = this.puntenGelijkwaardig + this.puntenHerleiding + this.puntenExact == 10;
		checkTotaalLabel.setVisible(!b);
	}
	
	public void actionPerformed(ActionEvent e)
	{	
	}
	
	public void itemStateChanged(ItemEvent e)
	{	if(e.getSource()==gelijkwaardigCB)
		{	boolean b = gelijkwaardigCB.getState();
			gelijkwaardigPV.setVisible(b);
			herleidingCB.setEnabled(b);
			exactCB.setEnabled(b);
			if(!b)
			{	herleidingPV.setVisible(b);
				herleidingPV.setText("0");
				puntenHerleiding = 0;
				herleidingCB.setState(b);
				herleidingsKeuze.setVisible(b);
				exactPV.setVisible(b);
				exactPV.setText("0");
				puntenExact = 0;
				exactCB.setState(b);
				exactCB.setEnabled(b);
				
			}
			herleidingCB.setEnabled(b);
		}
		if(e.getSource()==herleidingCB)
		{	boolean b = herleidingCB.getState();
			herleiding = b;
			herleidingPV.setVisible(b);
			herleidingsKeuze.setVisible(b);
			if(!b)
			{	herleidingPV.setText("0");
				puntenHerleiding = 0;
			}
			
		}
		if(e.getSource()==exactCB)
		{	boolean b = exactCB.getState();
			exact = b;
			exactPV.setVisible(b);
			if(!b)
			{	exactPV.setText("0");
				puntenExact = 0;
			}
		}
		if(e.getSource()==stappenCB)
		{	boolean b = stappenCB.getState();
			stappen = b;
		}
		if(e.getSource()==herleidingsKeuze)
		{	soortHerleiding = herleidingsKeuze.getSelectedIndex();
		}
	}
	
	//ActionProducer
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
