package nl.numworx.samllogin;

import java.util.Properties;

import javax.swing.JFrame;

import fi.beans.browser.PrintStreamConsole;
import fi.beans.browser.SimpleSwingBrowser;
import fi.beans.browser.Status;
import fi.previewhtml.DefaultAPI;

public class SamlLoginPanel {
	
	private static final class PrintStatus implements Status {
		@Override
		public void showStatus(String message) {
			System.out.println(message);
		}
	}

	private static class API extends DefaultAPI
	{
		Properties map = new Properties();
		@Override
		public String LMSSetValue(String key, String value) {
			System.out.println(key + " : " + value);
			map.setProperty(key, value);
			return super.LMSSetValue(key, value);
		}
		
	}
	
	
	public static void main(String[] args) {
		
		JFrame f = new JFrame("Login uu-dev");
		SimpleSwingBrowser browser = new SimpleSwingBrowser();
		browser.setConsole(new PrintStreamConsole());
		browser.setApi(new API());
		browser.setStatus(new PrintStatus());
		f.setContentPane(browser);
		
		f.pack();
		f.setVisible(true);
		browser.loadURL("https://uu-dev.dwo.nl/dwo/snoop");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
