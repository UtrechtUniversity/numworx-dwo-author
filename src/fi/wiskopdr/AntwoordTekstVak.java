package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleElement;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.FeedbackTekstArea;
import fi.wiskopdr.tekstobjects.TekstArea;
import fi.wiskopdr.tekstobjects.TekstElement;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public class AntwoordTekstVak extends JLayeredPane implements InteractiePanel, ActionListener, MouseListener, FormuleVakHouder, CBookAware
{
	//static Image GOEDKRUL,FOUTKRUIS, HALFKRUL;

	private int mode;

	private boolean ingevuld;
	private boolean nagekeken;

	private boolean correct;
	private boolean fout;

	private int score;
	private int scoreMax = 10;

	static int GOED = 1;
	static int FOUT = 0;
	static int HALF = 2;
	static int GEEN = 3;
	
	private boolean changed = false;

	private JTextField antwoordTF;

	private String antwoordString;
	private String[] juisteAntwoorden;
	private Hashtable[] answerModels;
	private boolean hasFeedback;

	private int goedHalfFout;
	private int puntenFeedback;
	private String feedback;
	private JPanel feedbackPanel;
	private boolean gelijkwaardig;
	private TekstArea feedbackTekst;
	private FormuleButton feedbackButton;

	private boolean check;
	private boolean teltMee;

	private String[] randomVars;
	private Hashtable randomValues;

	private ImageComponent goedIC, foutIC, halfIC, huidigIC;

	private Font formuleVakFont = (!WiskOpdr.formTimes) || WiskOpdr.mac || WiskOpdr.zoefi ? WiskOpdr.formuleFont1Mac : WiskOpdr.formuleFont1; //new Font("TimesRoman",Font.PLAIN,16);

	private boolean formuleMode;
	private FormuleVak formuleVak;
	private int minBreedte;
	private int ashoogte;

	private boolean tabletAan;
	private boolean formuleToolBijFocus;

	private boolean logOption;
	private String logID;

	private boolean[][] logObjectives;

	private int errorCount;
	private int attemptsCount;
	private Vector attempts;
	
	private boolean editable = true;
	
	static boolean fontOvererving;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);	
	private JButton sendCommandButton;
	
	public static void zetFontOverervingForm(boolean b)
	{	fontOvererving = b;
	}

	/*private static String[] imageNames = 
	{	"goedkrul.gif",
		"goedkrulhalf.gif",
		"foutkruis.gif",
		"goedkrul_en.gif",
		
	};
	
	private static Hashtable images;*/

	/*public static void zetPlaatjes(Image gk, Image fk, Image hk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
		HALFKRUL = hk;
	}*/

	public AntwoordTekstVak()
	{
		setLayout(null);

		attempts = new Vector();

		antwoordTF = new JTextField();
		antwoordTF.setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));
		antwoordTF.setBounds(0, 0, 80, 21);
		antwoordTF.addActionListener(this);
		if ("GR".equals(WiskOpdr.deployVariant))
			antwoordTF.setBackground(new Color(230, 230, 230));
		//add(antwoordTF);

		formuleVak = new FormuleVak();
		formuleVak.setFont(formuleVakFont);
		formuleVak.setBorder(false);
		formuleVak.addActionListener(this);
		formuleVak.setLocation(4, 4);
		addMouseListener(this);
		//add(formuleVak);

		/*if(images==null)
		{	images = new Hashtable();
			WiskOpdr.loadImages(images,imageNames);
		}*/

		goedIC = new ImageComponent(WiskOpdr.GOEDKRUL);
		((ImageComponent) goedIC).zetKlein(true);
		goedIC.setLocation(antwoordTF.getWidth(), 0);
		goedIC.setVisible(false);
		setLayer((Component) goedIC, JLayeredPane.PALETTE_LAYER.intValue());
		add(goedIC, 0);

		halfIC = new ImageComponent(WiskOpdr.HALFKRUL);
		((ImageComponent) halfIC).zetKlein(true);
		halfIC.setLocation(antwoordTF.getWidth(), 0);
		halfIC.setVisible(false);
		setLayer((Component) halfIC, JLayeredPane.PALETTE_LAYER.intValue());
		add(halfIC);

		foutIC = new ImageComponent(WiskOpdr.FOUTKRUIS);
		((ImageComponent) foutIC).zetKlein(true);
		foutIC.setLocation(antwoordTF.getWidth(), 0);
		foutIC.setVisible(false);
		setLayer((Component) foutIC, JLayeredPane.PALETTE_LAYER.intValue());
		add(foutIC);

		feedbackTekst = new FeedbackTekstArea();
		feedbackTekst.setSize(200, 20);
		feedbackTekst.setBackground(new Color(255, 255, 200));
		feedbackTekst.setBorders(true);
		feedbackTekst.setCloseable(true);
		feedbackTekst.addActionListener(this);

		feedbackButton = new FormuleButton("?");
		feedbackButton.addActionListener(this);
		feedbackButton.setBackground(new Color(215, 215, 215));
		feedbackButton.setVisible(false);
		setLayer((Component) feedbackButton, JLayeredPane.PALETTE_LAYER.intValue());
		add(feedbackButton);

		feedbackPanel = new JPanel();
		feedbackPanel.setLayout(null);

		setSize(antwoordTF.getWidth() + 30, 20);

	}

	public void zetMinBreedte(int b)
	{
		minBreedte = b;
	}
	
	public void zetFormuleMode(boolean b)
	{
		formuleMode = b;
	}

	public void paintComponent(Graphics g)
	{
		if (formuleMode)
		{
			g.setColor(Color.white);
			if ("GR".equals(WiskOpdr.deployVariant))
			{
				for (int i = 0; i < 12; i++)
				{
					g.setColor(new Color(230 + i, 230 + i, 230 + i));
					g.fillRect(i * getWidth() / 12, 2, getWidth() / 12 + 1, getHeight() - 4);
				}
			}
			else
				g.fillRect(1, 2, getSize().width - 2, getSize().height - 4);
			g.setColor(Color.gray);
			if ("GR".equals(WiskOpdr.deployVariant))
				g.setColor(new Color(153, 153, 153));
			g.drawRect(1, 2, getSize().width - 2, getSize().height - 4);
		}
		else
			super.paintComponent(g);
	}

	/*public static Image getImage(String name)
	{	return(Image)images.get(name);
	}*/

	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		String antwoordString = "";
		int puntenGelijkwaardig = 10;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		int scoreMax = 10;
		boolean check = true;
		boolean teltMee = true;
		boolean formuleMode = false;
		boolean formuleToolBijFocus = false;
		boolean logOption = false;
		String logID = "";
		boolean boxMetRand = true;
		boolean[][] logObjectives = null;

		if (h.containsKey("antwoordString"))
			antwoordString = (String) h.get("antwoordString");
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		if (h.containsKey("answerModels"))
			answerModels = (Hashtable[]) h.get("answerModels");
		if (h.containsKey("hasFeedback"))
			hasFeedback = ((Boolean) h.get("hasFeedback")).booleanValue();
		if (h.containsKey("check"))
			check = ((Boolean) h.get("check")).booleanValue();
		if (h.containsKey("teltMee"))
			teltMee = ((Boolean) h.get("teltMee")).booleanValue();
		if (h.containsKey("formuleMode"))
			formuleMode = ((Boolean) h.get("formuleMode")).booleanValue();
		if (h.containsKey("formuleToolBijFocus"))
			formuleToolBijFocus = ((Boolean) h.get("formuleToolBijFocus")).booleanValue();
		if (h.containsKey("logOption"))
			logOption = ((Boolean) h.get("logOption")).booleanValue();
		if (h.containsKey("logID"))
			logID = (String) h.get("logID");
		if (h.containsKey("boxMetRand"))
			boxMetRand = ((Boolean) h.get("boxMetRand")).booleanValue();
		if (h.containsKey("logObjectives"))
			logObjectives = (boolean[][]) h.get("logObjectives");

		try
		{
			antwoordString = FormuleParser.randomizeTekstVakString(antwoordString, randomVars, randomValues);
		}
		catch (Exception e)
		{
		}
		if (!formuleMode)
			antwoordString = StringUtils.replaceStr(antwoordString, "@", "");
		if (!formuleMode)
			antwoordString = StringUtils.replaceStr(antwoordString, "$f", "");
		antwoordString = StringUtils.replaceStr(antwoordString, " ", "");

		this.antwoordString = antwoordString;
		this.scoreMax = scoreMax;
		this.answerModels = answerModels;
		this.hasFeedback = hasFeedback;
		this.check = check;
		this.teltMee = teltMee;
		this.randomVars = randomVars;
		this.randomValues = randomValues;
		this.formuleMode = formuleMode;
		this.formuleToolBijFocus = formuleToolBijFocus;
		this.logOption = logOption;
		this.logID = logID;
		this.logObjectives = logObjectives;

		if (formuleVak != null)
			formuleVak.zetStippels(!boxMetRand);
		if (!boxMetRand){
			antwoordTF.setBorder(BorderFactory.createEmptyBorder());
			antwoordTF.setOpaque(false);
		}

		if (formuleMode)
		{
			remove(antwoordTF);
			add(formuleVak);
		}
		else
		{
			remove(formuleVak);
			add(antwoordTF);
		}
		zetJuisteAntwoord(antwoordString);
		
		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			antwoordTF.setFont(geerftFont);
			if (!geerftFont.getName().equals("TimesRoman") && WiskOpdr.formTimes && !WiskOpdr.mac) {
				geerftFont = new Font("TimesRoman", geerftFont.getStyle(), geerftFont.getSize() * 6 / 5);
			}
			formuleVakFont = geerftFont;
			formuleVak.setFont(formuleVakFont);
		}

	}

	public void setAnswerModel(int nr)
	{
		Hashtable h = answerModels[nr];
		if (h == null)
			return;

		String antwoordString = "$f@";
		int puntenFeedback = 0;
		String feedback = "";
		int goedHalfFout = 0;

		if (h != null)
		{
			if (h.containsKey("antwoordString"))
				antwoordString = (String) h.get("antwoordString");
			if (h.containsKey("puntenFeedback"))
				puntenFeedback = ((Integer) h.get("puntenFeedback")).intValue();
			if (h.containsKey("feedback"))
				feedback = (String) h.get("feedback");
			if (h.containsKey("goedHalfFout"))
				goedHalfFout = ((Integer) h.get("goedHalfFout")).intValue();

		}
		try
		{
			antwoordString = FormuleParser.randomizeTekstVakString(antwoordString, randomVars, randomValues);
		}
		catch (Exception e)
		{
		}

		try
		{
			feedback = FormuleParser.randomizeTekstVakString(feedback, randomVars, randomValues);
		}
		catch (Exception e)
		{
			feedback = "$f???@";
		}

		if (!formuleMode)
			antwoordString = StringUtils.replaceStr(antwoordString, "@", "");
		if (!formuleMode)
			antwoordString = StringUtils.replaceStr(antwoordString, "$f", "");
		antwoordString = StringUtils.replaceStr(antwoordString, " ", "");

		this.goedHalfFout = goedHalfFout;
		this.puntenFeedback = puntenFeedback;
		this.antwoordString = antwoordString;
		this.feedback = feedback;

		zetJuisteAntwoord(antwoordString);

	}

	public void zetJuisteAntwoord(String s)
	{
		juisteAntwoorden = StringUtils.split(s, "::");
		//juisteAntwoorden = new String[antwoordStrings.length];
	}

	public void setState(Hashtable h)
	{
		boolean ingevuld = false;
		boolean nagekeken = false;
		String antwoord = "";
		Vector attempts = new Vector();
		int attemptsCount = 0;
		int errorCount = 0;
		boolean editable = true;

		if (h.containsKey("ingevuld"))
			ingevuld = ((Boolean) h.get("ingevuld")).booleanValue();
		if (h.containsKey("nagekeken"))
			nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
		if (h.containsKey("antwoord"))
			antwoord = (String) h.get("antwoord");
		if (h.containsKey("attempts"))
			attempts = OpdrNavStruct.toVector(h.get("attempts"));
		if (h.containsKey("attemptsCount"))
			attemptsCount = ((Number) h.get("attemptsCount")).intValue();
		if (h.containsKey("errorCount"))
			errorCount = ((Number) h.get("errorCount")).intValue();
		if(h.containsKey("editable")) 
			editable = ((Boolean)h.get("editable")).booleanValue();

		this.ingevuld = ingevuld;
		this.nagekeken = nagekeken;
		this.attempts = attempts;
		this.attemptsCount = attemptsCount;
		this.errorCount = errorCount;
		this.editable = editable;

		antwoordTF.setEditable(editable);
		formuleVak.setEditable(editable);
		if(editable)
		{	antwoordTF.setForeground(Color.black);
			formuleVak.setFGColor(Color.black);
		}
		else
		{	antwoordTF.setForeground(Color.gray);
			formuleVak.setFGColor(Color.gray);
		}
		
		if (formuleMode)
			formuleVak.vulVak(antwoord);
		else
			antwoordTF.setText(antwoord);

		if (ingevuld && (mode == 0 || nagekeken))
			kijkNa();
	}

	public void setEditState(Hashtable h)
	{
		boolean formuleMode = false;

		if (h.containsKey("formuleMode"))
			formuleMode = ((Boolean) h.get("formuleMode")).booleanValue();

		this.formuleMode = formuleMode;

		remove(antwoordTF);
		remove(formuleVak);
		if (formuleMode)
			add(formuleVak);
		else
			add(antwoordTF);
	}

	public Hashtable getState()
	{
		boolean ingevuld = false;
		boolean nagekeken = false;
		String antwoord = "";
		Vector attempts = new Vector();
		int attemptsCount = 0;
		int errorCount = 0;
		boolean editable = true;

		kijkNa(false);

		ingevuld = this.ingevuld;
		nagekeken = this.nagekeken;
		if (formuleMode)
			antwoord = formuleVak.toString();
		else
			antwoord = antwoordTF.getText();
		attempts = this.attempts;
		attemptsCount = this.attemptsCount;
		errorCount = this.errorCount;
		editable = this.editable;

		if (logOption)
		{
			Hashtable logMap = new Hashtable();

			String logString = "";
			if (formuleMode)
				logString = formuleVak.toString();
			else
				logString = antwoordTF.getText();

			logMap.put("logAnswer", logString);
			logMap.put("logScore", new Integer(score));
			logMap.put("logMaxScore", new Integer(scoreMax));
			logMap.put("logErrorCount", new Integer(errorCount));
			logMap.put("logAttemptsCount", new Integer(attemptsCount));
			logMap.put("logAttempts", attempts);

			WiskOpdr.setLog(logID, logMap);
		}

		Hashtable h = new Hashtable();
		h.put("ingevuld", new Boolean(ingevuld));
		h.put("nagekeken", new Boolean(nagekeken));
		h.put("antwoord", antwoord);
		h.put("attempts", attempts);
		h.put("attemptsCount", new Integer(attemptsCount));
		h.put("errorCount", new Integer(errorCount));
		h.put("editable", new Boolean(editable));

		return h;
	}

	public void setAttempt()
	{
		setAttempt(false);
	}

	public void setAttempt(boolean start)
	{
		String goedFout = "";
		if (huidigIC == goedIC && huidigIC.isVisible())
			goedFout = "goed";
		if (huidigIC == halfIC && huidigIC.isVisible())
			goedFout = "half";
		if (huidigIC == foutIC && huidigIC.isVisible())
			goedFout = "fout";

		String antwoord = "";
		if (formuleMode)
			antwoord = formuleVak.toString();
		else
			antwoord = antwoordTF.getText();
		if (antwoord.equals(""))
			return;

		if (formuleMode)
		{
			String attemptFormuleString = FormuleParser.schoon(FormuleParser.formuleString(antwoord));
			attemptFormuleString = StringUtils.replaceStr(attemptFormuleString, "(0-", "(-");
			antwoord = FormuleParser.pel(attemptFormuleString);
		}
		String fbTekst = "";
		if (feedbackTekst.isVisible() && feedbackTekst.getParent() != null)
			fbTekst = feedbackTekst.getText();

		String s = antwoord;
		s = s + "   ;   ";
		s = s + new Date().toString();
		s = s + "   ;   ";
		s = s + "Regelnummer = " + 0;
		s = s + "   ;   ";
		s = s + goedFout;
		s = s + "   ;   ";
		s = s + "score = " + score;
		s = s + "   ;   ";
		s = s + fbTekst;

		attempts.addElement(s);
		System.out.println(s);
	}

	public Hashtable getEditState()
	{
		return null;
	}

	public InteractieEditPanel getEditPanel()
	{
		return new AntwoordTekstVakEditPanel();
	}

	public void setBounds(int x, int y, int b, int h)
	{

		antwoordTF.setBounds(1, 2, b - 2, h - 3);
		feedbackButton.setBounds(antwoordTF.getWidth() - 15, getSize().height - 14, 14, 14);
		goedIC.setLocation(getWidth() - 18, 0);
		halfIC.setLocation(getWidth() - 18, 0);
		foutIC.setLocation(getWidth() - 18, 0);
		super.setBounds(x, y, b, h);
	}

	public void wis()
	{
		if (huidigIC != null)
		{
			huidigIC.setVisible(false);

		}

		correct = false;
		score = 0;
		nagekeken = false;
		ingevuld = false;

		attempts = new Vector();
	}

	public void zetMaat()
	{
		if (formuleMode)
			setSize(Math.max(minBreedte, formuleVak.getSize().width + 24), formuleVak.getSize().height + 8);
		else
			setSize(Math.max(minBreedte, antwoordTF.getSize().width + 2), antwoordTF.getSize().height + 4);
		formuleVak.setLocation(4, 4);
		antwoordTF.setLocation(1, 2);
		feedbackButton.setBounds(getSize().width - 15, getSize().height - 12, 15, 15);
		if (formuleMode)
			ashoogte = formuleVak.ashoogte + 4;
		else
			ashoogte = antwoordTF.getHeight() + 3;
		if (getParent() instanceof FormuleElement)
			((FormuleElement) getParent()).zetMaat();
		if (getParent() instanceof TekstElement)
			((TekstElement) getParent()).zetMaat();
	}

	public FormuleVak geefFormuleVak()
	{
		return formuleVak;
	}

	public void zetTabletAan(boolean b)
	{
		tabletAan = b;
	}

	public int geefAsHoogte()
	{
		if (formuleMode)
		{
			return formuleVak.ashoogte + (getFontMetrics(formuleVakFont)).getAscent() / 2 + 5;
		}
		return antwoordTF.getHeight() / 2 + (getFontMetrics(formuleVakFont)).getAscent() / 2;
	}

	public int getIpId()
	{
		return 0;
	}

	public String getIpExpString()
	{
		return null;
	}

	public int getScore()
	{
		if (!teltMee)
			return 0;
		if(mode==1)
			return Math.max(0, score-errorCount*2);
		return score;
	}

	public int[][] getScoreObjectives()
	{
		if (logObjectives == null)
			return null;
		int[][] scoreObjectives = new int[logObjectives.length][];
		for (int i = 0; i < logObjectives.length; i++)
			scoreObjectives[i] = new int[logObjectives[i].length];
		for (int i = 0; i < logObjectives.length; i++)
			for(int j = 0; j<logObjectives[i].length; j++)
			{	if(logObjectives[i][j] && mode==1)
					scoreObjectives[i][j] = Math.max(0, score - errorCount*2);
				else if(logObjectives[i][j]) 
					scoreObjectives[i][j] = score;
			}
		return scoreObjectives;
	}

	public int getScoreMax()
	{
		if (!teltMee)
			return 0;
		return scoreMax;
	}

	public boolean isCorrect()
	{
		if (!teltMee)
			return true;
		return correct;
	}

	public boolean isFout()
	{
		if (!teltMee)
			return false;
		return fout;
	}

	public void zetMode(int mode)
	{
		this.mode = mode;
	}

	public void zetNagekeken(boolean b)
	{
		if (ingevuld)
			nagekeken = b;
	}

	public void stop()
	{
		if (mode != 1)
			kijkNa();
		if (feedbackPanel.getParent() != null)
		{
			Container c = feedbackPanel.getParent();
			c.remove(feedbackPanel);
			c.repaint();
		}
	}

	public void start()
	{
	}

	public void destroy()
	{
	}

	public void opnieuw()
	{
		score = 0;
		correct = false;
	}

	private void zetGoedFout(int uitslag)
	{
		if (!check)
			return;
		if (huidigIC != null)
		{
			huidigIC.setVisible(false);

		}
		if (uitslag == GEEN)
			return;
		if (uitslag == GOED)
			huidigIC = goedIC;
		else if (uitslag == FOUT)
			huidigIC = foutIC;
		else if (uitslag == HALF)
			huidigIC = halfIC;
		huidigIC.setVisible(true);
	}

	public void kijkNa()
	{
		kijkNa(true);
		if (correct)
			cbookEventHandler.fire("action.correct");
    	if (fout)
    		cbookEventHandler.fire("action.false");
    	if (fout && errorCount > 1)
    		cbookEventHandler.fire("action.false_2");
	}

	public void kijkNa(boolean show)
	{
		checkAntwoord(show);

		if (formuleMode)
			ingevuld = !(formuleVak.toString() == null || formuleVak.toString().equals("$f@"));
		else
			ingevuld = !(antwoordTF.getText() == null || antwoordTF.getText().equals(""));

		correct = false;
		fout = true;
		score = 0;

		if (hasFeedback)
		{
			if (goedHalfFout == 0)
			{
				if (show)
					zetGoedFout(GOED);
				score = puntenFeedback;
				correct = true;
				fout = false;
			}
			else if (goedHalfFout == 1)
			{
				if (show)
					zetGoedFout(HALF);
				score = puntenFeedback;
				correct = false;
				fout = false;
			}
			else if (goedHalfFout == 2)
			{
				if (show)
					zetGoedFout(FOUT);
				score = puntenFeedback;
				correct = false;
				fout = true;
				verhoogErrorCount();
			}
		}
		else
		{
			if (gelijkwaardig)
			{
				if (show)
					zetGoedFout(GOED);
				correct = true;
				fout = false;
				score = scoreMax;
			}
			else
			{
				if (show)
					zetGoedFout(FOUT);
				correct = false;
				fout = true;
				score = 0;
				verhoogErrorCount();
			}
		}

		if (ingevuld && show && mode != -1)
			produceAction("changed");
	}

	public void kijkNa(int stapNr)
	{
		kijkNa();
	}

	public void verhoogErrorCount()
	{
		if(changed)
			errorCount++;
		changed = false;
	}
	
	public void setFeedback(String tekst, boolean closeable)
	{
		feedbackTekst.setText("");
		feedbackTekst.setCloseable(closeable);
		feedbackTekst.setSize(200, 20);
		feedbackTekst.setText(tekst);
		feedbackTekst.resize();
		//add(feedbackTekst);
		//produceAction("feedback");
	}

	public void activateFeedback(Component c)
	{
		Container parent = getParent();
		int x = parent.getLocation().x;
		int y = parent.getLocation().y;
		int h = parent.getSize().height;
		for (int i = 0; parent != null && i < 40; i++)
		{
			if (parent instanceof OpdrNavStruct)
			{
				int cx = Math.min(parent.getSize().width - c.getSize().width, x + 10);
				int cy = y + h + 10 > parent.getSize().height ? y - c.getSize().height - 10 : y + h + 10;
				c.setLocation(cx, cy);
				((OpdrNavStruct) parent).add(c, 0);
				parent.repaint();
				break;
			}
			else
			{
				parent = parent.getParent();
				x += parent.getLocation().x;
				y += parent.getLocation().y;
			}
		}
	}

	public void activateTablet()
	{
		Container parent = getParent();
		int x = parent.getLocation().x;
		int y = parent.getLocation().y;
		int h = parent.getSize().height;
		for (int i = 0; parent != null && i < 40; i++)
		{
			if (parent instanceof TabletOwner)
			{
				((TabletOwner) parent).addTablet(this, x + 20, y + h + 20);
				Tablet tablet = ((TabletOwner) parent).getTablet();
				int tx = Math.min(parent.getSize().width - tablet.getSize().width, x + 20);
				int ty = y + h + 20 + tablet.getSize().height > parent.getSize().height ? y - tablet.getSize().height - 10 : y + h + 20;
				tablet.setLocation(tx, ty);
				break;
			}
			else
			{
				parent = parent.getParent();
				if (parent == null)
					return;
				x += parent.getLocation().x;
				y += parent.getLocation().y;
			}
		}

		//if(getParent().getParent()instanceof TabletOwner)((TabletOwner)getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getLocation().y+getSize().height-120);
		//else if(getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getLocation().y+getSize().height+50);
		//else if(getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20,getLocation().y+getSize().height+70);
		//else if(getParent().getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getParent().getParent().getLocation().y+getSize().height+70);
		//else if(getParent().getParent().getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getParent().getParent().getLocation().y+getSize().height+70);
	}

	public void zetTabletUser()
	{
		Container parent = getParent();
		for (int i = 0; parent != null && i < 40; i++)
		{
			if (parent instanceof TabletOwner)
			{
				((TabletOwner) parent).zetTabletUser(this);
				break;
			}
			else
			{
				parent = parent.getParent();
			}
		}
	}

	public void checkAntwoord()
	{
		checkAntwoord(true);
	}

	public void checkAntwoord(boolean show)
	{
		if (hasFeedback)
		{
			int aantalAnswerModels = answerModels.length;
			for (int h = 0; h < aantalAnswerModels; h++)
			{
				setAnswerModel(h);
				gelijkwaardig = false;
				for (int i = 0; i < juisteAntwoorden.length; i++)
				{
					String antw = antwoordTF.getText();
					if (formuleMode)
					{
						antw = formuleVak.toString();
						//antw = antw.substring(2, antw.length()-1);
					}
					antw = StringUtils.replaceStr(antw, " ", "");
					gelijkwaardig = gelijkwaardig || antw.equals(juisteAntwoorden[i]);

				}

				if (gelijkwaardig || h == aantalAnswerModels - 1)
				{
					if (!feedback.trim().equals("") && show)
					{
						setFeedback(feedback, true);
						feedbackButton.setVisible(true);
					}
					else
					{
						feedbackButton.setVisible(false);
						if (feedbackPanel.getParent() != null)
						{
							Container c = feedbackPanel.getParent();
							c.remove(feedbackPanel);
							c.repaint();
						}

					}
					break;
				}

			}
		}
		else
		{
			gelijkwaardig = false;
			for (int i = 0; i < juisteAntwoorden.length; i++)
			{
				String antw = antwoordTF.getText();
				if (formuleMode)
				{
					antw = formuleVak.toString();
					//antw = antw.substring(2, antw.length()-1);
				}
				antw = StringUtils.replaceStr(antw, " ", "");
				gelijkwaardig = gelijkwaardig || antw.equals(juisteAntwoorden[i]);
			}
		}
		repaint();
	}

	public void mousePressed(MouseEvent e)
	{
		formuleVak.requestFocus();
		formuleVak.zetOpEind();
		if (formuleToolBijFocus && editable)
			activateTablet();
	}

	public void mouseClicked(MouseEvent e)
	{
		;
	}

	public void mouseReleased(MouseEvent e)
	{
		;
	}

	public void mouseEntered(MouseEvent e)
	{
		;
	}

	public void mouseExited(MouseEvent e)
	{
		;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == antwoordTF)
		{
			if (mode == 0 || mode == 1)
			{
				changed = true;
				kijkNa();
//				if (fout) // errorcount wordt gezet in kijkNa(); daar wil je cbookEventHandler.fire("action.false") e.d. obv errorcount afvuren
//					errorCount++;
				attemptsCount++;
				setAttempt();
				zetNagekeken(true);
				if (ingevuld)
					produceAction("checked");
			}

		}
		else if (e.getSource() == formuleVak && e.getActionCommand().equals("ingevuld"))
		{
			if (mode == 0 || mode == 1)
			{
				kijkNa();
				zetNagekeken(true);
				if (ingevuld)
					produceAction("checked");
			}
		}
		else if (e.getSource() == formuleVak && e.getActionCommand().equals("formChanged"))
		{
			if (feedbackTekst != null && getParent() == null && feedbackTekst.getParent() != null)
			{
				remove(feedbackTekst);
				produceAction("feedbackWeg");
			}
			zetGoedFout(GEEN);
			changed = true;
		}
		else if (e.getSource() == feedbackButton)
		{
			feedbackTekst.setLocation(0, 0);
			feedbackPanel.setSize(feedbackTekst.getSize().width, feedbackTekst.getSize().height);
			feedbackPanel.add(feedbackTekst);
			this.activateFeedback(feedbackPanel);
			feedbackButton.setVisible(false);
		}
		else if (e.getSource() == feedbackTekst)
		{
			if (feedbackPanel.getParent() != null)
			{
				Container c = feedbackPanel.getParent();
				c.remove(feedbackPanel);
				c.repaint();
			}
			feedbackButton.setVisible(true);
		}

	}

	//ActionProducer
	private ActionListener actionListener = null;

	public void addActionListener(ActionListener l)
	{
		actionListener = AWTEventMulticaster.add(actionListener, l);
	}

	public void removeActionListener(ActionListener l)
	{
		actionListener = AWTEventMulticaster.remove(actionListener, l);
	}

	public void produceAction(String command)
	{
		if (actionListener != null)
		{
			actionListener.actionPerformed(new ActionEvent(this, 0, command));
		}
	}
	//

	// CrossWidget Communivcation doet nog niets
	@Override
	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if(command.startsWith("text"))
		{
			Map map = (Map)event.getParameters();
			if(map!=null)
			{	
				
				
			}
		}
		else if(command.startsWith("action.setNotEditable"))
		{	editable = false;
			antwoordTF.setEditable(editable);
			formuleVak.setEditable(editable);
			if(editable)
			{	antwoordTF.setForeground(Color.black);
				formuleVak.setFGColor(Color.black);
			}
			else
			{	antwoordTF.setForeground(Color.gray);
				formuleVak.setFGColor(Color.gray);
			}
		}
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}

	@Override
	public String[] getSendCmds() {
		String[] commands = {"text",
				"action.correct",
				"action.false",
				"action.false_2"};
		return commands;
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] commands = {"text",
				"action.setNotEditable"};
		return commands;
	}
	
	@Override
	public String getLocalizedCmd(String cmd) {
		String localizedCmd = WiskOpdr.rb.getString(CBA_PREFIX + cmd);
		if(localizedCmd==null)
			return cmd;
		return localizedCmd;
	}
}
