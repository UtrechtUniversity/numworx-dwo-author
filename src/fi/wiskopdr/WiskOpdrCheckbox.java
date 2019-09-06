package fi.wiskopdr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JCheckBox;

public class WiskOpdrCheckbox extends JCheckBox {

  private Color bgColor = new Color(120,150,202);
  private Color fgColor = Color.white;
  private FontMetrics fm;
  private Font font = new Font("SansSerif", Font.BOLD, 13);
  
  public WiskOpdrCheckbox(String label) {
    super(label);
    setForeground(fgColor);
    setOpaque(false);
    setFont(font);
  }
  
  @Override
  protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.setColor(Color.white);
      g.fillRect(4, 6, 13, 13);
      g.setColor(bgColor);
      g.drawRect(4, 6, 13, 13);
      
      if(isSelected()) {
        g.setColor(bgColor);
        g.fillRect(4, 6, 13, 13);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString("v",7,17);
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

  
  


