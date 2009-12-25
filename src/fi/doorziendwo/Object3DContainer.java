package fi.doorziendwo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

// class representing the drawing area (a Canvas), also initializes and
// manipulates the 3D model
public class Object3DContainer extends JPanel
{       
    // the 3D object(s) in worldspace
    ObjectGroup3D model, previewModel;
    // transforming world space to view space
    Matrix3D mat;
    // types of Painter's algo
    public static final int PUREZ = 0;
    public static final int NZMINFIRST = 1;
    public static final int NONZMIN = 2;
    public static final int HYBRID1 = 3;
    public static final int HYBRID2 = 4;
    public static final int SEMIEXACT = 5;
    public static final int EXACT = 6;    
    
    // default
    public int paintType = PUREZ;

    // sensitivity for clicking vertices and edges in pixels
    public static final int SSTT = 4;
    
    // for remembering mouse position at start of dragg
    int oldX, oldY;
    // remembering angles
    double angleX, angleY, angleZ;
    // scaling factor for drawing on the canvas
    // pixels per unit world space, incorporates zoomFactor
    double scaleFac;
    // view object from a point at distance VIEWRATIO times its size
    public static int VIEWRATIO = 5;
    // schadows
    boolean shadow = true;
    // showing inside in gray
    boolean showInside = true;
    // background color
    Color bgColor = Color.white;
    
    // distance of view point to screen i.e.
    // distance to z-plane through (model.origin.x, model.origin.y, 0)
    public static double MAXDISTANCE = 100000;
    double minDistance;
    double distance = MAXDISTANCE; // set in paint for perspective
    
    // projections
    public static int CENTRALPROJ = 0;
    public static int PARALLELPROJ = 1;
    
    public int projection = CENTRALPROJ;
    
    double zoomFactor = 9e-1d;
    boolean retransform = true;
    
    //Image offscreen = null;
    
    Vector3D helpStart;
    Point pt1 = new Point();
    Point pt2 = new Point();
    Color helpLineColor = null;
    boolean helpLine = false;

    Point hp = new Point();
    Color helpPointColor = null;
    boolean helpPoint = false;
    // point size
    public static int POINT = 14;
    
    // dash length in pixels
    public static int DASH = 4;
    
    public Font vertexFont = new Font("SansSerif", Font.ITALIC, 12);        
    public Font textFont = new Font("SansSerif", Font.PLAIN, 12);    
    
    private boolean bordered = true;

// testing    
public static String testString = "";    
    
    // constructor
    Object3DContainer()
    {   mat = new Matrix3D();
        setLayout(null);
        //setOpaque(false);
    }

    // assen extern maken en toevoegen    
    public void initializeModel(ObjectGroup3D m, boolean newModel)
    {   
        if (newModel)
            mat = new Matrix3D();
        
        model = m;
        
        double wFac = zoomFactor * 
            ((double) getSize().width) / model.diameter;
        double hFac = zoomFactor * 
            ((double) getSize().height) / model.diameter;
        scaleFac = Math.min(wFac, hFac);

        // make sure we are viewing from z+ thus y+ to the top
        // x+ to the right
        mat.setScale(scaleFac, - scaleFac, scaleFac);
        // center of viewing plane corresponds to 
        // center of world model
        mat.setOrigin(((double) getSize().width) / 2,
                      ((double) getSize().height) / 2,
                      0);

        // closer does not yet work well
        minDistance = scaleFac * model.diameter;
        // set initial angles
        
        if (newModel)
        {
            angleX = 80;
            angleY = 25;        
            angleZ = 25;
            
            mat.zRotateBy(angleZ);
            mat.xRotateBy(angleX);
//            mat.yRotateBy(angleY);
//            mat.vwRotate(new Vector3D(0, 0, 1),
//                         new Vector3D(0, 1, 0));
            
        }

        //Graphics gg = getGraphics();
        //if (gg != null)
        //    paint(getGraphics());
        //else
            repaint();
    }
    
    public void setBackground(Color c)
    {	if(bordered) bgColor = Color.white;
    	else bgColor = c;
	    super.setBackground(c);
	}
    
    public void setBordered(boolean b)
    {	bordered = b;
    	if(!b) bgColor = getBackground();
    	else bgColor = Color.white;
    	
    }
    
    

