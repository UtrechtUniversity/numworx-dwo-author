package org.cbook.cbookif.rm;

@SuppressWarnings("serial")
public class ReadOnlyException extends ResourceException {

	public ReadOnlyException() {
	}

	public ReadOnlyException(String arg0) {
		super(arg0);
	}

	public ReadOnlyException(Throwable arg0) {
		super(arg0);
	}

	public ReadOnlyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
