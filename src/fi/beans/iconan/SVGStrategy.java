package fi.beans.iconan;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fi.wiskopdr.SimpleSwingBrowser;

public class SVGStrategy implements Strategy {
	
    
  
  
  
  
	private static final String SVG = "http://www.w3.org/2000/svg";
	private final Iconan parent;
    private SimpleSwingBrowser _browser;
    /**
     * Lqzy initialization
     * @return svg browser
     */
    private synchronized SimpleSwingBrowser getBrowser() {
      if(_browser==null) _browser = new SimpleSwingBrowser();
      return _browser;
    }
    

	@Override
    public void dispose() {
      if (_browser != null) {
        _browser.dispose();
        _browser = null;
      }
    }

  SVGStrategy(Iconan parent) {
		this.parent = parent;
	}
	
	public int getHeight(String name) {
      Integer w = (Integer) parent.namemap.get(parent.suffix(name) + "/h");
      if (w == null)
        w = (Integer) parent.namemap.get(parent.strip(name) + "/h");
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
      Integer w = (Integer) parent.namemap.get(parent.suffix(name) + "/w");
      if (w == null)
        w = (Integer)parent.namemap.get(parent.strip(name) + "/w");
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

  
  private JComponent getPreviewPanel(String name, SimpleSwingBrowser simpleSwingBrowser) {
    InputStream in = new ByteArrayInputStream((byte[])parent.namemap.get(name));
// add unzip?
    //in = new GzipInputStream(in);
    try {
      byte[] data= new byte[in.available()];
      in.read(data);
      in.close();
      String content = new String(data, "UTF-8");
      simpleSwingBrowser.loadContent(content, "image/svg+xml");
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
 
    return simpleSwingBrowser.getBrowserPanel();
  }

  @Override
  public JComponent getComponent(String name) {
    SimpleSwingBrowser browser = new SimpleSwingBrowser();
    JComponent result = getPreviewPanel(name, browser);
    result.setSize(Math.max(getWidth(name),0), Math.max(getHeight(name),0));
    result.setPreferredSize(result.getSize());
    return result;
  }

  @Override
  public Icon getIcon(final String name) {
    
    SimpleSwingBrowser browser = new SimpleSwingBrowser();
    JComponent component = getPreviewPanel(name, browser);
    
    return new Icon() {
      {
        component.setSize(getIconWidth(), getIconHeight());
        component.doLayout();
       
      }
      @Override
      public void paintIcon(Component c, Graphics g, int x, int y) {
        browser.setRepaintObserver(c);
        g = g.create();
        g.translate(x, y);
        g.clipRect(0, 0, getIconWidth(), getIconHeight());
        component.print(g);
        g.dispose();
      }

      @Override
      public int getIconWidth() {
        return Math.max(1, getWidth(name));
      }

      @Override
      public int getIconHeight() {
        return Math.max(getHeight(name),1);
      } };
  }
	
}
