package fi.doorziendwo;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

import fi.beans.appletutil.*;

// the main applet frame
// contains DrawingPanel (if needed subdivided)
// menu bar and toolbar(s)
public class DoorzienFrame extends Frame
{	// attributes
    
    // versions
    public static final int EPN = 0;
    public static final int FI = 1;
    public static int version = EPN;
    
    
    
    // the applet with starter button
	DoorzienDWO starter;
	
	ViewerIF viewer;
	
	// main menu bar
	OptionsMenuBar mainMenus;
	// combined action and itemlistener for Menu items
	MIL listener;
	// info frame
    InfoFrame info;
    // GUI components
    // buffering drawing
    DrawingPanel drawingPanel;
    // top toolbar
    TopToolBar topToolBar;
    // right toolbar
    RightToolBar rightToolBar;
    // help bar
    HelpBar helpBar;
    // big bar on top
    TopBar topBar;
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
    // constants for sizes  
    // initial frame size, used only once
    public int minWidth = 700;
    public int minHeight = 500;
    // top tool bar
    public int topHeight = 42;
    // right tool bar
    public int rightWidth = 57;
    // help bar
    public int helpHeight = 23;
    // top tool bar + help bar
    public int totalTopHeight = 65;
    // some offSet
    int offSet = 5;
    
    // colors for GUI
    public static Color 
    
        appletBackground = Color.lightGray,
        topBackground = new Color(222, 222, 222),
        rightBackground = new Color(222, 222, 222),        
        helpBackground = new Color(222, 222, 222),        
        buttonColor = Color.lightGray,
        workBackground = Color.white
        ;
        
// dit object construeren na inlezen parameters        
        
    // menu labels and menu items
    
	Object[] subNumHelpPoints =
	{   tt("divideSidesText"),
	    "-" + tt("twoPartsText"),
        "-" + tt("threePartsText"),
	    "-" + tt("fourPartsText"),        
	    "-" + tt("fivePartsText"),        
	    "-" + tt("sixPartsText"),
        "-" + tt("sevenPartsText"),
	    "-" + tt("eightPartsText"),        
	    "-" + tt("ninePartsText"),        
	    "-" + tt("tenPartsText"),                
	};
    
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
    
    
	Object[] subHelpPoints =
	{   tt("helpPointsText"),	        
	    subNumHelpPoints,	
	    "+" + tt("noHelpPointsText"),
//	    subNumHelpPoints,
/*	    
	    tt("divideSidesText"),
	    "-" + tt("twoPartsText"),
        "-" + tt("threePartsText"),
	    "-" + tt("fourPartsText"),        
	    "-" + tt("fivePartsText"),        
	    "-" + tt("sixPartsText"),
        "-" + tt("sevenPartsText"),
	    "-" + tt("eightPartsText"),        
	    "-" + tt("ninePartsText"),        
	    "-" + tt("tenPartsText"),                
*/	    
	};
    
    Object[] subHouses =
    {   tt("housesText"),
        "-" + tt("pirHouseText"),
        "-" + tt("edgeHouseText"),
    };    

    Object[] subCones =
    {   tt("conesText"),
        "-" + tt("cone1Text"),
        "-" + tt("cone2Text"),
        "-" + tt("cone3Text"),
        "-" + tt("cone4Text"),        
    };    
    
    Object[] subPiramids =
    {   tt("piramidsText"),
        "-" + tt("threePiramidText"),
        "-" + tt("fourPiramidText"),
        "-" + tt("fivePiramidText"),
        "-" + tt("sixPiramidText"),        
        "-" + tt("sevenPiramidText"),        
        "-" + tt("eightPiramidText"),                
    };    
    
    Object[] subPrisms =
    {   tt("prismsText"),
        "-" + tt("threePrismText"),
        "-" + tt("fourPrismText"),
        "-" + tt("fivePrismText"),
        "-" + tt("sixPrismText"),        
    };    
    
    Object[] figures = new Object[12];
    
	Object menuStructure[][] =
	{	// applet menu
	    { tt("appletText"), 
	      tt("stopText"), 
	      tt("infoText"), 
	    },
	    // placeholders for figure menu
//	    figures,
	    
	    {  "", // title & sufficient(!) items, now 11
	       "", "", "", "", "", "", "", "", "", "", "",
	    },
	    // options menu
	    {   tt("optionsText"),
	        //subHelpPoints,
	        "-" + tt("helpPointsText"),	        	        
	        "-" + tt("lettersText"),
	        "-",	        
	        "+" + tt("centralProjText"),
	        "-" + tt("parallelProjText"),
//	        "-",
//	        "-" + tt("lettersText"),
	        //tt("helpPointsText"),	        
//	        subHelpPoints,
	    }    
	    // more menus
	    
	};
	
	// copyright
	String[] copyRight =
	{   "20061004",
		tt("titelText"), 
        tt("developText"),
	    tt("fiText"),         
	    tt("forEPNText"), 
	    tt("programText"),
	    tt("copyRightText"), 
	    "",
	};

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
	
	// change this after reading parameter figures
	public int defaultFigure = CUBE;
	// the figure string
	String figureString = null;
	// max number of figures, nodig?
//	int numFigures = 9;
	Object[] figureNames =  {
	    tt("octahedronText"),
	    tt("blockText"),
	    tt("cylinderText"),
	    subHouses,
	    subCones,
	    tt("cubeText"),
	    subPiramids,
	    subPrisms,
        tt("dodecahedronText"),	    	    
	    tt("icosahedronText"),
	    tt("tetrahedronText"),

	};    
	    
	// constants for tools
	String toolString = null;
	// lines
	public static final int drLines = 0;
	public static final int drLeLines = 1;
	// planes
	public static final int drPlanes = 2;
	public static final int drParPlanes = 3;
	public static final int drPlanesShowCut = 4;
	public static final int drPlanesCut = 5;
    public static final int drParPlanesShowCut = 6;
	public static final int drParPlanesCut = 7;
	public static final int drPlanesCutShowCut = 8;
	public static final int drParPlanesCutShowCut = 9;	
	// other
	public static final int makeConDraw = 10;
	
	// maximum number possible
	int toolChoices = 10;
	
	// top, lines
	boolean drawLines = true;
	boolean removeLines = true;
	boolean lengLines = true;
	
	// top, planes
	boolean drawPlanes = true;
	boolean parPlanes = true;
	boolean removePlanes = true;
	boolean fillPlanes = true;
	boolean showCut = true;
	boolean cutObject = true;

	// right
	boolean conDraw = true;

	
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
	

