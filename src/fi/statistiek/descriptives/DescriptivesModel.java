package fi.statistiek.descriptives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import fi.statistiek.SelectionListener;
import fi.statistiek.SplitOptions;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
// use some features from histogram
import fi.statistiek.histogram.HistogramModel.FrequencyTuple;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * MVC model for StatistiekView Descriptives
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class DescriptivesModel extends Observable implements
	TableModelListener, SelectionListener
{
	private int columnIndex;
	/**
	 * Bin boundaries (by default one bin)
	 * in order to be able to get the frequencies.
	 */
	private ArrayList<Double> binBoundaries;
	private int noBins;
	
	private SplitOptions splitOptions;

	private StatTableModel tableModel;
	private String viewName;

	/**
	 * Constructor
	 * 
	 * @param tableModel
	 *            the data model
	 * @param viewName
	 *            the initial name of this view
	 */
	public DescriptivesModel(StatTableModel tableModel, String viewName)
	{
		this.tableModel = tableModel;
		this.tableModel.addTableModelListener(this);
		this.tableModel.addSelectionListener(this);
		
		this.splitOptions = new SplitOptions();

		this.viewName = viewName;

		// set initial values
		this.columnIndex = -1;
		this.noBins = 1;
		this.binBoundaries = new ArrayList<Double>();
		this.binBoundaries.add(new Double(-100));
		this.binBoundaries.add(new Double(100));
	}

	/**
	 * Get the bin boundaries
	 * 
	 * @return The bin boundaries
	 */
	public ArrayList<Double> getBinBoundaries()
	{
		return this.binBoundaries;
	}

	/**
	 * Set the bin boundaries
	 * 
	 * @param bins
	 *            The new bin boundaries
	 */
	public void setBinBoundaries(ArrayList<Double> bins)
	{
		this.binBoundaries = bins;
		
		this.noBins = this.binBoundaries.size() - 1;
		this.changed();
	}

	/**
	 * Set the name of this StatistiekView
	 * 
	 * @param viewName
	 *            the new name of this view
	 */
	public void setViewName(String viewName)
	{
		if (!this.viewName.equals(viewName))
		{
			this.viewName = viewName;
			this.changed();
		}

	}

	/**
	 * @return this StatistiekView's name
	 */
	public String getViewName()
	{
		return this.viewName;
	}

	/**
	 * Set the data table
	 * 
	 * @param tableModel
	 *            the new data table
	 */
	public void setTableModel(StatTableModel tableModel)
	{
		if (!(this.tableModel == tableModel))
		{
			this.tableModel.removeTableModelListener(this);
			this.tableModel = tableModel;
			this.tableModel.addTableModelListener(this);
			this.tableModel.addSelectionListener(this);
			this.changed();
		}

	}

	/**
	 * @return the data table
	 */
	public StatTableModel getTableModel()
	{
		return this.tableModel;
	}

	/**
	 * Set the column index in the descriptives table.
	 * 
	 * @param columnIndex
	 *            The index of the column that will be shown
	 */
	public void setColumnIndex(int columnIndex)
	{
		if (!(this.columnIndex == columnIndex))
		{
			this.columnIndex = columnIndex;
			
			if (this.columnIndexValid()
				&& this.tableModel.getColumnTypes().get(this.columnIndex)
					.getType().isNumber())
			{
				this.binBoundaries = Statistiek.appropriateBoundaries(
					this.tableModel.getColumnMin(this.columnIndex),
					this.tableModel.getColumnMax(this.columnIndex),
					1);//this.noBins);
			}
			this.changed();
		}
	}

	/**
	 * @return The index of the column that is represented by this
	 *         StatistiekView
	 */
	public int getColumnIndex()
	{
		return this.columnIndex;
	}

	/**
	 * Check if the current column index is a valid column index
	 * 
	 * @return true iff valid
	 */
	public boolean columnIndexValid()
	{
		return this.columnIndex >= 0
			&& this.columnIndex < this.tableModel.getColumnCount();
	}

	/**
	 * Find the frequency of every bin, and the amount of selected objects in
	 * this bin. To be used only for columns of type integer or double.
	 * 
	 * @return array of frequencies, with index 2*i the frequency of bin i, and
	 *         2*i + 1 the amount of selected items in this bin.
	 */
	public int[][] numberClassFrequency()
	{
		return this.tableModel.numberClassFrequency(this.binBoundaries,
			this.columnIndex, this.splitOptions);
	}

	/**
	 * Find the frequency of every class. To be used only for columns of type enum or
	 * string.
	 * 
	 * @return array of FrequencyTuples (which contains class label and
	 *         frequency)
	 */
	public FrequencyTuple[][] enumClassFrequency()
	{
		return this.tableModel.enumClassFrequency(this.columnIndex, this.splitOptions);
	}

	public void tableChanged(TableModelEvent arg0)
	{
		this.changed();
	}

	public void selectionChanged()
	{
		this.changed();
	}
	
	/**
	 * Update the column index 
	 * given that removedColumn has been removed.
	 * @param removedColumn
	 */
	public void updateColumnIndex(int removedColumn)
	{
		// index van de geselecteerde variabele bijwerken
		if (removedColumn < this.columnIndex)
		{
			this.columnIndex = this.columnIndex - 1;
		}
		else if (removedColumn == this.columnIndex)
		{
			this.columnIndex = -1;
		}
		
		// index van de split variabele bijwerken
		if (removedColumn < this.splitOptions.getColumnSplitIndex())
		{
			setColumnSplitIndex(this.splitOptions.getColumnSplitIndex() - 1);
		}
		else if (removedColumn == this.splitOptions.getColumnSplitIndex())
		{
			setColumnSplitIndex(- 1);
		}
	}
	
	public SplitOptions getSplitOptions()
	{
		return this.splitOptions;
	}
	
	public void setSplitOptions(SplitOptions splitOptions)
	{
		this.splitOptions = splitOptions;
		this.changed();
	}
	
	/**
	 * Abbreviation of setChanged and notifyObservers
	 */
	private void changed()
	{
		// System.out.println("HistogramModel.changed()");
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setColumnSplitIndex(int columnSplitIndex)
	{
		if (this.splitOptions.getColumnSplitIndex() != columnSplitIndex)
		{
			this.splitOptions.setColumnSplitIndex(columnSplitIndex);
			this.changed();
		}
	}

	/**
	 * Get the index of the column by which the data is split
	 * 
	 * @return the index of the column by which the data is split
	 */
	public int getColumnSplitIndex()
	{
		return this.splitOptions.getColumnSplitIndex();
	}

	public void setSplitBoundaries(ArrayList<Double> boundaries)
	{
		this.splitOptions.setBinBoundaries(boundaries);
		this.changed();
	}
	
	/**
	 * Get the mode of column columnIndex for the given split class.
	 * if there is no split, the mode of column columnIndex for the complete data set is
	 * returned.
	 * If forSelection is true, the mode is determined for the 
	 * selected values within the given split class.
	 * 
	 * @param columnIndex
	 *            The column index
	 * @param splitClass
	 *            The split class
	 * @param forSelection
	 *            Only look at the selection y/n
	 * @return The mode of column columnIndex for the given split class. 
	 */
	public String getColumnMode(int columnIndex, int splitClass, boolean forSelection)
	{
		String mode = "";
		AllowedTypes type = this.tableModel.getColumnTypes().get(columnIndex).getType();
		int splitColumnIndex = this.splitOptions.getColumnSplitIndex();
		
		if (splitColumnIndex == -1)
		{
			// there is no split
			if (forSelection)
				mode = this.tableModel.getColumnModeOfSelection(columnIndex);
			else
				mode = this.tableModel.getColumnMode(columnIndex);
		}
		else
		{ 
			// there is a split
			AllowedTypes splitType = this.tableModel.getColumnTypes().get(splitColumnIndex).getType();
			int maxFreq = 0;
			boolean multipleModes = false;
			boolean valueInSplit = false;
			String valueString, splitValueString;
			
			ArrayList<String> data = new ArrayList<String>();
	
			for (int i = 0; i < this.tableModel.getRowCount(); i++)
			{
				if ((forSelection && this.tableModel.getSelectionList().get(i))
					|| (!forSelection))
				{
					valueString = (String) this.tableModel.getValueAt(i, columnIndex);
					splitValueString = (String) this.tableModel.getValueAt(i, splitColumnIndex);
		
					if (!valueString.equals(ColumnType.WILDCARD))
					{
						valueInSplit = this.isValueInSplit(splitValueString, splitType, splitClass);
						
						if (valueInSplit)
						{
							// add the value to a list based on the splitclass
							data.add(valueString);
						}
					}
				}
			} // for loop over all data rows
				
			Collections.sort(data);
			
			for (int i = 0; i < data.size(); i++)
			{
				int freq_i = Collections.frequency(data, data.get(i));
				if (freq_i > maxFreq)
				{
					maxFreq = Collections.frequency(data, data.get(i));
					mode = String.valueOf(data.get(i));
					multipleModes = false;
				}
				else if ((freq_i != 0) && (freq_i == maxFreq) && (!data.get(i).equals(data.get(i - 1))))
				{ // check if there is another value with the same max frequency
					multipleModes = true;
				}
			}
			
			if (multipleModes || mode.equals(""))
				mode = Statistiek.rb.getString("notAvailable");
		} // there is a split
		
		return mode;
	}	
	
	private boolean isValueInSplit(String splitValueString,
		AllowedTypes splitType, int splitClass)
	{
		boolean valueInSplit = false;
		
		if (splitType.isNumber())
		{
			// determine the split bin values
			Double splitClassMinValue = this.splitOptions.getBinBoundaries().get(splitClass); 
			Double splitClassMaxValue = this.splitOptions.getBinBoundaries().get(splitClass + 1);
			if (!splitValueString.equals(ColumnType.WILDCARD))
			{
				// get the value of the split column
				Double d_split = Double.parseDouble(splitValueString);

				// check if the value of the split column is in the split bin
				if ((d_split >= splitClassMinValue) && (d_split < splitClassMaxValue))
					valueInSplit = true;
			}
		}
		else
		{ // split type is string or enum
			if (splitValueString.equals(
				this.splitOptions.getSplitClassLabel(splitClass, this.tableModel)))
			{
				valueInSplit = true;
			}
		}
		
		return valueInSplit;
	}

	/**
	 * Get the minimum value of column columnIndex for the given split class.
	 * If there is no split, the minimum value of column columnIndex
	 * for the complete data set is returned.
	 * If forSelection is true, the minimum value is determined for the 
	 * selected values within the given split class.
	 * 
	 * @param columnIndex
	 *            The column index
	 * @param splitClass
	 *            The split class
	 * @param forSelection
	 *            Only look at the selection y/n
	 * @return 
	 * 		The minimum value of column columnIndex for the given split class.
	 * 		Returns 0 if column is not numerical. 
	 */
	public double getColumnMin(int columnIndex, int splitClass, boolean forSelection)
	{
		double min;
		AllowedTypes type = this.tableModel.getColumnTypes().get(columnIndex).getType();
		int splitColumnIndex = this.splitOptions.getColumnSplitIndex();
		
		if (!(type.equals(AllowedTypes.DOUBLE) 
			|| type.equals(AllowedTypes.INTEGER)))
		{
			// type is not numerical
			min = 0;
		}
		else if (splitColumnIndex == -1)
		{
			// there is no split
			if (forSelection)
				min = this.tableModel.getColumnMinOfSelection(columnIndex);
			else
				min = this.tableModel.getColumnMin(columnIndex);
		}
		else
		{
			// there is a split
			AllowedTypes splitType = this.tableModel.getColumnTypes().get(splitColumnIndex).getType();	
			Double minDouble = Double.MAX_VALUE;
			boolean valueInSplit = false;
			String valueString, splitValueString;

			for (int i = 0; i < this.tableModel.getRowCount(); i++)
			{
				if ((forSelection && this.tableModel.getSelectionList().get(i))
					|| (!forSelection))
				{
					valueString = (String) this.tableModel.getValueAt(i, columnIndex);
					splitValueString = (String) this.tableModel.getValueAt(i, splitColumnIndex);
	
					if (!valueString.equals(ColumnType.WILDCARD))
					{
						valueInSplit = this.isValueInSplit(splitValueString, splitType, splitClass);
						
						if (valueInSplit)
						{
							Double d = Double.parseDouble(valueString);
							if (d < minDouble)
							{
								minDouble = d;
							}
						}
					}
				}
			}
			
			if (minDouble.equals(Double.MAX_VALUE))
			{
				// no minimum found
				min = 0;
			}
			else
			{
				min = minDouble.doubleValue();
			}
		} // there is a split
		
		return min;
	}

	/**
	 * Get the maximum value of column columnIndex for the given split class.
	 * If there is no split, the maximum value of column columnIndex
	 * for the complete data set is returned.
	 * If forSelection is true, the maximum value is determined for the 
	 * selected values within the given split class.
	 * 
	 * @param columnIndex
	 *            The column index
	 * @param splitClass
	 *            The split class
	 * @param forSelection
	 *            Only look at the selection y/n
	 * @return 
	 * 		The maximum value of column columnIndex for the given split class.
	 * 		Returns 0 if column is not numerical. 
	 */
	public double getColumnMax(int columnIndex, int splitClass, boolean forSelection)
	{
		double max;
		AllowedTypes type = this.tableModel.getColumnTypes().get(columnIndex).getType();
		int splitColumnIndex = this.splitOptions.getColumnSplitIndex();
		
		if (!(type.equals(AllowedTypes.DOUBLE) 
			|| type.equals(AllowedTypes.INTEGER)))
		{
			// type is not numerical
			max = 0;
		}
		else if (splitColumnIndex == -1)
		{
			// there is no split
			if (forSelection)
				max = this.tableModel.getColumnMaxOfSelection(columnIndex);
			else
				max = this.tableModel.getColumnMax(columnIndex);
		}
		else
		{
			// there is a split
			AllowedTypes splitType = this.tableModel.getColumnTypes().get(splitColumnIndex).getType();	
			Double maxDouble = Double.MIN_VALUE;
			boolean valueInSplit = false;
			String valueString, splitValueString;

			for (int i = 0; i < this.tableModel.getRowCount(); i++)
			{
				if ((forSelection && this.tableModel.getSelectionList().get(i))
					|| (!forSelection))
				{
					valueString = (String) this.tableModel.getValueAt(i, columnIndex);
					splitValueString = (String) this.tableModel.getValueAt(i, splitColumnIndex);
	
					if (!valueString.equals(ColumnType.WILDCARD))
					{
						valueInSplit = this.isValueInSplit(splitValueString, splitType, splitClass);
						
						if (valueInSplit)
						{
							Double d = Double.parseDouble(valueString);
							if (d > maxDouble)
							{
								maxDouble = d;
							}
						}
					}
				}
			}
			
			if (maxDouble.equals(Double.MIN_VALUE))
			{
				// no maximum found
				max = 0;
			}
			else
			{
				max = maxDouble.doubleValue();
			}
		} // there is a split
		
		return max;
	}

	/**
	 * Get the mean value of column columnIndex for the given split class.
	 * If there is no split, the mean value of column columnIndex
	 * for the complete data set is returned.
	 * If forSelection is true, the mean value is determined for the 
	 * selected values within the given split class.
	 * 
	 * @param columnIndex
	 *            The column index
	 * @param splitClass
	 *            The split class
	 * @param forSelection
	 *            Only look at the selection y/n
	 * @return 
	 * 		The mean value of column columnIndex for the given split class.
	 * 		Returns 0 if column is not numerical or if the mean cannot be calculated.
	 */
	public double getColumnMean(int columnIndex, int splitClass, boolean forSelection)
	{
		double mean;
		AllowedTypes type = this.tableModel.getColumnTypes().get(columnIndex).getType();
		int splitColumnIndex = this.splitOptions.getColumnSplitIndex();
		
		if (!(type.equals(AllowedTypes.DOUBLE) 
			|| type.equals(AllowedTypes.INTEGER)))
		{
			// type is not numerical
			mean = 0;
		}
		else if (splitColumnIndex == -1)
		{
			// there is no split
			if (forSelection)
				mean = this.tableModel.getColumnMeanOfSelection(columnIndex);
			else
				mean = this.tableModel.getColumnMean(columnIndex);
		}
		else
		{
			// there is a split
			AllowedTypes splitType = this.tableModel.getColumnTypes().get(splitColumnIndex).getType();	
			boolean valueInSplit = false;
			String valueString, splitValueString;

			Double sum = 0.0;
			int count = 0; // number of valid values
			for (int i = 0; i < this.tableModel.getRowCount(); i++)
			{
				if ((forSelection && this.tableModel.getSelectionList().get(i))
					|| (!forSelection))
				{
					valueString = (String) this.tableModel.getValueAt(i, columnIndex);
					splitValueString = (String) this.tableModel.getValueAt(i, splitColumnIndex);
	
					if (!valueString.equals(ColumnType.WILDCARD))
					{
						valueInSplit = this.isValueInSplit(splitValueString, splitType, splitClass);
						
						if (valueInSplit)
						{
							Double d = Double.parseDouble(valueString);
							sum += d;
							count++;
						}
					}
				}
			}
			
			if (count > 0)
				mean = sum/count;
			else
				mean = 0;
		} // there is a split
		
		return mean;
	}

	/**
	 * Get the standard deviation of column columnIndex, excluding missing values,
	 * for the given split class.
	 * If there is no split, the standard deviation of column columnIndex
	 * for the complete data set is returned.
	 * If forSelection is true, the standard deviation is determined for the 
	 * selected values within the given split class.
	 * 
	 * @param columnIndex
	 *            The column index
	 * @param splitClass
	 *            The split class
	 * @param forSelection
	 *            Only look at the selection y/n
	 * @return 
	 * 		The standard deviation of column columnIndex for the given split class. 
	 * 		Returns 0 if column is not numerical or if the standard deviation cannot be calculated.
	 */
	public double getColumnSD(int columnIndex, int splitClass, boolean forSelection)
	{
		double sd;
		AllowedTypes type = this.tableModel.getColumnTypes().get(columnIndex).getType();
		int splitColumnIndex = this.splitOptions.getColumnSplitIndex();
		
		if (!(type.equals(AllowedTypes.DOUBLE) 
			|| type.equals(AllowedTypes.INTEGER)))
		{
			// type is not numerical
			sd = 0;
		}
		else if (splitColumnIndex == -1)
		{
			// there is no split
			sd = this.tableModel.getColumnSD(columnIndex);
		}
		else
		{
			// there is a split
			AllowedTypes splitType = this.tableModel.getColumnTypes().get(splitColumnIndex).getType();	
			boolean valueInSplit = false;
			String valueString, splitValueString;

			Double sum = 0.0;
			int count = 0; // number of valid values
			double mean;
			
			mean = this.getColumnMean(columnIndex, splitClass, forSelection);
			for (int i = 0; i < this.tableModel.getRowCount(); i++)
			{
				if ((forSelection && this.tableModel.getSelectionList().get(i))
					|| (!forSelection))
				{
					valueString = (String) this.tableModel.getValueAt(i, columnIndex);
					splitValueString = (String) this.tableModel.getValueAt(i, splitColumnIndex);
	
					if (!valueString.equals(ColumnType.WILDCARD))
					{
						valueInSplit = this.isValueInSplit(splitValueString, splitType, splitClass);
						
						if (valueInSplit)
						{
							Double d = Double.parseDouble(valueString);
							sum += Math.pow(d - mean, 2);
							count++;
						}
					}
				}
			}
			
			if (count > 0)
				sd = Math.sqrt(sum/count);
			else
				sd = 0;
		} // there is a split
		
		return sd;
	}

	/**
	 * Get the median value of column columnIndex, excluding missing values,
	 * for the given split class.
	 * If there is no split, the standard deviation of column columnIndex
	 * for the complete data set is returned.
	 * If forSelection is true, the median value is determined for the 
	 * selected values within the given split class.
	 * 
	 * @param columnIndex
	 *            The column index
	 * @param splitClass
	 *            The split class
	 * @param forSelection
	 *            Only look at the selection y/n
	 * @return 
	 * 		The median value of column columnIndex for the given split class. 
	 * 		Returns 0 if column is not numerical or if the median value cannot be calculated.
	 */
	public double getColumnMedian(int columnIndex, int splitClass, boolean forSelection)
	{
		double median;
		AllowedTypes type = this.tableModel.getColumnTypes().get(columnIndex).getType();
		int splitColumnIndex = this.splitOptions.getColumnSplitIndex();
		
		if (!(type.equals(AllowedTypes.DOUBLE) 
			|| type.equals(AllowedTypes.INTEGER)))
		{
			// type is not numerical
			median = 0;
		}
		else if (splitColumnIndex == -1)
		{
			// there is no split
			if (forSelection)
				median = this.tableModel.getColumnMedianOfSelection(columnIndex);
			else
				median = this.tableModel.getColumnMedian(columnIndex);
		}
		else
		{
			// there is a split and column type is numerical
			AllowedTypes splitType = this.tableModel.getColumnTypes().get(splitColumnIndex).getType();
			boolean valueInSplit = false;
			String valueString, splitValueString;
			
			ArrayList<Double> data = new ArrayList<Double>();
	
			for (int i = 0; i < this.tableModel.getRowCount(); i++)
			{
				if ((forSelection && this.tableModel.getSelectionList().get(i))
					|| (!forSelection))
				{
					valueString = (String) this.tableModel.getValueAt(i, columnIndex);
					splitValueString = (String) this.tableModel.getValueAt(i, splitColumnIndex);
		
					if (!valueString.equals(ColumnType.WILDCARD))
					{
						valueInSplit = this.isValueInSplit(splitValueString, splitType, splitClass);
						
						if (valueInSplit)
						{
							// get the value
							Double d = Double.parseDouble(valueString);
	
							// add the value to a list based on the splitclass
							data.add(d);
						}
					}
				}
			} // for loop over all data rows

			Collections.sort(data);
			int size = data.size();
			int index;

			if ((size == 0) ||(size == 1))
			{
				median = 0;
			}
			else if (size % 2 == 0)
			{
				// even number of values in data set
				index = (size/2) - 1;
				// mediaan is het gemiddelde van de twee waarden in het midden
				median = (data.get(index) + data.get(index + 1))/2;
			}
			else
			{
				// odd number of values in data set
				index = (int) ((size + 1)/2) - 1;
				// mediaan is de middelste waarde
				median = data.get(index);
			}
		} // there is a split
		
		return median;
	}

}
