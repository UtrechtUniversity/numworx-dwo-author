package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.cbook.cbookif.CBookWidgetEditIF;

import fi.beans.base64code.*;
import fi.beans.ideas.Exercise;
import fi.beans.ideas.IdeasIF;
import fi.beans.ideas.RuleIF;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.opdrnav.*;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.stringutils.StringUtils;


public class AntwoordFormuleVakEditPanel extends JLayeredPane implements InteractieEditPanel, ActionListener,  MouseListener, MouseMotionListener, TabletOwner
{
	private FormuleEditor antwoordvak,startEditor,vormEditor;
	private JLabel antwoordLabel, startLabel, feedbackLabel;
	private JCheckBox  gelijkwaardigCB, herleidingCB, exactCB, stappenCB, subKnopCB, subKnopExtraCB,antwoordCheckCB, rmKnopCB;
	private JCheckBox  significantCB;
	private JLabel ScoringLabel, puntenLabel, checkTotaalLabel;
	private JTextField gelijkwaardigPV, herleidingPV, exactPV, significantPV, feedbackPV;
	private JComboBox herleidingsKeuze;
	private JCheckBox rekenVakCB;
		
	private int puntenGelijkwaardig = 10;
	private int puntenHerleiding = 0;
	private int puntenExact = 0;
	private int puntenSignificant = 0;
	private int puntenFeedback = 0;
	
	private boolean	antwoordCheck = true;
	private boolean	gelijkwaardig = true;
	private boolean	herleiding;
	private boolean	exact;
	private boolean	significant;
	static boolean	significantieAan=false;
	private boolean	stappen = true;
	private boolean	subKnop = false;
	private boolean	subKnopExtra = false;
	
	private int soortHerleiding = 0;
		
	private String[] herleidingItems;
	
	
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
		
	private JTextField aantalDecRmField;
	
	private double eqTestValueMin = 0;
	private double eqTestValueMax = 5;
	
	private DialogFacade startEditorPopupFrame;
	private DialogFacade vormEditorPopupFrame;
	private DialogFacade feedbackEditorPopupFrame;
	
	private JCheckBox uitwCB;
	private JCheckBox boxMetRandCB;
	
	private JCheckBox tipsCB;
    private boolean tips;
    private IdeasInstellingenButton ideasButton;
    
    private JCheckBox eigenOpdrCB;
    
    public static void zetSignificantieAan(boolean b)
    {	significantieAan = b;
    }
    
