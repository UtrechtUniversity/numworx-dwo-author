package fi.wiskopdr.templatecomponents;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.iconan.Iconan;
import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.ObjectiveChoiceButton;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrButton;
import fi.wiskopdr.WiskOpdrCheckbox;
import fi.wiskopdr.WiskOpdrComboBox;
import fi.wiskopdr.WiskOpdrTextField;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstVak;

public class MultipleChoiceEditor implements TComponentEditor, ActionListener, FocusListener {

	private TekstVak tekstVak;
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	private int scoreMax;
	private boolean multiselections;
	private int aantalSelectables = 4;
	private int aantalSelectablesMax = 30;
	private boolean[][][] logMisconceptions;
	
	private DialogFacade frame;
	private JPanel preferencesPanel;
	private JPanel topPanel, mainPanel, bottomPanel;
	
	//topPanel
	private JLabel titleLabel;
	
	//mainPanel  //juiste antwoord
	private JLabel titleAntwoordLabel;
	private JCheckBox[] selectableCheckboxes;
	private Box selectableCBBox;
	private ObjectiveChoiceButton[] logMisconceptionsButtons;
	
	// instellingen
	private JLabel titleSettingsLabel;
	private JLabel itemCountLabel;
	private JTextField itemCountTF;
	private JCheckBox multiSelectionsCB;
	private JCheckBox hasPrefixCB;
	private JLabel listNumberTypeLabel;
	private JComboBox listNumberTypeComboBox;
	private JLabel tabWidthLabel;
	private JTextField tabWidthTF;
	private JLabel rowSpaceLabel;
	private JTextField rowSpaceTF;
	private JLabel imageKnopLabel;
	private FormuleButton knopImageButton;
	private Image knopImage;
	private Dialog imageDialog;
	private Iconan iconman;
	private String knopImageString = "";
	
	// logging /nakijken
	private JLabel titleLoggingLabel;
	private JLabel maxScoreLabel;
	private JTextField maxScoreTF;
	private JCheckBox checkCB;
	private JCheckBox teltMeeCB;
	private JCheckBox logCB;
	private JTextField logIDField;
	private JTextField logIDLabelField;
	private JLabel logIDLabelLabel;
	private ObjectiveChoiceButton logObjectivesButton;
	
	// bottomPanel
	private JButton okButton, cancelButton;
	private JTextField breedteTF, hoogteTF;
    private JLabel breedteLabel, hoogteLabel;
    private JCheckBox volledigeBreedteCB;
	
    
	public MultipleChoiceEditor(TekstVak tekstVak) {
		this.tekstVak = tekstVak;
		makeGUI();
		makeFrame();
	}
	
	public void setTekstVak(TekstVak tekstVak) {
		this.tekstVak = tekstVak;
	}
	
