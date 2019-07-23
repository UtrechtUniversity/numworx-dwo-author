package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.cbook.cbookif.CBookContext;

import fi.euclides.event.Tracker;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class LogAction extends AbstractAction implements ClipboardOwner {

  static Logger LOG = Logger.getLogger(LogAction.class.getName());
  
  CBookContext context;
  Object message = "nothing";
  
  @Inject LogAction(Tracker tracker) {
    super("Log");
    context = tracker.adapt(CBookContext.class);
  }


  @Override
  public void actionPerformed(ActionEvent ev) {
    LOG.info("logAction");
    JOptionPane.showMessageDialog((Component) ev.getSource(), message, "Log", JOptionPane.PLAIN_MESSAGE);
  }


  class StateModel extends AbstractTableModel {
    List<String> names;
    ObjectList items;
    private StateModel(Collection<String> names, ObjectList items) {
      this.names = new ArrayList<>(names);
      this.items = items;
    }
    @Override
    public int getColumnCount() {
      return names.size();
    }
    @Override
    public int getRowCount() {
      return items.size();
    }
    @Override
    public Object getValueAt(int row, int col) {
      String key = names.get(col);
      return items.getObjectMap(row).get(key);
    }
    @Override
    public String getColumnName(int col) {
      return names.get(col);
    }
    
  }
  
  
  
public void setLogState(Map<String, ?> state) {
	ObjectMap wrap = JSONUtilities.wrapMap(state);
	ObjectList items = wrap.getObjectList("logState");
	SortedSet<String> names = new TreeSet<>();
    for (int i = 0; i < items.size(); i++) {
      ObjectMap line = items.getObjectMap(i);
      Set<String> keys = line.keySet();
      names.addAll(keys);
    }
	JTable table = new JTable(new StateModel(names, items));
	
//	JTextArea area = new JTextArea();
//	for (int i = 0; i < items.size(); i++) {
//		ObjectMap line = items.getObjectMap(i);
//		area.append(line.toString());
//		area.append("\n");
//	}
	JScrollPane pane;
	JPanel wrapper = new JPanel(); wrapper.add(table);
	message = pane = new JScrollPane(wrapper,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	JPanel pnl = new JPanel(new BorderLayout());
	Dimension d = pane.getPreferredSize();
	d.height = Math.min(300, d.height);
	d.width = Math.min(400, d.width);
	pane.setPreferredSize(d);
	pnl.add(pane,BorderLayout.CENTER);
	JLabel title = new JLabel( context.getProperty("learner_name").toString());
	pnl.add(title, BorderLayout.NORTH);
	JButton copy = new JButton("copy");
	pnl.add(copy, BorderLayout.SOUTH);
	copy.addActionListener(a -> export(table.getModel()));
	//	pane.setMaximumSize(new Dimension(600,400));
//	pane.setPreferredSize(pane.getMaximumSize());
	message = pnl;
}



private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

private void export(TableModel model) {
  StringBuilder sb = new StringBuilder();
  int rows = model.getRowCount();
  int cols = model.getColumnCount();
  // hedaer
  for (int i = 0; i < cols; i++) {
    if (i != 0) sb.append("\t");
    sb.append(model.getColumnName(i));
  }
    sb.append("\n");
 for (int j = 0; j < rows; j++) {
   for (int i = 0; i < cols; i++) {
     if (i != 0) sb.append("\t");
     sb.append(model.getValueAt(j,i));
   }
   sb.append("\n");

 }

  String clip = sb.toString();
  clipboard.setContents(new StringSelection(clip), this);
}
@Override
public void lostOwnership(Clipboard arg0, Transferable arg1) {
}
}
