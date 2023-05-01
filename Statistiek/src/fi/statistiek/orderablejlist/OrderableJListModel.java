package fi.statistiek.orderablejlist;

import java.util.ArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * 
 * Model class for OrderableJList
 * @author Manu Drijvers
 *
 */
public class OrderableJListModel implements ListModel {
	private ArrayList data;
	private ArrayList<ListDataListener> listeners;
	
	/**
	 * Constructor
	 * @param data initial data
	 */
	public OrderableJListModel(ArrayList data) {
		this.data = data;
		this.listeners = new ArrayList<ListDataListener>();
	}
	
	public void addListDataListener(ListDataListener arg0) {
		this.listeners.add(arg0);		
	}

	public Object getElementAt(int arg0) {
		return data.get(arg0);
	}

	public int getSize() {
		return data.size();
	}

	public void removeListDataListener(ListDataListener arg0) {
		this.listeners.remove(arg0);
	}
	
	/**
	 * Move an element in list
	 * @param oldIndex index of the object that will be moved
	 * @param newIndex the index that the object will be moved to
	 */
	public void reorder(int oldIndex, int newIndex) {
		if(newIndex < 0) {
			System.out.println("newIndex < 0!");
			newIndex = 0;
		}
		
		//String s = this.d.get(oldIndex);
		Object o = this.data.get(oldIndex);
		//this.names.remove(oldIndex);
		this.data.remove(oldIndex);
		if(oldIndex < newIndex) {
			newIndex--;
		}
		//this.names.add(newIndex, s);
		this.data.add(newIndex, o);
		
		this.fireEvent();
	}
	
	/**
	 * Fire an event to all listeners
	 */
	private void fireEvent() {
		for(ListDataListener listener : this.listeners) {
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this.data.size()));
		}
	}
}
