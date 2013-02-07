package fi.doorziendwo;

import java.util.Hashtable;
import java.util.Vector;

public class NoSer 
{
	public static double[] getVectorDState(Vector3D vec)
	{
		double[] coeff = new double[9];
		coeff[0] = vec.x;
		coeff[1] = vec.y;
		coeff[2] = vec.z;
		
		return coeff;
		
	}

	public static Vector3D setVector3DState(double[] coeff)
	{
		Vector3D vec = new Vector3D();
		
		if (coeff.length >= 3)
			vec = new Vector3D(coeff[0], coeff[1], coeff[2]);
		
		return vec;
	}
	
	
	public static double[] getMatrix3DState(Matrix3D mat)
	{
		double[] coeff = new double[9];
		coeff[0] = mat.row1.x;
		coeff[1] = mat.row1.y;
		coeff[2] = mat.row1.z;
		coeff[3] = mat.row2.x;
		coeff[4] = mat.row2.y;
		coeff[5] = mat.row2.z;
		coeff[6] = mat.row3.x;
		coeff[7] = mat.row3.y;
		coeff[8] = mat.row3.z;
		
		return coeff;
	}
	
	public static Matrix3D setMatrix3DState(double[] coeff)
	{
		Matrix3D mat = new Matrix3D(0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		if (coeff.length >= 9)
			mat = new Matrix3D(coeff[0], coeff[1], coeff[2], 
					           coeff[3], coeff[4], coeff[5], 
					           coeff[6], coeff[7], coeff[8]);
		
		return mat;
	}

	public static double[] getLine3DState(Line3D li)
	{
		double[] params = new double[6];
		
		params[0] = li.point1.x;
		params[1] = li.point1.y;
		params[2] = li.point1.z;
		params[3] = li.point2.x;
		params[4] = li.point2.y;
		params[5] = li.point2.z;
		
		return params;
	}

	public static Line3D setLine3DState(double[] params)
	{
		Vector3D vec1 = new Vector3D(params[0], params[1], params[2]);
		Vector3D vec2 = new Vector3D(params[3], params[4], params[5]);
		
		return new Line3D(vec1, vec2);
	}

	public static double[] getPlane3DState(Plane3D pl)
	{
		double[] params = new double[9];
		
		Vector3D v1 = new Vector3D(pl.support);
		Vector3D v2 = Vector3D.plus(pl.direction1, v1);
		Vector3D v3 = Vector3D.plus(pl.direction2, v1);
		
		params[0] = v1.x;
		params[1] = v1.y;
		params[2] = v1.z;
		params[3] = v2.x;
		params[4] = v2.y;
		params[5] = v2.z;
		params[6] = v3.x;
		params[7] = v3.y;
		params[8] = v3.z;
		
		return params;
	}

	public static Plane3D setPlane3DState(double[] params)
	{
		Vector3D vec1 = new Vector3D(params[0], params[1], params[2]);
		Vector3D vec2 = new Vector3D(params[3], params[4], params[5]);
		Vector3D vec3 = new Vector3D(params[6], params[7], params[8]);
		
		return new Plane3D(vec1, vec2, vec3);
	}
	
	public static double[] getVerticesState(Vector3D[] vertices)
	{
		double[] vert = new double[3 * vertices.length];
		
		for (int vCnt = 0; vCnt < vertices.length; vCnt++)
		{	vert[3 * vCnt] = vertices[vCnt].x;
			vert[3 * vCnt + 1] = vertices[vCnt].y;
			vert[3 * vCnt + 2] = vertices[vCnt].z;
		}
		
		return vert;
	}
	
	public static Vector3D[] setVerticesState(double[] vert)
	{
		Vector3D[] vertices = new Vector3D[vert.length / 3];
		
		for (int vCnt = 0; vCnt < vertices.length; vCnt++)
		{	
			vertices[vCnt] = new Vector3D(vert[3 * vCnt], vert[3 * vCnt + 1], vert[3 * vCnt + 2]);
		} 
		
		return vertices;
	}
	
	public static Hashtable getFacet3DState(Facet3D facet)
	{
		Hashtable h = new Hashtable();
		
		int[] indices = facet.indices;
		String[] vertexLabels = facet.vertexLabels;
		
		h.put("indices", indices);
		h.put("vertexLabels", vertexLabels);
		
		return h;
	}
	
	public static double[] getFacet3DVertexState(Facet3D facet)
	{
		double[] vert = getVerticesState(facet.points);
		
		return vert;
	}
	
	public static Facet3D setFacet3DState(Hashtable h, Vector3D[] vertices)
	{
		int[] indices = new int[0];
		String[] vertexLabels = new String[0];
		
		if (h.containsKey("indices"))
			indices = (int[]) h.get("indices"); 
		if (h.containsKey("indices"))
			vertexLabels = (String[]) h.get("vertexLabels"); 
		
		Facet3D facet = new Facet3D(vertices, indices, DrawConstants.objectColor);
		
		facet.vertexLabels = vertexLabels;
		
		return facet;
	}
	
	public static Facet3D setFacet3DVertexState(double[] vertices)
	{
		Vector3D[] points = setVerticesState(vertices);
		
		int[] indices = new int[points.length];
		for (int iCnt = 0; iCnt < indices.length; iCnt++)
			indices[iCnt] = iCnt;
		
		return new Facet3D(points, indices, DrawConstants.objectColor);
	}
	
	
	public static Hashtable getObject3DState(Object3D object)
	{
		Hashtable h = new Hashtable();
		
		int numVertices = object.numVertices;
		double[] vertices = getVerticesState(object.vertices);
		int numVertexLabels = object.numVertexLabels;
		String[] vertexLabels = object.vertexLabels;

//System.out.println("get numVertices " + numVertices);
//System.out.println("get vertstate " + vertices.length);
		
		h.put("numVertices", new Integer(numVertices));
		h.put("vertices", vertices);
		h.put("numVertexLabels", new Integer(numVertexLabels));
		h.put("vertexLabels", vertexLabels);

		int numFacets = object.numFacets;
		Hashtable[] facets = new Hashtable[numFacets];
		for (int fCnt = 0; fCnt < numFacets; fCnt++)
			facets[fCnt] = getFacet3DState(object.facets[fCnt]);

//System.out.println("get numFacets " + numFacets);
//System.out.println("get facstate " + facets.length);
		
		h.put("numFacets", new Integer(numFacets));
		h.put("facets", facets);

        boolean centerSet = object.centerSet;
        boolean diamSet = object.diamSet;
        double diameter = object.diameter;
        double[] center = getVectorDState(object.center);
        int modelCode = object.modelCode;
		
        h.put("centerSet", new Boolean(centerSet));
        h.put("diamSet", new Boolean(diamSet));
        h.put("diameter", new Double(diameter));
        h.put("center", center);
        h.put("modelCode", new Integer(modelCode));
        
		return h;
	}
	
	public static Object3D setObject3DState(Hashtable h)
	{
		Object3D object = new EmptyObject3D();
		
		int numVertices = 0;
		double[] vertices = new double[0];
		int numVertexLabels = 0;
		String[] vertexLabels = new String[0];
		
		if (h.containsKey("numVertices"))
			numVertices = ((Integer) h.get("numVertices")).intValue();
		if (h.containsKey("vertices"))
			vertices = (double[]) h.get("vertices");
		if (h.containsKey("numVertexLabels"))
			numVertexLabels = ((Integer) h.get("numVertexLabels")).intValue();
		if (h.containsKey("vertexLabels"))
			vertexLabels = (String[]) h.get("vertexLabels");

//System.out.println("set numVertices " + numVertices);
//System.out.println("set vertstate " + vertices.length);
		
		int numFacets = 0;
		Hashtable[] facets = new Hashtable[0];
		
		if (h.containsKey("numFacets"))
			numFacets = ((Integer) h.get("numFacets")).intValue();
		if (h.containsKey("facets"))
			facets = (Hashtable[]) h.get("facets");

//System.out.println("set numFacets " + numFacets);
//System.out.println("set facstate " + facets.length);
		
		
		boolean centerSet = false;
		boolean diamSet = false;
		double diameter = 0;
		double[] center = new double[0];
		int modelCode = 0;
		
		if (h.containsKey("centerSet"))
			centerSet = ((Boolean) h.get("centerSet")).booleanValue();
		if (h.containsKey("diamSet"))
			diamSet = ((Boolean) h.get("diamSet")).booleanValue();
		if (h.containsKey("diameter"))
			diameter = ((Double) h.get("diameter")).doubleValue();
		if (h.containsKey("center"))
			center = (double[]) h.get("center");
		if (h.containsKey("modelCode"))
			modelCode = ((Integer) h.get("modelCode")).intValue();
		
		object.numVertices = numVertices;
		object.vertices = setVerticesState(vertices);
		object.trVertices = new Vector3D[numVertices];
		object.numVertexLabels = numVertexLabels;
		object.vertexLabels = vertexLabels;
		
//System.out.println("set vertices " + object.vertices.length);
		
		object.numFacets = numFacets;
		object.facets = new Facet3D[numFacets];
		for (int fCnt = 0; fCnt < numFacets; fCnt++)
		{	object.facets[fCnt] = setFacet3DState(facets[fCnt], object.vertices);
//System.out.println("facets " + fCnt);			
		}

//System.out.println("set facets " + object.facets.length);

		object.centerSet = centerSet;
		object.diamSet = diamSet;
		object.diameter = diameter;
		object.center = setVector3DState(center);
		object.modelCode = modelCode;
		
		return object;
	}
	
	public static Vector getConstructionState(Vector construction)
	{
		Vector conState = new Vector();
		
		for (int cCnt = 0; cCnt < construction.size(); cCnt++)
		{
			Object o = construction.elementAt(cCnt);
			
			if (o instanceof Line3D)
			{	Line3D line3D = (Line3D) o;
				double[] line = getLine3DState(line3D);
				conState.addElement(line);
			}
			else if (o instanceof Plane3D)
			{	Plane3D plane3D = (Plane3D) o;
				double[] plane = getPlane3DState(plane3D);
				conState.addElement(plane);
				
			}
				
		}
		
		
		return conState;
	}
	
	public static Vector setConstructionState(Vector conState)
	{
		Vector construction = new Vector();
		
		for (int cCnt = 0; cCnt < conState.size(); cCnt++)
		{
			double[] instruct = (double[]) conState.elementAt(cCnt); 
			
			if (instruct.length == 6)
			{	Line3D line3D = setLine3DState(instruct);
				construction.addElement(line3D);
			}
			else if (instruct.length == 9)
			{	Plane3D plane3D = setPlane3DState(instruct);
				construction.addElement(plane3D);
				
			}
		}
		
		return construction;
	}

	public static int containsFacet(Object3D o, Facet3D f)
    {   o.fixFacetArray();
        int result = -1;
        for (int i = 0; i < o.numFacets; i++)
        {   if (Facet3D.isEqualTo(o.facets[i], f) >= 0)
               return i;
        }
        return result;
    }	
	
/*	
    public void makeDeepObjectCopy(Object3D copy)
    {   

        // now make COMPLETELY new facets
        for (int k = 0; k < numFacets; k++)
        {   
            // now copy attributes of facets[k]                             
            Facet3D.copyAttributes(facets[k], copy.facets[k], true); 
        }
        
        // copy object attributes 
        copy.outlined = outlined;
        copy.filled = filled;
        copy.visible = visible;

        copy.centerSet = centerSet;
        copy.diamSet = diamSet;
        copy.diameter = diameter;
        copy.center = new Vector3D(center);
        
        copy.modelCode = modelCode;
        //Object3D parent = null;
        
        
    }    
*/	
}	
