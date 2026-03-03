package nl.numworx.samllogin;

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

  public static void main(String[] args) {
    
    final JFrame f = new JFrame("Login conext");
    SamlLoginPanel.debug = true;
    SamlLoginPanel browser = new SamlLoginPanel();
    browser.setEndpoint("/dwo/oauth2/entree");
    browser.loadURL("https://test.dwo.nl/dwo/oauth2/login3.jsp?idphint=conext");
    browser
      .getPromise()
      .then( p -> {
        p.getValue().store(System.out, "Login succeeded");
        SwingUtilities.invokeLater(f::dispose);
        
        if(args != null)
          SwingUtilities.invokeLater( () -> Main.main(null) );
        else 
          System.exit(1);
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
