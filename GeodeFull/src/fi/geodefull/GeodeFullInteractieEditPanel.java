package fi.geodefull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class GeodeFullInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener
{
    private TekenApplet3D tekenApplet3d;
    
    
    private JPanel cp;
    
    private JComboBox choice;
	private String[] choiceStrings = 
	{		"AntiPrisma",
			"Arch355Dod",
			"Arch355",
			"BewijsEuclides",
			"BouwplaatF11",
			"Diagonalen",
			"Fullereen40",
			"Geode12",
			"Geode21",
			"Geode21Tr",
			"Geode21Vouw",
			"Geode40",
			"GeodeDual",
			"GeodeFul",
			"GeodeKleur",
			"GeodeMaak",
			"IcoKnot",
			"Ico",
			"KubKnot",
			"KubOct",
			"KubTet",
			"KubusKnot",
			"KunstFul43",
			"KunstGeod43",
			"KunstGeod43Tr",
			"KunstGeod44",
			"PlatoBouwplaat",
			"PlatoDual",
			"PlatoKnotDual",
			"PlatoKnot",
			"Plato",
			"PlatoStulp",
			"Prisma",
			"Pyramide",
			"Triangulatie",
			"van6Naar5",
			"Veelhoek",
			"Viervlak",
			"Vijfvlak1",
			"Vijfvlak2",
			"Vijfvlak3",
			"Vlakvulling3",
			"Vlakvullingen5en10",
			"Voetbal",
			"Zeshoek",
			"Zeshoekrooster"
	};
    
    public GeodeFullInteractieEditPanel(){
        setLayout(null);
        
        tekenApplet3d = new TekenApplet3D();
        tekenApplet3d.setBounds(20,20,600,450);
        tekenApplet3d.init();
        
        add(tekenApplet3d);
        
        cp = new JPanel();
        cp.setLayout(null);
        cp.setOpaque(false);
        cp.setBounds(640,150,400,650);
        add(cp);
        
        choice = new JComboBox();
        choice.setBounds(0,100,150,20);
        for(int i=0 ; i<choiceStrings.length ; i++)
		{
			choice.addItem(choiceStrings[i]);
		}
		choice.addActionListener(this);
        cp.add(choice);
        
        doLayout();
       
    }
    
    public void actionPerformed(ActionEvent e) {
    	String name = "fi.geodefull." + (String)choice.getSelectedItem() + "Prog";
    	try
		{	Class c = Class.forName(name);
	    	Constructor cc = c.getDeclaredConstructor(new Class[] { } );
	    	Object o = cc.newInstance(new Object[] {} );
	    	
	    	if(tekenApplet3d!=null)remove(tekenApplet3d);
	    	tekenApplet3d = (TekenApplet3D)o;
		    
	    	tekenApplet3d.setBounds(0,0,600, 450);
			
			add(tekenApplet3d);
			tekenApplet3d.init();
			tekenApplet3d.doLayout();
			
			/*Thread startDraad = new Thread()
			{	public void run()
				{	
		    		try
		    		{   sleep(200);
					}
		    		catch(InterruptedException e)    
					{ }
		    		setSize(getSize().width+1,getSize().height+1);
		    		tekenApplet3d.doLayout();
				}
			};
			startDraad.start();
			*/
			//resize(getSize().width+1,getSize().height+1);
			
			
			
			
			
		}
		catch(Exception ex)
		{	System.out.println(ex.toString());
			
		}
		
        
    }

    
    public void addActionListener(ActionListener al) {
        // TODO Auto-generated method stub
        
    }

    
    public Hashtable getEditState() {
        String figuur = "Voetbal";
        Hashtable state = new Hashtable();
        
        figuur = (String)choice.getSelectedItem();
        state = tekenApplet3d.getState();
        
        Hashtable h = new Hashtable();
        h.put("figuur", figuur);
        h.put("state", state);
        
        return h;
    }

    
    public void setBounds(int x, int y, int b, int h) {
        super.setBounds(x,y,b,h);
        tekenApplet3d.setBounds(20,20,600, 450);
        tekenApplet3d.tekenOpnieuw();
        //setEditState(getEditState());
        
    }

    
    public void setEditState(Hashtable h) {
    	String figuur = "Voetbal";
    	Hashtable state = new Hashtable();
                
        if(h.containsKey("figuur"))figuur = (String)h.get("figuur");
        if(h.containsKey("state"))state = (Hashtable)h.get("state");
        
        choice.setSelectedItem(figuur);
        
        String name = "fi.geodefull." + figuur + "Prog";
        try
		{	Class c = Class.forName(name);
	    	Constructor cc = c.getDeclaredConstructor(new Class[] { } );
	    	Object o = cc.newInstance(new Object[] {} );
	    	
	    	if(tekenApplet3d!=null)remove(tekenApplet3d);
	    	tekenApplet3d = (TekenApplet3D)o;
		    
	    	tekenApplet3d.setBounds(20,20,600, 450);
			
			add(tekenApplet3d);
			tekenApplet3d.init();
			tekenApplet3d.doLayout();
			tekenApplet3d.setState(state);
			
			//tekenApplet3d.setBounds(0,0,getWidth(), getHeight());
			//resize(getSize().width+1,getSize().height+1);
		}
    	catch(Exception ex)
		{	System.out.println(ex.toString());
			
		}
			
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
