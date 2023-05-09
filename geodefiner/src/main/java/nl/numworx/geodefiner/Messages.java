package nl.numworx.geodefiner;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages extends fi.euclides.util.Messages {
	private static final String BUNDLE_NAME = "nl.numworx.geodefiner.resources.messages"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	static {
		fi.euclides.util.Messages.setInstance(new Messages());
	}
	private Messages() {
	}

	public static void setLocale(Locale locale) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}
	
	public String getStringImpl(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
