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
//    public static final int EPN = 0;
//    public static final int FI = 1;
//    public static int version = EPN;
    
    
    
    // the applet with starter button
//	DoorzienDWO starter;
	
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
//	    starter = s;
	    viewer = v;
	    
	    // read all parameter strings
	    figureString = null;//starter.figureString;
	    toolString = null;//starter.toolString;

	    // find images for buttons
	    // right tool bar
	    
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
		{   boolean helpPoints = getHelpPoints(); 
			boolean letters = getLetters();
			OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
		    m.switchto(tt("centralProjText"));
		    drawingPanel.setProjection(DrawingPanel.CENTRALPROJ);
		    if (helpPoints)
		    	setHelpPoints();
		    if (letters)
		    	setLetters();
		    
		}    
		else if ( choice.equals(tt("parallelProjText")) )
		{   boolean helpPoints = getHelpPoints(); 
			boolean letters = getLetters();
			OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
		    m.switchto(tt("parallelProjText"));
		    drawingPanel.setProjection(DrawingPanel.PARALLELPROJ);
		    if (helpPoints)
		    	setHelpPoints();
		    if (letters)
		    	setLetters();
		    
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
	
	public boolean getLetters()
	{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
    	CheckboxMenuItem cmi = (CheckboxMenuItem) m.getItem(tt("lettersText"));		
	    return cmi.getState();
	}
	
	
	public void resetHelpPoints()
	{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
        CheckboxMenuItem cmh = (CheckboxMenuItem) m.getItem(tt("helpPointsText"));
	    cmh.setState(false);
	}	
	public void setHelpPoints()
	{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
        CheckboxMenuItem cmh = (CheckboxMenuItem) m.getItem(tt("helpPointsText"));
	    cmh.setState(true);
	}    

	public boolean getHelpPoints()
	{   OptionsMenu m = mainMenus.getMenu(tt("optionsText"));
        CheckboxMenuItem cmh = (CheckboxMenuItem) m.getItem(tt("helpPointsText"));
	    return cmh.getState();
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




