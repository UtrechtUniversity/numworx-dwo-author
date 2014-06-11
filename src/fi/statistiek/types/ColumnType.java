package fi.statistiek.types;

import java.io.Serializable;
import java.text.CollationKey;
import java.text.Collator;
import java.util.Locale;

import fi.statistiek.addcolumndialog.AddColumnDialogModel;

/**
 * Geeft alle informatie over een kolom.
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class ColumnType implements Serializable
{
	private AllowedTypes type;
	private String[] enumOptions;
	private String uitleg;

	// Wildcard voor alle types, invullen als er informatie ontbreekt
	public static final String WILDCARD = new String("*");

	/**
	 * Constructor met alleen het AllowedType
	 * 
	 * @param type
	 *            Het AllowedType van deze kolom
	 */
	public ColumnType(AllowedTypes type)
	{
		if (type.equals(AllowedTypes.ENUM))
		{
			System.out.println("Enum zonder opties!");
		}
		this.type = type;
	}

	/**
	 * Constructor met allowedType en enumeratieopties
	 * 
	 * @param type
	 *            Het AllowedType van deze kolom
	 * @param options
	 *            De door de gebruiker samengestelde enumeratie
	 */
	public ColumnType(AllowedTypes type, String[] options)
	{
		this.type = type;
		this.enumOptions = options;
	}

	/**
	 * Constructor met allowedType en uitleg over de kolom
	 * 
	 * @param type
	 *            Het AllowedType van deze kolom
	 * @param uitleg
	 *            Uitleg over deze kolom
	 */
	public ColumnType(AllowedTypes type, String uitleg)
	{
		if (type.equals(AllowedTypes.ENUM))
		{
			System.out.println("Enum zonder opties!");
		}
		this.type = type;
		this.uitleg = uitleg;
	}

	/**
	 * Constructor met allowedType, enumeratieopties en uitleg over de kolom
	 * 
	 * @param type
	 *            Het AllowedType van deze kolom
	 * @param options
	 *            De door de gebruiker samengestelde enumeratie
	 * @param uitleg
	 *            Uitleg over deze kolom
	 */
	public ColumnType(AllowedTypes type, String[] options, String uitleg)
	{
		this.type = type;
		this.enumOptions = options;
		this.uitleg = uitleg;
	}

	/**
	 * Constructor die een AddColumnDialogModel krijgt, dus de informatie uit de
	 * "maak een nieuwe kolom" dialog
	 * 
	 * @param dialogModel
	 *            Het model uit de AddColumnDialog
	 */
	public ColumnType(AddColumnDialogModel dialogModel)
	{
		this.type = dialogModel.getType();
		if (this.type.equals(AllowedTypes.ENUM))
		{
			this.enumOptions = new String[dialogModel.getEnumOptions().size()];
			this.enumOptions = dialogModel.getEnumOptions().toArray(
				this.enumOptions);
		}

		this.uitleg = dialogModel.getUitleg();
	}

	/**
	 * @return Het AllowedType van deze kolom
	 */
	public AllowedTypes getType()
	{
		return this.type;
	}

	public String getUitleg()
	{
		return this.uitleg;
	}

	/**
	 * Compare two objects
	 * 
	 * @param a
	 *            Object a
	 * @param b
	 *            Object b
	 * @return 1 if a > b, 0 if a = b, -1 if a < b
	 */
	public int compare(Object a, Object b)
	{
		String s1 = (String) a;
		String s2 = (String) b;
		if (s1.equals(ColumnType.WILDCARD) && s2.equals(ColumnType.WILDCARD))
		{
			return 0;
		}
		else if (s1.equals(ColumnType.WILDCARD))
		{
			return 1;
		}
		else if (s2.equals(ColumnType.WILDCARD))
		{
			return -1;
		}
		else
		{
			if (this.type.equals(AllowedTypes.INTEGER))
			{
				int i = Integer.parseInt(s1);
				int j = Integer.parseInt(s2);
				if (i > j)
				{
					return 1;
				}
				else if (i == j)
				{
					return 0;
				}
				else
				{
					return -1;
				}
			}
			else if (this.type.equals(AllowedTypes.DOUBLE))
			{
				double i = Double.parseDouble(s1);
				double j = Double.parseDouble(s2);
				if (i > j)
				{
					return 1;
				}
				else if (i == j)
				{
					return 0;
				}
				else
				{
					return -1;
				}
			}
			else if (this.type.equals(AllowedTypes.STRING))
			{
				// Use collator to sort for example 'é' correctly
				Collator collator = Collator.getInstance(Locale.getDefault());
				CollationKey key1 = collator.getCollationKey(s1);
				CollationKey key2 = collator.getCollationKey(s2);
				return key1.compareTo(key2);
			}
			else if (this.type.equals(AllowedTypes.ENUM))
			{
				int i = this.indexOfStringInEnum(s1);
				int j = this.indexOfStringInEnum(s2);
				if (i > j)
				{
					return 1;
				}
				else if (i == j)
				{
					return 0;
				}
				else
				{
					return -1;
				}
			}
			else
			{
				System.out.println("Type not found in ColumnType.compare!");
				return 0;
			}
		}
	}

	public int indexOfStringInEnum(String s)
	{
		int i;
		int j = 0;
		for (i = 0; i < this.enumOptions.length; i++)
		{
			if (this.enumOptions[i].equals(s))
			{
				return j;
			}
			else if (!this.enumOptions[i].equals(WILDCARD))
			{
				j++;
			}
		}
		return j;
	}

	/**
	 * Tells whether object o is valid input for this column
	 * 
	 * @param o
	 *            Object to be tested
	 * @return true iff o is valid input for this colunm
	 */
	public boolean isValidInput(Object o)
	{
		if (o.equals(ColumnType.WILDCARD))
		{
			// wildcard is always valid
			return true;
		}
		else if (this.type.equals(AllowedTypes.ENUM))
		{
			String s = (String) o;
			for (String a : this.enumOptions)
			{
				if (a.equals(s))
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			return this.type.isValidInstance(o);
		}
	}

	public String[] getEnumOptions()
	{
		return this.enumOptions;
	}

	/**
	 * Override toString()
	 */
	public String toString()
	{
		String ret = this.type.toString();
		if (this.type.equals(AllowedTypes.ENUM))
		{
			ret = ret + ":{";
			for (int i = 0; i < this.enumOptions.length; i++)
			{
				ret = ret + this.enumOptions[i] + ",";
			}
			ret = ret + "}";
		}
		return ret;
	}
}
