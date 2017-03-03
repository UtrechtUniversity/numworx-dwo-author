package nl.numworx.geodefiner;

import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.Volgpunt;
import nl.numworx.geodefiner.ui.UIModelFactory;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.formuleobjects.TokenMgrError;
import fi.euclides.model.Label;
import fi.euclides.util.Observer;

/** FIXME split in a observable and a ListModel
 * 
 * @author wim
 *
 */
@SuppressWarnings("serial")
public class Definitions extends nl.numworx.geodefiner.common.Definitions implements Observer, ListModel<CELL> {

    protected EventListenerList listenerList = new EventListenerList();

	
	public Definitions(Tracker viewer) {
		super(viewer);
		factory  = new UIModelFactory(viewer);

	}

	public void update(CELL cell) {
		int i  = indexOf(cell);
		if(i >= 0)
			fireContentsChanged(this, i, i);
	}

// additions and removals of CELLs: fire events	
	
	@Override
	public void addElement(CELL element) {
		int s = getSize();
		super.addElement(element);
		fireIntervalAdded(this, s, s);
	}

	@Override
	public void clear() {
		int s = getSize()-1;
		super.clear();
		if(s >= 0)
			fireIntervalRemoved(this, 0, s);
	}

	Iterator<CELL> elements() {
		return delegate.iterator();
	}

	public void remove(int index) {
		super.remove(index);
		fireIntervalRemoved(this, index, index);
	}

	public void addListDataListener(ListDataListener l) {
        listenerList.add(ListDataListener.class, l);
	}

	public void removeListDataListener(ListDataListener l) {
        listenerList.remove(ListDataListener.class, l);
	}

    /**
     * Returns an array of all the list data listeners
     * registered on this <code>AbstractListModel</code>.
     *
     * @return all of this model's <code>ListDataListener</code>s,
     *         or an empty array if no list data listeners
     *         are currently registered
     *
     * @see #addListDataListener
     * @see #removeListDataListener
     *
     * @since 1.4
     */
    public ListDataListener[] getListDataListeners() {
        return listenerList.getListeners(ListDataListener.class);
    }


    /**
     * <code>AbstractListModel</code> subclasses must call this method
     * <b>after</b>
     * one or more elements of the list change.  The changed elements
     * are specified by the closed interval index0, index1 -- the endpoints
     * are included.  Note that
     * index0 need not be less than or equal to index1.
     *
     * @param source the <code>ListModel</code> that changed, typically "this"
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     * @see EventListenerList
     * @see DefaultListModel
     */
    protected void fireContentsChanged(Object source, int index0, int index1)
    {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index0, index1);
                }
                ((ListDataListener)listeners[i+1]).contentsChanged(e);
            }
        }
    }

    /**
     * <code>AbstractListModel</code> subclasses must call this method
     * <b>after</b>
     * one or more elements are added to the model.  The new elements
     * are specified by a closed interval index0, index1 -- the enpoints
     * are included.  Note that
     * index0 need not be less than or equal to index1.
     *
     * @param source the <code>ListModel</code> that changed, typically "this"
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     * @see EventListenerList
     * @see DefaultListModel
     */
    protected void fireIntervalAdded(Object source, int index0, int index1)
    {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index0, index1);
                }
                ((ListDataListener)listeners[i+1]).intervalAdded(e);
            }
        }
    }

    /**
     * <code>AbstractListModel</code> subclasses must call this method
     * <b>after</b> one or more elements are removed from the model.
     * <code>index0</code> and <code>index1</code> are the end points
     * of the interval that's been removed.  Note that <code>index0</code>
     * need not be less than or equal to <code>index1</code>.
     *
     * @param source the <code>ListModel</code> that changed, typically "this"
     * @param index0 one end of the removed interval,
     *               including <code>index0</code>
     * @param index1 the other end of the removed interval,
     *               including <code>index1</code>
     * @see EventListenerList
     * @see DefaultListModel
     */
    protected void fireIntervalRemoved(Object source, int index0, int index1)
    {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index0, index1);
                }
                ((ListDataListener)listeners[i+1]).intervalRemoved(e);
            }
        }
    }

    /**
     * Returns an array of all the objects currently registered as
     * <code><em>Foo</em>Listener</code>s
     * upon this model.
     * <code><em>Foo</em>Listener</code>s
     * are registered using the <code>add<em>Foo</em>Listener</code> method.
     * <p>
     * You can specify the <code>listenerType</code> argument
     * with a class literal, such as <code><em>Foo</em>Listener.class</code>.
     * For example, you can query a list model
     * <code>m</code>
     * for its list data listeners
     * with the following code:
     *
     * <pre>ListDataListener[] ldls = (ListDataListener[])(m.getListeners(ListDataListener.class));</pre>
     *
     * If no such listeners exist,
     * this method returns an empty array.
     *
     * @param listenerType  the type of listeners requested;
     *          this parameter should specify an interface
     *          that descends from <code>java.util.EventListener</code>
     * @return an array of all objects registered as
     *          <code><em>Foo</em>Listener</code>s
     *          on this model,
     *          or an empty array if no such
     *          listeners have been added
     * @exception ClassCastException if <code>listenerType</code> doesn't
     *          specify a class or interface that implements
     *          <code>java.util.EventListener</code>
     *
     * @see #getListDataListeners
     *
     * @since 1.3
     */
    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return listenerList.getListeners(listenerType);
    }

	public void redefine(Randomizer random) {
		List<CELL> undef = new LinkedList<CELL>();
		for(CELL cell : delegate) {
			if(cell.item == null) undef.add(cell);
		}
		for(CELL cell : undef) {
			try {
				String substring = random.randomize(cell.text);
				FormuleParser parser = new FormuleParser(substring.substring(2));
				OMObject object = parser.parse();
				define(cell.text, object);
			} catch (ParseException pe) {
			} catch (Exception e) {
				e.printStackTrace();
				addElement(cell);
			} catch (TokenMgrError te) {				
			}
		}
	}

	private UIModelFactory factory;

	protected void installConfig(CELL cell, Map<String, ?> config, String name) {
		if(config != null && !config.isEmpty()) {
			ObjectMap cellConfig = JSONUtilities.wrapMap(config);
			cell.config = factory.build(cell.item);
			cell.config.fromMap(cellConfig);
			cell.config.install();
		} else {
			if(cell.item instanceof Label) {
				if( ((Label)cell.item).getP() instanceof Volgpunt )
				cell.config = factory.build(cell.item); // save dx,dy in configuration
			}
		}
		super.installConfig(cell, config, name);
	}


}
