package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fi.beans.base64code.*;
import fi.algebrapijlenopdr.tekstobjects.*;
import fi.algebrapijlenopdr.opdrnav.*;

public class MyOpdrContainer extends OpdrContainer implements ActionListener 
{
	private Label titelLabel;
	private TekstArea tekstArea;
	//private TekstEditor tekstArea;
	private String codeString;
	private String opdrachtString;
	
	private AlgebraSchuifVeld as;
	private AntwoordFormuleVak antwoordFormuleVak;
	private TekstArea antwoordTekstVak;
	private Button kopieerKnop;
	
	private String gewensteAntwoordString;
	
	
	public MyOpdrContainer(int x, int y, int b, int h)
	{	setLayout(null);
		setBounds(x,y,b,h);
		
		titelLabel = new Label("");
		titelLabel.setBounds(15,10,200,30);
		titelLabel.setFont(new Font("SansSerif",Font.BOLD,16));
		add(titelLabel);
				
		//tekstArea = new TekstEditor(true,true);
		tekstArea = new TekstArea();
		tekstArea.setBounds(10,40,200,h-200);
		tekstArea.setFont(new Font("SansSerif",Font.PLAIN,12));
		add(tekstArea);
				
		as = new AlgebraSchuifVeld(218,10,b-220,h-94);
		//as.setBackground(getBackground());
		add(as);
		
		antwoordFormuleVak = new AntwoordFormuleVak();
		antwoordFormuleVak.setBounds(10,h-77,190,97);
		antwoordFormuleVak.addActionListener(this);
		add(antwoordFormuleVak);
		antwoordFormuleVak.setVisible(false);
		
		antwoordTekstVak = new TekstArea();
		antwoordTekstVak.setBounds(15,h-77,180,97);
		antwoordTekstVak.setFont(new Font("SansSerif",Font.PLAIN,12));
		antwoordTekstVak.setEditable(true);
		add(antwoordTekstVak);
		
		kopieerKnop = new Button("Kopieer vorige opdracht");
		kopieerKnop.setBounds(350,h-107,160,20);
		kopieerKnop.addActionListener(this);
		add(kopieerKnop,0);
	
		
	}
	
	public void zetOpdracht(String s)
	{	opdrachtString = s;
		setEditModeState(s);
		
	}
	
	public void setEditModeState(String s)
	{	if(s==null || s.equals(""))return;
		codeString = s;
		
		Object o = StringCodeObject.decodeStringToObject(s);
		if(o==null)return;
		Hashtable h = (Hashtable)o;
		
		String titel = "titel";
		String tekst = "tekst";
		Hashtable APLaunchState = null;
		boolean fixed = false;
		boolean kopieerOptie = false;
		boolean antwoordOptie = true;
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
		
		
		as.setEditModeState(APLaunchState);
		as.setFixed(fixed);
		if(fixed) add(as,0);
		kopieerKnop.setVisible(kopieerOptie);
		antwoordTekstVak.setVisible(antwoordOptie);
		antwoordFormuleVak.setVisible(antwoordFormuleOptie);
		titelLabel.setText(titel.trim());
		tekstArea.setText(tekst);
		tekstArea.resize();
		
		antwoordTekstVak.setBounds(antwoordTekstVak.getLocation().x,70+tekstArea.getSize().height,190,40);
		antwoordFormuleVak.setBounds(antwoordFormuleVak.getLocation().x,70+tekstArea.getSize().height,199,97);
		
		this.gewensteAntwoordString = antwoordString;
		antwoordFormuleVak.zetJuisteAntwoord(antwoordString);
		
	}
	
	
	public void setState(Hashtable h)
	{	Hashtable APState = null;
		String antwoordString = null;
		String antwoordFormuleString = null;
	
		if(h.containsKey("APState")) APState = (Hashtable)h.get("APState");
		if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
		if(h.containsKey("antwoordFormuleString")) antwoordFormuleString = (String)h.get("antwoordFormuleString");
		
		as.setState(APState);
		if(antwoordString!=null) antwoordTekstVak.setText(antwoordString);
		if(antwoordFormuleString!=null) antwoordFormuleVak.vulVak(antwoordFormuleString);
		
	}
	
	public void setNewState(Hashtable h)
	{	Hashtable APState = (Hashtable)h.get("APState");
		as.setState(APState);
	}
	
	public Hashtable getState()
	{	Hashtable APState=null;
		String antwoordString=null;
		String antwoordFormuleString = null;
		
		APState = as.getState();
		antwoordString = antwoordTekstVak.getText();
		antwoordFormuleString = antwoordFormuleVak.toString();
			    
	    Hashtable h = new Hashtable();
	    h.put("APState", APState);
	    h.put("antwoordString", antwoordString);
	    h.put("antwoordFormuleString", antwoordFormuleString);
	    
	    return h;
	}

	public void stop()
	{	
	}
	
	public void destroy()
	{	
		remove(antwoordFormuleVak);
		if(antwoordFormuleVak!=null) antwoordFormuleVak.destroy();
		antwoordFormuleVak = null;
		
		remove(antwoordTekstVak);
		if(antwoordTekstVak!=null) antwoordTekstVak.destroy();
		antwoordTekstVak = null;
		
		
		remove(tekstArea);
		tekstArea.destroy();
		tekstArea = null;
		
		as.destroy();
	}

	public void start()
	{	as.tekenOpnieuw();
	}

	public void kijkNa()
	{	
	}

	public void opnieuw()
	{	
		
	}

	public void zetMode(int mode)
	{	
		
	}
	
	
	
	public void zetOpdrachtTekst(String s)
	{	tekstArea.setText(s);
		tekstArea.resize();
	}

	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == kopieerKnop)
		{	produceAction("kopieer");
		}
		else if(e.getSource()==antwoordFormuleVak)
		{	if(e.getActionCommand().equals("changed"))
			{	score = antwoordFormuleVak.getScore();
				correct = antwoordFormuleVak.isCorrect();
				produceAction("changed");
			}
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
