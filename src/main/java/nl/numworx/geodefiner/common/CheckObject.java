package nl.numworx.geodefiner.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.numworx.geodefiner.common.math.EqualsVisitor;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.NameMapper;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.Expression;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class CheckObject extends Observable implements Observer {
	int maxScore;
	int present = Label.UNKNOWN;
	private Destroyable item, cache;
	String formule = "";
	Numbers marge = Numbers.createDouble(0.01);
	private EqualsVisitor eq;
	private List<Destroyable> depend = Collections.emptyList();
	
	public int getScore() {
		return (present > 0) ? getMaxScore() : 0;
	}
	
	public boolean isEmpty() {
		return formule.length() <= 3;
	}
	
	public int getMaxScore() {
		return isEmpty() ? 0 : maxScore;
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
		return map;
	}
	
	public Destroyable createObject(Expression interpreter, NameMapper mapper, Randomizer random) {
		try {
			String string = random.randomize(getFormule());
			FormuleParser parser = new FormuleParser(string.substring(2));
			OMObject obj = parser.expr();
			cache = interpreter.interpret(obj, new Label(), mapper);
			depend = LocusModelF.varsOf(obj, mapper);
			for(Destroyable d: depend) d.addObserver(this);
			eq = new EqualsVisitor(cache, mapper);
			return cache;
		} catch(Exception e) {
			return null;
		}
	}
	
	public void destroy() {
		for(Destroyable d:depend) d.deleteObserver(this);
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
		present = similar(item);
		if(present > 0)
		{
			item.addObserver(this);
			this.item = item;
		}
		return present > 0;
	}
	
	private int similar(Destroyable item) {
		if(item == null) return Label.FALSE;
		EqualsVisitor eq = this.eq;
		//if(item == this.item) return present;
		if(eq == null)
			return Label.UNKNOWN;
		Numbers test = Numbers.ONE;
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
		else if(item != null && arg == null) {
			verify();
		}
	}

	public boolean verify() {
		boolean b = verify(item);
		if(!b) {
			setItem();
			notifyObservers();
		}
		return b;
	}

	void setItem() {
		if(item != null) {
			DefaultAdapter.getDefault(item).put(CheckObject.class, null);
			item.deleteObserver(this);
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
	
}
