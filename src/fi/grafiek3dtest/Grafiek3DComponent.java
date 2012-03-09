package fi.grafiek3dtest;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;

import fi.grafiek3dtest.expressies.*;
import fi.grafiek3dtest.formuleobjects.*;
import fi.grafiek3dtest.tekstobjects.*;

public class Grafiek3DComponent extends JPanel implements ActionListener
{

    // defined colors
    public static final Color darkGreen = new Color(41, 156, 57); 
    public static final Color mediumGreen = new Color(173, 222, 99);
    public static final Color brownRed = new Color(214, 0, 0);
    public static final Color lightRed = new Color(255, 156, 74);
    public static final Color mediumBlue = new Color(99, 198, 222);    
	
    // drawing colors
    public static Color axesColor = Color.black;
    // floorColor is a dummy
    public static Color floorColor = Color.white;
    public static Color floorOutlineColor = Color.black;
    public static Color graphColor = new Color(Color.yellow.getRed(), Color.yellow.getGreen(), Color.yellow.getBlue(), 200);
    //public static Color graphColor = new Color(Color.cyan.getRed(), Color.cyan.getGreen(), Color.cyan.getBlue(), 200);
    //public static Color graphColor = new Color(Color.magenta.getRed(), Color.magenta.getGreen(), Color.magenta.getBlue(), 200);
    //public static Color graphColor = new Color(Color.green.getRed(), Color.green.getGreen(), Color.green.getBlue(), 200);
    public static Color surfaceColor = new Color(Color.yellow.getRed(), Color.yellow.getGreen(), Color.yellow.getBlue(), 200);
    public static Color graphOutlineColor = Color.lightGray;
    public static Color surfaceOutlineColor = Color.lightGray;
    public static Color curveColor = brownRed;    
    public static Color curveOutlineColor = brownRed;
    public static Color wireFrameColor = brownRed;
    
	String[] imageNames = 
	{	"zoominknop.gif",
		"zoomuitknop.gif",
	};
	Hashtable images;


	public static Font assenFont = new Font("SansSerif",Font.PLAIN, 10);
	
    public static double MAXZOOM = 15e-1d;
    public static double MINZOOM = 2e-1d; 
    public static double ZOOMSTEP = 1e-1d;
    public static double defaultZoom = 7e-1d;
    public double zoom = defaultZoom;
    
    // projections
    public static int CENTRALPROJ = 0;
    public static int PARALLELPROJ = 1;
    public int defaultProjection = CENTRALPROJ;
    //public int defaultProjection = PARALLELPROJ;
    
    // mouse modes
    public static final int INERT = 0;
    // default for mouseMode
    public int mouseMode = INERT;
    public int oldMouseMode;
    
    // listener for mouse movements on panel3D
    MLMML listener;

    // managing the cursor
    // coordinates
    int xClicked;
    int yClicked;
    int xMoved;
    int yMoved;

    // circle radius for rotate modes
    public static double RADFACTOR = 8e-1d;//1d;
    
    JScrollPane scrollPane;
    JPanel scrollPanel;
    int scrollPanelWidth = 450;
    int scrollPaneWidth = 470;
    int scrollBarWidth = 20;
    
    // hoeken
    double angleXG = Object3DContainer.angleXStart;
    double angleZG = Object3DContainer.angleZStart;
    double angleXS = Object3DContainer.angleXStart;
    double angleZS = Object3DContainer.angleZStart;
    double angleXC = Object3DContainer.angleXStart;
    double angleZC = Object3DContainer.angleZStart;
    
    // axes
    double xMinBegin = -2, xMaxBegin = 2, xStepBegin = 5e-1d, 
    	   yMinBegin = -2, yMaxBegin = 2, yStepBegin = 5e-1d, 
    	   zMinBegin = -2, zMaxBegin = 2, zStepBegin = 5e-1d;
    double xMinG = -2, xMaxG = 2, xStepG = 5e-1d, 
           yMinG = -2, yMaxG = 2, yStepG = 5e-1d, 
           zMinG = -2, zMaxG = 2, zStepG = 5e-1d;
    int xFinerStepsBegin = 2;
    int yFinerStepsBegin = 2;
	int xFinerStepsG = 2;
	int yFinerStepsG = 2;

    double xMinS = -2, xMaxS = 2, xStepS = 5e-1d, 
    	   yMinS = -2, yMaxS = 2, yStepS = 5e-1d, 
    	   zMinS = -2, zMaxS = 2, zStepS = 5e-1d;
	int xFinerStepsS = 2;
	int yFinerStepsS = 2;
    
    double xMinC = -2, xMaxC = 2, xStepC = 5e-1d, 
	   	   yMinC = -2, yMaxC = 2, yStepC = 5e-1d, 
	   	   zMinC = -2, zMaxC = 2, zStepC = 5e-1d;
	int xFinerStepsC = 2;
	int yFinerStepsC = 2;
    
	
	// state
    int zoomFactorG = 0;
    int translateXFactorG = 0;
    int translateYFactorG = 0;
    int translateZFactorG = 0;
    int finerFactorG = 0;

    int zoomFactorS = 0;
    int translateXFactorS = 0;
    int translateYFactorS = 0;
    int translateZFactorS = 0;
    //int finerFactorS = 0;
    
    int zoomFactorC = 0;
    int translateXFactorC = 0;
    int translateYFactorC = 0;
    int translateZFactorC = 0;
    //int finerFactorC = 0;
    
    
    // state    
    public boolean noAxesG = false;
    public boolean noAxesS = false;
    public boolean noAxesC = false;
    
    public static final int NOFLOOR = 0;
    public static final int TRANSFLOOR = 1;
//    public static final int SOLIDFLOOR = 2;
    // state
    int floorTypeG = NOFLOOR;
    int floorTypeS = NOFLOOR;
    int floorTypeC = NOFLOOR;
    
    public static final int NOLABELS = 0;
    public static final int ENDLABELS = 1;
    public static final int ALLLABELS = 2;
    // state
    int labelTypeG = ENDLABELS;
    int labelTypeS = ENDLABELS;
    int labelTypeC = ENDLABELS;
    
    // state
    boolean wireFrameG = false;
    boolean wireFrameS = false;
    
    long lastActionTime = 0;
    
    
    // the 3D panel(s)
    Object3DContainer panel3D = new Object3DContainer();
    int panel3DSize = 400;
    // the function editor
    FunctieEditor functieEditor;
    int functieEditorWidth = 350;
    int functieEditorHeight = 500;
    JPanel knoppenPanel;
    int knoppenPanelWidth = 40; 
    
    ObjectGroup3D currentObjectGroup;
    
    public static final int FUNCTION = 0;
    public static final int SURFACE = 1;
    public static final int CURVE = 2;
    // state
    int objectType = FUNCTION;
    
    FormuleButton zoomStandaardButton, zoomInButton, zoomUitButton, transPlusButton, asNaamButton, transMinButton, 
    			  solidDraadKeuzeButton, finerPlusButton, finerMinButton, asKeuzeButton, labelKeuzeButton;
    
    JPopupMenu assenPopup, labelsPopup;
    
	String varNaamX = "x";
	String varNaamY = "y";
	String paramNaam = "t";
	String paramNaamU = "u";
	String paramNaamV = "v";

	Axes axesObject;
	// state
	Expressie grafiek3DExpressie = null;
	Grafiek3D grafiek3DObject;
	// state
	Expressie surfaceXExpressie;
	Expressie surfaceYExpressie;
	Expressie surfaceZExpressie;
	double uMin = 0;
	double uMax = 2;
	int uPoints = 10;
	double vMin = 0;
	double vMax = 2;
	int vPoints = 10;
	Surface3D surface3DObject;	

	// state
	Expressie curveXExpressie;
	Expressie curveYExpressie;
	Expressie curveZExpressie;
	double tMin = 0;
	double tMax = 2;
	int tPoints = 10;
	Curve3D curve3DObject;

	// edit state variables
	boolean zoomOptie = true;
	boolean translateOptie = true;
	boolean solidDraadKeuzeOptie = true;
	boolean finerKeuzeOptie = true;
	boolean asKeuzeOptie = true;
	boolean labelKeuzeOptie = true;
	
    public Grafiek3DComponent(int x, int y, int w, int h, Hashtable ims, String[] imNames)
    {
    	setBounds(x, y, w, h);
    	
    	images = ims;
    	imageNames = imNames;
    	
    	setBackground(Color.lightGray);
    	
    	setLayout(null);
    	
    	panel3DSize = w - 10 - knoppenPanelWidth - scrollBarWidth;    	

    	scrollPanel = new JPanel();
    	scrollPanel.setLayout(null);
    	//scrollPanel.setSize(scrollPanelWidth, 10 + panel3DSize + functieEditorHeight);
    	//scrollPanel.setPreferredSize(new Dimension(scrollPanelWidth, 10 + panel3DSize + functieEditorHeight));
    	scrollPanel.setSize(w - scrollBarWidth, 10 + panel3DSize + functieEditorHeight);
    	scrollPanel.setPreferredSize(new Dimension(w - scrollBarWidth, 10 + panel3DSize + functieEditorHeight));    	
    	
    	panel3D.setBounds(10, 10, panel3DSize, panel3DSize);
    	scrollPanel.add(panel3D);
    	
    	functieEditor = new FunctieEditor(false);
    	functieEditor.setBounds(10,
    							panel3D.getLocation().y + panel3D.getSize().height, 
    							panel3DSize, functieEditorHeight);
    	functieEditor.zetGrafiek3DComponent(this);
    	functieEditor.zetFuncties(objectType, false);
    	scrollPanel.add(functieEditor);                        
    	
    	scrollPane = new JScrollPane(scrollPanel);
    	scrollPane.setBounds(0, 0, w, h);
    	scrollPane.setPreferredSize(new Dimension(w, h));
    	add(scrollPane);
    	
    	knoppenPanel = new JPanel();
    	knoppenPanel.setLayout(null);
    	knoppenPanel.setOpaque(false);
    	knoppenPanel.setBounds(10 + panel3DSize, 0, w - panel3DSize - 30, panel3DSize);
    	scrollPanel.add(knoppenPanel);
//System.out.println("kpw = " + knoppenPanel.getWidth());    	

    	zoomStandaardButton	= new ZoomKnop("standaard");
    	zoomStandaardButton.setVisible(false);
    	knoppenPanel.add(zoomStandaardButton);
    	zoomStandaardButton.addActionListener(this);
    	
		zoomInButton = new ZoomKnop("zoomin", getImage("zoominknop.gif"));
    	zoomInButton.setVisible(false);
    	knoppenPanel.add(zoomInButton);
    	zoomInButton.addActionListener(this);
		
		zoomUitButton = new ZoomKnop("zoomuit", getImage("zoomuitknop.gif"));
    	zoomUitButton.setVisible(false);
    	knoppenPanel.add(zoomUitButton);
    	zoomUitButton.addActionListener(this);
		
		transPlusButton = new ZoomKnop("transplus");
    	transPlusButton.setVisible(false);
    	knoppenPanel.add(transPlusButton);
    	transPlusButton.addActionListener(this);    	
		
		asNaamButton = new ZoomKnop("xasnaam");
    	asNaamButton.setVisible(false);
    	knoppenPanel.add(asNaamButton);
    	asNaamButton.addActionListener(this);        	
		
		transMinButton = new ZoomKnop("transmin");
    	transMinButton.setVisible(false);
		knoppenPanel.add(transMinButton);
    	transMinButton.addActionListener(this);    		
		
		solidDraadKeuzeButton = new ZoomKnop("draad");
		solidDraadKeuzeButton.setVisible(false);
      	knoppenPanel.add(solidDraadKeuzeButton);
    	solidDraadKeuzeButton.addActionListener(this);
		
		finerPlusButton = new ZoomKnop("finerplus");
    	finerPlusButton.setVisible(false);
    	knoppenPanel.add(finerPlusButton);
    	finerPlusButton.addActionListener(this);
		
		finerMinButton = new ZoomKnop("finermin");
    	finerMinButton.setVisible(false);
    	knoppenPanel.add(finerMinButton);
    	finerMinButton.addActionListener(this);
		
		asKeuzeButton = new ZoomKnop("askeuze");
    	asKeuzeButton.setVisible(false);
		knoppenPanel.add(asKeuzeButton);
       	asKeuzeButton.addActionListener(this);

		labelKeuzeButton = new ZoomKnop("labelkeuze");
    	labelKeuzeButton.setVisible(false);
		knoppenPanel.add(labelKeuzeButton);
    	labelKeuzeButton.addActionListener(this);
		
    	
    	layoutKnoppenPanel();    	
    	
		assenPopup = new JPopupMenu();
		JMenuItem mi = new JMenuItem(Grafiek3DTest.rb.getString("geenAssenTekst"));
		mi.addActionListener(this);
		assenPopup.add(mi);
		mi = new JMenuItem(Grafiek3DTest.rb.getString("xyzAsTekst"));
		mi.addActionListener(this);
		assenPopup.add(mi);
		mi = new JMenuItem(Grafiek3DTest.rb.getString("xyVloerTekst"));
		mi.addActionListener(this);
		assenPopup.add(mi);
		
		add(assenPopup);
		
		labelsPopup = new JPopupMenu();
		mi = new JMenuItem(Grafiek3DTest.rb.getString("geenLabelsTekst"));
		mi.addActionListener(this);
		labelsPopup.add(mi);
		mi = new JMenuItem(Grafiek3DTest.rb.getString("eindLabelsTekst"));
		mi.addActionListener(this);
		labelsPopup.add(mi);
		mi = new JMenuItem(Grafiek3DTest.rb.getString("alleLabelsTekst"));
		mi.addActionListener(this);
		labelsPopup.add(mi);
		
		add(labelsPopup);
		
        MLMML ml = new MLMML();
        panel3D.addMouseListener(ml);
        panel3D.addMouseMotionListener(ml);        
        
        setNewModel(0, true);
    	
        //setSize(w, h);
    }
    
