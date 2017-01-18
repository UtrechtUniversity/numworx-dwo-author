package fi.euclides.model.math;

import java.io.IOException;

public interface NumberCodec {

	public static final byte NUL = 0;
	public static final byte INT = 1;
	public static final byte RAT = 2;
	public static final byte FP = 3;
	public static final byte COMPLEX = 4;
	public static final byte HILBERT = 5;
	// for number streaming
	void writeDouble(Numbers numbers) throws IOException;
	void writeRational(long a, long b) throws IOException;
	void writeRational(String a, String b) throws IOException;
	void writeInteger(int i) throws IOException;
	void writeComplex(Numbers re, Numbers im) throws IOException;
	void writeHilbert(Numbers base, boolean neg, Numbers sqrt) throws IOException;
    void writeZero() throws IOException;
}
