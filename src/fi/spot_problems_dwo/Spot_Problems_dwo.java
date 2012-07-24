package fi.spot_problems_dwo;

/*
	Spot Problems version 130701
*/

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;

import fi.spot_problems_dwo.wiskopdr.*;
import fi.beans.base64code.*;
import fi.beans.scorm.*;
import fi.beans.copyright.*;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

// main applet class
public class Spot_Problems_dwo extends WiskOpdr implements WiskOpdrApplet
{   
	protected static ResourceBundle rb;
	protected static String langArg;
	protected static Color bgColor = new Color(230,240,255);
	
	// levels
    public static final int MAXLEVELS = 3;
    // problem numbers
    // random
    public final int RANDOM = 0;
    // level 0
    public final int VNUMBERS = 1;
    public final int WNUMBERS = 2;
    public final int SQRNUMBERS = 3;
    public final int BOXNUMBERS = 4;    
    public final int PLUSNUMBERS = 5;    
    public final int TOWERNUMBERS = 6; 
    public final int FNUMBERS1 = 7; 
    public final int FLAPNUMBERS = 8; 
    public final int HNUMBERS1 = 9; 
    public final int HNUMBERS2 = 10; 
    public final int XNUMBERS1 = 11; 
    public final int XNUMBERS2 = 12;
    public final int LNUMBERS1 = 13; 
    public final int LNUMBERS2 = 14;
    public final int TABLENUMBERS = 15;

    // level 1
    public final int OBLNUMBERS = 101;
    public final int TRIANUMBERS1 = 102;
    public final int TRIANUMBERS2 = 103;
    public final int TRIANUMBERS3 = 104;    
    public final int FNUMBERS2 = 105;
    public final int FLIPNUMBERS = 106;
    public final int TILENUMBERS1 = 107;            
    public final int TILENUMBERS2 = 108;                
    public final int STAIRNUMBERS = 109;
    public final int ZNUMBERS = 110;    
    public final int INSECTNUMBERS = 111;            
    public final int SPIDERNUMBERS = 112;        
    
    // level 2
    public final int PENTANUMBERS = 201;
    public final int SPIRALNUMBERS = 202;
    public final int BLOCKNUMBERS1 = 203; // 2^n
    public final int TILENUMBERS3 = 204;            
    public final int TILENUMBERS4 = 205;
    public final int BLOCKNUMBERS2 = 206; // n^3
    public final int BLOCKNUMBERS3 = 207; // 2*n^3
    public final int TRIANUMBERS4 = 208; // 3^n
    public final int PIRAMIDNUMBERS = 209;        

    // number of problems implemented
    public static final int[] MAXPROBLEMS = {15, 12, 9};
    // problem names (for table lookup)
    public static final String[][] PROBLEMNAMES =
        { // level 0
         {"randomText", "vNumbersText", 
          "wNumbersText", "sqrNumbersText", 
          "boxNumbersText", "plusNumbersText",
          "towerNumbersText", "fNumbers1Text",
          "flapNumbersText", "hNumbers1Text",
          "hNumbers2Text", "xNumbers1Text",
          "xNumbers2Text", "lNumbers1Text",
          "lNumbers2Text", "tableNumbersText",
         },
         // level 1
         {"randomText", "oblNumbersText", 
          "triaNumbers1Text", "triaNumbers2Text", 
          "triaNumbers3Text", "fNumbers2Text", 
          "flipNumbersText", "tileNumbers1Text", 
          "tileNumbers2Text", "stairNumbersText", 
          "zNumbersText", "insectNumbersText", 
          "spiderNumbersText",
         },
         // level 2
         {"randomText", "pentaNumbersText", 
           "spiralNumbersText", "blockNumbers1Text",
           "tileNumbers3Text", "tileNumbers4Text",
           "blockNumbers2Text", "blockNumbers3Text", 
           "triaNumbers4Text", "piramidNumbersText",
         }  
        };    
    // number of current level (default)
    int level = 0;
    // number of current problem (default)   
    int problemNumber = 1; // V-numbers

    // number of questions per problem
    static final int MAXQUESTIONS = 6;
    // number of current question
    int questionNumber = 0;
    
    // scores, total, price
    int initScore = 4;
    int score = initScore;
    int total = 0;
    // initial price for showing next pattern
    int initPrice = 4;
    int price = initPrice;
    // last random number
    int lastRandom;
    // an answer
    int answer;
    // answer key
    char guessKey;
    // boolean answer
    boolean ynAnswer;
    // formula in postfix
    String postFixFormula;
    // parser 
    PostFixParser parser;
    // large number
    static double NOTDEFINED = 1e10d;
    // postfix stack
    DoubleStack stack = new DoubleStack();
    
    // GUI attributes
    // buffer Panel
    BufferPanel bufferPanel;
    // drawing Container in buffer panel
    DrawingContainer drawCon;

    // question label
    Label questionLabel;
    // user input
    LWTextField inputField;
    // correct label
    Label correctLabel;
    // label for parser messages
    Label parserLabel;
    
    // next question button
    Button nextQuestionButton;
    // score and total labels
    Label scoreLabel, totalLabel;
    // score and total field
    TextField scoreField, totalField;
    
    // next pattern button
    Button nextPatternButton;

    // choosing the level
    Label levelLabel;
    TextField levelField;
    LWArrowButton levelUp, levelDown;
    
    
    // choose problem label
    Label chooseProblemLabel;
    // problem choice
    Choice problemChoice;

    // choosing colors
    CheckboxGroup colorGroup;
    Checkbox[] colorBoxes;
    
    // other attributes
    // language table    
    static LookUpTable languageTable;

    // copyright button
    FIButton fiButton;
    // applet's init()
	
	public static void main(String[] args)    
	{	int width = 800;
        int height = 560;
		//ScormEditMainFrame mf = new ScormEditMainFrame(new WiskOpdr(),width, height);
		ScormMainFrame mf = new ScormMainFrame(new Spot_Problems_dwo(),width, height);
		mf.setTitle("WiskOpdr");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
		//mf.doLayout();
	}
	
	public Spot_Problems_dwo()
	{	
		langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.spot_problems_dwo.text.Text", language);	
	}
	
	public Spot_Problems_dwo(Locale language)
	{	
		langArg = language.getLanguage();
		rb = ResourceBundle.getBundle("fi.spot_problems_dwo.text.Text", language);	
	}
	
