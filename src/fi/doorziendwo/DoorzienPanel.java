package fi.doorziendwo;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Vector;

import java.awt.event.*;

import javax.swing.*;

public class DoorzienPanel extends JPanel 
{
	// nodig??
	//ViewerIF viewer;
	
    // GUI components
    // buffering drawing
    DrawingPanel2 drawingPanel;
    // top toolbar
    TopToolBar2 topToolBar;
    // right toolbar
    RightToolBar2 rightToolBar;
    // help bar
    HelpBar helpBar;
    // big bar on top
    TopBar topBar;
	// menu-gebeuren
	JMenuBar menuBar;
	JMenu figurenMenu, optiesMenu;
	
	JCheckBoxMenuItem helpPuntenItem, lettersItem;
	JRadioButtonMenuItem centraleProjectieItem, parallelProjectieItem;
	JRadioButtonMenuItem achtvlakItem, balkItem, cilinderItem, piramideHuisItem, schildHuisItem,
					     kegel1Item, kegel2Item, kegel3Item, kegel4Item, kubusItem, 
					     piramide3Item, piramide4Item, piramide5Item, piramide6Item, piramide7Item, piramide8Item,
					     prisma3Item, prisma4Item, prisma5Item, prisma6Item, twaalfvlakItem, twintigvlakItem, viervlakItem,
					     mijnFiguurItem;
	JMenu huizenMenu, kegelsMenu,piramidesMenu, prismasMenu; 
    
    // images for tool bar(s)
    // right top to bottom
    Image rotateImage,
          wireFrameImage, solidImage,
          zoomInImage, zoomInOffImage, 
          zoomOutImage, zoomOutOffImage, 
          conDrawImage, 
          figureImage
          ;
    
    // top left to right
    Image drawLineImage, drawLineOffImage,
          deleteLineImage, deleteLineOffImage,  
          lengLinesImage, lengLinesOffImage,
          shortLinesImage, shortLinesOffImage,
          
          drawPlaneImage, drawPlaneOffImage,
          parPlaneImage, parPlaneOffImage,
          deletePlaneImage, deletePlaneOffImage,      
          planesFilledImage, planesFilledOffImage, planesEmptyImage,
          transPlaneImage, transPlaneOffImage, noTransPlaneImage,
          rotPlaneImage, rotPlaneOffImage, noRotPlaneImage, 
          showCutImage, showCutOffImage, hideCutImage,         
          cutImage, cutOffImage, glueImage,
          undoImage, undoOffImage,
          redoImage, redoOffImage
          ;  

    Image epnImage;
    
	protected String[] imageNames = {
			"rotate.gif",
			"wireframe.gif",
			"solid.gif",
			"zoomin.gif",
			"zoominoff.gif",
			"zoomout.gif",
			"zoomoutoff.gif",
			"condraw.gif",
			"figure.gif",
			"drawline.gif",
			"drawlineoff.gif",
			"deleteline.gif",
			"deletelineoff.gif",
			"lenglines.gif",
			"lenglinesoff.gif",
			"shortlines.gif",
			"shortlinesoff.gif",
			"drawplane.gif",
			"drawplaneoff.gif",
			"parplane.gif",
			"parplaneoff.gif",
			"deleteplane.gif",
			"deleteplaneoff.gif",
			"planesfilled.gif",
			"planesfilledoff.gif",
			"planesempty.gif",
			"transplane.gif",
			"transplaneoff.gif",
			"notransplane.gif",
			"rotplane.gif",
			"rotplaneoff.gif",
			"norotplane.gif",
			"showcut.gif",
			"showcutoff.gif",
			"hidecut.gif",
			"cut.gif",
			"cutoff.gif",
			"glue.gif",
			"undo.gif",
			"undooff.gif",
			"redo.gif",
			"redooff.gif",
			"EPNlogo.gif"};
	
	
	
	protected static Hashtable images;
    
	
    // constants for sizes  
    // initial frame size, used only once
    //public int minWidth = 700;
    //public int minHeight = 500;
	
	// menuBar
	public int defaultMenuHeight = 25;
	public int menuHeight = defaultMenuHeight; 
    // top tool bar
    public int topHeight = 42;
    // right tool bar
    public int rightWidth = 51;
    // help bar
    public int defaultHelpHeight = 23;
    public int helpHeight = defaultHelpHeight;
    // menu + top tool bar + help bar
    public int totalTopHeight = menuHeight + topHeight + helpHeight;
    // some offSet
    int offSet = 5;


	// constants for figures
	public static final int OCTAHEDRON = 0;
	public static final int BLOCK = 1;
	public static final int CYLINDER = 2;
	public static final int HOUSES = 3;
    	public static final int PIRHOUSE = 31;
	    public static final int EDGEHOUSE = 32;	
	public static final int CONES = 4;
	    public static final int CONE1 = 41;
	    public static final int CONE2 = 42;
	    public static final int CONE3 = 43;
	    public static final int CONE4 = 44;
	public static final int CUBE = 5;
	public static final int PIRAMIDS = 6;
	    public static final int PIRAMID3 = 61;
	    public static final int PIRAMID4 = 62;
	    public static final int PIRAMID5 = 63;
	    public static final int PIRAMID6 = 64;
	    public static final int PIRAMID7 = 65;
	    public static final int PIRAMID8 = 66;	    
	public static final int PRISMS = 7;
	    public static final int PRISM3 = 71;
	    public static final int PRISM4 = 72;
	    public static final int PRISM5 = 73;
	    public static final int PRISM6 = 74;
	public static final int DODECAHEDRON = 8;
	public static final int ICOSAHEDRON = 9;	
	public static final int TETRAHEDRON = 10;

	public static final int MYFIGURE = 100;
	
	
    String[] dropNumHelpPoints =
    {   tt("twoPartsText"),
        tt("threePartsText"),
	    tt("fourPartsText"),        
	    tt("fivePartsText"),        
	    tt("sixPartsText"),
        tt("sevenPartsText"),
	    tt("eightPartsText"),        
	    tt("ninePartsText"),        
	    tt("tenPartsText"),                
	};
	
	// change this after reading parameter figures
	public int defaultFigure = CUBE;
	// the figure string
	String figureString = null;

	boolean demo = false;
	
	boolean figurenMenuOptie = true;
	boolean optiesMenuOptie = true;
	boolean helpBarOptie = true;
	
	boolean lijnTekenOptie = true;
	boolean lijnVerlengOptie = true;
	
	boolean vlakTekenOptie = true;
	boolean evenwijdigVlakOptie = true;
	boolean toonDoorsnedeOptie = true;
	boolean splitsFiguurOptie = true;
	
	boolean bouwplaatOptie = true;
	
	// menukeuzes docent indien geen menu
	//int figuurCode = CUBE;
	boolean letters = false;
	boolean hulpPunten = false;
	boolean centraleProjectie = true;
	
