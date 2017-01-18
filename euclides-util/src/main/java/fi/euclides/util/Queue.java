package fi.euclides.util;

import java.util.Collection;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class Queue<T> extends LinkedList<T> {

	public Queue() {
	}

	public Queue(Collection<? extends T> arg0) {
		super(arg0);
	}

	/**
	 * @since J2ME
	 * @param o the element to add
	 */
	public void addElement(T o) {
		add(o);
	}

	/**
	 * @since 1.5
	 * @return head of queue
	 */
	public T poll() {
		if(isEmpty())
			return null;
		return removeFirst();
	}

}