	public void init()
	{   
		super.init();

  		for (int i = 0; i<3; i++) 
		{	for (int j = 0; j<10; j++) 
			{	MyOpdrContainer moc = geefMyOpdrContainer(i,j);
				moc.setBoundsTitelLabel(0,0,0,0);
				moc.setBoundsAntwoordVak(380,350,380,110);
				moc.setBoundsTekstArea(10,30,230,300);
				moc.zetStappenAntwoordVak(false);
				moc.zetAntwoordScrollOptie(false);
				
			}
	    }
  	
	
		// basic applet parameters
		//{{INIT_CONTROLS
		setLayout(null);
		//setSize(712, 430);
		//setBackground(new Color(12632256));
		
		String kleurcode = getParameter("bgcolor");
		if (kleurcode != null) 
			bgColor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		setBackground(bgColor);
		
		//}}
		
        // get the language parameter (if any), default is "nl"    
        langArg = getParameter("language");
        // initiate the LookUpTable
        languageTable = new LookUpTable(langArg);

        String[] copyRight = 
        {   languageTable.lookUp("titelText"),
    		languageTable.lookUp("versionText"),
	    	languageTable.lookUp("authorText"),
    		languageTable.lookUp("programText"),
    		languageTable.lookUp("fiText"),
	    	"www.fi.uu.nl",
    	};	
        
        // get current font
		Font fo = new Font("SansSerif", Font.PLAIN,12);
		FontMetrics fm = getFontMetrics(fo);
		Font bfo = new Font(fo.getName(), Font.BOLD, fo.getSize());
		FontMetrics bfm = getFontMetrics(bfo);
		
        // graphical part
        // buffer panel
		bufferPanel = new BufferPanel();
		
		bufferPanel.setBackground(bgColor);
		// take width and height as multiples of 
		// GRIDSIZE
		bufferPanel.setBounds(250, 10, 528, 320);
		add(bufferPanel,0);
		// drawing container
		drawCon = new DrawingContainer(this);
		drawCon.setBackground(bgColor);
		bufferPanel.add(drawCon, BorderLayout.CENTER);
		// initialize, problemNumber should be set!
		drawCon.initialize();

	    // user actions, bottom
		int currentX = 10;
		int currentY = bufferPanel.getLocation().y +
                       bufferPanel.getSize().height + 10; 
        int width = 2 * bufferPanel.getSize().width / 3 - 30;               
		int height =  3 * fm.getHeight() / 2;		
		
		// question label
		questionLabel = new Label("", Label.RIGHT);
//questionLabel.setBackground(Color.yellow);		
   		questionLabel.setBounds(
   		    currentX, currentY, width, height);
   		currentX += width + 15;   		    
   		add(questionLabel);    
   		
   		// inputField
   		width = bufferPanel.getSize().width / 3;
   		inputField = new LWTextField("testing");
   		inputField.setFont(bfo);
   		inputField.setBounds(
   		    currentX, currentY, width, height + 2);
   		currentX += width + 20;   		       		    
        add(inputField);
        inputField.addKeyListener(new InputKL());
        
		// correct label
    	width = getSize().width - bufferPanel.getSize().width - 20;		
		correctLabel = new Label();
//correctLabel.setBackground(Color.yellow);		
   		correctLabel.setBounds(
   		    currentX, currentY, width, height);
   		//add(correctLabel);    
   		currentY += height + 10;   		       		            
        
        // parser label   		
   		currentX = inputField.getLocation().x;
   		width = getSize().width - inputField.getLocation().x - 40;
		parserLabel = new Label();
//parserLabel.setBackground(Color.yellow);		
   		parserLabel.setBounds(
   		    currentX, currentY, width, height);
   		//add(parserLabel);    
   		
   		
        // color checkboxes		
		width = 18;
		currentX = 300;
		currentY = questionLabel.getLocation().y + 
		           questionLabel.getSize().height + 10;
		colorGroup = new CheckboxGroup();
		colorBoxes = new Checkbox[DrawingContainer.MAXCOLORS];
		for (int i = 0; i < colorBoxes.length; i++)
		{   // first box on	    
		    if (i == 0)
    		    colorBoxes[i] = new Checkbox("", colorGroup, true);
    		else
      		    colorBoxes[i] = new Checkbox("", colorGroup, false);    		
       		colorBoxes[i].setBounds(
       		    currentX, currentY, 
                width, height);
      		currentX += width + 5;   		        
    		colorBoxes[i].setBackground(DrawingContainer.spotColors[i]);      		
            //add(colorBoxes[i],0);    		           
            colorBoxes[i].addItemListener(new ColorIL());
        } // for   
   		
		// user actions right
    	currentX = bufferPanel.getLocation().x +
    	               bufferPanel.getSize().width + 20;
    	currentY = 10;
    	width = (getSize().width - bufferPanel.getSize().width - 40) / 5;
    	currentX += width;
    	
    	// level label
    	levelLabel = new Label(languageTable.lookUp("levelText"), Label.CENTER);
// levelLabel.setBackground(Color.yellow);    	    	
   		levelLabel.setBounds(
   		    currentX, currentY, 3 * width, height);
   		//add(levelLabel);
   		currentY += height + 15; 
    	
//    	width = (getSize().width - bufferPanel.getSize().width - 40) / 5;    	
//    	currentX += width;
    	levelDown = new LWArrowButton(2);
   		levelDown.setBounds(
   		    currentX, currentY, width, height);
   		//add(levelDown);
   		levelDown.setEnabled(false);
   		levelDown.addMouseListener(new LevelDownML());
   		currentX += width; 

    	levelField = new TextField("" + (level + 1));
   		levelField.setBounds(
   		    currentX, currentY, width, height);
   		//add(levelField);
   		levelField.setEditable(false);
   		levelField.setBackground(Color.white);
        levelField.setFont(bfo);
   		currentX += width; 
    	levelUp = new LWArrowButton(0);
   		levelUp.setBounds(
   		    currentX, currentY, width, height);
   		//add(levelUp);
   		levelUp.addMouseListener(new LevelUpML());   		

		//fiButton = new FIButton(
		//    languageTable.lookUp("infoText"), copyRight);
    	//fiButton.setBounds(getSize().width - 30,
    	 //                  parserLabel.getLocation().y, 
    	 //                  16, 24);
    	//add(fiButton); 
    	
    	//Fi-logo, copyright
		fi.beans.copyright.FIButton fiButton = new fi.beans.copyright.FIButton("Stippelalgebra opdrachten",new String[]
			{	"versie-info:20071229",
				"auteur:Martin Kindt",
				"programmeur:Huub Nilwik, Peter Boon",
				"Freudenthal Instituut",
				"www.fi.uu.nl",
				""
			});
		Panel p = new Panel();
		p.setLayout(null);
		p.setBounds(0,0,20,30);
		fiButton.setBounds(0,0,20,30);
		p.add(fiButton);
		add(p,0);                  

    	currentX = bufferPanel.getLocation().x +
    	               bufferPanel.getSize().width + 20;
   		currentY += height + 20;     	
    	width = getSize().width - bufferPanel.getSize().width - 40;    	

    	// choose problem label
    	chooseProblemLabel = new Label(languageTable.lookUp("chooseProblemText"));
//chooseProblemLabel.setBackground(Color.yellow);    	
   		chooseProblemLabel.setBounds(
   		    currentX, currentY, width, height);
   		add(chooseProblemLabel);
   		currentY += height + 15; 
    	
    	// problem choice
    	problemChoice = new Choice();
   		problemChoice.setBounds(
   		    currentX, currentY, width, height);
   		//add(problemChoice);
   		for (int i = 0; i <= MAXPROBLEMS[level]; i++)
   		    problemChoice.addItem(
   		        languageTable.lookUp(PROBLEMNAMES[level][i]));
   		problemChoice.addItemListener(new ChoiceIL());        
   		problemChoice.select(problemNumber);
   		currentY += height + 40; 
    	
        // show next pattern button		
        nextPatternButton = new Button(languageTable.lookUp("nextPatternText"));
   		nextPatternButton.setBounds(
   		    currentX, currentY, width, height);
   		//add(nextPatternButton);
   		nextPatternButton.addActionListener(new NextPatternAL());
   		currentY += height + 20; 
   		
        // score label and field
        width = width / 2 - 20;
        scoreLabel = new Label(languageTable.lookUp("scoreText"), Label.RIGHT);
//scoreLabel.setBackground(Color.yellow);
        scoreLabel.setBounds(
   		    currentX, currentY, width, height);
   		//add(scoreLabel);
   		currentX += width + 20;
   		
        scoreField = new TextField("" + initScore);
        scoreField.setEditable(false);
        scoreField.setBackground(Color.white);        
        scoreField.setFont(bfo);
        scoreField.setBounds(
   		    currentX, currentY, width, height);
   		//add(scoreField);
   		currentY += height + 20;    		
   		
        // total label and field
    	currentX = bufferPanel.getLocation().x +
    	               bufferPanel.getSize().width + 20;
        totalLabel = new Label(languageTable.lookUp("totalText"), Label.RIGHT);
// totalLabel.setBackground(Color.yellow);
        totalLabel.setBounds(
   		    currentX, currentY, width, height);
   		//add(totalLabel);
   		currentX += width + 20;   		
   		
        totalField = new TextField("" + total);
        totalField.setEditable(false);
        totalField.setBackground(Color.white);                
        totalField.setFont(bfo);
        totalField.setBounds(
   		    currentX, currentY, width, height);
   		//add(totalField);
   		currentY += height + 20;    		   		
   		
        // show next question button		
    	currentX = bufferPanel.getLocation().x +
    	               bufferPanel.getSize().width + 20;
        width = width * 2 + 40;
        nextQuestionButton = new Button(languageTable.lookUp("nextQuestionText"));
   		nextQuestionButton.setBounds(
   		    currentX, currentY, width, height);
   		//add(nextQuestionButton);
   		nextQuestionButton.addActionListener(new NextQuestionAL());   		
   		currentY += height + 20; 

   		parser = new PostFixParser(this);
   		
  		setQuestion(0);
  		
  		this.setBounds(0,0,getSize().width,getSize().height);
	} // init
    