	boolean rotateOption = true;
	boolean borderOption = false;
	boolean foldOption = false;
	
	
    public DoorzienPanel(int x, int y, int w, int h)
    {
    	super.setBounds(x, y, w, h);
    	
    	//setBackground(Color.white);
    	
	    if (images == null)
		{	images = new Hashtable();
			DoorzienDWO.loadImages(images, imageNames);
		}
	    
	    
	    rotateImage = getImage("rotate.gif");
		
	    wireFrameImage = getImage("wireframe.gif");
		solidImage = getImage("solid.gif");
		zoomInImage = getImage("zoomin.gif");
		zoomInOffImage = getImage("zoominoff.gif");
		zoomOutImage = getImage("zoomout.gif");
		zoomOutOffImage = getImage("zoomoutoff.gif");
		conDrawImage = getImage("condraw.gif");
		figureImage = getImage("figure.gif");

		drawLineImage = getImage("drawline.gif");
        drawLineOffImage = getImage("drawlineoff.gif");
        deleteLineImage = getImage("deleteline.gif");
        deleteLineOffImage = getImage("deletelineoff.gif");
        lengLinesImage = getImage("lenglines.gif");
        lengLinesOffImage = getImage("lenglinesoff.gif");
        shortLinesImage = getImage("shortlines.gif");
        shortLinesOffImage = getImage("shortlinesoff.gif");
        
        drawPlaneImage = getImage("drawplane.gif");
        drawPlaneOffImage = getImage("drawplaneoff.gif");
        parPlaneImage = getImage("parplane.gif");        
        parPlaneOffImage = getImage("parplaneoff.gif");        
        deletePlaneImage = getImage("deleteplane.gif");
        deletePlaneOffImage = getImage("deleteplaneoff.gif");
        planesFilledImage = getImage("planesfilled.gif");
        planesFilledOffImage = getImage("planesfilledoff.gif");        
        planesEmptyImage = getImage("planesempty.gif");        
        transPlaneImage = getImage("transplane.gif");
        transPlaneOffImage = getImage("transplaneoff.gif");
        noTransPlaneImage = getImage("notransplane.gif");
        rotPlaneImage = getImage("rotplane.gif");
        rotPlaneOffImage = getImage("rotplaneoff.gif");
        noRotPlaneImage = getImage("norotplane.gif");
        showCutImage = getImage("showcut.gif");
        showCutOffImage = getImage("showcutoff.gif");
        hideCutImage = getImage("hidecut.gif");
        cutImage = getImage("cut.gif");
        cutOffImage = getImage("cutoff.gif");
        glueImage = getImage("glue.gif");        
        undoImage = getImage("undo.gif");
        undoOffImage = getImage("undooff.gif");
        redoImage = getImage("redo.gif");
        redoOffImage = getImage("redooff.gif");
	    
	    epnImage = getImage("EPNlogo.gif");

		// layout and size
        // use BorderLayout so that Menubar is included in the layout
		setLayout(new BorderLayout());
		
		//setSize(minWidth, minHeight); 
        // create and add GUI compoments
        drawingPanel = new DrawingPanel2(this);
        add(drawingPanel, BorderLayout.CENTER);        
        
        // changing order irrelevant
        topBar = new TopBar(this);
        //topBar.setLayout(new BorderLayout());
        topBar.setLayout(null);

		menuBar = new JMenuBar();
		figurenMenu = new JMenu(Table.lookUp("figureText"));
		
		maakFigurenMenu();
		
		menuBar.add(figurenMenu);
		
		optiesMenu = new JMenu(Table.lookUp("optionsText"));

		maakOptiesMenu();
		
		menuBar.add(optiesMenu);

		menuBar.setBounds(0, 0, getSize().width, menuHeight);
		
        // topToolBar gets total width
        topToolBar = new TopToolBar2(this);
        topToolBar.setBounds(0, menuHeight, getSize().width, topHeight);        
        
        topBar.add(topToolBar);
        
        helpBar = new HelpBar(this);
        helpBar.setBounds(0, menuHeight + topHeight, getSize().width, helpHeight);
        
        topBar.add(helpBar);
        
        topBar.add(menuBar, 0);

        add(topBar, BorderLayout.NORTH);    
        
        rightToolBar = new RightToolBar2(this);
        add(rightToolBar, BorderLayout.EAST);        
        
        // for BorderLayout
        validate();
        
        drawingPanel.initialize(defaultFigure);
        
        	//else
        //	drawingPanel.initialize(MYFIGURE);	

        helpBar.setText(tt("rotateText"));
        
        setBackground(Color.white);
	    
    }

    public void setBackground(Color c)
    {
    	    		
    	if ((drawingPanel != null) && (drawingPanel.panel3D != null))
    		drawingPanel.panel3D.setBackground(c);
    	
    	super.setBackground(c);
    		
    }
    public void maakOptiesMenu()
    {
		helpPuntenItem = new JCheckBoxMenuItem(Table.lookUp("helpPointsText"));
		optiesMenu.add(helpPuntenItem);
		helpPuntenItem.addActionListener(new MenuListener());
		
		lettersItem = new JCheckBoxMenuItem(Table.lookUp("lettersText"));
		optiesMenu.add(lettersItem);
		lettersItem.addActionListener(new MenuListener());
		
		ButtonGroup projectieGroep = new ButtonGroup();
		centraleProjectieItem = new JRadioButtonMenuItem(Table.lookUp("centralProjText"));
		projectieGroep.add(centraleProjectieItem);
		centraleProjectieItem.setSelected(true);
		optiesMenu.add(centraleProjectieItem);
		centraleProjectieItem.addActionListener(new MenuListener());
		
		parallelProjectieItem = new JRadioButtonMenuItem(Table.lookUp("parallelProjText"));
		projectieGroep.add(parallelProjectieItem);
		optiesMenu.add(parallelProjectieItem);
		parallelProjectieItem.addActionListener(new MenuListener());

    }
    
