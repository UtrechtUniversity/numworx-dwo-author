package fi.dwo.dwojprint;

import java.io.IOException;
import java.io.OutputStream;

class NullOutputStream extends OutputStream {

	NullOutputStream() {
	}

	@Override
	public void write(int b) throws IOException {
	}

}
