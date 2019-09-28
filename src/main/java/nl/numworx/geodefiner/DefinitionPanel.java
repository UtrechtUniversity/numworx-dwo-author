package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.swing.Box;
import javax.swing.JPanel;
import fi.beans.numworxlf.JScrollPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import nl.numworx.geodefiner.Definitions.PosConvert;
import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.UIModel;
import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.model.AbstractViewer;

@SuppressWarnings("serial")
class DefinitionPanel extends JPanel implements PropertyChangeListener, PosConvert {

	Box list;
	AbstractViewer viewer;
	
	class Click extends MouseAdapter {
		private CellItem cellItem;
		
		Click(CellItem cellItem) {
			this.cellItem = cellItem;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			String text = cellItem.getCell().text;
			UIModel<?, ?> config = cellItem.getCell().config;
			Object map = config == null ? Collections.EMPTY_MAP : toMap(config);
			DefinitionPanel.this.firePropertyChange("command", map, text);
		}

		private Map<String, Object> toMap(UIModel<?, ?> config) {
			Map<String, Object> map = config.toMap();
			map.put("class", config.toString());
			return map;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
//			mouseClicked(e);
		}

//		@Override
//		public void mouseDragged(MouseEvent e) {
//            CellItem  button = cellItem;
//            MouseEvent ce = new MouseEvent(button, MouseEvent.MOUSE_PRESSED, e.getWhen(), e.getModifiersEx(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
//            TransferHandler handle = button.getTransferHandler();
//            handle.exportAsDrag(button, ce, TransferHandler.COPY);
//		}
		
	}
		
	class ListUpdater implements ListDataListener {

		public void intervalAdded(ListDataEvent e) {
			int i0 = e.getIndex0();

			CELL cell = model.getElementAt(i0);
			Object i1 = cell.extra;
			CellItem cellItem = new CellItem(cell,viewer, i0);
			cellItem.addPropertyChangeListener("item", DefinitionPanel.this);
// where to start?
			cellItem.center.formuleVak.geefKind1().addMouseListener(new Click(cellItem));
			if(i1 instanceof Number) {
			list.add(cellItem, ((Number)i1).intValue());
			} else 
				list.add(cellItem);
			DefinitionPanel.this.validate();
		}

		public void intervalRemoved(ListDataEvent e) {
			Component[] cs = list.getComponents();
			for (Component component : cs) {
				CellItem item = (CellItem) component;
				if (model.indexOf(item.cell) < 0) {
					list.remove(item);
				}
			}
			DefinitionPanel.this.validate();
			DefinitionPanel.this.repaint();
		}

		public void contentsChanged(ListDataEvent e) {
			int i0 = e.getIndex0();
			CELL c = model.getElementAt(i0);
			Component[] cs = list.getComponents();
			for (Component component : cs) {
				CellItem item = (CellItem) component;
				if(c == item.cell) {
					item.refresh();
					item.center.formuleVak.geefKind1().addMouseListener(new Click(item));					
					break;
				}
			}
		}
		
	}
	
	ListUpdater updater = new ListUpdater();
	
	Definitions model;
	Randomizer randomizer = new Randomizer() {
		public String randomize(String input) { return input; }
	};
	private Map<String, Object> config = Collections.EMPTY_MAP;
	
	@Inject DefinitionPanel(Definitions model, AbstractViewer viewer, Randomizer randomizer) {
		super(new BorderLayout());
		setName(Messages.getString("DefinitionPanel.1"));
		this.model = model;
		this.viewer = viewer;
		this.randomizer = randomizer;
		setPreferredSize(new Dimension(200,400));
		setBackground(Color.white);
		list = Box.createVerticalBox();
		model.addListDataListener(updater);
		model.ps = this;
		add(new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if("item".equals(name)) {
			CellItem source = (CellItem) evt.getSource();
			remove(source);
			return;
		}
		if("command".equals(name))
		{ 	String text = (String) evt.getOldValue();
			OMObject object = (OMObject) evt.getNewValue();
			model.define(text, object, config);
			model.redefine(randomizer);
		}
		if("config".equals(name)) {
			config = (Map<String, Object>) evt.getNewValue();
		}
	}

	void remove(CellItem source) {
		CELL cell = source.getCell();
		int size = model.getSize();
		for (int i= 0; i < size; i++)
			if (model.getElementAt(i) == cell) {
				model.remove(i);
				break;
			}
	}

	List<String> toList() {
		int size = list.getComponentCount();
		ArrayList<String> stringList = new ArrayList<String>(size);
		for(int i = 0;i < size; i++) {
			CellItem item = (CellItem) list.getComponent(i);
			if(item.cell.item != null)
				stringList.add(item.cell.var);
		}
		return stringList;
	}

	public void fromList(List<String> stringList) {
		int pos = 0;
		int size = list.getComponentCount();
		for(String var: stringList) {
			for (int i = pos; i < size; i++) {
				CellItem item = (CellItem) list.getComponent(i);
				if(var.equals(item.cell.var)) {
					if(i != pos) {
						list.remove(item);
						list.add(item, pos);
					}
					pos++;
					break;
				}
			}
		}
		repaint();
	}

	@Override
	public int to(int from) {
		CELL m = model.getElementAt(from);
		int size = list.getComponentCount();
		for(int i=0; i < size; i++) {
			CellItem item = (CellItem) list.getComponent(i);
			if(item.cell == m) return i;
		}
		return from;
	}
}