	private void makeGUI() {
		preferencesPanel = new JPanel(new BorderLayout());
		
		topPanel = new JPanel();
		topPanel.setBackground(WiskOpdr.colorBlue1);
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));
		
		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(WiskOpdr.colorGray2);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		//topPanel
		titleLabel = makeLabel(WiskOpdr.rb.getString("TCOMP_multip_settings"), new Font("SansSerif",Font.PLAIN, 24));
		titleLabel.setForeground(WiskOpdr.colorGray3);
		topPanel.add(titleLabel);
		
		//mainPanel  //juiste antwoord
		titleAntwoordLabel = makeLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"), font.deriveFont(Font.BOLD, 16));
		selectableCheckboxes = new JCheckBox[aantalSelectablesMax];
    	logMisconceptionsButtons = new ObjectiveChoiceButton[aantalSelectablesMax];
    	selectableCBBox = Box.createVerticalBox();
    	maakCheckboxes();
    	
		//mainPanel  //settings
		titleSettingsLabel = makeLabel(WiskOpdr.rb.getString("settingsLabel"), font.deriveFont(Font.BOLD, 16));
		itemCountLabel = makeLabel(WiskOpdr.rb.getString("TCOMP_multip_rowCount"), font);
		itemCountTF = makeTextField("", 30, 22, this);
		multiSelectionsCB = makeCheckBox(WiskOpdr.rb.getString("meervSelectiesLabel"), false, this);//("Meervoudige selecties mogelijk");
		hasPrefixCB = makeCheckBox(WiskOpdr.rb.getString("TCOMP_multip_hasPrefix"), true, this);
		listNumberTypeLabel = makeLabel(WiskOpdr.rb.getString("TCOMP_multip_numberType"), font);
		
		listNumberTypeComboBox = new WiskOpdrComboBox();
		listNumberTypeComboBox.setFont(font);
		listNumberTypeComboBox.setPreferredSize(new Dimension(80,22));
		listNumberTypeComboBox.addItem(WiskOpdr.rb.getString("TCOMP_multip_chooseType"));
		for(int i=0 ; i<MultipleChoiceGenerator.listNumbers.length ; i++) {
			listNumberTypeComboBox.addItem(MultipleChoiceGenerator.listNumbers[i][0]+" ,"+MultipleChoiceGenerator.listNumbers[i][1]+" ,"+MultipleChoiceGenerator.listNumbers[i][2]+" , ...");
		}
		tabWidthLabel = makeLabel(WiskOpdr.rb.getString("TCOMP_multip_tabWidth"),font);
		tabWidthTF = makeTextField("",30,22,this);
		rowSpaceLabel = makeLabel(WiskOpdr.rb.getString("TCOMP_multip_rowSpace"), font);
		rowSpaceTF = makeTextField("", 30, 22, this);
		imageKnopLabel = makeLabel(WiskOpdr.rb.getString("editImageKnopLabel"), font);
		
		knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setPreferredSize(new Dimension(80,22));
		knopImageButton.addActionListener(this);
			
		//mainPanel  // logging / nakijken
		titleLoggingLabel = makeLabel(WiskOpdr.rb.getString("FEV_titleLoggingLabel"),font.deriveFont(Font.BOLD, 16));
		maxScoreLabel = makeLabel(WiskOpdr.rb.getString("score"), font);
		maxScoreTF = makeTextField("0",50,22,this);
		checkCB = makeCheckBox(WiskOpdr.rb.getString("checkCBLabel"),true, null);
		teltMeeCB = makeCheckBox(WiskOpdr.rb.getString("teltMeeCBLabel"), true, null);
		logCB = makeCheckBox(WiskOpdr.rb.getString("logCBLabel"), false, this);
		logIDField = makeTextField("",50,22,this);
		logIDField.setVisible(false);
		logIDLabelLabel = makeLabel(WiskOpdr.rb.getString("TVEP_logIDLabelLabel"),font);
		logIDLabelLabel.setVisible(false);
		logIDLabelField = makeTextField("",50,22,this);
		logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString, WiskOpdr.studentModel);
        logObjectivesButton.setPreferredSize(new Dimension(120,22));
        logObjectivesButton.setMaximumSize(new Dimension(120,22));
                
        //plaats componenten mainPanel
        Component[] r11 = {titleAntwoordLabel, 	hgl()};
		Component[] r12 = {selectableCBBox, 	hgl()};
		
		Component[] k1 = {hb(r11), vst(15), hb(r12), vst(15)};
		
		Component[] r21 = {titleSettingsLabel, 	hgl()};
		Component[] r22 = {itemCountLabel, 		ra(10,10), 	hgl(), 	itemCountTF};
		Component[] r23 = {multiSelectionsCB, 	hgl()};
		Component[] r24 = {hasPrefixCB, 		hgl()};
		Component[] r25 = {listNumberTypeLabel, ra(10,10), 	hgl(), 	listNumberTypeComboBox};
		Component[] r26 = {tabWidthLabel, 		ra(10,10), 	hgl(), 	tabWidthTF};
		Component[] r27 = {rowSpaceLabel, 		ra(10,10), 	hgl(), 	rowSpaceTF};
		Component[] r28 = {imageKnopLabel, 		ra(5,5), 	hgl(), 	knopImageButton};
		
		Component[] k2 = {hb(r21), vst(15), hb(r22), vst(5), hb(r23), vst(5), hb(r24), vst(5), 
				hb(r25), vst(5), hb(r26), vst(5), hb(r27), vst(5), hb(r28), vst(5), vgl()};
		
		Component[] r31 = {titleLoggingLabel, 	hgl()};
		Component[] r32 = {maxScoreLabel, 		ra(5,10), maxScoreTF, hgl()};
		Component[] r33 = {checkCB, 			hgl()};
		Component[] r34 = {teltMeeCB, 			hgl()};
		Component[] r35 = {logCB, 				ra(5,10), logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField, hgl()};
		Component[] r36 = {logObjectivesButton, hgl()};
		
		Component[] k3 = {hb(r31), vst(15), hb(r32), vst(5), hb(r33), vst(5), hb(r34), vst(5), hb(r35), vst(10), hb(r36), vgl()};
		
		Component[] main = {vb(k1), hst(50), vb(k2), hst(50), vb(k3)};
		mainPanel.add(hb(main));
		
		// bottomPanel
		okButton = makeButton("Ok",this);//
		okButton.setPreferredSize(new Dimension(70,24));
		okButton.setBackground(WiskOpdr.colorBlue1);
		okButton.setForeground(WiskOpdr.colorGray3);
		
		cancelButton = makeButton("Cancel", this);//
		cancelButton.setPreferredSize(new Dimension(70,24));
		cancelButton.setBackground(WiskOpdr.colorBlue1);
		cancelButton.setForeground(WiskOpdr.colorGray3);
		
		breedteLabel = makeLabel(WiskOpdr.rb.getString("breedteLabel"),font);
		breedteTF = makeTextField("300", 40, 22, this);
        breedteTF.setEnabled(false);
        hoogteLabel = makeLabel(WiskOpdr.rb.getString("hoogteLabel"), font);
        hoogteTF = makeTextField("250", 40,22, this);
        hoogteTF.setEnabled(false);
        volledigeBreedteCB = makeCheckBox(WiskOpdr.rb.getString("volleBreedteLabel"),true,this);
       
        // plaats componenten bottomPanel
        Component[] comp = {okButton, hst(20), cancelButton, hst(20), breedteLabel, hst(5), breedteTF, hst(10), hoogteLabel, hst(5), hoogteTF, hst(20), volledigeBreedteCB, hgl()};
        bottomPanel.add(hb(comp));
		
		preferencesPanel.add(topPanel,BorderLayout.NORTH);
		preferencesPanel.add(bottomPanel,BorderLayout.SOUTH);
		preferencesPanel.add(mainPanel,BorderLayout.CENTER);
	}
	
	private JCheckBox makeCheckBox (String text, boolean selected, ActionListener al) {
		JCheckBox cb = new WiskOpdrCheckbox(text);
		if(al!=null) cb.addActionListener(al);
		cb.setSelected(selected);
		return cb;
	}
	
	private JButton makeButton (String text,  ActionListener al) {
		JButton bt = new WiskOpdrButton(text);
		if(al!=null) bt.addActionListener(al);
		return bt;
	}
	
	private JLabel makeLabel (String text, Font f) {
		JLabel lb = new JLabel(text);
		lb.setFont(f);
		lb.setForeground(WiskOpdr.colorBlue1);
		return lb;
	}
	
	private JTextField makeTextField (String text, int prefWidth, int prefHeight, ActionListener al) {
		JTextField tf = new WiskOpdrTextField(text);
		tf.setPreferredSize(new Dimension(prefWidth,prefHeight));
		if(al!=null) {
			tf.addActionListener(al);
			tf.addFocusListener(this);
		}
		return tf;
	}
	
	private Box hb(Component[] c) {
		Box box = Box.createHorizontalBox();
		for(int i=0 ; c!=null && i<c.length ; i++) 
			box.add(c[i]);
		return box;
	}
	
	private Box vb(Component[] c) {
		Box box = Box.createVerticalBox();
		for(int i=0 ; c!=null && i<c.length ; i++) 
			box.add(c[i]);
		return box;
	}
	
	private Component hgl() {
		return Box.createHorizontalGlue();
	}
	
	private Component vgl() {
		return Box.createVerticalGlue();
	}
	
	private Component hst(int n) {
		return Box.createHorizontalStrut(n);
	}
	
	private Component vst(int n) {
		return Box.createVerticalStrut(n);
	}
	
	private Component ra(int w, int h) {
		return Box.createRigidArea(new Dimension(w,h));
	}
	
	private int intFromText(int defaultInt, String text) {
		int i = defaultInt;
		try {
			i = Integer.parseInt(text);
		}
		catch(NumberFormatException e) {}
		return i;
	}
	
	public void makeFrame(){
	   	frame = DialogFacade.newInstance(tekstVak, WiskOpdr.rb.getString("TCOMP_multip"), true);
	    frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.getContentPane().add(preferencesPanel);
	    frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	    frame.pack();
	    Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
 	    int xD = (screenSize.width-frame.getSize().width)/2;
 	    int yD = (screenSize.height-frame.getSize().height)/2;
 	    frame.setLocation(xD, yD);
	}
	
	public void maakCheckboxes() {
		if(WiskOpdr.misconceptions!=null)
			logMisconceptions = new boolean[aantalSelectables][][];
				
		for(int i=0 ; i<aantalSelectables ; i++) {
			Box regelBox = Box.createHorizontalBox();
			if(selectableCheckboxes[i]==null) {   
				selectableCheckboxes[i] = new WiskOpdrCheckbox("Nr "+(i+1));
                regelBox.add(selectableCheckboxes[i]);
                regelBox.add(Box.createRigidArea(new Dimension(20,10)));
            }
		    if(logMisconceptionsButtons[i]==null) {
		    	logMisconceptionsButtons[i] = new ObjectiveChoiceButton(WiskOpdr.rb.getString("OPT_misconceptions"),WiskOpdr.misconceptions, WiskOpdr.mccCategorieString);
		    	logMisconceptionsButtons[i].setPreferredSize(new Dimension(120,22));
		    	regelBox.add(logMisconceptionsButtons[i]);
		    }
		    selectableCBBox.add(regelBox);
		}
		selectableCBBox.add(Box.createVerticalGlue());
		enableMisconceptions();
	}
	
	private void enableMisconceptions() {
		for(int i=0 ; i<aantalSelectables ; i++) {
			boolean visible = WiskOpdr.misconceptions!=null 
					&& !multiSelectionsCB.isSelected()
					&& checkCB.isSelected();
		   	if(logMisconceptionsButtons[i]!=null)
		   		logMisconceptionsButtons[i].setVisible(visible);
		}	
	}
	
	public void verwijderCheckboxes() {
		for(int i=0 ; i<aantalSelectablesMax ; i++) {
		    if(selectableCheckboxes[i]!=null) {	
		    	selectableCheckboxes[i] = null;
		    	if(logMisconceptionsButtons[i]!=null) 
		    		logMisconceptionsButtons[i] = null;
		    	
		    }
		}
		selectableCBBox.removeAll();
		mainPanel.validate();
		aantalSelectables = 0;
	} 
	
	@Override
	public Hashtable getPreferences() {
		int itemCount = MultipleChoiceGenerator.initialItemCount;
		int listNumberType = MultipleChoiceGenerator.initialListNumberType;
		int tabWidth = MultipleChoiceGenerator.initialTabWidth;
		int rowSpace = MultipleChoiceGenerator.initialRowSpace;
		boolean hasPrefix = MultipleChoiceGenerator.initialHasPrefix;
		
		boolean[] juisteSelecties = null;
		int scoreMax = 0;
	    int[][] scoreMaxObjectives = null;
	    boolean randomizePositions = false;
		boolean multiSelections = false;
		boolean logOption = false;
		String logID = "";
		boolean[][] logObjectives = null;
		String[] smObjectives = null;
		boolean check = true;
		boolean teltMee = true;
		boolean checkFormule = false;
		String[] formuleStrings = null;
		String knopImageString = "";
		boolean[][][] logMisconceptions = null;
		
		boolean volledigeBreedte = true;
		int breedte = 300;
		
		itemCount = intFromText(itemCount, itemCountTF.getText());
		listNumberType = listNumberTypeComboBox.getSelectedIndex()-1;
		tabWidth = intFromText(tabWidth, tabWidthTF.getText());
		rowSpace = intFromText(rowSpace, rowSpaceTF.getText());
		hasPrefix = hasPrefixCB.isSelected();
		
		knopImageString = this.knopImageString;
	    juisteSelecties = new boolean[aantalSelectables];
	    for(int i=0 ; i<aantalSelectables ; i++) {  
	    	juisteSelecties[i] = selectableCheckboxes[i].isSelected();
	    }
	    
	    scoreMax = intFromText(scoreMax, maxScoreTF.getText());
	    //randomizePositions = randomizePositionsCB.isSelected();
	    multiSelections = multiSelectionsCB.isSelected();
	    logOption = logCB.isSelected();
		logID = logIDField.getText();
		logObjectives = logObjectivesButton.getChoices();
		smObjectives = logObjectivesButton.getObjectives();
		check = checkCB.isSelected();
		teltMee = teltMeeCB.isSelected();
		//checkFormule = checkFormuleCB.isSelected();
		//formuleStrings = formuleEditor.geefRegels();
		logMisconceptions = this.logMisconceptions;
		
		volledigeBreedte = volledigeBreedteCB.isSelected();
		breedte = intFromText(breedte, breedteTF.getText());
		
		if(logObjectives!=null) {
			scoreMaxObjectives = new int[logObjectives.length][];
			for(int j=0 ; j<scoreMaxObjectives.length; j++)	{	
				scoreMaxObjectives[j] = new int[logObjectives[j].length];
				for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)	{	
					if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
				}
			}
		}
		
		if(logMisconceptions!=null)	{	
			for(int i=0 ; i<aantalSelectables ; i++)
				logMisconceptions[i] = logMisconceptionsButtons[i].getChoices();
		}
		
		Hashtable preferences = new Hashtable();
		
		preferences.put("itemCount", new Integer(itemCount));
		preferences.put("listNumberType", new Integer(listNumberType));
		preferences.put("tabWidth", new Integer(tabWidth));
		preferences.put("rowSpace", new Integer(rowSpace));
		preferences.put("hasPrefix", new Boolean(hasPrefix));
		
		preferences.put("juisteSelecties", juisteSelecties);
		preferences.put("scoreMax", new Integer(scoreMax));
		preferences.put("randomizePositions", new Boolean(randomizePositions));
		preferences.put("multiSelections", new Boolean(multiSelections));
		preferences.put("logOption",new Boolean(logOption));
		preferences.put("logID",logID);
		preferences.put("check",new Boolean(check));
		preferences.put("teltMee",new Boolean(teltMee));
		//preferences.put("checkFormule",new Boolean(checkFormule));
		//preferences.put("formuleStrings", formuleStrings);
		if(logObjectives!=null) {	
			preferences.put("logObjectives",logObjectives);
	    	preferences.put("scoreMaxObjectives",scoreMaxObjectives);
            try {
            	preferences.put(Constants.OBJECTIVES, smObjectives);
            } catch(Exception e) {}
	    }
		if(logMisconceptions!=null) {	
			preferences.put("logMisconceptions",logMisconceptions);
        }
		preferences.put("knopImageString", knopImageString);
		
		preferences.put("volledigeBreedte", new Boolean(volledigeBreedte));
		preferences.put("breedte", new Integer(breedte));
		
		return preferences;
	}

	@Override
	public void setPreferences(Hashtable preferences) {
		
		int itemCount = MultipleChoiceGenerator.initialItemCount;
		int listNumberType = MultipleChoiceGenerator.initialListNumberType;
		int tabWidth = MultipleChoiceGenerator.initialTabWidth;
		int rowSpace = MultipleChoiceGenerator.initialRowSpace;
		boolean hasPrefix = MultipleChoiceGenerator.initialHasPrefix;
		
		boolean[] juisteSelecties = null;
	    int scoreMax = 0;
		boolean randomizePositions = false;
		boolean multiSelections = false;
		boolean logOption = false;
		String logID = "";
		boolean[][] logObjectives = null;
		String[] smObjectives = null;
		boolean check = true;
		boolean teltMee = true;
		boolean checkFormule = false;
		String[] formuleStrings = null;
		String knopImageString = "";
		boolean[][][] logMisconceptions = null;
		
		boolean volledigeBreedte = true;
		int breedte = 300;
		
		if(preferences.containsKey("itemCount")) itemCount = ((Integer)preferences.get("itemCount")).intValue();
		if(preferences.containsKey("listNumberType")) listNumberType = ((Integer)preferences.get("listNumberType")).intValue()+1;
		if(preferences.containsKey("tabWidth")) tabWidth = ((Integer)preferences.get("tabWidth")).intValue();
		if(preferences.containsKey("rowSpace")) rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		if(preferences.containsKey("hasPrefix")) hasPrefix = ((Boolean)preferences.get("hasPrefix")).booleanValue();
		
		if(preferences.containsKey("juisteSelecties")) juisteSelecties = (boolean[])preferences.get("juisteSelecties");
	    if(preferences.containsKey("scoreMax")) scoreMax = ((Integer)preferences.get("scoreMax")).intValue();
	    if(preferences.containsKey("randomizePositions")) randomizePositions = ((Boolean)preferences.get("randomizePositions")).booleanValue();
	    if(preferences.containsKey("multiSelections")) multiSelections = ((Boolean)preferences.get("multiSelections")).booleanValue();
	    if(preferences.containsKey("logOption")) logOption = ((Boolean)preferences.get("logOption")).booleanValue();
		if(preferences.containsKey("logID")) logID = (String)preferences.get("logID");
		if(preferences.containsKey("check")) check = ((Boolean)preferences.get("check")).booleanValue();
		if(preferences.containsKey("teltMee")) teltMee = ((Boolean)preferences.get("teltMee")).booleanValue();
		if(preferences.containsKey("checkFormule")) checkFormule = ((Boolean)preferences.get("checkFormule")).booleanValue();
		if(preferences.containsKey("formuleStrings")) formuleStrings = (String[])preferences.get("formuleStrings");
		if(preferences.containsKey("logObjectives")) logObjectives = (boolean[][])preferences.get("logObjectives");
		if(preferences.containsKey(Constants.OBJECTIVES)) smObjectives = (String[]) preferences.get(Constants.OBJECTIVES);
		if(preferences.containsKey("knopImageString")) knopImageString = (String)preferences.get("knopImageString");
		if(preferences.containsKey("logMisconceptions")) logMisconceptions = (boolean[][][])preferences.get("logMisconceptions");
		
		if(preferences.containsKey("volledigeBreedte")) volledigeBreedte = ((Boolean)preferences.get("volledigeBreedte")).booleanValue();
		if(preferences.containsKey("breedte")) breedte = ((Integer)preferences.get("breedte")).intValue();
		
		itemCountTF.setText(""+itemCount);
		listNumberTypeComboBox.setSelectedIndex(listNumberType);
		tabWidthTF.setText(""+tabWidth);
		rowSpaceTF.setText(""+rowSpace);
		hasPrefixCB.setSelected(hasPrefix);
		
		if(juisteSelecties==null) 
			juisteSelecties = new boolean[itemCount];
	    
		verwijderCheckboxes();
	    aantalSelectables = juisteSelecties.length;
	  //randomizePositionsCB.setSelected(randomizePositions);
	    multiSelectionsCB.setSelected(multiSelections);
	    this.logMisconceptions = logMisconceptions;
	    
	    
	    maakCheckboxes();
	    for(int i=0 ; i<aantalSelectables ; i++) {
	    	selectableCheckboxes[i].setSelected(juisteSelecties[i]);
	    	if(WiskOpdr.misconceptions!=null && logMisconceptionsButtons[i]!=null && logMisconceptions!=null)
	    		logMisconceptionsButtons[i].setChoices(logMisconceptions[i]);
		}
	    enableMisconceptions();
	    
	    maxScoreTF.setText(""+scoreMax);
	    logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        logIDLabelLabel.setVisible(logOption);
        logIDLabelField.setVisible(logOption);
        logIDField.setText(logID);
        logObjectivesButton.setChoices(logObjectives);
        logObjectivesButton.setObjectives(smObjectives);
        checkCB.setSelected(check);
        teltMeeCB.setSelected(teltMee);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        
        //checkFormuleCB.setSelected(checkFormule);
        //if(formuleStrings!=null)formuleEditor.zetRegels(formuleStrings);
        //formuleEditor.setVisible(checkFormule);
        
        knopImageButton.setPopupButtonImage(knopImage);
    	iconman = new Iconan(WiskOpdr.applet, (Component)mainPanel, (Hashtable)TekstImageVak.getImageMap());
    	if(knopImageString!=null && !"".equals(knopImageString)) {
    		knopImage = iconman.getImage(knopImageString);
    		knopImageButton.setPopupButtonImage(knopImage);
    	}
    	else {
    		knopImageButton.setPopupButtonImage(null);
    		knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
    		knopImageButton.setPreferredSize(new Dimension(80,22));
    	}
    	this.knopImageString = knopImageString;
    	
    	volledigeBreedteCB.setSelected(volledigeBreedte);
	    breedteTF.setText(""+breedte);
	    
		frame.setVisible(true);
		frame.pack();
		//frame.setLocation(tekstVak.getLocationOnScreen().x, tekstVak.getLocationOnScreen().y);
		Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
 	    int xD = (screenSize.width-frame.getSize().width)/2;
 	    int yD = (screenSize.height-frame.getSize().height)/2;
 	    frame.setLocation(xD, yD);
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
	
	public void produceThisAction(ActionEvent e)
	{	if (actionListener != null)
		{	actionListener.actionPerformed(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource().equals(okButton)) {
			produceAction("ok");
			frame.setVisible(false);
		}
		else if(e.getSource().equals(cancelButton)) {
			frame.setVisible(false);
		}
		else if(e.getSource()==itemCountTF) {
			int aantal = Math.min(aantalSelectablesMax, intFromText(4, itemCountTF.getText()));
			if(aantal != aantalSelectables)	{	
				verwijderCheckboxes();
				aantalSelectables = aantal;
				maakCheckboxes();
			}
		}
		else if(e.getSource()==multiSelectionsCB) {   
			enableMisconceptions();
	    }
		else if(e.getSource()==hasPrefixCB) {   
			tabWidthLabel.setVisible(hasPrefixCB.isSelected());
			tabWidthTF.setVisible(hasPrefixCB.isSelected());
			listNumberTypeLabel.setVisible(hasPrefixCB.isSelected());
			listNumberTypeComboBox.setVisible(hasPrefixCB.isSelected());
	    }
		else if(e.getSource()==knopImageButton) {   
			iconman = new Iconan(WiskOpdr.applet, mainPanel, (Hashtable)TekstImageVak.getImageMap());
			iconman.editImage(knopImageString, mainPanel, this);
	    }
	    else if(e.getSource()==iconman) {
	    	String name = e.getActionCommand();
	        if(!"".equals(name)) {
	        	knopImageString = name;
	            this.knopImage = iconman.getImage(name);
	            knopImageButton.setPopupButtonImage(knopImage);
	            int imWidth = iconman.getWidth(knopImageString);
				int imHeight = iconman.getHeight(knopImageString);
				if(imWidth == -1) imWidth = 20;
				if(imHeight == -1) imHeight = 20;
				knopImageButton.setPreferredSize(new Dimension(Math.max(imWidth,80),Math.max(imHeight,22)));
	         }
	        else {
	    		knopImageButton.setPopupButtonImage(null);
	    		knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
	    		knopImageButton.setPreferredSize(new Dimension(80,22));
	    	}
	    }
	    else if(e.getSource()==logCB) {   
	    	logIDField.setVisible(logCB.isSelected());
	    	logIDLabelField.setVisible(logCB.isSelected());
	    	logIDLabelLabel.setVisible(logCB.isSelected());
	    	frame.pack();
	    }
	    else if(e.getSource()==hasPrefixCB) {   
	    	listNumberTypeLabel.setVisible(hasPrefixCB.isSelected());
	    	listNumberTypeComboBox.setVisible(hasPrefixCB.isSelected());
	    	frame.pack();
	    }
	    else if(e.getSource()==volledigeBreedteCB) {   
	    	breedteTF.setEnabled(!volledigeBreedteCB.isSelected());
	    }
		
	    //if(imageDialog!=null)
	    //    imageDialog.setVisible(false);
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource()==itemCountTF) {
			int aantal = Math.min(aantalSelectablesMax, intFromText(4,itemCountTF.getText()));
			if(aantal != aantalSelectables)	{	
				verwijderCheckboxes();
				aantalSelectables = aantal;
				maakCheckboxes();
			}
		}
	}
}
