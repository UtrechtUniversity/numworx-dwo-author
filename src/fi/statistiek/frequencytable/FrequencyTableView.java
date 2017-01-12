package fi.statistiek.frequencytable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import fi.statistiek.ColorGenerator;
import fi.statistiek.ColorPreviewer;
import fi.statistiek.Statistiek;
import fi.statistiek.histogram.HistogramModel.FrequencyTuple;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * MVC View for statistiekView FrequencyTable
 * 
 * @author Manu Drijvers
 * 
 */
public class FrequencyTableView extends JPanel implements Observer
{
	private FrequencyTableModel model;
	private FrequencyTableController controller;
	private FrequencyTableUserOptionsPanel userOptionsPanel;
	private JButton dialogButton;

	private JPanel mainPanel;
	private JScrollPane scrollPane;
	private FrequencyTablePanel[] splitClassPanels;
	/**
	 * If the frequency table has a split, splitClassLabelPanel contains a
	 * panel with the split label (e.g., "Geslacht: m"; actually, the last panel
	 * is stored). It is used to determine the height of the panel when determining 
	 * the row clicked 
	 */
	private JPanel splitClassLabelPanel;
	private int numberOfSplitClasses = 1;

	/**
	 * The number of rows in the frequency table, including
	 * header and total row.
	 */
	private int frequencyTableRows;
	private int mainPanelColumns;
	/**
	 * Array of maximum column widths, to be used to paint 
	 * row selection.
	 */
	private int[] maxColumnWidth;
	private Color[][] rowColors;
	/**
	 * Maximum number of rows for performance reasons.
	 */
	private int maxRows = 100;
	public static final int MAIN_GRID_HGAP = 8;
	public static final int GRID_TOPGAP = 5;
	public static final int GRID_BOTTOMGAP = 5;
	public static final int GRID_LEFTGAP = 5;
	public static final int GRID_RIGHTGAP = 5;
	public static final Color SELECTED_COLOR = ColorGenerator.SELECTION_COLOR;
	private int rowHeight;
	private int tableWidth;
	private int tableHeight;
	private boolean isTableHeightSet;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            MVC Model
	 * @param controller
	 *            MVC Controller
	 */
	public FrequencyTableView(FrequencyTableModel model,
		FrequencyTableController controller)
	{
		super(new BorderLayout());
		super.setBackground(Color.WHITE);

		this.model = model;
		this.model.addObserver(this);
		this.controller = controller;

		this.mainPanel = new JPanel();
		
		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewportView(mainPanel);
		this.scrollPane.getVerticalScrollBar().setUnitIncrement(Statistiek.scrollSpeedUnit);
		
		super.add(this.scrollPane, BorderLayout.CENTER);

		this.mainPanel.setBackground(Color.WHITE);
		this.mainPanel.addMouseListener(new RowClickListener());

		this.userOptionsPanel = new FrequencyTableUserOptionsPanel(this, controller, model);

		dialogButton = userOptionsPanel.getDialogButton();

		super.add(dialogButton, BorderLayout.SOUTH);

		this.update(null, null);
	}

	private class FrequencyTablePanel extends JPanel
	{
		private int splitClass;
		
		public FrequencyTablePanel(LayoutManager l, int splitClass)
		{
			super(l);
			super.setBackground(Color.WHITE);
			this.splitClass = splitClass;
		}
		
		@Override
		public Dimension getPreferredSize()
		{
//			System.out.println("FrequencyTableView.FrequencyTablePanel.getPreferredSize(): h = "
//				+ super.getPreferredSize().height + ", w = "
//				+ super.getPreferredSize().width);
			
			return super.getPreferredSize();
		}
		
		@Override
		public void setPreferredSize(Dimension d)
		{
//			System.out.println("FrequencyTableView.FrequencyTablePanel.setPreferredSize(): d = "
//				+ d);
			
			super.setPreferredSize(d);
		}		
		
		/**
		 * Paint a white background and the selected rows in the frequency table panel.
		 */
		public void paintComponent(Graphics g)
		{
			if (!FrequencyTableView.this.model.columnIndexValid())
			{
				// variable is not valid, so there is nothing to paint
				return;
			}
			else if (FrequencyTableView.this.frequencyTableRows > FrequencyTableView.this.maxRows)
			{
				return;
			}

			g.setColor(Color.BLACK);

			g.setColor(Color.WHITE);
//			g.setColor(Color.PINK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			// test syl
//			g.fillRect(0, 0, this.getWidth(), this.getPreferredSize().height);

			// draw selection color of rows
			if (FrequencyTableView.this.rowColors != null)
			{
				for (int i = 0; i < FrequencyTableView.this.rowColors.length; i++)
				{
					g.setColor(FrequencyTableView.this.rowColors[i][this.splitClass]);
					g.fillRect(
						0,
						(i + 1) * FrequencyTableView.this.rowHeight, 
						FrequencyTableView.this.tableWidth, FrequencyTableView.this.rowHeight);
				}
			}
		}
	}
	
