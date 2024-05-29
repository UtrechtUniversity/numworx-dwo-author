package nl.numworx.sqlite;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import fi.beans.numworxlf.JButton;
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class SQLiteInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	File db;
	Connection c; 
	JTextArea creator, output;
	JButton execBtn;
	
	public SQLiteInteractieEditPanel() {
		super(new BorderLayout(5,5));
		try {
			db = File.createTempFile("sqlite-", ".db");
			c = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath());
			db.deleteOnExit();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}		
		creator = new JTextArea(20, 60);
		output  = new JTextArea(20, 60);
		execBtn = new JButton("execute");
		execBtn.addActionListener(this);
		JScrollPane top = new JScrollPane(creator);
		JScrollPane bot = new JScrollPane(output);
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bot);
		add(split, BorderLayout.CENTER);
		add(execBtn, BorderLayout.SOUTH);		
	}
	

	public Hashtable getEditState() {
		Hashtable launchData = new Hashtable();
		launchData.put("create", creator.getText());
		try {
			launchData.put("url", db.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return launchData;
	}

	public void setEditState(Hashtable arg0) {
		String text = arg0.getOrDefault("create", "").toString();
		creator.setText(text);
	}

	public void start() {
	}

	public void stop() {
	}

	public void zetBreedte(int arg0) {
	}

	public void zetHoogte(int arg0) {
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String tekst = creator.getText();
		ScriptRunner runner = new ScriptRunner(c, false, true);
		StringWriter w = new StringWriter();
		PrintWriter pw = new PrintWriter(w);
		runner.setLogWriter(pw);
		runner.setErrorLogWriter(pw);
		try {
			runner.runScript(new StringReader(tekst));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(pw);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(pw);
		}
		output.setText(w.toString());
	}

}
