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


public class AntwoordKeuzeVakEditPanel extends JLayeredPane implements InteractieEditPanel, FocusListener, ActionListener,  MouseListener, MouseMotionListener, TabletOwner
{
	private TekstEditor antwoordvak;
	private JLabel antwoordLabel,  feedbackLabel;
	private JLabel ScoringLabel, checkTotaalLabel;
	private JTextField maxScorePV, feedbackPV;
	
	private int puntenGelijkwaardig = 10;
	
	private int puntenFeedback = 0;
	
	private int aantalKeuzes = 0;
	private JLabel aantalKeuzesLabel;
    private JTextField aantalKeuzesTF;
    
    private JTextField aantalSelectablesTF;
    
    private JCheckBox[] selectableCheckboxes;
	private TekstEditor[] keuzeVelden;
	private int maxKeuzeVelden = 50;
	private JPanel keuzeVeldenPanel;
	private JScrollPane scrollPaneKeuzeVelden;
	private JPanel basisKeuzeVeldenPanel;
	
    private JLabel[] keuzeLabels;
	
	
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
	
	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;
	
	private JCheckBox checkCB;
	private JCheckBox teltMeeCB;
	private JCheckBox logCB;
	private JTextField logIDField;
	private ObjectiveChoiceButton logObjectivesButton;
	
	private JCheckBox checkExternalCB;
	
	
	
	public AntwoordKeuzeVakEditPanel()
	{	setLayout(null);
		super.setSize(770,520); //voor dwo
		setBackground(Color.white);		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		aantalKeuzesLabel = new JLabel(WiskOpdr.rb.getString("aantalKeuzesLabel"));
		aantalKeuzesLabel.setBounds(10,50,180,20);
        add(aantalKeuzesLabel);
        
        aantalKeuzesTF = new JTextField("0");
        aantalKeuzesTF.setBounds(190,50,40,20);
        aantalKeuzesTF.addActionListener(this);
        aantalKeuzesTF.addFocusListener(this);
        add(aantalKeuzesTF);
        
        basisKeuzeVeldenPanel = new JPanel();
    	basisKeuzeVeldenPanel.setBounds(10,80,220,400);
		basisKeuzeVeldenPanel.setLayout(new BorderLayout());
    	add(basisKeuzeVeldenPanel);
    	basisKeuzeVeldenPanel.setOpaque(false);
        
        keuzeVeldenPanel = new JPanel();
		keuzeVeldenPanel.setLayout(null);
		keuzeVeldenPanel.setBounds(0,0,190,100);
    	keuzeVeldenPanel.setOpaque(false);
    	
    	scrollPaneKeuzeVelden = new JScrollPane(keuzeVeldenPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	scrollPaneKeuzeVelden.setBorder(BorderFactory.createEmptyBorder());
    	basisKeuzeVeldenPanel.add(scrollPaneKeuzeVelden);
    	scrollPaneKeuzeVelden.setBackground(getBackground());
    	
        
        selectableCheckboxes = new JCheckBox[20];
        keuzeVelden = new TekstEditor[maxKeuzeVelden];
        keuzeLabels = new JLabel[20];
		
		
		antwoordLabel = makeLabel(250,160,520,20,WiskOpdr.rb.getString("antwoordLabel"),true);
				
		antwoordvak = new TekstEditor();
		antwoordvak.setBounds(250,180,520,150);
		antwoordvak.setFont(font);
		antwoordvak.addActionListener(this);
		add(antwoordvak);
		antwoordvak.setResizable(true);
		
		feedbackCB = makeCheckBox(395,160,520,20,WiskOpdr.rb.getString("feedbackCBLabel"),false,true);
		checkCB = makeCheckBox(5,5,200,20,WiskOpdr.rb.getString("checkCBLabel"),true,true);
        teltMeeCB = makeCheckBox(225,5,200,20,WiskOpdr.rb.getString("teltMeeCBLabel"),true,true);
        logCB = makeCheckBox(450,5,70,20,WiskOpdr.rb.getString("logCBLabel"),false,true);
        logIDField = makeTextField(520,5,60,20,"0",false);
        
        checkExternalCB = makeCheckBox(250,120,200,20,WiskOpdr.rb.getString("checkExternalCBLabel"),false,true);
        
        logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        logObjectivesButton.setBounds(600,5,120,20);
        if(WiskOpdr.objectives!=null)add(logObjectivesButton);
			
		
		String[] items = {WiskOpdr.rb.getString("goedLabel"),WiskOpdr.rb.getString("halfLabel"),WiskOpdr.rb.getString("foutLabel")};
		goedFoutIP = new ActKeuzePanel(items,690,420,70,80);
		add(goedFoutIP,0);
		
		tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 500,184);
		tabbladTab.setSize(tabbladTab.getSize().width, 23);
		tabbladTab.setTab(true);
		tabbladTab.setScoresVisible(false);
		tabbladTab.addActionListener(this);
		tabbladTab.setBackground(new Color(210,210,210));
		tabbladTab.setSelected(1);
		add(tabbladTab,0);
		
		aantalTabsKnop = new PlusMinKnop(500+25*aantalAnswerModels+5 ,184,20,16,PlusMinKnop.HORIZONTAAL);
		aantalTabsKnop.setBackground(new Color(210,210,210));
		aantalTabsKnop.addActionListener(this);
    	add(aantalTabsKnop,0);
    	
    	tabPositieKnop = new PlusMinKnop(496+25*answerModelNr+5 ,162,20,16,PlusMinKnop.HORIZONTAAL);
    	tabPositieKnop.addActionListener(this);
    	add(tabPositieKnop,0);
		
		answerModels = new Hashtable[aantalAnswerModels];
		
		feedbackLabel = makeLabel(250,330,320,20,WiskOpdr.rb.getString("feedbackLabel"),true);
		
		feedbackTekst = new TekstEditor(false,true,true);
		feedbackTekst.setBounds(250,350,300,160);
		feedbackTekst.setFont(font);
		feedbackTekst.setBackground(new Color(255,255,200));
		add(feedbackTekst,0);
		
		ScoringLabel = makeLabel(570,385,160,20,WiskOpdr.rb.getString("score"),true);
		checkTotaalLabel = makeLabel(620,385,160,20,WiskOpdr.rb.getString("checkTotaalLabel"),false);
		checkTotaalLabel.setForeground(Color.red);
		
		maxScorePV = makeTextField(710,385,30,20,"0",false);
		feedbackPV = makeTextField(710,385,30,20,"0",false);
		
		 
		setFeedbackOption(false);
		
		
		
		
	}
	
