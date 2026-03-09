package fi.dwo.dwogwtprint;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		Runnable r = new Runnable() {
			public void run() {
				JFrame f = new JFrame("Stub");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setVisible(true);
				DWOGWTPrint job;
				try {
					job = new DWOGWTPrint(f);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				job.stub = f;
				//job.printDialog();
				PageFormat format = job.defaultPage();
				format = job.pageDialog(format);
				// wat is a4?
				Printable painter = new ExtraPager(1,1);
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

}
