package fi.wiskopdr.tekstobjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class CodePointVak extends TekstDeelVak {

  private String string = "";
  private int codepoint;
  private FontMetrics fm;
  
  public CodePointVak(TekstVak tv, int cp) {
    super(tv);
    codepoint = cp;
    string = new String(Character.toChars(cp));
    setForeground(tv.getForeground());
    setFont(tv.getFont());
  }

  public String toString() {
    return "$Z" + codepoint + "@";
  }

  @Override
  public void setFont(Font font) {
    super.setFont(font);
    fm = getFontMetrics(getFont());
    if (string != null)
      setSize(fm.stringWidth(string),fm.getAscent()+fm.getDescent());
    ashoogte = fm.getAscent();
  }

  @Override
  public void paintComponent(Graphics g) {
    // TODO Auto-generated method stub
    super.paintComponent(g);
    if (selected) {
      g.setColor(Color.gray);
      g.fillRect(0, 0, getWidth(), getHeight());
    }
    g.setColor(getForeground());
    g.drawString(string, 0, fm.getAscent());
  }

  @Override
  public void vulVak(String s) {
    codepoint = Integer.parseInt(s);
    string = new String(Character.toChars(codepoint));
    setFont(getFont()); // side effect
  }
  
  
  
}
