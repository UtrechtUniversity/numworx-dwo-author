package fi.nabouwenaanzichten;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.event.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
//import fi.beans.wiskopdrbeans.InteractiePanel;

public class NabouwenAanzichtenInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	NabouwenAanzichtenInteractiePanel naip;
	int editWidth = 250;
	int editHeight = 550;
	int naipBreedte = 500;
	int naipHoogte = 450;

	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;

	int offset = 10;
	boolean componentsCreated = false;

	// viewerOptiesPanel
	JCheckBox rotatieVastBox;
	JCheckBox nietBouwenSlopenBox;
	JCheckBox keuzeBouwenSlopenBox;
	JCheckBox perspectiefBox;

	JCheckBox volLeegBox;
	JCheckBox aantalBlokjesBox;

	ButtonGroup frontGroup;
	JRadioButton pijlButton, balkButton, geenButton;
	JCheckBox plattegrondBox;
	JCheckBox maakAanzichtBox;
	JLabel plattegrondLabel;
	ButtonGroup aanzichtGroup;
	JRadioButton bouwselButton, silhouetButton, drieButton, voorZijButton, bovenButton, voorButton, rechtsButton;
	JLabel roosterLabel;
	JTextField roosterTextField;

	// nakijkOptiesPanel
	JCheckBox nakijkBox;
	JCheckBox checkExternalBox;
	JLabel checkLabel;
	ButtonGroup checkAanzichtGroup;
	JRadioButton checkBouwselButton, checkDrieButton, checkVoorZijButton, checkBovenVoorButton, checkBovenZijButton,
		checkBovenButton, checkVoorButton, checkRechtsButton;
	JCheckBox aantalKubusBox;
	// JLabel minKubusLabel;

	JLabel maxScoreLabel;
	JTextField maxScoreVeld;
	JCheckBox logCB;
	JTextField logIDTF;

	JTabbedPane tabbedPane;

	JPanel viewerOptiesPanel, nakijkOptiesPanel;

	boolean noSetBounds = false;

	public NabouwenAanzichtenInteractieEditPanel()
	{
		setLayout(null);
		setBackground(new Color(230, 240, 255));

		naip = new NabouwenAanzichtenInteractiePanel();

		naip.naiep = this;

		setSize(naip.getSize().width + editWidth, naip.getSize().height);
		naip.setLocation(0, 0);
		add(naip);

		tabbedPane = new JTabbedPane();

		viewerOptiesPanel = new JPanel();
		viewerOptiesPanel.setLayout(null);
		tabbedPane.addTab(NabouwenAanzichten.rb.getString("viewerOpties"), viewerOptiesPanel);

		nakijkOptiesPanel = new JPanel();
		nakijkOptiesPanel.setLayout(null);
		tabbedPane.addTab(NabouwenAanzichten.rb.getString("nakijkOpties"), nakijkOptiesPanel);

		tabbedPane.setBounds(getSize().width - editWidth, 0, editWidth, getSize().height);
		add(tabbedPane);

		tabbedPane.addChangeListener(new TabbedPaneCL());

		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);

		// viewerOptiesPanel

		int width = 0;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = naip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset / 2;

		rotatieVastBox = new JCheckBox(NabouwenAanzichten.rb.getString("rotatieVast"));
		rotatieVastBox.setFont(theFont);
		width = theFM.stringWidth(rotatieVastBox.getText()) + 40;
		rotatieVastBox.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(rotatieVastBox);
		rotatieVastBox.addActionListener(this);
		currentY += height; // + offset / 2;

		nietBouwenSlopenBox = new JCheckBox(NabouwenAanzichten.rb.getString("isVoorbeeld"));
		nietBouwenSlopenBox.setFont(theFont);
		width = theFM.stringWidth(nietBouwenSlopenBox.getText()) + 40;
		nietBouwenSlopenBox.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(nietBouwenSlopenBox);
		nietBouwenSlopenBox.addActionListener(this);

		currentY += height;// + offset / 2;

		keuzeBouwenSlopenBox = new JCheckBox(NabouwenAanzichten.rb.getString("bouwSloopKeuze"));
		keuzeBouwenSlopenBox.setFont(theFont);
		width = theFM.stringWidth(keuzeBouwenSlopenBox.getText()) + 35;
		keuzeBouwenSlopenBox.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(keuzeBouwenSlopenBox);
		keuzeBouwenSlopenBox.addActionListener(this);

		currentY += height;// + offset / 2;

		perspectiefBox = new JCheckBox(NabouwenAanzichten.rb.getString("perspectief"));
		perspectiefBox.setFont(theFont);
		perspectiefBox.setSelected(true);
		width = theFM.stringWidth(perspectiefBox.getText()) + 40;
		perspectiefBox.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(perspectiefBox);
		perspectiefBox.addActionListener(this);

		currentY += height;// + offset;

		volLeegBox = new JCheckBox(NabouwenAanzichten.rb.getString("volLeegKnop"));
		volLeegBox.setFont(theFont);
		width = theFM.stringWidth(volLeegBox.getText()) + 40;
		volLeegBox.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(volLeegBox);
		volLeegBox.addActionListener(this);

		currentY += height;// + offset;

		aantalBlokjesBox = new JCheckBox(NabouwenAanzichten.rb.getString("aantalBlokjes"));
		aantalBlokjesBox.setFont(theFont);
		width = theFM.stringWidth(aantalBlokjesBox.getText()) + 40;
		aantalBlokjesBox.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(aantalBlokjesBox);
		aantalBlokjesBox.addActionListener(this);

		currentY += height + offset / 2;

		frontGroup = new ButtonGroup();

		pijlButton = new JRadioButton(NabouwenAanzichten.rb.getString("zwartePijl"), true);
		frontGroup.add(pijlButton);
		pijlButton.setFont(theFont);
		width = theFM.stringWidth(pijlButton.getText()) + 40;
		pijlButton.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(pijlButton);
		pijlButton.addActionListener(this);

		currentY += height; // + offset / 5;

		balkButton = new JRadioButton(NabouwenAanzichten.rb.getString("rodeBalk"));
		frontGroup.add(balkButton);
		balkButton.setFont(theFont);
		width = theFM.stringWidth(balkButton.getText()) + 40;
		balkButton.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(balkButton);
		balkButton.addActionListener(this);

		currentY += height; // + offset / 5;

		geenButton = new JRadioButton(NabouwenAanzichten.rb.getString("geenVoorkant"));
		frontGroup.add(geenButton);
		geenButton.setFont(theFont);
		width = theFM.stringWidth(geenButton.getText()) + 35;
		geenButton.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(geenButton);
		geenButton.addActionListener(this);

		currentY += height + offset;

		maakAanzichtBox = new JCheckBox(NabouwenAanzichten.rb.getString("maakAanzicht"));
		maakAanzichtBox.setFont(theFont);
		width = theFM.stringWidth(maakAanzichtBox.getText()) + 35;
		maakAanzichtBox.setBounds(currentX2, currentY, width, theFM.getHeight());
		viewerOptiesPanel.add(maakAanzichtBox);
		maakAanzichtBox.addActionListener(this);

		currentY += theFM.getHeight();

		plattegrondBox = new JCheckBox(NabouwenAanzichten.rb.getString("plattegrond"));
		plattegrondBox.setFont(theFont);
		width = theFM.stringWidth(plattegrondBox.getText()) + 35;
		plattegrondBox.setBounds(currentX2, currentY, width, theFM.getHeight());
		viewerOptiesPanel.add(plattegrondBox);
		plattegrondBox.addActionListener(this);

		currentY += theFM.getHeight();

		plattegrondLabel = new JLabel(NabouwenAanzichten.rb.getString("plattegrond2"));
		plattegrondLabel.setFont(theFont);
		width = theFM.stringWidth(plattegrondLabel.getText());
		plattegrondLabel.setBounds(currentX2, currentY, width, theFM.getHeight());
		viewerOptiesPanel.add(plattegrondLabel);

		currentY += height;// + offset / 2;

		height -= 3;

		aanzichtGroup = new ButtonGroup();

		bouwselButton = new JRadioButton(NabouwenAanzichten.rb.getString("blokkenbouwsel"), true);
		aanzichtGroup.add(bouwselButton);
		bouwselButton.setFont(theFont);
		width = theFM.stringWidth(bouwselButton.getText()) + 40;
		bouwselButton.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(bouwselButton);
		bouwselButton.addActionListener(this);

		currentY += height; // + offset / 5;

		silhouetButton = new JRadioButton(NabouwenAanzichten.rb.getString("silhouet"));
		aanzichtGroup.add(silhouetButton);
		silhouetButton.setFont(theFont);
		width = theFM.stringWidth(silhouetButton.getText()) + 40;
		silhouetButton.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(silhouetButton);
		silhouetButton.addActionListener(this);

		currentY += height; // + offset / 5;

		drieButton = new JRadioButton(NabouwenAanzichten.rb.getString("3Aanzichten"));
		aanzichtGroup.add(drieButton);
		drieButton.setFont(theFont);
		width = theFM.stringWidth(drieButton.getText()) + 40;
		drieButton.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(drieButton);
		drieButton.addActionListener(this);

		currentY += height; // + offset / 5;

		voorZijButton = new JRadioButton(NabouwenAanzichten.rb.getString("voorZijAanzicht"));
		aanzichtGroup.add(voorZijButton);
		voorZijButton.setFont(theFont);
		width = theFM.stringWidth(voorZijButton.getText()) + 40;
		voorZijButton.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(voorZijButton);
		voorZijButton.addActionListener(this);

		currentY += height; // + offset / 5;

		bovenButton = new JRadioButton(NabouwenAanzichten.rb.getString("bovenAanzicht"));
		aanzichtGroup.add(bovenButton);
		bovenButton.setFont(theFont);
		width = theFM.stringWidth(bovenButton.getText()) + 40;
		bovenButton.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(bovenButton);
		bovenButton.addActionListener(this);

		currentY += height; // + offset / 5;

		voorButton = new JRadioButton(NabouwenAanzichten.rb.getString("voorAanzicht"));
		aanzichtGroup.add(voorButton);
		voorButton.setFont(theFont);
		width = theFM.stringWidth(voorButton.getText()) + 40;
		voorButton.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(voorButton);
		voorButton.addActionListener(this);

		currentY += height; // + offset / 5;

		rechtsButton = new JRadioButton(NabouwenAanzichten.rb.getString("rechtsAanzicht"));
		aanzichtGroup.add(rechtsButton);
		rechtsButton.setFont(theFont);
		width = theFM.stringWidth(rechtsButton.getText()) + 40;
		rechtsButton.setBounds(currentX2, currentY, width, height);
		viewerOptiesPanel.add(rechtsButton);
		rechtsButton.addActionListener(this);

		height += 3;
		currentY += height + offset;

		roosterLabel = new JLabel(NabouwenAanzichten.rb.getString("roosterGrootte"));
		roosterLabel.setFont(theFont);
		width = theFM.stringWidth(roosterLabel.getText());
		roosterLabel.setBounds(currentX2, currentY, width, theFM.getHeight());
		viewerOptiesPanel.add(roosterLabel);

		currentY += height; // + offset;

		roosterTextField = new JTextField("4");
		roosterTextField.setFont(theFont);
		width = theFM.stringWidth("XXXXXX");
		roosterTextField.setBounds(currentX2 + 2 * offset, currentY, width, height);
		viewerOptiesPanel.add(roosterTextField);

		roosterTextField.addKeyListener(new InputKL(roosterTextField, false, true));
		roosterTextField.addActionListener(new TextAL(roosterTextField));
		roosterTextField.addFocusListener(new TextFL(roosterTextField));

		currentY += height + offset;

		// nakijkOptiesPanel

		currentY = offset;

		nakijkBox = new JCheckBox(NabouwenAanzichten.rb.getString("kijkNa"));
		nakijkBox.setFont(theFont);
		width = theFM.stringWidth(nakijkBox.getText()) + 40;
		nakijkBox.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(nakijkBox);
		nakijkBox.addActionListener(this);

		currentY += height + 3 * offset / 2;

		checkExternalBox = new JCheckBox(NabouwenAanzichten.rb.getString("checkExternal"));
		checkExternalBox.setFont(theFont);
		width = theFM.stringWidth(checkExternalBox.getText()) + 40;
		checkExternalBox.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(checkExternalBox);
		checkExternalBox.addActionListener(this);

		currentY += height + 3 * offset / 2;

		checkLabel = new JLabel(NabouwenAanzichten.rb.getString("controleer"));
		checkLabel.setFont(theFont);
		width = theFM.stringWidth(checkLabel.getText());
		checkLabel.setBounds(currentX2, currentY, width, theFM.getHeight());
		nakijkOptiesPanel.add(checkLabel);

		currentY += height; // + offset / 2;

		checkAanzichtGroup = new ButtonGroup();

		checkBouwselButton = new JRadioButton(NabouwenAanzichten.rb.getString("blokkenbouwsel"), true);
		checkAanzichtGroup.add(checkBouwselButton);
		checkBouwselButton.setFont(theFont);
		width = theFM.stringWidth(checkBouwselButton.getText()) + 40;
		checkBouwselButton.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(checkBouwselButton);
		checkBouwselButton.addActionListener(this);
		checkBouwselButton.setEnabled(false);

		currentY += height; // + offset / 5;

		checkDrieButton = new JRadioButton(NabouwenAanzichten.rb.getString("3Aanzichten"));
		checkAanzichtGroup.add(checkDrieButton);
		checkDrieButton.setFont(theFont);
		width = theFM.stringWidth(checkDrieButton.getText()) + 40;
		checkDrieButton.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(checkDrieButton);
		checkDrieButton.addActionListener(this);
		checkDrieButton.setEnabled(false);

		currentY += height; // + offset / 5;

		checkVoorZijButton = new JRadioButton(NabouwenAanzichten.rb.getString("voorZijAanzicht"));
		checkAanzichtGroup.add(checkVoorZijButton);
		checkVoorZijButton.setFont(theFont);
		width = theFM.stringWidth(checkVoorZijButton.getText()) + 40;
		checkVoorZijButton.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(checkVoorZijButton);
		checkVoorZijButton.addActionListener(this);
		checkVoorZijButton.setEnabled(false);

		currentY += height; // + offset / 5;

		checkBovenVoorButton = new JRadioButton(NabouwenAanzichten.rb.getString("bovenVoorAanzicht"));
		checkAanzichtGroup.add(checkBovenVoorButton);
		checkBovenVoorButton.setFont(theFont);
		width = theFM.stringWidth(checkBovenVoorButton.getText()) + 40;
		checkBovenVoorButton.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(checkBovenVoorButton);
		checkBovenVoorButton.addActionListener(this);
		checkBovenVoorButton.setEnabled(false);

		currentY += height; // + offset / 5;

		checkBovenZijButton = new JRadioButton(NabouwenAanzichten.rb.getString("bovenZijAanzicht"));
		checkAanzichtGroup.add(checkBovenZijButton);
		checkBovenZijButton.setFont(theFont);
		width = theFM.stringWidth(checkBovenZijButton.getText()) + 40;
		checkBovenZijButton.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(checkBovenZijButton);
		checkBovenZijButton.addActionListener(this);
		checkBovenZijButton.setEnabled(false);

		currentY += height; // + offset / 5;

		checkBovenButton = new JRadioButton(NabouwenAanzichten.rb.getString("bovenAanzicht"));
		checkAanzichtGroup.add(checkBovenButton);
		checkBovenButton.setFont(theFont);
		width = theFM.stringWidth(checkBovenButton.getText()) + 40;
		checkBovenButton.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(checkBovenButton);
		checkBovenButton.addActionListener(this);
		checkBovenButton.setEnabled(false);

		currentY += height; // + offset / 5;

		checkVoorButton = new JRadioButton(NabouwenAanzichten.rb.getString("voorAanzicht"));
		checkAanzichtGroup.add(checkVoorButton);
		checkVoorButton.setFont(theFont);
		width = theFM.stringWidth(checkVoorButton.getText()) + 40;
		checkVoorButton.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(checkVoorButton);
		checkVoorButton.addActionListener(this);
		checkVoorButton.setEnabled(false);

		currentY += height; // + offset / 5;

		checkRechtsButton = new JRadioButton(NabouwenAanzichten.rb.getString("rechtsAanzicht"));
		checkAanzichtGroup.add(checkRechtsButton);
		checkRechtsButton.setFont(theFont);
		width = theFM.stringWidth(checkRechtsButton.getText()) + 40;
		checkRechtsButton.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(checkRechtsButton);
		checkRechtsButton.addActionListener(this);
		checkRechtsButton.setEnabled(false);

		currentY += height + offset;

		aantalKubusBox = new JCheckBox(NabouwenAanzichten.rb.getString("aantalKubus"));
		aantalKubusBox.setFont(theFont);
		width = theFM.stringWidth(aantalKubusBox.getText()) + 40;
		aantalKubusBox.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(aantalKubusBox);
		aantalKubusBox.addActionListener(this);
		aantalKubusBox.setEnabled(false);

		currentY += height + 3 * offset / 2;

		maxScoreLabel = new JLabel(NabouwenAanzichten.rb.getString("maxScoreTekst"));
		maxScoreLabel.setFont(theFont);
		width = theFM.stringWidth(maxScoreLabel.getText());
		maxScoreLabel.setBounds(currentX2, currentY, width, theFM.getHeight());
		nakijkOptiesPanel.add(maxScoreLabel);

		currentY += height; // + offset;

		maxScoreVeld = new JTextField("10");
		maxScoreVeld.setFont(theFont);
		width = theFM.stringWidth("XXXXXX");
		maxScoreVeld.setBounds(currentX2 + 2 * offset, currentY, width, height);
		maxScoreVeld.setEditable(false);
		nakijkOptiesPanel.add(maxScoreVeld);

		maxScoreVeld.addKeyListener(new InputKL(maxScoreVeld, false, true));
		maxScoreVeld.addActionListener(new TextAL(maxScoreVeld));
		maxScoreVeld.addFocusListener(new TextFL(maxScoreVeld));
		
		currentY += height + offset;
		
		logCB = new JCheckBox(NabouwenAanzichten.rb.getString("logCBLabel"));
		logCB.setFont(theFont);
		width = theFM.stringWidth(logCB.getText()) + 40;
		logCB.setBounds(currentX2, currentY, width, height);
		nakijkOptiesPanel.add(logCB);
		logCB.addActionListener(this);
		
		logIDTF = new JTextField("");
		logIDTF.setFont(theFont);
		width = theFM.stringWidth("XXXXXX");
		logIDTF.setBounds(currentX2 + 80, currentY, width, height);
		logIDTF.setVisible(false);
		nakijkOptiesPanel.add(logIDTF);

		componentsCreated = true;
	}

	public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			tabbedPane.setBounds(naipBreedte, 0, editWidth, getSize().height);

			repaint();
		}
	}

	public void setEditState(Hashtable b)
	{
		// viewerOpties

		boolean rotatieVast = false;
		if (b.containsKey("rotatieVast"))
			rotatieVast = ((Boolean) b.get("rotatieVast")).booleanValue();
		rotatieVastBox.setSelected(rotatieVast);

		boolean nietBouwenSlopen = false;
		if (b.containsKey("nietBouwenSlopen"))
			nietBouwenSlopen = ((Boolean) b.get("nietBouwenSlopen")).booleanValue();
		nietBouwenSlopenBox.setSelected(nietBouwenSlopen);

		boolean keuzeBouwenSlopen = false;
		if (b.containsKey("keuzeBouwenSlopen"))
			keuzeBouwenSlopen = ((Boolean) b.get("keuzeBouwenSlopen")).booleanValue();
		keuzeBouwenSlopenBox.setSelected(keuzeBouwenSlopen);

		boolean perspectief = true;
		if (b.containsKey("perspectief"))
			perspectief = ((Boolean) b.get("perspectief")).booleanValue();
		perspectiefBox.setSelected(perspectief);

		boolean volLeegOptie = false;
		if (b.containsKey("volLeegOptie"))
			volLeegOptie = ((Boolean) b.get("volLeegOptie")).booleanValue();
		volLeegBox.setSelected(volLeegOptie);

		boolean aantalBlokjes = false;
		if (b.containsKey("aantalBlokjes"))
			aantalBlokjes = ((Boolean) b.get("aantalBlokjes")).booleanValue();
		aantalBlokjesBox.setSelected(aantalBlokjes);

		boolean pijlAan = true;
		if (b.containsKey("pijlAan"))
			pijlAan = ((Boolean) b.get("pijlAan")).booleanValue();
		pijlButton.setSelected(pijlAan);

		boolean balkAan = false;
		if (b.containsKey("balkAan"))
			balkAan = ((Boolean) b.get("balkAan")).booleanValue();
		balkButton.setSelected(balkAan);

		if (!pijlAan && !balkAan)
			geenButton.setSelected(true);

		boolean bovenAanzichtMetHoogtes = false;
		if (b.containsKey("bovenAanzichtMetHoogtes"))
			bovenAanzichtMetHoogtes = ((Boolean) b.get("bovenAanzichtMetHoogtes")).booleanValue();
		plattegrondBox.setSelected(bovenAanzichtMetHoogtes);

		boolean maakAanzicht = false;
		if (b.containsKey("maakAanzicht"))
			maakAanzicht = ((Boolean) b.get("maakAanzicht")).booleanValue();
		maakAanzichtBox.setSelected(maakAanzicht);

		boolean blokkenBouwsel = true;
		if (b.containsKey("blokkenBouwsel"))
			blokkenBouwsel = ((Boolean) b.get("blokkenBouwsel")).booleanValue();
		bouwselButton.setSelected(blokkenBouwsel);

		boolean silhouet = false;
		if (b.containsKey("silhouet"))
			silhouet = ((Boolean) b.get("silhouet")).booleanValue();
		silhouetButton.setSelected(silhouet);

		boolean drieAanzichten = false;
		if (b.containsKey("drieAanzichten"))
			drieAanzichten = ((Boolean) b.get("drieAanzichten")).booleanValue();
		drieButton.setSelected(drieAanzichten);

		boolean voorZijAanzicht = false;
		if (b.containsKey("voorZijAanzicht"))
			voorZijAanzicht = ((Boolean) b.get("voorZijAanzicht")).booleanValue();
		voorZijButton.setSelected(voorZijAanzicht);

		boolean bovenAanzicht = false;
		if (b.containsKey("bovenAanzicht"))
			bovenAanzicht = ((Boolean) b.get("bovenAanzicht")).booleanValue();
		bovenButton.setSelected(bovenAanzicht);

		boolean voorAanzicht = false;
		if (b.containsKey("voorAanzicht"))
			voorAanzicht = ((Boolean) b.get("voorAanzicht")).booleanValue();
		voorButton.setSelected(voorAanzicht);

		boolean rechtsAanzicht = false;
		if (b.containsKey("rechtsAanzicht"))
			rechtsAanzicht = ((Boolean) b.get("rechtsAanzicht")).booleanValue();
		rechtsButton.setSelected(rechtsAanzicht);

		if (plattegrondBox.isSelected() || maakAanzichtBox.isSelected())
		{
			rotatieVastBox.setEnabled(false);
			perspectiefBox.setEnabled(false);

			bouwselButton.setSelected(true);

			bouwselButton.setEnabled(false);
			silhouetButton.setEnabled(false);
			drieButton.setEnabled(false);
			voorZijButton.setEnabled(false);
			bovenButton.setEnabled(false);
			voorButton.setEnabled(false);
			rechtsButton.setEnabled(false);

			roosterTextField.setEnabled(false);
		}

		int roosterGrootte = 4;
		if (b.containsKey("roosterGrootte"))
			roosterGrootte = ((Integer) b.get("roosterGrootte")).intValue();

		roosterTextField.setText("" + roosterGrootte);

		// nakijkOpties

		boolean kijkNaActief = false;
		if (b.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) b.get("kijkNaActief")).booleanValue();
		nakijkBox.setSelected(kijkNaActief);

		boolean checkExternal = false;
		if (b.containsKey("checkExternal"))
			checkExternal = ((Boolean) b.get("checkExternal")).booleanValue();
		checkExternalBox.setSelected(checkExternal);

		if (nakijkBox.isSelected())
		{
			checkBouwselButton.setEnabled(true);
			checkDrieButton.setEnabled(true);
			checkVoorZijButton.setEnabled(true);
			checkBovenVoorButton.setEnabled(true);
			checkBovenZijButton.setEnabled(true);
			checkBovenButton.setEnabled(true);
			checkVoorButton.setEnabled(true);
			checkRechtsButton.setEnabled(true);
			aantalKubusBox.setEnabled(true);
			maxScoreVeld.setEditable(true);
		}

		boolean checkBlokkenBouwsel = true;
		if (b.containsKey("checkBlokkenBouwsel"))
			checkBlokkenBouwsel = ((Boolean) b.get("checkBlokkenBouwsel")).booleanValue();
		checkBouwselButton.setSelected(checkBlokkenBouwsel);

		boolean checkDrieAanzichten = false;
		if (b.containsKey("checkDrieAanzichten"))
			checkDrieAanzichten = ((Boolean) b.get("checkDrieAanzichten")).booleanValue();
		checkDrieButton.setSelected(checkDrieAanzichten);

		boolean checkVoorZijAanzicht = false;
		if (b.containsKey("checkVoorZijAanzicht"))
			checkVoorZijAanzicht = ((Boolean) b.get("checkVoorZijAanzicht")).booleanValue();
		checkVoorZijButton.setSelected(checkVoorZijAanzicht);

		boolean checkBovenVoorAanzicht = false;
		if (b.containsKey("checkBovenVoorAanzicht"))
			checkBovenVoorAanzicht = ((Boolean) b.get("checkBovenVoorAanzicht")).booleanValue();
		checkBovenVoorButton.setSelected(checkBovenVoorAanzicht);

		boolean checkBovenZijAanzicht = false;
		if (b.containsKey("checkBovenZijAanzicht"))
			checkBovenZijAanzicht = ((Boolean) b.get("checkBovenZijAanzicht")).booleanValue();
		checkBovenZijButton.setSelected(checkBovenZijAanzicht);

		boolean checkBovenAanzicht = false;
		if (b.containsKey("checkBovenAanzicht"))
			checkBovenAanzicht = ((Boolean) b.get("checkBovenAanzicht")).booleanValue();
		checkBovenButton.setSelected(checkBovenAanzicht);

		boolean checkVoorAanzicht = false;
		if (b.containsKey("checkVoorAanzicht"))
			checkVoorAanzicht = ((Boolean) b.get("checkVoorAanzicht")).booleanValue();
		checkVoorButton.setSelected(checkVoorAanzicht);

		boolean checkRechtsAanzicht = false;
		if (b.containsKey("checkRechtsAanzicht"))
			checkRechtsAanzicht = ((Boolean) b.get("checkRechtsAanzicht")).booleanValue();
		checkRechtsButton.setSelected(checkRechtsAanzicht);

		boolean checkAantalKubus = false;
		if (b.containsKey("checkAantalKubus"))
			checkAantalKubus = ((Boolean) b.get("checkAantalKubus")).booleanValue();
		aantalKubusBox.setSelected(checkAantalKubus);

		int scoreMax = 10;
		if (b.containsKey("scoreMax"))
			scoreMax = ((Integer) b.get("scoreMax")).intValue();
		maxScoreVeld.setText("" + scoreMax);
		
		boolean log = false;
		if (b.containsKey("log"))
			log = ((Boolean) b.get("log")).booleanValue();
		logCB.setSelected(log);
		logIDTF.setVisible(log);
		
		String logID = "";
		if (b.containsKey("logID"))
			logID = (String) b.get("logID");
		logIDTF.setText(logID);

		naip.setEditState(b);

		if (b.containsKey("naipBreedte"))
			naipBreedte = ((Integer) b.get("naipBreedte")).intValue();
		if (b.containsKey("naipHoogte"))
			naipHoogte = ((Integer) b.get("naipHoogte")).intValue();

		setBounds(getLocation().x, getLocation().y, naipBreedte + editWidth, Math.max(naipHoogte, editHeight));
	}

	public Hashtable getEditState()
	{
		Hashtable h = naip.getEditState();

		h.put("naipBreedte", new Integer(naipBreedte));
		h.put("naipHoogte", new Integer(naipHoogte));
		
		h.put("log", new Boolean(logCB.isSelected()));
		h.put("logID", logIDTF.getText());

		return h;
	}

	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{
			noSetBounds = false;
			return;
		}

		super.setBounds(x, y, naipBreedte + editWidth, Math.max(naipHoogte, editHeight));

		if (naip != null)
		{
			naip.noSetBounds = false;
			naip.setBounds(0, 0, naipBreedte, naipHoogte);

		}

		plaatsComponenten();
	}

	public void zetBreedte(int b)
	{
		naipBreedte = b;

		setBounds(getLocation().x, getLocation().y, b + editWidth, getSize().height);
		plaatsComponenten();
	}

	public void zetHoogte(int h)
	{
		naipHoogte = h;

		setBounds(getLocation().x, getLocation().y, getSize().width, Math.max(h, editHeight));
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
	{
	}

	public void addActionListener(ActionListener al)
	{
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == rotatieVastBox)
		{
			naip.zetRotatieVast(rotatieVastBox.isSelected());
		}

		else if (e.getSource() == nietBouwenSlopenBox)
		{
			naip.zetNietBouwenSlopen(nietBouwenSlopenBox.isSelected());

			if (nietBouwenSlopenBox.isSelected())
			{
				keuzeBouwenSlopenBox.setSelected(false);
				naip.zetKeuzeBouwenSlopen(false);
			}
		}
		else if (e.getSource() == keuzeBouwenSlopenBox)
		{
			naip.zetKeuzeBouwenSlopen(keuzeBouwenSlopenBox.isSelected());
		}
		else if (e.getSource() == perspectiefBox)
		{
			naip.zetPerspectief(perspectiefBox.isSelected());
		}
		else if (e.getSource() == volLeegBox)
		{
			naip.zetVolLeegOptie(volLeegBox.isSelected());
		}
		else if (e.getSource() == aantalBlokjesBox)
		{
			naip.zetAantalBlokjes(aantalBlokjesBox.isSelected());
		}

		else if (e.getSource() == pijlButton)
		{
			naip.zetPijlAan(pijlButton.isSelected());
		}
		else if (e.getSource() == balkButton)
		{
			naip.zetBalkAan(balkButton.isSelected());
		}
		else if (e.getSource() == geenButton)
		{
			if (geenButton.isSelected())
			{
				naip.zetPijlAan(false);
				naip.zetBalkAan(false);
			}
		}
		else if (e.getSource() == plattegrondBox)
		{
			boolean selected = plattegrondBox.isSelected();
			if (selected)
			{
				maakAanzichtBox.setSelected(false);
				naip.zetMaakAanzicht(false);
			}
			naip.zetBovenAanzichtMetHoogtes(selected);
			if (selected)
			{
				rotatieVastBox.setEnabled(false);
				perspectiefBox.setEnabled(false);

				nietBouwenSlopenBox.setEnabled(true);
				keuzeBouwenSlopenBox.setEnabled(true);
				volLeegBox.setEnabled(true);
				aantalBlokjesBox.setEnabled(true);

				pijlButton.setEnabled(true);
				balkButton.setEnabled(true);
				geenButton.setEnabled(true);

				bouwselButton.setSelected(true);

				bouwselButton.setEnabled(false);
				silhouetButton.setEnabled(false);
				drieButton.setEnabled(false);
				voorZijButton.setEnabled(false);
				bovenButton.setEnabled(false);
				voorButton.setEnabled(false);
				rechtsButton.setEnabled(false);

				roosterTextField.setEnabled(false);
			}
			else
			{
				rotatieVastBox.setEnabled(true);
				perspectiefBox.setEnabled(true);

				bouwselButton.setEnabled(true);
				silhouetButton.setEnabled(true);
				drieButton.setEnabled(true);
				voorZijButton.setEnabled(true);
				bovenButton.setEnabled(true);
				voorButton.setEnabled(true);
				rechtsButton.setEnabled(true);

				roosterTextField.setEnabled(true);

				if (naip.blokkenBouwsel)
				{

				}

				if (naip.silhouet)
				{
					nietBouwenSlopenBox.setEnabled(false);
					keuzeBouwenSlopenBox.setEnabled(false);
					volLeegBox.setEnabled(false);
					aantalBlokjesBox.setEnabled(false);

				}

				if (naip.drieAanzichten || naip.voorZijAanzicht || naip.bovenAanzicht || naip.voorAanzicht
					|| naip.rechtsAanzicht)
				{
					rotatieVastBox.setEnabled(false);
					nietBouwenSlopenBox.setEnabled(false);
					keuzeBouwenSlopenBox.setEnabled(false);
					perspectiefBox.setEnabled(false);
					volLeegBox.setEnabled(false);
					aantalBlokjesBox.setEnabled(false);

					pijlButton.setEnabled(false);
					balkButton.setEnabled(false);
					geenButton.setEnabled(false);

				}
			}

		}
		else if (e.getSource() == maakAanzichtBox)
		{
			boolean selected = maakAanzichtBox.isSelected();
			if (selected)
			{
				plattegrondBox.setSelected(false);
				naip.zetBovenAanzichtMetHoogtes(false);
			}

			naip.zetMaakAanzicht(selected);
			if (selected)
			{
				rotatieVastBox.setEnabled(false);
				perspectiefBox.setEnabled(false);

				nietBouwenSlopenBox.setEnabled(true);
				keuzeBouwenSlopenBox.setEnabled(true);
				volLeegBox.setEnabled(true);
				aantalBlokjesBox.setEnabled(true);

				pijlButton.setEnabled(true);
				balkButton.setEnabled(true);
				geenButton.setEnabled(true);

				bouwselButton.setSelected(true);

				bouwselButton.setEnabled(false);
				silhouetButton.setEnabled(false);
				drieButton.setEnabled(false);
				voorZijButton.setEnabled(false);
				bovenButton.setEnabled(false);
				voorButton.setEnabled(false);
				rechtsButton.setEnabled(false);

				roosterTextField.setEnabled(false);
			}
			else
			{
				rotatieVastBox.setEnabled(true);
				perspectiefBox.setEnabled(true);

				bouwselButton.setEnabled(true);
				silhouetButton.setEnabled(true);
				drieButton.setEnabled(true);
				voorZijButton.setEnabled(true);
				bovenButton.setEnabled(true);
				voorButton.setEnabled(true);
				rechtsButton.setEnabled(true);

				roosterTextField.setEnabled(true);

				if (naip.blokkenBouwsel)
				{

				}

				if (naip.silhouet)
				{
					nietBouwenSlopenBox.setEnabled(false);
					keuzeBouwenSlopenBox.setEnabled(false);
					volLeegBox.setEnabled(false);
					aantalBlokjesBox.setEnabled(false);

				}

				if (naip.drieAanzichten || naip.voorZijAanzicht || naip.bovenAanzicht || naip.voorAanzicht
					|| naip.rechtsAanzicht)
				{
					rotatieVastBox.setEnabled(false);
					nietBouwenSlopenBox.setEnabled(false);
					keuzeBouwenSlopenBox.setEnabled(false);
					perspectiefBox.setEnabled(false);
					volLeegBox.setEnabled(false);
					aantalBlokjesBox.setEnabled(false);

					pijlButton.setEnabled(false);
					balkButton.setEnabled(false);
					geenButton.setEnabled(false);

				}
			}

		}
		else if (e.getSource() == bouwselButton)
		{
			if (bouwselButton.isSelected())
			{
				naip.zetBlokkenBouwsel(true);

				if (bouwselButton.isSelected())
				{
					rotatieVastBox.setEnabled(true);
					nietBouwenSlopenBox.setEnabled(true);
					keuzeBouwenSlopenBox.setEnabled(true);
					perspectiefBox.setEnabled(true);
					volLeegBox.setEnabled(true);
					aantalBlokjesBox.setEnabled(true);

					pijlButton.setEnabled(true);
					balkButton.setEnabled(true);
					geenButton.setEnabled(true);
				}

			}
		}
		else if (e.getSource() == silhouetButton)
		{
			if (silhouetButton.isSelected())
			{
				naip.zetSilhouet(true);

				if (silhouetButton.isSelected())
				{
					nietBouwenSlopenBox.setEnabled(false);
					keuzeBouwenSlopenBox.setEnabled(false);
					volLeegBox.setEnabled(false);
					aantalBlokjesBox.setEnabled(false);
				}
			}
		}
		else if (e.getSource() == drieButton)
		{
			if (drieButton.isSelected())
			{
				naip.zetDrieAanzichten(true);
				if (drieButton.isSelected())
				{
					rotatieVastBox.setEnabled(false);
					nietBouwenSlopenBox.setEnabled(false);
					keuzeBouwenSlopenBox.setEnabled(false);
					perspectiefBox.setEnabled(false);
					volLeegBox.setEnabled(false);
					aantalBlokjesBox.setEnabled(false);

					pijlButton.setEnabled(false);
					balkButton.setEnabled(false);
					geenButton.setEnabled(false);

				}
			}
		}
		else if (e.getSource() == voorZijButton)
		{
			if (voorZijButton.isSelected())
			{
				naip.zetVoorZijAanzicht(true);

				if (voorZijButton.isSelected())
				{
					rotatieVastBox.setEnabled(false);
					nietBouwenSlopenBox.setEnabled(false);
					keuzeBouwenSlopenBox.setEnabled(false);
					perspectiefBox.setEnabled(false);
					volLeegBox.setEnabled(false);
					aantalBlokjesBox.setEnabled(false);

					pijlButton.setEnabled(false);
					balkButton.setEnabled(false);
					geenButton.setEnabled(false);

				}

			}
		}
		else if (e.getSource() == bovenButton)
		{
			if (bovenButton.isSelected())
			{
				naip.zetBovenAanzicht(true);

				if (bovenButton.isSelected())
				{
					rotatieVastBox.setEnabled(false);
					nietBouwenSlopenBox.setEnabled(false);
					keuzeBouwenSlopenBox.setEnabled(false);
					perspectiefBox.setEnabled(false);
					volLeegBox.setEnabled(false);
					aantalBlokjesBox.setEnabled(false);

					pijlButton.setEnabled(false);
					balkButton.setEnabled(false);
					geenButton.setEnabled(false);

				}
			}
		}
		else if (e.getSource() == voorButton)
		{
			if (voorButton.isSelected())
			{
				naip.zetVoorAanzicht(true);

				if (voorButton.isSelected())
				{
					rotatieVastBox.setEnabled(false);
					nietBouwenSlopenBox.setEnabled(false);
					keuzeBouwenSlopenBox.setEnabled(false);
					perspectiefBox.setEnabled(false);
					volLeegBox.setEnabled(false);
					aantalBlokjesBox.setEnabled(false);

					pijlButton.setEnabled(false);
					balkButton.setEnabled(false);
					geenButton.setEnabled(false);
				}
			}
		}
		else if (e.getSource() == rechtsButton)
		{
			if (rechtsButton.isSelected())
			{
				naip.zetRechtsAanzicht(true);

				if (rechtsButton.isSelected())
				{
					rotatieVastBox.setEnabled(false);
					nietBouwenSlopenBox.setEnabled(false);
					keuzeBouwenSlopenBox.setEnabled(false);
					perspectiefBox.setEnabled(false);
					volLeegBox.setEnabled(false);
					aantalBlokjesBox.setEnabled(false);

					pijlButton.setEnabled(false);
					balkButton.setEnabled(false);
					geenButton.setEnabled(false);
				}
			}
		}
		else if (e.getSource() == nakijkBox)
		{
			naip.zetKijkNaActief(nakijkBox.isSelected());

			if (nakijkBox.isSelected())
			{
				setVisibleNakijkModusFields(true);
				// extern controleren is alleen mogelijk als kijk na actief
				checkExternalBox.setEnabled(true);
			}
			else
			{
				setVisibleNakijkModusFields(false);
				// als kijk na niet actief kun je ook niet extern controleren
				checkExternalBox.setEnabled(false);
			}
		}
		else if (e.getSource() == checkExternalBox)
		{
			naip.zetCheckExternal(checkExternalBox.isSelected());
		}
		else if (e.getSource() == checkBouwselButton)
		{
			if (checkBouwselButton.isSelected())
			{
				naip.zetCheckBlokkenBouwsel(true);
			}
		}
		else if (e.getSource() == checkDrieButton)
		{
			if (checkDrieButton.isSelected())
			{
				naip.zetCheckDrieAanzichten(true);
			}
		}
		else if (e.getSource() == checkVoorZijButton)
		{
			if (checkVoorZijButton.isSelected())
			{
				naip.zetCheckVoorZijAanzicht(true);
			}
		}
		else if (e.getSource() == checkBovenVoorButton)
		{
			if (checkBovenVoorButton.isSelected())
			{
				naip.zetCheckBovenVoorAanzicht(true);
			}
		}
		else if (e.getSource() == checkBovenZijButton)
		{
			if (checkBovenZijButton.isSelected())
			{
				naip.zetCheckBovenZijAanzicht(true);
			}
		}
		else if (e.getSource() == checkBovenButton)
		{
			if (checkBovenButton.isSelected())
			{
				naip.zetCheckBovenAanzicht(true);
			}
		}
		else if (e.getSource() == checkVoorButton)
		{
			if (checkVoorButton.isSelected())
			{
				naip.zetCheckVoorAanzicht(true);
			}
		}
		else if (e.getSource() == checkRechtsButton)
		{
			if (checkRechtsButton.isSelected())
			{
				naip.zetCheckRechtsAanzicht(true);
			}
		}
		else if (e.getSource() == aantalKubusBox)
		{
			naip.zetCheckAantalKubus(aantalKubusBox.isSelected());
		}
		else if (e.getSource() == logCB)
		{
			logIDTF.setVisible(logCB.isSelected());
			if(!logCB.isSelected())
				logIDTF.setText("");
		}
	}

	/**
	 * Set visible van de componenten die horen bij de nakijkmodus.
	 */
	void setVisibleNakijkModusFields(boolean b)
	{
		checkBouwselButton.setEnabled(b);
		checkDrieButton.setEnabled(b);
		checkVoorZijButton.setEnabled(b);
		checkBovenVoorButton.setEnabled(b);
		checkBovenZijButton.setEnabled(b);
		checkBovenButton.setEnabled(b);
		checkVoorButton.setEnabled(b);
		checkRechtsButton.setEnabled(b);
		aantalKubusBox.setEnabled(b);
		maxScoreVeld.setEditable(b);
	}

	class TextFL implements FocusListener
	{
		JTextField inputTextField;

		public TextFL(JTextField input)
		{
			inputTextField = input;
		}

		public void focusGained(FocusEvent e)
		{
		}

		public void focusLost(FocusEvent e)
		{
			// invoer user
			String text = inputTextField.getText();
			String oldText = "" + naip.v.kr.maxAantal;

			// komma gebruikt
			if (text.indexOf(',') >= 0)
			{
				String text1 = trimTrailingZeros(text, ',');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, ',');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{
					text = text2;
				}
			}
			
			// punt gebruikt
			if (text.indexOf('.') >= 0)
			{
				String text1 = trimTrailingZeros(text, '.');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, '.');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{
					text = text2;
				}
			}
			
			inputTextField.setText(text);

			String format = new String(text);
			format = format.replace(',', '.');

			double userInput = 0;
			boolean error = false;
			try
			{
				userInput = Double.parseDouble(format);
			}
			catch (NumberFormatException nfe)
			{
				error = true;
			}

			// dit zou niet moeten gebeuren
			// Peter: nu wel bij de definitie van een random variabele ipv een
			// double
			if (error)
				return;

			if (inputTextField == roosterTextField)
			{
				int rGrootte = (int) userInput;

				if ((rGrootte >= 2) && (rGrootte <= 15))
				{
					naip.zetRoosterGrootte(rGrootte);
				}
				else
				{
					inputTextField.setText(oldText);
				}
			}

			if (inputTextField == maxScoreVeld)
			{

				int mScore = (int) userInput;

				if ((mScore >= 0) && (mScore <= 1500))
				{
					naip.zetMaxScore(mScore);
				}
				else
				{
					inputTextField.setText(oldText);
				}
			}

		} // focusLost
	}

	class TextAL implements ActionListener
	{
		JTextField inputTextField;

		public TextAL(JTextField input)
		{
			inputTextField = input;
		}

		public void actionPerformed(ActionEvent e)
		{
			String text = inputTextField.getText();
			String oldText = "" + naip.v.kr.maxAantal;

			// komma gebruikt
			if (text.indexOf(',') >= 0)
			{
				String text1 = trimTrailingZeros(text, ',');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, ',');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{
					text = text2;
				}
			}
			
			// punt gebruikt
			if (text.indexOf('.') >= 0)
			{
				String text1 = trimTrailingZeros(text, '.');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, '.');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{
					text = text2;
				}
			}
			inputTextField.setText(text);

			String format = new String(text);
			format = format.replace(',', '.');

			double userInput = 0;
			boolean error = false;
			try
			{
				userInput = Double.parseDouble(format);
			}
			catch (NumberFormatException nfe)
			{
				error = true;
			}
			
			// dit zou niet moeten gebeuren
			// Peter: nu wel bij de definitie van een random variabele ipv een
			// double
			if (error)
			{
				return;
			}

			if (inputTextField == roosterTextField)
			{

				int rGrootte = (int) userInput;

				if ((rGrootte >= 2) && (rGrootte <= 15))
				{
					naip.zetRoosterGrootte(rGrootte);
				}
				else
				{
					inputTextField.setText(oldText);
				}
			}

			if (inputTextField == maxScoreVeld)
			{

				int mScore = (int) userInput;

				if ((mScore >= 0) && (mScore <= 1500))
				{
					naip.zetMaxScore(mScore);
				}
				else
				{
					inputTextField.setText(oldText);
				}
			}

		} // actionPerformed
	}

	public String trimTrailingZeros(String s, char decSep)
	{
		String txt = new String(s);
		if (txt.indexOf(decSep) < 0)
			return txt;
		char c = txt.charAt(txt.length() - 1);
		while (c == '0')
		{
			txt = removeCharAt(txt, txt.length() - 1);
			c = txt.charAt(txt.length() - 1);
		}
		c = txt.charAt(txt.length() - 1);
		if (c == decSep)
			txt = removeCharAt(txt, txt.length() - 1);
		return txt;
	}

	public String addLeadingZero(String s, char decSep)
	{
		String txt = new String(s);
		// met minteken
		if ((txt.length() >= 2) && (txt.charAt(0) == '-') && (txt.charAt(1) == decSep))
		{
			txt = "-0" + txt.substring(1);
		}
		
		// zonder minteken
		if ((txt.length() >= 1) && (txt.charAt(0) == decSep))
		{
			txt = "0" + txt;
		}
		return txt;
	}

	public String removeCharAt(String s, int index)
	{
		String txt = new String(s);
		// eerste
		if (index == 0)
			txt = txt.substring(1);
		// laatste
		else if (index == (txt.length() - 1))
			txt = txt.substring(0, txt.length() - 1);
		// middenin
		else
		{
			String txt1 = txt.substring(0, index);
			String txt2 = txt.substring(index + 1);
			txt = txt1 + txt2;
		}
		return txt;
	}

	class InputKL extends KeyAdapter
	{
		JTextField inputTextField;
		boolean minusAllowed;
		boolean posIntegerInput;

		public InputKL(JTextField input, boolean minAllowed, boolean posIntInput)
		{
			inputTextField = input;
			minusAllowed = minAllowed;
			posIntegerInput = posIntInput;
		}

		public void keyReleased(KeyEvent e)
		{
			inputTextField.setForeground(Color.black);

			String txt = inputTextField.getText();

			// om randomvariabele in te kunnen vullen
			if (isLegal(txt))
				return;

			boolean corrected = false;

			// kijk of txt illegale characters bevat
			// dit zou er maximaal 1 moeten zijn
			int index = -1;
			for (int cCnt = 0; cCnt < txt.length(); cCnt++)
			{
				char c = txt.charAt(cCnt);
				if (!isLegal(c))
				{
					index = cCnt;
				}
			}
			// verwijder illegaal karakter
			if (index >= 0)
			{
				txt = removeCharAt(txt, index);
				corrected = true;
			}

			// dubbele decimale komma
			// voldoende er twee te zoeken
			int pIndex1 = txt.indexOf(',');
			int pIndex2 = txt.lastIndexOf(',');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 != pIndex2))
			{
				// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}

			// dubbele decimale punt
			// voldoende er twee te zoeken
			pIndex1 = txt.indexOf('.');
			pIndex2 = txt.lastIndexOf('.');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 != pIndex2))
			{
				// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}

			// komma na decimale punt
			pIndex1 = txt.indexOf('.');
			pIndex2 = txt.lastIndexOf(',');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 < pIndex2))
			{
				// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}

			// punt na decimale komma
			pIndex1 = txt.indexOf(',');
			pIndex2 = txt.lastIndexOf('.');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 < pIndex2))
			{
				// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}

			// proberen een legaal karakter voor het
			// minteken (dit staat dan op plek 1) in te vullen
			if (txt.indexOf('-') == 1)
			{
				txt = removeCharAt(txt, 0);
				corrected = true;
			}

			// minteken
			// alleen vooraan if any
			int minIndex = txt.lastIndexOf('-');
			if (minIndex > 0)
			{
				txt = removeCharAt(txt, minIndex);
				corrected = true;
			}

			// leading zeros, leiden niet tot een NumberFormatException
			// geval met minteken
			if ((txt.indexOf('-') == 0) && (txt.length() >= 3) && (txt.charAt(1) == '0')
				&& Character.isDigit(txt.charAt(2)))
			{
				txt = removeCharAt(txt, 1);
				corrected = true;
			}

			// leading zeros, leiden niet tot een NumberFormatException
			// geen minteken
			if ((txt.indexOf('-') < 0) && (txt.length() >= 2) && (txt.charAt(0) == '0')
				&& Character.isDigit(txt.charAt(1)))
			{
				txt = removeCharAt(txt, 0);
				corrected = true;
			}

			// trailing zeros na(!) decimale punt oplossen
			// bij actionPerformed of focusLost

			if (corrected)
			{
				inputTextField.setText(txt);
			}
		}

		public boolean isLegal(String s)
		{
			if (s != null && s.length() > 0)
				return s.charAt(0) == '#';
			else
				return false;
		}

		public boolean isLegal(char c)
		{
			if (posIntegerInput)
				return Character.isDigit(c);

			if (minusAllowed)
				return Character.isDigit(c) || (c == ',') || (c == '.') || (c == '-');
			else
				return Character.isDigit(c) || (c == ',') || (c == '.');
		}
	}

	class TabbedPaneCL implements ChangeListener
	{

		public void stateChanged(ChangeEvent e)
		{
			noSetBounds = true;
			int index = tabbedPane.getSelectedIndex();
			
			// terug naar viewerOptionsPanel
			if (index == 0)
			{
				naip.toonDocentViewer(false);

			}
			else // naar nakijkOptiesPanel
			{
				if (nakijkBox.isSelected() || checkExternalBox.isSelected())
					naip.toonDocentViewer(true);
			}
		}
	}
}
