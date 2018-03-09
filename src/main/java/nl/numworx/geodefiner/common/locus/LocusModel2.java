package nl.numworx.geodefiner.common.locus;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus.LocusModel;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.math.Numbers;
import fi.euclides.persist.CreateUtil;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import fi.euclides.util.Queue;

public class LocusModel2 extends Observable implements Codec, Observer, LocusModel {

	final private NameMapper mapper;
	final private PuntOp<?> source;
	private PuntOp<?> sourceCopy;
	final private Punt dest;
	private Punt destCopy;
	@SuppressWarnings("rawtypes")
	private Queue q;
	private HashMap<String,Destroyable> map;
	private int cnt;
	
	@SuppressWarnings("rawtypes")
	LocusModel2(PuntOp<?> sourceorg, Punt destorg, NameMapper mapper) {
		this.mapper = mapper;
		this.source = sourceorg;
		this.dest = destorg;
		map = new HashMap<String, Destroyable>();
		q = new Queue();
		punten = new Vector<Destroyable>();
		copies = new HashMap<>();
		toDestroy = new Vector<Destroyable>();
		writePunt(dest);
		destCopy = readPunt();
		
		for (Enumeration<Destroyable> iterator = punten.elements(); iterator.hasMoreElements();) {
			Destroyable punt = iterator.nextElement();
			punt.addObserver(this);
		}
		for (Enumeration<Destroyable> iterator = toDestroy.elements(); iterator.hasMoreElements();) {
			Destroyable d = iterator.nextElement();
			d.destroy();
		}
		toDestroy = null;
	}

	@SuppressWarnings("unchecked")
	private void writeObject(Object o) {
		q.addElement(o);
	}
	private Object readObject() {
		return q.poll();
	}
		
	Vector<Destroyable> punten, toDestroy;
	private HashMap<Destroyable,Destroyable> copies;
	public void destroy() {
		   for (Enumeration<Destroyable> iterator = punten.elements(); iterator.hasMoreElements();) {
				Destroyable punt = iterator.nextElement();
				punt.deleteObserver(this);
		   }
	}

	public Punt getDest() {
		return destCopy;
	}

	public PuntOp<?> getSource() {
		return sourceCopy;
	}

	@Override
	public Punt readPunt() {
		return (Punt) readDestroyable();
	}
	/**
	 * @param punt
	 * @return
	 */
	private boolean needCopy(Punt punt) {
		if(punt == null)
			return true;
		if(punt == source || punt == dest)
			return true;
		if(punt.key() == Punt.TYPE && punt.getIndex() != 0)
			return false;
		if(punt instanceof PuntOp)
		{
			return false;
		}
		return true;
	}

	@Override
	public void writePunt(Punt punt)  {
		Destroyable o;
		boolean b = punt != null && copies.containsKey(punt);
		boolean needCopy = needCopy(punt);

		if(needCopy)
			o = copy(punt);
		else
		{
			punten.addElement(punt);
			o = punt;
			b = true;
		}
		if(punt == source)
		{
			sourceCopy = (PuntOp<?>) o;
		}

		put(punt, o);
		writeObject(o);
		if (!b)
			try {
				punt.write(this);
			} catch (IOException e) {
				throw new IllegalArgumentException();
			}
	}

	@Override
	public Numbers readNumber() {
		return (Numbers) readObject();
	}

	@Override
	public void writeNumber(Numbers x) {
		writeObject(x);
	}

	/**
	 * @param destroyable
	 * @param o
	 */
	private void put(Destroyable destroyable, Destroyable o) {
		if(destroyable != null && map != null && mapper != null && destroyable.getIndex()!= 0)
			map.put(mapper.toString(destroyable), o);
	}

	@Override
	public Destroyable readDestroyable() {
		Destroyable object = (Destroyable) readObject();
		if(object.getIndex() == Integer.MIN_VALUE)
		{	object.setIndex(--cnt);
			try {
				object.read(this);
			} catch (IOException e) {
				throw new IllegalArgumentException();
			}		
		}
		if(object.getIndex()<0  && copies.containsKey(object))
		{
			Iterator<Destroyable> e = copies.keySet().iterator();
			while (e.hasNext()) {
				Destroyable original = e.next();
				if(original.equals(object))
				{
					toDestroy.addElement(object);
					object = original;						
					copies.put(original, original);
					put(original, original);
					punten.addElement(original);
					break;
				}
			}
		}
		return object;
	}

	private Destroyable copy(Destroyable op) {
		if(op == null)
			return null;
		Destroyable copy = (Destroyable) copies.get(op);
		if(copy != null)
			return copy;
		copy = CreateUtil.create(op.key());
		copy.setIndex(Integer.MIN_VALUE);
		copies.put(op, copy);
		return (Destroyable) copy;
	}

	
	
	@Override
	public void writeDestroyable(Destroyable op) {
		if(op instanceof Punt)
		{
			writePunt((Punt) op);
		} else
		{ 	
			boolean b = op != null && copies.containsKey(op);
			Destroyable copy = copy(op);
			put(op, copy);
			writeObject(copy);
			if(!b)
				try {
					op.write(this);
				} catch (IOException e) {
					throw new IllegalArgumentException();
				}			
		}
	}

	@Override
	public void writeVector(Vector v) throws IOException {
		throw new IOException();
	}

	@Override
	public Vector readVector() throws IOException {
		throw new IOException();
	}

	@Override
	public Lijn readLijn() {
		return (Lijn) readDestroyable();
	}

	@Override
	public void writeLijn(Lijn lijn) {
		writeDestroyable(lijn);		
	}

	@Override
	public void writeUTF(String string) {
		writeObject(string);
	}

	@Override
	public String readUTF() {
		return (String) readObject();
	}

	@Override
	public NameMapper getModel() {
		return mapper;
	}

	@Override
	public void write(Destroyable[] source) {
		for (int i = 0; i < source.length; i++) {
			writeDestroyable(source[i]);
		}
	}

	@Override
	public Destroyable[] read(Destroyable[] dest) throws IOException {
		throw new IOException();
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(arg != Destroyable.DESTROY && arg != Destroyable.VISIBLE)
			setChanged();
		notifyObservers();
	}

	@Override
	public void writeModel(Codec codec) throws IOException {
		codec.write(getDepend());
	}

	@Override
	public Destroyable[] getDepend() {
		return new Punt[] { source, dest };
	}

	@Override
	public void writeDelegate(LabelDelegate delegate) throws IOException {
		writeObject(delegate);
	}

	@Override
	public LabelDelegate readDelegate() throws IOException {
		return (LabelDelegate) readObject();
	}

}
