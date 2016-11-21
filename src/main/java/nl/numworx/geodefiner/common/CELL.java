package nl.numworx.geodefiner.common;

import fi.euclides.model.Destroyable;
import fi.euclides.util.DefaultAdapter;

public class CELL {
	public final String text;
	public Destroyable item;
	public UIModel<?, ?> config;
	
	public CELL(String text, Destroyable item) {
		super();
		this.text = text;
		this.item = item;
		DefaultAdapter.getDefault(item).put(this); // backlink!
	}

	public String toString() {
		return item + ": " + text;
	}
	
}