	public void maakKeuzeVelden()
    {
       
            /*if(keuzeLabels[i]==null)
            {   keuzeLabels[i] = new JLabel("Nr "+(i+1));
                keuzeLabels[i].setBounds(10,80+i*85,40,80);
                add(keuzeLabels[i]);
            }
        	if(selectableCheckboxes[i]==null)
            {   selectableCheckboxes[i] = new JCheckBox();//("Nr "+(i+1));
                selectableCheckboxes[i].setOpaque(false);
                selectableCheckboxes[i].setBounds(10,80+i*85,20,80);
                add(selectableCheckboxes[i],0);
            }*/
			keuzeVeldenPanel.setBounds(0,80,200,aantalKeuzes*85);
			for(int i=0 ; i<aantalKeuzes ; i++)
            {
	        	if(keuzeVelden[i]==null)
	            {   keuzeVelden[i] = new TekstEditor();
	            	keuzeVelden[i].setBounds(10,i*85,190,80);
	                keuzeVeldenPanel.add(keuzeVelden[i],0);
	            }
            }
        	
			keuzeVeldenPanel.setPreferredSize(new Dimension(200,aantalKeuzes*85));
			keuzeVeldenPanel.scrollRectToVisible(new Rectangle(0,0, 10, 100));
			keuzeVeldenPanel.revalidate();
			keuzeVeldenPanel.doLayout();
        	
        	
        	
//            if(keuzeVelden[i]==null)
//            {   keuzeVelden[i] = new TekstEditor();
//            	
//                keuzeVelden[i].setBounds(30,80+i*85,190,80);
//                add(keuzeVelden[i],0);
//            }
//        }   
        repaint();
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
	
	private Hashtable fillAnswerModel(Hashtable h)
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
		
		return h;
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
		answerModels[answerModelNr] = fillAnswerModel(new Hashtable());
	}
	
