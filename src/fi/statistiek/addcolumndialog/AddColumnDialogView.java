package fi.statistiek.addcolumndialog;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import fi.statistiek.Statistiek;
import fi.statistiek.orderablejlist.OrderableJList;
import fi.statistiek.orderablejlist.OrderableJListModel;
import fi.statistiek.types.AllowedTypes;

/**
 * View for add column dialog
 * 
 * @author Manu Drijvers
 * 
 */
public class AddColumnDialogView extends JDialog implements Observer
{
	private AddColumnDialogModel model;

	private JPanel alles;

	private JLabel kiesNaam;
	private JTextField nameField;

	private JLabel kiesType;
	private JComboBox typeBox;
	private JPanel setTypePanel;

	private JPanel createEnumPanel;
	private JPanel addEnumElementPanel;
	private JLabel addEnumElementLabel;
	private JTextField addEnumElementField;
	// private JTextArea enumElementsView;
	private OrderableJList enumElementsList;
	private JScrollPane enumScrollPane;
	private JButton removeSelectedElement;

	private JPanel typePanel;
	private JPanel uitlegPanel;
	private JLabel uitlegLabel;
	private JScrollPane uitlegScrollPane;
	private JTextArea uitlegArea;
	private JButton doneButton;

	private Font font;

	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 300;

	/**
	 * Constructor with Frame owner
	 * 
	 * @param owner
	 *            Dialog owner
	 * @param model
	 *            MVC Model
	 */
	public AddColumnDialogView(Frame owner, AddColumnDialogModel model)
	{
		super(owner, "Add a column", true);
		super.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		this.model = model;
		this.model.addObserver(this);

		this.initialize();
	}

	/**
	 * Constructor with Dialog owner
	 * 
	 * @param owner
	 *            Dialog owner
	 * @param model
	 *            MVC Model
	 */
	public AddColumnDialogView(Dialog owner, AddColumnDialogModel model)
	{
		super(owner, Statistiek.rb.getString("addacolumn"), true);
		super.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		this.model = model;
		this.model.addObserver(this);

		this.initialize();
	}

	/**
	 * Set up GUI
	 */
	private void initialize()
	{
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.font = new Font("Default", Font.PLAIN, 12);

		this.kiesNaam = new JLabel(Statistiek.rb.getString("columnname"));
		this.kiesNaam.setFont(this.font);
		this.nameField = new JTextField();
		this.nameField.setFont(this.font);
		this.nameField.setActionCommand("nameField");
		this.kiesType = new JLabel(Statistiek.rb.getString("choosetype"));
		this.kiesType.setFont(this.font);

//		this.typeBox = new JComboBox(AllowedTypes.allowedTypes);
		// Use strings from text file to create combobox
		String[] types = {
			Statistiek.rb.getString("integer"),
			Statistiek.rb.getString("double"),
			Statistiek.rb.getString("string"),
			Statistiek.rb.getString("enum")
		};
		this.typeBox = new JComboBox(types);

//		this.typeBox.setSelectedItem(this.model.getType());
		setTypeBox();

		this.typeBox.setActionCommand("typeBox");

		this.setTypePanel = new JPanel();
		this.setTypePanel.setLayout(new GridLayout(2, 2));
		this.setTypePanel.add(this.kiesNaam);
		this.setTypePanel.add(this.nameField);
		this.setTypePanel.add(this.kiesType);
		this.setTypePanel.add(this.typeBox);

		this.addEnumElementLabel = new JLabel(
			Statistiek.rb.getString("addenumeration"));
		this.addEnumElementLabel.setFont(this.font);
		this.addEnumElementField = new JTextField();
		this.addEnumElementField.setFont(this.font);
		this.addEnumElementField.setActionCommand("addEnumElementField");
		this.addEnumElementPanel = new JPanel();
		this.addEnumElementPanel.setLayout(new GridLayout(2, 1));
		this.addEnumElementPanel.add(this.addEnumElementLabel);
		this.addEnumElementPanel.add(this.addEnumElementField);

		this.createEnumPanel = new JPanel();
		/*
		 * this.enumElementsView = new JTextArea();
		 * this.enumElementsView.setFont(this.font);
		 * this.enumElementsView.setEditable(false);
		 */
		this.enumElementsList = new OrderableJList(new OrderableJListModel(
			this.model.getEnumOptions()));
		this.enumScrollPane = new JScrollPane(this.enumElementsList);
		this.removeSelectedElement = new JButton(
			Statistiek.rb.getString("removeselectedelement"));
		this.removeSelectedElement.setFont(this.font);
		this.removeSelectedElement.setActionCommand("removeSelectedElement");

		this.createEnumPanel = new JPanel();
		this.createEnumPanel.setLayout(new BorderLayout());
		this.createEnumPanel.add(enumScrollPane, BorderLayout.CENTER);
		this.createEnumPanel
			.add(this.removeSelectedElement, BorderLayout.SOUTH);
		this.createEnumPanel.add(this.addEnumElementPanel, BorderLayout.NORTH);

		this.typePanel = new JPanel();
		this.typePanel.setLayout(new BorderLayout());
		this.typePanel.add(this.createEnumPanel, BorderLayout.CENTER);
		this.typePanel.add(this.setTypePanel, BorderLayout.NORTH);

		this.uitlegLabel = new JLabel(Statistiek.rb.getString("uitlegbijkolom"));
		this.uitlegArea = new JTextArea();
		this.uitlegScrollPane = new JScrollPane(this.uitlegArea);
		this.doneButton = new JButton(Statistiek.rb.getString("doneButton"));
		this.doneButton.setActionCommand("doneButton");
		this.uitlegPanel = new JPanel();
		this.uitlegPanel.setLayout(new BorderLayout());
		this.uitlegPanel.add(this.uitlegLabel, BorderLayout.NORTH);
		this.uitlegPanel.add(this.uitlegArea, BorderLayout.CENTER);
		this.uitlegPanel.add(this.doneButton, BorderLayout.SOUTH);

		GridLayout gl = new GridLayout(1, 2);
		gl.setHgap(5);
		this.alles = new JPanel();
		this.alles.setLayout(gl);
		this.alles.add(this.typePanel);
		this.alles.add(this.uitlegPanel);
		this.add(this.alles);

		this.update(null, null);
	}

