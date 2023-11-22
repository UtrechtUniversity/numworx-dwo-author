package nl.numworx.samllogin;

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainPopup {

  public static void main(String[] args) {
    
    final JFrame f = new JFrame("Login conext");
    SamlLoginPanel.debug = true;
    SamlLoginPanel browser = new SamlLoginPanel();
    JButton btn = new JButton("GO");
    f.getContentPane().add(btn);
    browser.setEndpoint("/dwo/oauth2/entree");
    btn.addActionListener(ev -> {
    browser.popup(btn, "https://test.dwo.nl/dwo/oauth2/login3.jsp?idphint=conext")

      .then( p -> {
        p.getValue().store(System.out, "Login succeeded");
        SwingUtilities.invokeLater(f::dispose);
        
        if(args != null)
          SwingUtilities.invokeLater( () -> MainPopup.main(null) );
        else 
          System.exit(1);
        return null;
    });
      });
    f.show();
 }

}
