package fi.doorziendwo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fi.beans.scorm.*;
import fi.beans.base64code.*;

public class ViewPanel extends Panel implements ViewerIF
{	
	Button startButton, opnieuwButton;
	boolean frameStarted = false;
	
	Object3DContainer viewPanel3D;
	ObjectGroup3D viewGroup3D;
	
	DoorzienFrame doorzienFrame = null;
	public ScormedObject3D scormedObject3D = null;
	
	Font theFont;
	FontMetrics theFM;
	
	// managing the cursor
    int xMoved;
    int yMoved;
    // circle radius for rotate modes
    public static double RADFACTOR = 1d;

	public static int CUTOBJECT = 10;
	public static int FOLDOUT = 11;

    // using the slider
    double sliderValue = 0;
    ViewSlider slider;
    
    LWButton flatButton;
    boolean flattened;
    Facet3D startFacetCopy;
    
    MLMML mListener;
    
    DoorzienDWO doorzienDWO;
    
    String startFiguurString;
	
	public ViewPanel(int x, int y, int b, int h)
	{	
		setLayout(null);
		setBounds(x,y,b,h);
		
		theFont = getFont();
		if(theFont==null) theFont = new Font("SansSerif",Font.PLAIN,12);
		theFM = getFontMetrics(theFont);
		
		viewPanel3D = new Object3DContainer();
		viewPanel3D.setBounds(0, 0, b, h-25);
		add(viewPanel3D);
			
		mListener = new MLMML();
		viewPanel3D.addMouseListener(mListener);
		viewPanel3D.addMouseMotionListener(mListener);	
		
		startButton = new Button(Table.lookUp("startButtonViewerText"));
		startButton.setBounds(b-theFM.stringWidth(startButton.getLabel())-20, h-20, theFM.stringWidth(startButton.getLabel())+20,20);
		add(startButton);
		AL listener = new AL();
		startButton.addActionListener(listener);
		
		opnieuwButton = new Button(Table.lookUp("opnieuwButtonViewerText"));
		opnieuwButton.setBounds(0, h-20, 80,20);
		add(opnieuwButton);
		opnieuwButton.addActionListener(listener);
	}
	
	public void setApplet(DoorzienDWO d)
	{	doorzienDWO = d;
	}
	
	public void setMouse(boolean b)
    {	if(!b)
	    {  	viewPanel3D.removeMouseListener(mListener);
			viewPanel3D.removeMouseMotionListener(mListener);
			
		}
    }
    
    public void setBordered(boolean b)
    {	if(!b)
	    { 	viewPanel3D.setBordered(b);
	    }
	}
	
	public void setChangeable(boolean b)
    {	startButton.setVisible(b);
    	opnieuwButton.setVisible(b);
	}
	
	public void setState(String s)
	{	if(startFiguurString==null) startFiguurString = s;
		Object o = StringCodeObject.decodeStringToObject(s);
		ScormedObject3D s3d = (ScormedObject3D) o;
		scormedObject3D = s3d;
		if(scormedObject3D!=null)showScormedObject3D();
	}
	
	public String getState()
	{	scormedObject3D.mat = Matrix3D.copy(viewPanel3D.mat);
		String s = StringCodeObject.encodeObjectToString(scormedObject3D);
	    return s;
	}
	
	public void restart()
	{   frameStarted = false;
		startButton.setEnabled(true);
	}  
	
