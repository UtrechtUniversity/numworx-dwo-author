package fi.statistiek.crosstabulationtable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
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
 * MVC View for statistiekView CrossTabulationTable
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class CrossTabulationTableView extends JPanel implements Observer
{
	private CrossTabulationTableModel model;
	private CrossTabulationTableController controller;
	private CrossTabulationTableUserOptionsPanel userOptionsPanel;
	private JButton dialogButton;

	private JPanel mainPanel;
	private JScrollPane scrollPane;
	private int numberOfColumnBins = 0;
	private int numberOfRowBins = 0;

	/**
	 * Number of rows in the crosstab table, 
	 * including header and bottom rows
	 */
	private int crosstabRows;
	/**
	 * The type of the rows
	 */
	private AllowedTypes typeRows;
	/**
	 * The type of the columns
	 */
	private AllowedTypes typeColumns;
	/**
	 * Number of columns in the crosstab table,
	 * including variable name, bins and total columns
	 */
	private int crosstabColumns;
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
	 * The total frequencies per row bin 
	 */
	private int[] aantalPerRowBin;
	private int aantalTotaal;

	private JLabel[][] data;
	private JLabel[] binLabelsRows;
	private JLabel[] binLabelsColumns;
	private Color[][] cellColors;
	
	public static final Color SELECTED_COLOR = ColorGenerator.SELECTION_COLOR;
	public static final int GRID_TOPGAP = 5;
	public static final int GRID_BOTTOMGAP = 5;
	public static final int GRID_LEFTGAP = 5;
	public static final int GRID_RIGHTGAP = 5;
	private int ROW_HEIGHT;
	private int TABLE_WIDTH;
	private int[] maxColumnWidth;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            MVC Model
	 * @param controller
	 *            MVC Controller
	 */
	public CrossTabulationTableView(CrossTabulationTableModel model,
		CrossTabulationTableController controller)
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
//		this.mainPanel.addMouseListener(new RowClickListener());

		this.userOptionsPanel = new CrossTabulationTableUserOptionsPanel(this, controller, model);

		dialogButton = userOptionsPanel.getDialogButton();

		super.add(dialogButton, BorderLayout.SOUTH);

		this.update(null, null);
	}
	
	public JTextField getBinWidthRowsField()
	{
		return this.userOptionsPanel.getBinWidthRowsField();
	}

	public JTextField getMinBoundaryRowsField()
	{
		return this.userOptionsPanel.getMinBoundaryRowsField();
	}

	public JTextField getBinWidthColumnsField()
	{
		return this.userOptionsPanel.getBinWidthColumnsField();
	}

	public JTextField getMinBoundaryColumnsField()
	{
		return this.userOptionsPanel.getMinBoundaryColumnsField();
	}

	public double getBinWidthRows()
	{
		return this.userOptionsPanel.getBinWidthRows();
	}

	public void setBinWidthRows(double d)
	{
		this.userOptionsPanel.setBinWidthRows(d);
	}

	public double getMinBoundaryRows()
	{
		return this.userOptionsPanel.getMinBoundaryRows();
	}
	
	/**
	 * Set min boundary field for the row variable with value min
	 * @param min
	 */
	public void setMinBoundaryRows(double min)
	{
		this.userOptionsPanel.setMinBoundaryRows(min);
	}

	public double getBinWidthColumns()
	{
		return this.userOptionsPanel.getBinWidthColumns();
	}

	public void setBinWidthColumns(double d)
	{
		this.userOptionsPanel.setBinWidthColumns(d);
	}

	public double getMinBoundaryColumns()
	{
		return this.userOptionsPanel.getMinBoundaryColumns();
	}
	
	/**
	 * Set min boundary field for the column variable with value min
	 * @param min
	 */
	public void setMinBoundaryColumns(double min)
	{
		this.userOptionsPanel.setMinBoundaryColumns(min);
	}

	public int varRowsBoxSelectedIndex()
	{
		return this.userOptionsPanel.getVarRowsBoxSelectedIndex();
	}

	public int varColumnsBoxSelectedIndex()
	{
		return this.userOptionsPanel.getVarColumnsBoxSelectedIndex();
	}

	/**
	 * Gets which item the user selected from the radiogroup
	 * choosing between percentage or amount.
	 * 
	 * @return true if percentage is chosen
	 */
	public boolean percentageItemSelected()
	{
		return userOptionsPanel.percentageItemSelected();
	}

	/**
	 * Checks whether percentage_endTotal radio button is selected.
	 * 
	 * @return true if percentage_endTotal is chosen
	 */
	public boolean percentage_endTotalSelected()
	{
		return userOptionsPanel.percentage_endTotalSelected();
	}

	/**
	 * Checks whether percentage_rowTotal radio button is selected.
	 * 
	 * @return true if percentage_rowTotal is chosen
	 */
	public boolean percentage_rowTotalSelected()
	{
		return userOptionsPanel.percentage_rowTotalSelected();
	}

	/**
	 * Checks whether percentage_columnTotal radio button is selected.
	 * 
	 * @return true if percentage_columnTotal is chosen
	 */
	public boolean percentage_columnTotalSelected()
	{
		return userOptionsPanel.percentage_columnTotalSelected();
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
//		System.out.println("CrossTabulationTableView.update(): splitIndex = " + this.model.getSplitOptions().getColumnSplitIndex());
		
		this.dialogButton.setVisible(this.model.getTableModel()
			.isViewsEditable());
	
		// update the components in the useroptionspanel
		userOptionsPanel.update();

		this.mainPanel.removeAll();
		if (this.model.columnIndexValid())
		{
//			GridBagLayout layout = new GridBagLayout();
//			GridBagConstraints c = new GridBagConstraints();

			ColumnType cTypeRows = this.model.getTableModel().getColumnTypes()
				.get(this.model.getColumnIndex());
			typeRows = cTypeRows.getType();
			
//			System.out.println("CrossTabulationTable.update(): typeRows = " + typeRows.toString());

			ColumnType cTypeColumns;
			if (this.model.getColumnSplitIndex() > -1)
			{
				cTypeColumns = this.model.getTableModel().getColumnTypes()
					.get(this.model.getColumnSplitIndex());
				typeColumns = cTypeColumns.getType();

//				System.out.println("CrossTabulationTable.update(): typeColumns = " + typeColumns.toString());
			}
			else
			{
				// er is geen split
//				System.out.println("CrossTabulationTable.update(): this.model.getColumnSplitIndex() > -1");
			}

			if (typeRows.isNumber())
				numberOfRowBins = this.model.getBinBoundaries().size() - 1;
			else 
			{ // ENUM or STRING
				if (this.model.enumClassFrequency() != null)
					numberOfRowBins = this.model.enumClassFrequency()[0].length;
				else
					numberOfRowBins = 0;
			}
						
			numberOfColumnBins = this.model.getTableModel().splitVarClasses(
				this.model.getSplitOptions());
			
//			System.out.println("CrossTabulationTableView.update(): numberOfRowBins = "
//				+ numberOfRowBins + ", numberOfColumnBins = " + numberOfColumnBins);

			crosstabColumns = 3 + numberOfColumnBins;
			
			if (typeRows.isNumber())
			{
				this.crosstabRows = 3 + numberOfRowBins;
				if (this.crosstabRows < 0)
				{
					this.crosstabRows = 3;
				}
				
				// objects cannot be classified in the bins and frequencies_number will be null 
				// when the binBoundaries are not updated yet 
				frequencies_number = this.model.numberClassFrequency();
			}
			else
			{
				frequencies_enum = this.model.enumClassFrequency();
				this.crosstabRows = 3 + numberOfRowBins;
			}
			this.cellColors = new Color[numberOfColumnBins][numberOfRowBins];
			
			calculateData();

			Dimension dimension;
			int noBins_rowVariable = this.model.getNoBins();
			
			int h = this.scrollPane.getViewport().getHeight();
			int w = this.scrollPane.getViewport().getWidth();
			if (h <= 0) // for some reason scrollPane is 0 sometimes...
				h = 340; // hardcoded...
			if (w <= 0)
				w = 653;
			
    		if (noBins_rowVariable * minRowHeight > h)
    		{
    			dimension = new Dimension(w, noBins_rowVariable * minRowHeight);
    		}
    		else
    		{
    			dimension = new Dimension(w, h);
    		}

//				System.out.println("... CrossTabulationTableView.update(): splitClassPanel.setPreferredSize(h = " 
//					+ dimension.height + ", w = " + dimension.width + ")");

			// set colors for background
			if (typeRows.isNumber())
			{
				if (frequencies_number != null)
				{
					for (int i = 0; i < numberOfColumnBins; i++)
					{
						for (int j = 0; j < numberOfRowBins; j++)
						{
							double d = (frequencies_number[i][j * 2] == 0 ? 0.0
								: (double) frequencies_number[i][j * 2 + 1] / (double) frequencies_number[i][j * 2]);
							// System.out.println("CrossTabulationTableView.update(): "
							// + frequencies[i + 1] + " "
							// + frequencies[i] + " " + d);
							this.cellColors[i][j] = ColorPreviewer.mixColors(
								Color.WHITE, SELECTED_COLOR, d);
						}
					}
				}
			}
			else
			{
				if (frequencies_enum != null)
				{
					for (int i = 0; i < numberOfColumnBins; i++)
					{
						for (int j = 0; j < numberOfRowBins; j++)
						{
							FrequencyTuple[] ft = frequencies_enum[i];
							double d = (ft[j].frequency == 0 ? 0
								: (double) ft[j].selectionFrequency
									/ (double) ft[j].frequency);
							// System.out.println("CrossTabulationTableView.update(): "
							// + d);
							this.cellColors[i][j] = ColorPreviewer.mixColors(
								Color.WHITE, SELECTED_COLOR, d);
						}
					}
				}
			}
			
			makeHeaderRows();
			
			makeTable();				

			// this works!
			this.mainPanel.setBackground(Color.WHITE);
		} // columnIndexValid()
		
		this.setMainPanelSize();

		this.mainPanel.revalidate();

		this.repaint();
	}

	/**
	 * Calculates the data fields used to create the crosstab table.
	 */
	private void calculateData()
	{
		boolean isPercentage = CrossTabulationTableView.this.model.isShowPercentage();
		aantalPerColumnBin = null;
		aantalPerRowBin = null;
		aantalTotaal = 0;
		
		int numberOfSplits = CrossTabulationTableView.this.model.getTableModel()
			.splitVarClasses(CrossTabulationTableView.this.model.getSplitOptions());

		aantalPerColumnBin = new int[numberOfSplits];
		aantalPerRowBin = new int[numberOfRowBins];
		
		// bereken aantalPerSplit en aantalPerBin
		for (int i = 0; i < numberOfSplits; i++)
		{
			// tel de aantallen op voor split i
			aantalPerColumnBin[i] = 0;
			if (typeRows.isNumber() && frequencies_number != null) // number variable
			{
				for (int j = 0; j < numberOfRowBins; j++)
				{
					aantalPerColumnBin[i] = aantalPerColumnBin[i]
						+ frequencies_number[i][j * 2];
				}
//				System.out.println("... aantalPerColumnBin[" + i + "] = "
//				+ aantalPerColumnBin[i]);
			}
			else if (frequencies_enum != null) // enum variable
			{
				if ((frequencies_enum[0].length == numberOfRowBins) && (frequencies_enum.length == numberOfSplits))
				{
					for (int j = 0; j < numberOfRowBins; j++)
					{
						aantalPerColumnBin[i] = aantalPerColumnBin[i]
							+ frequencies_enum[i][j].frequency;
					}
//					System.out.println("... aantalPerColumnBin[" + i + "] = "
//					+ aantalPerColumnBin[i]);
				}
			}
			aantalTotaal = aantalTotaal + aantalPerColumnBin[i];
		}
		
		for (int j = 0; j < numberOfRowBins; j++)
		{
			// tel de aantallen op voor bin j
			
			aantalPerRowBin[j] = 0;
			if (typeRows.isNumber() && frequencies_number != null) // number variable
			{
				for (int i = 0; i < numberOfSplits; i++)
				{
					aantalPerRowBin[j] = aantalPerRowBin[j]
						+ frequencies_number[i][j * 2];
				}
			}
			else if (frequencies_enum != null) // enum variable
			{
				if ((frequencies_enum[0].length == numberOfRowBins) && (frequencies_enum.length == numberOfSplits))
				{
					for (int i = 0; i < numberOfSplits; i++)
					{
						aantalPerRowBin[j] = aantalPerRowBin[j]
							+ frequencies_enum[i][j].frequency;
					}
				}
			}
//				System.out.println("... aantalPerBin[" + j + "] = " +
//					aantalPerBin[j]);
		}

		setDataLabels();
		setBinLabels();
	}

	/**
	 * Set the bin labels to be used in the crosstab table.
	 */
	private void setBinLabels()
	{
		// set the labels for the row variable
		binLabelsRows = new JLabel[numberOfRowBins];

		if (typeRows.isNumber())
		{
			for (int i = 0; i < binLabelsRows.length; i++)
			{
				String text = this.model.getBinBoundaries().get(i).toString() 
					+ "-<" 
					+ this.model.getBinBoundaries().get(i + 1).toString();
				binLabelsRows[i] = new JLabel(text);
				binLabelsRows[i].setFont(Statistiek.font);
			}
		}
		else // enum or string
		{
			if (frequencies_enum != null)
			{
				for (int i = 0; i < binLabelsRows.length; i++)
				{
					String text = frequencies_enum[0][i].label;
					binLabelsRows[i] = new JLabel(text);
					binLabelsRows[i].setFont(Statistiek.font);
				}
			}
		}
		
		// set the labels for the column variable
		binLabelsColumns = new JLabel[numberOfColumnBins];

		if (typeColumns != null)
		{
			if (typeColumns.isNumber())
			{
				for (int i = 0; i < binLabelsColumns.length; i++)
				{
					String text = this.model.getSplitOptions().getBinBoundaries().get(i).toString() 
						+ "-<" 
						+ this.model.getSplitOptions().getBinBoundaries().get(i + 1).toString();
					binLabelsColumns[i] = new JLabel(text);
					binLabelsColumns[i].setFont(Statistiek.font);
				}
			}
			else // enum or string
			{
				for (int splitClass = 0; splitClass < numberOfColumnBins; splitClass++)
				{
					String text = this.model.getSplitOptions().getSplitClassLabel(splitClass,
						this.model.getTableModel());
					binLabelsColumns[splitClass] = new JLabel(text);
					binLabelsColumns[splitClass].setFont(Statistiek.font);
				}
			}
		}
	}

	/**
	 * Set the data labels to be used in the crosstab table.
	 */
	private void setDataLabels()
	{
		if ((numberOfRowBins > 0) && (numberOfColumnBins > 0))
		{
			int x = numberOfColumnBins + 1; // + 1 for totals column
			int y = numberOfRowBins + 1; // + 1 for totals row
			data = new JLabel[x][y];
			
			if (percentageItemSelected())
			{ // percentages
				
				// calculate the divisors in order to convert the data to the percentage value
				int[][] divisors = new int[numberOfColumnBins + 1][numberOfRowBins + 1];
				
				if (percentage_endTotalSelected())
				{
					for (int i = 0; i < x; i++)
					{
						for (int j = 0; j < y; j++)
						{
							if (aantalTotaal != 0)
								divisors[i][j] = aantalTotaal;
							else
								divisors[i][j] = 0;
						}
					}
				}
				else if (percentage_rowTotalSelected())
				{
					for (int i = 0; i < x; i++)
					{
						for (int j = 0; j < y - 1; j++)
						{
							if (aantalPerRowBin[j] != 0)
								divisors[i][j] = aantalPerRowBin[j];
							else
								divisors[i][j] = 0;
						}
						
						// factor voor de laatste totaal-rij
						if (aantalTotaal != 0)
							divisors[i][y-1] = aantalTotaal;
						else
							divisors[i][y-1] = 0;
					}
				}
				else if (percentage_columnTotalSelected())
				{
					for (int i = 0; i < x - 1; i++)
					{
						for (int j = 0; j < y; j++)
						{
							if (aantalPerColumnBin[i] != 0)
								divisors[i][j] = aantalPerColumnBin[i];
							else
								divisors[i][j] = 0;
						}
					}
					
					// factor voor de laatste totaal-kolom
					for (int j = 0; j < y; j++)
					{
						if (aantalTotaal != 0)
							divisors[x-1][j] = aantalTotaal;
						else
							divisors[x-1][j] = 0;
					}
				}
				
				// set the percentage labels
				double waarde;
				String waardeString;
				if (typeRows.isNumber() && frequencies_number != null)
				{
					// for number variables use frequencies_number
					
					// loop over x het aantal splitbins
					for (int i_x = 0; i_x < x - 1; i_x++)
					{
						// loop over het aantal bins van de row variabele
						for (int i_y = 0; i_y < y - 1; i_y++)
						{
							waardeString = this.getWaardeString(frequencies_number[i_x][i_y * 2], divisors[i_x][i_y]);
							
							data[i_x][i_y] = new JLabel(waardeString + "%");
							data[i_x][i_y].setFont(Statistiek.font);
						}
						
						// set total for the end of the columns
						waardeString = this.getWaardeString(aantalPerColumnBin[i_x], divisors[i_x][y-1]);
						data[i_x][y-1] = new JLabel(waardeString + "%");
//						data[i_x][y-1] = new JLabel(((double) aantalPerColumnBin[i_x]/divisors[i_x][y-1])*100 + "%");
						data[i_x][y-1].setFont(Statistiek.font);
					}
					
					// set totals for the end of the rows
					for (int i_y = 0; i_y < y - 1; i_y++)
					{
						waardeString = this.getWaardeString(aantalPerRowBin[i_y], divisors[x-1][i_y]);
						data[x-1][i_y] = new JLabel(waardeString + "%");
//						data[x-1][i_y] = new JLabel(((double) aantalPerRowBin[i_y]/divisors[x-1][i_y])*100 + "%");
						data[x-1][i_y].setFont(Statistiek.font);
					}		
					
					waardeString = this.getWaardeString(aantalTotaal, divisors[x-1][y-1]);
					data[x-1][y-1] = new JLabel(waardeString + "%");
//					data[x-1][y-1] = new JLabel(((double) aantalTotaal/divisors[x-1][y-1])*100 + "%");
					data[x-1][y-1].setFont(Statistiek.font);
				} // type number
				else if (!typeRows.isNumber() && frequencies_enum != null) // type enum or string
				{
					// for enum or string variables use frequencies_enum
					// loop over x het aantal splitbins
					for (int i_x = 0; i_x < x - 1; i_x++)
					{
						// check for the correct frequencies_enum; frequencies_enum kent de split in de eerste getriggerde update nog niet...
						if (frequencies_enum.length == numberOfColumnBins)
						{
							// loop over het aantal bins van de row variabele
							for (int i_y = 0; i_y < y - 1; i_y++)
							{
								waardeString = this.getWaardeString(frequencies_enum[i_x][i_y].frequency, divisors[i_x][i_y]);
								data[i_x][i_y] = new JLabel(waardeString + "%");
//								data[i_x][i_y] = new JLabel(((double) frequencies_enum[i_x][i_y].frequency/divisors[i_x][i_y])*100 + "%");
								data[i_x][i_y].setFont(Statistiek.font);
							}
							
							// set total for the end of the columns
							waardeString = this.getWaardeString(aantalPerColumnBin[i_x], divisors[i_x][y - 1]);
							data[i_x][y-1] = new JLabel(waardeString + "%");
//							data[i_x][y-1] = new JLabel(((double) aantalPerColumnBin[i_x]/divisors[i_x][y - 1])*100 + "%");
							data[i_x][y-1].setFont(Statistiek.font);
						}
					}
					
					// set totals for the end of the rows
					for (int i_y = 0; i_y < y - 1; i_y++)
					{
						waardeString = this.getWaardeString(aantalPerRowBin[i_y], divisors[x-1][i_y]);
						data[x-1][i_y] = new JLabel(waardeString + "%");
//						data[x-1][i_y] = new JLabel(((double) aantalPerRowBin[i_y]/divisors[x-1][i_y])*100 + "%");
						data[x-1][i_y].setFont(Statistiek.font);
					}		
					
					waardeString = this.getWaardeString(aantalTotaal, divisors[x-1][y-1]);
					data[x-1][y-1] = new JLabel(waardeString + "%");
//					data[x-1][y-1] = new JLabel(((double) aantalTotaal/divisors[x-1][y-1])*100 + "%");
					data[x-1][y-1].setFont(Statistiek.font);					
				}
			} // percentage
			else
			{ // amounts
				if (typeRows.isNumber() && frequencies_number != null)
				{
					// for number variables use frequencies_number
					
					// loop over x het aantal splitbins
					for (int i_x = 0; i_x < x - 1; i_x++)
					{
						// loop over het aantal bins van de row variabele
						for (int i_y = 0; i_y < y - 1; i_y++)
						{
							data[i_x][i_y] = new JLabel("" + frequencies_number[i_x][i_y * 2]);
							data[i_x][i_y].setFont(Statistiek.font);
						}
						
						// set total for the end of the columns
						data[i_x][y-1] = new JLabel("" + aantalPerColumnBin[i_x]);
						data[i_x][y-1].setFont(Statistiek.font);
					}
					
					// set totals for the end of the rows
					for (int i_y = 0; i_y < y - 1; i_y++)
					{
						data[x-1][i_y] = new JLabel("" + aantalPerRowBin[i_y]);
						data[x-1][i_y].setFont(Statistiek.font);
					}		
					
					data[x-1][y-1] = new JLabel("" + aantalTotaal);
					data[x-1][y-1].setFont(Statistiek.font);
				} // type number
				else if (!typeRows.isNumber() && frequencies_enum != null) // type enum or string
				{
					// for enum or string variables use frequencies_enum
					// loop over x het aantal splitbins
					for (int i_x = 0; i_x < x - 1; i_x++)
					{
						// check of frequencies_enum de goede lengte heeft 
						// (frequencies_enum kent de split nog niet in de eerste getriggerde update)
						if (frequencies_enum.length == numberOfColumnBins)
						{
							// loop over het aantal bins van de row variabele
							for (int i_y = 0; i_y < y - 1; i_y++)
							{
								data[i_x][i_y] = new JLabel("" + frequencies_enum[i_x][i_y].frequency);
								data[i_x][i_y].setFont(Statistiek.font);
							}
							
							// set total for the end of the columns
							data[i_x][y-1] = new JLabel("" + aantalPerColumnBin[i_x]);
							data[i_x][y-1].setFont(Statistiek.font);
						}
					}
					
					// set totals for the end of the rows
					for (int i_y = 0; i_y < y - 1; i_y++)
					{
						data[x-1][i_y] = new JLabel("" + aantalPerRowBin[i_y]);
						data[x-1][i_y].setFont(Statistiek.font);
					}		
					
					data[x-1][y-1] = new JLabel("" + aantalTotaal);
					data[x-1][y-1].setFont(Statistiek.font);					
				}
			} // amounts
		
			// add the listeners
			CellClickListener ccl = new CellClickListener();
			for (int i = 0; i < numberOfColumnBins; i++)
			{
				for (int j = 0; j < numberOfRowBins; j++)
				{
					if (data[i][j] != null)
						data[i][j].addMouseListener(ccl);
				}
			}

		} // there is data
		// else no data to set...
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
		DecimalFormat df = Statistiek.getDecimalFormat();
		
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

	private void printData()
	{
		String s = "";
		if (data != null)
		{
			for (int j = 0; j < numberOfRowBins + 1; j++)
			{
				for (int i = 0; i < numberOfColumnBins; i++)
				{
					if (data[i][j] != null)
					{
						s = s + "(" + i + "," + j + ")=" + data[i][j].getText() + ",";
					}
				}
				if (data[numberOfColumnBins][j] != null)
					System.out.println("rij " + j + ": " + s + ", " + "totaal = " + data[numberOfColumnBins][j].getText());
				// reset s
				s = "";
			}
		}
		else
			System.out.println("CrossTabulationTableView.printData(): data is null!");
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
		this.maxColumnWidth = new int[crosstabColumns];

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
		c.gridwidth = numberOfColumnBins;
		mainPanel.add(variableColumnsName, c);
		
		JLabel totaalLabel = new JLabel(Statistiek.rb.getString("totalLabel"));
		totaalLabel.setFont(Statistiek.font);
		totaalLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = numberOfColumnBins + 2;
		c.gridy = 0;
		c.gridwidth = 1;
		mainPanel.add(totaalLabel, c);
		this.ROW_HEIGHT = totaalLabel.getPreferredSize().height;
		// Set the max of column numberOfColumnBins + 2
		updateMaxColumnWidth(numberOfColumnBins + 2, totaalLabel);
		
		c.gridx = numberOfColumnBins + 3;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.REMAINDER;
		JLabel dummyRest0 = new JLabel(" ");
		dummyRest0.setFont(Statistiek.font);
		mainPanel.add(dummyRest0, c);
		
		// SECOND ROW with dummy labels and column bin labels
		// borders on all sides except the top side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);

		JLabel dummy01 = new JLabel(" ");
		dummy01.setFont(Statistiek.font);
		dummy01.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(dummy01, c);

		// borders on all sides except the top and left side
		matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK);

		JLabel dummy11 = new JLabel(" ");
		dummy11.setFont(Statistiek.font);
		dummy11.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 1;
		c.gridy = 1;
		mainPanel.add(dummy11, c);
		
		// column bin labels
		for (int i = 0; i < binLabelsColumns.length; i++)
		{
			if (binLabelsColumns[i] != null)
			{
				c.gridx = i + 2;
				c.gridy = 1;
				c.gridwidth = 1;
				binLabelsColumns[i].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
				binLabelsColumns[i].setHorizontalAlignment(SwingConstants.CENTER);
				mainPanel.add(binLabelsColumns[i], c);
				// Set the max of column i + 2
				updateMaxColumnWidth(i + 2, binLabelsColumns[i]);
			}
		}
		
		JLabel dummyEnd1 = new JLabel(" ");
		dummyEnd1.setFont(Statistiek.font);
		dummyEnd1.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = binLabelsColumns.length + 2;
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
	private void updateMaxColumnWidth(int i, JLabel label)
	{
		if (label.getPreferredSize().width > this.maxColumnWidth[i])
			this.maxColumnWidth[i] = label.getPreferredSize().width;		
	}

	private void calculateTableWidth()
	{
		this.TABLE_WIDTH = 0;
		
		for (int i = 0; i < this.maxColumnWidth.length; i++)
		{
			this.TABLE_WIDTH += this.maxColumnWidth[i];
		}
		
//		System.out.println("CrossTabulationTableView.calculateTableWidth(): this.TABLE_WIDTH = " 
//			+ this.TABLE_WIDTH);
	}

	/**
	 * Make the data part of the crosstab table view on panel.
	 */
	private void makeTable()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;		
//		c.weighty = 1;
//		c.weightx = 1;
//		c.weightx = 0.1;
		Border paddingBorder = BorderFactory.createEmptyBorder(this.GRID_TOPGAP, this.GRID_LEFTGAP,
			this.GRID_BOTTOMGAP, this.GRID_RIGHTGAP);
		// borders on all sides except the top side
		Border matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);

		// add the row variable name to the first column
		JLabel variableRowsName = new JLabel(this.model.getTableModel()
			.getColumnName(this.model.getColumnIndex()));
		variableRowsName.setFont(Statistiek.font);
		variableRowsName.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		variableRowsName.setVerticalAlignment(SwingConstants.CENTER);
		variableRowsName.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = numberOfRowBins;
		mainPanel.add(variableRowsName, c);
		// Set the max of column 0
		updateMaxColumnWidth(0, variableRowsName);
		
		// borders on all sides except the top and left side
		matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK);
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// add the row bin labels
		JLabel largestLabel = null;
		for (int i = 0; i < binLabelsRows.length; i++)
		{
			if (binLabelsRows[i] != null)
			{
				c.gridx = 1;
				c.gridy = i + 2;
				c.gridwidth = 1;
				c.gridheight = 1;
				binLabelsRows[i].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
				binLabelsRows[i].setHorizontalAlignment(SwingConstants.RIGHT);
				mainPanel.add(binLabelsRows[i], c);

				// Set the max of column 1
				updateMaxColumnWidth(1, binLabelsRows[i]);
			}
		}

		// borders on all sides except the top side
		matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);

		// add the total label
		JLabel totalLabel = new JLabel(Statistiek.rb.getString("totalLabel"));
		if (percentageItemSelected() && percentage_endTotalSelected())
			totalLabel.setFont(Statistiek.font_bold);
		else
			totalLabel.setFont(Statistiek.font);
		totalLabel.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 0;
		c.gridy = crosstabRows - 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		mainPanel.add(totalLabel, c);
		// Set the max of column 0
		updateMaxColumnWidth(0, totalLabel);
		
		// borders on all sides except the top and left side
		matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK);

		JLabel dummy1End = new JLabel(" ");
		dummy1End.setFont(Statistiek.font);
		dummy1End.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
		c.gridx = 1;
		c.gridy = crosstabRows - 1;
		mainPanel.add(dummy1End, c);
		
		// add the data labels
		if (data != null)
		{
			for (int j = 0; j < numberOfRowBins; j++)
			{
				for (int i = 0; i < numberOfColumnBins; i++)
				{
					if (data[i][j] != null)
					{
						c.gridx = i + 2;
						c.gridy = j + 2;
						c.gridwidth = 1;
						c.gridheight = 1;
						data[i][j].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
						data[i][j].setHorizontalAlignment(SwingConstants.RIGHT);
						data[i][j].setBackground(cellColors[i][j]);
						data[i][j].setOpaque(true);
						mainPanel.add(data[i][j], c);
						// Set the max of column i + 2
						updateMaxColumnWidth(i + 2, data[i][j]);
					}
				}
				
				// last column with totals
				if (data[numberOfColumnBins][j] != null)
				{
					if (data[numberOfColumnBins][j] != null)
					{
						c.gridx = numberOfColumnBins + 2;
						c.gridy = j + 2;
						c.gridwidth = 1;
						c.gridheight = 1;
						data[numberOfColumnBins][j].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
						data[numberOfColumnBins][j].setHorizontalAlignment(SwingConstants.RIGHT);
						if (percentageItemSelected() && percentage_rowTotalSelected()
							&& data[numberOfColumnBins][j].getText().equals("100%"))
							data[numberOfColumnBins][j].setFont(Statistiek.font_bold);
						else
							data[numberOfColumnBins][j].setFont(Statistiek.font);
						mainPanel.add(data[numberOfColumnBins][j], c);
						// Set the max of column numberOfColumnBins + 2
						updateMaxColumnWidth(numberOfColumnBins + 2, data[numberOfColumnBins][j]);
					}
				}
			}
			
			// Last row with totals
			for (int i = 0; i < numberOfColumnBins; i++)
			{
				if (data[i][numberOfRowBins] != null)
				{
					c.gridx = i + 2;
					c.gridy = numberOfRowBins + 2;
					c.gridwidth = 1;
					c.gridheight = 1;
					data[i][numberOfRowBins].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
					data[i][numberOfRowBins].setHorizontalAlignment(SwingConstants.RIGHT);
					if (percentageItemSelected() && percentage_columnTotalSelected()
						&& data[i][numberOfRowBins].getText().equals("100%"))
						data[i][numberOfRowBins].setFont(Statistiek.font_bold);
					else
						data[i][numberOfRowBins].setFont(Statistiek.font);
					mainPanel.add(data[i][numberOfRowBins], c);
					// Set the max of column i + 2
					updateMaxColumnWidth(i + 2, data[i][numberOfRowBins]);
				}
			}
			
			// endtotal
			if (data[numberOfColumnBins][numberOfRowBins] != null)
			{
				c.gridx = numberOfColumnBins + 2;
				c.gridy = numberOfRowBins + 2;
				c.gridwidth = 1;
				c.gridheight = 1;
				data[numberOfColumnBins][numberOfRowBins].setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
				data[numberOfColumnBins][numberOfRowBins].setHorizontalAlignment(SwingConstants.RIGHT);
				if (percentageItemSelected() && percentage_endTotalSelected())
					data[numberOfColumnBins][numberOfRowBins].setFont(Statistiek.font_bold);
				else
					data[numberOfColumnBins][numberOfRowBins].setFont(Statistiek.font);
				mainPanel.add(data[numberOfColumnBins][numberOfRowBins], c);
				// Set the max of column i + 2
				updateMaxColumnWidth(numberOfColumnBins + 2, data[numberOfColumnBins][numberOfRowBins]);
			}

			
			// add extra cell under last row to fill the space 
			// and make the table start at the northwest corner
			c.gridx = 0;
			c.gridy = numberOfRowBins + 3;
			c.weighty = 1;
			c.fill = GridBagConstraints.REMAINDER;
			JLabel dummy0Rest = new JLabel(" ");
			dummy0Rest.setFont(Statistiek.font);
			mainPanel.add(dummy0Rest, c);

		}