	public double getBinWidth()
	{
		return this.userOptionsPanel.getBinWidth();
	}

	public void setBinWidth(double d)
	{
		this.userOptionsPanel.setBinWidth(d);
	}

	public void setBinWidth()
	{
		this.userOptionsPanel.setBinWidth();
	}

	public double getMinBoundary()
	{
		return this.userOptionsPanel.getMinBoundary();
	}

	/**
	 * Set min boundary with value d
	 * @param d
	 */
	public void setMinBoundary(double d)
	{
		this.userOptionsPanel.setMinBoundary(d);
	}

//	public JTextField getNoBinsField()
//	{
//		return this.noBinsField;
//	}
//
//	public String getNoBinsFieldText()
//	{
//		return this.noBinsField.getText();
//	}
//
	public boolean isShowPercBoxSelected()
	{
		return this.userOptionsPanel.isShowPercBoxSelected();
	}

	public boolean isShowCumulativeBoxSelected()
	{
		return this.userOptionsPanel.isShowCumulativeBoxSelected();
	}

	public int varBoxSelectedIndex()
	{
		return this.userOptionsPanel.getVarXBoxSelectedIndex();
	}

	/**
	 * @return The sum of all elements on even indices
	 */
	private int arrayEvenSum(int[] array)
	{
		int sum = 0;
		for (int i = 0; i < array.length; i += 2)
		{
			sum += array[i];
		}
		return sum;
	}

	private int tupleArraySum(FrequencyTuple[] array)
	{
		int sum = 0;
		for (FrequencyTuple ft : array)
		{
			sum += ft.frequency;
		}
		return sum;
	}

