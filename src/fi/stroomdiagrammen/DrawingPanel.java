package fi.stroomdiagrammen;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.Serializable;

import javax.swing.*;

// class for main drawing area
public class DrawingPanel extends Container implements Runnable
{   // applet frame
    //FlowFrame owner;
	Stroomdiagrammen owner;
    // size constants    
    // grid size in pixels
    public static int GRIDSIZE = 10;
    // dimensions for vertices in pixels
    // this is also layerwidth
    public static int vertexWidth = 62;
    public static int leftButtonWidth = 10;    
    public static int arrowButtonWidth = 15;    
    public static int vertexHeight = 26;
    // proposed label height
    public static int LABELHEIGHT = 18;
    // actual label height
    public static int labelHeight = 0;
    // dimensions for numbers in edges
    public static int edgeNumberWidth = 38;    
    public static int edgeNumberHeight = 25;    
    // maximum width between layers
    public static int maxLayerDistance = 110;
    // actual width between layers
    public int layerDistance = maxLayerDistance;
    // roundedness (capacityFields)
    public static int roundWidth = 26;
    public static int roundHeight = 14;
    // left, right, top, bottom insets workSpace (pixels)
    public static int leftSpace = 10, rightSpace = 10, 
                      topSpace = 10, bottomSpace = 10; 
    // minimum vertical distance between two vertices in the same layer
    public static int minSpace = 5;
    // value not yet defined (empty)
//    public static double unDefined = -1e9d;    
    public static Rational unDef = Rational.unDefined();
    // maximum number of layers
    public static int maxLayers = 16;
    // actual number of layers, set in defineSpaces
    int numLayers = 0; //, oldNumLayers;
    // modes for showing flows
    public static int decMode = 0;
    public static int percMode = 1;
    public static int fracMode = 2;
    // actual mode
    int flowMode = decMode;
    // decimals for capacities
    public static int capDecs = 2;
    // decimals for vertices
    int vDecimals = 0;
    // modes for showing edge thickness
    public static int relMode = 0;
    public static int absMode = 1;
    // actual mode
    int thickMode = relMode;
    // work area
    // effective area after/before resizing
    Rectangle workSpace, oldWorkSpace;
    // first root, cannot be deleted 
//    Vertex root;
    // roots vector
    Vector roots = new Vector();
    // counting sources
    Vector sources = new Vector();
    // finding (forward) orbits
    Vector orbit = new Vector();
    // vertex being traced back
    Vertex traceFrom = null;
    // layout manager
    DiagramManager diagramManager;
    // thread for flowing
    Thread flowThread;
    // flagg for showing flow
    boolean flowOn = false;
    // other attributes
    // boolean for deleteMode
    boolean deleteMode = false;
    // vertex labels?
//    boolean vertexLabels = false;
    // listener for vertex movements
    MLMML listener;

	public static int vertexCode = 1;
    Vector history = new Vector();
    public static int MAXHISTORY = 50;

//    boolean sizeSet = false;
    
// for testing
String testString = "";
// font for testing
Font fo = new Font("Helvetica", Font.PLAIN, 11);

	Image offScreen;
	Graphics offGraphics;

    // constructor    
    public DrawingPanel(Stroomdiagrammen o)
    {   owner = o;
        // for Container
        setLayout(null);    
        // init diagramManager
        diagramManager = new DiagramManager(this);
        // add listener for resizing events
        ComponentListener cl = new CL();
        addComponentListener(cl);
        MouseListener ml = new ML();
        addMouseListener(ml);
        KeyListener kl = new KL();
        addKeyListener(kl);
    } // constructor
    
    public void addToHistory()
    {   //if (diagramManager.vertexLabelsChanged())
        //{   DiagramCopy dco = diagramManager.copyDiagram();
        //    history.addElement(dco);
        //}
        DiagramCopy dc = diagramManager.copyDiagram();
        history.addElement(dc);
        if (history.size() > MAXHISTORY)
            history.removeElementAt(0);
        if (history.size() > 1)
            owner.bPanel.previousButton.setEnabled(true);
    }

    public void updateHistoryLabels()
    {   // go through list of all vertices present
    	Vector vertexRefs = diagramManager.getVertexRefs();
    	for (int vCnt = 0; vCnt < vertexRefs.size(); vCnt++)
    	{	Vertex v = (Vertex) vertexRefs.elementAt(vCnt);
	        // go through list of all diagram copies
	        for (int hCnt = 0; hCnt < history.size(); hCnt++)
    	    {   DiagramCopy dc = (DiagramCopy) history.elementAt(hCnt);
        	    // go through list of vertexcopies in each diagram copy
        	    for (int vcCnt = 0; vcCnt < dc.vertexCopies.size(); vcCnt++)
            	{   VertexCopy vc = (VertexCopy) dc.vertexCopies.elementAt(vcCnt);
                	if (v.code == vc.code)
                    	vc.labelText = v.vLabel.getText();
                
            	}    
        	}
        }
    }
    
    public void previousDiagram()
    {   int hisSize = history.size();
        if (hisSize > 1)
        {   history.removeElementAt(hisSize - 1);
            DiagramCopy dc = (DiagramCopy) history.lastElement();
            diagramManager.recreateDiagram(dc);
        }
    }    
    
    // initialization of components etc.
    public void initialize()
    {   
    	
    	if (owner.breuken)
    		flowMode = fracMode;
    	
    	if (owner.absoluut)
    		thickMode = absMode;
    	
    	//dpSize = getSize();
        // set size of storageHeight
        // workSpace.height the rest 
        // takes care of borders
        defineSpaces(false);
        if (owner.diagramCopy == null)
		{
System.out.println("dc = null");        	
	        // create and add root vertex        
    	    Vertex root = new Vertex(true, 0);
	        roots.addElement(root);
    	    root.addEdgeButton.addMouseListener(new AddEdgeML());
	        MLMML lis = new MLMML();
    	    root.flowField.addMouseListener(lis);
        	root.flowField.addMouseMotionListener(lis);
	        diagramManager.insertVertex(root, null);
    	    addToHistory();
    	}
    	else
    	{	
    		
System.out.println("dc != null");    		
    		// truckje
    		owner.setSize(owner.getSize().width, owner.getSize().height + 1);
    		diagramManager.recreateDiagram(owner.diagramCopy);
    		addToHistory();
    	}
        
        
        
    }  // initialize  

    public void addNewRoot()
    {   Vertex newRoot = new Vertex(true, 0);
        roots.addElement(newRoot);
        newRoot.addEdgeButton.addMouseListener(new AddEdgeML());
        MLMML lis = new MLMML();
        newRoot.flowField.addMouseListener(lis);
        newRoot.flowField.addMouseMotionListener(lis);
        diagramManager.insertVertex(newRoot, null);
        addToHistory();
    }    

    // sets areas at initialize and after resizing
    public void defineSpaces(boolean resizing)
    {   if (resizing)
        {   // save old workSpace
        	oldWorkSpace = workSpace;
        }    
        workSpace = new Rectangle(GRIDSIZE, GRIDSIZE,
                                  getSize().width - 2 * GRIDSIZE,
                                  getSize().height - 2 * GRIDSIZE);
        if (oldWorkSpace == null)
        	oldWorkSpace = workSpace;	                          
        setLayerDistance();                          
    } // defineSpaces   
    
    // update workSpace after resizing
    public void updateWork()
    {  diagramManager.resizeDiagram(true); 
    }  // updateWork  
    
    public void setLayerDistance()
    {   if (numLayers == 0)
            layerDistance = maxLayerDistance;
        else
        {   int newLayerDistance = 
                (workSpace.width - leftSpace - rightSpace -
                (numLayers + 1) * vertexWidth) /
                numLayers;
            layerDistance = Math.min(newLayerDistance, maxLayerDistance); 
        }
    }    
    

    // check if r Rectangle r contains lwc    
    public boolean rectangleContains(Rectangle r, LWContainer lwc)
    {   return ((r.x <= lwc.getLocation().x) &&
                (r.y <= lwc.getLocation().y) &&
                ((lwc.getLocation().x + lwc.getSize().width) <=
                 (r.x + r.width)) &&
                ((lwc.getLocation().y + lwc.getSize().height) <=
                 (r.y + r.height)));
    }  // rectangleContains  
    
    public int isInLayer(Vertex v)
    {   if (!rectangleContains(workSpace, v))
            return -1;
        int index = -1;    
        for (int i = 0; i < maxLayers; i++)
        {   int layerStart = workSpace.x + leftSpace +
                             i * (layerDistance + vertexWidth);
            if (Math.abs(v.getLocation().x - layerStart) <=
                vertexWidth / 5)
                index = i;
        }    
        return index;
    }    
    public int getLayerStart(int lNum)
    {   return workSpace.x + leftSpace +
               lNum * (layerDistance + vertexWidth);
    }    
    

    // check if Rectangle r contains Rectangle s, overloaded        
    public boolean rectangleContains(Rectangle r, Rectangle s)
    {   return ((r.x <= s.x) && (r.y <= s.y) &&
                ((s.x + s.width) <= (r.x + r.width)) &&
                ((s.y + s.height) <= (r.y + r.height)));
    } // rectangleContains   

    // tracing flow recursively
    public void traceBack(Vertex v)
    {   for (int i = 0; i < v.inEdges.size(); i++)
        {   Edge ine = (Edge) v.inEdges.elementAt(i);
            ine.highlighted = true;
            traceBack(ine.fromVertex);
        }    
        repaint();
    }    
  
    // find forward orbit of vertex v
    public void forwardOrbit(Vertex v)
    {   for (int i = 0; i < v.outEdges.size(); i++)
        {   Edge oute = (Edge) v.outEdges.elementAt(i);
            Vertex outv = oute.toVertex;
            if (!orbit.contains(outv))
                orbit.addElement(outv);
            forwardOrbit(outv);
        }    
    }    
    
    // trace all roots connected with v
    public void traceAllSources(Vertex v)
    {   // reset
        sources.removeAllElements();
        // find some
        traceSomeSources(v);
        // take the first
        Vertex someRoot = (Vertex) sources.elementAt(0);
        // reset
        orbit.removeAllElements();
        // put forward orbit of first in 'orbit'
        forwardOrbit(someRoot);
        Vector someOrbit = new Vector();
        // copy
        for (int j = 0; j < orbit.size(); j++)
            someOrbit.addElement(orbit.elementAt(j));
        //     
        for (int i = 0; i < roots.size(); i++)
        {   Vertex rt = (Vertex) roots.elementAt(i);
            if (!sources.contains(rt))
            {   orbit.removeAllElements();
                forwardOrbit(rt);
                boolean intersection = false;
                for (int k = 0; k < orbit.size(); k++)
                {   if (someOrbit.contains(orbit.elementAt(k)))
                        intersection = true;
                }    
                if (intersection)
                    sources.addElement(rt);
            }
        }    
    }    
    
    // find at least one source connected to v
    // tracing flow from sources recursively
    // avoid counting double!!
    public void traceSomeSources(Vertex v)
    {   // reset sources first elsewhere!!!!!
        if (roots.contains(v))
        {   {   if (!sources.contains(v))
                    sources.addElement(v);
            }
        }    
        else
        {   for (int i = 0; i < v.inEdges.size(); i++)
            {   Edge ine = (Edge) v.inEdges.elementAt(i);
                // check for source
                traceSomeSources(ine.fromVertex);
            }    
        }    
    }    
    
    public Rational getSourceFlow(Vertex v)
    {   traceAllSources(v);
        Rational sFlow = new Rational(0, 1, 0);
        for (int s = 0; s < sources.size(); s++)
        {   Vertex source = (Vertex) sources.elementAt(s);
            if (source.flow.isUndefined())
                sFlow.decVal = Rational.unDefined;
            else    
                sFlow.decVal = Math.max(source.flow.decVal, sFlow.decVal);
        }        
        return sFlow;
    }    
    
    public Rational getMaxRootFlow()
    {   Rational sFlow = new Rational(0, 1, 0);
        for (int s = 0; s < roots.size(); s++)
        {   Vertex rt = (Vertex) roots.elementAt(s);
            if (rt.flow.isUndefined())
                return DrawingPanel.unDef;
            else    
                sFlow.decVal = Math.max(sFlow.decVal, rt.flow.decVal);
        }    
        return sFlow;
    }    
    
    // flow thread
    public void run()
    {   while (true)
        {   diagramManager.moveBubbles();
            try
            {   flowThread.sleep(150);
            }
            catch (InterruptedException ie) {}
        }
    }
    
    // private method to find a darker or a brighter version of color c, using the
    // HSB color model; factor determines the amount of change, a negative
    // factor produces darker colors, a positive factor brighter colors
    public static Color hsbChange(Color c, int factor)
    {   // array for storing hue, saturation, brigtness
        float[] hsbValues = new float[3];
        // the resulting variant of Color c
        Color result;
        // find hsbValues for Color c
        hsbValues = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(),
                    hsbValues);
        // if a darker color is wanted
        if (factor < 0)
        {   // if possible decrease brightness by |factor|*0.1
            if (hsbValues[2] >= -factor * 1e-1f)
                hsbValues[2] -= -factor * 1e-1f;
            // else try to increase saturation by |factor|*0.1        
            else    
                if (hsbValues[1] <= 1.0f + factor * 1e-1f)
                    hsbValues[1] += -factor * 1e-1f;
        }
        else // a brighter color is wanted
        {   // if possible increase brightness by factor*0.1
            if (hsbValues[2] <= 1.0f - factor * 1e-1f)
                hsbValues[2] += factor * 1e-1f;
            // else try to decrease saturation by factor*0.1        
            else    
                if (hsbValues[1] >= factor * 1e-1f)
                    hsbValues[1] -= factor * 1e-1f;
        }
        // get the resulting color in the RGB model            
        result = Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);            
        return result;
    }     
