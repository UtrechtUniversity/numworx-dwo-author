package fi.euclides.util;

import java.util.Vector;

import fi.euclides.gwt.CreateUtil;

public class Observable implements Adaptee {

	private boolean changed;
	private Vector<Observer> obs  = new Vector<Observer>();
	
	protected void setChanged() {
		changed = true;
	}
	
	public <T> T adapt(Class<T> clz) {
		Adapter a = getAdapter();
		if(a != null) return a.adapt(clz);
		return null;
	}

	protected void notifyObservers(Object arg) {
		if(!changed)
			return;
		changed = false;
		Observer[] observers = new Observer[obs.size()];
		obs.copyInto(observers);
		for (int i = 0; i < observers.length; i++) {
			observers[i].update(this,arg);
		}
		
	}
	public void notifyObservers() {
		notifyObservers(null);
	}
	public void addObserver(Observer observer) {
		if(!contains(observer))
			obs.addElement(observer);
		
	}


	/**
	 * contains met identity equality.
	 * @param observer
	 * @return true if observer == obs.element
	 */
	private boolean contains(Observer observer) {
		int len = obs.size();
		for(int i = 0; i < len; i++)
		{
			if( observer == obs.get(i))
				return true;
		}
		return false;
	}
	
	public void deleteObserver(Observer observer) {
		int len = obs.size();
		for(int i = 0 ; i < len; i++)
			if(observer == obs.get(i))
			{	obs.remove(i);
				break;
			}
	}

	protected String key() { throw new Error("wrong type"); }

	public Observable newInstance() {
		return CreateUtil.create(key());
	}


	public int getIndex() {
		return 0;
	}


	public Adapter getAdapter() {
		return null;
	}


	public void setAdapter(Adapter result) {
	}
	
}
