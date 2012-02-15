package fi.grafiek3dtest;

import java.awt.Color;
import fi.grafiek3dtest.expressies.*;


public class Grafiek3D extends Object3D
{
	final double VERYBIG = 1e10d;
	final double NZERO = 1e-5d;
	boolean trimTop = false;
	boolean trimBottom = false;
	Vector3D topMaxVertex = null;
	Vector3D bottomMinVertex = null;
	double bigPos = 0;
	double bigMin = 0;
	
//	String[] vLabels; 
	
    public Grafiek3D()
    {}
    public Grafiek3D(Expressie exp,
    			double xMin, double xMax, double xStep, 
    		    double yMin, double yMax, double yStep,
    		    double zMin, double zMax, double zStep,
    		    String varNaamX, String varNaamY, int xFinerSteps, int yFinerSteps)
    {
    	
		double xAsyPos = 0;
		double xAszPos = 0;
		double yAsxPos = 0;
		double yAszPos = 0;
		double zAsxPos = 0;
		double zAsyPos = 0;
		
		if (xMin > NZERO)
		{	yAsxPos = xMin;
			zAsxPos = xMin;
		}
		if (xMax < -NZERO)
		{	yAsxPos = xMax;
			zAsxPos = xMax;
		}
		if (yMin > NZERO)
		{	xAsyPos = yMin;
			zAsyPos = yMin;
		}
		if (yMax < -NZERO)
		{	xAsyPos = yMax;
			zAsyPos = yMax;
		}
		if (zMin > NZERO)
		{	xAszPos = zMin;
			yAszPos = zMin;
		}
		if (zMax < -NZERO)
		{	xAszPos = zMax;
			yAszPos = zMax;
		}
		
		bigPos = 10 * zMax;
		bigMin = 10 * zMin;
		
		double xStepFine = xStep / xFinerSteps;
		double yStepFine = yStep / yFinerSteps;
		
		
    	int numXFacets = (int) Math.round((xMax - xMin) / xStepFine);
    	int numYFacets = (int) Math.round((yMax - yMin) / yStepFine);
    	int numGraphFacets = numXFacets * numYFacets;
    	
   		int numGraphVertices = (numXFacets + 1) * (numYFacets + 1);
    	numVertices = numGraphVertices;
		numVertexLabels = numVertices;
		vertices = new Vector3D[numVertices];
		// do NOT forget this
		trVertices = new Vector3D[numVertices];
//		vLabels = new String[numVertices];

		double[] subst = new double[2];
		String[] vars = new String[2];
		vars[0] = varNaamX;
		vars[1] = varNaamY;
		for (int yCnt = 0; yCnt < (numYFacets + 1); yCnt++)			
			for (int xCnt = 0; xCnt < (numXFacets + 1); xCnt++)
			{	subst[0] = xMin + xCnt * xStepFine;
				subst[1] = yMin + yCnt * yStepFine;
				double expWaarde = exp.geefWaarde(subst, vars);

// LATER "oneindige" vertices omlabelen tot iets onschuldigs
				
				
				vertices[xCnt + (numXFacets + 1) * yCnt] = 
					new Vector3D(subst[0], subst[1], expWaarde);
				
				if (!isUnWanted(expWaarde))
				{
					if (expWaarde > (zMax + NZERO))
					{	trimTop = true;
						if ((topMaxVertex == null) || (topMaxVertex.z < (expWaarde - NZERO)))
							topMaxVertex = new Vector3D(vertices[xCnt + (numXFacets + 1) * yCnt]);
					}
				
					if (expWaarde < (zMin - NZERO))
					{	trimBottom = true;
						if ((bottomMinVertex == null) || (bottomMinVertex.z > (expWaarde + NZERO)))
							bottomMinVertex = new Vector3D(vertices[xCnt + (numXFacets + 1) * yCnt]);
					}
				}
					
			}
		
		
	    int tempNumFacets = numGraphFacets;
	    int numNonNullFacets = 0;
	    Facet3D[] tempFacets = new Facet3D[tempNumFacets];
	        
	    for (int yCnt = 0; yCnt < numYFacets; yCnt++)
	     	for (int xCnt = 0; xCnt < numXFacets; xCnt++)
	       	{	int[] indices = new int[4];
	       		indices[0] = xCnt + (numXFacets + 1) * yCnt;
	       		indices[1] = xCnt + 1 + (numXFacets + 1) * yCnt;
	       		indices[2] = xCnt + 1 + (numXFacets + 1) * (yCnt + 1);
	       		indices[3] = xCnt + (numXFacets + 1) * (yCnt + 1);	
	        		
// hier voor de "oneindige" vertices geen facet maken
	       		
	       		boolean vertexUnWanted = isUnWanted(vertices[indices[0]].z) || isUnWanted(vertices[indices[1]].z) ||
		       							 isUnWanted(vertices[indices[2]].z) || isUnWanted(vertices[indices[3]].z);
	       		boolean edgeUnWanted = false;
	       		// test alleen als de vertices "normaal" zijn
	        	if (!vertexUnWanted)
	        	{
	        		edgeUnWanted = isUnWanted(vertices[indices[0]], vertices[indices[1]], exp, varNaamX, varNaamY) || 
	        					   isUnWanted(vertices[indices[1]], vertices[indices[2]], exp, varNaamX, varNaamY) ||
	        					   isUnWanted(vertices[indices[2]], vertices[indices[3]], exp, varNaamX, varNaamY) || 
	        					   isUnWanted(vertices[indices[3]], vertices[indices[0]], exp, varNaamX, varNaamY);
	        	}
	       		if (!vertexUnWanted && !edgeUnWanted)
	       		{	
	       			numNonNullFacets++;
	       			tempFacets[xCnt + numXFacets * yCnt] = new Facet3D(vertices, indices, Grafiek3DComponent.graphColor);
	       			tempFacets[xCnt + numXFacets * yCnt].outlineColor = Grafiek3DComponent.graphOutlineColor;
	       			
	       		}
	        }

// hier de null-facets opruimen en numFacets aanpassen	        
	        
//System.out.println("tempNumFacets = " + tempNumFacets);
//System.out.println("numNonNullFacets = " + numNonNullFacets);
	    
	    numFacets = numNonNullFacets;
	    facets = new Facet3D[numFacets];
	    int nonNullCnt = 0;
	    for (int fCnt = 0; fCnt < tempNumFacets; fCnt++)
	    {  	if (tempFacets[fCnt] != null)
	    	{	facets[nonNullCnt] = tempFacets[fCnt];
	    	    nonNullCnt++;
	    	
	    	}
	    }
	    
for (int fCnt = 0; fCnt < numFacets; fCnt++)
{	if (facets[fCnt] == null)
	System.out.println("" + fCnt + " null");
	
}
	    
	    // label "oneindige" vertices tot iets onschuldigs	    
        for (int vCnt = 0; vCnt < numVertices; vCnt++)
        {	if (isUnWanted(vertices[vCnt].z))
        		vertices[vCnt] = new Vector3D(vertices[vCnt].x, vertices[vCnt].y, (zMin + zMax) / 2);
        }
	    
    	
        for (int fCnt = 0; fCnt < numFacets; fCnt++)
            for (int vCnt = 0; vCnt < facets[fCnt].numPoints; vCnt++)
                facets[fCnt].vertexLabels[vCnt] = "";
//                    vLabels[facets[fCnt].indices[vCnt]];
                    

        // find the center !!
        Vector3D center = new Vector3D((xMin + xMax) / 2, (yMin + yMax) / 2, (zMin + zMax) / 2);
        //initObject3D(true, false);
        initObject3D(true, center, false);
                
    	
    }
    
