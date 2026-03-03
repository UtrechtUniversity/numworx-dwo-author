package nl.numworx.geogebra4;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import fi.wiskopdr.DialogFacade;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

public class GeogebraParamButton extends JButton implements ActionListener
{
	public static final int GEOGEBRA3 = 0;
	public static final int GEOGEBRA4 = 1;
	
	
	private DialogFacade frame;
	private JPanel paramPanel;
	private JCheckBox[] paramCheckboxes;
	private JTextField[] paramTextFields;
	private JLabel[] defaultParamLabels;
	private static String[] paramNames3 = {
	        "showToolBar",
	        "customToolBar",
	        "showToolBarHelp",
	        "framePossible",
	        "showMenuBar",
	        "allowRescaling",
	        "enableShiftDragZoom",
	        "enableRightClick",
	        "enableLabelDrags",
	        "enableChooserPopups",
	        "errorDialogsActive",
	        "maxIconSize",
	        "showAlgebraInput",
	        //"showAlgebraView"
	        //"showResetIcon"
	    };

	private static String[] paramNames4 = {
	        "showToolBar",
	        "customToolBar",
	        "showToolBarHelp",
	        //"framePossible",
	        "showMenuBar",
	        //"allowRescaling",
	        "enableShiftDragZoom",
	        "enableRightClick",
	        "enableLabelDrags",
	        //"enableChooserPopups",
	        "errorDialogsActive",
	        "maxIconSize",
	        "showAlgebraInput",
	        "showAlgebraView",
	        "showResetIcon",
	        "allowStyleBar",
	    };

	
	private static String[] defaultParamValues3 = {
	        "false",				//showToolBar
	        "",    			//customToolBar
	        "false", 		//showToolBarHelp
	        "true",    	//framePossible
	        "false",      	//showMenuBar
	        "false", 		//allowRescaling
	        "true",	//enableShiftDragZoom
	        "true", 	//enableRightClick
	        "true", 	//enableLabelDrags
	        "true", 	//enableChooserPopups
	        "true", 	//errorDialogsActive
	        "32", 			//maxIconSize
	        "false", 		//showAlgebraInput
	        //"false",		//showAlgebraView
	        //"false"        //showResetIcon
	    };

	private static String[] defaultParamValues4 = {
	        "false",				//showToolBar
	        "",    	//customToolBar
	        "false", 		//showToolBarHelp
	        //"true",    	//framePossible
	        "false",      	//showMenuBar
	        //"false", 		//allowRescaling
	        "true",		//enableShiftDragZoom
	        "true", 	//enableRightClick
	        "true", 	//enableLabelDrags
	        //"true", 	//enableChooserPopups
	        "true", 	//errorDialogsActive
	        "32", 			//maxIconSize
	        "false", 		//showAlgebraInput
	        "false",		//showAlgebraView
	        "false",        //showResetIcon
	        "false",		// allowStyleBar
	    };

	
	private static String[] activeParamNames3 = {
	        "framePossible",
	        "enableShiftDragZoom",
	        "enableRightClick",
	        "enableLabelDrags",
	        "enableChooserPopups",
	};

	private static String[] activeParamNames4 = {
	        "enableShiftDragZoom",
	        "enableRightClick",
	        "enableLabelDrags",
	        //"enableChooserPopups",
	        "allowStyleBar",
	        "showMenuBar"
	    };
	
	private static String[] activeParamValues3 = {
			"false", 	//framePossible 
			"false",	//enableShiftDragZoom",
			"false",	//enableRightClick",
			"false",	//enableLabelDrags",
			"false",	//enableChooserPopups",
	};

	private static String[] activeParamValues4 = { 
			"false",	//enableShiftDragZoom",
			"false",	//enableRightClick",
			"false",	//enableLabelDrags",
			"false",	//enableChooserPopups",
			"false",	//allowStyleBar
			"false",	//showMenuBar
	    };
	
	private Hashtable currentParams;
	private Hashtable defaultParams;
	private JButton okButton; 
	private JButton cancelButton;
	
	private static String[][] activeParamNames = { activeParamNames3, activeParamNames4 };
	private static String[][] paramNames = { paramNames3, paramNames4 };
	private static String[][] activeParamValues = { activeParamValues3, activeParamValues4 };
	private static String[][] defaultParamValues = { defaultParamValues3, defaultParamValues4 };
	
