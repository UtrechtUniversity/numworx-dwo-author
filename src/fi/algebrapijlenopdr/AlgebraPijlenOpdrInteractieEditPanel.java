package fi.algebrapijlenopdr;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;

import javax.swing.*;

import fi.algebrapijlenopdr.expressies_ap.Expressie;
import fi.algebrapijlenopdr.expressies_ap.FormuleParser_ap;
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class AlgebraPijlenOpdrInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AlgebraPijlenOpdrInteractiePanel apoip;
	int editWidth = 180;
	int editHeight = 500;
	int apoipBreedte = 500; // startbreedte apoip
	int apoipHoogte = 450; // starthoogte apoip

	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;

	int offset = 10;
	boolean componentsCreated = false;

	boolean noSetBounds = false;

	ButtonGroup kettingGroup;
	JRadioButton toolkitButton, invulButton, demoButton;

	JCheckBox brugklasBox, terugHeenBox, tabelBox, grafiekBox, scrollBox, zoomBox, beginExpBox;

	JButton beginExpButton;
	JCheckBox kijkNaBox;
	JCheckBox checkExternalBox;
	JButton toonDocExpButton;
	JLabel maxScoreLabel;
	JTextField maxScoreVeld;

	int scoreMax = 10;

	ExpressiePanel docentExpressiePanel;
	ExpressiePanel beginExpressiePanel;

	public AlgebraPijlenOpdrInteractieEditPanel()
	{
		setLayout(null);
		apoip = new AlgebraPijlenOpdrInteractiePanel();
		add(apoip);

		docentExpressiePanel = new ExpressiePanel(10, 10, 360, 275, 6);
		docentExpressiePanel.setVisible(false);
		add(docentExpressiePanel, 0);
		docentExpressiePanel.closeButton.addActionListener(new CloseDocExpAL());

		beginExpressiePanel = new ExpressiePanel(10, 100, 360, 69, 1);
		beginExpressiePanel.setVisible(false);
		add(beginExpressiePanel, 0);
		beginExpressiePanel.closeButton.addActionListener(new CloseBeginExpAL());
		beginExpressiePanel.apoiep = this;

		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);

		// viewerOptiesPanel

		int width = 0;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = apoip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;

		kettingGroup = new ButtonGroup();

		toolkitButton = new JRadioButton(AlgebraPijlenOpdr.rb.getString("toolkitTekst"), true);
		kettingGroup.add(toolkitButton);
		toolkitButton.setFont(theFont);
		toolkitButton.setBackground(Color.white);
		width = theFM.stringWidth(toolkitButton.getText()) + 40;
		toolkitButton.setBounds(currentX, currentY, width, height);
		add(toolkitButton);
		// viewerOptiesPanel.add(toolkitButton);
		toolkitButton.addActionListener(this);

		currentY += height; // + offset / 5;

		invulButton = new JRadioButton(AlgebraPijlenOpdr.rb.getString("invulTekst"));
		kettingGroup.add(invulButton);
		invulButton.setFont(theFont);
		invulButton.setBackground(Color.white);
		width = theFM.stringWidth(invulButton.getText()) + 35;
		invulButton.setBounds(currentX, currentY, width, height);
		add(invulButton);
		// viewerOptiesPanel.add(invulButton);
		invulButton.addActionListener(this);

		currentY += height; // + offset / 5;

		demoButton = new JRadioButton(AlgebraPijlenOpdr.rb.getString("demoTekst"));
		kettingGroup.add(demoButton);
		demoButton.setFont(theFont);
		demoButton.setBackground(Color.white);
		width = theFM.stringWidth(demoButton.getText()) + 40;
		demoButton.setBounds(currentX, currentY, width, height);
		add(demoButton);
		// viewerOptiesPanel.add(demoButton);
		demoButton.addActionListener(this);

		currentY += height + offset;

		brugklasBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("brugklasTekst"));
		brugklasBox.setFont(theFont);
		brugklasBox.setBackground(Color.white);
		width = theFM.stringWidth(brugklasBox.getText()) + 40;
		brugklasBox.setBounds(currentX, currentY, width, height);
		add(brugklasBox);
		// viewerOptiesPanel.add(brugklasBox);
		brugklasBox.addActionListener(this);

		currentY += height + offset / 2;

		terugHeenBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("terugHeenTekst"));
		terugHeenBox.setFont(theFont);
		terugHeenBox.setBackground(Color.white);
		width = theFM.stringWidth(terugHeenBox.getText()) + 35;
		terugHeenBox.setBounds(currentX, currentY, width, height);
		terugHeenBox.setSelected(true);
		add(terugHeenBox);
		// viewerOptiesPanel.add(terugHeenBox);
		terugHeenBox.addActionListener(this);

		currentY += height + offset / 2;

		tabelBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("tabelTekst"));
		tabelBox.setFont(theFont);
		tabelBox.setBackground(Color.white);
		width = theFM.stringWidth(tabelBox.getText()) + 40;
		tabelBox.setBounds(currentX, currentY, width, height);
		tabelBox.setSelected(true);
		add(tabelBox);
		// viewerOptiesPanel.add(tabelBox);
		tabelBox.addActionListener(this);

		currentY += height + offset / 2;

		grafiekBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("grafiekTekst"));
		grafiekBox.setFont(theFont);
		grafiekBox.setBackground(Color.white);
		width = theFM.stringWidth(grafiekBox.getText()) + 40;
		grafiekBox.setBounds(currentX, currentY, width, height);
		grafiekBox.setSelected(true);
		add(grafiekBox);
		// viewerOptiesPanel.add(grafiekBox);
		grafiekBox.addActionListener(this);

		currentY += height + offset / 2;

		scrollBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("scrollTekst"));
		scrollBox.setFont(theFont);
		scrollBox.setBackground(Color.white);
		width = theFM.stringWidth(scrollBox.getText()) + 35;
		scrollBox.setBounds(currentX, currentY, width, height);
		scrollBox.setSelected(true);
		add(scrollBox);
		// viewerOptiesPanel.add(scrollBox);
		scrollBox.addActionListener(this);

		currentY += height + offset / 2;

		zoomBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("zoomTekst"));
		zoomBox.setFont(theFont);
		zoomBox.setBackground(Color.white);
		width = theFM.stringWidth(zoomBox.getText()) + 35;
		zoomBox.setBounds(currentX, currentY, width, height);
		zoomBox.setSelected(true);
		add(zoomBox);
		// viewerOptiesPanel.add(zoomBox);
		zoomBox.addActionListener(this);

		currentY += height + offset / 2;

		beginExpBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("beginExpTekst"));
		beginExpBox.setFont(theFont);
		beginExpBox.setBackground(Color.white);
		width = theFM.stringWidth(beginExpBox.getText()) + 35;
		beginExpBox.setBounds(currentX, currentY, width, height);
		beginExpBox.setSelected(false);
		// add(beginExpBox);
		beginExpBox.addActionListener(this);

		beginExpButton = new JButton(AlgebraPijlenOpdr.rb.getString("beginExpTekst"));
		beginExpButton.setFont(theFont);
		// beginExpButton.setBackground(Color.white);
		width = theFM.stringWidth(beginExpButton.getText()) + 40;
		beginExpButton.setBounds(currentX, currentY, width, height);
		// beginExpBox.setSelected(false);
		add(beginExpButton);
		beginExpButton.addActionListener(this);

		currentY += height + 4 * offset;

		kijkNaBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("kijkNaActief"));
		kijkNaBox.setFont(theFont);
		kijkNaBox.setBackground(Color.white);
		width = theFM.stringWidth(kijkNaBox.getText()) + 40;
		kijkNaBox.setBounds(currentX, currentY, width, 3 * theFM.getHeight() / 2);
		add(kijkNaBox);
		kijkNaBox.addActionListener(this);

		currentY += height + offset;

		checkExternalBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("checkExternal"));
		checkExternalBox.setFont(theFont);
		checkExternalBox.setBackground(Color.white);
		width = theFM.stringWidth(checkExternalBox.getText()) + 40;
		checkExternalBox.setBounds(currentX, currentY, width, 3 * theFM.getHeight() / 2);
		add(checkExternalBox);
		checkExternalBox.addActionListener(this);

		currentY += height + offset;

		toonDocExpButton = new JButton(AlgebraPijlenOpdr.rb.getString("toonDocExpTekst"));
		toonDocExpButton.setFont(theFont);
		int w = theFM.stringWidth(toonDocExpButton.getText()) + 40;
		currentX = (editWidth - w) / 2;
		toonDocExpButton.setBounds(currentX, currentY, w, 3 * theFM.getHeight() / 2);
		toonDocExpButton.setVisible(false);
		add(toonDocExpButton);
		toonDocExpButton.addActionListener(this);

		currentX = apoip.getSize().width + offset;
		currentY += height + offset;

		maxScoreLabel = new JLabel(AlgebraPijlenOpdr.rb.getString("maxScoreTekst"));
		maxScoreLabel.setFont(theFont);
		maxScoreLabel.setBackground(Color.white);
		width = theFM.stringWidth(maxScoreLabel.getText());
		maxScoreLabel.setBounds(currentX + offset, currentY + 3, width, theFM.getHeight());
		// maxScoreLabel.setEnabled(false);
		maxScoreLabel.setVisible(false);
		add(maxScoreLabel);

		currentY += height; // + offset;

		maxScoreVeld = new JTextField("" + scoreMax);
		maxScoreVeld.setFont(theFont);
		maxScoreVeld.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXX");
		maxScoreVeld.setBounds(currentX + 2 * offset, currentY, width, height);
		// maxScoreLabel.getLocation().x + maxScoreLabel.getSize().width +
		// offset,
		// currentY, width, height);
		add(maxScoreVeld);
		// maxScoreVeld.setEditable(false);
		maxScoreVeld.setVisible(false);

		maxScoreVeld.addKeyListener(new InputKL2(maxScoreVeld));
		maxScoreVeld.addActionListener(new TextAL2(maxScoreVeld));
		maxScoreVeld.addFocusListener(new TextFL2(maxScoreVeld));

		componentsCreated = true;
	}

	public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			toolkitButton.setLocation(apoip.getSize().width + offset, toolkitButton.getLocation().y);
			invulButton.setLocation(apoip.getSize().width + offset, invulButton.getLocation().y);
			demoButton.setLocation(apoip.getSize().width + offset, demoButton.getLocation().y);

			brugklasBox.setLocation(apoip.getSize().width + offset, brugklasBox.getLocation().y);
			terugHeenBox.setLocation(apoip.getSize().width + offset, terugHeenBox.getLocation().y);
			tabelBox.setLocation(apoip.getSize().width + offset, tabelBox.getLocation().y);
			grafiekBox.setLocation(apoip.getSize().width + offset, grafiekBox.getLocation().y);
			scrollBox.setLocation(apoip.getSize().width + offset, scrollBox.getLocation().y);
			zoomBox.setLocation(apoip.getSize().width + offset, zoomBox.getLocation().y);
			beginExpButton.setLocation(apoip.getSize().width + offset, beginExpButton.getLocation().y);

			kijkNaBox.setLocation(apoip.getSize().width + offset, kijkNaBox.getLocation().y);
			checkExternalBox.setLocation(apoip.getSize().width + offset, checkExternalBox.getLocation().y);
			toonDocExpButton.setLocation(apoip.getSize().width + offset, toonDocExpButton.getLocation().y);

			maxScoreLabel.setLocation(apoip.getSize().width + 2 * offset, maxScoreLabel.getLocation().y);
			maxScoreVeld.setLocation(apoip.getSize().width + 3 * offset, maxScoreVeld.getLocation().y);

			repaint();
		}
	}

	public void setEditState(Hashtable b)
	{
		boolean toolkit = true;
		if (b.containsKey("toolkit"))
			toolkit = ((Boolean) b.get("toolkit")).booleanValue();
		toolkitButton.setSelected(toolkit);

		boolean alleenInvullen = false;
		if (b.containsKey("alleenInvullen"))
			alleenInvullen = ((Boolean) b.get("alleenInvullen")).booleanValue();
		invulButton.setSelected(alleenInvullen);

		if (invulButton.isSelected())
		{
			brugklasBox.setEnabled(false);
			terugHeenBox.setEnabled(false);
			tabelBox.setEnabled(false);
			grafiekBox.setEnabled(false);
			// scrollBox.setEnabled(true);
			// zoomBox.setEnabled(true);

		}

		boolean isDemo = false;
		if (b.containsKey("isDemo"))
			isDemo = ((Boolean) b.get("isDemo")).booleanValue();
		demoButton.setSelected(isDemo);

		if (demoButton.isSelected())
		{
			brugklasBox.setEnabled(false);
			terugHeenBox.setEnabled(false);
			tabelBox.setEnabled(false);
			grafiekBox.setEnabled(false);
			scrollBox.setEnabled(false);
			zoomBox.setEnabled(false);
		}

		boolean brugklas = false;
		if (b.containsKey("brugklas"))
			brugklas = ((Boolean) b.get("brugklas")).booleanValue();
		brugklasBox.setSelected(brugklas);

		boolean terugHeen = true;
		if (b.containsKey("terugHeen"))
			terugHeen = ((Boolean) b.get("terugHeen")).booleanValue();
		terugHeenBox.setSelected(terugHeen);

		boolean tabelOptie = true;
		if (b.containsKey("tabelOptie"))
			tabelOptie = ((Boolean) b.get("tabelOptie")).booleanValue();
		tabelBox.setSelected(tabelOptie);

		boolean grafiekOptie = true;
		if (b.containsKey("grafiekOptie"))
			grafiekOptie = ((Boolean) b.get("grafiekOptie")).booleanValue();
		grafiekBox.setSelected(grafiekOptie);

		boolean scrollOptie = true;
		if (b.containsKey("scrollOptie"))
			scrollOptie = ((Boolean) b.get("scrollOptie")).booleanValue();
		scrollBox.setSelected(scrollOptie);

		boolean zoomOptie = true;
		if (b.containsKey("zoomOptie"))
			zoomOptie = ((Boolean) b.get("zoomOptie")).booleanValue();
		zoomBox.setSelected(zoomOptie);

		// beginExpressie ophalen
		String[] expStrings = new String[beginExpressiePanel.numInputs];
		for (int i = 0; i < expStrings.length; i++)
			expStrings[i] = "";
		if (b.containsKey("expStrings"))
			expStrings = (String[]) b.get("expStrings");
		beginExpressiePanel.zetExpressieStrings(expStrings);

		String beginExpressie = "";
		if (b.contains("beginexpressie"))
			beginExpressie = (String) b.get("beginexpressie");
		// wat hier?

		boolean toonBeginExpressie = false;
		if (b.contains("toonBeginExpressie"))
			toonBeginExpressie = ((Boolean) b.get("toonBeginExpressie")).booleanValue();
		beginExpBox.setSelected(toonBeginExpressie);

		boolean kijkNaActief = false;
		if (b.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) b.get("kijkNaActief")).booleanValue();
		kijkNaBox.setSelected(kijkNaActief);

		boolean checkExternal = false;
		if (b.containsKey("checkExternal"))
			checkExternal = ((Boolean) b.get("checkExternal")).booleanValue();
		checkExternalBox.setSelected(checkExternal);

		// altijd expressieStrings ophalen
		String[] expressieStrings = new String[docentExpressiePanel.numInputs];
		for (int i = 0; i < expressieStrings.length; i++)
			expressieStrings[i] = "";
		if (b.containsKey("expressieStrings"))
			expressieStrings = (String[]) b.get("expressieStrings");
		docentExpressiePanel.zetExpressieStrings(expressieStrings);

		boolean toonDocExpressies = false;
		if (b.containsKey("toonDocExpressies"))
			toonDocExpressies = ((Boolean) b.get("toonDocExpressies")).booleanValue();
		// toonDocExpBox.setSelected(toonDocExpressies);

		if (b.containsKey("scoreMax"))
			scoreMax = ((Integer) b.get("scoreMax")).intValue();
		maxScoreVeld.setText("" + scoreMax);
		// maxScoreVeld.setEditable(kijkNaActief);

		// toonDocExpBox.setEnabled(kijkNaBox.isSelected());
		// maxScoreLabel.setEnabled(kijkNaBox.isSelected());
		// maxScoreVeld.setEditable(kijkNaBox.isSelected());

		// toonDocExpBox.setVisible(kijkNaBox.isSelected());
		toonDocExpButton.setVisible(kijkNaBox.isSelected());
		maxScoreLabel.setVisible(kijkNaBox.isSelected());
		maxScoreVeld.setVisible(kijkNaBox.isSelected());

		if (b.containsKey("apoipBreedte"))
			apoipBreedte = ((Integer) b.get("apoipBreedte")).intValue();
		if (b.containsKey("apoipHoogte"))
			apoipHoogte = ((Integer) b.get("apoipHoogte")).intValue();

		setBounds(getLocation().x, getLocation().y, apoipBreedte + editWidth, Math.max(apoipHoogte, editHeight));

		// HIER !!
		if (kijkNaBox.isSelected() && toonDocExpressies)
		{
			docentExpressiePanel.setVisible(true);
			apoip.disableElements(true);
		}
		else if (!kijkNaBox.isSelected())// && toonDocExpBox.isSelected())
		{
			docentExpressiePanel.setVisible(false);
			apoip.disableElements(false);
		}

		// if (beginExpBox.isSelected())
		if (toonBeginExpressie)
		{
			beginExpressiePanel.setVisible(true);
			apoip.disableElements(true);

		}

		apoip.setEditState(b);

	}

	public Hashtable getEditState()
	{
		Hashtable h = apoip.getEditState();

		h.put("toonDocExpressies", new Boolean(docentExpressiePanel.isVisible()));
		h.put("toonBeginExpressie", new Boolean(beginExpressiePanel.isVisible()));

		// docentExpressies ophalen
		// alle
		h.put("expressieStrings", docentExpressiePanel.getExpressieStrings());
		// correcte
		h.put("docentExpressieStrings", docentExpressiePanel.getCorrectExpressieStrings());

		h.put("expStrings", beginExpressiePanel.getExpressieStrings());
		String beginExpressieString = "";
		if (beginExpressiePanel.getCorrectExpressieStrings().size() > 0)
			beginExpressieString = (String) beginExpressiePanel.getCorrectExpressieStrings().elementAt(0);
		h.put("beginExpressieString", beginExpressieString);

		boolean kijkNaActief = false;
		if (h.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();

		boolean checkExternal = false;
		if (h.containsKey("checkExternal"))
			checkExternal = ((Boolean) h.get("checkExternal")).booleanValue();

		if (kijkNaActief || checkExternal)
			h.put("scoreMax", new Integer(scoreMax));
		else
			h.put("scoreMax", new Integer(0));

		h.put("apoipBreedte", new Integer(apoipBreedte));
		h.put("apoipHoogte", new Integer(apoipHoogte));

		return h;
	}

	public void setBounds(int x, int y, int b, int h)
	{

		if (noSetBounds)
		{
			noSetBounds = false;
			return;
		}

		// System.out.println("apoiep setBounds raw " + x + " " + y + " " + b +
		// " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;

		super.setBounds(x, y, apoipBreedte + editWidth, Math.max(apoipHoogte, editHeight));

		// System.out.println("apoiep setBounds " + x + " " + y + " " +
		// (apoipBreedte + editWidth) + " " +
		// Math.max(apoipHoogte, editHeight));

		if (apoip != null)
			apoip.setBounds(0, 0, apoipBreedte, apoipHoogte);

		plaatsComponenten();

		// System.out.println("setBounds " + x + " " + y + " " + b + " " + h);

	}

	public void zetBreedte(int b)
	{
		apoipBreedte = b;

		setBounds(getLocation().x, getLocation().y, apoipBreedte + editWidth, Math.max(apoipHoogte, editHeight));
		// apoip.setBounds(apoip.getLocation().x, apoip.getLocation().y,
		// Math.max(0, b), apoip.getSize().height);
		plaatsComponenten();
	}

	public void zetHoogte(int h)
	{
		apoipHoogte = h;

		setBounds(getLocation().x, getLocation().y, apoipBreedte + editWidth, Math.max(apoipHoogte, editHeight));
		// apoip.setBounds(apoip.getLocation().x, apoip.getLocation().y,
		// apoip.getSize().width, h);
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

	public void maakBeginExpressie()
	{
		String beginExpString = "";
		if (beginExpressiePanel.getCorrectExpressieStrings().size() > 0)
		{
			beginExpString = (String) beginExpressiePanel.getCorrectExpressieStrings().elementAt(0);

			System.out.println(beginExpString);

			beginExpString = "$f" + beginExpString + "@";
			Expressie beginExp = FormuleParser_ap.geefExpressie(beginExpString);

			System.out.println(beginExp.toString());

			apoip.zetBeginExpressie(beginExp);

		}

	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == toolkitButton)
		{
			if (toolkitButton.isSelected())
			{
				apoip.zetToolkit(true);

				brugklasBox.setEnabled(true);
				terugHeenBox.setEnabled(true);
				tabelBox.setEnabled(true);
				grafiekBox.setEnabled(true);
				scrollBox.setEnabled(true);
				zoomBox.setEnabled(true);

			}
		}
		else if (e.getSource() == invulButton)
		{
			if (invulButton.isSelected())
			{
				apoip.zetAlleenInvullen(true);

				brugklasBox.setEnabled(false);
				terugHeenBox.setEnabled(false);
				tabelBox.setEnabled(false);
				grafiekBox.setEnabled(false);
				scrollBox.setEnabled(true);
				zoomBox.setEnabled(true);

			}
		}
		else if (e.getSource() == demoButton)
		{
			if (demoButton.isSelected())
			{
				apoip.zetIsDemo(true);

				brugklasBox.setEnabled(false);
				terugHeenBox.setEnabled(false);
				tabelBox.setEnabled(false);
				grafiekBox.setEnabled(false);
				scrollBox.setEnabled(false);
				zoomBox.setEnabled(false);
			}
		}

		else if (e.getSource() == brugklasBox)
		{
			apoip.zetBrugklas(brugklasBox.isSelected());
		}
		else if (e.getSource() == terugHeenBox)
		{
			apoip.zetTerugHeen(terugHeenBox.isSelected());
		}
		else if (e.getSource() == tabelBox)
		{
			apoip.zetTabelOptie(tabelBox.isSelected());
		}
		else if (e.getSource() == grafiekBox)
		{
			apoip.zetGrafiekOptie(grafiekBox.isSelected());
		}
		else if (e.getSource() == scrollBox)
		{
			apoip.zetScrollOptie(scrollBox.isSelected());
		}
		else if (e.getSource() == zoomBox)
		{
			apoip.zetZoomOptie(zoomBox.isSelected());
		}

		else if (e.getSource() == beginExpButton)
		{
			beginExpressiePanel.setVisible(true);
			apoip.disableElements(true);

			docentExpressiePanel.setVisible(false);

		}
		else if (e.getSource() == beginExpBox)
		{
			beginExpressiePanel.setVisible(beginExpBox.isSelected());
			apoip.disableElements(beginExpBox.isSelected());

			// beginExpressiePanel zichtbaar gemaakt
			if (beginExpBox.isSelected())
			{
				docentExpressiePanel.setVisible(false);
				// toonDocExpBox.setSelected(false);
			}
			else // beginExpressiePanel "gesloten"
			{
				String beginExpString = "";
				if (beginExpressiePanel.getCorrectExpressieStrings().size() > 0)
				{
					beginExpString = (String) beginExpressiePanel.getCorrectExpressieStrings().elementAt(0);

					// System.out.println(beginExpString);

					beginExpString = "$f" + beginExpString + "@";
					Expressie beginExp = FormuleParser_ap.geefExpressie(beginExpString);

					// System.out.println(beginExp.toString());
					apoip.zetBeginExpressie(beginExp);
				}
			}

		}

		else if (e.getSource() == kijkNaBox)
		{
			if (checkExternalBox.isSelected() || kijkNaBox.isSelected())
			{
				setVisibleNakijkModusFields(true);
			}
			else
			{
				setVisibleNakijkModusFields(false);
			}

			apoip.zetKijkNaActief(kijkNaBox.isSelected());
		}
		else if (e.getSource() == checkExternalBox)
		{
			if (checkExternalBox.isSelected() || kijkNaBox.isSelected())
			{
				setVisibleNakijkModusFields(true);
			}
			else
			{
				setVisibleNakijkModusFields(false);
			}

			apoip.zetCheckExternal(checkExternalBox.isSelected());
		}
		else if (e.getSource() == toonDocExpButton)
		{
			docentExpressiePanel.setVisible(true);
			apoip.disableElements(true);

			beginExpressiePanel.setVisible(false);
			maakBeginExpressie();
		}

	}

	/**
	 * Set visible van de componenten die horen bij de nakijkmodus.
	 */
	void setVisibleNakijkModusFields(boolean b)
	{
		toonDocExpButton.setVisible(b);
		maxScoreLabel.setVisible(b);
		maxScoreVeld.setVisible(b);
	}

	class CloseDocExpAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			docentExpressiePanel.setVisible(false);
			apoip.disableElements(false);
		}

	}

	class CloseBeginExpAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			beginExpressiePanel.setVisible(false);
			maakBeginExpressie();
			apoip.disableElements(false);
		}

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
		{ // invoer user
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
				// System.out.println("nfe");
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

			// System.out.println(txt);

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
					// System.out.println("illegal " + index);
				}
			}
			// verwijder illegaal karakter
			if (index >= 0)
			{
				txt = removeCharAt(txt, index);
				corrected = true;
				// System.out.println("corr " + txt);
			}

			// System.out.println(txt);

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
				// System.out.println("corr " + txt);
				inputTextField.setText(txt);

			}

		}

		public boolean isLegal(char c)
		{
			return Character.isDigit(c);
		}
	}

}
