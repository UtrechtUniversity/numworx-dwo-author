package fi.stroomdiagrammen;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Vector;
import java.util.ArrayList;

public class NoSer 
{
	public static int notDefined = -10000;
	
	public static HashMap<String,Object> vertexCopyToVertexHashMap(VertexCopy vc)
	{
		HashMap<String,Object> h = new HashMap<String,Object>(); 
    	h.put("code", new Integer(vc.code));
    	h.put("layernum", new Integer(vc.layerNum));
    	h.put("ylocation", new Integer(vc.yLocation));
    	int flowNom;
    	int flowDenom = 1;
    	if (vc.flow.isUndefined())
    		flowNom = notDefined;
    	else
    	{	flowNom = vc.flow.nom;
    		flowDenom = vc.flow.denom;
    	}
    	h.put("flownom", new Integer(flowNom));
    	h.put("flowdenom", new Integer(flowDenom));
    	h.put("decimals", new Integer(vc.decimals));
    	h.put("root", new Boolean(vc.root));
    	h.put("tracefrom", new Boolean(vc.traceFrom));
    	h.put("labeltext", vc.labelText);
    	return h;
	}
	
	public static VertexCopy vertexHashMapToVertexCopy(HashMap<String,Object> vhm)
	{
		int code = 0;
	    int layerNum = 0;
	    int yLocation = 0;
	    Rational flow = Rational.unDefined();
	    int flowNom = notDefined;
	    int flowDenom = 1;
	    int decimals = 0;
	    boolean root = false;  
	    boolean traceFrom = false; 
	    String labelText = "";

    	if (vhm.containsKey("code"))
    		code = ((Integer) vhm.get("code")).intValue();
    	if (vhm.containsKey("layernum"))
    		layerNum = ((Integer) vhm.get("layernum")).intValue();
    	if (vhm.containsKey("ylocation"))
    		yLocation = ((Integer) vhm.get("ylocation")).intValue();
    	if (vhm.containsKey("flownom"))
    		flowNom = ((Integer) vhm.get("flownom")).intValue();
    	if (vhm.containsKey("flowdenom"))
    		flowDenom = ((Integer) vhm.get("flowdenom")).intValue();
    	if (flowNom != notDefined)
    		flow = new Rational(flowNom,flowDenom);
    	if (vhm.containsKey("decimals"))
    		decimals = ((Integer) vhm.get("decimals")).intValue();
    	if (vhm.containsKey("root"))
    		root = ((Boolean) vhm.get("root")).booleanValue();
    	if (vhm.containsKey("tracefrom"))
    		traceFrom = ((Boolean) vhm.get("tracefrom")).booleanValue();
    	if (vhm.containsKey("labeltext"))
    		labelText = (String) vhm.get("labeltext");

    	VertexCopy vc = new VertexCopy(code, layerNum, yLocation, flow, decimals, root, labelText);
    	
    	vc.traceFrom = traceFrom; // nodig? 
    	
    	return vc;
    	
    	
	}
	
	public static HashMap<String,Object> edgeCopyToEdgeHashMap(EdgeCopy ec)
	{
		HashMap<String,Object> h = new HashMap<String,Object>(); 

		HashMap<String,Object> fromVertexHM = vertexCopyToVertexHashMap(ec.fromVertexCopy);
    	h.put("fromvertexhm", fromVertexHM);
    	HashMap<String,Object> toVertexHM = vertexCopyToVertexHashMap(ec.toVertexCopy);
    	h.put("tovertexhm", toVertexHM);
    	h.put("capnom", new Integer(ec.capacity.nom));
    	h.put("capdenom", new Integer(ec.capacity.denom));
    	h.put("lasttimechanged", new Long(ec.lastTimeChanged));
    	h.put("mode", new Integer(ec.mode));
	
		return h;
	}	
		
	public static EdgeCopy edgeHashMapToEdgeCopy(HashMap ehm)
	{
		VertexCopy fromVertexCopy = null, toVertexCopy = null;
	    int capNom = 0;
	    int capDenom = 1;
		Rational capacity;
	    long lastTimeChanged = 0;
	    int mode = 0;

	    HashMap<String,Object> fromVertexHM = new HashMap<String,Object>();
    	if (ehm.containsKey("fromvertexhm"))
    		fromVertexHM = (HashMap<String,Object>) ehm.get("fromvertexhm");
    	fromVertexCopy = vertexHashMapToVertexCopy(fromVertexHM);
	    HashMap<String,Object> toVertexHM = new HashMap<String,Object>();
    	if (ehm.containsKey("tovertexhm"))
    		toVertexHM = (HashMap<String,Object>) ehm.get("tovertexhm");
    	toVertexCopy = vertexHashMapToVertexCopy(toVertexHM);
    	if (ehm.containsKey("capnom"))
    		capNom = ((Integer) ehm.get("capnom")).intValue();
    	if (ehm.containsKey("capdenom"))
    		capDenom = ((Integer) ehm.get("capdenom")).intValue();
    	capacity = new Rational(capNom, capDenom);
    	if (ehm.containsKey("lasttimechanged"))
    		lastTimeChanged = ((Long) ehm.get("lasttimechanged")).longValue();
    	if (ehm.containsKey("mode"))
    		mode = ((Integer) ehm.get("mode")).intValue();
	    
	    EdgeCopy ec = new EdgeCopy(capacity, lastTimeChanged, mode);
	    
	    ec.fromVertexCopy = fromVertexCopy;
	    ec.toVertexCopy = toVertexCopy;
	    
	    return ec;
	    
	}
	
