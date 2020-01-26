package fi.ivmdraw;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class IVMdrawPanel extends JPanel implements InteractiePanel, CBookAware {

	private ActionListener listener;
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);

	public IVMdrawPanel(IVMdraw applet) {
		JLabel label = new JLabel("IVM Draw");
		add(label);
	}

	public void paintComponent(Graphics g) {
		g.setColor(new Color(200, 200, 200));
		g.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
		//super.paintComponent(g);
	}

	public void addActionListener(ActionListener listener) {
		this.listener = AWTEventMulticaster.add(this.listener, listener);
	}

	public void destroy() {

	}

	public IVMdrawEditPanel getEditPanel() {
		return new IVMdrawEditPanel(this);
	}

	public Hashtable getEditState() {
		Hashtable editState = new Hashtable();

		return editState;
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

	public void kijkNa(int n) {
	}

	public void opnieuw() {
	}

	public void setEditState(Hashtable editState) {

	}

	public void setState(Hashtable state) {

	}

	public void start() {

	}

	public void stop() {

	}

	public void wis() {

	}

	public void zetMaat() {

	}

	public void zetMode(int mode) {

	}

	public void zetNagekeken(boolean nagekeken) {

	}

	public void zetOpdracht(Hashtable editState, String[] names, Hashtable random) {

	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {

	}

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] commands = { "graph" };
		return commands;
	}

	@Override
	public String getLocalizedCmd(String command) {
		String localizedCmd = IVMdraw.rb.getString(CBA_PREFIX + command);
		if (localizedCmd == null)
			return command;
		return localizedCmd;
	}

	@Override
	public String[] getSendCmds() {
		String[] commands = { "action.correct", "action.false", "text.feedback", "graph" };
		return commands;
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
	}

}
