package fi.wiskopdr.tekstobjects;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import fi.beans.base64code.StringCodeObject;
import junit.framework.TestCase;

public class DeepIteratorTest extends TestCase {

  
  public void testHasNext() {
    DeepIterator i;
    i = new DeepIterator(Collections.EMPTY_MAP);
    assertFalse ("empty" , i.hasNext());
    i = new DeepIterator(Collections.singletonMap("x", "oops"));
    assertFalse ("single item" , i.hasNext());
    i = new DeepIterator(Collections.singletonMap("x", Collections.EMPTY_MAP));
    assertTrue("map", i.hasNext());
    assertEquals("empty map", Collections.EMPTY_MAP, i.next());
    assertFalse("next to map", i.hasNext());  
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public void testNext() {
    DeepIterator i;
    HashMap h = new LinkedHashMap();
    h.put("h", Collections.EMPTY_MAP);
    h.put("hh", Collections.EMPTY_MAP);
    h.put("nono", "none");
    i = new DeepIterator(Collections.singletonMap("hhh", h));
    assertEquals(Collections.EMPTY_MAP, i.next());
    assertEquals(Collections.EMPTY_MAP, i.next());
    assertEquals(h, i.next());
    assertFalse(i.hasNext());
  
  }

  public void testCollections() { 
    DeepIterator i;
    HashMap h = new LinkedHashMap();
    h.put("h",  Arrays.asList(Collections.EMPTY_MAP));
    h.put("hh", new Object[] { Collections.EMPTY_MAP, "oops", Collections.EMPTY_MAP });
    h.put("hhh", StringCodeObject.encodeObjectToString(Collections.EMPTY_MAP));
    i = new DeepIterator(h);
    assertEquals(Collections.EMPTY_MAP, i.next());
    assertEquals(Collections.EMPTY_MAP, i.next());
    assertEquals(Collections.EMPTY_MAP, i.next());
    assertEquals(Collections.EMPTY_MAP, i.next());
    assertFalse(i.hasNext());
    
  }
  
  
}
