package fi.wiskopdr;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class WiskOpdrTextField extends JTextField{
  
  public WiskOpdrTextField(String text) {
    super(text);
    setMaximumSize(new Dimension(250,22));
    setMinimumSize(new Dimension(30,20));
    setBorder(BorderFactory.createLineBorder(WiskOpdr.colorBlue3));
  }

}
