package nl.numworx.geodefiner;

import nl.numworx.geodefiner.ui.UIModel;
import fi.euclides.model.Destroyable;
import fi.euclides.util.DefaultAdapter;

public class CELL {
	public final String text;
	public final Destroyable item;
	public UIModel<?> config;
	
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