    public Hashtable makeDefaultParamValues(int variant)
	{	
		Hashtable h = new Hashtable();
		h.put("language","nl");
		h.put("bgcolor","#DDEEFF");
		
		Hashtable defaultEditModeLaunchData = new Hashtable();
		defaultEditModeLaunchData.put("titel","Titel");
		defaultEditModeLaunchData.put("tekst","Geef bij elke reeks stippelpatronen een formule waarmee je voor elke patroonnummer n het aantal stippen kunt berekenen, bijvoorbeeld:\n\n    $fAantal=3n+2@\n\nAls je wilt kun je extra stippen en patronen tekenen met de muis. Dat kan helpen bij het vinden van de formule. ");
		defaultEditModeLaunchData.put("randVarString","");
		defaultEditModeLaunchData.put("antwoordString","$f3x@");
		defaultEditModeLaunchData.put("herleiding",new Boolean(false));
		defaultEditModeLaunchData.put("exact",new Boolean(false));
		defaultEditModeLaunchData.put("soortHerleiding",new Integer(0));
		defaultEditModeLaunchData.put("puntenGelijkwaardig",new Integer(10));
		defaultEditModeLaunchData.put("puntenHerleiding",new Integer(0));
		defaultEditModeLaunchData.put("puntenExact",new Integer(0));
		
		String[][] defaultEditModeState = new String[3][10];
		
		String[][] antwoorden = 
		{
			{"2n+1",		"4n+1",			"n^2",		"4n",		"4n+1",			"3n+1",		"4n+2",		"4n",	"4n+3",				"5n+2"},	
			{"n(n+1)",		"n(n+1)/2",		"n(n+1)/2",	"n^2",		"6n^2",			"n^2+4",	"2n^2",		"8n+1",	"n^2",				"3(n+2)"} ,
			{"(3n^2-n)/2",	"(n^2+n+2)/2",	"2^n",		"4n^2+2",	"n^2+(n-1)^2",	"n^3",		"2n^3",		"3^n",	"(2n^3+3n^2+n)/6",	"(2n^3+3n^2+n)/6"}
		};
		
		
		
		for (int i = 0; i<3; i++) 
		{	for (int j = 0; j<10; j++) 
			{	defaultEditModeLaunchData.put("antwoordString","$fAantal =" + antwoorden[i][j] + "@");
				defaultEditModeState[i][j] = StringCodeObject.encodeObjectToString(defaultEditModeLaunchData);
			}
	    }
		
		
		if(variant==0)
		{	h.put("aantalActiviteiten","3");
 
 			h.put("activiteit_1","Niveau 1");
			h.put("aantalOpdrachten_1","10");
			for (int i = 0; i<10; i++) 
			{	h.put("opdracht_1_"+(i+1),defaultEditModeState[0][i]);
		    }
			h.put("activiteit_2","Niveau 2");
			h.put("aantalOpdrachten_2","10");
			for (int i = 0; i<10; i++) 
			{	h.put("opdracht_2_"+(i+1),defaultEditModeState[1][i]);
		    }
		    h.put("activiteit_3","Niveau 3");
			h.put("aantalOpdrachten_3","10");
			for (int i = 0; i<10; i++) 
			{	h.put("opdracht_3_"+(i+1),defaultEditModeState[2][i]);
		    }
		}
		
		return h;
	}
	
	public boolean hasEditMode()
	{	return false;
	}
	
