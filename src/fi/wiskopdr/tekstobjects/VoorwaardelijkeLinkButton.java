package fi.wiskopdr.tekstobjects;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.WiskOpdr;

public class VoorwaardelijkeLinkButton extends JButton implements ActionListener
{	
	private DialogFacade frame;
	JPanel urlPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JScrollPane scrollPane;
	
	private String[] urls;
	private int[] grensScores;
	
	private int maxUrls = 10;
	
	private JTextField[] urlTextFields;
	private JTextField[] grensTextFields;
	
	JButton okButton, cancelButton;
	
	public VoorwaardelijkeLinkButton(){	
		super("Voorwaarden");//Nog aanpassen! In: WiskOpdr.rb.getString("Voorwaarden") oid.
		addActionListener(this);
		
	}
	
	public void setUrls(String[] urls){   
		this.urls = urls;
	}
	
	public void setGrensScores(int[] grensScores){   
		this.grensScores = grensScores;
	}

	public void makeGUI(){
		urlPanel = new JPanel();
		bottomPanel = new JPanel();
        Box boxv = Box.createVerticalBox();
        
        Box boxh = Box.createHorizontalBox();
        boxh.add(Box.createHorizontalStrut(2));
        
        urlTextFields = new JTextField[maxUrls];
        grensTextFields = new JTextField[maxUrls];
        
        Box boxvl = Box.createVerticalBox();
        boxvl.add(Box.createVerticalStrut(maxUrls));
        Box boxvr = Box.createVerticalBox();
        boxvr.add(Box.createVerticalStrut(maxUrls));
        JLabel urlLabel, grensLabel;
        
        urlLabel = new JLabel("Url");//nog aanpassen?
        urlLabel.setPreferredSize(new Dimension(200,20));
        boxvl.add(urlLabel);
        
        grensLabel = new JLabel("Grensscore");//nog aanpassen!
        grensLabel.setPreferredSize(new Dimension(30,20));
        boxvr.add(grensLabel);
boolean c = urls == null;
System.out.println("urls = null in GUI " + c);          
        for(int i = 0; i < maxUrls; i++)
        {	urlTextFields[i] = new JTextField();
      
        	if(urls != null && i < urls.length)
        		urlTextFields[i].setText(urls[i]);
        	else
        		urlTextFields[i].setText("http://");
        	urlTextFields[i].setPreferredSize(new Dimension(200,20));
        	boxvl.add(urlTextFields[i]);
        	
        	grensTextFields[i] = new JTextField("");
        	grensTextFields[i].setPreferredSize(new Dimension(30,20));
        	boxvr.add(grensTextFields[i]);
        }
        boxh.add(boxvl);
        boxh.add(boxvr);
        
        boxv.add(boxh);
        	
        
        urlPanel.add(boxv);
        
        
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
        
		scrollPane = new JScrollPane(urlPanel);
    }
	
	
	public void makeFrame(){
    	frame = DialogFacade.newInstance(this, "", true);
    	Dimension preferredSize = new Dimension(400,320);
		frame.setPreferredSize(preferredSize);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(preferredSize);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		frame.pack();
	    frame.setVisible(true);
	    
    }
	
	 private void makeObjects(){   
	    	urls = null;
	    	String[] newObjects = null;
	    	/*
	    	for(int j=0 ; j<maxUrls ; j++)
	    	{	String checkObject = objectiveTextFields[j][0].getText();
	   			if(checkObject==null || "".equals(checkObject.trim()))
	   			{	objectives = new String[j][];
	   				break;
	   			}
	   			if(objectives == null)
	   				objectives = new String[maxCategories][];
	    	}
	    	*/
	    	//for(int j=0 ; j<objectives.length ; j++)
	    	//{	
	    	newObjects = new String[maxUrls];
		    for(int i=0 ; i<maxUrls ; i++)
		    {   String checkObject = urlTextFields[i].getText();
		       	if(checkObject!=null && !"".equals(checkObject.trim()) &&!"http://".equals(checkObject.trim()))	
		       		newObjects[i] = checkObject;
		        else   
		        {  	urls = new String[i];
		           	break;
		        }
		    }
		    if(urls==null)
		       	urls = new String[maxUrls];
		    for(int i=0 ; i<urls.length ; i++)
		       	urls[i] = newObjects[i];
		    grensScores = null;
	    	grensScores = new int[urls.length];
	    	for(int i = 0; i < urls.length; i++)
	    	{	try{
	    		grensScores[i] = Integer.parseInt(grensTextFields[i].getText());
	    		}
	    		catch(Exception e)
	    		{
	    			if(i==0)
	    				grensScores[i] = 0;
	    			else
	    				grensScores[i] = grensScores[i-1];
	    		}
	    	}	
	    }
	 
	 public String[] getUrls()
	 {   
	   	return urls;
	 }
	 
	 public int[] getGrensScores()
	 {
		 return grensScores;
	 }
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(this) && frame==null){	
			makeGUI();
			boolean b = urls == null;
System.out.println("urls is null: " + b);
if(urls != null)
System.out.println("lengte urls is " + urls.length);
			for (int i = 0 ; urls!=null &&  i < urls.length; i++)
			{	
				urlTextFields[i].setText(urls[i]);
			}
			for (int i = 0 ; grensScores!=null &&  i < grensScores.length; i++)
			{	
				grensTextFields[i].setText("" + grensScores[i]);
			}
			
			makeFrame();
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

}
