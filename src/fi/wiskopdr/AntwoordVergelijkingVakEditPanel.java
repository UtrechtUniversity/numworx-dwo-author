package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.swing.*;

import fi.beans.base64code.*;
import fi.beans.ideas.*;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.opdrnav.*;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;


public class AntwoordVergelijkingVakEditPanel extends JLayeredPane implements InteractieEditPanel, ActionListener,  MouseListener, MouseMotionListener, TabletOwner
{
    private FormuleEditor antwoordvak,startEditor,vormEditor;
    private JLabel antwoordLabel, startLabel, feedbackLabel;
    private JCheckBox  gelijkwaardigCB, vormCB, exactCB, stappenCB, eindOplossingCB, bewerkingKnoppenCB, 
    	bewerkingKnoppenExtraCB, abcKnopCB, subKnopExtraCB,subKnopCB;
    private JCheckBox  significantCB;
    private JCheckBox linStrategieVersieCB,linOefenVersieCB, bordjesMethodeCB;
    private JLabel ScoringLabel, puntenLabel, checkTotaalLabel;
    private JTextField gelijkwaardigPV, vormPV, exactPV, significantPV, eindOplossingPV, feedbackPV;
    private JCheckBox tipsCB;
        
    
    private int puntenGelijkwaardig = 0;
    private int puntenVorm = 0;
    private int puntenEindOplossing = 10;
    private int puntenExact = 0;
    private int puntenSignificant = 0;
    private int puntenFeedback = 0;
    
    private boolean gelijkwaardig = true;
    private boolean vorm;
    private boolean eindOplossingNodig = true;
    private boolean exact;
    private boolean	significant;
    
	static boolean	significantieAan=false;
    
    private boolean stappen = true;
    private boolean stappenDefault = true;
    
    private boolean abcKnop;
    private boolean subKnop;
    private boolean subKnopExtra;
    private boolean bewerkingKnoppen;
    private boolean bewerkingKnoppenExtra;
    
    
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
    private boolean tips;
    private IdeasInstellingenButton ideasButton;
    
    //private JCheckBox antwoordSubstitutiesCB;
    //private boolean antwoordSubstituties;
    private JButton substitutiesButton;
    private FormuleEditor antwoordSubstitutiesVak;
    
    private JButton functiesButton;
    private FormuleEditor antwoordFunctiesVak;
    
    private JButton solveButton;
    private FormuleEditor antwoordSolveVak;
    private JTextField solveTF;
    
    private JCheckBox pijlCB;
    private boolean pijl = true;
    
    private JCheckBox checkCB;
    private JCheckBox teltMeeCB;
    
    private JCheckBox logCB;
	private JTextField logIDField;
	private ObjectiveChoiceButton logObjectivesButton;
    
    private double eqTestValueMin = 0;
	private double eqTestValueMax = 5;
	
	private DialogFacade startEditorPopupFrame;
	private DialogFacade vormEditorPopupFrame;
	private DialogFacade feedbackEditorPopupFrame;
	
	private JCheckBox uitwCB;
	private JCheckBox casAntwCB;
	private JCheckBox boxMetRandCB;
	
	private static String[][] strategieDomeinNamen = {
    		{IdeasIF.MATH_LINEQ,"algebra.equations.linear"},
    		{IdeasIF.MATH_QUADREQ,"algebra.equations.quadratic"},
    		{IdeasIF.MATH_QUADREQ_WITH_APPROX,"algebra.equations.quadratic.approximate"},
    		{IdeasIF.MATH_QUADREQ_NO_ABC,"algebra.equations.quadratic.no-abc"},
    		{IdeasIF.MATH_HIGHERDEGREE,"algebra.equations.polynomial"},
    		{IdeasIF.MATH_LININEQ,"algebra.inequalities.linear"},
    		{IdeasIF.MATH_QUADRINEQ,"algebra.inequalities.quadratic"},
    		{IdeasIF.MATH_INEQHIGHERDEGREE,"algebra.inequalities.polynomial"},
    		{IdeasIF.MATH_COVERUP,"algebra.equations.coverup"}
    };
	public static Hashtable strategieOudNieuw = new Hashtable();
	
	public static void zetSignificantieAan(boolean b)
	{	significantieAan = b;
	}
    
