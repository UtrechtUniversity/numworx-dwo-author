package fi.statistiek.crosstabulationtable;

import java.util.ArrayList;
import java.util.Observable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import fi.statistiek.SelectionListener;
import fi.statistiek.SplitOptions;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;

// use some features from histogram
import fi.statistiek.histogram.HistogramModel.FrequencyTuple;
import fi.statistiek.histogram.StatBinsModel;

/**
 * MVC model for StatistiekView CrossTabulationTable
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class CrossTabulationTableModel extends Observable implements
	TableModelListener, SelectionListener, StatBinsModel
{
	private int columnIndex;
	private boolean showPercentage;
	private boolean showPercentage_endTotal;
	private boolean showPercentage_rowTotal;
	private boolean showPercentage_columnTotal;

	private int noBins;
	private ArrayList<Double> binBoundaries;
	
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
	public CrossTabulationTableModel(StatTableModel tableModel, String viewName)
	{
		this.tableModel = tableModel;
		this.tableModel.addTableModelListener(this);
		this.tableModel.addSelectionListener(this);
		
		this.splitOptions = new SplitOptions();

		this.viewName = viewName;

		// set initial values
		this.noBins = 10;
		this.columnIndex = -1;
		this.binBoundaries = new ArrayList<Double>();
		this.binBoundaries.add(new Double(-100));
		this.binBoundaries.add(new Double(100));

		this.showPercentage = false;
		this.showPercentage_endTotal = true;
		this.showPercentage_rowTotal = false;
		this.showPercentage_columnTotal = false;
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
		
		// Make a deep copy, not only copy the reference, since this will cause strange behavior
		// dit lijkt toch niet nodig
//		ArrayList<Double> copy = new ArrayList<Double>(bins.size());
//		for (Double d: bins)
//		{
//			copy.add(new Double(d));
//		}
//		
//		this.binBoundaries = copy; 

		this.noBins = this.binBoundaries.size() - 1;
		this.changed();
	}

	boolean isShowPercentage()
	{
		return this.showPercentage;
	}

	boolean isShowPercentage_endTotal()
	{
		return this.showPercentage_endTotal;
	}

	boolean isShowPercentage_rowTotal()
	{
		return this.showPercentage_rowTotal;
	}

	boolean isShowPercentage_columnTotal()
	{
		return this.showPercentage_columnTotal;
	}

	void setShowPercentage(boolean b)
	{
		this.showPercentage = b;
		this.changed();
	}

	void setShowPercentage_endTotal(boolean b)
	{
		this.showPercentage_endTotal = b;
		this.changed();
	}

	void setShowPercentage_rowTotal(boolean b)
	{
		this.showPercentage_rowTotal = b;
		this.changed();
	}

	void setShowPercentage_columnTotal(boolean b)
	{
		this.showPercentage_columnTotal = b;
		this.changed();
	}

	/**
	 * @return number of bins
	 */
	public int getNoBins()
	{
		return this.noBins;
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
	 * Set the column index, i.e. for the rows, in the crosstabulation table.
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
					10);//this.noBins);
				
				// test syl: niet fraai, maar het werkt wel: opnieuw berekenen met de berekende binboundaries
				// TODO appropriateBoundaries(min, max) implementeren die binwidth en het aantal klassen bepaalt 
				this.binBoundaries = Statistiek.appropriateBoundariesFromBinSettings(this.tableModel.getColumnMin(this.columnIndex),
					this.tableModel.getColumnMax(this.columnIndex), 
					this.binBoundaries.get(1) - this.binBoundaries.get(0), this.binBoundaries.get(0));
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
	 * Determines the bin in which double d is contained. The upper bin boundary 
	 * is exclusive.
	 * @param d
	 * @return
	 */
	public int binOfNumber(double d)
	{
		int bin = 0;
		int i = 0;
		
		while (i < this.binBoundaries.size()
			&& d >= this.binBoundaries.get(i))
		{
			i++;
		}
		
		bin = i-1;

		return bin;
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
		return this.tableModel.enumClassFrequency(this.columnIndex, true, this.splitOptions);
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
}
