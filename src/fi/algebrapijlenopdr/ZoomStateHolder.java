package fi.algebrapijlenopdr;

import java.util.*;

public class ZoomStateHolder {
	
	private AlgebraSchuifVeld asv;
	
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
	
	private Hashtable zoomStates;
	
	public void setSchaalFactorX(String varnaam, double schaalFactorX)
	{	ZoomState zs = null;
        if(zoomStates.containsKey(varnaam)) zs = (ZoomState)zoomStates.get(varnaam);
        if(zs ==null) zs = new ZoomState();
        zs.setSchaalFactorX(schaalFactorX);
        zoomStates.put(varnaam, zs);
	}
	
	public void setSchaalFactorY(String varnaam, double schaalFactorY)
	{	ZoomState zs = null;
        if(zoomStates.containsKey(varnaam)) zs = (ZoomState)zoomStates.get(varnaam);
        if(zs ==null) zs = new ZoomState();
        zs.setSchaalFactorY(schaalFactorY);
        zoomStates.put(varnaam, zs);
    }
	public void setFactorRijNummerX(String varnaam, int factorRijNummerX)
	{	ZoomState zs = null;
        if(zoomStates.containsKey(varnaam)) zs = (ZoomState)zoomStates.get(varnaam);
        if(zs ==null) zs = new ZoomState();
        zs.setFactorRijNummerX(factorRijNummerX);
        zoomStates.put(varnaam, zs);
	}
	
	public void setFactorRijNummerY(String varnaam, int factorRijNummerY)
	{	ZoomState zs = null;
        if(zoomStates.containsKey(varnaam)) zs = (ZoomState)zoomStates.get(varnaam);
        if(zs ==null) zs = new ZoomState();
        zs.setFactorRijNummerY(factorRijNummerY);
        zoomStates.put(varnaam, zs);
	}
	
	public void setBeginwaarde(String varnaam, int beginwaarde)
	{	ZoomState zs = null;
        if(zoomStates.containsKey(varnaam)) zs = (ZoomState)zoomStates.get(varnaam);
        if(zs ==null) zs = new ZoomState();
        zs.setBeginwaarde(beginwaarde);
        zoomStates.put(varnaam, zs);
	}
	
	public void setSelectnummer(String varnaam, int selectnummer)
	{	ZoomState zs = null;
        if(zoomStates.containsKey(varnaam)) zs = (ZoomState)zoomStates.get(varnaam);
        if(zs ==null) zs = new ZoomState();
        zs.setSelectnummer(selectnummer);
        zoomStates.put(varnaam, zs);
	}
	
	public void setZoomStates(String varnaam)
	{	asv.setZoomStates(varnaam, (ZoomState)zoomStates.get(varnaam));
	}
	
	public ZoomState getZoomState(String varnaam)
	{	return (ZoomState)zoomStates.get(varnaam);
	}
	
	
	
	
}
