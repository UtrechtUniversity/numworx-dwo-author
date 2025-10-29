/******************************************************
 * File: AppletUtil.java
 * created 16-May-00 11:54:46 AM by wim
 */

package fi.beans.appletutil;

import java.net.*;
import java.util.*;

import fi.beans.mainframe.JApplet;

import java.awt.*;
import java.io.*;

/**
 * Standaard Fi Utilities voor applets. Gebruik voor resourceBundles, Images
 * 
 * @author Wim van Velthoven
 */

public class AppletUtil {
	private JApplet applet;
	private String packageName, language;
	private Locale locale;
	private Hashtable images = new Hashtable();

	
	@Deprecated public AppletUtil(java.applet.Applet applet) {
		this ( (JApplet) applet);
	}
	
	/**
	 * Geef een Applet een standaard gelocaliseerde omgeving
	 */
	public AppletUtil(JApplet applet) {
		this.applet = applet;
		language = applet.getParameter("language");
		if (language == null)
			language = "nl";
		locale = new Locale(language, "");
		// System.err.println("lang=" + language + ", locale = " + locale);
		try {
			Locale.setDefault(locale);
		} catch (SecurityException ex) {
		}

		applet.setLocale(locale);
	}

	/**
	 * het l10n deel van een applet.
	 * 
	 * @param prefix
	 *            bundleprefix inclusief packagenaam.
	 */
	public ResourceBundle getBundle(String prefix) { // DIT IS 1.2
		return ResourceBundle.getBundle(prefix, locale,
		 applet.getClass().getClassLoader());
	}

	/**
	 * Haal Images als Resources op. Hack via getResourceAsStream als de
	 * ClassLoader getResource niet support (Netscape) en via getCodeBase als
	 * getResourceAsStream niet gesupport is.
	 * 
	 * @see java.lang.ClassLoader#getResource
	 */
	public Image getImage(String resourceName) {
		URL u = applet.getClass().getResource(resourceName);
		if (u != null) {
			return applet.getImage(u);
		}
		byte[] buffer = (byte[]) images.get(resourceName);
		if (buffer == null)
			try {
				InputStream in = applet.getClass().getResourceAsStream(resourceName);
				if (in == null) {
					// System.err.println(resourceName + " onvindbaar");
					// laatste kans via getcodebase
					return applet.getImage(applet.getCodeBase(), getPackage() + resourceName);
				}
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				buffer = new byte[1024];
				int len;
				do {
					len = in.read(buffer);
					// System.out.println("read " + len);
					if (len > 0)
						bos.write(buffer, 0, len);
				} while (len > 0);
				buffer = bos.toByteArray();
				in.close();
				bos.close();
				images.put(resourceName, buffer);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		return applet.getToolkit().createImage(buffer);
	}

	private String getPackage() {
		// dit is de fallback via codebase
		if (packageName == null) {
			String name = applet.getClass().getName();
			int i = name.lastIndexOf('.');
			if (i > 0)
				packageName = name.substring(0, i + 1).replace('.', '/');
			else
				packageName = "";
		}
		return packageName;
	}

	private URL getCodeBaseResource(String resource) {
		try {
			if (resource.charAt(0) == '/')
				return new URL(applet.getCodeBase(), resource.substring(1));
			else
				return new URL(applet.getCodeBase(), getPackage() + resource);
		} catch (MalformedURLException muex) {
			System.err.println(muex);
			return null;
		}
	}

	/**
	 * geef mij de Locale
	 * 
	 * @returns locale via applet parameter "language"
	 */
	public Locale getLocale() {
		return locale;
	}

	public InputStream getStream(String resource) {
		try {
			InputStream in = applet.getClass().getResourceAsStream(resource);
			if (in != null)
				return in;
		} catch (SecurityException sex) {
			System.err.println(sex);
		}
		URL u = getCodeBaseResource(resource);
		if (u != null)
			try {
				InputStream in = u.openStream();
				if (in != null)
					return in;
			} catch (IOException ioex) {
				System.err.println(ioex);
			}
		return null;
	}
}
