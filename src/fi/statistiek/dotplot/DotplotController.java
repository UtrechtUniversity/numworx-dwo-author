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
		this.view = new DotplotView(this.model, this);
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
		this.view = new DotplotView(this.model, this);
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
		// else if(actionCommand.equals("varSplitBox")) {
		// this.model.setColumnSplitIndex(this.view.getVarSplitBoxSelected());
		// }
		else if (actionCommand.equals("showCorrelationBox"))
		{
			this.model.setShowCorrelation(this.view
				.getShowCorrelationBoxSelected());
		}
		/*
		 * else if(actionCommand.equals("splitButton")) { Container c =
		 * Statistiek.getTopLevelAcestor(this.view);
		 * 
		 * SplitOptionsDialog dialog = null; if(c instanceof Dialog) { dialog =
		 * new SplitOptionsDialog((Dialog)c, this.model.getSplitOptions(),
		 * this.model.getTableModel()); } else if(c instanceof Frame) { dialog =
		 * new SplitOptionsDialog((Frame)c, this.model.getSplitOptions(),
		 * this.model.getTableModel()); }
		 * 
		 * dialog.setVisible(true); if(dialog.isDonePressed()) {
		 * this.model.setSplitOptions(dialog.getSplitOptions()); }
		 * 
		 * this.view.update(null, null); }
		 */
		// else if(actionCommand.equals("splitBinsBox")) {
		// this.model.setNoSplitBins(this.view.getSplitBinsBoxSelectedInt());
		// }
		else if (actionCommand.equals("splitVarBox"))
		{
			//System.out.println("DotplotController.actionPerformed(): splitVarBox!");
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
		else if (actionCommand.equals("splitBinsBox"))
		{
			this.setSplitType(this.model.getTableModel().getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex())
				.getType());
		}
		else if (actionCommand.equals("splitMinBoundary"))
		{
			updateSplitBoundariesFromBinSettings();
		}
		else if (actionCommand.equals("splitBinWidth"))
		{
			updateSplitBoundariesFromBinSettings();
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

	private void updateSplitBoundaries()
	{
		ArrayList<Double> boundaries = new ArrayList<Double>();
		for (int i = 0; i <= this.view.getSplitBinsBoxSelectedInt(); i++)
		{
			boundaries.add(new Double(view.getSplitMinBoundary() + i
				* view.getSplitBinWidth()));
		}
		this.model.setSplitBoundaries(boundaries);
		this.view.setModel(this.model);
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
		// System.out.println("   columnSplitIndex=" +
		// this.model.getSplitOptions().getColumnSplitIndex());
		h.put("splitBoundaries", this.model.getSplitBinBoundaries());

		h.put("splitInSingleView", this.model.splitInSingleView());
		// System.out.println("   splitInSingleView=" +
		// this.model.splitInSingleView());

		return h;
	}

	public void setState(Object state)
	{
		Hashtable h = (Hashtable) state;
		if (h.containsKey("showCorrelation"))
		{
			this.model.setShowCorrelation(((Boolean) h.get("showCorrelation"))
				.booleanValue());
		}
		if (h.containsKey("useColorScale"))
		{
			this.model.setUseColorScale(((Boolean) h.get("useColorScale"))
				.booleanValue());
		}
		if (h.containsKey("columnColorIndex"))
		{
			this.model
				.setColumnColorIndex(((Integer) h.get("columnColorIndex"))
					.intValue());
		}
		if (h.containsKey("columnXIndex"))
		{
			this.model.setColumnXIndex(((Integer) h.get("columnXIndex"))
				.intValue());
		}
		if (h.containsKey("columnYIndex"))
		{
			this.model.setColumnYIndex(((Integer) h.get("columnYIndex"))
				.intValue());
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
			this.model.setColumnSplitIndex((Integer) h.get("columnSplitIndex"));
		}
		if (h.containsKey("splitInSingleView"))
		{
			this.model.setSplitInSingleView(((Boolean) h
				.get("splitInSingleView")).booleanValue());
		}
		if (h.containsKey("splitBoundaries"))
		{
			this.model.setSplitBoundaries((ArrayList<Double>) h
				.get("splitBoundaries"));
		}
//		if (h.containsKey("scatterplotMode"))
//		{
//			this.model.setScatterplotMode(((Boolean) h
//				.get("scatterplotMode")).booleanValue());
//		}
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
			this.view.setSplitBinWidth();
			this.view.setSplitMinBoundary(oldBoundaries.get(0));
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
