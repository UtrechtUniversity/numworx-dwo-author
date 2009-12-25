package fi.doorziendwo;

import java.awt.Button;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;


public class DoorzienInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener
{
	ViewPanel viewPanel;
	private Font ifFont = new Font("SansSerif",Font.PLAIN,12);
	
	private JCheckBox rotateOptionCB;
	private JCheckBox borderOptionCB;
	private JCheckBox designOptionCB;
	private JCheckBox resetOptionCB;
	private JCheckBox foldOptionCB;
	
	private boolean rotateOption;
	private boolean borderOption;
	private boolean designOption;
	private boolean resetOption;
	private boolean foldOption;
	
	
	
	
	public DoorzienInteractieEditPanel()
	{
		this(0,0,250,250);
	}
	public DoorzienInteractieEditPanel(int x, int y, int b, int h)
	{	
		setLayout(null);
		
		rotateOption = true;
		borderOption = false;
		designOption = false;
		resetOption = false;
		foldOption = false;
		
		viewPanel = new ViewPanel(15,15,360,360);
		add(viewPanel);
		viewPanel.setBordered(borderOption);
		viewPanel.setMouse(rotateOption);
		viewPanel.setChangeable(designOption);	
		viewPanel.setBackground(getBackground());
		
		String editModeState = "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";
		viewPanel.setState(editModeState);
		
		rotateOptionCB = maakCheckBox("Draaibaar", 600,30,180,20, rotateOption);
		borderOptionCB = maakCheckBox("Rand", 600,60,180,20, borderOption);
		designOptionCB = maakCheckBox("Ontwerpmogelijkheid", 600,90,180,20, designOption);
		resetOptionCB = maakCheckBox("reset-optie", 600,120,180,20, resetOption);
		foldOptionCB = maakCheckBox("Vouwslider", 600,150,180,20, foldOption);
	}
	
	private JCheckBox maakCheckBox(String s, int x, int y, int b, int h, boolean selected)
	{	JCheckBox checkbox = new JCheckBox(s);
		checkbox.setBounds(x,y,b,h);
		checkbox.setOpaque(false);
		checkbox.setBackground(getBackground());
		checkbox.setSelected(selected);
		checkbox.addActionListener(this);
		add(checkbox);
		
		return checkbox;
	}
	
	public void setBackground(Color c)
	{
		if(viewPanel != null)viewPanel.setBackground(c);
		super.setBackground(c);
	}
	public void setEditState(Hashtable h)
	{
		String startFiguurString = null;
		boolean rotateOption = true;
		boolean borderOption = false;
		boolean designOption = false;
		boolean resetOption = false;
		boolean foldOption = false;
		
		if(h.containsKey("startFiguurString")) startFiguurString = (String)h.get("startFiguurString");
		if(h.containsKey("rotateOption")) rotateOption = ((Boolean)h.get("rotateOption")).booleanValue();
		if(h.containsKey("borderOption")) borderOption = ((Boolean)h.get("borderOption")).booleanValue();
		if(h.containsKey("designOption")) designOption = ((Boolean)h.get("designOption")).booleanValue();
		if(h.containsKey("resetOption")) resetOption = ((Boolean)h.get("resetOption")).booleanValue();
		if(h.containsKey("foldOption")) foldOption = ((Boolean)h.get("foldOption")).booleanValue();
	    
		viewPanel.setEditState(h);
		this.rotateOption = rotateOption;
		this.borderOption = borderOption;
		this.designOption = designOption;
		this.resetOption = resetOption;
		this.foldOption = foldOption;
		
		rotateOptionCB.setSelected(rotateOption);
		borderOptionCB.setSelected(borderOption);
		designOptionCB.setSelected(designOption);
		resetOptionCB.setSelected(resetOption);
		foldOptionCB.setSelected(foldOption);
	}
	
	public Hashtable getEditState()
	{
		String startFiguurString = null;
		boolean rotateOption;
		boolean borderOption;
		boolean designOption;
		boolean resetOption = false;
		boolean foldOption = false;
		
		startFiguurString = viewPanel.getStateString();
		rotateOption = this.rotateOption;
		borderOption = this.borderOption;
		designOption = this.designOption;
		resetOption = this.resetOption;
		foldOption = this.foldOption;
		
	    Hashtable h = new Hashtable(); //viewPanel.getEditState();
		h.put("startFiguurString",startFiguurString);
		h.put("rotateOption", new Boolean(rotateOption));
		h.put("borderOption", new Boolean(borderOption));
		h.put("designOption", new Boolean(designOption));
		h.put("resetOption", new Boolean(resetOption));
		h.put("foldOption", new Boolean(foldOption));
		
		return h;
	}
		
	public void setBounds(int x, int y, int b, int h)
	{	
		super.setBounds(x,y,b,h);
	}
	
	public void zetBreedte(int b)
	{
		viewPanel.setSize(b, viewPanel.getSize().height);
	}
	
	public void zetHoogte(int h)
	{
		viewPanel.setSize(viewPanel.getSize().width, h);
	}
	
	public void wis(){}
    
	public void zetMode(int mode){}
	
    public void stop(){}
    
    public void start(){}
    
    public void addActionListener(ActionListener al){}
    
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(rotateOptionCB))
		{	rotateOption = rotateOptionCB.isSelected();
			viewPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(borderOptionCB))
		{	borderOption = borderOptionCB.isSelected();
			viewPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(designOptionCB))
		{	designOption = designOptionCB.isSelected();
			viewPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(resetOptionCB))
		{	resetOption = resetOptionCB.isSelected();
			viewPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(foldOptionCB))
		{	foldOption = foldOptionCB.isSelected();
			viewPanel.setEditState(getEditState());
		}
	}
}
