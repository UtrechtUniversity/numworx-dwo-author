package fi.euclides.swing;

import java.text.NumberFormat;
import java.util.Locale;

public class DoubleFormat extends fi.euclides.model.math.DoubleFormat {

	
	private DoubleFormat(Locale locale, int maximum) {
		f = NumberFormat.getNumberInstance(locale);
		f.setGroupingUsed(false);
		setMaxDigits(maximum);
	}

	protected void setMaxDigits(int value)
	{
		f.setMinimumFractionDigits(Math.max(0,value));
		f.setMaximumFractionDigits(Math.abs(value));
	}
	
	static void setLocale(Locale locale, int maximumDigits)
	{
		DoubleFormat instance = new DoubleFormat(locale, maximumDigits);
		fi.euclides.model.math.DoubleFormat.setInstance(instance);		
	}
	
	public static void setLocale(Locale locale) {
		setLocale(locale, DEFAULT);
	}
	
	private NumberFormat f;
	
	protected String format(double doubleValue) {
		return f.format(doubleValue);
	}

	protected double parse(String string) throws Exception
	{
		return f.parse(string).doubleValue();
	}

}
