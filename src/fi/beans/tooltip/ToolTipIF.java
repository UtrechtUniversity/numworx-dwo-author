/*
 * Created on Mar 17, 2005
 *
 */
package fi.beans.tooltip;

import java.awt.Component;

/**
 * @author M.J.B. Kupers
 *
 */
public interface ToolTipIF {
    
    
    /**
     * Sets the toolTip of the component.
     * @param toolTip The toolTip to set.
     */
    public void setToolTip(String toolTip);
    
    /**
     * Returns the current toolTip of the component.
     * @return The current toolTip of the component.
     */
    public String getToolTip();
    
    /**
     * Returns the component implementing the ToolTipIF.
     * @return The component implementing the ToolTipIF.
     */
    public Component getComponent();
    

}
