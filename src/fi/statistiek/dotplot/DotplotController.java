package fi.statistiek.dotplot;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JComponent;

import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.StatistiekView;
import fi.statistiek.types.AllowedTypes;

/**
 * MVC Controller for StatistiekView Dotplot
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class DotplotController implements StatistiekView, ActionListener,
	MouseListener
{
	private DotplotView view;
	private DotplotModel model;

	/**
	 * Constructor of DotplotController.
	 * 
	 * @param tableModel
	 *            The data table
	 * @param viewName
	 *            this view's name
	 */
	public DotplotController(StatTableModel tableModel, String viewName,
		int startVar)
	{
		this.model = new DotplotModel(tableModel, viewName, false);
		model.setColumnXIndex(startVar);
		
		model.initializeMinXOnScale();
		model.initializeMaxXOnScale();
		
		this.view = new DotplotView(this.model, this);
		
		try
		{
			// voor standalone
			this.model.setMinXOnScale(this.view.getMinXOnScale());
			this.model.setMaxXOnScale(this.view.getMaxXOnScale());
		}
		catch (NumberFormatException e) 
		{
			// Bij create view vanuit zet opdracht wordt eerst een histogram-view met columnIndex 0 gemaakt
			// dan is er nog geen minBoundary en maxBinOnScale
		}

		this.view.update(null, null);
	}

	/**
	 * Constructor of DotplotController for scatterplot mode.
	 * 
	 * @param tableModel
	 *            The data table
	 * @param viewName
	 *            this view's name
	 */
	public DotplotController(StatTableModel tableModel, String viewName,
		int startVar1, int startVar2)
	{
		this.model = new DotplotModel(tableModel, viewName, true);
		model.setColumnXIndex(startVar1);
		model.setColumnYIndex(startVar2);
		
		model.initializeMinXOnScale();
		model.initializeMaxXOnScale();
		
		this.view = new DotplotView(this.model, this);
		
		try
		{
			// voor standalone
			this.model.setMinXOnScale(this.view.getMinXOnScale());
			this.model.setMaxXOnScale(this.view.getMaxXOnScale());
		}
		catch (NumberFormatException e) 
		{
			// Bij create view vanuit zet opdracht wordt eerst een histogram-view met columnIndex 0 gemaakt
			// dan is er nog geen minBoundary en maxBinOnScale
		}

		this.view.update(null, null);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		String actionCommand = arg0.getActionCommand();
		if (actionCommand.equals("varColorBox"))
		{
			this.model.setColumnColorIndex(this.view.getVarColorBoxSelected());
		}
		else if (actionCommand.equals("varXBox"))
		{
			this.model.setColumnXIndex(this.view.getVarXBoxSelected());
			if (this.model.isScatterplotMode() && !(this.view.getXType().isNumber() && this.view.getYType().isNumber()))
			{
				// if not both variables are numerical, no correlation should be shown 
				this.model.setShowCorrelation(false);
			}
		}
		else if (actionCommand.equals("varYBox"))
		{
			this.model.setColumnYIndex(this.view.getVarYBoxSelected());
			if (!(this.view.getXType().isNumber() && this.view.getYType().isNumber()))
			{
				// if not both variables are numerical, no correlation should be shown 
				this.model.setShowCorrelation(false);
			}
		}
		else if (actionCommand.equals("showCorrelationBox"))
		{
			this.model.setShowCorrelation(this.view
				.getShowCorrelationBoxSelected());
		}
		else if (actionCommand.equals("splitVarBox"))
		{
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
			processSplitMinBoundaryChanged();
		}
		else if (actionCommand.equals("splitBinWidth"))
		{
			processSplitBinWidthChanged();
		}
		else if (actionCommand.equals("splitSingleView"))
		{
			this.model.setSplitInSingleView(true);
		}
		else if (actionCommand.equals("separateFromEachOther"))
		{
			this.model.setSplitInSingleView(false);
		}
	}

	/**
	 * Process actions when minimum value on the scale has been changed.
	 */
	void processMinXOnScaleChanged()
	{
		if (model.getStatTableModel().isEmptyColumn(model.getColumnXIndex()))
		{
			// min > max is niet toegestaan
			if (view.getMinXOnScale() > view.getMaxXOnScale())
			{
				// set both min and max to the same value
				model.setMinXOnScaleWithoutEvent(view.getMinXOnScale());
				model.setMaxXOnScale(view.getMinXOnScale());
			}
			else
			{
				model.setMinXOnScale(view.getMinXOnScale());
			}
		} // empty column
		else
		{ 
			// data in column
			double minColumnXValue = model.getStatTableModel().getColumnMin(model.getColumnXIndex());
			// alleen check of data binnen grenzen als optimize scale
			if (model.isOptimizeScaleX())
			{
				if (view.getMinXOnScale() > minColumnXValue)
				{
					// invalid input
					
					if (model.getMinXOnScale() > minColumnXValue)
					{
						// the model's min is not correct, data may have been changed and the model's min on scale needs to be reset
						model.setMinXOnScale(minColumnXValue);
					}
					else
					{
						// reset to latest value
						view.getUserOptionsPanel().setMinXOnScale(model.getMinXOnScale());
					}
				}
				else
				{
					model.setMinXOnScale(view.getMinXOnScale());
				}
			}
			else
			{
				model.setMinXOnScale(view.getMinXOnScale());
			}
		} // data in column
	}
	
	/**
	 * Process actions when maximum value on the scale has been changed.
	 */
	void processMaxXOnScaleChanged()
	{
		if (model.getStatTableModel().isEmptyColumn(model.getColumnXIndex()))
		{
			// max < min is niet toegestaan
			if (view.getMaxXOnScale() < view.getMinXOnScale())
			{
				// set both min and max to the same value
				model.setMaxXOnScaleWithoutEvent(view.getMaxXOnScale());
				model.setMinXOnScale(view.getMaxXOnScale());
			}
			else
			{
				model.setMaxXOnScale(view.getMaxXOnScale());
			}
		} // empty column
		else
		{ // data in column
			double maxColumnXValue = model.getStatTableModel().getColumnMax(model.getColumnXIndex());
			
			// alleen check of data binnen grenzen als optimize scale
			if (model.isOptimizeScaleX())
			{
				if (view.getMaxXOnScale() < maxColumnXValue)
				{
					if (model.getMaxXOnScale() < maxColumnXValue)
					{
						// the model's max is not correct, data may have been changed and the model's max on scale needs to be reset
						model.setMaxXOnScale(maxColumnXValue);
					}
					else
					{
						// reset to latest value
						view.getUserOptionsPanel().setMaxXOnScale(model.getMaxXOnScale());
					}
				}
				else
				{
					model.setMaxXOnScale(view.getMaxXOnScale());
				}
			}
			else
			{
				model.setMaxXOnScale(view.getMaxXOnScale());
			}
		} // data in column
	}

	public void processSplitMinBoundaryChanged()
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
				resetSplitMin = model.getSplitOptions().getBinBoundaries().get(0).doubleValue();
			}
			else
			{
				resetSplitMin = splitMinData;
			}
			
			view.getUserOptionsPanel().setSplitMinBoundary(resetSplitMin);
		}
	}

	public void processSplitBinWidthChanged()
	{
		// update split index bin settings
		this.updateSplitBoundariesFromBinSettings();
	}

	private void setSplitType(AllowedTypes type)
	{
		if (type.isNumber())
		{
			ArrayList<Number> boundaries = new ArrayList<Number>();
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

	/**
	 * Set up procedure for Frame owner
	 */
	public void setUp(Frame owner)
	{
		DotplotSetUpDialog setUpDialog = new DotplotSetUpDialog(owner,
			this.model, this.view);
		setUpDialog.setVisible(true);
	}

	/**
	 * Set up procedure for Dialog owner
	 */

	public void setUp(Dialog owner)
	{
		DotplotSetUpDialog setUpDialog = new DotplotSetUpDialog(owner,
			this.model, this.view);
		setUpDialog.setVisible(true);
	}

	public JComponent getComponent()
	{
		return this.view;
	}

	public String getViewType()
	{
		return (this.model.isScatterplotMode() ? "Spreidingsdiagram"
			: "Dotplot");
	}

	public Object getState()
	{
		Hashtable h = new Hashtable();

		h.put("showCorrelation", this.model.isShowCorrelation());
		h.put("useColorScale", this.model.isUseColorScale());

		h.put("columnColorIndex", this.model.getColumnColorIndex());
		h.put("columnXIndex", this.model.getColumnXIndex());
		h.put("columnYIndex", this.model.getColumnYIndex());

		h.put("colorA", this.model.getColorA());
		h.put("colorB", this.model.getColorB());
		
		// for HTML5 version add color strings
		h.put("colorAString", this.model.getColorAString());
		h.put("colorBString", this.model.getColorBString());

		h.put("viewName", this.model.getViewName());

		h.put("columnSplitIndex", this.model.getSplitOptions()
			.getColumnSplitIndex());
		h.put("splitBoundaries", this.model.getSplitBinBoundaries());

		h.put("splitInSingleView", this.model.splitInSingleView());

		h.put("optimizeScaleX", this.model.isOptimizeScaleX());
		h.put("minXOnScale", this.model.getMinXOnScale());
		h.put("maxXOnScale", this.model.getMaxXOnScale());

		return h;
	}

	public void setState(Object state)
	{
		Map h = (Map) state;
		
		if (h.containsKey("showCorrelation"))
		{
			this.model.setShowCorrelation(((Boolean) h.get("showCorrelation")).booleanValue());
		}
		if (h.containsKey("useColorScale"))
		{
			this.model.setUseColorScale(((Boolean) h.get("useColorScale")).booleanValue());
		}
		if (h.containsKey("columnColorIndex"))
		{
			this.model.setColumnColorIndex(((Number) h.get("columnColorIndex")).intValue());
		}
		if (h.containsKey("columnXIndex"))
		{
			this.model.setColumnXIndex(((Number) h.get("columnXIndex")).intValue());
		}
		if (h.containsKey("columnYIndex"))
		{
			this.model.setColumnYIndex(((Number) h.get("columnYIndex")).intValue());
		}
		if (h.containsKey("colorA"))
		{
			this.model.setColorA((Color) h.get("colorA"));
		}
		if (h.containsKey("colorB"))
		{
			this.model.setColorB((Color) h.get("colorB"));
		}
		if (h.containsKey("viewName"))
		{
			this.model.setViewName((String) h.get("viewName"));
		}
		if (h.containsKey("columnSplitIndex"))
		{
			this.model.setColumnSplitIndex(((Number) h.get("columnSplitIndex")).intValue());
		}
		if (h.containsKey("splitInSingleView"))
		{
			this.model.setSplitInSingleView(((Boolean) h.get("splitInSingleView")).booleanValue());
		}
		if (h.containsKey("splitBoundaries"))
		{
			this.model.setSplitBoundaries((ArrayList<Number>) h.get("splitBoundaries"));
		}
		
		if (h.containsKey("optimizeScaleX"))
		{
			this.model.setOptimizeScaleXWithoutEvent(((Boolean) h.get("optimizeScaleX"))
				.booleanValue());
		}
		
		if (h.containsKey("minXOnScale"))
		{
			this.model.setMinXOnScaleWithoutEvent(((Number) h.get("minXOnScale")).doubleValue());
		}
		else
		{
			// default is the columnX's minimum value
			double minColumnXValue = this.model.getStatTableModel().getColumnMin(this.model.getColumnXIndex());
			this.model.setMinXOnScaleWithoutEvent(minColumnXValue);
		}
		
		if (h.containsKey("maxXOnScale"))
		{
			this.model.setMaxXOnScale(((Number) h.get("maxXOnScale")).doubleValue());
		}
		else
		{
			// default is the columnX's maximum value
			double maxColumnXValue = this.model.getStatTableModel().getColumnMax(this.model.getColumnXIndex());
			this.model.setMaxXOnScale(maxColumnXValue);
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

	/*
	 * Update the split bin boundaries using the settings for the minimum boundary
	 * and the bin width, and determine the number of bins.
	 */
	public void updateSplitBoundariesFromBinSettings()
	{
		ArrayList<Number> boundaries = new ArrayList<Number>();
		
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
			ArrayList<Number> oldBoundaries = this.model.getSplitOptions().getBinBoundaries(); 
			this.view.setSplitBinWidth();
			this.view.setSplitMinBoundary(oldBoundaries.get(0).doubleValue());
		}
	}
	
	public void mouseClicked(MouseEvent arg0)
	{
		Container c = Statistiek.getTopLevelAncestor(this.view);
		ChooseColorsDialog dialog;
		if (c instanceof Frame)
		{
			dialog = new ChooseColorsDialog((Frame) c, this.model.getColorA(),
				this.model.getColorB());
			dialog.setVisible(true);
			this.model.setColorA(dialog.getColorA());
			this.model.setColorB(dialog.getColorB());
		}
		else if (c instanceof Dialog)
		{
			dialog = new ChooseColorsDialog((Dialog) c, this.model.getColorA(),
				this.model.getColorB());
			dialog.setVisible(true);
			this.model.setColorA(dialog.getColorA());
			this.model.setColorB(dialog.getColorB());
		}
	}

	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public String toString()
	{
		return this.getViewName();
	}

	public DotplotModel getModel()
	{
		return this.model;
	}
}