    public void setProjection(int proj)
    {   if (proj == CENTRALPROJ)
            projection = CENTRALPROJ;
        if (proj == PARALLELPROJ)
            projection = PARALLELPROJ;    
        repaint();

    }
    public void setZoomFactor(double factor)
    {   zoomFactor = factor;
        double wFac = zoomFactor * 
            ((double) getSize().width) / model.diameter;
        double hFac = zoomFactor * 
            ((double) getSize().height) / model.diameter;
        scaleFac = Math.min(wFac, hFac);

        // make sure we are viewing from z+ thus y+ to the top
        // x+ to the right
        mat.setScale(scaleFac, - scaleFac, scaleFac);
        // closer does not yet work well
        minDistance = scaleFac * model.diameter;
        
        repaint();
    }    

// niet nodig gebruik initialize met false??
// zorg dat nieuwe afmetingen extern berekend worden??
    // iets met reset model wanneer de afmeting van de container
    // of de objectGroup3D model extern veranderd is
// eventueel met boolean resize    

    public void resetModel()
    {   //model.findCenter();
        //model.findDiameter();
    	if(model==null) return;
        
        double wFac = zoomFactor * 
            ((double) getSize().width) / model.diameter;
        double hFac = zoomFactor * 
            ((double) getSize().height) / model.diameter;
        scaleFac = Math.min(wFac, hFac);

        // make sure we are viewing from z+ thus y+ to the top
        // x+ to the right
        mat.setScale(scaleFac, - scaleFac, scaleFac);
        // center of viewing plane corresponds to origin (0, 0, 0)
        // of world model
        mat.setOrigin(((double) getSize().width) / 2,
                      ((double) getSize().height) / 2,
                      0);
        repaint();
    }


    //public void update(Graphics g)
    //{   paint(g);
    //    
    //}    

    public void paintComponent(Graphics g)
    {   

		if (model == null)
		{	// outline only
    	    g.setColor(Color.black);
	        if (bordered) g.drawRect(0,0,getSize().width - 1, getSize().height - 1);
			return;
        }
        // set distance in view space
        // object size is model.diameter * scaleFac
        // multiply by VIEWRATIO
        if (projection == CENTRALPROJ)
            distance = model.diameter * scaleFac * VIEWRATIO;// zoomFactor;
        if (projection == PARALLELPROJ)    
            distance = MAXDISTANCE;
//System.out.println("dis = " + distance);        
        //if (offscreen == null)
            //offscreen = createImage(getSize().width, getSize().height);
        //Graphics og = g;//offscreen.getGraphics();
        //g.setClip(0, 0, getSize().width, getSize().height);   
        // background
        g.setColor(bgColor);
        g.fillRect(0,0,getSize().width, getSize().height);

        g.setFont(vertexFont);
        if (previewModel == null)
            model.paintObject3D(g, shadow, showInside, distance, mat, paintType, retransform);
                                   
                                
                                    
        if (previewModel != null)
        {    previewModel.paintObject3D(g, shadow, showInside, distance, mat, paintType,
                                       retransform);
             //model.fixFacetArray();
             //model.transformBy(mat, true);
        }
        if (helpLine)
        {   g.setColor(helpLineColor);
            drawDashedLine(g, pt1.x, pt1.y, pt2.x, pt2.y);
        }    

        if (helpPoint)
        {   g.setColor(helpPointColor);
        
/*        
            og.drawOval(pt1.x - POINT / 2, pt1.y - POINT / 2, 
                        POINT, POINT);
*/
            g.drawLine(hp.x, hp.y - POINT / 2, 
                        hp.x, hp.y + POINT / 2 - 1);
            g.drawLine(hp.x - POINT / 2, hp.y, 
                        hp.x + POINT / 2 - 1, hp.y);
                        
                        
        }    
        // outline
        g.setColor(Color.gray);
        if (bordered)g.drawRect(0,0,getSize().width-1, getSize().height-1 );
/*        
og.drawLine(0, getSize().height / 2, getSize().width - 1, getSize().height /2);
og.drawLine(getSize().width / 2, 0, getSize().width / 2, getSize().height -1);
*/        
        
        //super.paint(og);

// testing        
g.setFont(textFont);       
g.setColor(Color.black);
g.drawString(testString, 10, 15);

         //g.drawImage(offscreen, 0, 0, null);
         //og.dispose();
  

    }

