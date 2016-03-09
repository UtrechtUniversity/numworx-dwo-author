package fi.wiskopdr.stelselsvergelijkingen;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.AntwoordVergelijkingVakEditPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.Vergelijking;
import fi.wiskopdr.formuleobjects.FormuleEditor;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.tekstobjects.TekstArea;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public class StelselAntwoordVak extends JPanel implements InteractiePanel{

	StelselRekenVak rekenVak;
	StelselOplossingenVak oplossingenVak;
	TekstArea oplossingenLabel;
	JPanel oplossingenRegel;
	
	boolean rekenVakZichtbaar = true;
	boolean oplossingenRegelZichtbaar = true;
	
	private String[] randomVarNamen;
	private Hashtable randomVarWaarden;
	
	private boolean check;
	private boolean teltMee;

	private boolean logOption;
	private String logID;
	
	private boolean[][] logObjectives;

	private double eqTestValueMin = 0;
	private double eqTestValueMax = 5;
	
	private int scoreMax = 10;

	
	
	public StelselAntwoordVak()
	{
		setLayout(null);
		setBorder(BorderFactory.createLineBorder(Color.gray));
		rekenVak = new StelselRekenVak(this);
		add(rekenVak);
		
		oplossingenRegel = new JPanel(){
			public void paintComponent(Graphics g)
			{
				if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
				{	g.setColor(Color.white);
					g.fillRect(0,0,getWidth(),getHeight());
				
				}
				else
					for(int i=0 ; i<10 ; i++)
					{	g.setColor(new Color(200+5*i,200+5*i,200+5*i));
						g.fillRect(0,getHeight() - (i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
					}
				
			}
		};
		oplossingenRegel.setBounds(0, getHeight() - 25, getWidth(), 25);
		oplossingenRegel.setLayout(null);
		oplossingenRegel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		
		add(oplossingenRegel);
		
		//oplossingenLabel = new JLabel(WiskOpdr.rb.getString("oplossingenLabel"));
		oplossingenLabel = new TekstArea();
		oplossingenLabel.setOpaque(false);
		oplossingenLabel.setText(WiskOpdr.rb.getString("oplossingenLabel"));
		oplossingenLabel.setLocation(5,2);
		oplossingenRegel.add(oplossingenLabel);
		
		oplossingenVak = new StelselOplossingenVak(this);
		oplossingenRegel.add(oplossingenVak);
		oplossingenVak.setBounds(getWidth() - 200, 1, 198, 24);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		int hoogteOplossingen = 25;
		
		if(rekenVakZichtbaar)
		{	super.setBounds(x,y,b,h);
			if(oplossingenRegelZichtbaar)
			{
				rekenVak.setBounds(1, 0, b-2, h - hoogteOplossingen - 5);
				oplossingenRegel.setBounds(0, h-hoogteOplossingen, b, hoogteOplossingen);
				oplossingenLabel.setBounds(5, 2, 150, 40); //150 was 110, 40 was 20
				oplossingenVak.setBounds(oplossingenLabel.getWidth() + 10, 0, getWidth() - oplossingenLabel.getWidth() - 12, 24);
			}
			else
			{
				rekenVak.setBounds(1, 0, b-2, h - 5);
				oplossingenRegel.setBounds(0, h, 0, 0);
			}
		}
		else
		{	super.setBounds(x, y, b, hoogteOplossingen);
			rekenVak.setBounds(0, h, 0, 0);
			oplossingenRegel.setBounds(0, 0, b, hoogteOplossingen);
		}
			
		
	}

	
	
	@Override
	public void zetOpdracht(Hashtable h, String[] randomVars,
			Hashtable randomValues) {
//		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
//		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
//			if (!geerftFont.getName().equals("TimesRoman") && WiskOpdr.formTimes && !WiskOpdr.mac) {
//				geerftFont = new Font("TimesRoman", geerftFont.getStyle(), geerftFont.getSize() * 6 / 5);
//			}
//			formuleVakFont = geerftFont;
//			formuleVakken[0].setFont(formuleVakFont);
//			formuleVakSimpel.setFont(formuleVakFont);
//		}
		
		randomVarNamen = randomVars;
		randomVarWaarden = randomValues;

		String antwoordString = "$f@";
		String variabelenString = "$f@";
		boolean onafhankelijkNodig = false;
		boolean exact = false;
		//boolean significant = false;
		//boolean stappen = true;
		int puntenGelijkwaardig = 10;
		int puntenExact = 0;
		int puntenSignificant = 0;
		int puntenOnafhankelijk = 0;
		boolean eindOplossingNodig = true;
		int puntenEindOplossing = 0;
		//int puntenVorm = 0;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		boolean feedbackSize = false;
		String vormString = "$f@";
		String[] antwoordSubStrings = null;
		String[] antwoordFuncStrings = null;
		boolean pijl = true;
		boolean check = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		double eqTestValueMin = 0;
		double eqTestValueMax = 5;
		int scoreMax = 0;
		boolean uitw = false;
		boolean boxMetRand = true;
		boolean rekenVakZichtbaar = true;
		boolean oplossingenRegelZichtbaar = true;
		boolean[][] logObjectives = null;
		
		if (h.containsKey("antwoordString"))
			antwoordString = (String) h.get("antwoordString");
		if (h.containsKey("variabelenString"))
			variabelenString = (String) h.get("variabelenString");
		if (h.containsKey("onafhankelijkNodig"))
			onafhankelijkNodig = ((Boolean) h.get("onafhankelijkNodig")).booleanValue();
		if (h.containsKey("exact"))
			exact = ((Boolean) h.get("exact")).booleanValue();
//		if(h.containsKey("significant")) 
//			significant = ((Boolean)h.get("significant")).booleanValue();
//		if (h.containsKey("stappen"))
//			stappen = ((Boolean) h.get("stappen")).booleanValue();
		if (h.containsKey("puntenGelijkwaardig"))
			puntenGelijkwaardig = ((Integer) h.get("puntenGelijkwaardig")).intValue();
		if (h.containsKey("puntenExact"))
			puntenExact = ((Integer) h.get("puntenExact")).intValue();
		if(h.containsKey("puntenSignificant")) 
			puntenSignificant = ((Integer)h.get("puntenSignificant")).intValue();
		if (h.containsKey("puntenOnafhankelijk"))
			puntenOnafhankelijk = ((Integer) h.get("puntenOnafhankelijk")).intValue();
		if (h.containsKey("puntenEindOplossing"))
			puntenEindOplossing = ((Integer) h.get("puntenEindOplossing")).intValue();
		if (h.containsKey("eindOplossingNodig"))
			eindOplossingNodig = ((Boolean) h.get("eindOplossingNodig")).booleanValue();
		if (h.containsKey("answerModels"))
			answerModels = (Hashtable[]) h.get("answerModels");
		if (h.containsKey("hasFeedback"))
			hasFeedback = ((Boolean) h.get("hasFeedback")).booleanValue();
		if(h.containsKey("feedbackSize")) 
			feedbackSize = ((Boolean)h.get("feedbackSize")).booleanValue();
		
		if (h.containsKey("vormString"))
			vormString = (String) h.get("vormString");
		

//		if (h.containsKey("feedbackModus"))
//			feedbackModus = ((Integer) h.get("feedbackModus")).intValue();
		if (h.containsKey("antwoordSubStrings"))
			antwoordSubStrings = (String[]) h.get("antwoordSubStrings");
		if (h.containsKey("antwoordFuncStrings"))
			antwoordFuncStrings = (String[]) h.get("antwoordFuncStrings");
		if (h.containsKey("pijl"))
			pijl = ((Boolean) h.get("pijl")).booleanValue();
		if (h.containsKey("check"))
			check = ((Boolean) h.get("check")).booleanValue();
		if (h.containsKey("teltMee"))
			teltMee = ((Boolean) h.get("teltMee")).booleanValue();
		if (h.containsKey("logOption"))
			logOption = ((Boolean) h.get("logOption")).booleanValue();
		if (h.containsKey("logID"))
			logID = (String) h.get("logID");

		if (h.containsKey("eqTestValueMin"))
			eqTestValueMin = ((Double) h.get("eqTestValueMin")).doubleValue();
		if (h.containsKey("eqTestValueMax"))
			eqTestValueMax = ((Double) h.get("eqTestValueMax")).doubleValue();
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		if (h.containsKey("uitw"))
			uitw = ((Boolean) h.get("uitw")).booleanValue();
		if (h.containsKey("boxMetRand"))
			boxMetRand = ((Boolean) h.get("boxMetRand")).booleanValue();
		if (h.containsKey("rekenVakZichtbaar"))
			rekenVakZichtbaar = ((Boolean) h.get("rekenVakZichtbaar")).booleanValue();
		if (h.containsKey("oplossingenRegelZichtbaar"))
			oplossingenRegelZichtbaar = ((Boolean) h.get("oplossingenRegelZichtbaar")).booleanValue();
		if(h.containsKey("logObjectives")) 
			logObjectives = (boolean[][])h.get("logObjectives");
		
		this.check = check;
		this.teltMee = teltMee;
		this.logOption = logOption;
		this.logID = logID;
		this.eqTestValueMin = eqTestValueMin;
		this.eqTestValueMax = eqTestValueMax;

		this.rekenVakZichtbaar = rekenVakZichtbaar;
		this.oplossingenRegelZichtbaar = oplossingenRegelZichtbaar;
		this.logObjectives = logObjectives;
		
		//zetStappen(stappen);

		String[] varNamen;
		Expressie[][] oplossingen;
		try
		{
			antwoordString = FormuleParser.randomizeTekstVakString(antwoordString, randomVars, randomValues);
		}
		catch (Exception e)
		{
		}
		antwoordString = StringUtils.replaceStr(antwoordString, " ", "");
		
		try
		{
			variabelenString = FormuleParser.randomizeTekstVakString(variabelenString, randomVars, randomValues);
		}
		catch (Exception e)
		{
		}
		variabelenString = StringUtils.replaceStr(variabelenString, " ", "");
		
		try{
			//als nodig: haakjes weghalen. Anders alleen $f en @ weghalen.
			if(variabelenString.startsWith("$f("))
				variabelenString = variabelenString.substring(3, variabelenString.length() - 2);
			else
				variabelenString = variabelenString.substring(2, variabelenString.length() -1);
			varNamen = StringUtils.split(variabelenString, ",");
			//variabelen omzetten naar nette varnamen (met name belangrijk voor variabelen met subscripts)
			for(int i = 0; i < varNamen.length; i++)
			{
				FormuleEditor hulpVak = new FormuleEditor(false);
				hulpVak.formuleVak.vulVak("$f" + varNamen[i] + "@");
				varNamen[i] = hulpVak.formuleVak.geefExpressie().geefVarNaam();
				
			}
			
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
			rekenVak.zetVarNamen(varNamen);
			rekenVak.zetJuisteOplossingen(oplossingen);
			oplossingenVak.zetVarNamen(varNamen);
			oplossingenVak.zetJuisteOplossingen(oplossingen);
			//Font f = oplossingenLabel.getFont();
			//FontMetrics fm = new FontMetrics(f){};
			
			oplossingenLabel.setText(WiskOpdr.rb.getString("oplossingenLabel") + " $f(" + variabelenString + ")@");
			//oplossingenLabel.setSize(fm.stringWidth(oplossingenLabel.getText()), 20);
			
		}
		catch(Exception e)
		{}
		
		
		

		//zetStartString(startString);

//		this.answerModels = answerModels;
//		this.hasFeedback = hasFeedback;
//		this.feedbackSize = feedbackSize;

		this.scoreMax = scoreMax;
		//this.uitw = uitw;
		
		
//		wisKnop.setVisible(casAntw  && !hasStartString || tips && !hasStartString && diagnose);
//		zetMetRand(boxMetRand);
//		feedbackIC.setVisible(false);
		
		rekenVak.zetOpdracht(h, randomVars, randomValues);
		oplossingenVak.zetOpdracht(h, randomVars, randomValues);
	}

	@Override
	public void setState(Hashtable b) {
		if(rekenVakZichtbaar)
			rekenVak.setState(b);
		if(oplossingenRegelZichtbaar)
		{	if(b.containsKey("oplRegelState"))
			{
				Hashtable h1 = (Hashtable) b.get("oplRegelState");
				oplossingenVak.setState(h1);
			}
		}
			
	}

	@Override
	public void setEditState(Hashtable b) {
		boolean rekenVakZichtbaar = true;
		boolean oplossingenRegelZichtbaar = true;
		
		if (b.containsKey("rekenVakZichtbaar"))
			rekenVakZichtbaar = ((Boolean) b.get("rekenVakZichtbaar")).booleanValue();
		if (b.containsKey("oplossingenRegelZichtbaar"))
			oplossingenRegelZichtbaar = ((Boolean) b.get("oplossingenRegelZichtbaar")).booleanValue();
		
		
		this.rekenVakZichtbaar = rekenVakZichtbaar;
		this.oplossingenRegelZichtbaar = oplossingenRegelZichtbaar;
		
	}

	@Override
	public Hashtable getState() {
		Hashtable h = new Hashtable();
		if(rekenVakZichtbaar)
		{	h = rekenVak.getState();
		}
		if(oplossingenRegelZichtbaar)
		{
			Hashtable h1 = oplossingenVak.getState();
			h.put("oplRegelState", h1);
		}
		return h;
	}

	@Override
	public Hashtable getEditState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InteractieEditPanel getEditPanel() {
		return new StelselAntwoordVakEditPanel();
	}

	@Override
	public void wis() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zetMaat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getIpId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScore() {
		if(oplossingenRegelZichtbaar)
			return oplossingenVak.getScore();
		else
			return rekenVak.geefHoofdEditor().getScore();
		//TODO: dit laatste is waarschijnlijk nog niet handig. Naar kijken voor als oplossingenregel niet zichtbaar.
	}

	@Override
	public int[][] getScoreObjectives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getScoreMax() {
		return scoreMax;
	}

	@Override
	public boolean isCorrect() {
		if(oplossingenRegelZichtbaar)
			return oplossingenVak.isCorrect();
		else
			return rekenVak.isCorrect();
	}

	@Override
	public boolean isFout() {
		if(oplossingenRegelZichtbaar)
			return oplossingenVak.isFout();
		else
			return rekenVak.isFout();
	}

	@Override
	public void zetMode(int mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zetNagekeken(boolean b) {
		// TODO Auto-generated method stub
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		if(oplossingenRegelZichtbaar)
			oplossingenVak.start();
		if(rekenVakZichtbaar)
			rekenVak.start();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void opnieuw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kijkNa() {
		// TODO Auto-generated method stub
		if(oplossingenRegelZichtbaar)
			oplossingenVak.kijkNa();
		if(rekenVakZichtbaar)
			rekenVak.kijkNa();
	}

	@Override
	public void kijkNa(int stapNr) {
		if(oplossingenRegelZichtbaar)
			oplossingenVak.kijkNa(stapNr);
		if(rekenVakZichtbaar)
			rekenVak.kijkNa(stapNr);
	}

	// ActionProducer
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

}
