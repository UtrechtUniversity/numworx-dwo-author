package fi.statistiek.histogram;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.statistiek.Statistiek;

/**
 * Dialog in which the user can manually define bin boundaries
 * @author Manu Drijvers
 *
 */
public class DefineBinBoundariesDialog extends JDialog implements ActionListener, FocusListener {
	private int noBins;
	private double minBoundary;
	private double binWidth;
	
	private JPanel panel;
	private JTextField noBinsField;
	private JTextField minBoundaryField;
	private JTextField binWidthField;
	
	private JLabel boundariesLabel;
	private JLabel noObjectsLabel;
	private JLabel minValueLabel;
	private JLabel maxValueLabel;
	
	private JButton doneButton;
	
	private boolean donePressed;
	
	private StatBinsModel model;
	
	/**
	 * Constructor for Dialog owner
	 * @param owner The dialog owner
	 * @param model datamodel
	 */
	public DefineBinBoundariesDialog(Dialog owner, StatBinsModel model) {
		super(owner, Statistiek.rb.getString("binboundariesDialog"), true);
		
		this.model = model;
		this.donePressed = false;
		
		if(this.model.getBinBoundaries().size() > 1) {
			this.noBins = this.model.getBinBoundaries().size()-1;
			this.minBoundary = this.model.getBinBoundaries().get(0);
			this.binWidth = this.model.getBinBoundaries().get(1) - this.model.getBinBoundaries().get(0); //this assumes equally sized bins 
		}
		else {
			this.noBins = 4;
			this.minBoundary = 0;
			this.binWidth = 10;
		}	
		
		this.buildGUI();
	}
	
	/**
	 * Constructor for Frame owner
	 * @param owner The dialog owner
	 * @param model datamodel
	 */
	public DefineBinBoundariesDialog(Frame owner, StatBinsModel model) {
		super(owner, Statistiek.rb.getString("binboundariesDialog"), true);
		
		this.model = model;
		this.donePressed = false;

		if(this.model.getBinBoundaries().size() > 1) {
			this.noBins = this.model.getBinBoundaries().size()-1;
			this.minBoundary = this.model.getBinBoundaries().get(0);
			this.binWidth = this.model.getBinBoundaries().get(1) - this.model.getBinBoundaries().get(0); //this assumes equally sized bins 
		}
		else {
			this.noBins = 4;
			this.minBoundary = 0;
			this.binWidth = 10;
		}	
		
		this.buildGUI();
	}
	
