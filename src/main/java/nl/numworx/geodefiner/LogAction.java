package nl.numworx.geodefiner;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class LogAction extends AbstractAction {

  static Logger LOG = Logger.getLogger(LogAction.class.getName());
  
  
  Object message = "nothing";
  
  @Inject LogAction() {
    super("Log");
  }


  @Override
  public void actionPerformed(ActionEvent ev) {
    LOG.info("logAction");
    JOptionPane.showMessageDialog((Component) ev.getSource(), message, "Log", JOptionPane.PLAIN_MESSAGE);
  }


public void setLogState(Map<String, ?> state) {
	ObjectMap wrap = JSONUtilities.wrapMap(state);
	ObjectList items = wrap.getObjectList("logState");
	JTextArea area = new JTextArea();
	for (int i = 0; i < items.size(); i++) {
		ObjectMap line = items.getObjectMap(i);
		area.append(line.toString());
		area.append("\n");
	}
	JScrollPane pane;
	message = pane = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	pane.setMaximumSize(new Dimension(600,400));
	pane.setPreferredSize(pane.getMaximumSize());
}

}
