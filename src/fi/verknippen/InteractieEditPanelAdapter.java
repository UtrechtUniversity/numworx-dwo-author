package fi.verknippen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fi.beans.scorm.Parameter;
import fi.beans.wiskopdrbeans.*;
import fi.dwo.client.gui.GuiConstants;
import fi.dwo.parameters.domain.ConvertorCreator;
import fi.dwo.parameters.domain.ConvertorIF;
import fi.dwo.parameters.gui.MainParameterComponent;
import fi.dwo.parameters.gui.ParameterComponent;

public class InteractieEditPanelAdapter extends JPanel implements InteractieEditPanel
{
	private InteractiePanelAdapter ipa;
	private Hashtable launchData;
	private MainParameterComponent parameterComponent;
	Parameter[] parameters;
	
	public InteractieEditPanelAdapter(InteractiePanelAdapter ipa)
	{
		setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1200,600));
		this.ipa = ipa;
		ipa.setBounds(0,0,750,500);
		add(ipa, BorderLayout.WEST);
		ipa.start();
		
		launchData = ipa.getLaunchData();
		
		
		parameters = ipa.getApplet().getEditableParameters();
        if(parameters == null)parameters = new Parameter[0];
		Hashtable tmp = launchData;
		ConvertorIF convertor = ConvertorCreator.createConverter(ConvertorCreator.CONV_LAUNCHDATA);
        tmp = (Hashtable) convertor.convertHashtable(tmp, parameters);
        parameterComponent = new MainParameterComponent(parameters, tmp);
        parameterComponent.setBounds(0,0,600,600);
        add(parameterComponent,BorderLayout.EAST);
    }
	
	public void setEditState(Hashtable h)
	{
		ipa.setEditState(h);
		launchData = ipa.getLaunchData();
		
		remove(parameterComponent);
		
		parameters = ipa.getApplet().getEditableParameters();
        if(parameters == null)parameters = new Parameter[0];
		Hashtable tmp = launchData;
		ConvertorIF convertor = ConvertorCreator.createConverter(ConvertorCreator.CONV_LAUNCHDATA);
        tmp = (Hashtable) convertor.convertHashtable(tmp, parameters);
        parameterComponent = new MainParameterComponent(parameters, tmp);
        parameterComponent.setBounds(0,0,600,600);
        add(parameterComponent,BorderLayout.EAST);
		
        ipa.start();
	}
	
	public Hashtable getEditState()
	{
		ConvertorIF convertor = ConvertorCreator.createConverter(ConvertorCreator.CONV_LAUNCHDATA);
		launchData = (Hashtable) convertor.convertHashtable(launchData, parameters);
        parameterComponent.addParameters(launchData);
        launchData = (Hashtable) convertor.createHashtable(launchData, parameters);
        
        ipa.setLaunchData(launchData);
        
		Hashtable h = ipa.getEditState();
		
		return h;
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
		
	}
	
	public void zetBreedte(int b)
	{
		
	}
	
	public void zetHoogte(int h)
	{
		
	}
	
	public void wis()
	{
		
	}
    
	public void zetMode(int mode)
	{
		
	}
	
    public void stop()
	{
		
	}
    
    public void start()
	{
		
	}
    
    public void addActionListener(ActionListener al)
	{
		
	}
    
	public void actionPerformed(ActionEvent e)
	{
		
	}

}
