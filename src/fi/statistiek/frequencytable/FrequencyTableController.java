package fi.statistiek.frequencytable;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComponent;

import fi.statistiek.Copy;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.StatistiekView;
import fi.statistiek.histogram.DefineBinBoundariesDialog;
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
		
//		System.out.println("FrequencyTableController.actionPerformed(): action = "
//			+ action);

		if (action.equals("columnIndexBox"))
		{
			this.model.initNoBins(5);
			this.model.setColumnIndex(this.view.varBoxSelectedIndex());
		}
		else if (action.equals("showPercBox"))
		{
			this.model.setShowPercentage(this.view.isShowPercBoxSelected());
		}
//		else if (action.equals("showFreqBox"))
//		{
//			this.model.setShowFreq(this.view.isShowFreqBoxSelected());
//		}
		else if (action.equals("showCumulativeBox"))
		{
			this.model.setShowCumulative(this.view
				.isShowCumulativeBoxSelected());
		}
//		else if (action.equals("noBinsField"))
//		{
//			String s = this.view.getNoBinsFieldText();
//			try
//			{
//				int i = Integer.parseInt(s);
//				this.model.setNoBins(i);
//			}
//			catch (NumberFormatException e)
//			{
//				this.view.update(null, null);
//			}
//		}
		else if (action.equals("minBoundary"))
		{
			updateBoundariesFromBinSettings();
		}
		else if (action.equals("binWidth"))
		{
			updateBoundariesFromBinSettings();
		}
		else if (action.equals("splitVarBox"))
		{
			// System.out.println("FrequencyTableController.actionPerformed(): splitVarBox, SplitColumnUpdate!");
			if (this.view.getSplitVarBoxSelectedIndex() - 1 != this.model
				.getSplitOptions().getColumnSplitIndex())
			{
				this.model.setColumnSplitIndex(this.view
					.getSplitVarBoxSelectedIndex() - 1);
				this.model.setSplitOptions(this.model.getSplitOptions());
				if (this.view.getSplitVarBoxSelectedIndex() > 0)
				{
					this.setSplitType(this.model
						.getTableModel()
						.getColumnTypes()
						.get(this.model.getSplitOptions().getColumnSplitIndex())
						.getType());
				}
			}
		}
		else if (action.equals("splitBinsBox"))
		{
			this.setSplitType(this.model.getTableModel().getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex())
				.getType());
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
		
//		if (arg0.getSource() == this.view.getNoBinsField())
//		{
//			System.out.println("focusLost van nobinsField");
//			String s = this.view.getNoBinsFieldText();
//			try
//			{
//				int i = Integer.parseInt(s);
//				this.model.setNoBins(i);
//			}
//			catch (NumberFormatException e)
//			{
//				this.view.update(null, null);
//			}
//		}

		updateBoundariesFromBinSettings();
	}

	/*
	 * Update the bin boundaries using the settings for the minimum boundary
	 * and the bin width.
	 * and determine the number of bins.
	 */
	private void updateBoundariesFromBinSettings()
	{
		ArrayList<Double> boundaries = new ArrayList<Double>();
		
		boundaries = Statistiek.appropriateBoundariesFromBinSettings(
			this.model.getTableModel().getColumnMin(
				this.model.getColumnIndex()),
			this.model.getTableModel().getColumnMax(
				this.model.getColumnIndex()),
			view.getBinWidth(),
			view.getMinBoundary());
		
		this.model.setBinBoundaries(boundaries);
	}
	
	private void setSplitType(AllowedTypes type)
	{
		if (type.isNumber())
		{
			ArrayList<Double> boundaries = new ArrayList<Double>();
			boundaries = Statistiek.appropriateBoundaries(
				this.model.getTableModel().getColumnMin(
					this.model.getSplitOptions().getColumnSplitIndex()),
				this.model.getTableModel().getColumnMax(
					this.model.getSplitOptions().getColumnSplitIndex()),
				this.view.getSplitBinsBoxSelectedInt());

			this.model.setSplitBoundaries(boundaries);
			this.model.setSplitOptions(this.model.getSplitOptions());
			this.view.setModel(this.model);
		}
	}
}
