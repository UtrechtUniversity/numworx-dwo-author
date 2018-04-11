package fi.beans.iconan;

import java.io.InputStream;

import junit.framework.TestCase;

public class SVGTest extends TestCase {

  public void testHeight() throws Exception {
    Iconan ic = new Iconan();
    SVGStrategy s = new SVGStrategy(ic);
    InputStream in = getClass().getResourceAsStream("ellipse.svg");
    byte[] data = new byte[in.available()];
    in.read(data);
    in.close();
    ic.getNamemap().put("ellipse", data);
    assertEquals("height", 150, s.getHeight("ellipse"));
    assertEquals("map", Integer.valueOf(150), ic.getNamemap().get("ellipse/h"));
    
  }
  public void testWidth() throws Exception {
    Iconan ic = new Iconan();
    SVGStrategy s = new SVGStrategy(ic);
    InputStream in = getClass().getResourceAsStream("ellipse.svg");
    byte[] data = new byte[in.available()];
    in.read(data);
    in.close();
    ic.getNamemap().put("ellipse", data);
    assertEquals("width", 500, s.getWidth("ellipse"));
    assertEquals("map", Integer.valueOf(500), ic.getNamemap().get("ellipse/w"));
    
  }
}
