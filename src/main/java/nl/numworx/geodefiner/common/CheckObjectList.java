package nl.numworx.geodefiner.common;

import java.util.Vector;

import fi.euclides.event.Tracker;
import fi.euclides.model.Label;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class CheckObjectList implements Observer {
	protected final Vector<CheckObject> list = new Vector<CheckObject>();

	final private Tracker tracker;
	private Expression expression;
	
	public CheckObjectList(Tracker tracker) {
		this.tracker = tracker;
		expression = new Expression(tracker);
	}

	public int getSize() {
		return list.size();
	}

	public void addElement(CheckObject obj) {
		list.addElement(obj);
	}

	public CheckObject remove(int index) {
		return list.remove(index);
	}

	public CheckObject getElementAt(int index) {
		return list.elementAt(index);
	}
	public void clear() {
		int s = getSize();
		for(int i = 0; i < s; i++) {
			CheckObject co = list.elementAt(i);
			if(co.cache != null) {
				co.cache.deleteObserver(this);
				co.cache = null;
				co.present = Label.FALSE;
			}
		}
		list.clear();
	}

	@Override
	public void update(Observable observable, Object arg) {
		
	}
	
	
}
