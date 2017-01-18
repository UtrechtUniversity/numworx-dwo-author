package fi.euclides.gwt;

import com.google.gwt.i18n.client.NumberFormat;

import fi.euclides.model.math.DoubleFormat;

public class PrettyFormat extends DoubleFormat {

	public PrettyFormat() {
		super();
		format = NumberFormat.getFormat("0.##"); // -2
	}
	NumberFormat format;
	@Override
	protected String format(double doubleValue) {
		return format.format(doubleValue);
	}
	@Override
	protected void setMaxDigits(int value) {
		char ch = value < 0 ? '#' : '0';
		value = Math.min(10, Math.abs(value));
		String formule = "0.";
		for(int i = 0 ; i < value ; i++) formule += ch;
		format = NumberFormat.getFormat(formule);
	}

}