	/**
	 * Set up the GUI
	 */
	private void buildGUI() {
		this.panel = new JPanel(new BorderLayout());
		//super.setFont(super.getFont().deriveFont(super.getFont().getStyle() ^ Font.BOLD));	
		
		System.out.println("DefineBinBoundariesDialog.buildGUI(): font bold=" + super.getFont().isBold());
		
		GridLayout gl = new GridLayout(3,2);
		JPanel northPanel = new JPanel(gl);
		northPanel.add(new JLabel(Statistiek.rb.getString("classesLabel")));
		this.noBinsField = new JTextField();
		this.noBinsField.setText(new Integer(this.noBins).toString());
		this.noBinsField.addActionListener(this);
		this.noBinsField.addFocusListener(this);
		northPanel.add(this.noBinsField);
		northPanel.add(new JLabel(Statistiek.rb.getString("startvalueLabel")));
		this.minBoundaryField = new JTextField();
		this.minBoundaryField.setText(new Double(this.minBoundary).toString());
		this.minBoundaryField.addActionListener(this);
		this.minBoundaryField.addFocusListener(this);
		northPanel.add(this.minBoundaryField);
		northPanel.add(new JLabel(Statistiek.rb.getString("classwidthLabel")));
		this.binWidthField = new JTextField();
		this.binWidthField.setText(new Double(this.binWidth).toString());
		this.binWidthField.addActionListener(this);
		this.binWidthField.addFocusListener(this);
		northPanel.add(this.binWidthField);
		
		this.panel.add(northPanel, BorderLayout.NORTH);
		
		this.panel.add(new JLabel(Statistiek.rb.getString("boundariesintervalsLabel")), BorderLayout.CENTER);
		this.boundariesLabel = new JLabel("0 - 10.0 - 20.0 - 30.0 - 40.0");
		this.panel.add(this.boundariesLabel, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel(new GridLayout(3,1));
		JLabel waarnemingenLabel = new JLabel(Statistiek.rb.getString("observationsLabel"));
		Font boldFont = super.getFont().deriveFont(super.getFont().getStyle() ^ Font.BOLD);
		System.out.println(boldFont.isBold());
		waarnemingenLabel.setFont(boldFont);
		//TODO fix bold, waarom werkt het niet?

		southPanel.add(waarnemingenLabel);
		JPanel subPanel = new JPanel(new GridLayout(1,3));
		this.noObjectsLabel = new JLabel(Statistiek.rb.getString("numberLabel") + this.model.getTableModel().getRowCount());
		subPanel.add(this.noObjectsLabel);
		this.minValueLabel = new JLabel(Statistiek.rb.getString("minLabel") + this.model.getTableModel().getColumnMin(this.model.getColumnIndex()));
		subPanel.add(this.minValueLabel);
		this.maxValueLabel = new JLabel(Statistiek.rb.getString("maxLabel") + this.model.getTableModel().getColumnMax(this.model.getColumnIndex()));
		subPanel.add(this.maxValueLabel);
		southPanel.add(subPanel);
		this.doneButton = new JButton(Statistiek.rb.getString("doneButton"));
		this.doneButton.addActionListener(this);
		southPanel.add(this.doneButton);
		
		this.panel.add(southPanel, BorderLayout.SOUTH);
		
		super.setContentPane(this.panel);
		super.setSize(300,250);
		
		System.out.println("DefineBinBoundariesDialog.buildGUI(): font bold=" + super.getFont().isBold());

	}

	/**
	 * ActionListener implementation
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.noBinsField) {
			this.updateNoBins();
		}
		else if(e.getSource() == this.minBoundaryField) {
			this.updateMinBoundary();
		}
		else if(e.getSource() == this.binWidthField) {
			this.updateBinWidth();
		}
		else if(e.getSource() == this.doneButton) {
			this.donePressed = true;
			this.setVisible(false);
		}
	}

	/**
	 * Update the bin width from currect textfield input
	 */
	private void updateBinWidth() {
		try {
			this.binWidth = Double.parseDouble(this.binWidthField.getText());
		}
		catch(NumberFormatException e) {
			this.binWidthField.setText(new Double(this.binWidth).toString());
		}
		this.updateLabels();
	}

	/**
	 * Update the lowest boundary from current textfield input	 
	 */
	private void updateMinBoundary() {
		try {
			this.minBoundary = Double.parseDouble(this.minBoundaryField.getText());
		}
		catch(NumberFormatException e) {
			this.minBoundaryField.setText(new Double(this.minBoundary).toString());
		}
		this.updateLabels();
	}

	/**
	 * Update the number of bins from the current textfield input
	 */
	private void updateNoBins() {
		try {
			this.noBins = Integer.parseInt(this.noBinsField.getText());
		}
		catch(NumberFormatException e) {
			this.noBinsField.setText(new Integer(this.noBins).toString());
		}
		this.updateLabels();
	}

	/**
	 * Update the labels showing the current boundaries
	 */
	private void updateLabels() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i <= this.noBins; i++) {
			sb.append(this.minBoundary + i * this.binWidth);
			sb.append(" - ");
		}
		sb.delete(sb.length()-3, sb.length());
		this.boundariesLabel.setText(sb.toString());
	}
	
	/**
	 * FocusListener implementation
	 */
	public void focusGained(FocusEvent arg0) {
		//Empty body, we only care about focus lost.		
	}

	/**
	 * FocusListener implementation
	 */
	public void focusLost(FocusEvent e) {
		if(e.getSource() == this.noBinsField) {
			this.updateNoBins();
		}
		else if(e.getSource() == this.minBoundaryField) {
			this.updateMinBoundary();
		}
		else if(e.getSource() == this.binWidthField) {
			this.updateBinWidth();
		}
	}
	
	/**
	 * Get the user defined boundaries in an ArrayList
	 * @return an ArrayList containing the user defined bin boundaries
	 */
	public ArrayList<Double> getBoundaries() {
		ArrayList<Double> boundaries = new ArrayList<Double>();
		for(int i = 0; i <= this.noBins; i++) {
			boundaries.add(new Double(this.minBoundary + i * this.binWidth));
		}
		return boundaries;
	}
	
	public boolean isDonePressed() {
		return this.donePressed;
	}
}
