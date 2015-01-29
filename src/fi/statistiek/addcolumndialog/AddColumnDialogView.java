package fi.statistiek.addcolumndialog;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import fi.statistiek.ColorGenerator;
import fi.statistiek.Statistiek;
import fi.statistiek.orderablejlist.OrderableJList;
import fi.statistiek.orderablejlist.OrderableJListModel;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

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
	private ArrayList<String> stringOptions;
	private AllowedTypes originalColumnType;
	private JScrollPane enumScrollPane;
	/**
	 * Panel with 'remove selected' and 'remove all' buttons for enumeration.
	 */
	private JPanel enumSouthPanel;
	/**
	 * Panel with move up/down and sort buttons for enumeration.
	 */
	private JPanel enumEastPanel;
	private JButton removeSelectedElement;
	private JButton removeAllElements;
	private JButton sortElements;
	private JButton moveElementUp;
	private JButton moveElementDown;

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

		this.font = Statistiek.font;
		Border border = BorderFactory.createEmptyBorder(5, 0, 5, 5);

		this.kiesNaam = new JLabel(Statistiek.rb.getString("columnname"));
		this.kiesNaam.setFont(this.font);
		this.kiesNaam.setBorder(border);
		this.nameField = new JTextField();
		this.nameField.setFont(this.font);
		this.nameField.setActionCommand("nameField");
		this.kiesType = new JLabel(Statistiek.rb.getString("choosetype"));
		this.kiesType.setFont(this.font);
		this.kiesType.setBorder(border);

		// Use strings from text file to create combobox
		String[] types = {
			Statistiek.rb.getString("integer"),
			Statistiek.rb.getString("double"),
			Statistiek.rb.getString("string"),
			Statistiek.rb.getString("enum")
		};
		this.typeBox = new JComboBox(types);
		this.typeBox.setFont(this.font);

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
		this.addEnumElementLabel.setBorder(border);
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
		this.enumElementsList.setFont(this.font);
		this.enumScrollPane = new JScrollPane(this.enumElementsList);
		this.removeSelectedElement = new JButton(
			Statistiek.rb.getString("removeselectedelement"));
		this.removeSelectedElement.setFont(this.font);
		this.removeSelectedElement.setActionCommand("removeSelectedElement");

		this.removeAllElements = new JButton(
			Statistiek.rb.getString("removeAllElements"));
		this.removeAllElements.setFont(this.font);
		this.removeAllElements.setActionCommand("removeAllElements");
		
		this.enumSouthPanel = new JPanel();
		this.enumSouthPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		enumSouthPanel.add(this.removeSelectedElement);
		enumSouthPanel.add(this.removeAllElements);
		
		this.sortElements = new JButton(
			Statistiek.rb.getString("sortElements"));
		this.sortElements.setFont(Statistiek.font_bold);
		this.sortElements.setForeground(ColorGenerator.BUTTON_TEXT_GREY);
		this.sortElements.setActionCommand("sortElements");
		this.sortElements.setToolTipText(Statistiek.rb.getString("sortElementsTooltip"));

		this.moveElementUp = new JButton();
		this.moveElementUp.setFont(this.font);
		this.moveElementUp.setActionCommand("moveElementUp");
		java.net.URL imageURL = Statistiek.class.getResource("resources/arrow-137-16_525252up.gif");
		if (imageURL != null) 
		{
			this.moveElementUp.setIcon(new ImageIcon(imageURL));
		}
		this.moveElementUp.setToolTipText(Statistiek.rb.getString("moveElementUpTooltip"));

		this.moveElementDown = new JButton();
		this.moveElementDown.setFont(this.font);
		this.moveElementDown.setActionCommand("moveElementDown");
		imageURL = Statistiek.class.getResource("resources/arrow-199-16_525252down.gif");
		if (imageURL != null) 
		{
			this.moveElementDown.setIcon(new ImageIcon(imageURL));
		}
		this.moveElementDown.setToolTipText(Statistiek.rb.getString("moveElementDownTooltip"));

		this.enumEastPanel = new JPanel();
		this.enumEastPanel.setLayout(new GridLayout(3, 1, 0, 10));
		this.enumEastPanel.add(this.moveElementUp);
		this.enumEastPanel.add(this.moveElementDown);
		this.enumEastPanel.add(this.sortElements);

		this.createEnumPanel = new JPanel();
		this.createEnumPanel.setLayout(new BorderLayout());
		this.createEnumPanel.add(this.addEnumElementPanel, BorderLayout.NORTH);
		this.createEnumPanel.add(this.enumScrollPane, BorderLayout.CENTER);
		this.createEnumPanel.add(this.enumEastPanel, BorderLayout.EAST);
		this.createEnumPanel.add(this.enumSouthPanel, BorderLayout.SOUTH);

		this.typePanel = new JPanel();
		this.typePanel.setLayout(new BorderLayout());
		this.typePanel.add(this.createEnumPanel, BorderLayout.CENTER);
		this.typePanel.add(this.setTypePanel, BorderLayout.NORTH);

		this.uitlegLabel = new JLabel(Statistiek.rb.getString("uitlegbijkolom"));
		this.uitlegLabel.setFont(this.font);
		this.uitlegLabel.setBorder(border);
		this.uitlegArea = new JTextArea();
		this.uitlegScrollPane = new JScrollPane(this.uitlegArea);
		this.doneButton = new JButton(Statistiek.rb.getString("doneButton"));
		this.doneButton.setFont(this.font);
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
		
		this.originalColumnType = this.model.getType();
		this.setStringOptions();

		this.update(null, null);
	}

	private void setStringOptions()
	{
		// Voor integerkolommen (dus ook nieuwe kolom) worden bij een switch naar enum 
		// de waarden niet vooringevuld in stringoptions
		if (this.wasInteger())
		{
			this.stringOptions = new ArrayList<String>();
		}
		else if (!this.wasEnum())
		{
			this.stringOptions = new ArrayList<String>(this.model.getTableModel().
				getStringOptions(this.model.getColumnIndex()));
		}
	}

	/**
	 * Select the column's type in the type box. 
	 */
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
	 * Get the string options.
	 * 
	 * @return The string options
	 */
	public ArrayList getStringOptions()
	{
		return this.stringOptions;
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
	public int getSelectedOptionInListIndex()
	{
		return this.enumElementsList.getSelectedIndex();
	}
	
	/**
	 * Set selected index in enum elements list.
	 * @param i The index to be selected
	 */
	public void setSelectedOptionInListIndex(int i)
	{
		this.enumElementsList.setSelectedIndex(i);
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
		this.removeAllElements.addActionListener(al);
		this.sortElements.addActionListener(al);
		this.moveElementUp.addActionListener(al);
		this.moveElementDown.addActionListener(al);
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

		// Update the list with options of current enumeration
		// or with string options if there is no current enumeration
		if (this.wasEnum())
		{
			this.enumElementsList.setModel(new OrderableJListModel(this.model
				.getEnumOptions()));
		}
		else
		{
			// vul met stringOptions
			this.enumElementsList.setModel(new OrderableJListModel(
				this.stringOptions));
		}

		this.nameField.setText(this.model.getName());

		this.uitlegArea.setText(this.model.getUitleg());

		super.validate();
	}

	public AllowedTypes getOriginalColumnType()
	{
		return this.originalColumnType;
	}

	/**
	 * Return whether the column originally was of type enumeration.
	 * @return
	 */
	private boolean wasEnum()
	{
		return this.originalColumnType.equals(AllowedTypes.ENUM);
	}

	/**
	 * Return whether the column originally was of type integer.
	 * @return
	 */
	private boolean wasInteger()
	{
		return this.originalColumnType.equals(AllowedTypes.INTEGER);
	}

	public void removeStringOption(int index)
	{
		if (index > -1)
		{
			this.stringOptions.remove(index);
		}
	}

	public void addStringOption(String s)
	{
		this.stringOptions.add(s);
	}

	/**
	 * Add the options in stringOptions to the enum options.
	 */
	public void updateEnumOptions()
	{
		for (int i = 0; i < this.stringOptions.size(); i++)
		{
			String newElement = (String) this.stringOptions.get(i);
			this.model.addEnumOptionWithoutEvent(newElement);
		}
	}

	/**
	 * Remove all string options except '*' from the string options.
	 */
	public void removeAllStringOptions()
	{
		for (int i = this.stringOptions.size() - 1; i > -1 ; i--)
		{
			if (!this.stringOptions.get(i).equals(ColumnType.WILDCARD))
			{
				this.stringOptions.remove(i);
			}
		}
	}

	/**
	 * Sort string options alphabetically ascending.
	 */
	public void sortStringOptions()
	{
		String[] sortedStringOptions = new String[this.stringOptions.size()];
		sortedStringOptions = this.stringOptions.toArray(sortedStringOptions);
		
		Arrays.sort(sortedStringOptions, new Comparator<String>() {
            @Override
            /**
             * Compare strings alphabetically. 
             * A wildcard is larger than any other string.
             * @param s1
             * @param s2
             * @return
             */
            public int compare(String s1, String s2) 
            {
            	// check for wildcard among the strings
            	if (s1.equals(ColumnType.WILDCARD))
            		return 1;
            	else if (s2.equals(ColumnType.WILDCARD))
            		return -1;
            	else 
            	{
            		// apart from '*' sort the enum options alphabetically
            		return s1.compareTo(s2);
            	}
            }
        });
		
		this.stringOptions = new ArrayList(Arrays.asList(sortedStringOptions));
	}

	/**
	 * Swap string options with index1 and index2. A wildcard is not swapped
	 * @param index1
	 * @param index2
	 */
	public void swapStringOptions(int index1, int index2)
	{
		if (this.validStringOptionsIndex(index1) && this.validStringOptionsIndex(index2)
			&& !this.stringOptions.get(index1).equals(ColumnType.WILDCARD) // the wildcard should stay at the end
			&& !this.stringOptions.get(index2).equals(ColumnType.WILDCARD))
		{
			Collections.swap(this.stringOptions, index1, index2);
		}
	}

	/**
	 * Check whether index is a valid index in string options.
	 * @param index
	 * @return True if index is a valid index, else false.
	 */
	private boolean validStringOptionsIndex(int index)
	{
		boolean isValid = false;
		
//		if ((this.enumOptions == null) || this.enumOptions.size() == 0)
//			isValid = false;
//		else 
			if ((index > -1) && (index < this.stringOptions.size()))
			isValid = true;
		
		return isValid;
	}
}
