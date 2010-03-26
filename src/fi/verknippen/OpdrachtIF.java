/*
 * Created on Feb 22, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fi.verknippen;

import java.awt.Component;
import java.awt.event.ActionListener;

public interface OpdrachtIF
{
    public Component getComponent();
    public void addActionListener(ActionListener al);
    public void setText(String text);
    public void setState(int i);
    public void setSelected(boolean select);
    public boolean isSelected();
    public boolean isEnabled();
    public void setEnabled(boolean enable);
    public int getState();
    public Object clone(); // prototype pattern

}