	public ScormEditComponentIF getEditComponent(Hashtable launchData)
	{	return null;
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("select"))
		{	int actNr = geefActiviteitNr();
			int opdrNr = geefOpdrachtNr();
			drawCon.initProblem(100*actNr+opdrNr+1);
			if(this.geefMyOpdrContainer(actNr,opdrNr).isCorrect()) drawCon.showAllPatterns();
		}
		else if(e.getActionCommand().equals("correct"))
		{	drawCon.showAllPatterns();
		}
	}
   
	//{{DECLARE_CONTROLS
	//}}

    // generate a random integer between min and max
    public int randomInteger(int min, int max)
    {   double num = min + Math.random() * (max - min);
        // cast long to int
        return (int) Math.round(num);
    }
    
    // generate a random integer between min and max
    // excluding old
    public int randomInteger(int min, int max, int old)
    {   int result = 0;
        do
        {   double num = min + Math.random() * (max - min);
            // cast long to int
            result = (int) Math.round(num);
        }
        while (result == old);
        return result;
    }
    
	
	public int getSpotsIn(int n)
	{   int result = 0;
	    switch (problemNumber)
	    {   case VNUMBERS: 
	        {   result = 1 + 2 * n;
	        }
	        break;
            case WNUMBERS: 
            case PLUSNUMBERS:
            case XNUMBERS1:
	        {   result = 1 + 4 * n;
	        }
	        break;	        
            case SQRNUMBERS: 
            case TRIANUMBERS3: 
            case STAIRNUMBERS:
	        {   result = n * n;
	        }
	        break;	      
            case OBLNUMBERS: 
	        {   result = n * (n + 1);
	        }
	        break;	      
            case TRIANUMBERS1: 
            case TRIANUMBERS2: 
	        {   result = n * (n + 1) / 2;
	        }
	        break;	      
            case PENTANUMBERS: 
	        {   result = (3 * n * n - n) / 2;
	        }
	        break;
            case BOXNUMBERS: 
            case FLAPNUMBERS:
	        {   result = 4 * n;
	        }
	        break;
            case SPIRALNUMBERS: 
	        {   result = (n * n + n + 2) / 2;
	        }
	        break;
            case TOWERNUMBERS: 
	        {   result = 3 * n + 1;
	        }
	        break;
            case BLOCKNUMBERS1: 
	        {   result = (int) Math.pow(2, n);
	        }
	        break;
            case FNUMBERS1: 
	        {   result = 4 * n + 2;
	        }
	        break;
            case FNUMBERS2: 
	        {   result = 6 * n * n;
	        }
	        break;
            case FLIPNUMBERS: 
	        {   result = 4 + n * n;
	        }
	        break;
            case TILENUMBERS1: 
	        {   result = 2 * n * n;
	        }
	        break;
            case TILENUMBERS2: 
	        {   result = 8 * n + 1;
	        }
	        break;
            case TILENUMBERS3: 
	        {   result = 4 * n * n + 2;
	        }
	        break;
            case TILENUMBERS4: 
	        {   result = 2 * n * n - 2 * n + 1;
	        }
	        break;
            case HNUMBERS1: 
	        {   result = 4 * n + 3;
	        }
	        break;
            case HNUMBERS2: 
	        {   result = 5 * n + 2;
	        }
	        break;
            case LNUMBERS1: 
            case TABLENUMBERS:
	        {   result = 2 * n + 2;
	        }
	        break;
            case LNUMBERS2: 
	        {   result = 4 * n * n;
	        }
	        break;
            case XNUMBERS2: 
	        {   result = 4 * n - 3;
	        }
	        break;
            case ZNUMBERS: 
	        {   result = 3 * n + 6;
	        }
	        break;
            case INSECTNUMBERS: 
	        {   result = 6 * n + 3;
	        }
	        break;
            case SPIDERNUMBERS: 
	        {   result = 8 * n + 3;
	        }
	        break;
            case BLOCKNUMBERS2: 
	        {   result = n * n * n;
	        }
	        break;
            case BLOCKNUMBERS3: 
	        {   result = 2 * n * n * n;
	        }
	        break;
            case TRIANUMBERS4: 
	        {   result = (int) Math.pow(3, n);
	        }
	        break;
            case PIRAMIDNUMBERS: 
	        {   result = (2*n*n*n + 3*n*n + n) / 6;
	        }
	        break;
	        
	        default: // nothing
	    }
	    return result;
	}    
	
	public boolean exists(int n)
	{   int counter = 0;
	    int spots = getSpotsIn(counter);
	    while (spots < n)
	    {   counter++;
	        spots = getSpotsIn(counter);
	    }
	    return (spots == n);
	}    
	
	public void setQuestion(int num)
	{   questionNumber = num;
	    if (questionNumber == 0)
	    {   price = initPrice;
	        nextPatternButton.setLabel(
	            languageTable.lookUp("nextPatternText") + 
    	        " (-" + price + ")");
    	    score = initScore;
	        scoreField.setText("" + score);
    	}        
	    inputField.setText("");
	    correctLabel.setText("");
	    switch (questionNumber)
	    {   case 0: // how many in n = 4
	        {   answer = 4; // randomInteger(5, 10);
	            lastRandom = answer;
	            questionLabel.setText(
	                languageTable.lookUp("howManyText") + 
	                answer + 
    	            languageTable.lookUp("howMany2Text"));
	            answer = getSpotsIn(answer);    
	                
	        }
	        break;
            case 1: // how many in n = 5
	        {   answer = 5; // randomInteger(5, 10, lastRandom);
	            questionLabel.setText(
	                languageTable.lookUp("howManyText") + 
	                answer + 
      	            languageTable.lookUp("howMany2Text"));
	            answer = getSpotsIn(answer);    	                
                if (drawCon.patternsShown == 2)
                {   drawCon.showNextPattern();
                    if (drawCon.patternsShown == drawCon.maxPatterns - 1)
                        nextPatternButton.setEnabled(false);
                }
	        }
	        break;	        
            case 2: // how many in n = 10
	        {   answer = 10; // randomInteger(5, 10, lastRandom);
	            questionLabel.setText(
	                languageTable.lookUp("howManyText") + 
	                answer + 
    	            languageTable.lookUp("howMany2Text"));	                
	            answer = getSpotsIn(answer);    	                
	        }
	        break;	        
            case 3: // exists - 1
	        {   answer = getSpotsIn(8) + randomInteger(- 4, 4);
	            lastRandom = answer;
	            questionLabel.setText(
	                languageTable.lookUp("existsText") + 
	                answer + 
	                languageTable.lookUp("spotsText") +
                    languageTable.lookUp("exists2Text") + 	                
	                languageTable.lookUp("yesNoText")); // +
//	                "?");
	            ynAnswer = exists(answer);    
	                
	        }
	        break;	        
            case 4: // exists - 2
	        {   answer = getSpotsIn(12) + randomInteger(- 6, 6);
	            questionLabel.setText(
	                languageTable.lookUp("existsText") + 
	                answer + 
	                languageTable.lookUp("spotsText") +
	                languageTable.lookUp("exists2Text") + 
	                languageTable.lookUp("yesNoText")); // +
//	                "?");
	            ynAnswer = exists(answer);    	                
	        }
	        break;	        
            case 5: // formula
	        {   questionLabel.setText(
	                languageTable.lookUp("formulaText"));

	        }
	        break;	        
	        default: // nothing
	    }    
	}    

    // reading in a number
    public int findNumber(String s)
    {   int result = - 1;
        boolean error = false;
        try
        {   result = Integer.parseInt(s);
        }
        catch (NumberFormatException ne)
        {   error = true;
//            correctLabel.setText(languageTable.lookUp("notANumberText"));
        }    
        if (error || (result <= 0))
            correctLabel.setText(languageTable.lookUp("notANumberText"));        
        return result;
    }    
	
	public char findKey(String s)
	{   char result = '%'; // dummy
	    if (s.length() > 0)
	        result = s.charAt(0);
	    return result;    
	}    
	
	public void getAnswer()
	{   String input = inputField.getText();
	    if (input.equals(""))
	    {   correctLabel.setText(languageTable.lookUp("nothingText"));
            return;
	    }    
	    switch (questionNumber)
	    {   case 0: // how many in #4
	        case 1: // how many in #5
	        case 2: // how many in #10
	        {   int guess = findNumber(input);
	            if (guess >= 0)
	            {   boolean correct = (guess == answer);
	                if (correct)
	                {   total += score;
	                    totalField.setText("" + total);
	                    score = 0;
                        scoreField.setText("" + score);	                    
	                    correctLabel.setText(
	                        languageTable.lookUp("correctText"));
if ((questionNumber == 0) && (drawCon.patternsShown == 2))
{   drawCon.showNextPattern();
    if (drawCon.patternsShown == drawCon.maxPatterns - 1)
        nextPatternButton.setEnabled(false);
    
}
if (questionNumber == 1)
{   if (drawCon.patternsShown == 3)
        drawCon.showNextPattern();    
    if (drawCon.patternsShown == drawCon.maxPatterns - 1)
        nextPatternButton.setEnabled(false);
}    
	                }
	                else
	                {   correctLabel.setText(
	                        languageTable.lookUp("notCorrectText"));
    	                if (score > 0)
	                    {   score--;
	                        scoreField.setText("" + score);
	                    }    
	                }
	            }    
	        }    
	        break;
            case 3: // exists - 1 
            case 4: // exists - 2
	        {    guessKey = findKey(input);
	             String language = languageTable.lookUp("languageText");
	             boolean correct = false;
	             boolean wrongKey = false;
	             if (language.equals("dutch"))
                 {    if ((guessKey == 'j') ||
                          (guessKey == 'J'))  
                          correct = (ynAnswer == true);           
                      else if ((guessKey == 'n') ||
                               (guessKey == 'N')) 
                          correct = (ynAnswer == false);                                   
                      else
                      {   wrongKey = true;
                          correctLabel.setText(
                            languageTable.lookUp("typeText") + "\"j\"" + 
                            languageTable.lookUp("orText") + "\"n\"");
                          
                      }
                 }   
                 else if (language.equals("english"))
                 {    if ((guessKey == 'y') ||
                          (guessKey == 'Y'))  
                          correct = (ynAnswer == true);           
                      else if ((guessKey == 'n') ||
                               (guessKey == 'N')) 
                          correct = (ynAnswer == false);                                   
                      else
                      {   wrongKey = true;
                          correctLabel.setText(
                            languageTable.lookUp("typeText") + "\"y\"" + 
                            languageTable.lookUp("orText") + "\"n\"");
                      }                        
                 }   
                 else if (language.equals("spanish") || 
                          language.equals("portugese"))
                 {    if ((guessKey == 's') ||
                          (guessKey == 'S'))  
                          correct = (ynAnswer == true);           
                      else if ((guessKey == 'n') ||
                               (guessKey == 'N')) 
                          correct = (ynAnswer == false);                                   
                      else
                      {   wrongKey = true;
                          correctLabel.setText(
                            languageTable.lookUp("typeText") + "\"s\"" + 
                            languageTable.lookUp("orText") + "\"n\"");
                      }      
                      
                 }   
                 else if (language.equals("japanese"))
                 {    if ((guessKey == 'y') ||
                          (guessKey == 'Y'))  
                          correct = (ynAnswer == true);           
                      else if ((guessKey == 'n') ||
                               (guessKey == 'N')) 
                          correct = (ynAnswer == false);                                   
                      else
                      {   wrongKey = true;
                          correctLabel.setText(
                            languageTable.lookUp("typeText") + "\"y\"" + 
                            languageTable.lookUp("orText") + "\"n\"");
                      }      

                 }   
                 
                 if (!wrongKey)
                 {   if (correct)
	                 {   total += score;
	                     totalField.setText("" + total);
	                     score = 0;
                         scoreField.setText("" + score);	                    
	                     correctLabel.setText(
	                         languageTable.lookUp("correctText"));
                     }
	                else
	                {   correctLabel.setText(
	                        languageTable.lookUp("notCorrectText"));
    	                score = 0;
	                    scoreField.setText("" + score);
	                }
                 }   
	        }    
	        break;	        
	        case 5: // formula
	        {   postFixFormula = parser.parseString(input);
	            // string is a legal formula
	            if (postFixFormula != null)
	            {   
// correctLabel.setText(postFixFormula);	                    
//*	                
	                boolean correct = isCorrectFormula(postFixFormula);
	                if (correct)
	                {   total += score;
	                    totalField.setText("" + total);
	                    score = 0;
                        scoreField.setText("" + score);	                    
	                    correctLabel.setText(
	                        languageTable.lookUp("correctText"));
	                    drawCon.showAllPatterns();    
	                }
	                else
	                {   correctLabel.setText(
	                        languageTable.lookUp("notCorrectText"));
    	                if (score > 0)
	                    {   score--;
	                        scoreField.setText("" + score);
	                    }    
	                }
//*/	                
	            }    
	        
	        }
	        break;
	        default: // nothing
	    }    
	}    
	
	public boolean isCorrectFormula(String postFix)
	{   double d2 = evaluatePostFix(postFix, 2);
	    double tmp = Math.round(d2);
	    if (d2 != tmp)
	        return false;
        double d3 = evaluatePostFix(postFix, 3);
	    tmp = Math.round(d3);
	    if (d3 != tmp)
	        return false;	        
        double d4 = evaluatePostFix(postFix, 4);
	    tmp = Math.round(d4);
	    if (d4 != tmp)
	        return false;	        
        double d5 = evaluatePostFix(postFix, 5);
	    tmp = Math.round(d5);
	    if (d5 != tmp)
	        return false;	        
        double d6 = evaluatePostFix(postFix, 6);
	    tmp = Math.round(d6);
	    if (d6 != tmp)
	        return false;	        
	    return (getSpotsIn(2) == d2) &&
	           (getSpotsIn(3) == d3) &&
	           (getSpotsIn(4) == d4) &&
	           (getSpotsIn(5) == d5) &&
	           (getSpotsIn(6) == d6);
// uitbreiden	           
	}    
	
    public double evaluatePostFix(String s, double n)
    {   
        double d = NOTDEFINED;
        stack.clear();
        String block = null;
        boolean lastBlock = false;
        while ((s.length() > 0) && (s.charAt(0) == '['))
        {   // remove first [
            s = s.substring(1);
            // check for more [
            int next = s.indexOf('[');
            // consume block
            if (next >= 0)
            {   block = s.substring(0, next);
                s = s.substring(next);
            }
            else
            {   block = s;
                s = "";
                lastBlock = true;
            }    
            // block now ends with ]    
            // consume ]
            block = block.substring(0, block.length() - 1);

            if (block.equals("+"))
            {   double d1 = stack.pop();
                double d2 = stack.pop();
                if (lastBlock)
                    d = d1 + d2;
                else    
                    stack.push(d1 + d2);
            }    
            else if (block.equals("-"))
            {   double d1 = stack.pop();
                double d2 = stack.pop();
                if (lastBlock)
                    d = d2 - d1;
                else     
                   stack.push(d2 - d1);
            }    
            else if (block.equals("*"))
            {   double d1 = stack.pop();
                double d2 = stack.pop();
                if (lastBlock)
                    d = d1 * d2;
                else    
                    stack.push(d1 * d2);
            }    
            else if (block.equals("/"))
            {   double d1 = stack.pop();
                double d2 = stack.pop();
                // division by "0"
                if (Math.abs(d1) < 1e-10d)
                    return NOTDEFINED;
                if (lastBlock)
                    d = d2 / d1;
                else    
                    stack.push(d2 / d1);
            }    
            else if (block.equals("^"))
            {   double d1 = stack.pop(); //b
                double d2 = stack.pop(); //a
                // division by "0"
                if ((d2 == 0.0) && (d1 < 0))
                    return NOTDEFINED;
                double tmp = Math.round(d1);
                if ((d2 <= 0.0) && (tmp != d1))
                    return NOTDEFINED;
                if (lastBlock)
                    d = Math.pow(d2, d1);
                else    
                    stack.push(Math.pow(d2, d1));
            }    
            else if (block.equals("n"))
            {   if (lastBlock)
                    d = n;
                else    
                    stack.push(n);
            }
            else
            {   double num = Double.valueOf(block).doubleValue();
                if (lastBlock)
                    d = n;
                else    
                    stack.push(num);
            }    
        }
        return d;   
    }    
	
	
	
    // inner classes for handling GUI events
    
    class LevelDownML extends MouseAdapter
    {   public void mousePressed(MouseEvent e)
        {   if (level > 0)
            {   level--; 
                levelField.setText("" + (level + 1));
                problemChoice.removeAll();
          	    for (int i = 0; i <= MAXPROBLEMS[level]; i++)
       		        problemChoice.addItem(
   	    	            languageTable.lookUp(PROBLEMNAMES[level][i]));
        		problemChoice.select(1);
       		    problemNumber = level * 100 + 1;
                drawCon.initProblem(problemNumber);
                nextPatternButton.setEnabled(true);
                nextQuestionButton.setEnabled(true);
                setQuestion(0);
                total = 0;
                totalField.setText("" + total);
       		  
       		    levelUp.setEnabled(true);
       		    if (level == 0)
       		       levelDown.setEnabled(false);
            }  
        }    
    }
    
    class LevelUpML extends MouseAdapter
    {   public void mousePressed(MouseEvent e)
        {   if (level < (MAXLEVELS - 1))
            {   level++; 
                levelField.setText("" + (level + 1));
                problemChoice.removeAll();                
          	    for (int i = 0; i <= MAXPROBLEMS[level]; i++)
   		        problemChoice.addItem(
   		            languageTable.lookUp(PROBLEMNAMES[level][i]));
        		problemChoice.select(1);
       		    problemNumber = level * 100 + 1;
                drawCon.initProblem(problemNumber);
                nextPatternButton.setEnabled(true);
                nextQuestionButton.setEnabled(true);
                setQuestion(0);
                total = 0;
                totalField.setText("" + total);
       		  
       		    levelDown.setEnabled(true);
       		    if (level == (MAXLEVELS - 1))
       		       levelUp.setEnabled(false);
            }  
        }    
    }
    
    
    
    // item listener for problem choice
	class ChoiceIL implements ItemListener
	{   public void itemStateChanged(ItemEvent e)
        {   int choosen = problemChoice.getSelectedIndex();
            if (choosen == RANDOM)
                problemNumber = level * 100 + randomInteger(1, MAXPROBLEMS[level]);
            else    
                problemNumber = level * 100 + choosen;
            drawCon.initProblem(problemNumber);
            nextPatternButton.setEnabled(true);
            nextQuestionButton.setEnabled(true);
            setQuestion(0);
            total = 0;
            totalField.setText("" + total);
        }    
	}    

	// next pattern button
	class NextPatternAL implements ActionListener
	{   public void actionPerformed(ActionEvent e)
	    {   total -= price;
	        totalField.setText("" + total);
	        drawCon.showNextPattern();
	        if (drawCon.patternsShown == 
	            (drawCon.maxPatterns - 1))
	        {   nextPatternButton.setEnabled(false);
	        }
	        else
	        {   price++;
           	    nextPatternButton.setLabel(
        	        languageTable.lookUp("nextPatternText") + 
	                " (-" + price + ")");
            }	            
	    }
	}    

	// next question button
	class NextQuestionAL implements ActionListener
	{   public void actionPerformed(ActionEvent e)
	    {   questionNumber++;
	        if (questionNumber < MAXQUESTIONS)
	        {   setQuestion(questionNumber);
	            score = initScore;
	            if ((questionNumber >= 2) &&
	                (questionNumber <= 4))
	                score++;
	            else if (questionNumber > 4)    
	                score += 2;
    	        scoreField.setText("" + score);	        
	        }    
	        if (questionNumber == 
	            (MAXQUESTIONS - 1))
	        {   nextQuestionButton.setEnabled(false);
	        }
	    }
	}    
	
	// inputfield
	class InputKL extends KeyAdapter
	{   public void keyPressed(KeyEvent e)
	    {   nextQuestionButton.setEnabled(false);
	        correctLabel.setText(languageTable.lookUp("enterText"));
            parserLabel.setText("");
	        int kc = e.getKeyCode();
	        if (kc == KeyEvent.VK_ENTER)
    	    {   getAnswer();
    	        if (questionNumber < MAXQUESTIONS - 1)
                    nextQuestionButton.setEnabled(true);    	    
    	    }
	    }
	}    
	// item listener for color group
	class ColorIL implements ItemListener
	{   public void itemStateChanged(ItemEvent e)
        {   Checkbox choosen = colorGroup.getSelectedCheckbox();
            // find index
            int colorNum = 0;
            for (int i = 0; i < colorBoxes.length; i++)
            {   if (choosen == colorBoxes[i])
                    colorNum = i;
            }
            drawCon.drawColor = drawCon.spotColors[colorNum];
        }    
	}    


	public InteractiePanel getInteractiePanel()
	{	
		return new SPInteractiePanel();
	}

} // class Spot_Problems


