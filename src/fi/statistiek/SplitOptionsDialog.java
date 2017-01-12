package fi.statistiek;

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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * Dialog allowing the user to define split options
 * @author Manu Drijvers
 *
 */
public class SplitOptionsDialog extends JDialog implements ActionListener, FocusListener {
	private SplitOptions splitOptions;
	private StatTableModel model;
	private BinBoundariesPanel binBoundariesPanel;

	private JComboBox splitColumnBox;
	
	private SplitEnumPanel enumPanel;
	private SplitStringPanel stringPanel;
	private JPanel panel;
	private JButton doneButton;
	private boolean donePressed;
	
	/**
	 * Constructor for Frame owner
	 * @param owner the dialog owner
	 * @param splitOptions the initial split options
	 * @param model the data model
	 */
	public SplitOptionsDialog(Frame owner, SplitOptions splitOptions, StatTableModel model) {
		super(owner, Statistiek.rb.getString("splitoptionsDialog"), true);
		this.splitOptions = splitOptions.clone();
		this.model = model;
		this.donePressed = false;
		
		this.buildGUI();
	}
	
	/**
	 * Constructor for Dialog owner
	 * @param owner the dialog owner
	 * @param splitOptions the initial split options
	 * @param model the data model
	 */
	public SplitOptionsDialog(Dialog owner, SplitOptions splitOptions, StatTableModel model) {
		super(owner, Statistiek.rb.getString("splitoptionsDialog"), true);
		this.splitOptions = splitOptions.clone();
		this.model = model;
		this.donePressed = false;
		
		this.buildGUI();
	}
	
	/**
	 * Build the GUI
	 */
	private void buildGUI() {
		this.binBoundariesPanel = new BinBoundariesPanel();
		this.enumPanel = new SplitEnumPanel();
		this.stringPanel = new SplitStringPanel();
		
		this.panel = new JPanel(new BorderLayout());
		JPanel splitColumnPanel = new JPanel(new GridLayout(2,1));
		JLabel splitColumnLabel = new JLabel(Statistiek.rb.getString("splitvariableLabel"));
		this.splitColumnBox = new JComboBox();
		splitColumnBox.addItem(Statistiek.rb.getString("noneItem"));
		for(int column = 0; column < this.model.getColumnCount(); column++) {
			splitColumnBox.addItem(this.model.getColumnName(column));
		}
		splitColumnBox.setSelectedIndex(this.splitOptions.getColumnSplitIndex()+1);
		this.splitColumnBox.setActionCommand("splitColumnBox");
		this.splitColumnBox.addActionListener(this);
		
		splitColumnPanel.add(splitColumnLabel);
		splitColumnPanel.add(this.splitColumnBox);
		this.panel.add(splitColumnPanel, BorderLayout.NORTH);
		
		this.doneButton = new JButton(Statistiek.rb.getString("doneButton"));
		this.doneButton.addActionListener(this);
		this.panel.add(this.doneButton, BorderLayout.SOUTH);
		
		super.setContentPane(this.panel);
		super.setSize(300,300);
		
		if(this.model.isColumnIndexValid(this.splitOptions.getColumnSplitIndex())) {
			this.setSplitType(this.model.getColumnTypes().get(this.splitOptions.getColumnSplitIndex()).getType());
		}

	}
	
	/**
	 * Update the GUI to display the right options for the specified type
	 * @param type the type of variable by which the data is split
	 */
	private void setSplitType(AllowedTypes type) {
		if(type.isNumber()) {
			ArrayList<Number> boundaries = new ArrayList<Number>();
			boundaries = Statistiek.appropriateBoundaries(
					this.model.getColumnMin(this.splitOptions.getColumnSplitIndex()),
					this.model.getColumnMax(this.splitOptions.getColumnSplitIndex()),
					this.binBoundariesPanel.noBins);
			
			this.splitOptions.setBinBoundaries(boundaries);
			this.binBoundariesPanel.minBoundary = boundaries.get(0).doubleValue();
			this.binBoundariesPanel.binWidth = boundaries.get(1).doubleValue() - boundaries.get(0).doubleValue(); //this assumes equally sized bins 

			super.remove(this.enumPanel);
			super.remove(this.stringPanel);
			super.add(this.binBoundariesPanel, BorderLayout.CENTER);
			
			this.binBoundariesPanel.update();
		}
		else if(type.equals(AllowedTypes.ENUM)){
			this.enumPanel.update();
			
			super.remove(this.binBoundariesPanel);
			super.remove(this.stringPanel);
			super.add(this.enumPanel, BorderLayout.CENTER);
		}
		else {
			this.stringPanel.update();
			
			super.remove(this.binBoundariesPanel);
			super.remove(this.enumPanel);
			super.add(this.stringPanel, BorderLayout.CENTER);
		}
		
		super.validate();
		super.repaint();
	}
	
