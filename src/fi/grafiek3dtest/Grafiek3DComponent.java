package fi.grafiek3dtest;

import java.awt.*;
import java.awt.event.*;
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
    public static Color floorColor = Color.white;
    public static Color floorOutlineColor = Color.black;
    public static Color graphColor = Color.yellow;
    public static Color graphOutlineColor = Color.lightGray;

	public static Font assenFont = new Font("SansSerif",Font.PLAIN, 10);
	
    public static double MAXZOOM = 15e-1d;
    public static double MINZOOM = 2e-1d; 
    public static double ZOOMSTEP = 1e-1d;
    public static double defaultZoom = 8e-1d;
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
    
    // axes
    double xMinBegin = -2, xMaxBegin = 2, xStepBegin = 5e-1d, 
    	   yMinBegin = -2, yMaxBegin = 2, yStepBegin = 5e-1d, 
    	   zMinBegin = -2, zMaxBegin = 2, zStepBegin = 5e-1d;
    double xMin = -2, xMax = 2, xStep = 5e-1d, 
           yMin = -2, yMax = 2, yStep = 5e-1d, 
           zMin = -2, zMax = 2, zStep = 5e-1d;
    
	int xFinerSteps = 2;
	int yFinerSteps = 2;
	
    int zoomFactor = 0;
        
    public boolean noAxes = false;
    
    public static final int NOFLOOR = 0;
    public static final int TRANSFLOOR = 1;
//    public static final int SOLIDFLOOR = 2;
    int floorType = NOFLOOR;
    
    public static final int NOLABELS = 0;
    public static final int ENDLABELS = 1;
    public static final int ALLLABELS = 2;
    int labelType = ENDLABELS;
    
    long lastActionTime = 0;
    
    
    // the 3D panel(s)
    Object3DContainer panel3D = new Object3DContainer();
    int panel3DSize = 400;
    // the function editor
    FunctieEditor functieEditor;
    int functieEditorWidth = 350;
    int functieEditorHeight = 300;
    
    Object3D originalObject;
    ObjectGroup3D currentObjectGroup;
    
    public static final int FUNCTION = 0;
    public static final int SURFACE = 1;
    public static final int CURVE = 2;
    int objectType = FUNCTION;
    
    FormuleButton zoomStandaard, zoomIn, zoomUit, transPlus, asNaam, transMin, asKeuze, labelKeuze;
    
    JPopupMenu assenPopup, labelsPopup;
    
	String varNaamX = "x";
	String varNaamY = "y";
	String paramNaam = "t";
	String paramNaamU = "u";
	String paramNaamV = "v";

	Expressie grafiek3DExpressie = null;

	//boolean busy = false;
	
    public Grafiek3DComponent(int x, int y, int w, int h)
    {
    	setBounds(x, y, w, h);
    	
    	setBackground(Color.lightGray);
    	
    	setLayout(null);

    	scrollPanel = new JPanel();
    	scrollPanel.setLayout(null);
    	scrollPanel.setSize(scrollPanelWidth, 10 + panel3DSize + functieEditorHeight);
    	scrollPanel.setPreferredSize(new Dimension(scrollPanelWidth, 10 + panel3DSize + functieEditorHeight));
    	
    	panel3D.setBounds(10, 10, panel3DSize, panel3DSize);
    	scrollPanel.add(panel3D);
    	
    	functieEditor = new FunctieEditor(false);
    	functieEditor.setBounds(10,
    							panel3D.getLocation().y + panel3D.getSize().height, 
    							functieEditorWidth, functieEditorHeight);
    	functieEditor.zetGrafiek3DComponent(this);
    	functieEditor.zetFuncties(objectType);
    	scrollPanel.add(functieEditor);                        
    	
    	scrollPane = new JScrollPane(scrollPanel);
    	scrollPane.setBounds(0, 0, scrollPaneWidth, getSize().height);
    	scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, getSize().height));
    	add(scrollPane);
    	
    	zoomStandaard	= new ZoomKnop("standaard");
		zoomStandaard.setBounds(10 + panel3DSize + 10, 10, 23, 23);
		zoomStandaard.addActionListener(this);
		scrollPanel.add(zoomStandaard);
		
		zoomIn	= new ZoomKnop("zoomin");
		zoomIn.setBounds(10 + panel3DSize + 10, 39, 21, 21);
		zoomIn.addActionListener(this);
		scrollPanel.add(zoomIn);
		
		zoomUit	= new ZoomKnop("zoomuit");
		zoomUit.setBounds(10 + panel3DSize + 10, 65, 21, 21);
		zoomUit.addActionListener(this);
		scrollPanel.add(zoomUit);
		
		transPlus = new ZoomKnop("transplus");
		transPlus.setBounds(10 + panel3DSize + 10, 96, 21, 21);
		transPlus.addActionListener(this);
		scrollPanel.add(transPlus);
		
		asNaam = new ZoomKnop("xasnaam");
		asNaam.setBounds(10 + panel3DSize + 5, 122, 31, 21);
		asNaam.addActionListener(this);
		scrollPanel.add(asNaam);
		
		transMin = new ZoomKnop("transmin");
		transMin.setBounds(10 + panel3DSize + 10, 148, 21, 21);
		transMin.addActionListener(this);
		scrollPanel.add(transMin);
		
		asKeuze = new ZoomKnop("askeuze");
		asKeuze.setBounds(10 + panel3DSize + 5, 179, 31, 21);
		asKeuze.addActionListener(this);
		scrollPanel.add(asKeuze);
    	
		labelKeuze = new ZoomKnop("labelkeuze");
		labelKeuze.setBounds(10 + panel3DSize + 5, 210, 31, 21);
		labelKeuze.addActionListener(this);
		scrollPanel.add(labelKeuze);
    	    	
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

			
        currentObjectGroup = makeNewModel(modelCode);        
   	    // HIER!
        //setFilled(false);        
   	    panel3D.initializeModel(currentObjectGroup, reallyNew);

        // reset zooming HERE
   	    zoom = defaultZoom;
       	panel3D.setZoomFactor(zoom);        
        
