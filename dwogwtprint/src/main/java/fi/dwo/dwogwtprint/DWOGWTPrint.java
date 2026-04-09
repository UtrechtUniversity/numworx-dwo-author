package fi.dwo.dwogwtprint;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.osgi.util.promise.Deferred;

import fi.beans.numworxlf.JFileChooser;
import fi.beans.numworxlf.JScrollPane;
import nl.numworx.swingbrowser.api.ConsoleEvent;
import nl.numworx.swingbrowser.api.StatusEvent;
import nl.numworx.swingbrowser.api.SwingBrowser;
import nl.numworx.swingbrowser.api.SwingBrowserFactory;
import nl.numworx.swingbrowser.api.SwingBrowserProvider;
import nl.numworx.swingbrowser.api.TitleEvent;
import nl.numworx.swingbrowser.print.PrintEvent;
import nl.numworx.swingbrowser.print.PrintListener;
import nl.numworx.swingbrowser.print.Printing;
import nl.numworx.swingbrowser.scorm.ConsoleListener;
import nl.numworx.swingbrowser.scorm.SCORM2004APIInterface;
import nl.numworx.swingbrowser.scorm.StatusListener;
import nl.numworx.swingbrowser.scorm.TitleListener;

public class DWOGWTPrint extends PrinterJob implements PrintListener, ConsoleListener, StatusListener, TitleListener {

	private SCORM2004APIInterface painter;
	private PageFormat format;
	private int copies;
	private String jobName;
	private boolean cancel;
	private File save = new File(new File(System.getProperty("user.home")), "Untitled.pdf");
	private Component main;
	private SwingBrowserFactory factory;
	JFrame stub;
	Pager pager;
	
	private static final Paper A4 = new Paper();
	private SwingBrowser browser;
	static {
		A4.setSize(595, 842);
		A4.setImageableArea(18, 18, 559, 783);
	}
	
	public DWOGWTPrint() throws IOException {
		format = defaultPage();
		copies = 1;
		jobName = "Untitled";
		SwingBrowserProvider provider = new SwingBrowserProvider();
		factory = provider.getFactory();
		stub = new JFrame("PDF Print");
		pager = new Pager();		
		SwingBrowser b = factory.newBrowser();
		boolean present = b.printing().isPresent();
		b.close();
		if (!present) throw new UnsupportedOperationException("no printing interface");
	}
	
	public DWOGWTPrint(Component main) throws IOException {
		this();
		this.main = main;
	}



	@Override
	public void setPrintable(Printable painter) {
		this.painter = (SCORM2004APIInterface) painter;		
	}

	@Override
	public void setPrintable(Printable painter, PageFormat format) {
		this.painter = (SCORM2004APIInterface) painter;
		this.format = format;
		
	}

	@Override
	public void setPageable(Pageable document) throws NullPointerException {		
	}

	@Override
	public boolean printDialog() throws HeadlessException {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int approve = chooser.showSaveDialog(main);
		if (approve != JFileChooser.APPROVE_OPTION) return false;
		save = new File( chooser.getSelectedFile(), "Untitled.pdf");
		setJobName(jobName);
		return true;
	}

	@Override
	public PageFormat pageDialog(PageFormat page) throws HeadlessException {
		return page;
	}

	@Override
	public PageFormat defaultPage(PageFormat page) {
		page = (PageFormat) page.clone();
		page.setOrientation(PageFormat.PORTRAIT);
		page.setPaper(A4);
		return page; // no default
	}

	@Override
	public PageFormat validatePage(PageFormat page) {
		return page;
	}

	@Override
	public void print() throws PrinterException {
		this.cancel = false;
		finish = new Deferred<Void>();
		browser = factory.newBrowser();
		JComponent comp = browser.asComponent();
		stub.setContentPane(new JScrollPane(comp, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		comp.setSize((int)format.getImageableWidth(),(int)format.getImageableHeight());
		comp.setPreferredSize(comp.getSize());
		stub.pack();
		stub.show();
		browser.addConsoleListener(this);
		browser.addStatusListener(this);
		browser.addTitleListener(this);
		pager.reset();
		pager.setDelegate(painter);
		browser.setAPI(pager);
		String serverUrl = painter.GetValue("dme.server_url");  // e.g. http://localhost:8080/dwo/
		//serverUrl = "https://teuniz.dwo.nl/dwo/";
		browser.loadURL(serverUrl + "apps/PrintPlayer.jsp#cmi.launch_data:1");
		Printing printing = browser.printing().get();
		printing.setPageFormat(format);
		printing.setPDFOutput(save);
		printing.addPrintListener(this);
		pager.getTerminated().onResolve(() -> {
			printing.start();
		}
		);
		try {
			finish.getPromise().getValue();
			System.out.println("Finished");
		} catch (InvocationTargetException | InterruptedException e) {
			System.err.println(e);
			PrinterException ex = new PrinterException("printing failed");
			ex.initCause(e);
			throw ex;
		} // BLOCK!
	}

	
	
	@Override
	public void setCopies(int copies) {
		this.copies = copies;
	}

	@Override
	public int getCopies() {
		return copies;
	}

	@Override
	public String getUserName() {		
		return System.getProperty("user");
	}

	@Override
	public void setJobName(String jobName) {
		this.jobName = jobName;
		save = new File(save.getParentFile(), jobName + ".pdf");
	}

	@Override
	public String getJobName() {
		return jobName;
	}

	@Override
	public void cancel() {
		this.cancel = true;
		finish.fail(new PrinterAbortException("Printing canceled"));
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	Deferred<Void> finish;
	
	@Override
	public void onPrint(PrintEvent event) {
		System.out.println(event);

		if(false) {
			finish.resolve(null);
			return;
		}
		//stub.hide();
		try {
			browser.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		browser = null;
		stub.setContentPane(new JPanel());
		finish.resolve(null);
	}

	@Override
	public void onConsole(ConsoleEvent event) {
		System.err.println(event.getMessage());		
	}

	@Override
	public void onStatus(StatusEvent event) {
		System.out.println("Status:" + event.getStatus());
	}

	@Override
	public void onTitle(TitleEvent event) {
		stub.setTitle(event.getTitle());
	}

}
