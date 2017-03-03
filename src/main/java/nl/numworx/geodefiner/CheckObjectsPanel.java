package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleEditor;
import fi.wiskopdr.formuleobjects.FormuleVak;
import nl.numworx.geodefiner.common.CheckObject;
import nl.numworx.geodefiner.common.CheckObjectList;
import nl.numworx.geodefiner.common.Randomizer;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;

public class CheckObjectsPanel extends JPanel implements ActionListener, Randomizer {

	JButton plus, min;
	JTable  table;
	CheckObjectList checkObjects;
	private CheckObjectsModel model;
	
	
	class CheckObjectsModel extends AbstractTableModel {

		@Override
		public int getRowCount() {
			return checkObjects.getSize();
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch(columnIndex) {
			case 0: return "Object_" + (rowIndex + 1);
			case 1: return checkObjects.getElementAt(rowIndex).getFormule();
			case 2: return checkObjects.getElementAt(rowIndex).getMaxScore();
			}
			return null;
		}

		final String[] names = { "NAME", "VALUE", "SCORE" };
		final Class[] classes = { String.class, String.class, Integer.class };
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int column) {
			return names[column];
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return classes[columnIndex];
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex > 0;
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
		 */
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch(columnIndex) {
			case 1: checkObjects.getElementAt(rowIndex).setFormule(aValue.toString()); break;
			case 2: checkObjects.getElementAt(rowIndex).setMaxScore((Integer)aValue); break;
			}
		}

		public void addElement(CheckObject checkObject) {
			int s = checkObjects.getSize();
			checkObjects.addElement(checkObject);
			fireTableRowsInserted(s, s);
		}

		public void remove(int i) {
			checkObjects.remove(i);
			fireTableRowsDeleted(i, i);
		}

		public void fromList(ObjectList objectList) {
			checkObjects.fromList(objectList);
			fireTableDataChanged();
		}
	}
	
	class FormuleRenderer extends FormuleVak implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			String f = (String) value;
			if (f==null) f = "$f@";
			else if (!f.startsWith("$f"))
				f = "$f" + f + "@";
			
			Color fg = Color.black;
			Color bg = Color.white;
			if(isSelected) {
				bg = table.getSelectionBackground();
				fg = table.getSelectionForeground();
			}
			setOpaque(true);
			setFGColor(fg);
			setBackground(bg);
			vulVak(f);
			setEditable(false);
			zetMaat();
			setPreferredSize(getSize());		
			return this;
		}
		
	}
	
	class FormuleCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

		private FormuleEditor vak = new FormuleEditor(false) {
			{
				setHeader(false);
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				super.actionPerformed(e);
				if(e.getSource() == this.formuleVak)
					produceAction(e.getActionCommand());
			} 
		};
		String value;
		@Override
		public Object getCellEditorValue() {
			return value;
		}

		{
			vak.addActionListener(this);
		}
		
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			String f = (String) value;
			if (f==null) f = "$f@";
			else if (!f.startsWith("$f"))
				f = "$f" + f + "@";
			value = f;
			Color fg = Color.black;
			Color bg = Color.white;
			if(isSelected) {
				bg = table.getSelectionBackground();
				fg = table.getSelectionForeground();
			}
			
			vak.formuleVak.setFGColor(fg);
			vak.setBackground(bg);
			vak.formuleVak.vulVak(f);
			vak.formuleVak.zetMaat();
			vak.doLayout();
			return vak;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() .equals( "ingevuld")) {
				stopCellEditing();
			}
		}

		/* (non-Javadoc)
		 * @see javax.swing.AbstractCellEditor#stopCellEditing()
		 */
		@Override
		public boolean stopCellEditing() {
			value = vak.formuleVak.toString();
			if("$f@".equals(value))
				return super.stopCellEditing();
			String string = value;
			try {
				string = randomizer.randomize(string);
				FormuleParser fp = new FormuleParser(string.substring(2));
				OMObject expr = fp.expr();
				assert expr != null; // assertionError
			} catch(Throwable e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "stopCellEditing", e);
				firePropertyChange(new PropertyChangeEvent(vak, "feedback", string, e));
				return false;
			}
			return super.stopCellEditing();
		}
	}
	
	Randomizer randomizer = this;
	
	@Override
	public String randomize(String input) {
		return input;
	}
	
	public void firePropertyChange(PropertyChangeEvent e) {
		for (PropertyChangeListener l : getPropertyChangeListeners(e.getPropertyName())) {
			l.propertyChange(e);
		}
		
	}

	CheckObjectsPanel(Tracker tracker) {
		super(new BorderLayout());
		setName("CheckObjects");
		checkObjects = new CheckObjectList(tracker);
		CheckObject obj = new CheckObject();
// Sample
		obj.setFormule("$f@");
		obj.setMaxScore(5);
		checkObjects.addElement(obj);
		model = new CheckObjectsModel();
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(0).setMaxWidth(60);
		table.getColumnModel().getColumn(2).setPreferredWidth(60);
		table.getColumnModel().getColumn(2).setMaxWidth(60);
		table.setRowHeight(80);
		TableColumn column = table.getColumnModel().getColumn(1);
		column.setCellRenderer(new FormuleRenderer());
		column.setCellEditor(new FormuleCellEditor());

		add(table.getTableHeader(), BorderLayout.NORTH);
		JPanel flow = new JPanel();
		plus = new JButton("+");
		plus.addActionListener(this);
		min  = new JButton("-");
		min.addActionListener(this);
		flow.add(plus);
		flow.add(min);
		add(flow, BorderLayout.SOUTH);
		add(new JScrollPane(table), BorderLayout.CENTER);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( "+".equals(e.getActionCommand())) {
			CheckObject o = new CheckObject();
			o.setMaxScore(5);
			model.addElement(o);
			return;
		}
		if ( "-".equals(e.getActionCommand())) {
			if (table.isEditing()) {
				TableCellEditor cellEditor = table.getCellEditor();
				//if (cellEditor != null)
				cellEditor.cancelCellEditing();
			}
			int[] rows = table.getSelectedRows();
			Arrays.sort(rows);
			for (int i = rows.length-1; i >=0; i--) {
				model.remove(rows[i]);				
			}
		}
	}

	public void fromList(ObjectList objectList) {
		model.fromList(objectList);
	}

	public List toList() {
		return checkObjects.toList();
	}
}
