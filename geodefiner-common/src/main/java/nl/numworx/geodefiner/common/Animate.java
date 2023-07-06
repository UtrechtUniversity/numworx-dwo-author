package nl.numworx.geodefiner.common;

import fi.euclides.util.Messages;

public enum Animate {
	NONE,
	SEE,
	SAW,
	SEESAW;
	
	public String toString() {
		return Messages.getString(name());
	}

}
