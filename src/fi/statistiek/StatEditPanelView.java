package fi.statistiek;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * Statistiek InteractieEditPanel MVC View
 * 
 * @author Manu Drijvers
 * 
 */
public class StatEditPanelView extends JPanel implements Observer
{
	private StatModel model;

	private StatInteractiePanel interactiePanel;

	private JCheckBox viewsEditableBox;
	private JCheckBox viewsAddableBox;
	private JCheckBox dataEditableBox;

	// private OrderableJList addedViewsList;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            The MVC model
	 * @param controller
	 *            The MVC Controller
	 */
	public StatEditPanelView(StatModel model, StatEditPanelController controller)
	{
		super(new BorderLayout());

		this.model = model;
		this.model.addObserver(this);
		this.setBackground(Color.WHITE);

		this.interactiePanel = new StatInteractiePanel(model);
		super.add(this.interactiePanel, BorderLayout.CENTER);

		GridLayout optionsLayout = new GridLayout(10, 1);
		optionsLayout.setVgap(5);
		optionsLayout.setHgap(5);
		JPanel optionsPanel = new JPanel(optionsLayout);
		optionsPanel.setOpaque(false);
		this.dataEditableBox = new JCheckBox(Statistiek.rb.getString("dataeditableCheckBox"));
		this.dataEditableBox.setActionCommand("dataEditableBox");
		this.dataEditableBox.addActionListener(controller);
		dataEditableBox.setOpaque(false);
		optionsPanel.add(this.dataEditableBox);
		this.viewsEditableBox = new JCheckBox(Statistiek.rb.getString("viewseditableCheckBox"));
		this.viewsEditableBox.setActionCommand("viewsEditableBox");
		this.viewsEditableBox.addActionListener(controller);
		this.viewsEditableBox.setOpaque(false);
		optionsPanel.add(this.viewsEditableBox);
		this.viewsAddableBox = new JCheckBox(Statistiek.rb.getString("viewsaddableCheckBox"));
		this.viewsAddableBox.setActionCommand("viewsAddableBox");
		this.viewsAddableBox.addActionListener(controller);
		this.viewsAddableBox.setOpaque(false);
		optionsPanel.add(this.viewsAddableBox);

		super.add(optionsPanel, BorderLayout.EAST);

		this.update(null, null);
	}

	/**
	 * Set a new model
	 * 
	 * @param model
	 *            the new MVC Model
	 */
	public void setModel(StatModel model)
	{
		this.model = model;
		this.model.addObserver(this);

		this.interactiePanel.setModel(model);

		this.update(null, null);
	}

	public boolean isDataEditableBoxSelected()
	{
		return this.dataEditableBox.isSelected();
	}

	public boolean isViewsEditableBoxSelected()
	{
		return this.viewsEditableBox.isSelected();
	}

	public boolean isViewsAddableBoxSelected()
	{
		return this.viewsAddableBox.isSelected();
	}

	public int getInteractiePanelSelectedView()
	{
		return this.interactiePanel.getSelectedView();
	}

	public void setInteractiePanelSelectedView(int view)
	{
		this.interactiePanel.setSelectedTab(view);
	}

	public StatInteractiePanel getInteractiePanel()
	{
		return this.interactiePanel;
	}

	/**
	 * Observer implementation Update to changed model
	 */
	public void update(Observable arg0, Object arg1)
	{
		this.dataEditableBox.setSelected(this.model.getStatTableModel().isDataEditable());
		this.viewsEditableBox.setSelected(this.model.getStatTableModel().isViewsEditable());
		this.viewsAddableBox.setSelected(this.model.getStatTableModel().isViewsAddable());
		this.viewsAddableBox.setEnabled(this.model.getStatTableModel().isViewsEditable());
	}
}
