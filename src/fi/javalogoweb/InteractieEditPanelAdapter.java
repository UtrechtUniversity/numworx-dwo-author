package fi.javalogoweb;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import fi.beans.scorm.Parameter;
import fi.beans.wiskopdrbeans.*;
import fi.dwo.client.gui.GuiConstants;
import fi.dwo.parameters.domain.ConvertorCreator;
import fi.dwo.parameters.domain.ConvertorIF;
import fi.dwo.parameters.gui.MainParameterComponent;
import fi.dwo.parameters.gui.ParameterComponent;

public class InteractieEditPanelAdapter extends JPanel implements InteractieEditPanel, ActionListener
{
	private InteractiePanelAdapter ipa;
	private Hashtable launchData;
	private MainParameterComponent parameterComponent;
	Parameter[] parameters;
	JPanel parameterPanel;
	JPanel buttonPanel;
	
	private JButton applyButton;
	
	public InteractieEditPanelAdapter(InteractiePanelAdapter ipa)
	{
		setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1200,600));
		this.ipa = ipa;
		
		JPanel basisPanel = new JPanel();
		basisPanel.setLayout(null);
		basisPanel.setOpaque(false);
		add(basisPanel, BorderLayout.CENTER);
		
		parameterPanel = new JPanel();
		parameterPanel.setOpaque(false);
		add(parameterPanel, BorderLayout.EAST);
		
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(null);
		buttonPanel.setPreferredSize(new Dimension(100,60));
		
		ipa.setBounds(0,0,500,450);
		basisPanel.add(ipa);
		ipa.restart();
		
		launchData = ipa.getLaunchData();
		
		applyButton = new JButton("Apply");
		applyButton.setSize(applyButton.getPreferredSize());
		applyButton.setLocation(0,10);
		applyButton.addActionListener(this);
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(null);
		buttonPanel.setPreferredSize(new Dimension(applyButton.getPreferredSize().width,50));
		buttonPanel.add(applyButton);
		
		parameters = ipa.getApplet().getEditableParameters();
        if(parameters == null)parameters = new Parameter[0];
		Hashtable tmp = launchData;
		ConvertorIF convertor = ConvertorCreator.createConverter(ConvertorCreator.CONV_LAUNCHDATA);
        tmp = (Hashtable) convertor.convertHashtable(tmp, parameters);
        parameterComponent = new MainParameterComponent(parameters, tmp);
        parameterComponent.setBounds(0,0,600,600);
        parameterPanel.add(parameterComponent);
        //parameterComponent.doLayout();
        
        parameterComponent.add(buttonPanel);
		//parameterComponent.add(applyButton);
    }
	
	public void setEditState(Hashtable h)
	{
		ipa.setEditState(h);
		launchData = ipa.getLaunchData();
		
		parameterPanel.remove(parameterComponent);
		
		parameters = ipa.getApplet().getEditableParameters();
        if(parameters == null)parameters = new Parameter[0];
		Hashtable tmp = launchData;
		
		ConvertorIF convertor = ConvertorCreator.createConverter(ConvertorCreator.CONV_LAUNCHDATA);
        tmp = (Hashtable) convertor.convertHashtable(tmp, parameters);
        parameterComponent = new MainParameterComponent(parameters, tmp);
        parameterComponent.setBounds(0,0,600,600);
        parameterPanel.add(parameterComponent);
        parameterComponent.add(buttonPanel);
        parameterComponent.doLayout();
        repaint();
        
        
        
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
	{	if(b == ipa.getSize().width) return;
		ipa.setBounds(0,0,b, ipa.getSize().height);
		ipa.setEditState(ipa.getEditState());
		revalidate();
	}
	
	public void zetHoogte(int h)
	{	if(h == ipa.getSize().height) return;
		ipa.setBounds(0,0,ipa.getSize().width, h);
		ipa.setEditState(ipa.getEditState());
		revalidate();
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
		if(e.getSource().equals(applyButton))
		{	ConvertorIF convertor = ConvertorCreator.createConverter(ConvertorCreator.CONV_LAUNCHDATA);
			launchData = (Hashtable) convertor.convertHashtable(launchData, parameters);
	        parameterComponent.addParameters(launchData);
	        launchData = (Hashtable) convertor.createHashtable(launchData, parameters);
	        
	        ipa.setLaunchData(launchData);
	        ipa.setEditState(ipa.getEditState());
	        revalidate();
	    }
	}

}
