package fi.statistiek.histogram;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComponent;

import fi.statistiek.SplitOptionsDialog;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.StatistiekView;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * MVC Controller for StatistiekView Histogram
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class HistogramController implements StatistiekView, ActionListener,
	FocusListener
{
	private HistogramModel model;
	private HistogramView view;

	/**
	 * Constructor
	 * 
	 * @param tableModel
	 *            the data table
	 * @param viewName
	 *            The initial name of the StatistiekView
	 */
	public HistogramController(StatTableModel tableModel, String viewName,
		boolean frequencyPolygonMode, int startVar)
	{
//		System.out.println("HistogramController(): maakt nieuw HistogramModel en daarmee HistogramView");
		
		this.model = new HistogramModel(tableModel, viewName, frequencyPolygonMode);
		model.setColumnIndex(startVar);
		model.setDefaultLabelPositioning();
		
		this.view = new HistogramView(this.model, this);
		
		this.view.update(null, null);
		
		try
		{
			this.model.setMinOnScale(this.view.getMinBoundary());
			this.model.setMaxOnScale(this.view.getMaxBinOnScale());
		}
		catch (NumberFormatException e)
		{
		}
	}

	private static int identityHashCode(Object o)
	{
		return System.identityHashCode(o);
	}
	
	/**
	 * Set up view with a Frame owner
	 */
	public void setUp(Frame owner)
	{
		this.model.setShowUserOptions(true);
		HistogramSetUpDialog setUpDialog = new HistogramSetUpDialog(owner,
			this.model, this.view);
		setUpDialog.setVisible(true);
		// this.model.setShowUserOptions(false);
	}

	/**
	 * Set up view with a Dialog owner
	 */
	public void setUp(Dialog owner)
	{
		this.model.setShowUserOptions(true);
		HistogramSetUpDialog setUpDialog = new HistogramSetUpDialog(owner,
			this.model, this.view);
		setUpDialog.setVisible(true);
		// this.model.setShowUserOptions(false);
	}

	/**
	 * Return the JComponent containing the graphical representation
	 */
	public JComponent getComponent()
	{
		return this.view;
	}

	/**
	 * @return The view's name
	 */
	public String getViewName()
	{
		return this.model.getViewName();
	}

	public void setViewName(String s)
	{
		this.model.setViewName(s);
	}

	/**
	 * ActionListener implementation
	 */
	public void actionPerformed(ActionEvent e)
	{
		String ac = e.getActionCommand();
		//System.out.println("HistogramController.actionPerformed(): " + ac);

		if (ac.equals("amountRadioItem") || ac.equals("percentageRadioItem"))
		{
			this.model.setPercentage(this.view.percentageItemSelected());
		}
		else if (ac.equals("percentage_splitTotal") || ac.equals("percentage_endTotal"))
		{
			this.model.setPercentageSplitTotal(this.view.percentageSplitTotalSelected());
		}
		else if (ac.equals("labelsBetweenBinsRadioItem") ||
			ac.equals("labelsUnderBinRadioItem"))
		{
			this.model.setLabelUnderBin(this.view.labelUnderBinItemSelected());
		}
		else if (ac.equals("varBox"))
		{
			this.model.setColumnIndex(this.view.getVarBoxSelectedIndex());
			this.model.setOptimizeScale(true); // default
		}
		else if (ac.equals("binsBox"))
		{
			this.model.setNoBins(this.view.getBinsBoxSelectedInt());
		}
		else if (ac.equals("minBoundary"))
		{
			processMinBoundaryChanged();
		}
		else if (ac.equals("maxOnScale"))
		{
			processMaxOnScaleChanged();
		}
		else if (ac.equals("binWidth"))
		{
			processBinWidthChanged();
		}
		else if (ac.equals("axisBox"))
		{
			this.model.setVerticalBars(this.view.xAxisSelected());
		}
		else if (ac.equals("cumulativeBox"))
		{
			this.model.setFrequencyPolygonCumulativeMode(this.view
				.isCumulativeBoxSelected());
		}
		else if (ac.equals("splitSingleView"))
		{
			this.model.setSplitInSingleView(this.view
				.isSplitSingleViewSelected());
		}
		else if (ac.equals("aboveEachOther")
			&& !this.model.isFrequencyPolygonMode())
		{
			this.model.setSplitInSingleView(this.view
				.isSplitSingleViewSelected());
//			this.model
//				.setNextToEachOther(this.view.isNextToEachOtherSelected());
			// code above is not working (anymore?), so straightforward
			this.model
				.setNextToEachOther(false);			
		}
		else if (ac.equals("nextToEachOther")
			&& !this.model.isFrequencyPolygonMode())
		{
			this.model.setSplitInSingleView(this.view
				.isSplitSingleViewSelected());
//			this.model
//				.setNextToEachOther(this.view.isNextToEachOtherSelected());
			// code above is not working (anymore?), so straightforward
			this.model
				.setNextToEachOther(true);
		}
		else if (ac.equals("separateFromEachOther"))
		{
			this.model.setSplitInSingleView(false);
		}
		else if (ac.equals("stackMode"))
		{
			this.model.setFrequencyPolygonStackMode(this.view
				.isStackModeBoxSelected());
		}
		else if (ac.equals("splitBinsBox"))
		{
			this.setSplitType(this.model.getStatTableModel().getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex())
				.getType());
		}
		else if (ac.equals("splitMinBoundary"))
		{
			processSplitMinBoundaryChanged();
		}
		else if (ac.equals("splitBinWidth"))
		{
			processSplitBinWidthChanged();
		}
		else if (ac.equals("chooseBinsButton"))
		{
			Container c = Statistiek.getTopLevelAncestor(this.view);

			DefineBinBoundariesDialog dialog = null;
			if (c instanceof Dialog)
			{
				dialog = new DefineBinBoundariesDialog((Dialog) c, this.model);
			}
			else if (c instanceof Frame)
			{
				dialog = new DefineBinBoundariesDialog((Frame) c, this.model);
			}

			dialog.setVisible(true);
			if (dialog.isDonePressed())
			{
				this.model.setBinBoundaries(dialog.getBoundaries());
			}
		}
		else if (ac.equals("optimizeScaleBox"))
		{
			model.setBinWidthWithoutEvent(view.getBinWidth());
			Double min = null;
			Double max = null;
			double columnMin = model.getStatTableModel().getColumnMin(model.getColumnIndex());
			double columnMax = model.getStatTableModel().getColumnMax(model.getColumnIndex());
			if (view.getUserOptionsPanel().isOptimizeScale()) // optimize wordt aangezet
			{
				if (view.getMinBoundary() > columnMin) // user zet optimaliseren aan en de oude min waarde is te groot
				{
					min = columnMin;
					view.setMinBoundary(min); // update in the view's settings
				}
				else
				{
					min = view.getMinBoundary();
				}
				
				if (view.getMaxBinOnScale() < columnMax) // user zet optimaliseren aan en de oude max waarde is te klein
				{
					max = columnMax;
					view.setMaxBoundary(max); // update in the view's settings
				}
			}
			else
			{
				min = Math.min(view.getMinBoundary(), columnMin);
			}
			model.setOptimizeScaleWithoutEvent(view.getUserOptionsPanel().isOptimizeScale());
			model.setMinOnScaleWithoutEvent(min);
			view.recalculateBinBoundaries(model.getColumnIndex(), false);
			model.setMaxOnScale(view.getMaxBinOnScale());
			view.getUserOptionsPanel().update();

		}
		else
		{
			//System.out.println("HistogramController.actionPerformed(): Unknown action source! " + e);
		}
	}

	/*
	 * Update the bin boundaries using the settings for the minimum boundary
	 * and the bin width, and determine the number of bins.
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
			this.model.setMinOnScale(view.getMinBoundary());
			this.model.setBinBoundaries(boundaries);
			this.model.setBinWidthWithoutEvent(view.getBinWidth());
		}
		else
		{
			// reset to old values
			ArrayList<Double> oldBoundaries = this.model.getBinBoundaries(); 
			this.view.setBinWidth();
			this.view.setMinBoundary(oldBoundaries.get(0));
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
			//this.view.setModel(this.model); // test syl: waarom moet dit hier en niet bij updateBoundariesFromBinSettings()?
		}
		else
		{
			// reset to old values
			ArrayList<Double> oldBoundaries = this.model.getSplitOptions().getBinBoundaries(); 
			this.view.setSplitBinWidth();
			this.view.setSplitMinBoundary(oldBoundaries.get(0));
		}
	}
	
	/*
	 * Update the bin boundaries with the set number of bins.
	 */
	private void updateBoundaries()
	{
		ArrayList<Double> boundaries = new ArrayList<Double>();
		for (int i = 0; i <= this.model.getNoBins(); i++)
		{
			boundaries.add(new Double(view.getMinBoundary() + i
				* view.getBinWidth()));
		}
		
		this.model.setBinBoundaries(boundaries);
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

	public String getViewType()
	{
		return (this.model.isFrequencyPolygonMode() ? "Frequentiepolygoon"
			: "Histogram");
	}

	public Object getState()
	{
//		System.out.println("HistogramController.getState()...");

		Hashtable h = new Hashtable();

		h.put("binBoundaries", this.model.getBinBoundaries());
		h.put("columnIndex", this.model.getColumnIndex());
		h.put("percentage", this.model.getPercentage());
		h.put("percentage_splitTotal", this.model.getPercentageSplitTotal());
		h.put("labelUnderBin", this.model.getLabelUnderBin());
		h.put("showUserOptions", this.model.getShowUserOptions());
		h.put("verticalBars", this.model.hasVerticalBars());
		h.put("viewName", this.model.getViewName());
		h.put("frequencyPolygonCumulativeMode",
			this.model.isFrequencyPolygonCumulativeMode());
		h.put("columnSplitIndex", this.model.getSplitOptions()
			.getColumnSplitIndex());
		h.put("splitBoundaries", this.model.getSplitOptions()
			.getBinBoundaries());
		h.put("splitInSingleView", this.model.isSplitInSingleView());
		h.put("nextToEachOther", this.model.isNextToEachOther());
		h.put("optimizeScale", this.model.isOptimizeScale());
		h.put("minOnScale", this.model.getMinOnScale());
		h.put("maxOnScale", this.model.getMaxOnScale());
		h.put("binWidth", this.model.getBinWidth());

		return h;
	}
	
	/**
	 * deeply clones a Hashtable by serializing and deserializing.
	 */
	private Hashtable<String, Object> deepCopy(Hashtable<String, Object> original)
	{
		Object copy = null;
		try
		{
			// Write the object out to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(original);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(
				new ByteArrayInputStream(bos.toByteArray()));
			copy = in.readObject();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
		return (Hashtable) copy;
	}

	public void setState(Object state)
	{
		if (!(state instanceof Hashtable))
		{
			return;
		}

		Hashtable h = deepCopy((Hashtable) state);

		if (h.containsKey("columnIndex"))
		{
			// Let op: setColumnIndex() zet ook de binBoundaries
			// Dat wordt hieronder goed gemaakt als de binBoundaries
			// uit de hashtable worden gezet.
			this.model.setColumnIndex(((Integer) h.get("columnIndex"))
				.intValue());
		}
		if (h.containsKey("binBoundaries"))
		{
			this.model.setBinBoundaries((ArrayList<Double>) h
				.get("binBoundaries"));
		}
		if (h.containsKey("percentage"))
		{
			this.model.setPercentage(((Boolean) h.get("percentage"))
				.booleanValue());
		}
		if (h.containsKey("percentage_splitTotal"))
		{
			this.model.setPercentageSplitTotal(((Boolean) h.get("percentage_splitTotal"))
				.booleanValue());
		}
		else
		{
			// default is true
			this.model.setPercentageSplitTotal(true);
		}
		
		if (h.containsKey("labelUnderBin"))
		{
			this.model.setLabelUnderBin(((Boolean) h.get("labelUnderBin"))
				.booleanValue());
//			System.out.println("HistogramController.setState(): labelUnderBin="
//				+ ((Boolean) h.get("labelUnderBin")).booleanValue());
		}
		if (h.containsKey("showUserOptions"))
		{
			this.model.setShowUserOptions(((Boolean) h.get("showUserOptions"))
				.booleanValue());
		}
		if (h.containsKey("verticalBars"))
		{
			this.model.setVerticalBars(((Boolean) h.get("verticalBars"))
				.booleanValue());
		}
		if (h.containsKey("viewName"))
		{
			this.model.setViewName((String) h.get("viewName"));
		}
		if (h.containsKey("frequencyPolygonCumulativeMode"))
		{
			this.model.setFrequencyPolygonCumulativeMode(((Boolean) h
				.get("frequencyPolygonCumulativeMode")).booleanValue());
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
		if (h.containsKey("splitInSingleView"))
		{
			this.model.setSplitInSingleView(((Boolean) h
				.get("splitInSingleView")).booleanValue());
		}
		if (h.containsKey("nextToEachOther"))
		{
			this.model.setNextToEachOther(((Boolean) h.get("nextToEachOther"))
				.booleanValue());
		}
		if (h.containsKey("optimizeScale"))
		{
			this.model.setOptimizeScale(((Boolean) h.get("optimizeScale"))
				.booleanValue());
		}
		if (h.containsKey("binWidth")) // bin width nodig voor berekenen maxonscale
		{
			this.model.setBinWidth(((Double) h.get("binWidth"))
				.doubleValue());
		}
		if (h.containsKey("minOnScale"))
		{
			this.model.setMinOnScale(((Double) h.get("minOnScale"))
				.doubleValue());
		}
		if (h.containsKey("maxOnScale"))
		{
			this.model.setMaxOnScale(((Double) h.get("maxOnScale"))
				.doubleValue());
		}
	}

	public String toString()
	{
		return this.getViewName();
	}

	public void focusGained(FocusEvent e)
	{
		// TODO Auto-generated method stub
	}

	public void focusLost(FocusEvent e)
	{
		if (e.getSource().equals(this.view.getUserOptionsPanel().getMinBoundaryTextField()))
		{
			processMinBoundaryChanged();
		}
		else if (e.getSource().equals(this.view.getUserOptionsPanel().getBinWidthTextField()))
		{
			processBinWidthChanged();
		}
		else if (e.getSource().equals(this.view.getUserOptionsPanel().getMaxOnScaleField()))
		{
			processMaxOnScaleChanged();
		} // maxOnScaleField
		
	} // focusLost()
	
	/**
	 * Process actions when max on scale has been changed.
	 */
	private void processMaxOnScaleChanged()
	{
		double maxOnScale = view.getUserOptionsPanel().getMaxOnScale();

		// column index bin settings
		updateBoundariesFromBinSettings();

		// max < min is niet toegestaan
		if (maxOnScale < view.getUserOptionsPanel().getMinBoundary())
		{
			// reset to latest value
			view.getUserOptionsPanel().setMaxOnScale(model.getMaxOnScale());
		}
		else
		{
			double maxBinValue = model.getMaxBinBoundaryValue();

			// alleen check of data binnen grenzen als optimize scale
			if (model.isOptimizeScale())
			{
				if (maxOnScale < maxBinValue) // the new user entered max value is not correct
				{
					if (model.getMaxOnScale() < maxBinValue) // the model's max value is not correct
					{
						// the model's max is not correct, data may have been changed and the model's max on scale needs to be reset
						model.setMaxOnScale(maxBinValue);
					}
					else
					{
						// reset to latest value
						view.getUserOptionsPanel().setMaxOnScale(model.getMaxOnScale());
					}
				}
				else
				{
					// everything is fine, set the value
					model.setMaxOnScale(maxOnScale);
				}
			}
			else
			{
				model.setMaxOnScale(maxOnScale);
			}
			
			if (model.getBinWidth() == 0)
			{
				model.setBinWidth(maxOnScale - model.getMinOnScale());
			}
		}
	}

	/**
	 * Process actions when minimum boundary has been changed.
	 */
	private void processMinBoundaryChanged()
	{
		double minOnScale = view.getUserOptionsPanel().getMinBoundary();
		double maxOnScale = view.getUserOptionsPanel().getMaxOnScale();

		if (model.getStatTableModel().isEmptyColumn(model.getColumnIndex()))
		{
			if (minOnScale > maxOnScale)
			{
				model.setMinOnScale(minOnScale);
				view.getUserOptionsPanel().setMinBoundary(minOnScale);
				model.setMaxOnScale(minOnScale);
				view.getUserOptionsPanel().setMaxOnScale(minOnScale);
			}
			else
			{
				model.setMinOnScale(minOnScale);
				view.getUserOptionsPanel().setMinBoundary(minOnScale);
			}
		} // empty column
		else
		{ // data in column
			
			// max < min is niet toegestaan
			if (minOnScale > maxOnScale)
			{
				// reset to latest value
				view.getUserOptionsPanel().setMinBoundary(model.getMinOnScale());
			}
			else
			{
				double dataMin = model.getStatTableModel().getColumnMin(model.getColumnIndex());
	
				// alleen check of data binnen grenzen als optimize scale
				if (model.isOptimizeScale())
				{
					if (minOnScale > dataMin) // the new user entered min value is not correct
					{
						if (model.getMinOnScale() > dataMin) // the model's min value is not correct
						{
							// the model's min is not correct, data may have been changed and the model's min on scale needs to be reset
							model.setMinOnScale(dataMin);
							view.getUserOptionsPanel().setMinBoundary(dataMin);
						}
						else
						{
							// reset to latest value
							view.getUserOptionsPanel().setMinBoundary(model.getMinOnScale());
						}
					}
					else
					{
						// everything is fine, set the value
						model.setMinOnScale(minOnScale);
						view.getUserOptionsPanel().setMinBoundary(minOnScale);
					}
				}
				else
				{
					model.setMinOnScale(minOnScale);
					view.getUserOptionsPanel().setMinBoundary(minOnScale);
				}
				
				// update column index bin settings
				updateBoundariesFromBinSettings();
	
				// zorg dat maximumwaarde overeenkomt met de hoogste bin waarde op de schaal
				double maxBinValue = view.getMaxBinOnScale();
				if (view.getUserOptionsPanel().getMaxOnScale() < maxBinValue)
				{
					view.getUserOptionsPanel().setMaxOnScale(maxBinValue);
					model.setMaxOnScale(maxBinValue);
				}
				else
				{
					// kom je hier ooit...?
					model.setMaxOnScale(view.getUserOptionsPanel().getMaxOnScale());
				}
	
				if (model.getBinWidth() == 0)
				{
					model.setBinWidth(maxOnScale - model.getMinOnScale());
				}
			}
		} // data in column
	}

	/**
	 * Process actions when bin width has been changed.
	 */
	private void processBinWidthChanged()
	{
		if (view.getBinWidth() <= 0) // zero or negative is not a valid value
		{
			// reset to the latest value
			model.setBinWidth(model.getBinWidth());
		}
		else
		{
			model.setBinWidthWithoutEvent(view.getBinWidth());
			
			// column index bin settings
			updateBoundariesFromBinSettings();

			// zorg dat maximumwaarde overeenkomt met de hoogste bin waarde op de schaal
			double maxBinValue = view.getMaxBinOnScale();
			if (view.getUserOptionsPanel().getMaxOnScale() < maxBinValue)
			{
				view.getUserOptionsPanel().setMaxOnScale(maxBinValue);
				model.setMaxOnScale(maxBinValue);
			}
			else
			{
				// kom je hier ooit...?
				model.setMaxOnScale(view.getUserOptionsPanel().getMaxOnScale());
			}
		}
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

	public void processSplitBinWidthChanged()
	{
		// update split index bin settings
		this.updateSplitBoundariesFromBinSettings();
	}


}
