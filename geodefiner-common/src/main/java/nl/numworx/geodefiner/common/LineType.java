package nl.numworx.geodefiner.common;

import fi.euclides.util.Messages;

public enum LineType {
	SOLID,
	DOTTED,
	DASHED,
	DASHDOTTED;
	
	public String toString() {
		return Messages.getString(name());
	}

}
