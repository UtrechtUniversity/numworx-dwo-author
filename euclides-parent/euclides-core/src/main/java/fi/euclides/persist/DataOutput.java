package fi.euclides.persist;

import java.io.IOException;

public interface DataOutput {

	void writeByte(int i) throws IOException;
	void writeUTF(String s) throws IOException;
	void writeInt(int i) throws IOException;
	void writeDouble(double doubleValue) throws IOException;
	void writeLong(long a) throws IOException;
	void writeBoolean(boolean neg) throws IOException;

}
