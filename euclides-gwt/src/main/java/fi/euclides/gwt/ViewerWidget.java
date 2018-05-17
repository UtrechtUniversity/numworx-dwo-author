package fi.euclides.gwt;

import com.google.gwt.user.client.ui.IsWidget;

import fi.euclides.event.NameMapper;
import fi.euclides.model.AbstractViewer;
import fi.euclides.event.EventHandler;

public interface ViewerWidget extends IsWidget {

	AbstractViewer getViewer();
	EventHandler   getPanHandler();
	
	void processMouseDown(int x, int y, int id);

	void processMouseUp(int x, int y, int id);

	void processMouseDrag(int x, int y, int id);
	
	void init(int w, int h);
	
	void moveAway(int x, int y);
	void moveBack();

	void setMapper(NameMapper mapper);

	void drawLine(double x1, double y1, double x2, double y2);
	void setColor(int c);

	String TEXT_BOTTOM = "text-after-edge";
	String TEXT_TOP = "text-before-edge";
	String TEXT_CENTRAL = "central";
	String TEXT_START = "start";
	String TEXT_MIDDLE = "middle";
	String TEXT_END = "end";
	void drawString(String string, double x, double y, String textEnd,
			String textTop, String background);
	void setBackground(String string);

}
