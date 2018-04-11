package fi.beans.iconan;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.StringTokenizer;

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
	private final Map<String,Object> map;
	private final Iconan parent;
    private SimpleSwingBrowser browser = new SimpleSwingBrowser();


	SVGStrategy(Iconan parent) {
		this.parent = parent;
		this.map = parent.getNamemap();
	}
	
	public int getHeight(String name) {
		Integer w = (Integer) map.get(name + "/h");
		if(w != null)
			return w.intValue();
		byte[] data = (byte[])map.get(name);
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
      Integer w = (Integer) map.get(name + "/w");
      if(w != null)
          return w.intValue();
      byte[] data = (byte[])map.get(name);
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
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(input);
    Element svg = doc.getDocumentElement();
    String width = svg.getAttributeNS(SVG, "width");
    if(width.isEmpty()) width = svg.getAttributeNS(null, "width");
    if (!width.isEmpty()) {
      result.width = Integer.parseInt(width);
      map.put(name + "/w", result.width);
    }
    String height = svg.getAttributeNS(SVG, "height");
    if(height.isEmpty()) height = svg.getAttributeNS(null, "height");
    if (!height.isEmpty()) {
      result.height = Integer.parseInt(height);
      map.put(name + "/h", result.height);
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
            result.width = Integer.parseInt(width);
            map.put(name + "/w", result.width);
          }
          if(result.height <= 0) {
            result.height = Integer.parseInt(height);
            map.put(name + "/h", result.height);
          }
        }
      }
    }
    return result;
  }

  @Override
  public JComponent getPreviewPanel(String name) {
    InputStream in = new ByteArrayInputStream((byte[])map.get(name));
    try {
      byte[] data= new byte[in.available()];
      in.read(data);
      in.close();
      String content = new String(data, "UTF-8");
      browser.loadContent(content, "image/svg+xml");
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
 
    return browser.getBrowserPanel();
  }
	
}