	private int version;
	public GeogebraParamButton(int version)
	{
		super("Parameters");
		this.version = version;
		addActionListener(this);
		
		defaultParams = new Hashtable();
		for(int i=0 ; i<activeParamNames[version].length ; i++)
        {	defaultParams.put(activeParamNames[version][i], activeParamValues[version][i]);
        }
		currentParams = new Hashtable(defaultParams);
	}
	
	public void setParams(Hashtable h)
	{
	    currentParams = h;
	}
	
	public Hashtable getParams()
    {
        return currentParams;
    }
	
	public Hashtable getDefaultParams()
    {
        return defaultParams;
    }
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(this))
		{	
		    JPanel paramPanel = new JPanel();
		    paramPanel = new JPanel();
	        
	        Box boxv = Box.createVerticalBox();
	        
	        Box boxh = Box.createHorizontalBox();
	        boxh.add(Box.createHorizontalStrut(10));
	        
	        JLabel label = new JLabel("NAME");
	        boxh.add(label);
	        
	        boxh.add(Box.createHorizontalStrut(200));
	        
	        label = new JLabel("VALUE");
	        boxh.add(label);
	        
	        boxh.add(Box.createHorizontalStrut(100));
	        
	        label = new JLabel("DEFAULT");
	        boxh.add(label);
	        
	        boxv.add(boxh);
	        boxv.add(Box.createVerticalStrut(10));
	        
	        paramCheckboxes = new JCheckBox[paramNames[version].length];
	        paramTextFields = new JTextField[paramNames[version].length];
	        defaultParamLabels = new JLabel[paramNames[version].length];
	        for(int i=0 ; i<paramNames[version].length ; i++)
	        {
	            boxh = Box.createHorizontalBox();
	            	            
	            paramCheckboxes[i] = new JCheckBox(paramNames[version][i]);
	            paramCheckboxes[i].setPreferredSize(new Dimension(200,20));
	            boxh.add(paramCheckboxes[i]);
	            
	            paramTextFields[i] = new JTextField("");
	            paramTextFields[i].setPreferredSize(new Dimension(200,20));
	            boxh.add(paramTextFields[i]);
	            
	            boxh.add(Box.createHorizontalStrut(20));
	            
	            defaultParamLabels[i] = new JLabel(defaultParamValues[version][i]);
	            defaultParamLabels[i].setPreferredSize(new Dimension(100,20));
	            boxh.add(defaultParamLabels[i]);
	            
	            boxv.add(boxh);
	        }
	        for(int i=0 ; i<paramNames[version].length ; i++)
	        {   boolean b = currentParams.containsKey(paramNames[version][i]);
	            paramCheckboxes[i].setSelected(b);
	            if(b)paramTextFields[i].setText((String)currentParams.get(paramNames[version][i]));
	        }
	        paramPanel.add(boxv);
	        
	        JPanel bottomPanel = new JPanel();
	        okButton = new JButton("Ok");
	        okButton.addActionListener(this);
	        bottomPanel.add(okButton);
	        
	        cancelButton = new JButton("Cancel");
	        cancelButton.addActionListener(this);
	        bottomPanel.add(cancelButton);
	        
	        JScrollPane scrollPane = new JScrollPane(paramPanel);
	        
	        frame = DialogFacade.newInstance(this, "");
	        Dimension preferred = new Dimension(600,400);
	        frame.setPreferredSize(preferred);
	        frame.setSize(preferred);
	        frame.getContentPane().setLayout(new BorderLayout());
	        frame.getContentPane().add(scrollPane);
	        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		    
			frame.pack();
        	frame.setVisible(true);
        }
		else if(e.getSource().equals(okButton))
        {   for(int i=0 ; i<paramNames[version].length ; i++)
            {   boolean b = paramCheckboxes[i].isSelected();
                if(b)currentParams.put(paramNames[version][i],paramTextFields[i].getText());
                else currentParams.remove(paramNames[version][i]);
            }
            frame.setVisible(false);
            frame.dispose();
        }
		else if(e.getSource().equals(cancelButton))
        {   frame.setVisible(false);
            frame.dispose();
        }
	}   
    
}

