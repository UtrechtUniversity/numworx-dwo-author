package fi.statistiek.piechart;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JComponent;

import fi.statistiek.StatTableModel;
import fi.statistiek.StatistiekView;

/**
 * MVC Controller for StatistiekView PieChart
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class PieChartController implements StatistiekView,	ActionListener
{
	private PieChartModel model;
	private PieChartView view;
	private int width;
	private int height;

	/**
	 * Constructor
	 * 
	 * @param tableModel
	 *            the data model
	 * @param viewName
	 *            the initial name of the view
	 */
	public PieChartController(StatTableModel tableModel, String viewName,
		int startVar, int width, int height)
	{
		setWidth(width);
		setHeight(height);
		this.model = new PieChartModel(tableModel, viewName);
		this.model.setColumnIndex(startVar);
		this.view = new PieChartView(this.model, this);
		this.view.update(null, null);
	}
	
	/**
	 * ActionListener implementation
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		String action = arg0.getActionCommand();
		
		if (action.equals("columnIndexBox"))
		{
			this.model.setColumnIndex(this.view.varBoxSelectedIndex());
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
		return "Cirkeldiagram";
	}

	public Object getState()
	{
		Hashtable h = new Hashtable();

		h.put("viewName", this.getViewName());
		h.put("columnIndex", this.model.getColumnIndex());

		return h;
	}

	public void setState(Object state)
	{
		Map h = (Map) state;
		
		if (h.containsKey("viewName"))
		{
			this.setViewName((String) h.get("viewName"));
		}
		if (h.containsKey("columnIndex"))
		{
			this.model.setColumnIndex(((Number) h.get("columnIndex")).intValue());
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
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public void setWidth(int w)
	{
		this.width = w;
	}
	
	public void setHeight(int h)
	{
		this.height = h;
	}
	
	/**
	 * Override setBounds
	 */
	public void setBounds(int x, int y, int b, int h)
	{
		setWidth(b);
		setHeight(h);
	}

}
