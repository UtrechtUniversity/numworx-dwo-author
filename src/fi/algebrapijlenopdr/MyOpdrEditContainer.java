package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fi.beans.base64code.*;
import fi.algebrapijlenopdr.tekstobjects.*;
import fi.algebrapijlenopdr.opdrnav.*;

public class MyOpdrEditContainer extends OpdrContainer implements ActionListener, ItemListener
{
	private AlgebraSchuifVeld as;
	//private TextField titelVeld;
	//private TextArea opdrachtVeld;
	private TekstArea titelEditor;//, tekstEditor;
	private TekstEditor tekstEditor;
	private AntwoordFormuleVak antwoordFormuleVak;
	private Checkbox fixedCheckbox, kopieerKnopCheckbox, antwoordTekstCheckbox, antwoordFormuleCheckbox;

	
	public MyOpdrEditContainer()
	{	setLayout(null);
		super.setSize(790,500); //voor dwo
		
		as = new AlgebraSchuifVeld(220,10,550,395);
		add(as);
		
		titelEditor = new TekstArea();
		titelEditor.setBounds(5,10,210,55);
		titelEditor.setEditable(true);
		titelEditor.setFont(new Font("SansSerif",Font.PLAIN,20));
		add(titelEditor);
		
		//tekstEditor = new TekstArea();
		//tekstEditor.setBounds(565,70,210,380);
		//tekstEditor.setEditable(true);
		//add(tekstEditor);
		
		tekstEditor = new TekstEditor(true,true);
		tekstEditor.setBounds(5,70,210,240);
		add(tekstEditor);
		
		antwoordFormuleVak = new AntwoordFormuleVak();
		antwoordFormuleVak.setBounds(5,320,210,97);
		add(antwoordFormuleVak);
		antwoordFormuleVak.setVisible(false);
		
		fixedCheckbox = new Checkbox("fixed");
		fixedCheckbox.setBounds(660,405,100,20);
		add(fixedCheckbox);
		
		kopieerKnopCheckbox = new Checkbox("kopieerOptie");
		kopieerKnopCheckbox.setBounds(460,405,150,20);
		add(kopieerKnopCheckbox);
		
		antwoordTekstCheckbox = new Checkbox("Antwoordvak tekst");
		antwoordTekstCheckbox.setBounds(5,getSize().height-45,210,20);
		antwoordTekstCheckbox.addItemListener(this);
		add(antwoordTekstCheckbox);
		
		antwoordFormuleCheckbox = new Checkbox("Antwoordvak getal/formule");
		antwoordFormuleCheckbox.setBounds(5,getSize().height-20,210,20);
		antwoordFormuleCheckbox.addItemListener(this);
		add(antwoordFormuleCheckbox);
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
		Hashtable APLaunchState = null;
		boolean fixed = false;
		boolean kopieerOptie = false;
		boolean antwoordOptie = false;
		boolean antwoordFormuleOptie = false;
		String antwoordString = "$f@";
		
		
		if(h.containsKey("titel")) titel = (String)h.get("titel");
		if(h.containsKey("tekst")) tekst = (String)h.get("tekst");
		if(h.containsKey("APLaunchState")) APLaunchState = (Hashtable)h.get("APLaunchState");
		if(h.containsKey("fixed")) fixed = ((Boolean)h.get("fixed")).booleanValue();
		if(h.containsKey("kopieerOptie")) kopieerOptie = ((Boolean)h.get("kopieerOptie")).booleanValue();
		if(h.containsKey("antwoordOptie")) antwoordOptie = ((Boolean)h.get("antwoordOptie")).booleanValue();
		if(h.containsKey("antwoordFormuleOptie")) antwoordFormuleOptie = ((Boolean)h.get("antwoordFormuleOptie")).booleanValue();
		if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
		
		titelEditor.setText(titel);
		//titelEditor.resize();
		tekstEditor.setText(tekst);
		//tekstEditor.resize();
		as.setState(APLaunchState);
		fixedCheckbox.setState(fixed);
		kopieerKnopCheckbox.setState(kopieerOptie);
		antwoordTekstCheckbox.setState(antwoordOptie);
		antwoordFormuleCheckbox.setState(antwoordFormuleOptie);
		antwoordFormuleVak.vulVak(antwoordString);
		antwoordFormuleVak.setVisible(antwoordFormuleOptie);
	}
	
	public String getEditState()
	{	String titel = null;
		String tekst = null;
		Hashtable APLaunchState = null;
		boolean fixed = false;
		boolean kopieerOptie = false;
		boolean antwoordOptie = false;
		boolean antwoordFormuleOptie = false;
		String antwoordString = null;
		
		titel = titelEditor.getText();
		tekst = tekstEditor.getText();
		APLaunchState = as.getState();
		fixed = fixedCheckbox.getState();
		kopieerOptie = kopieerKnopCheckbox.getState();
		antwoordOptie = antwoordTekstCheckbox.getState();
		antwoordFormuleOptie = antwoordFormuleCheckbox.getState();
		antwoordString = antwoordFormuleVak.geefFormuleVak().toString();
		
				
		Hashtable h = new Hashtable();
		h.put("titel",titel);
		h.put("tekst",tekst);
		h.put("APLaunchState",APLaunchState);
		h.put("fixed",new Boolean(fixed));
		h.put("kopieerOptie",new Boolean(kopieerOptie));
		h.put("antwoordOptie",new Boolean(antwoordOptie));
		h.put("antwoordFormuleOptie",new Boolean(antwoordFormuleOptie));
		h.put("antwoordString",antwoordString);
		
		String s = StringCodeObject.encodeObjectToString(h);
	    return s;
	}
	
	public void start()
	{	as.tekenOpnieuw();
	}
	
	public void destroy()
	{	
		remove(antwoordFormuleVak);
		antwoordFormuleVak.destroy();
		antwoordFormuleVak = null;
		
		
		remove(tekstEditor);
		tekstEditor.destroy();
		tekstEditor = null;
	}
	
	
	public void actionPerformed(ActionEvent e)
	{	
	}
	
	public void itemStateChanged(ItemEvent e)
	{	if(e.getSource()==antwoordTekstCheckbox && antwoordTekstCheckbox.getState()) 
		{	antwoordFormuleCheckbox.setState(false);
			antwoordTekstCheckbox.setState(true);
		}
		if(e.getSource()==antwoordFormuleCheckbox && antwoordFormuleCheckbox.getState()) 
		{	antwoordTekstCheckbox.setState(false);
			antwoordFormuleCheckbox.setState(true);
		}
		antwoordFormuleVak.setVisible(antwoordFormuleCheckbox.getState());
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
