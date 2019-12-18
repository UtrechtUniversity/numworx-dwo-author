package fi.wiskopdr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;

public class WiskOpdrComboBox extends JComboBox {

  private Color bgColor = WiskOpdr.colorBlue3;
  private Color fgColor = Color.white;
  private FontMetrics fm;
  private Font font = new Font("SansSerif", Font.PLAIN, 12);
  
  public WiskOpdrComboBox() {
    super();
    setForeground(WiskOpdr.colorBlue1);
    //setPreferredSize(new Dimension(50,22));
    //setMaximumSize(new Dimension(250,22));
    setOpaque(false);
    setFont(font);
    Component c0 = this.getComponent(0);
    c0.setVisible(false);
    Component c1 = this.getComponent(1);
    c1.setVisible(false);
    
    JPopupMenu popup = this.getComponentPopupMenu();
    if(popup!=null) {
      popup.setBorder(BorderFactory.createLineBorder(WiskOpdr.colorBlue3));
      popup.setBackground(WiskOpdr.colorGray3);
    }
  }
  
  public void addItem(Object item) {
	  super.addItem(item);
	  setPreferredSize(new Dimension(super.getPreferredSize().width , 22));
	  setMaximumSize(new Dimension(250 , 22));
  }
  
  @Override
  public void paintComponent(Graphics gr) {
    
      Graphics2D g = (Graphics2D)gr;
      ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
      
      
      //super.paintComponent(g);
      if(WiskOpdr.mac) {
        g.setColor(Color.white);
        g.fillRect(0,0,getWidth(), getHeight());
        g.setColor(bgColor);
        g.drawRect(0,0,getWidth()-1, getHeight()-1);
        
       
        
        g.setFont(font);
        String s = (String)this.getSelectedItem();
        fm = this.getFontMetrics(getFont());
        int stringwidth = fm.stringWidth(s);
        int stringheight = fm.getAscent()-fm.getDescent();
        g.setColor(WiskOpdr.colorBlue1);
        g.drawString(s,2 , getHeight()/2 + (stringheight)/2);
        
        g.setColor(WiskOpdr.colorBlue3);
        g.fillRect(getWidth()-getHeight(),0,getHeight(), getHeight());
        String button = "▾";
        g.setColor(Color.white);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString(button,getWidth()-getHeight()+5 , getHeight()/2 + (stringheight)/2+2);
      }
      else {
        g.setColor(Color.white);
        g.fillRect(0,0,getWidth(), getHeight());
        g.setColor(bgColor);
        g.drawRect(0,0,getWidth()-1, getHeight()-1);
        
       
        
        g.setFont(font);
        String s = (String)this.getSelectedItem();
        fm = this.getFontMetrics(getFont());
        int stringwidth = fm.stringWidth(s);
        int stringheight = fm.getAscent()-fm.getDescent();
        g.setColor(WiskOpdr.colorBlue1);
        g.drawString(s,2 , getHeight()/2 + (stringheight)/2);
        
        g.setColor(WiskOpdr.colorBlue3);
        g.fillRect(getWidth()-getHeight(),0,getHeight(), getHeight());
        String button = "▾";
        g.setColor(Color.white);
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString(button,getWidth()-getHeight() , getHeight()/2 + (stringheight)/2+5);
      }
        
      
//      fm = this.getFontMetrics(getFont());
//      String text = getText();
//      int stringwidth = fm.stringWidth(text);
//      int stringheight = fm.getAscent()-fm.getDescent();
//      g.setColor(fgColor);
//      g.drawString(text,(getWidth()-stringwidth)/2 , getHeight()/2 + (stringheight)/2);
  
     
  }
  @Override
  public void setBackground(Color c) {
      bgColor = c;
      super.setBackground(c);
  }
  
  @Override
  public void setForeground(Color c) {
      fgColor = c;
      super.setForeground(c);
  }
  
  
  
}
