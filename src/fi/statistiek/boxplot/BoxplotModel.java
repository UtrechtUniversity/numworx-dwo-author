package fi.statistiek.boxplot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import fi.statistiek.SplitOptions;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.types.ColumnType;

/**
 * MVC Model for statistiekview Boxplot
 * 
 * @author Manu Drijvers
 *
 */
public class BoxplotModel extends Observable implements TableModelListener {
	private StatTableModel tableModel;
	
	private int columnIndex;
	//private int columnSplitIndex;
	//private ArrayList<Double> splitBinBoundaries;
	private SplitOptions splitOptions;
	
	private ArrayList<Double> minValues;
	private ArrayList<Double> lowerQuartiles;
	private ArrayList<Double> medians;
	private ArrayList<Double> upperQuartiles;
	private ArrayList<Double> maxValues;	
	
	private Double dataMinValue;
	private Double dataMaxValue;
	
	private boolean verticalBoxplots;
	
	private String viewName;
	
	/**
	 * Constructor
	 * @param tableModel the tablemodel
	 * @param viewName the name of the view
	 */
	public BoxplotModel(StatTableModel tableModel, String viewName) {
		this.viewName = viewName;
		
		this.tableModel = tableModel;
		this.tableModel.addTableModelListener(this);
		
		this.columnIndex = -1;
		this.splitOptions = new SplitOptions();
		
		
		this.verticalBoxplots = false;
	}
	
	
	/**
	 * @return true if using verticalboxplots, false if using horizontal boxplots
	 */
	public boolean isVerticalBoxplots() {
		return verticalBoxplots;
	}


	/**
	 * Set the orientation of the boxplots
	 * @param verticalBoxplots the new orientation, true for vertical, false for horizontal
	 */
	public void setVerticalBoxplots(boolean verticalBoxplots) {
		if(this.verticalBoxplots != verticalBoxplots) {
			this.verticalBoxplots = verticalBoxplots;
			this.changed();
		}
	}


	/**
	 * Get the lowest value in the data
	 * @return the lowest value in the data
	 */
	public Double getDataMinValue() {
		return dataMinValue;
	}

	/**
	 * Get the highest value in the data
	 * @return the highest value in the data
	 */
	public Double getDataMaxValue() {
		return dataMaxValue;
	}

	/**
	 * Get the splitoptions
	 * @return the splitoptions
	 */
	public SplitOptions getSplitOptions() {
		return this.splitOptions;
	}
	
	/**
	 * Set the splitoptions
	 * @param splitOptions the new splitoptions
	 */
	public void setSplitOptions(SplitOptions splitOptions) {
		this.splitOptions = splitOptions;
		this.setPercentileValues();
		this.changed();
	}
	
	/**
	 * Get the name of this view
	 * @return the name of this view
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * set the name of this view
	 * @param viewName the new name of this view
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
		this.changed();
	}

	private void changed() {
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Get the tablemodel
	 * @return the tablemodel
	 */
	public StatTableModel getTableModel() {
		return this.tableModel;
	}

	/**
	 * Get the index of the column represented by this boxplot
	 * @return the index of the column represented by this boxplot
	 */
	public int getColumnIndex() {
		return columnIndex;
	}

	/**
	 * Set the column that is represented by this boxplot
	 * @param columnIndex the index of the column 
	 */
	public void setColumnIndex(int columnIndex) {
		System.out.println("Setting column from " + this.columnIndex + " to " + columnIndex);
		this.columnIndex = columnIndex;
		this.setPercentileValues();
		this.changed();
	}

	/**
	 * Get the index of the column by which the data is split
	 * @return the index of the column by which the data is split
	 */
	public int getColumnSplitIndex() {
		return this.splitOptions.getColumnSplitIndex();
	}
	
	public void setColumnSplitIndex(int columnSplitIndex) {
		this.splitOptions.setColumnSplitIndex(columnSplitIndex);
		this.setPercentileValues();
		this.changed();
	}

	public ArrayList<Double> getSplitBinBoundaries() {
		return this.splitOptions.getBinBoundaries();
	}
	
	public void setSplitBinBoundaries(ArrayList<Double> splitBoundaries) {
		this.splitOptions.setBinBoundaries(splitBoundaries);
		this.setPercentileValues();
		this.changed();
	}

	public Double getMinValue(int bin) {
		return minValues.get(bin);
	}
	
	public Double getLowerQuartile(int bin) {
		return lowerQuartiles.get(bin);
	}

	public Double getMedian(int bin) {
		return medians.get(bin);
	}

	public Double getUpperQuartile(int bin) {
		return upperQuartiles.get(bin);
	}

	public Double getMaxValue(int bin) {
		return maxValues.get(bin);
	}
	
