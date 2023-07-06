package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.formuleobjects.*;

public class GrafiekTekenEditPanel extends JPanel 
                    implements InteractieEditPanel , ActionListener
{
    private GrafiekTekenPanel grafiekTekenPanel;
    
    private JCheckBox zoomOptieCB, traceOptieCB, dragOptieCB, buttonCB;
    private JLabel varNaamLabel;
    private JTextField varNaamTF;

//  private JComboBox aantalGrafiekenCombo, typeVerbindingCombo;
//  private int aantalGrafieken, typeVerbinding;
    
    private String varNaam = "x";
    private boolean zoomOptie, traceOptie, dragOptie, buttonOptie;  

    private Tablet tablet;
    private boolean tabletAdded;
    private FormuleVakHouder tabletUser;
    
    public GrafiekTekenEditPanel()
    {   
        setLayout(null);
        setBackground(WiskOpdr.bgcolor);

        grafiekTekenPanel = new GrafiekTekenPanel();
        grafiekTekenPanel.setBounds(10,20,300,440);
        add(grafiekTekenPanel);
        
        zoomOptie = true;
        traceOptie = true;
        dragOptie = true;
        
        zoomOptieCB = 
            maakCheckBox( WiskOpdr.rb.getString("zoomOptie"), 500,50,160,20, zoomOptie);
        traceOptieCB = 
            maakCheckBox( WiskOpdr.rb.getString("traceOptie"), 500,80,160,20, traceOptie);
        dragOptieCB = 
            maakCheckBox( WiskOpdr.rb.getString("dragOptie"), 500,110,160,20, dragOptie);
/*      
        aantalGrafiekenCombo = new JComboBox();
        aantalGrafiekenCombo.setFont(WiskOpdr.tekstFont);   
        aantalGrafiekenCombo.setBackground(getBackground());
        aantalGrafiekenCombo.addItem("1 grafiek");
        aantalGrafiekenCombo.addItem("2 grafieken");
        aantalGrafiekenCombo.addItem("3 grafieken");
        aantalGrafiekenCombo.addItem("4 grafieken");
        aantalGrafiekenCombo.setBounds(500, 140, 190, 20);
        add(aantalGrafiekenCombo);
        aantalGrafiekenCombo.addActionListener(this);
        aantalGrafieken = 1;
*/
/*      
        typeVerbindingCombo = new JComboBox();
        typeVerbindingCombo.setFont(WiskOpdr.tekstFont);    
        typeVerbindingCombo.setBackground(getBackground());
        typeVerbindingCombo.addItem("punten niet verbinden");
        typeVerbindingCombo.addItem("punten verbinden met lijnen");
        typeVerbindingCombo.setBounds(500, 170, 190, 20);
        add(typeVerbindingCombo);
        typeVerbindingCombo.addActionListener(this);
        typeVerbinding = 0;
*/      
                
            
        buttonCB = 
            maakCheckBox("Weergave via pop-up", 500,190,160,20, buttonOptie);
        
        varNaamLabel = new JLabel("Naam van de variabele");
        varNaamLabel.setBounds(500,20,150,20);
        varNaamLabel.setFont(WiskOpdr.tekstFont);
        add(varNaamLabel);
        
        varNaamTF = new JTextField(varNaam);
        varNaamTF.setBounds(680,20,30,20);
        varNaamTF.setFont(WiskOpdr.tekstFont);
        varNaamTF.addActionListener(this);
        add(varNaamTF);
        
    }
    
    private JCheckBox maakCheckBox(String s, int x, int y, int b, int h, boolean selected)
    {   JCheckBox checkbox = new JCheckBox(s);
        checkbox.setBounds(x,y,b,h);
        checkbox.setFont(WiskOpdr.tekstFont);
        checkbox.setBackground(getBackground());
        checkbox.setSelected(selected);
        checkbox.addActionListener(this);
        add(checkbox);
        return checkbox;
    }
    
    public Hashtable getEditState()
    {   
        String varNaam = "x";
        boolean zoomOptie = true;
        boolean traceOptie = true;
        boolean dragOptie = true;
        boolean buttonOptie = false;
        
//      int aantalGrafieken = 1;
//      int typeVerbinding = 0;
        
        varNaam = this.varNaam;
        zoomOptie = this.zoomOptie;
        traceOptie = this.traceOptie;
        dragOptie = this.dragOptie;
        buttonOptie = this.buttonOptie;
        
//      aantalGrafieken = this.aantalGrafieken;
//      typeVerbinding = this.typeVerbinding;
        
        Hashtable h = grafiekTekenPanel.getEditState();
        
        h.put("varNaam", varNaam);
        h.put("zoomOptie", new Boolean(zoomOptie));
        h.put("traceOptie", new Boolean(traceOptie));
        h.put("dragOptie", new Boolean(dragOptie));
        h.put("buttonOptie", new Boolean(buttonOptie));
        
//      h.put("aantal grafieken", new Integer(aantalGrafieken));
//      h.put("type verbinding", new Integer(typeVerbinding));
        
        return h;
    }
    
    public void setEditState(Hashtable h)
    {
        String varNaam = "x";
        boolean zoomOptie = true;
        boolean traceOptie = true;
        boolean dragOptie = true;
        boolean buttonOptie = false;

//      int aantalGrafieken = 1;
//      int typeVerbinding = 0;
                    
        if(h.containsKey("varNaam")) 
            varNaam = (String)h.get("varNaam");
        if(h.containsKey("zoomOptie")) 
            zoomOptie = ((Boolean)h.get("zoomOptie")).booleanValue();
        if(h.containsKey("traceOptie")) 
            traceOptie = ((Boolean)h.get("traceOptie")).booleanValue();
        if(h.containsKey("dragOptie")) 
            dragOptie = ((Boolean)h.get("dragOptie")).booleanValue();
        if(h.containsKey("buttonOptie")) 
            buttonOptie = ((Boolean)h.get("buttonOptie")).booleanValue();

//      if(h.containsKey("aantal grafieken")) 
//          aantalGrafieken = ((Integer)h.get("aantal grafieken")).intValue();
//      if(h.containsKey("type verbinding")) 
//          typeVerbinding = ((Integer)h.get("type verbinding")).intValue();
        
        this.varNaam = varNaam;;
        this.zoomOptie = zoomOptie;
        this.traceOptie = traceOptie;
        this.dragOptie = dragOptie;
        this.buttonOptie = buttonOptie;
//      this.aantalGrafieken = aantalGrafieken;
//      this.typeVerbinding = typeVerbinding;
        
        varNaamTF.setText(varNaam);
        zoomOptieCB.setSelected(zoomOptie);
        traceOptieCB.setSelected(traceOptie);
        dragOptieCB.setSelected(dragOptie);
        buttonCB.setSelected(buttonOptie);
        
//      aantalGrafiekenCombo.setSelectedIndex(aantalGrafieken - 1);
//      typeVerbindingCombo.setSelectedIndex(typeVerbinding);
        
        grafiekTekenPanel.zetTraceOptie(traceOptie);
        grafiekTekenPanel.zetZoomOptie(zoomOptie);
        grafiekTekenPanel.zetDragOptie(dragOptie);
        
        grafiekTekenPanel.setEditState(h);
        grafiekTekenPanel.setButton(false);
        
    }
    
    public void zetBreedte(int b)
    {   grafiekTekenPanel.setSize(b,grafiekTekenPanel.getSize().height);
    }
    public void zetHoogte(int h)
    {   grafiekTekenPanel.setSize(grafiekTekenPanel.getSize().width, h);
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
        if(e.getSource().equals(varNaamTF))
        {   varNaam = varNaamTF.getText();
            grafiekTekenPanel.zetVarNaam(varNaam);
        }
        if(e.getSource().equals(zoomOptieCB))
        {   zoomOptie = zoomOptieCB.isSelected();
            grafiekTekenPanel.zetZoomOptie(zoomOptie);
        }
        if(e.getSource().equals(traceOptieCB))
        {   traceOptie = traceOptieCB.isSelected();
            grafiekTekenPanel.zetTraceOptie(traceOptie);
        }
        if(e.getSource().equals(dragOptieCB))
        {   dragOptie = dragOptieCB.isSelected();
            grafiekTekenPanel.zetDragOptie(dragOptie);
        }
        if(e.getSource().equals(buttonCB))
        {   buttonOptie = buttonCB.isSelected();
            
        }
/*      
        if(e.getSource().equals(aantalGrafiekenCombo))
        {   aantalGrafieken = aantalGrafiekenCombo.getSelectedIndex() + 1;
//System.out.println("ng= " + aantalGrafieken);         
            grafiekTekenPanel.getGrafiekTekenEditor().setNumGraphs(aantalGrafieken);
        }
        if(e.getSource().equals(typeVerbindingCombo))
        {   typeVerbinding = typeVerbindingCombo.getSelectedIndex();
//System.out.println("tv= " + typeVerbinding);  
            grafiekTekenPanel.getGrafiekTekenEditor().setConnectMode(typeVerbinding);                       
        }
*/      
        
    }
/*  
    public void zetTabletUser(FormuleVakHouder formuleVakHouder)
    {   if(tablet==null) return;
        tablet.zetFormuleVakHouder(formuleVakHouder);
        tabletUser = formuleVakHouder;
        
    }
*/
/*  
    public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
    {   if(tablet==null) 
        {   tablet = new Tablet(formuleVakHouder);
            tablet.setLocation(x,y);
            
        }
        tablet.zetFormuleVakHouder(formuleVakHouder);
        tabletUser = formuleVakHouder;
        
        
    }
*/
/*  
    public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
    {   if(tablet==null) 
        {   tablet = new Tablet(formuleVakHouder);
            
            
        }
        if(!tabletAdded)
        {   add(tablet,0);
            tablet.setLocation(x,y);
            tabletAdded = true;
            //resize();
            repaint();
        }
        tablet.zetFormuleVakHouder(formuleVakHouder);
    }
*/
/*  
    public void removeTablet()
    {   if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
        tabletAdded = false;
    }
*/  
    //ActionProducer
    private ActionListener actionListener = null;
    
    public void addActionListener(ActionListener l) 
    {   actionListener = AWTEventMulticaster.add(actionListener,l);
    }
    
    public void removeActionListener(ActionListener l)
    {   actionListener = AWTEventMulticaster.remove(actionListener, l);
    }   
    
    public void produceAction(String command)
    {   if (actionListener != null)
        {   actionListener.actionPerformed( new ActionEvent(this, 0, command) );
        }
    }
    //end ActionProducer
    
    
}
