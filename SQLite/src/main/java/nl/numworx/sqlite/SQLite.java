package nl.numworx.sqlite;

import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.sqlite.JDBC;

import fi.beans.mainframe.JApplet;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

public class SQLite extends JApplet implements WiskOpdrApplet {

	public InteractiePanel getInteractiePanel() {
		return new SQLiteInteractiePanel();
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("SQLite editor");
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		JPanel content = new SQLiteInteractieEditPanel();
		f.setContentPane(content);
		f.pack();
		f.show();
	}

	private final Locale locale;

	public SQLite(Locale locale) {
		this.locale = locale;
	}
	
	static {
		new JDBC();
	}
	
}
