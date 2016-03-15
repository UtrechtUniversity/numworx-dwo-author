package fi.statistiek.boxplot;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComponent;

import fi.statistiek.SplitOptionsDialog;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.StatistiekView;
import fi.statistiek.types.AllowedTypes;

/**
 * 
 * MVC Controller for statistiekview Boxplot
 * 
 * @author Manu Drijvers
 * 
 */
public class BoxplotController implements StatistiekView, ActionListener
{
	private BoxplotView view;
	private BoxplotModel model;

	/**
	 * Constructor
	 * 
	 * @param tableModel
	 *            the tablemodel
	 * @param viewName
	 *            the name of this view
	 */
	public BoxplotController(StatTableModel tableModel, String viewName,
		int startVar)
	{
		this.model = new BoxplotModel(tableModel, viewName);
		this.model.setColumnIndex(startVar);
		this.view = new BoxplotView(this.model, this);
		this.view.update(null, null);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		String actionCommand = arg0.getActionCommand();

		if (actionCommand.equals("varBox"))
		{
			String selectedString = this.view.getColumnBoxSelectedString();
			if (selectedString != null)
			{
				this.model.setColumnIndex(this.model.getStatTableModel()
					.getColumnIndexByName(selectedString));
				this.view.repaint();
			}
		}
		else if (actionCommand.equals("tukeyBox"))
		{
			model.setIsTukeyBox(view.isTukeyBoxSelected());
		}
		else if (actionCommand.equals("horizontalboxplotsRadio")
			|| actionCommand.equals("verticalboxplotsRadio"))
		{
			this.model.setVerticalBoxplots(this.view
				.isVerticalBoxesButtonSelected());
		}
		else if (actionCommand.equals("splitButton"))
		{
			Container c = Statistiek.getTopLevelAncestor(this.view);

			SplitOptionsDialog dialog = null;
			if (c instanceof Dialog)
			{
				dialog = new SplitOptionsDialog((Dialog) c,
					this.model.getSplitOptions(), this.model.getStatTableModel());
			}
			else if (c instanceof Frame)
			{
				dialog = new SplitOptionsDialog((Frame) c,
					this.model.getSplitOptions(), this.model.getStatTableModel());
			}

			dialog.setVisible(true);
			if (dialog.isDonePressed())
			{
				// only apply settings if done is pressed
				this.model.setSplitOptions(dialog.getSplitOptions());
			}
		}
		else if (actionCommand.equals("splitVarBox"))
		{
			//System.out.println("BoxplotController.actionperformed(): splitVarBox!");
			if (this.view.getSplitVarBoxSelectedIndex() - 1 != this.model
				.getSplitOptions().getColumnSplitIndex())
			{
				this.model.setColumnSplitIndex(this.view
					.getSplitVarBoxSelectedIndex() - 1);
				this.model.setSplitOptions(this.model.getSplitOptions());
				if (this.view.getSplitVarBoxSelectedIndex() > 0)
				{
					this.setSplitType(this.model
						.getStatTableModel()
						.getColumnTypes()
						.get(this.model.getSplitOptions().getColumnSplitIndex())
						.getType());
				}
				this.model.setPercentileValues();
			}
		}
		else if (actionCommand.equals("splitBinsBox"))
		{
			this.setSplitType(this.model.getStatTableModel().getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex())
				.getType());
		}
		else if (actionCommand.equals("splitMinBoundary"))
		{
			updateSplitBoundariesFromBinSettings();;
		}
		else if (actionCommand.equals("splitBinWidth"))
		{
			updateSplitBoundariesFromBinSettings();
		}

	}

	private void setSplitType(AllowedTypes type)
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

			this.model.setSplitBinBoundaries(boundaries);
			this.model.setSplitOptions(this.model.getSplitOptions());
			this.view.setModel(this.model);
		}

	}

	private void updateSplitBoundaries()
	{
		ArrayList<Double> boundaries = new ArrayList<Double>();
		for (int i = 0; i <= this.view.getSplitBinsBoxSelectedInt(); i++)
		{
			boundaries.add(new Double(view.getSplitminBoundary() + i
				* view.getSplitBinWidth()));
		}
		this.model.setSplitBinBoundaries(boundaries);
		this.view.setModel(this.model);
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
			view.getSplitminBoundary());
		
		// if result is valid, set boundaries
		if (boundaries != null)
		{
			this.model.setSplitBinBoundaries(boundaries);
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
		// nothing to set up
	}

	public void setUp(Dialog owner)
	{
		// nothing to set up
	}

	public JComponent getComponent()
	{
		return this.view;
	}

	public String getViewType()
	{
		return "Boxplot";
	}

	public void setViewName(String s)
	{
		this.model.setViewName(s);
	}

	public Object getState()
	{
		Hashtable<String, Object> h = new Hashtable<String, Object>();
		h.put("columnIndex", this.model.getColumnIndex());
		h.put("columnSplitIndex", this.model.getColumnSplitIndex());
		h.put("name", this.model.getViewName());
		h.put("splitBoundaries", this.model.getSplitBinBoundaries());
		h.put("tukeyBox", this.model.isTukeyBox());
		h.put("verticalBoxplots", this.model.isVerticalBoxplots());

		return h;
	}

	public void setState(Object state)
	{
		Hashtable<String, Object> h = (Hashtable<String, Object>) state;
		if (h.containsKey("columnIndex"))
		{
			this.model.setColumnIndex((Integer) h.get("columnIndex"));
		}
		if (h.containsKey("columnSplitIndex"))
		{
			this.model.setColumnSplitIndex((Integer) h.get("columnSplitIndex"));
		}
		if (h.containsKey("name"))
		{
			this.model.setViewName((String) h.get("name"));
		}
		if (h.containsKey("splitBoundaries"))
		{
			this.model.setSplitBinBoundaries((ArrayList<Double>) h
				.get("splitBoundaries"));
		}
		if (h.containsKey("tukeyBox"))
		{
			this.model.setIsTukeyBox((Boolean) h.get("tukeyBox"));
		}
		else
		{
			this.model.setIsTukeyBox(false); // oude boxplots default geen Tukey
		}
		if (h.containsKey("verticalBoxplots"))
		{
			this.model.setVerticalBoxplots((Boolean) h.get("verticalBoxplots"));
		}
	}

	public String getViewName()
	{
		return this.model.getViewName();
	}

}
