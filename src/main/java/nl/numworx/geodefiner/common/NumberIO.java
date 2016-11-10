package nl.numworx.geodefiner.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import fi.euclides.model.math.NumberCodec;
import fi.euclides.model.math.Numbers;

public class NumberIO implements NumberCodec {

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

	private boolean readBoolean() throws IOException {
		return input.getBoolean(i++);
	}

	private byte readByte() throws IOException {
		return (byte) input.getInt(i++);
	}

	private int readInt() throws IOException {
		return input.getInt(i++);
	}

	private double readDouble() throws IOException {
		return input.getDouble(i++);
	}

	private String readUTF() throws IOException {
		return input.getString(i++);
	}

	public void writeDouble(Numbers numbers) throws IOException {
		output.add(FP);output.add(numbers.doubleValue());
	}

	public void writeRational(long a, long b) throws IOException {
		writeRational(Long.toString(a),Long.toString(b));
	}

	public void writeRational(String a, String b) throws IOException {
		output.add(RAT);
		output.add(a);
		output.add(b);
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

	public Numbers readNumber() throws IOException {
		byte f = readByte();
		switch(f) {
		case NUL: return Numbers.ZERO;
		case INT: return Numbers.createInteger(readInt());
		case RAT: return Numbers.div(Numbers.valueOf(readUTF()), Numbers.valueOf(readUTF()));
		case FP:  return Numbers.createDouble(readDouble());
		case HILBERT: 
			Numbers base = readNumber();
			boolean neg = readBoolean();
			Numbers sqrt = Numbers.sqrt(readNumber());
			if(neg)
				return Numbers.sub(base, sqrt);
			return Numbers.add(base, sqrt);
		default: throw new IOException("Illegal Number");
		}
	}

}
