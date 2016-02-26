package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.Font;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.tekstobjects.*;

public class TekstEditorEditPanel extends JPanel implements InteractieEditPanel , ActionListener, TabletOwner
{
	private TekstEditor tekstEditor;
	
	private JCheckBox balkZichtbaarCB, rekenToolCB, grafToolCB, formuleKnopCB, formuleToolPopupCB, buttonCB, boxMetRandCB;
	private JLabel varNaamLabel;
	
	private JCheckBox logCB;
	private JTextField logIDField;
	
	private boolean balkZichtbaar, rekenTool, grafTool, formuleKnop, formuleToolPopup, buttonOptie, boxMetRand;
	
	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;
	
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	
	public TekstEditorEditPanel()
	{	
		setLayout(null);
		setBackground(WiskOpdr.bgcolor);
		tekstEditor = new TekstEditor();
		tekstEditor.setBounds(10,20,300,440);
		add(tekstEditor);
		
		balkZichtbaar = true;
		rekenTool = false;
		grafTool = false;
		formuleKnop = true;
		formuleToolPopup = false;
		boxMetRand = true;
		
		balkZichtbaarCB = maakCheckBox(WiskOpdr.rb.getString("TEEP_menuBalkOptie"), 500,50,160,20, balkZichtbaar);
		rekenToolCB = maakCheckBox(WiskOpdr.rb.getString("TEEP_rekenToolOptie"), 500,80,160,20, rekenTool);
		grafToolCB = maakCheckBox(WiskOpdr.rb.getString("TEEP_grafToolOptie"), 500,110,160,20, grafTool);
		formuleKnopCB = maakCheckBox("Formuleknop ", 500,110,160,20, formuleKnop);
		formuleToolPopupCB = maakCheckBox("Formuletool als popup", 500,140,160,20, formuleToolPopup);
		buttonCB = maakCheckBox("Weergave via pop-up", 500,190,160,20, buttonOptie);
		boxMetRandCB = maakCheckBox(WiskOpdr.rb.getString("boxMetRand"), 500,190,160,20, boxMetRand);
		
		// nog even niet
		//remove(rekenToolCB);
		remove(formuleKnopCB);
		remove(formuleToolPopupCB);
		remove(buttonCB);
		
		logCB = new JCheckBox(WiskOpdr.rb.getString("logCBLabel"));
        logCB.setBounds(450,5,70,20);
        logCB.addActionListener(this);
        logCB.setOpaque(false);
		add(logCB);
		
		logIDField = new JTextField("0");
		logIDField.setBounds(520,5,60,20);
		logIDField.addActionListener(this);
		logIDField.setVisible(false);
		add(logIDField);

		
	}
	
	private JCheckBox maakCheckBox(String s, int x, int y, int b, int h, boolean selected)
	{	JCheckBox checkbox = new JCheckBox(s);
		checkbox.setBounds(x,y,b,h);
		checkbox.setFont(font);
		checkbox.setBackground(getBackground());
		checkbox.setSelected(selected);
		checkbox.addActionListener(this);
		add(checkbox);
		
		return checkbox;
	}
	
	public Hashtable getEditState()
	{	
		boolean balkZichtbaar = true;
		boolean rekenTool = false;
		boolean grafTool = false;
		boolean formuleKnop = true;
		boolean formuleToolPopup = true;
		boolean buttonOptie = false;
		boolean boxMetRand = true;
		boolean logOption;
		String  logID;
		
		balkZichtbaar = this.balkZichtbaar;
		rekenTool = this.rekenTool;
		grafTool = this.grafTool;
		formuleKnop = this.formuleKnop;
		formuleToolPopup = this.formuleToolPopup;
		buttonOptie = this.buttonOptie;
		
		boxMetRand = boxMetRandCB.isSelected();
		logOption = logCB.isSelected();
		logID = logIDField.getText();
		
		Hashtable h = tekstEditor.getEditState();
		
		h.put("balkZichtbaar", new Boolean(balkZichtbaar));
		h.put("rekenTool", new Boolean(rekenTool));
		h.put("grafTool", new Boolean(grafTool));
		h.put("formuleKnop", new Boolean(formuleKnop));
		h.put("formuleToolPopup", new Boolean(formuleToolPopup));
		h.put("buttonOptie", new Boolean(buttonOptie));
		h.put("boxMetRand", new Boolean(boxMetRand));
		if(logOption) {
			h.put("logOption", Boolean.TRUE);
		} else {
			h.remove("logOption");
		}
		h.put("logID", logID);
		
		
		return h;
	}
	
