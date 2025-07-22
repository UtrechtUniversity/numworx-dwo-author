package fi.geodefull;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class GeodeFullInteractiePanel extends JPanel implements InteractiePanel
{
    private TekenApplet3D tekenApplet3d;
    private String figuur;
    
    public GeodeFullInteractiePanel()
    {
        setLayout(null);
        
        tekenApplet3d = new TekenApplet3D();
        tekenApplet3d.init();
        add(tekenApplet3d);
        
        
    }
    
    public void setBackground(Color c)
    {
        super.setBackground(c);
        if(tekenApplet3d!=null)tekenApplet3d.setBackground(c);
        
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
        return new GeodeFullInteractieEditPanel();
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
        return tekenApplet3d.getState();
        
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
        tekenApplet3d.setBounds(0,0,b,h);
        tekenApplet3d.tekenOpnieuw();
        
        /*String name = "fi.geodefull." + figuur + "Prog";
    	try
		{	Class c = Class.forName(name);
	    	Constructor cc = c.getDeclaredConstructor(new Class[] { } );
	    	Object o = cc.newInstance(new Object[] {} );
	    	
	    	if(tekenApplet3d!=null)remove(tekenApplet3d);
	    	tekenApplet3d = (TekenApplet3D)o;
		    
	    	tekenApplet3d.setBounds(0,0,b, h);
	    	tekenApplet3d.doLayout();
			add(tekenApplet3d);
			tekenApplet3d.init();
			//tekenApplet3d.doLayout();
			
			
		}
    	catch(Exception ex)
		{	System.out.println(ex.toString());
			
		}
        */
        
    }
    
    


    
    public void setEditState(Hashtable h) {
    	String figuur = "Voetbal";
    	Hashtable state = new Hashtable();
    	 
        if(h.containsKey("figuur"))figuur = (String)h.get("figuur");
        if(h.containsKey("state"))state = (Hashtable)h.get("state");
        
        this.figuur = figuur;
        
        String name = "fi.geodefull." + figuur + "Prog";
    	try
		{	Class c = Class.forName(name);
	    	Constructor cc = c.getDeclaredConstructor(new Class[] { } );
	    	Object o = cc.newInstance(new Object[] {} );
	    	
	    	if(tekenApplet3d!=null)remove(tekenApplet3d);
	    	tekenApplet3d = (TekenApplet3D)o;
		    
	    	tekenApplet3d.setBounds(0,0,getWidth(), getHeight());
			
			add(tekenApplet3d);
			tekenApplet3d.init();
			tekenApplet3d.doLayout();
			
			tekenApplet3d.setBounds(0,0,getWidth(), getHeight());
			//resize(getSize().width+1,getSize().height+1);
			tekenApplet3d.setState(state);
		}
    	catch(Exception ex)
		{	System.out.println(ex.toString());
			
		}
         
        
    }

    
    public void setState(Hashtable h) {
    	tekenApplet3d.setState(h);
        
    }

    
    public void start() {
        //tekenVeelvlak.start();
        
    }

    
    public void stop() {
        //tekenVeelvlak.stop();
        
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
    	String figuur = "Voetbal";
    	Hashtable state = new Hashtable();
    	
    	
        if(h.containsKey("figuur"))figuur = (String)h.get("figuur");
        if(h.containsKey("state"))state = (Hashtable)h.get("state");
        
        this.figuur = figuur;
        
        String name = "fi.geodefull." + figuur + "Prog";
        try
		{	Class c = Class.forName(name);
	    	Constructor cc = c.getDeclaredConstructor(new Class[] { } );
	    	Object o = cc.newInstance(new Object[] {} );
	    	
	    	if(tekenApplet3d!=null)remove(tekenApplet3d);
	    	tekenApplet3d = (TekenApplet3D)o;
		    
	    	tekenApplet3d.setBounds(0,20,getWidth(), getHeight()-20);
			
			add(tekenApplet3d);
			tekenApplet3d.init();
			tekenApplet3d.doLayout();
			
			tekenApplet3d.setBounds(0,0,getWidth(), getHeight());
			tekenApplet3d.setState(state);
			//resize(getSize().width+1,getSize().height+1);
		}
    	catch(Exception ex)
		{	System.out.println(ex.toString());
			
		}
    }

	@Override
	public int[][] getScoreObjectives() {
		return null;
	}

}
