package fi.dwo.dwogwtprint;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterIOException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fi.beans.numworxlf.JFileChooser;
import nl.numworx.swingbrowser.api.ConsoleEvent;
import nl.numworx.swingbrowser.api.StatusEvent;
import nl.numworx.swingbrowser.api.SwingBrowser;
import nl.numworx.swingbrowser.api.SwingBrowserFactory;
import nl.numworx.swingbrowser.api.SwingBrowserProvider;
import nl.numworx.swingbrowser.api.TitleEvent;
import nl.numworx.swingbrowser.jxb.JXBFactory;
import nl.numworx.swingbrowser.jxb.JXBStaticFactory;
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
	private Pageable document;
	private int copies;
	private String jobName;
	private boolean cancel;
	private File save = new File(new File(System.getProperty("user.home")), "Untitled.pdf");
	private Component main;
	private SwingBrowserFactory factory;
	private PrintRequestAttributeSet attributes;
	private JFrame stub;
	private Pager pager;
	
	private static final Paper A4 = new Paper();
	private SwingBrowser browser;
	static {
		A4.setSize(595, 842);
		A4.setImageableArea(18, 18, 559, 783);
	}
	
	
	public DWOGWTPrint() {
		format = defaultPage(new PageFormat());
		copies = 1;
		jobName = "Untitled";
		SwingBrowserProvider provider = new SwingBrowserProvider();
		factory = provider.getFactory();
		stub = new JFrame("PDF Print");
		pager = new Pager(1,1);
	}
	
	public DWOGWTPrint(Component main) {
		this();
		this.main = main;
	}

	public static void main(String[] args) throws PrinterException, InvocationTargetException, InterruptedException {
		Runnable r = new Runnable() {
			public void run() {
				JFrame f = new JFrame("Stub");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setVisible(true);
				DWOGWTPrint job = new DWOGWTPrint(f);
				job.stub = f;
				//job.printDialog();
				PageFormat format = job.defaultPage();
				format = job.pageDialog(format);
				// wat is a4?
				Pager painter = job.pager;
				job.setPrintable(painter, format);
				try {					
					job.print();
				} catch (PrinterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeAndWait(r);
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
		this.document = document;		
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
		page.setOrientation(PageFormat.LANDSCAPE);
		page.setPaper(A4);
		return page; // no default
	}

	@Override
	public PageFormat validatePage(PageFormat page) {
		return page;
	}


	@Override
	public void print(PrintRequestAttributeSet attributes) throws PrinterException {
		this.attributes = attributes;
		print();
	}

	@Override
	public void print() throws PrinterException {
		this.cancel = false;
		browser = factory.newBrowser();
		JComponent comp = browser.asComponent();
		stub.setContentPane(comp);
		comp.setSize((int)format.getImageableWidth(),(int)format.getImageableHeight());
		comp.setPreferredSize(comp.getSize());
		stub.pack();
		stub.show();
		browser.addConsoleListener(this);
		browser.addStatusListener(this);
		browser.addTitleListener(this);
		pager.reset();
		pager.setDelegate(painter);
		browser.setAPI(pager); // moet ik daartussen zitten, nu even niet
		browser.loadURL("http://localhost:8080/dwo/" + "apps/PrintPlayer.jsp#cmi.launch_data:1");
		Printing printing = browser.printing().get();
		printing.setPageFormat(format);
		printing.setPDFOutput(save);
		printing.addPrintListener(this);
		pager.getTerminated().onResolve(() -> {
			System.out.println("start print");
			printing.start();
		}
		);
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
		
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void onPrint(PrintEvent event) {
		if(true) return; /// keep 
		stub.hide();
		try {
			browser.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		browser = null;
		stub.setContentPane(new JPanel());
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
