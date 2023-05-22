package fi.wiskopdr.stelselsvergelijkingen;

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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.ImageComponent;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.Expressie;
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

public class StelselOplossingenVak extends JLayeredPane implements ActionListener, MouseListener, FormuleVakHouder
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
	
	//private String variabelenString;
	private String[] varNamen;
	private Expressie[][] juisteOplossingen;
	//private String[] juisteAntwoorden;
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
	
	static boolean fontOvererving;
	private StelselAntwoordVak parent;
	
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

	public StelselOplossingenVak(StelselAntwoordVak parent)
	{
		setLayout(null);
		this.parent = parent;

		attempts = new Vector();

		formuleVak = new FormuleVak();
		formuleVak.setFont(formuleVakFont);
		formuleVak.setBorder(false);
		formuleVak.addActionListener(this);
		formuleVak.setLocation(4, 4);
		addMouseListener(this);
		
		goedIC = new ImageComponent(WiskOpdr.GOEDKRUL);
		((ImageComponent) goedIC).zetKlein(true);
		goedIC.setLocation(formuleVak.getWidth(), 0);
		goedIC.setVisible(false);
		setLayer((Component) goedIC, JLayeredPane.PALETTE_LAYER.intValue());
		add(goedIC, 0);

		halfIC = new ImageComponent(WiskOpdr.HALFKRUL);
		((ImageComponent) halfIC).zetKlein(true);
		halfIC.setLocation(formuleVak.getWidth(), 0);
		halfIC.setVisible(false);
		setLayer((Component) halfIC, JLayeredPane.PALETTE_LAYER.intValue());
		add(halfIC);

		foutIC = new ImageComponent(WiskOpdr.FOUTKRUIS);
		((ImageComponent) foutIC).zetKlein(true);
		foutIC.setLocation(formuleVak.getWidth(), 0);
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

		setSize(formuleVak.getWidth() + 30, 20);

	}

	public void zetMinBreedte(int b)
	{
		minBreedte = b;
	}
	
	public void paintComponent(Graphics g)
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
		g.setColor(WiskOpdr.colorBlue4);
		if ("GR".equals(WiskOpdr.deployVariant))
			g.setColor(new Color(153, 153, 153));
		g.drawRect(1, 2, getSize().width - 2, getSize().height - 4);
		
	}

	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		int puntenGelijkwaardig = 10;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		int scoreMax = 10;
		boolean check = true;
		boolean teltMee = true;
		boolean formuleToolBijFocus = false;
		boolean logOption = false;
		String logID = "";
		boolean boxMetRand = true;
		boolean[][] logObjectives = null;

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

		this.scoreMax = scoreMax;
		this.answerModels = answerModels;
		this.hasFeedback = hasFeedback;
		this.check = check;
		this.teltMee = teltMee;
		this.randomVars = randomVars;
		this.randomValues = randomValues;
		this.formuleToolBijFocus = formuleToolBijFocus;
		this.logOption = logOption;
		this.logID = logID;
		this.logObjectives = logObjectives;

		if (formuleVak != null)
			formuleVak.zetStippels(!boxMetRand);
		
		add(formuleVak);
		
		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
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

		this.goedHalfFout = goedHalfFout;
		this.puntenFeedback = puntenFeedback;
		//this.antwoordString = antwoordString;
		this.feedback = feedback;

	}
	
	public void zetVarNamen(String[] namen)
	{
		varNamen = namen;
	}

	public void zetJuisteOplossingen(Expressie[][] oplossingen)
	{
		juisteOplossingen = oplossingen;
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

		this.ingevuld = ingevuld;
		this.nagekeken = nagekeken;
		this.attempts = attempts;
		this.attemptsCount = attemptsCount;
		this.errorCount = errorCount;
		formuleVak.vulVak(antwoord);
		
		if (ingevuld && (mode == 0 || nagekeken))
			kijkNa();
	}

	public void setEditState(Hashtable h)
	{
		
	}

	public Hashtable getState()
	{
		boolean ingevuld = false;
		boolean nagekeken = false;
		String antwoord = "";
		Vector attempts = new Vector();
		int attemptsCount = 0;
		int errorCount = 0;

		kijkNa(false);

		ingevuld = this.ingevuld;
		nagekeken = this.nagekeken;
		antwoord = formuleVak.toString();
		attempts = this.attempts;
		attemptsCount = this.attemptsCount;
		errorCount = this.errorCount;

		if (logOption)
		{
			Hashtable logMap = new Hashtable();

			String logString = "";
			logString = formuleVak.toString();
			
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
		antwoord = formuleVak.toString();
		if (antwoord.equals(""))
			return;

		String attemptFormuleString = FormuleParser.schoon(FormuleParser.formuleString(antwoord));
		attemptFormuleString = StringUtils.replaceStr(attemptFormuleString, "(0-", "(-");
		antwoord = FormuleParser.pel(attemptFormuleString);
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

	public void setBounds(int x, int y, int b, int h)
	{
		feedbackButton.setBounds(formuleVak.getWidth() - 15, getSize().height - 14, 14, 14);
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
		setSize(Math.max(minBreedte, formuleVak.getSize().width + 24), formuleVak.getSize().height + 8);
		formuleVak.setLocation(4, 4);
		feedbackButton.setBounds(getSize().width - 15, getSize().height - 12, 15, 15);
		ashoogte = formuleVak.ashoogte + 4;
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
		return formuleVak.ashoogte + (getFontMetrics(formuleVakFont)).getAscent() / 2 + 5;
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
			for (int j = 0; j < logObjectives[i].length; j++)
			{
				if (logObjectives[i][j])
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
	}

	public void kijkNa(boolean show)
	{
		checkAntwoord(show);

		ingevuld = !(formuleVak.toString() == null || formuleVak.toString().equals("$f@"));
		
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
			}
		}

		if (ingevuld && show && mode != -1)
			parent.produceAction("changed");
	}

	public void kijkNa(int stapNr)
	{
		kijkNa();
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
				gelijkwaardig = bepaalGelijkwaardig();
				
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
			gelijkwaardig = bepaalGelijkwaardig();
			
			
//			for (int i = 0; i < juisteAntwoorden.length; i++)
//			{	String antw = formuleVak.toString();
//				antw = StringUtils.replaceStr(antw, " ", "");
//				gelijkwaardig = gelijkwaardig || antw.equals(juisteAntwoorden[i]);
//			}
		}
		repaint();
	}
	
	public Expressie[][] bepaalOplossingen(String antwoordString)
	{
		antwoordString = StringUtils.replaceStr(antwoordString, " ", "");
		Expressie[][] oplossingen;
		try{
			//splitsen in verschillende oplossingen. Eerst $f en @ weghalen.
			antwoordString = antwoordString.substring(2, antwoordString.length() - 1);
			antwoordString = StringUtils.replaceStr(antwoordString, "),(", "):(");
			String[] oplossingenStrings = StringUtils.split(antwoordString, ":");
			oplossingen = new Expressie[oplossingenStrings.length][varNamen.length];
			for(int i = 0; i < oplossingenStrings.length; i++)
			{
				//haakjes verwijderen:
				String opl = oplossingenStrings[i].substring(1, oplossingenStrings[i].length() - 1);
				String[] varWaardes;
				if(opl.contains(";"))
					varWaardes = StringUtils.split(opl, ";");
				else
					varWaardes = StringUtils.split(opl, ",");
				for(int j = 0; j < varNamen.length; j++)
				{	oplossingen[i][j] = FormuleParser.geefExpressie("$f" + varWaardes[j] + "@");
				}
			}
			return oplossingen;
		}
		catch(Exception e)
		{return null;}
	}
	
	public boolean bepaalGelijkwaardig()
	{
		boolean gelijkwaardig = true;
		Expressie[][] oplossingen = bepaalOplossingen(formuleVak.toString());
		if(oplossingen == null)
			return false;
		boolean[] oplossingenCorrect = new boolean[juisteOplossingen.length];
		for(int i = 0; i < oplossingenCorrect.length; i++)
			oplossingenCorrect[i] = false;
		for(int i = 0; i < oplossingen.length; i++)
		{
			Expressie[] leerlingOpl = oplossingen[i];
			//mbv isOplossing houd je bij of deze leerlingOplossing inderdaad een oplossing is
			boolean isOplossing = false;
			for(int j = 0; j < juisteOplossingen.length; j++)
			{
				boolean gelijk = true;
				for(int k = 0; k < varNamen.length; k++)
				{
					if(!Algebra.isGelijkwaardig(leerlingOpl[k], juisteOplossingen[j][k]))
					{
						gelijk = false;
						break;
					}
				}
				if(gelijk)
				{	oplossingenCorrect[j] = true;
					isOplossing = true;
				}
			}
			if(!isOplossing)
				return false;
		}
		//controleren of alle oplossingen gevonden zijn. 
		for(int i = 0; i < oplossingenCorrect.length; i++)
		{	if(!oplossingenCorrect[i])
			{	gelijkwaardig = false;
				break;
			}
		}
		return gelijkwaardig;
	}

	public void mousePressed(MouseEvent e)
	{
		formuleVak.requestFocus();
		formuleVak.zetOpEind();
		if (formuleToolBijFocus)
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
	
	public boolean isIngevuld()
	{
		return ingevuld;
	}
	
	public boolean isNagekeken()
	{
		return nagekeken;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == formuleVak && e.getActionCommand().equals("ingevuld"))
		{
			if (mode == 0 || mode == 1)
			{
				kijkNa();
				zetNagekeken(true);
				if (ingevuld)
					parent.produceAction("checked");
			}
		}
		else if (e.getSource() == formuleVak && e.getActionCommand().equals("formChanged"))
		{
			if (feedbackTekst != null && getParent() == null && feedbackTekst.getParent() != null)
			{
				remove(feedbackTekst);
				parent.produceAction("feedbackWeg");
			}
			zetGoedFout(GEEN);
		}
		else if(e.getSource() == formuleVak && e.getActionCommand().equals("zetMaat"))
			parent.resize();
		else if(e.getSource() == formuleVak && e.getActionCommand().equals("focus"))
		{
			if(parent.rekenVakZichtbaar)
			{
				parent.rekenVak.geefHoofdEditor().zetFocusOplossingenRegel();
			}
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
}
