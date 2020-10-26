package fi.previewhtml;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Main implements AppletStub, AppletContext {

	private PreviewHTML printable;

	private Main() {
		super();
	}

		public static void main(String[] args) {
			PreviewHTML applet = new PreviewHTML();
			JFrame frame = new JFrame("preview html");
			Main stub = new Main();
			
			frame.setContentPane(applet);
			frame.setSize(1024,768);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			applet.setStub(stub);
			applet.init();
			applet.start();
			JMenuBar menubar = new JMenuBar();
			JMenu file = new JMenu("File");
			menubar.add(file);
			JMenuItem print = new JMenuItem("Print...");
			file.add(print);
			stub.printable = applet;
			print.addActionListener(stub::print);
			
			frame.setJMenuBar(menubar);
			
			frame.show();
						
		}

		private void print(ActionEvent ev) { 
			PrinterJob job = PrinterJob.getPrinterJob();
			PageFormat pf = job.defaultPage();
			job.setPrintable(printable, pf);
			boolean doPrint = job.printDialog();
			if( doPrint ) {
				try {
					job.print();
				} catch (PrinterException e1) {
					e1.printStackTrace();
				}
			}	
		}
		
		@Override
		public boolean isActive() {
			return true;
		}

		@Override
		public URL getDocumentBase() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public URL getCodeBase() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getParameter(String name) {
			if ("debug".equals(name)) return "true";
			if ("url".equals(name))
				return "https://test.dwo.nl/dwo/apps/player.html?locale=nl#371821";
			if ("API".equals(name))
			    return TestAPI.class.getName();
			return null;
		}

		@Override
		public AppletContext getAppletContext() {
			return this;
		}

		@Override
		public void appletResize(int width, int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public AudioClip getAudioClip(URL url) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Image getImage(URL url) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Applet getApplet(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enumeration<Applet> getApplets() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void showDocument(URL url) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void showDocument(URL url, String target) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void showStatus(String status) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setStream(String key, InputStream stream) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public InputStream getStream(String key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Iterator<String> getStreamKeys() {
			// TODO Auto-generated method stub
			return null;
		}
	
}
