package fi.euclides.swing;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import fi.euclides.event.EventHandler;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class XXXAction extends AbstractAction implements Observer {
	
	protected static final int SIZE = 24;
	protected EventHandler handler;
	public Cursor  cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	protected AWTViewer viewer;

	protected Icon reduced(String resource)
	{
			ImageIcon icon = new ImageIcon(getClass().getResource("/fi/euclides/resources" + resource));
			Image image = icon.getImage();
			image = image.getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH);
			icon.setImage(image);
			return icon;
	}

	public XXXAction(String name, String icon, EventHandler handler, AWTViewer viewer)
	{
		super(name);
		if(icon!=null)
		{ 
			Icon icn = reduced(icon);
			putValue(SMALL_ICON, icn);
			putValue(LARGE_ICON_KEY, icn);
			
		} 
		putValue(SHORT_DESCRIPTION, name);
		this.handler = handler;
		handler.setTracker(viewer);
		this.viewer = viewer;
		viewer.getModel().addObserver(this);
		viewer.addObserver(this);
	}

	public void actionPerformed(ActionEvent e) {
		viewer.setCursor(cursor);
		handler.command();
		viewer.paint();
	}

	public void update(Observable observable, Object arg) {
		if (observable == viewer) {
			viewer.getModel().addObserver(this);
		}  
		Vector selection = viewer.getModel().getSelect();
		setEnabled(handler.allowSelection(selection));
	}

}
