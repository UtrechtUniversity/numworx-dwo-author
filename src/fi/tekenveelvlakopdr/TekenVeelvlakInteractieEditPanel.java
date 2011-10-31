package fi.tekenveelvlakopdr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class TekenVeelvlakInteractieEditPanel extends JPanel implements InteractieEditPanel
{
    private TekenVeelvlak tekenVeelvlak;
    
    private JCheckBox viewerOnlyCB;
    private JCheckBox moveableCB;
    
    private JLabel hulppuntenLabel;
    private JTextField hulppuntenTF;
    
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
        cp.setBounds(600,150,400,650);
        add(cp);
        
        viewerOnlyCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("alleenViewerCBLabel"));
        viewerOnlyCB.setBounds(0,100,150,20);
        viewerOnlyCB.setOpaque(false);
        cp.add(viewerOnlyCB);
        
        moveableCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("draaibaarCBLabel"));
        moveableCB.setBounds(0,130,150,20);
        moveableCB.setOpaque(false);
        moveableCB.setSelected(true);
        cp.add(moveableCB);
        
        hulppuntenLabel = new JLabel(TekenVeelvlakOpdr.rb.getString("hulpPuntenCBLabel"));

        hulppuntenLabel.setBounds(0,170,140,20);
		cp.add(hulppuntenLabel);
		
		hulppuntenTF = new JTextField("0");
		hulppuntenTF.setBounds(140,170,60,20);
		//hulppuntenTF.addActionListener(this);
		//hulppuntenTF.addFocusListener(this);
		cp.add(hulppuntenTF);
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
        int basisFiguur = 1;
        int aantalHulppunten = 0;
        
        tvState = tekenVeelvlak.getState();
        viewerOnly = viewerOnlyCB.isSelected();
        moveable = moveableCB.isSelected();
        basisFiguur = tekenVeelvlak.geefBasisFiguur();
        try{
        	aantalHulppunten = Integer.parseInt(hulppuntenTF.getText());
        }
        catch(Exception e){
        	aantalHulppunten = 0;
        }
        
        Hashtable h = new Hashtable();
        h.put("tvState", tvState);
        h.put("viewerOnly", new Boolean(viewerOnly));
        h.put("moveable", new Boolean(moveable));
        h.put("basisFiguur", new Integer(basisFiguur));
        h.put("aantalHulppunten", new Integer(aantalHulppunten));
        return h;
    }

    
    public void setBounds(int x, int y, int b, int h) {
        super.setBounds(x,y,b,h);
        tekenVeelvlak.setBounds(20,20,500,400);
        setEditState(getEditState());
        
    }

    
    public void setEditState(Hashtable h) {
        Hashtable tvState = new Hashtable();
        boolean viewerOnly = false; 
        boolean moveable = true;
        int basisFiguur = 1;
        int aantalHulppunten = 0;
        
        if(h.containsKey("tvState"))tvState = (Hashtable)h.get("tvState");
        if(h.containsKey("viewerOnly"))viewerOnly = ((Boolean)h.get("viewerOnly")).booleanValue();
        if(h.containsKey("moveable"))moveable = ((Boolean)h.get("moveable")).booleanValue();
        if(h.containsKey("basisFiguur"))basisFiguur = ((Integer)h.get("basisFiguur")).intValue();
        if(h.containsKey("aantalHulppunten"))aantalHulppunten = ((Integer)h.get("aantalHulppunten")).intValue();
        
        //tekenVeelvlak.setBounds(20,20,500,400);
        tekenVeelvlak.zetKiesV(basisFiguur);
       	 tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
	     tekenVeelvlak.setState(tvState);
	     tekenVeelvlak.begin = true;
	     tekenVeelvlak.tekenOpnieuw();
	     //tekenVeelvlak.setEditState(tvState);
	     
	     
	     viewerOnlyCB.setSelected(viewerOnly);
	     moveableCB.setSelected(moveable);
	     hulppuntenTF.setText(""+aantalHulppunten);
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
