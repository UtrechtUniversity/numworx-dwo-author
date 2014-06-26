package fi.statistiek.descriptives;

import java.util.ArrayList;
import java.util.Observable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import fi.statistiek.SelectionListener;
import fi.statistiek.SplitOptions;
import fi.statistiek.StatTableModel;
// use some features from histogram
import fi.statistiek.histogram.HistogramModel.FrequencyTuple;

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
}
