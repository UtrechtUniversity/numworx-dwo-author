package fi.ivmdrawgwt.client;

import java.util.ArrayList;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;



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
	
	public void clear() {
		strokes.clear();
	}
	
	public void setIsJar(boolean b) {
		isJar = b;
	}
	
}
