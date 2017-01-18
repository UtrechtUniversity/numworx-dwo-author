package fi.euclides.persist;

import java.io.IOException;

public interface DataInput {

	int readUnsignedByte() throws IOException;
	int readInt() throws IOException;
	String readUTF() throws IOException;
	long readLong() throws IOException;
	boolean readBoolean() throws IOException;
	double readDouble() throws IOException;

}
