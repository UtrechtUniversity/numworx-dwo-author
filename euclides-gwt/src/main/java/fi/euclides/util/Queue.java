package fi.euclides.util;

import java.util.Collection;
import java.util.LinkedList;

public class Queue extends LinkedList {

	public Queue() {
	}

	public Queue(Collection arg0) {
		super(arg0);
	}

	/**
	 * @since J2ME
	 * @param o
	 */
	public void addElement(Object o) {
		add(o);
	}

	/**
	 * @since 1.5
	 * @return head of queue
	 */
	public Object poll() {
		if(isEmpty())
			return null;
		return removeFirst();
	}

}
