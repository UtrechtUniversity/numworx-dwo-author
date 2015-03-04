package fi.wiskopdr.scheikundeobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.base64code.*;
import fi.beans.ideas.*;
import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.ObjectiveChoiceButton;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.opdrnav.*;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;


public class ReactieVergelijkingVakEditPanel extends JLayeredPane implements InteractieEditPanel, ActionListener,  TabletOwner //, MouseListener
{
    private FormuleEditor antwoordvak;//,vormEditor;
    private JLabel antwoordLabel, feedbackLabel;
  //  private JCheckBox  gelijkwaardigCB, vormCB, exactCB, eindOplossingCB;
    //private JLabel ScoringLabel, puntenLabel;
    private JLabel correctScoreLabel, aftrekVereenvoudigbaarLabel, aftrekPijlLabel, lossePuntenLabel, beginstoffenLabel, productenLabel, elementenLabel, ladingenLabel;
    private JTextField correctPV, beginstoffenPV, productenPV, elementenPV, ladingenPV, vereenvoudigbaarPV, onjuistePijlPV, feedbackPV;
        
    
    private int puntenCorrect = 10;
	private int puntenBeginstoffen = 0;
	private int puntenProducten = 0;
	private int puntenElementen = 0;
	private int puntenLadingen = 0;
	private int aftrekVereenvoudigbaar = 0;
	private int aftrekOnjuistePijl = 0;
    private int puntenFeedback = 0;
    
//    private boolean gelijkwaardig = true;
//    private boolean vorm;
//    private boolean eindOplossingNodig = true;
//    private boolean exact;
//    private boolean	significant;
//    
//	static boolean	significantieAan=false;
    
    //private AntwoordEditPanel antwoordEditPanel;
    private TekstEditor feedbackEditor;
    private JCheckBox feedbackSizeCB;
    
    
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
        
    private JCheckBox checkCB;
    private JCheckBox teltMeeCB;
    
    private JCheckBox logCB;
	private JTextField logIDField;
	private ObjectiveChoiceButton logObjectivesButton;
    
    private double eqTestValueMin = 0;
	private double eqTestValueMax = 5;
	
	private DialogFacade vormEditorPopupFrame;
	private DialogFacade feedbackEditorPopupFrame;
	
	private JCheckBox boxMetRandCB;
	
	
	public ReactieVergelijkingVakEditPanel(int soort)
    {   setLayout(null);
        super.setSize(770,520); //voor dwo
        setBackground(Color.white); 
        setOpaque(true);
        
        antwoordLabel = makeLabel(5,160,770,20,WiskOpdr.rb.getString("antwoordLabel"),true);
                
        antwoordvak = new FormuleEditor(true);
        antwoordvak.zetReactieVergelijkingMode();
        antwoordvak.setBounds(5,180,770,150);
        antwoordvak.setFont(font);
        antwoordvak.addActionListener(this);
        add(antwoordvak);
       
        feedbackCB = makeCheckBox(145,160,80,20,WiskOpdr.rb.getString("feedbackCBLabel"),false,false);
        
        String[] items = {WiskOpdr.rb.getString("goedLabel"),WiskOpdr.rb.getString("doorLabel"),WiskOpdr.rb.getString("halfLabel"),WiskOpdr.rb.getString("foutLabel")};
        goedFoutIP = new ActKeuzePanel(items,440,420,80,80);
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
        
        feedbackLabel = makeLabel(20,330,320,20,"feedback",true);
        
        feedbackEditor = new TekstEditor(false,true,true);
        feedbackEditor.setBounds(5,350,300,160);
        feedbackEditor.setFont(font);
        feedbackEditor.addActionListener(this);
        feedbackEditor.setBackground(new Color(255,255,200));
        add(feedbackEditor,0);
        
        feedbackSizeCB = makeCheckBox(80,330,180,20,WiskOpdr.rb.getString("feedbackSizeCBLabel"),false,true);
		        
//        vormEditor = new FormuleEditor(true);
//        vormEditor.setResizable(true);
//        vormEditor.setMultiLine(true);
//        vormEditor.setScrollHorizontal(true);
//        vormEditor.setBounds(515,350,260,160);
//        vormEditor.setFont(font);
//        vormEditor.addActionListener(this);
//        vormEditor.setVisible(false);
//        add(vormEditor,0);
        //vormEditor.setResizable(true);
        
        correctScoreLabel = makeLabel(250, 360, 200, 20, WiskOpdr.rb.getString("rvScoreCorrect"), true);
        aftrekVereenvoudigbaarLabel = makeLabel(250, 385, 200, 20, WiskOpdr.rb.getString("rvAftrekVereenvoudigbaar"), true);
        aftrekPijlLabel = makeLabel(500, 385, 200, 20, WiskOpdr.rb.getString("rvAftrekPijl"), true);
        lossePuntenLabel = makeLabel(250, 410, 400, 20, WiskOpdr.rb.getString("rvLossePunten"), true);
        beginstoffenLabel = makeLabel(250, 435, 200, 20, WiskOpdr.rb.getString("rvScoreBeginstoffen"), true);
        productenLabel = makeLabel(500, 435, 200, 20, WiskOpdr.rb.getString("rvScoreProducten"), true);
        elementenLabel = makeLabel(250, 460, 200, 20, WiskOpdr.rb.getString("rvScoreElementen"), true);
        ladingenLabel = makeLabel(250, 485, 200, 20, WiskOpdr.rb.getString("rvScoreLadingen"), true);
        
//        ScoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
//        puntenLabel = makeLabel(460,385,40,20,WiskOpdr.rb.getString("puntenLabel"),true);
//        checkTotaalLabel = makeLabel(620,385,160,20,WiskOpdr.rb.getString("checkTotaalLabel"),false);
//        checkTotaalLabel.setForeground(Color.red);
        
//        gelijkwaardigCB = makeCheckBox(320,410,120,20,WiskOpdr.rb.getString("gelijkwaardigCBLabel"),true,true);
//        gelijkwaardigCB.addMouseListener(this);
//        vormCB = makeCheckBox(320,435,120,20,WiskOpdr.rb.getString("vormCBLabel"),false,true);
//        exactCB = makeCheckBox(320,significantieAan?510:485,120,20,WiskOpdr.rb.getString("exactCBLabel"),false,true);
//        significantCB = makeCheckBox(320,485,120,20,WiskOpdr.rb.getString("significantCBLabel"),false,significantieAan?true:false);
//		eindOplossingCB = makeCheckBox(320,460,120,20,WiskOpdr.rb.getString("eindOplossingCBLabel"),true,true);
       
        
        formuleToolBijFocusCB = makeCheckBox(600,40,200,20,WiskOpdr.rb.getString("formuleToolCBLabel"),false,true);
		checkCB = makeCheckBox(5,5,200,20,WiskOpdr.rb.getString("checkCBLabel"),true,true);
        teltMeeCB = makeCheckBox(225,5,200,20,WiskOpdr.rb.getString("teltMeeCBLabel"),true,true);
        logCB = makeCheckBox(450,5,70,20,WiskOpdr.rb.getString("logCBLabel"),false,true);
        logIDField = makeTextField(520,5,60,20,"0",false);
        boxMetRandCB = makeCheckBox(500,155,80,20,WiskOpdr.rb.getString("boxMetRand"),true,true);
        
        logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        logObjectivesButton.setBounds(600,5,120,20);
        if(WiskOpdr.objectives!=null)add(logObjectivesButton);
        
        correctPV = makeTextField(440,360,30,20,""+puntenCorrect,true);
        vereenvoudigbaarPV = makeTextField(440,385,30,20,""+aftrekVereenvoudigbaar,true);
        onjuistePijlPV = makeTextField(650, 385, 30, 20, "" + aftrekOnjuistePijl, true);
        beginstoffenPV = makeTextField(440,435,30,20,""+ puntenBeginstoffen, true);
        productenPV = makeTextField(650, 435, 30, 20, "" + puntenProducten, true);
        elementenPV = makeTextField(440,460,30,20,""+puntenElementen,true);
        ladingenPV = makeTextField(440,485,30,20,""+puntenLadingen,true);
		feedbackPV = makeTextField(440,385,30,20,""+puntenFeedback,true);
        
        
        
        setFeedbackOption(false);
        
        if(soort==1)
        {   formuleToolBijFocusCB.setVisible(false);
        }
        else if(soort==3)
        {   formuleToolBijFocusCB.setVisible(true);
        }
        
        	
    }
    
