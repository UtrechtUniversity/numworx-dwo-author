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
	private StatTableModel statTableModel;
	private int columnXIndex;
	private int columnYIndex;
	private final boolean scatterplotMode;

	private SplitOptions splitOptions;

	/**
	 * Whether or not the scale of variable X is optimized. Only available in dotplot,
	 * not in scatterplot.
	 */
	private boolean optimizeScaleX;
	/**
	 * The minimum value of columnX that is used on the scale of the dotplot.
	 */
	private double minXOnScale;
	/**
	 * The maximum value of columnX that is used on the scale of the dotplot.
	 */
	private double maxXOnScale;

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
		this.statTableModel = tableModel;
		this.statTableModel.addTableModelListener(this);
		this.statTableModel.addSelectionListener(this);

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

		ArrayList<Number> boundaries = new ArrayList<Number>();
		// by default 2 split bins
		boundaries.add(0.0);
		boundaries.add(50.0);
		boundaries.add(100.0);
		this.splitOptions.setBinBoundaries(boundaries);
		this.scatterplotMode = scatterplotMode;
		this.optimizeScaleX = true;
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
		if (!(this.statTableModel == tableModel))
		{
			this.statTableModel.removeTableModelListener(this);
			this.statTableModel = tableModel;
			this.statTableModel.addTableModelListener(this);
			this.statTableModel.addSelectionListener(this);
			this.changed();
		}
	}

	/**
	 * @return The data table
	 */
	public StatTableModel getStatTableModel()
	{
		return this.statTableModel;
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
	public void setUseColorScale(boolean b)
	{
		this.useColorScale = b;
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
			&& this.columnXIndex < this.statTableModel.getColumnCount();
	}

	/**
	 * Tests if the variable index for column Y is valid
	 * 
	 * @return true if index is valid
	 */
	public boolean columnYIndexValid()
	{
		return this.columnYIndex >= 0
			&& this.columnYIndex < this.statTableModel.getColumnCount();
	}

	/**
	 * Tests if the variable index for column Z is valid
	 * 
	 * @return true if index is valid
	 */
	public boolean columnZIndexValid()
	{
		return this.columnColorIndex >= 0
			&& this.columnColorIndex < this.statTableModel.getColumnCount();
	}

	/**
	 * Tests if the variable index for the color scale is valid
	 * 
	 * @return true if index is valid
	 */
	public boolean columnColorIndexValid()
	{
		return this.columnColorIndex >= 0
			&& this.columnColorIndex < this.statTableModel.getColumnCount();
	}

	public boolean columnSplitIndexValid()
	{
		return this.splitOptions.getColumnSplitIndex() >= 0
			&& this.splitOptions.getColumnSplitIndex() < this.statTableModel
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

	public ArrayList<Number> getSplitBinBoundaries()
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

	public void outliersChanged()
	{
		this.changed();
	}
	
	public void setNoSplitBins(int noBins)
	{
		this.splitOptions
			.setBinBoundaries(Statistiek.appropriateBoundaries(this.statTableModel
				.getColumnMin(this.splitOptions.getColumnSplitIndex()),
				this.statTableModel.getColumnMax(this.splitOptions
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
	
	public void setSplitBoundaries(ArrayList<Number> boundaries)
	{
		this.splitOptions.setBinBoundaries(boundaries);
		this.changed();
	}

	/**
	 * Get color A in string format, for example "rgb(255,0,0)".
	 * 
	 * @return
	 */
	public Object getColorAString()
	{
		String colorAString = "rgb(";
		
		colorAString = colorAString + this.colorA.getRed()
			+ "," + this.colorA.getGreen()
			+ "," + this.colorA.getBlue() + ")";
		
		return colorAString;
	}
	
	/**
	 * Get color B in string format, for example "rgb(255,0,0)".
	 * 
	 * @return
	 */
	public Object getColorBString()
	{
		String colorBString = "rgb(";
		
		colorBString = colorBString + this.colorB.getRed()
			+ "," + this.colorB.getGreen()
			+ "," + this.colorB.getBlue() + ")";
		
		return colorBString;
	}
	
	/**
	 * Get the minimum value of columnX on the scale.
	 * 
	 * @return
	 */
	public double getMinXOnScale()
	{
		return this.minXOnScale;
	}
	
	/**
	 * Set the minimum value of columnX on the scale.
	 * 
	 * @param min
	 *            the new minimum value
	 */
	public void setMinXOnScale(double min)
	{
		if (min != this.minXOnScale)
		{
			this.minXOnScale = min;
			this.changed();
		}
	}

	/**
	 * Set the minimum value of columnX on the scale without triggering events.
	 * 
	 * @param min
	 *            the new minimum value
	 */
	public void setMinXOnScaleWithoutEvent(double min)
	{
		if (min != this.minXOnScale)
		{
			this.minXOnScale = min;
		}
	}

	/**
	 * Initialize the minimum value of columnX on the scale.
	 * 
	 */
	public void initializeMinXOnScale()
	{
		double minColumnValue = this.getStatTableModel().getColumnMin(
			this.getColumnXIndex());

		this.minXOnScale = minColumnValue;
	}

	/**
	 * Initialize the maximum value of columnX on the scale.
	 * 
	 */
	public void initializeMaxXOnScale()
	{
		double maxColumnValue = this.getStatTableModel().getColumnMax(
			this.getColumnXIndex());

		this.maxXOnScale = maxColumnValue;
	}

	/**
	 * Get the maximum value of columnX on the scale.
	 * 
	 * @return
	 */
	public double getMaxXOnScale()
	{
		return this.maxXOnScale;
	}

	/**
	 * Set the maximum value of columnX on the scale.
	 * 
	 * @param max
	 *            the new maximum value
	 */
	public void setMaxXOnScale(double max)
	{
		if (max != this.maxXOnScale)
		{
			this.maxXOnScale = max;
			this.changed();
		}
	}

	/**
	 * Set the maximum value of columnX on the scale without triggering events.
	 * 
	 * @param max
	 *            the new maximum value
	 */
	public void setMaxXOnScaleWithoutEvent(double max)
	{
		if (max != this.maxXOnScale)
		{
			this.maxXOnScale = max;
		}
	}

	/**
	 * Returns whether the scale of columnX is optimized.
	 * @return
	 */
	public boolean isOptimizeScaleX()
	{
		return optimizeScaleX;
	}

	public void setOptimizeScaleX(boolean b)
	{
		if (this.optimizeScaleX != b)
		{
			this.optimizeScaleX = b;
			this.changed();
		}
	}

	public void setOptimizeScaleXWithoutEvent(boolean b)
	{
		if (this.optimizeScaleX != b)
		{
			this.optimizeScaleX = b;
		}
	}

}