	public void focusGained(FocusEvent arg0) {
		// Do nothing
		
	}

	public void focusLost(FocusEvent e) {
		if(e.getSource() == this.binBoundariesPanel.noBinsField) {
			this.binBoundariesPanel.updateNoBins();
		}
		else if(e.getSource() == this.binBoundariesPanel.minBoundaryField) {
			this.binBoundariesPanel.updateMinBoundary();
		}
		else if(e.getSource() == this.binBoundariesPanel.binWidthField) {
			this.binBoundariesPanel.updateBinWidth();
		}
	}

	/**
	 * ActionListener implementation
	 */
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		if(ac.equals("noBinsField")) {
			System.out.println("noBinFieldUpdate");
			this.binBoundariesPanel.updateNoBins();
		}
		else if(ac.equals("minBoundaryField")) {
			this.binBoundariesPanel.updateMinBoundary();
		}
		else if(ac.equals("binWidthField")) {
			this.binBoundariesPanel.updateBinWidth();
		}
		else if(ac.equals("autoButton")) {
			this.setSplitType(AllowedTypes.DOUBLE);
		}
		else if(e.getSource() == this.splitColumnBox) {
			System.out.println("SplitColumnUpdate!");
			if(this.splitColumnBox.getSelectedIndex()-1 != this.splitOptions.getColumnSplitIndex()) {
				this.splitOptions.setColumnSplitIndex(this.splitColumnBox.getSelectedIndex()-1);
				
				if(this.splitColumnBox.getSelectedIndex() == 0) {
					super.remove(this.binBoundariesPanel);
					super.remove(this.stringPanel);
					super.remove(this.enumPanel);
				}
				else {
					this.setSplitType(this.model.getColumnTypes().get(this.splitOptions.getColumnSplitIndex()).getType());
				}
			}
		}
		else if(e.getSource() == this.doneButton) {
			this.donePressed = true;
			super.setVisible(false);
		}
	}

	/**
	 * @return  donePressed
	 */
	public boolean isDonePressed() {
		return donePressed;
	}
	
	/**
	 * Get the split options
	 * @return the split options
	 */
	public SplitOptions getSplitOptions() {
		return this.splitOptions;
	}

	/**
	 * Component that allows a user to define bin boundaries to split on a numerical variable
	 */
	private class BinBoundariesPanel extends JPanel {
		private int noBins;
		private double minBoundary;
		private double binWidth;
		
		private JTextField noBinsField;
		private JTextField minBoundaryField;
		private JTextField binWidthField;
		
		private JButton autoButton;
		private JTextArea boundariesArea;
		private JLabel noObjectsLabel;
		private JLabel minValueLabel;
		private JLabel maxValueLabel;
		
		/**
		 * Constructor
		 */
		public BinBoundariesPanel() {
			super(new BorderLayout());
			
			GridLayout gl = new GridLayout(4,2);
			gl.setVgap(10);
			JPanel northPanel = new JPanel(gl);
			northPanel.add(new JLabel(Statistiek.rb.getString("classesLabel")));
			this.noBinsField = new JTextField();
			this.noBinsField.setText(new Integer(this.noBins).toString());
			this.noBinsField.setActionCommand("noBinsField");
			this.noBinsField.addActionListener(SplitOptionsDialog.this);
			this.noBinsField.addFocusListener(SplitOptionsDialog.this);
			northPanel.add(this.noBinsField);
			this.autoButton = new JButton(Statistiek.rb.getString("autoButton"));
			this.autoButton.setActionCommand("autoButton");
			this.autoButton.addActionListener(SplitOptionsDialog.this);
			northPanel.add(this.autoButton);
			northPanel.add(new JLabel());
			northPanel.add(new JLabel(Statistiek.rb.getString("startvalueLabel")));
			this.minBoundaryField = new JTextField();
			this.minBoundaryField.setText(new Double(this.minBoundary).toString());
			this.minBoundaryField.setActionCommand("minBoundaryField");
			this.minBoundaryField.addActionListener(SplitOptionsDialog.this);
			this.minBoundaryField.addFocusListener(SplitOptionsDialog.this);
			northPanel.add(this.minBoundaryField);
			northPanel.add(new JLabel(Statistiek.rb.getString("classwidthLabel")));
			this.binWidthField = new JTextField();
			this.binWidthField.setText(new Double(this.binWidth).toString());
			this.binWidthField.setActionCommand("binWidthField");
			this.binWidthField.addActionListener(SplitOptionsDialog.this);
			this.binWidthField.addFocusListener(SplitOptionsDialog.this);
			northPanel.add(this.binWidthField);
			
			super.add(northPanel, BorderLayout.NORTH);
			
			super.add(new JLabel(Statistiek.rb.getString("boundariesintervalsLabel")), BorderLayout.CENTER);
			this.boundariesArea = new JTextArea();
			this.boundariesArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(this.boundariesArea);
			super.add(scrollPane, BorderLayout.CENTER);
			
			JPanel southPanel = new JPanel(new GridLayout(3,1));
			JLabel waarnemingenLabel = new JLabel(Statistiek.rb.getString("observationsLabel"));
			Font boldFont = super.getFont().deriveFont(super.getFont().getStyle() ^ Font.BOLD);
			System.out.println(boldFont.isBold());
			waarnemingenLabel.setFont(boldFont);
	
			southPanel.add(waarnemingenLabel);
			JPanel subPanel = new JPanel(new GridLayout(1,3));
			this.noObjectsLabel = new JLabel(Statistiek.rb.getString("numberLabel"));
			subPanel.add(this.noObjectsLabel);
			this.minValueLabel = new JLabel(Statistiek.rb.getString("minLabel"));
			subPanel.add(this.minValueLabel);
			this.maxValueLabel = new JLabel(Statistiek.rb.getString("maxLabel"));
			subPanel.add(this.maxValueLabel);
			southPanel.add(subPanel);
			
			this.noBins = 4;
			
			super.add(southPanel, BorderLayout.SOUTH);
			
			this.update();
		}
		
		private void update() {
			if(SplitOptionsDialog.this.model.isColumnIndexValid(SplitOptionsDialog.this.splitOptions.getColumnSplitIndex())) {
				this.noBins = SplitOptionsDialog.this.splitOptions.getBinBoundaries().size()-1;
				this.minBoundary = SplitOptionsDialog.this.splitOptions.getBinBoundaries().get(0).doubleValue();
				this.binWidth = SplitOptionsDialog.this.splitOptions.getBinBoundaries().get(1).doubleValue()
					- SplitOptionsDialog.this.splitOptions.getBinBoundaries().get(0).doubleValue(); //this assumes equally sized bins 
				this.minValueLabel.setText(Statistiek.rb.getString("minLabel") + SplitOptionsDialog.this.model.getColumnMin(SplitOptionsDialog.this.splitOptions.getColumnSplitIndex()));
				this.maxValueLabel.setText(Statistiek.rb.getString("maxLabel") + SplitOptionsDialog.this.model.getColumnMax(SplitOptionsDialog.this.splitOptions.getColumnSplitIndex()));
				
				this.updateLabels();
			}
		}
		
		/**
		 * Update the bin width from current textfield input
		 */
		private void updateBinWidth() {
			try {
				this.binWidth = Double.parseDouble(this.binWidthField.getText());
				this.updateBinBoundaries();

			}
			catch(NumberFormatException e) {
				this.binWidthField.setText(new Double(this.binWidth).toString());
			}
		}

		/**
		 * Update the lowest boundary from current textfield input	 
		 */
		private void updateMinBoundary() {
			try {
				this.minBoundary = Double.parseDouble(this.minBoundaryField.getText());
				this.updateBinBoundaries();
			}
			catch(NumberFormatException e) {
				this.minBoundaryField.setText(new Double(this.minBoundary).toString());
			}
		}

		/**
		 * Update the number of bins from the current textfield input
		 */
		private void updateNoBins() {
			try {
				this.noBins = Integer.parseInt(this.noBinsField.getText());
				this.updateBinBoundaries();
			}
			catch(NumberFormatException e) {
				this.noBinsField.setText(new Integer(this.noBins).toString());
			}
		}
		
		/**
		 * Update the bin boundaries from the user input
		 */
		private void updateBinBoundaries() {
			ArrayList<Number> boundaries = new ArrayList<Number>();
			for(int i = 0; i <= this.noBins; i++) {
				boundaries.add((i * this.binWidth) + this.minBoundary);
			}
			SplitOptionsDialog.this.splitOptions.setBinBoundaries(boundaries);
			this.updateLabels();
		}
		
		private void updateMinMax() {
			this.minValueLabel = new JLabel(Statistiek.rb.getString("minLabel") + SplitOptionsDialog.this.model.getColumnMin(SplitOptionsDialog.this.splitOptions.getColumnSplitIndex()));
			this.maxValueLabel = new JLabel(Statistiek.rb.getString("maxLabel") + SplitOptionsDialog.this.model.getColumnMax(SplitOptionsDialog.this.splitOptions.getColumnSplitIndex()));
		}

		/**
		 * Update the labels showing the current boundaries
		 */
		private void updateLabels() {
			this.binWidthField.setText(Double.toString(this.binWidth));
			this.noBinsField.setText(Integer.toString(this.noBins));
			this.minBoundaryField.setText(Double.toString(this.minBoundary));

			StringBuilder sb = new StringBuilder();
			for(int i = 0; i <= this.noBins; i++) {
				sb.append(SplitOptionsDialog.this.splitOptions.getBinBoundaries().get(i));
				sb.append("\n");
			}
			sb.delete(sb.length()-3, sb.length());
			this.boundariesArea.setText(sb.toString());
		}
	}
	
	/**
	 * Component that allows a user to define how to split on a enum type variable
	 */
	private class SplitEnumPanel extends JPanel {
		private JTextArea textArea;
		
		public SplitEnumPanel() {
			super(new BorderLayout());
			this.textArea = new JTextArea();
			this.textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(this.textArea);
			super.add(scrollPane, BorderLayout.CENTER);
			this.setVisible(true);

			this.update();
		}
		
		/**
		 * update the view
		 */
		private void update() {
			this.textArea.setText("");
			if(SplitOptionsDialog.this.model.isColumnIndexValid(SplitOptionsDialog.this.splitOptions.getColumnSplitIndex())) {
				ColumnType cType = SplitOptionsDialog.this.model.getColumnTypes().get(SplitOptionsDialog.this.splitOptions.getColumnSplitIndex());
				AllowedTypes type = cType.getType();
				if(type.equals(AllowedTypes.ENUM)) {
					StringBuilder sb = new StringBuilder();
					for(String s : cType.getEnumOptions()) {
						sb.append(s);
						sb.append("\n");
					}
					sb.substring(0, sb.length()-1);
					this.textArea.setText(sb.toString());
				}
			}
		}
	}
	
	/**
	 * Component that displays the way data is split by a string type variable
	 */
	private class SplitStringPanel extends JPanel {
		private JTextArea textArea;
		
		public SplitStringPanel() {
			super(new BorderLayout());
			this.textArea = new JTextArea();
			this.textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(this.textArea);
			super.add(scrollPane, BorderLayout.CENTER);
			this.setVisible(true);
			this.update();
		}
		
		/**
		 * Update the view
		 */
		private void update() {
			this.textArea.setText("");
			if(SplitOptionsDialog.this.model.isColumnIndexValid(SplitOptionsDialog.this.splitOptions.getColumnSplitIndex())) {
				ColumnType cType = SplitOptionsDialog.this.model.getColumnTypes().get(SplitOptionsDialog.this.splitOptions.getColumnSplitIndex());
				AllowedTypes type = cType.getType();
				if(type.equals(AllowedTypes.STRING)) {
					StringBuilder sb = new StringBuilder();
					for(String s : SplitOptionsDialog.this.model.getStringOptions(SplitOptionsDialog.this.splitOptions.getColumnSplitIndex())) {
						sb.append(s);
						sb.append("\n");
					}
					sb.substring(0, sb.length()-1);
					this.textArea.setText(sb.toString());
				}
			}
		}
	}

}
