package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.*;

public class HelpBrowser extends JPanel
{
  //private DialogFacade frame;

  private JComponent panel;
  private int width = 300;
  private int height = 500;

  private SimpleSwingBrowser ssb;
  private Component src;

  public boolean hasPanel() {
    return ssb != null;
  }

  public HelpBrowser(Component c){
    super(null);
    src = c;
    try {
      ssb = new SimpleSwingBrowser();
      panel = ssb.getBrowserPanel();
      panel.invalidate();
      panel.setMinimumSize(new Dimension(10,10));
      panel.setBounds(0, 0, width, height); // o i d
      panel.validate(); 
      panel.doLayout();
      add(panel);
    } catch (Exception e) {
      width = 0;
      height = 0;
      panel = new JLabel("No inline browser"); // 
      panel.setSize(panel.getPreferredSize());
      add(panel);
    }
    setSize(width, height);
//    frame = DialogFacade.newInstance(c, "", true);
//
//
//    Dimension preferredSize = new Dimension(width,height);
//    frame.setPreferredSize(preferredSize);
//    frame.getContentPane().setLayout(new BorderLayout());
//    frame.getContentPane().add(panel);
  }
  
  public JComponent getBrowserPanel() {
	  return this;
  }

  
  public void loadURL(String url) {
    if (!hasPanel()) {
      try {
        if (url == null) return;
        java.awt.Desktop.getDesktop().browse(URI.create(url));
      } catch (Exception e) {
      }
      return;
    }
//    src = WiskOpdr.getWindowForComponent(src);
//    Dimension preferredSize = new Dimension(width,src.getHeight());
//    frame.setPreferredSize(preferredSize);
//    int x = src.getLocationOnScreen().x + src.getWidth() - width;
//    int y = src.getLocationOnScreen().y;
//    frame.setLocation(x,y);  


      ssb.loadURL(url);
      ssb.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, url));
//    frame.pack();
//    frame.setVisible(true);
  }

  public void dispose() {
    if (ssb != null) ssb.dispose();
    //frame.dispose();
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    if(ssb != null) panel.setSize(Math.max(10,width), Math.max(10, height));
  }
  
  public static void main(String[] args) {
	  JFrame f = new JFrame();
	  f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
	  HelpBrowser hb = new HelpBrowser(f);
	  hb.ssb.frame = f;
	  f.setContentPane(hb);
	  f.setSize(500,400);
	  f.setVisible(true);
	  hb.loadURL("https://app.dwo.nl/dwo/apps/player.html?t=1&profile=106&locale=nl#671367");
	 // hb.loadURL("https://app.dwo.nl");
	  
  }
  
  
}