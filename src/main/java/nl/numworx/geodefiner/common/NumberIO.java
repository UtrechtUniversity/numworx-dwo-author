package nl.numworx.geodefiner.common;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import fi.euclides.model.math.NumberCodec;
import fi.euclides.model.math.Numbers;

public class NumberIO implements DataInput,  NumberCodec {

	private List<Object> output;
	ObjectList input;
	int i = 0;
	
	public NumberIO() {
		this(null);
		output = new ArrayList<Object>(10);
	}
	
	public List<Object> toList() {
		return new ArrayList<Object>(output);
	}
	
	public NumberIO(ObjectList list) {
		this.input = list;
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
		while(n-->0) i++;
		return 0;
	}

	public boolean readBoolean() throws IOException {
		return input.getBoolean(i++);
	}

	public byte readByte() throws IOException {
		return (byte) input.getInt(i++);
	}

	public int readUnsignedByte() throws IOException {
		return readInt() & 0xFF;
	}

	public short readShort() throws IOException {
		return (short) readInt();
	}

	public int readUnsignedShort() throws IOException {
		return readInt() & 0xFFFF;
	}

	public char readChar() throws IOException {
		return (char) readUnsignedShort();
	}

	public int readInt() throws IOException {
		return input.getInt(i++);
	}

	public long readLong() throws IOException {
		return readInt();
	}

	public float readFloat() throws IOException {
		return (float) readDouble();
	}

	public double readDouble() throws IOException {
		//return input.getDouble(i++);
		return readInt(); // FIXME!!!
	}

	public String readLine() throws IOException {
		return null;
	}

	public String readUTF() throws IOException {
		return null;
	}


	public void writeDouble(Numbers numbers) throws IOException {
		output.add(FP);output.add(numbers.doubleValue());
	}


	public void writeRational(long a, long b) throws IOException {
		// FIXME |a| and |b| < MAX_INTEGER!!!!
		output.add(RAT);output.add(a); output.add(b);
	}


	public void writeRational(String a, String b) throws IOException {
		output.add(FP);
		output.add( Double.parseDouble(a)/Double.parseDouble(b));
	}


	public void writeInteger(int i) throws IOException {
		output.add(INT);output.add(i);
	}

	public void writeComplex(Numbers re, Numbers im) throws IOException {
		output.add(COMPLEX);re.writeNumber(this);im.writeNumber(this);
	}


	public void writeHilbert(Numbers base, boolean neg, Numbers sqrt)
			throws IOException {
		output.add(HILBERT);
		base.writeNumber(this);
		output.add(neg);
		sqrt.writeNumber(this);
	}

	public void writeZero() throws IOException {
		output.add(NUL);
	}

}
