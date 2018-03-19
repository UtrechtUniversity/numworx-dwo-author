package fi.euclides.persist;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.model.BoogRadiusHoek;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.MP;
import fi.euclides.model.RaakLijnConic;
import fi.euclides.model.RaaklijnLocus;
import fi.euclides.model.SnijpuntLijn;
import fi.euclides.model.Triangle;
import fi.euclides.model.Bissectrice;
import fi.euclides.model.Boog;
import fi.euclides.model.BoogHoek;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Cirkel3;
import fi.euclides.model.CirkelLijnSnijpunt;
import fi.euclides.model.CirkelRadius;
import fi.euclides.model.CirkelSnijpunt;
import fi.euclides.model.Codec;
import fi.euclides.model.ConflictLijn;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Dpunt;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.LoodLijn;
import fi.euclides.model.MiddelPunt;
import fi.euclides.model.Model;
import fi.euclides.model.ParallelLijn;
import fi.euclides.model.Poollijn;
import fi.euclides.model.Punt;
import fi.euclides.model.Punt2;
import fi.euclides.model.PuntOp;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.RaakLijnCirkel;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.SnijPunt;
import fi.euclides.model.SpiegelPunt;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.ZwaartePunt;
import fi.euclides.model.math.NumberCodec;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelDelegate;

public class Memento implements Codec , NumberCodec {

	protected static final String NULL = "N";

	protected static final String REF = "R";
	
	protected DataInput dis;
	protected DataOutput dos;

	protected Vector<Destroyable> readObjects, writeObjects;

	protected Model model;

	protected Tracker tracker;

	/**
	 * 
	 */
	public Memento() {
		model = new Model();
		
	}


	public Memento(DataInput dis) {
		this();
		setDataInputStream(dis);
	}
	
	public Memento(DataOutput dos) {
		setDataOutputStream(dos);
	}
	
	/**
	 * 
	 * @return a Number
	 * @throws IOException
	 */
	public Numbers readNumber() throws IOException {
		return Memento.readNumber(dis);
	}

	public Punt readPunt() throws IOException {
		return (Punt)readDestroyable();
	}
	
	public LabelDelegate readDelegate() throws IOException {
		String subkey = readUTF();
		LabelDelegate registered = tracker.getRegistered(subkey);
		if(registered == null)
			return Label.DEFAULT;
		return registered;
	}

	public void writeDelegate(LabelDelegate d) throws IOException {
		writeUTF(d.getSubKey());
	}
	
	public void writePunt(Punt p) throws IOException {
		writeDestroyable(p);
	}

	public Destroyable readDestroyable() throws IOException {
		String key = readUTF();
		if(NULL.equals(key))
		{
			return null;
		}
		if(REF.equals(key))
		{
			int index = readInt();
			return (Destroyable) readObjects.elementAt(index);
		}
		Destroyable p = CreateUtil.create(key);
		p.setIndex(dis.readUnsignedByte());
		if(p.getIndex()> maxIndex)
			maxIndex = p.getIndex();
		p.read(this);
		readObjects.addElement(p);
		return p;
	}

	protected int maxIndex;

	public void writeDestroyable(Destroyable p) throws IOException {
		int index;
		if(p == null)
			writeUTF(NULL);
		else if ((index = indexOfIsIs(p)) >= 0)
		{
			writeUTF(REF);
			writeInt(index);
		}
		else {			
			writeUTF(p.key());
			dos.writeByte(p.getIndex());
			p.write(this);
			writeObjects.addElement(p);
		}

	}

/**
 * indexOf op basis van ==.
 * @param p value
 * @return index
 */
	private int indexOfIsIs(Destroyable p) {
		for(int i = 0; i < writeObjects.size(); i++) {
			if ( p == writeObjects.elementAt(i)) return i;
		}
		return -1;
	}

	public void writeNumber(Numbers v) throws IOException {
		if(v == null) v = Numbers.ZERO;
		if(Numbers.ZERO == v)
			dos.writeByte(NumberCodec.NUL);
		else 
			v.writeNumber(this);
	}
	
	public void writeUTF(String s) throws IOException {
		dos.writeUTF(s);
	}
	

	public void setDataOutputStream(DataOutput dos) {
		this.dos = dos;
		writeObjects = new Vector<Destroyable>();
	}

	public void setDataInputStream(DataInput dis) {
		this.dis = dis;
		readObjects = new Vector<Destroyable>();
		maxIndex = 0;
		
	}