	public int getSplitClasses() {
		return this.tableModel.splitVarClasses(this.splitOptions);
	}
	
	/**
	 * Calculates the percentile values
	 */
	public void setPercentileValues() {
		if(!(this.getTableModel().isColumnIndexValid(this.columnIndex))) {
			return;
		}
		
		if(!this.getTableModel().isColumnIndexValid(this.splitOptions.getColumnSplitIndex())) {
			ArrayList<Double> data = new ArrayList<Double>();
			
			for(int i = 0; i < this.tableModel.getRowCount(); i++) {
				String valueString = (String)this.tableModel.getValueAt(i, columnIndex);
				if(!valueString.equals(ColumnType.WILDCARD)) {
					//get the value
					Double d = Double.parseDouble(valueString);
					
					//add the value to a list based on the spitclass
					data.add(d);
				}
			}
			
			this.minValues = new ArrayList<Double>();
			this.lowerQuartiles = new ArrayList<Double>();
			this.medians = new ArrayList<Double>();
			this.upperQuartiles = new ArrayList<Double>();
			this.maxValues = new ArrayList<Double>();
			
			Collections.sort(data);
			int size = data.size();
			if(size == 0) {
				this.minValues.add(null);
				this.lowerQuartiles.add(null);
				this.medians.add(null);
				this.upperQuartiles.add(null);
				this.maxValues.add(null);
			}
			else {	
				this.minValues.add(data.get(0));
				this.lowerQuartiles.add(data.get((int)Math.ceil(0.25*size)-1));
				this.medians.add(data.get((int)Math.ceil(0.5*size)-1));
				this.upperQuartiles.add(data.get((int)Math.ceil(0.75*size)-1));
				this.maxValues.add(data.get(size-1));
			}
			this.dataMinValue = this.getMinValue(0);
			this.dataMaxValue = this.getMaxValue(0);
		}
		else {
		
		
			int splitClasses = this.getSplitClasses();
			
			ArrayList<ArrayList<Double>> sortedData = new ArrayList<ArrayList<Double>>();
			for(int i = 0; i < splitClasses; i++) {
				sortedData.add(new ArrayList<Double>());
			}
			
			for(int i = 0; i < this.tableModel.getRowCount(); i++) {
				String valueString = (String)this.tableModel.getValueAt(i, columnIndex);
				String valueSplitString = (String)this.tableModel.getValueAt(i, this.splitOptions.getColumnSplitIndex());
				if(!valueSplitString.equals(ColumnType.WILDCARD) && !valueString.equals(ColumnType.WILDCARD)) {
					//get the value
					Double d = Double.parseDouble(valueString);
					
					//add the value to a list based on the spitclass
					sortedData.get(this.tableModel.classifyObject(valueSplitString, this.splitOptions.getColumnSplitIndex(), this.splitOptions.getBinBoundaries())).add(d);
				}
			}
			
			this.minValues = new ArrayList<Double>();
			this.lowerQuartiles = new ArrayList<Double>();
			this.medians = new ArrayList<Double>();
			this.upperQuartiles = new ArrayList<Double>();
			this.maxValues = new ArrayList<Double>();
			
			for(int i = 0; i < splitClasses; i++) {
				Collections.sort(sortedData.get(i));
				int size = sortedData.get(i).size();
				if(size == 0) {
					this.minValues.add(null);
					this.lowerQuartiles.add(null);
					this.medians.add(null);
					this.upperQuartiles.add(null);
					this.maxValues.add(null);
				}
				else {	
					this.minValues.add(sortedData.get(i).get(0));
					this.lowerQuartiles.add(sortedData.get(i).get((int)Math.ceil(0.25*size)-1));
					this.medians.add(sortedData.get(i).get((int)Math.ceil(0.5*size)-1));
					this.upperQuartiles.add(sortedData.get(i).get((int)Math.ceil(0.75*size)-1));
					this.maxValues.add(sortedData.get(i).get(size-1));
				}
			}
			
			if(splitClasses > 0) {
				this.dataMinValue = this.getMinValue(0);
				this.dataMaxValue = this.getMaxValue(0);
				
				for(int i = 1; i < splitClasses; i++) {
					if(this.getMinValue(i) != null && (this.dataMinValue == null || this.getMinValue(i) < this.dataMinValue)) {
						this.dataMinValue = this.getMinValue(i);
					}
					if(this.getMaxValue(i) != null && (this.dataMaxValue == null || this.getMaxValue(i) > this.dataMaxValue)) {
						this.dataMaxValue = this.getMaxValue(i);
					}
				}
			}
		}
	}

	public void tableChanged(TableModelEvent arg0) {
		this.setPercentileValues();
		this.changed();		
	}
	
}
