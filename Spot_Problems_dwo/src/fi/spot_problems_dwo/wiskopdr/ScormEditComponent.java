package fi.spot_problems_dwo.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fi.beans.scorm.*;
import fi.beans.base64code.*;
import fi.spot_problems_dwo.wiskopdr.tekstobjects.*;
import fi.spot_problems_dwo.wiskopdr.formuleobjects.*;
import fi.spot_problems_dwo.wiskopdr.opdrnav.*;

public class ScormEditComponent extends Panel implements ScormEditComponentIF, ActionListener
{
	private Hashtable launchData;
	private TekstEditor titelEditor, tekstEditor, randomVarEditor;
	private FormuleEditor antwoordvak;
	private Label titelLabel, tekstLabel, randVarLabel, antwoordLabel;
	private OpdrNavStruct onsEdit;
	
	public ScormEditComponent(Hashtable launchData)
	{	setLayout(null);
		super.setSize(770,520); //voor dwo
		setState(launchData);
		/*this.launchData = launchData;
		
		String aantalActiviteitenString = this.getParameter("aantalActiviteiten");
		int aantalActiviteiten = Integer.parseInt(aantalActiviteitenString);
		int[] aantalOpdrachten = new int[aantalActiviteiten];
		String[] activiteitNamen = new String[aantalActiviteiten];
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	activiteitNamen[i] = this.getParameter("activiteit_"+(i+1));
			String aantalString = this.getParameter("aantalOpdrachten_"+(i+1));
			aantalOpdrachten[i] = Integer.parseInt(aantalString);
		}
		
		onsEdit = new OpdrNavStruct(aantalActiviteiten,aantalOpdrachten,activiteitNamen,0,0,getSize().width, getSize().height, null, true);
		add(onsEdit);
		
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	
			for(int j=0 ; j<aantalOpdrachten[i] ; j++)
			{	String opdracht = 	this.getParameter("opdracht_"+(i+1)+"_"+(j+1));
				MyOpdrEditContainer opdrEditContainer = new MyOpdrEditContainer();
				onsEdit.zetOpdrContainer(opdrEditContainer,i,j);
			}
		}
		setState(launchData);*/
	}
	
	
	public void setSize(int b, int h)
	{	//tekstEditor.setBounds(30,20,b-60,200);
		//antwoordvak.setBounds(30,240,b-60,100);
		super.setSize(b,h);
	}
	
	public String getParameter(String name)
	{	String value = (String)launchData.get(name);
		return value;
	}
	
	public Hashtable getLaunchData()
    {   Hashtable h = onsEdit.getEditState();
    	h.put("language",getParameter("language"));
    	h.put("bgcolor",getParameter("bgcolor"));
    	return h;
	}
	
	public Component getComponent()
	{   return this;
	} 
	
	public void setState(Hashtable launchData)
	{	//this.launchData = launchData;
		//onsEdit.setEditState(launchData);
		
		this.launchData = launchData;
		
		String aantalActiviteitenString = this.getParameter("aantalActiviteiten");
		int aantalActiviteiten = Integer.parseInt(aantalActiviteitenString);
		int[] aantalOpdrachten = new int[aantalActiviteiten];
		String[] activiteitNamen = new String[aantalActiviteiten];
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	activiteitNamen[i] = this.getParameter("activiteit_"+(i+1));
			String aantalString = this.getParameter("aantalOpdrachten_"+(i+1));
			aantalOpdrachten[i] = Integer.parseInt(aantalString);
		}
		if(onsEdit != null) remove(onsEdit);
		onsEdit = new OpdrNavStruct(aantalActiviteiten,aantalOpdrachten,activiteitNamen,0,0,770, 520, null, true);
		add(onsEdit);
		
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	
			for(int j=0 ; j<aantalOpdrachten[i] ; j++)
			{	String opdracht = 	this.getParameter("opdracht_"+(i+1)+"_"+(j+1));
				MyOpdrEditContainer opdrEditContainer = new MyOpdrEditContainer();
				onsEdit.zetOpdrContainer(opdrEditContainer,i,j);
			}
		}
		onsEdit.setEditState(launchData);
	}
	
	public void end()
    {   if(onsEdit!=null) onsEdit.destroy();
	}
	
    public void reset()
    {   
	}	
	
	public void actionPerformed(ActionEvent e)
	{	
	}
}
