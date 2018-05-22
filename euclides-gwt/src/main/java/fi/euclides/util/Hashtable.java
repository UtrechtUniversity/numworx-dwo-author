package fi.euclides.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Hashtable<K,V> extends HashMap<K,V> {

	private static class Bridge<T> implements Enumeration<T> {

		private Iterator<T> iterator;

		private Bridge(Iterator<T> iterator) {
			this.iterator = iterator;
		}

		public boolean hasMoreElements() {
			return iterator.hasNext();
		}

		public T nextElement() {
			return iterator.next();
		}
	}

	public Hashtable() {
	}

	public Hashtable(int arg0) {
		super(arg0);
	}

	public Hashtable(Map<? extends K, ? extends V> arg0) {
		super(arg0);
	}

	public Hashtable(int arg0, float arg1) {
		super(arg0, arg1);
	}

	public Enumeration<K> keys() {
		return new Bridge<>(keySet().iterator());
	}

	public Enumeration<V> elements() {
		return new Bridge<>(values().iterator());
	}

}
