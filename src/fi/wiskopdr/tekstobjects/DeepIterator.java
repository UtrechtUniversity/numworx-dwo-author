package fi.wiskopdr.tekstobjects;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Depth first iterator on Maps.
 * @author wim
 *
 */
class DeepIterator implements Iterator<Map<?,?>> {

	private Iterator<?> values;
	private DeepIterator stack;
	private Map<?,?> next;
	
	public DeepIterator(Map<?,?> map) {
      values = map.values().iterator();
  }

  @Override
	public boolean hasNext() {
      if (stack != null) {
        boolean hasNext = stack.hasNext();
        if (hasNext) return hasNext;
        stack = null;
      }
      if (next != null) return true;
 
      while(values.hasNext()) {
        Object o = values.next();
        if (o instanceof Map) {
          next = (Map<?, ?>) o;
          stack = new DeepIterator(next);
          return true;
        }
      }
      return false;   
	}

	@Override
	public Map<?,?> next() {
		if (stack != null) {
		  if (stack.hasNext()) return stack.next();
		}
		if (next != null) {
		  Map m = next; next = null; return m;
		}
		if (hasNext()) return next(); // recurse		
		throw new NoSuchElementException("DeepIterator exhausted");
	}
	
}