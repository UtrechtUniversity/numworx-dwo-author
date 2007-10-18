package fi.algebrapijlenopdr.opdrnav;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.beans.scorm.*;

public class OpdrNavStruct extends Panel implements ActionListener
{
	protected static int OEFENEN = 0;
	protected static int OEFENEN_STRAFPUNTEN = 1;
	protected static int ZELFTOETS = 2;
	protected static int EINDTOETS = 3;
		
	private Image im;
  	private Graphics gIm;
  	
	private Label scoreLabel;
	private Label[] scores;
	private int[] aantalNakijken;
	
	private OpdrContainer[][] opdrContainers;
	private OpdrContainer opdrContainerActief;
	
	private int maxAantalActiviteiten = 5;
	private int aantalActiviteiten;
	private int activiteitNr;
	
	private String[] activiteitNamen;
	
	private int maxAantalOpdrachten = 10;
	private int[] aantalOpdrachten;
	private int opdrachtNr;
	
	private OpdrachtNrRij[] or;
	private OpdrachtNrRij orActief;	
	
	private ActKeuzePanel actKeuzePanel;
	
	private int orPosX, orPosY;
	private int actKeuzePanelX, actKeuzePanelY;
	
	private Button opnieuwKnop, nakijkKnop;
	private Label aantalNakijkLabel;
	
	private int mode;
	private int nakijkStraf = 5;
	private int foutStraf = 2;
	private int[][] strafpunten;
	
	private SCORM12APIInterface api;
	private boolean editMode;
	private PlusMinKnop aantalNivKnop;
	private PlusMinKnop aantalOpdrKnop;
	private PlusMinKnop opdrPositieKnop;

	public OpdrNavStruct(int aantalActiviteiten, int[] aantalOpdrachten, String[] activiteitNamen, int x, int y, int b, int h, SCORM12APIInterface api, boolean editMode) 
	{	setLayout(null);
		setBounds(x,y,b,h);
		
		
		this.api = api;
		this.editMode = editMode;
		
		orPosX = 380;
		orPosY = h-60;
		actKeuzePanelX = 240;
		actKeuzePanelY = orPosY-aantalActiviteiten*20-15;
		
		nakijkKnop = new Button("Kijk na");
		nakijkKnop.setBounds(orPosX + aantalOpdrachten[0]*25 + 20,orPosY,60,20);
		nakijkKnop.addActionListener(this);
		nakijkKnop.setVisible(false);
		add(nakijkKnop);
	
		aantalNakijkLabel = new Label("20 keer nagekeken");
		aantalNakijkLabel.setBounds(orPosX + aantalOpdrachten[0]*25,orPosY+25,110,20);
		aantalNakijkLabel.setAlignment(Label.CENTER);
		aantalNakijkLabel.setVisible(false);
		add(aantalNakijkLabel);
	
		opnieuwKnop = new Button("Opnieuw");
		opnieuwKnop.setBounds(orPosX + aantalOpdrachten[0]*25 + 100,orPosY,60,20);
		opnieuwKnop.addActionListener(this);
		opnieuwKnop.setVisible(false);
		add(opnieuwKnop);
		
		scoreLabel = new Label("score");
		scoreLabel.setBounds(orPosX-50,orPosY+25,50,24);
		//if(!editMode)add(scoreLabel);

		activiteitNr = 0;
		opdrachtNr = 0;
		
		this.aantalActiviteiten = aantalActiviteiten;
		this.aantalOpdrachten = aantalOpdrachten;
		this.activiteitNamen = activiteitNamen;
		
		if(editMode) actKeuzePanel = new ActKeuzePanel(activiteitNamen,actKeuzePanelX,actKeuzePanelY,140,aantalActiviteiten*20, true);
		else actKeuzePanel = new ActKeuzePanel(activiteitNamen,actKeuzePanelX,actKeuzePanelY,140,aantalActiviteiten*20);
		actKeuzePanel.addActionListener(this);
		actKeuzePanel.setBackground(getBackground());
		if(aantalActiviteiten>1 || editMode) add(actKeuzePanel);
		
		scores = new Label[aantalActiviteiten];
		
		for(int i=0 ; i<aantalActiviteiten; i++)
	    {	scores[i] = new Label("Score: "+0);
	    	scores[i].setBounds(actKeuzePanelX+140,actKeuzePanelY + i*20,100,20);
	    	scores[i].setFont(new Font("SansSerif",Font.PLAIN,14));
	    	if(aantalActiviteiten>1 && !editMode)add(scores[i]);
	    }
	 	
	 	aantalNakijken = new int[aantalActiviteiten];
	 	for(int i=0 ; i<aantalActiviteiten; i++)
	    {	aantalNakijken[i] = 0;
	    }
	    
	    for(int i=0 ; i<aantalActiviteiten; i++)
	    {	if(aantalOpdrachten[i]>maxAantalOpdrachten) maxAantalOpdrachten = aantalOpdrachten[i];
	    }
		
		opdrContainers = new OpdrContainer[aantalActiviteiten][maxAantalOpdrachten];

		or = new OpdrachtNrRij[aantalActiviteiten];
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	or[i] = new OpdrachtNrRij(aantalOpdrachten[i], orPosX,orPosY);
			or[i].addActionListener(this);
			if(editMode) or[i].setSize(or[i].getSize().width,25);
			or[i].setBackground(getBackground());
			or[i].setSelected(1);
			or[i].setVisible(false);
			add(or[i]);
		}
		orActief = or[activiteitNr];
		orActief.setVisible(true);
		
