package fi.statistiek.dotplot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import fi.statistiek.SelectionListener;
import fi.statistiek.SplitOptions;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;

/**
 * MVC Model for StatistiekView Scatterplot
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class DotplotModel extends Observable implements TableModelListener,
	SelectionListener
{
	private StatTableModel tableModel;
	private int columnXIndex;
	private int columnYIndex;
	private final boolean scatterplotMode;

	private SplitOptions splitOptions;

	private boolean useColorScale;
	private Color colorA;
	private Color colorB;
	private int columnColorIndex;

	// private ArrayList<Double> splitBinBoundaries;
	private boolean splitInSingleView;

	private boolean showCorrelation;

	private String viewName;

	/**
	 * Constructor
	 * 
	 * @param tableModel
	 *            data table
	 * @param viewName
	 *            The initial name of this view
	 */
	public DotplotModel(StatTableModel tableModel, String viewName,
		boolean scatterplotMode)
	{
		this.tableModel = tableModel;
		this.tableModel.addTableModelListener(this);
		this.tableModel.addSelectionListener(this);

		this.viewName = viewName;
		this.showCorrelation = false;
		this.useColorScale = false;

		this.colorA = Color.LIGHT_GRAY;
		this.colorB = Color.BLACK;

		this.columnColorIndex = -1;
		this.columnXIndex = -1;
		this.columnYIndex = -1;
		// this.columnSplitIndex = -1;
		this.splitOptions = new SplitOptions();
		this.splitOptions.setColumnSplitIndex(-1);

		ArrayList<Double> boundaries = new ArrayList<Double>();
		// by default 2 split bins
		boundaries.add(0.0);
		boundaries.add(50.0);
		boundaries.add(100.0);
		this.splitOptions.setBinBoundaries(boundaries);
		this.scatterplotMode = scatterplotMode;
	}

	private void changed()
	{
		this.setChanged();
		this.notifyObservers();
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
	 * @return The data table
	 */
	public StatTableModel getTableModel()
	{
		return this.tableModel;
	}

	/**
	 * @param colorA
	 *            the colorA to set
	 */
	public void setColorA(Color colorA)
	{
		this.colorA = colorA;
		this.changed();
	}

	/**
	 * @return the colorA
	 */
	public Color getColorA()
	{
		return colorA;
	}

	/**
	 * @param columnXIndex
	 *            the columnXIndex to set
	 */
	public void setColumnXIndex(int columnXIndex)
	{
		if (!(this.columnXIndex == columnXIndex))
		{
			this.columnXIndex = columnXIndex;
			this.changed();
		}
	}

	/**
	 * @return the columnXIndex
	 */
	public int getColumnXIndex()
	{
		return columnXIndex;
	}

	/**
	 * @param columnYIndex
	 *            the columnYIndex to set
	 */
	public void setColumnYIndex(int columnYIndex)
	{
		if (!(this.columnYIndex == columnYIndex))
		{
			this.columnYIndex = columnYIndex;
			this.changed();
		}
	}

	/**
	 * @return the columnYIndex
	 */
	public int getColumnYIndex()
	{
		return columnYIndex;
	}

	public void setColumnSplitIndex(int columnSplitIndex)
	{
		if (!(this.splitOptions.getColumnSplitIndex() == columnSplitIndex))
		{
			this.splitOptions.setColumnSplitIndex(columnSplitIndex);
			this.changed();
		}
	}

	public int getColumnSplitIndex()
	{
		return this.splitOptions.getColumnSplitIndex();
	}

	/**
	 * @param useColorScale
	 *            the useColorScale to set
	 */
	public void setUseColorScale(boolean useColorScale)
	{
		this.useColorScale = useColorScale;
		this.changed();
	}

	/**
	 * @return the useColorScale
	 */
	public boolean isUseColorScale()
	{
		return useColorScale;
	}

	/**
	 * @param colorB
	 *            the colorB to set
	 */
	public void setColorB(Color colorB)
	{
		this.colorB = colorB;
		this.changed();
	}

	/**
	 * @return the colorB
	 */
	public Color getColorB()
	{
		return colorB;
	}

	/**
	 * @param columnColorIndex
	 *            the columnColorIndex to set
	 */
	public void setColumnColorIndex(int columnColorIndex)
	{
		if (!(this.columnColorIndex == columnColorIndex))
		{
			this.columnColorIndex = columnColorIndex;
			this.changed();
		}
	}

	/**
	 * @return the columnColorIndex
	 */
	public int getColumnColorIndex()
	{
		return columnColorIndex;
	}

	/**
	 * @param viewName
	 *            the viewName to set
	 */
	public void setViewName(String viewName)
	{
		this.viewName = viewName;
		this.changed();
	}

	/**
	 * @return this StatistiekView's name
	 */
	public String getViewName()
	{
		return this.viewName;
	}

	/**
	 * Tests if the variable index for column X is valid
	 * 
	 * @return true if index is valid
	 */
	public boolean columnXIndexValid()
	{
		return this.columnXIndex >= 0
			&& this.columnXIndex < this.tableModel.getColumnCount();
	}

	/**
	 * Tests if the variable index for column Y is valid
	 * 
	 * @return true if index is valid
	 */
	public boolean columnYIndexValid()
	{
		return this.columnYIndex >= 0
			&& this.columnYIndex < this.tableModel.getColumnCount();
	}

	/**
	 * Tests if the variable index for column Z is valid
	 * 
	 * @return true if index is valid
	 */
	public boolean columnZIndexValid()
	{
		return this.columnColorIndex >= 0
			&& this.columnColorIndex < this.tableModel.getColumnCount();
	}

	/**
	 * Tests if the variable index for the color scale is valid
	 * 
	 * @return true if index is valid
	 */
	public boolean columnColorIndexValid()
	{
		return this.columnColorIndex >= 0
			&& this.columnColorIndex < this.tableModel.getColumnCount();
	}

	public boolean columnSplitIndexValid()
	{
		return this.splitOptions.getColumnSplitIndex() >= 0
			&& this.splitOptions.getColumnSplitIndex() < this.tableModel
				.getColumnCount();
	}

	public boolean splitInSingleView()
	{
		return splitInSingleView;
	}

	public void setSplitInSingleView(boolean b)
	{
		splitInSingleView = b;
		this.changed();
	}

	/**
	 * @return true if currently showing correlation
	 */
	public boolean isShowCorrelation()
	{
		return showCorrelation;
	}

	/**
	 * Set whether the correlation should be shown
	 * 
	 * @param showCorrelation
	 *            true if correlation should be shown
	 */
	public void setShowCorrelation(boolean showCorrelation)
	{
		this.showCorrelation = showCorrelation;
		this.changed();
	}

	public ArrayList<Double> getSplitBinBoundaries()
	{
		return this.splitOptions.getBinBoundaries();
	}

	public void tableChanged(TableModelEvent arg0)
	{
		this.changed();
	}

	public void selectionChanged()
	{
		this.changed();
	}

	public void setNoSplitBins(int noBins)
	{
		this.splitOptions
			.setBinBoundaries(Statistiek.appropriateBoundaries(this.tableModel
				.getColumnMin(this.splitOptions.getColumnSplitIndex()),
				this.tableModel.getColumnMax(this.splitOptions
					.getColumnSplitIndex()), noBins));
		this.changed();
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
	 * Update the column index and the split column index
	 * given that removedColumn has been removed.
	 * @param removedColumn
	 */
	public void updateColumnIndex(int removedColumn)
	{
		// index van de geselecteerde variabele bijwerken
		if (removedColumn < this.columnXIndex)
		{
			setColumnXIndex(this.columnXIndex - 1);
		}
		else if (removedColumn == this.columnXIndex)
		{
			setColumnXIndex(- 1);
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

	public boolean isScatterplotMode()
	{
		return this.scatterplotMode;
	}
	
	public void setSplitBoundaries(ArrayList<Double> boundaries)
	{
		this.splitOptions.setBinBoundaries(boundaries);
		this.changed();
	}
}
