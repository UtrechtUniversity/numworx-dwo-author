package fi.euclides.expr;

public class InterpretException extends RuntimeException {

	public InterpretException(String message, Throwable cause) {
		super(message, cause);
	}

	public InterpretException(String message) {
		super(message);
	}

	public InterpretException(Throwable cause) {
		super(cause);
	}

}
