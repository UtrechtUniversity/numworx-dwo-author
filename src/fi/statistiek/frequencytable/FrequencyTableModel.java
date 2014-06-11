package fi.statistiek.frequencytable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import fi.statistiek.SelectionListener;
import fi.statistiek.SplitOptions;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.histogram.HistogramModel;
import fi.statistiek.histogram.HistogramModel.FrequencyTuple;
import fi.statistiek.histogram.StatBinsModel;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * MVC model for StatistiekView FrequencyTable
 * 
 * @author Manu Drijvers
 * 
 */
public class FrequencyTableModel extends Observable implements
	TableModelListener, SelectionListener, StatBinsModel
{
	private int columnIndex;
	private boolean showPercentage;
	private boolean showFreqCumulative;

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
	public FrequencyTableModel(StatTableModel tableModel, String viewName)
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

		this.showPercentage = true;
		this.showFreqCumulative = true;
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
		this.setChanged();
		this.notifyObservers();
	}

	boolean isShowPercentage()
	{
		return this.showPercentage;
	}

	void setShowPercentage(boolean b)
	{
		this.showPercentage = b;
		this.setChanged();
		this.notifyObservers();
	}

	boolean isShowCumulative()
	{
		return this.showFreqCumulative;
	}

	void setShowCumulative(boolean b)
	{
		this.showFreqCumulative = b;
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Initialize the number of bins without updating other fields.
	 * @param noBins
	 */
	public void initNoBins(int noBins)
	{
		this.noBins = noBins;
	}

	/**
	 * Set the number of bins and update bin boundaries.
	 * 
	 * @param noBins
	 *            the new number of bins
	 */
	public void setNoBins(int noBins)
	{
		this.noBins = noBins;

		// set appropriate boundaries
		if (this.tableModel.getRowCount() > 0 && this.columnIndexValid())
		{
			double min = this.tableModel.getColumnMin(this.columnIndex);
			double max = this.tableModel.getColumnMax(this.columnIndex);
			this.binBoundaries = Statistiek.appropriateBoundaries(min, max,
				noBins);
		}

		this.setChanged();
		this.notifyObservers();
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
			this.setChanged();
			this.notifyObservers();
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
			this.setChanged();
			this.notifyObservers();
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
	 * Set the column of the datatable that this StatistiekView will show
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
					this.noBins);
				
				// test syl: kan mooier, maar het werkt wel: opnieuw berekenen met de berekende binboundaries
				// TODO appropriateBoundaries(min, max) implementeren die binwidth en het aantal klassen bepaalt 
				this.binBoundaries = Statistiek.appropriateBoundariesFromBinSettings(this.tableModel.getColumnMin(this.columnIndex),
					this.tableModel.getColumnMax(this.columnIndex), 
					this.binBoundaries.get(1) - this.binBoundaries.get(0), this.binBoundaries.get(0));
				this.noBins = this.binBoundaries.size() - 1;
			}
			this.setChanged();
			this.notifyObservers();
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
	 * this bin Only use for columns of type integer or double
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
	 * Find the frequency of every class Only use for columns of type enum of
	 * string
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
		this.setChanged();
		this.notifyObservers();
	}

	public void selectionChanged()
	{
		this.setChanged();
		this.notifyObservers();
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

	public void setSplitBoundaries(ArrayList<Double> boundaries)
	{
		this.splitOptions.setBinBoundaries(boundaries);
		this.changed();
	}

}