    public void maakFigurenMenu()
    {
    	ButtonGroup figurenGroep = new ButtonGroup();

    	achtvlakItem = new JRadioButtonMenuItem(Table.lookUp("octahedronText"));
    	figurenGroep.add(achtvlakItem);
    	figurenMenu.add(achtvlakItem);
    	achtvlakItem.addActionListener(new MenuListener());
    	
    	balkItem = new JRadioButtonMenuItem(Table.lookUp("blockText"));
    	figurenGroep.add(balkItem);
    	figurenMenu.add(balkItem);
    	balkItem.addActionListener(new MenuListener());
  
    	cilinderItem = new JRadioButtonMenuItem(Table.lookUp("cylinderText"));
    	figurenGroep.add(cilinderItem);
    	figurenMenu.add(cilinderItem);
    	cilinderItem.addActionListener(new MenuListener());
    	
    	huizenMenu = new JMenu(Table.lookUp("housesText"));
    	figurenMenu.add(huizenMenu);
    	
    	piramideHuisItem = new JRadioButtonMenuItem(Table.lookUp("pirHouseText"));
    	figurenGroep.add(piramideHuisItem);
    	huizenMenu.add(piramideHuisItem);
    	piramideHuisItem.addActionListener(new MenuListener());
    	
    	schildHuisItem = new JRadioButtonMenuItem(Table.lookUp("edgeHouseText"));
    	figurenGroep.add(schildHuisItem);
    	huizenMenu.add(schildHuisItem);
    	schildHuisItem.addActionListener(new MenuListener());
    	
    	kegelsMenu = new JMenu(Table.lookUp("conesText"));
    	figurenMenu.add(kegelsMenu);
    	
    	kegel1Item = new JRadioButtonMenuItem(Table.lookUp("cone1Text"));
    	figurenGroep.add(kegel1Item);
    	kegelsMenu.add(kegel1Item);
    	kegel1Item.addActionListener(new MenuListener());

    	kegel2Item = new JRadioButtonMenuItem(Table.lookUp("cone2Text"));
    	figurenGroep.add(kegel2Item);
    	kegelsMenu.add(kegel2Item);
    	kegel2Item.addActionListener(new MenuListener());

    	kegel3Item = new JRadioButtonMenuItem(Table.lookUp("cone3Text"));
    	figurenGroep.add(kegel3Item);
    	kegelsMenu.add(kegel3Item);
    	kegel3Item.addActionListener(new MenuListener());

    	kegel4Item = new JRadioButtonMenuItem(Table.lookUp("cone4Text"));
    	figurenGroep.add(kegel4Item);
    	kegelsMenu.add(kegel4Item);
    	kegel4Item.addActionListener(new MenuListener());
    	
    	kubusItem = new JRadioButtonMenuItem(Table.lookUp("cubeText"));
    	figurenGroep.add(kubusItem);
    	kubusItem.setSelected(true);
    	figurenMenu.add(kubusItem);
    	kubusItem.addActionListener(new MenuListener());
    	
    	piramidesMenu = new JMenu(Table.lookUp("piramidsText"));
    	figurenMenu.add(piramidesMenu);
    	
    	piramide3Item = new JRadioButtonMenuItem(Table.lookUp("threePiramidText"));
    	figurenGroep.add(piramide3Item);
    	piramidesMenu.add(piramide3Item);
    	piramide3Item.addActionListener(new MenuListener());
    	
    	piramide4Item = new JRadioButtonMenuItem(Table.lookUp("fourPiramidText"));
    	figurenGroep.add(piramide4Item);
    	piramidesMenu.add(piramide4Item);
    	piramide4Item.addActionListener(new MenuListener());

    	piramide5Item = new JRadioButtonMenuItem(Table.lookUp("fivePiramidText"));
    	figurenGroep.add(piramide5Item);
    	piramidesMenu.add(piramide5Item);
    	piramide5Item.addActionListener(new MenuListener());

    	piramide6Item = new JRadioButtonMenuItem(Table.lookUp("sixPiramidText"));
    	figurenGroep.add(piramide6Item);
    	piramidesMenu.add(piramide6Item);
    	piramide6Item.addActionListener(new MenuListener());

    	piramide7Item = new JRadioButtonMenuItem(Table.lookUp("sevenPiramidText"));
    	figurenGroep.add(piramide7Item);
    	piramidesMenu.add(piramide7Item);
    	piramide7Item.addActionListener(new MenuListener());

    	piramide8Item = new JRadioButtonMenuItem(Table.lookUp("eightPiramidText"));
    	figurenGroep.add(piramide8Item);
    	piramidesMenu.add(piramide8Item);
    	piramide8Item.addActionListener(new MenuListener());
    	
    	prismasMenu = new JMenu(Table.lookUp("prismsText"));
    	figurenMenu.add(prismasMenu);
    	
      	prisma3Item = new JRadioButtonMenuItem(Table.lookUp("threePrismText"));
    	figurenGroep.add(prisma3Item);
    	prismasMenu.add(prisma3Item);
    	prisma3Item.addActionListener(new MenuListener());
    	
      	prisma4Item = new JRadioButtonMenuItem(Table.lookUp("fourPrismText"));
    	figurenGroep.add(prisma4Item);
    	prismasMenu.add(prisma4Item);
    	prisma4Item.addActionListener(new MenuListener());

      	prisma5Item = new JRadioButtonMenuItem(Table.lookUp("fivePrismText"));
    	figurenGroep.add(prisma5Item);
    	prismasMenu.add(prisma5Item);
    	prisma5Item.addActionListener(new MenuListener());
    	
      	prisma6Item = new JRadioButtonMenuItem(Table.lookUp("sixPrismText"));
    	figurenGroep.add(prisma6Item);
    	prismasMenu.add(prisma6Item);
    	prisma6Item.addActionListener(new MenuListener());
    	
    	twaalfvlakItem = new JRadioButtonMenuItem(Table.lookUp("dodecahedronText"));
    	figurenGroep.add(twaalfvlakItem);
    	figurenMenu.add(twaalfvlakItem);
    	twaalfvlakItem.addActionListener(new MenuListener());
    	
    	twintigvlakItem = new JRadioButtonMenuItem(Table.lookUp("icosahedronText"));
    	figurenGroep.add(twintigvlakItem);
    	figurenMenu.add(twintigvlakItem);
    	twintigvlakItem.addActionListener(new MenuListener());
    	
    	viervlakItem = new JRadioButtonMenuItem(Table.lookUp("tetrahedronText"));
    	figurenGroep.add(viervlakItem);
    	figurenMenu.add(viervlakItem);
    	viervlakItem.addActionListener(new MenuListener());
    	
    	mijnFiguurItem = new JRadioButtonMenuItem(Table.lookUp("myFigureText"));
    	figurenGroep.add(mijnFiguurItem);
    	figurenMenu.add(mijnFiguurItem);
    	mijnFiguurItem.addActionListener(new MenuListener());
    }
    
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x, y, b, h);
		
		menuBar.setSize(getSize().width, menuHeight);
		topToolBar.setSize(getSize().width, topHeight);
		helpBar.setSize(getSize().width, helpHeight);
		
		validate();
	}
    
	public static Image getImage(String name)
	{	return (Image) images.get(name);
	}

	   // shortcut for tablelookup
	public String tt(String s)
	{   return Table.lookUp(s);
	}
	
    public void resetProjection(int proj)
    {   
        if (proj == drawingPanel.CENTRALPROJ)
            centraleProjectieItem.setSelected(true);
        else if (proj == drawingPanel.PARALLELPROJ)    
            parallelProjectieItem.setSelected(true);
    }    
	
	public void resetLetters()
	{   lettersItem.setState(false);
	}

	public void setLetters()
	{   lettersItem.setState(true);
	}
	
	public boolean getLetters()
	{   return lettersItem.getState();
	}
	
	public void resetHelpPoints()
	{	helpPuntenItem.setState(false);   
	}	
	
	public void enableOptions(boolean b)
	{   
	    lettersItem.setEnabled(b);
	    helpPuntenItem.setEnabled(b);
	}
	
	public void setRotateOption(boolean b)
	{	rotateOption = b;
		if (demo)
			drawingPanel.zetDraaibaar(rotateOption);
		else
			drawingPanel.zetDraaibaar(true);
	}

	public void setBorderOption(boolean b)
	{	borderOption = b;
		if (demo)
			drawingPanel.panel3D.setBordered(borderOption);
		else
			drawingPanel.panel3D.setBordered(true);
	}

	public void setFoldOption(boolean b)
	{	foldOption = b;
		if (demo)
		{
			if ((drawingPanel.mouseMode == DrawingPanel2.FOLDOUT) && (drawingPanel.startFacet != null))
			{
				drawingPanel.slider.setVisible(foldOption);
				drawingPanel.flatButton.setVisible(false);
			}
		}
/*		
		else
		{
			if ((drawingPanel.mouseMode == DrawingPanel2.FOLDOUT) && (drawingPanel.startFacet != null))
			{
				drawingPanel.slider.setVisible(true);
				drawingPanel.flatButton.setVisible(true);
			}
		}
*/			
	}
	
	
	public void zetDemo(boolean b)
	{	demo = b;
	
System.out.println("demo " + demo);

		if (demo)
		{
			topBar.setVisible(false);
			rightToolBar.setVisible(false);
			
			validate();
			
			// zet de mode maar vast terug
			if ((drawingPanel.mouseMode == DrawingPanel2.FOLDOUT) && (drawingPanel.startFacet != null))
			{
				if (foldOption)
				{
					drawingPanel.flatButton.setVisible(false);
				}
				else
				{
					drawingPanel.slider.setVisible(false);
					drawingPanel.flatButton.setVisible(false);
				}
				
					
			}
			else if ((drawingPanel.mouseMode == DrawingPanel2.CUTOBJECT) && (drawingPanel.planeChoosen != null))
			{
				
			}
			else
			{
				
				drawingPanel.mouseMode = DrawingPanel2.INERT;
				topToolBar.unPress();
				helpBar.setText(tt("rotateText"));           
				drawingPanel.panel3D.hideHelpLine();                        
				drawingPanel.panel3D.hideHelpPoint();
				drawingPanel.helpPoint = false;
                DrawConstants.TICKSVISIBLE = false;                
                drawingPanel.showHelpPointDrop(false);
                enableOptions(true);
                                
			}
			
//			setRotateOption(rotateOption);
//			setBorderOption(borderOption);
//			setFoldOption(foldOption);
			
			
		}
		else
		{
			topBar.setVisible(true);
			rightToolBar.setVisible(true);
			
			validate();
			
			if ((drawingPanel.mouseMode == DrawingPanel2.FOLDOUT) && (drawingPanel.startFacet != null))
			{
				drawingPanel.slider.setVisible(true);
				drawingPanel.flatButton.setVisible(true);
			}
			
		}
		
		setRotateOption(rotateOption);
		setBorderOption(borderOption);
//		setFoldOption(foldOption);
		
	}
	
	public void zetFigurenMenuOptie(boolean b)
	{	figurenMenuOptie = b;
		figurenMenu.setVisible(figurenMenuOptie);
		if (!figurenMenuOptie && !optiesMenuOptie)
		{	menuBar.setVisible(false);
    		topToolBar.setBounds(0, 0, getSize().width, topHeight);
    		helpBar.setBounds(0, topHeight, getSize().width, helpHeight);
		
		}
		else
		{	menuBar.setVisible(true);
        	topToolBar.setBounds(0, menuHeight, getSize().width, topHeight);
            helpBar.setBounds(0, menuHeight + topHeight, getSize().width, helpHeight);
		}
		
		if (helpBarOptie && menuBar.isVisible())
			totalTopHeight = menuHeight + topHeight + helpHeight;
		else if (helpBarOptie && !menuBar.isVisible())
			totalTopHeight = topHeight + helpHeight;
		else if (!helpBarOptie && menuBar.isVisible())
			totalTopHeight = menuHeight + topHeight;
		else if (!helpBarOptie && !menuBar.isVisible())
			totalTopHeight = topHeight;
			
		validate();
		
	}
	
	public void zetOptiesMenuOptie(boolean b)
	{	optiesMenuOptie = b;
		optiesMenu.setVisible(optiesMenuOptie);
		if (!figurenMenuOptie && !optiesMenuOptie)
		{	menuBar.setVisible(false);
			topToolBar.setBounds(0, 0, getSize().width, topHeight);
			helpBar.setBounds(0, topHeight, getSize().width, helpHeight);
		
		}
		else
		{	menuBar.setVisible(true);
    		topToolBar.setBounds(0, menuHeight, getSize().width, topHeight);
    		helpBar.setBounds(0, menuHeight + topHeight, getSize().width, helpHeight);
		}

		if (helpBarOptie && menuBar.isVisible())
			totalTopHeight = menuHeight + topHeight + helpHeight;
		else if (helpBarOptie && !menuBar.isVisible())
			totalTopHeight = topHeight + helpHeight;
		else if (!helpBarOptie && menuBar.isVisible())
			totalTopHeight = menuHeight + topHeight;
		else if (!helpBarOptie && !menuBar.isVisible())
			totalTopHeight = topHeight;
			
		validate();
		
	}

	public void zetHelpBarOptie(boolean b)
	{	helpBarOptie = b;
		helpBar.setVisible(helpBarOptie);
		if (helpBarOptie && menuBar.isVisible())
			totalTopHeight = menuHeight + topHeight + helpHeight;
		else if (helpBarOptie && !menuBar.isVisible())
			totalTopHeight = topHeight + helpHeight;
		else if (!helpBarOptie && menuBar.isVisible())
			totalTopHeight = menuHeight + topHeight;
		else if (!helpBarOptie && !menuBar.isVisible())
			totalTopHeight = topHeight;
			
		validate();
	}
	
	public void zetLijnTekenOptie(boolean b)
	{	lijnTekenOptie = b;
		if (!lijnTekenOptie)
			lijnVerlengOptie = false;
		topToolBar.layoutButtons();
	}

	public void zetLijnVerlengOptie(boolean b)
	{	lijnVerlengOptie = b;
		topToolBar.layoutButtons();
	}

	public void zetVlakTekenOptie(boolean b)
	{	vlakTekenOptie = b;
		if (!vlakTekenOptie)
		{
			evenwijdigVlakOptie = false;
			toonDoorsnedeOptie = false;
			splitsFiguurOptie = false;
		}
		topToolBar.layoutButtons();
	}
	
	public void zetEvenwijdigVlakOptie(boolean b)
	{	evenwijdigVlakOptie = b;
	topToolBar.layoutButtons();
	}
		
	public void zetToonDoorsnedeOptie(boolean b)
	{	toonDoorsnedeOptie = b;
		topToolBar.layoutButtons();
	}
	
	public void zetSplitsFiguurOptie(boolean b)
	{	splitsFiguurOptie = b;
		topToolBar.layoutButtons();
	}
	
	public void zetBouwplaatOptie(boolean b)
	{	bouwplaatOptie = b;
		//doorzienPanel.zetBouwplaatOptie(b);
	}
	
	
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
		
