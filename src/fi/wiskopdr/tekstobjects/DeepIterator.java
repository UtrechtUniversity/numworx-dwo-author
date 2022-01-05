package fi.wiskopdr.tekstobjects;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import fi.beans.base64code.StringCodeObject;

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
	public DeepIterator(Iterable<?> iterable) {
	  values = iterable.iterator();
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
        if (o instanceof String  && o.toString().startsWith("H4sIA")) {
          o  = StringCodeObject.decodeStringToObject((String) o);
        }
        if (o instanceof Map) {
          next = (Map<?, ?>) o;
          stack = new DeepIterator(next);
          return true;
        }
        if (o instanceof Object[]) o = Arrays.asList((Object[]) o);
        if (o instanceof Iterable) {
          stack = new DeepIterator( (Iterable<?>) o);
          return hasNext();
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