	public void update(Observable arg0, Object arg1)
	{
		try
		{
			this.dialogButton.setVisible(this.model.getStatTableModel()
				.isViewsEditable());
			
			this.mainPanel.setVisible(false);
			this.mainPanel.removeAll(); // this can be very slow if there are many children
			this.mainPanel.setVisible(true);
			
			// initialize table height
			this.tableHeight = 0;
			this.isTableHeightSet = false;
			
			if (this.model.columnIndexValid())
			{
				ColumnType cType = this.model.getStatTableModel().getColumnTypes()
					.get(this.model.getColumnIndex());
				AllowedTypes type = cType.getType();
	
				this.mainPanelColumns = 2;
				if (this.model.isShowCumulative())
				{
					this.mainPanelColumns++;
				}
				if (this.model.isShowPercentage())
				{
					this.mainPanelColumns++;
					if (this.model.isShowCumulative())
					{
						this.mainPanelColumns++;
					}
				}
	
				FrequencyTuple[][] frequencyTuple = null;
				int[] frequencies = null;
				numberOfSplitClasses = this.model.getStatTableModel().numberOfSplitVarClasses(
					this.model.getSplitOptions());
				this.splitClassPanels = new FrequencyTablePanel[numberOfSplitClasses];
				
				if (type.isNumber())
				{
					this.frequencyTableRows = this.model.getBinBoundaries().size() + 1;
					if (this.frequencyTableRows < 0)
					{
						this.frequencyTableRows = 0;
					}
					
					if (this.model.numberClassFrequency() != null)
						this.rowColors = new Color[this.model.numberClassFrequency()[0].length / 2][numberOfSplitClasses];
				}
				else
				{
					frequencyTuple = this.model.enumClassFrequency();
					this.frequencyTableRows = 0;
					if (frequencyTuple != null)
						this.frequencyTableRows = frequencyTuple[0].length + 2; // add 2 for header and total row
					
					this.rowColors = new Color[this.frequencyTableRows - 2][numberOfSplitClasses];
				}
				
				//System.out.println("FrequencyTableView.update(): rows = " + this.mainPanelRows);
				if (this.frequencyTableRows > this.maxRows)
				{
					String s = Statistiek.rb.getString("messageNrRowsMoreThan") + this.maxRows
						+ ". " + Statistiek.rb.getString("messageChooseOtherVar");
					JLabel message = new JLabel(s);
					message.setFont(Statistiek.font);
					this.mainPanel.add(message);
					userOptionsPanel.update();
					this.mainPanel.revalidate();
					this.repaint();
					return;
				}
	
				this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
				
				Dimension dimension;
				
				int h = this.scrollPane.getViewport().getHeight();
				int w = this.scrollPane.getViewport().getWidth();
				if (h <= 0) // for some reason scrollPane is 0 sometimes...
					h = 340; // hardcoded...
				if (w <= 0)
					w = 653;

				for (int i = 0; i < numberOfSplitClasses; i++)
				{
					splitClassPanels[i] = new FrequencyTablePanel(new GridBagLayout(), i);
					
					makeHeaderRow(splitClassPanels[i]);
					
					// make middle part
					int sum = 0;
					if (type.isNumber())
					{
						if (this.model.numberClassFrequency() != null)
						{
							frequencies = this.model.numberClassFrequency()[i];
							sum = this.arrayEvenSum(frequencies);
						}
					}
					else
					{
						sum = this.tupleArraySum(frequencyTuple[i]);
	
					}
	
					if (frequencyTuple == null)
					{
						if (frequencies != null)
							makeMiddlePart(splitClassPanels[i], type, frequencies, null, sum);
					}
					else
						makeMiddlePart(splitClassPanels[i], type, frequencies, frequencyTuple[i], sum);
					
					makeTotalRow(splitClassPanels[i], sum);
	
	    			dimension = new Dimension(w, this.tableHeight);

					splitClassPanels[i].setPreferredSize(dimension);
//					System.out.println("FrequencyTableView.update(): this.tableHeight = " + this.tableHeight);
					this.isTableHeightSet  = true;
					
					// set colors for background
					if (type.isNumber())
					{
						if (frequencies != null)
						{
							for (int j = 0; j < frequencies.length; j += 2)
							{
								double d = (frequencies[j] == 0 ? 0.0
									: (double) frequencies[j + 1] / (double) frequencies[j]);
		//						System.out.println("FrequencyTableView.update(): " + frequencies[i + 1] + " "
		//							+ frequencies[i] + " " + d);
								this.rowColors[j / 2][i] = ColorPreviewer.mixColors(
									Color.WHITE, SELECTED_COLOR, d);
							}
						}
					}
					else
					{
						if (frequencyTuple != null)
						{
							for (int k = 0; k < frequencyTuple[i].length; k++)
							{
								FrequencyTuple ft = frequencyTuple[i][k];
								double d = (ft.frequency == 0 ? 0
									: (double) ft.selectionFrequency
										/ (double) ft.frequency);
		//						System.out.println("FrequencyTableView.update(): " + d);
								this.rowColors[k][i] = ColorPreviewer.mixColors(Color.WHITE,
									SELECTED_COLOR, d);
							}
						}
					}
	
					this.mainPanel.add(splitClassPanels[i]);
					
					if (numberOfSplitClasses > 1)
					{
						// add panel with splitclass label
						JPanel labelPanel = new JPanel();
						labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
						labelPanel.setBackground(Color.WHITE);
						// test syl
//						labelPanel.setBackground(Color.CYAN);
						String splitVar = this.model.getStatTableModel()
							.getColumnName(this.model.getSplitOptions().getColumnSplitIndex());
						JLabel label = new JLabel(splitVar + ": " + this.model.getSplitOptions()
							.getSplitClassLabel(i, this.model.getStatTableModel()));
						label.setFont(Statistiek.font);
						label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
						labelPanel.add(label);
						this.mainPanel.add(labelPanel);
						
						// store a labelPanel in order to be able to access its height when painting row selection 
						this.splitClassLabelPanel = labelPanel;
					}
				}
	
				this.mainPanel.setBackground(Color.WHITE);
				
			} //columnIndexValid()
			
			userOptionsPanel.update();
	
			// set mainPanel size op het eind zodat splitClassPanels bestaan
			this.setMainPanelSize();
			this.scrollPane.setViewportView(mainPanel);
	
			this.mainPanel.revalidate();

			this.repaint();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * In order to determine the maximum width of column i, set the max width to the 
	 * width of label if label width is larger than the value in maxColumnWidth.
	 *  
	 * @param i
	 * @param label
	 */
	private void updateMaxColumnWidth(int i, JLabel label)
	{
		if (label.getPreferredSize().width > this.maxColumnWidth[i])
			this.maxColumnWidth[i] = label.getPreferredSize().width;
	}

	/**
	 * Calculate the width of the frequency table as visible on the screen,
	 * ignoring dummy labels.
	 */
	private void setTableWidth()
	{
		this.tableWidth = 0;
		
		if (this.maxColumnWidth != null)
		{
			for (int i = 0; i < this.maxColumnWidth.length; i++)
			{
				this.tableWidth += this.maxColumnWidth[i];
			}
		}

		//System.out.println("FrequencyTableView.setTableWidth(): width = " + this.TABLE_WIDTH);
	}

	private void makeTotalRow(JPanel panel, int sum)
	{
		//System.out.println("FrequencyTableView.makeTotalRow(sum=" + sum + ")");
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;		
		Border paddingBorder = BorderFactory.createEmptyBorder(this.GRID_TOPGAP, this.GRID_LEFTGAP,
			this.GRID_BOTTOMGAP, this.GRID_RIGHTGAP);
		// borders on all sides except the top and left side
		Border matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK);

		JLabel totalLabel = new JLabel(Statistiek.rb.getString("totalLabel"));
		totalLabel.setFont(Statistiek.font);
		totalLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0;
		c.gridy = this.frequencyTableRows - 1;
		panel.add(totalLabel, c);
		// administer the maximum column width
		this.updateMaxColumnWidth(0, totalLabel);
		// update table height with the height of the first label in the last row
		this.updateTableHeight(totalLabel);
		
		JLabel freqLabel = new JLabel(Integer.toString(sum),
			SwingConstants.TRAILING);
		freqLabel.setFont(Statistiek.font);
		freqLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 1;
		c.gridy = this.frequencyTableRows - 1;
		panel.add(freqLabel, c);
		this.updateMaxColumnWidth(1, freqLabel);
    	
		int percentage = 0;
    	if (this.model.isShowPercentage())
    	{
    		String percString;
    		if (sum == 0) // if there are no cases in the table, then total percentage is 0%
    			percString = "0%";
    		else
    			percString = "100%";
    		JLabel percLabel = new JLabel(percString, SwingConstants.TRAILING);
    		percLabel.setFont(Statistiek.font);
    		percLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
    		c.gridx = 2;
    		c.gridy = this.frequencyTableRows - 1;
    		panel.add(percLabel, c);
    		this.updateMaxColumnWidth(2, percLabel);
    		percentage++;
    	}
    	
    	if (this.model.isShowCumulative())
    	{
    		JLabel cumulLabel = new JLabel(Integer.toString(sum),
    			SwingConstants.TRAILING);
    		cumulLabel.setFont(Statistiek.font);
    		cumulLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
    		c.gridx = 2 + percentage;
    		c.gridy = this.frequencyTableRows - 1;
    		panel.add(cumulLabel, c);
    		this.updateMaxColumnWidth(2 + percentage, cumulLabel);
    	}
    	
    	if (this.model.isShowPercentage() && this.model.isShowCumulative())
    	{
    		String percString;
    		if (sum == 0) // if there are no cases in the table, then total percentage is 0%
    			percString = "0%";
    		else
    			percString = "100%";
    		JLabel cumulPercLabel = new JLabel(percString, SwingConstants.TRAILING);
    		cumulPercLabel.setFont(Statistiek.font);
    		cumulPercLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
    		c.gridx = 4;
    		c.gridy = this.frequencyTableRows - 1;
    		panel.add(cumulPercLabel, c);
    		this.updateMaxColumnWidth(4, cumulPercLabel);
    	}
    	
		// add extra cell under last row to fill the space 
		// and make the table start at the northwest corner
		c.gridx = 0;
		c.gridy = this.frequencyTableRows;
		c.weighty = 1;
		c.fill = GridBagConstraints.REMAINDER;
		JLabel dummy0Rest = new JLabel(" ");
		dummy0Rest.setFont(Statistiek.font);
		dummy0Rest.setPreferredSize(new Dimension(0, 0));
		panel.add(dummy0Rest, c);
	}

	/**
	 * Make the header row on panel.
	 * @param panel
	 */
	private void makeHeaderRow(JPanel panel)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;		
		c.weightx = 0;

		Border paddingBorder = BorderFactory.createEmptyBorder(this.GRID_TOPGAP, this.GRID_LEFTGAP,
			this.GRID_BOTTOMGAP, this.GRID_RIGHTGAP);
		// borders on all sides
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		// borders on all sides except the left side
		Border matteBorder = BorderFactory.createMatteBorder(1, 0, 1, 1, Color.BLACK);

		this.maxColumnWidth = new int[this.getNumberOfColumns()];

		JLabel variableName = new JLabel(this.model.getStatTableModel()
			.getColumnName(this.model.getColumnIndex()));
		variableName.setFont(Statistiek.font);
		// test syl: toon gridlines m.b.v. lineborders
//		variableName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		variableName.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0;
		c.gridy = 0;
		panel.add(variableName, c);
		// administer the maximum column width
		this.updateMaxColumnWidth(0, variableName);
		// update table height with the height of the first label in the first row
		this.updateTableHeight(variableName);
		
		JLabel freq = new JLabel("Freq.", SwingConstants.TRAILING);
		freq.setFont(Statistiek.font);
		freq.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 1;
		c.gridy = 0;
		panel.add(freq, c);
		this.updateMaxColumnWidth(1, freq);

		int xDummy = 2;
		int percentage = 0;
		if (this.model.isShowPercentage())
		{
			JLabel freqPerc = new JLabel("Freq.%", SwingConstants.TRAILING);
			freqPerc.setFont(Statistiek.font);
			freqPerc.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
			c.gridx = 2;
			c.gridy = 0;
			panel.add(freqPerc, c);
			this.updateMaxColumnWidth(2, freqPerc);
			// increase the x grid coordinate of the dummy variable at the end of the row
			xDummy++;
			percentage++;
		}
		if (this.model.isShowCumulative())
		{
			JLabel cumul = new JLabel("Cumul.", SwingConstants.TRAILING);
			cumul.setFont(Statistiek.font);
			cumul.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
			c.gridx = 2 + percentage;
			c.gridy = 0;
			panel.add(cumul, c);
			this.updateMaxColumnWidth(2 + percentage, cumul);
			xDummy++;
    	}
    	
    	if (this.model.isShowPercentage() && this.model.isShowCumulative())
    	{
			JLabel cumulPerc = new JLabel("Cumul.%",
				SwingConstants.TRAILING);
			cumulPerc.setFont(Statistiek.font);
			cumulPerc.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
			c.gridx = 4;
			c.gridy = 0;
			panel.add(cumulPerc, c);
			this.updateMaxColumnWidth(4, cumulPerc);
			xDummy++;
		}
    	
    	// add dummy variable at the end of the row
		c.fill = GridBagConstraints.REMAINDER;
		c.gridx = xDummy;
		c.gridy = 0;
		c.weightx = 1;
		JLabel dummyRest0 = new JLabel(" ");
		dummyRest0.setFont(Statistiek.font);
		dummyRest0.setPreferredSize(new Dimension(0, 0));
		panel.add(dummyRest0, c);
	}

