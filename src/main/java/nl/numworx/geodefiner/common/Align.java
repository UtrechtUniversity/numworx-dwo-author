package nl.numworx.geodefiner.common;

import fi.euclides.util.Messages;

public enum Align {
	BASE,
	LEFT, RIGHT,
	TOP , BOTTOM, NONE;
	
	public String toString() {
		return Messages.getString(name());
	}
}
