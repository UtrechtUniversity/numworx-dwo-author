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
  private Color fgColor = Color.white;
  private FontMetrics fm;
  private Font font = new Font("SansSerif", Font.BOLD, 13);
  
  public WiskOpdrRadioButton(String label) {
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
        g.setColor(Color.white);
        g.fillOval(7, 5, 14, 14);
        g.setColor(bgColor);
        g.drawOval(7, 5, 14, 14);
        
        if(isSelected()) {
          g.setColor(bgColor);
          g.fillOval(10, 8, 8, 8);
         
          
        }
      }
      else {
        g.setColor(Color.white);
        g.fillOval(4, 6, 13, 13);
        g.setColor(bgColor);
        g.drawOval(4, 6, 13, 13);
        
        if(isSelected()) {
          g.setColor(bgColor);
          g.fillOval(7, 9, 8, 8);
          
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