/*
    public void update(Graphics g)
    {   paint(g);
    }

	public void paint(Graphics g)
	{	if (offScreen == null)
            offScreen = createImage(getSize().width, getSize().height);
        offGraphics = offScreen.getGraphics();
        offGraphics.setClip(0, 0, getSize().width, getSize().height);
        paintOpBuffer(offGraphics);
        //super.paint(og);
        g.drawImage(offScreen, 0, 0, null);
	}
*/
    // paint
    public void paint(Graphics g)
    {   // paint backgrounds
        // workspace
        g.setColor(Stroomdiagrammen.workBackground);
        g.fillRect(workSpace.x, workSpace.y,
                   workSpace.width, workSpace.height);

/*
// (temporary) grids (for checking)
        // work space
        int numRows = workSpace.height / GRIDSIZE;
        int numColumns = workSpace.width / GRIDSIZE;        
        // horizontal
        for (int i = 0; i < numRows + 1; i++)
        {   g.setColor(new Color(222, 222, 222));        
            g.drawLine(workSpace.x, workSpace.y + i * GRIDSIZE, 
                       workSpace.x + workSpace.width - 1, workSpace.y + i * GRIDSIZE);
        }               
        // vertical               
        for (int j = 0; j < numColumns + 1; j++)
            g.drawLine(workSpace.x + j * GRIDSIZE, workSpace.y, 
                       workSpace.x + j * GRIDSIZE, workSpace.y + workSpace.height - 1);
*/                       
                       
        // outline layers                       
        g.setColor(new Color(222, 222, 222));
        for (int k = 0; k < maxLayers; k++)
        {   g.drawLine(
                workSpace.x + leftSpace + k * (vertexWidth + layerDistance),
                workSpace.y, 
                workSpace.x + leftSpace + k * (vertexWidth + layerDistance),
                workSpace.y + workSpace.height - 1);
            g.drawLine(
                workSpace.x + leftSpace + vertexWidth + k * (vertexWidth + layerDistance),
                workSpace.y, 
                workSpace.x + leftSpace + vertexWidth + k * (vertexWidth + layerDistance),
                workSpace.y + workSpace.height - 1);        
            
        }    
        

        // outlines
        g.setColor(Color.black);
        g.drawRect(workSpace.x, workSpace.y,
                   workSpace.width - 1, workSpace.height - 1);
                   
        // draw the edges           
        diagramManager.drawEdges(g);           
        
/*        
// testing        
        g.setFont(fo);        
        int bx = workSpace.x + 2 * GRIDSIZE;
        int by = workSpace.y + GRIDSIZE;
        g.drawString(
        " " + history.size()
// insert test string here        
// testString
        , bx, by);
*/        
        
        
        
        // paint vertices and edge capacity fields
        super.paint(g);               

        // fill borders to prevent drawing objects
        // outside predefined areas
        g.setColor(Stroomdiagrammen.appletBackground);
        // bottom
        g.fillRect(0, workSpace.y + workSpace.height, getSize().width, 
                   getSize().height - (workSpace.y + workSpace.height));
        // top                   
        g.fillRect(0, 0, getSize().width, workSpace.y);
        // left           
        g.fillRect(0, 0, workSpace.x, getSize().height);
        // right                   
        g.fillRect(workSpace.x + workSpace.width, 0,
                   getSize().width - (workSpace.x + workSpace.width), 
                   getSize().height);
    } // paint
    
    // handles for use from outside
    public TraceML getTraceML()
    {   return new TraceML();
    }    
    public AddEdgeML getAddEdgeML()
    {   return new AddEdgeML();
    }    
    public MLMML getMLMML()
    {   return new MLMML();
    }    
    // inner class for listening to resizing events of Drawpanel   
    class CL extends ComponentAdapter
    {   public void componentResized(ComponentEvent e)
        {   
            defineSpaces(true);
            updateWork();
//            boolean remember = !oldWorkSpace.equals(workSpace) && !sizeSet;            
//            if (remember)
//                addToHistory();
            
        }    
    } // class CL   

    class ML extends MouseAdapter
    {   public void mousePressed(MouseEvent e)
        {   requestFocus();
            Edge edge = diagramManager.getClickedEdge(e.getX(), e.getY());
            
            if ((edge != null) && 
                (deleteMode || (e.getModifiers() & e.BUTTON3_MASK) != 0)
               ) 
            {   diagramManager.deleteEdge(edge);
                deleteMode = false;
                addToHistory();
            }
        }
    }    
    class KL extends KeyAdapter
    {   public void keyPressed(KeyEvent e)
        {   int kc = e.getKeyCode();
            if (kc == KeyEvent.VK_DELETE)
            {   deleteMode = true;
            }    
        }    
        public void keyReleased(KeyEvent e)
        {   int kc = e.getKeyCode();
            if (kc == KeyEvent.VK_DELETE)
            {   deleteMode = false;
            }    
        }    
    }    

    // inner class for vertex color button
    class TraceML extends MouseAdapter
    {   public void mousePressed(MouseEvent e)
        {   requestFocus();
            diagramManager.lowLightEdges();
            Vertex v = (Vertex) e.getComponent().getParent();
            if (v != traceFrom)
            {   traceFrom = v;
                traceBack(v);
            }
            else
            {   traceFrom = null;
                repaint();
            }
            addToHistory();
        }    
    }    
    // inner class for vertex add edge button
    class AddEdgeML extends MouseAdapter
    {   public void mousePressed(MouseEvent e)
        {   requestFocus();
            LWArrowButton lwa = (LWArrowButton) e.getComponent();
            if (lwa.enabled)
            {   // diagramManager.lowLightEdges();
                Vertex v = (Vertex) e.getComponent().getParent();
                // create new vertex to be connected to v            
                Vertex newVertex = new Vertex(false, v.layerNum + 1);
                newVertex.decimals = vDecimals;
                // add listeners
                newVertex.addEdgeButton.addMouseListener(new AddEdgeML());            
                newVertex.colorButton.addMouseListener(new TraceML());    
                MLMML lis = new MLMML();
                newVertex.flowField.addMouseListener(lis);
                newVertex.flowField.addMouseMotionListener(lis);
                diagramManager.insertVertex(newVertex, v);
                // initially 0
                Rational cap = new Rational(0, 1, 0);
                // first outedge 1
                if (v.outEdges.size() == 0)
                     cap = new Rational(1, 1, 1);
                // second outedge 0.5 and 0.5     
                else if (v.outEdges.size() == 1)
                {   Edge ed = (Edge) v.outEdges.elementAt(0); 
                    ed.setCapacity(new Rational(1, 2, 5e-1d), false);
                    cap = new Rational(1, 2, 5e-1d);
                }
                Edge newEdge = new Edge(DrawingPanel.this, v, newVertex, cap); 
                // this sorts the outedges of v
                diagramManager.addEdge(newEdge); 
                // if v is not a root
                if (v.layerNum > 0)
                {   // this edge exists!!
                    Edge preEdge = (Edge) v.inEdges.elementAt(0);
                    Vertex preVertex = preEdge.fromVertex;
                    int preMode = preEdge.mode;
                    // copy capacities
                    if (preVertex.outEdges.size() == v.outEdges.size())
                    {   for (int i = 0; i < v.outEdges.size(); i++)
                        {   Edge preOe = (Edge) preVertex.outEdges.elementAt(i);
                            Edge postOe = (Edge) v.outEdges.elementAt(i);
                            postOe.setCapacity(preOe.capacity, false);
                        }    
                    }    
                    // copy mode
                    for (int j = 0; j < v.outEdges.size(); j++)
                    {   Edge oe = (Edge) v.outEdges.elementAt(j);
                        oe.setMode(preMode);
                    }    
                }    
                else
                    newEdge.setMode(DrawingPanel.decMode);                
                diagramManager.calculateDiagram();
                
                addToHistory();
            } // if enabled
        } // mousePressed   
    } // class AddEdgeML   
    
    // listening to mouse and mouse motion events on LWContainers
    class MLMML extends MouseAdapter implements MouseMotionListener
    {   // dragging with (left) mouse button
        boolean dragging = false;
        // anker for dragging
        Point anker = null;
        // Vertex where dragg events take place
        Vertex v;
        // old position of vertex
        Point oldPos = null;
        // shift while dragging
        int dx, dy;
        int leftBorder, rightBorder;        
        
        // mouse pressed events
        public void mousePressed(MouseEvent e)
        {   //requestFocus();
            // get the vertex
            v = (Vertex) e.getComponent().getParent();
            v.flowField.requestFocus();
            // note: we are dragging the flowField of vertex v
            leftBorder = getLayerStart(v.canMoveLeftTo()) 
                         - 4;
            rightBorder = getLayerStart(v.canMoveRightTo()) 
                         + 4;
            
//            if (!deleteMode)            
//            {   
                // waar was dit voor??
                //owner.requestFocus();
                dragging = true;
                // save old position
                oldPos = new Point(v.getLocation().x, v.getLocation().y);
                // put on top
                remove(v);
                add(v, 0);
                // set anker point relative to v!!!
                anker = new Point(e.getComponent().getLocation().x + e.getX(), 
                                  e.getComponent().getLocation().y + e.getY());
//            }
//            else // deleteMode on
//            {   dragging = false;
                //if (v != root)
                //    diagramManager.deleteVertex(v);
                //owner.unDelete();    
//            }    
        } // mousePressed
        
        public void mouseReleased(MouseEvent e)
        {   if ((anker != null) && dragging)
            {   int newLayerNum = isInLayer(v);
                if (newLayerNum >= 0)
                {   if (!diagramManager.intersectsVertex(v))
                    {   boolean remember = (newLayerNum != v.layerNum);
                        diagramManager.moveVertexTo(v, newLayerNum);
                        if (remember)
                            addToHistory();
                    }
                    else  
                    {   Vertex fv = diagramManager.fuseWith(v);
                        if (fv != null)
                        {   diagramManager.fuseVertices(v, fv);            
                            addToHistory();
                        }
                        else
                            v.setLocation(oldPos.x, oldPos.y);                        
                    }
                }    
                else
                    v.setLocation(oldPos.x, oldPos.y);
                diagramManager.updateEdges();    
// do not update vertex layers!!!                
//                diagramManager.redrawDiagram();
                anker = null;
            }
            dragging = false;
        } // mouseReleased
        
        // dragging 
        public void mouseDragged(MouseEvent e)
        {   // anker should be set
            if ((anker != null) && dragging)
            {   // find relative movement for v!!
                dx = e.getComponent().getLocation().x + e.getX() - anker.x;
                dy = e.getComponent().getLocation().y + e.getY() - anker.y;
                // new position
                Point newPos = new Point(
                               v.getLocation().x + dx,
                               v.getLocation().y + dy);
                if ((dx < 0) && (newPos.x < leftBorder))
                    newPos.x = leftBorder;
                else if ((newPos.x > rightBorder))
                    newPos.x = rightBorder;
                // check if v intersects edges    
                if (!rectangleContains(workSpace, v))
                {   boolean deletable = (v.outEdges.size() == 0);
                    if (deletable)
                    {   // put in original location
                        // and save configuration
                        v.setLocation(oldPos.x, oldPos.y);   
                        addToHistory();
                        for (int i = v.inEdges.size() - 1; i >= 0; i--)
                        {   Edge ie = (Edge) v.inEdges.elementAt(i);
                            diagramManager.deleteEdge(ie);
                        }    
                    }
                    else
                        v.setLocation(oldPos.x, oldPos.y);   
                    dragging = false;
                    anker = null;
                }    
                else
                    v.setLocation(newPos.x, newPos.y);   
                // only update the edges!!!!              
                diagramManager.updateEdges();
                repaint();                  
            } // if anker != null   
        } // mouseDragged       
        
        // mouse moved events, not used        
        public void mouseMoved(MouseEvent e) {}        
    } // class MLMML        
} // class DrawingPanel

// use Container
abstract class LWContainer extends Container
{   boolean selectable = false;
    boolean selected = false;
    boolean highlighted = false;
    Color lwcColor;
    public int getRight()
    {   return getLocation().x + getSize().width;
    }    
    public int getBottom()
    {   return getLocation().y  + getSize().height;
    }    
    public boolean isSelected()
    {   return selected;
    }    
    public void setSelected(boolean b)
    {   selected = b;
        repaint();
    }    
}

class VertexCopy implements Serializable
{   // attributes
	int code;
    int layerNum;
    int yLocation;
    Rational flow = DrawingPanel.unDef;
    int decimals;
    boolean root;  
    boolean traceFrom; 
    String labelText;
    //Vertex vertex;
    //Vector inEdgeCopies = new Vector();
    //Vector outEdgeCopies = new Vector();
    public VertexCopy(int co, int ln, int yl, Rational f, int d, boolean r,
                      String lText)
    {   code = co;
    	layerNum = ln;
        yLocation = yl;
        if (!f.isUndefined())
            flow = new Rational(f);
        decimals = d;
        root = r;
        labelText = lText;
    }
}
class Vertex extends LWContainer
{   // attributes