	public AntwoordFormuleVakEditPanel(int soort)
	{	setLayout(null);
		super.setSize(770,520); //voor dwo
		setBackground(Color.white);	
		setOpaque(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		
		startLabel = makeLabel(5,30,770,20,WiskOpdr.rb.getString("startExpLabel"),true);
				
		startEditor = new FormuleEditor(false);
        startEditor.setHeader(true);
		startEditor.setBounds(5,50,470,105);
        startEditor.setFont(font);
        startEditor.setResizable(true);	
        startEditor.addActionListener(this);
        add(startEditor);
		
		
		antwoordLabel = makeLabel(5,160,770,20,WiskOpdr.rb.getString("antwoordLabel"),true);
		//antwoordCheckCB = makeCheckBox(5,160,120,20,WiskOpdr.rb.getString("antwoordLabel"),true,true);
				
		antwoordvak = new FormuleEditor(true);
		antwoordvak.setBounds(5,180,770,150);
		antwoordvak.setFont(font);
		antwoordvak.addActionListener(this);
		add(antwoordvak);
		antwoordvak.setResizable(true);
		
		feedbackCB = makeCheckBox(145,160,80,20,WiskOpdr.rb.getString("feedbackCBLabel"),false,true);
		eigenOpdrCB = makeCheckBox(690,160,270,20,WiskOpdr.rb.getString("eigenOpdrCBLabel"),false,true);
			
		
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
		
		feedbackEditor = new TekstEditor(false,true,true);
		feedbackEditor.setBounds(5,350,300,160);
		feedbackEditor.setFont(font);
		feedbackEditor.addActionListener(this);
		feedbackEditor.setBackground(new Color(255,255,200));
		add(feedbackEditor,0);
		
		feedbackSizeCB = makeCheckBox(80,352,180,20,WiskOpdr.rb.getString("feedbackSizeCBLabel"),false,true);
		
		vormEditor = new FormuleEditor(true);
        vormEditor.setResizable(true);
        vormEditor.setMultiLine(true);
        vormEditor.setScrollHorizontal(true);
        vormEditor.setBounds(515,350,260,160);
        vormEditor.setFont(font);
        vormEditor.addActionListener(this);
        vormEditor.setVisible(false);
        add(vormEditor,0);
        
		ScoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
		puntenLabel = makeLabel(460,385,40,20,WiskOpdr.rb.getString("puntenLabel"),true);
		checkTotaalLabel = makeLabel(620,385,160,20,WiskOpdr.rb.getString("checkTotaalLabel"),false);
		checkTotaalLabel.setForeground(Color.red);
		
		gelijkwaardigCB = makeCheckBox(320,410,120,20,WiskOpdr.rb.getString("gelijkwaardigCBLabel"),true,true);
		gelijkwaardigCB.addMouseListener(this);
		herleidingCB = makeCheckBox(320,435,120,20,WiskOpdr.rb.getString("vormCBLabel"),false,true);
		significantCB = makeCheckBox(320,460,120,20,WiskOpdr.rb.getString("significantCBLabel"),false,significantieAan?true:false);
		exactCB = makeCheckBox(320,significantieAan?485:460,120,20,WiskOpdr.rb.getString("exactCBLabel"),false,true);
		stappenCB = makeCheckBox(630,50,200,20,WiskOpdr.rb.getString("stappenCBLabel"),true,false);
		rekenVakCB = makeCheckBox(630,50,200,20,"Formulevak als calculator",false,false);
		formuleToolBijFocusCB = makeCheckBox(530,25,270,20,WiskOpdr.rb.getString("formuleToolCBLabel"),false,false);
		subKnopCB = makeCheckBox(650,55,100,20,WiskOpdr.rb.getString("subKnopCBLabel"),false,true);
		subKnopExtraCB = makeCheckBox(650,80,100,20,WiskOpdr.rb.getString("subKnopExtraCBLabel"),false,false);
		rmKnopCB = makeCheckBox(650,105,100,20,WiskOpdr.rb.getString("rmCBLabel"),false,true);
		//subKnopCB.setVisible(false);
		checkCB = makeCheckBox(5,5,200,20,WiskOpdr.rb.getString("checkCBLabel"),true,true);
        teltMeeCB = makeCheckBox(225,5,200,20,WiskOpdr.rb.getString("teltMeeCBLabel"),true,true);
        logCB = makeCheckBox(450,5,70,20,WiskOpdr.rb.getString("logCBLabel"),false,true);
        logIDField = makeTextField(520,5,60,20,"0",false);
        uitwCB = makeCheckBox(530,50,80,20,WiskOpdr.rb.getString("uitwCBLabel"),false,false);
        boxMetRandCB = makeCheckBox(500,105,80,20,WiskOpdr.rb.getString("boxMetRand"),true,true);
        
        logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        logObjectivesButton.setBounds(600,5,120,20);
        if(WiskOpdr.objectives!=null)add(logObjectivesButton);
        
        aantalDecRmField = makeTextField(670,135,100,20,"10",false);
        		
		gelijkwaardigPV = makeTextField(460,410,30,20,"10",true);
		herleidingPV = makeTextField(460,435,30,20,"0",false);
		exactPV = makeTextField(460,significantieAan?485:460,30,20,"0",false);
		significantPV = makeTextField(460,460,30,20,"0",false);
		feedbackPV = makeTextField(460,385,30,20,"0",false);
		
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
		
		tipsCB = makeCheckBox(500,55,100,20,"Ideas [test]",false,true);
        
       ideasButton = new IdeasInstellingenButton();
       ideasButton.setFont(font);
       ideasButton.setBounds(500,80,80,20);
       ideasButton.setVisible(false);
       add(ideasButton);
       
       if(soort==0)
		{	stappen = true;				
		}
		
		else if(soort==2)
		{	stappen = false;
			startLabel.setVisible(false);
			startEditor.setVisible(false);
			rmKnopCB.setVisible(false);
			subKnopCB.setVisible(false);
			tipsCB.setVisible(false);
			
			formuleToolBijFocusCB.setVisible(true);
			uitwCB.setVisible(true);
			boxMetRandCB.setVisible(true);
			//feedbackCB.setVisible(false);
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
	
	private Hashtable fillAnswerModel(Hashtable h)
	{
		String antwoordString = "$f@";
		boolean gelijkwaardig = true;
		boolean herleiding = false;
		boolean exact = false;
		boolean significant = false;
		int puntenFeedback = 0;
		int soortHerleiding = 0;
		String feedback = "";
		int feedbackWidth = 195;
		int feedbackHeight = 20;
		String vormString = "$f@";
		int goedHalfFout = 2;
		
		antwoordString = antwoordvak.geefFormuleVak().toString();
		gelijkwaardig = this.gelijkwaardig;
		herleiding = this.herleiding;
		exact = this.exact;
		significant = this.significant;
		soortHerleiding = this.soortHerleiding;
				
		puntenFeedback = (Integer.parseInt(feedbackPV.getText()));
		feedback  = feedbackEditor.getText();
		if(feedbackEditorPopupFrame!=null)
		{	feedbackWidth = feedbackEditorPopupFrame.getSize().width - feedbackEditorPopupFrame.getInsets().left - feedbackEditorPopupFrame.getInsets().right;
			feedbackHeight = feedbackEditorPopupFrame.getSize().height - feedbackEditorPopupFrame.getInsets().top - feedbackEditorPopupFrame.getInsets().bottom;
		}
		
		//vormString = vormEditor.geefFormuleVak().toString();
		String[] vormStrings = vormEditor.geefRegels();
        if(vormStrings.length==1) vormString = vormEditor.geefFormuleVak().toString();
        else
        {	vormString = "$f";
        	for(int i=0 ; i<vormStrings.length ; i++)
        	{	vormString = vormString + vormStrings[i].substring(2,vormStrings[i].length()-1) + "::";
        	}
        	vormString = vormString.substring(0,vormString.length()-2) + "@";
        }
		goedHalfFout = goedFoutIP.geefKeuze()-1;
		
		h.put("antwoordString",antwoordString);
		h.put("gelijkwaardig",new Boolean(gelijkwaardig));
		h.put("herleiding",new Boolean(herleiding));
		h.put("exact",new Boolean(exact));
		h.put("significant",new Boolean(significant));
		h.put("puntenFeedback",new Integer(puntenFeedback));
		h.put("soortHerleiding",new Integer(soortHerleiding));
		h.put("feedback",feedback);
		h.put("feedbackWidth",new Integer(feedbackWidth));
		h.put("feedbackHeight",new Integer(feedbackHeight));
		h.put("vormString",vormString);
		h.put("goedHalfFout",new Integer(goedHalfFout));
		return h;
	}
	
	private void setAnswerModel(Hashtable h)
	{	String antwoordString = "$f@";
		boolean gelijkwaardig = true;
		boolean herleiding = false;
		boolean exact = false;
		boolean significant = false;
		int puntenFeedback = 0;
		int soortHerleiding = 0;
		String feedback = "";
		int feedbackWidth = 0;
        int feedbackHeight = 0;
		String vormString = "$f@";
		int goedHalfFout = 2;
		
		if(h!=null) 
		{	if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
			if(h.containsKey("gelijkwaardig")) gelijkwaardig = ((Boolean)h.get("gelijkwaardig")).booleanValue();
			if(h.containsKey("herleiding")) herleiding = ((Boolean)h.get("herleiding")).booleanValue();
			if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
			if(h.containsKey("significant")) significant = ((Boolean)h.get("significant")).booleanValue();
			if(h.containsKey("stappen")) stappen = ((Boolean)h.get("stappen")).booleanValue();
			if(h.containsKey("soortHerleiding")) soortHerleiding = ((Integer)h.get("soortHerleiding")).intValue();
			if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
			if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
			if(h.containsKey("feedbackWidth")) feedbackWidth = ((Integer)h.get("feedbackWidth")).intValue();
			if(h.containsKey("feedbackHeight")) feedbackHeight = ((Integer)h.get("feedbackHeight")).intValue();
            if(h.containsKey("vormString")) vormString = (String)h.get("vormString");
			if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
			
		}
		this.herleiding = herleiding;
		this.gelijkwaardig = gelijkwaardig;
		this.exact = exact;
		this.significant = significant;
		this.soortHerleiding = soortHerleiding;
		this.puntenFeedback = puntenFeedback;
		
		antwoordvak.geefFormuleVak().vulVak(antwoordString);
		//vormEditor.geefFormuleVak().vulVak(vormString);
		String[] vormStrings = StringUtils.split(vormString, "::");
        for(int i=0 ; i<vormStrings.length ; i++)
    	{	if(i==0) vormStrings[i] = vormStrings[i] + "@";
    		else if(i==vormStrings.length-1) vormStrings[i] = "$f" + vormStrings[i];
    		else  vormStrings[i] = "$f" + vormStrings[i] + "@";
    	}
        vormEditor.verwijderRegels();
        vormEditor.zetRegels(vormStrings);
			
		herleidingCB.setVisible(true);
		exactCB.setVisible(true);
		if(significantieAan) significantCB.setVisible(true);
		herleidingsKeuze.setVisible(true);
				
		gelijkwaardigCB.setSelected(gelijkwaardig);
		herleidingCB.setSelected(herleiding);
		exactCB.setSelected(exact);
		significantCB.setSelected(significant);
		
		vormEditor.setVisible(herleiding);
		
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
	{	if(answerModels==null)return;
		answerModels[answerModelNr] = fillAnswerModel(new Hashtable());
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
				boolean significant = false;
				boolean stappen = true;
				int soortHerleiding = 0;
				int puntenGelijkwaardig = 10;
				int puntenHerleiding = 0;
				int puntenExact = 0;
				int puntenSignificant = 0;
				boolean formuleToolBijFocus = false;
				Hashtable[] answerModels = null;
				boolean hasFeedback = false;
				boolean feedbackSize = false;
				String vormString = "$f@";
				boolean subKnop = false;
				boolean subKnopExtra = false;
				boolean rmKnop = false;
				boolean check = true;
				boolean teltMee = true;
				boolean logOption = false;
				String logID = "";
				boolean[][] logObjectives = null; 
				boolean hasObjectives = false;
				double eqTestValueMin = 0;
				double eqTestValueMax = 5;
				int aantalDecRm = 10;
				boolean uitw = false;
				boolean tips = false;
                Hashtable ideasInstellingen = new Hashtable();
                boolean eigenOpdr = false;
                boolean boxMetRand = true;
                
				
				if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
				if(interactiePanelLaunchState.containsKey("startString")) startString = (String)interactiePanelLaunchState.get("startString");
				if(interactiePanelLaunchState.containsKey("herleiding")) herleiding = ((Boolean)interactiePanelLaunchState.get("herleiding")).booleanValue();
				if(interactiePanelLaunchState.containsKey("exact")) exact = ((Boolean)interactiePanelLaunchState.get("exact")).booleanValue();
				if(interactiePanelLaunchState.containsKey("significant")) significant = ((Boolean)interactiePanelLaunchState.get("significant")).booleanValue();
				if(interactiePanelLaunchState.containsKey("stappen")) stappen = ((Boolean)interactiePanelLaunchState.get("stappen")).booleanValue();
				if(interactiePanelLaunchState.containsKey("soortHerleiding")) soortHerleiding = ((Integer)interactiePanelLaunchState.get("soortHerleiding")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)interactiePanelLaunchState.get("puntenGelijkwaardig")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenHerleiding")) puntenHerleiding = ((Integer)interactiePanelLaunchState.get("puntenHerleiding")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenExact")) puntenExact = ((Integer)interactiePanelLaunchState.get("puntenExact")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenSignificant")) puntenSignificant = ((Integer)interactiePanelLaunchState.get("puntenSignificant")).intValue();
				if(interactiePanelLaunchState.containsKey("formuleToolBijFocus")) formuleToolBijFocus = ((Boolean)interactiePanelLaunchState.get("formuleToolBijFocus")).booleanValue();
				if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
				if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
				if(interactiePanelLaunchState.containsKey("feedbackSize")) feedbackSize = ((Boolean)interactiePanelLaunchState.get("feedbackSize")).booleanValue();
				if(interactiePanelLaunchState.containsKey("vormString")) vormString = (String)interactiePanelLaunchState.get("vormString");
				if(interactiePanelLaunchState.containsKey("subKnop")) subKnop = ((Boolean)interactiePanelLaunchState.get("subKnop")).booleanValue();
				if(interactiePanelLaunchState.containsKey("subKnopExtra")) subKnopExtra = ((Boolean)interactiePanelLaunchState.get("subKnopExtra")).booleanValue();
				if(interactiePanelLaunchState.containsKey("rmKnop")) rmKnop = ((Boolean)interactiePanelLaunchState.get("rmKnop")).booleanValue();
				if(interactiePanelLaunchState.containsKey("check")) check = ((Boolean)interactiePanelLaunchState.get("check")).booleanValue();
				if(interactiePanelLaunchState.containsKey("teltMee")) teltMee = ((Boolean)interactiePanelLaunchState.get("teltMee")).booleanValue();
				if(interactiePanelLaunchState.containsKey("logOption")) logOption = ((Boolean)interactiePanelLaunchState.get("logOption")).booleanValue();
				if(interactiePanelLaunchState.containsKey("logID")) logID = (String)interactiePanelLaunchState.get("logID");
				if(interactiePanelLaunchState.containsKey("eqTestValueMin")) eqTestValueMin = ((Double)interactiePanelLaunchState.get("eqTestValueMin")).doubleValue();
				if(interactiePanelLaunchState.containsKey("eqTestValueMax")) eqTestValueMax = ((Double)interactiePanelLaunchState.get("eqTestValueMax")).doubleValue();
				if(interactiePanelLaunchState.containsKey("aantalDecRm")) aantalDecRm = ((Integer)interactiePanelLaunchState.get("aantalDecRm")).intValue();
				if(interactiePanelLaunchState.containsKey("uitw")) uitw = ((Boolean)interactiePanelLaunchState.get("uitw")).booleanValue();
				if(interactiePanelLaunchState.containsKey("eigenOpdr")) eigenOpdr = ((Boolean)interactiePanelLaunchState.get("eigenOpdr")).booleanValue();
				if(interactiePanelLaunchState.containsKey("boxMetRand")) boxMetRand = ((Boolean)interactiePanelLaunchState.get("boxMetRand")).booleanValue();
				if(interactiePanelLaunchState.containsKey("tips")) tips = ((Boolean)interactiePanelLaunchState.get("tips")).booleanValue();
                if(tips){
                	if(interactiePanelLaunchState.containsKey("ideasInstellingen")) ideasInstellingen = (Hashtable)interactiePanelLaunchState.get("ideasInstellingen");
                }
                if(interactiePanelLaunchState.containsKey("logObjectives")) 
                	try	{	
        				logObjectives = (boolean[][])interactiePanelLaunchState.get("logObjectives");
        			} catch(Exception ex){
        			}
        		if(interactiePanelLaunchState.containsKey("hasObjectives"))
        			hasObjectives = ((Boolean)interactiePanelLaunchState.get("hasObjectives")).booleanValue();
        		else
        			hasObjectives = logObjectives != null;
                
				this.herleiding = herleiding;
				this.exact = exact;
				this.significant = significant;
				this.soortHerleiding = soortHerleiding;
				this.puntenGelijkwaardig = puntenGelijkwaardig;
				this.puntenHerleiding = puntenHerleiding;
				this.puntenExact = puntenExact;
				this.puntenSignificant = puntenSignificant;
				this.stappen = stappen;
				this.formuleToolBijFocus = formuleToolBijFocus;
				
				this.answerModels = new Hashtable[answerModels.length];
				for(int i=0 ; i<answerModels.length ; i++)
				{	this.answerModels[i] = answerModels[i];
				}
					
					//this.answerModels = answerModels;
				
				this.subKnop = subKnop;
				this.subKnopExtra = subKnopExtra;
				
				this.eqTestValueMin = eqTestValueMin;
				this.eqTestValueMax = eqTestValueMax;
				
				this.tips = tips;
				ideasButton.setVisible(tips);
				if(tips)
                {  	ideasButton.zetInstellingen(ideasInstellingen);
                	tipsCB.setSelected(tips);
                    
                }
				
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
				//vormEditor.geefFormuleVak().vulVak(vormString);
				String[] vormStrings = StringUtils.split(vormString, "::");
                for(int i=0 ; i<vormStrings.length ; i++)
            	{	if(i==0) vormStrings[i] = vormStrings[i] + "@";
            		else if(i==vormStrings.length-1) vormStrings[i] = "$f" + vormStrings[i];
            		else  vormStrings[i] = "$f" + vormStrings[i] + "@";
            	}
                vormEditor.zetRegels(vormStrings);
                
				stappenCB.setSelected(stappen);
				formuleToolBijFocusCB.setSelected(formuleToolBijFocus);
				
				subKnopCB.setSelected(subKnop);
				subKnopExtraCB.setVisible(subKnop);
				subKnopExtraCB.setSelected(subKnopExtra);
				
	           
	                
	            checkCB.setSelected(check);
	            teltMeeCB.setSelected(teltMee);
	            logCB.setSelected(logOption);
	            
	            uitwCB.setSelected(uitw);
	            boxMetRandCB.setSelected(boxMetRand);
	            eigenOpdrCB.setSelected(eigenOpdr);
	            rmKnopCB.setVisible(uitw||stappen);
	            rmKnopCB.setSelected(rmKnop);
	            aantalDecRmField.setVisible(uitw && rmKnop);
		    	
	            //startLabel.setVisible(uitw);
				//startEditor.setVisible(uitw);
	                
	            logIDField.setVisible(logOption);
	            //logObjectivesButton.setVisible(hasObjectives);
	            logIDField.setText(logID);
	            logObjectivesButton.setChoices(logObjectives);
	            
	            aantalDecRmField.setVisible(rmKnop);
	            aantalDecRmField.setText(""+aantalDecRm);
				
                gelijkwaardigPV.setText(""+puntenGelijkwaardig);
                
				setFeedbackOption(hasFeedback);
				feedbackCB.setSelected(hasFeedback);
				feedbackSizeCB.setSelected(feedbackSize);
				feedbackEditor.setResizable(feedbackSize);
				if(hasFeedback)return;
				
                herleidingCB.setSelected(herleiding);
                herleidingPV.setVisible(herleiding);
                herleidingPV.setText(""+puntenHerleiding);
                
                vormEditor.setVisible(herleiding);
                
                exactCB.setSelected(exact);
                exactPV.setVisible(exact);
                exactPV.setText(""+puntenExact);
                
                significantCB.setSelected(significant);
                significantPV.setVisible(significant && significantieAan);
                significantPV.setText(""+puntenSignificant);
                
               
		
			//// EIND //// Deze code zal moeten worden aangepast als de interface meerdere antwoordvakken ondersteunt
			
			
		
	}
	
	public Hashtable getEditState()
	{	
		Hashtable interactiePanelLaunchState = new Hashtable();
        
        
			String antwoordString = null;
			String startString = null;
			boolean herleiding = false;
			boolean exact = false;
			boolean significant = false;
			boolean stappen = false;
			int soortHerleiding = 0;
			int puntenGelijkwaardig = 10;
			int puntenHerleiding = 0;
			int puntenExact = 0; 
			int puntenSignificant = 0; 
			int scoreMax = 0;
			int[][] scoreMaxObjectives = null;
			boolean formuleToolBijFocus = false;
			Hashtable[] answerModels;
			boolean hasFeedback;
			boolean feedbackSize;
			String vormString = "$f@";
			boolean subKnop = false;
			boolean subKnopExtra = false;
			boolean rmKnop = false;
			boolean check = true;
			boolean teltMee = true;
			boolean logOption = false;
			String logID = "";
			boolean[][] logObjectives = null;
			double eqTestValueMin = 0;
			double eqTestValueMax = 5;
			int aantalDecRm = 10;
			boolean uitw = false;
			boolean tips;
			Hashtable ideasInstellingen = new Hashtable();
			boolean eigenOpdr = false;
			boolean boxMetRand = true;
            
            
			
			getAnswerModel();
			answerModels = this.answerModels;
			if(answerModels!=null)setAnswerModel(answerModels[0]);
			
			antwoordString = antwoordvak.geefFormuleVak().toString();
			startString = startEditor.geefFormuleVak().toString();
			//vormString = vormEditor.geefFormuleVak().toString();
			String[] vormStrings = vormEditor.geefRegels();
            if(vormStrings.length==1) vormString = vormEditor.geefFormuleVak().toString();
            else
            {	vormString = "$f";
            	for(int i=0 ; i<vormStrings.length ; i++)
            	{	vormString = vormString + vormStrings[i].substring(2,vormStrings[i].length()-1) + "::";
            	}
            	vormString = vormString.substring(0,vormString.length()-2) + "@";
            }
			herleiding = this.herleiding;
			exact = this.exact;
			significant = this.significant;
			stappen = this.stappen;
			soortHerleiding = this.soortHerleiding;
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
			{	puntenSignificant = Integer.parseInt(significantPV.getText());
				this.puntenSignificant = puntenSignificant;
			}	
			catch(Exception ex)
			{	}
			try
			{	aantalDecRm = Integer.parseInt(aantalDecRmField.getText());
			}	
			catch(Exception ex)
			{	}
			
			check = checkCB.isSelected();
			teltMee = teltMeeCB.isSelected();
			logOption = logCB.isSelected();
			logID = logIDField.getText();
			logObjectives = logObjectivesButton.getChoices();
			
			puntenGelijkwaardig = this.puntenGelijkwaardig;
			puntenHerleiding = this.puntenHerleiding;
			puntenExact = this.puntenExact;
			puntenSignificant = this.puntenSignificant;
			formuleToolBijFocus = this.formuleToolBijFocus;
			scoreMax = puntenGelijkwaardig + puntenHerleiding + puntenExact;
			hasFeedback = this.hasFeedback;
			if(hasFeedback)scoreMax = puntenFeedback;
			feedbackSize = feedbackSizeCB.isSelected();
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
			subKnop = this.subKnop;
			subKnopExtra = this.subKnopExtra;
			rmKnop = rmKnopCB.isSelected();
						
			uitw = uitwCB.isSelected();
			eigenOpdr = eigenOpdrCB.isSelected();
			boxMetRand = boxMetRandCB.isSelected();
			
			eqTestValueMin = this.eqTestValueMin;
			eqTestValueMax = this.eqTestValueMax;
			
			tips = this.tips;
			if(tips) ideasInstellingen = ideasButton.geefInstellingen();
            
            
			
			interactiePanelLaunchState.put("antwoordString",antwoordString);
			interactiePanelLaunchState.put("startString",startString);
			interactiePanelLaunchState.put("herleiding",new Boolean(herleiding));
			interactiePanelLaunchState.put("exact",new Boolean(exact));
			interactiePanelLaunchState.put("significant",new Boolean(significant));
			interactiePanelLaunchState.put("stappen",new Boolean(stappen));
			interactiePanelLaunchState.put("soortHerleiding",new Integer(soortHerleiding));
			interactiePanelLaunchState.put("puntenGelijkwaardig",new Integer(puntenGelijkwaardig));
			interactiePanelLaunchState.put("puntenHerleiding",new Integer(puntenHerleiding));
			interactiePanelLaunchState.put("puntenExact",new Integer(puntenExact));
			interactiePanelLaunchState.put("puntenSignificant",new Integer(puntenSignificant));
			interactiePanelLaunchState.put("formuleToolBijFocus",new Boolean(formuleToolBijFocus));
			interactiePanelLaunchState.put("scoreMax",new Integer(scoreMax));
			if(answerModels!=null)interactiePanelLaunchState.put("answerModels",answerModels);
			interactiePanelLaunchState.put("hasFeedback",new Boolean(hasFeedback));
			interactiePanelLaunchState.put("feedbackSize",new Boolean(feedbackSize));
			interactiePanelLaunchState.put("vormString",vormString);
			interactiePanelLaunchState.put("subKnop",new Boolean(subKnop));
			interactiePanelLaunchState.put("subKnopExtra",new Boolean(subKnopExtra));
			interactiePanelLaunchState.put("rmKnop",new Boolean(rmKnop));
			interactiePanelLaunchState.put("check",new Boolean(check));
			interactiePanelLaunchState.put("teltMee",new Boolean(teltMee));
			interactiePanelLaunchState.put("logOption",new Boolean(logOption));
			interactiePanelLaunchState.put("logID",logID);
			interactiePanelLaunchState.put("eqTestValueMin",new Double(eqTestValueMin));
			interactiePanelLaunchState.put("eqTestValueMax",new Double(eqTestValueMax));
			interactiePanelLaunchState.put("aantalDecRm",new Integer(aantalDecRm));
			interactiePanelLaunchState.put("uitw",new Boolean(uitw));
			interactiePanelLaunchState.put("eigenOpdr",new Boolean(eigenOpdr));
			interactiePanelLaunchState.put("boxMetRand",new Boolean(boxMetRand));
			interactiePanelLaunchState.put("tips",new Boolean(tips));
			if(tips){
				interactiePanelLaunchState.put("ideasInstellingen",ideasInstellingen);
	        }
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
    
    public void maakVormPopupFrame()
	{
		vormEditorPopupFrame = DialogFacade.newInstance(this, "");
		
		
		vormEditorPopupFrame.getContentPane().setLayout(null);
		vormEditorPopupFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{   vormEditor.produceAction("verklein");
				vormEditor.setEnlarged(false);
			}
		});
		vormEditorPopupFrame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e)
			{   int x = 0;
				int y = 0;
				int b = vormEditorPopupFrame.getSize().width - vormEditorPopupFrame.getInsets().left - vormEditorPopupFrame.getInsets().right;
				int h = vormEditorPopupFrame.getSize().height - vormEditorPopupFrame.getInsets().top - vormEditorPopupFrame.getInsets().bottom;
				vormEditor.setBounds(x,y,b,h);
			}
		});
	}
    
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
		else if(e.getSource()==feedbackSizeCB)
		{	feedbackEditor.setResizable(feedbackSizeCB.isSelected());
			
		}
		else if(e.getSource()==antwoordCheckCB)
		{	antwoordCheck = antwoordCheckCB.isSelected();
			
		}
		else if(e.getSource()==subKnopCB)
	    {   subKnop = subKnopCB.isSelected();
	    	subKnopExtraCB.setVisible(subKnop);
	    	if(!subKnop)subKnopExtraCB.setSelected(subKnop);
	    }
		else if(e.getSource()==subKnopExtraCB)
	    {   subKnopExtra = subKnopExtraCB.isSelected();
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
			{	gelijkwaardigPV.setText("0");
				exactPV.setText("10");
				
				puntenGelijkwaardig = 0;
				puntenExact = 10;
			}
			else
			{	
				gelijkwaardigPV.setText("0");
				gelijkwaardigPV.setText("10");
				exactPV.setText("0");
			
				puntenGelijkwaardig = 10;
				puntenExact = 0;
			}	
		}
		else if(e.getSource()==significantCB)
		{
			boolean b = significantCB.isSelected();
			significant = b;
			if(!hasFeedback && answerModelNr==0)significantPV.setVisible(b);
		}
		
		else if(e.getSource()==stappenCB)
		{	boolean b = stappenCB.isSelected();
			stappen = b;
		}
		else if(e.getSource()==formuleToolBijFocusCB)
		{	boolean b = formuleToolBijFocusCB.isSelected();
			formuleToolBijFocus = b;
		}
		else if(e.getSource()==herleidingsKeuze)
		{	soortHerleiding = herleidingsKeuze.getSelectedIndex();
		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
		else if(e.getSource()==uitwCB)
	    {   rmKnopCB.setVisible(uitwCB.isSelected()); 
	    	aantalDecRmField.setVisible(uitwCB.isSelected() && rmKnopCB.isSelected());
		    //startLabel.setVisible(uitwCB.isSelected());
			//startEditor.setVisible(uitwCB.isSelected());
	    }
		else if(e.getSource()==rmKnopCB)
	    {   aantalDecRmField.setVisible(rmKnopCB.isSelected());   
	    }
		else if(e.getSource()==startEditor)
		{	if(e.getActionCommand().equals("vergroot"))
			{	if(startEditorPopupFrame==null)	maakStartPopupFrame();
				Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
				int x = startEditor.getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + 500));
				int y = startEditor.getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + 400));
				startEditorPopupFrame.setVisible(true);
				startLabel.setVisible(false);
				startEditorPopupFrame.getContentPane().add(startEditor);
				startEditorPopupFrame.pack();
				startEditorPopupFrame.setSize(500,400);
				startEditorPopupFrame.setLocation(x,y);
			}
			if(e.getActionCommand().equals("verklein"))
			{	startEditorPopupFrame.setVisible(false);
				startLabel.setVisible(true);
				startEditor.setBounds(5,50,470,105);
		        startLabel.setBounds(5,30,770,20);
				add(startEditor);
				startEditorPopupFrame.dispose();
			}
			revalidate();
            repaint();
		}
        else if(e.getSource()==vormEditor)
		{	if(e.getActionCommand().equals("vergroot"))
			{	if(vormEditorPopupFrame==null)	maakVormPopupFrame();
				Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
				int x = vormEditor.getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + 500));
				int y = vormEditor.getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + 400));
				vormEditorPopupFrame.setVisible(true);
				vormEditorPopupFrame.getContentPane().add(vormEditor);
				vormEditorPopupFrame.pack();
				vormEditorPopupFrame.setSize(500,400);
				vormEditorPopupFrame.setLocation(x,y);
			}
			if(e.getActionCommand().equals("verklein"))
			{	vormEditorPopupFrame.setVisible(false);
				vormEditor.setBounds(515,350,260,160);
		        add(vormEditor);
				vormEditorPopupFrame.dispose();
			}
			revalidate();
            repaint();
		}
        else if(e.getSource()==feedbackEditor)
        {   if(e.getActionCommand().equals("vergroot"))
            {   if(feedbackEditorPopupFrame==null)  maakFeedbackEditorPopupFrame();
                Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
                int x = feedbackEditor.getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + 500));
                int y = feedbackEditor.getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + 400));
                int eWidth = feedbackEditor.getEnlargedWidth();
                int eHeight = feedbackEditor.getEnlargedHeight();
                eWidth = eWidth==TekstEditor.defaultEnlargedWidth ? 195 : eWidth;
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
        else if(e.getSource()==tipsCB)
		{	tips = tipsCB.isSelected();
			ideasButton.setVisible(tips);
		}
        else
		{
			int puntenGelijkwaardig = 0;
			int puntenHerleiding = 0;
			int puntenExact = 0;
			int puntenSignificant = 0;
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
			if(e.getSource()==significantPV)
			{	try
				{	puntenSignificant = Integer.parseInt(significantPV.getText());
					this.puntenSignificant = puntenSignificant;
				}	
				catch(Exception ex)
				{	}
			}
			
			boolean b = false;
			b = this.puntenGelijkwaardig + this.puntenHerleiding + this.puntenExact == 10;
			 
		}
	}
	
	
	public void zetTekstVak(boolean b)
	{	rekenVakCB.setVisible(b);
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
		puntenLabel.setVisible(!b);
		gelijkwaardigPV.setVisible(!b);
		if(b || herleiding) herleidingPV.setVisible(!b);
		if(b || exact) exactPV.setVisible(!b);
		if(b || significant && significantieAan) significantPV.setVisible(!b);
		answerModelNr = 0;
		tabbladTab.setSelected(answerModelNr+1);
		if(b)setAnswerModel();
	}
	
	public void setVisibleAntwoordModel(boolean b)
	{
		
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
	{	if(e.getSource()==gelijkwaardigCB && e.getModifiers()== InputEvent.BUTTON3_MASK || e.isControlDown())
		{	try{
			new Expressie();
			String intervalString = JOptionPane.showInputDialog(this, "testwaarden interval is nu [" + Expressie.df.format(eqTestValueMin) + ";" + Expressie.df.format(eqTestValueMax) +"]", "Keuze testWaarden", JOptionPane.QUESTION_MESSAGE);
			intervalString = StringUtils.replaceStr(intervalString, "[", "");
			intervalString = StringUtils.replaceStr(intervalString, "]", "");
			String[] parts = StringUtils.split(intervalString, ";");
			eqTestValueMin = Double.parseDouble(parts[0]);
			eqTestValueMax = Double.parseDouble(parts[1]);
			} catch(Exception ex){}
			
		}
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
