package fi.ivmdrawgwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;



public class IVMStrokeContainer {
	
	private ArrayList<Stroke> strokes = new ArrayList<Stroke>();
	private boolean isJar = false;
	
	public void addStroke(Stroke stroke) {
		strokes.add(stroke);
	}
	
	public void draw(Context2d g) {
		g.setStrokeStyle(CssColor.make(80, 80, 80));
		if(isJar)
			g.setLineWidth(4.0);
		for(int i = 0 ; i < strokes.size() ; i++) {
			Stroke stroke = strokes.get(i);
			g.beginPath();
			double x0 = (int)stroke.getParsePoints().get(0).x;
			double y0 = (int)stroke.getParsePoints().get(0).y;
			g.moveTo(x0, y0);
			if(stroke.getParsePointsbox().width>3 ||  stroke.getParsePointsbox().height>3) {
				for(int j = 1 ; j < stroke.getParsePoints().size() ; j++) {
					double x = stroke.getParsePoints().get(j).x ;
					double y = stroke.getParsePoints().get(j).y;
					g.lineTo(x, y);
				}
				g.moveTo(x0, y0);
				g.closePath();
				g.stroke();
			}
			else {
				g.arc(x0, y0, 1.5, 0, 1.5* Math.PI);
				g.closePath();
				g.stroke();
			}
		}
	}
	
	public int getStrokeCount() {
		return strokes.size();
	}
	
	public Stroke getStroke(int nr) {
		if(nr>-1 && strokes.size() > nr)
			return strokes.get(nr);
		return null;
	}
	
	public Stroke getLastStroke() {
		if(strokes.size() > 0)
			return strokes.get(strokes.size()-1);
		return null;
	}
	
	public void clear() {
		strokes.clear();
	}
	
	public void setIsJar(boolean b) {
		isJar = b;
	}
	
	public HashMap<String,Object> getState() {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		ArrayList<Map<String,Object>> strokeList = new ArrayList<Map<String,Object>>();
		ArrayList<Object> wmStrokeIndicesList = new ArrayList<Object>();
		ArrayList<String> wmStrokeTekenList = new ArrayList<String>();
		for (int i = 0; i < strokes.size(); i++) {
			strokeList.add(strokes.get(i).getState());
		}
		h.put("strokeList", strokeList);
		return h;
	}
	
	public void setState(Map<String,Object> map) {
		if(map == null || map.isEmpty())
			return;
		
		//logger.info(map.toString());
		ObjectMap launchState = JSONUtilities.wrapMap(map);
		List<Map<String,Object>> strokeList = new ArrayList<Map<String,Object>>();
		
		if (launchState.containsKey("strokeList"))
			strokeList = launchState.getMapList("strokeList");
		
		for (int i = 0; i < strokeList.size(); i++)	{	
			Stroke stroke = Stroke.setState(strokeList.get(i));
			if(stroke!=null)
				strokes.add(stroke);
		}
	}	
}
