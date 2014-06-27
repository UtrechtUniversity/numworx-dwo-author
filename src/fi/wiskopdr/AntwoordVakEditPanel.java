package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.base64code.*;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.opdrnav.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;


public class AntwoordVakEditPanel extends JPanel implements InteractieEditPanel, ActionListener,  MouseListener, MouseMotionListener, TabletOwner
{
	private FormuleEditor antwoordvak,startEditor,vormEditor;
	private JLabel antwoordLabel, startLabel, feedbackLabel;
	private JCheckBox  gelijkwaardigCB, herleidingCB, exactCB, stappenCB, vergelijkingCB, eindOplossingCB, bewerkingKnoppenCB, abcKnopCB, subKnopCB;
	private JLabel ScoringLabel, puntenLabel, checkTotaalLabel;
	private JTextField gelijkwaardigPV, herleidingPV, exactPV, eindOplossingPV, feedbackPV;
	private JComboBox herleidingsKeuze;
	private JCheckBox tipsCB;
	
	
	private int puntenGelijkwaardig = 10;
	private int puntenHerleiding = 0;
	private int puntenExact = 0;
	private int puntenFeedback = 0;
	
	private boolean	gelijkwaardig = true;
	private boolean	herleiding;
	private boolean	exact;
	private boolean	stappen = true;
	
	private int soortHerleiding = 0;
		
	private String[] herleidingItems;
	
	private boolean vergelijking;
	private boolean eindOplossingNodig;
	private boolean abcKnop;
	private boolean subKnop;
	private boolean bewerkingKnoppen;
	private int puntenEindOplossing = 0;
	
	//private AntwoordEditPanel antwoordEditPanel;
	private TekstEditor feedbackTekst;
	
	
	private JCheckBox formuleToolBijFocusCB;
	private boolean formuleToolBijFocus;
	
	private JCheckBox feedbackCB;
	private boolean hasFeedback;
	private OpdrachtNrRij tabbladTab;
	private int aantalAnswerModels = 1;
	private Hashtable[] answerModels;
	private Hashtable resAnswerModel = new Hashtable();
	private int answerModelNr = 0;
	
	private PlusMinKnop aantalTabsKnop;
	private PlusMinKnop tabPositieKnop;
	
	private ActKeuzePanel goedFoutIP;
	
	private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;
	private boolean tips;
	
	public AntwoordVakEditPanel(int soort)
	{	setLayout(null);
		super.setSize(770,520); //voor dwo
		setBackground(Color.white);		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		
		startLabel = makeLabel(5,30,770,20,WiskOpdr.rb.getString("startExpLabel"),true);
				
		startEditor = new FormuleEditor(false);
		startEditor.setBounds(5,50,470,105);
		startEditor.setFont(font);
		add(startEditor);
		
		
		antwoordLabel = makeLabel(5,160,770,20,WiskOpdr.rb.getString("antwoordLabel"),true);
				
		antwoordvak = new FormuleEditor(true);
		antwoordvak.setBounds(5,180,770,150);
		antwoordvak.setFont(font);
		antwoordvak.addActionListener(this);
		add(antwoordvak);
		antwoordvak.setResizable(true);
		
		feedbackCB = makeCheckBox(145,160,770,20,"feedback",false,true);
			
		
		String[] items = {WiskOpdr.rb.getString("goedLabel"),WiskOpdr.rb.getString("halfLabel"),WiskOpdr.rb.getString("foutLabel")};
		goedFoutIP = new ActKeuzePanel(items,440,420,60,80);
		add(goedFoutIP,0);
		
		tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,184);
		tabbladTab.setSize(tabbladTab.getSize().width, 23);
		tabbladTab.setTab(true);
		tabbladTab.setScoresVisible(false);
		tabbladTab.addActionListener(this);
		tabbladTab.setBackground(new Color(210,210,210));
		tabbladTab.setSelected(1);
		add(tabbladTab,0);
		
		aantalTabsKnop = new PlusMinKnop(250+25*aantalAnswerModels+5 ,184,20,16,PlusMinKnop.HORIZONTAAL);
		aantalTabsKnop.setBackground(new Color(210,210,210));
		aantalTabsKnop.addActionListener(this);
    	add(aantalTabsKnop,0);
    	
    	tabPositieKnop = new PlusMinKnop(246+25*answerModelNr+5 ,162,20,16,PlusMinKnop.HORIZONTAAL);
    	tabPositieKnop.addActionListener(this);
    	add(tabPositieKnop,0);
		
		answerModels = new Hashtable[aantalAnswerModels];
		
		feedbackLabel = makeLabel(20,330,320,20,WiskOpdr.rb.getString("feedbackCBLabel"),true);
		
		feedbackTekst = new TekstEditor(false,true,true);
		feedbackTekst.setBounds(5,350,300,160);
		feedbackTekst.setFont(font);
		feedbackTekst.setBackground(new Color(255,255,200));
		add(feedbackTekst,0);
		
		vormEditor = new FormuleEditor(true);
		vormEditor.setBounds(515,350,260,160);
		vormEditor.setFont(font);
		vormEditor.addActionListener(this);
		vormEditor.setVisible(false);
		add(vormEditor,0);
		vormEditor.setResizable(true);
		
		ScoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
		puntenLabel = makeLabel(460,385,40,20,WiskOpdr.rb.getString("puntenLabel"),true);
		checkTotaalLabel = makeLabel(620,385,160,20,WiskOpdr.rb.getString("checkTotaalLabel"),false);
		checkTotaalLabel.setForeground(Color.red);
		
		gelijkwaardigCB = makeCheckBox(320,410,120,20,WiskOpdr.rb.getString("gelijkwaardigCBLabel"),true,true);
		herleidingCB = makeCheckBox(320,435,120,20,WiskOpdr.rb.getString("vormCBLabel"),false,true);
		exactCB = makeCheckBox(320,460,120,20,WiskOpdr.rb.getString("exactCBLabel"),false,true);
		stappenCB = makeCheckBox(630,50,200,20,WiskOpdr.rb.getString("stappenCBLabel"),true,false);
		bewerkingKnoppenCB = makeCheckBox(500,80,130,20,WiskOpdr.rb.getString("bewerkingKnoppenCBLabel"),false,false);
		abcKnopCB = makeCheckBox(630,80,60,20,WiskOpdr.rb.getString("abcCBLabel"),false,false);
		subKnopCB = makeCheckBox(690,80,60,20,WiskOpdr.rb.getString("subKnopCBLabel"),false,false);
		vergelijkingCB = makeCheckBox(520,50,110,20,WiskOpdr.rb.getString("vergelijkingCBLabel"),false,false);
		eindOplossingCB = makeCheckBox(320,435,120,20,WiskOpdr.rb.getString("eindOplossingCBLabel"),false,false);
		tipsCB = makeCheckBox(500,50,280,20,"Tips en hulp (experimenteel bij lin. verg.)",false,true);
		formuleToolBijFocusCB = makeCheckBox(630,20,200,20,WiskOpdr.rb.getString("formuleToolCBLabel"),false,false);
		
		gelijkwaardigPV = makeTextField(460,410,30,20,"10",true);
		herleidingPV = makeTextField(460,435,30,20,"0",false);
		exactPV = makeTextField(460,460,30,20,"0",false);
		feedbackPV = makeTextField(460,385,30,20,"0",false);
		eindOplossingPV = makeTextField(460,435,30,20,"0",false);
		
		herleidingsKeuze = new JComboBox();
		herleidingsKeuze.setBounds(570,435,170,20);
		herleidingsKeuze.setFont(font);
		herleidingsKeuze.addActionListener(this);
		herleidingsKeuze.setVisible(false);
		//add(herleidingsKeuze,0);
		
		herleidingItems = new String [7];
		herleidingItems[0] = WiskOpdr.rb.getString("herleidingKeuze_0");
		herleidingItems[1] = WiskOpdr.rb.getString("herleidingKeuze_1");
		herleidingItems[2] = WiskOpdr.rb.getString("herleidingKeuze_2");
		herleidingItems[3] = WiskOpdr.rb.getString("herleidingKeuze_3");
		herleidingItems[4] = WiskOpdr.rb.getString("herleidingKeuze_4");
		herleidingItems[5] = WiskOpdr.rb.getString("herleidingKeuze_5");
		herleidingItems[6] = WiskOpdr.rb.getString("herleidingKeuze_6");
		for (int i = 0; i<herleidingItems.length; i++) 
		{	herleidingsKeuze.addItem(herleidingItems[i]);
	    }
       
		setFeedbackOption(false);
		
