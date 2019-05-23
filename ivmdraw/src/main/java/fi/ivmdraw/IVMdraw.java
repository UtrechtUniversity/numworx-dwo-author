package fi.ivmdraw;

import java.util.Locale;

import javax.swing.JApplet;

import fi.beans.mainframe.MainFrame;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.ivmdraw.common.Model;

@SuppressWarnings("serial")
public class IVMdraw extends JApplet implements WiskOpdrApplet {

  Model model;
  
  public IVMdraw() {
    model = new Model();
  }

  public IVMdraw(Locale locale) {
    this();
    setLocale(locale);
  }

  public IVMdrawPanel getInteractiePanel() {    
    return new IVMdrawPanel(this);
  }

  public static void main(String[] args) {
    IVMdraw applet = new IVMdraw(Locale.forLanguageTag("nl"));
    MainFrame frame = new MainFrame(applet, 400, 400);
    frame.setSize(400,400);
    frame.setTitle("IVMdraw");
    frame.pack();
    frame.show();
  }

  IVMdrawPanel panel;
  
  public void init() {
    panel = getInteractiePanel();
    setContentPane(panel);
  }

  @Override
  public void start() {
    panel.start();
  }

  @Override
  public void stop() {
    panel.stop();
  }

  @Override
  public void destroy() {
    panel.destroy();
  }
  
  
}
