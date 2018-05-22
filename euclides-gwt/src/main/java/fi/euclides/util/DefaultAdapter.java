package fi.euclides.util;

import java.util.HashMap;
import java.util.Map;

public class DefaultAdapter implements Adapter {

	private Map<Class<?>, Object> table = new HashMap<>();
	
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

	public static DefaultAdapter getDefault(Adaptee d) {
		DefaultAdapter result = d.getAdapter().adapt(DefaultAdapter.class);
		if(result == null)
		{
			result = new DefaultAdapter();
			d.setAdapter(result);
		}
		return result;
	}

  @Override
  public String toString() {
    return "DefaultAdapter [table=" + table + "]";
  }

}
