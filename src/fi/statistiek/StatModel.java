package fi.statistiek;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Statistiek MVC Model
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class StatModel extends Observable implements TableModelListener
{
	private StatTableModel data;

	// Only resetting a StatTableModel is causing too much trouble with respect
	// to updating views
	// private StatTableModel resetData; // the data to be reset

	Hashtable resetHashtable;

	private ArrayList<StatistiekView> views;
	private ArrayList<Boolean> viewInOwnWindow;

	/**
	 * Constructor
	 */
	public StatModel()
	{
		this.views = new ArrayList<StatistiekView>();
		this.viewInOwnWindow = new ArrayList<Boolean>();
		this.data = new StatTableModel();
		this.resetHashtable = new Hashtable();

		this.data.addTableModelListener(this);

		this.addView(Statistiek.createView("Table",
			Statistiek.rb.getString("tableOption") + " 1", this.data, 0, 0, null));
	}

	/**
	 * Find the lowest i >= 1 for which the view name "viewTypeName i" is free
	 * 
	 * @param viewTypeName
	 *            The name of the type of view, element of Statistiek.VIEWS
	 * @return an unique viewname
	 */
	public String findUniqueViewName(String viewTypeName)
	{
		int i;
		findI: for (i = 1; true; i++)
		{
			String s = viewTypeName + " " + i;
			for (StatistiekView view : this.views)
			{
				if (view.getViewName().equals(s))
				{
					// try next i
					continue findI;
				}
			}

			return viewTypeName + " " + i;
		}
	}

	/**
	 * Add a StatistiekView
	 * 
	 * @param view
	 *            The view that will be added
	 */
	public void addView(StatistiekView view)
	{
		this.views.add(view);
		this.viewInOwnWindow.add(false);

		super.setChanged();
		super.notifyObservers();
	}

	public int mainWindowIndexToGeneralIndex(int mainWindowIndex)
	{
		int mainViews = 0;
		int separateViews = 0;
		while (mainViews <= mainWindowIndex)
		{
			if ((mainViews + separateViews) < viewInOwnWindow.size()
				&& this.viewInOwnWindow.get(mainViews + separateViews))
			{
				separateViews++;
			}
			else
			{
				mainViews++;
			}
		}

		return mainViews + separateViews - 1;
	}

	public void setViewSeparateWindowByObject(StatistiekView sv, boolean b)
	{
		int i = this.views.indexOf(sv);
		if (i >= 0)
		{
			this.setViewSeparateWindow(i, b);
		}
	}

	public void setViewSeparateWindow(int viewIndex, boolean b)
	{
		System.out.println("View " + viewIndex
			+ " is set to show in separate window: " + b);
		this.viewInOwnWindow.set(viewIndex, b);
		super.setChanged();
		super.notifyObservers();
	}

	/**
	 * Remove a view by name
	 * 
	 * @param viewName
	 *            the name of the view that will be removed
	 */
	public void removeView(String viewName)
	{
		for (int i = 0; i < this.views.size(); i++)
		{
			if (this.views.get(i).getViewName().equals(viewName))
			{
				this.removeView(i);
				break;
			}
		}
	}

	/**
	 * Remove a view by index
	 * 
	 * @param viewIndex
	 *            the index of the view that will be removed
	 */
	public void removeView(int viewIndex)
	{
		this.views.remove(viewIndex);
		this.viewInOwnWindow.remove(viewIndex);
		super.setChanged();
		super.notifyObservers();
	}

	/**
	 * Get the added views
	 * 
	 * @return ArrayList containing all views
	 */
	public ArrayList<StatistiekView> getViews()
	{
		return this.views;
	}

	/**
	 * Get the views that should be displayed in the main window
	 * 
	 * @return ArrayList of StatistiekViews that should be displayed in the main
	 *         window
	 */
	public ArrayList<StatistiekView> getMainWindowViews()
	{
		ArrayList<StatistiekView> mainWindowViews = new ArrayList<StatistiekView>();
		for (int i = 0; i < this.views.size(); i++)
		{
			if (!this.viewInOwnWindow.get(i))
			{
				mainWindowViews.add(this.views.get(i));
			}
		}

		return mainWindowViews;
	}

	/**
	 * Get the views that should be displayed in a separate window
	 * 
	 * @return ArrayList of StatistiekViews that should be displayed in a
	 *         separate window
	 */
	public ArrayList<StatistiekView> getSeparateWindowViews()
	{
		ArrayList<StatistiekView> separateWindowViews = new ArrayList<StatistiekView>();
		for (int i = 0; i < this.views.size(); i++)
		{
			if (this.viewInOwnWindow.get(i))
			{
				separateWindowViews.add(this.views.get(i));
			}
		}

		return separateWindowViews;
	}

	public ArrayList<Boolean> getViewInOwnWindow()
	{
		return this.viewInOwnWindow;
	}

	public void setViewInOwnWindow(ArrayList<Boolean> viewInOwnWindow)
	{
		this.viewInOwnWindow = viewInOwnWindow;
		super.setChanged();
		super.notifyObservers();
	}

	/**
	 * Get the data
	 * 
	 * @return the data in a StatTableModel
	 */
	public StatTableModel getData()
	{
		return this.data;
	}

	/**
	 * Get the reset hashtable
	 * 
	 * @return the reset hashtable
	 */
	public Hashtable getResetHashtable()
	{
		// System.out.println("StatModel.getResetHashtable(): resetHashtable=" +
		// resetHashtable);
		return this.resetHashtable;
	}

	/**
	 * Remove all views
	 */
	public void removeViewsWithoutEvent()
	{
		// test syl
        Iterator<StatistiekView> iterator = this.views.iterator();
        while (iterator.hasNext()) 
        {
        	StatistiekView view = iterator.next();
        	if (view != null)
        		iterator.remove();
        }

		this.views = new ArrayList<StatistiekView>();
		this.viewInOwnWindow = new ArrayList<Boolean>();
		// this.setChanged();
		// this.notifyObservers();
	}

	/**
	 * Change data table
	 * 
	 * @param data
	 *            the new data table
	 */
	public void setData(StatTableModel data)
	{
		this.data = data;
		this.data.addTableModelListener(this);
		super.setChanged();
		super.notifyObservers();
	}

	/**
	 * Set the reset hashtable
	 * 
	 * @param data
	 *            the reset hashtable
	 */
	public void setResetHashtable(Hashtable h)
	{
		// System.out.println("StatModel.setResetHashtable(h=" + h + ")");
		this.resetHashtable = h;
	}

	public void tableChanged(TableModelEvent e)
	{
		super.setChanged();
		super.notifyObservers();
	}
}