//		else
//			System.out.println("CrossTabulationTableView.makeTable(): data is null!");
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
			this.ROW_HEIGHT * (numberOfRowBins + 3));
		
//		System.out.println("CrossTabulationTableView.setMainPanelSize(): dimension.width = "
//			+ dimension.width + ", height = " + dimension.height);
		
		this.mainPanel.setPreferredSize(dimension);
	}
	
	// Override setBound
	public void setBounds(int x, int y, int w, int h)
	{
//		System.out.println("CrossTabulationTableView.setBounds(x=" + x + ", y=" + y 
//			+ ", w=" + w + ", h=" + h + ")");

		super.setBounds(x, y, w, h);

		this.setMainPanelSize();
		
		this.scrollPane.setViewportView(mainPanel);

		// System.out.println("HistogramView.setBounds(): Size histogram: " +
		// this.getBounds().toString()
		// + ", scrollbarVisible=" +
		// scrollPane.getVerticalScrollBar().isVisible());
		
//		 System.out.println("HistogramView.setBounds(): scrollPane w="
//			 + scrollPane.getWidth()
//			 + ", h=" + scrollPane.getHeight());
	}

	public void setModel(CrossTabulationTableModel model)
	{
		this.model = model;
		this.model.addObserver(this);
		userOptionsPanel.setModel(model);
		this.update(null, null);
	}

	public int getSplitBinsBoxSelectedInt()
	{
		return userOptionsPanel.getVarColumnsBoxSelectedIndex();
	}

	/*
	 * CellClickListener is used on the labels in the crosstabulation table.
	 */
	private class CellClickListener implements MouseListener
	{

		public void mouseClicked(MouseEvent arg0)
		{
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
//			System.out.println("CrossTabulationTableView.CellClickListener.mouseReleased()!");
			
			for (int i = 0; i < numberOfColumnBins; i++)
			{
				for (int j = 0; j < numberOfRowBins; j++)
				{
					if (arg0.getSource().equals(data[i][j]))
					{
//						System.out.println("select! data(" + i + ", " + j + ")");
						cellClicked(i, j);
						break;
					}
				}
			}
		}

		/**
		 * The user clicked on cell (columnNumber, rowNumber) in the crosstabulation
		 * table.
		 * @param columnNumber
		 * @param rowNumber
		 */
		private void cellClicked(int columnNumber, int rowNumber)
		{
			if (!CrossTabulationTableView.this.model.columnIndexValid())
			{
				return;
			}
			
			// update the selectionList

			ColumnType cType = CrossTabulationTableView.this.model.getTableModel()
				.getColumnTypes()
				.get(CrossTabulationTableView.this.model.getColumnIndex());

			if (typeRows.isNumber())
			{
				ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
					CrossTabulationTableView.this.model.getTableModel().getRowCount());
				for (int i = 0; i < CrossTabulationTableView.this.model
					.getTableModel().getRowCount(); i++)
				{
					Object o = CrossTabulationTableView.this.model.getTableModel()
						.getValueAt(i,
							CrossTabulationTableView.this.model.getColumnIndex());

					selectionList.add(!o.equals(ColumnType.WILDCARD)
						&& CrossTabulationTableView.this.model.binOfNumber(Double
							.parseDouble((String) o)) == rowNumber
						&& CrossTabulationTableView.this.model.getTableModel()
							.classifyObject(i,
								CrossTabulationTableView.this.model.getSplitOptions()) == columnNumber);

				}

				CrossTabulationTableView.this.model.getTableModel().setSelectionList(
					selectionList);
			}
			else
			{
				String clicked;
				if (typeRows.equals(AllowedTypes.ENUM))
				{
					clicked = cType.getEnumOptions()[rowNumber];
					int wildcardIndex = Arrays.asList(cType.getEnumOptions())
						.indexOf(ColumnType.WILDCARD);
					if (wildcardIndex <= 0 && wildcardIndex < rowNumber)
					{
						clicked = cType.getEnumOptions()[rowNumber + 1];
					}
				}
				else
				{
					ArrayList<String> options = CrossTabulationTableView.this.model
						.getTableModel().stringColumnOptions(
							CrossTabulationTableView.this.model.getColumnIndex());
					//System.out.println("CrossTabulationTableView.RowClickListener.rowClicked(): " + options);
					clicked = options.get(rowNumber);
				}

				ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
					CrossTabulationTableView.this.model.getTableModel().getRowCount());
				for (int i = 0; i < CrossTabulationTableView.this.model
					.getTableModel().getRowCount(); i++)
				{
					Object o = CrossTabulationTableView.this.model.getTableModel()
						.getValueAt(i,
							CrossTabulationTableView.this.model.getColumnIndex());

					selectionList
					.add(!o.equals(ColumnType.WILDCARD)
						&& ((String) o).equals(clicked)
						&& CrossTabulationTableView.this.model.getTableModel()
							.classifyObject(i,
								CrossTabulationTableView.this.model.getSplitOptions()) == columnNumber);
				}
				CrossTabulationTableView.this.model.getTableModel().setSelectionList(
					selectionList);
			}
			
			CrossTabulationTableView.this.mainPanel.revalidate();
		}
	}
}
