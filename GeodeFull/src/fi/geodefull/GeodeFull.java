package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.util.*;

import javax.swing.JComboBox;
import fi.beans.copyright.*;
import fi.beans.mainframe.JApplet;
import fi.beans.scorm.*;
import fi.beans.base64code.*;
import fi.beans.wiskopdrbeans.*;

public class GeodeFull extends JApplet implements ScormAppletIF , ActionListener, WiskOpdrApplet
{
	protected static ResourceBundle rb;
	protected SCORM12APIInterface api;
	private TextField textField;
	TekenApplet3D geode;
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
			"Vlakvullingen5en10",
			"Voetbal",
			"Zeshoek",
			"Zeshoekrooster"
			
			
			
			
			
			
			
			
	};
	
	public GeodeFull()
	{	Locale language = new Locale ("nl", "");
		rb = ResourceBundle.getBundle("fi.geodefull.text.Text",language);
	}
	
	public GeodeFull(Locale language)
	{	rb = ResourceBundle.getBundle("fi.geodefull.text.Text",language);
	}
	
	public static void main(String[] args)    
	{	int width = 800;
        int height = 600;
		ScormMainFrame mf = new ScormMainFrame(new GeodeFull(),width, height);
		mf.setTitle("GeodeFull");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public void init() 
	{	try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		setLayout(null);
		
		//instelling taal
		String langArg = getParameter("language");
		if ( langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.geodefull.text.Text",language);
		
		//instelling achtergrondkleur
		Color bgcolor = new Color(255,255,255);
		String kleurcode = getParameter("bgcolor");
		if(kleurcode!=null)bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		setBackground(bgcolor);
		
		//Fi-logo, copyright
		FIButton fiButton = new FIButton("GeodeFull",new String[]
			{	"versie-info: ...",
				"auteur: ...",
				"programmeur: ...",
				"Freudenthal Instituut",
				"www.fi.uu.nl",
				""
			});
		fiButton.setBounds(0,0,20,30);
		//add(fiButton);
		
		//Test-textfield
		textField = new TextField();
		textField.setBounds(50,100,200,25);
		//add(textField);
		
		choice = new JComboBox();
		choice.setBounds(0,0,100,20);
		for(int i=0 ; i<choiceStrings.length ; i++)
		{
			choice.addItem(choiceStrings[i]);
		}
		choice.addActionListener(this);
		add(choice);
		
		//geode = new GeodeMaakProg();
		//geode.zetRg(true);
		//geode.setBounds(0,20,getWidth(), getHeight()-20);
		//geode.init();
		//add(geode);
		
	}

	public void start()
	{	if(api!=null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if(s!=null && !s.equals(""))setState(s);
		}
	
	}
	
	public void stopSco()
	{	stop();
		api = null;
	}
	
	public void stop()
	{	if(api!=null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}
	
	
	
	public void setState(String s)
	{	//decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		Hashtable h = (Hashtable)o;
		
		//haal de data uit de hashtabel
		String text = (String)h.get("text");
		
	    //herstel de state van het applet
	    textField.setText(text);
	}
	
	public String getState()
	{	String text = null;
	
		//vraag de gegevens op die de state bepalen
	    text = textField.getText();
	    
	    Hashtable h = new Hashtable();
	    
	    //voeg de gegeven toe aan de hashtable
	    h.put("text", text);
	      
	    //codeer de hashtable tot string
	    String s = StringCodeObject.encodeObjectToString(h);
	    return s;
	}
	
	public double getScore()
	{	return 0.5;
	}
	
	public boolean hasEditMode()
	{	return false;
	}

    public ScormEditComponentIF getEditComponent(Hashtable launchdata)
    {	return null;
    }
    
    public Parameter[] getEditableParameters()
	{	return null;
    }

    public Parameter[] getAllParameters()
    {	return null;
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	String name = "fi.geodefull." + (String)choice.getSelectedItem() + "Prog";
    	try
		{	Class c = Class.forName(name);
	    	Constructor cc = c.getDeclaredConstructor(new Class[] { } );
	    	Object o = cc.newInstance(new Object[] {} );
	    	
	    	if(geode!=null)remove(geode);
		    geode = (TekenApplet3D)o;
		    
			geode.setBounds(0,20,getWidth(), getHeight()-20);
			
			add(geode);
			geode.init();
			geode.doLayout();
			
			geode.setBounds(0,0,getWidth(), getHeight());
			resize(getSize().width+1,getSize().height+1);
			
			
			
			
			
		}
		catch(Exception ex)
		{	System.out.println(ex.toString());
			
		}
		geode.doLayout();
    }

	public InteractiePanel getInteractiePanel() {
		// TODO Auto-generated method stub
		return new GeodeFullInteractiePanel();
	}
}