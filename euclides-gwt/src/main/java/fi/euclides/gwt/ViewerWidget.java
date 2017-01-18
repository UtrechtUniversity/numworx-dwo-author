package fi.euclides.gwt;

import com.google.gwt.user.client.ui.IsWidget;

import fi.euclides.model.AbstractViewer;

public interface ViewerWidget extends IsWidget {

	AbstractViewer getViewer();

	void processMouseDown(int x, int y);

	void processMouseUp(int x, int y);

	void processMouseDrag(int x, int y);
	
	void init(int w, int h);
}
