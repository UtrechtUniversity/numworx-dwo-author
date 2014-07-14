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
				this.model.setColumnIndex(this.model.getTableModel()
					.getColumnIndexByName(selectedString));
				this.view.repaint();
			}
		}
		else if (actionCommand.equals("horizontalboxplotsRadio")
			|| actionCommand.equals("verticalboxplotsRadio"))
		{
			this.model.setVerticalBoxplots(this.view
				.isVerticalBoxesButtonSelected());
		}
		else if (actionCommand.equals("splitButton"))
		{
			Container c = Statistiek.getTopLevelAcestor(this.view);

			SplitOptionsDialog dialog = null;
			if (c instanceof Dialog)
			{
				dialog = new SplitOptionsDialog((Dialog) c,
					this.model.getSplitOptions(), this.model.getTableModel());
			}
			else if (c instanceof Frame)
			{
				dialog = new SplitOptionsDialog((Frame) c,
					this.model.getSplitOptions(), this.model.getTableModel());
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
				this.model.setColumnSplitIndexWithoutEvent(this.view
					.getSplitVarBoxSelectedIndex() - 1);
				this.model.setSplitOptionsWithoutEvent(this.model.getSplitOptions());
				if (this.view.getSplitVarBoxSelectedIndex() > 0)
				{
					this.setSplitType(this.model
						.getTableModel()
						.getColumnTypes()
						.get(this.model.getSplitOptions().getColumnSplitIndex())
						.getType());
				}
				this.model.setPercentileValues();
			}
		}
		else if (actionCommand.equals("splitBinsBox"))
		{
			this.setSplitType(this.model.getTableModel().getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex())
				.getType());
		}
		else if (actionCommand.equals("splitMinBoundary"))
		{
			updateSplitBoundaries();
		}
		else if (actionCommand.equals("splitBinWidth"))
		{
			updateSplitBoundaries();
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