    public void setSize(int b, int h)
    {
    	panel3DSize = b - 10 - knoppenPanelWidth - scrollBarWidth;
    	
    	super.setSize(b, h);
    	// breedtes
    	scrollPane.setSize(b, h);
    	scrollPane.setPreferredSize(new Dimension(b, h));
    	scrollPanel.setSize(b - scrollBarWidth, 10 + panel3DSize + functieEditorHeight);
    	scrollPanel.setPreferredSize(new Dimension(b - scrollBarWidth, 10 + panel3DSize + functieEditorHeight));
    	panel3D.setSize(panel3DSize, panel3DSize);
    	knoppenPanel.setLocation(10 + panel3DSize, getLocation().y);
    	functieEditor.setBounds(10, 10 + panel3DSize, panel3DSize, functieEditorHeight);
    	setNewModel(0, false);
    }
    
	public Image getImage(String name)
	{	
		return (Image) images.get(name);
	}
    
    public void layoutKnoppenPanel()
    {
    	int currentY = 10;

    	if (zoomOptie || translateOptie)
    	{
    		zoomStandaardButton.setBounds(10, currentY, 23, 23);
    		zoomStandaardButton.setVisible(true);
    		currentY += 29;
    	}
    	else
    	{	zoomStandaardButton.setVisible(false);
    	}

    	if (zoomOptie)
    	{	
    		zoomInButton.setBounds(10, currentY, 21, 21);
    		zoomInButton.setVisible(true);
    		currentY += 26;
		
    		zoomUitButton.setBounds(10, currentY, 21, 21);
    		zoomUitButton.setVisible(true);
    		currentY += 31;
    	}
    	else
    	{	zoomInButton.setVisible(false);
    		zoomUitButton.setVisible(false);
    	}
    	
    	if (translateOptie)
    	{	
    		transPlusButton.setBounds(10, currentY, 21, 21);
    		transPlusButton.setVisible(true);
    		currentY += 26;
		
    		asNaamButton.setBounds(5, currentY, 31, 21);
    		asNaamButton.setVisible(true);
    		currentY += 26;
		
    		transMinButton.setBounds(10, currentY, 21, 21);
    		transMinButton.setVisible(true);
    		currentY += 31;
    	}
    	else
    	{	transPlusButton.setVisible(false);
    		asNaamButton.setVisible(false);
    		transMinButton.setVisible(false);
    	}
    	
    	if (solidDraadKeuzeOptie && (objectType != CURVE))
    	{	
    		solidDraadKeuzeButton.setBounds(10, currentY, 21, 21);
    		solidDraadKeuzeButton.setVisible(true);
    		currentY += 31;
    		if (objectType == FUNCTION)
    		{
    			if (wireFrameG)
    				solidDraadKeuzeButton.setCode("solid");
    			else
    				solidDraadKeuzeButton.setCode("draad");
    		}
    		if (objectType == SURFACE)
    		{
    			if (wireFrameS)
    				solidDraadKeuzeButton.setCode("solid");
    			else
    				solidDraadKeuzeButton.setCode("draad");
    		}
    		
    	}
    	else
    	{	solidDraadKeuzeButton.setVisible(false);
    	}
    	
    	if (finerKeuzeOptie && (objectType == FUNCTION))
    	{	
    		finerPlusButton.setBounds(10, currentY, 21, 21);
    		finerPlusButton.setVisible(true);
    		currentY += 26;
		
    		finerMinButton.setBounds(10, currentY, 21, 21);
    		finerMinButton.setVisible(true);
    		if (xFinerStepsG == 2)
    			finerMinButton.setEnabled(false);
    		else
    			finerMinButton.setEnabled(true);
    		currentY += 31;
    	}
    	else
    	{	finerPlusButton.setVisible(false);
			finerMinButton.setVisible(false);
    	}
    	
    	if (asKeuzeOptie)
    	{	
    		asKeuzeButton.setBounds(10, currentY, 21, 21);
    		asKeuzeButton.setVisible(true);
    		currentY += 31;
    	}
    	else
    	{	asKeuzeButton.setVisible(false);
    	}
    	
    	if (labelKeuzeOptie)
    	{	
    		labelKeuzeButton.setBounds(5, currentY, 31, 21);
    		labelKeuzeButton.setVisible(true);
    	}
    	else
    	{	labelKeuzeButton.setVisible(false);
    	}
    	
    	knoppenPanel.repaint();
    	
    }
    
    // changing the model to a new one
    public void setNewModel(int modelCode, boolean reallyNew)
    {
//    	busy = true;
    	
        setProjection(defaultProjection);
        
        panel3D.hideHelpLine();                
        panel3D.hideHelpPoint();                

        panel3D.testString = "";

        mouseMode = INERT;
//      history.removeAllElements();        

        //zetHoeken();
			
        currentObjectGroup = makeNewModel(modelCode);        
   	    // HIER!
        //setFilled(false);        
   	    panel3D.initializeModel(currentObjectGroup, false);

        // reset zooming HERE
   	    zoom = defaultZoom;
       	panel3D.setZoomFactor(zoom);        
        
//      addToHistory();

//        busy = false;
    }    
    
    public void zetHoeken()
    {
//System.out.println("zetH " + objectType);

        if (objectType == FUNCTION)
        {	panel3D.zetHoeken(angleXG, angleZG);
        }
        else if (objectType == SURFACE)
        {	panel3D.zetHoeken(angleXS, angleZS);
        }
        else if (objectType == CURVE)
        {	panel3D.zetHoeken(angleXC, angleZC);
        }
    	
    }
    
    public void getHoeken()
    {
//System.out.println("getH " + objectType);    	
        if (objectType == FUNCTION)
        {	angleXG = panel3D.angleX;
        	angleZG = panel3D.angleZ;
        }
        else if (objectType == SURFACE)
        {	angleXS = panel3D.angleX;
    		angleZS = panel3D.angleZ;
        }
        else if (objectType == CURVE)
        {	angleXC = panel3D.angleX;
    		angleZC = panel3D.angleZ;
        }
    	
    }
    
    public ObjectGroup3D makeNewModel(int code)
    {   //Object3D axesModel;
    	//Object3D graph3DModel;
        ObjectGroup3D modelGroup = null;

        // default?
// binnenvulling is onzichtbaar
// maar voor buitenkant toch NZMINFIRST
// is dit ook OK voor filled = false?
        //panel3D.paintType = Object3DContainer.PUREZ;
        
    	axesObject = makeNewAxes();
//System.out.println("axes diam = " + axesModel.getDiameter());    	
    	//axesModel = new Box(xMax - xMin, yMax - yMin, zMax - zMin, Color.yellow);        
    	
        if (((objectType == FUNCTION) && !noAxesG) ||
        	((objectType == SURFACE) && !noAxesS) ||
        	((objectType == CURVE) && !noAxesC)
           )
        {	
        	modelGroup = new ObjectGroup3D(axesObject, false);
        	modelGroup.numVertexLabels = axesObject.numVertexLabels;
        }
        
        if (grafiek3DExpressie != null)
        {
        	grafiek3DObject = makeGrafiek3D();
        	grafiek3DObject.modelCode = code;
        	if (modelGroup == null) // geen assen
        	{	grafiek3DObject.diameter = axesObject.getDiameter();
        		grafiek3DObject.diamSet = true;
        		modelGroup = new ObjectGroup3D(grafiek3DObject, false);
        	}
        	else // wel assen
        	{	modelGroup.addObject3D(grafiek3DObject);
        	}
        }
        
        if (surfaceXExpressie != null)
        {	surface3DObject = makeSurface3D();
        	surface3DObject.modelCode = code;
        	if (modelGroup == null) // geen assen
        	{	surface3DObject.diameter = axesObject.getDiameter();
        		surface3DObject.diamSet = true;
        		modelGroup = new ObjectGroup3D(surface3DObject, false);
        	}
        	else
        	{	
// HIER SNIJDEN MET ASSEN        		
        		modelGroup.addObject3D(surface3DObject);
        	}
        	
        }

        if (curveXExpressie != null)
        {
        	curve3DObject = makeCurve3D();
        	curve3DObject.modelCode = code;
        	if (modelGroup == null) // geen assen
        	{	curve3DObject.diameter = axesObject.getDiameter();
        		curve3DObject.diamSet = true;
        		modelGroup = new ObjectGroup3D(curve3DObject, false);
        	}
        	else
        	{	
// HIER SNIJDEN MET ASSEN        		
        		modelGroup.addObject3D(curve3DObject);
        	}
        }
        
        //System.out.println("model-numFacets = " + model.numFacets);        
        //modelGroup = new ObjectGroup3D(model, false);
        //modelGroup.numVertexLabels = axisModel.numVertexLabels;
        return modelGroup;
    }   
    
    public Axes makeNewAxes()
    {	Axes axes = null;
    	
    	if (objectType == FUNCTION)
    		return new Axes(xMinG, xMaxG, xStepG, yMinG, yMaxG, yStepG, zMinG, zMaxG, zStepG, 
    						floorTypeG, labelTypeG, xFinerStepsG, yFinerStepsG);
    	else if (objectType == SURFACE)
    		return new Axes(xMinS, xMaxS, xStepS, yMinS, yMaxS, yStepS, zMinS, zMaxS, zStepS, 
						    floorTypeS, labelTypeS, xFinerStepsS, yFinerStepsS);
    	else if (objectType == CURVE)
    		return new Axes(xMinC, xMaxC, xStepC, yMinC, yMaxC, yStepC, zMinC, zMaxC, zStepC, 
						    floorTypeC, labelTypeC, xFinerStepsC, yFinerStepsC);
    	
    	return axes;	
    }
    