System.out.println("zetOpdracht");

		boolean demo = false;

		boolean figurenMenuOptie = true;
		boolean optiesMenuOptie = true;
		boolean helpBarOptie = true;
		
		boolean lijnTekenOptie = true;
		boolean lijnVerlengOptie = true;
		
		boolean vlakTekenOptie = true;
		boolean evenwijdigVlakOptie = true;
		boolean toonDoorsnedeOptie = true;
		boolean splitsFiguurOptie = true;
		
		boolean bouwplaatOptie = true;

		int figuurCode = CUBE;
		boolean letters = false;
		boolean hulpPunten = false;
		boolean centraleProjectie = true;

		if (b.containsKey("demo"))
			demo = ((Boolean) b.get("demo")).booleanValue();
		
		if (b.containsKey("figurenMenuOptie"))
			figurenMenuOptie = ((Boolean) b.get("figurenMenuOptie")).booleanValue();
		if (b.containsKey("optiesMenuOptie"))
			optiesMenuOptie = ((Boolean) b.get("optiesMenuOptie")).booleanValue();
		if (b.containsKey("helpBarOptie"))
			helpBarOptie = ((Boolean) b.get("helpBarOptie")).booleanValue();
		
		if (b.containsKey("lijnTekenOptie"))
			lijnTekenOptie = ((Boolean) b.get("lijnTekenOptie")).booleanValue();
		if (b.containsKey("lijnVerlengOptie"))
			lijnVerlengOptie = ((Boolean) b.get("lijnVerlengOptie")).booleanValue();
		
		if (b.containsKey("vlakTekenOptie"))
			vlakTekenOptie = ((Boolean) b.get("vlakTekenOptie")).booleanValue();
		if (b.containsKey("evenwijdigVlakOptie"))
			evenwijdigVlakOptie = ((Boolean) b.get("evenwijdigVlakOptie")).booleanValue();
		if (b.containsKey("toonDoorsnedeOptie"))
			toonDoorsnedeOptie = ((Boolean) b.get("toonDoorsnedeOptie")).booleanValue();
		if (b.containsKey("splitsFiguurOptie"))
			splitsFiguurOptie = ((Boolean) b.get("splitsFiguurOptie")).booleanValue();

		if (b.containsKey("bouwplaatFiguurOptie"))
			bouwplaatOptie = ((Boolean) b.get("bouwplaatOptie")).booleanValue();

		if (b.containsKey("figuurCode"))
			figuurCode = ((Integer) b.get("figuurCode")).intValue();
		if (b.containsKey("letters"))
			letters = ((Boolean) b.get("letters")).booleanValue();
		if (b.containsKey("hulpPunten"))
			hulpPunten = ((Boolean) b.get("hulpPunten")).booleanValue();
		if (b.containsKey("centraleProjectie"))
			centraleProjectie = ((Boolean) b.get("centraleProjectie")).booleanValue();

		zetDemo(demo);		
		
		zetFigurenMenuOptie(figurenMenuOptie);
		zetOptiesMenuOptie(optiesMenuOptie);
		zetHelpBarOptie(helpBarOptie);
		
		zetLijnTekenOptie(lijnTekenOptie);
		zetLijnVerlengOptie(lijnVerlengOptie);
		
		zetVlakTekenOptie(vlakTekenOptie);
		zetEvenwijdigVlakOptie(evenwijdigVlakOptie);
		zetToonDoorsnedeOptie(toonDoorsnedeOptie);
		
		zetBouwplaatOptie(bouwplaatOptie);
		

