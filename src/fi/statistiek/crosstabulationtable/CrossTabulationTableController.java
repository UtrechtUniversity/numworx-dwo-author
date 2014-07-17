package fi.statistiek.crosstabulationtable;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComponent;

import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.StatistiekView;
import fi.statistiek.types.AllowedTypes;

/**
 * MVC Controller for StatistiekView CrossTabulationTable
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class CrossTabulationTableController implements StatistiekView,
	ActionListener, FocusListener
{

	private CrossTabulationTableModel model;
	private CrossTabulationTableView view;

	/**
	 * Constructor
	 * 
	 * @param tableModel
	 *            the data model
	 * @param viewName
	 *            the initial name of the view
	 */
	public CrossTabulationTableController(StatTableModel tableModel, String viewName,
		int startVarRows, int startVarColumns)
	{
		this.model = new CrossTabulationTableModel(tableModel, viewName);
		this.model.setColumnIndex(startVarRows);
		//setSplit(startVarColumns);
		this.view = new CrossTabulationTableView(this.model, this);
		this.view.update(null, null);
	}
	
	/**
	 * Set the split variable, i.e., the variable for the columns in the crosstab table.
	 * @param index
	 */
	public void setSplit(int index)
	{
		this.model.setColumnSplitIndex(index);
		this.model.setSplitOptions(this.model.getSplitOptions());
		if (index > -1)
		{
			this.setSplitType(this.model
				.getTableModel()
				.getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex())
				.getType(), CrossTabulationTableModel.DEFAULT_NUMBER_OF_BINS);//index);
		}
	}

	/**
	 * 
	 * @param type
	 * @param noBins
	 */
	private void setSplitType(AllowedTypes type, int noBins)
	{
		if (type.isNumber())
		{
			ArrayList<Double> boundaries = new ArrayList<Double>();
			boundaries = Statistiek.appropriateBoundaries(
				this.model.getTableModel().getColumnMin(
					this.model.getSplitOptions().getColumnSplitIndex()),
				this.model.getTableModel().getColumnMax(
					this.model.getSplitOptions().getColumnSplitIndex()),
				noBins);
			
			// test syl: niet fraai; opnieuw boundaries berekenen met de hierboven berekende binwidth
			// TODO appropriateBoundaries(min, max) implementeren die binwidth en het aantal klassen bepaalt 
			boundaries = Statistiek.appropriateBoundariesFromBinSettings(this.model.getTableModel().getColumnMin(
					this.model.getSplitOptions().getColumnSplitIndex()),
				this.model.getTableModel().getColumnMax(
					this.model.getSplitOptions().getColumnSplitIndex()), 
					boundaries.get(1) - boundaries.get(0), boundaries.get(0));

			this.model.setSplitBoundaries(boundaries);
			this.model.setSplitOptions(this.model.getSplitOptions());
			this.view.setModel(this.model);
		}
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		String action = arg0.getActionCommand();
		
//		System.out.println("CrossTabulationTableController.actionPerformed(): action = "
//			+ action);

		if (action.equals("rowIndexBox"))
		{
			this.model.setColumnIndex(this.view.varRowsBoxSelectedIndex());
		}
		else if (action.equals("columnIndexBox"))
		{
			this.setSplit(this.view.varColumnsBoxSelectedIndex());
		}
		else if (action.equals("minBoundaryRows"))
		{
			updateBoundariesFromRowsBinSettings();
		}
		else if (action.equals("binWidthRows"))
		{
			updateBoundariesFromRowsBinSettings();
		}
		else if (action.equals("minBoundaryColumns"))
		{
			updateBoundariesFromRowsBinSettings();
		}
		else if (action.equals("binWidthColumns"))
		{
			updateBoundariesFromRowsBinSettings();
		}
	}

	public void setUp(Frame owner)
	{
		// TODO Auto-generated method stub

	}

	public void setUp(Dialog owner)
	{
		// TODO Auto-generated method stub

	}

	public JComponent getComponent()
	{
		return this.view;
	}

	public String getViewType()
	{
		return "Kruistabel";
	}

	public Object getState()
	{
		Hashtable h = new Hashtable();

		h.put("viewName", this.getViewName());
		h.put("columnIndex", this.model.getColumnIndex());
		h.put("binBoundaries", this.model.getBinBoundaries());
		h.put("columnSplitIndex", this.model.getSplitOptions()
			.getColumnSplitIndex());
		h.put("splitBoundaries", this.model.getSplitOptions()
			.getBinBoundaries());
		h.put("showPercentage", this.model.isShowPercentage());
		h.put("showPercentage_endTotal", this.model.isShowPercentage_endTotal());
		h.put("showPercentage_rowTotal", this.model.isShowPercentage_rowTotal());
		h.put("showPercentage_columnTotal", this.model.isShowPercentage_columnTotal());

		return h;
	}

	public void setState(Object state)
	{
		Hashtable h = (Hashtable) state;
		
		// deep copy waarschijnlijk niet nodig...
		//Hashtable h = Copy.deepCopy((Hashtable) state);

		if (h.containsKey("viewName"))
		{
			this.setViewName((String) h.get("viewName"));
		}
		if (h.containsKey("columnIndex"))
		{
			// Let op: setColumnIndex() zet ook de binBoundaries
			// Dat wordt hieronder goed gemaakt als de binBoundaries
			// uit de hashtable worden gezet.
			this.model.setColumnIndex((Integer) h.get("columnIndex"));
		}
		if (h.containsKey("binBoundaries"))
		{
			this.model.setBinBoundaries((ArrayList<Double>) h
				.get("binBoundaries"));
		}
		if (h.containsKey("columnSplitIndex"))
		{
			this.model.setColumnSplitIndex((Integer) h.get("columnSplitIndex"));
		}
		if (h.containsKey("splitBoundaries"))
		{
			this.model.setSplitBoundaries((ArrayList<Double>) h
				.get("splitBoundaries"));
		}
		if (h.containsKey("showPercentage"))
		{
			this.model.setShowPercentage((Boolean) h.get("showPercentage"));
		}
		if (h.containsKey("showPercentage_endTotal"))
		{
			this.model.setShowPercentage_endTotal((Boolean) h.get("showPercentage_endTotal"));
		}
		if (h.containsKey("showPercentage_rowTotal"))
		{
			this.model.setShowPercentage_rowTotal((Boolean) h.get("showPercentage_rowTotal"));
		}
		if (h.containsKey("showPercentage_columnTotal"))
		{
			this.model.setShowPercentage_columnTotal((Boolean) h.get("showPercentage_columnTotal"));
		}
	}

	public String getViewName()
	{
		return this.model.getViewName();
	}

	public void setViewName(String s)
	{
		this.model.setViewName(s);
	}

	public void focusGained(FocusEvent arg0)
	{
		// TODO Auto-generated method stub
	}

	public void focusLost(FocusEvent arg0)
	{
		// source is one of the rows variable bin settings
		if ((arg0.getSource() == this.view.getBinWidthRowsField())
			|| (arg0.getSource() == this.view.getMinBoundaryRowsField()))
		{
			updateBoundariesFromRowsBinSettings();
		}
		// source is one of the columns variable bin settings
		else if ((arg0.getSource() == this.view.getBinWidthColumnsField())
			|| (arg0.getSource() == this.view.getMinBoundaryColumnsField()))
		{
			updateBoundariesFromColumnsBinSettings();
		}
	}

	/**
	 * Update the bin boundaries for the rows variable
	 * using the settings for the minimum boundary
	 * and the bin width,
	 * and determine the number of bins.
	 */
	private void updateBoundariesFromRowsBinSettings()
	{
		ArrayList<Double> boundaries = new ArrayList<Double>();
		
		boundaries = Statistiek.appropriateBoundariesFromBinSettings(
			this.model.getTableModel().getColumnMin(
				this.model.getColumnIndex()),
			this.model.getTableModel().getColumnMax(
				this.model.getColumnIndex()),
			view.getBinWidthRows(),
			view.getMinBoundaryRows());
		
		this.model.setBinBoundaries(boundaries);
	}
	
	/**
	 * Update the bin boundaries for the columns variable
	 * using the settings for the minimum boundary
	 * and the bin width,
	 * and determine the number of bins.
	 */
	private void updateBoundariesFromColumnsBinSettings()
	{
		ArrayList<Double> boundaries = new ArrayList<Double>();
		
		boundaries = Statistiek.appropriateBoundariesFromBinSettings(
			this.model.getTableModel().getColumnMin(
				this.model.getColumnSplitIndex()),
			this.model.getTableModel().getColumnMax(
				this.model.getColumnSplitIndex()),
			this.view.getBinWidthColumns(),
			this.view.getMinBoundaryColumns());
		
		this.model.setSplitBoundaries(boundaries);
	}
}
