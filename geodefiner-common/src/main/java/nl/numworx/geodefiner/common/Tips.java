package nl.numworx.geodefiner.common;

import fi.euclides.util.Messages;

public enum Tips {
	NOTIP,
	ATEND,
	ATSTART,
	ATSTARTEND;
	
	public String toString() {
		return Messages.getString(name());
	}

}
