package fi.statistiek.addcolumndialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import fi.statistiek.types.AllowedTypes;

/**
 * Controller for add column dialog
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class AddColumnDialogController implements ActionListener,
	DocumentListener, FocusListener
{
	private AddColumnDialogModel model;
	private AddColumnDialogView view;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            MVC Model
	 * @param view
	 *            MVC View
	 */
	public AddColumnDialogController(AddColumnDialogModel model,
		AddColumnDialogView view)
	{
		this.model = model;
		this.view = view;
		this.view.addActionListeners(this);
		this.view.addFocusListeners(this);
	}

	/**
	 * ActionListener implementation
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		String actionCommand = arg0.getActionCommand();
		if (actionCommand.equals("addEnumElementField"))
		{
			if (this.wasEnum())
			{
				String newElement = this.view.getEnumOption();
				this.model.addEnumOption(newElement);
				this.view.clearAddEnumElementField();
			}
			else
			{
				this.view.addStringOption(this.view.getEnumOption());
				this.view.update(null, null);
			}
		}
		else if (actionCommand.equals("removeSelectedElement"))
		{
			if (this.wasEnum())
			{
				this.model.removeEnumOption(this.view.getSelectedOptionInListIndex());
			}
			else
			{
				this.view.removeStringOption(this.view.getSelectedOptionInListIndex());
				this.view.update(null, null);
			}
		}
		else if (actionCommand.equals("removeAllElements"))
		{
			if (this.wasEnum())
			{
				this.model.removeAllEnumOption();
			}
			else
			{
				this.view.removeAllStringOptions();
				this.view.update(null, null);
			}
		}
		else if (actionCommand.equals("typeBox"))
		{
			this.model.setType(this.view.getSelectedType());
		}
		else if (actionCommand.equals("doneButton"))
		{
			if (!this.wasEnum())
			{
				this.view.updateEnumOptions();
			}

			this.model.setDonePressed(true);
			this.view.setVisible(false);
		}
		else if (actionCommand.equals("nameField"))
		{
			this.model.setName(this.view.getCurrentName());
		}
	}
	
	/**
	 * Return whether the column originally was of type enumeration.
	 * @return
	 */
	private boolean wasEnum()
	{
		return this.view.getOriginalColumnType().equals(AllowedTypes.ENUM);
	}

	/**
	 * DocumentListener implementation
	 */
	public void changedUpdate(DocumentEvent arg0)
	{
		this.model.setUitleg(this.view.getUitleg());

	}

	public void insertUpdate(DocumentEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void removeUpdate(DocumentEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void focusGained(FocusEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * FocusListener implementation
	 */
	public void focusLost(FocusEvent arg0)
	{
		if (arg0.getSource() == this.view.getNameField())
		{
			this.model.setName(this.view.getCurrentName());
		}
		else if (arg0.getSource() == this.view.getUitlegArea())
		{
			this.model.setUitleg(this.view.getUitleg());
		}
	}
}
