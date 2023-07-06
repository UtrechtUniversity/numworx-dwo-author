package fi.statistiek.dotplot;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.statistiek.Statistiek;


/**
 * Dialog for setting up a Histogram
 * @author ManuDrijvers
 *
 *
 * this class is not used at the moment, since views are added immediately now
 */
public class DotplotSetUpDialog extends JDialog implements ActionListener, FocusListener {
	private DotplotModel model;
	private DotplotView view;
	
	private JButton doneButton;
	private JTextField nameField;
	
	/**
	 * Constructor for Frame owners
	 * @param owner owner of this dialog
	 * @param model Scatterplot MVC model
	 * @param view Scatterplot MVC view
	 */
	public DotplotSetUpDialog(Frame owner, DotplotModel model, DotplotView view) {
		super(owner, Statistiek.rb.getString("setupDialog"), true);
		this.model = model;
		this.view = view;
		
		this.createGUI();
	}
	
	/**
	 * Constructor for Dialog owners
	 * @param owner owner of this dialog
	 * @param model Scatterplot MVC model
	 * @param view Scatterplot MVC view
	 */
	public DotplotSetUpDialog(Dialog owner, DotplotModel model, DotplotView view) {
		super(owner, Statistiek.rb.getString("setupDialog"), true);
		this.model = model;
		this.view = view;
		
		this.createGUI();
	}
	
	/**
	 * Create the GUI
	 */
	private void createGUI() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.view, BorderLayout.CENTER);
		
		GridLayout gl = new GridLayout(1,3);
		gl.setHgap(5);
		gl.setVgap(5);
		
		JPanel setUpPanel = new JPanel(gl);
		JLabel nameLabel = new JLabel(Statistiek.rb.getString("viewnameLabel"));
		setUpPanel.add(nameLabel);
		this.nameField = new JTextField();
		this.nameField.setText(this.model.getViewName());
		this.nameField.addActionListener(this);
		this.nameField.addFocusListener(this);
		setUpPanel.add(this.nameField);
		
		this.doneButton = new JButton(Statistiek.rb.getString("doneButton"));
		this.doneButton.addActionListener(this);
		setUpPanel.add(this.doneButton);
		panel.add(setUpPanel, BorderLayout.SOUTH);
		
		super.setContentPane(panel);
		super.setSize(Math.min(600, super.getOwner().getWidth()), Math.min(400, super.getOwner().getHeight()));
		super.setLocation(super.getOwner().getLocation());
	}

	/**
	 * ActionListener implementation
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.nameField) {
			this.model.setViewName(this.nameField.getText());
		}
		else if(e.getSource() == this.doneButton) {
			super.setVisible(false);
		}	
	}

	/**
	 * FocusListener implementation
	 */
	public void focusGained(FocusEvent e) {
		// empty body, we only care about focus lost.
		
	}

	/**
	 * FocusListener implementation
	 */
	public void focusLost(FocusEvent e) {
		if(e.getSource() == this.nameField) {
			this.model.setViewName(this.nameField.getText());
		}
	}
}