    // constructor
	public DoorzienFrame(DoorzienDWO s, ViewerIF v, ScormedObject3D scormedObject3D)
	{	// default constructor super() gets called here
  	    setVisible(true);
	    starter = s;
	    viewer = v;
	    
	    // read all parameter strings
	    figureString = null;//starter.figureString;
	    toolString = null;//starter.toolString;

	    // find images for buttons
	    // right tool bar
	    
	    if(images==null)
		{	images = new Hashtable();
			DoorzienDWO.loadImages(images,imageNames);
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
	    
	    /*if(rotateImage==null)
	    {	AppletUtil au = new AppletUtil(starter);
	    	
	    	rotateImage = au.getImage("resources/rotate.gif");	    
			wireFrameImage = au.getImage("resources/wireframe.gif");	    
			solidImage = au.getImage("resources/solid.gif");	    
			zoomInImage = au.getImage("resources/zoomin.gif");	    
			zoomInOffImage = au.getImage("resources/zoominoff.gif");	    		
			zoomOutImage = au.getImage("resources/zoomout.gif");	    
			zoomOutOffImage = au.getImage("resources/zoomoutoff.gif");	    		
			conDrawImage = au.getImage("resources/condraw.gif");	    				
	        figureImage = au.getImage("resources/figure.gif");	    						
	
			drawLineImage = au.getImage("resources/drawline.gif");	    						
			drawLineOffImage = au.getImage("resources/drawlineoff.gif");	    								
			deleteLineImage = au.getImage("resources/deleteline.gif");	    						
			deleteLineOffImage = au.getImage("resources/deletelineoff.gif");	    								
			lengLinesImage = au.getImage("resources/lenglines.gif");	    								
			lengLinesOffImage = au.getImage("resources/lenglinesoff.gif");	    										
			shortLinesImage = au.getImage("resources/shortlines.gif");	    								
			shortLinesOffImage = au.getImage("resources/shortlinesoff.gif");	    										
			
			drawPlaneImage = au.getImage("resources/drawplane.gif");	    										
			drawPlaneOffImage = au.getImage("resources/drawplaneoff.gif");	    										
			parPlaneImage = au.getImage("resources/parplane.gif");	    												
			parPlaneOffImage = au.getImage("resources/parplaneoff.gif");	    												
			deletePlaneImage = au.getImage("resources/deleteplane.gif");	    										
			deletePlaneOffImage = au.getImage("resources/deleteplaneoff.gif");	    										
			planesFilledImage = au.getImage("resources/planesfilled.gif");	    										
			planesFilledOffImage = au.getImage("resources/planesfilledoff.gif");	    										
	        planesEmptyImage = au.getImage("resources/planesempty.gif");	    												
			transPlaneImage = au.getImage("resources/transplane.gif");	    												
			transPlaneOffImage = au.getImage("resources/transplaneoff.gif");	    												
			noTransPlaneImage = au.getImage("resources/notransplane.gif");	    														
			rotPlaneImage = au.getImage("resources/rotplane.gif");	    												
			rotPlaneOffImage = au.getImage("resources/rotplaneoff.gif");	    												
			noRotPlaneImage = au.getImage("resources/norotplane.gif");	    														
			showCutImage = au.getImage("resources/showcut.gif");	    												
			showCutOffImage = au.getImage("resources/showcutoff.gif");	    												
			hideCutImage = au.getImage("resources/hidecut.gif");	    												
			cutImage = au.getImage("resources/cut.gif");	    												
			cutOffImage = au.getImage("resources/cutoff.gif");	    												
			glueImage = au.getImage("resources/glue.gif");	    														
			undoImage = au.getImage("resources/undo.gif");	    																
			undoOffImage = au.getImage("resources/undooff.gif");	    																		
			redoImage = au.getImage("resources/redo.gif");	    																
			redoOffImage = au.getImage("resources/redooff.gif");	    																		
			
			epnImage = au.getImage("resources/EPNlogo.gif");	 
	    }
*/
	    
		setTitle(tt("titelText"));
        // create instance of MenuItem Action and ItemListener here and pass
        // it as its common interface type EventListener!
        listener = new MIL();
	    // create and set(!) menus
	    // figure menu
	    // full menu wanted, default is cube
	    if ((figureString == null) || figureString.equals(""))
	    {   defaultFigure = CUBE;
	        // title
	        menuStructure[1][0] = tt("figureText"); 
	        // items
//  	        menuStructure[1][1] = "+" + figureNames[0];	        
	        for (int i = 0; i < figureNames.length; i++)
	        {   if (figureNames[i] instanceof String)
          	        menuStructure[1][i + 1] = "-" + figureNames[i];	        
	            else
          	        menuStructure[1][i + 1] = figureNames[i];
      	    }    
      	        

	    }
	    else // process choice
	    {   Vector figureNumbers = processSelection(figureString);
	        // remove wrong numbers, start from the back!
	        for (int i = figureNumbers.size() - 1; i >= 0; i--)
	        {   int number = ((Integer) figureNumbers.elementAt(i)).intValue();
	            if (number >= figureNames.length)
	                figureNumbers.removeElementAt(i);
	        }    
	        // just in case
	        if (figureNumbers.size() == 0)
	        {	if (scormedObject3D == null)
	        	{	figureNumbers.addElement(new Integer(CUBE));
            		//defaultFigure = ((Integer) figureNumbers.elementAt(0)).intValue();	        	
	        	}
	        }
	        if (scormedObject3D == null)
            	defaultFigure = ((Integer) figureNumbers.elementAt(0)).intValue();
	        // title
	        menuStructure[1][0] = tt("figureText"); 
	        // items
//  	        menuStructure[1][1] = "+" + figureNames[defaultFigure];	        
	        for (int j = 0; j < figureNumbers.size(); j++)
	        {   int figNum = ((Integer) figureNumbers.elementAt(j)).intValue();
	            if (figureNames[figNum] instanceof String)
          	        menuStructure[1][j + 1] = "-" + figureNames[figNum];	        
          	    else
          	        menuStructure[1][j + 1] = figureNames[figNum];	                  	    
      	    }    
	    
	    }
	    
  	    if ((toolString != null) && !toolString.equals(""))
  	    {   Vector toolNumbers = processSelection(toolString);
	        // remove wrong numbers, start from the back!
	        for (int i = toolNumbers.size() - 1; i >= 0; i--)
	        {   int number = ((Integer) toolNumbers.elementAt(i)).intValue();
	            if (number > toolChoices)
	                toolNumbers.removeElementAt(i);
	        }  
	        // lines
	        if (toolNumbers.contains(new Integer(drLeLines)))
	        {   //drawLines = true;
	            //removeLines = true;
	            //lengLines = true;
	        }    
	        else if (toolNumbers.contains(new Integer(drLines)))
	        {   //drawLines = true;
	            //removeLines = true;	        
	            lengLines = false;
	        }    
  	        else
            {   drawLines = false;
                removeLines = false;
   	            lengLines = false;
            }    
	        // planes
	        if (toolNumbers.contains(new Integer(drParPlanesCutShowCut)))	        
            {   //drawPlanes = true;
                //parPlanes = true;
	            //removePlanes = true;
	            //fillPlanes = true;
	            //showCut = true;
	            //cutObject = true;
	        }
	        else if (toolNumbers.contains(new Integer(drParPlanesCut)))	        	        
	        {   //drawPlanes = true;
	            //parPlanes = true;
	            //removePlanes = true;
	            //fillPlanes = true;
	            showCut = false;
	            //cutObject = true;
	        }
	        else if (toolNumbers.contains(new Integer(drParPlanesShowCut)))	        	        
	        {   //drawPlanes = true;
	            //parPlanes = true;
	            //removePlanes = true;
	            //fillPlanes = true;
	            //showCut = true;
	            cutObject = false;
	        }
	        else if (toolNumbers.contains(new Integer(drPlanesCutShowCut)))	        
            {   //drawPlanes = true;
                parPlanes = false;
	            //removePlanes = true;
	            //fillPlanes = true;
	            //showCut = true;
	            //cutObject = true;
	        }
	        else if (toolNumbers.contains(new Integer(drParPlanes)))	        	        
	        {   //drawPlanes = true;
	            //parPlanes = true;
	            //removePlanes = true;
	            //fillPlanes = true;
	            showCut = false;
	            cutObject = false;
	        }
	        else if (toolNumbers.contains(new Integer(drPlanesShowCut)))	        	        
	        {   //drawPlanes = true;
	            parPlanes = false;
	            //removePlanes = true;
	            //fillPlanes = true;
	            //showCut = true;
	            cutObject = false;
	        }
	        else if (toolNumbers.contains(new Integer(drPlanesCut)))	        	        
	        {   //drawPlanes = true;
	            parPlanes = false;
	            //removePlanes = true;
	            //fillPlanes = true;
	            showCut = false;
	            //cutObject = true;
	        }
	        
	        
	        else if (toolNumbers.contains(new Integer(drPlanes)))	        	        
	        {   //drawPlanes = true;
	            parPlanes = false;
	            //removePlanes = true;
	            //fillPlanes = true;
	            showCut = false;
	            cutObject = false;
	        }
	        else 
	        {   drawPlanes = false;
	            parPlanes = false;
	            removePlanes = false;
	            fillPlanes = false;
	            showCut = false;
	            cutObject = false;
	        }
	        
	        // cut-out
	        if (toolNumbers.contains(new Integer(makeConDraw)))	        	        
	        {   //conDraw = true;
	        }
	        else
	            conDraw = false;
	    }
	    // create menu structure and menus
		mainMenus = new OptionsMenuBar(menuStructure, listener);
		setMenuBar(mainMenus);
        OptionsMenu m = mainMenus.getMenu(tt("figureText"));		
        OptionsMenu subMenu = null;

        // figure selection
        if (scormedObject3D == null)
        {
	        if (figureNames[defaultFigure] instanceof String)
    			m.switchto((String) figureNames[defaultFigure]);
		}
		// check for submenus
	    String defaultString = "";	
		if (defaultFigure == HOUSES)
		{   defaultFigure = PIRHOUSE;
		    defaultString = tt("pirHouseText");
		    subMenu = (OptionsMenu) m.getItem(tt("housesText"));
		}    
		if (defaultFigure == CONES)
		{   defaultFigure = CONE1;
		    defaultString = tt("cone1Text");
		    subMenu = (OptionsMenu) m.getItem(tt("conesText"));		    
		}    
		if (defaultFigure == PIRAMIDS)
		{   defaultFigure = PIRAMID3;
		    defaultString = tt("threePiramidText");
		    subMenu = (OptionsMenu) m.getItem(tt("piramidsText"));		    
		}    
		if (defaultFigure == PRISMS)
		{   defaultFigure = PRISM3;
		    defaultString = tt("threePrismText");		    
		    subMenu = (OptionsMenu) m.getItem(tt("prismsText"));		    		    
        }		    
		if (subMenu != null)
		    subMenu.switchto(defaultString);
		
		
		if (scormedObject3D != null)
		{	m = mainMenus.getMenu(tt("figureText"));		
			CheckboxMenuItem cmItem = new CheckboxMenuItem(tt("myFigureText"));
			cmItem.addItemListener(listener);
			cmItem.setState(false);
			m.add(cmItem);
			m.switchto(tt("myFigureText"));			
		}	
        // layout and size
        // use BorderLayout so that Menubar is included in the layout
		setLayout(new BorderLayout());
		
		setSize(minWidth, minHeight); 
        // create and add GUI compoments
        drawingPanel = new DrawingPanel(this);
        add(drawingPanel, BorderLayout.CENTER);        
        
// hier de ToolKit raadplegen voor de systemcolors        
        
        
        // changing order irrelevant
        topBar = new TopBar(this);
        topBar.setLayout(new BorderLayout());
        
        // topToolBar gets total width
        topToolBar = new TopToolBar(this);
        topBar.add(topToolBar, BorderLayout.NORTH);        
        
        helpBar = new HelpBar(this);
        topBar.add(helpBar, BorderLayout.SOUTH);        

        add(topBar, BorderLayout.NORTH);        
        
        rightToolBar = new RightToolBar(this);
        add(rightToolBar, BorderLayout.EAST);        
        
        // for BorderLayout
        validate();
        
        // now sizes are known, so initialize
        rightToolBar.initialize();
        topToolBar.initialize();
        if (scormedObject3D == null)
        	drawingPanel.initialize(defaultFigure);
        else
        	drawingPanel.initialize(MYFIGURE);	

        helpBar.setText(tt("rotateText"));
        // add listener for closing event
		WL wListener = new WL();
		addWindowListener(wListener);
		CL cListener = new CL();
		addComponentListener(cListener);
		

	} // constructor
	
	public static Image getImage(String name)
	{	return(Image)images.get(name);
	}
	
    public Vector processSelection(String s)
    {   Vector result = new Vector();
        s = removeAllBlanks(s);
        int kommaIndex = s.indexOf(',');
        if (kommaIndex < 0) // single number
        {   result = processBlock(s);
            return result;
        }    
        else
        {   while (kommaIndex >= 0)
            {   String b = "";
                if (kommaIndex > 0)
                    b = s.substring(0, kommaIndex);
                String remainder = "";
                if (kommaIndex < (s.length() - 1))
                    remainder = s.substring(kommaIndex + 1);
                if (!b.equals(""))    
                {   Vector bResult = processBlock(b);
                    for (int i = 0; i < bResult.size(); i++)
                        result.addElement(bResult.elementAt(i));    
                }
                s = remainder;
                kommaIndex = s.indexOf(',');
            }  // while
            // last part
            if (!s.equals(""))
            {   Vector sResult = processBlock(s);
                for (int i = 0; i < sResult.size(); i++)
                    result.addElement(sResult.elementAt(i));    
            }
        } // else
        return result;
    }
    
    public Vector processBlock(String b)
    {   Vector result = new Vector();
        int hyphenIndex = b.indexOf('-');
        if (hyphenIndex > 0) // range
        {   String b1 = b.substring(0, hyphenIndex);
            String b2 = "";
            if (b.length() > (hyphenIndex + 1))
                b2 = b.substring(hyphenIndex + 1);
            boolean error1 = false;
            int num1 = 0;
            try
            {   num1 = Integer.parseInt(b1);
            }
            catch (NumberFormatException nfe)
            {   error1 = true;
            }    
            boolean error2 = false;
            int num2 = 0;
            try
            {   num2 = Integer.parseInt(b2);
            }
            catch (NumberFormatException nfe)
            {   error2 = true;
            }    
            if (!error1 && !error2 && (num1 >= 0) && (num2 >= 0))
            {   for (int i = num1; i <= num2; i++)
                result.addElement(new Integer(i));                        
            }    
            else if (error1 && !error2 && (num2 >= 0))
                result.addElement(new Integer(num2));                                    
            else if (!error1 && error2 && (num1 >= 0))
                result.addElement(new Integer(num1));                                    
                
                
                
        }    
        else if (hyphenIndex == 0) // wrong
        {   String bt = "";
            if (b.length() > 1)
                bt = b.substring(1);
            boolean error = false;
            int num = 0;
            try
            {   num = Integer.parseInt(bt);
            }
            catch (NumberFormatException nfe)
            {   error = true;
            }    
            if (!error && (num >= 0))
                result.addElement(new Integer(num));        
        }    
        else // b must be a number
        {   boolean error = false;
            int num = 0;
            try
            {   num = Integer.parseInt(b);
            }
            catch (NumberFormatException nfe)
            {   error = true;
            }    
            if (!error && (num >= 0))
                result.addElement(new Integer(num));
        }    
        return result;
    }    
    
    public String removeAllBlanks(String s)
    {           int index = s.indexOf(' ');
        while (index >= 0)
        {   s = s.substring(0, index) + s.substring(index + 1);
            index = s.indexOf(' ');
        }
        return s;
    }
	
	
	
	
	
    // shortcut for tablelookup
	public String tt(String s)
	{   return Table.lookUp(s);
	}
	// process menu choices
    public void processMenuChoice(MenuItem choosen)
    {   if (choosen instanceof CheckboxMenuItem)
            choosen = (CheckboxMenuItem) choosen;
        String choice = choosen.getLabel();
        
        // menu appletText (not exclusive)
		if ( choice.equals(tt("stopText")) )
		{   
			viewer.getScormedObject3D();	        		
			dispose();
            viewer.restart();
        }
		else if ( choice.equals(tt("infoText")) )
		{   info = new InfoFrame(tt("infoText"), copyRight, epnImage);
		    info.setVisible(true);
		    // infoFrame kills itselve
		}    
		
		// figure menu (exclusive)


		else if ( choice.equals(tt("myFigureText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("myFigureText"));
            processSubMenuSelection(null);		    
		    drawingPanel.setNewModel(MYFIGURE);
		}    



		else if ( choice.equals(tt("cubeText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("cubeText"));
            processSubMenuSelection(null);		    
		    drawingPanel.setNewModel(CUBE);
		}    
		else if ( choice.equals(tt("blockText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("blockText"));
		    processSubMenuSelection(null);		    
		    drawingPanel.setNewModel(BLOCK);		    
		}    
		else if ( choice.equals(tt("tetrahedronText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("tetrahedronText"));
		    processSubMenuSelection(null);		    
		    drawingPanel.setNewModel(TETRAHEDRON);		    
		}    
		else if ( choice.equals(tt("octahedronText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("octahedronText"));
		    processSubMenuSelection(null);		    
		    drawingPanel.setNewModel(OCTAHEDRON);		    
		}    
		else if ( choice.equals(tt("threePiramidText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("piramidsText"));
		    processSubMenuSelection(tt("threePiramidText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("threePiramidText"));
		    drawingPanel.setNewModel(PIRAMID3);		    
		}    
		else if ( choice.equals(tt("fourPiramidText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("piramidsText"));
		    processSubMenuSelection(tt("fourPiramidText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("fourPiramidText"));
		    drawingPanel.setNewModel(PIRAMID4);		    
		}    
		else if ( choice.equals(tt("fivePiramidText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("piramidsText"));
		    processSubMenuSelection(tt("fivePiramidText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("fivePiramidText"));
		    drawingPanel.setNewModel(PIRAMID5);		    
		}    
		else if ( choice.equals(tt("sixPiramidText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("piramidsText"));
		    processSubMenuSelection(tt("sixPiramidText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("sixPiramidText"));
		    drawingPanel.setNewModel(PIRAMID6);		    
		}    
		else if ( choice.equals(tt("sevenPiramidText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("piramidsText"));
		    processSubMenuSelection(tt("sevenPiramidText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("sixPiramidText"));
		    drawingPanel.setNewModel(PIRAMID7);		    
		}    
		else if ( choice.equals(tt("eightPiramidText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("piramidsText"));
		    processSubMenuSelection(tt("eightPiramidText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("sixPiramidText"));
		    drawingPanel.setNewModel(PIRAMID8);		    
		}    

		else if ( choice.equals(tt("threePrismText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("prismsText"));
		    processSubMenuSelection(tt("threePrismText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("threePiramidText"));
		    drawingPanel.setNewModel(PRISM3);		    
		}    
		else if ( choice.equals(tt("fourPrismText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("prismsText"));
		    processSubMenuSelection(tt("fourPrismText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("fourPiramidText"));
		    drawingPanel.setNewModel(PRISM4);		    
		}    
		else if ( choice.equals(tt("fivePrismText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("prismsText"));
		    processSubMenuSelection(tt("fivePrismText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("fivePiramidText"));
		    drawingPanel.setNewModel(PRISM5);		    
		}    
		else if ( choice.equals(tt("sixPrismText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("prismsText"));
		    processSubMenuSelection(tt("sixPrismText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("sixPiramidText"));
		    drawingPanel.setNewModel(PRISM6);		    
		}    

		
		else if ( choice.equals(tt("pirHouseText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("housesText"));
		    processSubMenuSelection(tt("pirHouseText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("sixPiramidText"));
		    drawingPanel.setNewModel(PIRHOUSE);		    
		}    
		
		else if ( choice.equals(tt("edgeHouseText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("housesText"));
		    processSubMenuSelection(tt("edgeHouseText"));		    
//		    m = (OptionsMenu) choosen.getParent();
//		    m.switchto(tt("sixPiramidText"));
		    drawingPanel.setNewModel(EDGEHOUSE);		    
		}    

		
		else if ( choice.equals(tt("dodecahedronText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("dodecahedronText"));
		    processSubMenuSelection(null);		    		    
		    drawingPanel.setNewModel(DODECAHEDRON);		    
		}    
		else if ( choice.equals(tt("icosahedronText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("icosahedronText"));
		    processSubMenuSelection(null);		    		    
		    drawingPanel.setNewModel(ICOSAHEDRON);		    
		}    
		else if ( choice.equals(tt("cylinderText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("cylinderText"));
		    processSubMenuSelection(null);		    		    
		    drawingPanel.setNewModel(CYLINDER);		    
		}    
		else if ( choice.equals(tt("cone1Text")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("conesText"));
		    processSubMenuSelection(tt("cone1Text"));
		    //m = (OptionsMenu) choosen.getParent();
		    //m.switchto(tt("cone1Text"));
		    drawingPanel.setNewModel(CONE1);		    
		}    
		else if ( choice.equals(tt("cone2Text")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("conesText"));
		    processSubMenuSelection(tt("cone2Text"));
		    //m = (OptionsMenu) choosen.getParent();
		    //m.switchto(tt("cone2Text"));
		    drawingPanel.setNewModel(CONE2);		    
		}    
		else if ( choice.equals(tt("cone3Text")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("conesText"));
		    processSubMenuSelection(tt("cone3Text"));
		    //m = (OptionsMenu) choosen.getParent();
		    //m.switchto(tt("cone3Text"));
		    drawingPanel.setNewModel(CONE3);		    
		}    
		else if ( choice.equals(tt("cone4Text")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
		    m.switchto(tt("conesText"));
		    processSubMenuSelection(tt("cone4Text"));
		    //m = (OptionsMenu) choosen.getParent();
		    //m.switchto(tt("cone3Text"));
		    drawingPanel.setNewModel(CONE4);		    
		}    

// andere figuren		
		
		
		// options menu
		else if ( choice.equals(tt("centralProjText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
		    m.switchto(tt("centralProjText"));
		    drawingPanel.setProjection(DrawingPanel.CENTRALPROJ);		    
		}    
		else if ( choice.equals(tt("parallelProjText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
		    m.switchto(tt("parallelProjText"));
		    drawingPanel.setProjection(DrawingPanel.PARALLELPROJ);		    
		}    
		
		else if ( choice.equals(tt("lettersText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
    	    CheckboxMenuItem cmi = (CheckboxMenuItem) m.getItem(tt("lettersText"));		
		    drawingPanel.setLetters(cmi.getState());		    
		}    
		
		else if ( choice.equals(tt("helpPointsText")) )
		{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
    	    CheckboxMenuItem cmi = (CheckboxMenuItem) m.getItem(tt("helpPointsText"));		
    	    boolean points = cmi.getState();
    	    if (points)
    		    drawingPanel.setHelpPointDrop(true);		    
    		else
    		    drawingPanel.setHelpPointDrop(false);
		}    
		
		
		
	} // processMenuChoice

    public void processSubMenuSelection(String selection)
    {   OptionsMenu m = mainMenus.getMenu(tt("figureText"));
        OptionsMenu subHouses = (OptionsMenu) m.getItem(tt("housesText"));
        OptionsMenu subCones = (OptionsMenu) m.getItem(tt("conesText"));
        OptionsMenu subPiramids = (OptionsMenu) m.getItem(tt("piramidsText"));
        OptionsMenu subPrisms = (OptionsMenu) m.getItem(tt("prismsText"));        
        if (selection == null)
        {   if (subHouses != null)
                subHouses.switchoff();
            if (subCones != null)                
                subCones.switchoff();
            if (subPiramids != null)    
                subPiramids.switchoff();
            if (subPrisms != null)    
                subPrisms.switchoff();
        }    
        else // submenu selection
        {   if (subHouses.getItem(selection) != null)
            {   subHouses.switchto(selection);
                if (subCones != null)                            
                    subCones.switchoff();
                if (subPiramids != null)                        
                    subPiramids.switchoff();
                if (subPrisms != null)                        
                    subPrisms.switchoff();
            }    
            else if (subCones.getItem(selection) != null)
            {   subCones.switchto(selection);
                if (subHouses != null)
                    subHouses.switchoff();
                if (subPiramids != null)                        
                    subPiramids.switchoff();
                if (subPrisms != null)                        
                    subPrisms.switchoff();
            }
            else if (subPiramids.getItem(selection) != null)
            {   subPiramids.switchto(selection);
                if (subHouses != null)
                    subHouses.switchoff();
                if (subCones != null)                                    
                    subCones.switchoff();
                if (subPrisms != null)                        
                    subPrisms.switchoff();
            }
            else if (subPrisms.getItem(selection) != null)
            {   subPrisms.switchto(selection);
                if (subHouses != null)            
                    subHouses.switchoff();
                if (subCones != null)                                    
                    subCones.switchoff();
                if (subPiramids != null)                        
                    subPiramids.switchoff();
            }
        }    
    }    
	
    public void resetProjection(int proj)
    {   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
        if (proj == drawingPanel.CENTRALPROJ)
            m.switchto(tt("centralProjText"));
        else if (proj == drawingPanel.PARALLELPROJ)    
            m.switchto(tt("parallelProjText"));
    }    
	
	public void resetLetters()
	{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
    	CheckboxMenuItem cmi = (CheckboxMenuItem) m.getItem(tt("lettersText"));		
	    cmi.setState(false);
	
	}

	public void setLetters()
	{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
    	CheckboxMenuItem cmi = (CheckboxMenuItem) m.getItem(tt("lettersText"));		
	    cmi.setState(true);
	
	}
	
	public void resetHelpPoints()
	{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
        CheckboxMenuItem cmh = (CheckboxMenuItem) m.getItem(tt("helpPointsText"));
	    cmh.setState(false);
	}    
	
	public void enableOptions(boolean b)
	{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
	    CheckboxMenuItem cml = (CheckboxMenuItem) m.getItem(tt("lettersText"));		
	    cml.setEnabled(b);
	    CheckboxMenuItem cmh = (CheckboxMenuItem) m.getItem(tt("helpPointsText"));
	    cmh.setEnabled(b);
	}
	
    // action listener class for menu choices
    class MIL implements ActionListener, ItemListener
    {   public void actionPerformed(ActionEvent e)
        {   MenuItem choosen = (MenuItem) e.getSource();
            processMenuChoice(choosen);
        }
        // NOTE: use getSource (which gives an Object), getItem (which gives an
        // ItemSelectable) does not work (cast or no cast), although
        // CheckboxMenu implements the ItemSelectable interface
        // reason: using CheckboxMenu as an ItemSelectable one can only
        // use methods defined in the ItemSelectable interface
        public void itemStateChanged(ItemEvent e)
        {   CheckboxMenuItem choosen = (CheckboxMenuItem) e.getSource();
            processMenuChoice(choosen);
        }
    }
    // window listener class, closes all open frames, then this
    class WL extends WindowAdapter
    {   public void windowClosing(WindowEvent e)
        {   
			viewer.getScormedObject3D();	        
        	dispose();
            viewer.restart();
        }
    }
    class CL extends ComponentAdapter
    {   public void componentResized(ComponentEvent e)
        {   
//System.out.println("resize event on DoorzienFrame");            
            invalidate();
            validate();
            repaint();
        }
    }
    
    
} // class DoorzienFrame


// toolbar on top
class TopToolBar extends Panel
{   // owner
    DoorzienFrame owner;
    // components here
    // left to right
    ImageButton drawLineButton, deleteLineButton, 
                lengLinesButton, shortLinesButton,
    
                drawPlaneButton, parPlaneButton,
                deletePlaneButton,
                planesFilledButton, 
                transPlaneButton,  rotPlaneButton, 
                showCutButton, cutButton,
                undoButton, redoButton
                ;
    // button width
    int buttonWidth = 47;

    
    // constructor
    public TopToolBar(DoorzienFrame o)
    {   owner = o;
        setBackground(DoorzienFrame.appletBackground);                                   
        // allow using coordinates
        setLayout(null);
        // create and add components left to right
        int currentX = owner.offSet;
        int currentY = owner.offSet;
        
        // lines
        drawLineButton = new ImageButton(owner.drawLineImage, 
                                         owner.drawLineOffImage);
        drawLineButton.setLocation(currentX, currentY);
        if (owner.drawLines)
        {
            add(drawLineButton);
            // listener
            drawLineButton.addMouseListener(new DrawLineML());
            currentX += buttonWidth + owner.offSet;
        }
        deleteLineButton = new ImageButton(owner.deleteLineImage, 
                                           owner.deleteLineOffImage);
        deleteLineButton.setOn(false);                                                                                                                        
        deleteLineButton.setLocation(currentX, currentY);
        if (owner.removeLines)
        {
            add(deleteLineButton);
            // listener
            deleteLineButton.addMouseListener(new DeleteLineML());
            currentX += buttonWidth + owner.offSet;
        }
        lengLinesButton = new ImageButton(owner.lengLinesImage, 
                                          owner.lengLinesOffImage);
        lengLinesButton.setOn(false);                                                                             
        lengLinesButton.setLocation(currentX, currentY);
        if (owner.lengLines)
        {
            add(lengLinesButton);
            // listener
            lengLinesButton.addMouseListener(new LengLinesML());
            currentX += buttonWidth + owner.offSet;
        }
        shortLinesButton = new ImageButton(owner.shortLinesImage, 
                                           owner.shortLinesOffImage);
        shortLinesButton.setOn(false);                                                                             
        shortLinesButton.setLocation(currentX, currentY);
        if (owner.lengLines)
        {
            add(shortLinesButton);
            // listener
            shortLinesButton.addMouseListener(new ShortLinesML());
            currentX += buttonWidth + 4 * owner.offSet;
        }

        // planes
        drawPlaneButton = new ImageButton(owner.drawPlaneImage, 
                                          owner.drawPlaneOffImage);
        drawPlaneButton.setLocation(currentX, currentY);
        if (owner.drawPlanes)
        {
            add(drawPlaneButton);
            // listener
            drawPlaneButton.addMouseListener(new DrawPlaneML());
            currentX += buttonWidth + owner.offSet;
        }
        
        parPlaneButton = new ImageButton(owner.parPlaneImage, 
                                         owner.parPlaneOffImage);
        parPlaneButton.setLocation(currentX, currentY);
        if (owner.parPlanes)
        {
            add(parPlaneButton);
            // listener
            parPlaneButton.addMouseListener(new ParPlaneML());
            currentX += buttonWidth + owner.offSet;
        }
        
        deletePlaneButton = new ImageButton(owner.deletePlaneImage, 
                                            owner.deletePlaneOffImage);
        deletePlaneButton.setOn(false);                                                                                                                        
        deletePlaneButton.setLocation(currentX, currentY);
        if (owner.removePlanes)
        {
            add(deletePlaneButton);
            // listener
            deletePlaneButton.addMouseListener(new DeletePlaneML());
            currentX += buttonWidth + owner.offSet;
        }

        planesFilledButton = new ImageButton(owner.planesFilledImage, 
                                             owner.planesFilledOffImage);
        planesFilledButton.setOn(false);                                                                                                                        
        planesFilledButton.setLocation(currentX, currentY);
        if (owner.fillPlanes)
        {
            add(planesFilledButton);
            // listener
            planesFilledButton.addMouseListener(new PlanesFilledML());
            currentX += buttonWidth + owner.offSet;
        }
        transPlaneButton = new ImageButton(owner.transPlaneImage, 
                                           owner.transPlaneOffImage);
        transPlaneButton.setOn(false);                                   
        transPlaneButton.setLocation(currentX, currentY);
//ALLEEN VOOR FI        
        if (DoorzienFrame.version == DoorzienFrame.FI)
        {
            add(transPlaneButton);
            // listener
            transPlaneButton.addMouseListener(new TranslatePlaneML());
            currentX += buttonWidth + owner.offSet;
        }
        
        rotPlaneButton = new ImageButton(owner.rotPlaneImage, 
                                         owner.rotPlaneOffImage);
        rotPlaneButton.setOn(false);                                   
        rotPlaneButton.setLocation(currentX, currentY);

//ALLEEN VOOR FI        
        if (DoorzienFrame.version == DoorzienFrame.FI)
        {   add(rotPlaneButton);
            // listener
            rotPlaneButton.addMouseListener(new RotatePlaneML());
            currentX += buttonWidth + owner.offSet;
        }
        showCutButton = new ImageButton(owner.showCutImage, 
                                    owner.showCutOffImage);
        showCutButton.setOn(false);                                   
        showCutButton.setLocation(currentX, currentY);
        if (owner.showCut)
        {
            add(showCutButton);
            // listener
            showCutButton.addMouseListener(new ShowCutML());
            currentX += buttonWidth + owner.offSet;
        }
        cutButton = new ImageButton(owner.cutImage, 
                                    owner.cutOffImage);
        cutButton.setOn(false);                                   
        cutButton.setLocation(currentX, currentY);
        if (owner.cutObject)
        {
            add(cutButton);
            // listener
            cutButton.addMouseListener(new CutML());
            currentX += buttonWidth + 4 * owner.offSet;
        }

        undoButton = new ImageButton(owner.undoImage, 
                                     owner.undoOffImage);
        undoButton.setOn(false);                                   
        undoButton.setLocation(currentX, currentY);
        add(undoButton);
        // listener
        undoButton.addMouseListener(new UndoML());        
        
        currentX += buttonWidth + owner.offSet;        
        
        redoButton = new ImageButton(owner.redoImage, 
                                     owner.redoOffImage);
        redoButton.setOn(false);                                   
        redoButton.setLocation(currentX, currentY);
        add(redoButton);
        // listener
        redoButton.addMouseListener(new RedoML());        
        
    }
    
    public void initialize()
    {
        drawLineButton.setSize(buttonWidth,
                               getSize().height - 2 * owner.offSet); 
        deleteLineButton.setSize(buttonWidth,
                               getSize().height - 2 * owner.offSet); 
        lengLinesButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
        shortLinesButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
                                
        drawPlaneButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
        parPlaneButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
        deletePlaneButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
        planesFilledButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
                                
                                
        transPlaneButton.setSize(buttonWidth,
                                 getSize().height - 2 * owner.offSet); 
        rotPlaneButton.setSize(buttonWidth,
                               getSize().height - 2 * owner.offSet); 
        showCutButton.setSize(buttonWidth,
                          getSize().height - 2 * owner.offSet); 
        cutButton.setSize(buttonWidth,
                          getSize().height - 2 * owner.offSet); 
                          
        undoButton.setSize(buttonWidth,
                          getSize().height - 2 * owner.offSet); 
        redoButton.setSize(buttonWidth,
                          getSize().height - 2 * owner.offSet); 
                          
                               
    }    
    // situation: no lines, no planes
    public void resetDefaults()
    {   // just in case
        drawLineButton.setOn(true);
        deleteLineButton.setOn(false);
        lengLinesButton.setOn(false);
        shortLinesButton.setOn(false);

        drawPlaneButton.setOn(true);
        parPlaneButton.setOn(false);
        deletePlaneButton.setOn(false);        
        planesFilledButton.setOn(false);        
        transPlaneButton.setOn(false);                                       
        rotPlaneButton.setOn(false);                                       
        showCutButton.setOn(false);
        cutButton.setOn(false);                                       
        
        undoButton.setOn(false);
        redoButton.setOn(false);
    }
    // true: at least one line
    // false: no lines
    public void activateLineButtons(boolean b)
    {   // just in case
        drawLineButton.setOn(true);
        deleteLineButton.setOn(b);
        lengLinesButton.setOn(b);  
        if (owner.drawingPanel.llFactor < Vector3D.NZero)
            shortLinesButton.setOn(false);
        else
            shortLinesButton.setOn(true);

        
    }
    
    public void disableLineButtons()
    {   // just in case
        drawLineButton.setOn(false);
        deleteLineButton.setOn(false);
        lengLinesButton.setOn(false);                                                                                         
//        if (owner.drawingPanel.llFactor < Vector3D.NZero)
            shortLinesButton.setOn(false);
//        else
//            shortLinesButton.setOn(true);

    }
    
    // true: at least one plane
    // false: no planes
    public void activatePlaneButtons(boolean b)
    {   drawPlaneButton.setOn(true);
        parPlaneButton.setOn(b);
        deletePlaneButton.setOn(b);        
        planesFilledButton.setOn(b);
        if (owner.drawingPanel.planesFilled && b)
            planesFilledButton.setImage(owner.planesEmptyImage);
        transPlaneButton.setOn(b);                                       
        rotPlaneButton.setOn(b);   
        showCutButton.setOn(b);
        if (owner.drawingPanel.showCut)
            showCutButton.setImage(owner.hideCutImage);
        cutButton.setOn(b);                                       
    }

    public void disablePlaneButtons()
    {   drawPlaneButton.setOn(false);
        parPlaneButton.setOn(false);
        deletePlaneButton.setOn(false);        
        planesFilledButton.setOn(false);
//        if (owner.drawingPanel.planesFilled && b)
//            planesFilledButton.setImage(owner.planesEmptyImage);
        transPlaneButton.setOn(false);                                       
        rotPlaneButton.setOn(false);   
        showCutButton.setOn(false);
//        if (owner.drawingPanel.showCut)
//            showCutButton.setImage(owner.hideCutImage);
        cutButton.setOn(false);                                       
    }
    
    
    // for BorderLayout    
    public Dimension getPreferredSize()
    {   return new Dimension(owner.getSize().width, 
                             owner.topHeight);
    }    
    
    public void update(Graphics g)
    {   paint(g);
    }    
    
	// draw offscreen
	public void paint(Graphics g)
	{   Image offscreen = createImage(getSize().width, getSize().height);
	    Graphics og = offscreen.getGraphics();
	    og.setClip(0, 0, getSize().width, getSize().height);
	    paintTop(og);
	    g.drawImage(offscreen, 0, 0, null);
	    og.dispose();
	}
    
    public void paintTop(Graphics g)
    {   
        
        g.setColor(DoorzienFrame.topBackground);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(Color.black);
        g.drawRect(0, 0, getSize().width - 1, getSize().height);
        super.paint(g);
    }    
    
    class DrawLineML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (drawLineButton.enabled)
            {   //if (owner.version == owner.FI)
                //{
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWLINE)
                        owner.drawingPanel.drawLine(0, true);
                    else
                    {   owner.drawingPanel.drawLine(0, false);
                    }
                //}
                //else if (owner.version == owner.EPN)
                //{   owner.drawingPanel.drawLine(0, true);
                    
                //}
                lastHelpMessage = owner.helpBar.text;
                owner.helpBar.setMessage(null, 0);
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (drawLineButton.enabled)
            {   
                lastHelpMessage = owner.helpBar.text;             
                if (owner.version == owner.FI)
                {
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWLINE)
                    {    
                        //owner.helpBar.setText(owner.tt("drawLinesText"));
                        owner.helpBar.setMessage(owner.tt("drawLinesText"),
                                                 drawLineButton.getLocation().x + buttonWidth / 2);
                        
                    }    
                }                    
                else if (owner.version == owner.EPN)
                {
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWLINE)
                    {    
                        //owner.helpBar.setText(owner.tt("drawLineText"));
                        owner.helpBar.setMessage(owner.tt("drawLineText"),
                                                 drawLineButton.getLocation().x + buttonWidth / 2);
                    }    
                }                    
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (drawLineButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    

    
    class DeleteLineML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (deleteLineButton.enabled)
            {   
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.DELETELINE)
                    owner.drawingPanel.deleteLine(0, true);
                else
                {   owner.drawingPanel.deleteLine(0, false);
                }

                lastHelpMessage = owner.helpBar.text;                
                owner.helpBar.setMessage(null, 0);
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (deleteLineButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
            
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.DELETELINE)
                owner.helpBar.setMessage(owner.tt("deleteLineText"),
                                         deleteLineButton.getLocation().x + buttonWidth / 2);
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (deleteLineButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    

    class LengLinesML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (lengLinesButton.enabled)
            {   owner.drawingPanel.lengthenLines();
                shortLinesButton.setOn(true);
                if (owner.drawingPanel.llFactor >= 
                    (owner.drawingPanel.MAXLLFACTOR - 
                     owner.drawingPanel.LLSTEP / 10))
                    lengLinesButton.setOn(false);    
                //lastHelpMessage = owner.helpBar.text;                            
                owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(owner.tt("rotateText"));                
                lastHelpMessage = owner.helpBar.text;                                            
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (lengLinesButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("lengLinesText"));
                owner.helpBar.setMessage(owner.tt("lengLinesText"),
                                         lengLinesButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (lengLinesButton.enabled)
            {   owner.helpBar.setMessage(null, 0); 
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    class ShortLinesML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (shortLinesButton.enabled)
            {   owner.drawingPanel.shortenLines();
                lengLinesButton.setOn(true);
                shortLinesButton.setOn(false);    
                //lastHelpMessage = owner.helpBar.text;                
                owner.helpBar.setMessage(null, 0);    
                owner.helpBar.setText(owner.tt("rotateText"));                
                lastHelpMessage = owner.helpBar.text;                                            
            }
            
        }  
        public void mouseEntered(MouseEvent e)
        {   if (shortLinesButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("shortLinesText"));
                owner.helpBar.setMessage(owner.tt("shortLinesText"),
                                         shortLinesButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (shortLinesButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    
    
    
    class DrawPlaneML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (drawPlaneButton.enabled)
            {   
//                if (owner.version == owner.FI)
//                {
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWPLANE)
                        owner.drawingPanel.drawPlane(0, true);
                    else
                    {   owner.drawingPanel.drawPlane(0, false);
                    }
//                }
//                else if (owner.version == owner.EPN)
//                    owner.drawingPanel.drawPlane(0, true);
                lastHelpMessage = owner.helpBar.text;
                owner.helpBar.setMessage(null, 0);
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (drawPlaneButton.enabled)
            {   
                lastHelpMessage = owner.helpBar.text;     
                if (owner.version == owner.FI)
                {
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWPLANE)
                    {    
                        //owner.helpBar.setText(owner.tt("drawPlanesText"));
                        owner.helpBar.setMessage(owner.tt("drawPlanesText"),
                                                 drawPlaneButton.getLocation().x + buttonWidth / 2);
                        
                    }    
                }    
                else if (owner.version == owner.EPN)
                {
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWPLANE)
                    {    
                        //owner.helpBar.setText(owner.tt("drawPlaneText"));
                        owner.helpBar.setMessage(owner.tt("drawPlaneText"),
                                                 drawPlaneButton.getLocation().x + buttonWidth / 2);
                    }    
                }    

            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (drawPlaneButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    

    class ParPlaneML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (parPlaneButton.enabled)
            {   
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.DRAWPARPLANE)
                    owner.drawingPanel.drawParPlane(0, true);
                else
                {   owner.drawingPanel.drawParPlane(0, false);
                }
                
                lastHelpMessage = owner.helpBar.text;                
                owner.helpBar.setMessage(null, 0);                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (parPlaneButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                //owner.helpBar.setText(owner.tt("parPlaneText"));
                if (owner.drawingPanel.mouseMode != 
                    owner.drawingPanel.DRAWPARPLANE)
                    owner.helpBar.setMessage(owner.tt("parPlaneText"),
                                             parPlaneButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (parPlaneButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    

    class DeletePlaneML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (deletePlaneButton.enabled)
            {   
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.DELETEPLANE)
                    owner.drawingPanel.deletePlane(0, true);
                else
                {   owner.drawingPanel.deletePlane(0, false);
                }
                lastHelpMessage = owner.helpBar.text;                
                owner.helpBar.setMessage(null, 0);                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (deletePlaneButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.DELETEPLANE)
                    owner.helpBar.setMessage(owner.tt("deletePlaneText"),
                                             deletePlaneButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (deletePlaneButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    

    class PlanesFilledML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (planesFilledButton.enabled)
            {
                if (owner.drawingPanel.planesFilled)
                {   owner.drawingPanel.fillPlanes(false);
                    planesFilledButton.setImage(owner.planesFilledImage);
                    //owner.helpBar.setText(owner.tt("planesFilledText"));                    
                    owner.helpBar.setMessage(null, 0);
//                    owner.helpBar.setMessage(owner.tt("planesFilledText"),
//                                             planesFilledButton.getLocation().x + buttonWidth / 2);
                    
                }
                else
                {   owner.drawingPanel.fillPlanes(true);
                    planesFilledButton.setImage(owner.planesEmptyImage);
                    //owner.helpBar.setText(owner.tt("planesEmptyText"));                    
                    owner.helpBar.setMessage(null, 0);                    
//                    owner.helpBar.setMessage(owner.tt("planesEmptyText"),
//                                             planesFilledButton.getLocation().x + buttonWidth / 2);
                    
                }    
                //lastHelpMessage = owner.helpBar.text;                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (planesFilledButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                if (owner.drawingPanel.planesFilled)
                {   //owner.helpBar.setText(owner.tt("planesEmptyText"));
                    owner.helpBar.setMessage(owner.tt("planesEmptyText"),
                                             planesFilledButton.getLocation().x + buttonWidth / 2);
                
                }
                else
                {   //owner.helpBar.setText(owner.tt("planesFilledText"));
                    owner.helpBar.setMessage(owner.tt("planesFilledText"),
                                             planesFilledButton.getLocation().x + buttonWidth / 2);
                
                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (planesFilledButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    


    class RotatePlaneML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (rotPlaneButton.enabled)
            {   
// dit werkt zowel als de knop is ingedrukt
// en wanneer je "gewoon stopt"
// tweede knop overbodig?
                if (owner.drawingPanel.mouseMode == 
                    DrawingPanel.ROTATEPLANE)
                {
                    owner.drawingPanel.rotatePlane(0, false);
                    rotPlaneButton.setImage(owner.rotPlaneImage);
                }
                else
                    owner.drawingPanel.rotatePlane(0, true);                
                lastHelpMessage = owner.helpBar.text;       
                owner.helpBar.setMessage(null, 0);                          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (rotPlaneButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("rotatePlaneText"));
                owner.helpBar.setMessage(owner.tt("rotatePlaneText"),
                                         rotPlaneButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (rotPlaneButton.enabled)
            {   owner.helpBar.setMessage(null, 0);           
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    

    class TranslatePlaneML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (transPlaneButton.enabled)
            {   
                if (owner.drawingPanel.mouseMode == 
                    DrawingPanel.TRANSLATEPLANE)
                {
                    owner.drawingPanel.translatePlane(0, false);                    
                    transPlaneButton.setImage(owner.transPlaneImage);                    
                    
                }    
                else
                    owner.drawingPanel.translatePlane(0, true);                
                lastHelpMessage = owner.helpBar.text;        
                owner.helpBar.setMessage(null, 0);                           
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (transPlaneButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("translatePlaneText"));
                owner.helpBar.setMessage(owner.tt("translatePlaneText"),
                                         transPlaneButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (transPlaneButton.enabled)
            {   owner.helpBar.setMessage(null, 0);            
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    

    class ShowCutML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (showCutButton.enabled)
            {   // cut visible
                if (owner.drawingPanel.showCut)
                {   owner.drawingPanel.showCut(0, false);
                    showCutButton.setImage(owner.showCutImage);
                    owner.helpBar.setMessage(null, 0);                                         
//                    owner.helpBar.setText(owner.tt("showCutText"));                    
                }
                else
                {   // still choosing the plane
                    if (showCutButton.pressed)
                    {   owner.drawingPanel.showCut(0, false);
                    }
                    else
                    {
                        owner.drawingPanel.showCut(0, true);
//                        lastHelpMessage = owner.helpBar.text;                                
                        //showCutButton.setImage(owner.hideCutImage);
    //                    owner.helpBar.setText(owner.tt("hideCutText"));                    
                        owner.helpBar.setMessage(null, 0);                     
                    }
                }    
                lastHelpMessage = owner.helpBar.text;                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (showCutButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                // cut visible
                if (owner.drawingPanel.showCut)
                {   
                    //owner.helpBar.setText(owner.tt("hideCutText"));
                    owner.helpBar.setMessage(owner.tt("hideCutText"),
                                             showCutButton.getLocation().x + buttonWidth / 2);                    
                }
                else
                {   if (showCutButton.pressed)
                    {   // geen message, er wordt nog een vlak gekozen
                    }
                    else // knop is inert
                    {
                        //owner.helpBar.setText(owner.tt("showCutText"));
                        owner.helpBar.setMessage(owner.tt("showCutText"),
                                             showCutButton.getLocation().x + buttonWidth / 2);
                    }
                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (showCutButton.enabled)
            {   owner.helpBar.setMessage(null, 0);                     
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    class CutML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (cutButton.enabled)
            {
                if (owner.drawingPanel.mouseMode != DrawingPanel.CUTOBJECT)
                {   owner.drawingPanel.cutObject(0, true);
                    //cutButton.setImage(owner.glueImage);
                }
                else
                {   owner.drawingPanel.cutObject(0, false);
                    cutButton.setImage(owner.cutImage);
                }    
                lastHelpMessage = owner.helpBar.text;                
                owner.helpBar.setMessage(null, 0);                          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (cutButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                if (owner.drawingPanel.mouseMode != DrawingPanel.CUTOBJECT)
                {   //owner.helpBar.setText(owner.tt("cutFigureText"));
                    owner.helpBar.setMessage(owner.tt("cutFigureText"),
                                             cutButton.getLocation().x + buttonWidth / 2);
                
                }
                else
                {   if (owner.drawingPanel.planeChoosen == null)
                    {    //owner.helpBar.setText(owner.tt("cutFigureText"));                
                    
                    }
                    else
                    {
                        //owner.helpBar.setText(owner.tt("glueFigureText"));
                        owner.helpBar.setMessage(owner.tt("glueFigureText"),
                                                 cutButton.getLocation().x + buttonWidth / 2);
                        
                    }    
                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (cutButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    

    
    class UndoML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (undoButton.enabled)
            {   owner.drawingPanel.undo();    
                lastHelpMessage = owner.helpBar.text;       
                owner.helpBar.setMessage(null, 0);          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (undoButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                //owner.helpBar.setText(owner.tt("undoText"));
                owner.helpBar.setMessage(owner.tt("undoText"),
                                         undoButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (undoButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    
    
    class RedoML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (redoButton.enabled)
            {   owner.drawingPanel.redo();    
                lastHelpMessage = owner.helpBar.text;       
                owner.helpBar.setMessage(null, 0);          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (redoButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                //owner.helpBar.setText(owner.tt("redoText"));
                owner.helpBar.setMessage(owner.tt("redoText"),
                                         redoButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (redoButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    
    
    
} // class TopToolBar


// toolbar on the right
class RightToolBar extends Panel
{   // owner
    DoorzienFrame owner;
       
    // components here
    // top to bottom
    ImageButton rotateButton,
                wireSolidButton, zoomInButton, zoomOutButton,
                conDrawButton, dimButton, axesButton
                ;
    // button height
    int buttonHeight = 32;

    // constructor
    public RightToolBar(DoorzienFrame o)
    {   owner = o;
        setBackground(DoorzienFrame.appletBackground);                                   
        // allow using coordinates
        setLayout(null);
        // create and add components top to bottom
        int currentX = owner.offSet;
        int currentY = owner.offSet;
        
        rotateButton = new ImageButton(DoorzienFrame.getImage("rotateImage.gif"), null);
        rotateButton.setLocation(currentX, currentY);
/*        
        add(rotateButton);
        // listener
        rotateButton.addMouseListener(new RotateML());
        currentY += buttonHeight + 2 * owner.offSet;
*/        
        wireSolidButton = new ImageButton(DoorzienFrame.getImage("solidImage.gif"), null);
        wireSolidButton.setLocation(currentX, currentY);
        add(wireSolidButton);
        // listener
        wireSolidButton.addMouseListener(new WireSolidML());
        currentY += buttonHeight + 2 * owner.offSet;
        
        zoomInButton = new ImageButton(owner.zoomInImage, 
                                       owner.zoomInOffImage);
        zoomInButton.setLocation(currentX, currentY);
        add(zoomInButton);
        // listener
        zoomInButton.addMouseListener(new ZoomInML());        
        currentY += buttonHeight + owner.offSet;
        
        zoomOutButton = new ImageButton(owner.zoomOutImage, 
                                       owner.zoomOutOffImage);
        zoomOutButton.setLocation(currentX, currentY);
        add(zoomOutButton);
        // listener
        zoomOutButton.addMouseListener(new ZoomOutML());                
        currentY += buttonHeight + 2 * owner.offSet;
        
        conDrawButton = new ImageButton(owner.conDrawImage, null);
//                                        owner.conDrawOffImage);
        conDrawButton.setLocation(currentX, currentY);

        
        if (owner.conDraw)
        {
            add(conDrawButton);
            // listener
            conDrawButton.addMouseListener(new ConDrawML());
            currentY += buttonHeight + 2 * owner.offSet;
        }
        
    }
    
    public void initialize()
    {
        
        rotateButton.setSize(getSize().width - 2 * owner.offSet, 
                             buttonHeight);    
        
        wireSolidButton.setSize(getSize().width - 2 * owner.offSet, 
                                buttonHeight);    
        zoomInButton.setSize(getSize().width - 2 * owner.offSet, 
                             buttonHeight);    
        zoomOutButton.setSize(getSize().width - 2 * owner.offSet, 
                              buttonHeight);    
        conDrawButton.setSize(getSize().width - 2 * owner.offSet, 
                              buttonHeight);    
/*                              
        dimButton.setSize(getSize().width - 2 * owner.offSet, 
                              buttonHeight);    
        axesButton.setSize(getSize().width - 2 * owner.offSet, 
                              buttonHeight);    
*/    
    }
    
    public void resetDefaults()
    {
        wireSolidButton.setImage(owner.solidImage);    
        zoomInButton.setOn(true);
        zoomOutButton.setOn(true);

        conDrawButton.setImage(owner.conDrawImage);        
    }
    // for BorderLayout    
    public Dimension getPreferredSize()
    {   return new Dimension(owner.rightWidth,
                             owner.getSize().height);
    }    
    
    public void update(Graphics g)
    {   paint(g);
    }    
    
    
	// draw offscreen
	public void paint(Graphics g)
	{   Image offscreen = createImage(getSize().width, getSize().height);
	    Graphics og = offscreen.getGraphics();
	    og.setClip(0, 0, getSize().width, getSize().height);
	    paintRight(og);
	    g.drawImage(offscreen, 0, 0, null);
	    og.dispose();
	}
    
    public void paintRight(Graphics g)
    {   
        // fill effective area with background and outline
        g.setColor(DoorzienFrame.rightBackground);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(Color.black);
        g.drawRect(0, 0, getSize().width - 1, getSize().height);
        super.paint(g);
    }    
  
    
    // listeners for each imagebutton
    class WireSolidML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (wireSolidButton.enabled)
            {
                if (owner.drawingPanel.filled)
                {   owner.drawingPanel.setFilled(false);
                    wireSolidButton.setImage(owner.solidImage);
                    //owner.helpBar.setText(owner.tt("solidText"));                    
                    owner.helpBar.setMessage(null, 0);
                }
                else
                {   owner.drawingPanel.setFilled(true);
                    wireSolidButton.setImage(owner.wireFrameImage);
                    //owner.helpBar.setText(owner.tt("wireFrameText"));                    
                    owner.helpBar.setMessage(null, 0);                    
                }    
                //lastHelpMessage = owner.helpBar.text;                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (wireSolidButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                if (owner.drawingPanel.filled)
                {   //owner.helpBar.setText(owner.tt("wireFrameText"));
                    owner.helpBar.setMessage(owner.tt("wireFrameText"),
                                             owner.helpBar.getSize().width - 
                                             owner.topToolBar.buttonWidth / 2);
                
                }
                else
                {   //owner.helpBar.setText(owner.tt("solidText"));
                    owner.helpBar.setMessage(owner.tt("solidText"),
                                             owner.helpBar.getSize().width - 
                                             owner.topToolBar.buttonWidth / 2);
                
                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (wireSolidButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    class ZoomInML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (zoomInButton.enabled)
            {   owner.drawingPanel.zoomIn();
                zoomOutButton.setOn(true);
                if (owner.drawingPanel.zoom >= 
                    (owner.drawingPanel.MAXZOOM - 
                     owner.drawingPanel.ZOOMSTEP / 10))
                    zoomInButton.setOn(false);    
                //lastHelpMessage = owner.helpBar.text;     
                owner.helpBar.setMessage(null, 0);          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (zoomInButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("zoomInText"));
                owner.helpBar.setMessage(owner.tt("zoomInText"),
                                         owner.helpBar.getSize().width - 
                                         owner.topToolBar.buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (zoomInButton.enabled)
            {    owner.helpBar.setMessage(null, 0);          
                 owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    class ZoomOutML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (zoomOutButton.enabled)
            {   owner.drawingPanel.zoomOut();
                zoomInButton.setOn(true);
                if (owner.drawingPanel.zoom <= 
                    (owner.drawingPanel.MINZOOM + 
                     owner.drawingPanel.ZOOMSTEP / 10))
                {                    
                    zoomOutButton.setOn(false);    
                }
                //lastHelpMessage = owner.helpBar.text;      
                owner.helpBar.setMessage(null, 0);          
            }
            
        }  
        public void mouseEntered(MouseEvent e)
        {   if (zoomOutButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("zoomOutText"));
                owner.helpBar.setMessage(owner.tt("zoomOutText"),
                                         owner.helpBar.getSize().width - 
                                         owner.topToolBar.buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (zoomOutButton.enabled)
            {   owner.helpBar.setMessage(null, 0);           
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    class ConDrawML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (conDrawButton.enabled)
            {   if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.FOLDOUT)
                {   owner.drawingPanel.makeFoldOut(0, true);
                    // figureImage is set in stepNum == 1
                }
                else
                {   // back to whole figure
                    conDrawButton.setImage(owner.conDrawImage);
                    owner.drawingPanel.makeFoldOut(0, false);
                }    
                lastHelpMessage = owner.helpBar.text;       
                owner.helpBar.setMessage(null, 0);          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (conDrawButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.FOLDOUT)
                {    
                    //owner.helpBar.setText(owner.tt("conDrawText"));
                    owner.helpBar.setMessage(owner.tt("conDrawText"),
                                             owner.helpBar.getSize().width - 
                                             owner.topToolBar.buttonWidth / 2);
                    
                }    
                else
                {   if (owner.drawingPanel.startFacet == null)
                    {
                    }
                    else
                    {
                        //owner.helpBar.setText(owner.tt("wholeFigureText"));
                        owner.helpBar.setMessage(owner.tt("wholeFigureText"),
                                                 owner.helpBar.getSize().width - 
                                                 owner.topToolBar.buttonWidth / 2);
                    }
                    
                }    
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (conDrawButton.enabled)
            {   owner.helpBar.setMessage(null, 0);           
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    
    class RotateML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (rotateButton.enabled)
            {   //if (owner.drawingPanel.escapeActive())
                //{
                    lastHelpMessage = owner.tt("rotateText");
                    owner.drawingPanel.rotate();
                    owner.helpBar.setMessage(null, 0);                      
                    owner.helpBar.setText(owner.tt("rotateText"));                
                //}
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (rotateButton.enabled)
            {   
                lastHelpMessage = owner.helpBar.text;                
//                if (owner.drawingPanel.escapeActive())
//                {
 //                   lastHelpMessage = owner.helpBar.text;
                    owner.helpBar.setMessage(owner.tt("escapeText"),
                                             owner.helpBar.getSize().width - 
                                             owner.topToolBar.buttonWidth / 2);
//                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (rotateButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
//                if (owner.drawingPanel.escapeActive())
                    owner.helpBar.setText(lastHelpMessage);
                
            }
        }    
    }    

/*
    class AxesML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (axesButton.enabled)
            {
                if (owner.drawingPanel.axes)
                {   owner.drawingPanel.showAxes(false);
                    axesButton.setImage(owner.axesImage);
                    owner.helpBar.setText(owner.tt("axesText"));                    
                }
                else
                {   owner.drawingPanel.showAxes(true);
                    axesButton.setImage(owner.axesOffImage);
                    owner.helpBar.setText(owner.tt("noAxesText"));                    
                }    
                //lastHelpMessage = owner.helpBar.text;                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (axesButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                if (owner.drawingPanel.axes)
                {   owner.helpBar.setText(owner.tt("noAxesText"));
                }
                else
                {   owner.helpBar.setText(owner.tt("axesText"));
                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (axesButton.enabled)
            {             
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
*/
} // class RightToolBar   

class HelpBar extends Canvas
{   // applet frame    
    DoorzienFrame owner;
    String text = "";
    String paintText;

    String messageText = null;
    int messageX = 0;
    
    public HelpBar(DoorzienFrame o)
    {   owner = o;
        
    }    
    
    public void setText(String t)
    {   text = t;
        repaint();
    }    
    
    public void setMessage(String m, int mX)
    {   messageText = m;
        messageX = mX;
        repaint();
    }    
    // for BorderLayout    
    public Dimension getPreferredSize()
    {   return new Dimension(owner.getSize().width,
                             owner.helpHeight);
    }    
    
    public void paint(Graphics g)
    {   
        
        g.setColor(DoorzienFrame.helpBackground);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(Color.black);
//        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        g.drawLine(0, 0, getSize().width - 1, 0);
        g.drawLine(0, 0, 0, getSize().height - 1);
        g.drawLine(getSize().width - 1, 0, getSize().width - 1, getSize().height - 1);
        
        Font fo = getFont();        
        if ((text.length() > 0) && text.substring(0,1).equals("#"))
        {   g.setColor(Color.red);        
            fo = new Font(fo.getName(), Font.BOLD, fo.getSize());
            paintText = text.substring(1);
        }
        else
        {    paintText = text;
        }
        FontMetrics fm = getFontMetrics(fo);
        g.setFont(fo);
        int vSpace = (getSize().height - fm.getHeight()) / 2;
        g.drawString(paintText, 2 * owner.offSet,
                     vSpace + fm.getAscent());
        
        if (messageText != null)
        {   Font mfo = getFont();
            g.setFont(mfo);
            FontMetrics mfm = getFontMetrics(mfo);
            int messageWidth = mfm.stringWidth(messageText + "  ");
            // try to center at messageX
            int bx = messageX - messageWidth / 2;
            if (bx < 0)
                bx = 0;
            if ((bx + messageWidth) > getSize().width)
                bx = getSize().width - messageWidth;
            g.setColor(new Color(255, 255, 198));
            g.fillRect(bx, 0, messageWidth, owner.helpHeight -1);
            g.setColor(Color.black);
            g.drawRect(bx, 0, messageWidth - 1, 
                       owner.helpHeight);            
            g.drawString(" " + messageText, bx, vSpace + mfm.getAscent());            
            
        }
    }    
    
    
}    


class TopBar extends Panel
{   // applet frame    
    DoorzienFrame owner;
    
    public TopBar(DoorzienFrame o)
    {   owner = o;
    }    
    // for BorderLayout    
    public Dimension getPreferredSize()
    {   return new Dimension(owner.getSize().width,
                             owner.totalTopHeight);
    }    

}
// additional classes 
// add when needed
class ImageButton extends Component
{
    boolean pressed = false;
    boolean enabled = true;
    Image imageOn, imageOff, image;

    public ImageButton(Image imageOn, Image imageOff, int width, int height) 
    {   this.imageOn = imageOn;
        if (imageOff != null)
            this.imageOff = imageOff;
        else
            this.imageOff = imageOn;        
        this.image = imageOn;
        setSize(width, height);
    } // ImageButton

    public ImageButton(Image imageOn, Image imageOff) 
    {   this.imageOn = imageOn;
        if (imageOff != null)    
            this.imageOff = imageOff;
        else
            this.imageOff = imageOn;        
        this.image = imageOn;
    } // ImageButton

    public void setOn(boolean b)
    {   enabled = b;
        if (enabled)
            setImage(imageOn);
        else
            setImage(imageOff);
        
        pressed = false;
    }    

    public void setPressed(boolean b)
    {   pressed = b;
        repaint();
    }    

    public void setImage(Image image)
    {
         this.image = image;
         repaint();
    
    }  

    public void update(Graphics g)
    {    paint(g);
    
    }  
  
    public void paint(Graphics g) 
    {    
         if (image == null)
             return;
         if  (
             (getSize().width > image.getWidth(this)) &&
             (getSize().height > image.getHeight(this))
             ) 
         {// de image is kleiner dan de button, centreren
    	     g.drawImage(image, 
	    	       (getSize().width - image.getWidth(this))/2, 
		    	   (getSize().height - image.getHeight(this))/2, 
			        this);
//System.out.println("centering");			        
         } 
         else 
         {// schalen
	         g.drawImage(image, 1, 1, 
	            getSize().width - 3, getSize().height - 3, this);
//System.out.println("scaling");			        	         
         }
         
         // button outline 
         if (pressed)
         {
         g.setColor(Color.black);
         g.drawLine(0, 0, getSize().width - 1, 0);         
         g.drawLine(0, 0, 0, getSize().height - 1);         
         g.setColor(Color.white);
         g.drawLine(0, getSize().height - 1, getSize().width - 1, getSize().height - 1);         
         g.drawLine(getSize().width - 1, 0, getSize().width - 1, getSize().height - 1);         
         }
         else
         {
         g.setColor(Color.white);
         g.drawLine(0, 0, getSize().width - 1, 0);         
         g.drawLine(0, 0, 0, getSize().height - 1);         
         g.setColor(Color.black);
         g.drawLine(0, getSize().height - 1, getSize().width - 1, getSize().height - 1);         
         g.drawLine(getSize().width - 1, 0, getSize().width - 1, getSize().height - 1);         
         }
         

         
         
    } // paint

} // class ImageButton



// light weight (else does not show in browser)
// label with painted(!) strings
class PaintedLabel extends Component
{   boolean outline = false;
    String text = "";
    // default constructor
    public void setLabel(String t)
    {   text = t;
        repaint();
    }    
    
    public void update(Graphics g)
    {   paint(g);
        
    }    
    
	// draw offscreen
	public void paint(Graphics g)
	{   Image offscreen = createImage(getSize().width, getSize().height);
	    Graphics og = offscreen.getGraphics();
	    og.setClip(0, 0, getSize().width, getSize().height);
	    paintLabel(og);
	    g.drawImage(offscreen, 0, 0, null);
	    og.dispose();
	}
    
    
    public void paintLabel(Graphics g)
    {   
        
        g.setColor(getBackground());
        g.fillRect(0, 0, getSize().width, getSize().height);
        
        if (outline)
        {   g.setColor(Color.black);
            g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);        
        }
        // get font with metrics
        Font fo = getFont();
        FontMetrics fm = getFontMetrics(fo);
        // determine baseline bx, by
        //int hSpace = (getSize().width -
        //             fm.stringWidth(text)) / 2;
        int bx = 3;
//        if (hSpace > 0)
//            bx = hSpace;
        int vSpace = (getSize().height -
                      fm.getHeight()) / 2;    
        int by = getSize().height - fm.getDescent();;
        if (vSpace > 0)
            by = vSpace + fm.getHeight() - fm.getDescent();
        g.setColor(getForeground());    
        paintLabeledString(g, text, fo, bx, by);
    }    
    
    public void paintLabeledString(Graphics g, String cs, Font f, 
                                   int bx, int by)
    {   String current = cs;
        String temp;
        char marker = '!';
        char labelChar;
        int markerIndex = current.indexOf(marker);
        int baseX = bx;
        Color c = Color.black; 
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics(f);        
        Font fSmall = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
        FontMetrics fmSmall = g.getFontMetrics(fSmall);        
        boolean superOn = false;
        boolean subOn = false;
        // loop through current as long as current contains a '!'
        while (markerIndex > -1)
        {   // '!' is not first character 
            if (markerIndex > 0)
            {   // get part before the marker
                temp = current.substring(0, markerIndex);
                // draw it
                g.drawString(temp, baseX, by);
                if (superOn || subOn)
                    baseX += fmSmall.stringWidth(temp);
                else
                    baseX += fm.stringWidth(temp);
                // throw it away
                current = current.substring(markerIndex);
                markerIndex = 0;
            }
            // first character is '!' 
            else if (markerIndex <= (current.length() - 2))
            {   // try to process label instructions
                temp = current.substring(0, 2);
                current = current.substring(2);
                labelChar = temp.charAt(1);
                switch (labelChar)
                {   // start subscript
                    // end superscript
                    case 's': 
                    {   by += fm.getDescent();
                        if (superOn)
                        {   superOn = false;
                            g.setFont(f);
                        }
                        else
                        {   subOn = true;
                            g.setFont(fSmall);                        
                        }
                        
                    }
                    break;
                    // end subscript
                    // start superscript
                    case 't': 
                    {   by -= fm.getDescent();
                        if (subOn)
                        {   subOn = false;
                            g.setFont(f);                        
                        }
                        else
                        {   superOn = true;
                            g.setFont(fSmall);                        
                        }
                    }
                    break;
                    default: // nothing
                }  
                markerIndex = current.indexOf(marker);                
            }    
        } // while
        // draw the remaining part
        g.drawString(current, baseX, by);
    } // paintLabeledString   
}  // class PaintedLabel  




