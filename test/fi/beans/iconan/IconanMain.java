package fi.beans.iconan;

import java.awt.BorderLayout;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JFrame;

public class IconanMain {
  public static void main(String[] args) { 
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLocale(new Locale("en"));
    Hashtable<Object, Object> hashtable = new Hashtable<Object, Object>();
    Iconan i = new Iconan(f, hashtable);
    f.getContentPane().setLayout(new BorderLayout());
    f.getContentPane().add(i);
    f.setSize(100,100);
    f.pack();
    f.setVisible(true);
}

}
