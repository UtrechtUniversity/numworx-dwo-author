package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.base64code.*;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.opdrnav.*;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;


public class AntwoordTekstVakEditPanel extends JLayeredPane implements InteractieEditPanel, ActionListener,  MouseListener, MouseMotionListener, TabletOwner
{
	private TekstEditor antwoordvak;
	private JLabel antwoordLabel,  feedbackLabel;
	private JLabel ScoringLabel, checkTotaalLabel;
	private JTextField scoreMaxPV, feedbackPV;
	
	private int puntenGelijkwaardig = 10;
	
	private int puntenFeedback = 0;
	
	
	
	
	
	//private AntwoordEditPanel antwoordEditPanel;
	private TekstEditor feedbackTekst;
	
	
	
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
	
	private JCheckBox checkCB;
	private JCheckBox teltMeeCB;
	
	private JCheckBox formuleModeCB;
	private JCheckBox formuleToolBijFocusCB;
	
	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;
	
	private JCheckBox logCB;
	private JTextField logIDField;
	private JTextField logIDLabelField;
	private JLabel logIDLabelLabel;
	private ObjectiveChoiceButton logObjectivesButton;
	
	private JCheckBox boxMetRandCB;
		
	
	public AntwoordTekstVakEditPanel()
	{	setLayout(null);
		super.setSize(770,520); //voor dwo
		setBackground(Color.white);		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		
		
		
		antwoordLabel = makeLabel(5,160,770,20,WiskOpdr.rb.getString("antwoordLabel"),true);
				
		antwoordvak = new TekstEditor();
		antwoordvak.setBounds(5,180,770,150);
		antwoordvak.setFont(font);
		antwoordvak.addActionListener(this);
		add(antwoordvak);
		antwoordvak.setResizable(true);
		
		feedbackCB = makeCheckBox(145,160,770,20,WiskOpdr.rb.getString("feedbackCBLabel"),false,true);
		checkCB = makeCheckBox(5,5,200,20,WiskOpdr.rb.getString("checkCBLabel"),true,true);
        teltMeeCB = makeCheckBox(225,5,200,20,WiskOpdr.rb.getString("teltMeeCBLabel"),true,true);
        
        formuleModeCB = makeCheckBox(500,50,200,20,WiskOpdr.rb.getString("formuleInvoerModeCBLabel"),false,true);
        formuleToolBijFocusCB = makeCheckBox(500,70,200,20,WiskOpdr.rb.getString("formuleToolCBLabel"),false,false);
        
        boxMetRandCB = makeCheckBox(500,105,80,20,WiskOpdr.rb.getString("boxMetRand"),true,true);
        
    		
		
		String[] items = {WiskOpdr.rb.getString("goedLabel"),WiskOpdr.rb.getString("halfLabel"),WiskOpdr.rb.getString("foutLabel")};
		goedFoutIP = new ActKeuzePanel(items,440,420,70,80);
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
		
		feedbackLabel = makeLabel(20,330,320,20,WiskOpdr.rb.getString("feedbackLabel"),true);
		
		feedbackTekst = new TekstEditor(false,true,true);
		feedbackTekst.setBounds(5,350,300,160);
		feedbackTekst.setFont(font);
		feedbackTekst.setBackground(new Color(255,255,200));
		add(feedbackTekst,0);
		
		ScoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
		checkTotaalLabel = makeLabel(620,385,160,20,WiskOpdr.rb.getString("checkTotaalLabel"),false);
		checkTotaalLabel.setForeground(Color.red);
		
		scoreMaxPV = makeTextField(460,385,30,20,"0",false);
		feedbackPV = makeTextField(460,385,30,20,"0",false);
		
		 
		setFeedbackOption(false);
		
		logCB = new JCheckBox(WiskOpdr.rb.getString("logCBLabel"));
        logCB.setBounds(450,5,70,20);
        logCB.addActionListener(this);
        logCB.setOpaque(false);
		add(logCB);
		
		logIDField = new JTextField("0");
		logIDField.setBounds(520,5,60,20);
		logIDField.addActionListener(this);
		logIDField.setVisible(false);
		add(logIDField);
		
		logIDLabelField = makeTextField(520,25,60,20,"",false);
        logIDLabelLabel = makeLabel(470,25,50,20,WiskOpdr.rb.getString("TVEP_logIDLabelLabel"),false);
        
		
		logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString, WiskOpdr.studentModel);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        logObjectivesButton.setBounds(600,5,120,20);
        if(WiskOpdr.objectives!=null)add(logObjectivesButton);
		
		
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
		int puntenFeedback = 0;
		String feedback = "";
		int goedHalfFout = 2;
		