	public static HashMap<String,Object> diagramCopyToDiagramHashMap(DiagramCopy dc)
	{
		HashMap<String,Object> h = new HashMap<String,Object>();
		
		h.put("maxcode", new Integer(dc.maxCode));
		h.put("layerdistance", new Integer(dc.layerDistance));
		h.put("numlayers", new Integer(dc.numLayers));
	    h.put("breedte", new Integer(dc.size.width));
	    h.put("hoogte", new Integer(dc.size.height));
	    h.put("flowmode", new Integer(dc.flowMode));
	    h.put("thickmode", new Integer(dc.thickMode));
	    h.put("labelheight", new Integer(dc.labelHeight));
	    h.put("flowon", new Boolean(dc.flowOn));

	    ArrayList<HashMap<String,Object>> vertexArrayList = new ArrayList<HashMap<String,Object>>();
	    for (int vCnt = 0; vCnt < dc.vertexCopies.size(); vCnt++)
	    {	VertexCopy vc = (VertexCopy) dc.vertexCopies.elementAt(vCnt);
	    	HashMap <String,Object> vhm = vertexCopyToVertexHashMap(vc);
	    	vertexArrayList.add(vhm);
	    }
	    h.put("vertexarraylist", vertexArrayList);
	    
	    ArrayList<HashMap<String,Object>> edgeArrayList = new ArrayList<HashMap<String,Object>>();
	    for (int eCnt = 0; eCnt < dc.edgeCopies.size(); eCnt++)
	    {	EdgeCopy ec = (EdgeCopy) dc.edgeCopies.elementAt(eCnt);
	    	HashMap <String,Object> ehm = edgeCopyToEdgeHashMap(ec);
	    	edgeArrayList.add(ehm);
	    }
	    h.put("edgearraylist", edgeArrayList);
		
		return h;
	}
	

	public static DiagramCopy diagramHashMapToDiagramCopy(HashMap dhm)
	{
		int maxCode = 1;
		int layerDistance = 1;
	    int numLayers = 1;
	    int breedte = 10;
	    int hoogte = 10;
	    //Dimension size;
	    int flowMode = 0;
	    int thickMode = 0;
	    int labelHeight = 0;
	    boolean flowOn = false;
	    
	    ArrayList<HashMap<String,Object>> vertexArrayList = new ArrayList<HashMap<String,Object>>();
	    ArrayList<HashMap<String,Object>> edgeArrayList = new ArrayList<HashMap<String,Object>>();
		
    	if (dhm.containsKey("maxcode"))
    		maxCode = ((Integer) dhm.get("maxcode")).intValue();
    	if (dhm.containsKey("layerdistance"))
    		layerDistance = ((Integer) dhm.get("layerdistance")).intValue();
    	if (dhm.containsKey("numlayers"))
    		numLayers = ((Integer) dhm.get("numlayers")).intValue();
    	if (dhm.containsKey("breedte"))
    		breedte = ((Integer) dhm.get("breedte")).intValue();
    	if (dhm.containsKey("hoogte"))
    		hoogte = ((Integer) dhm.get("hoogte")).intValue();
    	if (dhm.containsKey("flowmode"))
    		flowMode = ((Integer) dhm.get("flowmode")).intValue();
    	if (dhm.containsKey("thickmode"))
    		thickMode = ((Integer) dhm.get("thickmode")).intValue();
    	if (dhm.containsKey("labelheight"))
    		labelHeight = ((Integer) dhm.get("labelheight")).intValue();
    	if (dhm.containsKey("flowon"))
    		flowOn = ((Boolean) dhm.get("flowon")).booleanValue();
    	if (dhm.containsKey("vertexarraylist"))
    		vertexArrayList = (ArrayList<HashMap<String,Object>>) dhm.get("vertexarraylist");
    	if (dhm.containsKey("edgearraylist"))
    		edgeArrayList = (ArrayList<HashMap<String,Object>>) dhm.get("edgearraylist");
		
		
		DiagramCopy dc = new DiagramCopy();
		dc.maxCode = maxCode;
		dc.layerDistance = layerDistance;
		dc.numLayers = numLayers;
		dc.size = new Dimension(breedte,hoogte);
		dc.flowMode = flowMode;
		dc.thickMode = thickMode;
		dc.labelHeight = labelHeight;
		dc.flowOn = flowOn;
		for (int vCnt = 0; vCnt < vertexArrayList.size(); vCnt++)
		{	HashMap<String,Object> vhm = vertexArrayList.get(vCnt);
			VertexCopy vc = vertexHashMapToVertexCopy(vhm);
			dc.vertexCopies.addElement(vc);
		}
		for (int eCnt = 0; eCnt < edgeArrayList.size(); eCnt++)
		{	HashMap<String,Object> ehm = edgeArrayList.get(eCnt);
			EdgeCopy ec = edgeHashMapToEdgeCopy(ehm);
			dc.edgeCopies.addElement(ec);
		}
		
		
		return dc;
		
	}

}
