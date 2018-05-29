package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fi.wiskopdr.opdrnav.PlusMinKnop;

public class LayersButton extends JButton implements ActionListener, FocusListener
{
	private DialogFacade frame;
	JPanel layersPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JScrollPane scrollPane;
	Box[] boxKolom;
	
	private String[] layerNames;
	private boolean[] layerVisible;
	
	private int maxLayers = 10; 
	
	private JTextField[] layerNameTextFields;
	private JCheckBox[] layerVisibleCB;
	
	JButton okButton, cancelButton;
	PlusMinKnop aantalRijenKnop;
	int aantalRijen = 1;
	
	public LayersButton()
	{	super(WiskOpdr.rb.getString("L_layers"));
		addActionListener(this);
		
		layerNames = new String[maxLayers];
		layerVisible = new boolean[maxLayers];
	}
	
	public void zetLayerInfo(String[] layerNames, boolean[] layerVisible)
	{	
		for(int i = 0; layerNames!=null && layerVisible!=null && i < layerNames.length; i++)
        {	this.layerNames[i] = layerNames[i];
			this.layerVisible[i] = layerVisible[i];
        }
		aantalRijen = layerNames.length;
	}
	
	public String[] getLayerNames()
	{
		String[] currentLayerNames = new String[aantalRijen];
		for(int i = 0; i < aantalRijen; i++)
        	currentLayerNames[i] = layerNames[i];
        return currentLayerNames;
	}
	
	public boolean[] getLayerVisible()
	{
		boolean[] currentLayerVisible = new boolean[aantalRijen];
		for(int i = 0; i < aantalRijen; i++)
			currentLayerVisible[i] = layerVisible[i];
        return currentLayerVisible;
	}
	
	public void makeRowFields()
	{
		layerNameTextFields = new JTextField[maxLayers];
        layerVisibleCB = new JCheckBox[maxLayers];
        
        for(int i = 0; i < maxLayers; i++)
        {	
        	if(layerNames[i]==null || layerNames[i].trim().equals(""))
        	{	layerNames[i] = "layer"+(i+1);
        		layerVisible[i] = true;
        	}
        	
        	Dimension dim = new Dimension(120,20);
        	layerNameTextFields[i] = new JTextField();
        	layerNameTextFields[i].setPreferredSize(dim);
        	layerNameTextFields[i].addFocusListener(this);
        	layerNameTextFields[i].addActionListener(this);
    		
        	dim = new Dimension(100,20);
    		layerVisibleCB[i] = new JCheckBox();
    		layerVisibleCB[i].setPreferredSize(dim);
    		layerVisibleCB[i].addActionListener(this);
    		layerVisibleCB[i].setSelected(true);
        }
	}
	
	
	public void zetTeksten()
	{	
		for(int i = 0; i < aantalRijen; i++)
        {	if(layerNames != null && i < layerNames.length)
        		layerNameTextFields[i].setText(layerNames[i]);
        	if(layerVisible != null && i < layerVisible.length)
        		layerVisibleCB[i].setSelected(layerVisible[i]);
        	
        }
	 }
	
	
	
	public void makeGUI(int aantalRijen){
		layersPanel = new JPanel();
		bottomPanel = new JPanel();
        Box boxv = Box.createVerticalBox();
        
        Box boxh = Box.createHorizontalBox();
        boxh.add(Box.createHorizontalStrut(10));
        
        boxKolom = new Box[2];
        for(int i = 0; i < boxKolom.length; i++)
        {	boxKolom[i] = Box.createVerticalBox();
        	boxKolom[i].add(Box.createVerticalStrut(10));
        }
        JLabel vanLabel, naarLabel, scoresLabel, grensLabel;
        
        vanLabel = new JLabel(WiskOpdr.rb.getString("L_layerName"));
        vanLabel.setPreferredSize(new Dimension(120,20));
        boxKolom[0].add(vanLabel);
        
        naarLabel = new JLabel(WiskOpdr.rb.getString("L_layerVisible"));
        naarLabel.setPreferredSize(new Dimension(100,20));
        boxKolom[1].add(naarLabel);
        
               
        for(int i = 0; i < aantalRijen; i++)
        {	boxKolom[0].add(layerNameTextFields[i]);
        	boxKolom[1].add(layerVisibleCB[i]);
        }
        
        for(int i = 0; i < boxKolom.length; i++)
        	boxh.add(boxKolom[i]);
        boxv.add(boxh);
        
        boxh = Box.createHorizontalBox();
        boxh.add(Box.createHorizontalStrut(20));
        
        aantalRijenKnop = new PlusMinKnop(0, 0, 16, 20, PlusMinKnop.VERTIKAAL);
        aantalRijenKnop.setPreferredSize(new Dimension(16, 20));
        aantalRijenKnop.setSize(getPreferredSize());
        aantalRijenKnop.addActionListener(this);
        boxh.add(aantalRijenKnop);
        
        boxv.add(boxh);
        layersPanel.add(boxv);
         
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
                
		scrollPane = new JScrollPane(layersPanel);
    }
	
	public void makeFrame(){
    	frame = DialogFacade.newInstance(this, "", true);
    	frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		frame.pack();
	    frame.setVisible(true);
	}
	
	private void makeObjects(){   
    	
    	
    }
	
	
	public void actionPerformed(ActionEvent e){
	
		if(e.getSource().equals(this) && frame==null)
		{	makeRowFields();
			
			if(layerNames != null && layerNames.length > 0)
			{	makeGUI(aantalRijen);
				//aantalRijen = layerNames.length;
			}
			else
			{	makeGUI(1);
				aantalRijen = 1;
			}
			zetTeksten();
			makeFrame();
		}
		else if(e.getSource() instanceof JTextField)
		{	for(int i = 0; i < aantalRijen; i++)
			{	layerNames[i] = layerNameTextFields[i].getText();
			}
		}
		else if(e.getSource() instanceof JCheckBox)
		{	for(int i = 0; i < aantalRijen; i++)
			{	layerVisible[i] = layerVisibleCB[i].isSelected();
			}
		}
		else if(e.getSource().equals(aantalRijenKnop))
		{	if(e.getActionCommand().equals("min") && aantalRijen < maxLayers)
			{	makeGUI(aantalRijen + 1);
				aantalRijen++;
				layerNameTextFields[aantalRijen-1].setText("layer"+aantalRijen);
				frame.getContentPane().removeAll();
				frame.getContentPane().add(scrollPane);
				frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
				frame.pack();
			}
			else if(e.getActionCommand().equals("plus") && aantalRijen > 0)
			{	makeGUI(aantalRijen - 1);
				aantalRijen--;
				layerNameTextFields[aantalRijen].setText("layer"+(aantalRijen+1));
				layerVisibleCB[aantalRijen].setSelected(true);
		 	
				frame.getContentPane().removeAll();
				frame.getContentPane().add(scrollPane);
				frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
				frame.pack();
			}
		}
		else if(e.getSource().equals(okButton)) {   
			makeObjects();
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

	public void focusGained(FocusEvent arg0) {
	}

	public void focusLost(FocusEvent e) 
	{	if(e.getSource() instanceof JTextField)
		{	for(int i = 0; i < aantalRijen; i++)
			{	layerNames[i] = layerNameTextFields[i].getText();
			}
		}
	}   
}