	// get the current ObjectGroup3D uit drawingPanel
	// en maak er een ScormedObject3D van
	// laat het zien in de viewer
	// merk op: het DoorzienFrame (en dus Panel3D) is hierna killed
	public void getScormedObject3D()
	{	// shortcut
		DrawingPanel dp = doorzienFrame.drawingPanel;

		// dit moet je altijd doen!!
		// creeer
		scormedObject3D = new ScormedObject3D();
		// drawingPanel main items
		scormedObject3D.mode = dp.INERT;
		if (dp.TICKNUM > 0)
			dp.setHelpPoints(0);
		scormedObject3D.theObjectGroup = dp.currentObjectGroup;
		scormedObject3D.numLines = dp.numLines;
		scormedObject3D.numPlanes = dp.numPlanes;
		scormedObject3D.filled = dp.filled;
		scormedObject3D.planesFilled = dp.planesFilled;
		scormedObject3D.lengthFactor = dp.llFactor;
		scormedObject3D.letters = dp.letters;

		// drawingPanel.panel3D items
		scormedObject3D.projection = dp.panel3D.projection;
		scormedObject3D.mat = dp.panel3D.mat;
		scormedObject3D.paintType = dp.panel3D.paintType;
		scormedObject3D.zoomFactor = dp.panel3D.zoomFactor;
		scormedObject3D.showInside = dp.panel3D.showInside;

		if ((dp.mouseMode == dp.FOLDOUT) && (dp.startFacet != null))
		{	scormedObject3D.mode = dp.FOLDOUT;
			// toestand originele object
			scormedObject3D.oldFilled = dp.oldFilled;
			scormedObject3D.oldPos = dp.oldPos;
			// toestand fold out
			scormedObject3D.flattened = dp.flattened;
			scormedObject3D.angle = dp.currentFoldOut;
			scormedObject3D.theFoldOutGroup = dp.foldOutObjectGroup;			
			scormedObject3D.theFoldOutTreeRoot = dp.foldOutTreeRoot;
			scormedObject3D.theStartFacet = dp.startFacet;
		}

		if ((dp.mouseMode == dp.CUTOBJECT) && (dp.planeChoosen != null))
		{	scormedObject3D.mode = dp.CUTOBJECT;
			// toestand originele object
			scormedObject3D.oldPlanesFilled = dp.oldPlanesFilled;
			// toestand cut object
			scormedObject3D.volumeString = dp.panel3D.testString;
			scormedObject3D.theCutObjectGroup = dp.cutObjectGroup;
			scormedObject3D.planeChoosen = dp.planeChoosen;
		}

		// zet viewPanel items
		viewPanel3D.testString = "";
		//setSlider(false, 0, 0, 0);
		if (scormedObject3D.mode == dp.FOLDOUT)
		{	viewGroup3D = (ObjectGroup3D) scormedObject3D.theFoldOutGroup.deepCopy();
//			setStartFacetCopy(viewGroup3D);
			setSlider(true, scormedObject3D.angle, 0, 1);
			// niet nodig?
//			flattened 
		}
		else if (scormedObject3D.mode == dp.CUTOBJECT)
		{	viewGroup3D = (ObjectGroup3D) scormedObject3D.theCutObjectGroup.deepCopy();
			viewPanel3D.testString = scormedObject3D.volumeString;
		}
		else	
		{	viewGroup3D = (ObjectGroup3D) scormedObject3D.theObjectGroup.deepCopy();			
		}
			
		viewPanel3D.initializeModel(viewGroup3D, false);			
		viewPanel3D.setProjection(scormedObject3D.projection);		
		viewPanel3D.mat = Matrix3D.copy(scormedObject3D.mat);
		viewPanel3D.mat.setOrigin(getSize().width / 2, getSize().height / 2-12, 0);
		viewPanel3D.paintType = scormedObject3D.paintType;					
		viewPanel3D.showInside = scormedObject3D.showInside;					
		viewPanel3D.setZoomFactor(scormedObject3D.zoomFactor);					
		viewPanel3D.repaint();
			
		//rotateString = textTable.lookUp("rotateText");

	}