	int code;
    int layerNum;
    Rational flow = DrawingPanel.unDef;
    NumberField flowField, vLabel;
    int decimals = 2;
    LWArrowButton colorButton;
    LWArrowButton addEdgeButton;
    boolean root;  
    Vector inEdges = new Vector();
    Vector outEdges = new Vector();
    // constructor
    public Vertex(boolean rt, int ln)
    {   
    	code = DrawingPanel.vertexCode;
    	DrawingPanel.vertexCode++;
    	root = rt;
        layerNum = ln;
        setSize(DrawingPanel.vertexWidth, 
                DrawingPanel.vertexHeight + DrawingPanel.labelHeight);
        int currentX = 1;
        if (!root)
        {   colorButton = new LWArrowButton(3, Color.lightGray);
            colorButton.setBounds(currentX, 1 + DrawingPanel.labelHeight, 
                        DrawingPanel.leftButtonWidth, 
                        getSize().height - 1 - DrawingPanel.labelHeight);
            add(colorButton);
            currentX += colorButton.getSize().width;
            flowField = new NumberField(
                root, 
                getSize().width - 
                    DrawingPanel.leftButtonWidth -
                    DrawingPanel.arrowButtonWidth,
                getSize().height - DrawingPanel.labelHeight);
            flowField.setLocation(currentX - 1, DrawingPanel.labelHeight);
            flowField.setBackground(new Color(255, 255, 150));
            add(flowField);
            currentX += flowField.getSize().width;
        }
        else // root of diagram
        {   flowField = new NumberField(
                root, 
                getSize().width - DrawingPanel.arrowButtonWidth,
                getSize().height - DrawingPanel.labelHeight);
            flowField.setLocation(currentX - 1, DrawingPanel.labelHeight);
            add(flowField);
            VertexIAL kfListener = new VertexIAL();
            flowField.addKeyListener(kfListener);
            flowField.addFocusListener(kfListener);
            currentX += flowField.getSize().width;
        }    
        addEdgeButton = new LWArrowButton(1, Color.lightGray);
        addEdgeButton.setBounds(currentX - 1, 1 + DrawingPanel.labelHeight, 
                      DrawingPanel.arrowButtonWidth, 
                      getSize().height - 1 - DrawingPanel.labelHeight);
        add(addEdgeButton);
        vLabel = new NumberField(true, DrawingPanel.vertexWidth,
                                 DrawingPanel.LABELHEIGHT);
        vLabel.addFocusListener(new VertexLabelIAL());                         
        vLabel.olColor = Color.red;                                 
        vLabel.setLocation(0, 0);              
        if (DrawingPanel.labelHeight > 0)
            add(vLabel);
        
    }    
    
    public void setLabel(boolean b)
    {   // add label
        if (b)
        {   setSize(DrawingPanel.vertexWidth,
                    DrawingPanel.vertexHeight + DrawingPanel.LABELHEIGHT);
            if (!root)
                colorButton.setLocation(colorButton.getLocation().x,
                    colorButton.getLocation().y + DrawingPanel.LABELHEIGHT);
            flowField.setLocation(flowField.getLocation().x,
                flowField.getLocation().y + DrawingPanel.LABELHEIGHT);
            addEdgeButton.setLocation(addEdgeButton.getLocation().x,
                addEdgeButton.getLocation().y + DrawingPanel.LABELHEIGHT);
            add(vLabel);
            //vLabel.setText("");
        }
        else // remove label
        {   remove(vLabel);
            setSize(DrawingPanel.vertexWidth,
                    DrawingPanel.vertexHeight);
            if (!root)
                colorButton.setLocation(colorButton.getLocation().x,
                    colorButton.getLocation().y - DrawingPanel.LABELHEIGHT);
            flowField.setLocation(flowField.getLocation().x,
                flowField.getLocation().y - DrawingPanel.LABELHEIGHT);
            addEdgeButton.setLocation(addEdgeButton.getLocation().x,
                addEdgeButton.getLocation().y - DrawingPanel.LABELHEIGHT);            
        }    
    }    
    public void setFlow(Rational f)
    {   
        
        flow = f;
        if (f.isUndefined())
            flowField.setText("");
        else if ( ((DrawingPanel) getParent()).flowMode == DrawingPanel.fracMode)
        {   if (flow.isInteger())
                flowField.setText(UF.format(flow.nom, 0));        
            else
                flowField.setText(UF.format(flow.nom, 0) + "/" +
                                  UF.format(flow.denom, 0));  
        }    
        else if ( ((DrawingPanel) getParent()).flowMode == DrawingPanel.decMode)
            flowField.setText(UF.format(flow.decVal, decimals));
//            flowField.setText(UF.format(flow.nom / flow.denom, decimals));

    }    

    public int canMoveLeftTo()
    {   if (root)
            return 0;
        int lNum = 0;
        for (int i = 0; i < inEdges.size(); i++)
        {   Edge ie = (Edge) inEdges.elementAt(i);
            lNum = Math.max(ie.fromVertex.layerNum + 1, lNum);
        }    
        return lNum;    
    
        
        
    }    
    public int canMoveRightTo()
    {   if (root)
            return 0;
        int lNum = 16; //(big)    
        for (int i = 0; i < outEdges.size(); i++)
        {   Edge oe = (Edge) outEdges.elementAt(i);
            lNum = Math.min(oe.toVertex.layerNum - 1, lNum);
        }    
        return lNum;    
        
        
    }    

    // bubble sort outEdges by y-location of toVertex
    public void sortOutEdges()
    {   // bubble sort on y location of toVertex
        Edge tEdge;
        boolean swapped;
        for (int i = outEdges.size() - 1; i >= 0; i--)
        {   swapped = false;
            for (int j = 0; j < i; j++)
            {   Edge e1 = (Edge) outEdges.elementAt(j);
                Edge e2 = (Edge) outEdges.elementAt(j + 1);
                double angle1 = getAngle(e1.toVertex);
                double angle2 = getAngle(e2.toVertex);                
                if (angle2 > angle1)
                {   tEdge = e1;
                    outEdges.setElementAt(e2, j);
                    outEdges.setElementAt(tEdge, j + 1);
                    swapped = true;
                }
            } // for       
            if (!swapped)
                return;
        } // for
    }


    public double getAngle(Vertex v)
    {   DrawingPanel dp = (DrawingPanel) getParent();
        double x = v.getLocation().x - getLocation().x + 
                   dp.vertexWidth;
        double y = getLocation().y - v.getLocation().y;
        return Math.atan(y/x);
    }    
    
/*
    public boolean hasFlow()
    {   double inCap = 0;
        for (int i = 0; i < inEdges.size(); i++)
        {   Edge ie = (Edge) inEdges.elementAt(i);
            inCap += ie.capacity.decVal;
        }    
        return inCap > 0;
    }    
*/    
    
    
    public void calculateFlow()
    {   Rational inFlow = new Rational(0, 1, 0);
        for (int i = 0; i < inEdges.size(); i++)
        {   Edge ie = (Edge) inEdges.elementAt(i);
            if (ie.fromVertex.flow.isUndefined())
            {   setFlow(DrawingPanel.unDef);
                return;
            }
            else
//            else if ( ((DrawingPanel) getParent()).flowMode == DrawingPanel.fracMode)
//            {   
                inFlow = inFlow.plus(ie.fromVertex.flow.times(ie.capacity));            
//            }
//            else
//            {   inFlow.decVal += ie.fromVertex.flow.decVal * ie.capacity.decVal;
//            }
        }
        setFlow(inFlow);
    }    

    public Edge oldestOutChanged()
    {   if (outEdges.size() == 0)
            return null;
        else
        {   int oldestChange = 0;
            long changeTime = Long.MAX_VALUE;
            // find "oldest" edge that was changed 
            for (int i = 0; i < outEdges.size(); i++)
            {   Edge ed = (Edge) outEdges.elementAt(i);
                long temp = ed.lastTimeChanged;
                if (temp < changeTime)
                {   changeTime = temp;
                    oldestChange = i;
                }    
            }    
            return (Edge) outEdges.elementAt(oldestChange);
        }
    }    
    public void updateOutCapacities(Edge changedEdge, int mode)
    {   Rational total = new Rational(0, 1, 0);
        int oldestChange = 0;
        long changeTime = Long.MAX_VALUE;
        // find sum of all capacities and
        // "oldest" edge that was changed 
        for (int i = 0; i < outEdges.size(); i++)
        {   Edge ed = (Edge) outEdges.elementAt(i);
            total = total.plus(ed.capacity);
            long temp = ed.lastTimeChanged;
            if (temp < changeTime)
            {   changeTime = temp;
                oldestChange = i;
            }    
        }    
        Rational temp = total.minus(new Rational(1, 1, 1));
        if (temp.isLarger(new Rational(0, 1, 0), mode))
        {   // note: case only one edge does not occur here
            Edge led = (Edge) outEdges.elementAt(oldestChange);
            // if possible decrease capacity of oldestChange
            // by total - 1
            if (led.capacity.isLargerOrEqual(temp, mode))
                led.setCapacity(led.capacity.minus(temp), false);
            else // not elegant??
            // set capacity of oldestChange to 1-changed
            // all others to zero
            {   Rational t1 = new Rational(1, 1, 1);
                led.setCapacity(t1.minus(changedEdge.capacity), false);
                for (int j = 0; j < outEdges.size(); j++)
                {    Edge oed = (Edge) outEdges.elementAt(j);
                     if ((oed != led) && (oed != changedEdge))
                         oed.setCapacity(new Rational(0, 0, 0), false);
                    
                }    
            }    
        }    
        else if (temp.isSmaller(new Rational(0, 1, 0), mode))
        {   // increase capacity of oldestChange by
            // 1 - total, i.e. decrease by total - 1
            Edge led = (Edge) outEdges.elementAt(oldestChange);
            led.setCapacity(led.capacity.minus(temp), false);
        }
        // else total = 1, nothing to do
    }    

    public void setOutModes(int mode)
    {   for (int i = 0; i < outEdges.size(); i++)
        {   Edge oe = (Edge) outEdges.elementAt(i);
            oe.setMode(mode);
        }    
    }    

    public Edge hasInEdgeFrom(Vertex v)
    {   Edge result = null;
        for (int i = 0; i < inEdges.size(); i++)
        {   Edge ie = (Edge) inEdges.elementAt(i);
            if (ie.fromVertex == v)
                result = ie;
        }
        return result;
    }
    
    public Edge hasOutEdgeTo(Vertex v)
    {   Edge result = null;
        for (int i = 0; i < outEdges.size(); i++)
        {   Edge ie = (Edge) outEdges.elementAt(i);
            if (ie.toVertex == v)
                result = ie;
        }
        return result;
    }
    
    public void paint(Graphics g)
    {   g.setColor(Color.black);
        g.drawRect(0, 0, getSize().width - 1, 
                         getSize().height - 1); 
        super.paint(g);   
/*        
        g.setColor(Color.black);
        g.drawRoundRect(0, 0, getSize().width - 1, 
                              getSize().height - 1, 
                              DrawingPanel.roundWidth, 
                              DrawingPanel.roundHeight);         
*/                              
    }    

    
    public void processRational(String t)
    {   boolean error = false;
        int nom = 0, denom = 1;
        String nomStr = null, denomStr = null;
        Rational value;
        // error handling here
        try
        {   t = removeAllBlanks(t);
            int slash = t.indexOf('/');
            if (slash >= 0)
            {   
                nomStr = t.substring(0, slash);
                if (slash == (t.length() - 1))
                {   error = true;
                    // reset
                    setFlow(flow); 
                }
                else
                    denomStr = t.substring(slash + 1);
            }
            else
            {   nomStr = t; 
                denomStr = "1";
            }
            if (!error)    
            {   // these lines generate exceptions and activate catch
                nom = Integer.parseInt(nomStr);
                denom = Integer.parseInt(denomStr);
            }    
        } // try
        catch (NumberFormatException nfe)
        {   error = true;
            // reset
            setFlow(flow); 
        }  // catch
        if (!error)
        {   value = new Rational(nom, denom);
            if (value.decVal < 0)
                setFlow(flow); // reset
            else
            {   setDecimals(value.decVal);
                boolean remember = !value.equals(flow);
                setFlow(value);
                ((DrawingPanel) getParent()).diagramManager.calculateDiagram();
                if (remember)
                    ((DrawingPanel) getParent()).addToHistory();
            }
        }    
    }    
  
    public void processDecimal(String t)
    {   boolean error = false;
        double value = 0;
        // error handling here
        try
        {   // change decimal separator to "."
            if (Stroomdiagrammen.rb.getString("decSep") == ",")
                t = t.replace(',', '.');
            // note:  "." is always allowed
            // check for double ..
            int k = 0;
            int j = t.indexOf('.');
            // only if there is a .
            if (j >= 0)
            {   k = t.lastIndexOf('.');
                if (j != k)
                {   error = true;
                    // reset
                    setFlow(flow);
                }
            }
            if (!error)
            {   // this line generates exception and activates catch
                value = (Double.valueOf(t)).doubleValue();
            }
        } // try
        catch (NumberFormatException nfe)
        {   error = true;
            // reset
            setFlow(flow);
        }  
        if (!error)
        {   if (value < 0)
                setFlow(flow); // reset
            else
            {   setDecimals(value);
                boolean remember = (value != flow.decVal);            
                setFlow(new Rational(value, decimals));
                ((DrawingPanel) getParent()).diagramManager.calculateDiagram();
                if (remember)
                    ((DrawingPanel) getParent()).addToHistory();                
                
            }
        }    
    }    
  
    public void processInput()
    {   boolean error = false;
        double value = 0;
        // get current text
        String t = flowField.getText();
        // undo wrapping
        flowField.setText(t);
        if (t.equals(""))
        {   boolean remember = !flow.isUndefined();
            setFlow(DrawingPanel.unDef);
            ((DrawingPanel) getParent()).diagramManager.calculateDiagram();
            if (remember)
                ((DrawingPanel) getParent()).addToHistory();
        }
        else
        {   if (Stroomdiagrammen.rb.getString("decSep") == ",")
                t = t.replace(',', '.');
            int mode = ((DrawingPanel) getParent()).flowMode;
            int divIndex = t.indexOf('/');
            int decIndex = t.indexOf('.');
            if ((divIndex < 0) && (decIndex < 0))
                processRational(t);
            else if (mode == DrawingPanel.fracMode)
            {   if (decIndex >= 0)
                    setFlow(flow); // reset
                else    
                    processRational(t);
            }    
            else if (mode == DrawingPanel.decMode)
            {   if (divIndex >= 0)
                    setFlow(flow); // reset
                else    
                    processDecimal(t);
            }    
        }    
    }    

    public String removeAllBlanks(String s)
    {   int index = s.indexOf(' ');
        while (index >= 0)
        {   s = s.substring(0, index) + s.substring(index + 1);
            index = s.indexOf(' ');
        }
        return s;
    }
    
