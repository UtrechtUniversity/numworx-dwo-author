package nl.numworx.geodefiner.common;

import nl.tue.win.riaca.openmath.lang.OMVariable;
import fi.euclides.model.Destroyable;
import fi.euclides.util.DefaultAdapter;

public class CELL {
	public String text;
	public Destroyable item;
	public UIModel<? extends Destroyable, ?> config;
	public String var;
	public Object extra;
	
	public CELL(String text, Destroyable item, OMVariable var) {
		this(text, item, var.getName());
	}
	
	public CELL(String text, Destroyable item, String var) {
		super();
		this.text = text;
		this.var = var;
		this.item = item;
		DefaultAdapter.getDefault(item).put(this); // backlink!
	}

	public String toString() {
		return item + ": " + text;
	}
	
}