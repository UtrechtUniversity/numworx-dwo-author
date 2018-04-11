package fi.beans.iconan;

import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JApplet;
import javax.swing.JFrame;

import fi.wiskopdr.SimpleSwingBrowser;

public class SVGView extends JApplet {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static void main(String[] args) {
      SVGView v = new SVGView();
      JFrame frame = new JFrame("SVG Viewer");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(v);
      v.setPreferredSize(new Dimension(300,200));
      v.init();
      v.start();
      frame.pack();
      frame.setVisible(true);
  }

  
  public void init() {
    SimpleSwingBrowser browser = new SimpleSwingBrowser();
    setContentPane(browser.getBrowserPanel());
    InputStream in = getClass().getResourceAsStream("easypeasy.svg");
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
    
  }
}
