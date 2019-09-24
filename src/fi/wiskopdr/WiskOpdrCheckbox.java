package fi.wiskopdr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class WiskOpdrCheckbox extends JCheckBox {

  private Color bgColor = new Color(120,150,202);
  private Color fgColor = WiskOpdr.colorBlue1;
  private FontMetrics fm;
  private Font font = new Font("SansSerif", Font.PLAIN, 12);
  
  public WiskOpdrCheckbox(String label) {
    super(label);
    setForeground(fgColor);
    setOpaque(false);
    setFont(font);
  }
  
  @Override
  protected void paintComponent(Graphics gr) {
    Graphics2D g = (Graphics2D)gr;
    ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
      super.paintComponent(g);
      if(WiskOpdr.mac) {
        int m = (getHeight() - 24)/2-1;
        g.setColor(Color.white);
        g.fillRect(6, m+5, 15, 15);
        g.setColor(bgColor);
        g.drawRect(6, m+5, 15, 15);
        
        if(isSelected()) {
          g.setColor(bgColor);
          g.fillRect(6, m+5, 15, 15);
          g.setColor(Color.white);
          g.setFont(font);
          g.drawString("v",10,m+16);
        }
      }
      else {
        int m = (getHeight() - 24)/2-1;
        g.setColor(Color.white);
        g.fillRect(4, m+6, 13, 13);
        g.setColor(bgColor);
        g.drawRect(4, m+6, 13, 13);
        
        if(isSelected()) {
          g.setColor(bgColor);
          g.fillRect(4, m+6, 13, 13);
          g.setColor(Color.white);
          g.setFont(font);
          g.drawString("v",7,m+17);
        }
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
      //bgColor = c;
      super.setBackground(c);
  }
  
  @Override
  public void setForeground(Color c) {
      fgColor = c;
      super.setForeground(c);
  }
  
  


}

  
  


