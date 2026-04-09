package nl.numworx.fsm.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.euclides.persist.DataOutput;

public class Output implements DataOutput {
	
	List<Object> objects;

	public Output() {
		objects = new ArrayList();
	}

	@Override
	public void writeByte(int i) throws IOException {
		objects.add(Byte.valueOf((byte) i));

	}

	@Override
	public void writeUTF(String s) throws IOException {
		objects.add(s);
	}

	@Override
	public void writeInt(int i) throws IOException {
		objects.add(Integer.valueOf(i));

	}

	@Override
	public void writeDouble(double doubleValue) throws IOException {
		objects.add(Double.valueOf(doubleValue));

	}

	@Override
	public void writeLong(long a) throws IOException {
		objects.add(Long.valueOf(a));

	}

	@Override
	public void writeBoolean(boolean b) throws IOException {
		objects.add(Boolean.valueOf(b));
	}

	public Object getData() { return objects; }
}