    public void setPreviewModel(ObjectGroup3D pvModel)
    {   previewModel = pvModel;
        //Graphics gg = getGraphics();
        //if (gg != null)
         //   paint(getGraphics());
        //else
            repaint();

    }
    
    public void setShowInside(boolean b)
    {   showInside = b;
        repaint();
    }

    public void setFilled(boolean b)
    {   model.setFilled(b);
        repaint();
    }    

    public void setDistance(int d)
    {   if (d > MAXDISTANCE)
            distance = MAXDISTANCE;
        else if (d <= minDistance)
            distance = minDistance;
        else
            distance = d;
        repaint();
    }

    
    public void rotateByZ(double zTheta)
    {   
        mat.zRotateBy(zTheta);        
    }
    
    
    public void rotateBy(double xTheta, double yTheta)
    {   
        mat.xRotateBy(xTheta);        
        mat.yRotateBy(yTheta);
    }

	// werkt wel!
    public void vwRotate(Vector3D v, Vector3D w)
    {   
        mat.vwRotate(v, w);        
        //Graphics gg = getGraphics();
        //if (gg != null)
        //    paint(getGraphics());
        //else
            repaint();
    }

    
    
    
    public void rotateCake(double xTheta, double yTheta)        
    {
         angleX += xTheta;
         angleY += yTheta;
         if (angleX > 80)
            angleX = 80;
         if (angleX < 0)
            angleX = 0;
         mat.reset();                    
         mat.zRotateBy(angleY);                    
         mat.xRotateBy(angleX);       
    }

    // shortcuts
    public Object3D objectClicked(int x, int y)
    {   return model.objectClicked(x, y, distance, mat.origin, paintType);
    }
    public Facet3D clickedFacet(int x, int y)
    {   return model.clickedFacet(x, y, distance, mat.origin, paintType);
    }               
    public int facetClicked(int x, int y)
    {   return model.facetClicked(x, y, distance, mat.origin, paintType);
    }               

/*
    public Vector3D vertexClicked(int x, int y)
    {   return model.vertexClicked(x, y, distance, mat.origin);
    }                                      
*/

    public Vector3D[] edgeClicked(int x, int y)
    {   return model.edgeClicked(x, y, distance, mat.origin); 
    }                                      

    public FacetWithVertex facetWithVertexClicked(int x, int y)
    {   return model.facetWithVertexClicked(x, y, distance, mat.origin);
    }                                      
    public FacetWithEdgePoint facetWithEdgePointClicked(int x, int y)
    {   return model.facetWithEdgePointClicked(x, y, distance, mat.origin); 
    }                                      



// dit werkt niet goed voor subModels
// omdat subarrays NIET aan de paint algo zijn aangepast
/*
    public Vector3D vertexClicked(int x, int y, boolean includeInvisibles,
                                  Object3D subModel)
    {   return subModel.vertexClicked(x, y, distance, mat.origin, 
                                      includeInvisibles);                                  
    }                                      
    public Vector3D[] edgeClicked(int x, int y, boolean includeInvisibles,
                                  Object3D subModel)
    {   return subModel.edgeClicked(x, y, distance, mat.origin, 
                                    includeInvisibles);                                  
    }                                      
*/    
    
    public void hideHelpLine()
    {   helpLine = false;
        //repaint();
    }    
    
    public void showHelpLine(Vector3D v, Point p, Color c)
    {   helpLine = true;
        helpStart = new Vector3D(v);
        Vector3D trV = mat.transform(Vector3D.minus(helpStart, model.center));
        double temp =  distance / (distance - trV.z);
        pt1.x = (int) Math.round((mat.origin.x + (trV.x - mat.origin.x) * temp));
        pt1.y = (int) Math.round((mat.origin.y + (trV.y - mat.origin.y) * temp));
        pt2 = p;
        helpLineColor = c;
        repaint();
    }    
    public void updateHelpLine(Point p)
    {   if (helpLine)
        {
            Vector3D trV = mat.transform(Vector3D.minus(helpStart, model.center));
            double temp =  distance / (distance - trV.z);
            pt1.x = (int) Math.round((mat.origin.x + (trV.x - mat.origin.x) * temp));
            pt1.y = (int) Math.round((mat.origin.y + (trV.y - mat.origin.y) * temp));
            pt2 = p;
        //repaint();
        }
    }    

    
    public void hideHelpPoint()
    {   helpPoint = false;
        //repaint();
    }    
    
