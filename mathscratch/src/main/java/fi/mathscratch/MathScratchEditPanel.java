package fi.mathscratch;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingUtilities;

import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JRadioButton;
import fi.beans.numworxlf.JTextField;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;

public class MathScratchEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
	// Basis GUI
    private JPanel mainPanel;
    private JPanel leftPanel;
	private JPanel instellingenPanel;
	private MathScratchPanel mathcratchPanel;
	
	// Logging/Nakijken
  	private Box settingsBox;
 	private JLabel titleLoggingLabel;
    private JCheckBox checkCB;
    private Component checkCBRA = ra(0,5);
    private JLabel maxScoreLabel;
    private JTextField maxScoreField;
    private JCheckBox logCB;
 	private JTextField logIDField;
 	
 	// Settings
 	private JLabel titleSettingsLabel;
 	private JCheckBox drawingCB;
 	private JCheckBox formRecognitionCB;
 	private Component formRecognitionCBRA = ra(0,5);
 	private JRadioButton showWritingRB;
 	private Component showWritingRBRA = ra(0,5);
 	private JRadioButton showFormFontRB;
 	private Component showFormFontRBRA = ra(0,5);
 	private ButtonGroup buttonGroup;
 	private JCheckBox calculatorCB;
	private JCheckBox inputOptionCB;
	private Component inputOptionCBRA = ra(0,5);
	private InstellingenButton instellingenButton;
	
 	// Layout
  	private JLabel titleLayoutLabel;
  	private JCheckBox gridCB;
  	private JCheckBox scaleWritingCB;
  	private Component scaleWritingCBRA = ra(0,5);
  	private JLabel scaleWritingLabel;
  	private JTextField scaleWritingTF;
 	
    
	public MathScratchEditPanel(MathScratchPanel mathcratchPanel) {
		this.mathcratchPanel = mathcratchPanel;
		mathcratchPanel.setPreferredSize(new Dimension(500,450));
		makeGUI();
	}
	
	private void makeGUI() {
		// Main
   		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(MathScratch.colorGray3);
		
		// Panels links/rechts
		leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(mathcratchPanel);
		
		instellingenPanel = new JPanel(null);
		instellingenPanel.setPreferredSize(new Dimension(200,451));
		instellingenPanel.setMaximumSize(new Dimension(200,451));
		instellingenPanel.setLayout(new BorderLayout());
   		
		// Logging/Nakijken
 		titleLoggingLabel = new JLabel(MathScratch.rb.getString("titleLoggingLabel"));
     	titleLoggingLabel.setForeground(MathScratch.colorBlue1);
     	titleLoggingLabel.setFont(font.deriveFont(Font.BOLD, 16));
     	
     	checkCB = new JCheckBox(MathScratch.rb.getString("checkCB"));
     	checkCB.addActionListener(this);
     	checkCBRA.setVisible(false);
     	
     	maxScoreLabel = new JLabel(MathScratch.rb.getString("maxScoreLabel"));
     	maxScoreLabel.setForeground(MathScratch.colorBlue1);
     	maxScoreLabel.setFont(font);
     	maxScoreLabel.setVisible(false);
     	
     	maxScoreField = new JTextField("0");
     	maxScoreField.setFont(font);
     	maxScoreField.setPreferredSize(new Dimension(50,22));
     	maxScoreField.setMaximumSize(new Dimension(50,22));
     	maxScoreField.setVisible(false);
     	
     	logCB = new JCheckBox(MathScratch.rb.getString("logCB"));
     	logCB.addActionListener(this);
     	
        logIDField = new JTextField("0");
        logIDField.setPreferredSize(new Dimension(50,22));
        logIDField.setMaximumSize(new Dimension(50,22));
        logIDField.setFont(font);
        
        logIDField.setVisible(false);
        
        // Instellingen
        titleSettingsLabel = new JLabel(MathScratch.rb.getString("titleSettingsLabel"));
        titleSettingsLabel.setForeground(MathScratch.colorBlue1);
        titleSettingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
        
        drawingCB = new JCheckBox(MathScratch.rb.getString("drawingCB"));
        drawingCB.setSelected(true);
        drawingCB.addActionListener(this);
        
        formRecognitionCB = new JCheckBox(MathScratch.rb.getString("formRecognitionCB"));
        formRecognitionCB.setSelected(true);
        formRecognitionCB.addActionListener(this);
        
        showWritingRB = new JRadioButton(MathScratch.rb.getString("showWritingRB"));
        showWritingRB.setSelected(true);
        
        showFormFontRB = new JRadioButton(MathScratch.rb.getString("showFormFontRB"));
        
        buttonGroup = new ButtonGroup();
        buttonGroup.add(showWritingRB);
        buttonGroup.add(showFormFontRB);
        
        calculatorCB = new JCheckBox(MathScratch.rb.getString("calculatorCB"));
        calculatorCB.setSelected(true);
        
        inputOptionCB = new JCheckBox(MathScratch.rb.getString("inputOptionCB"));
        inputOptionCB.addActionListener(this);
        inputOptionCBRA.setVisible(false);
        
		instellingenButton = new InstellingenButton (MathScratch.rb.getString("inputAreasButton"));
		instellingenButton.setVisible(false);
		
		// Opmaak
		titleLayoutLabel = new JLabel(MathScratch.rb.getString("titleLayoutLabel"));
		titleLayoutLabel.setForeground(MathScratch.colorBlue1);
		titleLayoutLabel.setFont(font.deriveFont(Font.BOLD, 16));
		
		gridCB = new JCheckBox(MathScratch.rb.getString("gridCB"));
		gridCB.addActionListener(this);
		
		scaleWritingCB = new JCheckBox(MathScratch.rb.getString("scaleWritingCB"));
		scaleWritingCB.addActionListener(this);
		scaleWritingCB.setSelected(true);
		
		scaleWritingLabel = new JLabel(MathScratch.rb.getString("scaleWritingLabel"));
		scaleWritingLabel.setForeground(MathScratch.colorBlue1);
		scaleWritingLabel.setFont(font);
		
		scaleWritingTF = new JTextField("2.2");
		scaleWritingTF.setFont(font);
		scaleWritingTF.setPreferredSize(new Dimension(50,22));
		scaleWritingTF.setMaximumSize(new Dimension(50,22));
        
		plaatsGUI();
	}
	
	public void plaatsGUI() {
		Component[] k1 = {leftPanel, vgl()};
		
		Component[] r11 = {titleLoggingLabel, hgl()};
		Component[] r12 = {checkCB, hgl() };
		Component[] r13 = {ra(25,0), maxScoreLabel, ra(10,0), maxScoreField, hgl() };
		Component[] r14 = {logCB, ra(10,0), logIDField, hgl() };
		Component[] r15 = {titleSettingsLabel, hgl()};
		Component[] r16 = {drawingCB, hgl() };
		Component[] r17 = {formRecognitionCB, hgl() };
		Component[] r18 = {ra(20,0), showWritingRB, hgl() };
		Component[] r19 = {ra(20,0), showFormFontRB, hgl() };
		Component[] r110 = {calculatorCB, hgl() };
		Component[] r111 = {inputOptionCB, hgl() };
		Component[] r112 = {ra(25,0), instellingenButton, hgl() };
		Component[] r113 = {titleLayoutLabel, hgl() };
		Component[] r114 = {gridCB, hgl() };
		Component[] r115 = {scaleWritingCB, hgl() };
		Component[] r116 = {ra(25,0), scaleWritingLabel, ra(10,0), scaleWritingTF, hgl() };
		
		
		Component[] k2 = {hb(r11), vst(10), hb(r12), checkCBRA, hb(r13), vst(5), hb(r14), vst(15), 
				hb(r15), vst(10), hb(r16), vst(5), hb(r17), formRecognitionCBRA, hb(r18), showWritingRBRA, hb(r19), showFormFontRBRA, hb(r110), vst(5), hb(r111), inputOptionCBRA, hb(r112), vst(15),
				hb(r113), vst(10), hb(r114), vst(5), hb(r115), scaleWritingCBRA, hb(r116), vgl()};
		
		Component[] rr = {vb(k1), ra(20,0), vb(k2)};
		
		mainPanel.add(hb(rr));
		add(mainPanel);
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
	
	private Component ln(int w, int h) {
	  Component c =  ln(h);
	  c.setPreferredSize(new Dimension(w,h));
	  c.setMinimumSize(new Dimension(w,h));
	  c.setMaximumSize(new Dimension(w,h));
	  return c;
	}
	private Component ln(int h) {
	  JPanel p = new JPanel() {
	      public void paintComponent(Graphics g) {
	        g.setColor(MathScratch.colorBlue4);  
	        g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
	        //g.drawLine(0, getHeight()/2+1, getWidth(), getHeight()/2+1);
	      }
	  };
	  p.setPreferredSize(new Dimension(1,h));
	  p.setMaximumSize(new Dimension(1000,h));
	  return p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==checkCB) {
			boolean check = checkCB.isSelected();
			checkCBRA.setVisible(check);
			maxScoreLabel.setVisible(check);
			maxScoreField.setVisible(check);
		}
		if(e.getSource()==logCB) {
			boolean log = logCB.isSelected();
			logIDField.setVisible(log);
			mainPanel.validate();
		}
		if(e.getSource()==formRecognitionCB) {
			boolean form = formRecognitionCB.isSelected();
			formRecognitionCBRA.setVisible(form);
			showWritingRB.setVisible(form);
			showWritingRBRA.setVisible(form);
			showFormFontRB.setVisible(form);
			showFormFontRBRA.setVisible(form);
			calculatorCB.setVisible(form);
			if(!form)
				drawingCB.setSelected(true);
		}
		if(e.getSource()==drawingCB) {
			boolean drawing = drawingCB.isSelected();
			if(!drawing) {
				formRecognitionCB.setSelected(true);
				showWritingRB.setVisible(true);
				showWritingRBRA.setVisible(true);
				showFormFontRB.setVisible(true);
				showFormFontRBRA.setVisible(true);
			}
		}
		
		if(e.getSource()==scaleWritingCB) {
			boolean scale = scaleWritingCB.isSelected();
			scaleWritingCBRA.setVisible(scale);
			scaleWritingLabel.setVisible(scale);
			scaleWritingTF.setVisible(scale);
			validate();
		}
		if(e.getSource()==inputOptionCB) {
			boolean input = inputOptionCB.isSelected();
			inputOptionCBRA.setVisible(input);
			instellingenButton.setVisible(input);
		}
		if(e.getSource()==gridCB) {
			mathcratchPanel.setGrid(gridCB.isSelected());
		}
		((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)this)).pack();
	}

	@Override
	public Hashtable getEditState() {
		int scoreMax = 0;
		try {
			scoreMax = Integer.parseInt(maxScoreField.getText());
		}
		catch(Exception e) {}
		
		double writingScale = 2.2;
		try {
			writingScale = Double.parseDouble(scaleWritingTF.getText());
		}
		catch(Exception e) {}
		
		Hashtable h = new Hashtable();
		h.put("logOption", new Boolean(logCB.isSelected()));
		h.put("logID", logIDField.getText());
		h.put("checkDocent", new Boolean(checkCB.isSelected()));
		h.put("scoreMax", new Integer(scoreMax));
		h.put("drawOption", drawingCB.isSelected());
		h.put("formOption", formRecognitionCB.isSelected());
		h.put("showWriting", showWritingRB.isSelected());
		h.put("calculator", new Boolean(calculatorCB.isSelected()));
		h.put("inputOption", new Boolean(inputOptionCB.isSelected()));
		h.put("areaSettings", instellingenButton.getInstellingen());
		h.put("grid", new Boolean(gridCB.isSelected()));
		h.put("scaleWriting", new Boolean(scaleWritingCB.isSelected()));
		h.put("writingScale", new Double(writingScale));
		return h;
	}

	@Override
	public void setEditState(Hashtable h) {
		if (h.containsKey("logOption")) {
			boolean logOption = ((Boolean) h.get("logOption"));
			logCB.setSelected(logOption);
		}
		if (h.containsKey("logID")) {
			String logID = ((String) h.get("logID"));
			logIDField.setText(logID);
		}
		if (h.containsKey("checkDocent")) {
			boolean checkDocent = ((Boolean) h.get("checkDocent"));
			checkCB.setSelected(checkDocent);
		}
		if (h.containsKey("scoreMax")) {
			int scoreMax = ((Integer) h.get("scoreMax"));
			maxScoreField.setText(""+scoreMax);
		}
		if (h.containsKey("drawOption")) {
			boolean drawOption = ((Boolean) h.get("drawOption"));
			drawingCB.setSelected(drawOption);
		}
		if (h.containsKey("formOption")) {
			boolean formOption = ((Boolean) h.get("formOption"));
			formRecognitionCB.setSelected(formOption);
		}
		if (h.containsKey("showWriting")) {
			boolean showWriting = ((Boolean) h.get("showWriting"));
			showWritingRB.setSelected(showWriting);
			showFormFontRB.setSelected(!showWriting);
		}
		if (h.containsKey("calculator")) {
			boolean calculator = ((Boolean) h.get("calculator"));
			calculatorCB.setSelected(calculator);
		}
		if (h.containsKey("inputOption")) {
			boolean inputOption = ((Boolean) h.get("inputOption"));
			inputOptionCB.setSelected(inputOption);
		}
		if (h.containsKey("areaSettings")) {
			Hashtable areaSettings = ((Hashtable) h.get("areaSettings"));
			instellingenButton.setInstellingen(areaSettings);
		}
		if (h.containsKey("grid")) {
			boolean grid = ((Boolean) h.get("grid"));
			gridCB.setSelected(grid);
		}
		if (h.containsKey("scaleWriting")) {
			boolean scaleWriting = ((Boolean) h.get("scaleWriting"));
			scaleWritingCB.setSelected(scaleWriting);
		}
		if (h.containsKey("writingScale")) {
			double writingScale = ((Double) h.get("writingScale"));
			scaleWritingTF.setText(""+writingScale);
		}
		
		mathcratchPanel.setGrid(gridCB.isSelected());
		
		boolean check = checkCB.isSelected();
		checkCBRA.setVisible(check);
		maxScoreLabel.setVisible(check);
		maxScoreField.setVisible(check);
		
		boolean log = logCB.isSelected();
		logIDField.setVisible(log);
		
		boolean form = formRecognitionCB.isSelected();
		formRecognitionCBRA.setVisible(form);
		showWritingRB.setVisible(form);
		showWritingRBRA.setVisible(form);
		showFormFontRB.setVisible(form);
		showFormFontRBRA.setVisible(form);
		calculatorCB.setVisible(form);
		
		boolean scale = scaleWritingCB.isSelected();
		scaleWritingCBRA.setVisible(scale);
		scaleWritingLabel.setVisible(scale);
		scaleWritingTF.setVisible(scale);
		
		boolean input = inputOptionCB.isSelected();
		inputOptionCBRA.setVisible(input);
		instellingenButton.setVisible(input);
		((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)this)).pack();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		
	}

	@Override
	public void zetBreedte(int b) {
		mathcratchPanel.setBounds(0,0,b,mathcratchPanel.getHeight());
	}

	@Override
	public void zetHoogte(int h) {
		mathcratchPanel.setBounds(0,0,mathcratchPanel.getWidth(), h);
	
	}
}
