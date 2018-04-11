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
  
  public void testViewBox() throws Exception {
    Iconan ic = new Iconan();
    SVGStrategy s = new SVGStrategy(ic);
    InputStream in = getClass().getResourceAsStream("easypeasy.svg");
    byte[] data = new byte[in.available()];
    in.read(data);
    in.close();
    ic.getNamemap().put("easypeasy", data);
    assertEquals("height", 91, s.getHeight("easypeasy"));
    assertEquals("map/h", Integer.valueOf(91), ic.getNamemap().get("easypeasy/h"));
    assertEquals("map/w", Integer.valueOf(110), ic.getNamemap().get("easypeasy/w"));
  }

}
