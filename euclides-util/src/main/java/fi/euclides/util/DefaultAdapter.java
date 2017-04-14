package fi.euclides.util;


public class DefaultAdapter implements Adapter {

	Hashtable<Class<?>,Object> table = new Hashtable<Class<?>, Object>();
	
	@SuppressWarnings("unchecked")
	public <T> T adapt(Class<T> cls) {
		if(cls == getClass()) return (T) this;
		Object instance = table.get(cls);
		return (T) instance;
	}

	public void put(Object instance) {
		table.put(instance.getClass(), instance);
	}
	
	public <K> void put(Class<? super K> key, K value) {
		if(value == null) {
			table.remove(key);
		} else {
			table.put(key, value);
		}
	}
	
	public static DefaultAdapter getDefault(Observable d) {
		return getDefault( (Adaptee) d);
	}

	public static DefaultAdapter getDefault(Adaptee d) {
		DefaultAdapter result = d.getAdapter().adapt(DefaultAdapter.class);
		if(result == null)
		{
			result = new DefaultAdapter();
			d.setAdapter(result);
		}
		return result;
	}

}
