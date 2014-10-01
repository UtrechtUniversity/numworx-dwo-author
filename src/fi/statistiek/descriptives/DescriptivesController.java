package fi.statistiek.descriptives;

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
 * MVC Controller for StatistiekView Descriptives
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class DescriptivesController implements StatistiekView,
	ActionListener, FocusListener
{
	private DescriptivesModel model;
	private DescriptivesView view;

	/**
	 * Constructor
	 * 
	 * @param tableModel
	 *            the data model
	 * @param viewName
	 *            the initial name of the view
	 */
	public DescriptivesController(StatTableModel tableModel, String viewName,
		int startVar)
	{
		this.model = new DescriptivesModel(tableModel, viewName);
		this.model.setColumnIndex(startVar);
		this.view = new DescriptivesView(this.model, this);
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
				.getType(), 5);//index);
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
	
	/**
	 * ActionListener implementation
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		String action = arg0.getActionCommand();
		
//		System.out.println("DescriptivesController.actionPerformed(): action = "
//			+ action);

		if (action.equals("columnIndexBox"))
		{
			this.model.setColumnIndex(this.view.varBoxSelectedIndex());
		}
		else if (action.equals("splitVarBox"))
		{
			// System.out.println("HistogramController.actionPerformed(): splitVarBox, SplitColumnUpdate!");
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
		else if (action.equals("splitNoBinsBox"))
		{
			this.setSplitType(this.model.getTableModel().getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex())
				.getType());
		}
		else if (action.equals("splitMinBoundary"))
		{
			updateSplitBoundariesFromBinSettings();
		}
		else if (action.equals("splitBinWidth"))
		{
			updateSplitBoundariesFromBinSettings();
		}
	}

	private void updateSplitBoundaries()
	{
		ArrayList<Double> boundaries = new ArrayList<Double>();
		for (int i = 0; i <= this.view.getSplitVarBoxSelectedIndex(); i++)
		{
			boundaries.add(new Double(view.getSplitMinBoundary() + i
				* view.getSplitBinWidth()));
		}
		this.model.setSplitBoundaries(boundaries);
		this.view.setModel(this.model);
	}

	/*
	 * Update the split bin boundaries using the settings for the minimum boundary
	 * and the bin width, and determine the number of bins.
	 */
	private void updateSplitBoundariesFromBinSettings()
	{
		ArrayList<Double> boundaries = new ArrayList<Double>();

		double min = this.model.getTableModel().getColumnMin(
			this.model.getSplitOptions().getColumnSplitIndex());
		double max = this.model.getTableModel().getColumnMax(
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
			this.view.setSplitBinWidth(oldBoundaries.get(1) - oldBoundaries.get(0));
			this.view.setSplitMinBoundary(oldBoundaries.get(0));
		}
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
		return "Kengetallen";
	}

	public Object getState()
	{
		Hashtable h = new Hashtable();

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
		
		if (h.containsKey("viewName"))
		{
			this.setViewName((String) h.get("viewName"));
		}
		if (h.containsKey("columnIndex"))
		{
			this.model.setColumnIndex((Integer) h.get("columnIndex"));
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

	public void focusLost(FocusEvent e)
	{
//		System.out.println("DescriptivesController.focusLost(): e.getSource()="
//			+ e.getSource());

		updateSplitBoundariesFromBinSettings();
	}
}
