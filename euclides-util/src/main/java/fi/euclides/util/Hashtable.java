package fi.euclides.util;

import java.util.Map;

@SuppressWarnings("serial")
public class Hashtable<K,V> extends java.util.Hashtable<K,V> {


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

}
