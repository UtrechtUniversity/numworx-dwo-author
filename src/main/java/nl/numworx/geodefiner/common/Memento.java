package nl.numworx.geodefiner.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.persist.DataInput;
import fi.euclides.persist.DataOutput;

public class Memento extends fi.euclides.persist.Memento implements DataInput, DataOutput {
	
	
	private List<Object> list;
	private ObjectList olist; int cursor;

	public Memento(Tracker tracker) {
		list = new ArrayList<Object>();
		this.tracker = tracker;
		setDataInputStream(this);
		setDataOutputStream(this);
	}

	public void prepare(List<Destroyable> initial) {
		readObjects = new Vector<>(initial);
		writeObjects = new Vector<>(initial);
		maxIndex = 0;
	}
	
	public void fromList(ObjectList list) {
		olist = list;
		cursor = 0;
	}
	
	public List toList() {
		return list;
	}
	
	@Override
	public void writeUTF(String string) throws IOException {
		list.add(string);
	}

	@Override
	public String readUTF() throws IOException {
		return olist.getString(cursor++);
	}


	@Override
	public NameMapper getModel() {
		return tracker.getMapper();
	}


	@Override
	public int readUnsignedByte() throws IOException {
		return olist.getInt(cursor++);
	}

	@Override
	public int readInt() throws IOException {
	    if (cursor >= olist.size()) throw new IOException("EOF");
		return olist.getInt(cursor++);
	}

	@Override
	public long readLong() throws IOException {
		return Long.parseLong(olist.getString(cursor++));
	}

	@Override
	public boolean readBoolean() throws IOException {
		return olist.getBoolean(cursor++);
	}

	@Override
	public double readDouble() throws IOException {
		try {
			return olist.getDouble(cursor++);
		} catch (NullPointerException e) { // need patch in OBJECTLIST?
			return Double.NaN;
		}
	}

	@Override
	public void writeByte(int i) throws IOException {
		list.add(i);
	}

	@Override
	public void writeInt(int i) throws IOException {
		list.add(i);
	}
	
	@Override
	public void writeDouble(double d) throws IOException {
		list.add(d);
	}

	@Override
	public void writeLong(long a) throws IOException {
		list.add(Long.toString(a));
	}

	@Override
	public void writeBoolean(boolean b) throws IOException {
		list.add(b);
	}

	@Override
	public Model readModel(Tracker tracker) throws IOException {
		final Model m = tracker.getModel();
		Set<Destroyable> set = new HashSet<Destroyable>(m.getPunten());
		set.addAll(m.getLijnen());
		set.removeAll(readObjects); // keep readObjects
		for(Destroyable d : set) d.destroy(); // side effects.
		m.getPunten().clear();
		m.getLijnen().clear();
		super.readModel(tracker);
		m.readTrail(this); // Optional, throws and catches EOFException

		int f = m.getIndex(); // bepaal hoogste index;
		if (!m.getLijnen().isEmpty()) f = Math.max(f, m.getLijnen().lastElement().getIndex());
		if (!m.getPunten().isEmpty()) f = Math.max(f, m.getPunten().lastElement().getIndex());
		m.setIndex(f);
		return m;
	}

  @Override
  public void writeModel(Model model) throws IOException {
    super.writeModel(model);
    model.writeTrail(this);
  }
	
}
