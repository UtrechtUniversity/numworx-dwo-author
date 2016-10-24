package nl.numworx.geodefiner;

import fi.euclides.model.Destroyable;

class CELL {
	final String text;
	final Destroyable item;
	
	CELL(String text, Destroyable item) {
		super();
		this.text = text;
		this.item = item;
	}

	public String toString() {
		return item + ": " + text;
	}
	
}