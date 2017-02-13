package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import nl.numworx.geodefiner.common.CELL;
import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.model.AbstractViewer;

@SuppressWarnings("serial")
class DefinitionPanel extends JPanel implements PropertyChangeListener {

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
			DefinitionPanel.this.firePropertyChange("command", null, text);
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
			CellItem cellItem = new CellItem(cell,viewer, i0);
			cellItem.addPropertyChangeListener("item", DefinitionPanel.this);
// where to start?
			cellItem.center.formuleVak.geefKind1().addMouseListener(new Click(cellItem));
			list.add( cellItem);
			DefinitionPanel.this.validate();
		}

		public void intervalRemoved(ListDataEvent e) {
			int i0 = e.getIndex0();
			int i1 = e.getIndex1();
			while( i1 >= i0 )
				list.remove(i1--);
			DefinitionPanel.this.validate();
			DefinitionPanel.this.repaint();
		}

		public void contentsChanged(ListDataEvent e) {
			int i0 = e.getIndex0();
			CellItem item = (CellItem) list.getComponent(i0);
			item.refresh();
			item.center.formuleVak.geefKind1().addMouseListener(new Click(item));
		}
		
	}
	
	ListUpdater updater = new ListUpdater();
	
	Definitions model;
	Randomizer randomizer = new Randomizer() {
		public String randomize(String input) { return input; }
	};
	
	DefinitionPanel(Definitions model, AbstractViewer viewer) {
		super(new BorderLayout());
		setName(Messages.getString("DefinitionPanel.1"));
		this.model = model;
		this.viewer = viewer;
		setPreferredSize(new Dimension(200,400));
		setBackground(Color.white);
		list = Box.createVerticalBox();
		model.addListDataListener(updater);
		add(new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if("item".equals(name)) {
			CellItem source = (CellItem) evt.getSource();
			remove(source);
			return;
		}
		String text = (String) evt.getOldValue();
		OMObject object = (OMObject) evt.getNewValue();
		model.define(text, object);
		model.redefine(randomizer);
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
		int size = getComponentCount();
		ArrayList<String> list = new ArrayList<String>(size);
		for(int i = 0;i < size; i++) {
			CellItem item = (CellItem) getComponent(i);
			list.add(item.cell.var);
		}
		return list;
	}
}
