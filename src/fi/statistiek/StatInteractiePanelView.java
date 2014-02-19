package fi.statistiek;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
	private JComboBox viewsBox, startVarBox;
	private JLabel addViewLabel;
	private JLabel chooseStartVarLabel;

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

		this.startVarBox = new JComboBox();
		this.startVarBox.setPreferredSize(new Dimension(150, 24));
		this.startVarBox.setActionCommand("startVarBox");
		this.startVarBox.addActionListener(this.controller);
		this.startVarBox.setVisible(false);

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

		Box vb = Box.createVerticalBox();
		vb.add(Box.createVerticalStrut(25));
		vb.add(hb1);
		vb.add(Box.createVerticalStrut(15));
		vb.add(hb2);

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
	}

	public void setStartVarBox(boolean b)
	{
		startVarBox.setVisible(b);
		chooseStartVarLabel.setVisible(b);
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
		int tab;
		int count = 0;

		ArrayList<Boolean> viewInOwnWindow = getModel().getViewInOwnWindow();

		// System.out.println("determineTab(view=" + view +
		// "): viewInOwnWindow=" + viewInOwnWindow);

		if (viewInOwnWindow.get(view).booleanValue())
		{
			// view is not in tabPane
			tab = 0;
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

	private boolean isInOwnWindow(int view)
	{
		ArrayList<Boolean> viewInOwnWindow = getModel().getViewInOwnWindow();

		if (viewInOwnWindow.get(view))
			return true;
		else
			return false;
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
				System.out.println("Dialog for statistiekview "
					+ sv.getViewName() + " already exists.");
				return true;
			}
		}

		System.out.println("Dialog for statistiekview " + sv.getViewName()
			+ " doesn't exist yet");
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
		Container owner = Statistiek.getTopLevelAcestor(this);
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
		this.tabPane.setSelectedIndex(this.tabPane.getTabCount() - 2);
	}

	public void update(Observable arg0, Object arg1)
	{
		// System.out.println("StatInteractiePanelView.update()");
		super.removeAll();

		// test syl; hoe krijg ik de button op het goede panel?
		// wat doet al het layout-werk in de constructor??
		// if(this.model.getData().isDataEditable())
		// {
		// super.add(resetButton, BorderLayout.NORTH);
		// }

		ArrayList<StatistiekView> views = this.model.getMainWindowViews();
		ArrayList<StatistiekView> separateWindowViews = this.model.getSeparateWindowViews();

		int amountOfTabs = views.size();
		if (this.model.getData().isViewsAddable())
		{
			amountOfTabs++;
		}

		// draw main window
		if (amountOfTabs == 0)
		{
			// System.out.println("no views added");
			super.add(NO_VIEWS_LABEL);
		}
		else if (views.size() == 1 && !this.model.getData().isViewsAddable())
		{
			// System.out.println("one view added");
			super.add(views.get(0).getComponent(), BorderLayout.CENTER);
		}
		else
		{
			// System.out.println(">1 views added");
			this.tabPane = new JTabbedPane();
			this.tabPane.setFont(Statistiek.font);
			if (this.model.getData().isViewsAddable())
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

			if (this.model.getData().isViewsAddable())
			{
				this.tabPane.add(this.addViewTab, "+");
			}

			this.tabPane.addMouseListener(new DraggedTabListener(this.tabPane));
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

		// this.startVarBox.addItem("");
		for (String varName : this.model.getData().getColumnNames())
		{
			this.startVarBox.addItem(varName);
		}

		this.revalidate();
		this.repaint();
	}

	/**
	 * class that handles tabs being dragged out of the JTabbedPane
	 * 
	 * @author Manu Drijvers
	 * 
	 */
	public class DraggedTabListener implements MouseListener
	{
		private Point startPoint;
		private boolean inDrag;
		private int draggedTab;

		private JTabbedPane tabPane;

		/**
		 * Constructor
		 * 
		 * @param tabPane
		 *            the DraggedTabListener will look for tabs being dragged
		 *            out of this JTabbedPane
		 */
		public DraggedTabListener(JTabbedPane tabPane)
		{
			this.tabPane = tabPane;
			
			// initialize with an invalid value
			this.draggedTab = -1;
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
//			System.out.println("StatInteractiePanelView.mousePressed(): tab=" + tab);
			if (tab < 0)
			{
				return;
			}

//			System.out.println("---------------- MousePressed " + tab);
			if (arg0.getButton() == MouseEvent.BUTTON1
				&& !(StatInteractiePanelView.this.getParent().getParent() instanceof StatEditPanelView))
			{
				this.inDrag = true;
				this.startPoint = arg0.getPoint();
				this.draggedTab = tab;
				if (this.draggedTab >= 0
					&& this.draggedTab < StatInteractiePanelView.this.model
						.getMainWindowViews().size())
				{
					StatInteractiePanelView.super.setCursor(new Cursor(
						Cursor.MOVE_CURSOR));
				}
			}
			else if (arg0.getButton() == MouseEvent.BUTTON2
				&& StatInteractiePanelView.this.model.getData()
					.isViewsAddable()
				&& tab >= 0
				&& (tab < StatInteractiePanelView.this.tabPane.getTabCount() - 1))
			{
				StatInteractiePanelView.this.model
					.removeView(StatInteractiePanelView.this.model
						.mainWindowIndexToGeneralIndex(tab));
			}
			else if (arg0.getButton() == MouseEvent.BUTTON3
				&& StatInteractiePanelView.this.model.getData()
					.isViewsEditable())
			{
				Container c = Statistiek.getTopLevelAcestor(StatInteractiePanelView.this);
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

		public void mouseReleased(MouseEvent arg0)
		{
			if (StatInteractiePanelView.this.getParent().getParent() instanceof StatEditPanelView)
			{
				return;
			}
			this.inDrag = false;
			StatInteractiePanelView.super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//			System.out.println("Drag finished, dragged from " + this.startPoint
//				+ " to " + arg0.getPoint());
			if (this.draggedTab >= 0
				&& this.draggedTab < StatInteractiePanelView.this.model
					.getMainWindowViews().size())
			{
//				 System.out.println("This dragged tab: " + this.draggedTab);

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

//					 System.out.println("mouseReleased(): setSelectedTab(draggedTab="
//					 + this.draggedTab + ")");
				}
			}
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

			// System.out.println("StatInteractiePanelView.SeparateViewDialog.windowActivated()");
			// System.out.println("... selectedView = " + selectedView);
			// System.out.println("... indexOf(sv) = " +
			// StatInteractiePanelView.this.getModel().getViews().indexOf(sv));

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
			// System.out.println("windowClosing(): previousSelectedView="
			// + StatInteractiePanelView.this.previousSelectedView +
			// ", selectedView=" + selectedView);

			// oldSelectedTab is de oude selectedIndex van tabPane.
			int oldSelectedTab = tabPane.getSelectedIndex();

			int newSelectedTab;

			// System.out.println("VOOR setViewSeparateWindow..... tabPane.getSelectedIndex()="
			// + tabPane.getSelectedIndex());

			// zet de view terug in tabPane; hierna is tabPane.selectedIndex 0
			StatInteractiePanelView.this.model.setViewSeparateWindowByObject(
				this.sv, false);
			// System.out.println("NA setViewSeparateWindow..... tabPane.getSelectedIndex()="
			// + tabPane.getSelectedIndex());

			// Als er een view wordt teruggezet vòòr de oude selectedTab,
			// dan wordt de nieuwe selectedTab 1 hoger
			if (selectedView <= oldSelectedTab)
				newSelectedTab = oldSelectedTab + 1;
			else
				newSelectedTab = oldSelectedTab;
			StatInteractiePanelView.this.processSelectedTab(newSelectedTab);

			// System.out.println("windowClosing(): tabPane.setSelectedIndex(selectedTab="
			// + selectedTab + ")");
			// dit geeft problemen, omdat selectedView niet is gezet
			// tabPane.setSelectedIndex(selectedTab);
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
}
