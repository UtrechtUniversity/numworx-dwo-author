package fi.kladje;




import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;

public class FormuleInstellingenButton extends JButton implements ActionListener
{
	
	private JDialog frame;
	private Hashtable currentInstellingen = new Hashtable();
	private JButton okButton; 
	private JButton cancelButton;
	
	private JTextField[][] rectangleDataFields;
	private ArrayList<int[]> rectangleData;
	private int rectangleCount = 10;
	
	private boolean calculator = true;
	private JCheckBox calculatorCB;
	
	public FormuleInstellingenButton(String text)
	{
		super(text);
		addActionListener(this);
		
		
		rectangleDataFields = new JTextField[rectangleCount][4];
		rectangleData = new ArrayList<int[]>();
		for(int i=0 ; i<rectangleCount ; i++) {
			for(int j=0 ; j<4 ; j++) {
				rectangleDataFields[i][j] = new JTextField();
			}
        }
		
		calculatorCB = new JCheckBox("calculator");
		calculatorCB.setSelected(calculator);
        
	}
	
	public void setInstellingen(Hashtable h)
	{
		currentInstellingen = h;
		if(h.containsKey("rectangleData"))
			rectangleData = (ArrayList<int[]>)h.get("rectangleData");
		for(int i=0 ; i<rectangleData.size() ; i++) {
			for(int j=0 ; j<4 ; j++) {
				rectangleDataFields[i][j].setText(""+rectangleData.get(i)[j]);
			}
        }
		if(h.containsKey("calculator"))
			calculator = ((Boolean)h.get("calculator")).booleanValue();
		calculatorCB.setSelected(calculator);
	}
	
	public Hashtable getInstellingen()
    {
        return currentInstellingen;
    }
	
	
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(this))
		{	
		    JPanel paramPanel = new JPanel();
		    paramPanel = new JPanel();
	        
	        Box boxv = Box.createVerticalBox();
	        
	        Box boxh = Box.createHorizontalBox();
	        boxh.add(Box.createHorizontalStrut(70));
	        
	        
	        JLabel label = new JLabel("x");
	        boxh.add(label);
	        
	        boxh.add(Box.createHorizontalStrut(40));
	        
	        label = new JLabel("y");
	        boxh.add(label);
	        
	        boxh.add(Box.createHorizontalStrut(40));
	        
	        label = new JLabel("width");
	        boxh.add(label);
	        
	        boxh.add(Box.createHorizontalStrut(40));
	        
	        label = new JLabel("height");
	        boxh.add(label);
	        
	        boxv.add(boxh);
	        boxv.add(Box.createVerticalStrut(10));
	        
	         
	        for(int i=0 ; i<rectangleCount ; i++) {
	        	boxh = Box.createHorizontalBox();
	        	boxh.add(Box.createHorizontalStrut(10));
	        	label = new JLabel("Area "+(i+1));
		        boxh.add(label);
		        boxh.add(Box.createHorizontalStrut(20));
				for(int j=0 ; j<4 ; j++) {
					if(j>0)
						boxh.add(Box.createHorizontalStrut(20));
					boxh.add(rectangleDataFields[i][j]);
				}
				//boxh.add(Box.createHorizontalStrut(10));
				boxv.add(boxh);
	        }
	        
	       
	        
	        paramPanel.add(boxv);
	        
	        paramPanel.add(calculatorCB);
	        
	        
	        JPanel bottomPanel = new JPanel();
	        okButton = new JButton("Ok");
	        okButton.addActionListener(this);
	        bottomPanel.add(okButton);
	        
	        cancelButton = new JButton("Cancel");
	        cancelButton.addActionListener(this);
	        bottomPanel.add(cancelButton);
	        
	        JScrollPane scrollPane = new JScrollPane(paramPanel);
	        
	        frame = new JDialog(JOptionPane.getFrameForComponent(this),true);
	        Dimension preferred = new Dimension(400,400);
	        frame.setPreferredSize(preferred);
	        frame.setSize(preferred);
	        frame.getContentPane().setLayout(new BorderLayout());
	        frame.getContentPane().add(scrollPane);
	        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		    
			frame.pack();
        	frame.setVisible(true);
        }
		else if(e.getSource().equals(okButton))
        {   
			rectangleData = new ArrayList<int[]>();
			for(int i=0 ; i<rectangleCount ; i++) {
				int[] rectAttr = new int[4];
				boolean lastRect = false;
				for(int j=0 ; j<4 ; j++) {
					try {
						rectAttr[j] = Integer.parseInt(rectangleDataFields[i][j].getText());
					}
					catch(Exception ex) {
						rectAttr[j] = 0;
						lastRect=true;
					}
				}
				if(!lastRect)
					rectangleData.add(rectAttr);
				else
					break;
	        }
			currentInstellingen.put("rectangleData", rectangleData);
			currentInstellingen.put("calculator", new Boolean(calculatorCB.isSelected()));
            frame.setVisible(false);
            frame.dispose();
        }
		else if(e.getSource().equals(cancelButton))
        {   frame.setVisible(false);
            frame.dispose();
        }
	}   
    
}