	private void setAnswerModel()
	{	if(answerModels==null)return;
		setAnswerModel(answerModels[answerModelNr]);	
	}
	
			
	public void setEditState(Hashtable interactiePanelLaunchState)
	{			
				String[] keuzeMogelijkheden = null;	
				String antwoordString = "$f@";
				String startString = "$f@";
				int scoreMax = 10;
				Hashtable[] answerModels = null;
				boolean hasFeedback = false;
				boolean check = true;
				boolean teltMee = true;
				boolean logOption = false;
				String logID = "";
				boolean[][] logObjectives = null;
				boolean checkExternal = false;
				
				
				if(interactiePanelLaunchState.containsKey("keuzeMogelijkheden")) keuzeMogelijkheden = (String[])interactiePanelLaunchState.get("keuzeMogelijkheden");
                if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
				if(interactiePanelLaunchState.containsKey("startString")) startString = (String)interactiePanelLaunchState.get("startString");
				if(interactiePanelLaunchState.containsKey("scoreMax")) scoreMax = ((Integer)interactiePanelLaunchState.get("scoreMax")).intValue();
				if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
				if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
				if(interactiePanelLaunchState.containsKey("check")) check = ((Boolean)interactiePanelLaunchState.get("check")).booleanValue();
				if(interactiePanelLaunchState.containsKey("teltMee")) teltMee = ((Boolean)interactiePanelLaunchState.get("teltMee")).booleanValue();
				if(interactiePanelLaunchState.containsKey("logOption")) logOption = ((Boolean)interactiePanelLaunchState.get("logOption")).booleanValue();
				if(interactiePanelLaunchState.containsKey("logID")) logID = (String)interactiePanelLaunchState.get("logID");
				if(interactiePanelLaunchState.containsKey("logObjectives")) logObjectives = (boolean[][])interactiePanelLaunchState.get("logObjectives");
				if(interactiePanelLaunchState.containsKey("checkExternal")) checkExternal = ((Boolean)interactiePanelLaunchState.get("checkExternal")).booleanValue();
				
				aantalKeuzes = keuzeMogelijkheden.length;
			    aantalKeuzesTF.setText(""+aantalKeuzes);
			    maxScorePV.setText(""+scoreMax);
			     
			    
//			    for(int i=0 ; i<aantalKeuzes ; i++)
//			    {   
//			    	/*selectableCheckboxes[i] = new JCheckBox();//("Nr "+(i+1));
//		            selectableCheckboxes[i].setOpaque(false);
//		            selectableCheckboxes[i].setBounds(10,80+i*85,20,80);
//		            add(selectableCheckboxes[i],0);*/
//		           
//			    	
//			    	keuzeVelden[i] = new TekstEditor();
//			        keuzeVelden[i].zetTekst(keuzeMogelijkheden[i]);
//			        keuzeVelden[i].layoutTekst();
//			        keuzeVelden[i].setBounds(30,80+i*85,170,80);
//			        add(keuzeVelden[i],0);
//			    }
			    
			    maakKeuzeVelden();
				
			    this.answerModels = new Hashtable[answerModels.length];
				for(int i=0 ; i<answerModels.length ; i++)
				{	this.answerModels[i] = answerModels[i];
				}
					
					//this.answerModels = answerModels;
				
				if(hasFeedback)
				{	aantalAnswerModels = answerModels.length;
					remove(tabbladTab);
					tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 500,184);
					tabbladTab.setTab(true);
					tabbladTab.setScoresVisible(false);
					tabbladTab.setSize(tabbladTab.getSize().width, 23);
					tabbladTab.addActionListener(this);
					tabbladTab.setBackground(new Color(210,210,210));
					tabbladTab.setSelected(answerModelNr+1);
					add(tabbladTab,0);
					aantalTabsKnop.setLocation(500+25*aantalAnswerModels+5 ,184);
					
					answerModelNr = 0;
					setAnswerModel();
				}
				
				antwoordvak.zetTekst(antwoordString);
				antwoordvak.layoutTekst();
				maxScorePV.setText(""+scoreMax);
				
				checkCB.setSelected(check);
                teltMeeCB.setSelected(teltMee);
                logCB.setSelected(logOption);
                    
                logIDField.setVisible(logOption);
                //logObjectivesButton.setVisible(logOption);
                logIDField.setText(logID);
                logObjectivesButton.setChoices(logObjectives);
                
				setFeedbackOption(hasFeedback);
				feedbackCB.setSelected(hasFeedback);
				//if(hasFeedback)return;
				