    public void showHelpPoint(Point p, Color c)
    {   helpPoint = true;
/*    
        helpStart = new Vector3D(v);
        Vector3D trV = mat.transform(Vector3D.minus(helpStart, model.center));
        double temp =  distance / (distance - trV.z);
        pt1.x = (int) Math.round((mat.origin.x + (trV.x - mat.origin.x) * temp));
        pt1.y = (int) Math.round((mat.origin.y + (trV.y - mat.origin.y) * temp));
*/
        hp = new Point(p.x, p.y); 
        helpPointColor = c;
        repaint();
    }    
    
    
    public void updateHelpPoint(Point p)
    {   if (helpPoint)
        {
/*            
            Vector3D trV = mat.transform(Vector3D.minus(helpStart, model.center));
            double temp =  distance / (distance - trV.z);
            pt1.x = (int) Math.round((mat.origin.x + (trV.x - mat.origin.x) * temp));
            pt1.y = (int) Math.round((mat.origin.y + (trV.y - mat.origin.y) * temp));
*/
        hp = new Point(p.x, p.y);
        //repaint();
        }
    }    
    
    public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2)
    {   int dis = (int) Math.round(pixDis(x1, y1, x2, y2));
    
        boolean dashOn = true;
        int xStart, xEnd, yStart, yEnd;
        // points too close to dash
        if (dis <= DASH)
        {   g.drawLine(x1, y1, x2, y2); 
        }    
        // vertical line
        else if (x1 == x2)
        {   xStart = x1;
            if (y1 < y2)
            {   yStart = y1; 
                yEnd = y2;
            }
            else
            {   yStart = y2;
                yEnd = y1;
            }    
            while (yStart < yEnd)
            {   if (dashOn)
                {   g.drawLine(xStart, yStart, xStart, 
                        Math.min(yStart + DASH, yEnd));
                    dashOn = false;        
                }
                else
                {   dashOn = true;        
                }    
                yStart += DASH;                
            }
        }
        // horizontal line
        else if (y1 == y2)
        {   yStart = y1;
            if (x1 < x2)
            {   xStart = x1; 
                xEnd = x2;
            }
            else
            {   xStart = x2;
                xEnd = x1;
            }    
            while (xStart < xEnd)
            {   if (dashOn)
                {   g.drawLine(xStart, yStart, 
                        Math.min(xStart + DASH, xEnd), yStart);
                    dashOn = false;        
                }
                else
                {   dashOn = true;        
                }    
                xStart += DASH;                
            }
        }
        // sloped line
        else
        {   if (x1 < x2)
            {   xStart = x1; 
                xEnd = x2;
                yStart = y1;
                yEnd = y2;
            }
            else
            {   xStart = x2;
                xEnd = x1;
                yStart = y2;
                yEnd = y1;
            }    
            // find slope of segment
            double slope = ((double) (yEnd - yStart))/(xEnd - xStart);
            double alpha = Math.atan(slope);            
//System.out.println("alpha = " + UF.format(alpha, 4));            
            double length = pixDis(xStart, yStart, xEnd, yEnd);
            int steps = (int) Math.round(
                pixDis(xStart, yStart, xEnd, yEnd) / DASH);
//System.out.println("steps = " + steps);
                
            for (int i = 1; i <= steps; i++)
            {   int xFrom = xStart +
                    (int) Math.round(Math.cos(alpha) * DASH * (i - 1));
                int yFrom = yStart + 
                    (int) Math.round(Math.sin(alpha) * DASH * (i - 1));
                int xTo = xStart + 
                    (int) Math.round(Math.cos(alpha) * DASH * i);
                int yTo = yStart + 
                    (int) Math.round(Math.sin(alpha) * DASH * i);
                    
                xTo = Math.min(xTo, xEnd);    
                if (yStart > yEnd)
                    yTo = Math.max(yTo, yEnd);
                else
                    yTo = Math.min(yTo, yEnd);
                    
                    
                if ((i % 2) != 0)
                {   g.drawLine(xFrom, yFrom, xTo, yTo);
                }
            }
            
        }    
    }
    
    private double pixDis(int x1, int y1, int x2, int y2)
    {   return Math.sqrt((x1 - x2) * (x1 - x2) +
                         (y1 - y2) * (y1 - y2));
        
    }    
    
    
    
}