//      addToHistory();

//        busy = false;
    }    
    
    public ObjectGroup3D makeNewModel(int code)
    {   Object3D axesModel;
    	Object3D graph3DModel;
        ObjectGroup3D modelGroup = null;

        // default?
// binnenvulling is onzichtbaar
// maar voor buitenkant toch NZMINFIRST
// is dit ook OK voor filled = false?
        //panel3D.paintType = Object3DContainer.PUREZ;
        
    	axesModel = makeNewAxes();
//System.out.println("axes diam = " + axesModel.getDiameter());    	
        
        if (!noAxes)
        {	
        	//axesModel = makeNewAxes();
        	modelGroup = new ObjectGroup3D(axesModel, false);
        	modelGroup.numVertexLabels = axesModel.numVertexLabels;
        }
        
        //axesModel = new Box(xMax - xMin, yMax - yMin, zMax - zMin, Color.yellow);
        
        if (grafiek3DExpressie != null)
        {
        	graph3DModel = makeGrafiek3D();
        	graph3DModel.modelCode = code;
        	originalObject = graph3DModel;
        	if (modelGroup == null)
        	{	graph3DModel.diameter = axesModel.getDiameter();
        		graph3DModel.diamSet = true;
        		
        		modelGroup = new ObjectGroup3D(graph3DModel, false);
        	
        	}
        	else
        		modelGroup.addObject3D(graph3DModel);
        }
        
        //System.out.println("model-numFacets = " + model.numFacets);        
        //modelGroup = new ObjectGroup3D(model, false);
        //modelGroup.numVertexLabels = axisModel.numVertexLabels;
        return modelGroup;
    }   
    
    public Object3D makeNewAxes()
    {
    	return new Axes(xMin, xMax, xStep, yMin, yMax, yStep, zMin, zMax, zStep, 
    					floorType, labelType, xFinerSteps, yFinerSteps);
    }
    
    public Object3D makeGrafiek3D()
    {	Grafiek3D grafiek3DObject = new Grafiek3D(grafiek3DExpressie, 
			 					 				  xMin, xMax, xStep, yMin, yMax, yStep, zMin, zMax, zStep, 
			 					 				  varNaamX, varNaamY, xFinerSteps, yFinerSteps);    	 
    
    	if (grafiek3DObject.trimTop)
    	{	
//System.out.println("trimTop");    		
    		Plane3D zMaxPlane = new Plane3D(0, 0, 1, zMax);
    		ObjectGroup3D grafiek3DObjectGroup = new ObjectGroup3D(grafiek3DObject, false);
    		ObjectGroup3D topTrimmedGroup = cutObjectGroup(grafiek3DObjectGroup, zMaxPlane);
//System.out.println("ttsize = " + topTrimmedGroup.objects.size());    

// hier nog kiezen !!
			grafiek3DObject = (Grafiek3D) topTrimmedGroup.objects.elementAt(0);
			if (grafiek3DObject.containsVertex(grafiek3DObject.topMaxVertex) >= 0)
				grafiek3DObject = (Grafiek3D) topTrimmedGroup.objects.elementAt(1);
				

//if (grafiek3DObject instanceof Grafiek3D)
//System.out.println("Grafiek3D");	
    		
    	}
    	if (grafiek3DObject.trimBottom)
    	{
    		Plane3D zMinPlane = new Plane3D(0, 0, 1, zMin);
    		ObjectGroup3D grafiek3DObjectGroup = new ObjectGroup3D(grafiek3DObject, false);
    		ObjectGroup3D topTrimmedGroup = cutObjectGroup(grafiek3DObjectGroup, zMinPlane);

// hier nog kiezen !!
			grafiek3DObject = (Grafiek3D) topTrimmedGroup.objects.elementAt(0);
			if (grafiek3DObject.containsVertex(grafiek3DObject.bottomMinVertex) >= 0)
				grafiek3DObject = (Grafiek3D) topTrimmedGroup.objects.elementAt(1);
    		
    	}
    
    	return grafiek3DObject; 
    }
  
    public void zetGrafiek3D(Expressie exp)
    {
//System.out.println("zetGrafiek3D");    	
    	grafiek3DExpressie = exp;
    	
    	if (noAxes)
    		setNewModel(0, true);
    	else
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
    
    public void zoomStandaard()
    {
    	
    }
    
    public void zoomIn()
    {	

    	double centerX = (xMin + xMax) / 2;
    	double centerY = (yMin + yMax) / 2;
    	double centerZ = (zMin + zMax) / 2;
    	xMax = centerX + (xMax - centerX) / 2;
    	xMin = centerX - (centerX - xMin) / 2;
    	yMax = centerY + (yMax - centerY) / 2;
    	yMin = centerY - (centerY - yMin) / 2;
    	zMax = centerZ + (zMax - centerZ) / 2;
    	zMin = centerZ - (centerZ - zMin) / 2;
    	xStep /= 2;
    	yStep /= 2;
    	zStep /= 2;

    	zoomFactor++;
    	
    	if (zoomFactor >= 0)
    	{	xFinerSteps = 2;
    		yFinerSteps = 2;
    	}
    	else
    	{	xFinerSteps -= 1;
			yFinerSteps -= 1;
    		
    	}

//System.out.println("zf = " + zoomFactor);    	
//System.out.println("xfs = " + xFinerSteps);    	
    	
    	setNewModel(0, false);
    }
    
    public void zoomUit()
    {
    	double centerX = (xMin + xMax) / 2;
    	double centerY = (yMin + yMax) / 2;
    	double centerZ = (zMin + zMax) / 2;
    	xMax = centerX + (xMax - centerX) * 2;
    	xMin = centerX - (centerX - xMin) * 2;
    	yMax = centerY + (yMax - centerY) * 2;
    	yMin = centerY - (centerY - yMin) * 2;
    	zMax = centerZ + (zMax - centerZ) * 2;
    	zMin = centerZ - (centerZ - zMin) * 2;
    	xStep *= 2;
    	yStep *= 2;
    	zStep *= 2;
    	
    	zoomFactor--;

    	if (zoomFactor < 0)
    	{	xFinerSteps += 1;
    		yFinerSteps += 1;
    	}

//System.out.println("zf = " + zoomFactor);    	
//System.out.println("xfs = " + xFinerSteps);    	
    	
    	setNewModel(0, false);    	
    	
    }
    
    public void transPlusX()
    {	xMin += xStep;
    	xMax += xStep;
    	setNewModel(0, false);
    	
    }
    public void transMinX()
    {	xMin -= xStep;
		xMax -= xStep;
		setNewModel(0, false);
    	
    }
    public void transPlusY()
    {	yMin += yStep;
		yMax += yStep;
		setNewModel(0, false);
    	
    }
    public void transMinY()
    {	yMin -= yStep;
		yMax -= yStep;
		setNewModel(0, false);
    	
    }
    public void transPlusZ()
    {	zMin += zStep;
		zMax += zStep;
		setNewModel(0, false);
    	
    }
    public void transMinZ()
    {	zMin -= zStep;
		zMax -= zStep;
		setNewModel(0, false);
    	
    }
    
    public void zetGeenAssen()
    {
    	noAxes = true;
    	setNewModel(0, false);
    }
    
    public void zetxyzAs()
    {
    	noAxes = false;
    	floorType = NOFLOOR;
    	setNewModel(0, false);
    }
    
    public void zetxyVloer()
    {
    	noAxes = false;
    	floorType = TRANSFLOOR;
    	setNewModel(0, false);
    }
    
    
    public void zetLabelKeuze(int type)
    {
    	labelType = type;
    	setNewModel(0, false);
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
    	if (e.getSource() == zoomStandaard)
    	{	zoomStandaard();
    	}
    	else if (e.getSource() == zoomIn) 
    	{	zoomIn();	
    	}
    	else if (e.getSource() == zoomUit) 
    	{	zoomUit();
    	}
    	else if (e.getSource() == transPlus) 
    	{	if (asNaam.getCode().equals("xasnaam"))
			{	transPlusX();
			}
			else if (asNaam.getCode().equals("yasnaam"))
			{	transPlusY();
			}
			else if (asNaam.getCode().equals("zasnaam"))
			{	transPlusZ();
			}
    	}
    	else if (e.getSource() == transMin) 
    	{	if (asNaam.getCode().equals("xasnaam"))
			{	transMinX();
			}
			else if (asNaam.getCode().equals("yasnaam"))
			{	transMinY();
			}
			else if (asNaam.getCode().equals("zasnaam"))
			{	transMinZ();
			}
    	}
    	else if (e.getSource() == asNaam) 
    	{	if (asNaam.getCode().equals("xasnaam"))
    		{	asNaam.setCode("yasnaam");
    		}
    		else if (asNaam.getCode().equals("yasnaam"))
    		{	asNaam.setCode("zasnaam");
    		}
    		else if (asNaam.getCode().equals("zasnaam"))
    		{	asNaam.setCode("xasnaam");
    		}
    	}
    	else if (e.getSource() == asKeuze) 
    	{
//System.out.println("pw = " + assenPopup.getSize().width);
//System.out.println("ph = " + assenPopup.getSize().height);
			int width = 93;
			if (assenPopup.getSize().width != 0)
				width = assenPopup.getSize().width;

    		assenPopup.show(this, asKeuze.getLocation().x - width, asKeuze.getLocation().y);
//System.out.println("pw = " + assenPopup.getSize().width);
//System.out.println("ph = " + assenPopup.getSize().height);
    	}
    	else if (e.getSource() == labelKeuze) 
    	{
//System.out.println("pw = " + labelsPopup.getSize().width);
//System.out.println("ph = " + labelsPopup.getSize().height);
			int width = 91;
			if (labelsPopup.getSize().width != 0)
				width = labelsPopup.getSize().width;

			labelsPopup.show(this, labelKeuze.getLocation().x - width, labelKeuze.getLocation().y);
//System.out.println("pw = " + labelsPopup.getSize().width);
//System.out.println("ph = " + labelsPopup.getSize().height);
    		
    	}
    	else if (((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("geenAssenTekst")))
    	{
    		zetGeenAssen();
    	}
    	else if (((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("xyzAsTekst")))
    	{
    		zetxyzAs();
    	}
    	else if (((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("xyVloerTekst")))
    	{
    		zetxyVloer();
    	}
    	else if (((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("geenLabelsTekst")))
    	{
    		zetLabelKeuze(NOLABELS);
    	}
    	else if (((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("eindLabelsTekst")))
    	{
    		zetLabelKeuze(ENDLABELS);
    	}
    	else if (((JMenuItem) e.getSource()).getText().equals(Grafiek3DTest.rb.getString("alleLabelsTekst")))
    	{
    		zetLabelKeuze(ALLLABELS);
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
        	right = new Grafiek3D();
        	((Grafiek3D) right).trimTop = ((Grafiek3D) start).trimTop;
        	((Grafiek3D) right).trimBottom = ((Grafiek3D) start).trimBottom;
        	((Grafiek3D) right).topMaxVertex  = Vector3D.copyVector3D(((Grafiek3D) start).topMaxVertex);
        	((Grafiek3D) right).bottomMinVertex  = Vector3D.copyVector3D(((Grafiek3D) start).bottomMinVertex);
        	
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
                    Facet3D.copyAttributes(owp.facets[i], leftFacet, false);
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
                    Facet3D.copyAttributes(owp.facets[i], rightFacet, false);
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
                else if ((leftPos == 0) && (rightPos == 0))
                {   // facet is the cut, add to right
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
}