    public void setDecimals(double value)
    {   int decs;
        if (value >= 100)
            decs = 0;
        else if ((value >= 10) && (value < 100))
            decs = 1;
        else if ((value > 1) && (value < 10))
            decs = 2;
        else
            decs = 3;
        // do not forget this one
        decimals = decs;
        ((DrawingPanel) getParent()).diagramManager.setDecimals(decs);                            
    }                
    
    // inner class for root numberfield (later all vertices??)
    // starts calculations on Enter
    class VertexIAL extends KeyAdapter implements FocusListener
    {   public void keyPressed(KeyEvent e)
        {   // Vertex v = (Vertex) e.getComponent().getParent();
//            boolean error = false;
//            double value = 0;
            // only action at Enter
            int kc = e.getKeyCode();
            if (kc == KeyEvent.VK_ENTER)
            {   processInput();
                
            }  // if (kc == VK_ENTER)  
             
        } // keyPressed   
        public void focusGained(FocusEvent e)
        {}
        public void focusLost(FocusEvent e)
        {   processInput();
        }
        
    } // inner class VertexIAL   
    
    // inner class vertex labels
    class VertexLabelIAL implements FocusListener
    {   
        public void focusGained(FocusEvent e)
        {}
        public void focusLost(FocusEvent e)
        {   if (vLabel.textValueChanged)
                //((DrawingPanel) getParent()).addToHistory();
                ((DrawingPanel) getParent()).updateHistoryLabels();
        }
        
    } // inner class VertexIAL       
    
    
}

class EdgeCopy implements Serializable
{   VertexCopy fromVertexCopy, toVertexCopy;
    Rational capacity;
    long lastTimeChanged;
    int mode;
    public EdgeCopy(Rational c, long t, int m)
    {   capacity = new Rational(c);
        lastTimeChanged = t;
        mode = m;
    }       
}    


class Edge 
{   DrawingPanel owner;
    Vertex fromVertex, toVertex;
    NumberField capacityField;
    Point edgeStart = new Point(), edgeEnd = new Point();
    // angle (edgeEnd.y - edgeStart.y) / (edgeEnd.x - edgeStart.x)
    double alpha;
    // required thickness (perpendicular to flow)
    // will be corrected later
    double thickness;
    // corrected thickness (pixels) perpendicular to flow
    int corrThickness;
    // offSet from edgeStart.y (pixels)
    int vOffSet;
    // vertical thickness (pixels)
    int vThickness;
    // edge polygon, click polygon
    Polygon p, cp;
    // highlighting
    boolean highlighted = false;
    // bubbles
    int bOffMaxInit = 30;
    int bOffMax = bOffMaxInit;
    int bOffStep = 5;
    // offset for bubbles
    int bOffSet = randomInteger(1, bOffMaxInit);
    
    public static int numWaveColors = 4;
    int waveStep = randomInteger(0, numWaveColors);
    
    // Date instance for fixing time
    Date date;
    //arrows om LWTextField langs pijl te schuiven??
    //hoe??
    //paint
    //teken pijl
    Rational capacity;
    long lastTimeChanged;
    int mode = DrawingPanel.decMode;
    int thickMode = DrawingPanel.relMode;
    public Edge(DrawingPanel o, Vertex from, Vertex to, Rational c)
    {   owner = o;
        fromVertex = from;
        toVertex = to;
        capacity = new Rational(c);
        capacityField = new NumberField(
            true, DrawingPanel.edgeNumberWidth, 
            DrawingPanel.edgeNumberHeight);
        capacityField.rounded = true;    
        capacityField.setText(UF.format(capacity.decVal, DrawingPanel.capDecs));    
        EdgeIAL kfListener = new EdgeIAL();
        capacityField.addKeyListener(kfListener);
        capacityField.addFocusListener(kfListener);
        capacityField.addMouseListener(new DelEdgeML());
        from.outEdges.addElement(this);
        to.inEdges.addElement(this);
        thickMode = owner.thickMode;
    }

    public void setCapacity(Rational c, boolean newTime)
    {   capacity = new Rational(c);
        if (mode == DrawingPanel.decMode)
            capacityField.setText(UF.format(capacity.decVal, DrawingPanel.capDecs));      
        else if (mode == DrawingPanel.percMode)    
            capacityField.setText(UF.format(capacity.decVal * 100, 0) + "%");              
        else  // mode = DrawingPanel.fracMode  
        {   if (capacity.isInteger())
                capacityField.setText(UF.format(capacity.nom, 0));        
            else
                capacityField.setText(UF.format(capacity.nom, 0) + "/" +
                                      UF.format(capacity.denom, 0));  
        }                          
        if (newTime)
        {   date = new Date();
            lastTimeChanged = date.getTime();
        }
        // thickness 
        setThickness();
        //thickness = DrawingPanel.vertexHeight * capacity.decVal;        
        owner.repaint();
        // no updates here, infinite loop!
    }    

    public void setMode(int m)
    {   mode = m;
        if (mode == DrawingPanel.decMode)
            capacityField.setText(UF.format(capacity.decVal, DrawingPanel.capDecs));      
        else if (mode == DrawingPanel.percMode)    
            capacityField.setText(UF.format(capacity.decVal * 100, 0) + "%");              
        else  // mode = DrawingPanel.fracMode  
        {   if (capacity.isInteger())
                capacityField.setText(UF.format(capacity.nom, 0));        
            else
                capacityField.setText(UF.format(capacity.nom, 0) + "/" +
                                      UF.format(capacity.denom, 0));  
        }                          
    }    
    
    // used when creating, redrawing
    public void setEdge()
    {   edgeStart.x = fromVertex.getLocation().x + owner.vertexWidth;
        edgeStart.y = fromVertex.getLocation().y + owner.labelHeight;
        edgeEnd.x = toVertex.getLocation().x;
        edgeEnd.y = toVertex.getLocation().y + owner.labelHeight;
        alpha = Math.atan(((double) (edgeEnd.y - edgeStart.y)) / (edgeEnd.x - edgeStart.x));
        // thickness
        setThickness();
        //thickness = DrawingPanel.vertexHeight * capacity.decVal;
    }    
    
    public void setThicknessMode(int tMode)
    {   thickMode = tMode;
        setThickness();
    }    
    
    // determine required thickness
    public void setThickness()
    {   // relative to capacity
        if (thickMode == DrawingPanel.relMode)
            thickness = DrawingPanel.vertexHeight * capacity.decVal;        
        else // absolute
        {   // max of flow in all roots
            Rational sFlow = owner.getMaxRootFlow();
            // if not all roots filled in
            // finds maximum of roots connected to fromVertex
            if (sFlow.isUndefined())
                sFlow = owner.getSourceFlow(fromVertex);
            if (sFlow.decVal <= 1e-6d)
                thickness = 0;
            else    
                thickness = DrawingPanel.vertexHeight * 
                            capacity.decVal *
                            (fromVertex.flow.decVal / sFlow.decVal);        
        }    
    }
    
    // generate a random integer between min and max
    public int randomInteger(int min, int max)
    {   double num = min + Math.random() * (max - min);
        // cast long to int
        return (int) Math.round(num);
    }
    
    public void drawEdge(Graphics g)
    {   // thickness of all relevant edges MUST have been set
        // before drawing    
        
        // vertical thickness is thickness / cos(alpha)
        // but these do not sum to DrawingPanel.vertexHeight anymore
        // so find totalThickness and rescale
        double dVOffSet = 0;
        double totalThickness = 0;
        boolean found = false;
        for (int i = 0; i < fromVertex.outEdges.size(); i++)
        {   Edge oe = (Edge) fromVertex.outEdges.elementAt(i);
            totalThickness += (oe.thickness / Math.cos(oe.alpha)); 
            if (oe == this)
                found = true;
            if (!found)
            {    dVOffSet += (oe.thickness / Math.cos(oe.alpha));
            }    
        }    
        // vertical offSet of this edge
        if ((thickMode == DrawingPanel.relMode) ||
            (totalThickness > DrawingPanel.vertexHeight))
            vOffSet = (int) Math.round(dVOffSet * 
                            DrawingPanel.vertexHeight / totalThickness);        
        else 
        {    vOffSet = (int) Math.round(dVOffSet + 
                             (DrawingPanel.vertexHeight - totalThickness) / 2);        
        }                    
        // horizontal offset of center of capacity field
        int hOffSet = Math.max(
            (edgeEnd.x - edgeStart.x) / 4,
            capacityField.getSize().width / 2); 
        int lTop = 0, rTop = 0;
        double tan = 0;
        if ((capacity.decVal == 0) || (thickness == 0))
        {   g.setColor(Stroomdiagrammen.zeroEdgeColor);                 
            // no vThickness
            lTop = edgeStart.y + vOffSet;
            // endPoint at edgeEnd.y + DrawingPanel.VertexHeight / 2
            rTop = edgeEnd.y + DrawingPanel.vertexHeight / 2;
            tan = ((double) (rTop - lTop)) / (edgeEnd.x - edgeStart.x);           
            // draw line
//            g.drawLine(edgeStart.x, lTop, edgeEnd.x, rTop);
            int nPoints = 4;
            int[] xPoints = new int[nPoints];
            int[] yPoints = new int[nPoints];
            int[] cyPoints = new int[nPoints];
            // clockwise: start at left bottom
            xPoints[0] = edgeStart.x;
            xPoints[1] = edgeStart.x;
            xPoints[2] = edgeEnd.x;
            xPoints[3] = edgeEnd.x;
            yPoints[0] = edgeStart.y + vOffSet + 1;
            yPoints[1] = edgeStart.y + vOffSet;            
            // middle of flow end in middle of 
            // to vertex
            yPoints[2] = edgeEnd.y + (DrawingPanel.vertexHeight - 1) / 2;
            yPoints[3] = edgeEnd.y + (DrawingPanel.vertexHeight + 1) / 2;            
            p = new Polygon(xPoints, yPoints, nPoints);
            g.fillPolygon(p);
            cyPoints[0] = yPoints[0] + 4;
            cyPoints[1] = yPoints[1] - 4;            
            cyPoints[2] = yPoints[2] - 4;
            cyPoints[3] = yPoints[3] + 4;            
            cp = new Polygon(xPoints, cyPoints, nPoints);            
            
//g.drawPolygon(cp);
            capacityField.setLocation(
                edgeStart.x + hOffSet -
                capacityField.getSize().width / 2, 
                lTop + (int) Math.round(hOffSet * tan) -
                capacityField.getSize().height / 2);
        }    
        else // positive capacity/thickness   
        {   if (highlighted)
                g.setColor(Stroomdiagrammen.highEdgeColor);                         
            else
                g.setColor(Stroomdiagrammen.edgeColor);                 
            if ((thickMode == DrawingPanel.relMode) ||
                (totalThickness > DrawingPanel.vertexHeight))
                vThickness = (int) Math.round((thickness / Math.cos(alpha)) * 
                                       DrawingPanel.vertexHeight / totalThickness);
            else
            {    vThickness = (int) Math.round((thickness / Math.cos(alpha)));// * 
            }
            corrThickness = (int) Math.round(vThickness * Math.cos(alpha));            
            lTop = edgeStart.y + vOffSet;
            rTop = edgeEnd.y + (DrawingPanel.vertexHeight - vThickness) / 2;        
            tan = ((double) (rTop - lTop)) / (edgeEnd.x - edgeStart.x);                       
            int nPoints = 4;
            int[] xPoints = new int[nPoints];
            int[] yPoints = new int[nPoints];
            int[] cyPoints = new int[nPoints];
            // clockwise: start at left bottom
            xPoints[0] = edgeStart.x;
            xPoints[1] = edgeStart.x;
            xPoints[2] = edgeEnd.x;
            xPoints[3] = edgeEnd.x;
            yPoints[0] = edgeStart.y + vOffSet + vThickness;
            yPoints[1] = edgeStart.y + vOffSet;            
            // middle of flow end in middle of 
            // to vertex
            yPoints[2] = edgeEnd.y + (DrawingPanel.vertexHeight - vThickness) / 2;
            yPoints[3] = edgeEnd.y + (DrawingPanel.vertexHeight + vThickness) / 2;            
            p = new Polygon(xPoints, yPoints, nPoints);
            g.fillPolygon(p);
            if (vThickness < 9)
            {   int shift = (9 - vThickness) / 2 + 1;
                cyPoints[0] = yPoints[0] + shift;
                cyPoints[1] = yPoints[1] - shift;            
                cyPoints[2] = yPoints[2] - shift;
                cyPoints[3] = yPoints[3] + shift;            
                cp = new Polygon(xPoints, cyPoints, nPoints);            
                
            }    
            else
                cp = new Polygon(xPoints, yPoints, nPoints);            
            capacityField.setLocation(
                edgeStart.x + hOffSet -
                capacityField.getSize().width / 2, 
                lTop + (int) Math.round(hOffSet * tan) + vThickness / 2 -
                capacityField.getSize().height / 2);
                

/*
            // simulation
            // distance between bubbles
            double frac = ((double) (edgeEnd.x - edgeStart.x)) / owner.maxLayerDistance;
            bOffMax = (int) Math.round(Math.min(1, frac) * bOffMaxInit);
            int steps = (int) Math.round(frac * 4);
            int step = (edgeEnd.x - edgeStart.x) / steps;
            int numBubbles = (int) Math.round(frac * 5);
//            for (int i = 0; i < numBubbles; i++)
//                drawBubbleAt(g, bOffSet + i * (step - 1));
*/

            
            drawWavesAt(g, waveStep);           
                       
            // "smaller" polygon for outline    
            xPoints[0]--;
            xPoints[1]--;
            yPoints[0]--;
            yPoints[3]--;
            Polygon q = new Polygon(xPoints, yPoints, nPoints);
            g.setColor(Color.black);
            g.drawPolygon(q);
        }
    }    

