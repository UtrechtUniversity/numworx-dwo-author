package fi.euclides.formuleobjects;

import static org.junit.Assert.*;

import org.junit.Test;

import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;

public class Parser4Test {

  @Test
  public void testsin() throws ParseException {
    String test = "sin$m-1@x";
    test(test, "arcsin");
  }

  @Test
  public void testcos() throws ParseException {
    String test = "cos$m-1@x";
    test(test, "arccos");
  }
  @Test
  public void testtan() throws ParseException {
    String test = "tan$m-1@x";
    test(test, "arctan");
  }

  @Test
  public void testlog() throws ParseException {
    String test = "log$m-1@x";
    test(test, "power");
  }
  @Test
  public void testpower() throws ParseException {
    String test = "$psin x$n-1@@";
    test(test, "power");
  }

  
  private void test(String test, String string) throws ParseException {
    FormuleParser p = new FormuleParser("y=" + test);
    OMObject o = p.parse();
    OMApplication a = (OMApplication) o;
    a = (OMApplication) a.getElementAt(2);
    o = a.firstElement();
    OMSymbol f = (OMSymbol) o;
    assertEquals(string, string, f.getName());
  }

}