    public AntwoordVergelijkingVakEditPanel(int soort)
    {   setLayout(null);
        super.setSize(770,520); //voor dwo
        setBackground(Color.white); 
        setOpaque(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        
        
        startLabel = makeLabel(5,30,770,20,WiskOpdr.rb.getString("startVergLabel"),true);
                
        startEditor = new FormuleEditor(false);
        startEditor.setHeader(true);
		startEditor.setBounds(5,50,470,105);
        startEditor.setFont(font);
        startEditor.setResizable(true);	
        startEditor.addActionListener(this);
        add(startEditor);
        
        
        antwoordLabel = makeLabel(5,160,770,20,WiskOpdr.rb.getString("antwoordLabel"),true);
                
        antwoordvak = new FormuleEditor(true);
        antwoordvak.setBounds(5,180,770,150);
        antwoordvak.setFont(font);
        antwoordvak.addActionListener(this);
        add(antwoordvak);
        //antwoordvak.setResizable(true);
        
        antwoordSubstitutiesVak = new FormuleEditor(true);
        antwoordSubstitutiesVak.setBounds(15,270,570,150);
        antwoordSubstitutiesVak.setFont(font);
        antwoordSubstitutiesVak.addActionListener(this);
        antwoordSubstitutiesVak.setMultiLine(true);
        antwoordSubstitutiesVak.setResizable(true);
        setLayer((Component)antwoordSubstitutiesVak, JLayeredPane.POPUP_LAYER.intValue());
        
        substitutiesButton = new JButton(WiskOpdr.rb.getString("substitutiesButtonLabel"));
        substitutiesButton.setBounds(10,305,150,20);
        substitutiesButton.setMargin(new Insets(3,2,3,2));
        substitutiesButton.setFont(font);
        substitutiesButton.addActionListener(this);
        setLayer((Component)substitutiesButton, JLayeredPane.PALETTE_LAYER.intValue());
        add(substitutiesButton,0);
        
        antwoordFunctiesVak = new FormuleEditor(true);
        antwoordFunctiesVak.setBounds(125,270,570,150);
        antwoordFunctiesVak.setFont(font);
        antwoordFunctiesVak.addActionListener(this);
        antwoordFunctiesVak.setMultiLine(true);
        antwoordFunctiesVak.setResizable(true);
        setLayer((Component)antwoordFunctiesVak, JLayeredPane.POPUP_LAYER.intValue());
        
        functiesButton = new JButton(WiskOpdr.rb.getString("functiesButtonLabel"));
        functiesButton.setBounds(180,305,150,20);
        functiesButton.setMargin(new Insets(3,2,3,2));
        functiesButton.setFont(font);
        functiesButton.addActionListener(this);
        setLayer((Component)functiesButton, JLayeredPane.PALETTE_LAYER.intValue());
        add(functiesButton,0);
        
        antwoordSolveVak = new FormuleEditor(true);
        antwoordSolveVak.setBounds(15,130,480,150);
        antwoordSolveVak.setFont(font);
        antwoordSolveVak.addActionListener(this);
        antwoordSolveVak.setMultiLine(true);
        antwoordSolveVak.setResizable(true);
        setLayer((Component)antwoordSolveVak, JLayeredPane.POPUP_LAYER.intValue());
        
        solveButton = new JButton(WiskOpdr.rb.getString("antwoordmodelButtonLabel"));
        solveButton.setBounds(270,130,175,20);
        solveButton.setFont(font);
        solveButton.addActionListener(this);
        setLayer((Component)solveButton, JLayeredPane.PALETTE_LAYER.intValue());
        add(solveButton,0);
        
        solveTF = makeTextField(450,130,20,20,"x",true);
        setLayer((Component)solveTF, JLayeredPane.PALETTE_LAYER.intValue());
        
        feedbackCB = makeCheckBox(145,160,80,20,WiskOpdr.rb.getString("feedbackCBLabel"),false,true);
        
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
		        
        vormEditor = new FormuleEditor(true);
        vormEditor.setResizable(true);
        vormEditor.setMultiLine(true);
        vormEditor.setScrollHorizontal(true);
        vormEditor.setBounds(515,350,260,160);
        vormEditor.setFont(font);
        vormEditor.addActionListener(this);
        vormEditor.setVisible(false);
        add(vormEditor,0);
        //vormEditor.setResizable(true);
        
        
        ScoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
        puntenLabel = makeLabel(460,385,40,20,WiskOpdr.rb.getString("puntenLabel"),true);
        checkTotaalLabel = makeLabel(620,385,160,20,WiskOpdr.rb.getString("checkTotaalLabel"),false);
        checkTotaalLabel.setForeground(Color.red);
        
        gelijkwaardigCB = makeCheckBox(320,410,120,20,WiskOpdr.rb.getString("gelijkwaardigCBLabel"),true,true);
        gelijkwaardigCB.addMouseListener(this);
        vormCB = makeCheckBox(320,435,120,20,WiskOpdr.rb.getString("vormCBLabel"),false,true);
        exactCB = makeCheckBox(320,significantieAan?510:485,120,20,WiskOpdr.rb.getString("exactCBLabel"),false,true);
        significantCB = makeCheckBox(320,485,120,20,WiskOpdr.rb.getString("significantCBLabel"),false,significantieAan?true:false);
		stappenCB = makeCheckBox(630,50,200,20,WiskOpdr.rb.getString("stappenCBLabel"),true,false);
        bewerkingKnoppenCB = makeCheckBox(500,115,130,20,WiskOpdr.rb.getString("bewerkingKnoppenCBLabel"),false,true);
        bewerkingKnoppenExtraCB = makeCheckBox(500,135,230,20,WiskOpdr.rb.getString("bewerkingKnoppenExtraCBLabel"),false,true);
        abcKnopCB = makeCheckBox(630,115,60,20,WiskOpdr.rb.getString("abcCBLabel"),false,true);
        subKnopCB = makeCheckBox(690,115,100,20,WiskOpdr.rb.getString("subKnopCBLabel"),false,true);
        subKnopExtraCB = makeCheckBox(690,135,100,20,WiskOpdr.rb.getString("subKnopExtraCBLabel"),false,false);
        eindOplossingCB = makeCheckBox(320,460,120,20,WiskOpdr.rb.getString("eindOplossingCBLabel"),true,true);
        tipsCB = makeCheckBox(690,40,280,20,"Ideas [test]",false,true);
		formuleToolBijFocusCB = makeCheckBox(600,40,200,20,WiskOpdr.rb.getString("formuleToolCBLabel"),false,true);
		pijlCB = makeCheckBox(500,95,130,20,WiskOpdr.rb.getString("pijlCBLabel"),true,true);
		linStrategieVersieCB = makeCheckBox(500,40,190,15,WiskOpdr.rb.getString("strategieVersieLabel"),false,true);
		linOefenVersieCB = makeCheckBox(500,55,190,15,WiskOpdr.rb.getString("oefenVersieLabel"),false,true);
		bordjesMethodeCB = makeCheckBox(500,70,190,15,WiskOpdr.rb.getString("bordjesVersieLabel"),false,true);
		checkCB = makeCheckBox(5,5,200,20,WiskOpdr.rb.getString("checkCBLabel"),true,true);
        teltMeeCB = makeCheckBox(225,5,200,20,WiskOpdr.rb.getString("teltMeeCBLabel"),true,true);
        logCB = makeCheckBox(450,5,70,20,WiskOpdr.rb.getString("logCBLabel"),false,true);
        logIDField = makeTextField(520,5,60,20,"0",false);
        uitwCB = makeCheckBox(600,65,270,20,WiskOpdr.rb.getString("uitwCBLabel"),false,true);
        casAntwCB = makeCheckBox(700,160,270,20,WiskOpdr.rb.getString("casAntwCBLabel")+" [test]",false,true);
        boxMetRandCB = makeCheckBox(500,155,80,20,WiskOpdr.rb.getString("boxMetRand"),true,true);
        
        logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        logObjectivesButton.setBounds(600,5,120,20);
        if(WiskOpdr.objectives!=null)add(logObjectivesButton);
        
        ideasButton = new IdeasInstellingenButton();
        ideasButton.setFont(font);
        ideasButton.setBounds(695,65,80,20);
        ideasButton.setVisible(false);
        add(ideasButton);
        
        for (int i = 0; i < strategieDomeinNamen.length; i++) {
        	strategieOudNieuw.put(strategieDomeinNamen[i][0],strategieDomeinNamen[i][1]);
		}
        
        gelijkwaardigPV = makeTextField(460,410,30,20,""+puntenGelijkwaardig,true);
        vormPV = makeTextField(460,435,30,20,""+puntenVorm,false);
        eindOplossingPV = makeTextField(460,460,30,20,""+puntenEindOplossing,true);
        exactPV = makeTextField(460,significantieAan?510:485,30,20,""+puntenExact,false);
        significantPV = makeTextField(460,485,30,20,"0",false);
		feedbackPV = makeTextField(460,385,30,20,""+puntenFeedback,false);
        
        
        
        setFeedbackOption(false);
        
        if(soort==1)
        {   stappen = true;
       		stappenDefault = true;
        	formuleToolBijFocusCB.setVisible(false);
        	uitwCB.setVisible(false);
        }
        else if(soort==3)
        {   stappen = false;
        	stappenDefault = false;
            zetVergelijkingKnoppen(false);
            startLabel.setVisible(false);
            startEditor.setVisible(false);
            linStrategieVersieCB.setVisible(false);
            linOefenVersieCB.setVisible(false);
            bordjesMethodeCB.setVisible(false);
            //feedbackCB.setVisible(false);
            tipsCB.setVisible(false);
            formuleToolBijFocusCB.setVisible(true);
            solveButton.setVisible(false);
            solveTF.setVisible(false);
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
        boolean gelijkwaardig = true;
        boolean vorm = false;
        boolean eindOplossingNodig = true;
        boolean exact = false;
        boolean significant = false;
        int puntenFeedback = 0;
        String feedback = "";
        int feedbackWidth = 200;
		int feedbackHeight = 20;
        String vormString = "$f@";
        int goedHalfFout = 3;
        
        antwoordString = antwoordvak.geefFormuleVak().toString();
        gelijkwaardig = this.gelijkwaardig;
        vorm = this.vorm;
        eindOplossingNodig = this.eindOplossingNodig;
        exact = this.exact;
        significant = this.significant;
		
                
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
        h.put("vorm",new Boolean(vorm));
        h.put("eindOplossingNodig",new Boolean(eindOplossingNodig));
        h.put("exact",new Boolean(exact));
        h.put("significant",new Boolean(significant));
        h.put("puntenFeedback",new Integer(puntenFeedback));
        h.put("feedback",feedback);
        h.put("feedbackWidth",new Integer(feedbackWidth));
		h.put("feedbackHeight",new Integer(feedbackHeight));
		h.put("vormString",vormString);
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
		String vormString = "$f@";
        int goedHalfFout = 3;
        
        if(h!=null) 
        {   if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
            if(h.containsKey("gelijkwaardig")) gelijkwaardig = ((Boolean)h.get("gelijkwaardig")).booleanValue();
            if(h.containsKey("vorm")) vorm = ((Boolean)h.get("vorm")).booleanValue();
            if(h.containsKey("eindOplossingNodig")) eindOplossingNodig = ((Boolean)h.get("eindOplossingNodig")).booleanValue();
            if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
            if(h.containsKey("significant")) significant = ((Boolean)h.get("significant")).booleanValue();
			if(h.containsKey("stappen")) stappen = ((Boolean)h.get("stappen")).booleanValue();
            if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
            if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
            if(h.containsKey("feedbackWidth")) feedbackWidth = ((Integer)h.get("feedbackWidth")).intValue();
			if(h.containsKey("feedbackHeight")) feedbackHeight = ((Integer)h.get("feedbackHeight")).intValue();
            if(h.containsKey("vormString")) vormString = (String)h.get("vormString");
            if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
            
        }
        this.vorm = vorm;
        this.gelijkwaardig = gelijkwaardig;
        this.eindOplossingNodig = eindOplossingNodig;
        this.exact = exact;
        this.significant = significant;
		this.puntenFeedback = puntenFeedback;
        
        antwoordvak.geefFormuleVak().vulVak(antwoordString);
        
        String[] vormStrings = StringUtils.split(vormString, "::");
        for(int i=0 ; i<vormStrings.length ; i++)
    	{	if(i==0) vormStrings[i] = vormStrings[i] + "@";
    		else if(i==vormStrings.length-1) vormStrings[i] = "$f" + vormStrings[i];
    		else  vormStrings[i] = "$f" + vormStrings[i] + "@";
    	}
        vormEditor.verwijderRegels();
        vormEditor.zetRegels(vormStrings);
        
        //vormEditor.geefFormuleVak().vulVak(vormString);
            
        vormCB.setVisible(true);
        exactCB.setVisible(true);
        if(significantieAan) significantCB.setVisible(true);
		
                
        gelijkwaardigCB.setSelected(gelijkwaardig);
        vormCB.setSelected(vorm);
        eindOplossingCB.setSelected(eindOplossingNodig);
        exactCB.setSelected(exact);
        significantCB.setSelected(significant);
		
        
        vormEditor.setVisible(vorm);
        
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
    
    public Hashtable changeToCompatibleEditState(Hashtable interactiePanelLaunchState)
    {	Hashtable compatibleLaunchSate = new Hashtable();
    	compatibleLaunchSate.putAll(interactiePanelLaunchState);
    	compatibleLaunchSate.remove("startString");
    	compatibleLaunchSate.remove("stappen");
    	compatibleLaunchSate.remove("bewerkingKnoppen");
    	compatibleLaunchSate.remove("bewerkingKnoppenExtra");
    	compatibleLaunchSate.remove("abcKnop");
    	compatibleLaunchSate.remove("subKnop");
    	compatibleLaunchSate.remove("subKnopExtra");
    	compatibleLaunchSate.remove("formuleToolBijFocus");
    	compatibleLaunchSate.remove("pijl");
    	compatibleLaunchSate.remove("linStrategieVersie");
    	compatibleLaunchSate.remove("linOefenVersie");
    	compatibleLaunchSate.remove("bordjesMethode");
    	compatibleLaunchSate.remove("tips");
    	compatibleLaunchSate.remove("ideasInstellingen");
    	compatibleLaunchSate.remove("tipOpBalk");
    	compatibleLaunchSate.remove("hulpOpBalk");
    	compatibleLaunchSate.remove("stapOpBalk");
    	compatibleLaunchSate.remove("solveOpBalk");
    	compatibleLaunchSate.remove("tipBijFout");
    	compatibleLaunchSate.remove("meerTips");
    	compatibleLaunchSate.remove("feedbackBijFout");
    	compatibleLaunchSate.remove("hulpBijTip");
    	compatibleLaunchSate.remove("changedTexts");
    	compatibleLaunchSate.remove("strategieDomein");
    	compatibleLaunchSate.remove("uitw");
    	compatibleLaunchSate.remove("casAntw");
    	return compatibleLaunchSate;
   }
            
    public void setEditState(Hashtable interactiePanelLaunchState)
    {           String antwoordString = "$f@";
                String startString = "$f@";
                boolean vorm = false;
                boolean exact = false;
                boolean significant = false;
				boolean stappen = stappenDefault;
                int puntenGelijkwaardig = 10;
                int puntenVorm = 0;
                int puntenExact = 0;
                int puntenSignificant = 0;
				boolean eindOplossingNodig = true;
                int puntenEindOplossing = 10;
                boolean bewerkingKnoppen = false;
                boolean bewerkingKnoppenExtra = false;
                boolean abcKnop = false;
                boolean subKnop = false;
                boolean subKnopExtra = false;
				boolean formuleToolBijFocus = false;
                Hashtable[] answerModels = null;
                boolean hasFeedback = false;
                boolean feedbackSize = false;
                String vormString = "$f@";
                boolean tips = false;
                Hashtable ideasInstellingen = null;
                String strategieDomein = "";
                int feedbackModus = 0;
                String[] antwoordSubStrings = null;
                String[] antwoordFuncStrings = null;
                boolean pijl = true;
                boolean linStrategieVersie = false;
                boolean linOefenVersie = false;
                boolean bordjesMethode = false;
                boolean check = true;
                boolean teltMee = true;
                boolean logOption = false;
				String logID = "";
				boolean[][] logObjectives = null;
                boolean tipOpBalk = true;
                boolean hulpOpBalk = false;
                boolean stapOpBalk = false;
                boolean solveOpBalk = false;
                boolean meerTips = false;
                boolean tipBijFout = false;
                boolean feedbackBijFout = false;
                boolean hulpBijTip = false;
                Hashtable changedTexts = new Hashtable();
                double eqTestValueMin = 0;
				double eqTestValueMax = 5;
				boolean uitw = false;
				boolean casAntw = false;
				boolean boxMetRand = true;
				
                if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
                if(interactiePanelLaunchState.containsKey("startString")) startString = (String)interactiePanelLaunchState.get("startString");
                if(interactiePanelLaunchState.containsKey("vorm")) vorm = ((Boolean)interactiePanelLaunchState.get("vorm")).booleanValue();
                if(interactiePanelLaunchState.containsKey("exact")) exact = ((Boolean)interactiePanelLaunchState.get("exact")).booleanValue();
                if(interactiePanelLaunchState.containsKey("significant")) significant = ((Boolean)interactiePanelLaunchState.get("significant")).booleanValue();
				if(interactiePanelLaunchState.containsKey("stappen")) stappen = ((Boolean)interactiePanelLaunchState.get("stappen")).booleanValue();
                if(interactiePanelLaunchState.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)interactiePanelLaunchState.get("puntenGelijkwaardig")).intValue();
                if(interactiePanelLaunchState.containsKey("puntenVorm")) puntenVorm = ((Integer)interactiePanelLaunchState.get("puntenVorm")).intValue();
                if(interactiePanelLaunchState.containsKey("puntenExact")) puntenExact = ((Integer)interactiePanelLaunchState.get("puntenExact")).intValue();
                if(interactiePanelLaunchState.containsKey("puntenSignificant")) puntenSignificant = ((Integer)interactiePanelLaunchState.get("puntenSignificant")).intValue();
				if(interactiePanelLaunchState.containsKey("eindOplossingNodig")) eindOplossingNodig = ((Boolean)interactiePanelLaunchState.get("eindOplossingNodig")).booleanValue();
                if(interactiePanelLaunchState.containsKey("puntenEindOplossing")) puntenEindOplossing = ((Integer)interactiePanelLaunchState.get("puntenEindOplossing")).intValue();
                if(interactiePanelLaunchState.containsKey("bewerkingKnoppen")) bewerkingKnoppen = ((Boolean)interactiePanelLaunchState.get("bewerkingKnoppen")).booleanValue();
                if(interactiePanelLaunchState.containsKey("bewerkingKnoppenExtra")) bewerkingKnoppenExtra = ((Boolean)interactiePanelLaunchState.get("bewerkingKnoppenExtra")).booleanValue();
                if(interactiePanelLaunchState.containsKey("abcKnop")) abcKnop = ((Boolean)interactiePanelLaunchState.get("abcKnop")).booleanValue();
                if(interactiePanelLaunchState.containsKey("subKnop")) subKnop = ((Boolean)interactiePanelLaunchState.get("subKnop")).booleanValue();
                if(interactiePanelLaunchState.containsKey("subKnopExtra")) subKnopExtra = ((Boolean)interactiePanelLaunchState.get("subKnopExtra")).booleanValue();
                if(interactiePanelLaunchState.containsKey("formuleToolBijFocus")) formuleToolBijFocus = ((Boolean)interactiePanelLaunchState.get("formuleToolBijFocus")).booleanValue();
                if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
                if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
                if(interactiePanelLaunchState.containsKey("feedbackSize")) feedbackSize = ((Boolean)interactiePanelLaunchState.get("feedbackSize")).booleanValue();
				if(interactiePanelLaunchState.containsKey("vormString")) vormString = (String)interactiePanelLaunchState.get("vormString");
                if(interactiePanelLaunchState.containsKey("tips")) tips = ((Boolean)interactiePanelLaunchState.get("tips")).booleanValue();
                if(tips) {
                	if(interactiePanelLaunchState.containsKey("ideasInstellingen")) ideasInstellingen = (Hashtable)interactiePanelLaunchState.get("ideasInstellingen");
                    if(ideasInstellingen==null) { // voor de backwards comp.
                    	if(interactiePanelLaunchState.containsKey("tipOpBalk")) tipOpBalk = ((Boolean)interactiePanelLaunchState.get("tipOpBalk")).booleanValue();
                        if(interactiePanelLaunchState.containsKey("hulpOpBalk")) hulpOpBalk = ((Boolean)interactiePanelLaunchState.get("hulpOpBalk")).booleanValue();
                        if(interactiePanelLaunchState.containsKey("stapOpBalk")) stapOpBalk = ((Boolean)interactiePanelLaunchState.get("stapOpBalk")).booleanValue();
                        if(interactiePanelLaunchState.containsKey("solveOpBalk")) solveOpBalk = ((Boolean)interactiePanelLaunchState.get("solveOpBalk")).booleanValue();
                        if(interactiePanelLaunchState.containsKey("meerTips")) meerTips = ((Boolean)interactiePanelLaunchState.get("meerTips")).booleanValue();
                        if(interactiePanelLaunchState.containsKey("tipBijFout")) tipBijFout = ((Boolean)interactiePanelLaunchState.get("tipBijFout")).booleanValue();
                        if(interactiePanelLaunchState.containsKey("feedbackBijFout")) feedbackBijFout = ((Boolean)interactiePanelLaunchState.get("feedbackBijFout")).booleanValue();
                        if(interactiePanelLaunchState.containsKey("hulpBijTip")) hulpBijTip = ((Boolean)interactiePanelLaunchState.get("hulpBijTip")).booleanValue();
                        if(interactiePanelLaunchState.containsKey("changedTexts")) changedTexts = (Hashtable)interactiePanelLaunchState.get("changedTexts");
                        if(interactiePanelLaunchState.containsKey("strategieDomein")) strategieDomein = (String)interactiePanelLaunchState.get("strategieDomein");
                        
                    }
                }
                if(interactiePanelLaunchState.containsKey("antwoordSubStrings")) antwoordSubStrings = (String[])interactiePanelLaunchState.get("antwoordSubStrings");
                if(interactiePanelLaunchState.containsKey("antwoordFuncStrings")) antwoordFuncStrings = (String[])interactiePanelLaunchState.get("antwoordFuncStrings");
                if(interactiePanelLaunchState.containsKey("pijl")) pijl = ((Boolean)interactiePanelLaunchState.get("pijl")).booleanValue();
                if(interactiePanelLaunchState.containsKey("linStrategieVersie")) linStrategieVersie = ((Boolean)interactiePanelLaunchState.get("linStrategieVersie")).booleanValue();
                if(interactiePanelLaunchState.containsKey("linOefenVersie")) linOefenVersie = ((Boolean)interactiePanelLaunchState.get("linOefenVersie")).booleanValue();
                if(interactiePanelLaunchState.containsKey("bordjesMethode")) bordjesMethode = ((Boolean)interactiePanelLaunchState.get("bordjesMethode")).booleanValue();
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
				
                this.vorm = vorm;
                this.exact = exact;
                this.significant = significant;
				this.puntenGelijkwaardig = puntenGelijkwaardig;
                this.puntenVorm = puntenVorm;
                this.puntenExact = puntenExact;
                this.puntenSignificant = puntenSignificant;
				this.stappen = stappen;
                this.eindOplossingNodig = eindOplossingNodig;
                this.puntenEindOplossing = puntenEindOplossing;
                this.bewerkingKnoppen = bewerkingKnoppen;
                this.bewerkingKnoppenExtra = bewerkingKnoppenExtra;
                this.abcKnop = abcKnop;
                this.subKnop = subKnop;
                this.subKnopExtra = subKnopExtra;
                this.formuleToolBijFocus = formuleToolBijFocus;
                
                this.answerModels = new Hashtable[answerModels.length];
				for(int i=0 ; i<answerModels.length ; i++)
				{	this.answerModels[i] = answerModels[i];
				}
					
					//this.answerModels = answerModels;
                this.tips = tips;
                this.pijl = pijl;
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
                startEditor.geefFormuleVak().vulVak(startString);
                
                String[] vormStrings = StringUtils.split(vormString, "::");
                for(int i=0 ; i<vormStrings.length ; i++)
            	{	if(i==0) vormStrings[i] = vormStrings[i] + "@";
            		else if(i==vormStrings.length-1) vormStrings[i] = "$f" + vormStrings[i];
            		else  vormStrings[i] = "$f" + vormStrings[i] + "@";
            	}
                vormEditor.zetRegels(vormStrings);
                //vormEditor.geefFormuleVak().vulVak(vormString);
                
                antwoordSubstitutiesVak.zetRegels(antwoordSubStrings);
                antwoordFunctiesVak.zetRegels(antwoordFuncStrings);
                
                stappenCB.setSelected(stappen);
                abcKnopCB.setSelected(abcKnop);
                subKnopCB.setSelected(subKnop);
                subKnopExtraCB.setVisible(subKnop);
                subKnopExtraCB.setSelected(subKnopExtra);
                bewerkingKnoppenCB.setSelected(bewerkingKnoppen);
                bewerkingKnoppenExtraCB.setSelected(bewerkingKnoppenExtra);
                formuleToolBijFocusCB.setSelected(formuleToolBijFocus);
                pijlCB.setSelected(pijl);
                
                
                
               
                linStrategieVersieCB.setSelected(linStrategieVersie);
                linOefenVersieCB.setSelected(linOefenVersie);
                bordjesMethodeCB.setSelected(bordjesMethode);
                
                checkCB.setSelected(check);
                teltMeeCB.setSelected(teltMee);
                
                logCB.setSelected(logOption);
                logIDField.setVisible(logOption);
	            //logObjectivesButton.setVisible(logOption);
	            logIDField.setText(logID);
	            logObjectivesButton.setChoices(logObjectives);
                
                uitwCB.setSelected(uitw);
                casAntwCB.setSelected(casAntw);
                boxMetRandCB.setSelected(boxMetRand);
	            //startLabel.setVisible(uitw);
				//startEditor.setVisible(uitw);
                
                tipsCB.setSelected(tips);
                ideasButton.setVisible(tips);
                if(tips && ideasInstellingen !=null)
                {  	ideasButton.zetInstellingen(ideasInstellingen);
                }
                else if(tips)
                {	// converteer naar nieuwe opzet
                	ideasInstellingen = new Hashtable();
                	ideasInstellingen.put("tipOpBalk", new Boolean(tipOpBalk));
                	ideasInstellingen.put("hulpOpBalk", new Boolean(hulpOpBalk));
                	ideasInstellingen.put("stapOpBalk", new Boolean(stapOpBalk));
                	ideasInstellingen.put("solveOpBalk", new Boolean(solveOpBalk));
                	ideasInstellingen.put("meerTips", new Boolean(meerTips));
                	ideasInstellingen.put("tipBijFout", new Boolean(tipBijFout));
                	ideasInstellingen.put("feedbackBijFout", new Boolean(feedbackBijFout));
                	ideasInstellingen.put("hulpBijTip", new Boolean(hulpBijTip));
                	if(strategieOudNieuw.containsKey(strategieDomein)) strategieDomein = (String)strategieOudNieuw.get(strategieDomein);
                	ideasInstellingen.put("strategieDomein", strategieDomein);
                	
                	ideasButton.zetInstellingen(ideasInstellingen);
                }
                setFeedbackOption(hasFeedback);
                feedbackCB.setSelected(hasFeedback);
                feedbackSizeCB.setSelected(feedbackSize);
				feedbackEditor.setResizable(feedbackSize);
				
                if(hasFeedback)return;
                
                eindOplossingCB.setSelected(eindOplossingNodig);
                eindOplossingPV.setVisible(eindOplossingNodig);
                eindOplossingPV.setText(""+puntenEindOplossing);
                    
                gelijkwaardigPV.setText(""+puntenGelijkwaardig);
                                
                exactCB.setSelected(exact);
                exactPV.setVisible(exact);
                exactPV.setText(""+puntenExact);
                
                significantCB.setSelected(significant);
                significantPV.setVisible(significant && significantieAan);
                significantPV.setText(""+puntenSignificant);
                
                               
                vormCB.setSelected(vorm);
                vormPV.setVisible(vorm);
                vormPV.setText(""+puntenVorm);
                    
                vormEditor.setVisible(vorm);
                    
                exactCB.setSelected(exact);
                exactPV.setVisible(exact);
                exactPV.setText(""+puntenExact);
    }
    
    public Hashtable getEditState()
    {   
        Hashtable interactiePanelLaunchState = new Hashtable();
        
        
            String antwoordString = null;
            String startString = null;
            boolean vorm = false;
            boolean exact = false;
            boolean significant = false;
			boolean stappen = false;
            int puntenGelijkwaardig = 10;
            int puntenVorm = 0;
            int puntenExact = 0; 
            int puntenSignificant = 0; 
			boolean eindOplossingNodig = true;
            int puntenEindOplossing = 10;
            boolean bewerkingKnoppen = false;
            boolean bewerkingKnoppenExtra = false;
            boolean abcKnop = false;
            boolean subKnop = false;
            boolean subKnopExtra = false;
            int scoreMax = 0;
            int[][] scoreMaxObjectives = null;
            boolean formuleToolBijFocus = false;
            Hashtable[] answerModels;
            boolean hasFeedback;
            boolean feedbackSize;
            String vormString = "$f@";
            boolean tips;
            Hashtable ideasInstellingen = new Hashtable();
            int feedbackModus = 0;
            String[] antwoordSubStrings = null;
            String[] antwoordFuncStrings = null;
            boolean pijl = true;
            boolean linStrategieVersie = false;
            boolean linOefenVersie = false;
            boolean bordjesMethode = false;
            boolean check = true;
            boolean teltMee = true;
            boolean logOption = false;
			String logID = "";
			boolean[][] logObjectives = null;
            double eqTestValueMin = 0;
			double eqTestValueMax = 5;
			boolean uitw = false;
			boolean casAntw = false;
			boolean boxMetRand = true;
			
            getAnswerModel();
            answerModels = this.answerModels;
            if(answerModels!=null)setAnswerModel(answerModels[0]);
            
            antwoordString = antwoordvak.geefFormuleVak().toString();
            startString = startEditor.geefFormuleVak().toString();
            String[] vormStrings = vormEditor.geefRegels();
            if(vormStrings.length==1) vormString = vormEditor.geefFormuleVak().toString();
            else
            {	vormString = "$f";
            	for(int i=0 ; i<vormStrings.length ; i++)
            	{	vormString = vormString + vormStrings[i].substring(2,vormStrings[i].length()-1) + "::";
            	}
            	vormString = vormString.substring(0,vormString.length()-2) + "@";
            }
            vorm = this.vorm;
            exact = this.exact;
            significant = this.significant;
			stappen = this.stappen;
            tips = this.tips;
            if(tips)ideasInstellingen = ideasButton.geefInstellingen();
            
            try
            {   puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
                this.puntenGelijkwaardig = puntenGelijkwaardig;
            }   
            catch(Exception ex)
            {   }
            try
            {   puntenVorm = Integer.parseInt(vormPV.getText());
                this.puntenVorm = puntenVorm;
            }   
            catch(Exception ex)
            {   }
            try
            {   puntenExact = Integer.parseInt(exactPV.getText());
                this.puntenExact = puntenExact;
            }   
            catch(Exception ex)
            {   }
            try
			{	puntenSignificant = Integer.parseInt(significantPV.getText());
				this.puntenSignificant = puntenSignificant;
			}	
			catch(Exception ex)
			{	}
            try
            {   puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
                this.puntenEindOplossing = puntenEindOplossing;
            }   
            catch(Exception ex)
            {   }
            puntenGelijkwaardig = this.puntenGelijkwaardig;
            puntenVorm = this.puntenVorm;
            puntenExact = this.puntenExact;
            puntenSignificant = this.puntenSignificant;
			eindOplossingNodig = this.eindOplossingNodig;
            puntenEindOplossing = this.puntenEindOplossing;
            bewerkingKnoppen = this.bewerkingKnoppen;
            bewerkingKnoppenExtra = this.bewerkingKnoppenExtra;
            abcKnop = this.abcKnop;
            subKnop = this.subKnop;
            subKnopExtra = this.subKnopExtra;
            pijl = this.pijl;
            formuleToolBijFocus = this.formuleToolBijFocus;
            scoreMax = puntenGelijkwaardig + puntenVorm + puntenEindOplossing + puntenExact;
            hasFeedback = this.hasFeedback;
            if(hasFeedback)scoreMax = puntenFeedback;
            feedbackSize = feedbackSizeCB.isSelected();
            
            //if(tips) feedbackModus = feedbackModusKeuze.getSelectedIndex();
            
            antwoordSubStrings = antwoordSubstitutiesVak.geefRegels();
            antwoordFuncStrings = antwoordFunctiesVak.geefRegels();
           	
    		for (int i = 0; i < antwoordFuncStrings.length; i++)
    		{
    			if(antwoordFuncStrings[i]==null || antwoordFuncStrings[i].equals("$f@"))
    				break;
    			String[] functieDelen = antwoordFuncStrings[i].split("=");
    			if(functieDelen.length!=2) {
    				JOptionPane.showMessageDialog(this, "Syntax van functiedefinitie klopt niet");
    				break;
    			}
    			//String functieExpressieString = "$f"+functieDelen[1];
    			//Expressie functieExpressie = FormuleParser.geefExpressie(functieExpressieString);
    			//if(functieExpressie==null) {
    			//	JOptionPane.showMessageDialog(this, "Syntax van functie-expressie klopt niet");
    			//	break;
    			//}
    			System.out.println(functieDelen[0].substring(2));
    			/*String pattern = "[a-zA-Z]+[']?[(][a-zA-Z][)]";
    	        boolean matches = Pattern.matches(pattern, functieDelen[0].substring(2));
    	        if(!matches)
    	        {	JOptionPane.showMessageDialog(this, "Syntax klopt niet. Gebruik bv:\n f(x)=expressie \n of \n func(x)=expressie");
    	        	break;
    	        }*/
    		}
    		
            
            linStrategieVersie = linStrategieVersieCB.isSelected();
            linOefenVersie = linOefenVersieCB.isSelected();
            bordjesMethode = bordjesMethodeCB.isSelected();
            
            if(linStrategieVersie || linOefenVersie) bewerkingKnoppen = true;
            
            check = checkCB.isSelected();
            teltMee = teltMeeCB.isSelected();
            logOption = logCB.isSelected();
			logID = logIDField.getText();
			logObjectives = logObjectivesButton.getChoices();
			
			uitw = uitwCB.isSelected();
			casAntw = casAntwCB.isSelected();
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
            interactiePanelLaunchState.put("startString",startString);
            interactiePanelLaunchState.put("vorm",new Boolean(vorm));
            interactiePanelLaunchState.put("exact",new Boolean(exact));
            interactiePanelLaunchState.put("significant",new Boolean(significant));
			interactiePanelLaunchState.put("stappen",new Boolean(stappen));
            interactiePanelLaunchState.put("puntenGelijkwaardig",new Integer(puntenGelijkwaardig));
            interactiePanelLaunchState.put("puntenVorm",new Integer(puntenVorm));
            interactiePanelLaunchState.put("puntenExact",new Integer(puntenExact));
            interactiePanelLaunchState.put("puntenSignificant",new Integer(puntenSignificant));
			interactiePanelLaunchState.put("eindOplossingNodig",new Boolean(eindOplossingNodig));
            interactiePanelLaunchState.put("puntenEindOplossing",new Integer(puntenEindOplossing));
            interactiePanelLaunchState.put("bewerkingKnoppen",new Boolean(bewerkingKnoppen));
            interactiePanelLaunchState.put("bewerkingKnoppenExtra",new Boolean(bewerkingKnoppenExtra));
            interactiePanelLaunchState.put("abcKnop",new Boolean(abcKnop));
            interactiePanelLaunchState.put("subKnop",new Boolean(subKnop));
            interactiePanelLaunchState.put("subKnopExtra",new Boolean(subKnopExtra));
            interactiePanelLaunchState.put("formuleToolBijFocus",new Boolean(formuleToolBijFocus));
            interactiePanelLaunchState.put("scoreMax",new Integer(scoreMax));
            if(answerModels!=null)interactiePanelLaunchState.put("answerModels",answerModels);
            interactiePanelLaunchState.put("hasFeedback",new Boolean(hasFeedback));
            interactiePanelLaunchState.put("feedbackSize",new Boolean(feedbackSize));
			interactiePanelLaunchState.put("vormString",vormString);
            interactiePanelLaunchState.put("tips",new Boolean(tips));
            if(tips){
            	interactiePanelLaunchState.put("ideasInstellingen",ideasInstellingen);
            }
            interactiePanelLaunchState.put("antwoordSubStrings",antwoordSubStrings);
            interactiePanelLaunchState.put("antwoordFuncStrings",antwoordFuncStrings);
            interactiePanelLaunchState.put("pijl",new Boolean(pijl));
            interactiePanelLaunchState.put("linStrategieVersie",new Boolean(linStrategieVersie));
            interactiePanelLaunchState.put("linOefenVersie",new Boolean(linOefenVersie));
            interactiePanelLaunchState.put("bordjesMethode",new Boolean(bordjesMethode));
            interactiePanelLaunchState.put("check",new Boolean(check));
            interactiePanelLaunchState.put("teltMee",new Boolean(teltMee));
            interactiePanelLaunchState.put("logOption",new Boolean(logOption));
			interactiePanelLaunchState.put("logID",logID);     
			interactiePanelLaunchState.put("eqTestValueMin",new Double(eqTestValueMin));
			interactiePanelLaunchState.put("eqTestValueMax",new Double(eqTestValueMax));
			interactiePanelLaunchState.put("uitw",new Boolean(uitw));
			interactiePanelLaunchState.put("casAntw",new Boolean(casAntw));
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
        else if(e.getSource()==tipsCB)
		{	tips = tipsCB.isSelected();
			ideasButton.setVisible(tips);
			//if(tips)presentRules();
		}
        else if(e.getSource()==gelijkwaardigCB)
        {   gelijkwaardig = gelijkwaardigCB.isSelected();
            if(!hasFeedback && answerModelNr==0)gelijkwaardigPV.setVisible(gelijkwaardig);
            
        }
        else if(e.getSource()==vormCB)
        {   boolean b = vormCB.isSelected();
            vorm = b; 
            if(!hasFeedback && answerModelNr==0)vormPV.setVisible(b);
            vormEditor.setVisible(b);
            if(b)
            {   eindOplossingNodig = false;
            	eindOplossingCB.setSelected(false);
            	eindOplossingPV.setText("0");
            	eindOplossingPV.setVisible(false);
            	puntenEindOplossing = 0;
            	
            	exact = false;
            	exactCB.setSelected(false);
            	exactPV.setText("0");
            	exactPV.setVisible(false);
            	puntenExact = 0;
            	
            	gelijkwaardigPV.setText("0");
            	puntenGelijkwaardig = 0;
            	
            	vormPV.setText("10");
            	//vormPV.setVisible(true);
            	puntenVorm = 10;
                
            }
            else
            {
            	gelijkwaardigPV.setText("10");
            	puntenGelijkwaardig = 10;
            }
            /*
            if(b && !eindOplossingNodig)
            {   gelijkwaardigPV.setText("0");
                puntenGelijkwaardig = 0;
                vormPV.setText("10");
                puntenVorm = 10;
            }
            if(!b && !eindOplossingNodig)
            {   gelijkwaardigPV.setText("10");
                puntenGelijkwaardig = 10;
                vormPV.setText("0");
                puntenVorm = 0;
            }
            */
        }
        else if(e.getSource()==exactCB)
        {   boolean b = exactCB.isSelected();
            exact = b;
            if(!hasFeedback && answerModelNr==0)exactPV.setVisible(b);
            
            if(b)
            {   eindOplossingNodig = true;
                eindOplossingCB.setSelected(true);
                if(!hasFeedback)eindOplossingPV.setVisible(true);
                
                vorm = false;
                vormCB.setSelected(false);
            	vormPV.setVisible(false);
            	vormEditor.setVisible(false);
                
                gelijkwaardigPV.setText("0");
                vormPV.setText("0");
                eindOplossingPV.setText("0");
                exactPV.setText("10");
                
                puntenEindOplossing = 0;
                puntenGelijkwaardig = 0;
                puntenExact = 10;
            }
            else
            {   
                gelijkwaardigPV.setText("0");
                vormPV.setText("0");
                eindOplossingPV.setText("10");
                exactPV.setText("0");
            
                puntenGelijkwaardig = 0;
                puntenVorm = 0;
                puntenEindOplossing = 10;
                puntenExact = 0;
            }   
        }
        else if(e.getSource()==significantCB)
		{
			boolean b = significantCB.isSelected();
			significant = b;
			if(!hasFeedback && answerModelNr==0)significantPV.setVisible(b);
			if(b)
	        {   eindOplossingNodig = true;
	            eindOplossingCB.setSelected(true);
	            if(!hasFeedback)eindOplossingPV.setVisible(true);
	            eindOplossingPV.setText("10");
	            puntenEindOplossing = 10;
	        }
		}
        else if(e.getSource()==eindOplossingCB)
        {   boolean b = eindOplossingCB.isSelected();
            eindOplossingNodig = b;
            if(!hasFeedback && answerModelNr==0)eindOplossingPV.setVisible(b);
            if(b)
            {   vorm = false;
            	vormCB.setSelected(false);
            	vormPV.setVisible(false);
            	vormEditor.setVisible(false);
            	gelijkwaardigPV.setText("0");
                vormPV.setText("0");
                eindOplossingPV.setText("10");
                exactPV.setText("0");
                
                puntenGelijkwaardig = 0;
                puntenVorm = 0;
                puntenEindOplossing = 10;
                puntenExact = 0;
            }
            else
            {   exactCB.setSelected(false);
                exactPV.setVisible(false);
                
                significantCB.setSelected(false);
              	significantPV.setVisible(false);
                
                gelijkwaardigPV.setText("10");
                if(vorm)
                {   gelijkwaardigPV.setText("0");
                    vormPV.setText("10");
                    puntenGelijkwaardig = 0;
                    puntenVorm = 10;
                }
                else
                {   gelijkwaardigPV.setText("10");
                    vormPV.setText("0");
                    puntenGelijkwaardig = 10;
                    puntenVorm = 0;
                }
                eindOplossingPV.setText("0");
                exactPV.setText("0");
                significantPV.setText("0");
                
                puntenEindOplossing = 0;
                puntenExact = 0;
                puntenSignificant = 0;
            }
        }
        else if(e.getSource()==stappenCB)
        {   boolean b = stappenCB.isSelected();
            stappen = b;
        }
        else if(e.getSource()==bewerkingKnoppenCB)
        {   boolean b = bewerkingKnoppenCB.isSelected();
            bewerkingKnoppen = b;
        }
        else if(e.getSource()==bewerkingKnoppenExtraCB)
        {   boolean b = bewerkingKnoppenExtraCB.isSelected();
            bewerkingKnoppenExtra = b;
        }
        else if(e.getSource()==abcKnopCB)
        {   boolean b = abcKnopCB.isSelected();
            abcKnop = b;
        }
        else if(e.getSource()==subKnopCB)
	    {   subKnop = subKnopCB.isSelected();
	    	subKnopExtraCB.setVisible(subKnop);
	    	if(!subKnop)subKnopExtraCB.setSelected(subKnop);
	    }
		else if(e.getSource()==subKnopExtraCB)
	    {   subKnopExtra = subKnopExtraCB.isSelected();
	    }
        else if(e.getSource()==pijlCB)
        {   pijl = pijlCB.isSelected();
        }
        else if(e.getSource()==linStrategieVersieCB)
        {   if(linStrategieVersieCB.isSelected()) {
        		linOefenVersieCB.setSelected(false);
        		bordjesMethodeCB.setSelected(false);
    		}
        }
        else if(e.getSource()==linOefenVersieCB)
        {   if(linOefenVersieCB.isSelected()){ 
        		linStrategieVersieCB.setSelected(false);
        		bordjesMethodeCB.setSelected(false);
    		}
        }
        else if(e.getSource()==bordjesMethodeCB)
        {   if(bordjesMethodeCB.isSelected()) {
        		linStrategieVersieCB.setSelected(false);
        		linOefenVersieCB.setSelected(false);
        	}
        }
        else if(e.getSource()==formuleToolBijFocusCB)
        {   boolean b = formuleToolBijFocusCB.isSelected();
            formuleToolBijFocus = b;
        }
        else if(e.getSource() == antwoordSubstitutiesVak)
        {  	remove(antwoordSubstitutiesVak);
        	repaint();
        }
        else if(e.getSource() == substitutiesButton)
        {  	add(antwoordSubstitutiesVak,0);
        	repaint();
        }
        else if(e.getSource() == antwoordFunctiesVak)
        {  	remove(antwoordFunctiesVak);
        	repaint();
        }
        else if(e.getSource() == functiesButton)
        {  	add(antwoordFunctiesVak,0);
        	repaint();
        }
        else if(e.getSource() == antwoordSolveVak)
        {  	remove(antwoordSolveVak);
        	repaint();
        }
        else if(e.getSource() == solveButton)
        {  	
        	
        	String vergStringE = startEditor.geefFormuleVak().toString();
        	String vergString = StringUtils.replaceStr(vergStringE,"#","");
        	boolean rand = vergStringE.length() != vergString.length();
        	//System.out.println(vergStringE);
        	//System.out.println(vergString);
        	
        	VergelijkingMeerv vm = FormuleParser.parseVergelijking(vergString);
        	String vergStringCas = "$f@";
        	
        	VergelijkingMeerv vmAntw = null;
        	if(vm!=null)
        	{
        		Vergelijking v = vm.geefVergelijking(0);
        		vergStringCas = v.geefExpLinks().toStringCAS() + "==" + v.geefExpRechts().toStringCAS();
        		vmAntw = // Expressie.solveWithCAS(vergStringCas, solveTF.getText());
        				Expressie.solve(v, solveTF.getText());
        		String def = "";
        		if(rand && vmAntw!=null)
        		{
        			Vergelijking[] vs = new Vergelijking[vmAntw.geefAantal()];
        			for(int i=0 ; i<vmAntw.geefAantal() ; i++)
        			{	if(i>0) def = def + "  "+WiskOpdr.rb.getString("ofLabel")+"  ";
        				vs[i] = vmAntw.geefVergelijking(i);
        				def = def + vs[i].geefExpLinks().toString() + " = #" + vs[i].geefExpRechts().toString() + "#";
        			}
        			
        		}
        		else if(vmAntw!=null) def = vmAntw.toString();
        		antwoordSolveVak.geefFormuleVak().vulVak("$f"+def+"@");
        		
        	}
        	add(antwoordSolveVak,0);
        }
        
        else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());   
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
        else if(e.getSource()==uitwCB)
	    {   //startLabel.setVisible(uitwCB.isSelected());
			//startEditor.setVisible(uitwCB.isSelected());
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
            int puntenGelijkwaardig = 0;
            int puntenHerleiding = 0;
            int puntenExact = 0;
            int puntenSignificant = 0;
			int puntenEindOplossing = 0;
            
            if(e.getSource()==gelijkwaardigPV)
            {   try
                {   puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
                    this.puntenGelijkwaardig = puntenGelijkwaardig;
                }   
                catch(Exception ex)
                {   }
            }
            if(e.getSource()==vormPV)
            {   try
                {   puntenHerleiding = Integer.parseInt(vormPV.getText());
                    this.puntenVorm = puntenVorm;
                }   
                catch(Exception ex)
                {   }
            }
            if(e.getSource()==exactPV)
            {   try
                {   puntenExact = Integer.parseInt(exactPV.getText());
                    this.puntenExact = puntenExact;
                }   
                catch(Exception ex)
                {   }
            }
            if(e.getSource()==significantPV)
			{	try
				{	puntenSignificant = Integer.parseInt(significantPV.getText());
					this.puntenSignificant = puntenSignificant;
				}	
				catch(Exception ex)
				{	}
			}
            if(e.getSource()==eindOplossingPV)
            {   try
                {   puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
                    this.puntenEindOplossing = puntenEindOplossing;
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
        puntenLabel.setVisible(!b);
        
        gelijkwaardigPV.setVisible(!b);
        if(b || vorm)vormPV.setVisible(!b);
        if(b || eindOplossingNodig) eindOplossingPV.setVisible(!b);
        if(b || exact)exactPV.setVisible(!b);
        if(b || significant && significantieAan) significantPV.setVisible(!b);
		
        
        //eindOplossingCB.setVisible(!b);
        //vormCB.setVisible(b);
        
        answerModelNr = 0;
        tabbladTab.setSelected(answerModelNr+1);
        if(b)setAnswerModel();
    }
    
    
    public void zetVergelijkingKnoppen(boolean b)
    {   bewerkingKnoppenCB.setVisible(b);
    	bewerkingKnoppenExtraCB.setVisible(b);
    	pijlCB.setVisible(b);
        abcKnopCB.setVisible(b);
        subKnopCB.setVisible(b);
        
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
    
    public void mousePressed(MouseEvent e)
    {   if(e.getSource()==gelijkwaardigCB && e.getModifiers()== InputEvent.BUTTON3_MASK || e.isControlDown())
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
