package fi.tekenveelvlakopdr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class TekenVeelvlakInteractieEditPanel extends JPanel implements InteractieEditPanel
{
    private TekenVeelvlak tekenVeelvlak;
    
    private JCheckBox viewerOnlyCB;
    private JCheckBox moveableCB;
    private JCheckBox pijlOmlaagCB;
    private JCheckBox pijlZichtbaarCB;
    private JCheckBox vijftallenZichtbaarCB;
    private JCheckBox tientallenZichtbaarCB;
    private JCheckBox horizontaalCB;
    private JCheckBox eenhedenNummersCB;
    private JCheckBox tientallenNummersCB;
    private JCheckBox vijftallenNummersCB;
    
    private JPanel cp;
    
    public TekenVeelvlakInteractieEditPanel(){
        setLayout(null);
        
        tekenVeelvlak = new TekenVeelvlak();
        tekenVeelvlak.setBounds(20,20,500,400);
        tekenVeelvlak.init();
        add(tekenVeelvlak);
        
        cp = new JPanel();
        cp.setLayout(null);
        cp.setOpaque(false);
        cp.setBounds(400,150,400,650);
        add(cp);
        
        viewerOnlyCB = new JCheckBox("Viewer");
        viewerOnlyCB.setBounds(0,100,150,20);
        viewerOnlyCB.setOpaque(false);
        cp.add(viewerOnlyCB);
        
        moveableCB = new JCheckBox("Draaibaar");
        moveableCB.setBounds(0,130,150,20);
        moveableCB.setOpaque(false);
        cp.add(moveableCB);
    }
    
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }

    
    public void addActionListener(ActionListener al) {
        // TODO Auto-generated method stub
        
    }

    
    public Hashtable getEditState() {
        Hashtable tvState = new Hashtable();
        boolean viewerOnly = false; 
        boolean moveable = true;
        
        tvState = tekenVeelvlak.getState();
        viewerOnly = viewerOnlyCB.isSelected();
        moveable = moveableCB.isSelected();
        
        Hashtable h = new Hashtable();
        h.put("tvState", tvState);
        h.put("viewerOnly", new Boolean(viewerOnly));
        h.put("moveable", new Boolean(moveable));
        
        return h;
    }

    
    public void setBounds(int x, int y, int b, int h) {
        super.setBounds(x,y,b,h);
        
    }

    
    public void setEditState(Hashtable h) {
        Hashtable tvState = new Hashtable();
        boolean viewerOnly = false; 
        boolean moveable = true;
        
        if(h.containsKey("tvState"))tvState = (Hashtable)h.get("tvState");
        if(h.containsKey("viewerOnly"))viewerOnly = ((Boolean)h.get("viewerOnly")).booleanValue();
        if(h.containsKey("moveable"))moveable = ((Boolean)h.get("moveable")).booleanValue();
        
        tekenVeelvlak.setEditState(tvState);
        viewerOnlyCB.setSelected(viewerOnly);
        moveableCB.setSelected(moveable);
        
        
        
        
        
    }

    
    public void start() {
        // TODO Auto-generated method stub
        
    }

    
    public void stop() {
        // TODO Auto-generated method stub
        
    }

    
    public void wis() {
        // TODO Auto-generated method stub
        
    }

    
    public void zetBreedte(int b) {
        // TODO Auto-generated method stub
        
    }

    
    public void zetHoogte(int h) {
        // TODO Auto-generated method stub
        
    }

    
    public void zetMode(int mode) {
        // TODO Auto-generated method stub
        
    }

}
