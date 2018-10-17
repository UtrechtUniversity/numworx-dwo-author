package nl.numworx.samllogin;

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import fi.beans.scorm.SAMLLoginIF;

public class Main {

  public static void main(String[] args) {
    
    final JFrame f = new JFrame("Login uu-dev");
    SamlLoginPanel.debug = true;
    SAMLLoginIF browser = new SamlLoginPanel("https://uu-dev.dwo.nl/dwo/saml/login.jsp");
    browser
      .getPromise()
      .then( p -> {
        p.getValue().store(System.out, "Login succeeded");
        SwingUtilities.invokeLater(f::dispose);
        System.exit(0);
        return null;
    });
    browser.asComponent().addComponentListener(new ComponentAdapter() {

      @Override
      public void componentResized(ComponentEvent e) {
        System.out.println("size = " + e.getComponent().getSize());
      }
      
    });
    f.setContentPane(browser.asComponent());
    
    f.pack();
    Insets inset = f.getInsets();
    f.setSize(320 + inset.left + inset.right, 446 + inset.top + inset.bottom);
    f.setVisible(true);
    //browser.loadURL("https://uu-dev.dwo.nl/dwo/saml/login.jsp");
    //browser.loadURL("http://localhost:8080/dwo/saml/login.jsp");
    //browser.loadURL("https://idptestbed/dwo/saml/login.jsp");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

}
