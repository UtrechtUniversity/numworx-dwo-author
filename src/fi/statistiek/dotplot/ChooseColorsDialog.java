package fi.statistiek.dotplot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * A dialog that lets the users choose two colors using JColorChooser class
 * @author Manu Drijvers
 *
 */
public class ChooseColorsDialog extends JDialog implements ActionListener {
	private JButton doneButton;
	private JColorChooser chooserA;
	private JColorChooser chooserB;
	
	/**
	 * Constructor for Dialog owner
	 * @param owner the owner of this dialog
	 * @param initialA initial color A
	 * @param initialB initial color B
	 */
	public ChooseColorsDialog(Dialog owner, Color initialA, Color initialB) {
		super(owner, true);
		
		this.createGUI(initialA, initialB);
	}
	
	/**
	 * Constructor for Frame owner
	 * @param owner the owner of this dialog
	 * @param initialA initial color A
	 * @param initialB initial color B
	 */
	public ChooseColorsDialog(Frame owner, Color initialA, Color initialB) {
		super(owner, true);
		
		this.createGUI(initialA, initialB);
	}

	/**
	 * Procedure that builds the GUI
	 * @param initialA initial color A
	 * @param initialB initial color B
	 */
	private void createGUI(Color initialA, Color initialB) {
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel centerPanel = new JPanel(new GridLayout(1,2));
		this.chooserA = new JColorChooser(initialA);
		this.chooserB = new JColorChooser(initialB);

		centerPanel.add(this.chooserA);
		centerPanel.add(this.chooserB);
		panel.add(centerPanel, BorderLayout.CENTER);
		
		this.doneButton = new JButton("Done");
		this.doneButton.addActionListener(this);
		panel.add(this.doneButton, BorderLayout.SOUTH);
		
		super.setContentPane(panel);
		super.setSize(300, 300);
	}

	/**
	 * @return The current value of color A
	 */
	public Color getColorA() {
		return this.chooserA.getColor();
	}
	
	/**
	 * @return The current value of color B
	 */
	public Color getColorB() {
		return this.chooserB.getColor();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		this.setVisible(false);		
	}
}
