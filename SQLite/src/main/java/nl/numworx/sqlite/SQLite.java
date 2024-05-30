package nl.numworx.sqlite;

import java.util.Locale;

import javax.sql.DataSource;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.sqlite.SQLiteDataSource;

import fi.beans.mainframe.JApplet;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

public class SQLite extends JApplet implements WiskOpdrApplet {

	public InteractiePanel getInteractiePanel() {
		return new SQLiteInteractiePanel();
	}

	static public DataSource getDataSource(String url) {
		SQLiteDataSource ds = new SQLiteDataSource();
		ds.setUrl(url);
		return ds;
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("SQLite editor");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JPanel content = new SQLiteInteractieEditPanel();
		f.setContentPane(content);
		f.pack();
		f.show();
	}

	private final Locale locale;

	public SQLite(Locale locale) {
		this.locale = locale;
	}
		
}
