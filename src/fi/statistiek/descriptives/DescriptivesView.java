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
	private JLabel[][][] dataLabels;
	
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
//		System.out.println("DescriptivesView.update(): splitIndex = " + this.model.getSplitOptions().getColumnSplitIndex());
		
		this.dialogButton.setVisible(this.model.getStatTableModel()
			.isViewsEditable());
	
		this.mainPanel.setVisible(false);
		this.mainPanel.removeAll(); // this can be very slow if there are many children
		this.mainPanel.setVisible(true);
		
		if (this.model.columnIndexValid())
		{
			ColumnType cTypeColumnIndex = this.model.getStatTableModel().getColumnTypes()
				.get(this.model.getColumnIndex());
			this.typeColumnIndex = cTypeColumnIndex.getType();
			
			ColumnType cTypeSplit;
			if (this.model.getColumnSplitIndex() > -1)
			{
				cTypeSplit = this.model.getStatTableModel().getColumnTypes()
					.get(this.model.getColumnSplitIndex());
				typeSplitVar = cTypeSplit.getType();

				//System.out.println("DescriptivesView.update(): typeSplitVar = " + typeSplitVar.toString());
			}
			else
			{
				// er is geen split
				//System.out.println("DescriptivesView.update(): this.model.getColumnSplitIndex() > -1");
			}
			
			if (this.typeColumnIndex.isNumber())
			{
				this.frequencies_number = this.model.numberClassFrequency();
			}
			else
			{ // enum or string
				this.frequencies_enum = this.model.enumClassFrequency();
			}

			setData();

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

		// update the components in the useroptionspanel
		userOptionsPanel.update();

		this.mainPanel.revalidate();

		this.repaint();
	}

	/**
	 * Calculate the data and set the data labels used to create the descriptives table.
	 */
	private void setData()
	{
		int numberOfSplits = DescriptivesView.this.model.getStatTableModel()
			.numberOfSplitVarClasses(DescriptivesView.this.model.getSplitOptions());
		
		int columnIndex = this.model.getColumnIndex(); // this.varBoxSelectedIndex() is nog niet geupdate!

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
			// number of cases, missing excluded (missing is wildcard '*')
			this.numberOfCases[0][i] = this.getNumberOfCases(0, i);

			if (this.hasSelection())
				this.numberOfCases[1][i] = this.getNumberOfCases(1, i);
		}
		
		
		for (int i = 0; i < numberOfSplits; i++)
		{
			String s;

			s = this.model.getColumnMode(columnIndex, i, false);
			if (this.isNumeric(s))
				this.mode[0][i] = Statistiek.getStringValue(Double.valueOf(s));
			else
				this.mode[0][i] = s;
			
			if (this.hasSelection())
			{
//				s = this.model.getTableModel().getColumnModeOfSelection(columnIndex);
				s = this.model.getColumnMode(columnIndex, i, true);
				if (this.isNumeric(s))
					this.mode[1][i] = Statistiek.getStringValue(Double.valueOf(s));
				else
					this.mode[1][i] = s;
			}
		}		
		
		// set data for both the main table and the selection table
		setDataLabels();
	}

	/**
	 * Get the number of cases for the given splitClass,
	 * excluding wildcards and outliers. 
	 * 
	 * @param selection
	 * 		If selection is 0, the number of cases for the splitClass is returned.
	 * 		If selection is 1, the number of selected cases for the splitClass is returned. 
	 * @param splitClass
	 * @return
	 */
	private int getNumberOfCases(int selection, int splitClass)
	{
		int numberOfCases = 0;
		
		if (this.typeColumnIndex.isNumber())
		{
			if (this.frequencies_number != null)
			{
				int[] frequencies = frequencies_number[splitClass];
//				int sum = 0;
//				for (int i = 0; i < frequencies.length/2; i++)
//				{
//					if (selection == 0)
//						sum += frequencies[2*i];
//					else
//						sum += frequencies[2*i + 1];
//				}
//				numberOfCases = sum;
				
				// Er is altijd maar 1 bin; bovenstaande voor het geval er meerdere bins zijn
				if (selection == 0)
					numberOfCases = frequencies[0];
				else
					numberOfCases = frequencies[1];
			}
		}
		else
		{ // enum or string
			if (frequencies_enum != null)
			{
				FrequencyTuple[] frequencies = frequencies_enum[splitClass];
				int sum = 0;
				
				for (int i = 0; i < frequencies.length; i++)
				{
					if (!frequencies[i].label.equals(ColumnType.WILDCARD))
					{
						if (selection == 0)
							sum += frequencies[i].frequency;
						else
							sum += frequencies[i].selectionFrequency;
					}
				}
				numberOfCases = sum;
			}
		}
		
		return numberOfCases;
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
		int numberOfSplits = DescriptivesView.this.model.getStatTableModel()
			.numberOfSplitVarClasses(DescriptivesView.this.model.getSplitOptions());

		if (this.hasSelection())
			dataLabels = new JLabel[this.NUMBER_OF_DESCRIPTIVES][2][numberOfSplits];
		else
			dataLabels = new JLabel[this.NUMBER_OF_DESCRIPTIVES][1][numberOfSplits];

		for (int i = 0; i < numberOfSplits; i++)
		{
			// the main data
			this.setDataLabels(0, i);
			
			if (this.hasSelection())
			{
				// the selection data
				this.setDataLabels(1, i);
			}
		}
	}

	public JLabel[][][] getDataLabels()
	{
		return dataLabels;
	}

	public int[][] getNumberOfCases()
	{
		return numberOfCases;
	}

	public String[][] getMode()
	{
		return mode;
	}

	private void setDataLabels(int selection, int splitClass)
	{
		String minimumString;
		String maximumString;
		String meanString;
		String sdString;
		String medianString;

		if (this.typeColumnIndex.isNumber() && (this.numberOfCases[selection][splitClass] > 0))
		{
			int columnIndex = this.model.getColumnIndex(); // this.varBoxSelectedIndex() is nog niet geupdate!
			int numberOfDecimals = determineMaxNumberOfDecimals(columnIndex) + 2;
			
			if (selection == 0)
			{
				minimumString = this.getMinimumValue(columnIndex, splitClass, false);
				maximumString = this.getMaximumValue(columnIndex, splitClass, false);
				
				meanString = this.model.getColumnMean(columnIndex, splitClass, false);
				if (!meanString.equals(Statistiek.rb.getString("notAvailable")))
				{
					meanString = Statistiek.getStringValue(
						Statistiek.round(Statistiek.parseDouble(meanString), numberOfDecimals));
				}
				
				sdString = this.model.getColumnSD(columnIndex, splitClass, false);
				if (!sdString.equals(Statistiek.rb.getString("notAvailable")))
				{
					sdString = Statistiek.getStringValue(
						Statistiek.round(Statistiek.parseDouble(sdString), numberOfDecimals));
				}
				medianString = getMedianValue(columnIndex, splitClass, false);
			}
			else
			{
				minimumString = this.getMinimumValue(columnIndex, splitClass, true);
				maximumString = this.getMaximumValue(columnIndex, splitClass, true);
				
				meanString = this.model.getColumnMean(columnIndex, splitClass, true);
				if (!meanString.equals(Statistiek.rb.getString("notAvailable")))
				{
					meanString = Statistiek.getStringValue(
						Statistiek.round(Statistiek.parseDouble(meanString), numberOfDecimals));
				}
				
				sdString = this.model.getColumnSD(columnIndex, splitClass, true);
				if (!meanString.equals(Statistiek.rb.getString("notAvailable")))
				{
					sdString = Statistiek.getStringValue(
						Statistiek.round(Statistiek.parseDouble(sdString), numberOfDecimals));
				}
				medianString = getMedianValue(columnIndex, splitClass, true);
			}
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
		dataLabels[0][selection][splitClass] = new JLabel(String.valueOf(this.numberOfCases[selection][splitClass]));
		dataLabels[0][selection][splitClass].setFont(Statistiek.font);
		
		// minimum
		dataLabels[1][selection][splitClass] = new JLabel(minimumString);
		dataLabels[1][selection][splitClass].setFont(Statistiek.font);

		// maximum
		dataLabels[2][selection][splitClass] = new JLabel(maximumString);
		dataLabels[2][selection][splitClass].setFont(Statistiek.font);

		// mean
		dataLabels[3][selection][splitClass] = new JLabel(meanString);
		dataLabels[3][selection][splitClass].setFont(Statistiek.font);

		// standard deviation
		dataLabels[4][selection][splitClass] = new JLabel(sdString);
		dataLabels[4][selection][splitClass].setFont(Statistiek.font);

		// median
		dataLabels[5][selection][splitClass] = new JLabel(medianString);
		dataLabels[5][selection][splitClass].setFont(Statistiek.font);

		// modus
		dataLabels[6][selection][splitClass] = new JLabel(this.mode[selection][splitClass]);
		dataLabels[6][selection][splitClass].setFont(Statistiek.font);

		if (selection == 1)
		{
			this.setSelectionColors(selection, splitClass);
		}
	}

	private void setSelectionColors(int selection, int splitClass)
	{
		for (int i = 0; i < this.NUMBER_OF_DESCRIPTIVES; i++)
		{
			dataLabels[i][selection][splitClass].setBackground(this.SELECTED_COLOR);
			dataLabels[i][selection][splitClass].setOpaque(true);
		}
	}

	private String getMedianValue(int columnIndex, int splitClass, boolean forSelection)
	{
		String medianValue;
		
		int numberOfDecimals = determineMaxNumberOfDecimals(columnIndex) + 2;

		double medianDouble = this.model.getColumnMedian(columnIndex, splitClass, forSelection);

		medianValue = Statistiek.getStringValue(Statistiek.round(medianDouble, numberOfDecimals));
		
		return medianValue;
	}

	private String getMaximumValue(int columnIndex, int splitClass, boolean forSelection)
	{
		String maximumValue = this.model.getColumnMax(columnIndex, splitClass, forSelection);
		
		return maximumValue;
	}

	private String getMinimumValue(int columnIndex, int splitClass, boolean forSelection)
	{
		String minimumValue = this.model.getColumnMin(columnIndex, splitClass, forSelection);
		
		return minimumValue;
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
		for (int i = 0; i < this.model.getStatTableModel().getRowCount(); i++)
		{
			if (!this.model.getStatTableModel().isOutlier(i, columnIndex))
			{
				numberString = String.valueOf(this.model.getStatTableModel().getValueAt(i, columnIndex));
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
		DecimalFormat df = Statistiek.getDefaultDecimalFormat();
		
		if (divisor != 0)
		{
			waarde = ((double) frequency/divisor)*100;
			waardeString = df.format(waarde);
		}
		else
			waardeString = "0";
		
		return waardeString;
	}

	/**
	 * Make the descriptives table view on panel. When there is a selection, 
	 * a table for the selected data is shown next to the descriptives table
	 * for the complete data set.
	 */
	private void makeDescritivesTable()
	{
		int numberOfSplits = DescriptivesView.this.model.getStatTableModel()
			.numberOfSplitVarClasses(DescriptivesView.this.model.getSplitOptions());

		for (int i = 0; i < numberOfSplits; i++)
		{
			makeDescriptivesTable(0, i);
			
			if (this.hasSelection())
				makeDescriptivesTable(1, i);
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
		
		int y_shift = (splitClass) * (this.NUMBER_OF_TABLE_ROWS + 2);
		String splitClassString = "";
		
		if (this.hasSplit())
		{
			String splitColumnName = this.model.getStatTableModel().getColumnName(this.model.getColumnSplitIndex());
			splitClassString = splitColumnName + ": "
				+ this.model.getSplitOptions().getSplitClassLabel(splitClass, this.model.getStatTableModel()); // e.g., "geslacht: m"
		}
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;		

		Border paddingBorder = BorderFactory.createEmptyBorder(this.GRID_TOPGAP, this.GRID_LEFTGAP,
			this.GRID_BOTTOMGAP, this.GRID_RIGHTGAP);
		// borders on all sides except the top side
		Border matteBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);

		// add the selected variable name to the first column
		String label = "";
		if (selection == 0)
			label = this.model.getStatTableModel().getColumnName(this.model.getColumnIndex());
		else
			label = Statistiek.rb.getString("selection");
		JLabel columnIndexName = new JLabel(label);
		columnIndexName.setFont(Statistiek.font);
		columnIndexName.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		columnIndexName.setVerticalAlignment(SwingConstants.CENTER);
		columnIndexName.setHorizontalAlignment(SwingConstants.LEFT);
		c.gridx = 0 + x_shift;
		c.gridy = 0 + y_shift;
		c.gridwidth = 2;
		mainPanel.add(columnIndexName, c);
		
		// borders on all sides except the top and left side
		matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK);
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// borders on all sides except the top side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);

		JLabel dummy1End = new JLabel("          ");
		dummy1End.setFont(Statistiek.font);
		//dummy1End.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 2 + x_shift;
		c.gridy = 0 + y_shift;
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
		c.gridy = 1 + y_shift;
		c.weightx = 0; // Makes the grid cells as small as possible 
		c.gridwidth = 1;
		mainPanel.add(aantalLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		dataLabels[0][selection][splitClass].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		dataLabels[0][selection][splitClass].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 1 + y_shift;
		mainPanel.add(dataLabels[0][selection][splitClass], c);
		
		// add row Minimum
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel minLabel = new JLabel(Statistiek.rb.getString("minimum"));
		minLabel.setFont(Statistiek.font);
		minLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 2 + y_shift;
		mainPanel.add(minLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		dataLabels[1][selection][splitClass].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		dataLabels[1][selection][splitClass].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 2 + y_shift;
		mainPanel.add(dataLabels[1][selection][splitClass], c);
		
		// add row Maximum
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel maxLabel = new JLabel(Statistiek.rb.getString("maximum"));
		maxLabel.setFont(Statistiek.font);
		maxLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 3 + y_shift;
		mainPanel.add(maxLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		dataLabels[2][selection][splitClass].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		dataLabels[2][selection][splitClass].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 3 + y_shift;
		mainPanel.add(dataLabels[2][selection][splitClass], c);
		
		// add row Mean
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel meanLabel = new JLabel(Statistiek.rb.getString("mean"));
		meanLabel.setFont(Statistiek.font);
		meanLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 4 + y_shift;
		mainPanel.add(meanLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		dataLabels[3][selection][splitClass].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		dataLabels[3][selection][splitClass].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 4 + y_shift;
		mainPanel.add(dataLabels[3][selection][splitClass], c);
		
		// add row Standard Deviation
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel sdLabel = new JLabel(Statistiek.rb.getString("standardDeviation"));
		sdLabel.setFont(Statistiek.font);
		sdLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 5 + y_shift;
		mainPanel.add(sdLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		dataLabels[4][selection][splitClass].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		dataLabels[4][selection][splitClass].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 5 + y_shift;
		mainPanel.add(dataLabels[4][selection][splitClass], c);
		
		// add row Median
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel medianLabel = new JLabel(Statistiek.rb.getString("median"));
		medianLabel.setFont(Statistiek.font);
		medianLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 6 + y_shift;
		mainPanel.add(medianLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		dataLabels[5][selection][splitClass].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		dataLabels[5][selection][splitClass].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 6 + y_shift;
		mainPanel.add(dataLabels[5][selection][splitClass], c);
		
		// add row Modus
		// label
		// borders on left and bottom side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK);
		JLabel modusLabel = new JLabel(Statistiek.rb.getString("mode"));
		modusLabel.setFont(Statistiek.font);
		modusLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0 + x_shift;
		c.gridy = 7 + y_shift;
		mainPanel.add(modusLabel, c);
		// value
		// borders on left, bottom and right side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		dataLabels[6][selection][splitClass].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		dataLabels[6][selection][splitClass].setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1 + x_shift;
		c.gridy = 7 + y_shift;
		mainPanel.add(dataLabels[6][selection][splitClass], c);
		
		if (selection == 0)
		{
			// add label for the split class
			// no borders
			matteBorder = BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK);
			JLabel splitClassLabel = new JLabel(splitClassString);
			splitClassLabel.setFont(Statistiek.font);
			splitClassLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
			c.gridx = 0;
			c.gridy = DescriptivesView.NUMBER_OF_TABLE_ROWS + y_shift;
			mainPanel.add(splitClassLabel, c);
		}
		
		// add extra cell under last row to fill the space 
		// and make the table start at the northwest corner
		c.gridx = 0 + x_shift;
		c.gridy = DescriptivesView.NUMBER_OF_TABLE_ROWS + 1 + y_shift;
		if (this.lastRowInView(splitClass))
		{
			c.weighty = 1;
			c.fill = GridBagConstraints.REMAINDER;
		}

		JLabel dummy0Rest = new JLabel(" ");
		dummy0Rest.setFont(Statistiek.font);
		mainPanel.add(dummy0Rest, c);	
	}

	private boolean lastRowInView(int splitClass)
	{
		int numberOfSplits = DescriptivesView.this.model.getStatTableModel()
			.numberOfSplitVarClasses(DescriptivesView.this.model.getSplitOptions());
		
		return (splitClass == numberOfSplits - 1);
	}

	/**
	 * Returns true if the view has a split, else false.
	 * @return
	 * 		 True if the view has a split, else false.
	 */
	private boolean hasSplit()
	{
		return (this.model.getColumnSplitIndex() > -1);
	}

	private boolean hasSelection()
	{
		boolean hasSelection = false;
		
		ArrayList<Boolean> list = this.model.getStatTableModel().getSelectionList();
		
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).booleanValue())
			{
				hasSelection = true;
				break;
			}
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
	
	// Override setBound
	public void setBounds(int x, int y, int w, int h)
	{
//		System.out.println("DescriptivesView.setBounds(x=" + x + ", y=" + y 
//			+ ", w=" + w + ", h=" + h + ")");

		super.setBounds(x, y, w, h);
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
	
	public int getSplitVarBoxSelectedIndex()
	{
		return userOptionsPanel.getSplitVarBoxSelectedIndex();
	}
	
	public int getSplitBinsBoxSelectedInt()
	{
		return userOptionsPanel.getSplitBinsBoxSelectedInt();
	}

	public double getSplitMinBoundary()
	{
		return userOptionsPanel.getSplitminBoundary();
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

	/**
	 *Set the split bin width based on the model's split bin boundaries. 
	 */
	public void setSplitBinWidth()
	{
		this.userOptionsPanel.setSplitBinWidth();
	}
}
