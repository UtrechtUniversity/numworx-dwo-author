package fi.statistiek.frequencytable;

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
 * MVC Controller for StatistiekView FrequencyTable
 * 
 * @author ManuDrijvers
 * 
 */
public class FrequencyTableController implements StatistiekView,
	ActionListener, FocusListener
{

	private FrequencyTableModel model;
	private FrequencyTableView view;

	/**
	 * Constructor
	 * 
	 * @param tableModel
	 *            the data model
	 * @param viewName
	 *            the initial name of the view
	 */
	public FrequencyTableController(StatTableModel tableModel, String viewName,
		int startVar)
	{
		this.model = new FrequencyTableModel(tableModel, viewName);
		this.model.setColumnIndex(startVar);
		this.view = new FrequencyTableView(this.model, this);
		this.view.update(null, null);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		String action = arg0.getActionCommand();
		
		if (action.equals("columnIndexBox"))
		{
			this.model.initNoBins(5);
			this.model.setColumnIndex(this.view.varBoxSelectedIndex());
		}
		else if (action.equals("showPercBox"))
		{
			this.model.setShowPercentage(this.view.isShowPercBoxSelected());
		}
		else if (action.equals("showCumulativeBox"))
		{
			this.model.setShowCumulative(this.view
				.isShowCumulativeBoxSelected());
		}
		else if (action.equals("minBoundary"))
		{
			processMinBoundaryChanged();
		}
		else if (action.equals("binWidth"))
		{
			processBinWidthChanged();
		}
		else if (action.equals("splitBinsBox"))
		{
			this.setSplitType(this.model.getStatTableModel().getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex())
				.getType());
		}
		else if (action.equals("splitMinBoundary"))
		{
			processSplitMinBoundaryChanged();
		}
		else if (action.equals("splitBinWidth"))
		{
			processSplitBinWidthChanged();
		}
	}

	private void processMinBoundaryChanged()
	{
		double minBoundary = view.getUserOptionsPanel().getMinBoundary(); // the user entered value
		double minData = this.model.getStatTableModel().getColumnMin(this.model.getColumnIndex());
		
		if (minBoundary <= minData)
		{
			// update bin settings
			this.updateBoundariesFromBinSettings();
		}
		else
		{
			// reset to latest value
			double resetMin;
			if (model.getBinBoundaries() != null && model.getBinBoundaries().size() > 0)
			{
				resetMin = model.getBinBoundaries().get(0);
			}
			else
			{
				resetMin = minData;
			}
			
			view.getUserOptionsPanel().setMinBoundary(resetMin);
		}
	}

	public void processBinWidthChanged()
	{
		// update bin settings
		this.updateBoundariesFromBinSettings();
	}

	public void processSplitBinWidthChanged()
	{
		// update split index bin settings
		this.updateSplitBoundariesFromBinSettings();
	}

	void processSplitMinBoundaryChanged()
	{
		double splitMinBoundary = view.getUserOptionsPanel().getSplitMinBoundary(); // the user entered value
		double splitMinData = this.model.getStatTableModel().getColumnMin(this.model.getSplitOptions().getColumnSplitIndex());
		
		if (splitMinBoundary <= splitMinData)
		{
			// update split index bin settings
			this.updateSplitBoundariesFromBinSettings();
		}
		else
		{
			// reset to latest value
			double resetSplitMin;
			if (model.getSplitOptions().getBinBoundaries() != null && model.getSplitOptions().getBinBoundaries().size() > 0)
			{
				resetSplitMin = model.getSplitOptions().getBinBoundaries().get(0);
			}
			else
			{
				resetSplitMin = splitMinData;
			}
			
			view.getUserOptionsPanel().setSplitMinBoundary(resetSplitMin);
		}
	}

	/*
	 * Update the split bin boundaries using the settings for the minimum boundary
	 * and the bin width, and determine the number of bins.
	 */
	public void updateSplitBoundariesFromBinSettings()
	{
		ArrayList<Double> boundaries = new ArrayList<Double>();

		double min = this.model.getStatTableModel().getColumnMin(
			this.model.getSplitOptions().getColumnSplitIndex());
		double max = this.model.getStatTableModel().getColumnMax(
			this.model.getSplitOptions().getColumnSplitIndex());
		
		boundaries = Statistiek.appropriateBoundariesFromBinSettings(
			min,
			max,
			view.getSplitBinWidth(),
			view.getSplitMinBoundary());
		
		// if result is valid, set boundaries
		if (boundaries != null)
		{
			this.model.setSplitBoundaries(boundaries);
		}
		else
		{
			// reset to old values
			ArrayList<Double> oldBoundaries = this.model.getSplitOptions().getBinBoundaries(); 
			this.view.setSplitBinWidth();
			this.view.setSplitMinBoundary(oldBoundaries.get(0));
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
		return "Frequentietabel";
	}

	public Object getState()
	{
		Hashtable h = new Hashtable();

		h.put("showPercentage", this.model.isShowPercentage());
		h.put("showCumulative", this.model.isShowCumulative());
		h.put("binBoundaries", this.model.getBinBoundaries());
		h.put("viewName", this.getViewName());
		h.put("columnIndex", this.model.getColumnIndex());
		h.put("columnSplitIndex", this.model.getSplitOptions()
			.getColumnSplitIndex());
		h.put("splitBoundaries", this.model.getSplitOptions()
			.getBinBoundaries());

		return h;
	}

	public void setState(Object state)
	{
		Hashtable h = (Hashtable) state;
		
		// deep copy waarschijnlijk niet nodig...
		//Hashtable h = Copy.deepCopy((Hashtable) state);

		if (h.containsKey("showPercentage"))
		{
			this.model.setShowPercentage((Boolean) h.get("showPercentage"));
		}
		if (h.containsKey("showCumulative"))
		{
			this.model.setShowCumulative((Boolean) h.get("showCumulative"));
		}
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
		//System.out.println("FrequencyTableController.focusLost()");
		
		if (arg0.getSource() == this.view.getMinBoundaryField())
		{
			processMinBoundaryChanged();
		}
		else if (arg0.getSource() == this.view.getBinWidthField())
		{
			processBinWidthChanged();
		}
		else if (arg0.getSource() == this.view.getSplitMinBoundaryField())
		{
			processSplitMinBoundaryChanged();
		}
		else if (arg0.getSource() == this.view.getSplitBinWidthField())
		{
			processSplitBinWidthChanged();
		}
	}

	/*
	 * Update the bin boundaries using the settings for the minimum boundary
	 * and the bin width.
	 * and determine the number of bins.
	 */
	private void updateBoundariesFromBinSettings()
	{
		ArrayList<Double> boundaries = new ArrayList<Double>();
		
		double min = this.model.getStatTableModel().getColumnMin(
			this.model.getColumnIndex());
		double max = this.model.getStatTableModel().getColumnMax(
			this.model.getColumnIndex());
		
		boundaries = Statistiek.appropriateBoundariesFromBinSettings(
			min,
			max,
			view.getBinWidth(),
			view.getMinBoundary());
		
		// if result is valid, set boundaries
		if (boundaries != null)
		{
			this.model.setBinBoundaries(boundaries);
		}
		else
		{
			// reset to old values
			ArrayList<Double> oldBoundaries = this.model.getBinBoundaries(); 
			this.view.setBinWidth();
			this.view.setMinBoundary(oldBoundaries.get(0));
		}
	}
	
	public void setSplitType(AllowedTypes type)
	{
		if (type.isNumber())
		{
			ArrayList<Double> boundaries = new ArrayList<Double>();
			boundaries = Statistiek.appropriateBoundaries(
				this.model.getStatTableModel().getColumnMin(
					this.model.getSplitOptions().getColumnSplitIndex()),
				this.model.getStatTableModel().getColumnMax(
					this.model.getSplitOptions().getColumnSplitIndex()),
				this.view.getSplitBinsBoxSelectedInt());

			this.model.setSplitBoundaries(boundaries);
			this.model.setSplitOptions(this.model.getSplitOptions());
			this.view.setModel(this.model);
		}
	}
}