				checkExternalCB.setSelected(checkExternal);
			
		
	}
	
	public Hashtable getEditState()
	{	
		Hashtable interactiePanelLaunchState = new Hashtable();
        
		    // boolean randomizePositions = false;
		    String[] keuzeMogelijkheden = null;
	        String antwoordString = null;
			int scoreMax = 0;
			int[][] scoreMaxObjectives = null;
			Hashtable[] answerModels;
			boolean hasFeedback;
			boolean check = true;
			boolean teltMee = true;
			boolean logOption = false;
			String logID = "";
			boolean[][] logObjectives = null;
			boolean checkExternal = false;
			
			keuzeMogelijkheden = new String[aantalKeuzes];
			for(int i=0 ; i<aantalKeuzes ; i++)
	        {   keuzeMogelijkheden[i] = keuzeVelden[i].getCompleteText();
	        }
			getAnswerModel();
			answerModels = this.answerModels;
			if(answerModels!=null)setAnswerModel(answerModels[0]);
			
			antwoordString = antwoordvak.getCompleteText();
			scoreMax = Integer.parseInt(maxScorePV.getText());
			hasFeedback = this.hasFeedback;
			if(hasFeedback)scoreMax = puntenFeedback;
			
			check = checkCB.isSelected();
			teltMee = teltMeeCB.isSelected();
			logOption = logCB.isSelected();
			logID = logIDField.getText();	
			logObjectives = logObjectivesButton.getChoices();
			
			if(logObjectives!=null)
			{	scoreMaxObjectives = new int[logObjectives.length][];
				for(int j=0 ; j<scoreMaxObjectives.length; j++)
				{	scoreMaxObjectives[j] = new int[logObjectives[j].length];
					for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)
					{	if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
					}
				}
			}
			
			checkExternal = checkExternalCB.isSelected();
			
			interactiePanelLaunchState.put("keuzeMogelijkheden",keuzeMogelijkheden);
			interactiePanelLaunchState.put("antwoordString",antwoordString);
			interactiePanelLaunchState.put("scoreMax",new Integer(scoreMax));
			if(answerModels!=null)interactiePanelLaunchState.put("answerModels",answerModels);
			interactiePanelLaunchState.put("hasFeedback",new Boolean(hasFeedback));
			interactiePanelLaunchState.put("check",new Boolean(check));
			interactiePanelLaunchState.put("teltMee",new Boolean(teltMee));
			interactiePanelLaunchState.put("logOption",new Boolean(logOption));
			interactiePanelLaunchState.put("logID",logID);
			if(logObjectives!=null)
	        {	interactiePanelLaunchState.put("logObjectives",logObjectives);
	        	interactiePanelLaunchState.put("scoreMaxObjectives",scoreMaxObjectives);
	        }
			interactiePanelLaunchState.put("checkExternal",checkExternal);
			
		
		
		
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
    
	public void focusLost(FocusEvent e)
	{
		if(e.getSource()==aantalKeuzesTF)
        {
            aantalKeuzes = Math.min(20, Integer.parseInt(aantalKeuzesTF.getText()));
            maakKeuzeVelden();
        }
	}
	
	public void focusGained(FocusEvent e)
	{
		
	}
	
	public void actionPerformed(ActionEvent e)
	{	
	    if(e.getSource()==aantalKeuzesTF)
        {
            aantalKeuzes = Math.min(20, Integer.parseInt(aantalKeuzesTF.getText()));
            maakKeuzeVelden();
        }
	    if(e.getSource() == tabbladTab)
		{	int nr = Integer.parseInt(e.getActionCommand())-1;
			if(answerModelNr != nr) 
			{
				getAnswerModel();
				answerModelNr = nr;
				setAnswerModel();
				tabPositieKnop.setLocation(496+25*answerModelNr+5 ,162);
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
				tabPositieKnop.setLocation(496+25*answerModelNr+5 ,162);
			}
			if(e.getActionCommand().equals("min") && answerModelNr>0) 
			{	resAnswerModel = new Hashtable();
				fillAnswerModel(resAnswerModel);
				answerModels[answerModelNr] = answerModels[answerModelNr-1];
				answerModels[answerModelNr-1] = resAnswerModel;
				answerModelNr--;
				tabbladTab.setSelected(answerModelNr+1);
				tabPositieKnop.setLocation(496+25*answerModelNr+5 ,162);
			}
			
		}
		else if(e.getSource() == aantalTabsKnop)
		{	if(e.getActionCommand().equals("min") && aantalAnswerModels>1)
			{	//remove(opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1]);
				//opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1] = null;
				aantalAnswerModels--;
				if(answerModelNr>aantalAnswerModels-1) answerModelNr--;
				setAnswerModel();
				aantalTabsKnop.setLocation(500+25*aantalAnswerModels+5 ,184);
				remove(tabbladTab);
				tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 500,184);
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
				aantalTabsKnop.setLocation(500+25*aantalAnswerModels+5 ,184);
				remove(tabbladTab);
				tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 500,184);
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
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());   
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
		maxScorePV.setVisible(!b);
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

