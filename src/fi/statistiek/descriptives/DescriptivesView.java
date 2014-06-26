package fi.statistiek.descriptives;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import fi.statistiek.ColorGenerator;
import fi.statistiek.Statistiek;
import fi.statistiek.histogram.HistogramModel.FrequencyTuple;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * MVC View for statistiekView Descriptives
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class DescriptivesView extends JPanel implements Observer
{
	private DescriptivesModel model;
	private DescriptivesController controller;
	private DescriptivesUserOptionsPanel userOptionsPanel;
	private JButton dialogButton;

	private JPanel mainPanel;
	private JScrollPane scrollPane;

	public static final Color SELECTED_COLOR = ColorGenerator.SELECTION_COLOR;
	public static final int GRID_TOPGAP = 5;
	public static final int GRID_BOTTOMGAP = 5;
	public static final int GRID_LEFTGAP = 5;
	public static final int GRID_RIGHTGAP = 5;
	private int ROW_HEIGHT;
	private int TABLE_WIDTH;
	/**
	 * The number of decriptives in the table.
	 */
	private static int NUMBER_OF_DESCRIPTIVES = 7;
	/**
	 * Number of rows in a single descriptives table, 
	 * including header row
	 */
	private static int NUMBER_OF_TABLE_ROWS = 8;
	/**
	 * The type of the selected variable
	 */
	private AllowedTypes typeColumnIndex;
	/**
	 * The type of the split variable
	 */
	private AllowedTypes typeSplitVar;
	private int minRowHeight = 30;
	
	/**
	 * The frequencies in case of an enum rows variable
	 */
	private FrequencyTuple[][] frequencies_enum;
	/**
	 * The frequencies in case of a number rows variable
	 */
	private int[][] frequencies_number;
	/**
	 * The total frequencies per column bin (i.e., the split bins) 
	 */
	int[] aantalPerColumnBin;

	/**
	 * The labels with the data[descriptive fields][0..1 if selection][splitClass].
	 * Contains descriptive fields:
	 *   - number of cases
	 *   - minimum
	 *   - maximum
	 *   - mean
	 *   - standard deviation
	 *   - median
	 *   - modus
	 */
	private JLabel[][][] data;
	
	/**
	 * The number of cases[0..1 if selection][splitClass].
	 */
	private int[][] numberOfCases;
	
	/**
	 * The minimum[0..1 if selection][splitClass].
	 */
	private int[][] minimum;
	
	/**
	 * The maximum[0..1 if selection][splitClass].
	 */
	private int[][] maximum;
	
	/**
	 * The mean[0..1 if selection][splitClass].
	 */
	private int[][] mean;
	
	/**
	 * The standard deviation[0..1 if selection][splitClass].
	 */
	private int[][] standardDeviation;
	
	/**
	 * The median[0..1 if selection][splitClass].
	 */
	private int[][] median;
	
	/**
	 * The mode[0..1 if selection][splitClass].
	 */
	private String[][] mode;
	
	/**
	 * Bin labels for the split variable. Split is not yet implemented.
	 */
	private JLabel[] binLabelsSplitVar;
	
	private int[] maxColumnWidth;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            MVC Model
	 * @param controller
	 *            MVC Controller
	 */
	public DescriptivesView(DescriptivesModel model,
		DescriptivesController controller)
	{
		super(new BorderLayout());
		super.setBackground(Color.WHITE);

		this.model = model;
		this.model.addObserver(this);
		this.controller = controller;

		this.mainPanel = new JPanel(new GridBagLayout());
		
		this.scrollPane = new JScrollPane(this.mainPanel);
		this.scrollPane.getVerticalScrollBar().setUnitIncrement(Statistiek.scrollSpeedUnit);
		
		super.add(this.scrollPane, BorderLayout.CENTER);

		this.mainPanel.setBackground(Color.WHITE);

		this.userOptionsPanel = new DescriptivesUserOptionsPanel(this, controller, model);

		dialogButton = userOptionsPanel.getDialogButton();

		super.add(dialogButton, BorderLayout.SOUTH);

		this.update(null, null);
	}
	
	public int varColumnsBoxSelectedIndex()
	{
		return this.userOptionsPanel.getVarColumnsBoxSelectedIndex();
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
		System.out.println("DescriptivesView.update(): splitIndex = " + this.model.getSplitOptions().getColumnSplitIndex());
		
		this.dialogButton.setVisible(this.model.getTableModel()
			.isViewsEditable());
	
		// update the components in the useroptionspanel
		userOptionsPanel.update();

		this.mainPanel.removeAll();
		if (this.model.columnIndexValid())
		{
			ColumnType cTypeColumnIndex = this.model.getTableModel().getColumnTypes()
				.get(this.model.getColumnIndex());
			typeColumnIndex = cTypeColumnIndex.getType();
			
//			ColumnType cTypeSplit;
//			if (this.model.getColumnSplitIndex() > -1)
//			{
//				cTypeSplit = this.model.getTableModel().getColumnTypes()
//					.get(this.model.getColumnSplitIndex());
//				typeSplitVar = cTypeSplit.getType();
//
////				System.out.println("Descriptives.update(): typeColumns = " + typeColumns.toString());
//			}
//			else
//			{
//				// er is geen split
////				System.out.println("Descriptives.update(): this.model.getColumnSplitIndex() > -1");
//			}

			calculateData();

			Dimension dimension;
			
			int h = this.scrollPane.getViewport().getHeight();
			int w = this.scrollPane.getViewport().getWidth();
			if (h <= 0) // for some reason scrollPane is 0 sometimes...
				h = 340; // hardcoded...
			if (w <= 0)
				w = 653;
			
			makeDescritivesTable();				

			this.mainPanel.setBackground(Color.WHITE);
		} // columnIndexValid()
		
		this.setMainPanelSize();

		this.mainPanel.revalidate();

		this.repaint();
	}

	/**
	 * Calculates the data set used to create the descriptives table.
	 */
	private void calculateData()
	{
		int numberOfSplits = DescriptivesView.this.model.getTableModel()
			.splitVarClasses(DescriptivesView.this.model.getSplitOptions());

		if (this.hasSelection())
		{
			this.numberOfCases = new int[2][numberOfSplits];
			this.mode = new String[2][numberOfSplits];
		}
		else
		{
			this.numberOfCases = new int[1][numberOfSplits];
			this.mode = new String[1][numberOfSplits];
		}
		
		for (int i = 0; i < numberOfSplits; i++)
		{
			// number of cases, missing excluded (wildcard '*')
			this.numberOfCases[0][i] = this.model.getTableModel().getDataColumnMissingExcluded(
				this.varBoxSelectedIndex()).length;
			if (this.hasSelection())
				this.numberOfCases[1][i] = this.model.getTableModel().getDataColumnMissingExcludedOfSelection(
					this.varBoxSelectedIndex()).length;
		}
		
		
		for (int i = 0; i < numberOfSplits; i++)
		{
			String s;
			
			s = this.model.getTableModel().getColumnMode(this.varBoxSelectedIndex());
			if (this.isNumeric(s))
				this.mode[0][i] = this.getStringValue(Double.valueOf(s));
			else
				this.mode[0][i] = s;
			
			if (this.hasSelection())
			{
				s = this.model.getTableModel().getColumnModeOfSelection(this.varBoxSelectedIndex());
				if (this.isNumeric(s))
					this.mode[1][i] = this.getStringValue(Double.valueOf(s));
				else
					this.mode[1][i] = s;
			}
		}		
		
		// set data for both the main table and the selection table
		setDataLabels();
	}

	/**
	 * Checks whether string s contains a numerical value.
	 * 
	 * @param s
	 * @return True if string s contains a numerical value, else false.
	 */
	private boolean isNumeric(String s)
	{
		try
		{
			double d = Double.parseDouble(s);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	/**
	 * Set the data labels to be used in the descriptives table.
	 */
	private void setDataLabels()
	{
		int numberOfSplits = DescriptivesView.this.model.getTableModel()
			.splitVarClasses(DescriptivesView.this.model.getSplitOptions());

		// test syl: TODO implement numberOfSplits
		if (this.hasSelection())
			data = new JLabel[this.NUMBER_OF_DESCRIPTIVES][2][numberOfSplits];
		else
			data = new JLabel[this.NUMBER_OF_DESCRIPTIVES][1][numberOfSplits];
		
		this.setMainDataLabels();
		
		if (this.hasSelection())
			this.setSelectionDataLabels();
	}

	/**
	 * Set the data labels of the descriptives table of the selection.
	 */
	private void setSelectionDataLabels()
	{
		String minimumString;
		String maximumString;
		String meanString;
		String sdString;
		String medianString;

		if (this.typeColumnIndex.isNumber())
		{
			int columnIndex = this.varBoxSelectedIndex();
			int numberOfDecimals = determineMaxNumberOfDecimals(columnIndex) + 2;
			
			minimumString = this.getMinimumValueOfSelection(columnIndex);
			maximumString = this.getMaximumValueOfSelection(columnIndex);
			meanString = this.getStringValue(
				Statistiek.round(this.model.getTableModel().getColumnMeanOfSelection(columnIndex), numberOfDecimals));
			sdString = this.getStringValue(
				Statistiek.round(this.model.getTableModel().getColumnSDOfSelection(columnIndex), numberOfDecimals));
			medianString = getMedianValueOfSelection(columnIndex);
		} // type is number
		else
		{ // type is enum or string
			minimumString = Statistiek.rb.getString("notAvailable");
			maximumString = Statistiek.rb.getString("notAvailable");
			meanString = Statistiek.rb.getString("notAvailable");
			sdString = Statistiek.rb.getString("notAvailable");
			medianString = Statistiek.rb.getString("notAvailable");
		}

		// number of cases
		data [0][1][0] = new JLabel(String.valueOf(this.numberOfCases[1][0]));
		data [0][1][0].setBackground(this.SELECTED_COLOR);
		data [0][1][0].setOpaque(true);
		data [0][1][0].setFont(Statistiek.font);
		
		// minimum
		data [1][1][0] = new JLabel(minimumString);
		data [1][1][0].setBackground(this.SELECTED_COLOR);
		data [1][1][0].setOpaque(true);
		data [1][1][0].setFont(Statistiek.font);

		// maximum
		data [2][1][0] = new JLabel(maximumString);
		data [2][1][0].setBackground(this.SELECTED_COLOR);
		data [2][1][0].setOpaque(true);
		data [2][1][0].setFont(Statistiek.font);

		// mean
		data [3][1][0] = new JLabel(meanString);
		data [3][1][0].setBackground(this.SELECTED_COLOR);
		data [3][1][0].setOpaque(true);
		data [3][1][0].setFont(Statistiek.font);

		// standard deviation
		data [4][1][0] = new JLabel(sdString);
		data [4][1][0].setBackground(this.SELECTED_COLOR);
		data [4][1][0].setOpaque(true);
		data [4][1][0].setFont(Statistiek.font);

		// median
		data [5][1][0] = new JLabel(medianString);
		data [5][1][0].setBackground(this.SELECTED_COLOR);
		data [5][1][0].setOpaque(true);
		data [5][1][0].setFont(Statistiek.font);

		// modus
		data [6][1][0] = new JLabel(this.mode[1][0]);
		data [6][1][0].setBackground(this.SELECTED_COLOR);
		data [6][1][0].setOpaque(true);
		data [6][1][0].setFont(Statistiek.font);
	}

	/**
	 * Set the data labels of the main descriptives table.
	 */
	private void setMainDataLabels()
	{
		String minimumString;
		String maximumString;
		String meanString;
		String sdString;
		String medianString;

		if (this.typeColumnIndex.isNumber())
		{
			int columnIndex = this.varBoxSelectedIndex();
			int numberOfDecimals = determineMaxNumberOfDecimals(columnIndex) + 2;
			
			minimumString = this.getMinimumValue(columnIndex);
			maximumString = this.getMaximumValue(columnIndex);
			meanString = this.getStringValue(
				Statistiek.round(this.model.getTableModel().getColumnMean(columnIndex), numberOfDecimals));
			sdString = this.getStringValue(
				Statistiek.round(this.model.getTableModel().getColumnSD(columnIndex), numberOfDecimals));
			medianString = getMedianValue(columnIndex);
		} // type is number
		else
		{ // type is enum or string
			minimumString = Statistiek.rb.getString("notAvailable");
			maximumString = Statistiek.rb.getString("notAvailable");
			meanString = Statistiek.rb.getString("notAvailable");
			sdString = Statistiek.rb.getString("notAvailable");
			medianString = Statistiek.rb.getString("notAvailable");
		}

		// number of cases
		data [0][0][0] = new JLabel(String.valueOf(this.numberOfCases[0][0]));
		data [0][0][0].setFont(Statistiek.font);
		
		// minimum
		data [1][0][0] = new JLabel(minimumString);
		data [1][0][0].setFont(Statistiek.font);

		// maximum
		data [2][0][0] = new JLabel(maximumString);
		data [2][0][0].setFont(Statistiek.font);

		// mean
		data [3][0][0] = new JLabel(meanString);
		data [3][0][0].setFont(Statistiek.font);

		// standard deviation
		data [4][0][0] = new JLabel(sdString);
		data [4][0][0].setFont(Statistiek.font);

		// median
		data [5][0][0] = new JLabel(medianString);
		data [5][0][0].setFont(Statistiek.font);

		// modus
		data [6][0][0] = new JLabel(this.mode[0][0]);
		data [6][0][0].setFont(Statistiek.font);
	}

	private String getMedianValue(int columnIndex)
	{
		String medianValue;
		double medianDouble = this.model.getTableModel().getColumnMedian(columnIndex);
		
		medianValue = getStringValue(medianDouble);
		
		return medianValue;
	}

	private String getMedianValueOfSelection(int columnIndex)
	{
		String medianValue;
		double medianDouble = this.model.getTableModel().getColumnMedianOfSelection(columnIndex);
		
		medianValue = getStringValue(medianDouble);
		
		return medianValue;
	}

	private String getMaximumValue(int columnIndex)
	{
		String maximumValue;
		double maxDouble = this.model.getTableModel().getColumnMax(columnIndex);
		
		maximumValue = getStringValue(maxDouble);
		
		return maximumValue;
	}

	private String getMaximumValueOfSelection(int columnIndex)
	{
		String maximumValue;
		double maxDouble = this.model.getTableModel().getColumnMaxOfSelection(columnIndex);
		
		maximumValue = getStringValue(maxDouble);
		
		return maximumValue;
	}

	private String getMinimumValue(int columnIndex)
	{
		String minimumValue;
		double minDouble = this.model.getTableModel().getColumnMin(columnIndex);
		
		minimumValue = getStringValue(minDouble);
		
		return minimumValue;
	}

	private String getMinimumValueOfSelection(int columnIndex)
	{
		String minimumValue;
		double minDouble = this.model.getTableModel().getColumnMinOfSelection(columnIndex);
		
		minimumValue = getStringValue(minDouble);
		
		return minimumValue;
	}

	/**
	 * Get the string value of double. If the value is an integer value
	 * a string is returned without decimals.
	 * @param d The double value
	 * @return
	 */
	private String getStringValue(double d)
	{
		String s;
		if ((d == Math.floor(d)) && !Double.isInfinite(d))
			s = String.valueOf((int) d);
		else
			s = String.valueOf(d);
		
		return s;
	}

	/**
	 * Determine the number of decimals that is used in column columnIndex. 
	 * 
	 * @param columnIndex
	 * @return
	 */
	private int determineMaxNumberOfDecimals(int columnIndex)
	{
		String numberString;
		int max = 0;
		int integerPlaces, decimalPlaces;
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			numberString = String.valueOf(this.model.getTableModel().getValueAt(i, columnIndex));
			integerPlaces = numberString.indexOf('.');
			if (integerPlaces > -1)
			{
				decimalPlaces = numberString.length() - integerPlaces - 1;
				if (decimalPlaces > max)
				{
					max = decimalPlaces;
				}
			}
		}
		return max;
	}

	/**
	 * Calculates the percentage value of the ratio frequency/divisor. If divisor is 0, 0 is returned.
	 * If the percentage value is an integer value, no decimals are returned in waardeString.
	 * 
	 * @param frequency
	 * @param divisor
	 * @return waardeString The percentage value of the ratio frequency/divisor
	 */
	private String getWaardeString(int frequency, int divisor)
	{
		double waarde;
		String waardeString;
		DecimalFormat df = new DecimalFormat("0.#");
		
		if (divisor != 0)
		{
			waarde = ((double) frequency/divisor)*100;
//			if (waarde == Math.floor(waarde))
//				waardeString = String.valueOf((int) waarde);
//			else
				waardeString = df.format(waarde);
		}
		else
			waardeString = "0";
		
		return waardeString;
	}

	/**
	 * Make the two header rows on mainPanel.
	 * @param panel
	 */
	private void makeHeaderRows()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;		
		c.weightx = 0;
		
		this.ROW_HEIGHT = 0;
		this.TABLE_WIDTH = 0;

		Border paddingBorder = BorderFactory.createEmptyBorder(this.GRID_TOPGAP, this.GRID_LEFTGAP,
			this.GRID_BOTTOMGAP, this.GRID_RIGHTGAP);
		// borders on all sides
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		// borders on all sides except the left side
		Border matteBorder = BorderFactory.createMatteBorder(1, 0, 1, 1, Color.BLACK);
		String columnsNameString;
		
		int columnsVarIndex = this.model.getSplitOptions().getColumnSplitIndex();
		if (columnsVarIndex < 0)
			columnsNameString = "onbekende variabele";
		else 
			columnsNameString = this.model.getTableModel()
				.getColumnName(columnsVarIndex);
		
		// FIRST ROW with dummy labels, column variable name and "total" 
		JLabel dummy00 = new JLabel(" ");
		dummy00.setFont(Statistiek.font);
		dummy00.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(dummy00, c);
		JLabel dummy10 = new JLabel(" ");
		dummy10.setFont(Statistiek.font);
		dummy10.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(dummy10, c);
		
		JLabel variableColumnsName = new JLabel(columnsNameString);
		variableColumnsName.setFont(Statistiek.font);
		variableColumnsName.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		variableColumnsName.setHorizontalAlignment(SwingConstants.CENTER);
		c.gridx = 2;
		c.gridy = 0;
		mainPanel.add(variableColumnsName, c);
		
		
		JLabel dummyEnd1 = new JLabel(" ");
		dummyEnd1.setFont(Statistiek.font);
		dummyEnd1.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = binLabelsSplitVar.length + 2;
		c.gridy = 1;
		mainPanel.add(dummyEnd1, c);
	}

	/**
	 * In order to determine the maximum width of column i, set the max width to the 
	 * width of label if label width is larger than the value in maxColumnWidth.
	 *  
	 * @param i
	 * @param label
	 */
	private void setMaxColumnWidth(int i, JLabel label)
	{
		if (label.getPreferredSize().width > this.maxColumnWidth[i])
			this.maxColumnWidth[i] = label.getPreferredSize().width;		
	}

	private void calculateTableWidth()
	{
		this.TABLE_WIDTH = 0;
		
//		for (int i = 0; i < this.maxColumnWidth.length; i++)
//		{
//			this.TABLE_WIDTH += this.maxColumnWidth[i];
//		}
		
//		System.out.println("DescriptivesView.calculateTableWidth(): this.TABLE_WIDTH = " 
//			+ this.TABLE_WIDTH);
	}

	/**
	 * Make the descriptives table view on panel. When there is a selection, 
	 * a table for the selected data is shown next to the descriptives table
	 * for the complete data set.
	 */
	private void makeDescritivesTable()
	{
		makeDescriptivesTable(0, 0);
		
		if (this.hasSelection())
		{
			makeDescriptivesTable(1, 0);
		}
		
	}

	/**
	 * Make the descriptives table.
	 * 
	 * @param selection
	 * 		If selection is 0, the main descriptives table is made.
	 * 		If selection is 1, the descriptive table for the selection is made.
	 * @param splitClass
	 */
	private void makeDescriptivesTable(int selection, int splitClass)
	{
		// for the selection descriptives table, the grid's x coordinate is shifted 3 positions
		int x_shift = 0;
		if (selection == 1)
			x_shift = 3;
			
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;		
//		c.weighty = 1;
//		c.weightx = 1;
//		c.weightx = 0.1;
		Border paddingBorder = BorderFactory.createEmptyBorder(this.GRID_TOPGAP, this.GRID_LEFTGAP,
			this.GRID_BOTTOMGAP, this.GRID_RIGHTGAP);
		// borders on all sides except the top side
		Border matteBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);

		// add the selected variable name to the first column
		JLabel columnIndexName = new JLabel(this.model.getTableModel()
			.getColumnName(this.model.getColumnIndex()));
		columnIndexName.setFont(Statistiek.font);
		columnIndexName.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		columnIndexName.setVerticalAlignment(SwingConstants.CENTER);
		columnIndexName.setHorizontalAlignment(SwingConstants.LEFT);
		//c.fill = GridBagConstraints.BOTH;
		c.gridx = 0 + x_shift;
		c.gridy = 0;
		c.gridwidth = 2;
		mainPanel.add(columnIndexName, c);
		// Set the max of column 0
//		setMaxColumnWidth(0, columnIndexName);
		
		// borders on all sides except the top and left side
		matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK);
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// borders on all sides except the top side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);

		JLabel dummy1End = new JLabel("          ");
		dummy1End.setFont(Statistiek.font);
		//dummy1End.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 2 + x_shift;
		c.gridy = 0;
		c.gridwidth = 1;
		/*
		 *  the dummy label creates space between the two tables (if there is a selection)
		 *  and makes sure that the table is aligned to the left side
		 *  (because the label takes the remaining space)
		 */
		if ((selection == 1) || !this.hasSelection())
		{
			c.fill = GridBagConstraints.REMAINDER;
			c.weightx = 1; // makes the fill = REMAINDER setting work
		}
		else
		{
			//c.fill = GridBagConstraints.NONE;
		}
		mainPanel.add(dummy1End, c);

		// add row Number
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel aantalLabel = new JLabel(Statistiek.rb.getString("amountLabel"));
		aantalLabel.setFont(Statistiek.font);
		aantalLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0 + x_shift;
		c.gridy = 1;
		c.weightx = 0; // Makes the grid cells as small as possible 
		c.gridwidth = 1;
		mainPanel.add(aantalLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		data[0][selection][0].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		data[0][selection][0].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 1;
		mainPanel.add(data[0][selection][0], c);
		
		// add row Minimum
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel minLabel = new JLabel(Statistiek.rb.getString("minimum"));
		minLabel.setFont(Statistiek.font);
		minLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 2;
		mainPanel.add(minLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		data[1][selection][0].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		data[1][selection][0].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 2;
		mainPanel.add(data[1][selection][0], c);
		
		// add row Maximum
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel maxLabel = new JLabel(Statistiek.rb.getString("maximum"));
		maxLabel.setFont(Statistiek.font);
		maxLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 3;
		mainPanel.add(maxLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		data[2][selection][0].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		data[2][selection][0].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 3;
		mainPanel.add(data[2][selection][0], c);
		
		// add row Mean
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel meanLabel = new JLabel(Statistiek.rb.getString("mean"));
		meanLabel.setFont(Statistiek.font);
		meanLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 4;
		mainPanel.add(meanLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		data[3][selection][0].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		data[3][selection][0].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 4;
		mainPanel.add(data[3][selection][0], c);
		
		// add row Standard Deviation
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel sdLabel = new JLabel(Statistiek.rb.getString("standardDeviation"));
		sdLabel.setFont(Statistiek.font);
		sdLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 5;
		mainPanel.add(sdLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		data[4][selection][0].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		data[4][selection][0].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 5;
		mainPanel.add(data[4][selection][0], c);
		
		// add row Median
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel medianLabel = new JLabel(Statistiek.rb.getString("median"));
		medianLabel.setFont(Statistiek.font);
		medianLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 6;
		mainPanel.add(medianLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		data[5][selection][0].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		data[5][selection][0].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 6;
		mainPanel.add(data[5][selection][0], c);
		
		// add row Modus
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel modusLabel = new JLabel(Statistiek.rb.getString("mode"));
		modusLabel.setFont(Statistiek.font);
		modusLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 7;
		mainPanel.add(modusLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		data[6][selection][0].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		data[6][selection][0].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 7;
		mainPanel.add(data[6][selection][0], c);
		
		// add extra cell under last row to fill the space 
		// and make the table start at the northwest corner
		c.gridx = 0 + x_shift;
		c.gridy = DescriptivesView.NUMBER_OF_TABLE_ROWS;
		c.weighty = 1;
		c.fill = GridBagConstraints.REMAINDER;
		JLabel dummy0Rest = new JLabel(" ");
		dummy0Rest.setFont(Statistiek.font);
		mainPanel.add(dummy0Rest, c);	}

	private boolean hasSelection()
	{
		boolean hasSelection = false;
		
		ArrayList<Boolean> list = this.model.getTableModel().getSelectionList();
		
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).booleanValue())
				hasSelection = true;
		}
		
		return hasSelection;
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
	
	/**
	 * Set the preferred size of the main panel, so that if required scrollbars
	 * may appear.
	 */
	private void setMainPanelSize()
	{
		calculateTableWidth();
		
		Dimension dimension = new Dimension(this.TABLE_WIDTH,
			this.ROW_HEIGHT * (this.NUMBER_OF_TABLE_ROWS));
		
//		System.out.println("DescriptivesView.setMainPanelSize(): dimension.width = "
//			+ dimension.width + ", height = " + dimension.height);
		
		this.mainPanel.setPreferredSize(dimension);
	}
	
	// Override setBound
	public void setBounds(int x, int y, int w, int h)
	{
//		System.out.println("DescriptivesView.setBounds(x=" + x + ", y=" + y 
//			+ ", w=" + w + ", h=" + h + ")");

		super.setBounds(x, y, w, h);

		this.setMainPanelSize();
		
		this.scrollPane.setViewportView(mainPanel);
	}

	public void setModel(DescriptivesModel model)
	{
		this.model = model;
		this.model.addObserver(this);
		userOptionsPanel.setModel(model);
		this.update(null, null);
	}

	public int varBoxSelectedIndex()
	{
		return userOptionsPanel.getVarColumnsBoxSelectedIndex();
	}
}