		if(soort==0)
		{	stappen = true;
			zetVergelijkingsMode(false);
					
		}
		else if(soort==1)
		{	stappen = true;
			zetVergelijkingsMode(true);
		}
		else if(soort==2)
		{	stappen = false;
			zetVergelijkingsMode(false);
			zetVergelijkingKnoppen(false);
			
			startLabel.setVisible(false);
			startEditor.setVisible(false);
			
			formuleToolBijFocusCB.setVisible(true);
			//feedbackCB.setVisible(false);
		}
		else if(soort==3)
		{	stappen = false;
			zetVergelijkingsMode(true);
			zetVergelijkingKnoppen(false);
			
			startLabel.setVisible(false);
			startEditor.setVisible(false);
			feedbackCB.setVisible(false);
		}
		
		
		
	}
	
	public JCheckBox makeCheckBox(int x, int y, int b, int h, String text, boolean selected, boolean visible)
	{	JCheckBox checkbox = new JCheckBox(text);
		checkbox.setBounds(x,y,b,h);
		checkbox.setFont(font);
		checkbox.setOpaque(false);
		checkbox.addActionListener(this);
		checkbox.setSelected(selected);
		checkbox.setVisible(visible);
		add(checkbox,0);
		return checkbox;
	}
	
	public JLabel makeLabel(int x, int y, int b, int h, String text, boolean visible)
	{	JLabel label = new JLabel(text);
		label.setBounds(x,y,b,h);
		label.setFont(font);
		label.setVisible(visible);
		add(label,0);
		return label;
	}
	
	public JTextField makeTextField(int x, int y, int b, int h, String text, boolean visible)
	{	JTextField textField = new JTextField(text);
		textField.setBounds(x,y,b,h);
		textField.setFont(font);
		textField.addActionListener(this);
		textField.setVisible(visible);
		add(textField,0);
		return textField;
	}
	
	public void zetBreedte(int b)
	{	//grafiekPanel.setSize(b,grafiekPanel.getSize().height);
	}
	public void zetHoogte(int h)
	{	//grafiekPanel.setSize(grafiekPanel.getSize().width, h);
	}
	
	private void fillAnswerModel(Hashtable h)
	{
		String antwoordString = "$f@";
		boolean gelijkwaardig = true;
		boolean herleiding = false;
		boolean exact = false;
		int puntenFeedback = 0;
		int soortHerleiding = 0;
		String feedback = "";
		String vormString = "$f@";
		int goedHalfFout = 2;
		
		antwoordString = antwoordvak.geefFormuleVak().toString();
		gelijkwaardig = this.gelijkwaardig;
		herleiding = this.herleiding;
		exact = this.exact;
		soortHerleiding = this.soortHerleiding;
				
		puntenFeedback = (Integer.parseInt(feedbackPV.getText()));
		feedback  = feedbackTekst.getText();
		vormString = vormEditor.geefFormuleVak().toString();
		goedHalfFout = goedFoutIP.geefKeuze()-1;
		
		h.put("antwoordString",antwoordString);
		h.put("gelijkwaardig",new Boolean(gelijkwaardig));
		h.put("herleiding",new Boolean(herleiding));
		h.put("exact",new Boolean(exact));
		h.put("puntenFeedback",new Integer(puntenFeedback));
		h.put("soortHerleiding",new Integer(soortHerleiding));
		h.put("feedback",feedback);
		h.put("vormString",vormString);
		h.put("goedHalfFout",new Integer(goedHalfFout));
		
	}
	
	private void setAnswerModel(Hashtable h)
	{	String antwoordString = "$f@";
		boolean gelijkwaardig = true;
		boolean herleiding = false;
		boolean exact = false;
		int puntenFeedback = 0;
		int soortHerleiding = 0;
		String feedback = "";
		String vormString = "$f@";
		int goedHalfFout = 2;
		
		if(h!=null) 
		{	if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
			if(h.containsKey("gelijkwaardig")) gelijkwaardig = ((Boolean)h.get("gelijkwaardig")).booleanValue();
			if(h.containsKey("herleiding")) herleiding = ((Boolean)h.get("herleiding")).booleanValue();
			if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
			if(h.containsKey("stappen")) stappen = ((Boolean)h.get("stappen")).booleanValue();
			if(h.containsKey("soortHerleiding")) soortHerleiding = ((Integer)h.get("soortHerleiding")).intValue();
			if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
			if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
			if(h.containsKey("vormString")) vormString = (String)h.get("vormString");
			if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
			
		}
		this.herleiding = herleiding;
		this.gelijkwaardig = gelijkwaardig;
		this.exact = exact;
		this.soortHerleiding = soortHerleiding;
		this.puntenFeedback = puntenFeedback;
		
		antwoordvak.geefFormuleVak().vulVak(antwoordString);
		vormEditor.geefFormuleVak().vulVak(vormString);
			
		herleidingCB.setVisible(true);
		exactCB.setVisible(true);
		herleidingsKeuze.setVisible(true);
				
		gelijkwaardigCB.setSelected(gelijkwaardig);
		herleidingCB.setSelected(herleiding);
		exactCB.setSelected(exact);
		
		vormEditor.setVisible(herleiding);
		
		feedbackPV.setVisible(hasFeedback);
		feedbackPV.setText(""+puntenFeedback);
		
		feedbackTekst.zetTekst(feedback);
		feedbackTekst.repaint();
		
		goedFoutIP.setItem(goedHalfFout);
		
		
	}
	
	private void getAnswerModel()
	{	if(answerModels==null)return;
		if(answerModels[answerModelNr]==null) answerModels[answerModelNr] = new Hashtable();
		fillAnswerModel(answerModels[answerModelNr]);
	}
	
	private void setAnswerModel()
	{	if(answerModels==null)return;
		setAnswerModel(answerModels[answerModelNr]);	
	}
	
			
	public void setEditState(Hashtable interactiePanelLaunchState)
	{			
					
				String antwoordString = "$f@";
				String startString = "$f@";
				boolean herleiding = false;
				boolean exact = false;
				boolean stappen = true;
				int soortHerleiding = 0;
				int puntenGelijkwaardig = 10;
				int puntenHerleiding = 0;
				int puntenExact = 0;
				boolean vergelijking = false;
				boolean eindOplossingNodig = true;
				int puntenEindOplossing = 10;
				boolean bewerkingKnoppen = false;
				boolean abcKnop = false;
				boolean subKnop = false;
				boolean formuleToolBijFocus = false;
				Hashtable[] answerModels = null;
				boolean hasFeedback = false;
				String vormString = "$f@";
				boolean tips = false;
				
				if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
				if(interactiePanelLaunchState.containsKey("startString")) startString = (String)interactiePanelLaunchState.get("startString");
				if(interactiePanelLaunchState.containsKey("herleiding")) herleiding = ((Boolean)interactiePanelLaunchState.get("herleiding")).booleanValue();
				if(interactiePanelLaunchState.containsKey("exact")) exact = ((Boolean)interactiePanelLaunchState.get("exact")).booleanValue();
				if(interactiePanelLaunchState.containsKey("stappen")) stappen = ((Boolean)interactiePanelLaunchState.get("stappen")).booleanValue();
				if(interactiePanelLaunchState.containsKey("soortHerleiding")) soortHerleiding = ((Integer)interactiePanelLaunchState.get("soortHerleiding")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)interactiePanelLaunchState.get("puntenGelijkwaardig")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenHerleiding")) puntenHerleiding = ((Integer)interactiePanelLaunchState.get("puntenHerleiding")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenExact")) puntenExact = ((Integer)interactiePanelLaunchState.get("puntenExact")).intValue();
				if(interactiePanelLaunchState.containsKey("vergelijking")) vergelijking = ((Boolean)interactiePanelLaunchState.get("vergelijking")).booleanValue();
				if(interactiePanelLaunchState.containsKey("eindOplossingNodig")) eindOplossingNodig = ((Boolean)interactiePanelLaunchState.get("eindOplossingNodig")).booleanValue();
				if(interactiePanelLaunchState.containsKey("puntenEindOplossing")) puntenEindOplossing = ((Integer)interactiePanelLaunchState.get("puntenEindOplossing")).intValue();
				if(interactiePanelLaunchState.containsKey("bewerkingKnoppen")) bewerkingKnoppen = ((Boolean)interactiePanelLaunchState.get("bewerkingKnoppen")).booleanValue();
				if(interactiePanelLaunchState.containsKey("abcKnop")) abcKnop = ((Boolean)interactiePanelLaunchState.get("abcKnop")).booleanValue();
				if(interactiePanelLaunchState.containsKey("subKnop")) subKnop = ((Boolean)interactiePanelLaunchState.get("subKnop")).booleanValue();
				if(interactiePanelLaunchState.containsKey("formuleToolBijFocus")) formuleToolBijFocus = ((Boolean)interactiePanelLaunchState.get("formuleToolBijFocus")).booleanValue();
				if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
				if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
				if(interactiePanelLaunchState.containsKey("vormString")) vormString = (String)interactiePanelLaunchState.get("vormString");
				if(interactiePanelLaunchState.containsKey("tips")) tips = ((Boolean)interactiePanelLaunchState.get("tips")).booleanValue();
				
				this.herleiding = herleiding;
				this.exact = exact;
				this.soortHerleiding = soortHerleiding;
				this.puntenGelijkwaardig = puntenGelijkwaardig;
				this.puntenHerleiding = puntenHerleiding;
				this.puntenExact = puntenExact;
				this.vergelijking = vergelijking;
				this.stappen = stappen;
				this.eindOplossingNodig = eindOplossingNodig;
				this.puntenEindOplossing = puntenEindOplossing;
				this.bewerkingKnoppen = bewerkingKnoppen;
				this.abcKnop = abcKnop;
				this.subKnop = subKnop;
				this.formuleToolBijFocus = formuleToolBijFocus;
				this.answerModels = answerModels;
				this.tips = tips;
				
				
				if(hasFeedback)
				{	aantalAnswerModels = answerModels.length;
					remove(tabbladTab);
					tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,184);
					tabbladTab.setTab(true);
					tabbladTab.setScoresVisible(false);
					tabbladTab.setSize(tabbladTab.getSize().width, 23);
					tabbladTab.addActionListener(this);
					tabbladTab.setBackground(new Color(210,210,210));
					tabbladTab.setSelected(answerModelNr+1);
					add(tabbladTab,0);
					aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,184);
					
					answerModelNr = 0;
					setAnswerModel();
				}
				
				
				antwoordvak.geefFormuleVak().vulVak(antwoordString);
				startEditor.geefFormuleVak().vulVak(startString);
				vormEditor.geefFormuleVak().vulVak(vormString);
				
				stappenCB.setSelected(stappen);
				vergelijkingCB.setSelected(vergelijking);
				abcKnopCB.setSelected(abcKnop);
				subKnopCB.setSelected(subKnop);
				bewerkingKnoppenCB.setSelected(bewerkingKnoppen);
				formuleToolBijFocusCB.setSelected(formuleToolBijFocus);
				
				
				setFeedbackOption(hasFeedback);
				feedbackCB.setSelected(hasFeedback);
				tipsCB.setSelected(tips);
				if(hasFeedback)return;
				
				
			
				if(vergelijking)
				{	bewerkingKnoppenCB.setVisible(true);
					abcKnopCB.setVisible(true);
					subKnopCB.setVisible(true);
					herleidingCB.setVisible(false);
					exactCB.setVisible(true);
					herleidingPV.setVisible(false);
					exactPV.setVisible(true);
					herleidingsKeuze.setVisible(false);
								
					eindOplossingCB.setVisible(true);
					eindOplossingCB.setSelected(eindOplossingNodig);
					eindOplossingPV.setVisible(eindOplossingNodig);
					eindOplossingPV.setText(""+puntenEindOplossing);
					
					gelijkwaardigPV.setText(""+puntenGelijkwaardig);
								
					exactCB.setSelected(exact);
					exactPV.setVisible(exact);
					exactPV.setText(""+puntenExact);
				}
				else
				{	bewerkingKnoppenCB.setVisible(false);
					abcKnopCB.setVisible(false);
					subKnopCB.setVisible(false);
					herleidingCB.setVisible(true);
					exactCB.setVisible(true);
					herleidingPV.setVisible(true);
					exactPV.setVisible(true);
					herleidingsKeuze.setVisible(true);
				
					eindOplossingCB.setVisible(false);
					eindOplossingPV.setVisible(false);
					
					gelijkwaardigPV.setText(""+puntenGelijkwaardig);
				
					herleidingCB.setSelected(herleiding);
					herleidingPV.setVisible(herleiding);
					herleidingPV.setText(""+puntenHerleiding);
					
					//herleidingsKeuze.setVisible(herleiding);
					//herleidingsKeuze.setSelectedIndex(soortHerleiding);
					vormEditor.setVisible(herleiding);
					
					exactCB.setSelected(exact);
					exactPV.setVisible(exact);
					exactPV.setText(""+puntenExact);
				}
		
			//// EIND //// Deze code zal moeten worden aangepast als de interface meerdere antwoordvakken ondersteunt
			
			
		
	}
	
	public Hashtable getEditState()
	{	
		Hashtable interactiePanelLaunchState = new Hashtable();
        
        
			String antwoordString = null;
			String startString = null;
			boolean herleiding = false;
			boolean exact = false;
			boolean stappen = false;
			int soortHerleiding = 0;
			int puntenGelijkwaardig = 10;
			int puntenHerleiding = 0;
			int puntenExact = 0; 
			boolean vergelijking = false;
			boolean eindOplossingNodig = true;
			int puntenEindOplossing = 10;
			boolean bewerkingKnoppen = false;
			boolean abcKnop = false;
			boolean subKnop = false;
			int scoreMax = 0;
			boolean formuleToolBijFocus = false;
			Hashtable[] answerModels;
			boolean hasFeedback;
			String vormString = "$f@";
			boolean tips;
			
			getAnswerModel();
			answerModels = this.answerModels;
			if(answerModels!=null)setAnswerModel(answerModels[0]);
			
			antwoordString = antwoordvak.geefFormuleVak().toString();
			startString = startEditor.geefFormuleVak().toString();
			vormString = vormEditor.geefFormuleVak().toString();
			herleiding = this.herleiding;
			exact = this.exact;
			stappen = this.stappen;
			soortHerleiding = this.soortHerleiding;
			tips = this.tips;
			
			try
			{	puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
				this.puntenGelijkwaardig = puntenGelijkwaardig;
			}	
			catch(Exception ex)
			{	}
			try
			{	puntenHerleiding = Integer.parseInt(herleidingPV.getText());
				this.puntenHerleiding = puntenHerleiding;
			}	
			catch(Exception ex)
			{	}
			try
			{	puntenExact = Integer.parseInt(exactPV.getText());
				this.puntenExact = puntenExact;
			}	
			catch(Exception ex)
			{	}
			try
			{	puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
				this.puntenEindOplossing = puntenEindOplossing;
			}	
			catch(Exception ex)
			{	}
			puntenGelijkwaardig = this.puntenGelijkwaardig;
			puntenHerleiding = this.puntenHerleiding;
			puntenExact = this.puntenExact;
			vergelijking = this.vergelijking;
			eindOplossingNodig = this.eindOplossingNodig;
			puntenEindOplossing = this.puntenEindOplossing;
			bewerkingKnoppen = this.bewerkingKnoppen;
			abcKnop = this.abcKnop;
			subKnop = this.subKnop;
			formuleToolBijFocus = this.formuleToolBijFocus;
			if(vergelijking)scoreMax = puntenGelijkwaardig + puntenEindOplossing + puntenExact;
			else scoreMax = puntenGelijkwaardig + puntenHerleiding + puntenExact;
			hasFeedback = this.hasFeedback;
			if(hasFeedback)scoreMax = puntenFeedback;
			
			
			
			
			interactiePanelLaunchState.put("antwoordString",antwoordString);
			interactiePanelLaunchState.put("startString",startString);
			interactiePanelLaunchState.put("herleiding",new Boolean(herleiding));
			interactiePanelLaunchState.put("exact",new Boolean(exact));
			interactiePanelLaunchState.put("stappen",new Boolean(stappen));
			interactiePanelLaunchState.put("soortHerleiding",new Integer(soortHerleiding));
			interactiePanelLaunchState.put("puntenGelijkwaardig",new Integer(puntenGelijkwaardig));
			interactiePanelLaunchState.put("puntenHerleiding",new Integer(puntenHerleiding));
			interactiePanelLaunchState.put("puntenExact",new Integer(puntenExact));
			interactiePanelLaunchState.put("vergelijking",new Boolean(vergelijking));
			interactiePanelLaunchState.put("eindOplossingNodig",new Boolean(eindOplossingNodig));
			interactiePanelLaunchState.put("puntenEindOplossing",new Integer(puntenEindOplossing));
			interactiePanelLaunchState.put("bewerkingKnoppen",new Boolean(bewerkingKnoppen));
			interactiePanelLaunchState.put("abcKnop",new Boolean(abcKnop));
			interactiePanelLaunchState.put("subKnop",new Boolean(subKnop));
			interactiePanelLaunchState.put("formuleToolBijFocus",new Boolean(formuleToolBijFocus));
			interactiePanelLaunchState.put("scoreMax",new Integer(scoreMax));
			if(answerModels!=null)interactiePanelLaunchState.put("answerModels",answerModels);
			interactiePanelLaunchState.put("hasFeedback",new Boolean(hasFeedback));
			interactiePanelLaunchState.put("vormString",vormString);
			interactiePanelLaunchState.put("tips",new Boolean(tips));
			
		
		
		
		return interactiePanelLaunchState;
	}
	public void destroy()
	{	
		
	}
		
	public void wis()
	{
	}
	public void zetMode(int mode)
	{
	}
    public void stop()
    {
	}
    public void start()
    {	antwoordvak.setNewScrollSize();
	}
    
	
	
	/*public void textValueChanged(TextEvent e)
	{	int puntenGelijkwaardig = 0;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		int puntenEindOplossing = 0;
		
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
		if(e.getSource()==eindOplossingPV)
		{	try
			{	puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
				this.puntenEindOplossing = puntenEindOplossing;
			}	
			catch(Exception ex)
			{	}
		}
		boolean b = false;
		if(vergelijking)b = this.puntenGelijkwaardig + this.puntenEindOplossing + this.puntenExact == 10;
		else b = this.puntenGelijkwaardig + this.puntenHerleiding + this.puntenExact == 10;
		 
		//checkTotaalLabel.setVisible(!b);
	}*/
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource() == tabbladTab)
		{	int nr = Integer.parseInt(e.getActionCommand())-1;
			if(answerModelNr != nr) 
			{
				getAnswerModel();
				answerModelNr = nr;
				setAnswerModel();
				tabPositieKnop.setLocation(246+25*answerModelNr+5 ,162);
			}
			
		}
		else if(e.getSource() == tabPositieKnop)
		{	if(e.getActionCommand().equals("plus") && answerModelNr<aantalAnswerModels-1) 
			{	resAnswerModel = new Hashtable();
				fillAnswerModel(resAnswerModel);
				answerModels[answerModelNr] = answerModels[answerModelNr+1];
				answerModels[answerModelNr+1] = resAnswerModel;
				answerModelNr++;
				tabbladTab.setSelected(answerModelNr+1);
				tabPositieKnop.setLocation(246+25*answerModelNr+5 ,162);
			}
			if(e.getActionCommand().equals("min") && answerModelNr>0) 
			{	resAnswerModel = new Hashtable();
				fillAnswerModel(resAnswerModel);
				answerModels[answerModelNr] = answerModels[answerModelNr-1];
				answerModels[answerModelNr-1] = resAnswerModel;
				answerModelNr--;
				tabbladTab.setSelected(answerModelNr+1);
				tabPositieKnop.setLocation(246+25*answerModelNr+5 ,162);
			}
			
		}
		else if(e.getSource() == aantalTabsKnop)
		{	if(e.getActionCommand().equals("min") && aantalAnswerModels>1)
			{	//remove(opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1]);
				//opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1] = null;
				aantalAnswerModels--;
				if(answerModelNr>aantalAnswerModels-1) answerModelNr--;
				setAnswerModel();
				aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,184);
				remove(tabbladTab);
				tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,184);
				tabbladTab.setTab(true);
				tabbladTab.setScoresVisible(false);
				tabbladTab.setSize(tabbladTab.getSize().width, 23);
				tabbladTab.addActionListener(this);
				tabbladTab.setBackground(new Color(210,210,210));
				tabbladTab.setSelected(answerModelNr+1);
				add(tabbladTab,0);
				Hashtable[] answerModelsNew = new Hashtable[aantalAnswerModels];
				for(int i=0 ; i<aantalAnswerModels ; i++)
				{	answerModelsNew[i] = answerModels[i];
				}
				answerModels = answerModelsNew;
				repaint();
				
			}
			if(e.getActionCommand().equals("plus") && aantalAnswerModels<20)
			{	aantalAnswerModels++;
				aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,184);
				remove(tabbladTab);
				tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,184);
				tabbladTab.setTab(true);
				tabbladTab.setScoresVisible(false);
				tabbladTab.setSize(tabbladTab.getSize().width, 23);
				tabbladTab.addActionListener(this);
				tabbladTab.setBackground(new Color(210,210,210));
				tabbladTab.setSelected(answerModelNr+1);
				add(tabbladTab,0);
				Hashtable[] answerModelsNew = new Hashtable[aantalAnswerModels];
				for(int i=0 ; i<aantalAnswerModels-1 ; i++)
				{	answerModelsNew[i] = answerModels[i];
				}
				answerModels = answerModelsNew;
				repaint();
			}
		}
		else if(e.getSource()==feedbackCB)
		{	setFeedbackOption(feedbackCB.isSelected());
			
		}
		else if(e.getSource()==tipsCB)
		{	tips = tipsCB.isSelected();
			
		}
		else if(e.getSource()==vergelijkingCB)
		{	boolean b = vergelijkingCB.isSelected();
			zetVergelijkingsMode(b);
		}
	
		else if(e.getSource()==gelijkwaardigCB)
		{	gelijkwaardig = gelijkwaardigCB.isSelected();
			if(!hasFeedback && answerModelNr==0)gelijkwaardigPV.setVisible(gelijkwaardig);
			
		}
		else if(e.getSource()==herleidingCB)
		{	boolean b = herleidingCB.isSelected();
			herleiding = b; //herleiding wordt gebruikt voor vormen en moet op false blijven staan
			if(!hasFeedback && answerModelNr==0)herleidingPV.setVisible(b);
			herleidingsKeuze.setVisible(b);
			vormEditor.setVisible(b);
			if(!b)
			{	herleidingPV.setText("0");
				puntenHerleiding = 0;
			}
			
		}
		else if(e.getSource()==exactCB)
		{	boolean b = exactCB.isSelected();
			exact = b;
			if(!hasFeedback && answerModelNr==0)exactPV.setVisible(b);
			
			if(b)
			{	eindOplossingNodig = true;
				eindOplossingCB.setSelected(true);
				if(vergelijking)eindOplossingPV.setVisible(true);
				eindOplossingCB.setSelected(true);
				
				
				gelijkwaardigPV.setText("0");
				eindOplossingPV.setText("0");
				exactPV.setText("10");
				
				puntenEindOplossing = 0;
				puntenGelijkwaardig = 0;
				puntenExact = 10;
			}
			else
			{	
				gelijkwaardigPV.setText("0");
				if(vergelijking)eindOplossingPV.setText("10");
				else gelijkwaardigPV.setText("10");
				exactPV.setText("0");
			
				puntenGelijkwaardig = 0;
				puntenEindOplossing = 10;
				puntenExact = 0;
			}	
		}
		else if(e.getSource()==eindOplossingCB)
		{	boolean b = eindOplossingCB.isSelected();
			eindOplossingNodig = b;
			eindOplossingPV.setVisible(b);
			if(b)
			{	gelijkwaardigPV.setText("0");
				eindOplossingPV.setText("10");
				exactPV.setText("0");
				
				puntenGelijkwaardig = 0;
				puntenEindOplossing = 10;
				puntenExact = 0;
			}
			else
			{	exactCB.setSelected(false);
				exactPV.setVisible(false);
				
				gelijkwaardigPV.setText("10");
				eindOplossingPV.setText("0");
				exactPV.setText("0");
				
				puntenGelijkwaardig = 10;
				puntenEindOplossing = 0;
				puntenExact = 0;
			}
		}
		else if(e.getSource()==stappenCB)
		{	boolean b = stappenCB.isSelected();
			stappen = b;
		}
		else if(e.getSource()==bewerkingKnoppenCB)
		{	boolean b = bewerkingKnoppenCB.isSelected();
			bewerkingKnoppen = b;
		}
		else if(e.getSource()==abcKnopCB)
		{	boolean b = abcKnopCB.isSelected();
			abcKnop = b;
		}
		else if(e.getSource()==subKnopCB)
		{	boolean b = subKnopCB.isSelected();
			subKnop = b;
		}
		else if(e.getSource()==formuleToolBijFocusCB)
		{	boolean b = formuleToolBijFocusCB.isSelected();
			formuleToolBijFocus = b;
		}
		else if(e.getSource()==herleidingsKeuze)
		{	soortHerleiding = herleidingsKeuze.getSelectedIndex();
		}
		else
		{
			int puntenGelijkwaardig = 0;
			int puntenHerleiding = 0;
			int puntenExact = 0;
			int puntenEindOplossing = 0;
			
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
			if(e.getSource()==eindOplossingPV)
			{	try
				{	puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
					this.puntenEindOplossing = puntenEindOplossing;
				}	
				catch(Exception ex)
				{	}
			}
			boolean b = false;
			if(vergelijking)b = this.puntenGelijkwaardig + this.puntenEindOplossing + this.puntenExact == 10;
			else b = this.puntenGelijkwaardig + this.puntenHerleiding + this.puntenExact == 10;
			 
		}
	}
	
	public void zetVergelijkingsMode(boolean b)
	{	vergelijking = b;
		herleidingCB.setVisible(!b);
		//exactCB.setVisible(b);
		herleidingPV.setVisible(!b);
		//exactPV.setVisible(b);
		herleidingsKeuze.setVisible(!b);
		
		eindOplossingCB.setVisible(b);
		eindOplossingCB.setSelected(b);
		eindOplossingPV.setVisible(b);
		bewerkingKnoppenCB.setVisible(b);
		abcKnopCB.setVisible(b);
		subKnopCB.setVisible(b);
		if(b)
		{	feedbackCB.setVisible(false);
			
			eindOplossingNodig = true;
			eindOplossingPV.setText("10");
			gelijkwaardigPV.setText("0");
			puntenEindOplossing = 10;
			puntenExact = 0;
			puntenGelijkwaardig = 0;
			exactCB.setSelected(false);
			exactPV.setText("0");
			
		}
		else
		{	feedbackCB.setVisible(true);
			
			eindOplossingPV.setText("0");
			gelijkwaardigPV.setText("10");
			puntenEindOplossing = 0;
			puntenExact = 0;
			puntenGelijkwaardig = 10;
			puntenHerleiding = 0;
			herleidingCB.setSelected(false);
			herleidingPV.setText("0");
			herleidingsKeuze.setVisible(b);
			exactCB.setSelected(false);
			exactPV.setText("0");
		}
	}
	
	public void zetTekstVak(boolean b)
	{	//tipsCB.setVisible(b);
	}
	
	public void setFeedbackOption(boolean b)
	{
		hasFeedback = b;
		tabbladTab.setVisible(b);
		feedbackTekst.setVisible(b);
		feedbackLabel.setVisible(b);
		aantalTabsKnop.setVisible(b);
		tabPositieKnop.setVisible(b);
		feedbackPV.setVisible(b);
		goedFoutIP.setVisible(b);
		puntenLabel.setVisible(!b);
		gelijkwaardigPV.setVisible(!b);
		if(vergelijking)eindOplossingPV.setVisible(!b);
		if(vergelijking)eindOplossingCB.setVisible(!b);
		if(vergelijking)herleidingCB.setVisible(b);
		if(!vergelijking)herleidingPV.setVisible(!b);
		exactPV.setVisible(!b);
		answerModelNr = 0;
		tabbladTab.setSelected(answerModelNr+1);
		if(b)setAnswerModel();
	}
	
	/*public void itemStateChanged(ItemEvent e)
	{	if(e.getSource()==feedbackCB)
		{	setFeedbackOption(feedbackCB.isSelected());
			
		}
		if(e.getSource()==vergelijkingCB)
		{	boolean b = vergelijkingCB.isSelected();
			zetVergelijkingsMode(b);
		}
	
		if(e.getSource()==gelijkwaardigCB)
		{	gelijkwaardig = gelijkwaardigCB.isSelected();
			if(!hasFeedback && answerModelNr==0)gelijkwaardigPV.setVisible(gelijkwaardig);
			
		}
		if(e.getSource()==herleidingCB)
		{	boolean b = herleidingCB.isSelected();
			herleiding = b; //herleiding wordt gebruikt voor vormen en moet op false blijven staan
			if(!hasFeedback && answerModelNr==0)herleidingPV.setVisible(b);
			herleidingsKeuze.setVisible(b);
			vormEditor.setVisible(b);
			if(!b)
			{	herleidingPV.setText("0");
				puntenHerleiding = 0;
			}
			
		}
		if(e.getSource()==exactCB)
		{	boolean b = exactCB.isSelected();
			exact = b;
			if(!hasFeedback && answerModelNr==0)exactPV.setVisible(b);
			
			if(b)
			{	eindOplossingNodig = true;
				eindOplossingCB.setSelected(true);
				if(vergelijking)eindOplossingPV.setVisible(true);
				eindOplossingCB.setSelected(true);
				
				
				gelijkwaardigPV.setText("0");
				eindOplossingPV.setText("0");
				exactPV.setText("10");
				
				puntenEindOplossing = 0;
				puntenGelijkwaardig = 0;
				puntenExact = 10;
			}
			else
			{	
				gelijkwaardigPV.setText("0");
				if(vergelijking)eindOplossingPV.setText("10");
				else gelijkwaardigPV.setText("10");
				exactPV.setText("0");
			
				puntenGelijkwaardig = 0;
				puntenEindOplossing = 10;
				puntenExact = 0;
			}	
		}
		if(e.getSource()==eindOplossingCB)
		{	boolean b = eindOplossingCB.isSelected();
			eindOplossingNodig = b;
			eindOplossingPV.setVisible(b);
			if(b)
			{	gelijkwaardigPV.setText("0");
				eindOplossingPV.setText("10");
				exactPV.setText("0");
				
				puntenGelijkwaardig = 0;
				puntenEindOplossing = 10;
				puntenExact = 0;
			}
			else
			{	exactCB.setSelected(false);
				exactPV.setVisible(false);
				
				gelijkwaardigPV.setText("10");
				eindOplossingPV.setText("0");
				exactPV.setText("0");
				
				puntenGelijkwaardig = 10;
				puntenEindOplossing = 0;
				puntenExact = 0;
			}
		}
		if(e.getSource()==stappenCB)
		{	boolean b = stappenCB.isSelected();
			stappen = b;
		}
		if(e.getSource()==bewerkingKnoppenCB)
		{	boolean b = bewerkingKnoppenCB.isSelected();
			bewerkingKnoppen = b;
		}
		if(e.getSource()==abcKnopCB)
		{	boolean b = abcKnopCB.isSelected();
			abcKnop = b;
		}
		if(e.getSource()==subKnopCB)
		{	boolean b = subKnopCB.isSelected();
			subKnop = b;
		}
		if(e.getSource()==formuleToolBijFocusCB)
		{	boolean b = formuleToolBijFocusCB.isSelected();
			formuleToolBijFocus = b;
		}
		if(e.getSource()==herleidingsKeuze)
		{	soortHerleiding = herleidingsKeuze.getSelectedIndex();
		}
		
	}*/
	
	public void zetVergelijkingKnoppen(boolean b)
	{	bewerkingKnoppenCB.setVisible(b);
		abcKnopCB.setVisible(b);
		subKnopCB.setVisible(b);
		
	}
	
	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{	if(tablet==null) return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
	}
	
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x,y);
			
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
		
	}
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			
			
		}
		if(!tabletAdded)
		{	add(tablet,0);
			tablet.setLocation(x,y);
			tabletAdded = true;
			//resize();
            repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}
	
	public void removeTablet()
	{	if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
		tabletAdded = false;
	}
	
	public Tablet getTablet()
	{	return tablet;
	}
	
	public void mousePressed(MouseEvent e)
	{	
	}
	
	public void mouseClicked(MouseEvent e){;}
	public void mouseReleased(MouseEvent e)
	{	
	}
	public void mouseEntered(MouseEvent e)
	{	
	}
	public void mouseExited(MouseEvent e)
	{	
	}
	
	public void mouseDragged(MouseEvent e)
	{	
	}
	public void mouseMoved(MouseEvent e)
	{	
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