	public Vector<Destroyable> readVector() throws IOException {
		Vector<Destroyable> v;
		int size = readInt();
		v = new Vector<Destroyable>(size);
		for(int i = 0; i < size; i++)
		{
			v.addElement(readDestroyable());
		}
		return v;
	}


	/**
	 * @return
	 * @throws IOException
	 */
	public int readInt() throws IOException {
		return dis.readInt();
	}


	public void writeVector(Vector v) throws IOException {
		final int size = v.size();
		writeInt(size);
		for(int index = 0; index < size; index++)
			writeDestroyable((Destroyable) v.elementAt(index));
	}


	/**
	 * @param i
	 * @throws IOException
	 */
	public void writeInt(final int i) throws IOException {
		dos.writeInt(i);
	}


	public Lijn readLijn() throws IOException {
		return (Lijn)readDestroyable();
	}


	public void writeLijn(Lijn lijn) throws IOException {
		writeDestroyable(lijn);
	}

	public void writeModel(Model model) throws IOException {
		writeUTF("EUCLIDES MODEL V1.0\r\n");
		model.write(this);
		Vector invisible = new Vector();
		for(int i = 0; i < writeObjects.size(); i++)
		{
			Destroyable d = (Destroyable) writeObjects.elementAt(i);
			if(!d.isVisible())
				invisible.addElement(d);
		}
		writeVector(invisible);
	}
	public Model readModel(Tracker tracker) throws IOException { 
		String version =  readUTF();
		this.tracker = tracker;
		// TODO CHECK version
		if( !version.startsWith("EUCLIDES MODEL V"))
			throw new IOException("Version conflict: " + version);
		model = tracker.getModel();
		model.destroyAll();
		model.read(this);
		model.setIndex(maxIndex);
		Vector<Destroyable> invisible = readVector();
		for(int i = 0; i < invisible.size(); i++)
			(invisible.elementAt(i)).setVisible(false);
		return model;
	}

	public NameMapper getModel() {
		return model;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public String readUTF() throws IOException {
		return dis.readUTF();
	}


	public Destroyable[] read(Destroyable[] dest) throws IOException {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readDestroyable();
		}
		return dest;
	}

	public void write(Destroyable[] depend) throws IOException {
		for (int i = 0; i < depend.length; i++) {
			Destroyable destroyable = depend[i];
			writeDestroyable(destroyable);		
		}
	}


	public static Numbers readNumber(DataInput dis) throws IOException {
		int b = dis.readUnsignedByte();
		switch(b) {
		case NUL: return Numbers.ZERO;
		case NAN: return Numbers.NaN;
		case INT: return Numbers.createInteger(dis.readInt());
		case RAT: return Numbers.createRational(dis.readLong(), dis.readLong());
		case FP:  return Numbers.createDouble(dis.readDouble());
		case COMPLEX: return Numbers.createComplex(readNumber(dis), readNumber(dis));
		case HILBERT: 
				Numbers base = readNumber(dis);
				boolean neg = dis.readBoolean();
				Numbers sqrt = Numbers.sqrt(readNumber(dis));
				if(neg)
					return Numbers.sub(base, sqrt);
				return Numbers.add(base, sqrt);
				
		default: throw new IOException("Illegal Number");
		}
	}

	public void writeDouble(Numbers numbers) throws IOException {
		if (numbers.isNaN())
			dos.writeByte(NAN);
		else {
			dos.writeByte(NumberCodec.FP);
			dos.writeDouble(numbers.doubleValue());
		}
	}


	public void writeRational(long a, long b) throws IOException {
		dos.writeByte(NumberCodec.RAT);
		dos.writeLong(a);
		dos.writeLong(b);
	}


	public void writeInteger(int i) throws IOException {
		dos.writeByte(NumberCodec.INT);
		dos.writeInt(i);
	}


	public void writeComplex(Numbers re, Numbers im) throws IOException {
		dos.writeByte(NumberCodec.COMPLEX);
		writeNumber(re);
		writeNumber(im);
		
	}

	public void writeHilbert(Numbers base, boolean neg, Numbers sqrt)
			throws IOException {
		dos.writeByte(HILBERT);
		writeNumber(base);
		dos.writeBoolean(neg);
		writeNumber(sqrt);
		
	}

	public void writeZero() throws IOException {
		dos.writeByte(NUL);
	}


	public void writeRational(String a, String b) throws IOException {
		dos.writeByte(FP);
		dos.writeDouble(Double.parseDouble(a)/Double.parseDouble(b));
	}
}