class DoubleStack
{   Vector stack;
    public DoubleStack()
    {   stack = new Vector(100);
    }
    
    public void push(double d)
    {   stack.addElement(new Double(d));
    }
    public double pop()
    {   double result = Spot_Problems_dwo.NOTDEFINED;
        if (stack.size() > 0)
        {   Double D = (Double) stack.elementAt(stack.size() - 1);
            stack.removeElementAt(stack.size() - 1);
            result = D.doubleValue();
        }
        return result;
    }    
    public void clear()
    {   stack.removeAllElements();
    }    
}    


// a class representing a lookup table for internationalization
// the main applet class creates one instance, which takes care 
// of filling the static HashTable table with the keys and the 
// desired language. The static method lookUp(...) can then be 
// accessed throughout the program via LookUpTable.lookUp(...)
class LookUpTable
{   // language table
    private static String[][] contents =
    //  keys, dutch, english, spanish, japanese   etc.
        
        
      {
       {"infoText",  "info",
                     "info",
                     "info",                     
                     "info",                                          
        			 "\u60C5\u5831"},
        
       {"titelText", "Stippelproblemen",
                     "Spot problems",
                     "Problemas con puntos",
                     "Problemas com pontos",                     
        "\u70B9\u306E\u500B\u6570\u306E\u554F\u984C"},
       {"versionText", "versie 20020702",
                       "version 20020702",
                       "versi\u00F3n 20020702",
                       "vers\u00E3o 20020702",                       
                       "20020702"},
//        "\u0031\u0035\u0031\u0030\u0030\u0031"},
       {"programText", "programmeur: Huub Nilwik",
                       "programmer: Huub Nilwik",
                       "programador: Huub Nilwik",
                       "programador: Huub Nilwik",                       
                       "programmer: Huub Nilwik"},                       
//        "\u0048\u0075\u0075\u0062\u0020\u004E\u0069\u006C\u0077\u0069\u006B"},
       {"authorText",  "auteur: Martin Kindt",
                       "author: Martin Kindt",
                       "autor: Martin Kindt", 
                       "autor: Martin Kindt",                       
                       "author: Martin Kindt"},                       
//        "\u004D\u0061\u0072\u0074\u0069\u006E\u0020\u004B\u0069\u006E\u0064\u0074"},
       {"fiText",    "FREUDENTHAL INSTITUUT",
                     "FREUDENTHAL INSTITUTE",
                     "INSTITUTO FREUDENTHAL",
                     "INSTITUTO FREUDENTHAL",                     
        "\u0046\u0052\u0045\u0055\u0044\u0045\u004E\u0054\u0048\u0041\u004C\u0020\u0049\u004E\u0053\u0054\u0049\u0054\u0055\u0054\u0045"},
        
       {"randomText",    "random keuze",
                         "random choice",
                         "elecci\u00F3n arbitraria",                         
                         "escolha arbitr\u00E1ria",                         
        "\uFF97\uFF9D\uFF80\uFF9E\uFF91\u306B\u9078\u3076"},
       {"vNumbersText",    "V-getallen",
                           "V numbers",
                           "n\u00FAmeros V", 
                           "n\u00FAmeros V",                            
        "\u0056\u5B57\u578B\u306E\u70B9\u306E\u6570"},
       {"wNumbersText",    "W-getallen",
                           "W numbers",
                           "n\u00FAmeros W",
                           "n\u00FAmeros W",                           
        "\u0057\u5B57\u578B\u306E\u70B9\u306E\u6570"},
        
       {"sqrNumbersText",    "vierkants-getallen",
                             "square numbers",
                             "n\u00FAmeros cuadrados",
                             "n\u00FAmeros quadrados",                             
        "\u56DB\u89D2\u6570"},
       {"oblNumbersText",    "rechthoeks-getallen",
                             "oblong numbers",
                             "n\u00FAmeros rectangulares",
                             "n\u00FAmeros rectangulares",                             
        "\u9577\u65B9\u5F62\u306E\u70B9\u306E\u6570"},
       {"triaNumbers1Text",    "driehoeks-getallen 1",
                               "triangular numbers 1",
                               "n\u00FAmeros triangulares 1",
                               "n\u00FAmeros triangulares 1",                               
        "\u4E09\u89D2\u6570\u0031"},
       {"triaNumbers2Text",   "driehoeks-getallen 2",
                               "triangular numbers 2",
                               "n\u00FAmeros triangulares 2",
                               "n\u00FAmeros triangulares 2",                               
        "\u4E09\u89D2\u6570\u0032"},
       {"triaNumbers3Text",   "driehoeks-getallen 3",
                               "triangular numbers 3",
                               "n\u00FAmeros triangulares 3", 
                               "n\u00FAmeros triangulares 3",                                
        "\u4E09\u89D2\u6570\u0033"},
        
       {"pentaNumbersText",    "vijfhoeks-getallen",
                               "pentagonal numbers",
                               "n\u00FAmeros pentagonales",
                               "n\u00FAmeros pentagonais",                               
        "\u4E94\u89D2\u5F62\u306E\u70B9\u306E\u6570"},
       {"boxNumbersText",    "doos-getallen",
                             "box numbers",
                             "n\u00FAmeros de caja",
                             "n\u00FAmeros caixa",                             
        "\u6B63\u65B9\u5F62\u306E\u70B9\u306E\u6570"},
       {"plusNumbersText",    "plus-getallen",
                              "plus numbers",
                              "n\u00FAmeros plus",
                              "n\u00FAmeros cruzados",                              
        "\u5341\u5B57\u578B\u306E\u70B9\u306E\u6570"},
       {"spiralNumbersText",    "spiraal-getallen",
                                "spiral numbers",
                                "n\u00FAmeros de espiral",
                                "n\u00FAmeros espiral",                                
        "\u6E26\u5DFB\u304D\u578B\u306E\u70B9\u306E\u6570"},
       {"towerNumbersText",    "toren-getallen",
                               "tower numbers",
                               "n\u00FAmeros de pila",
                               "n\u00FAmeros torre",                               
        "\uFF80\uFF9C\u002D\u578B\u306E\u70B9\u306E\u6570"},
       {"blockNumbers1Text",    "blok-getallen 1",
                                "block numbers 1",
                                "n\u00FAmeros de bloque 1",
                                "n\u00FAmeros de blocos 1",                                
        "\uFF8C\uFF9E\uFF9B\uFF6F\uFF78\u578B\u306E\u70B9\u306E\u6570\u0031"},
       {"fNumbers1Text",    "F-getallen 1",
                            "F numbers 1",
                            "n\u00FAmeros F 1", 
                            "n\u00FAmeros F 1",                             
        "\u0046\u5B57\u578B\u306E\u70B9\u306E\u6570\u0031"},
       {"fNumbers2Text",    "F-getallen 2",
                            "F numbers 2",
                            "n\u00FAmeros F 2",
                            "n\u00FAmeros F 2",                             
         "\u0046\u5B57\u578B\u306E\u70B9\u306E\u6570\u0032"},
         
       {"flapNumbersText",   "flap-getallen",
                             "flap numbers",
                             "n\u00FAmeros semi-circulares", 
                             "n\u00FAmeros quase circulares",                              
        "\u304B\u3069\u306A\u3057\u578B\u306E\u70B9\u306E\u6570"},
       {"flipNumbersText",   "flip-getallen",
                             "flip numbers",
                             "n\u00FAmeros de estrella",                             
                             "n\u00FAmeros estrelados",                                                          
        "\u306F\u307F\u51FA\u3057\u578B\u306E\u70B9\u306E\u6570"},
       {"tileNumbers1Text",   "tegel-getallen 1",
                              "tile numbers 1",
                              "n\u00FAmeros de baldosa 1",
                              "n\u00FAmeros espelhados 1",                              
        "\uFF80\uFF72\uFF99\u578B\u306E\u70B9\u306E\u6570\u0031"},
       {"tileNumbers2Text",   "tegel-getallen 2",
                              "tile numbers 2",
                              "n\u00FAmeros de baldosa 2",
                              "n\u00FAmeros espelhados 2",                                                            
        "\uFF80\uFF72\uFF99\u578B\u306E\u70B9\u306E\u6570\u0032"},
       {"tileNumbers3Text",   "tegel-getallen 3",
                              "tile numbers 3",
                              "n\u00FAmeros de baldosa 3",
                              "n\u00FAmeros espelhados 3",                                                            
        "\uFF80\uFF72\uFF99\u578B\u306E\u70B9\u306E\u6570\u0033"},
       {"tileNumbers4Text",   "tegel-getallen 4",
                              "tile numbers 4",
                              "n\u00FAmeros de baldosa 4",
                              "n\u00FAmeros espelhados 4",                                                            
        "\uFF80\uFF72\uFF99\u578B\u306E\u70B9\u306E\u6570\u0034"},
        
       {"hNumbers1Text",    "H-getallen 1",
                            "H numbers 1",
                            "n\u00FAmeros H 1", 
                            "n\u00FAmeros H 1",                             
        "\u0048\u5B57\u578B\u306E\u70B9\u306E\u6570\u0031"},
       {"hNumbers2Text",    "H-getallen 2",
                            "H numbers 2",
                            "n\u00FAmeros H 2", 
                            "n\u00FAmeros H 2",                             
        "\u0048\u5B57\u578B\u306E\u70B9\u306E\u6570\u0032"},
       {"lNumbers1Text",    "L-getallen 1",
                            "L numbers 1",
                            "n\u00FAmeros L 1",
                            "n\u00FAmeros L 1",                            
        "\u004C\u5B57\u578B\u306E\u70B9\u306E\u6570\u0031"},
       {"lNumbers2Text",    "L-getallen 2",
                            "L numbers 2",
                            "n\u00FAmeros L 2",
                            "n\u00FAmeros L 2",                            
        "\u004C\u5B57\u578B\u306E\u70B9\u306E\u6570\u0032"},
       {"xNumbers1Text",    "X-getallen 1",
                            "X numbers 1",
                            "n\u00FAmeros X 1",
                            "n\u00FAmeros X 1",                            
        "\u0058\u5B57\u578B\u306E\u70B9\u306E\u6570\u0031"},
       {"xNumbers2Text",    "X-getallen 2",
                            "X numbers 2",
                            "n\u00FAmeros X 2",
                            "n\u00FAmeros X 2",                            
        "\u0058\u5B57\u578B\u306E\u70B9\u306E\u6570\u0032"},
       {"zNumbersText",    "Z-getallen",
                           "Z numbers",
                           "n\u00FAmeros Z",
                           "n\u00FAmeros Z",                           
        "\u005A\u5B57\u578B\u306E\u70B9\u306E\u6570"},
         
       {"insectNumbersText",    "insect-getallen",
                                "insect numbers",
                                "n\u00FAmeros de insecto",
                                "n\u00FAmeros insecto",                                
        "\u6606\u866B\u578B\u306E\u70B9\u306E\u6570"},
       {"spiderNumbersText",    "spin-getallen",
                                "spider numbers",
                                "n\u00FAmeros de ara\u00F1a",
                                "n\u00FAmeros aranha",                                
        "\uFF78\uFF93\u306E\u578B\u306E\u70B9\u306E\u6570"},
       {"piramidNumbersText",    "piramide-getallen",
                                 "piramid numbers",
                                 "n\u00FAmeros piramidales",
                                 "n\u00FAmeros pir\u00E2mide",                                 
        "\uFF8B\uFF9F\uFF97\uFF90\uFF6F\uFF84\uFF9E\u578B\u306E\u70B9\u306E\u6570"},
       {"tableNumbersText",    "tafel-getallen",
                               "table numbers",
                               "n\u00FAmeros de mesa",
                               "n\u00FAmeros mesa",                               
        "\uFF83\u002D\uFF8C\uFF9E\uFF99\u578B\u306E\u70B9\u306E\u6570"},
       {"stairNumbersText",    "trap-getallen",
                               "stair numbers",
                               "n\u00FAmeros de escalera",
                               "n\u00FAmeros escada",                               
        "\u968E\u6BB5\u6570"},
       {"blockNumbers2Text",   "block-getallen 2",
                               "block numbers 2",
                               "n\u00FAmeros de bloque 2",
                               "n\u00FAmeros de blocos 2",                               
        "\uFF8C\uFF9E\uFF9B\uFF6F\uFF78\u578B\u306E\u70B9\u306E\u6570\u0032"},
       {"blockNumbers3Text",   "block-getallen 3",
                               "block numbers 3",
                               "n\u00FAmeros de bloque 3",
                               "n\u00FAmeros de blocos 3",                               
        "\uFF8C\uFF9E\uFF9B\uFF6F\uFF78\u578B\u306E\u70B9\u306E\u6570\u0033"},

       {"triaNumbers4Text",   "driehoeks-getallen 4",
                               "triangular numbers 4",
                               "n\u00FAmeros triangulares 4", 
                               "n\u00FAmeros triangulares 4",                                
        "\u4E09\u89D2\u6570\u0034"},
        
        
       {"levelText",  "niveau",
                      "level",
                      "nivel",
                      "n\u00EDvel",                      
        "\uFF9A\uFF8D\uFF9E\uFF99"},
       {"chooseProblemText",   "kies een plobleem",
                               "choose a problem",
                               "escojer un problema",
                               "escolhe uma sequ\u00EAncia",                               
        "\u554F\u984C\u3092\u9078\u3076"},
       {"nextPatternText",   "toon volgend patroon",
                             "show next pattern",
                             "dibujo siguiente",
                             "figura seguinte",                             
        "\u6B21\u306E\uFF8A\uFF9F\uFF80\u002D\uFF9D\u3092\u793A\u3059"},
       {"scoreText",    "score",
                       "score",
                       "puntuaci\u00F3n",
                       "pontua\u00E7\u00E3o",                       
        "\u5F97\u70B9"},
       {"totalText",   "totaal",
                      "total",
                      "total",
                      "total",                      
        "\u5408\u8A08"},
       {"nextQuestionText",  "volgende vraag",
                             "next question",
                             "pregunta siguiente", 
                             "pergunta seguinte",                              
        "\u6B21\u306E\u554F\u984C"},
       {"spotsText",   " stippen",
                       " spots",
                       " puntos",
                       " pontos",                       
        "\u500B"},
        
       {"yesNoText",    " (j/n)?",
                        " (y/n)?",
                        " (s/n)?",
                        " (s/n)?",                        
//                        " (y/n)"},
        " (\u0079\u0065\u0073\u002F\u006E\u006F)"},
        
       {"orText",    " of ",
                     " or ",
                     " o ",
                     " ou ",                     
                     " "},

       {"typeText",  "Type ",
                     "Type ",
                     "Tocar ",
                     "Digita ",                     
                     " "},
        
        
        
       {"howManyText",    "Hoeveel stippen bevat patroon n=",
                          "How many spots are contained in pattern n=",
                          "\u00BFCuantos puntos contiene el dibujo n=",
                          "Quantos pontos tem a figura n=",                          
                          "n="},
//        "\u53F3\u306E\u5834\u5408\u306F\u4F55\u500B\u306E\u70B9\u304C\u3042\u308A\u307E\u3059\u304B\u3002\u006E\u003D"},
       {"howMany2Text",   "?",
                          "?",
                          "?",
                          "?",                          
        "\u306E\u5834\u5408\u306F\u4F55\u500B\u306E\u70B9\u304C\u3042\u308A\u307E\u3059\u304B\u3002"},
        
        
       {"existsText",  "Bestaat er een patroon met ",
                       "Does there exist a pattern with ",
                       "\u00BFExiste un dibujo con ",
                       "Existe uma figura com ",
//        "\u53F3\u306E\u5834\u5408\u306F\u3067\u304D\u307E\u3059\u304B"},
        "\u70B9\u306E\u500B\u6570\u304C"},        
       {"exists2Text", "",
                       "",
                       "",
                       "",                       
        "\u306E\u5834\u5408\u306F\u3042\u308A\u307E\u3059\u304B\u3002"},
//        "\u70B9\u306E\u500B\u6570\u304C\"},        
        
       {"formulaText",   "Geef een expressie (in n) voor deze reeks patronen",
                         "Enter an expression (in n) for this series of patterns",
                         "Dar una expresi\u00F3n (en n) para este serie de dibujos",
                         "Digita uma express\u00E3o (em n) para esta sequ\u00EAncia",                         
        "\u006E\u756A\u76EE\u306E\u5F0F\u3092\u006E\u3092\u7528\u3044\u3066\u8868\u3057\u306A\u3055\u3044"},
        
        
       {"enterText",   "Druk op Enter na je antwoord",
                       "Press Enter after your answer",
                       "Enter despues la respuesta",
                       "Preme Enter ap\u00F3s responderes",                       
        "\u5165\u529B\u3057\u305F\u3089\u0045\u006E\u0074\u0065\u0072\u3092\u62BC\u3057\u307E\u3057\u3087\u3046"},
       {"correctText",  "Dit klopt",
                        "This is correct",
                        "Este es correcto",
                        "Est\u00E1 certo",                        
        "\u6B63\u89E3\u0021"},
       {"notCorrectText",  "Dit klopt niet",
                           "This is not correct",
                           "Este no es correcto",
                           "N\u00E3o est\u00E1 correcto",                           
        "\u307E\u3061\u304C\u3044"},
       {"languageText",   "dutch",
                          "english",
                          "spanish",
                          "portugese",
                          "japanese"},
//        "\u65E5\u672C\u8A9E"},
        
       // parsing errors
       {"missingText",   "ontbrekende term: ",
                         "missing term: ",
                         "falta t\u00E9rmino: ",
                         "falta texto",                         
        "\u8A00\u8449\u304C\u629C\u3051\u3066\u3044\u307E\u3059"},
       {"nothingText",   "er is niets ingevoerd",
                         "nothing was entered",
                         "nada entrada",
                         "nada foi inserido",                         
        "\u4F55\u3082\u5165\u529B\u3055\u308C\u3066\u3044\u307E\u307E\u3093"},
       {"unknownText",    "onbekend symbool: ",
                          "unknown symbol: ",
                          "s\u00EDmbolo desconocido:",
                          "s\u00EDmbolo desconhecido:",                          
        "\u3053\u306E\u8A18\u53F7\u306F\u5165\u529B\u3067\u304D\u307E\u305B\u3093"},
       {"leftParText",  "( ontbreekt: ",
                        "missing (: ",
                        "falta (: ",
                        "falta (: ",                        
        "\u5165\u529B\u3055\u308C\u3066\u3044\u307E\u305B\u3093"},
       {"rightParText",   ") ontbreekt: ",
                          "missing ): ",
                          "falta ): ", 
                          "falta ): ",                           
        "\u5165\u529B\u3055\u308C\u3066\u3044\u307E\u305B\u3093"},
       {"opExpectedText",   "ontbrekende operator: ",
                            "missing operator: ",
                            "falta operador: ",
                            "falta operador: ",                            
        "\u6F14\u7B97\u8A18\u53F7\u304C\u629C\u3051\u3066\u3044\u307E\u3059"},
       {"rightTooMuchText",    ") teveel: ",
                               "too many ): ",
                               "demasiado ): ",
                               "demasiado ): ",                               
        "\u591A\u3059\u304E\u307E\u3059"},
       {"numberErrorText",    "fout in getal: ",
                              "error in number: ",
                              "error en n\u00FAmero: ",
                              "erro no n\u00FAmero: ",                              
        "\u6570\u306B\u9593\u9055\u3044\u304C\u3042\u308A\u307E\u3059"},
         {"notANumberText", "geen positief geheel getal ",
                            "not a positive integer ", 
                            "no es un entero positivo ",
                            "n\u00E3o \u00E9 n\u00FAmero inteiro positivo",                            
        "\u6570\u306B\u9593\u9055\u3044\u304C\u3042\u308A\u307E\u3059"},
        

        };
        
        
        
        
        
        
        
        
        
