package fi.binomverdeling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class BVInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	public BVInteractieEditPanel() {
		super();
		super.add(new JLabel("test BVInteractieEditPanel"));
	}

	public void setEditState(Hashtable h) {

	}

	public Hashtable getEditState() {
		Hashtable h = new Hashtable();

		return h;
	}

	public void setBounds(int x, int y, int b, int h) {
		super.setBounds(x, y, b, h);

	}

	public void zetBreedte(int b) {
	}

	public void zetHoogte(int h) {
	}

	public void wis() {

	}

	public void zetMode(int mode) {

	}

	public void stop() {

	}

	public void start() {

	}

	public void addActionListener(ActionListener al) {

	}

	public void actionPerformed(ActionEvent e) {

	}

}
