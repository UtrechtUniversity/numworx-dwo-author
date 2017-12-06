package fi.statistiek;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.MouseInputListener;

import fi.statistiek.Statistiek;

/**
 * Statistiek InteractiePanel MVC View
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class StatInteractiePanelView extends JPanel implements Observer
{
	private static final String RESET_ICON_PATH = "resources/reseticon.gif";
	protected StatModel model;
	private StatInteractiePanel controller;
	private static JLabel NO_VIEWS_LABEL = new JLabel("No views added.");
	private JTabbedPane tabPane;
	private JPanel addViewTab;
	private JComboBox viewsBox, startVarBox, startVar2Box;
	private JLabel addViewLabel;
	private JLabel chooseStartVarLabel, chooseStartVar2Label;

	// button to reset the data -> button now implemented in StatTable for
	// layout reasons
	// keep the code, because StatInteractiePanelView is a more logical place
	// private JButton resetButton;

	// The index of the selected view
	private int selectedView;

	// The index of the previous selected view
	private int previousSelectedView = 0;

	// The index of the selected view in tabPane
	private int selectedViewInTabPane = 0;

	private ArrayList<SeparateViewDialog> dialogs;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            MVC Model
	 * @param controller
	 *            MVC Controller
	 */
	public StatInteractiePanelView(StatModel model, StatInteractiePanel controller)
	{
		super(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.model = model;
		this.model.addObserver(this);
		this.controller = controller;
		this.dialogs = new ArrayList<SeparateViewDialog>();

		this.addViewTab = new JPanel(new FlowLayout());

		this.addViewLabel = new JLabel(Statistiek.rb.getString("addaviewKnopTekst"));
		this.addViewLabel.setPreferredSize(new Dimension(150, 20));

		this.chooseStartVarLabel = new JLabel(Statistiek.rb.getString("chooseStartVarLabel"));
		this.chooseStartVarLabel.setPreferredSize(new Dimension(150, 20));
		this.chooseStartVarLabel.setVisible(false);

		this.chooseStartVar2Label = new JLabel(Statistiek.rb.getString("chooseStartVarColumnLabel"));
		this.chooseStartVar2Label.setPreferredSize(new Dimension(150, 20));
		this.chooseStartVar2Label.setVisible(false);

		String[] options = new String[Statistiek.VIEWS_translated.length + 1];
		options[0] = Statistiek.rb.getString("chooseaviewOption");
		for (int i = 0; i < Statistiek.VIEWS_translated.length; i++)
		{
			options[i + 1] = Statistiek.VIEWS_translated[i];
		}
		this.viewsBox = new JComboBox(options);
		this.viewsBox.setPreferredSize(new Dimension(150, 24));
		this.viewsBox.setActionCommand("viewsBox");
		this.viewsBox.addActionListener(controller);
		this.viewsBox.setMaximumRowCount(9); // default is 8; liever niet scrollen voor 1 extra optie

		this.startVarBox = new JComboBox();
		this.startVarBox.setPreferredSize(new Dimension(150, 24));
		this.startVarBox.setActionCommand("startVarBox");
		this.startVarBox.addActionListener(this.controller);
		this.startVarBox.setVisible(false);

		this.startVar2Box = new JComboBox();
		this.startVar2Box.setPreferredSize(new Dimension(150, 24));
		this.startVar2Box.setActionCommand("startVar2Box");
		this.startVar2Box.addActionListener(this.controller);
		this.startVar2Box.setVisible(false);

		// Create the reset button -> button now implemented in StatTable
		// this.resetButton = new JButton();
		// try
		// {
		// Image img = ImageIO.read(getClass().getResource(RESET_ICON_PATH));
		// resetButton.setIcon(new ImageIcon(img));
		// }
		// catch (IOException ex)
		// {
		// }
		// this.resetButton.setActionCommand("reset");
		// this.resetButton.addActionListener(this.controller);

		Box hb1 = Box.createHorizontalBox();
		hb1.add(addViewLabel);
		hb1.add(Box.createHorizontalStrut(10));
		hb1.add(viewsBox);
		hb1.add(Box.createHorizontalGlue());

		Box hb2 = Box.createHorizontalBox();
		hb2.add(chooseStartVarLabel);
		hb2.add(Box.createHorizontalStrut(10));
		hb2.add(startVarBox);
		hb2.add(Box.createHorizontalGlue());

		Box hb3 = Box.createHorizontalBox();
		hb3.add(chooseStartVar2Label);
		hb3.add(Box.createHorizontalStrut(10));
		hb3.add(startVar2Box);
		hb3.add(Box.createHorizontalGlue());

		Box vb = Box.createVerticalBox();
		vb.add(Box.createVerticalStrut(25));
		vb.add(hb1);
		vb.add(Box.createVerticalStrut(15));
		vb.add(hb2);
		vb.add(Box.createVerticalStrut(15));
		vb.add(hb3);

		Box hb = Box.createHorizontalBox();
		hb.add(Box.createHorizontalStrut(20));
		hb.add(vb);
		hb.add(Box.createHorizontalGlue());

		addViewTab.add(hb);

		// addViewTab.add(p);

		this.update(null, null);
	}

	public void clearAddViewTab()
	{
		this.viewsBox.removeActionListener(controller);
		viewsBox.setSelectedIndex(0);
		this.viewsBox.addActionListener(controller);
		this.startVarBox.removeActionListener(controller);
		startVarBox.setSelectedIndex(0);
		this.startVarBox.addActionListener(controller);
		startVarBox.setVisible(false);
		chooseStartVarLabel.setVisible(false);
		
		this.startVar2Box.removeActionListener(controller);
		startVar2Box.setSelectedIndex(0);
		this.startVar2Box.addActionListener(controller);
		startVar2Box.setVisible(false);
		chooseStartVar2Label.setVisible(false);
	}

	public void setStartVarBox(boolean b)
	{
		startVarBox.setVisible(b);
		chooseStartVarLabel.setVisible(b);
	}
	
	public void setStartVarLabel(String s)
	{
		chooseStartVarLabel.setText(s);
	}

	public void setStartVar2Label(String s)
	{
		chooseStartVar2Label.setText(s);
	}

	public void setStartVar2Box(boolean b)
	{
		startVar2Box.setVisible(b);
		chooseStartVar2Label.setVisible(b);
	}

	/**
	 * Get the model
	 */
	public StatModel getModel()
	{
		return model;
	}

	/**
	 * Get the controller
	 */
	public StatInteractiePanel getController()
	{
		return controller;
	}

	/**
	 * Set new model
	 * 
	 * @param model
	 *            The new model
	 */
	public void setModel(StatModel model)
	{
		if (this.model != model)
		{
			System.out.println("StatInteractiePanelView.setModel()");
			this.model.deleteObserver(this);
			this.model = model;
			this.model.addObserver(this);
			this.update(null, null);
		}
	}

	/**
	 * @return The index of the currently selected view
	 */
	public int getSelectedView()
	{
		return selectedView;
	}

	/**
	 * @return The index of the previous selected view
	 */
	public int getPreviousSelectedView()
	{
		return previousSelectedView;
	}

	/**
	 * Set the field previousSelectedView
	 * 
	 * @param view
	 *            The index of the previous selected view
	 */
	public void setPreviousSelectedView(int view)
	{
//		System.out.println("setPreviousSelectedView(view=" + view + ")");
		previousSelectedView = view;
	}

	/**
	 * Process the actions related to selecting a view: Set the selected view,
	 * the previous selected view, and update the selected view in tabPane. This
	 * method is called when a view in own window is activated.
	 * 
	 * @param view
	 *            the index of the view that has to be selected
	 */
	public void processSelectedView(int view)
	{
		// System.out.println("*** begin *** StatInteractiePanelView.processSelectedView(view="
		// + view + ")");

		if (view < this.model.getViews().size()
			&& this.model.getViews().size() > 1) // check if valid view number
		{
			// set the previous selected view
			if (view != selectedView)
			{
				setPreviousSelectedView(selectedView);
			}
			else
				// System.out.println("view = selectedView!");

				// set the selected view
				setSelectedView(view);

			// update the selected view in tabPane
			setTabPane(selectedViewInTabPane);
		}
		// System.out.println("*** end *** StatInteractiePanelView.processSelectedView(view="
		// + view + ")");
	}

	/**
	 * Set the field selectedView
	 * 
	 * @param view
	 */
	private void setSelectedView(int view)
	{
		// System.out.println("setSelectedView(view=" + view + ")");
		selectedView = view;
	}

	/**
	 * Select the correct tab in tabPane.
	 */
	private void setTabPane(int view)
	{
		// maak selectedViewInTabPane zichtbaar in tabPane
		selectViewInTabPane(view);
	}

	/**
	 * Select view in tabPane
	 * 
	 * @param view
	 */
	private void selectViewInTabPane(int view)
	{
		int tab;

		// Determine the tab number of view in tabPane
		// taking into account views in own window
		tab = determineTab(view);

		tabPane.setSelectedIndex(tab);
	}

	/**
	 * Determine the tab number of view in tabPane taking into account views in
	 * own window
	 * 
	 * @param view
	 * @return
	 */
	private int determineTab(int view)
	{
		int tab = 0;
		int count = 0;

		ArrayList<Boolean> viewInOwnWindow = getModel().getViewInOwnWindow();
//		System.out.println("StatInteractiePanelView.determineTab(" + view 
//			+ "): viewInOwnWindow.size() = " + viewInOwnWindow.size());
 
		// System.out.println("determineTab(view=" + view +
		// "): viewInOwnWindow=" + viewInOwnWindow);

		try
		{
			if (viewInOwnWindow.get(view).booleanValue())
			{
				// view is not in tabPane
				tab = determineTab(previousSelectedView);
			}
			else
			{
				for (int i = 0; i < view; i++)
				{
					if (!viewInOwnWindow.get(i).booleanValue())
					{
						// count the number of views in tabPane
						count++;
					}
				}
				tab = count;
			}
		}
		catch (Exception e)
		{
			// caused by triggering updates while part of the objects are still in old state
			tab = 0;
		}

		// System.out.println("determineTab(view=" + view + "): count=" +
		// count);

		return tab;
	}

	/**
	 * Process the actions related to selecting a tab: Set the selected tab in
	 * the tabbed pane and update selectedView and previousSelectedView. This
	 * method is called when a tab in tabPane is selected.
	 * 
	 * @param tab
	 *            the index of the tab that refers to the view that has to be
	 *            selected
	 */
	public void processSelectedTab(int tab)
	{
		// System.out.println("...begin... StatInteractiePanelView.processSelectedTab(tab="
		// + tab + "): this.model.getViews().size() = " +
		// this.model.getViews().size());

		if (tab < this.model.getViews().size()
			&& this.model.getViews().size() > 1)
		{
			// determine the view related to tab
			int view = determineView(tab);

			// set the field selectedViewInTabPane to keep track of the visible
			// tab in tabPane in case of views in own window
			setSelectedViewInTabPane(view);

			// set previous selected view
			if (view != selectedView)
			{
				setPreviousSelectedView(selectedView);
			}
			// else
			// System.out.println("tab = selectedView!");

			setSelectedView(view);

			this.tabPane.setSelectedIndex(tab);
			// System.out.println("StatInteractiePanelView.processSelectedTab(): tabPane.setSelectedIndex(tab="
			// + tab + ")");
		}
		// System.out.println("...end.... StatInteractiePanelView.processSelectedTab");
	}

	/**
	 * Set the field selectedViewInTabPane.
	 * 
	 * @param view
	 */
	public void setSelectedViewInTabPane(int view)
	{
		selectedViewInTabPane = view;
		// System.out.println("setSelectedViewInTabPane(view=" + view + ")");
	}

	/**
	 * Determine the view number related to tab in tabPane.
	 * 
	 * @param tab
	 * @return
	 */
	private int determineView(int tab)
	{
		int count = 0;
		int view = 0;

		ArrayList<Boolean> viewInOwnWindow = getModel().getViewInOwnWindow();

		// System.out.println("determineView(tab=" + tab + "): viewInOwnWindow="
		// + viewInOwnWindow);

		for (int i = 0; i < viewInOwnWindow.size(); i++)
		{
			if (!viewInOwnWindow.get(i).booleanValue())
			{
				count++;
			}
			if (count == tab + 1)
			{
				view = i;
				break;
			}
		}

		// System.out.println("determineView(tab=" + tab + "): view = " + view);

		return view;
	}

	/**
	 * Check whether view is shown in its own window.
	 * @param view
	 * @return true if view is shown in its own window, else return false
	 */
	private boolean isInOwnWindow(int view)
	{
		ArrayList<Boolean> viewInOwnWindow = getModel().getViewInOwnWindow();

		try
		{
			if ((viewInOwnWindow.size() > 0) && viewInOwnWindow.get(view))
				return true;
			else
				return false;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public int indexOfTabComponent(Component tabComponent)
	{
		return this.tabPane.indexOfTabComponent(tabComponent);
	}

	/**
	 * Tests whether there is a dialog showing the statistiekview "sv"
	 * 
	 * @param sv
	 *            The StatistiekView of which we want to know whether its
	 *            currently being displayed in a dialog
	 * @return true if there is a dialog showing sv
	 */
	private boolean dialogExists(StatistiekView sv)
	{
		for (SeparateViewDialog dialog : this.dialogs)
		{
			if (sv == dialog.sv)
			{
//				System.out.println("Dialog for statistiekview "
//					+ sv.getViewName() + " already exists.");
				return true;
			}
		}

//		System.out.println("Dialog for statistiekview " + sv.getViewName()
//			+ " doesn't exist yet");
		return false;
	}

	/**
	 * Shows a StatistiekView in a separate dialog
	 * 
	 * @param sv
	 *            The statistiekview that will be shown in a separate dialog
	 * @param location
	 *            The initial centerlocation of the dialog
	 */
	public void showViewInDialog(StatistiekView sv, Point location)
	{
		Container owner = Statistiek.getTopLevelAncestor(this);
		if (owner instanceof Frame)
		{
			Frame frameOwner = (Frame) owner;
			SeparateViewDialog dialog = new SeparateViewDialog(sv, frameOwner);
			this.dialogs.add(dialog);
			dialog.setLocation(location);
			dialog.setVisible(true);
		}
		else if (owner instanceof Dialog)
		{
			Dialog dialogOwner = (Dialog) owner;
			SeparateViewDialog dialog = new SeparateViewDialog(sv, dialogOwner);
			this.dialogs.add(dialog);
			dialog.setLocation(location);
			dialog.setVisible(true);
		}
		else
		{
			System.out.println("Error finding top level frame/dialog");
		}
	}

	/**
	 * Select the last real tab (not the add view tab) in the tabPane
	 */
	public void selectLastTab()
	{
		this.processSelectedTab(this.tabPane.getTabCount() - 2);
	}

	public void update(Observable arg0, Object arg1)
	{
		//System.out.println("StatInteractiePanelView.update()");
		super.setVisible(false);
		super.removeAll(); // this can be very slow if there are many children
		super.setVisible(true);

		ArrayList<StatistiekView> views = this.model.getMainWindowViews();
		ArrayList<StatistiekView> separateWindowViews = this.model.getSeparateWindowViews();

		int amountOfTabs = views.size();
		if (this.model.getStatTableModel().isViewsAddable())
		{
			amountOfTabs++;
		}

		// draw main window
		if (amountOfTabs == 0)
		{
			// System.out.println("no views added");
			super.add(NO_VIEWS_LABEL);
		}
		else if (views.size() == 1 && !this.model.getStatTableModel().isViewsAddable())
		{
			// System.out.println("one view added");
			super.add(views.get(0).getComponent(), BorderLayout.CENTER);
		}
		else
		{
			// System.out.println(">1 views added");
			this.tabPane = new JTabbedPane();
			this.tabPane.setFont(Statistiek.font);
			if (this.model.getStatTableModel().isViewsAddable())
			{
				for (int i = 0; i < views.size(); i++)
				{
					StatistiekView view = views.get(i);
					this.tabPane.add(view.getComponent(), view.getViewName());
					this.tabPane.setTabComponentAt(i, new ButtonTabComponent(
						this.tabPane, this.controller));
				}
			}
			else
			{
				for (StatistiekView view : views)
				{
					this.tabPane.add(view.getComponent(), view.getViewName());
				}
			}

			if (this.model.getStatTableModel().isViewsAddable())
			{
				this.tabPane.add(this.addViewTab, "+");
			}
			
			TabListener listener = new TabListener(this.tabPane);
			this.tabPane.addMouseListener(listener);
			// test syl: ook mouseMotion t.b.v. mouseDragged()
			this.tabPane.addMouseMotionListener(listener);
			super.add(this.tabPane, BorderLayout.CENTER);
		}

		// remove dialogs that are no longer needed
		for (int i = 0; i < this.dialogs.size(); i++)
		{
			SeparateViewDialog dialog = this.dialogs.get(i);
			if (!separateWindowViews.contains(dialog.sv))
			{
				dialog.setVisible(false);
				this.dialogs.remove(dialog);
				i--;
			}
		}

		// add new dialogs
		for (StatistiekView sv : separateWindowViews)
		{
			if (!this.dialogExists(sv))
			{
				this.showViewInDialog(sv, new Point(super.getX()
					+ (int) (0.5 * super.getWidth()), super.getY()
					+ (int) (0.5 * super.getHeight())));
			}
		}

		// update the selected view in tabPane
//		System.out.println("StatInteractiePanelView.update(): selectedView = " + this.selectedView
//			+ ", selectedViewInPane = " + this.selectedViewInTabPane);
		
		// update selected view if out of bounds
		if (selectedView >= views.size() && views.size() > 0)
		{
			setSelectedView(views.size() - 1);
			setSelectedViewInTabPane(views.size() - 1);
		}
		
		if (this.isInOwnWindow(selectedView))
		{
			this.setTabPane(previousSelectedView);
		}
		else
		{
			this.setTabPane(selectedViewInTabPane);
		}

		// Fill boxes with variable names
		updateStartVarBox();
		updateStartVar2Box();

		this.revalidate();
		this.repaint();
	}

	/*
	 * Update the startVarBox with the variable names.
	 */
	private synchronized void updateStartVarBox()
	{
//		System.out.println("StatInteractiePanelView.updateStartVarBox()");

		// test syl: moet deze methode niet zonder actionListener op startVarBox?
		
		// Alleen updaten als er kolomnamen zijn 
		if (this.model.getStatTableModel().getColumnNames().size() > 0)
		{
    		// Check the first item
			String firstItem = Statistiek.rb.getString("chooseAVariableOption");
			if (!firstItem.equals(this.startVarBox.getItemAt(0)))
			{
				this.startVarBox.addItem(firstItem);
			}
    
			boolean exists;
			String columnName;
    		// Check the variable names in model.getData()
//			for (String varName : this.model.getData().getColumnNames())
			for (int j = 0; j < this.model.getStatTableModel().getColumnNames().size(); j++)
			{
				columnName = this.model.getStatTableModel().getColumnNames().get(j);
				exists = false;
				for (int i = 0; i < this.startVarBox.getItemCount() && !exists; i++)
				{
					if (columnName.equals(this.startVarBox.getItemAt(i)))
					{
						exists = true;
					}
				}
				if (!exists)
				{
					// startVarBox heeft een eerste item 'Kies een variabele', dus j + 1
					this.startVarBox.insertItemAt(columnName, j + 1);
				}
			}
			
			// Check if items from startVarBox need to be removed
			for (int i = 1; i < this.startVarBox.getItemCount(); i++)
			{
				exists = false;
				for (String varName : this.model.getStatTableModel().getColumnNames())
				{
					if (varName.equals(this.startVarBox.getItemAt(i)))
					{
						exists = true;
						break;
					}
				}
				if (!exists)
				{
					this.startVarBox.removeItemAt(i);
				}					
			}
		}
	}

	/*
	 * Update the startVar2Box with the variable names.
	 */
	private synchronized void updateStartVar2Box()
	{
//		System.out.println("StatInteractiePanelView.updateStartVar2Box()");

		// test syl: moet deze methode niet zonder actionListener op startVar2Box?
		
		// Alleen updaten als er kolomnamen zijn 
		if (this.model.getStatTableModel().getColumnNames().size() > 0)
		{
    		// Check the first item
			String firstItem = Statistiek.rb.getString("chooseAVariableOption");
			if (!firstItem.equals(this.startVar2Box.getItemAt(0)))
			{
				this.startVar2Box.addItem(firstItem);
			}
    
			boolean exists;
			String columnName;
    		// Check the variable names in model.getData()
//			for (String varName : this.model.getData().getColumnNames())
			for (int j = 0; j < this.model.getStatTableModel().getColumnNames().size(); j++)
			{
				columnName = this.model.getStatTableModel().getColumnNames().get(j);
				exists = false;
				for (int i = 0; i < this.startVar2Box.getItemCount() && !exists; i++)
				{
					if (columnName.equals(this.startVar2Box.getItemAt(i)))
					{
						exists = true;
					}
				}
				if (!exists)
				{
					// startVarBox heeft een eerste item 'Kies een variabele', dus j + 1
					this.startVar2Box.insertItemAt(columnName, j + 1);
				}
			}
			
			// Check if items from startVarBox need to be removed
			for (int i = 1; i < this.startVar2Box.getItemCount(); i++)
			{
				exists = false;
				for (String varName : this.model.getStatTableModel().getColumnNames())
				{
					if (varName.equals(this.startVar2Box.getItemAt(i)))
					{
						exists = true;
						break;
					}
				}
				if (!exists)
				{
					this.startVar2Box.removeItemAt(i);
				}					
			}
		}
	}

	/**
	 * Class that handles tabs being selected or dragged out of the JTabbedPane.
	 * 
	 * @author Manu Drijvers, Sylvia van Borkulo
	 * 
	 */
	public class TabListener implements MouseInputListener // MouseListener
	{
		private Point startPoint;
		private Point currentPoint; // test syl
		private boolean inDrag;
		private int draggedTab;
		private int selectedTab;
		private ImageIcon draggedImage;

		private JTabbedPane tabPane;

		/**
		 * Constructor
		 * 
		 * @param tabPane
		 *            the DraggedTabListener will look for tabs being dragged
		 *            out of this JTabbedPane
		 */
		public TabListener(JTabbedPane tabPane)
		{
			this.tabPane = tabPane;
			
			// initialize with an invalid value
			this.draggedTab = -1;
			
			java.net.URL imageURL = Statistiek.class.getResource("resources/arrow-137-16_525252up.gif");
			this.draggedImage = new ImageIcon(imageURL);
		}

		public void mouseClicked(MouseEvent arg0)
		{
			// TODO Auto-generated method stub
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
			int tab = this.tabPane.indexAtLocation(arg0.getPoint().x, arg0.getPoint().y);

			if (tab < 0)
			{
				return;
			}
			
			if (StatInteractiePanelView.this.getParent().getParent() instanceof StatEditPanelView)
			{
				// only set the selected tab
				selectedTab = tab;
				// dragging is not allowed, but the view name may be changed
			}
			
			if (arg0.getButton() == MouseEvent.BUTTON1
				// check dat het geen control-klik is om viewnaam te wijzigen
				&& !((arg0.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK)
				&& !(StatInteractiePanelView.this.getParent().getParent() instanceof StatEditPanelView))
			{
				this.inDrag = true;
				this.startPoint = arg0.getPoint();
				this.draggedTab = tab;
				if (this.draggedTab >= 0
					&& this.draggedTab < StatInteractiePanelView.this.model
						.getMainWindowViews().size())
				{
					// test syl: move cursor blijft
//					StatInteractiePanelView.super.setCursor(new Cursor(
//						Cursor.MOVE_CURSOR));
				}
			}
			else if (arg0.getButton() == MouseEvent.BUTTON2
				&& StatInteractiePanelView.this.model.getStatTableModel()
					.isViewsAddable()
				&& tab >= 0
				&& (tab < StatInteractiePanelView.this.tabPane.getTabCount() - 1))
			{
				StatInteractiePanelView.this.model
					.removeView(StatInteractiePanelView.this.model
						.mainWindowIndexToGeneralIndex(tab));
			}
			else if (
					((arg0.getButton() == MouseEvent.BUTTON3) 
					// allow mac users to control-click to change the view name
					|| ((arg0.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK))
				&& StatInteractiePanelView.this.model.getStatTableModel()
					.isViewsEditable())
			{
				// set the selected tab
				StatInteractiePanelView.this.controller
					.setSelectedTab(tab);

				if (tab < StatInteractiePanelView.this.model.getViews().size())
				{
					Container c = Statistiek.getTopLevelAncestor(StatInteractiePanelView.this);
					if (c instanceof Frame)
					{
						ChangeViewNameDialog dialog = new ChangeViewNameDialog(
							(Frame) c, StatInteractiePanelView.this.model, tab,
							StatInteractiePanelView.this,
							StatInteractiePanelView.this.getLocationOnScreen());
						dialog.setVisible(true);
					}
					else if (c instanceof Dialog)
					{
						ChangeViewNameDialog dialog = new ChangeViewNameDialog(
							(Dialog) c, StatInteractiePanelView.this.model, tab,
							StatInteractiePanelView.this,
							StatInteractiePanelView.this.getLocationOnScreen());
						dialog.setVisible(true);
					}
				}
			}
		}

		public void mouseReleased(MouseEvent arg0)
		{
			if (StatInteractiePanelView.this.getParent().getParent() instanceof StatEditPanelView)
			{
				// set the selected tab
				StatInteractiePanelView.this.controller
					.setSelectedTab(this.selectedTab);

			}
			else
			{
				this.inDrag = false;
				StatInteractiePanelView.super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

				if (this.draggedTab >= 0
					&& this.draggedTab < StatInteractiePanelView.this.model
						.getMainWindowViews().size())
				{
					// Test if tab is dragged outside of tabPane
					if (!this.tabPane.contains(arg0.getPoint()))
					{
						// tab is dragged outside of tabPane
						int viewIndex = StatInteractiePanelView.this.model
							.mainWindowIndexToGeneralIndex(this.draggedTab);
						StatInteractiePanelView.this.showViewInDialog(
							StatInteractiePanelView.this.model.getViews().get(
								viewIndex), arg0.getLocationOnScreen());
	
						StatInteractiePanelView.this.model.setViewSeparateWindow(
							StatInteractiePanelView.this.model
								.mainWindowIndexToGeneralIndex(this.draggedTab),
							true);
					}
					else
					{
						// tab is not dragged but selected
						// set the selected tab
						StatInteractiePanelView.this.controller
							.setSelectedTab(this.draggedTab);
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent arg0)
		{
			// test syl
			//System.out.println("StatInteractiePanelView.DraggedTabListener.mouseDragged()!");
			// test syl: deze toont alleen boven StatInteractiePanelView; je wilt hem boven het hele scherm
			//StatInteractiePanelView.super.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			// A tab is dragged
			if (this.draggedTab >= 0)
			{
				// tab is dragged -> creates new instance of dialog each method call... 
//				int viewIndex = StatInteractiePanelView.this.model
//					.mainWindowIndexToGeneralIndex(this.draggedTab);
//				StatInteractiePanelView.this.showViewInDialog(
//					StatInteractiePanelView.this.model.getViews().get(
//						viewIndex), arg0.getLocationOnScreen());
				
				//repaint();
				StatInteractiePanelView.super.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			} // test syl

		}
		
		@Override
		public void mouseMoved(MouseEvent arg0)
		{
		}
	}

	/**
	 * A Dialog for showing a single StatistiekView
	 * 
	 * @author Manu Drijvers
	 * 
	 */
	public class SeparateViewDialog extends JDialog implements WindowListener
	{
		private StatistiekView sv;

		/**
		 * Constructor for Frame owner
		 * 
		 * @param sv
		 *            The statistiekview that will be shown
		 * @param owner
		 *            The owner
		 */
		public SeparateViewDialog(StatistiekView sv, Frame owner)
		{
			super(owner, sv.getViewName(), false);
			this.sv = sv;
			this.setUp();
		}

		/**
		 * Constructor for Dialog owner
		 * 
		 * @param sv
		 *            The statistiekview that will be shown
		 * @param owner
		 *            The owner
		 */
		public SeparateViewDialog(StatistiekView sv, Dialog owner)
		{
			super(owner, sv.getViewName(), false);
			this.sv = sv;
			this.setUp();
		}

		private void setUp()
		{
			this.setBackground(Color.WHITE);
			super.setContentPane(sv.getComponent());
			super.setSize(StatInteractiePanelView.this.getSize());
			super.addWindowListener(this);
		}

		public void windowActivated(WindowEvent arg0)
		{
			// Deze methode wordt aangeroepen als een window wordt aangeklikt,
			// en dus ook als een window wordt gesloten.

//			System.out.println("StatInteractiePanelView.SeparateViewDialog.windowActivated()");
//			System.out.println("... selectedView = " + selectedView);
//			System.out.println("... indexOf(sv) = " +
//			StatInteractiePanelView.this.getModel().getViews().indexOf(sv));

			int view = StatInteractiePanelView.this.getModel().getViews()
				.indexOf(sv);
			StatInteractiePanelView.this.processSelectedView(view);
		}

		public void windowClosed(WindowEvent arg0)
		{
			// TODO Auto-generated method stub
		}

		public void windowClosing(WindowEvent arg0)
		{
//			 System.out.println("windowClosing(): previousSelectedView="
//				 + StatInteractiePanelView.this.previousSelectedView +
//				 ", selectedView=" + selectedView);

//			 System.out.println("VOOR setViewSeparateWindow..... tabPane.getSelectedIndex()="
//				 + tabPane.getSelectedIndex());

			// zet de view terug in tabPane; hierna is tabPane.selectedIndex 0
			StatInteractiePanelView.this.model.setViewSeparateWindowByObject(
				this.sv, false);

//			System.out.println("NA setViewSeparateWindow..... tabPane.getSelectedIndex()="
//				+ tabPane.getSelectedIndex());
		}

		public void windowDeactivated(WindowEvent arg0)
		{
			// TODO Auto-generated method stub
		}

		public void windowDeiconified(WindowEvent arg0)
		{
			// TODO Auto-generated method stub
		}

		public void windowIconified(WindowEvent arg0)
		{
			// TODO Auto-generated method stub
		}

		public void windowOpened(WindowEvent arg0)
		{
			// TODO Auto-generated method stub
		}
	}

	public String getViewsBoxString()
	{
		return (String) this.viewsBox.getSelectedItem();
	}

	public int getStartVarBoxSelectedIndex()
	{
		return this.startVarBox.getSelectedIndex();
	}
	
	public int getStartVar2BoxSelectedIndex()
	{
		return this.startVar2Box.getSelectedIndex();
	}
}
