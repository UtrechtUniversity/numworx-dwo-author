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
 * @author Manu Drijvers
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
		
//		System.out.println("... HistogramController(): this.model.binBoundaries=" + this.model.getBinBoundaries());
//		System.out.println("... HistogramController(): identityHashCode(this)=" 
//			+ identityHashCode(this));
//		System.out.println("... HistogramController(): identityHashCode(this.model.getBinBoundaries())=" 
//			+ identityHashCode(this.model.getBinBoundaries()));
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

//		System.out.println("HistogramController.actionPerformed(): ac=" + ac);

		if (ac.equals("amountRadioItem") || ac.equals("percentageRadioItem"))
		{
			this.model.setPercentage(this.view.percentageItemSelected());
		}
		else if (ac.equals("labelsBetweenBinsRadioItem") ||
			ac.equals("labelsUnderBinRadioItem"))
		{
			this.model.setLabelUnderBin(this.view.labelUnderBinItemSelected());
		}
		else if (ac.equals("varBox"))
		{
			this.model.setColumnIndex(this.view.getVarBoxSelectedIndex());
			//System.out.println("Var set to " + this.model.getColumnIndex());
		}
		else if (ac.equals("binsBox"))
		{
			this.model.setNoBins(this.view.getBinsBoxSelectedInt());
		}
		else if (ac.equals("minBoundary"))
		{
			updateBoundariesFromBinSettings();
		}
		else if (ac.equals("binWidth"))
		{
			updateBoundariesFromBinSettings();
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
		else if (ac.equals("splitVarBox"))
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
				// boolean b = this.view.isNextToEachOtherSelected();
				// this.model.setNextToEachOther(!b);
				// this.model.setNextToEachOther(b);

				// als je een splitsvariabele kiest, dan wordt de
				// splitsing effectief
				this.model.setSplitInSingleView(false);
			}
		}
		else if (ac.equals("splitBinsBox"))
		{
			this.setSplitType(this.model.getTableModel().getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex())
				.getType());
		}
		else if (ac.equals("splitMinBoundary"))
		{
			updateSplitBoundaries();
		}
		else if (ac.equals("splitBinWidth"))
		{
			updateSplitBoundaries();
		}
		/*
		 * else if(ac.equals("splitButton")) { Container c =
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
		else if (ac.equals("chooseBinsButton"))
		{
			Container c = Statistiek.getTopLevelAcestor(this.view);

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

		else
		{
			System.out.println("Unknown action source! " + e);
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
		
		boundaries = Statistiek.appropriateBoundariesFromBinSettings(
			this.model.getTableModel().getColumnMin(
				this.model.getColumnIndex()),
			this.model.getTableModel().getColumnMax(
				this.model.getColumnIndex()),
			view.getBinWidth(),
			view.getMinBoundary());
		this.model.setBinBoundaries(boundaries);
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
			boundaries.add(new Double(view.getSplitminBoundary() + i
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
//		System.out.println("   binBoundaries=" +
//			this.model.getBinBoundaries());

		h.put("columnIndex", this.model.getColumnIndex());
		// System.out.println("   columnIndex=" + this.model.getColumnIndex());

		h.put("percentage", this.model.getPercentage());
		// System.out.println("   percentage=" + this.model.getPercentage());

		h.put("labelUnderBin", this.model.getLabelUnderBin());
		//System.out.println("   labelUnderBin=" + this.model.getLabelUnderBin());

		h.put("showUserOptions", this.model.getShowUserOptions());
		// System.out.println("   showUserOptions=" +
		// this.model.getShowUserOptions());

		h.put("verticalBars", this.model.getVerticalBars());
		// System.out.println("   verticalBars=" +
		// this.model.getVerticalBars());

		h.put("viewName", this.model.getViewName());
		// System.out.println("   viewName=" + this.model.getViewName());

		h.put("frequencyPolygonCumulativeMode",
			this.model.isFrequencyPolygonCumulativeMode());
		// System.out.println("   frequencyPolygonCumulativeMode=" +
		// this.model.isFrequencyPolygonCumulativeMode());

		h.put("columnSplitIndex", this.model.getSplitOptions()
			.getColumnSplitIndex());
		// System.out.println("   columnSplitIndex=" +
		// this.model.getSplitOptions().getColumnSplitIndex());

		h.put("splitBoundaries", this.model.getSplitOptions()
			.getBinBoundaries());
		// System.out.println("   splitBoundaries=" +
		// this.model.getSplitOptions().getBinBoundaries());

		h.put("splitInSingleView", this.model.isSplitInSingleView());
		// System.out.println("   splitInSingleView=" +
		// this.model.isSplitInSingleView());

		h.put("nextToEachOther", this.model.isNextToEachOther());
		// System.out.println("   nextToEachOther=" +
		// this.model.isNextToEachOther());

		// System.out.println("END HistogramController.getState()...");

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
//			System.out.println("HistogramController.setState(): binBoundaries="
//				+ (ArrayList<Double>) h.get("binBoundaries"));
			this.model.setBinBoundaries((ArrayList<Double>) h
				.get("binBoundaries"));
//			System.out.println("... (setState) identityHashCode(this.model)=" + identityHashCode(this.model));
//			System.out.println("... (setState) identityHashCode(this.model.getBinBoundaries())=" + identityHashCode(this.model.getBinBoundaries()));
//			System.out.println("... (setState) identityHashCode(this.view)=" + identityHashCode(this.view));
//			System.out.println("... (setState) identityHashCode(this.view.getModel().getBinBoundaries())=" + identityHashCode(this.view.getModel().getBinBoundaries()));
//			System.out.println("... (setState) identityHashCode(this)=" + identityHashCode(this));
		}
		if (h.containsKey("percentage"))
		{
			this.model.setPercentage(((Boolean) h.get("percentage"))
				.booleanValue());
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
//		System.out.println("HistogramController.focusLost(): e.getSource()="
//			+ e.getSource());

		updateBoundariesFromBinSettings();
	}
}
