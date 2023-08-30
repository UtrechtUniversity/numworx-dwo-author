package nl.numworx.aimodel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

@SuppressWarnings("serial")
public class AIModelInteractiePanel extends JPanel implements InteractiePanel, CBookAware {

	public AIModelInteractiePanel() {
		setOpaque(true);
		setBackground(Color.GRAY);
	}

	public void addActionListener(ActionListener arg0) {
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.LIGHT_GRAY);
		int w = getWidth();
		int h = getHeight();
		g.fillRect(0, 0, w, h);
		g.setColor(Color.BLACK);
		g.drawString("AI-model", w/10, h/2);
	}

	public void destroy() {
	}

	public InteractieEditPanel getEditPanel() {
		return new AIModelInteractieEditPanel();
	}

	@SuppressWarnings("rawtypes")
	public Hashtable getEditState() {
		Hashtable launchData = new Hashtable();
		return launchData;
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
	}

	@SuppressWarnings("rawtypes")
	public void setState(Hashtable arg0) {
	}

	public void start() {
	}

	public void stop() {
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
	public void zetOpdracht(Hashtable arg0, String[] arg1, Hashtable arg2) {
	}

	@Override
	public void acceptCBookEvent(CBookEvent arg0) {
	}

	@Override
	public void addCBookEventListener(CBookEventListener arg0, String arg1) {
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] cmds = new String[] { "text.aimodel" };
		return cmds;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		return cmd;
	}

	@Override
	public String[] getSendCmds() {
		String[] cmds = new String[] { "tupels.aimodel", "action.visible" };
		return cmds;
	}

	@Override
	public void removeCBookEventListener(CBookEventListener arg0, String arg1) {
	}

	
	
}
