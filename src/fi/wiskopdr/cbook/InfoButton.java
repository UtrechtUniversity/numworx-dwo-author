package fi.wiskopdr.cbook;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.Constants;

import fi.beans.numworxlf.JOptionPane;
import fi.beans.wiskopdrbeans.CBookAware;

public class InfoButton extends JButton implements Action, Icon, Constants {
	public static final String INFO = "?";
	private Map<String, Object> values = new HashMap<String, Object>();
	private CBookContext context;
	private CBookAware   cba;
	
	public InfoButton(CBookInteractieEditPanel cBookInteractieEditPanel) {
		this(new CBABridge(cBookInteractieEditPanel),cBookInteractieEditPanel);
	}

	// This can be used by other dme widgets
	public InfoButton(CBookAware cba, CBookContext context) {
		super();
		this.cba = cba;
		this.context = context;
		values.put(LARGE_ICON_KEY, this);
		values.put(SMALL_ICON, this);
		setAction(this);
		setBorderPainted(false);
		setBorder(null);
		setContentAreaFilled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		StringBuilder tekst = new StringBuilder("<html>");
		tekst.append(cba).append("<br>");
		tekst.append("id: ").append(context.getProperty(UUID)).append("<br>");
		String[] sendCmds = cba.getSendCmds();
		if(sendCmds != null && sendCmds.length>0)
		{
			tekst.append("<h3>Sending</h3>");
			append(tekst, sendCmds);
		}
		String[] acceptedCmds = cba.getAcceptedCmds();
		if(acceptedCmds != null && acceptedCmds.length>0)
		{
			tekst.append("<h3>Accepting</h3>");
			append(tekst, acceptedCmds);
		}
		tekst.append("</html>");
		JEditorPane lbl = new JEditorPane("text/html", tekst.toString());
		lbl.setEditable(false);
		String title = String.valueOf(context.getProperty(INFO));
		JOptionPane.showMessageDialog(this, new JScrollPane(lbl), title, JOptionPane.PLAIN_MESSAGE);
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(c.getForeground());
		g.drawOval(x, y, getIconWidth()-1, getIconHeight()-1);
		Font f = c.getFont();
		FontMetrics fm = g.getFontMetrics(f);
		Rectangle2D rect = fm.getStringBounds(INFO, g);
		double w = rect.getWidth();
		double h = -rect.getY();	
		g.drawString(INFO, x+(int)(getIconWidth()-w)/2, y + (int)(getIconHeight()+h)/2);
		
	}

	@Override
	public int getIconWidth() {
		return 16;
	}

	@Override
	public int getIconHeight() {
		return 16;
	}

	@Override
	public Object getValue(String key) {
		return values.get(key);
	}

	@Override
	public void putValue(String key, Object value) {
		values.put(key, value);
	}

	private void append(StringBuilder tekst, String[] cmds) {
		for (int i = 0; i < cmds.length; i++) {
			String m = cmds[i];
			String m2 = cba.getLocalizedCmd(m);
			if(m.equals(m2)) {
				tekst.append(m);
			} else {
				tekst.append(m2).append(" (").append(m).append(")");
			}
			tekst.append("<br>");
		}
	}

}
