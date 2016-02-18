package fi.wiskopdr;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

public class ObjectiveChoiceButton extends JButton implements ActionListener
{
	private DialogFacade frame;
	private JCheckBox[][] choicesCBs;
	private JLabel[] categorieLabels;
	private boolean[][] choices;
	
	//private int maxObjectives = 20;
	
	private String[][] objectives;
	private String[] categorieString;
	private JButton okButton; 
	private JButton cancelButton;
	
	private JPanel objectivesPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JScrollPane scrollPane;
	private String labelString;
	
	public ObjectiveChoiceButton(String[][] objectives, String[] categorieString)
	{	
		this(WiskOpdr.rb.getString("OPT_objectives"), objectives, categorieString);
	}
	
	public ObjectiveChoiceButton(String labelString, String[][] objectives, String[] categorieString)
	{	super(labelString);
		this.labelString = labelString;
		this.objectives = objectives;
		this.categorieString = categorieString;
		addActionListener(this);
	}
	
	public void setChoices (boolean[][] choices)
	{
		this.choices = choices;
		
	}
	
	private void makeChoices(){   
    	if(objectives==null) return;
		choices = new boolean[objectives.length][];
		for(int j=0 ; j<objectives.length ; j++)
		{	choices[j] = new boolean[objectives[j].length];
        	for(int i=0; i<objectives[j].length; i++){   
        	choices[j][i] = choicesCBs[j][i].isSelected();
        	}
        }
     }
    
    public boolean[][] getChoices(){   
    	return choices;
    }
    
    public void makeGUI(){
    	objectivesPanel = new JPanel();
		bottomPanel = new JPanel();
        
        Box boxv = Box.createVerticalBox();
        
        Box boxh = Box.createHorizontalBox();
        boxh.add(Box.createHorizontalStrut(10));
        
       
        JLabel label = new JLabel(labelString);
        boxh.add(label);
        
        boxv.add(boxh);
        boxv.add(Box.createVerticalStrut(10));
        
        if(objectives!=null)
        {   categorieLabels = new JLabel[objectives.length];
        	choicesCBs = new JCheckBox[objectives.length][];
        	int maxLength = 0;
    		for (int j = 0; j < objectives.length; j++)
    			if(objectives[j].length > maxLength)
    				maxLength = objectives[j].length;
        	Box boxh2 = Box.createHorizontalBox();
        	for(int j = 0; j < objectives.length; j++)
        	{	Box boxv2 = Box.createVerticalBox();
        		categorieLabels[j] = new JLabel(categorieString[j]);
        		categorieLabels[j].setPreferredSize(new Dimension(160,20));
        		boxv2.add(categorieLabels[j]);
        		choicesCBs[j] = new JCheckBox[objectives[j].length];
        		for(int i=0 ; i<objectives[j].length ; i++){
		            choicesCBs[j][i] = new JCheckBox(objectives[j][i]);
		            if(choices!=null && choices.length>j && choices[j].length>i )choicesCBs[j][i].setSelected(choices[j][i]);
		            choicesCBs[j][i].setPreferredSize(new Dimension(160,20));
		            boxv2.add(choicesCBs[j][i]);
		        }
        		boxv2.add(Box.createHorizontalGlue());
        		boxv2.add(Box.createVerticalStrut(25 * maxLength - 25 * objectives[j].length));
        		boxh2.add(boxv2);
	        }
        	boxv.add(boxh2);
	        objectivesPanel.add(boxv);
        }
       
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
        
		scrollPane = new JScrollPane(objectivesPanel);
    }
    
    public void makeFrame(){
    	frame = DialogFacade.newInstance(this, "", true);
    	//Dimension preferredSize = new Dimension(400,320);
		//frame.setPreferredSize(preferredSize);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //frame.setSize(preferredSize);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		frame.pack();
	    frame.setVisible(true);
	    
    }
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(this) && frame==null){	
			makeGUI();
			makeFrame();
		}
		else if(e.getSource().equals(okButton)) {   
			makeChoices();
        	frame.setVisible(false);
            frame.dispose();
            frame=null;
        }
		else if(e.getSource().equals(cancelButton)) {   
			frame.getContentPane().removeAll();
			frame.setVisible(false);
            frame.dispose();
            frame=null;
        }
	}   
}

