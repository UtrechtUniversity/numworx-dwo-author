package nl.numworx.fsm.editor;

import java.io.EOFException;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import fi.euclides.persist.DataInput;

public class Input implements DataInput {

	Iterator<Object> objects;
	
	@SuppressWarnings("unchecked")
	public Input(Object data) {
		if (data instanceof Iterable) 
			objects = ((Iterable<Object>) data).iterator();
		else
			objects = Collections.emptyIterator();
	}

	@SuppressWarnings("unchecked")
	private <T> T next() throws EOFException {
		if (!objects.hasNext()) throw new EOFException("End of stream");
		return (T) objects.next();
	}
	
	@Override
	public int readUnsignedByte() throws IOException {
		Number n = next();
		return n.intValue() & 0xFF;
	}

	@Override
	public int readInt() throws IOException {
		Number i = next();
		return i.intValue();
	}

	@Override
	public String readUTF() throws IOException {
		return next();
	}

	@Override
	public long readLong() throws IOException {
		Number l = next();
		return l.longValue();
	}

	@Override
	public boolean readBoolean() throws IOException {
		Boolean b = next();
		return b;
	}

	@Override
	public double readDouble() throws IOException {
		Number d = next();
		return d.doubleValue();
	}

}
