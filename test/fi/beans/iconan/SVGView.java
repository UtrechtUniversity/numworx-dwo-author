package fi.beans.iconan;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JApplet;
import javax.swing.JComponent;
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
      v.setSize(v.getPreferredSize());
      v.init();
      v.start();
      frame.pack();
      frame.setVisible(true);
  }

  private Image image;
  private JComponent view;
  
  
  public void init() {
    SimpleSwingBrowser browser = new SimpleSwingBrowser();
    setContentPane(browser.getBrowserPanel());
    InputStream in = getClass().getResourceAsStream("easypeasy.svg");
    try {
      byte[] data= new byte[in.available()];
      in.read(data);
      in.close();
      String content = new String(data, "UTF-8");
      view = browser.getBrowserPanel();
      browser.loadContent(content, "image/svg+xml");
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    
  }

  public void initImage(JComponent view) {
    view.setSize(getSize()); view.validate();
    image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_USHORT_555_RGB);
    Graphics g = image.getGraphics();
    view.printAll(g);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
//    if (image == null) initImage(view);
//    g.drawImage(image, 0,0, this);
  }
  
  
  
}