    public boolean isUnWanted(double d)
    {	boolean unWanted = false;
    	
    	unWanted = Double.isNaN(d) || Double.isInfinite(d) || (Math.abs(d) > VERYBIG); 
    	
    	return unWanted;
    }
    
    public boolean isUnWanted(Vector3D v1, Vector3D v2, Expressie exp, String varNaamX, String varNaamY)
    {	boolean unWanted = false;
    
    	double[] subst = new double[2];
    	String[] vars = new String[2];
    	vars[0] = varNaamX;
    	vars[1] = varNaamY;
    	subst[0] = v1.x;
		subst[1] = v1.y;
		double expWaarde = exp.geefWaarde(subst, vars);
    
    	double max = expWaarde;
    	double min = expWaarde;
    	int steps = 100;
    	double stepX = (v2.x - v1.x) / steps;
    	double stepY = (v2.y - v1.y) / steps;
    	for (int stepCnt = 1; stepCnt <= steps; stepCnt++)
    	{
    		subst[0] = v1.x + stepCnt * stepX;
    		subst[1] = v1.y + stepCnt * stepY;
    		expWaarde = exp.geefWaarde(subst, vars);
    		
    		if (!Double.isNaN(expWaarde) && !Double.isInfinite(expWaarde))
    		{
    			max = Math.max(max, expWaarde);
    			min = Math.min(min, expWaarde);
    		}
    		
    	}

if ((Math.abs(v1.x - 1) < NZERO) && (Math.abs(v1.y) < NZERO))
{
//System.out.println("v1.x = 1");	
//System.out.println("max = " + max);
//System.out.println("min = " + min);	
}
    	
    	if ((max > bigPos) && (min < bigMin))
    	{	unWanted = true;
    	
    	}
    
    	return unWanted;	
    }
    
    public Object3D deepCopy()
    {   Grafiek3D copy = new Grafiek3D();
        makeDeepObjectCopy(copy);
        copy.trimTop = trimTop;
        copy.trimBottom = trimBottom;
        copy.topMaxVertex = Vector3D.copyVector3D(topMaxVertex);
        copy.bottomMinVertex = Vector3D.copyVector3D(bottomMinVertex);
        return copy;        
    }   
    
}