    public void drawBubbleAt(Graphics g, int step)
    {   if (highlighted)
            g.setColor(Stroomdiagrammen.highBubbleColor);                
        else
            g.setColor(Stroomdiagrammen.bubbleColor);            
        // left top of polygon
        int lTop = edgeStart.y + vOffSet;
        // right top of polygon
        // middle of flow end in middle of right vertex
        int rTop = edgeEnd.y + (DrawingPanel.vertexHeight - vThickness) / 2;        
        double tan = ((double) (rTop - lTop)) /
                     (edgeEnd.x - edgeStart.x);           
        int stepy = (int) Math.round(step * tan);
        int cx = edgeStart.x + step;            
        int cy = lTop + stepy + vThickness / 2;
        if (cx <= edgeEnd.x)
        {   if (highlighted)
                g.setColor(DrawingPanel.hsbChange(Stroomdiagrammen.highBubbleColor, 2));        
            else
                g.setColor(DrawingPanel.hsbChange(Stroomdiagrammen.bubbleColor, 2));
            g.fillArc(cx - corrThickness / 2 + 1, 
                      cy - corrThickness / 2 + 1,
                      corrThickness - 1, corrThickness - 1,
                      90, 90); 
            if (highlighted)
                g.setColor(DrawingPanel.hsbChange(Stroomdiagrammen.highBubbleColor, - 2));            
            else
                g.setColor(DrawingPanel.hsbChange(Stroomdiagrammen.bubbleColor, - 2));
            g.fillArc(cx - corrThickness / 2 + 1, 
                      cy - corrThickness / 2 + 1,
                      corrThickness - 1, corrThickness - 1,
                      270, 90); 
            if (highlighted)
                g.setColor(Stroomdiagrammen.highBubbleColor);                                  
            else
                g.setColor(Stroomdiagrammen.bubbleColor);                      
            g.fillArc(cx - corrThickness / 2 + 1, 
                      cy - corrThickness / 2 + 1,
                      corrThickness - 1, corrThickness - 1,
                      0, 90); 
            g.fillArc(cx - corrThickness / 2 + 1, 
                      cy - corrThickness / 2 + 1,
                      corrThickness - 1, corrThickness - 1,                      
                      180, 90); 
            g.fillOval(cx - corrThickness / 4 + 1, 
                      cy - corrThickness / 4 + 1,
                      (corrThickness - 1) / 2, (corrThickness - 1) / 2);                      
                      
        }               
    }

    public void drawWavesAt(Graphics g, int step)
    {   if (vThickness < 1)
            return; // no waves

        // left top of polygon
        int lTop = edgeStart.y + vOffSet;
        // right top of polygon
        int rTop = edgeEnd.y + (DrawingPanel.vertexHeight - vThickness) / 2;        
        double tan = ((double) (rTop - lTop)) /
                     (edgeEnd.x - edgeStart.x);           
        double atan = Math.atan(tan);             
        double absAtan = Math.abs(tan);
        
        double waveXThickness = 5;
        double waveYThickness = Math.abs(tan) * waveXThickness;
        int numWaves = (int) Math.round( ((double) (edgeEnd.x - edgeStart.x)) / waveXThickness) + 1;
        Vector waves = new Vector();        
        int nPoints;
        int[] xPoints;
        int[] yPoints;
        Polygon w;
        for (int cnt = 0; cnt < numWaves; cnt++)
        {   nPoints = 4;
            xPoints = new int[4];
            yPoints = new int[4];
            if (atan >= 0)
            {    
                xPoints[0] = edgeStart.x + (int) Math.round(
                             cnt * waveXThickness
                             );
                xPoints[1] = edgeStart.x + (int) Math.round(
                             (cnt + 1) * waveXThickness
                             );
                xPoints[2] = edgeStart.x + (int) Math.round(
                             (cnt + 1) * waveXThickness - 
                             vThickness * Math.cos(atan) * Math.sin(atan)
                             );
                xPoints[3] = edgeStart.x + (int) Math.round(
                             cnt * waveXThickness -
                             vThickness * Math.cos(atan) * Math.sin(atan)                             
                             );
                
                yPoints[0] = lTop + (int) Math.round(
                             cnt * waveYThickness
                             );
                yPoints[1] = lTop + (int) Math.round(
                             (cnt + 1) * waveYThickness
                             );
                yPoints[2] = lTop + (int) Math.round(
                             (cnt + 1) * waveYThickness + 
                             vThickness * Math.cos(atan) * Math.cos(atan)                             
                             );
                yPoints[3] = lTop + (int) Math.round(
                             cnt * waveYThickness +
                             vThickness * Math.cos(atan) * Math.cos(atan)
                             );
            }
            else //if (atan < 0
            {
                xPoints[0] = edgeStart.x + (int) Math.round(
                             cnt * waveXThickness
                             );
                xPoints[1] = edgeStart.x + (int) Math.round(
                             cnt * waveXThickness +   
                             vThickness * Math.cos(- atan) * Math.sin(- atan)
                             );
                xPoints[3] = edgeStart.x + (int) Math.round(
                             (cnt - 1) * waveXThickness
                             );
                xPoints[2] = edgeStart.x + (int) Math.round(
                             (cnt - 1) * waveXThickness +
                             vThickness * Math.cos(- atan) * Math.sin( - atan)                             
                             );              
                
                
                yPoints[0] = lTop - (int) Math.round(
                             cnt * waveYThickness
                             );
                yPoints[1] = lTop - (int) Math.round(
                             cnt * waveYThickness - 
                             vThickness * Math.cos(atan) * Math.cos(atan)                                                          
                             );
                             

                yPoints[3] = lTop - (int) Math.round(
                             (cnt - 1) * waveYThickness
                             );
                yPoints[2] = lTop - (int) Math.round(
                             (cnt - 1) * waveYThickness -
                             vThickness * Math.cos(atan) * Math.cos(atan)                                                          
                             );
                
            }
            
            w = new Polygon(xPoints, yPoints, nPoints);
            waves.addElement(w);
        }
        Color[] colors = new Color[numWaveColors];
        if (highlighted)
        {
        colors[0] = DrawingPanel.hsbChange(Stroomdiagrammen.highBubbleColor, 1);
        colors[1] = DrawingPanel.hsbChange(Stroomdiagrammen.highBubbleColor, 2);
        colors[2] = DrawingPanel.hsbChange(Stroomdiagrammen.highBubbleColor, 1);
        colors[3] = Stroomdiagrammen.highBubbleColor;        
            
        }
        else
        {
        colors[0] = DrawingPanel.hsbChange(Stroomdiagrammen.bubbleColor, 1);
        colors[1] = DrawingPanel.hsbChange(Stroomdiagrammen.bubbleColor, 2);
        colors[2] = DrawingPanel.hsbChange(Stroomdiagrammen.bubbleColor, 1);
        colors[3] = Stroomdiagrammen.bubbleColor;        
        }
        for (int i = 0; i < waves.size(); i++)
        {   g.setColor(colors[(i + step) % 4]);
            
            Polygon pw = (Polygon) waves.elementAt(i);    
            g.fillPolygon(pw);    
        }    
        
        
    }
    
    public void processDouble(String t, boolean percentage)
    {   boolean error = false;
        double value = 0;
        // error handling here
        try
        {   if (percentage)
            {   int pc = t.indexOf('%');
                if (pc >= 0)
                    t = t.substring(0, pc);
            }
            // change decimal separator to "."
            if (Stroomdiagrammen.rb.getString("decSep") == ",")
                t = t.replace(',', '.');
            // note:  "." is always allowed
            // check for double ..
            int k = 0;
            int j = t.indexOf('.');
            // only if there is a .
            if (j >= 0)
            {   k = t.lastIndexOf('.');
                if (j != k)
                {   error = true;
                    // reset
                    setCapacity(capacity, false); 
                }
            } // if (j >= 0)
            if (!error)
            {   // this line generates exception and activates catch
                value = (Double.valueOf(t)).doubleValue();
            }                        
        } // try
        catch (NumberFormatException nfe)
        {   error = true;
            // reset
            setCapacity(capacity, false); 
        }  // catch
        if (!error)
        {   if (percentage)
                value /= 100;
            if ((value >= 0) && (value <= 1))
            {   
                int newMode;
                if (percentage)
                    newMode = DrawingPanel.percMode;
                else
                    newMode = DrawingPanel.decMode;
                boolean remember = (value != capacity.decVal) || (mode != newMode);
                setCapacity(new Rational(value), true);
                fromVertex.updateOutCapacities(Edge.this, newMode);
                // includes this one
                fromVertex.setOutModes(newMode);
                ((DrawingPanel) capacityField.getParent()).diagramManager.calculateDiagram();                        
                if (remember)
                    ((DrawingPanel) capacityField.getParent()).addToHistory();
            }
            else // reset
                setCapacity(capacity, false);
        }    
    }    
    
    public void processRational(String t)
    {   boolean error = false;
        int nom = 0, denom = 1;
        String nomStr = null, denomStr = null;
        Rational value;
        // error handling here
        try
        {   t = removeAllBlanks(t);
            int slash = t.indexOf('/');
//            if (slash >= 0)
//            {   
                nomStr = t.substring(0, slash);
                if (slash == (t.length() - 1))
                {   error = true;
                    // reset
                    setCapacity(capacity, false); 
                }
                else
                    denomStr = t.substring(slash + 1);
//            }
//            else
//            {   nomStr = t; 
//                denomStr = "1";
//            }
            if (!error)    
            {   // these lines generate exceptions and activate catch
                nom = Integer.parseInt(nomStr);
                denom = Integer.parseInt(denomStr);
            }    
        } // try
        catch (NumberFormatException nfe)
        {   error = true;
            // reset
            setCapacity(capacity, false); 
        }  // catch
        if (!error)
        {   value = new Rational(nom, denom);
            int newMode = DrawingPanel.fracMode;
            if (value.isLargerOrEqual(new Rational(0, 1, 0), newMode) && 
                value.isSmallerOrEqual(new Rational(1, 1, 1), newMode))
            {   boolean remember = !capacity.equals(value) || (mode != newMode);
                setCapacity(value, true);
                fromVertex.updateOutCapacities(Edge.this, newMode);
                // includes this one
                fromVertex.setOutModes(newMode);                
                ((DrawingPanel) capacityField.getParent()).diagramManager.calculateDiagram();                        
                if (remember)
                    ((DrawingPanel) capacityField.getParent()).addToHistory();
                
            }
            else // reset
                setCapacity(capacity, false);
        }    
    }    
    
    public void processInput()
    {   // get current text
        String t = capacityField.getText();
        // undo wrapping
        capacityField.setText(t);
        int divIndex = t.indexOf('/');
        if (divIndex >= 0)
            processRational(t);
        else
        {   int percIndex = t.indexOf('%');
            if (percIndex >= 0)
                processDouble(t, true);   
            else
                processDouble(t, false);
        }    
//        if ((mode == DrawingPanel.decMode) ||
//            (mode == DrawingPanel.percMode))
//             processDouble(t);
//        else // mode == DrawingPanel.fracMode    
//            processRational(t);
    }    
    public String removeAllBlanks(String s)
    {   int index = s.indexOf(' ');
        while (index >= 0)
        {   s = s.substring(0, index) + s.substring(index + 1);
            index = s.indexOf(' ');
        }
        return s;
    }



    class DelEdgeML extends MouseAdapter
    {   public void mousePressed(MouseEvent e)
        {   DrawingPanel dp = (DrawingPanel) e.getComponent().getParent();
            // put capacity field on top
//            if (!dp.deleteMode)
//            {   
                dp.remove(capacityField);
                dp.add(capacityField, 0);
                
//            }  
//            else // deleteMode on
//            {   // see if this edge can be deleted
//                //dp.diagramManager.deleteEdge(Edge.this, true);
//                //dp.owner.unDelete();
//            }
        }    
    }    
    
    
    
    
    class EdgeIAL extends KeyAdapter implements FocusListener
    {   public void keyPressed(KeyEvent e)
        {   // only action at Enter
            int kc = e.getKeyCode();
            if (kc == KeyEvent.VK_ENTER)
            {   processInput();
/*                
                // get current text
                String t = capacityField.getText();
                // undo wrapping
                capacityField.setText(t);
                if ((mode == DrawingPanel.decMode) ||
                    (mode == DrawingPanel.percMode))
                    processDouble(t);
                else // mode == DrawingPanel.fracMode    
                    processRational(t);
*/                    
            }  // if (kc == VK_ENTER)  
        } // keyPressed   
        public void focusGained(FocusEvent e)
        {}
        public void focusLost(FocusEvent e)
        {   processInput();
        }
        
    } // inner class InputAL   
} // class Edge   

