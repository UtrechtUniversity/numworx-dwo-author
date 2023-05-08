package fi.euclides.model;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import fi.euclides.model.algo.FreePoint;
import fi.euclides.util.Observable;

public class Groep extends Destroyable {

	protected Destroyable[] depend;
	
	@Override
	public Destroyable[] getDepend() {
		return depend;
	}

	public Groep (Destroyable ... depend) {
		this.depend = depend;
		for (int i = 0; i < depend.length; i++) {
			depend[i].addObserver(this);
		}
	}
 	
	private Vector<Destroyable> items = new Vector<Destroyable>();

    private boolean free;
	
	public static String TYPE = "G";

	@Override
	public void update(Observable observable, Object arg) {
		if(arg == DESTROY) 
		{
			destroy();
		}
	}

	@Override
	public void visit(Visitor v) {
		Enumeration<Destroyable> e = items.elements();
		while (e.hasMoreElements()) {
			Destroyable destroyable = e.nextElement();
			destroyable.visit(v);
		}
	}

	public Destroyable prototype() {
		return items.firstElement();
	}
	
	@Override
	public String key() {
		return TYPE;
	}

	@Override
	public void write(Codec codec) throws IOException {
		codec.writeVector(items);
	}

	@Override
	public void read(Codec codec) throws IOException {
		items = codec.readVector();
	}

	@Override
	public void destroy() {
		clear();
		super.destroy();
	}

	public int size() {
		return items.size();
	}

	public Enumeration<Destroyable> elements() {
		return items.elements();
	}

	public Destroyable elementAt(int index) {
		return items.elementAt(index);
	}

	public void addElement(Destroyable obj) {
		obj.setVisible(isVisible());
		if (obj instanceof FreePoint )
		  ((FreePoint) obj).setFree(free);
		items.addElement(obj);
	}

	public void clear() {
		Enumeration<Destroyable> e = items.elements();
		while (e.hasMoreElements()) {
			Destroyable destroyable = e.nextElement();
			destroyable.destroy();
		}
		items.clear();
	}

	@Override
	public void setVisible(boolean visible) {
		Enumeration<Destroyable> e = items.elements();
		while (e.hasMoreElements()) {
			Destroyable destroyable = e.nextElement();
			destroyable.setVisible(visible);
		}
		super.setVisible(visible);
	}

	public Destroyable prototype(int index) {
		return prototype();
	}

  public void setFree(boolean b) {
    this.free = b;
    for(Destroyable x : items) {
      if (x instanceof FreePoint) {
        ((FreePoint) x).setFree(b);
      }
    }
  }

}
