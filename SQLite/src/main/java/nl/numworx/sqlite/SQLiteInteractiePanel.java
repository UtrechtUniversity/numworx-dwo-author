package nl.numworx.sqlite;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.numworxlf.JScrollPane;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

@SuppressWarnings("serial")
public class SQLiteInteractiePanel extends JPanel implements InteractiePanel, CBookAware {

	JTextArea output = new JTextArea();
	Connection c;
	Hashtable launch = new Hashtable();
  
	public SQLiteInteractiePanel() {
	    super(new BorderLayout());
	    add(new JScrollPane(output));
        try {
			c = SQLite.getDataSource("jdbc:sqlite:").getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addActionListener(ActionListener arg0) {
	}

	public void destroy() {
	}

	public InteractieEditPanel getEditPanel() {
		return new SQLiteInteractieEditPanel();
	}

	@SuppressWarnings("rawtypes")
	public Hashtable getEditState() {
		return launch;
	}

	public int getIpId() {
		return 0;
	}

	public int getScore() {
		return 0;
	}

	public int getScoreMax() {
		return 0;
	}

	public int[][] getScoreObjectives() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Hashtable getState() {
		Hashtable state = new Hashtable();
		state.put("output", output.getText());
		return state;
	}

	public boolean isCorrect() {
		return false;
	}

	public boolean isFout() {
		return false;
	}

	public void kijkNa() {
	}

	public void kijkNa(int arg0) {
	}

	public void opnieuw() {
	}

	@SuppressWarnings("rawtypes")
	public void setEditState(Hashtable arg0) {
	  launch = arg0;
	  if (arg0.containsKey("url")) {
		  try {
			c.close();
			c = SQLite.getDataSource("jdbc:sqlite::resource:" + arg0.get("url")).getConnection();
		} catch (SQLException e) {
		}		  
	  }	  
	  String create = arg0.getOrDefault("create", "").toString();
	  execute(c, create);
	}

	@SuppressWarnings("rawtypes")
	public void setState(Hashtable arg0) {
	  String o = arg0.getOrDefault("output", "").toString();
	  output.setText(o);
	}

	public void start() {
	}

	public void stop() {
	  try {
		c.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	}

	public void wis() {
	}

	public void zetMaat() {
	}

	public void zetMode(int arg0) {
	}

	public void zetNagekeken(boolean arg0) {
	}

	@SuppressWarnings("rawtypes")
	public void zetOpdracht(Hashtable h, String[] arg1, Hashtable arg2) {
	  setEditState(h);
	}

	@Override
	public void acceptCBookEvent(CBookEvent e) {
	  if ("text.program".equals(e.getCommand())) {
	    String content = Objects.toString(e.getParameter("content"), "");
	    StringWriter w = execute(c, content);
	    output.setText(w.toString());
	  }
	}

	static StringWriter execute(Connection c, String tekst) {
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
	        return w;
	}
	
	
	@Override
	public void addCBookEventListener(CBookEventListener arg0, String arg1) {
	  
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] cmds = new String[] { "text.program", "action.reset" };
		return cmds;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		return cmd;
	}

	@Override
	public String[] getSendCmds() {
		return new String[0];
	}

	@Override
	public void removeCBookEventListener(CBookEventListener arg0, String arg1) {
	}

	
	
}
