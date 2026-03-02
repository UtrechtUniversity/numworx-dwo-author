package fi.dwo.dwojprint;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class Pager implements Printable {

	private int pages;

	public Pager(int pages) {
		this.pages = pages;
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex >= pages) return NO_SUCH_PAGE;
		graphics.drawString("Page " + (pageIndex+1), (int) pageFormat.getImageableX()+10, (int)pageFormat.getImageableY() + 20);
		return PAGE_EXISTS;
	}

}
