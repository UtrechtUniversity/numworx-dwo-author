package fi.euclides.model.math;

public class DoubleFormat {

	public static int DEFAULT = -2;
	
	static public void setMaximumFractionDigits(int value)
	{
		_instance.setMaxDigits(value);
	}
		
	protected void setMaxDigits(int value) {
	}

	private static DoubleFormat _instance = new DoubleFormat();

	public static String toString(double doubleValue) {
		return _instance.format(doubleValue);
	}

	protected String format(double doubleValue) {
		return String.valueOf(doubleValue);
	}

	public static String toString(Numbers value) {
		return toString(value.doubleValue());
	}

	public static Numbers valueOf(String value) {
		try {
			return Numbers.createDouble(_instance.parse(value));
		} catch (Exception e) {
		}
		return null;
	}

	protected double parse(String value) throws Exception {
		return new Double(value).doubleValue();
	}

	public static void setInstance(DoubleFormat instance) {
		_instance = instance;
	}

}