	/**
	 * Update the table height with the height of the given label.
	 * @param label
	 */
	private void updateTableHeight(JLabel label)
	{
		if (!this.isTableHeightSet)
			this.tableHeight += label.getPreferredSize().height;
	}

	/**
	 * Get the number of columns in the frequency table.
	 * @return
	 */
	private int getNumberOfColumns()
	{
		int n = 2;
		
		if (this.model.isShowPercentage())
			n++;
		if (this.model.isShowCumulative())
			n++;
		if (this.model.isShowPercentage() && this.model.isShowCumulative())
			n = 5;
		
		return n;
	}

	/**
	 * Make the middle part of the frequency table view on panel.
	 * @param panel
	 * @param type
	 * @param frequencies
	 * @param frequencyTuple
	 * @param sum
	 */
	private void makeMiddlePart(JPanel panel, AllowedTypes type, int[] frequencies, 
		FrequencyTuple[] frequencyTuple, double sum)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;		
		Border paddingBorder = BorderFactory.createEmptyBorder(this.GRID_TOPGAP, this.GRID_LEFTGAP,
			this.GRID_BOTTOMGAP, this.GRID_RIGHTGAP);
		// borders on all sides except the top and left side
		Border matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK);

		int cumulative = 0;
		int freq;
		
		//System.out.println("FrequencyTableView.makeMiddlePart(): mainPanelRows = " + this.mainPanelRows);
		
		for (int bin = 0; bin < this.frequencyTableRows - 2; bin++)
		{
			// first column
			if (type.isNumber())
			{
				freq = frequencies[bin * 2];
				
				
				String text;
				if (type.equals(AllowedTypes.INTEGER)
					&& (this.model.getBinBoundaries().get(1).doubleValue() - this.model.getBinBoundaries().get(0).doubleValue()) == 1)
				{
					// bin width is 1
					text = Statistiek.getStringValue(this.model.getBinBoundaries().get(bin).doubleValue()); // getStringValue will give an integer value without decimals
				}
				else
				{
					text = Statistiek.getStringValue(this.model.getBinBoundaries().get(bin).doubleValue())
						+ " -< "
						+ Statistiek.getStringValue(this.model.getBinBoundaries().get(bin + 1).doubleValue());
				}
				JLabel label = new JLabel(text);

				label.setFont(Statistiek.font);
				label.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
				c.gridx = 0;
				c.gridy = bin + 1;
				panel.add(label, c);
				// administer the maximum column width
				this.updateMaxColumnWidth(0, label);
				// update table height with the height of the first label in row bin + 1
				this.updateTableHeight(label);
			}
			else
			{
				freq = frequencyTuple[bin].frequency;
				JLabel label = new JLabel(frequencyTuple[bin].label);
				label.setFont(Statistiek.font);
				label.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
				c.gridx = 0;
				c.gridy = bin + 1;
				panel.add(label, c);
				// administer the maximum column width
				this.updateMaxColumnWidth(0, label);
				// update table height with the height of the first label in row bin + 1
				this.updateTableHeight(label);
			}
			
			// second column
			JLabel freqLabel = new JLabel(Integer.toString(freq),
				SwingConstants.TRAILING);
			freqLabel.setFont(Statistiek.font);
			freqLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
			c.gridx = 1;
			c.gridy = bin + 1;
			panel.add(freqLabel, c);
			this.updateMaxColumnWidth(1, freqLabel);

			// set row height, to be used for row click calculations
			this.rowHeight = freqLabel.getPreferredSize().height;
//			System.out.println("FrequencyTableView.makeMiddelPart(): freqLabel.getPreferredSize() = " 
//				+ freqLabel.getPreferredSize());

			int percentage = 0;
			// third column
			if (this.model.isShowPercentage())
			{
				double d = freq * 100 / (double) sum;
				d = Math.round(d * 100) / (double) 100;
				// round to one decimal
				int decimals = 1;
				d = Statistiek.round(d, decimals);
				JLabel percLabel = new JLabel(Statistiek.getStringValue(d) + "%",
					SwingConstants.TRAILING);
				percLabel.setFont(Statistiek.font);
				percLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
				c.gridx = 2;
				c.gridy = bin + 1;
				panel.add(percLabel, c);
				this.updateMaxColumnWidth(2, percLabel);
				percentage++;
			}
			
			// fourth column
			if (this.model.isShowCumulative())
			{
				cumulative += freq;
				JLabel cumulLabel = new JLabel(Integer.toString(cumulative),
					SwingConstants.TRAILING);
				cumulLabel.setFont(Statistiek.font);
				cumulLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
				c.gridx = 2 + percentage;
				c.gridy = bin + 1;
				panel.add(cumulLabel, c);
				this.updateMaxColumnWidth(2 + percentage, cumulLabel);
	    	}
	    	
			// fifth column
	    	if (this.model.isShowPercentage() && this.model.isShowCumulative())
	    	{
				double d = (double) cumulative * 100 / (double) sum;
				d = Math.round(d * 100) / (double) 100;
				// round to one decimal
				int decimals = 1;
				d = Statistiek.round(d, decimals);
				JLabel cumulPercLabel = new JLabel(Statistiek.getStringValue(d) + "%",
					SwingConstants.TRAILING);
				cumulPercLabel.setFont(Statistiek.font);
				cumulPercLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
				c.gridx = 4;
				c.gridy = bin + 1;
				panel.add(cumulPercLabel, c);
				this.updateMaxColumnWidth(4, cumulPercLabel);
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// clear panel
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		if (!this.model.columnIndexValid())
		{
			// variable is not valid, so there is nothing to paint
			return;
		}
	}
	
	private void setMainPanelSize()
	{
		this.setTableWidth();
		
		Dimension dimension;
		
		if (this.model.getSplitOptions().getColumnSplitIndex() == -1)
		{
    		if (this.tableHeight > this.scrollPane.getHeight())
    		{
    			dimension = new Dimension(this.scrollPane.getViewport()
    				.getWidth(), this.tableHeight);
    		}
    		else
    		{
    			dimension = new Dimension(
    				this.scrollPane.getViewport().getWidth(), 
    				this.scrollPane.getViewport().getHeight());
    		}
		}
		else // er is een split
		{
			int correctieBreedte = 20;
			int correctieHoogte = 20;

			int totalHeight = this.tableHeight + this.splitClassLabelPanel.getPreferredSize().height; 
			dimension = new Dimension(this.scrollPane.getWidth() - correctieBreedte, 
				numberOfSplitClasses * totalHeight);
		}
		
		// Vreemde situatie: scrollPane = 0x0 als we na een paginawissel direct een frequentietabel tonen
		if (scrollPane.getHeight() == 0 || scrollPane.getWidth() == 0)
		{
			// even hardcoded op iets groots...
			int h;
			int h_singlePanel = 340;
			int w = 651;
			if (splitClassPanels != null)
			{
				// test syl: de hoogte hardcoded zetten, want splitClassPanels hebben hoogte 0...
				h = h_singlePanel * splitClassPanels.length;
//				System.out.println("FrequencyTableView.setMainPanelSize(): --------- RESET! h = " 
//					+ h + ", splitClassPanels.length = " + splitClassPanels.length
//					+ ", splitClassPanels[0].getHeight() = " + splitClassPanels[0].getHeight());
			}
			else
			{
				h = h_singlePanel;
//				System.out.println("FrequencyTableView.setMainPanelSize(): --------- RESET! h = " 
//					+ h);
			}
			dimension.setSize(w, h);
		}

//		System.out.println("FrequencyTableView.setMainPanelSize(): mainPanel.setPreferredSize(h = " 
//			+ dimension.height	+ ", w = " + dimension.width + ")");
//		System.out.println("FrequencyTableView.setMainPanelSize(): scrollPane.h = " 
//			+ scrollPane.getHeight()
//			+ ", scrollPane.w = " + scrollPane.getWidth());
		
		this.mainPanel.setPreferredSize(dimension);
	}
	
	// Override setBound
	public void setBounds(int x, int y, int w, int h)
	{
//		System.out.println("FrequencyTableView.setBounds(x=" + x + ", y=" + y 
//			+ ", w=" + w + ", h=" + h + ")");

		super.setBounds(x, y, w, h);

		this.setMainPanelSize();
	}

	public void setModel(FrequencyTableModel model)
	{
		this.model = model;
		this.model.addObserver(this);
		userOptionsPanel.setModel(model);
		this.update(null, null);
	}

	public int getSplitVarBoxSelectedIndex()
	{
		return userOptionsPanel.getSplitVarBoxSelectedIndex();
	}

	public double getSplitMinBoundary()
	{
		return userOptionsPanel.getSplitMinBoundary();
	}

	/**
	 * Set min boundary with value min
	 * @param min
	 */
	public void setSplitMinBoundary(double min)
	{
		this.userOptionsPanel.setSplitMinBoundary(min);
	}

	public double getSplitBinWidth()
	{
		return userOptionsPanel.getSplitBinWidth();
	}

	public void setSplitBinWidth(double d)
	{
		this.userOptionsPanel.setSplitBinWidth(d);
	}

	public void setSplitBinWidth()
	{
		this.userOptionsPanel.setSplitBinWidth();
	}

	public int getSplitBinsBoxSelectedInt()
	{
		return userOptionsPanel.getSplitBinsBoxSelectedInt();
	}


	
	/*
	 * RowClickListener is used on mainPanel which may contain several splitClassPanels
	 * in case of a split.
	 */
	private class RowClickListener implements MouseListener
	{

		public void mouseClicked(MouseEvent arg0)
		{
			if (FrequencyTableView.this.splitClassPanels[0] != null)
			{
				int x = arg0.getPoint().x;
				int y = arg0.getPoint().y;
				int y_transformed = y;
				int heightSplitClassPanel = FrequencyTableView.this.splitClassPanels[0].getHeight();
				int heightLabelPanel = 0;
				if (FrequencyTableView.this.splitClassLabelPanel != null)
					heightLabelPanel = FrequencyTableView.this.splitClassLabelPanel.getHeight();
				// total height of a split class frequency table
				int heightSplitFrequencyTable = heightSplitClassPanel + heightLabelPanel;
				// number of data rows in a frequency table
				int numberOfRows = FrequencyTableView.this.frequencyTableRows - 2;
				
				int count = 0;
				// transform y
				while (y_transformed > heightSplitFrequencyTable)
				{
					y_transformed = y_transformed - heightSplitFrequencyTable;
					count++;
	//				System.out.println("FrequencyTableView.RowClickListener.mouseClicked(): y_transformed = "
	//					+ y_transformed + ", count = " + count);
				}

				int clicked = (y_transformed / FrequencyTableView.this.rowHeight) - 1;
	
	//			System.out.println("FrequencyTableView.RowClickListener.mouseClicked(): y = "
	//				+ y + ", y_transformed = " + y_transformed + ", count = " + count
	//				+ ", heightSplitClassPanel = " + heightSplitClassPanel
	//				+ ", rowHeight = " + rowHeight
	//				+ ", roundRowHeight = " + roundRowHeight
	//				+ ", clicked = " + clicked);			
	
				if (clicked >= 0 && clicked < numberOfRows 
					&& x <= FrequencyTableView.this.tableWidth)
				{
					int clicked_transformed = clicked + (count * numberOfRows);
					this.rowClicked(clicked_transformed);
				}
			} // splitClassPanels != null
		}

		public void mouseEntered(MouseEvent arg0)
		{
			// TODO Auto-generated method stub

		}

		public void mouseExited(MouseEvent arg0)
		{
			// TODO Auto-generated method stub

		}

		public void mousePressed(MouseEvent arg0)
		{
			// TODO Auto-generated method stub

		}

		public void mouseReleased(MouseEvent arg0)
		{
			// TODO Auto-generated method stub

		}

		/*
		 * The user clicked on row rowNumber. When there is a split, rowNumber has a maximum
		 * of numberOfBins * numberOfSplitClasses.
		 */
		private void rowClicked(int rowNumber)
		{
			if (!FrequencyTableView.this.model.columnIndexValid())
			{
				return;
			}

			int bins = FrequencyTableView.this.model.getStatTableModel()
				.numberOfBins(FrequencyTableView.this.model.getColumnIndex(),
					FrequencyTableView.this.model.getBinBoundaries());
			int row = rowNumber % bins;
			int splitClass = rowNumber / bins;

//			System.out.println("FrequencyTableView.RowClickListener.rowClicked(): rowNumber = " 
//				+ rowNumber + ", row " + row + " in splitClass " + splitClass);

			ColumnType cType = FrequencyTableView.this.model.getStatTableModel()
				.getColumnTypes()
				.get(FrequencyTableView.this.model.getColumnIndex());
			AllowedTypes type = cType.getType();
			if (type.isNumber())
			{
				ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
					FrequencyTableView.this.model.getStatTableModel().getRowCount());
				for (int i = 0; i < FrequencyTableView.this.model
					.getStatTableModel().getRowCount(); i++)
				{
					Object o = FrequencyTableView.this.model.getStatTableModel()
						.getValueAt(i,
							FrequencyTableView.this.model.getColumnIndex());

					selectionList.add(!o.equals(ColumnType.WILDCARD)
						&& FrequencyTableView.this.model.binOfNumber(Double
							.parseDouble((String) o)) == row
						&& FrequencyTableView.this.model.getStatTableModel()
							.classifyObject(i,
								FrequencyTableView.this.model.getSplitOptions()) == splitClass);

				}

				FrequencyTableView.this.model.getStatTableModel().setSelectionList(
					selectionList);
			}
			else
			{
				String clicked;
				if (type.equals(AllowedTypes.ENUM))
				{
					clicked = cType.getEnumOptions()[row];
					int wildcardIndex = Arrays.asList(cType.getEnumOptions())
						.indexOf(ColumnType.WILDCARD);
					if (wildcardIndex <= 0 && wildcardIndex < row)
					{
						clicked = cType.getEnumOptions()[row + 1];
					}
				}
				else
				{
					// stringColumnOptions staan niet in alfabetische volgorde; neem enumClassFrequency
					FrequencyTuple[] freqTuple = FrequencyTableView.this.model.enumClassFrequency()[splitClass];
					clicked = freqTuple[row].label;
//					System.out.println("FrequencyTableView.RowClickListener.rowClicked("
//						+ rowNumber + ") in splitClass " + splitClass);
				}

				ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
					FrequencyTableView.this.model.getStatTableModel().getRowCount());
				for (int i = 0; i < FrequencyTableView.this.model
					.getStatTableModel().getRowCount(); i++)
				{
					Object o = FrequencyTableView.this.model.getStatTableModel()
						.getValueAt(i,
							FrequencyTableView.this.model.getColumnIndex());

					selectionList
					.add(!o.equals(ColumnType.WILDCARD)
						&& ((String) o).equals(clicked)
						&& FrequencyTableView.this.model.getStatTableModel()
							.classifyObject(i,
								FrequencyTableView.this.model.getSplitOptions()) == splitClass);
				}
				FrequencyTableView.this.model.getStatTableModel().setSelectionList(
					selectionList);
			}
			
			FrequencyTableView.this.mainPanel.revalidate();
		}
	}

	public FrequencyTableUserOptionsPanel getUserOptionsPanel()
	{
		return userOptionsPanel;
	}

	public JTextField getMinBoundaryField()
	{
		return userOptionsPanel.getMinBoundaryField();
	}

	public JTextField getBinWidthField()
	{
		return userOptionsPanel.getBinWidthField();
	}

	public JTextField getSplitMinBoundaryField()
	{
		return userOptionsPanel.getSplitMinBoundaryField();
	}

	public JTextField getSplitBinWidthField()
	{
		return userOptionsPanel.getSplitBinWidthField();
	}
}
