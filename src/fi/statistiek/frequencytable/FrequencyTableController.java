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

import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.StatistiekView;
import fi.statistiek.histogram.DefineBinBoundariesDialog;

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
		model.setColumnIndex(startVar);
		this.view = new FrequencyTableView(this.model, this);

	}

	public void actionPerformed(ActionEvent arg0)
	{
		String action = arg0.getActionCommand();
		if (action.equals("columnIndexBox"))
		{
			this.model.setColumnIndex(this.view.varBoxSelectedIndex());
		}
		else if (action.equals("showPercBox"))
		{
			this.model.setShowPercentage(this.view.isShowPercBoxSelected());
		}
		else if (action.equals("showFreqBox"))
		{
			this.model.setShowFreq(this.view.isShowFreqBoxSelected());
		}
		else if (action.equals("showCumulativeBox"))
		{
			this.model.setShowCumulative(this.view
				.isShowCumulativeBoxSelected());
		}
		else if (action.equals("noBinsField"))
		{
			String s = this.view.getNoBinsFieldText();
			try
			{
				int i = Integer.parseInt(s);
				this.model.setNoBins(i);
			}
			catch (NumberFormatException e)
			{
				this.view.update(null, null);
			}
		}
		else if (action.equals("chooseBinsButton"))
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
		h.put("showFrequency", this.model.isShowFreq());
		h.put("binBoundaries", this.model.getBinBoundaries());
		h.put("viewName", this.getViewName());
		h.put("columnIndex", this.model.getColumnIndex());

		return h;
	}

	public void setState(Object state)
	{
		Hashtable h = (Hashtable) state;

		if (h.containsKey("showPercentage"))
		{
			this.model.setShowPercentage((Boolean) h.get("showPercentage"));
		}
		if (h.containsKey("showCumulative"))
		{
			this.model.setShowCumulative((Boolean) h.get("showCumulative"));
		}
		if (h.containsKey("showFrequency"))
		{
			this.model.setShowFreq((Boolean) h.get("showFrequency"));
		}
		if (h.containsKey("binBoundaries"))
		{
			this.model.setBinBoundaries((ArrayList<Double>) h
				.get("binBoundaries"));
		}
		if (h.containsKey("viewName"))
		{
			this.setViewName((String) h.get("viewName"));
		}
		if (h.containsKey("columnIndex"))
		{
			this.model.setColumnIndex((Integer) h.get("columnIndex"));
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
		System.out.println("focusLost");
		if (arg0.getSource() == this.view.getNoBinsField())
		{
			System.out.println("focusLost van nobinsField");
			String s = this.view.getNoBinsFieldText();
			try
			{
				int i = Integer.parseInt(s);
				this.model.setNoBins(i);
			}
			catch (NumberFormatException e)
			{
				this.view.update(null, null);
			}
		}

	}

}