// wat als figuur veranderd is?		
		//drawingPanel.setNewModel(figuurCode);
		
		if (hulpPunten)
			drawingPanel.setHelpPointDrop(true);		    
		else
			drawingPanel.setHelpPointDrop(false);
		helpPuntenItem.setSelected(hulpPunten);
		drawingPanel.setLetters(letters);
		lettersItem.setSelected(letters);
		if (centraleProjectie)
		{	drawingPanel.setProjection(DrawingPanel.CENTRALPROJ);
			centraleProjectieItem.setSelected(true);
		}
		else
		{	drawingPanel.setProjection(DrawingPanel.PARALLELPROJ);
			parallelProjectieItem.setSelected(true);
		}
		
		setState(b);		
	}
	

	public void setEditState(Hashtable b)
	{
		
System.out.println("setEditState");

		boolean demo = false;

		boolean figurenMenuOptie = true;
		boolean optiesMenuOptie = true;
		boolean helpBarOptie = true;
		
		boolean lijnTekenOptie = true;
		boolean lijnVerlengOptie = true;
		
		boolean vlakTekenOptie = true;
		boolean evenwijdigVlakOptie = true;
		boolean toonDoorsnedeOptie = true;
		boolean splitsFiguurOptie = true;
		
		boolean bouwplaatOptie = true;

		//int figuurCode = CUBE;
		//boolean letters = false;
		//boolean hulpPunten = false;
		//boolean centraleProjectie = true;
		
		if (b.containsKey("demo"))
			demo = ((Boolean) b.get("demo")).booleanValue();
		
		if (b.containsKey("figurenMenuOptie"))
			figurenMenuOptie = ((Boolean) b.get("figurenMenuOptie")).booleanValue();
		if (b.containsKey("optiesMenuOptie"))
			optiesMenuOptie = ((Boolean) b.get("optiesMenuOptie")).booleanValue();
		if (b.containsKey("helpBarOptie"))
			helpBarOptie = ((Boolean) b.get("helpBarOptie")).booleanValue();
		
		if (b.containsKey("lijnTekenOptie"))
			lijnTekenOptie = ((Boolean) b.get("lijnTekenOptie")).booleanValue();
		if (b.containsKey("lijnVerlengOptie"))
			lijnVerlengOptie = ((Boolean) b.get("lijnVerlengOptie")).booleanValue();
		
		if (b.containsKey("vlakTekenOptie"))
			vlakTekenOptie = ((Boolean) b.get("vlakTekenOptie")).booleanValue();
		if (b.containsKey("evenwijdigVlakOptie"))
			evenwijdigVlakOptie = ((Boolean) b.get("evenwijdigVlakOptie")).booleanValue();
		if (b.containsKey("toonDoorsnedeOptie"))
			toonDoorsnedeOptie = ((Boolean) b.get("toonDoorsnedeOptie")).booleanValue();
		if (b.containsKey("splitsFiguurOptie"))
			splitsFiguurOptie = ((Boolean) b.get("splitsFiguurOptie")).booleanValue();

		if (b.containsKey("bouwplaatFiguurOptie"))
			bouwplaatOptie = ((Boolean) b.get("bouwplaatOptie")).booleanValue();

		//if (b.containsKey("figuurCode"))
		//	figuurCode = ((Integer) b.get("figuurCode")).intValue();
		//if (b.containsKey("letters"))
		//	letters = ((Boolean) b.get("letters")).booleanValue();
		//if (b.containsKey("hulpPunten"))
		//	hulpPunten = ((Boolean) b.get("hulpPunten")).booleanValue();
		//if (b.containsKey("centraleProjectie"))
		//	centraleProjectie = ((Boolean) b.get("centraleProjectie")).booleanValue();
		
		zetDemo(demo);

		zetFigurenMenuOptie(figurenMenuOptie);
		zetOptiesMenuOptie(optiesMenuOptie);
		zetHelpBarOptie(helpBarOptie);
		
		zetLijnTekenOptie(lijnTekenOptie);
		zetLijnVerlengOptie(lijnVerlengOptie);
		
		zetVlakTekenOptie(vlakTekenOptie);
		zetEvenwijdigVlakOptie(evenwijdigVlakOptie);
		zetToonDoorsnedeOptie(toonDoorsnedeOptie);
		
		zetBouwplaatOptie(bouwplaatOptie);
		

// wat als figuur veranderd is?		
		//drawingPanel.setNewModel(figuurCode);
		
/*		
		// true voegt de helpPointDrop toe maar laat
		// die niet zien
		if (hulpPunten)
			drawingPanel.setHelpPointDrop(true);		    
		else
			drawingPanel.setHelpPointDrop(false);
		helpPuntenItem.setSelected(hulpPunten);
		drawingPanel.setLetters(letters);
		lettersItem.setSelected(letters);
		if (centraleProjectie)
		{	drawingPanel.setProjection(DrawingPanel.CENTRALPROJ);
			centraleProjectieItem.setSelected(true);
		}
		else
		{	drawingPanel.setProjection(DrawingPanel.PARALLELPROJ);
			parallelProjectieItem.setSelected(true);
		}
*/		
		setState(b);

	}
	
	public Hashtable getEditState()
	{
		boolean demo = false;
		
		boolean figurenMenuOptie = true;
		boolean optiesMenuOptie = true;
		boolean helpBarOptie = true;
		
		boolean lijnTekenOptie = true;
		boolean lijnVerlengOptie = true;
		
		boolean vlakTekenOptie = true;
		boolean evenwijdigVlakOptie = true;
		boolean toonDoorsnedeOptie = true;
		boolean splitsFiguurOptie = true;
		
		boolean bouwplaatOptie = true;
		
		//int figuurCode = CUBE;
		//boolean letters = false;
		//boolean hulpPunten = false;
		//boolean centraleProjectie = true;

		demo = this.demo;
		
		figurenMenuOptie = this.figurenMenuOptie;
		optiesMenuOptie = this.optiesMenuOptie;
		helpBarOptie = this.helpBarOptie;
		
		lijnTekenOptie = this.lijnTekenOptie;
		lijnVerlengOptie = this.lijnVerlengOptie;
		
		vlakTekenOptie = this.vlakTekenOptie;
		evenwijdigVlakOptie = this.evenwijdigVlakOptie;
		toonDoorsnedeOptie = this.toonDoorsnedeOptie;
		splitsFiguurOptie = this.splitsFiguurOptie;
		
		bouwplaatOptie = this.splitsFiguurOptie;

		//figuurCode = this.figuurCode;
		//letters = this.letters;
		//hulpPunten = this.hulpPunten;
		//centraleProjectie = this.centraleProjectie;
		
		Hashtable h = getState();//new Hashtable();
		
		h.put("demo", new Boolean(demo));
		
		h.put("figurenMenuOptie", new Boolean(figurenMenuOptie));
		h.put("optiesMenuOptie", new Boolean(optiesMenuOptie));
		h.put("helpBarOptie", new Boolean(helpBarOptie));
		
		h.put("lijnTekenOptie", new Boolean(lijnTekenOptie));
		h.put("lijnVerlengOptie", new Boolean(lijnVerlengOptie));
		
		h.put("vlakTekenOptie", new Boolean(vlakTekenOptie));
		h.put("evenwijdigVlakOptie", new Boolean(evenwijdigVlakOptie));
		h.put("toonDoorsnedeOptie", new Boolean(toonDoorsnedeOptie));
		h.put("splitsFiguurOptie", new Boolean(splitsFiguurOptie));
		
		h.put("bouwplaatOptie", new Boolean(bouwplaatOptie));
		
		//h.put("figuurCode", new Integer(figuurCode));
		//h.put("letters", new Boolean(letters));
		//h.put("hulpPunten", new Boolean(hulpPunten));
		//h.put("centraleProjectie", new Boolean(centraleProjectie));
		
		return h;
	}	

	public Hashtable getState()
	{
		
		// de leerling KAN deze veranderd hebben
		boolean letters = this.letters;
		boolean hulpPunten = this.hulpPunten;
		boolean centraleProjectie = this.centraleProjectie;
		
		Hashtable h = new Hashtable();
		
		h.put("letters", new Boolean(letters));
		h.put("hulpPunten", new Boolean(hulpPunten));
		h.put("centraleProjectie", new Boolean(centraleProjectie));
		
		// status van de knoppen/het object
		int numLines = drawingPanel.numLines;
		int numPlanes = drawingPanel.numPlanes;
		boolean filled = drawingPanel.filled;
		boolean planesFilled = drawingPanel.planesFilled;
		
		h.put("numLines", new Integer(numLines));
		h.put("numPlanes", new Integer(numPlanes));
		h.put("filled", new Boolean(filled));
		h.put("planesFilled", new Boolean(planesFilled));
		
		int figuurCode = drawingPanel.modelCode;
		if ((drawingPanel.numLines > 0) || (drawingPanel.numPlanes > 0))
			figuurCode = MYFIGURE;
		h.put("figuurCode", new Integer(figuurCode));
		
		double lengthFactor = DrawConstants.llFactor;
		h.put("lengthFactor", new Double(lengthFactor));
		
		// drawingPanel.panel3D items
		Matrix3D mat = drawingPanel.panel3D.mat;
		int paintType = drawingPanel.panel3D.paintType;
		double zoomFactor = drawingPanel.panel3D.zoomFactor;
		boolean showInside = drawingPanel.panel3D.showInside;
		
		double[] coeff = NoSer.getMatrix3DState(mat);
		h.put("matrix3D", coeff);
		h.put("paintType", new Integer(paintType));
		h.put("zoomFactor", new Double(zoomFactor));
		h.put("showInside", new Boolean(showInside));
		
		Hashtable origObject = NoSer.getObject3DState(drawingPanel.originalObject);

		Vector construction = new Vector();
        if (drawingPanel.currentObjectGroup instanceof ObjectWithLine)
            construction = ((ObjectWithLine) drawingPanel.currentObjectGroup).getConstruction();
        else if (drawingPanel.currentObjectGroup instanceof ObjectWithPlane)
            construction = ((ObjectWithPlane) drawingPanel.currentObjectGroup).getConstruction();
        
        Vector conState = NoSer.getConstructionState(construction);
		
		h.put("origObject", origObject);
		h.put("conState", conState);
		
		int mode = drawingPanel.INERT;
		
		if ((drawingPanel.mouseMode == drawingPanel.FOLDOUT) && 
			(drawingPanel.startFacet != null))
		{
			mode = drawingPanel.FOLDOUT;
			
			// toestand originele object
			boolean oldFilled = drawingPanel.oldFilled;
			Matrix3D oldPos = drawingPanel.oldPos;
			double[] oldCoeff = NoSer.getMatrix3DState(oldPos);
			h.put("oldFilled", new Boolean(oldFilled));
			h.put("oldPos", oldCoeff);
			
			// toestand fold out
			boolean flattened = drawingPanel.flattened;
			double angle = drawingPanel.currentFoldOut;
			h.put("flattened", new Boolean(flattened));
			h.put("angle", new Double(angle));
			
			Facet3D theStartFacet = drawingPanel.startFacet;
			double[] startFacet = NoSer.getFacet3DVertexState(theStartFacet);
			h.put("startFacet", startFacet);
			
			//scormedObject3D.theFoldOutGroup = dp.foldOutObjectGroup;			
			//scormedObject3D.theFoldOutTreeRoot = dp.foldOutTreeRoot;
		}
		if ((drawingPanel.mouseMode == drawingPanel.CUTOBJECT) && 
			(drawingPanel.planeChoosen != null))
		{
			mode = drawingPanel.CUTOBJECT;
			// toestand originele object
			boolean oldPlanesFilled = drawingPanel.oldPlanesFilled;
			h.put("oldPlanesFilled", new Boolean("oldPlanesFilled"));
			// toestand cut object
			String volumeString = drawingPanel.panel3D.testString;
			h.put("volumeString", volumeString);
			
			Plane3D thePlaneChoosen = drawingPanel.planeChoosen;
			double[] planeChoosen = NoSer.getPlane3DState(thePlaneChoosen);
			h.put("planeChoosen", planeChoosen);
			
			//scormedObject3D.theCutObjectGroup = dp.cutObjectGroup;
		}
		
		
		
		h.put("mode", new Integer(mode));
/*		
		// dit moet je altijd doen!!
		// creeer
		scormedObject3D = new ScormedObject3D();
		
		if (DrawConstants.TICKNUM > 0)
			dp.setHelpPoints(0);
		
		scormedObject3D.theObjectGroup = dp.currentObjectGroup;
		

		if ((dp.mouseMode == dp.FOLDOUT) && (dp.startFacet != null))
		{	
			scormedObject3D.theFoldOutGroup = dp.foldOutObjectGroup;			
			scormedObject3D.theFoldOutTreeRoot = dp.foldOutTreeRoot;
			scormedObject3D.theStartFacet = dp.startFacet;		
		}
		if ((dp.mouseMode == dp.CUTOBJECT) && (dp.planeChoosen != null))
		{	
			scormedObject3D.theCutObjectGroup = dp.cutObjectGroup;

		}

		// zet viewPanel items
		viewPanel3D.testString = "";
		//setSlider(false, 0, 0, 0);
		if (scormedObject3D.mode == dp.FOLDOUT)
		{	viewGroup3D = (ObjectGroup3D) scormedObject3D.theFoldOutGroup.deepCopy();
//			setStartFacetCopy(viewGroup3D);
			//setSlider(true, scormedObject3D.angle, 0, 1);
			// niet nodig?
//			flattened 
		
			vouwSlider.zetStand((int)Math.round((scormedObject3D.angle * vouwSlider.geeflengte())));
		}
		else if (scormedObject3D.mode == dp.CUTOBJECT)
		{	viewGroup3D = (ObjectGroup3D) scormedObject3D.theCutObjectGroup.deepCopy();
			viewPanel3D.testString = scormedObject3D.volumeString;
		}
		else	
		{	viewGroup3D = (ObjectGroup3D) scormedObject3D.theObjectGroup.deepCopy();			
		}
			
		
*/		
		
		
		return h;
	}
	
	public void setState(Hashtable b)
	{
		
System.out.println("setState");

		boolean letters = false;
		boolean hulpPunten = false;
		boolean centraleProjectie = true;

		if (b.containsKey("letters"))
			letters = ((Boolean) b.get("letters")).booleanValue();
		if (b.containsKey("hulpPunten"))
			hulpPunten = ((Boolean) b.get("hulpPunten")).booleanValue();
		if (b.containsKey("centraleProjectie"))
			centraleProjectie = ((Boolean) b.get("centraleProjectie")).booleanValue();

		// true voegt de helpPointDrop toe maar laat
		// die niet zien
		if (hulpPunten)
			drawingPanel.setHelpPointDrop(true);		    
		else
			drawingPanel.setHelpPointDrop(false);
		helpPuntenItem.setSelected(hulpPunten);
		
		drawingPanel.setLetters(letters);

		lettersItem.setSelected(letters);

		if (centraleProjectie)
		{	drawingPanel.setProjection(DrawingPanel.CENTRALPROJ);
			centraleProjectieItem.setSelected(true);
		}
		else
		{	drawingPanel.setProjection(DrawingPanel.PARALLELPROJ);
			parallelProjectieItem.setSelected(true);
		}

		int figuurCode = CUBE;
		if (b.containsKey("figuurCode"))
			figuurCode = ((Integer) b.get("figuurCode")).intValue();
		
		selectItem(figuurCode);
		
		
		int numLines = 0;
		int numPlanes = 0;
		boolean filled = false;
		boolean planesFilled = false;

		if (b.containsKey("numLines"))
			numLines = ((Integer) b.get("numLines")).intValue();
		if (b.containsKey("numPlanes"))
			numPlanes = ((Integer) b.get("numPlanes")).intValue();
		if (b.containsKey("filled"))
			filled = ((Boolean) b.get("filled")).booleanValue();
		if (b.containsKey("planesFilled"))
			planesFilled = ((Boolean) b.get("planesFilled")).booleanValue();
		
		// dit enabled/disabled de lijn knoppen
		drawingPanel.setNumLines(numLines);
		// dit enabled/disabled de vlak knoppen
		drawingPanel.setNumPlanes(numPlanes);
		if (filled)
			rightToolBar.wireSolidButton.setImage(getImage("wireframe.gif"));
		if (planesFilled)
			topToolBar.planesFilledButton.setImage(getImage("planesempty.gif"));
		
		double lengthFactor = 0;
		if (b.containsKey("lengthFactor"))
			lengthFactor = ((Double) b.get("lengthFactor")).doubleValue();

		DrawConstants.llFactor = lengthFactor;
		// dit betekent dat er zeker lijnen zijn!
		if (lengthFactor > 0)
		{	topToolBar.shortLinesButton.setImage(getImage("shortLines.gif"));		
			topToolBar.shortLinesButton.enabled = true;
            if (lengthFactor >= (drawingPanel.MAXLLFACTOR - drawingPanel.LLSTEP / 10))
                topToolBar.lengLinesButton.setOn(false);    
		}
		
		Matrix3D mat = new Matrix3D();
		double[] coeff = new double[9]; 
		int paintType = Object3DContainer.PUREZ;
		double zoomFactor = 9e-1d;
		boolean showInside = true;
		
		if (b.containsKey("matrix3D"))
			coeff = (double[]) b.get("matrix3D");
		mat = NoSer.setMatrix3DState(coeff);
		if (b.containsKey("paintType"))
			paintType = ((Integer) b.get("paintType")).intValue();
		if (b.containsKey("zoomFactor"))
			zoomFactor = ((Double) b.get("zoomFactor")).doubleValue();
		if (b.containsKey("showInside"))
			showInside = ((Boolean) b.get("showInside")).booleanValue();

		// moet dit VOOR of NA het creeeren van het Object3D?
		drawingPanel.panel3D.mat = mat;
		drawingPanel.panel3D.mat.setOrigin(
				drawingPanel.panel3D.getSize().width / 2,
				drawingPanel.panel3D.getSize().height / 2,
				0);
		drawingPanel.panel3D.paintType = paintType;					
		drawingPanel.panel3D.showInside = showInside;
		
		drawingPanel.panel3D.setZoomFactor(zoomFactor);					
		
		drawingPanel.zoom = zoomFactor;
        if (drawingPanel.zoom <= (drawingPanel.MINZOOM + drawingPanel.ZOOMSTEP / 10))
        {	rightToolBar.zoomOutButton.setOn(false);    
        }
        if (drawingPanel.zoom >= (drawingPanel.MAXZOOM - drawingPanel.ZOOMSTEP / 10))
        {   rightToolBar.zoomInButton.setOn(false);    
		}
		
        int mode = drawingPanel.INERT;
        if (b.containsKey("mode"))
        	mode = ((Integer) b.get("mode")).intValue();
        
        drawingPanel.mouseMode = mode;
        
		Hashtable origObject = new Hashtable();
		Vector conState = new Vector();
		
		if (b.containsKey("origObject"))
		{	origObject = (Hashtable) b.get("origObject");
		}
		if (b.containsKey("conState"))
		{	conState = (Vector) b.get("conState");
		}
		
		Object3D originalObject = NoSer.setObject3DState(origObject);
		Vector construction = NoSer.setConstructionState(conState);
		
		drawingPanel.currentObjectGroup = drawingPanel.rebuild(originalObject, construction, null);
		drawingPanel.originalObject = drawingPanel.currentObjectGroup.leftMostLeaf();

		if (drawingPanel.mouseMode == drawingPanel.FOLDOUT)
		{
			// toestand originele object
			boolean oldFilled = false;
			Matrix3D oldPos = new Matrix3D();
			double[] oldCoeff = new double[9];
			if (b.containsKey("oldFilled"))
				oldFilled = ((Boolean) b.get("oldFilled")).booleanValue();
			if (b.containsKey("oldPos"))
				oldCoeff = (double[]) b.get("oldPos");
			oldPos = NoSer.setMatrix3DState(oldCoeff);
			
			// toestand fold out
			boolean flattened = false;
			double angle = 2e-1d;
			if (b.containsKey("flattened"))
				flattened = ((Boolean) b.get("flattened")).booleanValue();
			if (b.containsKey("angle"))
				angle = ((Double) b.get("angle")).doubleValue();
			drawingPanel.flattened = flattened;
			drawingPanel.currentFoldOut = angle;
			
			Facet3D startFacet = null;
			double[] vertices = new double[0];;
			if (b.containsKey("startFacet"))
				vertices = (double[]) b.get("startFacet");
	
			startFacet = NoSer.setFacet3DVertexState(vertices);

			if (startFacet != null)
			{	
				//for (int i = 0; i < drawingPanel.originalObject.numFacets; i++)
				//{	if (Facet3D.isEqualTo(drawingPanel.originalObject.facets[i], startFacet) >= 0)
						drawingPanel.startFacet = startFacet; //drawingPanel.originalObject.facets[i];
				//}
				
				drawingPanel.makeFoldOut(0, true);
			}
			
		}
		else if (drawingPanel.mouseMode == drawingPanel.CUTOBJECT)
		{
			// toestand originele object
			boolean oldPlanesFilled = false;
			if (b.containsKey("oldPlanesFilled"))
				oldPlanesFilled = ((Boolean) b.get("oldPlanesFilled")).booleanValue();
			drawingPanel.oldPlanesFilled = oldPlanesFilled;

			// toestand cut object
			String volumeString = "";
			if (b.containsKey("volumeString"))
				volumeString = (String) b.get("volumeString");
			drawingPanel.panel3D.testString = volumeString;
			
			
			Plane3D planeChoosen = new Plane3D(1, 0, 0, 0);
			double[] planeChoosenCoeff = new double[9];
			if (b.containsKey("planeChoosen"))
				planeChoosenCoeff = (double[]) b.get("planeChoosen");
			planeChoosen = NoSer.setPlane3DState(planeChoosenCoeff);
			drawingPanel.planeChoosen = planeChoosen; 
			
			drawingPanel.figureCut = true;
			drawingPanel.cutObject(1, true);
			
		}
		else
			drawingPanel.panel3D.initializeModel(drawingPanel.currentObjectGroup, false);

		// dit moet NA het creeeren van het Object3D
		drawingPanel.setFilled(filled);
		drawingPanel.fillPlanes(planesFilled);
		
		drawingPanel.addToHistory();
		
		if (drawingPanel.mouseMode != drawingPanel.INERT)
		{
			rightToolBar.undoButton.setOn(false);
		}
		
/*		


		
		// drawingPanel.panel3D
		dp.panel3D.testString = "";
		if (dp.mouseMode == dp.FOLDOUT)
		{	
			dp.foldOutObjectGroup = scormedObject3D.theFoldOutGroup;
			dp.panel3D.initializeModel(scormedObject3D.theFoldOutGroup, false);					
		}
		else if (dp.mouseMode == dp.CUTOBJECT)
		{	
			dp.cutObjectGroup = scormedObject3D.theCutObjectGroup;
			dp.panel3D.initializeModel(scormedObject3D.theCutObjectGroup, false);					
			dp.panel3D.testString = scormedObject3D.volumeString;
		}
		else
			dp.panel3D.initializeModel(scormedObject3D.theObjectGroup, false);			

		

		// dit is altijd de currentObjectGroup
     	dp.addToHistory();
       	doorzienFrame.helpBar.setText(doorzienFrame.tt("rotateText"));

		// override standard here
		if (dp.mouseMode == dp.FOLDOUT)
		{	// toestand originele object
			dp.oldFilled = scormedObject3D.oldFilled;
			dp.oldPos = scormedObject3D.oldPos;
			// toestand fold out
			dp.currentFoldOut = scormedObject3D.angle;
			dp.foldOutObject = dp.foldOutObjectGroup.leftMostLeaf();			
			dp.foldOutTreeRoot = scormedObject3D.theFoldOutTreeRoot;
			dp.startFacet = scormedObject3D.theStartFacet;
			// gui status fold out
            doorzienFrame.enableOptions(false);
            doorzienFrame.topToolBar.disableLineButtons();
            doorzienFrame.topToolBar.disablePlaneButtons();
			doorzienFrame.topToolBar.undoButton.setOn(false);
			doorzienFrame.topToolBar.redoButton.setOn(false);
			

            dp.setSlider(true, dp.currentFoldOut, 0, 1);        
            // hier!
            dp.flattened = scormedObject3D.flattened;

            doorzienFrame.rightToolBar.conDrawButton.setImage(doorzienFrame.getImage("figure.gif"));
		}
		
		if (dp.mouseMode == dp.CUTOBJECT)
		{	// toestand originele object
			dp.oldPlanesFilled = scormedObject3D.oldPlanesFilled;
			// toestand cut object
			dp.figureCut = scormedObject3D.figureCut;
			dp.planeChoosen = scormedObject3D.planeChoosen;
			// gui status cut object
            doorzienFrame.helpBar.setText(doorzienFrame.tt("selectCutFigureText"));
            doorzienFrame.topToolBar.disableLineButtons();            
            doorzienFrame.topToolBar.disablePlaneButtons();
            doorzienFrame.topToolBar.cutButton.setOn(true); 
            
            //doorzienFrame.topToolBar.cutButton.setImage(DoorzienDWO.glue);  
            doorzienFrame.topToolBar.cutButton.setImage(doorzienFrame.getImage("glue.gif")); 
            doorzienFrame.topToolBar.undoButton.setOn(false);
            doorzienFrame.topToolBar.redoButton.setOn(false);
			
		}

*/		
			
		drawingPanel.panel3D.repaint();

	}

	public void selectItem(int code)
	{	
		if (code == OCTAHEDRON)
			achtvlakItem.setSelected(true);
		else if (code == BLOCK)
			balkItem.setSelected(true);
		else if (code == CYLINDER)
			cilinderItem.setSelected(true);
		else if (code == PIRHOUSE)
			piramideHuisItem.setSelected(true);
		else if (code == EDGEHOUSE)
			schildHuisItem.setSelected(true);	
		else if (code == CONE1)
			kegel1Item.setSelected(true);
		else if (code == CONE2)
			kegel2Item.setSelected(true);
		else if (code == CONE3)
			kegel3Item.setSelected(true);
		else if (code == CONE4)
			kegel4Item.setSelected(true);
		else if (code == CUBE)
			kubusItem.setSelected(true);
		else if (code == PIRAMID3)
			piramide3Item.setSelected(true);
		else if (code == PIRAMID4)
			piramide4Item.setSelected(true);
		else if (code == PIRAMID5)
			piramide5Item.setSelected(true);
		else if (code == PIRAMID6)
			piramide6Item.setSelected(true);
		else if (code == PIRAMID7)
			piramide7Item.setSelected(true);
		else if (code == PIRAMID8)
			piramide8Item.setSelected(true);	    
		else if (code == PRISM3)
			prisma3Item.setSelected(true);
		else if (code == PRISM4)
			prisma4Item.setSelected(true);
		else if (code == PRISM5)
			prisma5Item.setSelected(true);
		else if (code == PRISM6)
			prisma6Item.setSelected(true);
		else if (code == DODECAHEDRON)
			twaalfvlakItem.setSelected(true);
		else if (code == ICOSAHEDRON)
			twintigvlakItem.setSelected(true);	
		else if (code == TETRAHEDRON)
			viervlakItem.setSelected(true);
		else if (code == MYFIGURE)
			mijnFiguurItem.setSelected(true);
		
	}
	
	class MenuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == helpPuntenItem)
			{	hulpPunten = helpPuntenItem.getState();
				if (hulpPunten)
					drawingPanel.setHelpPointDrop(true);		    
				else
					drawingPanel.setHelpPointDrop(false);
			}
			else if (e.getSource() == lettersItem)
			{	letters = lettersItem.getState();
				drawingPanel.setLetters(letters);
			}
			else if (e.getSource() == centraleProjectieItem)
			{	centraleProjectie = centraleProjectieItem.isSelected();
				drawingPanel.setProjection(DrawingPanel.CENTRALPROJ);
			}
			else if (e.getSource() == parallelProjectieItem)
			{	centraleProjectie = centraleProjectieItem.isSelected();
				drawingPanel.setProjection(DrawingPanel.PARALLELPROJ);
				
			}
			
			else if (e.getSource() == achtvlakItem)
			{	//figuurCode = OCTAHEDRON;
				drawingPanel.setNewModel(OCTAHEDRON);
			}
			else if (e.getSource() == balkItem)
			{	//figuurCode = BLOCK;
				drawingPanel.setNewModel(BLOCK);
			}
			else if (e.getSource() == cilinderItem)
			{	//figuurCode = CYLINDER;
				drawingPanel.setNewModel(CYLINDER);
			}
			else if (e.getSource() == piramideHuisItem)
			{	//figuurCode = PIRHOUSE;
				drawingPanel.setNewModel(PIRHOUSE);
			}
			else if (e.getSource() == schildHuisItem)
			{	//figuurCode = EDGEHOUSE;
				drawingPanel.setNewModel(EDGEHOUSE);
			}
			else if (e.getSource() == kegel1Item)
			{	//figuurCode = CONE1;
				drawingPanel.setNewModel(CONE1);
			}
			else if (e.getSource() == kegel2Item)
			{	//figuurCode = CONE2;
				drawingPanel.setNewModel(CONE2);
			}
			else if (e.getSource() == kegel3Item)
			{	//figuurCode = CONE3;
				drawingPanel.setNewModel(CONE3);
			}
			else if (e.getSource() == kegel4Item)
			{	//figuurCode = CONE4;
				drawingPanel.setNewModel(CONE4);
			}
			else if (e.getSource() == kubusItem)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(CUBE);
			}
			else if (e.getSource() == piramide3Item)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(PIRAMID3);
			}
			else if (e.getSource() == piramide4Item)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(PIRAMID4);
			}
			else if (e.getSource() == piramide5Item)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(PIRAMID5);
			}
			else if (e.getSource() == piramide6Item)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(PIRAMID6);
			}
			else if (e.getSource() == piramide7Item)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(PIRAMID7);
			}
			else if (e.getSource() == piramide8Item)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(PIRAMID8);
			}
			else if (e.getSource() == prisma3Item)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(PRISM3);
			}
			else if (e.getSource() == prisma4Item)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(PRISM4);
			}
			else if (e.getSource() == prisma5Item)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(PRISM5);
			}
			else if (e.getSource() == prisma6Item)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(PRISM6);
			}
			else if (e.getSource() == twaalfvlakItem)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(DODECAHEDRON);
			}
			else if (e.getSource() == twintigvlakItem)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(ICOSAHEDRON);
			}
			else if (e.getSource() == viervlakItem)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(TETRAHEDRON);
			}
			else if (e.getSource() == mijnFiguurItem)
			{	//figuurCode = CUBE;
				drawingPanel.setNewModel(MYFIGURE);
			}
		}
	}
}