    public Grafiek3D makeGrafiek3D()
    {	grafiek3DObject = new Grafiek3D(grafiek3DExpressie, 
					 				    xMinG, xMaxG, xStepG, yMinG, yMaxG, yStepG, zMinG, zMaxG, zStepG, 
			 					 		varNaamX, varNaamY, xFinerStepsG, yFinerStepsG);    	 
    
    
    	if (grafiek3DObject.trimTop)
    	{	
//System.out.println("trimTop");    		
    		Plane3D zMaxPlane = new Plane3D(0, 0, 1, zMaxG);
    		ObjectGroup3D grafiek3DObjectGroup = new ObjectGroup3D(grafiek3DObject, false);
    		ObjectGroup3D topTrimmedGroup = cutObjectGroup(grafiek3DObjectGroup, zMaxPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			grafiek3DObject = (Grafiek3D) topTrimmedGroup.objects.elementAt(0);
			if (grafiek3DObject.insideVertex != null)
			{	if (grafiek3DObject.containsVertex(grafiek3DObject.insideVertex) < 0)
					grafiek3DObject = (Grafiek3D) topTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (grafiek3DObject.containsVertex(grafiek3DObject.topMaxVertex) >= 0)
					grafiek3DObject = (Grafiek3D) topTrimmedGroup.objects.elementAt(1);
			}	
//System.out.println("topMax = " + grafiek3DObject.topMaxVertex.toString());
//if (grafiek3DObject.insideVertex != null)
//System.out.println("inside = " + grafiek3DObject.insideVertex.toString());

//if (grafiek3DObject instanceof Grafiek3D)
//System.out.println("Grafiek3D");	
    		
    	}
    	if (grafiek3DObject.trimBottom)
    	{
//System.out.println("trimBottom");    		
    		
    		Plane3D zMinPlane = new Plane3D(0, 0, 1, zMinG);
    		ObjectGroup3D grafiek3DObjectGroup = new ObjectGroup3D(grafiek3DObject, false);
    		ObjectGroup3D bottomTrimmedGroup = cutObjectGroup(grafiek3DObjectGroup, zMinPlane);

    		// hier nog kiezen !!
    		
			grafiek3DObject = (Grafiek3D) bottomTrimmedGroup.objects.elementAt(0);
			if (grafiek3DObject.insideVertex != null)
			{	if (grafiek3DObject.containsVertex(grafiek3DObject.insideVertex) < 0)
					grafiek3DObject = (Grafiek3D) bottomTrimmedGroup.objects.elementAt(1);
			}
			else
			{
				if (grafiek3DObject.containsVertex(grafiek3DObject.bottomMinVertex) >= 0)
					grafiek3DObject = (Grafiek3D) bottomTrimmedGroup.objects.elementAt(1);
			}	
//System.out.println("bottomMin = " + grafiek3DObject.bottomMinVertex.toString());    		
    	}
    
    	grafiek3DObject.setOutlineColor(graphOutlineColor);
    	
    	if (wireFrameG)
    		zetDraadFiguur(true, objectType);
    	
    	return grafiek3DObject; 
    }
  
    public void zetGrafiek3D(Expressie exp)
    {
//System.out.println("zetGrafiek3D");    	
    	grafiek3DExpressie = exp;
    	
//    	if (noAxesG)
//    		setNewModel(0, true);
//    	else
    		setNewModel(0, false);
    }
    
    public Surface3D makeSurface3D()
    {
    	surface3DObject = new Surface3D(surfaceXExpressie, surfaceYExpressie, surfaceZExpressie,
    			                        uMin, uMax, uPoints, vMin, vMax, vPoints,
    			                        xMinS, xMaxS, yMinS, yMaxS, zMinS, zMaxS,
    			                        paramNaamU, paramNaamV);

    	if (surface3DObject.trimTop)
    	{	
//System.out.println("trimTop");    		
    		Plane3D zMaxPlane = new Plane3D(0, 0, 1, zMaxS);
    		ObjectGroup3D surface3DObjectGroup = new ObjectGroup3D(surface3DObject, false);
    		ObjectGroup3D topTrimmedGroup = cutObjectGroup(surface3DObjectGroup, zMaxPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			surface3DObject = (Surface3D) topTrimmedGroup.objects.elementAt(0);
			if (surface3DObject.insideVertex != null)
			{	if (surface3DObject.containsVertex(surface3DObject.insideVertex) < 0)
					surface3DObject = (Surface3D) topTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (surface3DObject.containsVertex(surface3DObject.topMaxVertex) >= 0)
					surface3DObject = (Surface3D) topTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("topMax = " + surface3DObject.topMaxVertex.toString());
//if (surface3DObject.insideVertex != null)
//System.out.println("inside = " + surface3DObject.insideVertex.toString());

    	}
    	if (surface3DObject.trimBottom)
    	{	
//System.out.println("trimBottom");    		
    		Plane3D zMinPlane = new Plane3D(0, 0, 1, zMinS);
    		ObjectGroup3D surface3DObjectGroup = new ObjectGroup3D(surface3DObject, false);
    		ObjectGroup3D bottomTrimmedGroup = cutObjectGroup(surface3DObjectGroup, zMinPlane);
//System.out.println("ttsize = " + bottomTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			surface3DObject = (Surface3D) bottomTrimmedGroup.objects.elementAt(0);
			if (surface3DObject.insideVertex != null)
			{	if (surface3DObject.containsVertex(surface3DObject.insideVertex) < 0)
					surface3DObject = (Surface3D) bottomTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (surface3DObject.containsVertex(surface3DObject.bottomMinVertex) >= 0)
					surface3DObject = (Surface3D) bottomTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("bottomMin = " + surface3DObject.bottomMinVertex.toString());
//if (surface3DObject.insideVertex != null)
//System.out.println("inside = " + surface3DObject.insideVertex.toString());

    	}
    	if (surface3DObject.trimRight)
    	{	
//System.out.println("trimRight");    		
    		Plane3D xMaxPlane = new Plane3D(1, 0, 0, xMaxS);
    		ObjectGroup3D surface3DObjectGroup = new ObjectGroup3D(surface3DObject, false);
    		ObjectGroup3D rightTrimmedGroup = cutObjectGroup(surface3DObjectGroup, xMaxPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			surface3DObject = (Surface3D) rightTrimmedGroup.objects.elementAt(0);
			if (surface3DObject.insideVertex != null)
			{	if (surface3DObject.containsVertex(surface3DObject.insideVertex) < 0)
					surface3DObject = (Surface3D) rightTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (surface3DObject.containsVertex(surface3DObject.rightMaxVertex) >= 0)
					surface3DObject = (Surface3D) rightTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("rightMax = " + surface3DObject.rightMaxVertex.toString());
//if (surface3DObject.insideVertex != null)
//System.out.println("inside = " + surface3DObject.insideVertex.toString());

    	}
    	if (surface3DObject.trimLeft)
    	{	
//System.out.println("trimLeft");    		
    		Plane3D xMinPlane = new Plane3D(1, 0, 0, xMinS);
    		ObjectGroup3D surface3DObjectGroup = new ObjectGroup3D(surface3DObject, false);
    		ObjectGroup3D leftTrimmedGroup = cutObjectGroup(surface3DObjectGroup, xMinPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			surface3DObject = (Surface3D) leftTrimmedGroup.objects.elementAt(0);
			if (surface3DObject.insideVertex != null)
			{	if (surface3DObject.containsVertex(surface3DObject.insideVertex) < 0)
					surface3DObject = (Surface3D) leftTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (surface3DObject.containsVertex(surface3DObject.leftMinVertex) >= 0)
					surface3DObject = (Surface3D) leftTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("leftMin = " + surface3DObject.leftMinVertex.toString());
//if (surface3DObject.insideVertex != null)
//System.out.println("inside = " + surface3DObject.insideVertex.toString());

    	}
    	if (surface3DObject.trimBack)
    	{	
//System.out.println("trimBack");    		
    		Plane3D yMaxPlane = new Plane3D(0, 1, 0, yMaxS);
    		ObjectGroup3D surface3DObjectGroup = new ObjectGroup3D(surface3DObject, false);
    		ObjectGroup3D backTrimmedGroup = cutObjectGroup(surface3DObjectGroup, yMaxPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			surface3DObject = (Surface3D) backTrimmedGroup.objects.elementAt(0);
			if (surface3DObject.insideVertex != null)
			{	if (surface3DObject.containsVertex(surface3DObject.insideVertex) < 0)
					surface3DObject = (Surface3D) backTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (surface3DObject.containsVertex(surface3DObject.backMaxVertex) >= 0)
					surface3DObject = (Surface3D) backTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("backMax = " + surface3DObject.backMaxVertex.toString());
//if (surface3DObject.insideVertex != null)
//System.out.println("inside = " + surface3DObject.insideVertex.toString());

    	}
    	if (surface3DObject.trimFront)
    	{	
//System.out.println("trimFront");    		
    		Plane3D yMinPlane = new Plane3D(0, 1, 0, yMinS);
    		ObjectGroup3D surface3DObjectGroup = new ObjectGroup3D(surface3DObject, false);
    		ObjectGroup3D frontTrimmedGroup = cutObjectGroup(surface3DObjectGroup, yMinPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			surface3DObject = (Surface3D) frontTrimmedGroup.objects.elementAt(0);
			if (surface3DObject.insideVertex != null)
			{	if (surface3DObject.containsVertex(surface3DObject.insideVertex) < 0)
					surface3DObject = (Surface3D) frontTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (surface3DObject.containsVertex(surface3DObject.frontMinVertex) >= 0)
					surface3DObject = (Surface3D) frontTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("frontMin = " + surface3DObject.frontMinVertex.toString());
//if (surface3DObject.insideVertex != null)
//System.out.println("inside = " + surface3DObject.insideVertex.toString());

    	}
    	
    	surface3DObject.setOutlineColor(surfaceOutlineColor);
    	
    	if (wireFrameS)
    		zetDraadFiguur(true, objectType);

    	
    	return surface3DObject;
    }
    
    
    public Curve3D makeCurve3D()
    {
    	curve3DObject = new Curve3D(curveXExpressie, curveYExpressie, curveZExpressie,
                tMin, tMax, tPoints,
                xMinC, xMaxC, yMinC, yMaxC, zMinC, zMaxC,
                paramNaam);

    	if (curve3DObject.trimTop)
    	{	
//System.out.println("trimTop");    		
    		Plane3D zMaxPlane = new Plane3D(0, 0, 1, zMaxS);
    		ObjectGroup3D curve3DObjectGroup = new ObjectGroup3D(curve3DObject, false);
    		ObjectGroup3D topTrimmedGroup = cutObjectGroup(curve3DObjectGroup, zMaxPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			curve3DObject = (Curve3D) topTrimmedGroup.objects.elementAt(0);
			if (curve3DObject.insideVertex != null)
			{	if (curve3DObject.containsVertex(curve3DObject.insideVertex) < 0)
					curve3DObject = (Curve3D) topTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (curve3DObject.containsVertex(curve3DObject.topMaxVertex) >= 0)
					curve3DObject = (Curve3D) topTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("topMax = " + curve3DObject.topMaxVertex.toString());
//if (curve3DObject.insideVertex != null)
//System.out.println("inside = " + curve3DObject.insideVertex.toString());

    	}
    	if (curve3DObject.trimBottom)
    	{	
//System.out.println("trimBottom");    		
    		Plane3D zMinPlane = new Plane3D(0, 0, 1, zMinS);
    		ObjectGroup3D curve3DObjectGroup = new ObjectGroup3D(curve3DObject, false);
    		ObjectGroup3D bottomTrimmedGroup = cutObjectGroup(curve3DObjectGroup, zMinPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			curve3DObject = (Curve3D) bottomTrimmedGroup.objects.elementAt(0);
			if (curve3DObject.insideVertex != null)
			{	if (curve3DObject.containsVertex(curve3DObject.insideVertex) < 0)
					curve3DObject = (Curve3D) bottomTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (curve3DObject.containsVertex(curve3DObject.bottomMinVertex) >= 0)
					curve3DObject = (Curve3D) bottomTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("bottomMin = " + curve3DObject.bottomMinVertex.toString());
//if (curve3DObject.insideVertex != null)
//System.out.println("inside = " + curve3DObject.insideVertex.toString());

    	}
    	if (curve3DObject.trimRight)
    	{	
//System.out.println("trimRight");    		
    		Plane3D xMaxPlane = new Plane3D(1, 0, 0, xMaxS);
    		ObjectGroup3D curve3DObjectGroup = new ObjectGroup3D(curve3DObject, false);
    		ObjectGroup3D rightTrimmedGroup = cutObjectGroup(curve3DObjectGroup, xMaxPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			curve3DObject = (Curve3D) rightTrimmedGroup.objects.elementAt(0);
			if (curve3DObject.insideVertex != null)
			{	if (curve3DObject.containsVertex(curve3DObject.insideVertex) < 0)
					curve3DObject = (Curve3D) rightTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (curve3DObject.containsVertex(curve3DObject.rightMaxVertex) >= 0)
					curve3DObject = (Curve3D) rightTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("rightMax = " + curve3DObject.rightMaxVertex.toString());
//if (curve3DObject.insideVertex != null)
//System.out.println("inside = " + curve3DObject.insideVertex.toString());

    	}
    	if (curve3DObject.trimLeft)
    	{	
//System.out.println("trimLeft");    		
    		Plane3D xMinPlane = new Plane3D(1, 0, 0, xMinS);
    		ObjectGroup3D curve3DObjectGroup = new ObjectGroup3D(curve3DObject, false);
    		ObjectGroup3D leftTrimmedGroup = cutObjectGroup(curve3DObjectGroup, xMinPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			curve3DObject = (Curve3D) leftTrimmedGroup.objects.elementAt(0);
			if (curve3DObject.insideVertex != null)
			{	if (curve3DObject.containsVertex(curve3DObject.insideVertex) < 0)
					curve3DObject = (Curve3D) leftTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (curve3DObject.containsVertex(curve3DObject.leftMinVertex) >= 0)
					curve3DObject = (Curve3D) leftTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("leftMin = " + curve3DObject.leftMinVertex.toString());
//if (curve3DObject.insideVertex != null)
//System.out.println("inside = " + curve3DObject.insideVertex.toString());

    	}
    	if (curve3DObject.trimBack)
    	{	
//System.out.println("trimBack");    		
    		Plane3D yMaxPlane = new Plane3D(0, 1, 0, yMaxS);
    		ObjectGroup3D curve3DObjectGroup = new ObjectGroup3D(curve3DObject, false);
    		ObjectGroup3D backTrimmedGroup = cutObjectGroup(curve3DObjectGroup, yMaxPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			curve3DObject = (Curve3D) backTrimmedGroup.objects.elementAt(0);
			if (curve3DObject.insideVertex != null)
			{	if (curve3DObject.containsVertex(curve3DObject.insideVertex) < 0)
					curve3DObject = (Curve3D) backTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (curve3DObject.containsVertex(curve3DObject.backMaxVertex) >= 0)
					curve3DObject = (Curve3D) backTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("backMax = " + curve3DObject.backMaxVertex.toString());
//if (curve3DObject.insideVertex != null)
//System.out.println("inside = " + curve3DObject.insideVertex.toString());

    	}
    	if (curve3DObject.trimFront)
    	{	
//System.out.println("trimFront");    		
    		Plane3D yMinPlane = new Plane3D(0, 1, 0, yMinS);
    		ObjectGroup3D curve3DObjectGroup = new ObjectGroup3D(curve3DObject, false);
    		ObjectGroup3D frontTrimmedGroup = cutObjectGroup(curve3DObjectGroup, yMinPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

    		// hier nog kiezen !!
    		
			curve3DObject = (Curve3D) frontTrimmedGroup.objects.elementAt(0);
			if (curve3DObject.insideVertex != null)
			{	if (curve3DObject.containsVertex(curve3DObject.insideVertex) < 0)
					curve3DObject = (Curve3D) frontTrimmedGroup.objects.elementAt(1);
			}
			else
			{	if (curve3DObject.containsVertex(curve3DObject.frontMinVertex) >= 0)
					curve3DObject = (Curve3D) frontTrimmedGroup.objects.elementAt(1);
			}
			
//System.out.println("frontMin = " + curve3DObject.frontMinVertex.toString());
//if (curve3DObject.insideVertex != null)
//System.out.println("inside = " + curve3DObject.insideVertex.toString());

    	}
    	
    	return curve3DObject;

    }
    
    public void zetSurface3D(Expressie xExp, Expressie yExp, Expressie zExp, 
    						 double uMi, double uMa, int uPo,
    						 double vMi, double vMa, int vPo)
    {	if (xExp == null)
    	{	surfaceXExpressie = xExp;
    	}
    	else
    	{	surfaceXExpressie = xExp;
    		surfaceYExpressie = yExp;
    		surfaceZExpressie = zExp;
    		uMin = uMi;
    		uMax = uMa;
    		uPoints = uPo;
    		vMin = vMi;
    		vMax = vMa;
    		vPoints = vPo;
    		
    	}

//    	if (noAxesS)
//    		setNewModel(0, true);
//    	else
    		setNewModel(0, false);
    
    }
    
    public void zetCurve3D(Expressie xExp, Expressie yExp, Expressie zExp, 
			 			   double tMi, double tMa, int tPo)
    {	if (xExp == null)
    	{	curveXExpressie = xExp;
    	}
    	else
    	{	curveXExpressie = xExp;
    		curveYExpressie = yExp;
    		curveZExpressie = zExp;
    		tMin = tMi;
    		tMax = tMa;
    		tPoints = tPo;

    	}

//    	if (noAxesC)
//    		setNewModel(0, true);
//    	else
    		setNewModel(0, false);

    }
    
    
    public void setProjection(int proj)
    {   if (proj == CENTRALPROJ)
            defaultProjection = CENTRALPROJ;
        if (proj == PARALLELPROJ)    
            defaultProjection = PARALLELPROJ;    
        panel3D.setProjection(defaultProjection);
    }

/*    
    public void setLetters(boolean b)
    {   letters = b;
        panel3D.repaint();
    }
*/    
    
    public void zoomStandaard(boolean newModel, int objectType)
    {
    	if (objectType == FUNCTION)
    	{	
    		xMinG = xMinBegin;
    		xMaxG = xMaxBegin;
    		xStepG = xStepBegin;
    		yMinG = yMinBegin;
    		yMaxG = yMaxBegin;
    		yStepG = yStepBegin;
    		zMinG = zMinBegin;
    		zMaxG = zMaxBegin;
    		zStepG = zStepBegin;
    		xFinerStepsG = xFinerStepsBegin;
    		yFinerStepsG = yFinerStepsBegin;
    		finerMinButton.setEnabled(false);
    	
    		zoomFactorG = 0;
    		translateXFactorG = 0;
    		translateYFactorG = 0;
    		translateZFactorG = 0;
    		finerFactorG = 0;
    		
    		angleXG = Object3DContainer.angleXStart;
    		angleZG = Object3DContainer.angleZStart;
    	}
    	else if (objectType == SURFACE)
    	{	
    		xMinS = xMinBegin;
    		xMaxS = xMaxBegin;
    		xStepS = xStepBegin;
    		yMinS = yMinBegin;
    		yMaxS = yMaxBegin;
    		yStepS = yStepBegin;
    		zMinS = zMinBegin;
    		zMaxS = zMaxBegin;
    		zStepS = zStepBegin;
    		xFinerStepsS = xFinerStepsBegin;
    		yFinerStepsS = yFinerStepsBegin;
    	
    		zoomFactorS = 0;
    		translateXFactorS = 0;
    		translateYFactorS = 0;
    		translateZFactorS = 0;
    		
    		angleXS = Object3DContainer.angleXStart;
    		angleZS = Object3DContainer.angleZStart;
    		
    	}
    	else if (objectType == CURVE)
    	{	
    		xMinC = xMinBegin;
    		xMaxC = xMaxBegin;
    		xStepC = xStepBegin;
    		yMinC = yMinBegin;
    		yMaxC = yMaxBegin;
    		yStepC = yStepBegin;
    		zMinC = zMinBegin;
    		zMaxC = zMaxBegin;
    		zStepC = zStepBegin;
    		xFinerStepsC = xFinerStepsBegin;
    		yFinerStepsC = yFinerStepsBegin;
    	
    		zoomFactorC = 0;
    		translateXFactorC = 0;
    		translateYFactorC = 0;
    		translateZFactorC = 0;
    		
    		angleXC = Object3DContainer.angleXStart;
    		angleZC = Object3DContainer.angleZStart;
    		
    	}
    	
    	if (newModel)
    	{	
    		zetHoeken();
    		setNewModel(0, false);
    	
    	}
    }
    
    public void zoomIn(boolean newModel, int objectType)
    {	
    	if (objectType == FUNCTION)
    	{	
    		double centerX = (xMinG + xMaxG) / 2;
    		double centerY = (yMinG + yMaxG) / 2;
    		double centerZ = (zMinG + zMaxG) / 2;
    		xMaxG = centerX + (xMaxG - centerX) / 2;
    		xMinG = centerX - (centerX - xMinG) / 2;
    		yMaxG = centerY + (yMaxG - centerY) / 2;
    		yMinG = centerY - (centerY - yMinG) / 2;
    		zMaxG = centerZ + (zMaxG - centerZ) / 2;
    		zMinG = centerZ - (centerZ - zMinG) / 2;
    		xStepG /= 2;
    		yStepG /= 2;
    		zStepG /= 2;
    		zoomFactorG++;

    	}
    	else if (objectType == SURFACE)
    	{	
    		double centerX = (xMinS + xMaxS) / 2;
    		double centerY = (yMinS + yMaxS) / 2;
    		double centerZ = (zMinS + zMaxS) / 2;
    		xMaxG = centerX + (xMaxS - centerX) / 2;
    		xMinG = centerX - (centerX - xMinS) / 2;
    		yMaxG = centerY + (yMaxS - centerY) / 2;
    		yMinG = centerY - (centerY - yMinS) / 2;
    		zMaxG = centerZ + (zMaxS - centerZ) / 2;
    		zMinG = centerZ - (centerZ - zMinS) / 2;
    		xStepS /= 2;
    		yStepS /= 2;
    		zStepS /= 2;
    		zoomFactorS++;

    	}
    	else if (objectType == CURVE)
    	{	
    		double centerX = (xMinC + xMaxC) / 2;
    		double centerY = (yMinC + yMaxC) / 2;
    		double centerZ = (zMinC + zMaxC) / 2;
    		xMaxG = centerX + (xMaxC - centerX) / 2;
    		xMinG = centerX - (centerX - xMinC) / 2;
    		yMaxG = centerY + (yMaxC - centerY) / 2;
    		yMinG = centerY - (centerY - yMinC) / 2;
    		zMaxG = centerZ + (zMaxC - centerZ) / 2;
    		zMinG = centerZ - (centerZ - zMinC) / 2;
    		xStepC /= 2;
    		yStepC /= 2;
    		zStepC /= 2;
    		zoomFactorC++;

    	}
    	

    	if (newModel)    	
		{	setNewModel(0, false);
		}
    	
    }
    
    public void zoomUit(boolean newModel, int objectType)
    {
    	
    	if (objectType == FUNCTION)
    	{	
    		double centerX = (xMinG + xMaxG) / 2;
    		double centerY = (yMinG + yMaxG) / 2;
    		double centerZ = (zMinG + zMaxG) / 2;
    		xMaxG = centerX + (xMaxG - centerX) * 2;
    		xMinG = centerX - (centerX - xMinG) * 2;
    		yMaxG = centerY + (yMaxG - centerY) * 2;
    		yMinG = centerY - (centerY - yMinG) * 2;
    		zMaxG = centerZ + (zMaxG - centerZ) * 2;
    		zMinG = centerZ - (centerZ - zMinG) * 2;
    		xStepG *= 2;
    		yStepG *= 2;
    		zStepG *= 2;
    		zoomFactorG--;
    	}
    	else if (objectType == SURFACE)
    	{	
    		double centerX = (xMinS + xMaxS) / 2;
    		double centerY = (yMinS + yMaxS) / 2;
    		double centerZ = (zMinS + zMaxS) / 2;
    		xMaxG = centerX + (xMaxS - centerX) * 2;
    		xMinG = centerX - (centerX - xMinS) * 2;
    		yMaxG = centerY + (yMaxS - centerY) * 2;
    		yMinG = centerY - (centerY - yMinS) * 2;
    		zMaxG = centerZ + (zMaxS - centerZ) * 2;
    		zMinG = centerZ - (centerZ - zMinS) * 2;
    		xStepS *= 2;
    		yStepS *= 2;
    		zStepS *= 2;
    		zoomFactorS--;
    	}
    	else if (objectType == CURVE)
    	{	
    		double centerX = (xMinC + xMaxC) / 2;
    		double centerY = (yMinC + yMaxC) / 2;
    		double centerZ = (zMinC + zMaxC) / 2;
    		xMaxG = centerX + (xMaxC - centerX) * 2;
    		xMinG = centerX - (centerX - xMinC) * 2;
    		yMaxG = centerY + (yMaxC - centerY) * 2;
    		yMinG = centerY - (centerY - yMinC) * 2;
    		zMaxG = centerZ + (zMaxC - centerZ) * 2;
    		zMinG = centerZ - (centerZ - zMinC) * 2;
    		xStepC *= 2;
    		yStepC *= 2;
    		zStepC *= 2;
    		zoomFactorC--;
    	}
    	
    	
		if (newModel)    	
		{	setNewModel(0, false);
		}	
    	
    }
    
    public void transPlusX(boolean newModel, int objectType)
    {	
    	if (objectType == FUNCTION)
    	{	
    		xMinG += xStepG;
    		xMaxG += xStepG;
    		translateXFactorG++;
    	}	
    	else if (objectType == SURFACE)
    	{	
    		xMinS += xStepS;
    		xMaxS += xStepS;
    		translateXFactorS++;
    	}	
    	else if (objectType == CURVE)
    	{	
    		xMinC += xStepC;
    		xMaxC += xStepC;
    		translateXFactorC++;
    	}	
    	
    	if (newModel)    	
    	{	setNewModel(0, false);
    	}	
    	
    	
    }
    public void transMinX(boolean newModel, int objectType)
    {	
    	if (objectType == FUNCTION)
    	{	
    		xMinG -= xStepG;
    		xMaxG -= xStepG;
    		translateXFactorG--;
    	}
    	else if (objectType == SURFACE)
    	{	
    		xMinS -= xStepS;
    		xMaxS -= xStepS;
    		translateXFactorS--;
    	}
    	else if (objectType == CURVE)
    	{	
    		xMinC -= xStepC;
    		xMaxC -= xStepC;
    		translateXFactorC--;
    	}
    	
    	if (newModel)		
    	{	setNewModel(0, false);
    	}	
    	
    }
    public void transPlusY(boolean newModel, int objectType)
    {	
    	if (objectType == FUNCTION)
    	{	
    		yMinG += yStepG;
    		yMaxG += yStepG;
    		translateYFactorG++;
    	}	
    	else if (objectType == SURFACE)
    	{	
    		yMinS += yStepS;
    		yMaxS += yStepS;
    		translateYFactorS++;
    	}	
    	else if (objectType == CURVE)
    	{	
    		yMinC += yStepC;
    		yMaxC += yStepC;
    		translateYFactorC++;
    	}	
    	
    	if (newModel)		
		{	setNewModel(0, false);
		}		
    	
    }
    public void transMinY(boolean newModel, int objectType)
    {	
    	if (objectType == FUNCTION)
    	{	
    		yMinG -= yStepG;
    		yMaxG -= yStepG;
    		translateYFactorG--;
    	}
    	else if (objectType == SURFACE)
    	{	
    		yMinS -= yStepS;
    		yMaxS -= yStepS;
    		translateYFactorS--;
    	}
    	else if (objectType == CURVE)
    	{	
    		yMinC -= yStepC;
    		yMaxC -= yStepC;
    		translateYFactorC--;
    	}
    	
   		if (newModel)		
   		{	setNewModel(0, false);
		}
    	
    }
    public void transPlusZ(boolean newModel, int objectType)
    {	
    	if (objectType == FUNCTION)
    	{	
    		zMinG += zStepG;
    		zMaxG += zStepG;
    		translateZFactorG++;
    	}
    	else if (objectType == SURFACE)
    	{	
    		zMinS += zStepS;
    		zMaxS += zStepS;
    		translateZFactorS++;
    	}	
    	else if (objectType == CURVE)
    	{	
    		zMinC += zStepC;
    		zMaxC += zStepC;
    		translateZFactorC++;
    	}	
    	
    	
    	if (newModel)
    	{	setNewModel(0, false);
    	}
    	
    }
    public void transMinZ(boolean newModel, int objectType)
    {	
    	if (objectType == FUNCTION)
    	{	
    		zMinG -= zStepG;
    		zMaxG -= zStepG;
    		translateZFactorG--;
    	}	
    	else if (objectType == SURFACE)
    	{	
    		zMinS -= zStepS;
    		zMaxS -= zStepS;
    		translateZFactorS--;
    	}
    	else if (objectType == CURVE)
    	{	
    		zMinC -= zStepC;
    		zMaxC -= zStepC;
    		translateZFactorC--;
    	}
    	
    	if (newModel)
    	{	setNewModel(0, false);
    	}
    }
    
    public void zetDraadFiguur(boolean b, int objectType)
    {
    	if (objectType == FUNCTION)
    	{	
    		wireFrameG = b;
    		if (b)
    		{	if (grafiek3DObject != null)
    			{	grafiek3DObject.setFilled(false);
    				grafiek3DObject.setOutlineColor(wireFrameColor);
    			}
    		}
    		else
    		{	if (grafiek3DObject != null)
				{	grafiek3DObject.setFilled(true);
					grafiek3DObject.setOutlineColor(graphOutlineColor);
				}
    		}
    	}
    	else if (objectType == SURFACE)
    	{	
    		wireFrameS = b;
    		if (b)
    		{	if (surface3DObject != null)
    			{	surface3DObject.setFilled(false);
    				surface3DObject.setOutlineColor(wireFrameColor);
    			}
    		}
    		else
    		{	if (surface3DObject != null)
    			{	surface3DObject.setFilled(true);
    				surface3DObject.setOutlineColor(surfaceOutlineColor);
    			}
    		}
    	}
    		
    	panel3D.repaint();
    }
    
    public void zetFijner(boolean newModel, int objectType)
    {
    	if (objectType == FUNCTION)
    	{	
    		xFinerStepsG += 1;
    		yFinerStepsG += 1;
    		finerMinButton.setEnabled(true);
    		finerFactorG++;
    	
    		if (newModel)
    		{	setNewModel(0, false);
    		}
    	}	
    }
    
    public void zetGrover(boolean newModel, int objectType)
    {
    	if (objectType == FUNCTION)
    	{	
    		xFinerStepsG -= 1;
    		yFinerStepsG -= 1;
    		if (xFinerStepsG == 2)
    			finerMinButton.setEnabled(false);
    		finerFactorG--;
    	
    		if (newModel)
    		{	setNewModel(0, false);
    		}
    	}	
    }
    public void zetGeenAssen(boolean newModel, int objectType)
    {
    	if (objectType == FUNCTION)
    	{	
    		noAxesG = true;
    	}
    	else if (objectType == SURFACE)
    	{	
    		noAxesS = true;
    	}
    	else if (objectType == CURVE)
    	{	
    		noAxesC = true;
    	}
    	if (newModel)
    		setNewModel(0, false);
    }
    
    public void zetxyzAs(boolean newModel, int objectType)
    {
    	if (objectType == FUNCTION)
    	{	
    		noAxesG = false;
    		floorTypeG = NOFLOOR;
    	}
    	else if (objectType == SURFACE)
    	{	
    		noAxesS = false;
    		floorTypeS = NOFLOOR;
    	}
    	else if (objectType == CURVE)
    	{	
    		noAxesC = false;
    		floorTypeC = NOFLOOR;
    	}
    	if (newModel)
    		setNewModel(0, false);
    }
    
    public void zetxyVloer(boolean newModel, int objectType)
    {
    	if (objectType == FUNCTION)
    	{	
    		noAxesG = false;
    		floorTypeG = TRANSFLOOR;
    	}
    	else if (objectType == SURFACE)
    	{	
    		noAxesS = false;
    		floorTypeS = TRANSFLOOR;
    	}
    	else if (objectType == CURVE)
    	{	
    		noAxesC = false;
    		floorTypeC = TRANSFLOOR;
    	}
    	
    	
    	if (newModel)
    		setNewModel(0, false);
    }
    
    
    public void zetLabelKeuze(boolean newModel, int type, int objectType)
    {
    	if (objectType == FUNCTION)
    	{	
    		labelTypeG = type;
    	}
    	else if (objectType == SURFACE)
    	{	
    		labelTypeS = type;
    	}
    	else if (objectType == CURVE)
    	{	
    		labelTypeS = type;
    	}
    	if (newModel)
    		setNewModel(0, false);
    }

    
	public void zetZoomOptie(boolean b)
	{	zoomOptie = b;
		zoomUitButton.setVisible(zoomOptie);
		zoomInButton.setVisible(zoomOptie);
		
	}
	
	public void zetTranslateOptie(boolean b)
	{	translateOptie = b;
		transPlusButton.setVisible(translateOptie);
		transMinButton.setVisible(translateOptie);
		asNaamButton.setVisible(translateOptie);
	}
	
	public void zetSolidDraadKeuzeOptie(boolean b)
	{	solidDraadKeuzeOptie = b;
		solidDraadKeuzeButton.setVisible(solidDraadKeuzeOptie);
		
	}
	
	public void zetFinerKeuzeOptie(boolean b)
	{	finerKeuzeOptie = b;
		finerPlusButton.setVisible(finerKeuzeOptie);
		finerMinButton.setVisible(finerKeuzeOptie);
	}
	
	public void zetAsKeuzeOptie(boolean b)
	{	asKeuzeOptie = b;
		asKeuzeButton.setVisible(asKeuzeOptie);
		
	}
	
	public void zetLabelKeuzeOptie(boolean b)
	{	labelKeuzeOptie = b;
		labelKeuzeButton.setVisible(labelKeuzeOptie);
		
	}
    
    public void actionPerformed(ActionEvent e)
    {	if (e.getActionCommand().equals("focus"))
    		return;
    
    	if (e.getActionCommand().equals("knop1"))
    		return;
/*    
    	long actionTime = System.currentTimeMillis();
    	if ((actionTime - lastActionTime) < 100)
    	{	lastActionTime = actionTime;
//System.out.println("at return = " + actionTime);    	
    		return;
    	}
    	else
    		lastActionTime = actionTime;
    	
//System.out.println("at = " + actionTime);    	
*/    
    	if (e.getSource() == zoomStandaardButton)
    	{	zoomStandaard(true, objectType);
    	}
    	else if (e.getSource() == zoomInButton) 
    	{	zoomIn(true, objectType);	
    	}
    	else if (e.getSource() == zoomUitButton) 
    	{	zoomUit(true, objectType);
    	}
    	else if (e.getSource() == transPlusButton) 
    	{	if (asNaamButton.getCode().equals("xasnaam"))
			{	transPlusX(true, objectType);
			}
			else if (asNaamButton.getCode().equals("yasnaam"))
			{	transPlusY(true, objectType);
			}
			else if (asNaamButton.getCode().equals("zasnaam"))
			{	transPlusZ(true, objectType);
			}
    	}
    	else if (e.getSource() == transMinButton) 
    	{	if (asNaamButton.getCode().equals("xasnaam"))
			{	transMinX(true, objectType);
			}
			else if (asNaamButton.getCode().equals("yasnaam"))
			{	transMinY(true, objectType);
			}
			else if (asNaamButton.getCode().equals("zasnaam"))
			{	transMinZ(true, objectType);
			}
    	}
    	else if (e.getSource() == asNaamButton) 
    	{	if (asNaamButton.getCode().equals("xasnaam"))
    		{	asNaamButton.setCode("yasnaam");
    		}
    		else if (asNaamButton.getCode().equals("yasnaam"))
    		{	asNaamButton.setCode("zasnaam");
    		}
    		else if (asNaamButton.getCode().equals("zasnaam"))
    		{	asNaamButton.setCode("xasnaam");
    		}
    	}
    	else if (e.getSource() == solidDraadKeuzeButton)
    	{
    		if (solidDraadKeuzeButton.getCode().equals("solid"))
    		{	solidDraadKeuzeButton.setCode("draad");
    			zetDraadFiguur(false, objectType);    			
    		}
    		else if (solidDraadKeuzeButton.getCode().equals("draad"))
    		{	solidDraadKeuzeButton.setCode("solid");
				zetDraadFiguur(true, objectType);
    		}
    	}
    	else if (e.getSource() == finerPlusButton)
    	{
    		zetFijner(true, objectType);
    	}
    	else if (e.getSource() == finerMinButton)
    	{
    		zetGrover(true, objectType);
    	}
    	
    	else if (e.getSource() == asKeuzeButton) 
    	{
//System.out.println("pw = " + assenPopup.getSize().width);
//System.out.println("ph = " + assenPopup.getSize().height);
			int width = 93;
			if (assenPopup.getSize().width != 0)
				width = assenPopup.getSize().width;

    		assenPopup.show(this, asKeuzeButton.getLocation().x + knoppenPanel.getLocation().x - width, asKeuzeButton.getLocation().y);
//System.out.println("pw = " + assenPopup.getSize().width);
//System.out.println("ph = " + assenPopup.getSize().height);
    	}
    	else if (e.getSource() == labelKeuzeButton) 
    	{
//System.out.println("pw = " + labelsPopup.getSize().width);
//System.out.println("ph = " + labelsPopup.getSize().height);
			int width = 91;
			if (labelsPopup.getSize().width != 0)
				width = labelsPopup.getSize().width;

			labelsPopup.show(this, labelKeuzeButton.getLocation().x + knoppenPanel.getLocation().x - width, labelKeuzeButton.getLocation().y);
//System.out.println("pw = " + labelsPopup.getSize().width);
//System.out.println("ph = " + labelsPopup.getSize().height);
    		
    	}
    	else if ((e.getSource() instanceof JMenuItem) && 
    			((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("geenAssenTekst")))
    	{
    		zetGeenAssen(true, objectType);
    	}
    	else if ((e.getSource() instanceof JMenuItem) &&
    			((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("xyzAsTekst")))
    	{
    		zetxyzAs(true, objectType);
    	}
    	else if ((e.getSource() instanceof JMenuItem) &&
    			((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("xyVloerTekst")))
    	{
    		zetxyVloer(true, objectType);
    	}
    	else if ((e.getSource() instanceof JMenuItem) &&
    	        ((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("geenLabelsTekst")))
    	{
    		zetLabelKeuze(true, NOLABELS, objectType);
    	}
    	else if ((e.getSource() instanceof JMenuItem) &&
    	        ((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("eindLabelsTekst")))
    	{
    		zetLabelKeuze(true, ENDLABELS, objectType);
    	}
    	else if ((e.getSource() instanceof JMenuItem) &&
    			((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("alleLabelsTekst")))
    	{
    		zetLabelKeuze(true, ALLLABELS, objectType);
    	}
    	
    	
    	
    	
    	
    	



    
    
    }
    
    public ObjectGroup3D cutObjectGroup(ObjectGroup3D ob, Plane3D plane)
    {   
        Object3D start = ob.leftMostLeaf().deepCopy();
        start.setVisible(true);
        start.setFilled(ob.filled);
        ObjectGroup3D startGroup = new ObjectGroup3D(start, false);
        startGroup.filled = start.filled;
        startGroup.visible = start.visible;
        startGroup.numVertexLabels = start.numVertexLabels;
        startGroup.fixFacetArray();
        ObjectWithPlane owp = new ObjectWithPlane(startGroup, plane.support,
            Vector3D.plus(plane.support, plane.direction1),
            Vector3D.plus(plane.support, plane.direction2),
            0, false);
        owp.fixFacetArray();    

//if (start instanceof Grafiek3D)
//System.out.println("start is Grafiek3D");

        Object3D left = new EmptyObject3D();
        Object3D right = new EmptyObject3D();
        
        if (start instanceof Grafiek3D)
        {
        	left = new Grafiek3D();
        	((Grafiek3D) left).trimTop = ((Grafiek3D) start).trimTop;
        	((Grafiek3D) left).trimBottom = ((Grafiek3D) start).trimBottom;
        	((Grafiek3D) left).topMaxVertex  = Vector3D.copyVector3D(((Grafiek3D) start).topMaxVertex);
        	((Grafiek3D) left).bottomMinVertex  = Vector3D.copyVector3D(((Grafiek3D) start).bottomMinVertex);
        	((Grafiek3D) left).insideVertex  = Vector3D.copyVector3D(((Grafiek3D) start).insideVertex);
        	right = new Grafiek3D();
        	((Grafiek3D) right).trimTop = ((Grafiek3D) start).trimTop;
        	((Grafiek3D) right).trimBottom = ((Grafiek3D) start).trimBottom;
        	((Grafiek3D) right).topMaxVertex  = Vector3D.copyVector3D(((Grafiek3D) start).topMaxVertex);
        	((Grafiek3D) right).bottomMinVertex  = Vector3D.copyVector3D(((Grafiek3D) start).bottomMinVertex);
        	((Grafiek3D) right).insideVertex  = Vector3D.copyVector3D(((Grafiek3D) start).insideVertex);
        	
        }
        
        if (start instanceof Surface3D)
        {
        	left = new Surface3D();
        	((Surface3D) left).trimTop = ((Surface3D) start).trimTop;
        	((Surface3D) left).trimBottom = ((Surface3D) start).trimBottom;
        	((Surface3D) left).trimFront = ((Surface3D) start).trimFront;
        	((Surface3D) left).trimBack = ((Surface3D) start).trimBack;
        	((Surface3D) left).trimLeft = ((Surface3D) start).trimLeft;
        	((Surface3D) left).trimRight = ((Surface3D) start).trimRight;
        	
        	((Surface3D) left).topMaxVertex  = Vector3D.copyVector3D(((Surface3D) start).topMaxVertex);
        	((Surface3D) left).bottomMinVertex  = Vector3D.copyVector3D(((Surface3D) start).bottomMinVertex);
        	((Surface3D) left).frontMinVertex  = Vector3D.copyVector3D(((Surface3D) start).frontMinVertex);
        	((Surface3D) left).backMaxVertex  = Vector3D.copyVector3D(((Surface3D) start).backMaxVertex);
        	((Surface3D) left).leftMinVertex  = Vector3D.copyVector3D(((Surface3D) start).leftMinVertex);
        	((Surface3D) left).rightMaxVertex  = Vector3D.copyVector3D(((Surface3D) start).rightMaxVertex);
        	
        	((Surface3D) left).insideVertex  = Vector3D.copyVector3D(((Surface3D) start).insideVertex);
        	
        	right = new Surface3D();
        	((Surface3D) right).trimTop = ((Surface3D) start).trimTop;
        	((Surface3D) right).trimBottom = ((Surface3D) start).trimBottom;
        	((Surface3D) right).trimFront = ((Surface3D) start).trimFront;
        	((Surface3D) right).trimBack = ((Surface3D) start).trimBack;
        	((Surface3D) right).trimLeft = ((Surface3D) start).trimLeft;
        	((Surface3D) right).trimRight = ((Surface3D) start).trimRight;
        	
        	((Surface3D) right).topMaxVertex  = Vector3D.copyVector3D(((Surface3D) start).topMaxVertex);
        	((Surface3D) right).bottomMinVertex  = Vector3D.copyVector3D(((Surface3D) start).bottomMinVertex);
        	((Surface3D) right).frontMinVertex  = Vector3D.copyVector3D(((Surface3D) start).frontMinVertex);
        	((Surface3D) right).backMaxVertex  = Vector3D.copyVector3D(((Surface3D) start).backMaxVertex);
        	((Surface3D) right).leftMinVertex  = Vector3D.copyVector3D(((Surface3D) start).leftMinVertex);
        	((Surface3D) right).rightMaxVertex  = Vector3D.copyVector3D(((Surface3D) start).rightMaxVertex);
        	
        	((Surface3D) right).insideVertex  = Vector3D.copyVector3D(((Surface3D) start).insideVertex);
        	
        }

        if (start instanceof Curve3D)
        {
        	left = new Curve3D();
        	((Curve3D) left).trimTop = ((Curve3D) start).trimTop;
        	((Curve3D) left).trimBottom = ((Curve3D) start).trimBottom;
        	((Curve3D) left).trimFront = ((Curve3D) start).trimFront;
        	((Curve3D) left).trimBack = ((Curve3D) start).trimBack;
        	((Curve3D) left).trimLeft = ((Curve3D) start).trimLeft;
        	((Curve3D) left).trimRight = ((Curve3D) start).trimRight;
        	
        	((Curve3D) left).topMaxVertex  = Vector3D.copyVector3D(((Curve3D) start).topMaxVertex);
        	((Curve3D) left).bottomMinVertex  = Vector3D.copyVector3D(((Curve3D) start).bottomMinVertex);
        	((Curve3D) left).frontMinVertex  = Vector3D.copyVector3D(((Curve3D) start).frontMinVertex);
        	((Curve3D) left).backMaxVertex  = Vector3D.copyVector3D(((Curve3D) start).backMaxVertex);
        	((Curve3D) left).leftMinVertex  = Vector3D.copyVector3D(((Curve3D) start).leftMinVertex);
        	((Curve3D) left).rightMaxVertex  = Vector3D.copyVector3D(((Curve3D) start).rightMaxVertex);
        	
        	((Curve3D) left).insideVertex  = Vector3D.copyVector3D(((Curve3D) start).insideVertex);
        	
        	right = new Curve3D();
        	((Curve3D) right).trimTop = ((Curve3D) start).trimTop;
        	((Curve3D) right).trimBottom = ((Curve3D) start).trimBottom;
        	((Curve3D) right).trimFront = ((Curve3D) start).trimFront;
        	((Curve3D) right).trimBack = ((Curve3D) start).trimBack;
        	((Curve3D) right).trimLeft = ((Curve3D) start).trimLeft;
        	((Curve3D) right).trimRight = ((Curve3D) start).trimRight;
        	
        	((Curve3D) right).topMaxVertex  = Vector3D.copyVector3D(((Curve3D) start).topMaxVertex);
        	((Curve3D) right).bottomMinVertex  = Vector3D.copyVector3D(((Curve3D) start).bottomMinVertex);
        	((Curve3D) right).frontMinVertex  = Vector3D.copyVector3D(((Curve3D) start).frontMinVertex);
        	((Curve3D) right).backMaxVertex  = Vector3D.copyVector3D(((Curve3D) start).backMaxVertex);
        	((Curve3D) right).leftMinVertex  = Vector3D.copyVector3D(((Curve3D) start).leftMinVertex);
        	((Curve3D) right).rightMaxVertex  = Vector3D.copyVector3D(((Curve3D) start).rightMaxVertex);
        	
        	((Curve3D) right).insideVertex  = Vector3D.copyVector3D(((Curve3D) start).insideVertex);
        	
        }
        
        ObjectGroup3D leftGroup, rightGroup;
  
/*
oud omitted         
/
        Vector leftVerticesLabeled = new Vector();
        Vector leftVertexLabels = new Vector();
        Vector rightVerticesLabeled = new Vector();
        Vector rightVertexLabels = new Vector();
        Facet3D leftCutFacet = null;
        Facet3D rightCutFacet = null;
*/        
        for (int i = 0; i < owp.numFacets; i++)
        {   
            if (!owp.hasReplacement(owp.facets[i]))
            {   // planepos gebruiken
                // om te kijken waar facet heen moet
                int leftPos = 0;
                int onPos = 0;
                int rightPos = 0;
                for (int j = 0; j < owp.facets[i].numPoints; j++)
                {   int pPos = plane.planePosition(owp.facets[i].points[j]);
                    if (pPos == -1)
                        leftPos++;
                    else if (pPos == 1)
                        rightPos++;    
                    else // pPos == 0
                        onPos++;
// cut apart bekijken, kom je vanzelf tegen
// de cut hoort rechts(!)
// zijn omgekeerde links
            
                } // points of facet[i]
                // left of cut
                if ((leftPos > 0) && (onPos >= 0))
                {   // add facet to left
                    int firstIndex = left.numVertices;
                    for (int j = 0; j < owp.facets[i].numPoints; j++)
                        left.addVertex(new Vector3D(owp.facets[i].points[j]), null);
                    int[] inds = new int[owp.facets[i].numPoints];
                    for (int k = 0; k < owp.facets[i].numPoints; k++)
                        inds[k] = k + firstIndex;
                    Facet3D leftFacet = new Facet3D(left.vertices, inds, owp.facets[i].color);
                    left.addFacet(leftFacet);
                    if (owp.facets[i].numPoints == leftFacet.numPoints)
                    	Facet3D.copyAttributes(owp.facets[i], leftFacet, true);
                    else
                    	Facet3D.copyAttributes(owp.facets[i], leftFacet, false);
                    
                    int inPlaneEdgeIndex = -1;
                    for (int vCnt = 0; vCnt < leftFacet.numPoints; vCnt++)
                    {	int pPos1 = plane.planePosition(leftFacet.points[vCnt]);
                    	int pPos2 = plane.planePosition(leftFacet.points[(vCnt + 1) % leftFacet.numPoints]);
                    	boolean inPlane = (pPos1 == 0) && (pPos2 == 0);
                    	if (inPlane)
                    		inPlaneEdgeIndex = vCnt;
                    }
                    if (inPlaneEdgeIndex >= 0)
                    {	leftFacet.edgeCodes[inPlaneEdgeIndex] = 52;
//System.out.println("left 52");                    
                    }
/* 
oud omitted
                    for (int m = 0; m < leftFacet.numPoints; m++)
                    {   if (owp.facets[i].vertexLabels[m] != null)
                            leftFacet.vertexLabels[m] = new String(owp.facets[i].vertexLabels[m]);
                        if (!leftVerticesLabeled.contains(leftFacet.points[m]))
                        {   leftVerticesLabeled.addElement(leftFacet.points[m]);
                            leftVertexLabels.addElement(leftFacet.vertexLabels[m]);
                        }   
                    }    
*/                    
                    // update cut colors?
                    
                }    
                // right of cut
                else if ((rightPos > 0) && (onPos >= 0))
                {   // add facet to right
                    int firstIndex = right.numVertices;
                    for (int j = 0; j < owp.facets[i].numPoints; j++)
                        right.addVertex(new Vector3D(owp.facets[i].points[j]), null);
                    int[] inds = new int[owp.facets[i].numPoints];
                    for (int k = 0; k < owp.facets[i].numPoints; k++)
                        inds[k] = k + firstIndex;
                    Facet3D rightFacet = new Facet3D(right.vertices, inds, owp.facets[i].color);
                    right.addFacet(rightFacet);
                    if (owp.facets[i].numPoints == rightFacet.numPoints)
                    	Facet3D.copyAttributes(owp.facets[i], rightFacet, true);
                    else
                    	Facet3D.copyAttributes(owp.facets[i], rightFacet, false);
                    
                    int inPlaneEdgeIndex = -1;
                    for (int vCnt = 0; vCnt < rightFacet.numPoints; vCnt++)
                    {	int pPos1 = plane.planePosition(rightFacet.points[vCnt]);
                    	int pPos2 = plane.planePosition(rightFacet.points[(vCnt + 1) % rightFacet.numPoints]);
                    	boolean inPlane = (pPos1 == 0) && (pPos2 == 0);
                    	if (inPlane)
                    		inPlaneEdgeIndex = vCnt;
                    }
                    if (inPlaneEdgeIndex >= 0)
                    	rightFacet.edgeCodes[inPlaneEdgeIndex] = 52;
/*
oud omitted                      
                    for (int m = 0; m < rightFacet.numPoints; m++)
                    {   if (owp.facets[i].vertexLabels[m] != null)
                            rightFacet.vertexLabels[m] = new String(owp.facets[i].vertexLabels[m]);
                        if (!rightVerticesLabeled.contains(rightFacet.points[m]))
                        {   rightVerticesLabeled.addElement(rightFacet.points[m]);
                            rightVertexLabels.addElement(rightFacet.vertexLabels[m]);
                        }   
                    }    
*/                    
                    // update cut colors?
                    
                }    
                
// dit gebeurt niet wanneer je geen cut maakt
                
                else if ((leftPos == 0) && (rightPos == 0))
                {   
                	
System.out.println("(leftPos == 0) && (rightPos == 0)");                	
                	// facet is the cut, add to right
                    int firstIndex = right.numVertices;
                    for (int j = 0; j < owp.facets[i].numPoints; j++)
                        right.addVertex(new Vector3D(owp.facets[i].points[j]), null);
                    int[] inds = new int[owp.facets[i].numPoints];
                    for (int k = 0; k < owp.facets[i].numPoints; k++)
                        inds[k] = k + firstIndex;
                    Facet3D rightCutFacet = new Facet3D(right.vertices, inds, graphColor);
                    right.addFacet(rightCutFacet);
                    Facet3D.copyAttributes(owp.facets[i], rightCutFacet, false);
                    // update cut colors and not outlined
                    rightCutFacet.color = graphColor;                    
                    // note: there is only one cut!
                    for (int m = 0; m < rightCutFacet.numPoints; m++)
                        rightCutFacet.edgeCodes[m] = 0;
                    
                    // add reverse facet to left
                    firstIndex = left.numVertices;
                    for (int j = owp.facets[i].numPoints - 1; j >= 0; j--)
                        left.addVertex(new Vector3D(owp.facets[i].points[j]), null);
                    inds = new int[owp.facets[i].numPoints];
                    for (int k = 0; k < owp.facets[i].numPoints; k++)
                        inds[k] = k + firstIndex;
                    Facet3D leftCutFacet = new Facet3D(left.vertices, inds, graphColor);
                    left.addFacet(leftCutFacet);
                    Facet3D.copyAttributes(owp.facets[i], leftCutFacet, false);
                    leftCutFacet.color = graphColor;
                    for (int m = 0; m < leftCutFacet.numPoints; m++)
                        leftCutFacet.edgeCodes[m] = 0;
                    
                    
                    
                } // allocation of facet[i]   
            
            } // !hasReplacement facet[i]
            
        } // owp facet loop    

//System.out.println("left " + leftVerticesLabeled.size());            
//for (int lft = 0; lft < leftVertexLabels.size(); lft++)
//System.out.println((String) leftVertexLabels.elementAt(lft));
//System.out.println("right " + rightVerticesLabeled.size());                        
        // find true center and diameter    
/*        
oud omitted
        for (int lft = 0; lft < leftCutFacet.numPoints; lft++)
        {   int lIndex = leftVerticesLabeled.indexOf(leftCutFacet.points[lft]);
            if (lIndex >= 0)
                leftCutFacet.vertexLabels[lft] = 
                    new String((String) leftVertexLabels.elementAt(lIndex));
        
        }
*/
/*
oud omitted
        for (int rgt = 0; rgt < rightCutFacet.numPoints; rgt++)
        {   int rIndex = rightVerticesLabeled.indexOf(rightCutFacet.points[rgt]);
            if (rIndex >= 0)
                rightCutFacet.vertexLabels[rgt] = 
                    new String((String) rightVertexLabels.elementAt(rIndex));
        
        }
*/
/*
oud omitted 
        int leftIndex = 0;
        for (int lCnt = 0; lCnt < leftVertexLabels.size(); lCnt++)
        {   leftIndex = Math.max(leftIndex,
                getLabelIndex((String) leftVertexLabels.elementAt(lCnt)));
        }    
*/
/*
oud omitted 
        int rightIndex = 0;
        for (int rCnt = 0; rCnt < rightVertexLabels.size(); rCnt++)
        {   rightIndex = Math.max(rightIndex,
                getLabelIndex((String) rightVertexLabels.elementAt(rCnt)));
        }    
*/
/*
oud omitted 
        left.numVertexLabels = leftIndex; // not relevant?
        right.numVertexLabels = rightIndex; // not relevant?
*/        
        left.initObject3D(true, false);
        right.initObject3D(true, false);
        

// note: up to here the labelling of the two basic halves is consistent
// with that of the original basic object

// now find all OTHER labels present in the original object

        Vector otherVerticesLabeled = new Vector();
        Vector otherVertexLabels = new Vector();
        // assume ob's facetArray is fixed
        for (int obFCnt = 0; obFCnt < ob.numFacets; obFCnt++)
        {   for (int obVCnt = 0; obVCnt < ob.facets[obFCnt].numPoints; obVCnt++)
            {   Vector3D oVertex = ob.facets[obFCnt].points[obVCnt];
                String oLabel = ob.facets[obFCnt].vertexLabels[obVCnt];
                if ((oLabel != null) && 
                    !oLabel.equals("") && !oLabel.equals("XX")
                   ) 
                {    if (!otherVerticesLabeled.contains(oVertex))
                     {    otherVerticesLabeled.addElement(oVertex);
                          otherVertexLabels.addElement(oLabel); 
                         
                     }  
                }   
            }
        }
/*        
new omitted 
        // find maximum labelindex of ob
        int otherIndex = 0;
        for (int oCnt = 0; oCnt < otherVertexLabels.size(); oCnt++)
        {   otherIndex = Math.max(otherIndex,
                getLabelIndex((String) otherVertexLabels.elementAt(oCnt)));
        }    
*/        
/*        
new omitted        
        if ((start.modelCode == CYLINDER) ||
            (start.modelCode == CONE1) ||
            (start.modelCode == CONE2) ||
            (start.modelCode == CONE3) ||
            (start.modelCode == CONE4)
            )
        {   if (isCylinderType(left))    
                left.modelCode = start.modelCode;
            if (isCylinderType(right))        
                right.modelCode = start.modelCode;
        }
*/        
//        letterObject(left);
//        letterObject(right);
//System.out.println("left-vert = " + left.numVertices);
//System.out.println("right-vert = " + right.numVertices);

                
        Vector3D trVector = new Vector3D(plane.normal);
        Vector3D.scaleBy(trVector, ob.diameter / 3);
               
        Vector3D minTrVector = Vector3D.minus(new Vector3D(0,0,0), trVector);

        double trPos = Vector3D.dotProduct(plane.normal, trVector) -
                       Vector3D.dotProduct(plane.normal, plane.point);
        double minTrPos = Vector3D.dotProduct(plane.normal, minTrVector) -
                                  Vector3D.dotProduct(plane.normal, plane.point);

        if (trPos < minTrPos)
        {   //left.translateBy(trVector.x, trVector.y, trVector.z);
            //right.translateBy(minTrVector.x, minTrVector.y, minTrVector.z);
        }
        else
        {   //left.translateBy(minTrVector.x, minTrVector.y, minTrVector.z);
            //right.translateBy(trVector.x, trVector.y, trVector.z);
                
        }

        Vector origConstruction = new Vector();   
        if (ob instanceof ObjectWithPlane)
            origConstruction = ((ObjectWithPlane) ob).getConstruction();
        else if (ob instanceof ObjectWithLine)
            origConstruction = ((ObjectWithLine) ob).getConstruction();
        origConstruction.removeElement(plane);            
        Vector trConstruction = new Vector();
        Vector minTrConstruction = new Vector();
        for (int i = 0; i < origConstruction.size(); i++)
        {   Object conObject = origConstruction.elementAt(i);
            if (conObject instanceof Line3D)
            {   Line3D trLine = ((Line3D) conObject).translateBy(trVector);
                trConstruction.addElement(trLine);
                Line3D minTrLine = ((Line3D) conObject).translateBy(minTrVector);
                minTrConstruction.addElement(minTrLine);            
            
            }
            else if (conObject instanceof Plane3D)
            {   Plane3D trPlane = ((Plane3D) conObject).translateBy(trVector);
                trConstruction.addElement(trPlane);
                Plane3D minTrPlane = ((Plane3D) conObject).translateBy(minTrVector);
                minTrConstruction.addElement(minTrPlane);            
                    
            }    
        }        
//System.out.println("" + trConstruction.size());            
//System.out.println("" + minTrConstruction.size());            
        
        if (trPos < minTrPos)
        {   leftGroup = ObjectWithPlane.rebuild(left, trConstruction);
            leftGroup.fixFacetArray();
//            int labelCnt = otherIndex;
            for (int lFCnt = 0; lFCnt < leftGroup.numFacets; lFCnt ++)
            {   for (int lVCnt = 0; lVCnt < leftGroup.facets[lFCnt].numPoints; lVCnt ++)
                {   // study this vertex
                    Vector3D lVertex = leftGroup.facets[lFCnt].points[lVCnt];
                    String lLabel = leftGroup.facets[lFCnt].vertexLabels[lVCnt];
                    Vector3D trLVertex = new Vector3D(lVertex);
                    // translate back
                    Vector3D.translateBy(trLVertex, -trVector.x, -trVector.y, -trVector.z);
/*                    
new omitted
                    // if lVertex has a Label
                    if ((lLabel != null) && !lLabel.equals("") && !lLabel.equals("XX"))
                    {   if (otherVerticesLabeled.contains(trLVertex))
                        {   // relabel as in ob-group
                            int index = otherVerticesLabeled.indexOf(trLVertex);
                            String newLabel = (String) otherVertexLabels.elementAt(index);
                            leftGroup.facets[lFCnt].vertexLabels[lVCnt] = new String(newLabel);
                        } 
                        else // a new label, which should reappear in right
                        {   labelCnt++;
                            lLabel = getLabel(labelCnt);
                            leftGroup.facets[lFCnt].vertexLabels[lVCnt] = new String(lLabel);
                            otherVerticesLabeled.addElement(trLVertex);
                            otherVertexLabels.addElement(new String (lLabel));
                        }    
                        
                    }
*/                    
                }    
            }
            
            rightGroup = ObjectWithPlane.rebuild(right, minTrConstruction);
            rightGroup.fixFacetArray();            
            
            for (int rFCnt = 0; rFCnt < rightGroup.numFacets; rFCnt ++)
            {   for (int rVCnt = 0; rVCnt < rightGroup.facets[rFCnt].numPoints; rVCnt ++)
                {   // study this vertex
                    Vector3D rVertex = rightGroup.facets[rFCnt].points[rVCnt];
                    String rLabel = rightGroup.facets[rFCnt].vertexLabels[rVCnt];
                    Vector3D trRVertex = new Vector3D(rVertex);
                    // translate back
                    Vector3D.translateBy(trRVertex, -minTrVector.x, -minTrVector.y, -minTrVector.z);
/*         
new omitted            
                    // if rVertex has a Label
                    if ((rLabel != null) && !rLabel.equals("") && !rLabel.equals("XX"))
                    {   if (otherVerticesLabeled.contains(trRVertex))
                        {   // relabel as in ob-group
                            int index = otherVerticesLabeled.indexOf(trRVertex);
                            String newLabel = (String) otherVertexLabels.elementAt(index);
                            rightGroup.facets[rFCnt].vertexLabels[rVCnt] = new String(newLabel);
                        } 
                        else // a new label, which should reappear in right
                        // cannot happen?
                        {   labelCnt++;
                            rLabel = getLabel(labelCnt);
                            rightGroup.facets[rFCnt].vertexLabels[rVCnt] = new String(rLabel);
                            otherVerticesLabeled.addElement(trRVertex);
                            otherVertexLabels.addElement(new String (rLabel));
                        }    
                        
                    }
*/                    
                }    
            }
            
        }
        else
        {   

            leftGroup = ObjectWithPlane.rebuild(left, minTrConstruction);
            leftGroup.fixFacetArray();            
//            int labelCnt = otherIndex;            
            for (int lFCnt = 0; lFCnt < leftGroup.numFacets; lFCnt ++)
            {   for (int lVCnt = 0; lVCnt < leftGroup.facets[lFCnt].numPoints; lVCnt ++)
                {   // study this vertex
                    Vector3D lVertex = leftGroup.facets[lFCnt].points[lVCnt];
                    String lLabel = leftGroup.facets[lFCnt].vertexLabels[lVCnt];
                    Vector3D trLVertex = new Vector3D(lVertex);
                    Vector3D.translateBy(trLVertex, -minTrVector.x, -minTrVector.y, -minTrVector.z);
/*                    
new omitted
                    // if lVertex has a Label
                    if ((lLabel != null) && !lLabel.equals("") && !lLabel.equals("XX"))
                    {   if (otherVerticesLabeled.contains(trLVertex))
                        {   // relabel                        
                            int index = otherVerticesLabeled.indexOf(trLVertex);
                            String newLabel = (String) otherVertexLabels.elementAt(index);
                            leftGroup.facets[lFCnt].vertexLabels[lVCnt] = new String(newLabel);
                        } 
                        else // a new label, which should reappear in right
                        {   labelCnt++;
                            lLabel = getLabel(labelCnt);
                            leftGroup.facets[lFCnt].vertexLabels[lVCnt] = new String(lLabel);
                            otherVerticesLabeled.addElement(trLVertex);
                            otherVertexLabels.addElement(new String (lLabel));
                        }    
                        
                    }
*/                    
                }    
            }
            
            rightGroup = ObjectWithPlane.rebuild(right, trConstruction);
            rightGroup.fixFacetArray();            

            for (int rFCnt = 0; rFCnt < rightGroup.numFacets; rFCnt ++)
            {   for (int rVCnt = 0; rVCnt < rightGroup.facets[rFCnt].numPoints; rVCnt ++)
                {   // study this vertex
                    Vector3D rVertex = rightGroup.facets[rFCnt].points[rVCnt];
                    String rLabel = rightGroup.facets[rFCnt].vertexLabels[rVCnt];
                    Vector3D trRVertex = new Vector3D(rVertex);
                    // translate back
                    Vector3D.translateBy(trRVertex, -trVector.x, -trVector.y, -trVector.z);
/*                    
new omitted
                    // if rVertex has a Label
                    if ((rLabel != null) && !rLabel.equals("") && !rLabel.equals("XX"))
                    {   if (otherVerticesLabeled.contains(trRVertex))
                        {   // relabel as in ob-group
                            int index = otherVerticesLabeled.indexOf(trRVertex);
                            String newLabel = (String) otherVertexLabels.elementAt(index);
                            rightGroup.facets[rFCnt].vertexLabels[rVCnt] = new String(newLabel);
                        } 
                        else // a new label, which should reappear in right
                        // cannot happen?
                        {   labelCnt++;
                            rLabel = getLabel(labelCnt);
                            rightGroup.facets[rFCnt].vertexLabels[rVCnt] = new String(rLabel);
                            otherVerticesLabeled.addElement(trRVertex);
                            otherVertexLabels.addElement(new String (rLabel));
                        }    
                        
                    }
*/            
                }    
            }
                
        }


        // leftGroup, rightGroup have correct diameter and translated center
        
        // rebuild de twee stukken
        ObjectGroup3D result = new ObjectGroup3D();
        result.addObject3D(left);
        result.addObject3D(right);
        // force center and diameter
        result.initObject3D(true, new Vector3D(ob.center), ob.diameter, false);
        return result;
        
    } //    
    
    // inner class to process mousePressed events
    class MLMML extends MouseAdapter implements MouseMotionListener
    {   // dragging with RIGHT button to rotate
        boolean dragging = false; 
//        Facet3D startF;
        // counter for draggEvents
        int draggCount = 0;
        
        // cursor start
        int xStart;
        int yStart;
        
        //boolean helpPoint = false; 
        
        boolean inCircle;
        
        boolean draggingOnExit = false;
        //boolean inCircleOnExit = false;
        boolean draggStartInCircle = false;
        
        public void mousePressed(MouseEvent e)
        {   // right button for rotating
            if ((e.getModifiers() & e.BUTTON1_MASK) == 0)
            {   
                panel3D.oldX = e.getX();
                panel3D.oldY = e.getY();
                //startF = panel3D.clickedFacet(e.getX(), e.getY());                            
                xStart = e.getX();
                yStart = e.getY();
                dragging = true;
                //panel3D.remove(dropMenu);
            }
            else // other button(s)
            {   xClicked = e.getX();
                yClicked = e.getY();
                //panel3D.remove(dropMenu);                
                if (mouseMode == INERT)
                {   panel3D.oldX = e.getX();
                    panel3D.oldY = e.getY();
//                    startF = panel3D.clickedFacet(e.getX(), e.getY());
                    xStart = e.getX();
                    yStart = e.getY();
                    dragging = true;
                    
                }    
            }
        } // mousePressed

        public void mouseReleased(MouseEvent e)
        {   if (dragging)
            {
                // make sure the dragg-event-queue for rotating is completed!!
                panel3D.repaint();
                dragging = false;
                draggStartInCircle = false;
                draggingOnExit = false;
            }
/*            
            else
            {   
                // speeding up?
                panel3D.retransform = false;
                panel3D.repaint();
                panel3D.retransform = true;                
            }    
*/            
            
        }

        public void mouseDragged(MouseEvent e)
        {   if (dragging)
            {
/*
                int xCenter = panel3D.getSize().width / 2;
                int yCenter = panel3D.getSize().height / 2;
                int minRad = Math.min(xCenter, yCenter);
            
                inCircle = 
                    Math.sqrt((xStart - xCenter) * (xStart - xCenter) +
                              (yStart - yCenter) * (yStart - yCenter)) <
                              minRad * RADFACTOR;
*/
                if ((e.getX() <= 0) || (e.getY() <= 0) ||
                    (e.getX() >= panel3D.getSize().width) ||
                    (e.getY() >= panel3D.getSize().height) 
                    )
                {    
                    dragging = false;
                    return;    
                }

//dit doet WireFrame met 360 i.p.v. 180 graden
                double xTheta = (panel3D.oldY - e.getY()) * 180.0d /
                                 panel3D.getSize().width;
                double yTheta = (panel3D.oldX - e.getX()) * 180.0d /
                                 panel3D.getSize().height;
//Peter's versie
//            double xTheta = (panel3D.oldY - e.getY()) * 5e-1d;
//            double yTheta = (panel3D.oldX - e.getX()) * 5e-1d;

                panel3D.rotateCake(xTheta, yTheta);                
                
                panel3D.repaint();
                
                panel3D.oldX = e.getX();
                panel3D.oldY = e.getY();

/*                
                if (inCircle)
                {
                    
// hier checken voor buiten beeld                    

//System.out.println("dragging");
//dit doet WireFrame met 360 i.p.v. 180 graden
                    double xTheta = (panel3D.oldY - e.getY()) * 180.0d /
                                     panel3D.getSize().width;
                    double yTheta = (panel3D.oldX - e.getX()) * 180.0d /
                                     panel3D.getSize().height;
// Peter's versie
//                double xTheta = (panel3D.oldY - e.getY()) * 5e-1d;
//                double yTheta = (panel3D.oldX - e.getX()) * 5e-1d;

               
// ECHT draaien
//                double xChange = panel3D.oldY - e.getY();
//                double yChange = panel3D.oldX - e.getX();
//                double theta = 5e-1d * Math.sqrt(
//                    xChange * xChange + yChange * yChange);
//                Vector3D axis = new Vector3D(xChange, yChange, 0);    
                // axis is no zo gekozen dat er theta gedraaid moet worden
                // met de klok mee gezien vanuit axis

                                
                    panel3D.rotateBy(xTheta, yTheta);
                    panel3D.updateHelpPoint(new Point(e.getX(), e.getY()));
                    panel3D.updateHelpLine(new Point(e.getX(), e.getY()));
//                panel3D.rotateBy(theta, axis);
//                panel3D.rotateCake(xTheta, yTheta);
                    //panel3D.paint(panel3D.getGraphics());
                    panel3D.repaint();
                
                    panel3D.oldX = e.getX();
                    panel3D.oldY = e.getY();
                }
                else
                {
                    
// hier afkappen voor buiten beeld                    
                    // choose correct direction
                    double centerX = ((double) panel3D.getSize().width) / 2;
                    double centerY = ((double) panel3D.getSize().height) / 2;                    
                    double xTheta = 0;
                    double yTheta = 0;

                    if (e.getX() < centerX)
                        yTheta = (panel3D.oldY - e.getY()) * 180.0d /
                                 panel3D.getSize().height;
                    else                    
                        yTheta = (e.getY() - panel3D.oldY) * 180.0d /
                                 panel3D.getSize().height;
                    if (e.getY() < centerY)             
                        xTheta = (e.getX() - panel3D.oldX) * 180.0d /
                                 panel3D.getSize().width;
                    else                 
                        xTheta = (panel3D.oldX - e.getX()) * 180.0d /
                                 panel3D.getSize().width;
                                     
// Peter's versie
//                double xTheta = (panel3D.oldY - e.getY()) * 5e-1d;
//                double yTheta = (panel3D.oldX - e.getX()) * 5e-1d;

                    double zTheta = 0;
                    
                    if (Math.abs(yTheta) > Math.abs(xTheta))
                        zTheta = yTheta;
                    else
                        zTheta = xTheta;
                    
                    panel3D.rotateByZ(zTheta);
//                    panel3D.updateHelpPoint(new Point(e.getX(), e.getY()));                    
//                    panel3D.updateHelpLine(new Point(e.getX(), e.getY()));                    
                    panel3D.repaint();
                
                    panel3D.oldX = e.getX();
                    panel3D.oldY = e.getY();
                    
                }
*/                    
            }
            else
            {   
/*                
*/                
                dragging = false;
            }    
            
            
        } // mouseDragged
        
        public void mouseEntered(MouseEvent e)
        {   
/*        	
            int centerX = panel3D.getSize().width / 2;
            int centerY = panel3D.getSize().height / 2;
            int minRad = Math.min(centerX, centerY);
            
            inCircle = 
                Math.sqrt((e.getX() - centerX) * (e.getX() - centerX) +
                          (e.getY() - centerY) * (e.getY() - centerY)) <
                          minRad * RADFACTOR;
            if (inCircle)
                panel3D.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));   
            else
                panel3D.setCursor(new Cursor(Cursor.HAND_CURSOR));               

//System.out.println("mouse entered " + inCircle);
             
 */
        }

        public void mouseMoved(MouseEvent e)
        {
/*        	
            int centerX = panel3D.getSize().width / 2;
            int centerY = panel3D.getSize().height / 2;
            int minRad = Math.min(centerX, centerY);
            
            inCircle = 
                Math.sqrt((e.getX() - centerX) * (e.getX() - centerX) +
                          (e.getY() - centerY) * (e.getY() - centerY)) <
                          minRad * RADFACTOR;
            if (inCircle)
                panel3D.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));   
            else
                panel3D.setCursor(new Cursor(Cursor.HAND_CURSOR));               

//System.out.println("mouse moved " + inCircle);
             
 */
        }
        
        public void mouseExited(MouseEvent e)
        {   
//System.out.println("mouse exited");            
        }
        
    } // class MLMML   
    
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
		// edit state
		boolean zoomOptie = true;
		boolean translateOptie = true;
		boolean solidDraadKeuzeOptie = true;
		boolean finerKeuzeOptie = true;
		boolean asKeuzeOptie = true;
		boolean labelKeuzeOptie = true;
		
		if (b.containsKey("zoomOptie"))
			zoomOptie = ((Boolean) b.get("zoomOptie")).booleanValue();
		if (b.containsKey("translateOptie"))
			zoomOptie = ((Boolean) b.get("translateOptie")).booleanValue();
		if (b.containsKey("solidDraadKeuzeOptie"))
			zoomOptie = ((Boolean) b.get("solidDraadKeuzeOptie")).booleanValue();
		if (b.containsKey("finerKeuzeOptie"))
			zoomOptie = ((Boolean) b.get("finerKeuzeOptie")).booleanValue();
		if (b.containsKey("asKeuzeOptie"))
			zoomOptie = ((Boolean) b.get("asKeuzeOptie")).booleanValue();
		if (b.containsKey("labelKeuzeOptie"))
			zoomOptie = ((Boolean) b.get("labelKeuzeOptie")).booleanValue();

		zetZoomOptie(zoomOptie);
		zetTranslateOptie(translateOptie);
		zetSolidDraadKeuzeOptie(solidDraadKeuzeOptie);
		zetFinerKeuzeOptie(finerKeuzeOptie);
		zetAsKeuzeOptie(asKeuzeOptie);
		zetLabelKeuzeOptie(labelKeuzeOptie);
		
		// state
		
		int objectType = FUNCTION;
		
		double angleXG = Object3DContainer.angleXStart;
		double angleZG = Object3DContainer.angleZStart;
		double angleXS = Object3DContainer.angleXStart;
		double angleZS = Object3DContainer.angleZStart;
		double angleXC = Object3DContainer.angleXStart;
		double angleZC = Object3DContainer.angleZStart;
		
		int zoomFactorG = 0;
		int translateXFactorG = 0;
		int translateYFactorG = 0;
		int translateZFactorG = 0;
		int finerFactorG = 0;
		
		int zoomFactorS = 0;
		int translateXFactorS = 0;
		int translateYFactorS = 0;
		int translateZFactorS = 0;

		int zoomFactorC = 0;
		int translateXFactorC = 0;
		int translateYFactorC = 0;
		int translateZFactorC = 0;

		if (b.containsKey("objectType"))
			objectType = ((Integer) b.get("objectType")).intValue();
		this.objectType = objectType;
		
		// FUNCTION
		if (b.containsKey("angleXG"))
			angleXG = ((Double) b.get("angleXG")).doubleValue();
		if (b.containsKey("angleZG"))
			angleZG = ((Double) b.get("angleZG")).doubleValue();
		
		if (b.containsKey("zoomFactorG"))
			zoomFactorG = ((Integer) b.get("zoomFactorG")).intValue();
		if (zoomFactorG > 0)
		{	for (int zUitCnt = 0; zUitCnt < zoomFactorG; zUitCnt++)
				zoomUit(false, FUNCTION);
		}
		if (zoomFactorG < 0)
		{	for (int zInCnt = zoomFactorG; zInCnt < 0; zInCnt++)
			zoomIn(false, FUNCTION);
		}
		
		if (b.containsKey("translateXFactorG"))
			translateXFactorG = ((Integer) b.get("translateXFactorG")).intValue();
		if (translateXFactorG > 0)
		{	for (int tPlusCnt = 0; tPlusCnt < translateXFactorG; tPlusCnt++)
				transPlusX(false, FUNCTION);
		}
		if (translateXFactorG < 0)
		{	for (int tMinCnt = translateXFactorG; tMinCnt < 0; tMinCnt++)
				transMinX(false, FUNCTION);
		}
		if (b.containsKey("translateYFactorG"))
			translateYFactorG = ((Integer) b.get("translateYFactorG")).intValue();
		if (translateYFactorG > 0)
		{	for (int tPlusCnt = 0; tPlusCnt < translateYFactorG; tPlusCnt++)
				transPlusY(false, FUNCTION);
		}
		if (translateYFactorG < 0)
		{	for (int tMinCnt = translateYFactorG; tMinCnt < 0; tMinCnt++)
				transMinY(false, FUNCTION);
		}
		if (b.containsKey("translateZFactorG"))
			translateZFactorG = ((Integer) b.get("translateZFactorG")).intValue();
		if (translateZFactorG > 0)
		{	for (int tPlusCnt = 0; tPlusCnt < translateZFactorG; tPlusCnt++)
				transPlusZ(false, FUNCTION);
		}
		if (translateZFactorG < 0)
		{	for (int tMinCnt = translateZFactorG; tMinCnt < 0; tMinCnt++)
				transMinZ(false, FUNCTION);
		}
		
		
		// hier, objectType nodig
		layoutKnoppenPanel();
		
		functieEditor.zetOpdracht(b, randomVars, randomValues);
		
		// hier !!
// grafiek oppervlak kromme maken
// model maken		
// zetBeginHoeken		

	}
	
	public void setState(Hashtable b)
	{
		
		
		functieEditor.setState(b);
	}
	
	public void setEditState(Hashtable b)
	{
		
		functieEditor.setEditState(b);
	}
	
	public Hashtable getState()
	{
		Hashtable h = functieEditor.getState();
		
		return h;
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = functieEditor.getEditState();
		
		return h;
	}
    
}