	public void setEditState(Hashtable h)
	{
		boolean balkZichtbaar = true;
		boolean rekenTool = false;
		boolean grafTool = false;
		boolean formuleKnop = true;
		boolean formuleToolPopup = true;
		boolean buttonOptie = false;
		boolean boxMetRand = true;
		boolean logOption = false;
		String logID = "";
				
		if(h.containsKey("balkZichtbaar")) balkZichtbaar = ((Boolean)h.get("balkZichtbaar")).booleanValue();
		if(h.containsKey("rekenTool")) rekenTool = ((Boolean)h.get("rekenTool")).booleanValue();
		if(h.containsKey("grafTool")) grafTool = ((Boolean)h.get("grafTool")).booleanValue();
		if(h.containsKey("formuleKnop")) formuleKnop = ((Boolean)h.get("formuleKnop")).booleanValue();
		if(h.containsKey("formuleToolPopup")) formuleToolPopup = ((Boolean)h.get("formuleToolPopup")).booleanValue();
		if(h.containsKey("buttonOptie")) buttonOptie = ((Boolean)h.get("buttonOptie")).booleanValue();
		if(h.containsKey("boxMetRand")) boxMetRand = ((Boolean)h.get("boxMetRand")).booleanValue();
		if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		
		this.balkZichtbaar = balkZichtbaar;
		this.rekenTool = rekenTool;
		this.grafTool = grafTool;
		this.formuleKnop = formuleKnop;
		this.formuleToolPopup = formuleToolPopup;
		this.buttonOptie = buttonOptie;
		this.boxMetRand = boxMetRand;
		
		balkZichtbaarCB.setSelected(balkZichtbaar);
		rekenToolCB.setSelected(rekenTool);
		grafToolCB.setSelected(grafTool);
		formuleKnopCB.setSelected(formuleKnop);
		formuleToolPopupCB.setSelected(formuleToolPopup);
		buttonCB.setSelected(buttonOptie);
		boxMetRandCB.setSelected(boxMetRand);
		
        logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);

		
		//grafiekPanel.zetFormulesZichtbaar(formulesZichtbaar);
		//grafiekPanel.zetTraceOptie(traceOptie);
		//grafiekPanel.zetZoomOptie(zoomOptie);
		//grafiekPanel.zetDragOptie(dragOptie);
		
		tekstEditor.setEditState(h);
		tekstEditor.setButton(false);
		
	}
	
	public void zetBreedte(int b)
	{	tekstEditor.setSize(b,tekstEditor.getSize().height);
	}
	public void zetHoogte(int h)
	{	tekstEditor.setSize(tekstEditor.getSize().width, h);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
	}
	
	public void wis(){}
    
	public void zetMode(int mode){}
	
    public void stop(){}
    
    public void start(){}
	
	public void actionPerformed(ActionEvent e)
	{	
		
		if(e.getSource().equals(balkZichtbaarCB))
		{	balkZichtbaar = balkZichtbaarCB.isSelected();
			tekstEditor.zetBalkZichtbaar(balkZichtbaar);
		}
		if(e.getSource().equals(rekenToolCB))
		{	rekenTool = rekenToolCB.isSelected();
			tekstEditor.zetRekenTool(rekenTool);
		}
		if(e.getSource().equals(grafToolCB))
		{	grafTool = grafToolCB.isSelected();
			tekstEditor.zetGrafTool(grafTool);
		}
		if(e.getSource().equals(formuleKnopCB))
		{	formuleKnop = formuleKnopCB.isSelected();
			tekstEditor.zetFormuleKnop(formuleKnop);
		}
			if(e.getSource().equals(formuleToolPopupCB))
		{	formuleToolPopup = formuleToolPopupCB.isSelected();
			tekstEditor.zetFormuleToolPopup(formuleToolPopup);
		}
		if(e.getSource().equals(buttonCB))
		{	buttonOptie = buttonCB.isSelected();
			
		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }

	}
	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{	if(tablet==null) return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
	}
	
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x,y);
			
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
		
	}
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			
			
		}
		if(!tabletAdded)
		{	add(tablet,0);
			tablet.setLocation(x,y);
			tabletAdded = true;
			//resize();
            repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}
	
	public void removeTablet()
	{	if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
		tabletAdded = false;
	}
	
	public Tablet getTablet()
	{	return tablet;
	}
	
	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//end ActionProducer
	
	
}
