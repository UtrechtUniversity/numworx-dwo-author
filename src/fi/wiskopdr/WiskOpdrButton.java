package fi.wiskopdr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JButton;

public class WiskOpdrButton extends JButton {

  private Color bgColor = new Color(120,150,202);
  private Color fgColor = Color.white;
  private FontMetrics fm;
  private Font font = new Font("SansSerif", Font.BOLD, 13);
  
  public WiskOpdrButton(String label) {
    super(label);
    setFont(font);
  }
  
  @Override
  protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.setColor(bgColor);
      g.fillRect(0, 0, getWidth(), getHeight());
      
      fm = this.getFontMetrics(getFont());
      String text = getText();
      int stringwidth = fm.stringWidth(text);
      int stringheight = fm.getAscent()-fm.getDescent();
      g.setColor(fgColor);
      g.drawString(text,(getWidth()-stringwidth)/2 , getHeight()/2 + (stringheight)/2);
  
     
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