class DiagramManager
{   DrawingPanel owner;
    Vector[] vertexLayers = new Vector[owner.maxLayers];
    Vector edges = new Vector();
    // constructor    
    public DiagramManager(DrawingPanel o)
    {   owner = o;
        for (int i = 0; i < vertexLayers.length; i++)
            vertexLayers[i] = new Vector();
    }    
    // this procedure produces a tree    
    public void insertVertex(Vertex v, Vertex o)
    {   boolean numLayersChanged = false;
        // adding a root 
        if (o == null)
        {   vertexLayers[v.layerNum].addElement(v);                        
        }    
        else // some other vertex
        // note: here it is assumed the vertices in both the o-layer
        // and the v-layer (the next one) are sorted by
        // vertical position
        // note also: outEdges of o are supposed to be sorted
        // by y-location of toVertex
// optimize for minimal number of crossings, how??
// als o "onderaan" gaat het al goed
// als o "bovenaan" vertex "bovenaan" inserten
// bekijk uitgaande vertices vanaf o (die als enige in een vertex komen??)
// en kijk of je voor de eerste of na de laatste kunt
// inserten zonder crossings te maken
// als dat niet lukt: ertussen??
// methode om aan edgeLayer crossings te vragen??
        {   // number of vertices in layer where v should be inserted            
            int vIndex = vertexLayers[v.layerNum].size();
            if (vIndex == 0)
            {   if (v.layerNum > owner.numLayers)
                {   owner.numLayers++;
                    owner.setLayerDistance();
                    numLayersChanged = true;
                }    
            }    
            // start at bottom of layer where v should be
            // inserted
            boolean allCrosses = true;
            for (int k = vertexLayers[v.layerNum].size() - 1; k >= 0; k--)
            {   Vertex tv = (Vertex) vertexLayers[v.layerNum].elementAt(k);
                int endy = tv.getLocation().y + 
                           (owner.vertexHeight + owner.labelHeight) / 2;
                if (allCrosses)
                {   if (createsCrossing(v.layerNum, o, endy))
                       vIndex--;
                    else
                    {    allCrosses = false;
                    }
                }    
            }
            // start at vIndex = layer size
            // if crosses at each step vIndex ends at 0
            // try top
            if ((vIndex == 0) && (vertexLayers[v.layerNum].size() > 0))
            {   Vertex topv = (Vertex) vertexLayers[v.layerNum].elementAt(0);
                int topendy = topv.getLocation().y - 
                              (owner.labelHeight + owner.vertexHeight) / 2;
                if (!createsCrossing(v.layerNum, o, topendy))
                {   allCrosses = false;
                    vIndex = 0;
                }
            }
            
            if (allCrosses) // take old algo
            {
                int oIndex = vertexLayers[o.layerNum].indexOf(o);
                vIndex = 0; // reset
                Vector targetVertices = new Vector();             
                for (int i = 0; i <= oIndex; i++)
                {   Vertex w = (Vertex) vertexLayers[o.layerNum].elementAt(i);
                    for (int j = 0; j < w.outEdges.size(); j++) 
                    {   Edge te = (Edge) w.outEdges.elementAt(j);
                        Vertex tw = te.toVertex;
                        if (tw.layerNum == v.layerNum)
                        {   int twIndex = vertexLayers[tw.layerNum].indexOf(tw);
                            if (!targetVertices.contains(tw)
                                && (twIndex == vIndex))
                            {   if (i < oIndex)
                                {   targetVertices.addElement(tw);
                                    vIndex++;
                                }
                                else
                                {    if (tw.inEdges.size() == 1)
                                         vIndex++;
                                }    
                            }    
                        }    
                    }    
                } // for   
            } // if (allCrosses)
            if (vertexLayers[v.layerNum].size() > 0)            
            {   if (vIndex == 0) // top
                {   Vertex ov = (Vertex) vertexLayers[v.layerNum].elementAt(vIndex);                                   
                    v.setLocation(v.getLocation().x, 
                        Math.max(owner.workSpace.y + owner.topSpace,
                                 ov.getLocation().y -
                                 (ov.getSize().height + owner.minSpace)));
                }
                else 
                {   Vertex ov = (Vertex) vertexLayers[v.layerNum].elementAt(vIndex - 1);                                   
                    v.setLocation(v.getLocation().x, 
                                  ov.getLocation().y +
                                  ov.getSize().height + owner.minSpace);
                }    
            }    
            vertexLayers[v.layerNum].insertElementAt(v, vIndex);                        
        } // else for non roots   
        owner.add(v);
        // roots are nicely spaced
        if (o == null)
            updateVertexLayer(v.layerNum);
        else if (vertexLayers[v.layerNum].size() > 1)
            updateVertexLayer(v.layerNum);
        else // first vertex in layer
            v.setLocation(owner.getLayerStart(v.layerNum), o.getLocation().y);
        updateEdges();
        if (numLayersChanged)
            resizeDiagram(false);    
    }   
    
    public void moveVertexTo(Vertex v, int layerNum)
    {   v.setLocation(owner.workSpace.x + owner.leftSpace +
                      layerNum * 
                      (owner.vertexWidth + owner.layerDistance),
                      v.getLocation().y);
        vertexLayers[v.layerNum].removeElement(v);
        v.layerNum = layerNum;
        // maintain sorting
        int index = 0;
        for (int i = 0; i < vertexLayers[v.layerNum].size(); i++)
        {   Vertex av = (Vertex) vertexLayers[v.layerNum].elementAt(i);
            if (av.getLocation().y < v.getLocation().y)
                index++;
        }    
        vertexLayers[v.layerNum].insertElementAt(v, index);
        if (v.layerNum > owner.numLayers)
            owner.numLayers = v.layerNum;
        for (int k = 0; k < v.inEdges.size(); k++)
        {   Edge ie = (Edge) v.inEdges.elementAt(k);
            ie.fromVertex.sortOutEdges();
        }    
        
            
// do not update vertex layers
// edges are updated in mouseReleased
    }    
    
    // vertical "spreading" / "cascading"
    // vertices are put in the same vertical order as in
    // vertexLayers[layerNum]
    public void updateVertexLayer(int layerNum)
    {   // horizontal position
        int horPos = owner.workSpace.x + owner.leftSpace +
                     layerNum * (owner.vertexWidth + owner.layerDistance);
        // vertical positioning
        int spacing = owner.workSpace.height - (owner.topSpace + owner.bottomSpace) -
                      vertexLayers[layerNum].size() * 
                      (owner.vertexHeight + owner.labelHeight);

//System.out.println("sp = " + spacing); 
//System.out.println("wsh = " + owner.workSpace.height);
        
        if (spacing >= 0)              
            spacing /= vertexLayers[layerNum].size() + 1;             
        else
            spacing /= vertexLayers[layerNum].size() - 1;                     
        // set positions
        for (int i = 0; i < vertexLayers[layerNum].size(); i++)
        {   Vertex w = (Vertex) vertexLayers[layerNum].elementAt(i);
            if (spacing >= 0)
                w.setLocation(horPos, owner.workSpace.y + owner.topSpace + spacing +
                                      i * (owner.vertexHeight + owner.labelHeight + spacing));  
            else 
                w.setLocation(horPos, owner.workSpace.y + owner.topSpace +
                                      i * (owner.vertexHeight + owner.labelHeight + spacing));  
        }    
        owner.repaint();
    }  // updateVertexLayer

/*
// dit is niet zo mooi als de oude methode
// i.h.b. als weinig vertices per laag
    // version avoiding overlap, if unavoidable
    // vertical "cascading"
    // vertices are put in the same vertical order as in
    // vertexLayers[layerNum]
    public void updateVertexLayer2(int layerNum)
    {   // horizontal position
        int horPos = owner.workSpace.x + owner.leftSpace +
                     layerNum * (owner.vertexWidth + owner.layerDistance);
        // vertical positioning
        int spacing = owner.workSpace.height - 
                      (owner.topSpace + owner.bottomSpace) -
                      vertexLayers[layerNum].size() * 
                      (owner.vertexHeight + owner.labelHeight) -
                      Math.max(0, vertexLayers[layerNum].size() - 1) *
                      owner.minSpace;
        if (spacing >= 0) // nice spacing possible             
            correctOverlap(layerNum);
        else // use old procedure
            updateVertexLayer(layerNum);
        // set only horizontal positions
        for (int i = 0; i < vertexLayers[layerNum].size(); i++)
        {   Vertex w = (Vertex) vertexLayers[layerNum].elementAt(i);
            w.setLocation(horPos, w.getLocation().y);
        }    
        owner.repaint();
    }  // updateVertexLayer2
*/    
    public void correctOverlap(int layerNum)
    {   Vertex v = firstOverlap(layerNum);
// hier oneindige loop, waarom    
//        while (v != null)
//        {   
            int index = vertexLayers[layerNum].indexOf(v);
            Vertex above = (Vertex) vertexLayers[layerNum].elementAt(index - 1);
            // shift v downward
            v.setLocation(v.getLocation().x,
                          above.getLocation().y +
                          above.getSize().height + owner.minSpace);
            v = firstOverlap(layerNum);              
//        }    
            
    }
    
    public Vertex firstOverlap(int layerNum)
    {   if (vertexLayers[layerNum].size() == 1)
            return null;
        Vertex result = null;    
        for (int i = 0; i < vertexLayers[layerNum].size() - 1; i++)
        {   Vertex v1 = (Vertex) vertexLayers[layerNum].elementAt(i);
            Vertex v2 = (Vertex) vertexLayers[layerNum].elementAt(i + 1);
            if ((v1.getLocation().y + 
                 v1.getSize().height + owner.minSpace) >=
                 v2.getLocation().y)
                return v2; // exit for-loop and method
        }    
        return result;
    }
    public void fuseVertices(Vertex v1, Vertex v2)
    {   // v1 will disappear, v2 gets all the edges
        // incoming edges
        for (int i = 0; i < v1.inEdges.size(); i++)
        {   // edge to relocate/fuse
            Edge ie1 = (Edge) v1.inEdges.elementAt(i);
            // check if v2 has an edge starting at the same
            // vertex ie1.fromVertex
            Edge ie2 = v2.hasInEdgeFrom(ie1.fromVertex);
            if (ie2 != null)
            {   // remove ie1 from ie1.fromVertex
                ie1.fromVertex.outEdges.removeElement(ie1);
                // sum the capacities
                Rational temp = ie1.capacity.plus(ie2.capacity);
                ie2.setCapacity(temp, false);
                edges.removeElement(ie1);
//                edgeLayers[v1.layerNum - 1].removeElement(ie1);
                owner.remove(ie1.capacityField);
            }    
            else
            {   ie1.toVertex = v2;
                v2.inEdges.addElement(ie1);
            }    
        }    
        // outgoing edges
        // remember old number of v2 outEdges
        int oldV2Out = v2.outEdges.size();
        for (int j = 0; j < v1.outEdges.size(); j++)
        {   // edge to relocate/fuse
            Edge oe1 = (Edge) v1.outEdges.elementAt(j);
            // check if v2 has an edge ending at the same
            // vertex oe1.toVertex
            Edge oe2 = v2.hasOutEdgeTo(oe1.toVertex);
            if (oe2 != null)
            {   // remove oe1 from oe1.toVertex
                oe1.toVertex.inEdges.removeElement(oe1);
                Rational temp = oe1.capacity.plus(oe2.capacity);
                oe2.setCapacity(temp, false);
                edges.removeElement(oe1);
//                edgeLayers[v1.layerNum].removeElement(oe1);
                owner.remove(oe1.capacityField);                
            }    
            else 
            {   oe1.fromVertex = v2;
                v2.outEdges.addElement(oe1);
                // for writing???
                oe1.setCapacity(oe1.capacity, false);
            }    
        }    
        // if v1 had any outgoing edges and v2 had any outgoing
        // edges BEFORE the fusion
        // "new" capacities add up to 2 so divide
        if ((v1.outEdges.size() > 0) && (oldV2Out > 0))
            for (int k = 0; k < v2.outEdges.size(); k++)
            {   Edge e2 = (Edge) v2.outEdges.elementAt(k);
                Rational temp = new Rational(1, 2, 5e-1d);
                e2.setCapacity(e2.capacity.times(temp), false);
            }    
        // remove from vector    
        vertexLayers[v1.layerNum].removeElement(v1);
        // remove from screen
        owner.remove(v1);
        // fusing roots
        if (owner.roots.contains(v1))
            owner.roots.removeElement(v1);
// do not update vertex layer
//        updateVertexLayer(v1.layerNum);
        // edgeLayers are updated in mouseReleased
        calculateDiagram();
        // bubblesort on v2 outEdges on y-locations
        v2.sortOutEdges();
        for (int k = 0; k < v2.inEdges.size(); k++)
        {   Edge ie = (Edge) v2.inEdges.elementAt(k);
            ie.fromVertex.sortOutEdges();
        }    
        if (owner.traceFrom != null)
        {   if (owner.traceFrom == v1)
                owner.traceFrom = v2;
            lowLightEdges();
            owner.traceBack(owner.traceFrom);
        }    
    }
    
    // find a vertex "close enough" to v (in same layer)
    public Vertex fuseWith(Vertex v)
    {   Vertex result = null;
    // zoek in alle vertices    
        for (int j = 0; j < vertexLayers.length; j++)
        {   for (int i = 0; i < vertexLayers[j].size(); i++)
            {   Vertex av = (Vertex) vertexLayers[j].elementAt(i);
                if ((v != av) && 
                    (Math.abs(v.getLocation().x - av.getLocation().x) <=
                     owner.vertexWidth / 5) &&  
                    (Math.abs(v.getLocation().y - av.getLocation().y) <= 
                     (owner.vertexHeight + owner.labelHeight) / 5))
                    result = av;     
            }
        }        
        return result;
    }    
  
    public boolean vertexLabelsChanged()
    {   boolean result = false;
        // zoek in alle vertices    
        for (int j = 0; j < vertexLayers.length; j++)
        {   for (int i = 0; i < vertexLayers[j].size(); i++)
            {   Vertex av = (Vertex) vertexLayers[j].elementAt(i);
                result = result || av.vLabel.textValueChanged;
            }
        }    
        return result;
    }
    
    // check if v intersects any other vertex
    public boolean intersectsVertex(Vertex v)
    {   boolean result = false;
        Rectangle vRec = v.getBounds();
        for (int j = 0; j < vertexLayers.length; j++)
        {   for (int i = 0; i < vertexLayers[j].size(); i++)
            {   Vertex av = (Vertex) vertexLayers[j].elementAt(i);
                Rectangle avRec = av.getBounds();
                if (v != av)
                    result = result || vRec.intersects(avRec);
            }
        }        
            return result;
    }    
    
