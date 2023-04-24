package fi.beans.lwmobjects_swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import fi.beans.lwmobjects_swing.LWMObject;

/**
 * Adapt een java.awt.Component naar een immovable LWMObject.
 * Adapter pattern.
 * @author wim
 * @since 1.2
 */

public class LWMAdapter extends LWMObject {

	public LWMAdapter(Component component) {
		super(component.getWidth(), component.getHeight());
		setLayout(new GridLayout(1,1));
		add(component);
		//setMovable(false);
	}

	/**
	 * Paint lightweight component.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		paintComponents(g);
	}

	public Dimension getPreferredSize() {
		return getComponent(0).getPreferredSize();
	}


	
}
