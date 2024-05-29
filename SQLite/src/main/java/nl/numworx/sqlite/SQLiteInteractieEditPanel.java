package nl.numworx.sqlite;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import fi.beans.numworxlf.JButton;
import fi.beans.numworxlf.JFileChooser;
import fi.beans.numworxlf.JLabel;
import fi.beans.numworxlf.JTextField;
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class SQLiteInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	File db;
	Connection c; 
	JTextArea creator, output;
	JButton execBtn, importBtn, saveBtn;
	JTextField urlField;
	
	public SQLiteInteractieEditPanel() {
		super(new BorderLayout(5,5));
		try {
			db = File.createTempFile("sqlite-", ".db");
			c = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath());
			db.deleteOnExit();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}		
		creator = new JTextArea(15, 60);
		output  = new JTextArea(5, 60);
		execBtn = new JButton("execute");
		execBtn.addActionListener(this);
		importBtn = new JButton("import");
		importBtn.addActionListener(this);
		saveBtn = new JButton("save DB");
		saveBtn.addActionListener(this);
		urlField = new JTextField(db.toURI().toString());
		JScrollPane top = new JScrollPane(creator);
		JScrollPane bot = new JScrollPane(output);
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bot);
		split.setDividerLocation(creator.getPreferredSize().height);
		add(split, BorderLayout.CENTER);
		add(execBtn, BorderLayout.SOUTH);
		Box vb = Box.createVerticalBox();
		Box hb = Box.createHorizontalBox();
		hb.add(new JLabel("DB URL:"));
		hb.add(urlField);
		add(hb, BorderLayout.NORTH);
		vb.add(importBtn);
		vb.add(saveBtn);
		add(vb, BorderLayout.EAST);		
	}
	

	public Hashtable getEditState() {
		Hashtable<String,String> launchData = new Hashtable<>();
		launchData.put("create", creator.getText());
		String url = urlField.getText();
		if (url.equals(db.toURI().toString())) {
			// de default, geen!
		} else {
			launchData.put("url", url);
		}
		String path = db.getAbsolutePath();
		launchData.put("file", path);
		return launchData;
	}

	public void setEditState(Hashtable arg0) {
		String text = arg0.getOrDefault("create", "").toString();
		creator.setText(text);
		String path = (String) arg0.get("file");
		File f = new File(path);
		if (f.exists()) {
			try {
				c.close();
				db.delete();
				db = f;
				c = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath());
				urlField.setText(db.toURI().toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (arg0.containsKey("url")) {
			urlField.setText(arg0.get("url").toString());
		}
		
	}

	public void start() {
	}

	public void stop() {
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void zetBreedte(int arg0) {
	}

	public void zetHoogte(int arg0) {
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (execBtn == e.getSource()) {
			StringWriter w = executeCreator();
			output.setText(w.toString());
		} else if (importBtn == e.getSource()) {
			JFileChooser jf = new JFileChooser();
			int ok = jf.showOpenDialog(this);
			if (ok == JFileChooser.APPROVE_OPTION) {
				output.setText(jf.getSelectedFile() + " import\n\n");
				try {
					FileReader rf = new FileReader(jf.getSelectedFile());
					creator.setText("");
					char[] buffer = new char[4096];int len; 
					while( (len = rf.read(buffer)) > 0) {
						creator.append(new String(buffer, 0, len));
					}
					rf.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					c.close();
					db.delete();
					c = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath());
					output.append(executeCreator().toString());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		} else if (saveBtn == e.getSource()) {
			JFileChooser jf = new JFileChooser();
			int ok = jf.showSaveDialog(this);
			if (ok == JFileChooser.APPROVE_OPTION) {
				output.setText(jf.getSelectedFile() + " saved");
			}
		}
	}


	private StringWriter executeCreator() {
		String tekst = creator.getText();
		StringWriter w = SQLiteInteractiePanel.execute(c, tekst);
		return w;
	}

}
