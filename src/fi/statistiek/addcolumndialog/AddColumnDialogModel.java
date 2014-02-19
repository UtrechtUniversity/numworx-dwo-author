package fi.statistiek.addcolumndialog;
import java.util.ArrayList;
import java.util.Observable;

import fi.statistiek.StatTableModel;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * Model for the add column dialog
 * @author Manu Drijvers
 */
public class AddColumnDialogModel extends Observable{
	private String name;
	private String oldName;
	private AllowedTypes type;
	private ArrayList<String> enumOptions;
	private String uitleg;
	private StatTableModel tableModel;
	private boolean donePressed;
	
	/**
	 * Constructor
	 */
	public AddColumnDialogModel(StatTableModel tableModel) {
		this.tableModel = tableModel;
		this.name = this.getFirstFreeName();
		this.oldName = new String("");
		this.type = AllowedTypes.INTEGER;
		this.enumOptions = new ArrayList<String>();
		this.enumOptions.add(ColumnType.WILDCARD);
		this.uitleg = new String();
		this.donePressed = false;
	}
	
	/**
	 * Constructor for restoring previous settings
	 * @param tableModel The table model
	 * @param name The initial column name
	 * @param cType The initial column type
	 */
	public AddColumnDialogModel(StatTableModel tableModel, String name, ColumnType cType) {
		this.name = name;
		this.oldName = name;
		this.tableModel = tableModel;
		this.type = cType.getType();
		this.enumOptions = new ArrayList<String>();
		if(this.type.equals(AllowedTypes.ENUM)) {
			for(String s : cType.getEnumOptions()) {
				this.enumOptions.add(s);
			}
		}
		else {
			this.enumOptions.add(ColumnType.WILDCARD);
		}
		
		this.uitleg = cType.getUitleg();
		this.donePressed = false;
	}
	
	/**
	 * Set new type value
	 * @param type new AllowedType value
	 */
	public void setType(AllowedTypes type) {
		this.type = type;
		this.setChanged();
		this.notifyObservers();
	}
	
	public boolean getDonePressed() {
		return this.donePressed;
	}
	
	public void setDonePressed(boolean b) {
		this.donePressed = b;
	}
	
	/**
	 * Check if a name is already in use
	 * @param name The name to check
	 * @return true iff name is in use
	 */
	private boolean nameInUse(String name) {
		if(name.equals(this.oldName)) {
			//You can use the old name again
			return false;
		}
		for(String s : this.tableModel.getColumnNames()) {
			if(s.equals(name) && !s.equals(this.oldName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Find the first unused column name "Column #"
	 * @return the first unused column name
	 */
	private String getFirstFreeName() {
		int i = 1;
		while(this.nameInUse("Column " + i)) {
			i++;
		}
		return "Column " + i;
	}
	
	/**
	 * Set new column name
	 * @param name new name
	 */
	public void setName(String name) {
		if(!name.isEmpty() && !this.nameInUse(name)) {
			this.name = name;
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * @return this column's name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Add an element to enumeration
	 * @param option new element
	 */
	public void addEnumOption(String option) {
		if(!this.enumOptions.contains(option)) {
			//set as last element of current arraylist
			this.enumOptions.set(this.enumOptions.size()-1, option);
			
			//add WILDCARD again
			this.enumOptions.add(ColumnType.WILDCARD);
			
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	/**
	 * Remove last element from enumeration
	 */
	public void removeLastEnumOption() {
		if(this.enumOptions.size()>1) {
			this.enumOptions.remove(this.enumOptions.size()-2);
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	/**
	 * Removes an enum option if that is a valid option and the option is not the wildcard option
	 * @param index the index of the option to remove
	 */
	public void removeEnumOption(int index) {
		if(index >= 0 && index < this.enumOptions.size() && !this.enumOptions.get(index).equals(ColumnType.WILDCARD)) {
			this.enumOptions.remove(index);
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	/**
	 * Set new uitleg for this column
	 * @param uitleg new uitleg
	 */
	public void setUitleg(String uitleg) {
		System.out.println("setUitleg(" + uitleg + ")");
		this.uitleg = uitleg;
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * @return enumeration elements as ArrayList
	 */
	public ArrayList<String> getEnumOptions() {
		return this.enumOptions;
	}
	
	/**
	 * @return enumeration elements as String
	 */
	public String getEnumOptionsString() {
		String ret = new String();
		for(int i = 0; i < this.enumOptions.size(); i++) {
			ret = ret + this.enumOptions.get(i) + "\n";
		}
		return ret;
	}
	
	/**
	 * @return this column's AllowedType
	 */
	public AllowedTypes getType() {
		return this.type;
	}
	
	/**
	 * @return this column's uitleg
	 */
	public String getUitleg() {
		return this.uitleg;
	}
}
