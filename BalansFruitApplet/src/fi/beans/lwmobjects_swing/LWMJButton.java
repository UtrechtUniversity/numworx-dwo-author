package fi.beans.lwmobjects_swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;

public class LWMJButton extends LWMObject {

	private JButton button;
	
	public LWMJButton(Color c, int w, int h) {
		super(c, w, h);
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		button = new JButton();
		add(button, BorderLayout.CENTER);
		
		
	}

	public LWMJButton(Image i, int w, int h) {
		super(i, w, h);
		initialize();
	}

	public LWMJButton(Image i) {
		super(i);
		initialize();
	}

	public LWMJButton(int w, int h) {
		super(w, h);
		initialize();
	}

	public LWMJButton(String string, int i, int j) {
		super(i,j);
		initialize();
		setText(string);
	}

	public LWMJButton(Action action, int i, int j) {
		super(i,j);
		initialize();
		button.setAction(action);
	}

	public void addActionListener(ActionListener l) {
		button.addActionListener(l);
	}

	public void disable() {
		button.disable();
	}

	public void enable() {
		button.enable();
	}

	public String getActionCommand() {
		return button.getActionCommand();
	}

	public Dimension getPreferredSize() {
		return button.getPreferredSize();
	}

	public String getText() {
		return button.getText();
	}

	public boolean isEnabled() {
		return button.isEnabled();
	}

	public void setAction(Action a) {
		button.setAction(a);
	}

	public void setActionCommand(String actionCommand) {
		button.setActionCommand(actionCommand);
	}

	public void setEnabled(boolean b) {
		button.setEnabled(b);
	}

	public void setLabel(String label) {
		button.setLabel(label);
	}

	public void setText(String text) {
		button.setText(text);
	}

	public void setToolTipText(String text) {
		button.setToolTipText(text);
	}

	public String getToolTipText() {
		return button.getToolTipText();
	}

	public void paint(Graphics g) {
		super.paint(g);
		paintComponents(g);
	}

}
