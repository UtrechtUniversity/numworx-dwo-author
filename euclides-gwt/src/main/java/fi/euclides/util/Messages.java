package fi.euclides.util;

public class Messages {

	private static Messages _instance = new Messages();

	protected String getStringImpl(String string) {
		return string;
	}
	
	public static String getString(String string) {
		return _instance .getStringImpl(string);
	}

	/**
	 * @param instance the _instance to set
	 */
	public static void setInstance(Messages instance) {
		Messages._instance = instance;
	}

}
