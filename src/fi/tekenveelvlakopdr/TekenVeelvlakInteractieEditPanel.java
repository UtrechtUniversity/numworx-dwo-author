package fi.tekenveelvlakopdr;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class TekenVeelvlakInteractieEditPanel extends JPanel
	implements InteractieEditPanel, ActionListener, ItemListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int MARGE_LABEL = 5;
	private static final int HEIGHT_LABEL = 25;
	
	int editWidth = 300;
	int editHeight = 450;
	int tvipBreedte = 500; // startbreedte tvip
	int tvipHoogte = 450; // starthoogte tvip

	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;

	int offSet = 9;
	boolean componentsCreated = false;

	boolean noSetBounds = false;

	private JLabel antwoordModelLabel;
	
	protected TekenVeelvlakInteractiePanel tvip;

//	private JCheckBox viewerOnlyCB;
	JRadioButton teacherViewRB;
	/**
	 * De radio button groep voor het kiezen van het type tool: de veelvlak
	 * tekentool of een van de verschillende viewers.
	 */
	ButtonGroup toolGroup;
	JRadioButton tekenVeelvlakRB;
	JRadioButton toonViewerRB;

	ButtonGroup viewGroup;
	private JRadioButton moveableRB;
	private JRadioButton frontViewRB, backViewRB, topViewRB, bottomViewRB, leftViewRB, rightViewRB, allViewsRB;

	private JLabel hulppuntenLabel;
	private JTextField hulppuntenTF;

	JCheckBox frontArrowCB;

	JCheckBox vlakkenKleurenCB;

	JTabbedPane tabbedPane;

	JPanel tekenVVOptiesPanel, nakijkOptiesPanel;

	JCheckBox kijkDraaihoekNaCB;

	JRadioButton dezeDraaihoekRB, voorkantRB, achterkantRB, bovenkantRB, onderkantRB, linkerkantRB, rechterkantRB;
	ButtonGroup kijkDraaihoekNaGroup;

	JCheckBox kijkVlakkenNaCB;

	JCheckBox checkExternalCB;

	// JRadioButton profielenKleurenRB, viewerKleurenRB;
	// ButtonGroup kleurGroup;

	double laatsteDraaiX, laatsteDraaiY;

	double tvipDraaiX, tvipDraaiY;
	boolean tvipViewerOnly;
	boolean tvipProfilesOnly;
	int tvipViewerPosition;

	JLabel maxScoreLabel;
	JTextField maxScoreVeld;

	int scoreMax = 10;

	public TekenVeelvlakInteractieEditPanel()
	{
		setLayout(null);
		setBackground(Color.WHITE);

		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);

		antwoordModelLabel = new JLabel(TekenVeelvlakOpdr.rb.getString("antwoordModelLabel"));
		antwoordModelLabel.setBounds(MARGE_LABEL, MARGE_LABEL, theBoldFM.stringWidth(TekenVeelvlakOpdr.rb.getString("antwoordModelLabel")), HEIGHT_LABEL);
		antwoordModelLabel.setVisible(false); // initieel staat tekenveelvlakopties-tab geselecteerd
		add(antwoordModelLabel);
		
		tvip = new TekenVeelvlakInteractiePanel();
		tvip.setBackground(Color.WHITE);
		add(tvip);
		tvip.editMode = this;

		tabbedPane = new JTabbedPane();

		tekenVVOptiesPanel = new JPanel();
		tekenVVOptiesPanel.setLayout(null);
		tabbedPane.addTab(TekenVeelvlakOpdr.rb.getString("tekenVVOptiesLabel"), tekenVVOptiesPanel);

		nakijkOptiesPanel = new JPanel();
		nakijkOptiesPanel.setLayout(null);
		tabbedPane.addTab(TekenVeelvlakOpdr.rb.getString("nakijkOptiesLabel"), nakijkOptiesPanel);

		tabbedPane.setEnabledAt(1, false);

		tabbedPane.setBounds(getSize().width - editWidth, 0, editWidth, getSize().height);
		add(tabbedPane);

		tabbedPane.addChangeListener(new TabbedPaneCL());

		int width = editWidth - 2 * offSet;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = offSet;
		int currentY = offSet;

		// Tekenveelvlakopties-tab
		// nieuwe radiobutton voor tool-keuze
		toolGroup = new ButtonGroup();

		tekenVeelvlakRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("tekenVeelvlakRBLabel"), true);
		tekenVeelvlakRB.setBounds(currentX, currentY, width, height);
		tekenVeelvlakRB.setOpaque(false);
		tekenVVOptiesPanel.add(tekenVeelvlakRB);
		toolGroup.add(tekenVeelvlakRB);
		tekenVeelvlakRB.addItemListener(this);

		currentY += height + offSet/2;

		hulppuntenLabel = new JLabel(TekenVeelvlakOpdr.rb.getString("hulpPuntenCBLabel"));
		hulppuntenLabel.setBounds(currentX + 2 * offSet, currentY, width, height);
		tekenVVOptiesPanel.add(hulppuntenLabel);

		hulppuntenTF = new JTextField("0");
		hulppuntenTF.setBounds(currentX + 2 * offSet + theFM.stringWidth(TekenVeelvlakOpdr.rb.getString("hulpPuntenCBLabel")) + 10, currentY, 35, height);
		tekenVVOptiesPanel.add(hulppuntenTF);
		hulppuntenTF.addActionListener(this);

		currentY += height + offSet;

		toonViewerRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("toonViewerRBLabel"), true);
		toonViewerRB.setBounds(currentX, currentY, width, height);
		toonViewerRB.setOpaque(false);
		tekenVVOptiesPanel.add(toonViewerRB);
		toolGroup.add(toonViewerRB);
		toonViewerRB.addItemListener(this);

		currentY += height; // + offSet/2;

		viewGroup = new ButtonGroup();

		moveableRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("draaibaarRBLabel"), true);
		moveableRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		moveableRB.setOpaque(false);
		tekenVVOptiesPanel.add(moveableRB);
		moveableRB.addItemListener(this);
		viewGroup.add(moveableRB);

		currentY += height; // + offSet/4;

		// wat is dit? "Docent draaihoek"
		teacherViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("docentDraaihoekRBLabel"), false);
		teacherViewRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		teacherViewRB.setOpaque(false);
		// tekenVVOptiesPanel.add(teacherViewRB);
		// teacherViewRB.addItemListener(this);
		// viewGroup.add(teacherViewRB);

		// currentY += height + offSet;

		frontViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("vooraanzichtRBLabel"), true);
		frontViewRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		frontViewRB.setOpaque(false);
		tekenVVOptiesPanel.add(frontViewRB);
		frontViewRB.addItemListener(this);
		viewGroup.add(frontViewRB);

		currentY += height; // + offSet/4;

		backViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("achteraanzichtRBLabel"), true);
		backViewRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		backViewRB.setOpaque(false);
		tekenVVOptiesPanel.add(backViewRB);
		backViewRB.addItemListener(this);
		viewGroup.add(backViewRB);

		currentY += height; // + offSet/2;

		topViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("bovenaanzichtRBLabel"), true);
		topViewRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		topViewRB.setOpaque(false);
		tekenVVOptiesPanel.add(topViewRB);
		topViewRB.addItemListener(this);
		viewGroup.add(topViewRB);

		currentY += height; // + offSet/2;

		bottomViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("onderaanzichtRBLabel"), true);
		bottomViewRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		bottomViewRB.setOpaque(false);
		tekenVVOptiesPanel.add(bottomViewRB);
		bottomViewRB.addItemListener(this);
		viewGroup.add(bottomViewRB);

		currentY += height; // + offSet/2;

		leftViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("linkeraanzichtRBLabel"), true);
		leftViewRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		leftViewRB.setOpaque(false);
		tekenVVOptiesPanel.add(leftViewRB);
		leftViewRB.addItemListener(this);
		viewGroup.add(leftViewRB);

		currentY += height; // + offSet/2;

		rightViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("rechteraanzichtRBLabel"), true);
		rightViewRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		rightViewRB.setOpaque(false);
		tekenVVOptiesPanel.add(rightViewRB);
		rightViewRB.addItemListener(this);
		viewGroup.add(rightViewRB);

		currentY += height;

		// voorheen 'Toon profielen' nu '2D alle aanzichten'
		allViewsRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("alleAanzichtenRBLabel"), true);
		allViewsRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		allViewsRB.setOpaque(false);
		tekenVVOptiesPanel.add(allViewsRB);
		viewGroup.add(allViewsRB);
		allViewsRB.addItemListener(this);

		currentY += height + offSet;

		vlakkenKleurenCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("vlakkenKleurenCBLabel"));
		vlakkenKleurenCB.setBounds(currentX + 2 * offSet, currentY, width, height);
		vlakkenKleurenCB.setOpaque(false);
		tekenVVOptiesPanel.add(vlakkenKleurenCB);
		vlakkenKleurenCB.addItemListener(this);
		vlakkenKleurenCB.setEnabled(false);

		currentY += height + 2 * offSet;

		// setting geldig voor zowel tekentool als viewer 
		frontArrowCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("vooraanzichtPijlCBLabel"));
		frontArrowCB.setBounds(currentX, currentY, width, height);
		frontArrowCB.setOpaque(false);
		frontArrowCB.setSelected(false);
		tekenVVOptiesPanel.add(frontArrowCB);
		frontArrowCB.addActionListener(this);

		// Nakijkopties-tab

		currentY += height; // + offSet;

		currentX = offSet;
		currentY = offSet;

		kijkDraaihoekNaCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("kijkDraaihoekNaCBLabel"));
		kijkDraaihoekNaCB.setBounds(currentX, currentY, width, height);
		kijkDraaihoekNaCB.setOpaque(false);
		nakijkOptiesPanel.add(kijkDraaihoekNaCB);
		kijkDraaihoekNaCB.addItemListener(this);

		currentY += height; // + offSet/4;

		kijkDraaihoekNaGroup = new ButtonGroup();

		dezeDraaihoekRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("dezeDraaihoekRBLabel"), false);
		dezeDraaihoekRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		dezeDraaihoekRB.setOpaque(false);
		nakijkOptiesPanel.add(dezeDraaihoekRB);
		dezeDraaihoekRB.addItemListener(this);
		kijkDraaihoekNaGroup.add(dezeDraaihoekRB);

		currentY += height; // + offSet/4;

		voorkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("voorkantRBLabel"), false);
		voorkantRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		voorkantRB.setOpaque(false);
		nakijkOptiesPanel.add(voorkantRB);
		voorkantRB.addItemListener(this);
		kijkDraaihoekNaGroup.add(voorkantRB);

		currentY += height; // + offSet/4;

		achterkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("achterkantRBLabel"), false);
		achterkantRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		achterkantRB.setOpaque(false);
		nakijkOptiesPanel.add(achterkantRB);
		achterkantRB.addItemListener(this);
		kijkDraaihoekNaGroup.add(achterkantRB);

		currentY += height; // + offSet/2;

		bovenkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("bovenkantRBLabel"), false);
		bovenkantRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		bovenkantRB.setOpaque(false);
		nakijkOptiesPanel.add(bovenkantRB);
		bovenkantRB.addItemListener(this);
		kijkDraaihoekNaGroup.add(bovenkantRB);

		currentY += height; // + offSet/2;

		onderkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("onderkantRBLabel"), false);
		onderkantRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		onderkantRB.setOpaque(false);
		nakijkOptiesPanel.add(onderkantRB);
		onderkantRB.addItemListener(this);
		kijkDraaihoekNaGroup.add(onderkantRB);

		currentY += height; // + offSet/2;

		linkerkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("linkerkantRBLabel"), false);
		linkerkantRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		linkerkantRB.setOpaque(false);
		nakijkOptiesPanel.add(linkerkantRB);
		linkerkantRB.addItemListener(this);
		kijkDraaihoekNaGroup.add(linkerkantRB);

		currentY += height; // + offSet/2;

		rechterkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("rechterkantRBLabel"), false);
		rechterkantRB.setBounds(currentX + 2 * offSet, currentY, width, height);
		rechterkantRB.setOpaque(false);
		nakijkOptiesPanel.add(rechterkantRB);
		rechterkantRB.addItemListener(this);
		kijkDraaihoekNaGroup.add(rechterkantRB);

		zetDraaihoekOptiesEnabled(false);
		zetViewerOptiesEnabled(false);

		currentY += height + 2 * offSet;

		kijkVlakkenNaCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("kijkVlakkenNaCBLabel"));
		kijkVlakkenNaCB.setBounds(currentX, currentY, width, height);
		kijkVlakkenNaCB.setOpaque(false);
		nakijkOptiesPanel.add(kijkVlakkenNaCB);
		kijkVlakkenNaCB.addItemListener(this);

		currentY += height - 5;

		currentY += height;

		checkExternalCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("checkExternal"));
		checkExternalCB.setBounds(currentX, currentY, width, height);
		checkExternalCB.setOpaque(false);
		nakijkOptiesPanel.add(checkExternalCB);
		checkExternalCB.addItemListener(this);

		currentY += height + 2 * offSet;

		zetVlakkenKleurenEnabled(false);

		maxScoreLabel = new JLabel(TekenVeelvlakOpdr.rb.getString("maxScoreTekst"));
		maxScoreLabel.setFont(theFont);
		maxScoreLabel.setBackground(Color.white);
		width = theFM.stringWidth(maxScoreLabel.getText());
		maxScoreLabel.setBounds(currentX + MARGE_LABEL, currentY + 3, width, theFM.getHeight());
		nakijkOptiesPanel.add(maxScoreLabel);

		//currentY += height; // + offset;

		maxScoreVeld = new JTextField("" + scoreMax);
		maxScoreVeld.setFont(theFont);
		maxScoreVeld.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXX");
		maxScoreVeld.setBounds(currentX + theFM.stringWidth(TekenVeelvlakOpdr.rb.getString("maxScoreTekst") + 2 * MARGE_LABEL), currentY, width, height);
		nakijkOptiesPanel.add(maxScoreVeld);

		maxScoreVeld.addKeyListener(new InputKL2(maxScoreVeld));
		maxScoreVeld.addActionListener(new TextAL2(maxScoreVeld));
		maxScoreVeld.addFocusListener(new TextFL2(maxScoreVeld));

		componentsCreated = true;
		
		zetKijkNaTab();
	}

	public void showNakijkOpties(boolean b)
	{
		tabbedPane.setEnabledAt(1, b);
		if (b)
		{
		}
		else
		{
			kijkDraaihoekNaCB.setEnabled(false);
			dezeDraaihoekRB.setSelected(true);
			zetDraaihoekOptiesEnabled(false);
			tvip.docentDraaihoekX = 1e-5d;
			tvip.docentDraaihoekY = 1e-5d;

			kijkVlakkenNaCB.setEnabled(false);
			checkExternalCB.setEnabled(false);
			tvip.docentKleuren = null;
			tvip.viewer.kijkNaPanel.setVisible(false);
			tvip.vaktek.kijkNaPanel.setVisible(false);
		}
	}

	public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			/*
			 * hulppuntenLabel.setLocation(tvip.getSize().width + offSet,
			 * hulppuntenLabel.getLocation().y);
			 * hulppuntenTF.setLocation(tvip.getSize().width + 3*offSet,
			 * hulppuntenTF.getLocation().y);
			 * 
			 * frontArrowCB.setLocation(tvip.getSize().width + offSet,
			 * frontArrowCB.getLocation().y);
			 * 
			 * viewerOnlyCB.setLocation(tvip.getSize().width + offSet,
			 * viewerOnlyCB.getLocation().y);
			 * moveableRB.setLocation(tvip.getSize().width + offSet,
			 * moveableRB.getLocation().y);
			 * frontViewRB.setLocation(tvip.getSize().width + offSet,
			 * frontViewRB.getLocation().y);
			 * backViewRB.setLocation(tvip.getSize().width + offSet,
			 * backViewRB.getLocation().y);
			 * topViewRB.setLocation(tvip.getSize().width + offSet,
			 * topViewRB.getLocation().y);
			 * bottomViewRB.setLocation(tvip.getSize().width + offSet,
			 * bottomViewRB.getLocation().y);
			 * leftViewRB.setLocation(tvip.getSize().width + offSet,
			 * leftViewRB.getLocation().y);
			 * rightViewRB.setLocation(tvip.getSize().width + offSet,
			 * rightViewRB.getLocation().y);
			 * 
			 * profilesOnlyCB.setLocation(tvip.getSize().width + offSet,
			 * profilesOnlyCB.getLocation().y);
			 * 
			 * vlakkenKleurenCB.setLocation(tvip.getSize().width + offSet,
			 * vlakkenKleurenCB.getLocation().y);
			 * profielenKleurenRB.setLocation(tvip.getSize().width + offSet,
			 * profielenKleurenRB.getLocation().y);
			 * viewerKleurenRB.setLocation(tvip.getSize().width + offSet,
			 * viewerKleurenRB.getLocation().y);
			 */
			tabbedPane.setBounds(tvipBreedte, 0, editWidth, getSize().height);
		}
	}

	public void viewerMuisLos()
	{
		if (kijkDraaihoekNaCB.isSelected() && dezeDraaihoekRB.isSelected())
		{
			laatsteDraaiX = tvip.viewer.geefDraaiX();
			laatsteDraaiY = tvip.viewer.geefDraaiY();
		}
	}

	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource() == tekenVeelvlakRB)
		{
			zetTekenToolOptiesEnabled((tekenVeelvlakRB.isSelected()));
			// toon tekentool...
			if (tekenVeelvlakRB.isSelected())
			{
				tvip.setViewerOnly(false);
				tvip.setProfilesOnly(false);
			}
		}
		else if (e.getSource() == toonViewerRB)
		{
			zetViewerOptiesEnabled(toonViewerRB.isSelected());
			tvip.setViewerOnly(toonViewerRB.isSelected());
			
			if (toonViewerRB.isSelected())
			{
				if (allViewsRB.isSelected())
					tvip.setProfilesOnly(true);
				else
					zetViewerPosition();
				
				zetKijkNaTab();
			}
			else
			{
				tvip.setProfilesOnly(false);
			}
		}
		else if (e.getSource() == moveableRB)
		{
			if (moveableRB.isSelected())
			{
				tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.MOVEABLE);
				zetKijkNaTab();
			}
		}
		else if (e.getSource() == teacherViewRB)
		{
			if (teacherViewRB.isSelected())
				tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.TEACHER);
		}

		else if (e.getSource() == frontViewRB)
		{
			if (frontViewRB.isSelected())
			{
				tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.FRONTVIEW);
				zetKijkNaTab();
			}
		}
		else if (e.getSource() == backViewRB)
		{
			if (backViewRB.isSelected())
			{
				tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.BACKVIEW);
				zetKijkNaTab();
			}
		}
		else if (e.getSource() == topViewRB)
		{
			if (topViewRB.isSelected())
			{
				tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.TOPVIEW);
				zetKijkNaTab();
			}
		}
		else if (e.getSource() == bottomViewRB)
		{
			if (bottomViewRB.isSelected())
			{
				tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.BOTTOMVIEW);
				zetKijkNaTab();
			}
		}
		else if (e.getSource() == leftViewRB)
		{
			if (leftViewRB.isSelected())
			{
				tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.LEFTVIEW);
				zetKijkNaTab();
			}
		}
		else if (e.getSource() == rightViewRB)
		{
			if (rightViewRB.isSelected())
			{
				tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.RIGHTVIEW);
				zetKijkNaTab();
			}
		}
		else if (e.getSource() == allViewsRB)
		{
			if (allViewsRB.isSelected())
			{
				tvip.setProfilesOnly(true);
				tvip.setViewerOnly(false);
				zetKijkNaTab();
			}
			else
			{
				tvip.setViewerOnly(true);
				tvip.setProfilesOnly(false);
			}
		}
		else if (e.getSource() == vlakkenKleurenCB)
		{
			tvip.zetVlakkenKleurenOptie(vlakkenKleurenCB.isSelected());
			zetVlakkenKleurenEnabled(vlakkenKleurenCB.isSelected());

			zetViewerOptiesEnabled(toonViewerRB.isSelected());
			zetKijkNaTab();
		}
		else if (e.getSource() == kijkDraaihoekNaCB)
		{
			tvip.zetKijkDraaihoekNa(kijkDraaihoekNaCB.isSelected());

			if (kijkDraaihoekNaCB.isSelected())
			{
				zetDraaihoekOpties(true);
				checkExternalCB.setEnabled(true);
				kijkVlakkenNaCB.setEnabled(false);
			}
			else
			{
				zetDraaihoekOpties(false);
				if (vlakkenKleurenCB.isSelected())
					kijkVlakkenNaCB.setEnabled(true);
				checkExternalCB.setEnabled(kijkVlakkenNaCB.isSelected());
			}

			zetKijkNaKnop();
			zetKijkNaTab();
		}
		else if (e.getSource() == checkExternalCB)
		{
			if (kijkDraaihoekNaCB.isSelected())
				tvip.zetCheckExternalDraaihoek(checkExternalCB.isSelected());
			else if (kijkVlakkenNaCB.isSelected())
				tvip.zetCheckExternalVlakken(checkExternalCB.isSelected());

			zetKijkNaKnop();
		}
		else if (e.getSource() == dezeDraaihoekRB)
		{
			if (dezeDraaihoekRB.isSelected())
			{
				zetDocentDraaihoek(laatsteDraaiX, laatsteDraaiY);
				tvip.viewer.zetAfstand(1000);
				tvip.viewer.zetSchaduw(true);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = true;
			}
		}
		else if (e.getSource() == voorkantRB)
		{
			if (voorkantRB.isSelected())
			{
				if (tvip.viewer.muisAan)
				{
					laatsteDraaiX = tvip.viewer.geefDraaiX();
					laatsteDraaiY = tvip.viewer.geefDraaiY();
				}

				zetDocentDraaihoek(0, 0);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
			}
		}
		else if (e.getSource() == achterkantRB)
		{
			if (tvip.viewer.muisAan)
			{
				laatsteDraaiX = tvip.viewer.geefDraaiX();
				laatsteDraaiY = tvip.viewer.geefDraaiY();
			}

			if (achterkantRB.isSelected())
			{
				zetDocentDraaihoek(0, 180);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
			}
		}
		else if (e.getSource() == bovenkantRB)
		{
			if (tvip.viewer.muisAan)
			{
				laatsteDraaiX = tvip.viewer.geefDraaiX();
				laatsteDraaiY = tvip.viewer.geefDraaiY();
			}

			if (bovenkantRB.isSelected())
			{
				zetDocentDraaihoek(90, 0);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
			}
		}
		else if (e.getSource() == onderkantRB)
		{
			if (tvip.viewer.muisAan)
			{
				laatsteDraaiX = tvip.viewer.geefDraaiX();
				laatsteDraaiY = tvip.viewer.geefDraaiY();
			}

			if (onderkantRB.isSelected())
			{
				zetDocentDraaihoek(-90, 0);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
			}
		}
		else if (e.getSource() == linkerkantRB)
		{
			if (tvip.viewer.muisAan)
			{
				laatsteDraaiX = tvip.viewer.geefDraaiX();
				laatsteDraaiY = tvip.viewer.geefDraaiY();
			}

			if (linkerkantRB.isSelected())
			{
				zetDocentDraaihoek(0, 90);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
			}
		}
		else if (e.getSource() == rechterkantRB)
		{
			if (tvip.viewer.muisAan)
			{
				laatsteDraaiX = tvip.viewer.geefDraaiX();
				laatsteDraaiY = tvip.viewer.geefDraaiY();
			}

			if (rechterkantRB.isSelected())
			{
				zetDocentDraaihoek(0, -90);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
			}
		}
		else if (e.getSource() == kijkVlakkenNaCB)
		{
			tvip.zetKijkVlakkenNa(kijkVlakkenNaCB.isSelected());

			if (kijkVlakkenNaCB.isSelected())
			{
				zetVlakkenOpties(true);
				checkExternalCB.setEnabled(true);
				kijkDraaihoekNaCB.setEnabled(false);
			}
			else
			{
				zetVlakkenOpties(false);
				
				if (isKijkDraaihoekNaMogelijk())
				{
					kijkDraaihoekNaCB.setEnabled(true);
					if (kijkDraaihoekNaCB.isSelected())
						checkExternalCB.setEnabled(true);
					else
						checkExternalCB.setEnabled(false);
				}
			}

			zetKijkNaKnop();
			zetKijkNaTab();
		}
	}
	
	/**
	 * True als draaihoek nakijken mogelijk is voor de gekozen viewer, false
	 * als dit niet zo is, of als de tekentool is geselecteerd.
	 * 
	 * @return
	 */
	private boolean isKijkDraaihoekNaMogelijk()
	{
		boolean isMogelijk = false;
		if (toonViewerRB.isSelected() && moveableRB.isSelected())
			isMogelijk = true;
		else
			isMogelijk = false;
		
		return isMogelijk;
	}

	/**
	 * Zet de viewer positie van de gekozen view.
	 */
	private void zetViewerPosition()
	{
		if (moveableRB.isSelected())
			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.MOVEABLE);
		else if (teacherViewRB.isSelected())
			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.TEACHER);
		else if (frontViewRB.isSelected())
			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.FRONTVIEW);
		else if (backViewRB.isSelected())
			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.BACKVIEW);
		else if (topViewRB.isSelected())
			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.TOPVIEW);
		else if (bottomViewRB.isSelected())
			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.BOTTOMVIEW);
		else if (leftViewRB.isSelected())
			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.LEFTVIEW);
		else if (rightViewRB.isSelected())
			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.RIGHTVIEW);
	}
	
	/**
	 * Gegeven alle instellingen zet het nakijktab al dan niet enabled
	 * met de juiste onderdelen enabled.
	 */
	private void zetKijkNaTab()
	{
		if (tekenVeelvlakRB.isSelected())
		{
			// nakijktab disablen
			tabbedPane.setEnabledAt(1, false);
		}
		else if (toonViewerRB.isSelected())
		{
			if (moveableRB.isSelected())
			{
				// nakijktab enablen
				tabbedPane.setEnabledAt(1, true);
				kijkDraaihoekNaCB.setEnabled(!kijkVlakkenNaCB.isSelected());
				if (vlakkenKleurenCB.isSelected())
				{
					kijkVlakkenNaCB.setEnabled(!kijkDraaihoekNaCB.isSelected());
				}	
			}
			else
			{
				if (vlakkenKleurenCB.isSelected())
				{
					// nakijktab enablen
					tabbedPane.setEnabledAt(1, true);
					kijkDraaihoekNaCB.setEnabled(false);
					kijkVlakkenNaCB.setEnabled(true);
				}
				else
				{
					// nakijktab enablen
					tabbedPane.setEnabledAt(1, false);
				}
			}
			
			checkExternalCB.setEnabled(kijkDraaihoekNaCB.isSelected() || kijkVlakkenNaCB.isSelected());
		}
	}

	private void zetKijkNaKnop()
	{
		if (checkExternalCB.isSelected())
			tvip.viewer.setVisibleKijkNaButton(false);
		else
			tvip.viewer.setVisibleKijkNaButton(true);
	}

	private void zetDraaihoekOpties(boolean b)
	{
		if (b)
		{
			kijkVlakkenNaCB.setSelected(false);
			zetVlakkenKleurenOptiesEnabled(false);
		}
		else
		{
			tvip.vaktek.kijkNaPanel.setVisible(false);
		}

		zetDraaihoekOptiesEnabled(b);
		tvip.viewer.kijkNaPanel.setVisible(b);
	}

	/**
	 * Zet de vlakken-nakijk-opties aan of uit.
	 * 
	 * @param b
	 */
	private void zetVlakkenOpties(boolean b)
	{
		if (b)
		{
			zetVlakkenKleurenOptiesEnabled(true);

			kijkDraaihoekNaCB.setSelected(false);
			zetDraaihoekOptiesEnabled(false);

			tvip.viewer.updateViewerKleuren();
		}
		else
		{
			tvip.vaktek.zetDocentModus(false);
			tvip.vaktek.zetKlikAan(false);
			tvip.vaktek.resetColors();
			tvip.viewer.resetColors();
			tvip.vaktek.kijkNaPanel.setVisible(false);
		}

		tvip.viewer.zetDocentModus(b);
		tvip.viewer.zetKlikAan(b);
		tvip.viewer.kijkNaPanel.setVisible(b);
	}

	public void zetDocentDraaihoek(double ddhX, double ddhY)
	{
		tvip.zetDocentDraaihoek(ddhX, ddhY);
		tvip.viewer.zetBeginHoeken(ddhX, ddhY);
		// tvip.viewer.tekenOpnieuw();
	}

	public int isAanzicht(double ddhX, double ddhY)
	{
		double tol = 5e-1d;
		if ((Math.abs(ddhX) < tol) && (Math.abs(ddhY) < tol))
			return TekenVeelvlakInteractiePanel.FRONTVIEW;
		else if ((Math.abs(ddhX) < tol) && (Math.abs(ddhY - 180) < tol))
			return TekenVeelvlakInteractiePanel.BACKVIEW;
		else if ((Math.abs(ddhX - 90) < tol) && (Math.abs(ddhY) < tol))
			return TekenVeelvlakInteractiePanel.TOPVIEW;
		else if ((Math.abs(ddhX + 90) < tol) && (Math.abs(ddhY) < tol))
			return TekenVeelvlakInteractiePanel.BOTTOMVIEW;
		else if ((Math.abs(ddhX) < tol) && (Math.abs(ddhY - 90) < tol))
			return TekenVeelvlakInteractiePanel.LEFTVIEW;
		else if ((Math.abs(ddhX) < tol) && (Math.abs(ddhY + 90) < tol))
			return TekenVeelvlakInteractiePanel.RIGHTVIEW;
		else
			return -1;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == hulppuntenTF)
		{
			int aantalHulppunten = 0;
			try
			{
				aantalHulppunten = Integer.parseInt(hulppuntenTF.getText());
			}
			catch (Exception ex)
			{
				aantalHulppunten = 0;
			}
			hulppuntenTF.setText("" + aantalHulppunten);
			tvip.zetAantalHulppunten(aantalHulppunten);

		}
		else if (e.getSource() == frontArrowCB)
		{
			tvip.toonVooraanzichtPijl(frontArrowCB.isSelected());
		}

		/*
		 * else if (e.getSource() == vlakkenKleurenCB) {
		 * tvip.zetVlakkenKleurenOptie(vlakkenKleurenCB.isSelected());
		 * //zetVlakkenKleurenOptiesEnabled(vlakkenKleurenCB.isSelected());
		 * 
		 * if (vlakkenKleurenCB.isSelected()) { } else {
		 * //profilesOnlyCB.setEnabled(true); //viewerOnlyCB.setEnabled(true);
		 * zetVlakkenKleurenEnabled(false); }
		 * 
		 * }
		 */
	}

	/**
	 * Switch van nakijkopties-tab naar tekenveelvlakopties-tab.
	 */
	public void nakijkOptiesNaarTekenVeelvlakOpties()
	{
		antwoordModelLabel.setVisible(false);

		if (kijkDraaihoekNaCB.isSelected())
		{
			// laat een viewer zien (niet in docentstand) met nakijkKnop

			tvip.viewer.zetBeginHoeken(tvipDraaiX, tvipDraaiY);
			tvip.viewer.kijkNaPanel.setVisible(true);
			toonViewerRB.setSelected(true);
			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.MOVEABLE);
			tvip.viewer.zetSchaduw(true);
			tvip.viewer.zetAfstand(1000);
			moveableRB.setSelected(true); // draaihoek nakijken alleen mogelijk voor 3D viewer
		}
		else if (kijkVlakkenNaCB.isSelected())
		{
			tvip.viewer.zetDocentModus(false);
			tvip.vaktek.zetDocentModus(false);

			tvip.viewer.zetKleuren(tvip.viewerKleuren);
			tvip.vaktek.setVaktekKleuren(tvip.viewerKleuren);
			if (tvipViewerOnly)
				tvip.setViewerOnly(tvipViewerOnly);
			if (tvipProfilesOnly)
				tvip.setProfilesOnly(tvipProfilesOnly);

			tvip.vaktek.kijkNaPanel.setVisible(true);
		}
		else // geen van twee
		{
			if (tvipViewerOnly)
				tvip.setViewerOnly(tvipViewerOnly);
			if (tvipProfilesOnly)
				tvip.setProfilesOnly(tvipProfilesOnly);
			if (tvip.viewerOnly)
			{
				tvip.viewer.zetBeginHoeken(tvipDraaiX, tvipDraaiY);
			}
			else
			{
				tvip.tekenVeelvlak.zetBeginHoeken(tvipDraaiX, tvipDraaiY);
			}
		}
	}

	/**
	 * Switch van tekenveelvlakopties-tab naar nakijkopties-tab.
	 */
	public void tekenVeelvlakOptiesNaarNakijkOpties()
	{
		antwoordModelLabel.setVisible(true);
		tvipViewerOnly = tvip.viewerOnly;
		tvipProfilesOnly = tvip.profilesOnly;
		tvipViewerPosition = tvip.viewerPosition;
		if (tvip.viewerOnly)
		{
			tvipDraaiX = tvip.viewer.geefDraaiX();
			tvipDraaiY = tvip.viewer.geefDraaiY();
		}
		else
		{
			tvipDraaiX = tvip.tekenVeelvlak.geefDraaiX();
			tvipDraaiY = tvip.tekenVeelvlak.geefDraaiY();
		}

		if (tvip.viewerKleuren == null)
			tvip.viewerKleuren = tvip.viewer.getKleuren();

		if (kijkDraaihoekNaCB.isSelected())
		{
			zetDraaihoekOptiesEnabled(true);
			tvip.setViewerOnly(true);
			tvip.viewer.zetBeginHoeken(tvip.docentDraaihoekX, tvip.docentDraaihoekY);
			if (!dezeDraaihoekRB.isSelected())
			{
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.muisAan = false;
			}
			else
			{
				tvip.viewer.muisAan = true;
			}

		}
		else if (kijkVlakkenNaCB.isSelected())
		{
			tvip.viewer.zetDocentModus(true);
			// tvip.vaktek.zetDocentModus(true);
			// dit is al zo
			// tvip.viewer.zetKlikAan(true);
			// tvip.vaktek.zetKlikAan(true);
			tvip.setViewerOnly(true);
			if (tvip.docentKleuren != null)
				tvip.viewer.zetKleuren(tvip.docentKleuren);

		}
		else // geen van twee
		{

			// doe maar een viewer
			tvip.setViewerOnly(true);
			laatsteDraaiX = tvip.viewer.geefDraaiX();
			laatsteDraaiY = tvip.viewer.geefDraaiY();
			tvip.viewer.resetColors();

			// zetDraaihoekOptiesEnabled(true);
			dezeDraaihoekRB.setSelected(true);

			// if (tvip)

		}

	}

	class TabbedPaneCL implements ChangeListener
	{

		public void stateChanged(ChangeEvent e)
		{
			noSetBounds = true;
			int index = tabbedPane.getSelectedIndex();
			// terug naar tekenVVOptionsPanel
			if (index == 0)
			{
				nakijkOptiesNaarTekenVeelvlakOpties();
			}
			else // naar nakijkOptiesPanel
			{
				tekenVeelvlakOptiesNaarNakijkOpties();
			}
		}
	}

	/**
	 * Zet de opties enabled die horen bij de keuze voor
	 * tekentool 'teken veelvlak'.
	 * @param b
	 */
	private void zetTekenToolOptiesEnabled(boolean b)
	{
		hulppuntenLabel.setEnabled(b);
		hulppuntenTF.setEnabled(b);
	}

	public void zetViewerOptiesEnabled(boolean b)
	{
		moveableRB.setEnabled(b);
		frontViewRB.setEnabled(b);
		backViewRB.setEnabled(b);
		topViewRB.setEnabled(b);
		bottomViewRB.setEnabled(b);
		leftViewRB.setEnabled(b);
		rightViewRB.setEnabled(b);
		allViewsRB.setEnabled(b);

		// zet kijk na draaihoek (in)actief
		if (b)
			kijkDraaihoekNaCB.setEnabled(moveableRB.isSelected());
	}

	public void zetDraaihoekOptiesEnabled(boolean b)
	{
		if (!b)
			dezeDraaihoekRB.setSelected(true);
		
		dezeDraaihoekRB.setEnabled(b);
		voorkantRB.setEnabled(b);
		achterkantRB.setEnabled(b);
		bovenkantRB.setEnabled(b);
		onderkantRB.setEnabled(b);
		linkerkantRB.setEnabled(b);
		rechterkantRB.setEnabled(b);
	}

	public void zetVlakkenKleurenOptiesEnabled(boolean b)
	{
		// kijkVlakkenNaCB.setEnabled(b);
		// kijkVlakkenNaLabel.setEnabled(b);
		// profielenKleurenRB.setEnabled(b);
		// viewerKleurenRB.setEnabled(b);

	}

	public void zetVlakkenKleurenEnabled(boolean b)
	{
		if (b)
		{
			kijkVlakkenNaCB.setEnabled(b);
		}
		else
		{
			kijkVlakkenNaCB.setSelected(false);
			kijkVlakkenNaCB.setEnabled(b);
			tvip.docentKleuren = null;

			// zetVlakkenKleurenOptiesEnabled(b);
			// tvip.viewer.resetColors();
			// tvip.vaktek.resetColors();
			// profielenKleurenRB.setEnabled(b);
			// viewerKleurenRB.setEnabled(b);
		}

		if (!kijkVlakkenNaCB.isEnabled() && !kijkDraaihoekNaCB.isEnabled())
			tabbedPane.setEnabledAt(1, false);
		else
			tabbedPane.setEnabledAt(1, true);
	}

	public void addActionListener(ActionListener al)
	{
		// TODO Auto-generated method stub

	}

	public Hashtable getEditState()
	{
		Hashtable h = tvip.getEditState();

		h.put("tvipBreedte", new Integer(tvipBreedte));
		h.put("tvipHoogte", new Integer(tvipHoogte));

		if (isNakijkModus())
			h.put("scoreMax", new Integer(scoreMax));
		else
			h.put("scoreMax", new Integer(0));

		return h;
	}

	/**
	 * Retourneert of is nakijkmodus en nagekeken moet worden.
	 * 
	 * @return
	 */
	private boolean isNakijkModus()
	{
		boolean b = false;

		b = kijkDraaihoekNaCB.isSelected() || kijkVlakkenNaCB.isSelected();

		return b;
	}

	public void setBounds(int x, int y, int b, int h)
	{
		if ((x < 0) || (y < 0) || (h < 5))
			return;

		super.setBounds(x, y, tvipBreedte + editWidth, Math.max(tvipHoogte, editHeight));

		if (tvip != null)
			tvip.setBounds(0, MARGE_LABEL + HEIGHT_LABEL, tvipBreedte, tvipHoogte);

		plaatsComponenten();

	}

	public void setEditState(Hashtable h)
	{
		// tekenVVOptiesPanel

		boolean toonVooraanzichtPijl = false;
		boolean viewerOnly = false;
		boolean profilesOnly = false;
		int viewerPosition = 0;
		int aantalHulppunten = 0;

		boolean vlakkenKleurenOptie = false;

		if (h.containsKey("toonVooraanzichtPijl"))
			toonVooraanzichtPijl = ((Boolean) h.get("toonVooraanzichtPijl")).booleanValue();

		if (h.containsKey("viewerOnly"))
			viewerOnly = ((Boolean) h.get("viewerOnly")).booleanValue();
		if (h.containsKey("profilesOnly"))
			profilesOnly = ((Boolean) h.get("profilesOnly")).booleanValue();

		if (h.containsKey("viewerPosition"))
			viewerPosition = ((Integer) h.get("viewerPosition")).intValue();

		// if(h.containsKey("basisFiguur"))
		// basisFiguur = ((Integer)h.get("basisFiguur")).intValue();
		if (h.containsKey("aantalHulppunten"))
			aantalHulppunten = ((Integer) h.get("aantalHulppunten")).intValue();

		if (h.containsKey("vlakkenKleurenOptie"))
			vlakkenKleurenOptie = ((Boolean) h.get("vlakkenKleurenOptie")).booleanValue();

		// if (h.containsKey("profielenKleurenOptie"))
		// profielenKleurenOptie = ((Boolean)
		// h.get("profielenKleurenOptie")).booleanValue();
		// if (h.containsKey("viewerKleurenOptie"))
		// viewerKleurenOptie = ((Boolean)
		// h.get("viewerKleurenOptie")).booleanValue();

		frontArrowCB.setSelected(toonVooraanzichtPijl);

		vlakkenKleurenCB.setSelected(vlakkenKleurenOptie);
		vlakkenKleurenCB.setEnabled(viewerOnly || profilesOnly);

		zetVlakkenKleurenOptiesEnabled(vlakkenKleurenOptie);

		if (profilesOnly)
		{
			toonViewerRB.setSelected(true);
			allViewsRB.setSelected(true);
			viewerOnly = true; // compatible maken met oude settings
		}
		else if (viewerPosition == TekenVeelvlakInteractiePanel.MOVEABLE)
			moveableRB.setSelected(true);
		else if (viewerPosition == TekenVeelvlakInteractiePanel.FRONTVIEW)
			frontViewRB.setSelected(true);
		else if (viewerPosition == TekenVeelvlakInteractiePanel.BACKVIEW)
			backViewRB.setSelected(true);
		else if (viewerPosition == TekenVeelvlakInteractiePanel.TOPVIEW)
			topViewRB.setSelected(true);
		else if (viewerPosition == TekenVeelvlakInteractiePanel.BOTTOMVIEW)
			bottomViewRB.setSelected(true);
		else if (viewerPosition == TekenVeelvlakInteractiePanel.LEFTVIEW)
			leftViewRB.setSelected(true);
		else if (viewerPosition == TekenVeelvlakInteractiePanel.RIGHTVIEW)
			rightViewRB.setSelected(true);

		toonViewerRB.setSelected(viewerOnly);
		zetViewerOptiesEnabled(viewerOnly);

		hulppuntenTF.setText("" + aantalHulppunten);

		// nakijkOptiesPanel

		boolean kijkDraaihoekNa = false;
		boolean checkExternalDraaihoek = false;
		double docentDraaihoekX = 20;
		double docentDraaihoekY = -30;

		if (h.containsKey("kijkDraaihoekNa"))
			kijkDraaihoekNa = ((Boolean) h.get("kijkDraaihoekNa")).booleanValue();
		kijkDraaihoekNaCB.setSelected(kijkDraaihoekNa);
		if (h.containsKey("checkExternalDraaihoek"))
			checkExternalDraaihoek = ((Boolean) h.get("checkExternalDraaihoek")).booleanValue();

		zetDraaihoekOptiesEnabled(kijkDraaihoekNa || checkExternalDraaihoek);

		if (h.containsKey("docentDraaihoekX"))
			docentDraaihoekX = ((Double) h.get("docentDraaihoekX")).doubleValue();
		if (h.containsKey("docentDraaihoekY"))
			docentDraaihoekY = ((Double) h.get("docentDraaihoekY")).doubleValue();

		int aanzicht = isAanzicht(docentDraaihoekX, docentDraaihoekY);
		if (aanzicht == TekenVeelvlakInteractiePanel.FRONTVIEW)
			voorkantRB.setSelected(true);
		else if (aanzicht == TekenVeelvlakInteractiePanel.BACKVIEW)
			achterkantRB.setSelected(true);
		else if (aanzicht == TekenVeelvlakInteractiePanel.TOPVIEW)
			bovenkantRB.setSelected(true);
		else if (aanzicht == TekenVeelvlakInteractiePanel.BOTTOMVIEW)
			onderkantRB.setSelected(true);
		else if (aanzicht == TekenVeelvlakInteractiePanel.LEFTVIEW)
			linkerkantRB.setSelected(true);
		else if (aanzicht == TekenVeelvlakInteractiePanel.RIGHTVIEW)
			rechterkantRB.setSelected(true);
		else if (aanzicht == -1)
		{
			if ((docentDraaihoekX < 1000) && (docentDraaihoekY < 1000))
				dezeDraaihoekRB.setSelected(true);
		}

		boolean kijkVlakkenNa = false;
		boolean checkExternalVlakken = false;

		if (h.containsKey("kijkVlakkenNa"))
			kijkVlakkenNa = ((Boolean) h.get("kijkVlakkenNa")).booleanValue();
		if (h.containsKey("checkExternalVlakken"))
			checkExternalVlakken = ((Boolean) h.get("checkExternalVlakken")).booleanValue();

		kijkVlakkenNaCB.setSelected(kijkVlakkenNa);
		checkExternalCB.setSelected(checkExternalVlakken || checkExternalDraaihoek);

		zetKijkNaTab();

		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		maxScoreVeld.setText("" + scoreMax);

		setBounds(getLocation().x, getLocation().y, tvipBreedte + editWidth, Math.max(tvipHoogte, editHeight));

		tvip.setEditState(h);
	}

	public void start()
	{
		// TODO Auto-generated method stub

	}

	public void stop()
	{
		// TODO Auto-generated method stub

	}

	public void wis()
	{
		// TODO Auto-generated method stub

	}

	public void zetBreedte(int b)
	{
		tvipBreedte = b;

		setBounds(getLocation().x, getLocation().y, tvipBreedte + editWidth, Math.max(tvipHoogte, editHeight));

		plaatsComponenten();
	}

	public void zetHoogte(int h)
	{
		tvipHoogte = h;

		setBounds(getLocation().x, getLocation().y, tvipBreedte + editWidth, Math.max(tvipHoogte, editHeight));

	}

	public void zetMode(int mode)
	{
		// TODO Auto-generated method stub

	}

	class TextFL2 implements FocusListener
	{
		JTextField inputTextField;

		public TextFL2(JTextField input)
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
			String oldText = "" + scoreMax;

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

			if (inputTextField == maxScoreVeld)
			{

				int mScore = (int) userInput;

				if ((mScore >= 0) && (mScore <= 1500))
				{
					scoreMax = mScore;
				}
				else
				{
					inputTextField.setText(oldText);
				}
			}
		} // focusLost
	}

	class TextAL2 implements ActionListener
	{
		JTextField inputTextField;

		public TextAL2(JTextField input)
		{
			inputTextField = input;
		}

		public void actionPerformed(ActionEvent e)
		{
			String text = inputTextField.getText();
			String oldText = "" + scoreMax;

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

			if (inputTextField == maxScoreVeld)
			{

				int mScore = (int) userInput;

				if ((mScore >= 0) && (mScore <= 1500))
				{
					scoreMax = mScore;
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

	class InputKL2 extends KeyAdapter
	{
		JTextField inputTextField;

		public InputKL2(JTextField input)
		{
			inputTextField = input;
		}

		public void keyReleased(KeyEvent e)
		{
			inputTextField.setForeground(Color.black);

			String txt = inputTextField.getText();

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

		public boolean isLegal(char c)
		{
			return Character.isDigit(c);
		}
	}
}