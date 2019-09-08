package fi.wiskopdr;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JRadioButton;

public class WiskOpdrRadioButton extends JRadioButton {

  private Color bgColor = new Color(120,150,202);
  private Color fgColor = WiskOpdr.colorBlue1;
  private FontMetrics fm;
  private Font font = new Font("SansSerif", Font.BOLD, 13);
  
  public WiskOpdrRadioButton(String label) {
    super(label);
    setForeground(fgColor);
    setOpaque(false);
    setFont(font);
  }
  
  public WiskOpdrRadioButton(String label, boolean b) {
    super(label);
    setForeground(fgColor);
    setOpaque(false);
    setFont(font);
    setSelected(b);
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
        g.fillOval(7, m+5, 14, 14);
        g.setColor(WiskOpdr.colorBlue3);
        g.drawOval(7, m+5, 14, 14);
        
        if(isSelected()) {
          g.setColor(WiskOpdr.colorBlue3);
          g.fillOval(10, m+8, 8, 8);
         
          
        }
      }
      else {
        int m = (getHeight() - 24)/2-1;
        g.setColor(Color.white);
        g.fillOval(4, m+6, 13, 13);
        g.setColor(WiskOpdr.colorBlue3);
        g.drawOval(4, m+6, 13, 13);
        
        if(isSelected()) {
          g.setColor(WiskOpdr.colorBlue3);
          g.fillOval(7, m+9, 8, 8);
          
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
      bgColor = c;
      super.setBackground(c);
  }
  
  @Override
  public void setForeground(Color c) {
      fgColor = c;
      super.setForeground(c);
  }
  
}
