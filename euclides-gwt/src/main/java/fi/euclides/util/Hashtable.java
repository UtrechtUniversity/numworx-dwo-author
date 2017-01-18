package fi.euclides.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Hashtable<K,V> extends HashMap<K,V> {

	private static class Bridge implements Enumeration {

		private Iterator iterator;

		private Bridge(Iterator iterator) {
			this.iterator = iterator;
		}

		public boolean hasMoreElements() {
			return iterator.hasNext();
		}

		public Object nextElement() {
			return iterator.next();
		}
	}

	public Hashtable() {
		// TODO Auto-generated constructor stub
	}

	public Hashtable(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Hashtable(Map<? extends K, ? extends V> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Hashtable(int arg0, float arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public Enumeration keys() {
		// TODO Auto-generated method stub
		return new Bridge(keySet().iterator());
	}

	public Enumeration elements() {
		return new Bridge(values().iterator());
	}

}