    // delete vertex v under circumstances
    public void deleteVertex(Vertex v)
    {   // a vertex is deletable if
        // it is a leaf i.e. no outEdges
// dit later        
        // if it has outedges a minimal subgraph has to be removed
        // so that connectivity is maintained
        boolean deletable = (v.outEdges.size() == 0);
        boolean numLayersChanged = false;
        if (deletable)
        {   int vIndex = vertexLayers[v.layerNum].size();
            // last vertex in layer
            // must be last layer (connectivity)
            if (vIndex == 1)
            {   owner.numLayers--;
                owner.setLayerDistance();
                numLayersChanged = true;
            }   
//            while (v.inEdges.size() > 0)
//            {   Edge ie = (Edge) v.inEdges.elementAt(0);
//                deleteEdge(ie, false);
//            }    
            // skipped at the moment
//            while (v.outEdges.size() > 0)
//            {   Edge oe = (Edge) v.outEdges.elementAt(0);
//                deleteEdge(oe, false);
//            }    
            owner.remove(v);
            vertexLayers[v.layerNum].removeElement(v);
            if (numLayersChanged)
                resizeDiagram(false);
            if (owner.traceFrom == v)
            {   lowLightEdges();
                owner.traceFrom = null;
            }
        }
    }    
    // add edge e
    public void addEdge(Edge e)
    {   // add to correct layer
        edges.addElement(e);
        e.fromVertex.sortOutEdges();
        updateEdges();        
        owner.add(e.capacityField);
        owner.repaint();
    }    
    // delete edge e under circumstances
    public void deleteEdge(Edge e)
    {   // deleting is allowed when:
        // e.toVertex has more than one inEdge
        // e.toVertex has one inEdge (must be e) and has no outEdges
        // in the last case remove the vertex also
        boolean toVertexIsLeaf = (e.toVertex.inEdges.size() == 1) &&
                                 (e.toVertex.outEdges.size() == 0);       
        // edge is deletable
        boolean deletable = (e.toVertex.inEdges.size() > 1) ||
                            toVertexIsLeaf;
        if (deletable)
        {   e.fromVertex.outEdges.removeElement(e);
            Edge oce = e.fromVertex.oldestOutChanged();
            if (oce != null)
                oce.setCapacity(e.capacity.plus(oce.capacity), true);
            // else e was the only outEdge of fromVertex    
            e.toVertex.inEdges.removeElement(e);
            owner.remove(e.capacityField);
            edges.removeElement(e);
            if (toVertexIsLeaf)
                deleteVertex(e.toVertex);
            else    
                owner.repaint();
            if (owner.traceFrom != null)
            {   lowLightEdges();
                owner.traceBack(owner.traceFrom);
            }    
            calculateDiagram();
        }
    }    
    
    // recalculates the edges positions from the vertex info
    public void updateEdges()
    {   for (int i = 0; i < edges.size(); i++)
        {   Edge e = (Edge) edges.elementAt(i);
            e.setEdge();
        } // for
        owner.repaint();

    }    
    
    public Edge getClickedEdge(int x, int y)
    {   Edge result = null;
        for (int i = 0; i < edges.size(); i++)
        {   Edge e = (Edge) edges.elementAt(i);
            if (e.cp.contains(x, y))
                result = e;
        } // for
        return result;
    }    
    public void setVertexLabels(boolean b)
    {   if (b)
            owner.labelHeight = owner.LABELHEIGHT;
        else
            owner.labelHeight = 0;
        for (int i = 0; i < vertexLayers.length; i++)
                for (int j = vertexLayers[i].size() - 1; j >= 0; j--)
            {   Vertex v = (Vertex) vertexLayers[i].elementAt(j);
                v.setLabel(b);                
            }
        redrawDiagram();    
        owner.repaint();       
        //owner.addToHistory();
    }    
    public boolean createsCrossing(int toLayerNum, Vertex start, int endy)
    {   boolean cross = false;
        Point end = new Point(owner.getLayerStart(toLayerNum), endy);
    
/*        
         for (int i = 0; i < vertexLayers[toLayerNum - 1].size(); i++)
         {   Vertex v = (Vertex) vertexLayers[toLayerNum - 1].elementAt(i);
             for (int j = 0; j < v.outEdges.size(); j++)
             {   Edge e = (Edge) v.outEdges.elementAt(j);
*/                
                
         for (int i = 0; i < edges.size(); i++)
         {   Edge e = (Edge) edges.elementAt(i);
             Vertex v = e.fromVertex;          
             if (v != start)
                 cross = cross || intersects(e, start, end);                   
         }                    
//         }
        return cross;
    }    
    // check if edge e intersects the segment starting at
    // topright of vertex st and ending in point end
    public boolean intersects(Edge e, Vertex st, Point end)
    {   Point start = new Point(st.getLocation().x + owner.vertexWidth, 
                                st.getLocation().y);
        Point temp;
        // left to right, not necessary??
        if (start.x > end.x)
        {   temp = start;
            start = end;
            end = temp;
        }    
        if ((start.x > e.edgeEnd.x) || (e.edgeStart.x > end.x))
            return false;
        double angle1 = ((double) (e.edgeEnd.y - e.edgeStart.y)) /
                        (e.edgeEnd.x - e.edgeStart.x);          
        double angle2 = ((double) (end.y - start.y)) /
                        (end.x - start.x);                                            
        double ic1 = e.edgeStart.y - angle1 * e.edgeStart.x;
        double ic2 = start.y - angle2 * start.x;
        if ((Math.abs(angle1 - angle2) < 1e-6d) &&
            (Math.abs(ic1 - ic2) < 1e-6d))
            return true;
        else if (Math.abs(angle1 - angle2) < 1e-6d)
            return false;
        else
        {   double xi = (ic2 - ic1) / (angle1 - angle2);
            int ii = (int) Math.round(xi);
            return (ii > start.x) && (ii < end.x); 
        }
    }
    
    public void drawEdges(Graphics g)
    {    for (int j = 0; j < edges.size(); j++)
         {   Edge e = (Edge) edges.elementAt(j);
             e.drawEdge(g);
         }    
    }    
    
    public void lowLightEdges()
    {    for (int j = 0; j < edges.size(); j++)
         {   Edge e = (Edge) edges.elementAt(j);
             e.highlighted = false;
         }    
    }    

    public void moveBubbles()
    {   for (int j = 0; j < edges.size(); j++)
        {   Edge e = (Edge) edges.elementAt(j);
            int tStep = e.waveStep - 1;
            if (tStep < 0)
                tStep += Edge.numWaveColors;
            e.waveStep = tStep;
/*            
            e.bOffSet += e.bOffStep;
            if (e.bOffSet > e.bOffMax)
                e.bOffSet = 1;
*/                
        }    
        owner.repaint();
    }    
    
    public void setFlowMode(int mode)
    {   boolean remember = (owner.flowMode != mode);
        owner.flowMode = mode; 
        calculateDiagram();
        for (int i = 0; i < vertexLayers.length; i++)
            for (int j = vertexLayers[i].size() - 1; j >= 0; j--)
            {   Vertex v = (Vertex) vertexLayers[i].elementAt(j);
                v.setFlow(v.flow);
            }
        if (remember)
            owner.addToHistory();
    }    
    
    // use this also for updating edge thickness
    // in abslute mode
    public void setEdgeThicknessMode(int tMode)
    {   boolean remember = (owner.thickMode != tMode);
        owner.thickMode = tMode; 
        for (int j = 0; j < edges.size(); j++)
        {   Edge e = (Edge) edges.elementAt(j);
            e.setThicknessMode(tMode);
        }   
        owner.repaint();        
        if (remember)
            owner.addToHistory();
    }    
    
    public void clearDiagram(boolean newRoot)
    {   
        // kill flow
        owner.flowOn = false;
        if (owner.flowThread != null)
            owner.flowThread.stop();
        owner.owner.bPanel.bubbleButton.setLabel(Stroomdiagrammen.rb.getString("flowOnText"));        
        for (int j = edges.size() - 1; j >= 0; j--)
        {   Edge e = (Edge) edges.elementAt(j);
            edges.removeElement(e);
            owner.remove(e.capacityField);
        }    
        for (int i = 0; i < vertexLayers.length; i++)
            for (int j = vertexLayers[i].size() - 1; j >= 0; j--)
            {   Vertex v = (Vertex) vertexLayers[i].elementAt(j);
                vertexLayers[i].removeElement(v);
                owner.remove(v);
            }
        owner.traceFrom = null;    
        owner.roots.removeAllElements();    
//        owner.root.outEdges.removeAllElements();    
//        owner.root.setFlow(owner.unDef);    
//        owner.roots.addElement(owner.root);
//        insertVertex(owner.root, null);
        owner.layerDistance = owner.maxLayerDistance;
        owner.numLayers = 0;
        if (newRoot)
        {   // clear history
            owner.history.removeAllElements();
            owner.owner.bPanel.previousButton.setEnabled(false);
            owner.addNewRoot();
        }
        owner.repaint();    
    }    
    
    // root treated in listener
    public void setDecimals(int decs)
    {   owner.vDecimals = decs;
        for (int i = 1; i < vertexLayers.length; i++)
             for (int j = 0; j < vertexLayers[i].size(); j++)
             {   Vertex v = (Vertex) vertexLayers[i].elementAt(j);
                 v.decimals = decs;
             }   
    }    
    
    public void calculateDiagram()
    {   for (int i = 1; i < vertexLayers.length; i++)
             for (int j = 0; j < vertexLayers[i].size(); j++)
             {   Vertex v = (Vertex) vertexLayers[i].elementAt(j);
                 v.calculateFlow();
             }   
        if (owner.thickMode == owner.absMode)
            setEdgeThicknessMode(owner.absMode);
        owner.repaint();     
    }    
    
    public Vector getVertexRefs()
    {   Vector refs = new Vector();
        for (int i = 0; i < vertexLayers.length; i++)
             for (int j = 0; j < vertexLayers[i].size(); j++)
             {   Vertex v = (Vertex) vertexLayers[i].elementAt(j);
                 refs.addElement(v);
             }   
        return refs;     
    }    
    
    public void redrawDiagram()
    {   for (int i = 0; i < vertexLayers.length; i++)
            updateVertexLayer(i);
        updateEdges();    
        owner.repaint();    
    }    
    

    // vertices are put in the same vertical order as in
    // vertexLayers[layerNum]
    public void resizeVertexLayer(int layerNum, boolean vertical)
    {   // horizontal position
        // layerDistance MUST have been adapted
        int horPos = owner.workSpace.x + owner.leftSpace +
                     layerNum * (owner.vertexWidth + owner.layerDistance);
        // vertical positioning
        // number of spacings present
        int spacings = vertexLayers[layerNum].size() + 1; 
        // difference per vertex
        int ds = 0;
        if (vertical)
            ds = (int) Math.round(
                    ((double) owner.workSpace.height - owner.oldWorkSpace.height) /
                    spacings);   
        // set positions
        for (int i = 0; i < vertexLayers[layerNum].size(); i++)
        {   Vertex w = (Vertex) vertexLayers[layerNum].elementAt(i);
            if (ds >= 0)
                w.setLocation(horPos, w.getLocation().y + ds);  
            else 
                w.setLocation(horPos, w.getLocation().y + ds);              
//                w.setLocation(horPos, owner.workSpace.y + owner.topSpace +
//                                      i * (owner.vertexHeight + owner.labelHeight + spacing));  
        }    
        owner.repaint();
    }  // resizeVertexLayer

    public void resizeDiagram(boolean vertical)
    {   for (int i = 0; i < vertexLayers.length; i++)
            resizeVertexLayer(i, vertical);
        updateEdges();    
        owner.repaint();    
    }    
    
    public DiagramCopy copyDiagram()
    {   DiagramCopier diagramCopier = new DiagramCopier(this);
    	DiagramCopy dc = diagramCopier.getDiagramCopy();
        return dc;
    }    