    public JCheckBox makeCheckBox(int x, int y, int b, int h, String text, boolean selected, boolean visible)
    {   JCheckBox checkbox = new JCheckBox(text);
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
    {   JLabel label = new JLabel(text);
        label.setBounds(x,y,b,h);
        label.setFont(font);
        label.setVisible(visible);
        add(label,0);
        return label;
    }
    
    public JTextField makeTextField(int x, int y, int b, int h, String text, boolean visible)
    {   JTextField textField = new JTextField(text);
        textField.setBounds(x,y,b,h);
        textField.setFont(font);
        textField.addActionListener(this);
        textField.setVisible(visible);
        add(textField,0);
        return textField;
    }
    
    public void zetBreedte(int b)
    {   //grafiekPanel.setSize(b,grafiekPanel.getSize().height);
    }
    public void zetHoogte(int h)
    {   //grafiekPanel.setSize(grafiekPanel.getSize().width, h);
    }
    
    private Hashtable fillAnswerModel(Hashtable h)
    {
        String antwoordString = "$f@";
//        boolean gelijkwaardig = true;
//        boolean vorm = false;
//        boolean eindOplossingNodig = true;
//        boolean exact = false;
//        boolean significant = false;
        int puntenFeedback = 0;
        String feedback = "";
        int feedbackWidth = 200;
		int feedbackHeight = 20;
        String vormString = "$f@";
        int goedHalfFout = 3;
        
        antwoordString = antwoordvak.geefFormuleVak().toString();
//        gelijkwaardig = this.gelijkwaardig;
//        vorm = this.vorm;
//        eindOplossingNodig = this.eindOplossingNodig;
//        exact = this.exact;
//        significant = this.significant;
		
                
        puntenFeedback = (Integer.parseInt(feedbackPV.getText()));
        feedback  = feedbackEditor.getText();
        if(feedbackEditorPopupFrame!=null)
		{	feedbackWidth = feedbackEditorPopupFrame.getSize().width - feedbackEditorPopupFrame.getInsets().left - feedbackEditorPopupFrame.getInsets().right;
			feedbackHeight = feedbackEditorPopupFrame.getSize().height - feedbackEditorPopupFrame.getInsets().top - feedbackEditorPopupFrame.getInsets().bottom;
		}
        //vormString = vormEditor.geefFormuleVak().toString();
//        String[] vormStrings = vormEditor.geefRegels();
//        if(vormStrings.length==1) vormString = vormEditor.geefFormuleVak().toString();
//        else
//        {	vormString = "$f";
//        	for(int i=0 ; i<vormStrings.length ; i++)
//        	{	vormString = vormString + vormStrings[i].substring(2,vormStrings[i].length()-1) + "::";
//        	}
//        	vormString = vormString.substring(0,vormString.length()-2) + "@";
//        }
        goedHalfFout = goedFoutIP.geefKeuze()-1;
        
        h.put("antwoordString",antwoordString);
//        h.put("gelijkwaardig",new Boolean(gelijkwaardig));
//        h.put("vorm",new Boolean(vorm));
//        h.put("eindOplossingNodig",new Boolean(eindOplossingNodig));
//        h.put("exact",new Boolean(exact));
//        h.put("significant",new Boolean(significant));
        h.put("puntenFeedback",new Integer(puntenFeedback));
        h.put("feedback",feedback);
        h.put("feedbackWidth",new Integer(feedbackWidth));
		h.put("feedbackHeight",new Integer(feedbackHeight));
		//h.put("vormString",vormString);
        h.put("goedHalfFout",new Integer(goedHalfFout));
        
        return h;
    }
    
    private void setAnswerModel(Hashtable h)
    {   String antwoordString = "$f@";
        boolean gelijkwaardig = true;
        boolean vorm = false;
        boolean eindOplossingNodig = true;
        boolean exact = false;
        boolean significant = false;
		int puntenFeedback = 0;
        String feedback = "";
        int feedbackWidth = 0;
        int feedbackHeight = 0;
		//String vormString = "$f@";
        int goedHalfFout = 3;
        
        if(h!=null) 
        {   if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
//            if(h.containsKey("gelijkwaardig")) gelijkwaardig = ((Boolean)h.get("gelijkwaardig")).booleanValue();
//            if(h.containsKey("vorm")) vorm = ((Boolean)h.get("vorm")).booleanValue();
//            if(h.containsKey("eindOplossingNodig")) eindOplossingNodig = ((Boolean)h.get("eindOplossingNodig")).booleanValue();
//            if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
//            if(h.containsKey("significant")) significant = ((Boolean)h.get("significant")).booleanValue();
			if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
            if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
            if(h.containsKey("feedbackWidth")) feedbackWidth = ((Integer)h.get("feedbackWidth")).intValue();
			if(h.containsKey("feedbackHeight")) feedbackHeight = ((Integer)h.get("feedbackHeight")).intValue();
           // if(h.containsKey("vormString")) vormString = (String)h.get("vormString");
            if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
            
        }
//        this.vorm = vorm;
//        this.gelijkwaardig = gelijkwaardig;
//        this.eindOplossingNodig = eindOplossingNodig;
//        this.exact = exact;
//        this.significant = significant;
		this.puntenFeedback = puntenFeedback;
        
        antwoordvak.geefFormuleVak().vulVak(antwoordString);
        
//        String[] vormStrings = StringUtils.split(vormString, "::");
//        for(int i=0 ; i<vormStrings.length ; i++)
//    	{	if(i==0) vormStrings[i] = vormStrings[i] + "@";
//    		else if(i==vormStrings.length-1) vormStrings[i] = "$f" + vormStrings[i];
//    		else  vormStrings[i] = "$f" + vormStrings[i] + "@";
//    	}
        //vormEditor.verwijderRegels();
        //vormEditor.zetRegels(vormStrings);
        
        //vormEditor.geefFormuleVak().vulVak(vormString);
            
       
        
       // vormEditor.setVisible(vorm);
        
        feedbackPV.setVisible(hasFeedback);
        feedbackPV.setText(""+puntenFeedback);
        
        feedbackEditor.zetTekst(feedback);
        feedbackEditor.setEnlargedWidth(feedbackWidth);
		feedbackEditor.setEnlargedHeight(feedbackHeight);
		if(feedbackEditorPopupFrame!=null)
		{	feedbackEditor.setEnlargedSize();
			int width = feedbackWidth + feedbackEditorPopupFrame.getInsets().left + feedbackEditorPopupFrame.getInsets().right;
        	int height = feedbackHeight + feedbackEditorPopupFrame.getInsets().top + feedbackEditorPopupFrame.getInsets().bottom;
        	feedbackEditorPopupFrame.setSize(width,height);
		}
        feedbackEditor.repaint();
        
        goedFoutIP.setItem(goedHalfFout);
        
        
    }
    
    private void getAnswerModel()
    {   if(answerModels==null)return;
       	answerModels[answerModelNr] = fillAnswerModel(new Hashtable());
    }
    
    private void setAnswerModel()
    {   if(answerModels==null)return;
        setAnswerModel(answerModels[answerModelNr]);    
    }
    
            
    public void setEditState(Hashtable interactiePanelLaunchState)
    {           String antwoordString = "$f@";
                String startString = "$f@";
                int puntenCorrect = 10;
            	int puntenBeginstoffen = 0;
            	int puntenProducten = 0;
            	int puntenElementen = 0;
            	int puntenLadingen = 0;
            	int aftrekVereenvoudigbaar = 0;
            	int aftrekOnjuistePijl = 0;
                boolean formuleToolBijFocus = false;
                Hashtable[] answerModels = null;
                boolean hasFeedback = false;
                boolean feedbackSize = false;
               // String vormString = "$f@";
                String strategieDomein = "";
                int feedbackModus = 0;
                String[] antwoordSubStrings = null;
                boolean check = true;
                boolean teltMee = true;
                boolean logOption = false;
				String logID = "";
				boolean[][] logObjectives = null;
				Hashtable changedTexts = new Hashtable();
                double eqTestValueMin = 0;
				double eqTestValueMax = 5;
				boolean uitw = false;
				boolean casAntw = false;
				boolean boxMetRand = true;
				
                if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
                if(interactiePanelLaunchState.containsKey("startString")) startString = (String)interactiePanelLaunchState.get("startString");
                if(interactiePanelLaunchState.containsKey("puntenCorrect")) puntenCorrect = ((Integer)interactiePanelLaunchState.get("puntenCorrect")).intValue();
                if(interactiePanelLaunchState.containsKey("puntenBeginstoffen")) puntenBeginstoffen = ((Integer)interactiePanelLaunchState.get("puntenBeginstoffen")).intValue();
                if(interactiePanelLaunchState.containsKey("puntenProducten")) puntenProducten = ((Integer)interactiePanelLaunchState.get("puntenProducten")).intValue();
                if(interactiePanelLaunchState.containsKey("puntenElementen")) puntenElementen = ((Integer)interactiePanelLaunchState.get("puntenElementen")).intValue();
                if(interactiePanelLaunchState.containsKey("puntenLadingen")) puntenLadingen = ((Integer)interactiePanelLaunchState.get("puntenLadingen")).intValue();
                if(interactiePanelLaunchState.containsKey("aftrekVereenvoudigbaar")) aftrekVereenvoudigbaar = ((Integer)interactiePanelLaunchState.get("aftrekVereenvoudigbaar")).intValue();
				if(interactiePanelLaunchState.containsKey("aftrekOnjuistePijl")) aftrekOnjuistePijl = ((Integer)interactiePanelLaunchState.get("aftrekOnjuistePijl")).intValue();
                if(interactiePanelLaunchState.containsKey("formuleToolBijFocus")) formuleToolBijFocus = ((Boolean)interactiePanelLaunchState.get("formuleToolBijFocus")).booleanValue();
                if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
                if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
                if(interactiePanelLaunchState.containsKey("feedbackSize")) feedbackSize = ((Boolean)interactiePanelLaunchState.get("feedbackSize")).booleanValue();
				//if(interactiePanelLaunchState.containsKey("vormString")) vormString = (String)interactiePanelLaunchState.get("vormString");
                if(interactiePanelLaunchState.containsKey("antwoordSubStrings")) antwoordSubStrings = (String[])interactiePanelLaunchState.get("antwoordSubStrings");
                if(interactiePanelLaunchState.containsKey("check")) check = ((Boolean)interactiePanelLaunchState.get("check")).booleanValue();
                if(interactiePanelLaunchState.containsKey("teltMee")) teltMee = ((Boolean)interactiePanelLaunchState.get("teltMee")).booleanValue();
                
                if(interactiePanelLaunchState.containsKey("logOption")) logOption = ((Boolean)interactiePanelLaunchState.get("logOption")).booleanValue();
                if(interactiePanelLaunchState.containsKey("logID")) logID = (String)interactiePanelLaunchState.get("logID");
				if(interactiePanelLaunchState.containsKey("eqTestValueMin")) eqTestValueMin = ((Double)interactiePanelLaunchState.get("eqTestValueMin")).doubleValue();
				if(interactiePanelLaunchState.containsKey("eqTestValueMax")) eqTestValueMax = ((Double)interactiePanelLaunchState.get("eqTestValueMax")).doubleValue();
				if(interactiePanelLaunchState.containsKey("uitw")) uitw = ((Boolean)interactiePanelLaunchState.get("uitw")).booleanValue();
				if(interactiePanelLaunchState.containsKey("casAntw")) casAntw = ((Boolean)interactiePanelLaunchState.get("casAntw")).booleanValue();
				if(interactiePanelLaunchState.containsKey("boxMetRand")) boxMetRand = ((Boolean)interactiePanelLaunchState.get("boxMetRand")).booleanValue();
				if(interactiePanelLaunchState.containsKey("logObjectives")) logObjectives = (boolean[][])interactiePanelLaunchState.get("logObjectives");
				
                this.puntenCorrect = puntenCorrect;
                this.puntenBeginstoffen = puntenBeginstoffen;
                this.puntenProducten = puntenProducten;
                this.puntenElementen = puntenElementen;
                this.puntenLadingen = puntenLadingen;
                this.aftrekVereenvoudigbaar = aftrekVereenvoudigbaar;
                this.aftrekOnjuistePijl = aftrekOnjuistePijl;
				this.formuleToolBijFocus = formuleToolBijFocus;
                
                this.answerModels = new Hashtable[answerModels.length];
				for(int i=0 ; i<answerModels.length ; i++)
				{	this.answerModels[i] = answerModels[i];
				}
					
					//this.answerModels = answerModels;
                this.eqTestValueMin = eqTestValueMin;
				this.eqTestValueMax = eqTestValueMax;
				this.hasFeedback = hasFeedback;
                
                
                
                //feedbackModusKeuze.setVisible(tips);
                //feedbackModusKeuze.setSelectedIndex(feedbackModus);
                
                if(hasFeedback)
                {   aantalAnswerModels = answerModels.length;
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
               
//                String[] vormStrings = StringUtils.split(vormString, "::");
//                for(int i=0 ; i<vormStrings.length ; i++)
//            	{	if(i==0) vormStrings[i] = vormStrings[i] + "@";
//            		else if(i==vormStrings.length-1) vormStrings[i] = "$f" + vormStrings[i];
//            		else  vormStrings[i] = "$f" + vormStrings[i] + "@";
//            	}
//                vormEditor.zetRegels(vormStrings);
//                //vormEditor.geefFormuleVak().vulVak(vormString);
                
                formuleToolBijFocusCB.setSelected(formuleToolBijFocus);
                checkCB.setSelected(check);
                teltMeeCB.setSelected(teltMee);
                
                logCB.setSelected(logOption);
                logIDField.setVisible(logOption);
	            //logObjectivesButton.setVisible(logOption);
	            logIDField.setText(logID);
	            logObjectivesButton.setChoices(logObjectives);
                
                boxMetRandCB.setSelected(boxMetRand);
	            //startLabel.setVisible(uitw);
				//startEditor.setVisible(uitw);
                
                setFeedbackOption(hasFeedback);
                feedbackCB.setSelected(hasFeedback);
                feedbackSizeCB.setSelected(feedbackSize);
				feedbackEditor.setResizable(feedbackSize);
				
                if(hasFeedback)return;
                
                correctPV.setText(""+puntenCorrect);
                vereenvoudigbaarPV.setText(""+aftrekVereenvoudigbaar);
                onjuistePijlPV.setText("" + aftrekOnjuistePijl);
                beginstoffenPV.setText(""+puntenBeginstoffen);
                productenPV.setText("" + puntenProducten);
                elementenPV.setText(""+puntenElementen);
                ladingenPV.setText(""+puntenLadingen);
                    
    }
    
    public Hashtable getEditState()
    {   
        Hashtable interactiePanelLaunchState = new Hashtable();
        
        
            String antwoordString = null;
            int puntenCorrect = 10;
        	int puntenBeginstoffen = 0;
        	int puntenProducten = 0;
        	int puntenElementen = 0;
        	int puntenLadingen = 0;
        	int aftrekVereenvoudigbaar = 0;
        	int aftrekOnjuistePijl = 0;
            int scoreMax = 0;
            int[][] scoreMaxObjectives = null;
            boolean formuleToolBijFocus = false;
            Hashtable[] answerModels;
            boolean hasFeedback;
            boolean feedbackSize;
            //String vormString = "$f@";
            int feedbackModus = 0;
            boolean check = true;
            boolean teltMee = true;
            boolean logOption = false;
			String logID = "";
			boolean[][] logObjectives = null;
            double eqTestValueMin = 0;
			double eqTestValueMax = 5;
			boolean uitw = false;
			boolean boxMetRand = true;
			
            getAnswerModel();
            answerModels = this.answerModels;
            if(answerModels!=null)setAnswerModel(answerModels[0]);
            
            antwoordString = antwoordvak.geefFormuleVak().toString();
//            String[] vormStrings = vormEditor.geefRegels();
//            if(vormStrings.length==1) vormString = vormEditor.geefFormuleVak().toString();
//            else
//            {	vormString = "$f";
//            	for(int i=0 ; i<vormStrings.length ; i++)
//            	{	vormString = vormString + vormStrings[i].substring(2,vormStrings[i].length()-1) + "::";
//            	}
//            	vormString = vormString.substring(0,vormString.length()-2) + "@";
//            }
            
            try
            {   puntenCorrect = Integer.parseInt(correctPV.getText());
                this.puntenCorrect = puntenCorrect;
            }   
            catch(Exception ex)
            {   }
            try
            {   aftrekVereenvoudigbaar = Integer.parseInt(vereenvoudigbaarPV.getText());
                this.aftrekVereenvoudigbaar = aftrekVereenvoudigbaar;
            }   
            catch(Exception ex)
            {   }
            try
            {
            	aftrekOnjuistePijl = Integer.parseInt(onjuistePijlPV.getText());
            	this.aftrekOnjuistePijl = aftrekOnjuistePijl;
            }
            catch(Exception ex)
            {   }
            try
            {   puntenBeginstoffen = Integer.parseInt(beginstoffenPV.getText());
                this.puntenBeginstoffen = puntenBeginstoffen;
            }   
            catch(Exception ex)
            {   }
            try
            {   puntenProducten = Integer.parseInt(productenPV.getText());
                this.puntenProducten = puntenProducten;
            }   
            catch(Exception ex)
            {   }
            try
            {   puntenElementen = Integer.parseInt(elementenPV.getText());
                this.puntenElementen = puntenElementen;
            }   
            catch(Exception ex)
            {   }
            try
			{	puntenLadingen = Integer.parseInt(ladingenPV.getText());
				this.puntenLadingen = puntenLadingen;
			}	
			catch(Exception ex)
			{	}
            puntenCorrect = this.puntenCorrect;
            aftrekVereenvoudigbaar = this.aftrekVereenvoudigbaar;
            aftrekOnjuistePijl = this.aftrekOnjuistePijl;
            puntenBeginstoffen = this.puntenBeginstoffen;
            puntenProducten = this.puntenProducten;
            puntenElementen = this.puntenElementen;
			puntenLadingen = this.puntenLadingen;
            formuleToolBijFocus = this.formuleToolBijFocus;
            scoreMax = puntenCorrect;
            hasFeedback = this.hasFeedback;
            if(hasFeedback)scoreMax = puntenFeedback;
            feedbackSize = feedbackSizeCB.isSelected();
            
            //if(tips) feedbackModus = feedbackModusKeuze.getSelectedIndex();
            
           
            check = checkCB.isSelected();
            teltMee = teltMeeCB.isSelected();
            logOption = logCB.isSelected();
			logID = logIDField.getText();
			logObjectives = logObjectivesButton.getChoices();
			
			boxMetRand = boxMetRandCB.isSelected();
			
			
			if(!teltMee)scoreMax = 0;
			
			if(logObjectives!=null)
			{	scoreMaxObjectives = new int[logObjectives.length][];
				for(int j=0 ; j<scoreMaxObjectives.length; j++)
				{	scoreMaxObjectives[j] = new int[logObjectives[j].length];
					for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)
					{	if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
					}
				}
			}
            
            //changedTexts = this.changedTexts;
            
            eqTestValueMin = this.eqTestValueMin;
			eqTestValueMax = this.eqTestValueMax;
            
            interactiePanelLaunchState.put("antwoordString",antwoordString);
            interactiePanelLaunchState.put("puntenCorrect",new Integer(puntenCorrect));
            interactiePanelLaunchState.put("aftrekVereenvoudigbaar",new Integer(aftrekVereenvoudigbaar));
            interactiePanelLaunchState.put("aftrekOnjuistePijl", new Integer(aftrekOnjuistePijl));
            interactiePanelLaunchState.put("puntenBeginstoffen",new Integer(puntenBeginstoffen));
            interactiePanelLaunchState.put("puntenProducten", new Integer(puntenProducten));
            interactiePanelLaunchState.put("puntenElementen",new Integer(puntenElementen));
			interactiePanelLaunchState.put("puntenLadingen",new Integer(puntenLadingen));
            interactiePanelLaunchState.put("formuleToolBijFocus",new Boolean(formuleToolBijFocus));
            interactiePanelLaunchState.put("scoreMax",new Integer(scoreMax));
            if(answerModels!=null)interactiePanelLaunchState.put("answerModels",answerModels);
            interactiePanelLaunchState.put("hasFeedback",new Boolean(hasFeedback));
            interactiePanelLaunchState.put("feedbackSize",new Boolean(feedbackSize));
			//interactiePanelLaunchState.put("vormString",vormString);
            interactiePanelLaunchState.put("check",new Boolean(check));
            interactiePanelLaunchState.put("teltMee",new Boolean(teltMee));
            interactiePanelLaunchState.put("logOption",new Boolean(logOption));
			interactiePanelLaunchState.put("logID",logID);     
			interactiePanelLaunchState.put("eqTestValueMin",new Double(eqTestValueMin));
			interactiePanelLaunchState.put("eqTestValueMax",new Double(eqTestValueMax));
			interactiePanelLaunchState.put("uitw",new Boolean(uitw));
			interactiePanelLaunchState.put("boxMetRand",new Boolean(boxMetRand));
			if(logObjectives!=null)
	        {	interactiePanelLaunchState.put("logObjectives",logObjectives);
	        	interactiePanelLaunchState.put("scoreMaxObjectives",scoreMaxObjectives);
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
    {   antwoordvak.setNewScrollSize();
    }
    
    
    /*
   
    public void maakStartPopupFrame()
	{
		startEditorPopupFrame = DialogFacade.newInstance(this, "");
		startEditorPopupFrame.getContentPane().setLayout(null);
		startEditorPopupFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{   startEditor.produceAction("verklein");
				startEditor.setEnlarged(false);
			}
		});
		startEditorPopupFrame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e)
			{   int x = 0;
				int y = 0;
				int b = startEditorPopupFrame.getSize().width - startEditorPopupFrame.getInsets().left - startEditorPopupFrame.getInsets().right;
				int h = startEditorPopupFrame.getSize().height - startEditorPopupFrame.getInsets().top - startEditorPopupFrame.getInsets().bottom;
				startEditor.setBounds(x,y,b,h);
			}
		});
	}
    */
    
//    public void maakVormPopupFrame()
//	{
//    	vormEditorPopupFrame = DialogFacade.newInstance(this, "");
//		vormEditorPopupFrame.getContentPane().setLayout(null);
//		vormEditorPopupFrame.addWindowListener(new WindowAdapter(){
//			public void windowClosing(WindowEvent e)
//			{   vormEditor.produceAction("verklein");
//				vormEditor.setEnlarged(false);
//			}
//		});
//		vormEditorPopupFrame.addComponentListener(new ComponentAdapter(){
//			public void componentResized(ComponentEvent e)
//			{   int x = 0;
//				int y = 0;
//				int b = vormEditorPopupFrame.getSize().width - vormEditorPopupFrame.getInsets().left - vormEditorPopupFrame.getInsets().right;
//				int h = vormEditorPopupFrame.getSize().height - vormEditorPopupFrame.getInsets().top - vormEditorPopupFrame.getInsets().bottom;
//				vormEditor.setBounds(x,y,b,h);
//			}
//		});
//	}
    
    public void maakFeedbackEditorPopupFrame()
    {
        feedbackEditorPopupFrame = DialogFacade.newInstance(this, "");
        
        
        feedbackEditorPopupFrame.getContentPane().setLayout(null);
        feedbackEditorPopupFrame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {   feedbackEditor.produceAction("verklein");
                feedbackEditor.setEnlarged(false);
            }
        });
        feedbackEditorPopupFrame.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e)
            {   int x = 0;
                int y = 0;
                //int b = feedbackEditor.getEnlargedWidth() + feedbackEditorPopupFrame.getInsets().left + feedbackEditorPopupFrame.getInsets().right;
                //int h = feedbackEditor.getEnlargedHeight() + feedbackEditorPopupFrame.getInsets().top + feedbackEditorPopupFrame.getInsets().bottom;
                //feedbackEditor.setLocation(0,0);
                //feedbackEditor.setEnlargedSize();
                //feedbackEditorPopupFrame.setSize(b,h);
                
                int b = feedbackEditorPopupFrame.getSize().width - feedbackEditorPopupFrame.getInsets().left - feedbackEditorPopupFrame.getInsets().right;
                int h = feedbackEditorPopupFrame.getSize().height - feedbackEditorPopupFrame.getInsets().top - feedbackEditorPopupFrame.getInsets().bottom;
                feedbackEditor.setEnlargedWidth(b);
                feedbackEditor.setEnlargedHeight(h);
                if(feedbackEditor.isEnlarged())feedbackEditor.setBounds(x,y,b,h);
                
            }
        });
    }
    
    public void actionPerformed(ActionEvent e)
    {   
        if(e.getSource() == tabbladTab)
        {   int nr = Integer.parseInt(e.getActionCommand())-1;
            if(answerModelNr != nr) 
            {
                getAnswerModel();
                answerModelNr = nr;
                setAnswerModel();
                tabPositieKnop.setLocation(246+25*answerModelNr+5 ,162);
            }
            
        }
        else if(e.getSource() == tabPositieKnop)
        {   if(e.getActionCommand().equals("plus") && answerModelNr<aantalAnswerModels-1) 
            {   resAnswerModel = new Hashtable();
                fillAnswerModel(resAnswerModel);
                answerModels[answerModelNr] = answerModels[answerModelNr+1];
                answerModels[answerModelNr+1] = resAnswerModel;
                answerModelNr++;
                tabbladTab.setSelected(answerModelNr+1);
                tabPositieKnop.setLocation(246+25*answerModelNr+5 ,162);
            }
            if(e.getActionCommand().equals("min") && answerModelNr>0) 
            {   resAnswerModel = new Hashtable();
                fillAnswerModel(resAnswerModel);
                answerModels[answerModelNr] = answerModels[answerModelNr-1];
                answerModels[answerModelNr-1] = resAnswerModel;
                answerModelNr--;
                tabbladTab.setSelected(answerModelNr+1);
                tabPositieKnop.setLocation(246+25*answerModelNr+5 ,162);
            }
            
        }
        else if(e.getSource() == aantalTabsKnop)
        {   if(e.getActionCommand().equals("min") && aantalAnswerModels>1)
            {   //remove(opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1]);
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
                {   answerModelsNew[i] = answerModels[i];
                }
                answerModels = answerModelsNew;
                repaint();
                
            }
            if(e.getActionCommand().equals("plus") && aantalAnswerModels<20)
            {   aantalAnswerModels++;
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
                {   answerModelsNew[i] = answerModels[i];
                }
                answerModels = answerModelsNew;
                repaint();
            }
        }
        else if(e.getSource()==feedbackCB)
        {   setFeedbackOption(feedbackCB.isSelected());
            
        }
        else if(e.getSource()==feedbackSizeCB)
		{	feedbackEditor.setResizable(feedbackSizeCB.isSelected());
			
		}
//        else if(e.getSource()==gelijkwaardigCB)
//        {   gelijkwaardig = gelijkwaardigCB.isSelected();
//            if(!hasFeedback && answerModelNr==0)gelijkwaardigPV.setVisible(gelijkwaardig);
//            
//        }
//        else if(e.getSource()==vormCB)
//        {   boolean b = vormCB.isSelected();
//            vorm = b; 
//            if(!hasFeedback && answerModelNr==0)vormPV.setVisible(b);
//            vormEditor.setVisible(b);
//            if(b)
//            {   eindOplossingNodig = false;
//            	eindOplossingCB.setSelected(false);
//            	eindOplossingPV.setText("0");
//            	eindOplossingPV.setVisible(false);
//            	puntenEindOplossing = 0;
//            	
//            	exact = false;
//            	exactCB.setSelected(false);
//            	exactPV.setText("0");
//            	exactPV.setVisible(false);
//            	puntenExact = 0;
//            	
//            	gelijkwaardigPV.setText("0");
//            	puntenGelijkwaardig = 0;
//            	
//            	vormPV.setText("10");
//            	//vormPV.setVisible(true);
//            	puntenVorm = 10;
//                
//            }
//            else
//            {
//            	gelijkwaardigPV.setText("10");
//            	puntenGelijkwaardig = 10;
//            }
//            /*
//            if(b && !eindOplossingNodig)
//            {   gelijkwaardigPV.setText("0");
//                puntenGelijkwaardig = 0;
//                vormPV.setText("10");
//                puntenVorm = 10;
//            }
//            if(!b && !eindOplossingNodig)
//            {   gelijkwaardigPV.setText("10");
//                puntenGelijkwaardig = 10;
//                vormPV.setText("0");
//                puntenVorm = 0;
//            }
//            */
//        }
//        else if(e.getSource()==exactCB)
//        {   boolean b = exactCB.isSelected();
//            exact = b;
//            if(!hasFeedback && answerModelNr==0)exactPV.setVisible(b);
//            
//            if(b)
//            {   eindOplossingNodig = true;
//                eindOplossingCB.setSelected(true);
//                if(!hasFeedback)eindOplossingPV.setVisible(true);
//                
//                vorm = false;
//                vormCB.setSelected(false);
//            	vormPV.setVisible(false);
//            	vormEditor.setVisible(false);
//                
//                gelijkwaardigPV.setText("0");
//                vormPV.setText("0");
//                eindOplossingPV.setText("0");
//                exactPV.setText("10");
//                
//                puntenEindOplossing = 0;
//                puntenGelijkwaardig = 0;
//                puntenExact = 10;
//            }
//            else
//            {   
//                gelijkwaardigPV.setText("0");
//                vormPV.setText("0");
//                eindOplossingPV.setText("10");
//                exactPV.setText("0");
//            
//                puntenGelijkwaardig = 0;
//                puntenVorm = 0;
//                puntenEindOplossing = 10;
//                puntenExact = 0;
//            }   
//        }
//        else if(e.getSource()==significantCB)
//		{
//			boolean b = significantCB.isSelected();
//			significant = b;
//			if(!hasFeedback && answerModelNr==0)significantPV.setVisible(b);
//			if(b)
//	        {   eindOplossingNodig = true;
//	            eindOplossingCB.setSelected(true);
//	            if(!hasFeedback)eindOplossingPV.setVisible(true);
//	            eindOplossingPV.setText("10");
//	            puntenEindOplossing = 10;
//	        }
//		}
//        else if(e.getSource()==eindOplossingCB)
//        {   boolean b = eindOplossingCB.isSelected();
//            eindOplossingNodig = b;
//            if(!hasFeedback && answerModelNr==0)eindOplossingPV.setVisible(b);
//            if(b)
//            {   vorm = false;
//            	vormCB.setSelected(false);
//            	vormPV.setVisible(false);
//            	vormEditor.setVisible(false);
//            	gelijkwaardigPV.setText("0");
//                vormPV.setText("0");
//                eindOplossingPV.setText("10");
//                exactPV.setText("0");
//                
//                puntenGelijkwaardig = 0;
//                puntenVorm = 0;
//                puntenEindOplossing = 10;
//                puntenExact = 0;
//            }
//            else
//            {   exactCB.setSelected(false);
//                exactPV.setVisible(false);
//                
//                significantCB.setSelected(false);
//              	significantPV.setVisible(false);
//                
//                gelijkwaardigPV.setText("10");
//                if(vorm)
//                {   gelijkwaardigPV.setText("0");
//                    vormPV.setText("10");
//                    puntenGelijkwaardig = 0;
//                    puntenVorm = 10;
//                }
//                else
//                {   gelijkwaardigPV.setText("10");
//                    vormPV.setText("0");
//                    puntenGelijkwaardig = 10;
//                    puntenVorm = 0;
//                }
//                eindOplossingPV.setText("0");
//                exactPV.setText("0");
//                significantPV.setText("0");
//                
//                puntenEindOplossing = 0;
//                puntenExact = 0;
//                puntenSignificant = 0;
//            }
//        }
        else if(e.getSource()==formuleToolBijFocusCB)
        {   boolean b = formuleToolBijFocusCB.isSelected();
            formuleToolBijFocus = b;
        }
        else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());   
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
//        else if(e.getSource()==vormEditor)
//		{	if(e.getActionCommand().equals("vergroot"))
//			{	if(vormEditorPopupFrame==null)	maakVormPopupFrame();
//				Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
//				int x = vormEditor.getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + 500));
//				int y = vormEditor.getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + 400));
//				vormEditorPopupFrame.setVisible(true);
//				vormEditorPopupFrame.getContentPane().add(vormEditor);
//				vormEditorPopupFrame.pack();
//				vormEditorPopupFrame.setSize(500,400);
//				vormEditorPopupFrame.setLocation(x,y);
//			}
//			if(e.getActionCommand().equals("verklein"))
//			{	vormEditorPopupFrame.setVisible(false);
//				vormEditor.setBounds(515,350,260,160);
//		        add(vormEditor);
//				vormEditorPopupFrame.dispose();
//			}
//			revalidate();
//            repaint();
//		}
        else if(e.getSource()==feedbackEditor)
        {   if(e.getActionCommand().equals("vergroot"))
            {   if(feedbackEditorPopupFrame==null)  maakFeedbackEditorPopupFrame();
                Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
                int x = feedbackEditor.getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + 500));
                int y = feedbackEditor.getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + 400));
                int eWidth = feedbackEditor.getEnlargedWidth();
                int eHeight = feedbackEditor.getEnlargedHeight();
                eWidth = eWidth==TekstEditor.defaultEnlargedWidth ? 200 : eWidth;
                eHeight = eHeight==TekstEditor.defaultEnlargedHeigth ? 20 : eHeight;
                int b = eWidth + feedbackEditorPopupFrame.getInsets().left + feedbackEditorPopupFrame.getInsets().right;
                int h = eHeight + feedbackEditorPopupFrame.getInsets().top + feedbackEditorPopupFrame.getInsets().bottom;
                
                feedbackEditorPopupFrame.setVisible(true);
                feedbackEditorPopupFrame.getContentPane().add(feedbackEditor);
                feedbackEditorPopupFrame.pack();
                feedbackEditorPopupFrame.setSize(b,h);
                feedbackEditorPopupFrame.setLocation(x,y);
            }
            if(e.getActionCommand().equals("verklein"))
            {	int b = feedbackEditorPopupFrame.getSize().width;// - feedbackEditorPopupFrame.getInsets().left - feedbackEditorPopupFrame.getInsets().right;
                int h = feedbackEditorPopupFrame.getSize().height;// - feedbackEditorPopupFrame.getInsets().top - feedbackEditorPopupFrame.getInsets().bottom;
                feedbackEditor.setEnlargedWidth(b);
                feedbackEditor.setEnlargedHeight(h);
            	feedbackEditorPopupFrame.setVisible(false);
                feedbackEditor.setBounds(5,350,300,160);
                add(feedbackEditor);
                feedbackEditorPopupFrame.dispose();
            }
            revalidate();
            repaint();
        }
        else
        {
            int puntenCorrect = 0;
            int aftrekVereenvoudigbaar = 0;
            int aftrekOnjuistePijl = 0;
            int puntenBeginstoffen = 0;
            int puntenProducten = 0;
            int puntenElementen = 0;
			int puntenLadingen = 0;
            
            if(e.getSource()==correctPV)
            {   try
                {   puntenCorrect = Integer.parseInt(correctPV.getText());
                    this.puntenCorrect = puntenCorrect;
                }   
                catch(Exception ex)
                {   }
            }
            if(e.getSource()==vereenvoudigbaarPV)
            {   try
                {   aftrekVereenvoudigbaar = Integer.parseInt(vereenvoudigbaarPV.getText());
                    this.aftrekVereenvoudigbaar = aftrekVereenvoudigbaar;
                }   
                catch(Exception ex)
                {   }
            }
            if(e.getSource() == onjuistePijlPV)
            {
            	try
            	{
            		aftrekOnjuistePijl = Integer.parseInt(onjuistePijlPV.getText());
            		this.aftrekOnjuistePijl = aftrekOnjuistePijl;
            	}
            	catch(Exception ex)
                {   }
            }
            if(e.getSource()==beginstoffenPV)
            {   try
                {   puntenBeginstoffen = Integer.parseInt(beginstoffenPV.getText());
                    this.puntenBeginstoffen = puntenBeginstoffen;
                }   
                catch(Exception ex)
                {   }
            }
            if(e.getSource()==productenPV)
            {   try
                {   puntenProducten = Integer.parseInt(productenPV.getText());
                    this.puntenProducten = puntenProducten;
                }   
                catch(Exception ex)
                {   }
            }
            if(e.getSource()==elementenPV)
			{	try
				{	puntenElementen = Integer.parseInt(elementenPV.getText());
					this.puntenElementen = puntenElementen;
				}	
				catch(Exception ex)
				{	}
			}
            if(e.getSource()==ladingenPV)
            {   try
                {   puntenLadingen = Integer.parseInt(ladingenPV.getText());
                    this.puntenLadingen = puntenLadingen;
                }   
                catch(Exception ex)
                {   }
            }
            
        }
    }
    
    public void zetTekstVak(boolean b)
    {   //tipsCB.setVisible(b);
    }
    
    public void setFeedbackOption(boolean b)
    {
        hasFeedback = b;
        tabbladTab.setVisible(b);
        feedbackEditor.setVisible(b);
        feedbackSizeCB.setVisible(b);
        feedbackLabel.setVisible(b);
        aantalTabsKnop.setVisible(b);
        tabPositieKnop.setVisible(b);
        feedbackPV.setVisible(b);
        goedFoutIP.setVisible(b);
        //puntenLabel.setVisible(!b);
        
//        gelijkwaardigPV.setVisible(!b);
//        if(b || vorm)vormPV.setVisible(!b);
//        if(b || eindOplossingNodig) eindOplossingPV.setVisible(!b);
//        if(b || exact)exactPV.setVisible(!b);
//        if(b || significant && significantieAan) significantPV.setVisible(!b);
		
        
        //eindOplossingCB.setVisible(!b);
        //vormCB.setVisible(b);
        
        answerModelNr = 0;
        tabbladTab.setSelected(answerModelNr+1);
        if(b)setAnswerModel();
    }
    
    
    public void zetTabletUser(FormuleVakHouder formuleVakHouder)
    {   if(tablet==null) return;
        tablet.zetFormuleVakHouder(formuleVakHouder);
        tabletUser = formuleVakHouder;
        
    }
    
    public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
    {   if(tablet==null) 
        {   tablet = new Tablet(formuleVakHouder);
            tablet.setLocation(x,y);
            
        }
        tablet.zetFormuleVakHouder(formuleVakHouder);
        tabletUser = formuleVakHouder;
        
        
    }
    
    public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
    {   if(tablet==null) 
        {   tablet = new Tablet(formuleVakHouder);
            
            
        }
        if(!tabletAdded)
        {   add(tablet,0);
            tablet.setLocation(x,y);
            tabletAdded = true;
            //resize();
            repaint();
        }
        tablet.zetFormuleVakHouder(formuleVakHouder);
    }
    
    public void removeTablet()
    {   if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
        tabletAdded = false;
    }
    
    public Tablet getTablet()
	{	return tablet;
	}
    
