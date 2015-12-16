package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.Constants;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.FeedbackTekstArea;
import fi.wiskopdr.tekstobjects.TekstArea;

public class AntwoordKeuzeVak extends JLayeredPane implements InteractiePanel, ActionListener, CBookAware
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

	private JComboBox antwoordKV;

	private String antwoordString;
	private String[] keuzeMogelijkheden;
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

	private boolean logOption;
	private String logID;

	private boolean[][] logObjectives;

	private String[] randomVars;
	private Hashtable randomValues;

	private int errorCount;
	private int attemptsCount;
	private Vector attempts;

	private String[] stringKV;

	private ImageComponent goedIC, foutIC, halfIC, huidigIC;
	
	private boolean checkExternal = false;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);

	/*private static String[] imageNames = 
	{	"goedkrul.gif",
		"goedkrulhalf.gif",
		"foutkruis.gif",
		"goedkrul_en.gif",
		
	};*/

	//private static Hashtable images;

	/*public static void zetPlaatjes(Image gk, Image fk, Image hk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
		HALFKRUL = hk;
	}*/

	public AntwoordKeuzeVak()
	{
		setLayout(null);

		attempts = new Vector();

		//Integer[] intArray = {new Integer(0),new Integer(1),new Integer(2),new Integer(3)};
		antwoordKV = new JComboBox();
		antwoordKV.setBounds(0, 0, 180, 20);
		antwoordKV.setFont(WiskOpdr.tekstFont);

		ComboBoxFormuleRenderer renderer = new ComboBoxFormuleRenderer();
		//renderer.setPreferredSize(new Dimension(200, 130));
		antwoordKV.setRenderer(renderer);

		antwoordKV.setRenderer(new ComboBoxFormuleRenderer());
		//antwoordKV.setBorder(BorderFactory.createLineBorder(Color.gray));
		//antwoordKV.setMaximumRowCount(3);

		antwoordKV.addItem(WiskOpdr.rb.getString("keuzeVakKiesLabel"));
		antwoordKV.addActionListener(this);
		add(antwoordKV);
		//

		/*if(images==null)
		{	images = new Hashtable();
			WiskOpdr.loadImages(images,imageNames);
		}*/

		goedIC = new ImageComponent(WiskOpdr.GOEDKRUL);
		((ImageComponent) goedIC).zetKlein(true);
		goedIC.setLocation(antwoordKV.getWidth(), 0);
		goedIC.setVisible(false);
		setLayer((Component) goedIC, JLayeredPane.PALETTE_LAYER.intValue());
		add(goedIC, 0);

		halfIC = new ImageComponent(WiskOpdr.HALFKRUL);
		((ImageComponent) halfIC).zetKlein(true);
		halfIC.setLocation(antwoordKV.getWidth(), 0);
		halfIC.setVisible(false);
		setLayer((Component) halfIC, JLayeredPane.PALETTE_LAYER.intValue());
		add(halfIC);

		foutIC = new ImageComponent(WiskOpdr.FOUTKRUIS);
		((ImageComponent) foutIC).zetKlein(true);
		foutIC.setLocation(antwoordKV.getWidth(), 0);
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
		add(feedbackButton, 0);

		feedbackPanel = new JPanel();
		feedbackPanel.setLayout(null);

		setSize(antwoordKV.getWidth() + 30, 20);

	}

	class ComboBoxFormuleRenderer extends TekstArea implements ListCellRenderer
	{

		public ComboBoxFormuleRenderer()
		{

			//zetBovenMarge(-1);
			setOpaque(true);
			//setPreferredSize(new Dimension(100,30));
		}

		/*
		* This method finds the image and text corresponding
		* to the selected value and returns the label, set up
		* to display the text and image.
		*/
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			//Get the selected index. (The index param isn't
			//always valid, so just use the value.)

			//int selectedIndex = ((Integer)value).intValue();

			if (isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			//Set the icon and text.  If icon was null, say so.
			//$f$b$w5-x$m2@@$nx-1@@@
			//setBorder(BorderFactory.createLineBorder(Color.gray));
			setSize(antwoordKV.getWidth() + 50, 20);
			this.setText((String) value);

			//System.out.println("ooo" + (String) value);

			resize();
			setPreferredSize(new Dimension(antwoordKV.getWidth() + 50, getHeight() + 1));

			return this;
		}

	}

	/*public static Image getImage(String name)
	{	return(Image)images.get(name);
	}*/

	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		String[] keuzeMogelijkheden = null;
		String antwoordString = "";
		int puntenGelijkwaardig = 10;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		int scoreMax = 10;
		boolean check = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		boolean[][] logObjectives = null;
		boolean checkExternal = false;

		if (h.containsKey("keuzeMogelijkheden"))
			keuzeMogelijkheden = (String[]) h.get("keuzeMogelijkheden");
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
		if (h.containsKey("logOption"))
			logOption = ((Boolean) h.get("logOption")).booleanValue();
		if (h.containsKey("logID"))
			logID = (String) h.get("logID");
		if (h.containsKey("logObjectives"))
			logObjectives = (boolean[][]) h.get("logObjectives");
		if (h.containsKey("checkExternal"))
			checkExternal = ((Boolean) h.get("checkExternal")).booleanValue();
		
		int aantalKeuzes = 0;
		if (keuzeMogelijkheden != null)
			aantalKeuzes = keuzeMogelijkheden.length;

		for (int i = 0; i < aantalKeuzes; i++)
		{
			try
			{
				System.out.println("keuzeMogelijkheden[" + i + "]: " + keuzeMogelijkheden[i]);
				keuzeMogelijkheden[i] = FormuleParser.randomizeTekstVakString(keuzeMogelijkheden[i], randomVars, randomValues);
				System.out.println("keuzeMogelijkheden[" + i + "] na parsen: " + keuzeMogelijkheden[i]);
			}
			catch (Exception e)
			{
			}
			antwoordKV.addItem(keuzeMogelijkheden[i].trim());

		}

		try
		{
			antwoordString = FormuleParser.randomizeTekstVakString(antwoordString, randomVars, randomValues);
		}
		catch (Exception e)
		{
		}

		this.keuzeMogelijkheden = keuzeMogelijkheden;
		this.antwoordString = antwoordString;
		this.scoreMax = scoreMax;
		this.answerModels = answerModels;
		this.hasFeedback = hasFeedback;
		this.check = check;
		this.teltMee = teltMee;
		this.logOption = logOption;
		this.logID = logID;
		this.logObjectives = logObjectives;
		this.randomVars = randomVars;
		this.randomValues = randomValues;
		this.checkExternal = checkExternal;
		zetJuisteAntwoord(antwoordString);

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
		this.antwoordString = antwoordString;
		this.feedback = feedback;

		zetJuisteAntwoord(antwoordString);

	}

	public void zetJuisteAntwoord(String s)
	{
		antwoordString = s;
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

		antwoordKV.removeActionListener(this);
		antwoordKV.setSelectedItem(antwoord.trim());
		antwoordKV.addActionListener(this);
		
		if (ingevuld && (mode == 0 || nagekeken))
		{	kijkNa();
		}
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

		ingevuld = this.ingevuld;
		nagekeken = this.nagekeken;
		antwoord = (String) antwoordKV.getSelectedItem();
		attempts = this.attempts;
		attemptsCount = this.attemptsCount;
		errorCount = this.errorCount;

		if (!("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)))
			kijkNa(false);
		if (logOption)
		{
			Hashtable logMap = new Hashtable();

			String logString = antwoord;
			if ("Kies".equals("antwoord"))
				logString = "";

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
		String goedFout = "";
		if (huidigIC == goedIC && huidigIC.isVisible())
			goedFout = "goed";
		if (huidigIC == halfIC && huidigIC.isVisible())
			goedFout = "half";
		if (huidigIC == foutIC && huidigIC.isVisible())
			goedFout = "fout";

		String formule = "";
		String string = (String) antwoordKV.getSelectedItem();

		String fbTekst = "";
		if (feedbackTekst.isVisible() && feedbackTekst.getParent() != null)
			fbTekst = feedbackTekst.getText();

		String s = string;
		s = s + "   ;   ";
		s = s + "Regelnummer = ";
		s = s + "   ;   ";
		s = s + goedFout;
		s = s + "   ;   ";
		s = s + "score = " + score;
		s = s + "   ;   ";
		s = s + new Date().toString();
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
		return new AntwoordKeuzeVakEditPanel();
	}

	public void setBounds(int x, int y, int b, int h)
	{
		antwoordKV.setBounds(0, 0, b - 20, h);
		//feedbackButton.setBounds(antwoordKV.getWidth()-15, getSize().height-14, 14,14);
		feedbackButton.setBounds(getWidth() - 15, getSize().height - 12, 12, 12);
		goedIC.setLocation(getWidth() - 18, -3);
		halfIC.setLocation(getWidth() - 18, -3);
		foutIC.setLocation(getWidth() - 18, -3);
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
		errorCount = 0;
		attemptsCount = 0;

		attempts = new Vector();

	}

	public void zetMaat()
	{
	}

	public int geefAsHoogte()
	{
		return 8;
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
		if(correct)cbookEventHandler.fire("action.correct");
    	if(fout)cbookEventHandler.fire("action.false");
	}

	public void kijkNa(boolean show)
	{
		checkAntwoord();

		ingevuld = antwoordKV.getSelectedIndex() > 0;

		correct = false;
		fout = true;
		score = 0;

		if (!ingevuld)
		{
			if (show)
				zetGoedFout(GEEN);
			return;
		}

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

		if (show)
			if (ingevuld)
				produceAction("changed");
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

	public void checkAntwoord()
	{
		if (hasFeedback)
		{
			int aantalAnswerModels = answerModels.length;
			for (int h = 0; h < aantalAnswerModels; h++)
			{
				setAnswerModel(h);
				gelijkwaardig = false;

				gelijkwaardig = antwoordString.trim().equals(((String) antwoordKV.getSelectedItem()).trim());

				if (gelijkwaardig || h == aantalAnswerModels - 1)
				{
					if (!feedback.trim().equals(""))
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
			gelijkwaardig = antwoordString.trim().equals(((String) antwoordKV.getSelectedItem()).trim());
			System.out.println("+" + antwoordString);
			System.out.println("+" + (String) antwoordKV.getSelectedItem());
		}
		repaint();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == antwoordKV)
		{
			if (!checkExternal && (mode == 0 || mode == 1))
			{
				kijkNa();
				if (fout)
					errorCount++;
				System.out.println("errocount:"+errorCount);
				attemptsCount++;
				setAttempt();
				zetNagekeken(true);
				if (ingevuld)
					produceAction("checked");

			}
			else if(checkExternal)
			{
				zetGoedFout(GEEN);
			}
			// TODO  if checkExternal, dan werkt dit nog niet goed met attempts en errors
			
			//cbookEventHandler.fire("index", "index", new Integer(antwoordKV.getSelectedIndex()));
			cbookEventHandler.fire("index", (new Integer(antwoordKV.getSelectedIndex())).toString());
			if(antwoordKV.getSelectedIndex() != 0) 
				cbookEventHandler.fire(Constants.USER_INPUT, antwoordKV.getSelectedItem().toString());

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


	 @Override
		public void acceptCBookEvent(CBookEvent event) {
			// TODO Auto-generated method stub
			
		}
	    
	    @Override
		public void addCBookEventListener(CBookEventListener listener, String command) {
			cbookEventHandler.addCBookEventListener(listener, command);
			
		}

		@Override
		public void removeCBookEventListener(CBookEventListener listener, String command) {
			cbookEventHandler.removeCBookEventListener(listener, command);
			
		}

		@Override
		public String[] getSendCmds() {
			String[] commands = {"index",
					"action.correct",
					"action.false"};
			return commands;
		}

		@Override
		public String[] getAcceptedCmds() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getLocalizedCmd(String cmd) {
			String localizedCmd = WiskOpdr.rb.getString(CBA_PREFIX + cmd);
			if(localizedCmd==null)
				return cmd;
			return localizedCmd;
		}
	
	
	
}
