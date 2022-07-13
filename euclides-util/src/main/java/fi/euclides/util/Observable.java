package fi.euclides.util;

public class Observable extends java.util.Observable {

	/**
	 * Prototype pattern. Platform dependent
	 * @return a empty copy of this.
	 */
	public Observable newInstance() {
		try {
			return (Observable) getClass().newInstance();
		} catch (InstantiationException e) {
			throw new Error(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new Error(e.getMessage(), e);
		}
	}


	public int getIndex() {
		return 0;
	}

	public Adapter getAdapter() {
		return null;
	}

//	public void setAdapter(Adapter result) {
//	}

	public <T> T adapt(Class<T> clz) {
		Adapter a = getAdapter();
		if(a != null) return a.adapt(clz);
		return null;
	}
	
	public void addObserver(Observer observer) {
		super.addObserver(observer);
	}

	public void deleteObserver(Observer observer) {
		super.deleteObserver(observer);
	}

}
