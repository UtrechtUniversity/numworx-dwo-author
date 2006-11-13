package fi.doorziendwo;

import java.awt.*;
import java.util.*;

// a menu bar which can be filled with menutitles, menus (and their submenus)
// in one pass
public class OptionsMenuBar extends MenuBar
 {	// constructor
    public OptionsMenuBar(Object[][] menus, EventListener el)
	{	// for the structure of menus see class ScopeFrame;
		for (int i = 0; i < menus.length; i++)
		{	add(new OptionsMenu(menus[i], el));
		}
	}
    // return the OptionsMenu with name menuName (or null)
	public OptionsMenu getMenu(String menuName)
	{	int count = getMenuCount();
		for (int i = 0; i < count; i++)
        	if (menuName.equals(getMenu(i).getLabel()))
				return (OptionsMenu) getMenu(i);
		return null;
	}
 } // class OptionsMenuBar
