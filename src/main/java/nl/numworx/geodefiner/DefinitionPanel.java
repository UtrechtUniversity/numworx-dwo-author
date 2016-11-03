package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMInteger;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.tue.win.riaca.openmath.lang.OMVariable;

class DefinitionPanel extends JPanel implements PropertyChangeListener {

	Box list;
	AbstractViewer viewer;
	
	class ListUpdater implements ListDataListener {

		public void intervalAdded(ListDataEvent e) {
			int i0 = e.getIndex0();
			CELL cell = model.elementAt(i0);
			list.add( new CellItem(cell,viewer));
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
		}
		
	}
	
	ListUpdater updater = new ListUpdater();
	
	Definitions model;
	
	DefinitionPanel(Definitions model, AbstractViewer viewer) {
		super(new BorderLayout());
		this.model = model;
		this.viewer = viewer;
		setPreferredSize(new Dimension(200,400));
		setBackground(Color.white);
		list = Box.createVerticalBox();
		model.addListDataListener(updater);
		add(new JScrollPane(list), BorderLayout.CENTER);
		//add(new JLabel("Elementen"), BorderLayout.NORTH);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String text = (String) evt.getOldValue();
		OMObject object = (OMObject) evt.getNewValue();
		model.define(text, object);
	}

}
