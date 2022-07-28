package fi.beans.iconan;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fi.wiskopdr.SimpleSwingBrowser;
import nl.numworx.swingbrowser.api.SwingBrowser;

public class SVGStrategy implements Strategy {
	
    
    class ScaledImageComponent extends ImageComponent {

      final int width, height;
      ScaledImageComponent(BufferedImage image, int width, int height) {
        super(image, width, height);
        this.height = image.getHeight();
        this.width = image.getWidth();
      }

      @Override
      public void paintComponent(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        int x = 0;
        int y = 0;
        if (w * height > h * width) {
          int d = w - h * width / height;
          x = d/2;
          w = w - d/2;
        } else {
          int d = h - w * height / width;
          y = d/2;
          h = h - d/2;
        }
        g.drawImage(image, x, y, w, h, 0, 0, width, height, this);
      }
      
    }
  
  
  
	private static final String SVG = "http://www.w3.org/2000/svg";
	private final Iconan parent;
    private SwingBrowser _browser;
    /**
     * Lqzy initialization
     * @return svg browser
     */
    private synchronized SwingBrowser getBrowser() {
      if(_browser==null) _browser = SimpleSwingBrowser.BROWSER_PROVIDER.getFactory().newBrowser();
      return _browser;
    }
    

	@Override
    public void dispose() {
      if (_browser != null) {
        try {
          _browser.close();
        } catch (IOException e) {
        }
        _browser = null;
      }
    }

  SVGStrategy(Iconan parent) {
		this.parent = parent;
	}
	
	public int getHeight(String name) {
      Integer w = parent.getSuffix(name, "/h");
	  if(w != null)
			return w.intValue();
		byte[] data = (byte[])parent.namemap.get(name);
		if (data == null) return -1;
		try {
			return parseData(data, name).height;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
    public int getWidth(String name) {
      Integer w = parent.getSuffix(name, "/w");
      if(w != null)
          return w.intValue();
      byte[] data = (byte[])parent.namemap.get(name);
      if (data == null) return -1;
      try {
          return parseData(data, name).width;
      } catch (ParserConfigurationException | SAXException | IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      return -1;
  }

  private Dimension parseData(byte[] data, String name)
      throws ParserConfigurationException, SAXException, IOException {
    name = parent.strip(name);
    Dimension result = new Dimension(-1, -1);
    ByteArrayInputStream in = new ByteArrayInputStream(data);
    InputSource input = new InputSource(in);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    dbFactory.setValidating(false);
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(input);
    Element svg = doc.getDocumentElement();
    String width = svg.getAttributeNS(SVG, "width");
    if(width.isEmpty()) width = svg.getAttributeNS(null, "width");
    if (!width.isEmpty()) {
      if(width.endsWith("px"))
        width = width.substring(0, width.length()-2);
      result.width = Integer.parseInt(width);
      parent.namemap.put(name + "/w", result.width);
    }
    String height = svg.getAttributeNS(SVG, "height");
    if(height.isEmpty()) height = svg.getAttributeNS(null, "height");
    if (!height.isEmpty()) {
      if(height.endsWith("px"))
        height = height.substring(0, height.length()-2);
      result.height = Integer.parseInt(height);
      parent.namemap.put(name + "/h", result.height);
    }
    if (result.width <=0 || result.height <= 0) {
      String viewBox = svg.getAttributeNS(SVG, "viewBox");
      if(viewBox.isEmpty()) viewBox = svg.getAttributeNS(null, "viewBox");
      if(!viewBox.isEmpty()) {
        StringTokenizer st = new StringTokenizer(viewBox, " ,");
        if(st.countTokens() == 4) {
          st.nextToken(); st.nextToken();
          width = st.nextToken();
          height = st.nextToken();
          if(result.width <= 0) {
            result.width = Math.round(Float.parseFloat(width));
            parent.namemap.put(name + "/w", result.width);
          }
          if(result.height <= 0) {
            result.height = Math.round(Float.parseFloat(height));
            parent.namemap.put(name + "/h", result.height);
          }
        }
      }
    }
    return result;
  }

  @Override
  public JComponent getPreviewPanel(String name) {
    return getPreviewPanel(name, getBrowser());
  }

  
  private ImageComponent getPreviewPanel(String name, SwingBrowser simpleSwingBrowser) {
    String sname = parent.strip(name);
    Image im = parent.imagemap.get(sname);
    if (im instanceof BufferedImage) {
      BufferedImage buf = (BufferedImage)im;
      return new ScaledImageComponent(buf, buf.getWidth(), buf.getHeight());      
    }
    
    
    InputStream in = new ByteArrayInputStream((byte[])parent.namemap.get(sname));
// add unzip?
    //in = new GzipInputStream(in);
    try {
      byte[] data= new byte[in.available()];
      in.read(data);
      in.close();
      String content = new String(data, StandardCharsets.UTF_8);
      int w = getWidth(sname);
      int h = getHeight(sname);
      simpleSwingBrowser.setSize(w, h);
      simpleSwingBrowser.loadContentAndWait(content, "image/svg+xml");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    Optional<BufferedImage> bitmap = simpleSwingBrowser.bitmap();
    BufferedImage buf;
    if (bitmap.isPresent()) {
      buf = bitmap.get();
      parent.imagemap.put(sname, buf);
    } else 
    {
      System.err.println("Failure for " + name);
      buf = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
    }
    
    
    
    return new ScaledImageComponent(buf, buf.getWidth(), buf.getHeight());
    
  }

  @Override
  public JComponent getComponent(String name) {
    JComponent result = getPreviewPanel(name, getBrowser());
    result.setSize(Math.max(getWidth(name),0), Math.max(getHeight(name),0));
    result.setPreferredSize(result.getSize());
    return result;
  }

  @Override
  public Icon getIcon(final String name) {
    
    ImageComponent component = getPreviewPanel(name, getBrowser());
    Image image = component.image;
    int w = getWidth(name);
    int h = getHeight(name);
    if(w > 0 && h > 0) {
        image = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    }
    return new ImageIcon(image);
  }
	
}
