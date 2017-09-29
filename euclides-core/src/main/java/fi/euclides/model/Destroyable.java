package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.Locus;
import fi.euclides.model.algo.Algorithm;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Adapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public abstract class Destroyable extends Observable implements Observer {
	public final static String DESTROY = "DESTROY";
	public static final String VISIBLE = "VISIBLE";
	public static final Object RENAME  = new Object();

	private int index;
	boolean visible = true;
	private byte dstate;
	
	private Adapter adapter = Adapter.NULL;
	
	public void destroy()
	{
		if (dstate != 0) {
			//dstate = dstate;
			return;
		}
		dstate = 1;
		Destroyable[] dd = getDepend();
		for (int i = 0; i < dd.length; i++) {
			Destroyable d = dd[i];
			//dd[i] = null; TODO probleem met observer als dependency niet meer bestaat
			if(d != null)
			{
				d.deleteObserver(this);
				if(d.getIndex() == 0) d.destroy(); // destroy anonymous dependencies
			}
		}
		dstate=2;
		setChanged();
		notifyObservers(DESTROY);
		setIndex(0);
		dstate = 3;
	}
	
	abstract public void visit(Visitor v);
	
	public boolean isDefined() {
		return true;
	}
	/**
	 * TODO make abstract.
	 * @return array of dependents.
	 */
	public Destroyable[] getDepend() { 
		return Label.EMPTY;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	public abstract String key();
	
	public abstract void write(Codec codec) throws IOException;
	public abstract void read(Codec codec) throws IOException;

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		if(visible != this.visible)
			setChanged();
		this.visible = visible;
		notifyObservers(VISIBLE);
	}

	public String toString() {
		return key().substring(0,1)+getIndex();
	}
	
	public void rename() {
		setChanged();
		notifyObservers(RENAME);
	}

	public Destroyable trail() {
		return null;
	}

	// proven technology.
	public boolean incident(Destroyable other)
	{
		return other == this;
	}
	
	public Destroyable[] getImage(Destroyable mirror)
	{
		if(this instanceof OpObject)
		{
			OpObject<?> opo = (OpObject<?>) this;
			PuntOp<?> source = opo . pointOn(Numbers.ZERO, Numbers.ZERO);
			source.setVisible(false);
			Punt dest   = source.getImage(mirror, this);
			dest.setVisible(false);
			Locus locus = new Locus(source, dest, null);
			return new Destroyable[] { source, dest, locus } ;
		}	
		return null;
	}

	public Adapter getAdapter() {
		return adapter;
	}

	public void setAdapter(Adapter adapter) {
		if(adapter == null) adapter = Adapter.NULL;
		this.adapter = adapter;
	}
	
	
}
