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
	private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
	private DialogFacade frame;
	private JPanel preferencesPanel;
	private JPanel topPanel, mainPanel, bottomPanel;
	private JButton okButton, cancelButton;
	private JTextField breedteTF, hoogteTF;
    private JLabel breedteLabel, hoogteLabel;
    private JCheckBox volledigeBreedteCB;
	
	private JLabel titleLabel;
	private JLabel titleOpmaakLabel;
	private JLabel itemCountLabel;
	private JTextField itemCountTF;
	private JLabel listNumberTypeLabel;
	private JComboBox listNumberTypeComboBox;
	private JLabel tabWidthLabel;
	private JTextField tabWidthTF;
	private JLabel rowSpaceLabel;
	private JTextField rowSpaceTF;
	private JCheckBox hasPrefixCB;
	
	private JLabel titleLoggingLabel;
	private JCheckBox checkCB;
	private JCheckBox teltMeeCB;
	private JCheckBox logCB;
	private JTextField logIDField;
	private JTextField logIDLabelField;
	private JLabel logIDLabelLabel;
	private ObjectiveChoiceButton logObjectivesButton;
	private ObjectiveChoiceButton[] logMisconceptionsButtons;
	
	private JLabel titleSettingsLabel;
	private JCheckBox multiSelectionsCB;
	
	private JLabel titleAntwoordLabel;
	private JLabel maxScoreLabel;
	private JTextField maxScoreTF;
	private JCheckBox[] selectableCheckboxes;
	private Box selectableCBBox;
	
	private FormuleButton knopImageButton;
	private Dialog imageDialog;
	private Iconan iconman;
	private String knopImageString = "";
	private Image knopImage;
	
	private int scoreMax;
	private boolean multiselections;
	private int aantalSelectables = 4;
	private int aantalSelectablesMax = 30;
	private boolean[][][] logMisconceptions;
	
	
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
		
		titleLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_settings"));
		titleLabel.setForeground(WiskOpdr.colorGray3);
		titleLabel.setFont(new Font("SansSerif",Font.PLAIN, 24));
		topPanel.add(titleLabel);
		
		titleOpmaakLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleOpmaakLabel"));
    	titleOpmaakLabel.setForeground(WiskOpdr.colorBlue1);
    	titleOpmaakLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
		itemCountLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_rowCount"));
		itemCountLabel.setFont(font);
		itemCountLabel.setForeground(WiskOpdr.colorBlue1);
		
		itemCountTF = new WiskOpdrTextField("");
		itemCountTF.addActionListener(this);
		itemCountTF.setPreferredSize(new Dimension(30,22));
		itemCountTF.setMaximumSize(new Dimension(30,22));
		
		listNumberTypeLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_numberType"));
		listNumberTypeLabel.setFont(font);
		listNumberTypeLabel.setForeground(WiskOpdr.colorBlue1);
		
		listNumberTypeComboBox = new WiskOpdrComboBox();
		listNumberTypeComboBox.setFont(font);
		listNumberTypeComboBox.setPreferredSize(new Dimension(80,22));
		listNumberTypeComboBox.addItem(WiskOpdr.rb.getString("TCOMP_multip_chooseType"));
		for(int i=0 ; i<MultipleChoiceGenerator.listNumbers.length ; i++) {
			listNumberTypeComboBox.addItem(MultipleChoiceGenerator.listNumbers[i][0]+" ,"+MultipleChoiceGenerator.listNumbers[i][1]+" ,"+MultipleChoiceGenerator.listNumbers[i][2]+" , ...");
		}
		
		tabWidthLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_tabWidth"));
		tabWidthLabel.setFont(font);
		tabWidthLabel.setForeground(WiskOpdr.colorBlue1);
		
		tabWidthTF = new WiskOpdrTextField("");
		tabWidthTF.addActionListener(this);
		tabWidthTF.setPreferredSize(new Dimension(30,22));
		tabWidthTF.setMaximumSize(new Dimension(30,22));
		
		rowSpaceLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_rowSpace"));
		rowSpaceLabel.setFont(font);
		rowSpaceLabel.setForeground(WiskOpdr.colorBlue1);
		
		rowSpaceTF = new WiskOpdrTextField("");
		rowSpaceTF.addActionListener(this);
		rowSpaceTF.setPreferredSize(new Dimension(30,22));
		rowSpaceTF.setMaximumSize(new Dimension(30,22));
		
		hasPrefixCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("TCOMP_multip_hasPrefix"));
		hasPrefixCB.setOpaque(false);
		hasPrefixCB.setSelected(true);
		hasPrefixCB.setFont(font);
		hasPrefixCB.addActionListener(this);
		
		titleLoggingLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleLoggingLabel"));
    	titleLoggingLabel.setForeground(WiskOpdr.colorBlue1);
    	titleLoggingLabel.setFont(font.deriveFont(Font.BOLD, 16));
		
		checkCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("checkCBLabel"));
		checkCB.setOpaque(false);
		checkCB.setSelected(true);
		checkCB.setFont(font);
		
		teltMeeCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("teltMeeCBLabel"));
		teltMeeCB.setOpaque(false);
		teltMeeCB.setSelected(true);
		teltMeeCB.setFont(font);
		
		logCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("logCBLabel"));
		logCB.setOpaque(false);
		logCB.setSelected(false);
		logCB.setFont(font);
		logCB.addActionListener(this);
		
		logIDField = new WiskOpdrTextField("");
		logIDField.addActionListener(this);
		logIDField.setPreferredSize(new Dimension(50,22));
		logIDField.setMaximumSize(new Dimension(50,22));
		logIDField.setVisible(false);
		
		logIDLabelLabel = new JLabel(WiskOpdr.rb.getString("TVEP_logIDLabelLabel"));
		logIDLabelLabel.setFont(font);
		logIDLabelLabel.setForeground(WiskOpdr.colorBlue1);
		logIDLabelLabel.setVisible(false);
		
		logIDLabelField = new WiskOpdrTextField("");
		logIDLabelField.addActionListener(this);
		logIDLabelField.setPreferredSize(new Dimension(50,22));
		logIDLabelField.setMaximumSize(new Dimension(50,22));
		logIDLabelField.setVisible(false);
		
		titleSettingsLabel = new JLabel(WiskOpdr.rb.getString("settingsLabel"));
		titleSettingsLabel.setForeground(WiskOpdr.colorBlue1);
		titleSettingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
		
		
		maxScoreLabel = new JLabel(WiskOpdr.rb.getString("score"));//Score");
		maxScoreLabel.setBounds(300,50,80,20);
		maxScoreLabel.setFont(font);
		
		maxScoreTF = new WiskOpdrTextField("0");
		maxScoreTF.setPreferredSize(new Dimension(50,22));
		maxScoreTF.setBounds(380,50,40,20);
		maxScoreTF.addActionListener(this);
		
		multiSelectionsCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("meervSelectiesLabel"));//("Meervoudige selecties mogelijk");
		multiSelectionsCB.setBounds(300,120,280,20);
		multiSelectionsCB.setOpaque(false);
		multiSelectionsCB.setFont(font);
		
		titleAntwoordLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
		titleAntwoordLabel.setForeground(WiskOpdr.colorBlue1);
		titleAntwoordLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	selectableCheckboxes = new JCheckBox[aantalSelectablesMax];
    	logMisconceptionsButtons = new ObjectiveChoiceButton[aantalSelectablesMax];
    	selectableCBBox = Box.createVerticalBox();
    	
    	logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString, WiskOpdr.studentModel);
        //logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        logObjectivesButton.setPreferredSize(new Dimension(120,22));
        logObjectivesButton.setMaximumSize(new Dimension(120,22));
        //if(WiskOpdr.objectives!=null)add(logObjectivesButton);
                
        knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setBounds(550,50,80,20);
		knopImageButton.addActionListener(this);
		//add(knopImageButton);
	
	
	
		
		
		Box boxh = Box.createHorizontalBox();
		Box boxv0 = Box.createVerticalBox();
		Box boxv1 = Box.createVerticalBox();
		Box boxv2 = Box.createVerticalBox();
		
		Box regelBox = Box.createHorizontalBox();
		regelBox.add(titleAntwoordLabel);
		regelBox.add(Box.createHorizontalGlue());
		boxv0.add(regelBox);
		boxv0.add(Box.createVerticalStrut(15));
		
		maakCheckboxes();
		regelBox = Box.createHorizontalBox();
		regelBox.add(selectableCBBox);
		regelBox.add(Box.createHorizontalGlue());
		boxv0.add(regelBox);
		boxv0.add(Box.createVerticalGlue());
		
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(titleSettingsLabel);
		regelBox.add(Box.createHorizontalGlue());
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(15));
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(itemCountLabel);
		regelBox.add(Box.createRigidArea(new Dimension(10,10)));
		regelBox.add(Box.createHorizontalGlue());
		regelBox.add(itemCountTF);
		
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(5));
		
		regelBox = Box.createHorizontalBox();
    	regelBox.add(multiSelectionsCB);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv1.add(regelBox);
    	boxv1.add(Box.createVerticalStrut(5));
    	
    	regelBox = Box.createHorizontalBox();
		regelBox.add(hasPrefixCB);
		regelBox.add(Box.createHorizontalGlue());
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(5));
		
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(listNumberTypeLabel);
		regelBox.add(Box.createRigidArea(new Dimension(10,10)));
		regelBox.add(Box.createHorizontalGlue());
		regelBox.add(listNumberTypeComboBox);
		
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(5));
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(tabWidthLabel);
		regelBox.add(Box.createRigidArea(new Dimension(10,10)));
		regelBox.add(Box.createHorizontalGlue());
		regelBox.add(tabWidthTF);
		
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(5));
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(rowSpaceLabel);
		regelBox.add(Box.createRigidArea(new Dimension(10,10)));
		regelBox.add(Box.createHorizontalGlue());
		regelBox.add(rowSpaceTF);
				
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(5));
		
		
		boxv1.add(Box.createVerticalGlue());
		
		regelBox = Box.createHorizontalBox();
    	regelBox.add(titleLoggingLabel);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv2.add(regelBox);
    	boxv2.add(Box.createVerticalStrut(15));
    	
    	regelBox = Box.createHorizontalBox();
    	regelBox.add(checkCB);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv2.add(regelBox);
    	
    	regelBox = Box.createHorizontalBox();
    	regelBox.add(teltMeeCB);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv2.add(regelBox);
    	
    	regelBox = Box.createHorizontalBox();
    	regelBox.add(logCB);
    	regelBox.add(Box.createRigidArea(new Dimension(5,10)));
    	regelBox.add(logIDField);
    	regelBox.add(Box.createRigidArea(new Dimension(5,10)));
    	regelBox.add(logIDLabelLabel);
    	regelBox.add(Box.createRigidArea(new Dimension(5,10)));
    	regelBox.add(logIDLabelField);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv2.add(regelBox);
    	if(WiskOpdr.objectives!=null) {
	    	boxv2.add(Box.createRigidArea(new Dimension(5,5)));
	    	regelBox = Box.createHorizontalBox();
	    	regelBox.add(Box.createRigidArea(new Dimension(5,5)));
	    	regelBox.add(logObjectivesButton);
	    	regelBox.add(Box.createHorizontalGlue());
	    	boxv2.add(regelBox);
    	}
    	boxv2.add(Box.createVerticalGlue());
		
    	boxh.add(boxv0);
		boxh.add(Box.createHorizontalStrut(50));
		boxh.add(boxv1);
		boxh.add(Box.createHorizontalStrut(50));
		boxh.add(boxv2);
		mainPanel.add(boxh);
		
		preferencesPanel.add(topPanel,BorderLayout.NORTH);
		preferencesPanel.add(bottomPanel,BorderLayout.SOUTH);
		preferencesPanel.add(mainPanel,BorderLayout.CENTER);
		
		topPanel.add(titleLabel);
		
		
		
		okButton = new WiskOpdrButton("Ok");//
		okButton.setBackground(WiskOpdr.colorBlue1);
		okButton.setForeground(WiskOpdr.colorGray3);
		okButton.setPreferredSize(new Dimension(70,24));
		okButton.addActionListener(this);
		
		cancelButton = new WiskOpdrButton("Cancel");//
		cancelButton.setBackground(WiskOpdr.colorBlue1);
		cancelButton.setForeground(WiskOpdr.colorGray3);
		cancelButton.setPreferredSize(new Dimension(70,24));
		cancelButton.addActionListener(this);
		
		breedteLabel = new JLabel(WiskOpdr.rb.getString("breedteLabel"));
		breedteLabel.setForeground(WiskOpdr.colorBlue1);
        breedteLabel.setBounds(370,20,40,22);
        breedteLabel.setFont(font);
        
        breedteTF = new WiskOpdrTextField("300");
        breedteTF.setBounds(420,20,40,22);
        breedteTF.setPreferredSize(new Dimension(40,22));
        breedteTF.setFont(font);
        breedteTF.addActionListener(this);
        breedteTF.addFocusListener(this);
        breedteTF.setEnabled(false);
        
        hoogteLabel = new JLabel(WiskOpdr.rb.getString("hoogteLabel"));
        hoogteLabel.setForeground(WiskOpdr.colorBlue1);
        hoogteLabel.setBounds(465,20,50,22);
        hoogteLabel.setFont(font);
        
        hoogteTF = new WiskOpdrTextField("250");
        hoogteTF.setBounds(510,20,40,22);
        hoogteTF.setPreferredSize(new Dimension(40,22));
        hoogteTF.setFont(font);
        hoogteTF.addActionListener(this);
        hoogteTF.addFocusListener(this);
        hoogteTF.setEnabled(false);
        
        
        
        volledigeBreedteCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("volleBreedteLabel"));
        volledigeBreedteCB.setOpaque(false);
        volledigeBreedteCB.setFont(font);
        volledigeBreedteCB.setBounds(560,20,100,22);
        volledigeBreedteCB.addActionListener(this);
        volledigeBreedteCB.setSelected(true);
        
		Box boxBottom = Box.createHorizontalBox();
		boxBottom.add(okButton);
		boxBottom.add(Box.createHorizontalStrut(20));
		boxBottom.add(cancelButton);
		boxBottom.add(Box.createHorizontalStrut(20));
		boxBottom.add(breedteLabel);
		boxBottom.add(Box.createHorizontalStrut(5));
		boxBottom.add(breedteTF);
		boxBottom.add(Box.createHorizontalStrut(10));
		boxBottom.add(hoogteLabel);
		boxBottom.add(Box.createHorizontalStrut(5));
		boxBottom.add(hoogteTF);
		boxBottom.add(Box.createHorizontalStrut(20));
		boxBottom.add(volledigeBreedteCB);
		boxBottom.add(Box.createHorizontalStrut(20));
		   
		boxBottom.add(Box.createHorizontalGlue());
		bottomPanel.add(boxBottom);
	}
	
	public void makeFrame(){
	   	frame = DialogFacade.newInstance(tekstVak, WiskOpdr.rb.getString("TCOMP_multip"), true);
	   	//Dimension preferredSize = new Dimension(400,320);
		//frame.setPreferredSize(preferredSize);
	    frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    //frame.setSize(preferredSize);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.getContentPane().add(preferencesPanel);
	    frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	    frame.pack();
	    //frame.setLocation(tekstVak.getLocationOnScreen().x, tekstVak.getLocationOnScreen().y);
		//frame.setVisible(true);
		    
	}
	
	public void maakCheckboxes()
	{
		if(WiskOpdr.misconceptions!=null)
			logMisconceptions = new boolean[aantalSelectables][][];
				
		for(int i=0 ; i<aantalSelectables ; i++)
		{
			Box regelBox = Box.createHorizontalBox();
			if(selectableCheckboxes[i]==null)
            {   
				selectableCheckboxes[i] = new WiskOpdrCheckbox("Nr "+(i+1));
                regelBox.add(selectableCheckboxes[i]);
                regelBox.add(Box.createRigidArea(new Dimension(20,10)));
                
                
            }
		    if(logMisconceptionsButtons[i]==null)
		    {
		    	logMisconceptionsButtons[i] = new ObjectiveChoiceButton(WiskOpdr.rb.getString("OPT_misconceptions"),WiskOpdr.misconceptions, WiskOpdr.mccCategorieString);
		    	logMisconceptionsButtons[i].setPreferredSize(new Dimension(120,22));
		    	//if(WiskOpdr.misconceptions!=null)
		        	regelBox.add(logMisconceptionsButtons[i]);
		    }
		    selectableCBBox.add(regelBox);
		   
		}
		
	    selectableCBBox.add(Box.createVerticalGlue());
		enableMisconceptions();
		
	}
	
	private void enableMisconceptions()
	{
		for(int i=0 ; i<aantalSelectables ; i++)
		{
			boolean visible = WiskOpdr.misconceptions!=null 
					&& !multiSelectionsCB.isSelected()
					&& checkCB.isSelected();
		   	if(logMisconceptionsButtons[i]!=null)
		   		logMisconceptionsButtons[i].setVisible(visible);
		   
		}	
	}
	
	public void verwijderCheckboxes()
	{
		for(int i=0 ; i<aantalSelectables ; i++)
		{
		    if(selectableCheckboxes[i]!=null) 
		    {	selectableCheckboxes[i] = null;
		    	if(logMisconceptionsButtons[i]!=null) 
		    	{	logMisconceptionsButtons[i] = null;
		    	}
		    }
		}
		selectableCBBox.removeAll();
		aantalSelectables = 0;
	}
	
	public void editImage() {
        if(imageDialog == null)
        {
        	
//        	Frame f = JOptionPane.getFrameForComponent(this);
//			imageDialog = new Dialog(f,"title", true);
//			imageDialog.setLayout(new BorderLayout());
//			iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
//            imageDialog.add(iconman);
//            imageDialog.pack();
//            iconman.addActionListener(this);
        }
        iconman.select(knopImageString);
        imageDialog.setVisible(true);
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
		
		itemCount = Integer.parseInt(itemCountTF.getText());
		listNumberType = listNumberTypeComboBox.getSelectedIndex()-1;
		tabWidth = Integer.parseInt(tabWidthTF.getText());
		rowSpace = Integer.parseInt(rowSpaceTF.getText());
		hasPrefix = hasPrefixCB.isSelected();
		
		knopImageString = this.knopImageString;
	    juisteSelecties = new boolean[aantalSelectables];
	    for(int i=0 ; i<aantalSelectables ; i++)
	    {  juisteSelecties[i] = selectableCheckboxes[i].isSelected();
	    }
	    
	    scoreMax = Integer.parseInt(maxScoreTF.getText());
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
		breedte = Integer.parseInt(breedteTF.getText());
		
		if(logObjectives!=null)
		{	scoreMaxObjectives = new int[logObjectives.length][];
			for(int j=0 ; j<scoreMaxObjectives.length; j++)
			{	scoreMaxObjectives[j] = new int[logObjectives[j].length];
				for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)
				{	if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
				}
			}
		}
		
		if(logMisconceptions!=null)
		{	for(int i=0 ; i<aantalSelectables ; i++)
			{	logMisconceptions[i] = logMisconceptionsButtons[i].getChoices();
			}
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
		if(logObjectives!=null)
	    {	preferences.put("logObjectives",logObjectives);
	    	preferences.put("scoreMaxObjectives",scoreMaxObjectives);
            try {
            	preferences.put(Constants.OBJECTIVES, smObjectives);
            } catch(Exception e) {}
	    }
		if(logMisconceptions!=null)
        {	preferences.put("logMisconceptions",logMisconceptions);
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
	    //aantalSelectablesTF.setText(""+aantalSelectables);
	    maxScoreTF.setText(""+scoreMax);
	    //randomizePositionsCB.setSelected(randomizePositions);
	    multiSelectionsCB.setSelected(multiSelections);
	    this.logMisconceptions = logMisconceptions;
	    
	    volledigeBreedteCB.setSelected(volledigeBreedte);
	    breedteTF.setText(""+breedte);
	    
//	    for(int i=0 ; i<aantalSelectables ; i++)
//		{
//		    selectableCheckboxes[i] = new JCheckBox("Nr "+(i+1));
//		    selectableCheckboxes[i].setSelected(juisteSelecties[i]);
//            selectableCheckboxes[i].setOpaque(false);
//            selectableCheckboxes[i].setBounds(10,80+i*25,60,20);
//            add(selectableCheckboxes[i],0);
//            
//            if(logMisconceptions!=null)
//            {   logMisconceptionsButtons[i] = new ObjectiveChoiceButton(WiskOpdr.rb.getString("OPT_misconceptions"),WiskOpdr.misconceptions, WiskOpdr.mccCategorieString);
//			    logMisconceptionsButtons[i].setBounds(100,80+i*25,100,20);
//			    logMisconceptionsButtons[i].setChoices(logMisconceptions[i]);
//			    add(logMisconceptionsButtons[i]);
//		    }
//		}
	    
	    maakCheckboxes();
	    for(int i=0 ; i<aantalSelectables ; i++)
		{
	    	selectableCheckboxes[i].setSelected(juisteSelecties[i]);
	    	if(WiskOpdr.misconceptions!=null && logMisconceptionsButtons[i]!=null && logMisconceptions!=null)
	    		logMisconceptionsButtons[i].setChoices(logMisconceptions[i]);
		}
	    enableMisconceptions();
	    
	    logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);
        logObjectivesButton.setChoices(logObjectives);
        logObjectivesButton.setObjectives(smObjectives);
        checkCB.setSelected(check);
        teltMeeCB.setSelected(teltMee);
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
    		knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
    	}
    	this.knopImageString = knopImageString;
		
		frame.setVisible(true);
		frame.setLocation(tekstVak.getLocationOnScreen().x, tekstVak.getLocationOnScreen().y);
		
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
		if(e.getSource().equals(cancelButton)) {
			frame.setVisible(false);
		}
		
		if(e.getSource()==itemCountTF)
		{
			int aantal = Math.min(aantalSelectablesMax, Integer.parseInt(itemCountTF.getText()));
			if(aantal != aantalSelectables)
			{	verwijderCheckboxes();
				aantalSelectables = aantal;
				maakCheckboxes();
			}
			
		}
		else if(e.getSource()==multiSelectionsCB)
	    {   enableMisconceptions();
	    }
		else if(e.getSource()==knopImageButton)
	    {   editImage();
	            
	    }
	    else if(e.getSource()==iconman)
	    {
	    	String name = e.getActionCommand();
	        if(!"".equals(name))
	        {
	        	knopImageString = name;
	            this.knopImage = iconman.getImage(name);
	            knopImageButton.setPopupButtonImage(knopImage);
	            int imWidth = iconman.getWidth(knopImageString);
				int imHeight = iconman.getHeight(knopImageString);
				if(imWidth == -1) imWidth = 20;
				if(imHeight == -1) imHeight = 20;
				knopImageButton.setSize(imWidth,imHeight);
	           // repaint();
	        }
	    }
	    else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());
	    	logIDLabelField.setVisible(logCB.isSelected());
	    	logIDLabelLabel.setVisible(logCB.isSelected());
	    	frame.pack();
	    }
	    else if(e.getSource()==hasPrefixCB)
	    {   listNumberTypeLabel.setVisible(hasPrefixCB.isSelected());
	    	listNumberTypeComboBox.setVisible(hasPrefixCB.isSelected());
	    	frame.pack();
	    }
	    else if(e.getSource()==volledigeBreedteCB)
	    {   breedteTF.setEnabled(!volledigeBreedteCB.isSelected());
	    }
		

	    if(imageDialog!=null)
	        imageDialog.setVisible(false);
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
}
