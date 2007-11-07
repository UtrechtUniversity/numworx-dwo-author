package fi.algebrapijlenopdr;

import java.util.*;

public class ZoomStateHolder {
	
	private AlgebraSchuifVeld asv;
	
	public ZoomStateHolder(AlgebraSchuifVeld asv)
	{	this.asv = asv;
	}
	
	private Hashtable zoomStates;
	
	public void setSchaalFactorX(String varnaam, double schaalFactorX)
	{	if(zoomStates.containsKey(varnaam))((ZoomState)zoomStates.get(varnaam)).setSchaalFactorX(schaalFactorX);
		else 
		{	zoomStates.put(varnaam, new ZoomState());
			((ZoomState)zoomStates.get(varnaam)).setSchaalFactorX(schaalFactorX);
		}
	}
	
	public void setSchaalFactorY(String varnaam, double schaalFactorY)
	{	if(zoomStates.containsKey(varnaam))((ZoomState)zoomStates.get(varnaam)).setSchaalFactorY(schaalFactorY);
		else 
		{	zoomStates.put(varnaam, new ZoomState());
			((ZoomState)zoomStates.get(varnaam)).setSchaalFactorY(schaalFactorY);
		}
	}
	
	public void setFactorRijNrX(String varnaam, int factorRijNummerX)
	{	if(zoomStates.containsKey(varnaam))((ZoomState)zoomStates.get(varnaam)).setFactorRijNrX(factorRijNummerX);
		else 
		{	zoomStates.put(varnaam, new ZoomState());
			((ZoomState)zoomStates.get(varnaam)).setFactorRijNrX(factorRijNummerX);
		}
	}
	
	public void setFactorRijNrY(String varnaam, int factorRijNummerY)
	{	if(zoomStates.containsKey(varnaam))((ZoomState)zoomStates.get(varnaam)).setFactorRijNrY(factorRijNummerY);
		else 
		{	zoomStates.put(varnaam, new ZoomState());
			((ZoomState)zoomStates.get(varnaam)).setFactorRijNrY(factorRijNummerY);
		}
	}
	
	public void setBeginwaarde(String varnaam, int beginwaarde)
	{	if(zoomStates.containsKey(varnaam))((ZoomState)zoomStates.get(varnaam)).setBeginwaarde(beginwaarde);
		else 
		{	zoomStates.put(varnaam, new ZoomState());
			((ZoomState)zoomStates.get(varnaam)).setBeginwaarde(beginwaarde);
		}
	}
	
	public void setSelectnummer(String varnaam, int selectnummer)
	{	if(zoomStates.containsKey(varnaam))((ZoomState)zoomStates.get(varnaam)).setSelectnummer(selectnummer);
		else 
		{	zoomStates.put(varnaam, new ZoomState());
			((ZoomState)zoomStates.get(varnaam)).setSelectnummer(selectnummer);
		}
	}
	
	public void setZoomStates(String varnaam)
	{	
		
		asv.setZoomStates(varnaam, (ZoomState)zoomStates.get(varnaam));
	}
	
	
	
	
}
