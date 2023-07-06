package fi.beans.lwmobjects_swing;

import java.util.Vector;

/**
 * A MovePermissions Object contains a list (Vector) of LWMContainers that a LWMComponent
 * is permitted to move into.
 * A LWMComponent may be associated with an instance of MovePermissions, but the
 * associated MovePermissions may be null. In that case all moves are permitted.
 * Note: non-movable LWMComponents don't need a MovePermissions, they cannot be moved
 * even if the associated MovePermissions is null. This especially goes for LWMRootContainer.
 * 
 * @author Paul Bergervoet
 *
 * @version 1, 1 september 2000
 */

public class MovePermissions extends Object
{	// variables
	Vector containerlijst;

/**
 * Constructs a new, empty MovePermissions object.
 */
	public MovePermissions()
	{	containerlijst = new Vector();
	}

/**
 * Add a LWMContainer to the permitted list
 *
 * param c The permitted LWMContainer.
 */
	public void addPermission(LWMContainer c)
	{	containerlijst.addElement(c);
	}

/**
 * Checks if a LWMContainer is permitted by this MovePermissions
 *
 * @return boolean True if it is permitted!
 */
	public boolean isPermitted(LWMContainer c)
	{	return containerlijst.contains(c);
	}
}
