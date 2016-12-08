package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.Expression;
import fi.euclides.openmath.Popcorn;
import fi.euclides.swing.AWTViewer;
import fi.euclides.util.DefaultAdapter;
import nl.numworx.geodefiner.common.CELL;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMInteger;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.tue.win.riaca.openmath.lang.OMVariable;

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
	
	DefinitionPanel(Definitions model, AbstractViewer viewer) {
		super(new BorderLayout());
		setName("Objects");
		this.model = model;
		this.viewer = viewer;
		setPreferredSize(new Dimension(200,400));
		setBackground(Color.white);
		list = Box.createVerticalBox();
		model.addListDataListener(updater);
		add(new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		//add(new JLabel("Elementen"), BorderLayout.NORTH);
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

}