    // actual lookup table    
    public static Hashtable table = new Hashtable();
    // language string read from parameter file
    private String language;
    // code for language, (column) 1 = dutch,  
    // (column) 2 = english, (column) 3 = spanish    
    private int languageCode;

    // constructor, find the table column number for the required language
    // and initialize the hashtable with the keys column and the
    // language column
    LookUpTable(String lang)
    {   language = lang;
        initLanguageCode();
        // copy keyword column (0) and desired language
        // column (languageCode) in the HashTable
        for (int i = 0; i < contents.length; i++)
            table.put(contents[i][0], contents[i][languageCode]);
    } // constructor
    
    // translate language (String) to languageCode (int)
    private void initLanguageCode()
    {   // no string defaults to dutch
        if (language == null)
            languageCode = 1;
        else if (language.equals("nl"))
            languageCode = 1;
        else if (language.equals("en"))
            languageCode = 2;    
        else if (language.equals("es"))
            languageCode = 3;    
        else if (language.equals("pt"))
            languageCode = 4;    
        else if (language.equals("jp"))
            languageCode = 5;    
        // unknown string defaults to dutch    
        else languageCode = 1;    
    } // initLanguageCode
    
    // reading from the hash table
    public static String lookUp(String key)
    {   if (table.containsKey(key))
            return (String) table.get(key);
        else
            return "";
    } // lookUp
    
} // class LookUpTable    

