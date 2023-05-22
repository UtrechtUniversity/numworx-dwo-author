package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

public class HelpBrowser extends JPanel
{
  //private DialogFacade frame;

  private JComponent panel;
  private int width = 300;
  private int height = 500;

  private SimpleSwingBrowser ssb;
  private Component src;


  public HelpBrowser(Component c){
    super(null);
    src = c;
    ssb = new SimpleSwingBrowser();
    panel = ssb.getBrowserPanel();
    panel.invalidate();
    panel.setMinimumSize(new Dimension(10,10));
    panel.setBounds(0, 0, width, height); // o i d
    panel.validate(); 
    panel.doLayout();
    add(panel);
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
//    src = WiskOpdr.getWindowForComponent(src);
//    Dimension preferredSize = new Dimension(width,src.getHeight());
//    frame.setPreferredSize(preferredSize);
//    int x = src.getLocationOnScreen().x + src.getWidth() - width;
//    int y = src.getLocationOnScreen().y;
//    frame.setLocation(x,y);  


    ssb.loadURL(url);
//    frame.pack();
//    frame.setVisible(true);
  }

  public void dispose() {
    ssb.dispose();
    //frame.dispose();
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    panel.setSize(Math.max(10,width), Math.max(10, height));
  }
}