package fi.dwo.dwojprint;

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

import fi.beans.numworxlf.JFileChooser;

public class DWOJPrint extends PrinterJob {

	private Printable painter;
	private PageFormat format;
	private Pageable document;
	private int copies;
	private String jobName;
	private boolean cancel;
	private File save = new File("Untitled.pdf");
	private Component main;
	
	private static final Paper A4 = new Paper();
	static {
		A4.setSize(595, 842);
		A4.setImageableArea(18, 18, 559, 783);
	}
	
	
	public DWOJPrint() {
		format = defaultPage(new PageFormat());
		copies = 1;
		jobName = "Untitled";
	}
	
	public DWOJPrint(Component main) {
		this();
		this.main = main;
	}

	public static void main(String[] args) throws PrinterException {
		PrinterJob job = new DWOJPrint();
		job.printDialog();
		PageFormat format = job.defaultPage();
		format = job.pageDialog(format);
		// wat is a4?
		Printable painter = new Pager(3);
		job.setPrintable(painter, format);
		job.print();
	}

	@Override
	public void setPrintable(Printable painter) {
		this.painter = painter;		
	}

	@Override
	public void setPrintable(Printable painter, PageFormat format) {
		this.painter = painter;
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
	public void print() throws PrinterException {
		this.cancel = false;
		try {
			FileOutputStream out = new FileOutputStream(save);
			PdfPrinter.printToPdf(painter, painter, format.getOrientation(), out);
			out.close();
		} catch(IOException oops) {
			throw new PrinterIOException(oops);
		}
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

}
