package fi.javalogoweb;

import javax.swing.JTextField;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.*;

import fi.javalogoweb.expressies.*;
import fi.javalogoweb.formuleobjects.*;

/**
 * TextField for editing parameters and other changeable values in CommandComponents.
 * It must be initialized by the owner CC, will appear by cal to vulIn(..) and disappear
 * with call back to owner when it loses focus.
 * 
 * Focus is a bit complicated. You will typically start editing when clicking the CC,
 * but when you click the same CC while editing, you will want to finish editing and not edit again.
 * So loss of focus is arranged by requestFocus() when:
 * (1) clicking any component that's not a CC (JavaLogoSchuifVeld.mousePressed, buttons)
 * (2) dragging any CC (CommandComponent.mouseDragged)
 * (3) clicking a CC without parameters (CommandComponent.mouseReleased)
 * (4) clicking a different editable CC (automatically by focus request of that CC's TextField)
 * So a second click on the owner CC will reach that CC without loss of focus and CC
 * may decide what to do (end edit, edit second parameter...)
 * 
 * @author berge020
 */
public class ParameterTextField extends JTextField implements FocusListener, ActionListener
{
	private ParameterEditorListener owner;
	private int minimumWidth = 60;
	private FontMetrics fm;
	
	public ParameterTextField(int x, int y, int b, int h, ParameterEditorListener o)
	{	
		setBounds(x,y,b,h);
		owner = o;
		addActionListener(this);
		addFocusListener(this);
		setVisible(false);
		setEnabled(false);
		setFont(JavaLogoWeb.defaultfont);
		fm = getFontMetrics(JavaLogoWeb.defaultfont);	
	}
	
	public void vulIn(String text)
	{	
		setText(text);
		int breedte = Math.max(minimumWidth, fm.stringWidth(getText())+40);
		int space = getParent().getWidth()-getX();
		if ( space < minimumWidth )
		{
			// less than minimumSpace space at separatorrX, move to left a bit
			setBounds(getParent().getWidth()-minimumWidth, getY(), minimumWidth, getHeight());
		} else
		{
			// adjust width to prevent the TextField from going outside its parent
			breedte = Math.min(breedte, space);
			setSize(breedte,getSize().height);
		}
		setVisible(true);
		setEnabled(true);
		selectAll();
		requestFocus();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Just disable. This will trigger a 'focus lost' event that will report the change.
		setEnabled(false);
	}
	
	public void focusLost(FocusEvent e)
	{	
		owner.parameterEdited(getText());
		setVisible(false);
		setEnabled(false);
	}
	
	public void focusGained(FocusEvent e)
	{ }
	
}