    public void recreateDiagram(DiagramCopy dc)
    {   // remove all
        clearDiagram(false);
        // first arrange the global diagram attributes
        owner.layerDistance = dc.layerDistance;
        owner.numLayers = dc.numLayers;
//        owner.sizeSet = true;
//        owner.owner.setSize(dc.size); // FlowFrame
//        owner.sizeSet = false;

/*        
        owner.flowMode = dc.flowMode;
        
        OptionsMenu m = owner.owner.mainMenus.getMenu(Table.lookUp("represText"));
        if (owner.flowMode == DrawingPanel.decMode)
		    m.switchto(Table.lookUp("decimalText"));
        else
		    m.switchto(Table.lookUp("fractionText"));
        owner.thickMode = dc.thickMode;
        m = owner.owner.mainMenus.getMenu(Table.lookUp("thicknessText"));        
        if (owner.thickMode == DrawingPanel.relMode)
		    m.switchto(Table.lookUp("relativeText"));
        else
		    m.switchto(Table.lookUp("absoluteText"));
		    
        owner.labelHeight = dc.labelHeight;
        m = owner.owner.mainMenus.getMenu(Table.lookUp("optionsText"));
        CheckboxMenuItem cmi = (CheckboxMenuItem) m.getItem(Table.lookUp("labelsText"));        
        if (owner.labelHeight == 0)
            cmi.setState(false);
        else
            cmi.setState(true);
*/            
        owner.flowMode = dc.flowMode;
        if (owner.flowMode == DrawingPanel.decMode)
        	owner.owner.rbDecimaalItem.setSelected(true);
        else
        	owner.owner.rbBreukenItem.setSelected(true);
        
        owner.thickMode = dc.thickMode;
        if (owner.thickMode == DrawingPanel.relMode)
        	owner.owner.rbRelatiefItem.setSelected(true);
        else
        	owner.owner.rbAbsoluutItem.setSelected(true);
        
        owner.labelHeight = dc.labelHeight;        
        if (owner.labelHeight == 0)
        	owner.owner.cbLabelsItem.setSelected(false);
        else
        	owner.owner.cbLabelsItem.setSelected(true);
            
            
        owner.flowOn = dc.flowOn;
        // recreate vertices, in same order as copies!!
        Vector vertices = new Vector();
        for (int i = 0; i < dc.vertexCopies.size(); i++)
        {   VertexCopy vc = (VertexCopy) dc.vertexCopies.elementAt(i);
            Vertex v = new Vertex(vc.root, vc.layerNum);
            if (vc.traceFrom)
                owner.traceFrom = v;
            // label is taken care of globally
            v.decimals = vc.decimals;
            v.vLabel.setText(vc.labelText);
            v.setLocation(owner.getLayerStart(v.layerNum),
                          vc.yLocation);  
            // add listeners
            v.addEdgeButton.addMouseListener(owner.getAddEdgeML());            
            if (!v.root)
                v.colorButton.addMouseListener(owner.getTraceML());    
            else
                owner.roots.addElement(v);
            DrawingPanel.MLMML lis = owner.getMLMML();
            v.flowField.addMouseListener(lis);
            v.flowField.addMouseMotionListener(lis);
            vertices.addElement(v); // also save in array form
            // save as usual
            vertexLayers[v.layerNum].addElement(v);
            owner.add(v);
            // v must have been added
            if (v.root)
                v.setFlow(new Rational(vc.flow));
            
        }    
        
        for (int j = 0; j < dc.edgeCopies.size(); j++)
        {   EdgeCopy ec = (EdgeCopy) dc.edgeCopies.elementAt(j);
            int fromIndex = dc.vertexCopies.indexOf(ec.fromVertexCopy);
            int toIndex = dc.vertexCopies.indexOf(ec.toVertexCopy);
            Vertex fromVertex = (Vertex) vertices.elementAt(fromIndex);
            Vertex toVertex = (Vertex) vertices.elementAt(toIndex);
            Edge e = new Edge(owner, fromVertex, toVertex, 
                              new Rational(ec.capacity));
            e.lastTimeChanged = ec.lastTimeChanged;
            e.setEdge(); // needs coordinates of from-toVertex
            e.setMode(ec.mode);
            //e.thickMode = owner.thickMode;
            edges.addElement(e);
            owner.add(e.capacityField);
        }
// this is already done in the Edge constructor!!        
/*        
        // now fix in- and outedges
        for (int k = 0; k < vertices.size(); k++)
        {   Vertex v = (Vertex) vertices.elementAt(k);
            VertexCopy vc = (VertexCopy) dc.vertexCopies.elementAt(k);
            for (int m = 0; m < vc.inEdgeCopies.size(); m++)
            {   EdgeCopy inec = (EdgeCopy) vc.inEdgeCopies.elementAt(m);
                int inIndex = dc.edgeCopies.indexOf(inec);
                v.inEdges.addElement(edges.elementAt(inIndex));
            }
            for (int n = 0; n < vc.outEdgeCopies.size(); n++)
            {   EdgeCopy outec = (EdgeCopy) vc.outEdgeCopies.elementAt(n);
                int outIndex = dc.edgeCopies.indexOf(outec);            
                v.outEdges.addElement(edges.elementAt(outIndex));                
            }
            
            
        }
*/        
        setEdgeThicknessMode(owner.thickMode);
        if (owner.traceFrom != null)
        {   owner.traceBack(owner.traceFrom);
        }    
        if (owner.flowOn)
        {    owner.flowThread = new Thread(owner);
             owner.flowThread.start();                
             owner.owner.bPanel.bubbleButton.setLabel(Stroomdiagrammen.rb.getString("flowOffText"));
        }    
        calculateDiagram();
        resizeDiagram(true);
        owner.repaint();
        
    }  // recreateDiagram  
/*    
BEWAREN VOOR KANSENBOMEN
    // this procedure produces a tree    
    public void insertVertex(Vertex v, Vertex o)
    {   // adding the root (only once)
        if (o == null)
        {   vertexLayers[v.layerNum].addElement(v);                        
        }    
        else // some other vertex
        // note: here it is assumed the vertices in both the o-layer
        // and the v-layer (the next one) are sorted by
        // vertical position
        // determine the number of outgoing edges from all 
        // vertices preceeding o and the number of outgoing
        // edges from o and insert v in the next layer after
        // these outgoing vertices
        // this works for trees since each of these outgoing edges 
        // ends in a DIFFERENT vertex
        {   int oIndex = vertexLayers[o.layerNum].indexOf(o);
            int vIndex = 0;
            for (int i = 0; i <= oIndex; i++)
            {   Vertex w = (Vertex) vertexLayers[o.layerNum].elementAt(i);
                vIndex += w.outEdges.size(); TREE version
            }    
            vertexLayers[v.layerNum].insertElementAt(v, vIndex);                        
        }    
        updateVertexLayer(v.layerNum);
        if (edgeLayers[v.layerNum].size() > 0)
            updateEdgeLayer(v.layerNum);
        owner.add(v);
        owner.repaint();
    }   
    // vertical "spreading" / "cascading"
    // vertices are put in the same vertical order as in
    // vertexLayers[layerNum]
    public void updateVertexLayer(int layerNum)
    {   // horizontal position
        int horPos = owner.workSpace.x + owner.leftSpace +
                     layerNum * (owner.vertexWidth + owner.layerDistance);
        // vertical positioning
        int spacing = owner.workSpace.height - (owner.topSpace + owner.bottomSpace) -
                      vertexLayers[layerNum].size() * 
                      (owner.vertexHeight + owner.labelHeight);
        if (spacing >= 0)              
            spacing /= vertexLayers[layerNum].size() + 1;             
        else
            spacing /= vertexLayers[layerNum].size() - 1;                     
        // set positions
        for (int i = 0; i < vertexLayers[layerNum].size(); i++)
        {   Vertex w = (Vertex) vertexLayers[layerNum].elementAt(i);
            if (spacing >= 0)
                w.setLocation(horPos, owner.workSpace.y + owner.topSpace + spacing +
                                      i * (owner.vertexHeight + owner.labelHeight + spacing));  
            else 
                w.setLocation(horPos, owner.workSpace.y + owner.topSpace +
                                      i * (owner.vertexHeight + owner.labelHeight + spacing));  
        }    
        owner.repaint();
    }  // updateVertexLayer
    
EINDE BEWAREN    
*/    
}    // class DiagramManager


// deze klasse maakt m.b.v. de DiagramManager een kopie
// van het huidige flowdiagram
// maak, om status te saven, van de kopie een aparte klasse 

class DiagramCopier
{   DiagramManager owner;
    int layerDistance;
    int numLayers;
    Dimension size;
    int flowMode;
    int thickMode;
    int labelHeight;
    boolean flowOn;
    Vector vertexCopies = new Vector();
    Vector edgeCopies = new Vector();
    public DiagramCopier(DiagramManager o)
    {   owner = o;
        layerDistance = owner.owner.layerDistance;
        numLayers = owner.owner.numLayers;
        size = owner.owner.owner.getSize(); // FlowFrame
        flowMode = owner.owner.flowMode;        
        thickMode = owner.owner.thickMode;        
        labelHeight = owner.owner.labelHeight;
        flowOn = owner.owner.flowOn;
        // vector containing references to all vertices
        Vector vertexRefs = owner.getVertexRefs();
        // make copies in same order
        for (int i = 0; i < vertexRefs.size(); i++)
        {   Vertex v = (Vertex) vertexRefs.elementAt(i);
            VertexCopy vc = new VertexCopy(v.code,
                            v.layerNum, v.getLocation().y,
                            v.flow, v.decimals, v.root,
                            v.vLabel.getText());
            if (owner.owner.traceFrom != null)
            {   if (owner.owner.traceFrom == v)
                    vc.traceFrom = true;
                else    
                    vc.traceFrom = false;
            }    
            vertexCopies.addElement(vc);
        }    
        // make copies in same order
        for (int j = 0; j < owner.edges.size(); j++)
        {   Edge e = (Edge) owner.edges.elementAt(j);
            EdgeCopy ec = new EdgeCopy(
                          e.capacity, e.lastTimeChanged, e.mode);
            edgeCopies.addElement(ec);
        }    
        
/*        
        // NOTE: copies and originals are now in same order
        // now fix the interrelations in the graph
        for (int i = 0; i < vertexRefs.size(); i++)
        {   // vertex i
            Vertex v = (Vertex) vertexRefs.elementAt(i);
            // copy of vertex i 
            VertexCopy vc = (VertexCopy) vertexCopies.elementAt(i);
            for (int m = 0; m < v.inEdges.size(); m++)
            {   Edge ine = (Edge) v.inEdges.elementAt(m);
                int inIndex = owner.edges.indexOf(ine);
                vc.inEdgeCopies.addElement(edgeCopies.elementAt(inIndex));
            }
            for (int n = 0; n < v.outEdges.size(); n++)
            {   Edge oute = (Edge) v.outEdges.elementAt(n);
                int outIndex = owner.edges.indexOf(oute);            
                vc.outEdgeCopies.addElement(edgeCopies.elementAt(outIndex));                
            }
            
        }
*/        
        for (int j = 0; j < edgeCopies.size(); j++)
        {   Edge e = (Edge) owner.edges.elementAt(j);
            EdgeCopy ec = (EdgeCopy) edgeCopies.elementAt(j);
            int fromIndex = vertexRefs.indexOf(e.fromVertex);
            int toIndex = vertexRefs.indexOf(e.toVertex);
            ec.fromVertexCopy = (VertexCopy) vertexCopies.elementAt(fromIndex);
            ec.toVertexCopy = (VertexCopy) vertexCopies.elementAt(toIndex);            
        }    
        
    }    
    
    public DiagramCopy getDiagramCopy()
    {	DiagramCopy dc = new DiagramCopy();
    	dc.layerDistance = layerDistance;
    	dc.numLayers = numLayers;
    	dc.size = size;
    	dc.flowMode = flowMode;        
        dc.thickMode = thickMode;        
        dc.labelHeight = labelHeight;
        dc.flowOn = flowOn;
		dc.vertexCopies = vertexCopies;
		dc.edgeCopies = edgeCopies;        
		return dc;
    }
    
} // class DiagramCopier   


// een "pure" klasse voor diagram copies
class DiagramCopy implements Serializable
{   int maxCode;
	int layerDistance;
    int numLayers;
    Dimension size;
    int flowMode;
    int thickMode;
    int labelHeight;
    boolean flowOn;
    Vector vertexCopies = new Vector();
    Vector edgeCopies = new Vector();
    public DiagramCopy()
    {   
    }    
    
}    


// light weight button
class LWButton extends LWContainer
{   // attributes
    String buttonText;
    Font fo1 = new Font("Helvetica", Font.PLAIN, 11);
    // constructor    
    public LWButton(String s, int w, int h)
    {   buttonText = s;
        setSize(w, h);
    }    
    public void setText(String s)
    {   buttonText = s;
        repaint();
    }    
    public void paint(Graphics g)
    {   // background
        g.setColor(Stroomdiagrammen.buttonColor);
        g.fillRect(0, 0, getSize().width, getSize().height);         
        // outline        
        g.setColor(Color.black);
        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1); 
        // right vertical
        g.drawLine(getSize().width - 2, 1, 
                   getSize().width - 2, getSize().height - 2);
        // bottom horizontal           
        g.drawLine(1, getSize().height - 2, 
                   getSize().width - 2, getSize().height - 2);
        g.setColor(Color.white);                   
        // top vertical
        g.drawLine(1, 1, getSize().width - 2, 1);
        g.drawLine(2, 2, getSize().width - 4, 2);
        // left vertical
        g.drawLine(1, 1, 1, getSize().height - 2);
        g.drawLine(2, 2, 2, getSize().height - 4);
        g.setColor(Color.black);        
        g.setFont(fo1);
        int by = getBaseLine(g.getFont(), buttonText);
        drawCenteredString(g, buttonText, by);   
    }    
    public int getBaseLine(Font f, String s)
    {   FontMetrics fm = getFontMetrics(f);
        int charHeight = fm.getHeight() - 2 * fm.getDescent();
        int by = 0;
        int verSpace = getSize().height - charHeight;
        if (verSpace > 0)
            by = verSpace / 2 + charHeight;
        else    
            by = getSize().height;
        return by;        
    }    
    public void drawCenteredString(Graphics g, String s, int by)
    {   FontMetrics fm = getFontMetrics(g.getFont());
        int bx = 0;
        int horSpace = getSize().width - fm.stringWidth(s);
        if (horSpace > 0)
            bx = horSpace / 2;
        g.drawString(s, bx, by);    
    }    
} // class LWButton   

// light weight textfield/label
class NumberField extends LWTextField
{   Font fo1 = new Font("Helvetica", Font.PLAIN, 11);
    public NumberField(boolean edit, int w, int h)
    {   // constructor LWTextField() called by default
        editable = edit;
        setFont(fo1);
        setSize(w, h);
    }
    public void paint(Graphics g)
    {   // if editable behave as superclass LWTextField
        if (editable)
        {   super.paint(g);
        }
        // else behave as a label
        else
        {   g.setColor(bgColor);
            if (rounded)
                g.fillRoundRect(0, 0, getSize().width, getSize().height, DrawingPanel.roundWidth, DrawingPanel.roundHeight);            
            else
                g.fillRect(0, 0, getSize().width, getSize().height);
            g.setColor(Color.black);
            // outline
            if (rounded)
                g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, DrawingPanel.roundWidth, DrawingPanel.roundHeight);            
            else
                g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
            g.setFont(fo1);
            int by = getBaseLine(g.getFont(), text);
            drawCenteredString(g, text, by);
        }
    }
    public int getBaseLine(Font f, String s)
    {   FontMetrics fm = getFontMetrics(f);
        int charHeight = fm.getHeight() - 2 * fm.getDescent();
        int by = 0;
        int verSpace = getSize().height - charHeight;
        if (verSpace > 0)
            by = verSpace / 2 + charHeight;
        else
            by = getSize().height;
        return by;
    }
    public void drawCenteredString(Graphics g, String s, int by)
    {   FontMetrics fm = getFontMetrics(g.getFont());
        int bx = 3;
        // find space
        int horSpace = getSize().width - fm.stringWidth(s + " ");
        if (horSpace > 0)
            bx = horSpace / 2;
        g.drawString(s, bx, by);
    }
} // class NumberField


