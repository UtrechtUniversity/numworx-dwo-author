package nl.numworx.geodefiner;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import fi.euclides.model.math.NumberCodec;
import fi.euclides.model.math.Numbers;

public class NumberIO implements DataInput,  NumberCodec {

	ListIterator<Number> i;
	List<Number> list;
	
	public NumberIO() {
		this(new ArrayList<Number>(10));
	}
	
	public List<Number> toList() {
		return new ArrayList<Number>(list);
	}
	
	public NumberIO(List<Number> list) {
		this.list = list;
		i = list.listIterator();
	}

	public void readFully(byte[] b) throws IOException {
		readFully(b, 0, b.length);
	}

	public void readFully(byte[] b, int off, int len) throws IOException {
		len += off;
		for (int i = off; i < len; i++) {
			b[i] = readByte();
		}
	}

	public int skipBytes(int n) throws IOException {
		while(n-->0) i.next();
		return 0;
	}

	// 0 = true
	public boolean readBoolean() throws IOException {
		return 0 == readByte();
	}

	public byte readByte() throws IOException {
		return i.next().byteValue();
	}

	public int readUnsignedByte() throws IOException {
		return i.next().intValue() & 0xFF;
	}

	public short readShort() throws IOException {
		return i.next().shortValue();
	}

	public int readUnsignedShort() throws IOException {
		return i.next().intValue() & 0xFFFF;
	}

	public char readChar() throws IOException {
		return (char) readUnsignedShort();
	}

	public int readInt() throws IOException {
		return i.next().intValue();
	}

	public long readLong() throws IOException {
		return i.next().longValue();
	}

	public float readFloat() throws IOException {
		return i.next().floatValue();
	}

	public double readDouble() throws IOException {
		return i.next().doubleValue();
	}

	public String readLine() throws IOException {
		return null;
	}

	public String readUTF() throws IOException {
		return null;
	}


	public void writeDouble(Numbers numbers) throws IOException {
		list.add(FP);list.add(numbers.doubleValue());
	}


	public void writeRational(long a, long b) throws IOException {
		list.add(RAT);list.add(a); list.add(b);
	}


	public void writeRational(String a, String b) throws IOException {
		list.add(FP);
		list.add( Double.parseDouble(a)/Double.parseDouble(b));
	}


	public void writeInteger(int i) throws IOException {
		list.add(INT);list.add(i);
	}

	public void writeComplex(Numbers re, Numbers im) throws IOException {
		list.add(COMPLEX);re.writeNumber(this);im.writeNumber(this);
	}


	public void writeHilbert(Numbers base, boolean neg, Numbers sqrt)
			throws IOException {
		list.add(HILBERT);
		base.writeNumber(this);
		list.add(neg?0:-1);
		sqrt.writeNumber(this);
	}

	public void writeZero() throws IOException {
		list.add(NUL);
		
	}

}
