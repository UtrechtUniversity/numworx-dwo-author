package fi.tekenveelvlakopdr;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;

import fi.beans.tekstobjects.TekstArea;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class TekenVeelvlakInteractiePanel extends JPanel implements InteractiePanel
{
    private TekenVeelvlak tekenVeelvlak;
    private Viewer3d viewer;
    private boolean viewOnly;
    
    public TekenVeelvlakInteractiePanel()
    {
        setLayout(null);
        
        tekenVeelvlak = new TekenVeelvlak();
        tekenVeelvlak.init();
        add(tekenVeelvlak);
        
        viewer = new Viewer3d(10,10,180,180);
        viewer.setVisible(false);
        add(viewer);
    }
    
    public void setBackground(Color c)
    {
        super.setBackground(c);
        if(tekenVeelvlak!=null)tekenVeelvlak.setBackground(c);
        if(viewer!=null)viewer.setBackground(c);
    }
    
    public void addActionListener(ActionListener al) {
        // TODO Auto-generated method stub
        
    }

    
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

    
    public int geefAsHoogte() {
        // TODO Auto-generated method stub
        return 0;
    }

    
    public InteractieEditPanel getEditPanel() {
        return new TekenVeelvlakInteractieEditPanel();
    }

    
    public Hashtable getEditState() {
        
        
        return null;
    }

    
    public int getIpId() {
        // TODO Auto-generated method stub
        return 0;
    }

    
    public int getScore() {
        // TODO Auto-generated method stub
        return 0;
    }

    
    public int getScoreMax() {
        // TODO Auto-generated method stub
        return 0;
    }

    
    public Hashtable getState() {
        return tekenVeelvlak.getState();
        
    }

    
    public boolean isCorrect() {
        // TODO Auto-generated method stub
        return true;
    }

    
    public boolean isFout() {
        // TODO Auto-generated method stub
        return false;
    }

    
    public void kijkNa() {
        // TODO Auto-generated method stub
        
    }

    
    public void kijkNa(int stapNr) {
        // TODO Auto-generated method stub
        
    }

    
    public void opnieuw() {
        // TODO Auto-generated method stub
        
    }

    
    public void setBounds(int x, int y, int b, int h) {
        super.setBounds(x,y,b,h);
        tekenVeelvlak.setBounds(0,0,b,h);
        tekenVeelvlak.setButtonHeights(h/2-170);
        viewer.setBounds(0,0,b,h);
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
         
         if(viewerOnly)
         {	 tekenVeelvlak.setVisible(false);
        	 viewer.setVisible(true);
        	 viewer.setState(tvState);
         }
         else
         {
        	 viewer.setVisible(false);
        	 tekenVeelvlak.setVisible(true);
        	 tekenVeelvlak.zetKiesV(basisFiguur);
        	 tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
	         tekenVeelvlak.setState(tvState);
	         tekenVeelvlak.begin = true;
	         tekenVeelvlak.tekenOpnieuw();
	         
	         
         }
        
    }

    
    public void setState(Hashtable h) {
        tekenVeelvlak.setState(h);
        
    }

    
    public void start() {
        //tekenVeelvlak.start();
        
    }

    
    public void stop() {
        tekenVeelvlak.stop();
        
    }

    
    public void wis() {
        // TODO Auto-generated method stub
        
    }

    
    public void zetMaat() {
        // TODO Auto-generated method stub
        
    }

    
    public void zetMode(int mode) {
        // TODO Auto-generated method stub
        
    }

    
    public void zetNagekeken(boolean b) {
        // TODO Auto-generated method stub
        
    }

    
    public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {
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
        
        if(viewerOnly)
        {	 tekenVeelvlak.setVisible(false);
	       	 viewer.setVisible(true);
	       	 viewer.setState(tvState);
	       	 viewer.zetMuisAan(moveable);
        }
        else
        {
	       	 viewer.setVisible(false);
	       	 tekenVeelvlak.setVisible(true);
	       	 tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
	         tekenVeelvlak.zetOpdracht(tvState,randomVars,randomValues);
	         tekenVeelvlak.begin = true;
	         tekenVeelvlak.tekenOpnieuw();
        }
        
    }

}
