package org.cbook.cbookif.rm;

import java.io.IOException;

@SuppressWarnings("serial")
public class ResourceException extends IOException {

	public ResourceException() {
	}

	public ResourceException(String arg0) {
		super(arg0);
	}

	public ResourceException(Throwable t) {
		this(t.getMessage(), t);
	}

	public ResourceException(String message, Throwable t) {
		super(message);
		initCause(t);
	}

}
