package fi.algebraexpressies;

import java.util.*;

public class ZoomStateHolder 
{
	
	private AlgebraSchuifVeld asv;
	private Hashtable zoomStates;
	
	public ZoomStateHolder(AlgebraSchuifVeld asv)
	{	this.asv = asv;
        zoomStates = new Hashtable();
        setBeginwaarde("", 0);
        setSchaalFactorX("", 1);
        setFactorRijNummerX("", 99);
        setSchaalFactorY("", 1);
        setFactorRijNummerY("", 99);
        setZoomStates("");
        
	}
	
	public Enumeration keys()
	{	return zoomStates.keys();
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		Enumeration en = zoomStates.keys();
		while(en.hasMoreElements())
		{	String key = (String) en.nextElement();
			h.put(key, ((ZoomState) zoomStates.get(key)).getState());
		}
		return h;
	}

    public void setState(Hashtable h)
    {	if (h == null) 
    		return;
    	Enumeration en = h.keys();
		while(en.hasMoreElements())
		{	String key = (String) en.nextElement();
			ZoomState zs = new ZoomState();
			zs.setState((Hashtable) h.get(key));
			zoomStates.put(key, zs);
		}
    }
    
	public void setSchaalFactorX(String varnaam, double schaalFactorX)
	{	ZoomState zs = null;
        if (zoomStates.containsKey(varnaam)) 
        	zs = (ZoomState) zoomStates.get(varnaam);
        if (zs == null) 
        	zs = new ZoomState();
        zs.setSchaalFactorX(schaalFactorX);
        zoomStates.put(varnaam, zs);
	}
	
	public void setSchaalFactorY(String varnaam, double schaalFactorY)
	{	ZoomState zs = null;
        if (zoomStates.containsKey(varnaam)) 
        	zs = (ZoomState) zoomStates.get(varnaam);
        if (zs == null) 
        	zs = new ZoomState();
        zs.setSchaalFactorY(schaalFactorY);
        zoomStates.put(varnaam, zs);
    }
	public void setFactorRijNummerX(String varnaam, int factorRijNummerX)
	{	ZoomState zs = null;
        if (zoomStates.containsKey(varnaam)) 
        	zs = (ZoomState)zoomStates.get(varnaam);
        if (zs == null) 
        	zs = new ZoomState();
        zs.setFactorRijNummerX(factorRijNummerX);
        zoomStates.put(varnaam, zs);
	}
	
	public void setFactorRijNummerY(String varnaam, int factorRijNummerY)
	{	ZoomState zs = null;
        if (zoomStates.containsKey(varnaam)) 
        	zs = (ZoomState)zoomStates.get(varnaam);
        if (zs == null) 
        	zs = new ZoomState();
        zs.setFactorRijNummerY(factorRijNummerY);
        zoomStates.put(varnaam, zs);
	}
	
	public void setBeginwaarde(String varnaam, int beginwaarde)
	{	ZoomState zs = null;
        if (zoomStates.containsKey(varnaam)) 
        	zs = (ZoomState)zoomStates.get(varnaam);
        if (zs == null) 
        	zs = new ZoomState();
        zs.setBeginwaarde(beginwaarde);
        zoomStates.put(varnaam, zs);
	}
	
	public void setSelectnummer(String varnaam, int selectnummer)
	{	ZoomState zs = null;
        if (zoomStates.containsKey(varnaam)) 
        	zs = (ZoomState)zoomStates.get(varnaam);
        if (zs == null) 
        	zs = new ZoomState();
        zs.setSelectnummer(selectnummer);
        zoomStates.put(varnaam, zs);
	}
	
	public void setBeginx(String varnaam, double beginx)
	{	ZoomState zs = null;
        if (zoomStates.containsKey(varnaam)) 
        	zs = (ZoomState)zoomStates.get(varnaam);
        if (zs == null) 
        	zs = new ZoomState();
        zs.setBeginx(beginx);
        zoomStates.put(varnaam, zs);
	}
	
	public void setBeginy(String varnaam, double beginy)
	{	ZoomState zs = null;
        if (zoomStates.containsKey(varnaam)) 
        	zs = (ZoomState)zoomStates.get(varnaam);
        if (zs == null) 
        	zs = new ZoomState();
        zs.setBeginy(beginy);
        zoomStates.put(varnaam, zs);
	}
	
	public void setTracexD(String varnaam, double tracexD)
	{	ZoomState zs = null;
        if (zoomStates.containsKey(varnaam)) 
        	zs = (ZoomState)zoomStates.get(varnaam);
        if (zs == null) 
        	zs = new ZoomState();
        zs.setTracexD(tracexD);
        zoomStates.put(varnaam, zs);
	}
	
	public void setZoomStates(String varnaam)
	{	asv.setZoomStates(varnaam, (ZoomState) zoomStates.get(varnaam));
	}
	
	public void copyZoomState(String varnaam, ZoomState zs)
	{	zoomStates.put(varnaam, zs);
	}
	
	public ZoomState getZoomState(String varnaam)
	{	return (ZoomState) zoomStates.get(varnaam);
	}
	
	
	
	
}
