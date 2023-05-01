package fi.statistiek;

import java.util.ArrayList;

import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * Class describing how data is split
 * 
 * @author Manu Drijvers
 * 
 */
public class SplitOptions
{
	private int columnSplitIndex;
	private ArrayList<Number> binBoundaries;

	/**
	 * Constructor
	 */
	public SplitOptions()
	{
		this.columnSplitIndex = -1;
		this.binBoundaries = new ArrayList<Number>();
		this.binBoundaries.add(0.0);
		this.binBoundaries.add(50.0);
		this.binBoundaries.add(100.0);
	}

	public int getColumnSplitIndex()
	{
		return columnSplitIndex;
	}

	public void setColumnSplitIndex(int columnSplitIndex)
	{
		this.columnSplitIndex = columnSplitIndex;
	}

	public ArrayList<Number> getBinBoundaries()
	{
		return binBoundaries;
	}

	public void setBinBoundaries(ArrayList<Number> boundaries)
	{
		this.binBoundaries = boundaries;
	}

	/**
	 * Gets the description of a split class
	 * 
	 * @param splitClass
	 *            the split class
	 * @param model
	 *            the data model
	 * @return a string containing the description of splitclass
	 */
	public String getSplitClassLabel(int splitClass, StatTableModel model)
	{
		String label;
		ColumnType splitCType = model.getColumnTypes().get(
			this.columnSplitIndex);
		
		if (splitCType.getType().isNumber())
		{
			// use getStringValue() to show the correct decimal separator
			String binValue1 = Statistiek.getStringValue(this.binBoundaries.get(splitClass).doubleValue());
			String binValue2 = Statistiek.getStringValue(this.binBoundaries.get(splitClass + 1).doubleValue());
			label = binValue1 + " -< " + binValue2;
		}
		else if (splitCType.getType().equals(AllowedTypes.ENUM))
		{
			label = splitCType.getEnumOptions()[splitClass];
		}
		else
		{
			label = model.getStringOptions(this.columnSplitIndex)
				.get(splitClass);
		}
		
		return label;
	}

	public SplitOptions clone()
	{
		SplitOptions clone = new SplitOptions();
		clone.setColumnSplitIndex(this.columnSplitIndex);
		ArrayList<Number> cloneBoundaries = new ArrayList<Number>();
		for (Number d : this.binBoundaries)
		{
			cloneBoundaries.add(new Double(d.doubleValue()));
		}
		clone.setBinBoundaries(cloneBoundaries);
		return clone;
	}
}