		strafpunten = new int[aantalActiviteiten][maxAantalOpdrachten];
		for(int i=0 ; i<aantalActiviteiten; i++)
	    {	for(int j=0 ; j<maxAantalOpdrachten ; j++)
	    	{	strafpunten[i][j] = 0;
	    	}
	    }
	    
	    if(editMode)
	    {	aantalNivKnop = new PlusMinKnop(actKeuzePanelX-2,actKeuzePanelY+aantalActiviteiten*20,16,20,PlusMinKnop.VERTIKAAL);
	    	aantalNivKnop.addActionListener(this);
	    	add(aantalNivKnop);
	    	
	    	aantalOpdrKnop = new PlusMinKnop(orPosX+25*aantalOpdrachten[activiteitNr]+5 ,orPosY+2,20,16,PlusMinKnop.HORIZONTAAL);
	    	aantalOpdrKnop.addActionListener(this);
	    	add(aantalOpdrKnop);
	    		    	
	    	opdrPositieKnop = new PlusMinKnop(orPosX ,orPosY+30,20,16,PlusMinKnop.HORIZONTAAL);
	    	opdrPositieKnop.addActionListener(this);
	    	add(opdrPositieKnop);
	    	
	    }
	}

	public void setState(Hashtable h)
	{	int aantalActiviteiten = ((Integer)h.get("aantalActiviteiten")).intValue();
	    int activiteitNr = ((Integer)h.get("activiteitNr")).intValue();
	    int maxAantalOpdrachten = ((Integer)h.get("maxAantalOpdrachten")).intValue();
	    int opdrachtNr = ((Integer)h.get("opdrachtNr")).intValue();
		int[] aantalOpdrachten = (int[])h.get("aantalOpdrachten");
		Hashtable[][] opdrContStates = (Hashtable[][])h.get("opdrContStates");
		boolean[][] orGoedFout = (boolean[][])h.get("orGoedFout");
		int[][] orScores = (int[][])h.get("orScores");
		int[] aantalNakijken = (int[])h.get("aantalNakijken");
		//
		int[][] strafpunten = null;
		if(h.containsKey("strafpunten")) strafpunten = (int[][])h.get("strafpunten");
		//
		this.aantalActiviteiten = aantalActiviteiten;
		this.activiteitNr = activiteitNr;
		this.opdrachtNr = opdrachtNr;
		//
		if(strafpunten != null) this.strafpunten = strafpunten;
		//
		for(int i=0 ; i<aantalActiviteiten; i++)
	    {	this.aantalOpdrachten[i] = aantalOpdrachten[i];
	    	this.aantalNakijken[i] = aantalNakijken[i];
	    	int totaal = 0;
	    	for(int j=0 ; j<aantalOpdrachten[i]; j++)
		    {	opdrContainers[i][j].setState(opdrContStates[i][j]);
		    	or[i].zetGemaakt(j+1, orGoedFout[i][j]);
		    	or[i].zetScore(j+1, orScores[i][j]);
		    	totaal += orScores[i][j];
		    }
		    if(mode==2 && totaal>0)totaal = Math.max(0,totaal-(aantalNakijken[i]-1)*nakijkStraf);
			scores[i].setText("Score: "+totaal);
	    }
	    aantalNakijkLabel.setText(""+aantalNakijken[activiteitNr]+" keer nagekeken");
	    if(mode==2 && aantalNakijken[activiteitNr]>0)aantalNakijkLabel.setVisible(true);
	    
	    
	    if(opdrContainerActief!=null)opdrContainerActief.setVisible(false);
	    opdrContainerActief = opdrContainers[activiteitNr][opdrachtNr];
		opdrContainerActief.setVisible(true);
		opdrContainerActief.start();
		
		orActief.setVisible(false);
		orActief = or[activiteitNr];
		orActief.setSelected(opdrachtNr+1);
		orActief.setVisible(true);
		
		actKeuzePanel.setItem(activiteitNr);
		
		
	}
	
	public Hashtable getState()
	{	int aantalActiviteiten = 0;
	    int activiteitNr = 0;
	    int opdrachtNr = 0;
	    int maxAantalOpdrachten = 0;
		int[] aantalOpdrachten = null;
		Hashtable[][] opdrContStates = null;
		boolean[][] orGoedFout = null;
		int[][] orScores = null;
		int[] aantalNakijken = null;
		int[][] strafpunten = null;
		
		aantalActiviteiten = this.aantalActiviteiten;
		activiteitNr = this.activiteitNr;
		opdrachtNr = this.opdrachtNr;
		maxAantalOpdrachten = this.maxAantalOpdrachten;
		strafpunten = this.strafpunten;
		aantalOpdrachten = new int[aantalActiviteiten];
		aantalNakijken = new int[aantalActiviteiten];
		orGoedFout = new boolean [aantalActiviteiten][maxAantalOpdrachten];
		orScores =  new int[aantalActiviteiten][maxAantalOpdrachten];
		opdrContStates = new Hashtable[aantalActiviteiten][maxAantalOpdrachten];
		for(int i=0 ; i<aantalActiviteiten; i++)
	    {	aantalOpdrachten[i] = this.aantalOpdrachten[i];
	    	aantalNakijken[i] = this.aantalNakijken[i];
	    	for(int j=0 ; j<aantalOpdrachten[i]; j++)
		    {	opdrContStates[i][j] = opdrContainers[i][j].getState();
		    	orGoedFout[i][j] = or[i].geefGoedFout(j+1);
		    	orScores[i][j] = or[i].geefScore(j+1);
		    }
	    }
			    
	    Hashtable h = new Hashtable();
	    h.put("aantalActiviteiten", new Integer(aantalActiviteiten));
	    h.put("activiteitNr", new Integer(activiteitNr));
	    h.put("opdrachtNr", new Integer(opdrachtNr));
	    h.put("maxAantalOpdrachten", new Integer(maxAantalOpdrachten));
	    h.put("aantalOpdrachten", aantalOpdrachten);
	    h.put("opdrContStates", opdrContStates);
	    h.put("orGoedFout", orGoedFout);
	    h.put("orScores", orScores);
	    h.put("aantalNakijken", aantalNakijken);
	    h.put("strafpunten", strafpunten);
  
	    return h;
	    
	}

	public double getScore()
	{	int totaal = 0; 
		for(int i=0 ; i<aantalActiviteiten; i++)
	    {	String scoreString = scores[i].getText();
	    	int score = 10*Integer.parseInt(scoreString.substring(7))/aantalOpdrachten[i];
	    	totaal += score;
	    }
	    double doubleScore = (double)totaal/aantalActiviteiten;
	    if(Double.isInfinite(doubleScore) || Double.isNaN(doubleScore)) doubleScore=0;
	    return doubleScore;
	}
	
	public void setEditState(Hashtable h)
	{	int aantalActiviteiten = Integer.parseInt(((String)h.get("aantalActiviteiten")));
		  
  		this.aantalActiviteiten = aantalActiviteiten;
  		
  		for(int i=0 ; i<aantalActiviteiten; i++)
	    {	this.aantalOpdrachten[i] = Integer.parseInt(((String)h.get("aantalOpdrachten_"+(i+1))));
	    	for(int j=0 ; j<aantalOpdrachten[i]; j++)
		    {	opdrContainers[i][j].setEditState((String)h.get("opdracht_"+(i+1)+"_"+(j+1)));
		    }
	    }
	    opdrContainerActief.setVisible(false);
	    opdrContainerActief = opdrContainers[0][0];
		opdrContainerActief.setVisible(true);
		
		orActief.setVisible(false);
		orActief = or[0];
		orActief.setSelected(0);
		orActief.setVisible(true);
		
		actKeuzePanel.setItem(activiteitNr);
	}
	
	public Hashtable getEditState()
	{	int aantalActiviteiten = 0;
		
		aantalActiviteiten = this.aantalActiviteiten;
					    
	    Hashtable h = new Hashtable();
	    h.put("aantalActiviteiten", new String(""+aantalActiviteiten));
	    for(int i=0 ; i<aantalActiviteiten; i++)
	    {	h.put("activiteit_"+(i+1), activiteitNamen[i]);
	    	h.put("aantalOpdrachten_"+(i+1), new String(""+aantalOpdrachten[i]));
	    	for(int j=0 ; j<aantalOpdrachten[i]; j++)
		    {	h.put("opdracht_"+(i+1)+"_"+(j+1), opdrContainers[i][j].getEditState());
		    }
	    }
	    return h;
	}
	
	public void update(Graphics g)
	{	paint(g);
	}

	public void paint(Graphics g)
	{	Dimension dd = getSize();				
		if (im == null)
		{	im = createImage(dd.width, dd.height);
			gIm = im.getGraphics();
		}
		gIm.setColor(getBackground());
		gIm.fillRect(0,0,dd.width, dd.height);
		super.paint(gIm);
		g.drawImage(im, 0, 0, null);
	}	
	
	public void zetOpdrContainer(OpdrContainer opdrContainer, int actNr, int opdrNr)
	{	opdrContainers[actNr][opdrNr] = opdrContainer;
		if(actNr==0 && opdrNr==0)opdrContainerActief = opdrContainers[actNr][opdrNr];
		opdrContainers[actNr][opdrNr].addActionListener(this);
		add(opdrContainers[actNr][opdrNr]);
		if(actNr!=activiteitNr || opdrNr!=opdrachtNr)opdrContainers[actNr][opdrNr].setVisible(false);
	}
	
	public void zetOpdrachtNr(int actNr, int opdrNr)
	{	if(opdrContainerActief!=null)opdrContainerActief.setVisible(false);
		activiteitNr = actNr;
		opdrachtNr = opdrNr;
		opdrContainerActief = opdrContainers[activiteitNr][opdrachtNr];
		opdrContainerActief.setVisible(true);
		opdrContainerActief.start();
	}

	public void zetMode(int mode)
	{	this.mode = mode;
	    for(int i=0 ; i<aantalActiviteiten; i++)
	    {	for(int j=0 ; j<aantalOpdrachten[i]; j++)
		    {	if(opdrContainers[i][j] != null)
		    	{	opdrContainers[i][j].zetMode(mode);
		    	}
		    }
	    }
	 	if(mode==ZELFTOETS)
	 	{	opnieuwKnop.setVisible(true);
	 		nakijkKnop.setVisible(true);
	 	}
	 	if(mode==OEFENEN_STRAFPUNTEN)
	 	{	opnieuwKnop.setVisible(true);
	 	}
	 	if(mode==EINDTOETS)
	 	{	String s = api.LMSGetValue("USER_GROUP");
			if(s!=null && s.equals("UG_TEACHER"))
	 		{	nakijkKnop.setVisible(true);
	 		}
	 	}
	}
	
	public int geefAantalActiviteiten()
	{	return aantalActiviteiten;
	}
	
	public int geefAantalOpdrachten(int activiteit)
	{	return aantalOpdrachten[activiteit];
	}
	
	public void setTeacher(boolean b)
	{	if(b && mode==EINDTOETS) nakijkKnop.setVisible(true);
	}
	
	public void start()
	{	opdrContainerActief = opdrContainers[activiteitNr][opdrachtNr];
		opdrContainerActief.setVisible(true);
		opdrContainerActief.start();
	}
	
	public void destroy()
	{	
		for(int i=0 ; i<aantalActiviteiten; i++)
	    {	for(int j=0 ; j<aantalOpdrachten[i]; j++)
		    {	opdrContainers[i][j].destroy();
		    	remove(opdrContainers[i][j]);
		    	opdrContainers[i][j] = null;
		    }
	    }
	    if(gIm!=null)
		{	gIm.dispose();
			gIm = null;
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == nakijkKnop)
		{	aantalNakijken[activiteitNr]++;
			int totaal = 0;
		    for(int j=0 ; j<aantalOpdrachten[activiteitNr]; j++)
			{	opdrContainers[activiteitNr][j].kijkNa();
			   	int score = opdrContainers[activiteitNr][j].getScore();
				boolean correct = opdrContainers[activiteitNr][j].isCorrect();
				orActief.zetGemaakt(j+1,correct);
				orActief.zetScore(j+1,score);
				totaal += score;
			}
			if(mode==2)
			{	totaal = Math.max(0,totaal-(aantalNakijken[activiteitNr]-1)*nakijkStraf);
				aantalNakijkLabel.setText(""+aantalNakijken[activiteitNr]+" keer nagekeken");
	    		if(aantalNakijken[activiteitNr]>0)aantalNakijkLabel.setVisible(true);
			}
			scores[activiteitNr].setText("Score: "+totaal);
			
	    
			    	
		}
		else if(e.getSource() == opnieuwKnop)
		{	aantalNakijken[activiteitNr] = 0;
			scores[activiteitNr].setText("Score: "+0);
			for(int j=0 ; j<aantalOpdrachten[activiteitNr]; j++)
			{	opdrContainers[activiteitNr][j].opnieuw();
			   	orActief.zetGemaakt(j+1,false);
				orActief.zetScore(j+1,0);
				strafpunten[activiteitNr][j] = 0;
			}
			if(mode==2)
			{	aantalNakijkLabel.setText(""+aantalNakijken[activiteitNr]+" keer nagekeken");
	    		aantalNakijkLabel.setVisible(false);
			}
		}
		else if(e.getSource() == orActief)
		{	opdrContainerActief.stop();
			opdrachtNr = Integer.parseInt(e.getActionCommand())-1;
			if(opdrContainerActief!=null)opdrContainerActief.setVisible(false);
			if(editMode)opdrPositieKnop.setLocation(orPosX+25*opdrachtNr,orPosY+30);
			opdrContainerActief = opdrContainers[activiteitNr][opdrachtNr];
			opdrContainerActief.setVisible(true);
			opdrContainerActief.start();
		}
		else if(e.getSource()== actKeuzePanel)
		{	if(e.getActionCommand().equals("editLabel"))
			{	for (int i = 0; i<aantalActiviteiten; i++) 
				{	activiteitNamen[i] = actKeuzePanel.getlabel(i);
				}
			}
			else
			{	opdrContainerActief.stop();
				if(activiteitNr==actKeuzePanel.geefKeuze()-1)return;
				orActief.setVisible(false);
				activiteitNr = actKeuzePanel.geefKeuze()-1;
				orActief = or[activiteitNr];
				orActief.setSelected(1);
				orActief.setVisible(true);
				opdrachtNr = 0;
				if(editMode)opdrPositieKnop.setLocation(orPosX+25*opdrachtNr,orPosY+30);
				if(opdrContainerActief!=null)opdrContainerActief.setVisible(false);
				opdrContainerActief = opdrContainers[activiteitNr][opdrachtNr];
				opdrContainerActief.setVisible(true);
				opdrContainerActief.start();
				if(editMode) 
				{	aantalOpdrKnop.setLocation(orPosX+25*aantalOpdrachten[activiteitNr]+5, orPosY+2);
					return;
				}
				aantalNakijkLabel.setText(""+aantalNakijken[activiteitNr]+" keer nagekeken");
				if(aantalNakijken[activiteitNr]==0)aantalNakijkLabel.setVisible(false);
				else if(mode==2)aantalNakijkLabel.setVisible(true);
			}
		}
		else if(e.getSource() == opdrContainerActief)
		{	if(e.getActionCommand().equals("changed"))
			{	int score = opdrContainerActief.getScore();
				boolean correct = opdrContainerActief.isCorrect();
				if(!correct && mode==OEFENEN_STRAFPUNTEN)strafpunten[activiteitNr][opdrachtNr] += foutStraf;
				if(correct) score = Math.max(0,score - strafpunten[activiteitNr][opdrachtNr]);
				orActief.zetGemaakt(opdrachtNr+1,correct);
				orActief.zetScore(opdrachtNr+1,score);
				int totaal = 0;
				for(int i=0 ; i<aantalOpdrachten[activiteitNr]; i++)
				{	totaal += orActief.geefScore(i+1);
				}
				scores[activiteitNr].setText("Score: "+totaal);
			}
			else if(e.getActionCommand().equals("kopieer")  && opdrachtNr>0)
			{	Hashtable h = opdrContainers[activiteitNr][opdrachtNr-1].getState();
				opdrContainerActief.setNewState(h);
			}
		}
		else if(e.getSource() == aantalOpdrKnop)
		{	if(e.getActionCommand().equals("min") && aantalOpdrachten[activiteitNr]>1)
			{	remove(opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1]);
				opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1] = null;
				aantalOpdrachten[activiteitNr]--;
				opdrachtNr = 0;
				opdrContainerActief.setVisible(false);
				opdrContainerActief = opdrContainers[activiteitNr][opdrachtNr];
				opdrContainerActief.setVisible(true);
				remove(or[activiteitNr]);
				orActief = null;
				or[activiteitNr] = new OpdrachtNrRij(aantalOpdrachten[activiteitNr], orPosX,orPosY);
				or[activiteitNr].addActionListener(this);
				if(editMode) or[activiteitNr].setSize(or[activiteitNr].getSize().width,25);
				or[activiteitNr].setBackground(getBackground());
				or[activiteitNr].setSelected(opdrachtNr+1);
				orActief = or[activiteitNr];
				add(or[activiteitNr],0);
				aantalOpdrKnop.setLocation(orPosX+25*aantalOpdrachten[activiteitNr]+5, orPosY+2);
			}
			if(e.getActionCommand().equals("plus") && aantalOpdrachten[activiteitNr]<maxAantalOpdrachten)
			{	aantalOpdrachten[activiteitNr]++;
				Class c = opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-2].getClass();
				String editState = opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-2].getEditState();
				try 
				{	opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1] = (OpdrContainer)c.newInstance();
					zetOpdrContainer(opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1],activiteitNr,aantalOpdrachten[activiteitNr]-1);
					opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1].setEditState(editState);
				}
				catch (Exception e1) {}
				remove(or[activiteitNr]);
				orActief = null;
				or[activiteitNr] = new OpdrachtNrRij(aantalOpdrachten[activiteitNr], orPosX,orPosY);
				or[activiteitNr].addActionListener(this);
				if(editMode) or[activiteitNr].setSize(or[activiteitNr].getSize().width,25);
				or[activiteitNr].setBackground(getBackground());
				or[activiteitNr].setSelected(opdrachtNr+1);
				orActief = or[activiteitNr];
				add(or[activiteitNr],0);
				aantalOpdrKnop.setLocation(orPosX+25*aantalOpdrachten[activiteitNr]+5, orPosY+2);
			}
		}
		else if(e.getSource() == aantalNivKnop)
		{	if(e.getActionCommand().equals("plus") && aantalActiviteiten>1)
			{	for (int i = 0; i<aantalOpdrachten[aantalActiviteiten-1]; i++) 
				{	remove(opdrContainers[aantalActiviteiten-1][i]);
				}
				orActief = null;
				remove(or[aantalActiviteiten-1]);
				activiteitNr = 0;
				opdrachtNr = 0;
				opdrContainerActief.setVisible(false);
				opdrContainerActief = opdrContainers[activiteitNr][opdrachtNr];
				opdrContainerActief.setVisible(true);
				orActief = or[activiteitNr];
				orActief.setVisible(true);
				
				aantalActiviteiten--;
				remove(actKeuzePanel);
				actKeuzePanelY = orPosY-aantalActiviteiten*20-15;
				actKeuzePanel = new ActKeuzePanel(activiteitNamen,actKeuzePanelX,actKeuzePanelY,140,aantalActiviteiten*20, true);
				actKeuzePanel.addActionListener(this);
				actKeuzePanel.setBackground(getBackground());
				add(actKeuzePanel,0);
			}
			if(e.getActionCommand().equals("min") && aantalActiviteiten<maxAantalActiviteiten)
			{	aantalActiviteiten++;
				
				
				OpdrContainer[][] opdrContainersNieuw = new OpdrContainer[aantalActiviteiten][maxAantalOpdrachten];
				for (int i = 0; i<aantalActiviteiten-1; i++) 
				{	for (int j = 0; j<aantalOpdrachten[i]; j++) 
					{	opdrContainersNieuw[i][j] = opdrContainers[i][j];
					}
			    }
			    opdrContainers = opdrContainersNieuw;
			    
			    int[] aantalOpdrachtenNieuw = new int[aantalActiviteiten];
			    for (int i = 0; i<aantalActiviteiten-1; i++) 
			    {	aantalOpdrachtenNieuw[i] = aantalOpdrachten[i];
			    }
			    aantalOpdrachten = aantalOpdrachtenNieuw;
			    
			    String[] activiteitNamenNieuw = new String[aantalActiviteiten];
			    for (int i = 0; i<aantalActiviteiten-1; i++) 
			    {	activiteitNamenNieuw[i] = activiteitNamen[i];
			    }
			    activiteitNamen = activiteitNamenNieuw;
			    
			    aantalOpdrachten[aantalActiviteiten-1] = 1;
			    activiteitNamen[aantalActiviteiten-1] = "Niveau "+ aantalActiviteiten;
			    
			    Class c = opdrContainers[aantalActiviteiten-2][0].getClass();
			    try 
				{	opdrContainers[aantalActiviteiten-1][0] = (OpdrContainer)c.newInstance();
					zetOpdrContainer(opdrContainers[aantalActiviteiten-1][0],aantalActiviteiten-1,0);
				}
				catch (Exception e1) {}
			    

				OpdrachtNrRij[] orNieuw = new OpdrachtNrRij[aantalActiviteiten];
				for(int i=0 ; i<aantalActiviteiten-1 ; i++)
				{	orNieuw[i] = or[i];
				}
				or = orNieuw;
				or[aantalActiviteiten-1] = new OpdrachtNrRij(aantalOpdrachten[aantalActiviteiten-1], orPosX,orPosY);
				or[aantalActiviteiten-1].addActionListener(this);
				if(editMode) or[activiteitNr].setSize(or[activiteitNr].getSize().width,25);
				or[aantalActiviteiten-1].setBackground(getBackground());
				or[aantalActiviteiten-1].setSelected(1);
				or[aantalActiviteiten-1].setVisible(false);
				add(or[aantalActiviteiten-1],0);
				
				remove(actKeuzePanel);
				actKeuzePanelY = orPosY-aantalActiviteiten*20-15;
				actKeuzePanel = new ActKeuzePanel(activiteitNamen,actKeuzePanelX,actKeuzePanelY,140,aantalActiviteiten*20, true);
				actKeuzePanel.addActionListener(this);
				actKeuzePanel.setBackground(getBackground());
				add(actKeuzePanel,0);
				
			}
			
		}
		else if(e.getSource() == opdrPositieKnop)
		{	if(e.getActionCommand().equals("min") && opdrachtNr>0)
			{	OpdrContainer oc = opdrContainers[activiteitNr][opdrachtNr];
				opdrContainers[activiteitNr][opdrachtNr] = opdrContainers[activiteitNr][opdrachtNr-1];
				opdrContainers[activiteitNr][opdrachtNr-1] = oc;
				opdrachtNr--;
				or[activiteitNr].setSelected(opdrachtNr+1);
				opdrPositieKnop.setLocation(orPosX+25*opdrachtNr,orPosY+30);
				
			}
			if(e.getActionCommand().equals("plus") && opdrachtNr<aantalOpdrachten[activiteitNr]-1)
			{	OpdrContainer oc = opdrContainers[activiteitNr][opdrachtNr];
				opdrContainers[activiteitNr][opdrachtNr] = opdrContainers[activiteitNr][opdrachtNr+1];
				opdrContainers[activiteitNr][opdrachtNr+1] = oc;
				opdrachtNr++;
				or[activiteitNr].setSelected(opdrachtNr+1);
				opdrPositieKnop.setLocation(orPosX+25*opdrachtNr,orPosY+30);
			}
		}
	}
}
