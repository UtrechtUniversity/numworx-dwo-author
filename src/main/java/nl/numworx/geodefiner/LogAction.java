package nl.numworx.geodefiner;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.swing.AbstractAction;

public class LogAction extends AbstractAction {

  static Logger LOG = Logger.getLogger(LogAction.class.getName());
  
  
  @Inject LogAction() {
    super("Log");
  }


  @Override
  public void actionPerformed(ActionEvent ev) {
    LOG.info("logAction");
  }

}