	// zorg dat het ScormedObject3D uit de viewer
	// in Doorzien terecht komt
	// gebruik dit ook als je in het menu "mijn figuur" kiest
	public void setScormedObject3D()
	{	// shortcut
	
		DrawingPanel dp = doorzienFrame.drawingPanel;

		scormedObject3D.mat = Matrix3D.copy(viewPanel3D.mat);

		// zet de basis elementen
		// drawingPanel
		dp.mouseMode = scormedObject3D.mode;
		dp.currentObjectGroup = scormedObject3D.theObjectGroup;
		dp.originalObject = dp.currentObjectGroup.leftMostLeaf();
		// dit enabeld/disabled de lijn knoppen
		dp.setNumLines(scormedObject3D.numLines);
		// dit enabeld/disabled de knoppen
		dp.setNumPlanes(scormedObject3D.numPlanes);
		dp.llFactor = scormedObject3D.lengthFactor;
		// dit betekent dat er zeker lijnen zijn!
		if (dp.llFactor > 0)
		{	doorzienFrame.topToolBar.shortLinesButton.setImage(DoorzienDWO.shortLines);		
			doorzienFrame.topToolBar.shortLinesButton.enabled = true;
            if (dp.llFactor >= (dp.MAXLLFACTOR - dp.LLSTEP / 10))
                doorzienFrame.topToolBar.lengLinesButton.setOn(false);    

		}

		dp.zoom = scormedObject3D.zoomFactor;
        if (dp.zoom <= (dp.MINZOOM + dp.ZOOMSTEP / 10))
        {	doorzienFrame.rightToolBar.zoomOutButton.setOn(false);    
        }
        if (dp.zoom >= (dp.MAXZOOM - dp.ZOOMSTEP / 10))
        {   doorzienFrame.rightToolBar.zoomInButton.setOn(false);    
		}
		
		// drawingPanel.panel3D
		dp.panel3D.testString = "";
		if (dp.mouseMode == dp.FOLDOUT)
		{	dp.foldOutObjectGroup = scormedObject3D.theFoldOutGroup;
			dp.panel3D.initializeModel(scormedObject3D.theFoldOutGroup, false);					
		}
		else if (dp.mouseMode == dp.CUTOBJECT)
		{	dp.cutObjectGroup = scormedObject3D.theCutObjectGroup;
			dp.panel3D.initializeModel(scormedObject3D.theCutObjectGroup, false);					
			dp.panel3D.testString = scormedObject3D.volumeString;
		}
		else
			dp.panel3D.initializeModel(scormedObject3D.theObjectGroup, false);			

		dp.panel3D.setProjection(scormedObject3D.projection);		
		doorzienFrame.resetProjection(scormedObject3D.projection);
		dp.panel3D.mat = scormedObject3D.mat;
		dp.panel3D.mat.setOrigin(
			dp.panel3D.getSize().width / 2,
			dp.panel3D.getSize().height / 2,
			0);
		dp.panel3D.paintType = scormedObject3D.paintType;					
		dp.panel3D.showInside = scormedObject3D.showInside;					
		dp.panel3D.setZoomFactor(scormedObject3D.zoomFactor);					
		// dit EERST
		dp.setFilled(scormedObject3D.filled);
		if (dp.filled)
			doorzienFrame.rightToolBar.wireSolidButton.setImage(DoorzienDWO.wireFrame);
		// else niet nodig
					
		dp.fillPlanes(scormedObject3D.planesFilled);
		// als true dan zijn er zeker vlakken
		if (dp.planesFilled)
			doorzienFrame.topToolBar.planesFilledButton.setImage(DoorzienDWO.planesEmpty);

		// dit is altijd de currentObjectGroup
     	dp.addToHistory();
       	doorzienFrame.helpBar.setText(doorzienFrame.tt("rotateText"));

		dp.setLetters(scormedObject3D.letters);
		if (dp.letters)
			doorzienFrame.setLetters();	

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
			
            //doorzienFrame.rightToolBar.wireSolidButton.setImage(wireFrame);
            dp.setSlider(true, dp.currentFoldOut, 0, 1);        
            // hier!
            dp.flattened = scormedObject3D.flattened;
            doorzienFrame.rightToolBar.conDrawButton.setImage(DoorzienDWO.figure);
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
            doorzienFrame.topToolBar.cutButton.setImage(DoorzienDWO.glue);                
            doorzienFrame.topToolBar.undoButton.setOn(false);
            doorzienFrame.topToolBar.redoButton.setOn(false);
			
		}

			
		dp.panel3D.repaint();
		
		
	}

	// laat het ingelezen ScormedObject3D zien in de viewer
	public void showScormedObject3D()
	{	
		viewPanel3D.testString = "";
		//setSlider(false, 0, 0, 0);
		if (scormedObject3D.mode == FOLDOUT)
		{	viewGroup3D = (ObjectGroup3D) scormedObject3D.theFoldOutGroup.deepCopy();
//			setStartFacetCopy(viewGroup3D);		
			//setSlider(true, scormedObject3D.angle, 0, 1);
		}
		else if (scormedObject3D.mode == CUTOBJECT)
		{	viewGroup3D = (ObjectGroup3D) scormedObject3D.theCutObjectGroup.deepCopy();
			viewPanel3D.testString = scormedObject3D.volumeString;
		}
		else	
		{	viewGroup3D = (ObjectGroup3D) scormedObject3D.theObjectGroup.deepCopy();
		}
			
		viewPanel3D.initializeModel(viewGroup3D, false);			
		viewPanel3D.setProjection(scormedObject3D.projection);		
		viewPanel3D.mat = Matrix3D.copy(scormedObject3D.mat);
		viewPanel3D.mat.setOrigin(getSize().width / 2, getSize().height / 2-12, 0);
		viewPanel3D.paintType = scormedObject3D.paintType;					
		viewPanel3D.showInside = scormedObject3D.showInside;							
		viewPanel3D.setZoomFactor(scormedObject3D.zoomFactor);					
		viewPanel3D.repaint();
		//rotateString = textTable.lookUp("rotateText");

	}
	
	public void setSlider(boolean b, double init, double min, double max)
    {   if (b)
        {   sliderValue = init;
//System.out.println("initValue = " + UF.format(sliderValue, 2));            
            slider = new ViewSlider(this, min, max);
            //currentFoldOut = sliderValue;
            
            //slider.setLocation(offSet, 
	        //    getSize().height - offSet - slider.getSize().height); 
	        slider.setLocation(0,getSize().height-20);
	        	   
            //add(slider);
            flatButton = new LWButton(Table.lookUp("flatText"),
                             30, slider.getSize().height);
            flatButton.setLocation(
                 slider.getLocation().x + slider.getSize().width, 
                 slider.getLocation().y);
            //add(flatButton);
            // add listener                
            flatButton.addMouseListener(new FlatML());
            //    flattened = false;
            
        }
        else
        {   if (slider != null)
                remove(slider); 
            slider = null;    
            if (flatButton != null)
                remove(flatButton); 
            flatButton = null;    
        }    
    }
    
	public void processSlider(double newValue)
	{	sliderValue = newValue;
		scormedObject3D.angle = newValue;    
//System.out.println("newValue = " + UF.format(newValue, 2));    
//        currentFoldOut = sliderValue;
        foldOut(scormedObject3D.theFoldOutTreeRoot, sliderValue);
		viewGroup3D = (ObjectGroup3D) scormedObject3D.theFoldOutGroup.deepCopy();        
        viewPanel3D.initializeModel(viewGroup3D, false);
		
// scormedObject3D veranderen		
	}

    // recursively
    // foldOutFactor bewteen 0 and 1
    public void foldOut(FoldOutTreeNode startNode, double foldOutFactor)
    {   // skip the root completely
        if (startNode.parentNode != null)
        {   // determine angle which is needed if no folding
            // occured yet, between minAngle and Math.PI
            // relative to minAngle
            double foldAngle = //startNode.minAngle +
                (Math.PI - startNode.minAngle)* foldOutFactor; 
            // adapt to current fold status    
            // note: currentAngle is also relative to minAngle
            double rotAngle = foldAngle - startNode.currentAngle;    
if (Math.abs(rotAngle) > Vector3D.NZero)
{
//System.out.println("sNode-level = " + startNode.level);
//System.out.println("cos = " + UF.format(Math.cos(rotAngle), 12));
//System.out.println("sNode-rotAngle = " + 
//                   UF.format(rotAngle * 360 / (2 * Math.PI), 12));
}                   
            startNode.currentAngle = foldAngle;
            // find rotation axis            
            Vector3D axisStart = startNode.facet.points[startNode.axisFrom];
            Vector3D axisEnd = startNode.facet.points[startNode.axisTo];
            Line3D rotationAxis = new Line3D(axisStart, axisEnd);
            for (int j = 0; j < startNode.foldOutFacets.size(); j++)
            {   Facet3D fa = (Facet3D) startNode.foldOutFacets.elementAt(j);
                // find the object containing fa
                Object3D ob = 
                	scormedObject3D.theFoldOutGroup.objectContains(fa);
                for (int k = 0; k < fa.numPoints; k++)
                {   // find point k on facet 
                    Vector3D v = fa.points[k];
                    // rotate it around axis
                    Vector3D rotV = rotationAxis.rotateBy(v, rotAngle);
                    // replace vertex in vertex array
                    // deze vertex staat op index fa.indices[k]
                    // in ob.vertices
//System.out.println("ob.vertices = " + ob.vertices.length);                    
                    ob.vertices[fa.indices[k]] = rotV;
                    fa.points[k] = rotV;                
//                    fa.setNormal();
                }
                fa.setNormal();                
            }
        }
        
        for (int i = 0; i < startNode.childNodes.size(); i++)
        {   FoldOutTreeNode childNode = (FoldOutTreeNode) startNode.childNodes.elementAt(i);
            // this folds the whole subtree!!
            foldOut(childNode, foldOutFactor);
        }
        
    }

	public void setStartFacetCopy(ObjectGroup3D foldOutDeepCopy)
	{	foldOutDeepCopy.fixFacetArray();
		for (int i = 0; i < foldOutDeepCopy.numFacets; i++)
		{	if (Facet3D.isEqualTo(
					foldOutDeepCopy.facets[i], 
					scormedObject3D.theStartFacet) >= 0
			   )
				startFacetCopy = foldOutDeepCopy.facets[i];
		}
		
	}
	
	class AL implements ActionListener
	{   public void actionPerformed(ActionEvent e)
	    {   if (!frameStarted && e.getSource()==startButton)
			{	frameStarted = true;
				startButton.setEnabled(false);
				// note the handle to the outer class! 
				if (scormedObject3D == null)
				{	doorzienFrame = new DoorzienFrame(doorzienDWO, ViewPanel.this, null);
				}
				else
				{	doorzienFrame = new DoorzienFrame(doorzienDWO, ViewPanel.this, scormedObject3D);
					setScormedObject3D();					
				}
				doorzienFrame.setSize(
				    doorzienFrame.getSize().width + 1,
				    doorzienFrame.getSize().height + 1);
				doorzienFrame.repaint();    
				
					
             }
             if (!frameStarted && e.getSource()==opnieuwButton)
			 {	setState(startFiguurString);
			 }
	    }    
	} 
	
	class MLMML extends MouseAdapter implements MouseMotionListener
    {   boolean dragging = false; 
        // counter for draggEvents
        int draggCount = 0;
        // cursor start
        int xStart;
        int yStart;
        boolean inCircle;
        boolean draggingOnExit = false;
        boolean draggStartInCircle = false;
        
        public void mousePressed(MouseEvent e)
        {   if (viewPanel3D.model == null)
        		return;
        		
        	viewPanel3D.oldX = e.getX();
            viewPanel3D.oldY = e.getY();
            xStart = e.getX();
            yStart = e.getY();
            dragging = true;
        }
        public void mouseReleased(MouseEvent e)
        {   if (dragging)
            {   // make sure the dragg-event-queue for rotating is completed!!
                viewPanel3D.repaint();
                dragging = false;
                draggStartInCircle = false;
                draggingOnExit = false;
            }
		}
        public void mouseMoved(MouseEvent e)
        {   
        	if (viewPanel3D.model == null)
        		return;
        
        	xMoved = e.getX();
            yMoved = e.getY();

            int centerX = viewPanel3D.getSize().width / 2;
            int centerY = viewPanel3D.getSize().height / 2;
            int minRad = Math.min(centerX, centerY);
            
            inCircle = 
                Math.sqrt((e.getX() - centerX) * (e.getX() - centerX) +
                          (e.getY() - centerY) * (e.getY() - centerY)) <
                          minRad * RADFACTOR;
            if (inCircle)
                viewPanel3D.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));   
            else
                viewPanel3D.setCursor(new Cursor(Cursor.HAND_CURSOR));               
		}	
        public void mouseDragged(MouseEvent e)
        {   if (dragging)
            {
                int xCenter = viewPanel3D.getSize().width / 2;
                int yCenter = viewPanel3D.getSize().height / 2;
                int minRad = Math.min(xCenter, yCenter);
            
                inCircle = 
                    Math.sqrt((xStart - xCenter) * (xStart - xCenter) +
                              (yStart - yCenter) * (yStart - yCenter)) <
                              minRad * RADFACTOR;

                if ((e.getX() <= 0) || (e.getY() <= 0) ||
                    (e.getX() >= viewPanel3D.getSize().width) ||
                    (e.getY() >= viewPanel3D.getSize().height) 
                    )
                {    
                    dragging = false;
                    return;    
                }
                
                if (inCircle)
                {
                    double xTheta = (viewPanel3D.oldY - e.getY()) * 180.0d /
                                     viewPanel3D.getSize().width;
                    double yTheta = (viewPanel3D.oldX - e.getX()) * 180.0d /
                                     viewPanel3D.getSize().height;
                    viewPanel3D.rotateBy(xTheta, yTheta);
                    viewPanel3D.paint(viewPanel3D.getGraphics());
                
                
                    viewPanel3D.oldX = e.getX();
                    viewPanel3D.oldY = e.getY();
                }
                else
                {
                    
                    // choose correct direction
                    double centerX = ((double) viewPanel3D.getSize().width) / 2;
                    double centerY = ((double) viewPanel3D.getSize().height) / 2;                    
                    double xTheta = 0;
                    double yTheta = 0;

                    if (e.getX() < centerX)
                        yTheta = (viewPanel3D.oldY - e.getY()) * 180.0d /
                                 viewPanel3D.getSize().height;
                    else                    
                        yTheta = (e.getY() - viewPanel3D.oldY) * 180.0d /
                                 viewPanel3D.getSize().height;
                    if (e.getY() < centerY)             
                        xTheta = (e.getX() - viewPanel3D.oldX) * 180.0d /
                                 viewPanel3D.getSize().width;
                    else                 
                        xTheta = (viewPanel3D.oldX - e.getX()) * 180.0d /
                                 viewPanel3D.getSize().width;
                                     
                    double zTheta = 0;
                    
                    if (Math.abs(yTheta) > Math.abs(xTheta))
                        zTheta = yTheta;
                    else
                        zTheta = xTheta;
                    
                    viewPanel3D.rotateByZ(zTheta);
                    viewPanel3D.paint(viewPanel3D.getGraphics());
                
                
                    viewPanel3D.oldX = e.getX();
                    viewPanel3D.oldY = e.getY();
                    
                }    
            }
            
        } // mouseDragged

        public void mouseEntered(MouseEvent e)
        {   
        	if (viewPanel3D.model == null)
        		return;
        
            int centerX = viewPanel3D.getSize().width / 2;
            int centerY = viewPanel3D.getSize().height / 2;
            int minRad = Math.min(centerX, centerY);
            
            inCircle = 
                Math.sqrt((e.getX() - centerX) * (e.getX() - centerX) +
                          (e.getY() - centerY) * (e.getY() - centerY)) <
                          minRad * RADFACTOR;
            if (inCircle)
                viewPanel3D.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));   
            else
                viewPanel3D.setCursor(new Cursor(Cursor.HAND_CURSOR));               
        }
		
	}
	
	class FlatML extends MouseAdapter
    {   public void mousePressed(MouseEvent e)
        {   
        	// update, er kan gedraaid zijn
			setStartFacetCopy(viewGroup3D);                    
            // roteer de foldOutGroup in view space
            Vector3D from = new Vector3D(
                startFacetCopy.unitNormal.x,
                startFacetCopy.unitNormal.y,
                startFacetCopy.unitNormal.z);
//System.out.println("from = " + from.toString());            
            Vector3D to = new Vector3D(0, 0, 1);
            // draai
            viewPanel3D.vwRotate(from, to);
            // update
			setStartFacetCopy(viewGroup3D);            
            
//System.out.println("new from = " + startFacetCopy.unitNormal.toString());                        



// ook (tijdelijk) parallele projectie??

            // zet slider op 100% (maakt maximale foldout)
            
            processSlider(1);
            slider.setPosition(1);
        }    
    }
}
