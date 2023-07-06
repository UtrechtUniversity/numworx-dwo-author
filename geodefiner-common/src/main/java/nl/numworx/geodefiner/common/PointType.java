package nl.numworx.geodefiner.common;

import fi.euclides.util.Messages;

public enum PointType {
	DISK, 
	CIRCLE;

	public String toString() {
		return Messages.getString(name());
	}
}