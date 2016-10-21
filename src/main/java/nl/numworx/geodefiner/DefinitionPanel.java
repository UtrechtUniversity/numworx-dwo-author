package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.Popcorn;
import fi.euclides.swing.AWTViewer;
import fi.euclides.util.DefaultAdapter;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMInteger;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.tue.win.riaca.openmath.lang.OMVariable;

class DefinitionPanel extends JPanel implements PropertyChangeListener {

	
	
	
	JList<Destroyable> list;
	DefaultListModel<Destroyable> model;
	AWTViewer viewer;
	
	DefinitionPanel() {
		super(new BorderLayout());
		setPreferredSize(new Dimension(200,400));
		setBackground(Color.white);
		model = new DefaultListModel<Destroyable>();
		list = new JList<Destroyable>(model);
		add(list, BorderLayout.CENTER);
		add(new JLabel("Elementen"), BorderLayout.NORTH);
	}

	static final OMSymbol POINT = new OMSymbol("geodefiner", "point");
	
	public void propertyChange(PropertyChangeEvent evt) {
		String text = (String) evt.getOldValue();
		OMObject object = (OMObject) evt.getNewValue();
		if(object instanceof OMApplication) {
			OMApplication oma = (OMApplication) object;
			OMObject first = oma.firstElement();
			if( first.isSame(Popcorn.PROG1_ASSIGN))
			{
				OMVariable var = (OMVariable) oma.getElementAt(1);
				oma = (OMApplication) oma.getElementAt(2);
				OMObject f = oma.firstElement();
				if(POINT.isSame(f)) {
// $P := point(1,2)
					OMInteger ix = (OMInteger) oma.getElementAt(1); // toNumber(object)
					OMInteger iy = (OMInteger) oma.getElementAt(2);
					Numbers x = Numbers.createInteger(ix.intValue());
					Numbers y = Numbers.createInteger(iy.intValue());
					Punt p = viewer.getModel().buildCoordinaten(x, y);
					DefaultAdapter.getDefault(p).put(var.getName());
					model.addElement(p);
					return;
				}
			}
		}
	}
	
}
