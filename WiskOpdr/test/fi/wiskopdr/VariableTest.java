package fi.wiskopdr;

import static org.junit.Assert.assertEquals;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class VariableTest {

  @Test
  public void test() {
    VariableCollection vc = new VariableCollection();
    vc.setVariables("a~1..4");
    Set set = new TreeSet();
    for(int i = 0; i < 4 ; i++)
    {
      System.out.println(i);
      Hashtable r = vc.getRandomValues();
      System.out.println(r);
      set.addAll(r.values());
    }
    assertEquals(4, set.size()); 
  }

}