//    public void mousePressed(MouseEvent e)
//    {   if(e.getSource()==gelijkwaardigCB && e.getModifiers()== InputEvent.BUTTON3_MASK || e.isControlDown())
//		{	try{
//			new Expressie();
//			String intervalString = JOptionPane.showInputDialog(this, "testwaarden interval is nu [" + Expressie.df.format(eqTestValueMin) + ";" + Expressie.df.format(eqTestValueMax) +"]", "Keuze testWaarden", JOptionPane.QUESTION_MESSAGE);
//			intervalString = StringUtils.replaceStr(intervalString, "[", "");
//			intervalString = StringUtils.replaceStr(intervalString, "]", "");
//			String[] parts = StringUtils.split(intervalString, ";");
//			eqTestValueMin = Double.parseDouble(parts[0]);
//			eqTestValueMax = Double.parseDouble(parts[1]);
//			} catch(Exception ex){}
//			
//		}
//    }
    
   
        
    //ActionProducer
    private ActionListener actionListener = null;
    
    public void addActionListener(ActionListener l) 
    {   actionListener = AWTEventMulticaster.add(actionListener,l);
    }
    
    public void removeActionListener(ActionListener l)
    {   actionListener = AWTEventMulticaster.remove(actionListener, l);
    }   
    
    public void produceAction(String command)
    {   if (actionListener != null)
        {   actionListener.actionPerformed( new ActionEvent(this, 0, command) );
        }
    }
    //end ActionProducer
}
