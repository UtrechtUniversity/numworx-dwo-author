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
			}
			else
			{
				this.view.addStringOption(this.view.getEnumOption());
				this.view.update(null, null);
			}

			// clear the text in the input field
			this.view.clearAddEnumElementField();
		}
		else if (actionCommand.equals("removeSelectedElement"))
		{
			if (this.wasEnum())
			{
				// Changes are definitively made in the model
				this.model.removeEnumOption(this.view.getSelectedOptionInListIndex());
			}
			else
			{
				// Changes are preliminarily made in the view.
				// Changes are made definitive after click on OK button.
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
		else if (actionCommand.equals("sortElements"))
		{
			if (this.wasEnum())
			{
				this.model.sortEnumOptions();
			}
			else
			{
				this.view.sortStringOptions();
				this.view.update(null, null);
			}
		}
		else if (actionCommand.equals("moveElementUp"))
		{
			int index = this.view.getSelectedOptionInListIndex();
			
			if (this.wasEnum())
			{
				this.model.swapEnumOptions(index,
					index - 1);
			}
			else
			{
				this.view.swapStringOptions(index,
					index - 1);
				this.view.update(null, null);
			}
			
			this.view.setSelectedOptionInListIndex(index - 1);
		}
		else if (actionCommand.equals("moveElementDown"))
		{
			int index = this.view.getSelectedOptionInListIndex();
			
			if (this.wasEnum())
			{
				this.model.swapEnumOptions(index,
					index + 1);
			}
			else
			{
				this.view.swapStringOptions(index,
					index + 1);
				this.view.update(null, null);
			}
			
			this.view.setSelectedOptionInListIndex(index + 1);
		}
		else if (actionCommand.equals("typeBox"))
		{
			this.model.setType(this.view.getSelectedType());
		}
		else if (actionCommand.equals("columnsBox"))
		{
			this.view.addToEditor();
			// reset de listbox zodat je ook twee keer dezelfde variabele kunt kiezen
			this.view.getColumnsBox().setSelectedIndex(0);
		}
		else if (actionCommand.equals("doneButton"))
		{
			// als type gewijzigd in enum, update enum options
			if (!this.wasEnum() && this.model.getType().equals(AllowedTypes.ENUM))
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
		else if (actionCommand.equals("computeVariable"))
		{
			this.view.setHasClickedComputeVariable(!this.view.hasClickedComputeVariable());
			this.view.update(null, null);
		}
	}
	
	/**
	 * Return whether the column originally was of type enumeration.
	 * When the type was originally string, a list of stringOptions is generated 
	 * as possible enum options. Changes in the string options list are
	 * not immediately processed, but effecuated when OK button is clicked. 
	 * When the original type was enum, the current enum options are shown. 
	 * Changes in the enum list are immediately processed.
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
