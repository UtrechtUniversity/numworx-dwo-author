package nl.numworx.geodefiner.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.numworx.geodefiner.common.math.EqualsVisitor;
import nl.numworx.geodefiner.common.math.Expression;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMString;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.proof.LabelTester;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class CheckObject extends Observable implements Observer, Comparable<CheckObject> {
	final static private Logger LOG = Logger.getLogger(CheckObject.class.getName());
	int maxScore;
	int present = Label.UNKNOWN;
	private Destroyable item, cache;
	String formule = "";
	Numbers marge = Numbers.createDouble(0.01);
	private EqualsVisitor eq;
	private List<Destroyable> depend = Collections.emptyList();
	
	public final int order;
	
	
	public CheckObject(int order) {
		this.order = order;
	}

	public int getScore() {
		return (present > 0) ? getMaxScore() : 0;
	}
	
	public boolean isEmpty() {
		return formule.length() <= 3;
	}
	
	public int getMaxScore() {
		return maxScore;
	}
	public String getFormule() {
		return formule;
	}
	public void setFormule(String formule) {
		if(formule == null) formule = "";
		this.formule = formule;
	}
	/**
	 * @param maxScore the maxScore to set
	 */
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public void setMaxScore(Number maxScore) {
		if (maxScore == null) this.maxScore = 0;
		else this.maxScore = maxScore.intValue();
	}

	public void fromMap(ObjectMap map) {
		if(map.containsKey("score"))
				setMaxScore(map.getInt("score"));
		else 
			setMaxScore(0);
		if (map.containsKey("marge")) {
			setMarge(map.getDouble("marge"));
		} else {
			setMarge(null);
		}
		if(map.containsKey("value")) 
				setFormule(map.getString("value"));
		else
			setFormule("");
	}
	
	public Map<String,Object> toMap() {
		HashMap<String,Object> map = new HashMap<>();
		if(maxScore > 0)
			map.put("score", maxScore);
		if(!formule.isEmpty())
			map.put("value", formule);
		map.put("marge", marge.doubleValue());
		return map;
	}
	
	public Destroyable createObject(Expression interpreter, NameMapper mapper, Randomizer random) {
		try {
			String string = random.randomize(getFormule());
			FormuleParser parser = new FormuleParser(string.substring(2));
			OMObject obj = parser.expr();
			
			cache = interpret(interpreter, mapper, obj);
			depend = LocusModelF.varsOf(obj, mapper);
			for(Destroyable d: depend) d.addObserver(this);
			if(!isTest(cache))
				eq = new EqualsVisitor(cache, mapper);
			else 
				cache.addObserver(this);
			return cache;
		} catch(Exception e) {
			LOG.log(Level.WARNING, "createObject " + getFormule(), e);
			return null;
		}
	}

	protected Destroyable interpret(Expression interpreter, NameMapper mapper, OMObject obj) {
		if(obj instanceof OMApplication) {
			OMApplication oma = (OMApplication)obj;
// point("LABEL")
			if (Definitions.POINT.isSame( oma.firstElement()) && oma.getLength() == 2 && oma.getElementAt(1) instanceof OMString) {
				String str = ((OMString) oma.getElementAt(1)).getString();
				Tracker tracker = interpreter.CONST.getTracker();
				return new NamedPoint(str, tracker);
			}
// point()
			if (Definitions.POINT.isSame(oma.firstElement()) && oma.getLength() == 1) {
			    Tracker tracker = interpreter.CONST.getTracker();
			    return new UnnamedPoint(tracker);
			}
		}		
		return interpreter.interpret(obj, new Label(), mapper);
	}
	
	public static boolean isTest(Destroyable c) {
		return c instanceof Label && ((Label) c).getRegistered() instanceof LabelTester;
	}

	public boolean isTest() {
	  return isTest(cache);
	}
	
	public void destroy() {
		for(Destroyable d:depend) d.deleteObserver(this);
		if(cache != null)
			cache.deleteObserver(this);
		if(cache != null && cache.getIndex() == 0) 
			cache.destroy();
		cache = null;
		setItem();
		eq = null;
		depend = Collections.emptyList();
		present = Label.UNKNOWN;
		notifyObservers(Destroyable.DESTROY);
	}
	
	public boolean verify(Destroyable item) {
		if(isTest(cache))
		{	int oldpresent = present;
			present = test(cache);
			if(present != oldpresent) 
			{
				setChanged();
			}
		}
		else
		{	present = similar(item);
			if(present > 0)
			{	
				if(cache instanceof NamedPoint) {
					item = ((NamedPoint) cache).getP();
				} else if (cache instanceof UnnamedPoint) {
				    item = ((UnnamedPoint) cache).getP();
				}
				item.addObserver(this);
				this.item = item;
			}
		}
		return present > 0;
	}
	
	private int test(Destroyable d) {
		Label l = (Label)d;
		if(!l.isDefined()) return Label.FALSE;
		l.registered.update(l, Model.DELAY);
		return l.getState();
	}

	private int similar(Destroyable item) {
		if(item == null) return Label.FALSE;
		EqualsVisitor eq = this.eq;
		//if(item == this.item) return present;
		if(eq == null || !cache.isDefined())
			return Label.UNKNOWN;
		Numbers test;
		synchronized(eq) {
			eq.reset();
			item.visit(eq);
			test = eq.test();
		}
		if(test == Numbers.ZERO) return Label.EXACT;
		if(Math.abs(test.doubleValue()) < marge.doubleValue()) {
			return Label.INEXACT;
		}
		return Label.FALSE;
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(arg == Label.DESTROY) destroy();
		else if(item != null && arg == null|| (arg == Label.STATE && observable == cache)) {
			verify();
		} else if (observable == cache) {
			setChanged();
			notifyObservers(arg);
		}
	}

	public boolean verify() {
		boolean b = verify(item);
		if(!b) {
			setItem();
		}
		notifyObservers();
		return b;
	}

	void setItem() {
		if(item != null) {
			item.deleteObserver(this);
			removeFeedback();
			item = null;
			setChanged();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T adapt(Class<T> clz) {
		if(CheckObject.class.equals(clz)) return (T) this;
		return super.adapt(clz);
	}

	public Destroyable getCache() {
		return cache;
	}

	public Destroyable getItem() {
		return item;
	}

	public void setItem(Destroyable item) {
		this.item = item;
	}

	boolean removeFeedback() {
		boolean b = false;
		if(item != null) {
			b = item.adapt(CheckObject.class) != null;
			DefaultAdapter.getDefault(item).put(CheckObject.class,null);
//			if(cache instanceof Label) {
//				boolean free = VrijPunt.TYPE == ((Label) cache).getP().key();
//				if(free) {
//					Destroyable p = ((Label) item).getP().getDepend()[0];
//					DefaultAdapter.getDefault(p).put(CheckObject.class,null);
//				}
//			}
		}
		return b;
	}

	void feedback() {
		if(item != null) {
			DefaultAdapter.getDefault(item).put(this);
//			if(cache instanceof Label) {
//				boolean free = VrijPunt.TYPE == ((Label) cache).getP().key();
//				if(free) {
//					Destroyable p = ((Label) item).getP().getDepend()[0];
//					DefaultAdapter.getDefault(p).put(this);
//				}
//			}
		}
	}

	@Override
	public int compareTo(CheckObject o) {
		return Integer.signum(order-o.order);
	}

	public double getMarge() {
		return marge.doubleValue();
	}
	public void setMarge(Number marge) {
		if (marge == null) {
			this.marge = Numbers.createDouble(0.01);
		} else if (marge instanceof Integer) { // byte/short?
			this.marge = Numbers.createInteger(marge.intValue());
		} else if (marge instanceof Long) {
			this.marge = Numbers.createRational(marge.longValue(), 1L);
		} else {
			this.marge = Numbers.createDouble(marge.doubleValue());
		}
	}
	
}
