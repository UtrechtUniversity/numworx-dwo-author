package nl.numworx.geodefiner.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import fi.euclides.event.Tracker;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.openmath.Expression;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class CheckObjectList extends Groep implements Observer {
	
	public static class CheckVisitor implements Visitor {
		CheckObjectList l;
		Model m;

		public CheckVisitor(CheckObjectList l, Model m) {
			this.l = l;
			this.m = m;
		}

		@Override
		public void visitBoog(Boog arg0) {
			update(arg0);
			
		}

		private void update(Destroyable arg0) {
			if(l != null) {
				l.update(m, arg0);
			}
		}

		@Override
		public void visitCirkel(Cirkel arg0) {
			update(arg0);
		}

		@Override
		public void visitKegelsnede(Kegelsnede2 arg0) {
			update(arg0);
		}

		@Override
		public void visitLabel(Label arg0) {
			update(arg0);
		}

		@Override
		public void visitLijn(Lijn arg0) {
			update(arg0);
		}

		@Override
		public void visitLocus(Locus arg0) {
			update(arg0);
		}

		@Override
		public void visitPunt(Punt arg0) {
			update(arg0);			
		}

		@Override
		public void visitSegment(Segment arg0) {
			update(arg0);
		}

		@Override
		public void visitTriangle(Triangle arg0) {
			update(arg0);
		}
		
	}
	
	protected final Vector<CheckObject> list = new Vector<CheckObject>();
	protected Set<CheckObject> running = new HashSet<CheckObject>();
	protected Map<Destroyable,CheckObject> userItems = new HashMap<Destroyable,CheckObject>();
	private int userIndex;
	
	final private Tracker tracker;
	private Expression expression;
	private Instance instance;
	
	public CheckObjectList(Tracker tracker) {
		this.tracker = tracker;
		expression = tracker.adapt(Expression.class);
	}

	public int getSize() {
		return list.size();
	}

	public void addElement(CheckObject obj) {
		list.addElement(obj);
		running.add(obj);
	}

	public CheckObject remove(int index) {
		CheckObject r = list.remove(index);
		running.remove(r);
		return r;
	}

	public CheckObject getElementAt(int index) {
		return list.elementAt(index);
	}
	
	public void clear() {
		running.clear();
		int s = getSize();
		for(int i = 0; i < s; i++) {
			CheckObject co = list.elementAt(i);
			co.destroy();
		}
		list.clear();
	}

	@Override
	public void update(Observable observable, Object arg) {
		if (observable == tracker.getModel()) {
			if (arg == null) {
				setNagekeken(false);
			} else
			
			// something added
			if(arg instanceof Destroyable) {
				Destroyable d = (Destroyable)arg;
				userItems.put(d,null);
				d.addObserver(this);
				Iterator<CheckObject> i = running.iterator();
				while(i.hasNext()) {
					CheckObject co = i.next();
					if(co.getCache() == null) {
						co.createObject(expression, tracker.getMapper(), tracker.adapt(Randomizer.class));
					}
					if(co.verify(d))
					{	co.addObserver(this);
						i.remove();
						break;
					}
				}
			}
		} else if(arg == Destroyable.DESTROY) {
			observable.deleteObserver(this);
			CheckObject co = findCO(observable);
			if(co != null) 
			{	co.deleteObserver(this);
				co.destroy();
				running.add(co);
				setNagekeken(false);
			}
			userItems.remove(observable);
		} else if(arg == null) {
			CheckObject co = findCO(observable);
			if (co != null && !co.verify() ) {
				co.deleteObserver(this);
				running.add(co);
				setNagekeken(false);
			}
		}
	}
	
	private CheckObject findCO(Observable observable) {
		CheckObject result = observable.adapt(CheckObject.class);
		if(result != null) return result;
		for(CheckObject i: list) {
			if(i.getItem() == observable)
				return i;
		}
		return null;
	}

	public Vector<Map<String, ?>> toList() {
		Vector<Map<String,?>> result = new Vector<Map<String,?>>(list.size());
		Enumeration<CheckObject> e = list.elements();
		while (e.hasMoreElements()) {
			CheckObject checkObject = e.nextElement();
			result.add(checkObject.toMap());
		}
		return result;
	}
	
	public void fromList(ObjectList list) {
		clear();
		int s = list.size();
		for(int i = 0; i < s; i++) {
			CheckObject co = new CheckObject();
			co.fromMap(list.getObjectMap(i));
			addElement(co);
		}
	}
	
	public int getMaxScore() {
		int sum = 0;
		for(CheckObject co: list) {
			sum += co.getMaxScore();
		}
		return sum;
	}
	
	public int getScore() {
		int sum = 0;
		for(CheckObject co: list) {
			sum += co.getScore();
		}
		return sum;
		
	}

	public Boolean isStatus() {
		int s = getScore();
		int m = getMaxScore();
		if  ( s == m ) return Boolean.TRUE;
		if  ( s == 0 ) return Boolean.FALSE;
		return null;
	}
	
	public void start() {
		userIndex = tracker.getModel().getIndex();
		tracker.getModel().addObserver(this);
		
	}
	
	public void stop() {		
		tracker.getModel().deleteObserver(this);
	}

	public void feedback() {
		for(CheckObject co: list) {
			if(co.getItem() != null) {
				DefaultAdapter.getDefault(co.getItem()).put(co);
			}
		}
		tracker.paint();
	}
	
	public void removeFeedback() {
		for(CheckObject co: list) {
			if(co.getItem() != null) {
				DefaultAdapter.getDefault(co.getItem()).put(CheckObject.class,null);
			}
		}
		tracker.paint();
	}

	/**
	 * Verify userItems.
	 */
	public void verify() {
		Set<Destroyable> userItems = new HashSet<Destroyable>(this.userItems.keySet());
		for(CheckObject co: list) {
			if(co.verify()) { // side effect: adds to running if failed
				userItems.remove(co.getItem());
			}
		}
		
		Iterator<CheckObject> i = running.iterator();
		while (i.hasNext()) {
			CheckObject co = i.next();
			if(co.getItem() != null) {
				i.remove(); // should not happen
				
			} else
			for(Iterator<Destroyable> u = userItems.iterator(); u.hasNext(); ) {
				Destroyable d = u.next();
				if(co.verify(d))
				{
					co.addObserver(this);
					i.remove();
					u.remove();
					break;
				}
			}
		}
	}
	
	public void setNagekeken(boolean b) {
		if(instance != null)
			instance.setNagekeken(b);
	}
	public void setInstance(Instance instance) {
		this.instance = instance;
	}
}