		antwoordString = antwoordvak.getText().trim();
		puntenFeedback = (Integer.parseInt(feedbackPV.getText()));
		feedback  = feedbackTekst.getText();
		goedHalfFout = goedFoutIP.geefKeuze()-1;
		
		h.put("antwoordString",antwoordString);
		h.put("puntenFeedback",new Integer(puntenFeedback));
		h.put("feedback",feedback);
		h.put("goedHalfFout",new Integer(goedHalfFout));
		
	}
	
	private void setAnswerModel(Hashtable h)
	{	String antwoordString = "";
		int puntenFeedback = 0;
		String feedback = "";
		int goedHalfFout = 2;
		
		if(h!=null) 
		{	if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
			if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
			if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
			if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
			
		}
		this.puntenFeedback = puntenFeedback;
		antwoordvak.zetTekst(antwoordString);
		antwoordvak.layoutTekst();
		
		feedbackPV.setVisible(hasFeedback);
		feedbackPV.setText(""+puntenFeedback);
		
		feedbackTekst.zetTekst(feedback);
		feedbackTekst.layoutTekst();
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
		int scoreMax = 10;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		boolean subKnop = false;
		boolean check = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean formuleMode = false;
		boolean formuleToolBijFocus = false;
		boolean boxMetRand = true;
		boolean[][] logObjectives = null;
		String[] smObjectives = null;
        
		
		
		
		if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
		if(interactiePanelLaunchState.containsKey("startString")) startString = (String)interactiePanelLaunchState.get("startString");
		if(interactiePanelLaunchState.containsKey("scoreMax")) scoreMax = ((Integer)interactiePanelLaunchState.get("scoreMax")).intValue();
		if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
		if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
		if(interactiePanelLaunchState.containsKey("check")) check = ((Boolean)interactiePanelLaunchState.get("check")).booleanValue();
		if(interactiePanelLaunchState.containsKey("teltMee")) teltMee = ((Boolean)interactiePanelLaunchState.get("teltMee")).booleanValue();
		if(interactiePanelLaunchState.containsKey("logOption")) logOption = ((Boolean)interactiePanelLaunchState.get("logOption")).booleanValue();
		if(interactiePanelLaunchState.containsKey("logID")) logID = (String)interactiePanelLaunchState.get("logID");
		if(interactiePanelLaunchState.containsKey("logIDLabel")) logIDLabel = (String)interactiePanelLaunchState.get("logIDLabel");
		if(interactiePanelLaunchState.containsKey("formuleMode")) formuleMode = ((Boolean)interactiePanelLaunchState.get("formuleMode")).booleanValue();
		if(interactiePanelLaunchState.containsKey("formuleToolBijFocus")) formuleToolBijFocus = ((Boolean)interactiePanelLaunchState.get("formuleToolBijFocus")).booleanValue();
		if(interactiePanelLaunchState.containsKey("boxMetRand")) boxMetRand = ((Boolean)interactiePanelLaunchState.get("boxMetRand")).booleanValue();
		if(interactiePanelLaunchState.containsKey("logObjectives")) logObjectives = (boolean[][])interactiePanelLaunchState.get("logObjectives");
		if(interactiePanelLaunchState.containsKey(Constants.OBJECTIVES)) smObjectives = (String[]) interactiePanelLaunchState.get(Constants.OBJECTIVES);
		this.puntenGelijkwaardig = puntenGelijkwaardig;
		this.answerModels = answerModels;
		
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
		
		antwoordvak.zetTekst(antwoordString);
		antwoordvak.layoutTekst();
		scoreMaxPV.setText(""+scoreMax);
		
		setFeedbackOption(hasFeedback);
		feedbackCB.setSelected(hasFeedback);
		//if(hasFeedback)return;
		
		checkCB.setSelected(check);
        teltMeeCB.setSelected(teltMee);
        logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        logIDLabelField.setVisible(logOption);
        logIDLabelLabel.setVisible(logOption);
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);
        logIDLabelField.setText(logIDLabel);
        logObjectivesButton.setChoices(logObjectives);
        logObjectivesButton.setObjectives(smObjectives);
       
        formuleModeCB.setSelected(formuleMode);
        formuleToolBijFocusCB.setVisible(formuleMode);
        formuleToolBijFocusCB.setSelected(formuleToolBijFocus);
        boxMetRandCB.setSelected(boxMetRand);
        
	}
	
	public Hashtable getEditState()
	{	
		Hashtable interactiePanelLaunchState = new Hashtable();
        
        
		String antwoordString = null;
		int scoreMax = 0;
		int[][] scoreMaxObjectives = null;
		Hashtable[] answerModels;
		boolean hasFeedback;
		boolean check = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean[][] logObjectives = null;
		String[] smObjectives = null;
		
		boolean formuleMode = false;
		boolean formuleToolBijFocus = false;
		boolean boxMetRand = true;		
		
		getAnswerModel();
		answerModels = this.answerModels;
		if(answerModels!=null)setAnswerModel(answerModels[0]);
		
		antwoordString = antwoordvak.getText().trim();
		scoreMax = Integer.parseInt(scoreMaxPV.getText());
		hasFeedback = this.hasFeedback;
		if(hasFeedback)scoreMax = puntenFeedback;
		
		check = checkCB.isSelected();
		teltMee = teltMeeCB.isSelected();
		logOption = logCB.isSelected();
		logID = logIDField.getText();
		logIDLabel = logIDLabelField.getText();
		logObjectives = logObjectivesButton.getChoices();
		smObjectives = logObjectivesButton.getObjectives();
		formuleMode = formuleModeCB.isSelected();
		formuleToolBijFocus = formuleToolBijFocusCB.isSelected();
		boxMetRand = boxMetRandCB.isSelected();
		
		if(logObjectives!=null)
		{	scoreMaxObjectives = new int[logObjectives.length][];
			for(int j=0 ; j<scoreMaxObjectives.length; j++)
			{	scoreMaxObjectives[j] = new int[logObjectives[j].length];
				for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)
				{	if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
				}
			}
		}
		
		interactiePanelLaunchState.put("antwoordString",antwoordString);
		interactiePanelLaunchState.put("scoreMax",new Integer(scoreMax));
		if(answerModels!=null)interactiePanelLaunchState.put("answerModels",answerModels);
		interactiePanelLaunchState.put("hasFeedback",new Boolean(hasFeedback));
		interactiePanelLaunchState.put("check",new Boolean(check));
		interactiePanelLaunchState.put("teltMee",new Boolean(teltMee));
		interactiePanelLaunchState.put("logOption",new Boolean(logOption));
		interactiePanelLaunchState.put("logID",logID);
		interactiePanelLaunchState.put("logIDLabel",logIDLabel);
		interactiePanelLaunchState.put("formuleMode",new Boolean(formuleMode));
		interactiePanelLaunchState.put("formuleToolBijFocus",new Boolean(formuleToolBijFocus));
		interactiePanelLaunchState.put("boxMetRand",new Boolean(boxMetRand));
		if(logObjectives!=null)
        {	interactiePanelLaunchState.put("logObjectives",logObjectives);
        	interactiePanelLaunchState.put("scoreMaxObjectives",scoreMaxObjectives);
            try {
              interactiePanelLaunchState.put(Constants.OBJECTIVES, smObjectives);
            } catch(Exception e) {}
        }
		
		
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
		else if(e.getSource()==formuleModeCB)
		{	formuleToolBijFocusCB.setVisible(formuleModeCB.isSelected());
			
		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());
	    	logIDLabelField.setVisible(logCB.isSelected());	
	    	logIDLabelLabel.setVisible(logCB.isSelected());
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
		
		//else if(e.getSource()==gelijkwaardigCB)
		//{	gelijkwaardig = gelijkwaardigCB.isSelected();
		//	if(!hasFeedback && answerModelNr==0)gelijkwaardigPV.setVisible(gelijkwaardig);
			
		//}
		
		else
		{
			int puntenGelijkwaardig = 0;
			int puntenHerleiding = 0;
			int puntenExact = 0;
			int puntenEindOplossing = 0;
			
			/*if(e.getSource()==gelijkwaardigPV)
			{	try
				{	puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
					this.puntenGelijkwaardig = puntenGelijkwaardig;
				}	
				catch(Exception ex)
				{	}
			}*/
			
			
			
			
			 
		}
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
		scoreMaxPV.setVisible(!b);
		answerModelNr = 0;
		tabbladTab.setSelected(answerModelNr+1);
		if(b)setAnswerModel();
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
			this.setLayer((Component)tablet, JLayeredPane.PALETTE_LAYER.intValue());
			
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

