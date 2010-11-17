package fi.tekenveelvlakopdr;

import java.awt.event.*;
import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

import fi.beans.copyright.*;
import fi.beans.scorm.*;
import fi.beans.appletutil.*;
import fi.beans.base64code.*;
import fi.beans.tekstobjects.*;
import fi.beans.wiskopdrbeans.*;
import fi.tekenveelvlakopdr.opdrnav.*;

//import grnuminput.*;


public class TekenVeelvlakOpdr extends Applet implements WiskOpdrApplet, ScormAppletIF, ActionListener
{	
	protected SCORM12APIInterface api;
	private long sessionStartTime;
	private Hashtable defaultParamValues;
	
	protected static ResourceBundle rb;
	
	private TekenVeelvlak tekenVeelvlak;
	private Viewer3d viewer;
	private TekstArea tekstArea;
	private OpdrNavStruct ons;
	private Hashtable[][] veelvlakStates;
	private boolean uitleg;
	//private URLButton[] uitlegKnoppen;
	
	
	public static void main(String[] args)    
	{	int width = 780;
        int height = 480;
		//TekenVeelvlakFrame mf = new TekenVeelvlakFrame(new TekenVeelvlak(),width, height);
		ScormMainFrame mf = new ScormMainFrame(new TekenVeelvlakOpdr(),width, height);
		mf.setTitle("TekenVeelvlak");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public TekenVeelvlakOpdr()
	{	Locale language = new Locale ("nl", "");
		rb = ResourceBundle.getBundle("fi.tekenveelvlakopdr.text.Text",language);
	}
	
	public TekenVeelvlakOpdr(Locale language)
	{	rb = ResourceBundle.getBundle("fi.tekenveelvlakopdr.text.Text",language);
	}
	
	public void init()
	{	String variantString = super.getParameter("variant");
		int variant = 0;
		if(variantString!=null) variant = Integer.parseInt(variantString);
		
		defaultParamValues = makeDefaultParamValues(variant);
		
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		setLayout(null);
		String langArg = getParameter("language");
		if (langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.tekenveelvlakopdr.text.Text",language);

		Color bgcolor = new Color(230,240,255);
		String kleurcode = getParameter("bgcolor");
		if(kleurcode!=null)bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		setBackground(bgcolor);
		
		//achtergrondkleur(153,204,255);
		
		
		FIButton fiButton = new FIButton("TekenVeelvlak",
			new String[]{"","versie-info: 20060912",
							"auteur: Peter Boon",
							"programmeur: Peter Boon",
							"Freudenthal Instituut","www.fi.uu.nl",""});
		fiButton.setBounds(5,5,20,30);
		add(fiButton);
		
		Panel p1 = new Panel();
		p1.setLayout(null);
		p1.setBounds(10,10,180,180);
		add(p1);
		
		viewer = new Viewer3d(0,0,180,180);
		viewer.setBackground(bgcolor);
		p1.add(viewer);
		//viewer.setState("H4sIAAAAAAAAAF1UTUwTQRSe7vaX/1IqLFCqMZjogcTExAOVVYJESDwYb3iwCzS2ZdNiu62YGDSKEQET48ULiQYjkaAXTyroXDx4wZOKxgtGDgaU+AchJoq73W+W2TZ5nfe9782b773Z3YffiCubIYGkkldaclpCbTmmZOOa0qvGAnOVzbu6l5+IROgkJWpa6e9U+rR0pov4tHgmlo2n1f6hQfkwMX7ec179v1Q3p0YqepVsItuT6ItrvYqS0ev7C/VVJXWmpT2dVmNKamFn5tLbyT9rAnH0EFdeUXOxoUGiEbeaSKZiqVyGCKe6jj+L7smvPL4vEKKThDST7Z8Xqw+rU7cS3UTocOlWppsDMS94J3C1bgHwxurH6tZNAC+ghgC+FrwHtYyzSzVSEk/HBgZzKQ26O9qmJ2pm+3qamO4offV6/8LIgRvySsfGyzubu+nn4OaXB1fG5aXIkUly+R6Ly4u3+9/dEqPydzOOfdfn75pNym/8yRNtK/9YnK59mj3542pEXjbiq4SiDsunqCMjPzK9uO/D35+PqFlni8Vl1KHQw+pQU89p89xVB7XntyJ/itr1K8gbtfoy+52wMNOPuHWePT6G+UzJdj1R+cXM0Zlf7V9RZxTxXvZwyB/nCj9rDqZ+wTp3Ya+0PuxJUsyN1Y+w/CI9ln4TX6P2e5yQ3xfiY+DH54cPnn96sfsC7muL2s8dpKb+DWrXH4VOUbbfbys160+wOhF7XwT5I7JZP4X5/GbzsZ4r5FN7vlp8L+h3HDqXrDmwOuiT2u933Jq3Of/nkZsm1ognryoDA/oLcpYMF96hdd0O4V0s161Ct0rgClwjwwSxKuAqcITDfi7fqFeDPSL8Ug6XIhbkcJDjWZxhw3YU4SBiIr4LFUW4isuvBcf0G9+UuiJcxe0vg18H7ALPY4nb7wIncfOQMBMRq8TNpwS+xOH6Il5CTMTq4HgH5sfjesRExIvn30DM7yjDAjdnATzDjcA1XH45d54xGw83Dyf2NpDtb32Iw43Qx3A9cni+huvXCz8E7AaWOBzi8t3gWH4IuBpYglaGq4FZvg8+6yeAHtk8mqA1xGHDD3M4zPUTBu/jsI+rH4bv4bCHbD9/YfgBTk+QO8+Js3hs+MGh/xeIEPRQCAAA");
		
		
		tekstArea = new TekstArea();
		tekstArea.setBounds(10,200,180,300);
		tekstArea.setText("Maak de bovenstaande figuur na. Klik met de muis op de hoekpunten van de draadfiguur om vlakken en eventueel hulplijnen te maken");
		//add(tekstArea);
		tekstArea.resize();
		
		Panel p2 = new Panel();
		p2.setLayout(null);
		p2.setBounds(200,10,getSize().width-200,getSize().height-80);
		add(p2);
		tekenVeelvlak = new TekenVeelvlak();
		tekenVeelvlak.setBounds(0,0,getSize().width-200,getSize().height-80);
		tekenVeelvlak.setBackground(bgcolor);
		tekenVeelvlak.init();
		
		String aantalActiviteitenString = this.getParameter("aantalActiviteiten");
			int aantalActiviteiten = Integer.parseInt(aantalActiviteitenString);
			int[] aantalOpdrachten = new int[aantalActiviteiten];
			String[] activiteitNamen = new String[aantalActiviteiten];
			for(int i=0 ; i<aantalActiviteiten ; i++)
			{	activiteitNamen[i] = this.getParameter("activiteit_"+(i+1));
				String aantalString = this.getParameter("aantalOpdrachten_"+(i+1));
				aantalOpdrachten[i] = Integer.parseInt(aantalString);
			}
			
			ons = new OpdrNavStruct(aantalActiviteiten,aantalOpdrachten,activiteitNamen,0,0,getSize().width, getSize().height, api, false);
			ons.setBackground(bgcolor);
			ons.addActionListener(this);
			add(ons);
			for(int i=0 ; i<aantalActiviteiten ; i++)
			{	
				for(int j=0 ; j<aantalOpdrachten[i] ; j++)
				{	String opdracht = 	this.getParameter("opdracht_"+(i+1)+"_"+(j+1));
					MyOpdrContainer opdrContainer = new MyOpdrContainer(0,0,getSize().width, getSize().height, tekenVeelvlak, viewer);
					opdrContainer.setBackground(bgcolor);
					opdrContainer.zetOpdracht(opdracht);
					ons.zetOpdrContainer(opdrContainer,i,j);
				}
			}
		
		
		p2.add(tekenVeelvlak,0);
		
		/*String optieUitlegString = getParameter("optieuitleg");
		if ( optieUitlegString.equals("true")) uitleg = true;
		
		if(uitleg)
		{	uitlegKnoppen = new URLButton[aantalActiviteiten];
			for(int i=0 ; i<aantalActiviteiten ; i++)
			{	String uitlegURL = getParameter("uitlegURL");
				if(!uitlegURL.equals("about:blank")) uitlegURL = uitlegURL + "help" + (i+1) + ".html";
				
				uitlegKnoppen[i] = new URLButton(this,uitlegURL,"Uitleg");// "+(i+1));
				uitlegKnoppen[i].setBounds(630,getSize().height-60,120,20);
				uitlegKnoppen[i].setPopUpSize(440,450);
				uitlegKnoppen[i].setVisible(false);
				add(uitlegKnoppen[i],0);
			}
			uitlegKnoppen[0].setVisible(true);
		}*/
		
		AppletUtil au = new AppletUtil(this);
		Image uitleg = null;
		//if(langArg.equals("nl"))uitleg = au.getImage("resources/help.gif");
		//else uitleg = au.getImage("resources/help_en.gif");
		uitleg = au.getImage("resources/help.gif");
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(uitleg,0);
		try{tr.waitForAll();} 
		catch(Exception e) {}
		UitlegButton uitlegButton = new UitlegButton("Uitleg",uitleg);
		uitlegButton.setBounds(660,getSize().height-60,90,20);
		uitlegButton.setFrameBackground(getBackground());
		add(uitlegButton,0);
		
		
	}
	
	public InteractiePanel getInteractiePanel()
	{
	    return new TekenVeelvlakInteractiePanel();
		
	    /*TekenVeelvlak tekenVeelvlak = new TekenVeelvlak();
		tekenVeelvlak.setSize(500,400);
		tekenVeelvlak.init();
		return tekenVeelvlak;*/
	}
	
	public Hashtable getDefaultParamValues(int variant)
	{	return makeDefaultParamValues(variant);
	}
	
	public String getParameter(String name)
	{	String value = super.getParameter(name);
		if(value==null)value = (String)defaultParamValues.get(name);
		return value;
	}
	
	private Hashtable makeDefaultParamValues(int variant)
	{	Hashtable h = new Hashtable();
		h.put("language","nl");
		h.put("bgcolor","#DDEEFF");
		//h.put("optieuitleg","true");
		//h.put("uitlegURL","assets/uitleg_");
							

		if(variant==0)
		{						
			Hashtable defaultEditModeLaunchData = new Hashtable();
			/*defaultEditModeLaunchData.put("voorbeeldState",(Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAF1UTUwTQRSe7vaX/1IqLFCqMZjogcTExAOVVYJESDwYb3iwCzS2ZdNiu62YGDSKEQET48ULiQYjkaAXTyroXDx4wZOKxgtGDgaU+AchJoq73W+W2TZ5nfe9782b773Z3YffiCubIYGkkldaclpCbTmmZOOa0qvGAnOVzbu6l5+IROgkJWpa6e9U+rR0pov4tHgmlo2n1f6hQfkwMX7ec179v1Q3p0YqepVsItuT6ItrvYqS0ev7C/VVJXWmpT2dVmNKamFn5tLbyT9rAnH0EFdeUXOxoUGiEbeaSKZiqVyGCKe6jj+L7smvPL4vEKKThDST7Z8Xqw+rU7cS3UTocOlWppsDMS94J3C1bgHwxurH6tZNAC+ghgC+FrwHtYyzSzVSEk/HBgZzKQ26O9qmJ2pm+3qamO4offV6/8LIgRvySsfGyzubu+nn4OaXB1fG5aXIkUly+R6Ly4u3+9/dEqPydzOOfdfn75pNym/8yRNtK/9YnK59mj3542pEXjbiq4SiDsunqCMjPzK9uO/D35+PqFlni8Vl1KHQw+pQU89p89xVB7XntyJ/itr1K8gbtfoy+52wMNOPuHWePT6G+UzJdj1R+cXM0Zlf7V9RZxTxXvZwyB/nCj9rDqZ+wTp3Ya+0PuxJUsyN1Y+w/CI9ln4TX6P2e5yQ3xfiY+DH54cPnn96sfsC7muL2s8dpKb+DWrXH4VOUbbfbys160+wOhF7XwT5I7JZP4X5/GbzsZ4r5FN7vlp8L+h3HDqXrDmwOuiT2u933Jq3Of/nkZsm1ognryoDA/oLcpYMF96hdd0O4V0s161Ct0rgClwjwwSxKuAqcITDfi7fqFeDPSL8Ug6XIhbkcJDjWZxhw3YU4SBiIr4LFUW4isuvBcf0G9+UuiJcxe0vg18H7ALPY4nb7wIncfOQMBMRq8TNpwS+xOH6Il5CTMTq4HgH5sfjesRExIvn30DM7yjDAjdnATzDjcA1XH45d54xGw83Dyf2NpDtb32Iw43Qx3A9cni+huvXCz8E7AaWOBzi8t3gWH4IuBpYglaGq4FZvg8+6yeAHtk8mqA1xGHDD3M4zPUTBu/jsI+rH4bv4bCHbD9/YfgBTk+QO8+Js3hs+MGh/xeIEPRQCAAA"));
			defaultEditModeLaunchData.put("tekst","Maak de bovenstaande figuur na. Klik met de muis op de hoekpunten van de draadfiguur om vlakken en eventueel hulplijnen te maken.");
			defaultEditModeLaunchData.put("basisFiguurState",(Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLNZGRCAEYiZIfZxZeSnZheU5pVA9bnYLesRWZ0cJQfTx7//xDnDsy0mvfavXL4dmf9Def9j0R8vVjR32z+wcZzD0LQYJm5/fXrKtUnMCfYfIOJQfZ17FkAstb8imBVo9+ofTHz/u4ergz+22uyHmrMfZj7EnPgSBvaynMTsbKDDChnqGNiARrBB3c+C5A8mkF8qAIwnICmMAQAA"));
			defaultEditModeLaunchData.put("aantalHulpPunten",new Integer(3));
			defaultEditModeLaunchData.put("basisFiguur",new Integer(0));*/
			
			String[][] defaultEditModeState = new String[1][10];
			/**/
			Hashtable[][] voorbeeldStates = new Hashtable[1][10];
			String[][] teksten = new String[1][10];
			Hashtable[][] basisFiguurStates = new Hashtable[1][10];
			int[][] aantalHulpPuntenRij = new int[1][10];
			int[][] basisFiguren = new int[1][10];
			
			voorbeeldStates[0][0] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLMZQVaVMHBl5KdmF5TmlUDVudgt6xFZnRwlB1Mnsf9R5XLXCcYy9lAanY9LfD+JNLp5hGhC9sHESxjYy3ISs7OBHixkqGNgA/pJHohBNAsDBIDCAuhfBmYkMRDNCpVjgdKsUH1MSPIgmh3JPJh+Zqg4C5TNBFXDXgEA89KkEigCAAA=");
			teksten[0][0] = "Maak de kubus hierboven door in de draadfiguur alle zijvlakken te tekenen. Lees in de uitleg hoe je vlakken kunt tekenen."; 
			basisFiguurStates[0][0] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLMZQVaVMHBl5KdmF5TmlUDVudgt6xFZnRwlh6IOopa9LCcxOxuosJChjoENJlcBAAIpwdX4AAAA");
			aantalHulpPuntenRij[0][0] = 0;
			basisFiguren[0][0] = 0;
			
			voorbeeldStates[0][1] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFDCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDEBJBgZWBgRgBGKgOANzCQNXRn5qdkFpXglUn4vdsh6R1clRcjB90vsfVS53nWAsYw+l0fnoNCF5dHUwR9nj4O8nksZlPy51JQzsZTmJ2dlAjxcy1DGwAa0Ug4YTMyguoGw2JD47mjwHVAzGZ4OKweTYoOrZKwDVt2uHLAIAAA==");
			teksten[0][1] = "Maak deze piramide in de draadfiguur van de kubus rechts."; 
			basisFiguurStates[0][1] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLNZgZiJAQIYoWzmEgaujPzU7ILSvBKoPhe7ZT0iq5Oj5GD6+Pc/qlzuOsFYxh5Ko/PRaULy6OqgbmKwR+OXMLCX5SRmZwMdVshQx8AGdTdDBQA44s2qeAEAAA==");		
			aantalHulpPuntenRij[0][1] = 0;
			basisFiguren[0][1] = 0;
			
			voorbeeldStates[0][2] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFDCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDEBJBgZJBgRgBGKgOAMzFLNAxVmhmBEqzgaVA9HsUDkWqF4GqBgTVB6kh72EgSsjPzW7oDSvBOoOF7tlPSKrk6PkYO7Q2v+ocrnrBGMZeyiNzkenCcmjq8NF41KHyx5c6mCBiM7fj8bHBdDdjUsdIfNg8iUM7GU5idnZwAAvZKgDx4UiEHNA4xBEcwIxFxIfxOaG8kFsHjQ+J1QMpp4XKgbjc0PFmKE0D5I8L1SepwIAyfHjMSADAAA=");		
			teksten[0][2] = "Hierboven zie je een octaeder, ofwel een regelmatig achtvlak. Maak deze figuur in de kubus hiernaast,\n\nTip: Teken eerst de diagonalen van de zijvlakken."; 
			basisFiguurStates[0][2] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLMZQVaVMHBl5KdmF5TmlUDVudgt6xFZnRwlh6IOopa9LCcxOxuosJChjoENJlcBAAIpwdX4AAAA");
			aantalHulpPuntenRij[0][2] = 0;
			basisFiguren[0][2] = 0;
			
			voorbeeldStates[0][3] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFDCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDEBJBgZGkFUlDFwZ+anZBaV5JVB1LnbLekRWJ0fJwdSpMECB/aPK5a4TjGX2Q2kYHya/HwcfnUaXt8dhHrp96OLEugtdHbHmopuHyx8lDOxlOYnZ2cAALGSoY2ADGQXEfKD4gyoBhTUwPBmYoZgBKscI5YNoVqgamDiIZoOKw8xhB6UOKB9mDjNUnBkqx4YkzwkVY4eKgfhcUDUcSGIw/dxI+rihapHFYe7jhrJZkdSwQs3lqgAAZAUScggDAAA=");		
			teksten[0][3] = "Maak deze figuur na."; 
			basisFiguurStates[0][3] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLMZQVaVMHBl5KdmF5TmlUDVudgt6xFZnRwlh6IOopa9LCcxOxuosJChjoENJlcBAAIpwdX4AAAA");		
			aantalHulpPuntenRij[0][3] = 1;
			basisFiguren[0][3] = 0;
			
			voorbeeldStates[0][4] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFDCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDEBJBgZmBgRgLGHgyshPzS4ozSuBqnexW9Yjsjo5Sg6mnmf/o8rlrhOMZeyhNDofF72fAA1TV8LAXpaTmJ0NdEAhQx0DG9BOQVAYILmVEYiZoHxGqBgzkjwTEp8RSjNVAACubb4mmAEAAA==");
			teksten[0][4] = "Hierboven zie je een tetraeder, ofwel een regelmatig viervlak. Maak deze figuur in de kubus hiernaast,"; 
			basisFiguurStates[0][4] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLMZQVaVMHBl5KdmF5TmlUDVudgt6xFZnRwlh6IOopa9LCcxOxuosJChjoENJlcBAAIpwdX4AAAA");
			aantalHulpPuntenRij[0][4] = 0;
			basisFiguren[0][4] = 0;
			
			voorbeeldStates[0][5] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAI2Uu04bQRSGjzcLthevjeMbWAhIkZRu6GMUJQgiUdCaJmOzyhqPdp3dsTENygOkyAPQJimSKlUEBU+QMi8QpYpEHiAVM/gfaTXSyKz0e2a+c5lzZnf87ZaW0oRqp2zKOhMx5J19loaC9XlQu648ffL6z49H5OyRx2N2sscGIk4OqCjCJEjDmJ/Mxt1dUk/hrCB/V6RcQeU+S4dpbzgIRZ+xROav3ufnLHrbeRHHPGDRz+3k/a/L//8cyvVoacr4JJiNSdAyH55GQTRJyDk+OLx682z69/snh0gaibZo/ngYi9jTw5ywrkitQjWoDilbU+oxVIUakLKVpMpSa1JtqZbUuiAvjIPReBIJ1Pfy+ecP9a+D3qau76j7+/zLq487G6iFbrC2jd0Fa5Pb7Ivy2eJtdts+ui/TbvZrnoPtXOiBjxlvclt+Wz+L4m3vz9a/zc92HrY+Hnoui/IKyk85G43kh/qOLmhZonOaf8+u1DrN70IB7i7m6j60Dab8yrDpWB+snmEe8vsZRsinmQeu7mMuw9S8YdSnYkrwdeFTA2uA+YhpgGvWRr1lMDWqe9/K1KKYuut52FzYK2DFDCsava2B5ZFDsybyaj8f8SuoSedT/0sOuItRrasZvyp68jOsBO4gh96jhHVrdgeGQIIV0AUAAA==");
			teksten[0][5] = "Maak deze vier blokken."; 
			basisFiguurStates[0][5] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLMZQVaVMHBl5KdmF5TmlUDVudgt6xFZnRwlh6IOopa9LCcxOxuosJChjoENJlcBAAIpwdX4AAAA");		
			aantalHulpPuntenRij[0][5] = 1;
			basisFiguren[0][5] = 0;
			
			voorbeeldStates[0][6] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAJ1WsW4TQRCdXM4hBCkQ7AIDQqQApXJDz0WRiQgxApNAYtOwdk6x45PPnM/GoUB8AAUfQAsIQUWBEBSW6GlOoqJDFAgJPoCK3Xg2PmYZbcDS8+2+nZ19Ozt7N69+QKYbQXZH9EWhFzeDwmXRbcSiFvjZ90fPzV/5+nYSnGWYCUKxtSzqcRitwOG4EfndRhhsDTreIqjf9L1p+X9Ewo1htia6zW61WW/ENSEi6X9uz38g2tuFpTAMfNH+eDZ6+OnJr58OTFQh0xdBzx90IIapoLnT9tu9CJzbK1ff3Tnf//76qQMgBwHmYfRz1DoSGYlj2J9EZBEujh1HTCHUHKXZQxQRSwg1lpc4iTiFOIHIxzDTCP1Wp9eOUWfx4rNHuZf16hnUOTE7/LL7/NLjC6c9fA4/4JPwuB8w7JM/+5TX9kPKk3l03PD3+c39b5ulFzaerkftDD2E9xg7j4kL5ak91Ut5W5wMu4TRYYkzG3+qP2F00v0yuj1OH3OulKd+qL3OQ2pPeXqOlDfWs+SRkf9oz61r8DRuNP6WvDfynMtnLt+o/388J+7+H/Q8uDhy523Lp30/zL7o+vt9En9DDzPfsE+YfVG9zDqGHst7wsgj5n5S/n/vH/0OcOfB2dvyzPa+NHitk34HCG/EwcKzcUiY/SZMfAjPvd+N/Vl49l4fcL4R/+Tv+2Xz0KKDu3/cPYjhUD8QrZYsSO7Cg706ZxfrFVUHXYNxrbSQ4nTbSXGqPYf2usZS/RzOV9yixCqM6q3rKU61yzCqozSn2iW0d9FG12ll5FSddUNiDUa1luZUe13iZopTbQ/tXdSm9efwuYprZnEtF+cpX0Wc78K4/ltL+dO14i2JDeRUO4/xTHPKbhPHXHyqfiUV+wpyU+hbr7GBNhWifSEV5xLGrwzjmnYd46I0Fwe/AcjeW5XACwAA");		
			teksten[0][6] = "Maak deze vier torens"; 
			basisFiguurStates[0][6] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLMZQVaVMHBl5KdmF5TmlUDVudgt6xFZnRwlh6IOopa9LCcxOxuosJChjoENJlcBAAIpwdX4AAAA");		
			aantalHulpPuntenRij[0][6] = 7;
			basisFiguren[0][6] = 0;
			
			voorbeeldStates[0][7] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAJWVO28TQRDHJ/Y5DwN54EAChJAUjiMI5hEDJhIcQiRKEPSRaVg7J87x6c7crY2hQHwACj4ALSAEDRQIQWGJnsYSFR2iQEjwAaiYjcdRmNXqgqWf9/a/szNzs2vP61+QikLIbIqmyDdk1cuvisiVouw5mY8j2dnr398nIbECaS8QGyuiIoNwDYakGzqRG3gbrbp9BdRn8N4gfu9BLAnDZRFVo1K14sqyECH6H9vy7wn/Tv5qEHiO8D/PhI++PP3zOwF9JUg1hddwWnWQ0O9VN33Hb4SQuLV288PtuebPt88SALgIcAm6H5xDkkYLSSH9yIDKBBlC0sheZB+Nw8QIofRR2qvG/eQrg4wh4+RT6QeQgxRTrU8ih5DDyBFkCjmKTCPHqA4zSB8yS/nMIceRE8gCchLJI6eQ08gZel4gXa2fQ85LSLuBU6s3fEl1uXb5+ePxV5XSdK8ub9rf7r9YfrI4ZdPY/kQj06l+oNl3/p1zvWff5jrbx9c1f1/fPfixfuNlnM7jcTstH6bbBjvbUBeuc3ueL9fj6qTZdQx5xNTZWH+ef8eQJ39fQ962KT/DuXKd++H2vXvI7bnOz5HrWryYe6Tdf7I3xdV0Xjde/5h7r91z03023Tfu/z/PyfT73+15mOpoOu+4+7Ttx/BePP72nNVfy8ewX7PvGN6L52uIw/ORMND0RK2Gf9R34eFW/1iGbt9RfUb1jrPIIlKAbu/o9RzVby7QutJUz8mSPrpDU8/z0O1pSdLUc27HPEd7LfJXRC5SXKVN0J55midJy5FuUS5Z0otkkyWbCXqfItmqfrhEMSx6rwJpk6QVKL6yWWr9BQctb8R4CAAA");		
			teksten[0][7] = "Gebruik de draadfiguur om een gebouw te ontwerpen, bijvoorbeeld een kerk als hierboven."; 
			basisFiguurStates[0][7] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLMZQVaVMHBl5KdmF5TmlUDVudgt6xFZnRwlh6IOopa9LCcxOxuosJChjoENJlcBAAIpwdX4AAAA");		
			aantalHulpPuntenRij[0][7] = 7;
			basisFiguren[0][7] = 0;
			
			voorbeeldStates[0][8] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAG1UTWgTQRid/LVJmt8maZr010PVU+nBQw/BKSLFCiLiReql2zaYtGvSJptYEMRjK/WgFy9C9aAXLQTxIFaYUw9VPLRYWkuFCi2CWlGPFsHd7Jvk2+DC5Pvem2/e9zKzO88PmatYYJEppaz0l7Ss2n9OKWY0ZVxNR1aCfcfO779yMPsw86p5ZXJYmdDyhRHm0TKFdDGTVyfnZvgQMx73Dbf+26IPp8YC40oxWxzNTmS0cUUp6Prhqr6q5K71n8nn1bSSe99buL358M8PO7ONMldZUUvpuRmmsSY1O5VL50oFZr86cuH12PHy1xdP7Izpk4x1MfNp0ocd0YHoNVzow4YosfG4DF+IDkQv5jyIIcyFwHkR9f/jzeTT0zOlnAZfZ08/XYw+mxjtlr7GxJcTa7P5ozLfXepbFYc7Ym/jrf/xZR9fr1zv6vVEJc+/Dc63DDv2wYdq9SZ/AJ1Srd7UCfDvVfxR8vDMhKkTE1adfQ4/wqrjF9CRfGrp/sWlK38foL4krP6D0G8VVv8Hsj//ySyPWGla32D3Bmr/68O7m0cjyrLY/DR7ia8tSj5VwXro1+rXXg7w3/ObjTw3+R3oD9Z4U79S69/gh5v1J4XVT4WbfhbE//czKqx+dsFHhNXPHvQHRIMfvlrVvyP9Yj/98L3FJS/rP0Mf/JtH8G/Wb8t6qS9M/bvcqu+Dj21h1V8W0Ef9qZTUt/pMyfcN+guoH+JW/S3R4EdjzWVVmZ7WP5BZdqv6Pf7SRwrflBd3g49gPziJA+AkDoKT2AfOAd5J6gOYswEHSS8H+jDCtWCtHTiCOyFMcFQfzQSHwEkcZvU7w8iNu6aV1e+QcAOOkfUhzMWAjegh8zHMy/vNDb020s9FsBt5nGAjbye4nfhphzbFCdLPCS5OcILoObHnCYLj5DzsyOMEx4lfO/IkwUlWfx+SOC+K21j9/raht3w/GGo6CDbyToKNXJ6/5Ck26uV5J9FL6nUARwiOkPou+LERbOTdxG+QYCN6UCfrY2T/u5AnCPaw+vtirO8BJ7H8NhykTw/hOgnuhPceUmfgKMFG7pn7B9XQvw4gCAAA");		
			teksten[0][8] = "Bonus-opdracht:\n\nHierboven zie je de kleine keplerster. Maak die na in het draadmodel hiernaast. Het is nodig om eerst hulplijnen te tekenen. Een aantal daarvan is al getekend."; 
			basisFiguurStates[0][8] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLNFgZiLAQIYoZgJipmhmAWKQYAViNmgNDuU5oDSnFCaq4SBKyM/NbugNK8Eaq+L3bIekdXJUXIwexX3P1c/WZj/u8z+znzVo/vf3tr/4NIp3oXBPPYXN+TKKnCKwMTtX1t0cLsxP4GKC8DVQ8SfQs0phauHmMNn/wbMvwkTZ0AD9h9Q+fuv3i0MtD/ZA1NvswEqvpvt4iWGiRZw86+crv7tmbgB6h4huPjJLQb2nzpuoYvvh4hfhZpjABeHmLO2hIG9LCcxOxsYUIUMdfCwZYSGPRcsbCsALYhq1lgCAAA=");		
			aantalHulpPuntenRij[0][8] = 0;
			basisFiguren[0][8] = 2;
			
			voorbeeldStates[0][9] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAF1UTUwTQRSe7vaX/1IqLFCqMZjogcTExAOVVYJESDwYb3iwCzS2ZdNiu62YGDSKEQET48ULiQYjkaAXTyroXDx4wZOKxgtGDgaU+AchJoq73W+W2TZ5nfe9782b773Z3YffiCubIYGkkldaclpCbTmmZOOa0qvGAnOVzbu6l5+IROgkJWpa6e9U+rR0pov4tHgmlo2n1f6hQfkwMX7ec179v1Q3p0YqepVsItuT6ItrvYqS0ev7C/VVJXWmpT2dVmNKamFn5tLbyT9rAnH0EFdeUXOxoUGiEbeaSKZiqVyGCKe6jj+L7smvPL4vEKKThDST7Z8Xqw+rU7cS3UTocOlWppsDMS94J3C1bgHwxurH6tZNAC+ghgC+FrwHtYyzSzVSEk/HBgZzKQ26O9qmJ2pm+3qamO4offV6/8LIgRvySsfGyzubu+nn4OaXB1fG5aXIkUly+R6Ly4u3+9/dEqPydzOOfdfn75pNym/8yRNtK/9YnK59mj3542pEXjbiq4SiDsunqCMjPzK9uO/D35+PqFlni8Vl1KHQw+pQU89p89xVB7XntyJ/itr1K8gbtfoy+52wMNOPuHWePT6G+UzJdj1R+cXM0Zlf7V9RZxTxXvZwyB/nCj9rDqZ+wTp3Ya+0PuxJUsyN1Y+w/CI9ln4TX6P2e5yQ3xfiY+DH54cPnn96sfsC7muL2s8dpKb+DWrXH4VOUbbfbys160+wOhF7XwT5I7JZP4X5/GbzsZ4r5FN7vlp8L+h3HDqXrDmwOuiT2u933Jq3Of/nkZsm1ognryoDA/oLcpYMF96hdd0O4V0s161Ct0rgClwjwwSxKuAqcITDfi7fqFeDPSL8Ug6XIhbkcJDjWZxhw3YU4SBiIr4LFUW4isuvBcf0G9+UuiJcxe0vg18H7ALPY4nb7wIncfOQMBMRq8TNpwS+xOH6Il5CTMTq4HgH5sfjesRExIvn30DM7yjDAjdnATzDjcA1XH45d54xGw83Dyf2NpDtb32Iw43Qx3A9cni+huvXCz8E7AaWOBzi8t3gWH4IuBpYglaGq4FZvg8+6yeAHtk8mqA1xGHDD3M4zPUTBu/jsI+rH4bv4bCHbD9/YfgBTk+QO8+Js3hs+MGh/xeIEPRQCAAA");
			teksten[0][9] = "Bonus-opdracht:\n\nHierboven zie je de grote keplerster. Maak die na in het draadmodel hiernaast."; 
			basisFiguurStates[0][9] = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLMZQVaVMHBl5KdmF5TmlUDVudgt6xFZnRwlh6IOopa9LCcxOxuosJChjoENJlcBAAIpwdX4AAAA");		
			aantalHulpPuntenRij[0][9] = 0;
			basisFiguren[0][9] = 3;
			
			for (int i = 0; i<1; i++) 
			{	for (int j = 0; j<10; j++) 
				{	/**/
					defaultEditModeLaunchData.put("voorbeeldState",voorbeeldStates[i][j]);
					defaultEditModeLaunchData.put("tekst",teksten[i][j]);
					defaultEditModeLaunchData.put("basisFiguurState",basisFiguurStates[i][j]);
					defaultEditModeLaunchData.put("aantalHulpPunten",new Integer(aantalHulpPuntenRij[i][j]));
					defaultEditModeLaunchData.put("basisFiguur",new Integer(basisFiguren[i][j]));
					
					defaultEditModeState[i][j] = StringCodeObject.encodeObjectToString(defaultEditModeLaunchData);
				}
		    }
			
			
			h.put("aantalActiviteiten","1");
	 		h.put("activiteit_1","Niveau 1");
			h.put("aantalOpdrachten_1","10");
			for (int i = 0; i<10; i++) 
			{	h.put("opdracht_1_"+(i+1),defaultEditModeState[0][i]);
			}
			
		}
		
		if(variant==1)
		{	Hashtable defaultEditModeLaunchData = new Hashtable();
			String[] defaultEditModeState = new String[10];
			
			Hashtable voorbeeldState = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFDCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDEBJBgZGkFUlDFwZ+anZBaV5JVB1LnbLekRWJ0fJoaiDqGUvy0nMzgYqLGSoY2CDyVUAAK1qdaX4AAAA");			
			String tekst = "Gebruik de draadfiguur om een eigen figuur te ontwerpen."; 
			Hashtable basisFiguurState = (Hashtable)StringCodeObject.decodeStringToObject("H4sIAAAAAAAAAFvzloG1uIhBOCuxLFGvtCQzR88jsTijJDEpJ1V4N7+qoteTHcwMTG4MXDn5iSluickl+UWeDJwlGUWpxRn5OSkVBfYODCDAUc4BJLmBmKWEgS8psTizOCozOaMkKTGxCGi+INj8nMS8dD2n/Pyc1MS8swpFDVfn/HrHxMAYxcBalphTmlpRwFjCwJaTmZWXmldaxMAU7em7K0Gt7NWmpUwMDBUFQLMZQVaVMHBl5KdmF5TmlUDVudgt6xFZnRwlh6IOopa9LCcxOxuosJChjoENJlcBAAIpwdX4AAAA");		
			int aantalHulpPunten = 7;
			int basisFiguur = 0;
			
			for (int i = 0; i<10; i++) 
			{	defaultEditModeLaunchData.put("voorbeeldState",voorbeeldState);
				defaultEditModeLaunchData.put("tekst","Ontwerp "+(i+1)+"\n\n"+tekst);
				defaultEditModeLaunchData.put("basisFiguurState",basisFiguurState);
				defaultEditModeLaunchData.put("aantalHulpPunten",new Integer(aantalHulpPunten));
				defaultEditModeLaunchData.put("basisFiguur",new Integer(basisFiguur));
				
				defaultEditModeState[i] = StringCodeObject.encodeObjectToString(defaultEditModeLaunchData);
			}
			
			h.put("aantalActiviteiten","1");
 			h.put("activiteit_1","Niveau 1");
			h.put("aantalOpdrachten_1","10");
			for (int i = 0; i<10; i++) 
			{	h.put("opdracht_1_"+(i+1),defaultEditModeState[i]);
		    }
			
		}
		
		return h;
	}
	
	public void start()
	{	sessionStartTime = System.currentTimeMillis();
		if(api!=null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if(s!=null && !s.equals(""))setState(s);
			//
			api.LMSSetValue("cmi.launch_data",StringCodeObject.encodeObjectToString(defaultParamValues));
			//
		}
		ons.start();
	}
	
	public void stopSco()
	{	if(api!=null)
		{	stop();
			api = null;
		}
	}
	
	public void stop()
	{	ons.stop();
		if(api!=null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			String t = getSessionTime();
			api.LMSSetValue("cmi.core.session_time",t);
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}
	
	public void setState(String s)
	{	Object o = StringCodeObject.decodeStringToObject(s);
		if(o==null)return;
		Hashtable h = (Hashtable)o;
		{	Hashtable onsState = (Hashtable)h.get("onsState");
			ons.setState(onsState);
		}
	}
	
	public String getState()
	{	Hashtable onsState = null;
	
	    onsState = ons.getState();
	    
	    Hashtable h = new Hashtable();
	    h.put("onsState", onsState);

	    String s = StringCodeObject.encodeObjectToString(h);
	    return s;
	}
	
		
	public double getScore()
	{	return ons.getScore();
	}
	
	public String getSessionTime()
	{	long sessionTime = System.currentTimeMillis() - sessionStartTime;
		String s = "";
		int hours = (int)sessionTime/3600000;
		int minutes = (int)sessionTime/60000 - hours*60;
		int seconds = (int)sessionTime/1000 - hours*3600 - minutes*60;
		if(hours<10)s += "0";
		s += hours;
		s += ":";
		if(minutes<10)s += "0";
		s += minutes;
		s += ":";
		if(seconds<10)s += "0";
		s += seconds;
		return s;
	}
	
	public boolean hasEditMode()
	{	return false;
	}
	
	public ScormEditComponentIF getEditComponent(Hashtable launchData)
	{	return null;
	}
	
	public Parameter[] getEditableParameters()
	{	return null;
	}
	
	public Parameter[] getAllParameters()
	{	return null;

	}
		
	
	public void actionPerformed(ActionEvent e)
	{		boolean animatieWasAan=false;
		
			if(e.getSource()==ons && e.getActionCommand().equals("select"))
			{	int actNr = ons.geefActiviteitNr();
				int opdrNr = ons.geefOpdrachtNr();
				
			}

			
	}

	
	
}