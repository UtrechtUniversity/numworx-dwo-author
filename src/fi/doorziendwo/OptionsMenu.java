package fi.doorziendwo;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

// class that actually builds the total menu structure of a Frame
public class OptionsMenu extends Menu
{	// attributes used in constructor
    CheckboxMenuItem cmItem;
	MenuItem mItem;
    // constructor
	public OptionsMenu(Object[] items, EventListener el)
	{	// first string in the list is menu title
	    // use shortcut to Table.lookUp
	    super((String) items[0]);
	    // check the rest of the list
		for (int i = 1; i < items.length; i++)
		{	// if items[i] is a String it is a (Checkbox)MenuItem
		    if (items[i] instanceof String)
			{	if (((String)items[i]).length() == 0) // skip empty strings
			    {}
			    // just "-" represents a separator
			    else if ( items[i].equals("-") )
				    addSeparator();
				else // assume some string
				{	String item = (String) items[i];
					char firstChar = item.charAt(0);
					// switch according to firstChar
					switch(firstChar)
					{   // use first character as switch
					    case '+': // an enabled checkbox with v
                            cmItem = new CheckboxMenuItem(item.substring(1));
                            cmItem.addItemListener((ItemListener) el);
    						cmItem.setState(true);
	    					add(cmItem);
						break;
    					case '#': // a disabled checkbox with v (usefull?)
    						cmItem = new CheckboxMenuItem(item.substring(1));
                            cmItem.addItemListener((ItemListener) el);
	    					cmItem.setState(true);
		    				cmItem.setEnabled(false);
			    			add(cmItem);
						break;
                        case '-': // an enabled checkbox without v
    						cmItem = new CheckboxMenuItem(item.substring(1));
                            cmItem.addItemListener((ItemListener) el);
	    					cmItem.setState(false);
		    				add(cmItem);
						break;
                        case '=': // a disabled checkbox without v
    						cmItem = new CheckboxMenuItem(item.substring(1));
                            cmItem.addItemListener((ItemListener) el);
	    					cmItem.setState(false);
		    				cmItem.setEnabled(false);
			    			add(cmItem);
						break;
				        case '~': // a disabled menu item
    						mItem = new MenuItem(item.substring(1));
                            mItem.addActionListener((ActionListener) el);
	    					mItem.setEnabled(false);
		    				add(mItem);
						break;
					    default: // an enabled menu item
    						mItem = new MenuItem(item);
                            mItem.addActionListener((ActionListener) el);
						    add(mItem);
					} // switch
				} // if
			} // if
			// else items[i] is an array and represents a menu
			// so start again
			else
			{	// cast items[i] to an array of Objects
			    add(new OptionsMenu((Object[])items[i], el));
			}
		}
	}
	// short cut for table lookup
    public String tt(String s)
	{   return Table.lookUp(s);
	}
    // return the MenuItem with name itemName (or null)
	public MenuItem getItem(String itemName)
	{	int count = countItems();
		for (int i = 0; i < count; i++)
        	if (itemName.equals(getItem(i).getLabel()))
				return getItem(i);
		return null;
	}
	// when CheckboxMenuItem with name itemName is pressed (and a v appears),
	// remove the v from all other CheckboxMenuItems in this menu
    public void switchto(String itemName)
    {   int count = countItems();
		for (int i = 0; i < count; i++)
		{   MenuItem mi = getItem(i);
		    if (mi instanceof CheckboxMenuItem)
		    {   // cast necessary
		        CheckboxMenuItem nmi = (CheckboxMenuItem) mi;
            	if ( !itemName.equals(nmi.getLabel()) )
				    nmi.setState(false);
				else
				    nmi.setState(true);
			}
		}
    }
    // deselect all selectable items
    public void switchoff()
    {   int count = countItems();
		for (int i = 0; i < count; i++)
		{   MenuItem mi = getItem(i);
		    if (mi instanceof CheckboxMenuItem)
		    {   // cast necessary
		        CheckboxMenuItem nmi = (CheckboxMenuItem) mi;
    		    nmi.setState(false);
			}
		}
    }
    
    // for submenus
 } // class OptionsMenu
