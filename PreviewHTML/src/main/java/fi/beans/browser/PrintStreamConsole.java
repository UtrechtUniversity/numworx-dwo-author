package fi.beans.browser;

import java.io.PrintStream;

public class PrintStreamConsole extends Console {

	private PrintStream stream;
	
	public PrintStreamConsole() {
		this(System.out);
	}

	public PrintStreamConsole(PrintStream out) {
		stream = out;
	}

	public void log(String message) {
		stream.println(message);
	}

	@Override
	public void log(Object o) {
		// TODO Auto-generated method stub
		super.log(o);
	}

	@Override
	public void error(Object msg) {
		// TODO Auto-generated method stub
		super.error(msg);
	}

	@Override
	public void warn(Object msg) {
		// TODO Auto-generated method stub
		super.warn(msg);
	}

	@Override
	public void info(Object msg) {
		// TODO Auto-generated method stub
		super.info(msg);
	}

	@Override
	public void debug(Object msg) {
		// TODO Auto-generated method stub
		super.debug(msg);
	}
	
	
}