	private void setTypeBox()
	{
		String typeString = "";
		AllowedTypes type = this.model.getType(); 
		
		if (type.equals(AllowedTypes.INTEGER))
			typeString = Statistiek.rb.getString("integer");
		else if (type.equals(AllowedTypes.DOUBLE))
			typeString = Statistiek.rb.getString("double");
		else if (type.equals(AllowedTypes.STRING))
			typeString = Statistiek.rb.getString("string");
		else if (type.equals(AllowedTypes.ENUM))
			typeString = Statistiek.rb.getString("enum");
			
		this.typeBox.setSelectedItem(typeString);
	}

	/**
	 * @return The currently selected AllowedType
	 */
	public AllowedTypes getSelectedType()
	{
		String typeString = (String) this.typeBox.getSelectedItem();
		AllowedTypes type = null;
		
		if (typeString.equals(Statistiek.rb.getString("integer")))
			type = AllowedTypes.INTEGER;
		else if (typeString.equals(Statistiek.rb.getString("double")))
			type = AllowedTypes.DOUBLE;
		else if (typeString.equals(Statistiek.rb.getString("string")))
			type = AllowedTypes.STRING;
		else if (typeString.equals(Statistiek.rb.getString("enum")))
			type = AllowedTypes.ENUM;
			
		return type;
		
//		return (AllowedTypes) this.typeBox.getSelectedItem();
	}

	/**
	 * @return The text in this.uitlegArea
	 */
	public String getUitleg()
	{
		return this.uitlegArea.getText();
	}

	/**
	 * @return Get the text in the addEnumElementField textfield
	 */
	public String getEnumOption()
	{
		return this.addEnumElementField.getText();
	}

	/**
	 * @return The text in the nameField textfield
	 */
	public String getCurrentName()
	{
		return this.nameField.getText();
	}

	/**
	 * @return The index of the selected enum item in the enum elements list
	 */
	public int getSelectedEnumOptionIndex()
	{
		return this.enumElementsList.getSelectedIndex();
	}

	/**
	 * Add a actionlistener to all buttons and fields
	 * 
	 * @param al
	 *            the subscribing ActionListener
	 */
	public void addActionListeners(ActionListener al)
	{
		this.typeBox.addActionListener(al);
		this.addEnumElementField.addActionListener(al);
		this.removeSelectedElement.addActionListener(al);
		this.doneButton.addActionListener(al);
		this.nameField.addActionListener(al);
	}

	/**
	 * Add a focuslistener to textfields
	 * 
	 * @param fl
	 *            the subscribing FocusListener
	 */
	public void addFocusListeners(FocusListener fl)
	{
		this.nameField.addFocusListener(fl);
		this.uitlegArea.addFocusListener(fl);
	}

	public JTextArea getUitlegArea()
	{
		return this.uitlegArea;
	}

	public JTextField getNameField()
	{
		return this.nameField;
	}

	public void clearAddEnumElementField()
	{
		this.addEnumElementField.setText("");
	}

	/**
	 * Implementation of Observer
	 */
	public void update(Observable o, Object arg)
	{
		// update typeBox selected item
		this.typeBox.setSelectedItem(this.model.getType());

		// set the visibility
		this.createEnumPanel.setVisible(this.model.getType().equals(
			AllowedTypes.ENUM));
		this.enumElementsList.setVisible(this.model.getType().equals(
			AllowedTypes.ENUM));

		// update string representation of current enumeration
		this.enumElementsList.setModel(new OrderableJListModel(this.model
			.getEnumOptions()));

		this.nameField.setText(this.model.getName());

		this.uitlegArea.setText(this.model.getUitleg());

		super.validate();
	}
}
