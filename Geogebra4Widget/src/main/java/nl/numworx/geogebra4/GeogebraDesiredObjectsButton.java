package nl.numworx.geogebra4;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JTable;

import fi.wiskopdr.DialogFacade;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

public class GeogebraDesiredObjectsButton extends JButton implements ActionListener
{
	private DialogFacade frame;
	private JTextField[] objectTextFields;
	private JTextField[] scoreTextFields;
    private JLabel[] objectLabels;
	
	private int maxObjects  = 10;
	private String[] objects;
	private int[] scores;
	private int scoreMax;
	private JButton okButton; 
	private JButton cancelButton;
	
	JPanel paramPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JScrollPane scrollPane;
	
	
	public GeogebraDesiredObjectsButton(){	
		super("Desired objects");
		addActionListener(this);
	}
	
	public void setObjects(String[] objects){   
		this.objects = objects;
	}
	
	public void setScores(int[] scores){   
		this.scores = scores;
    }
	
	public void setScoreMax(int scoreMax){   
        this.scoreMax = scoreMax;
    }
    
    private void makeObjects(){   
    	objects = null;
    	scoreMax = 0;
        String[] checkObjects = new String[maxObjects];
        int[] checkScores = new int[maxObjects];
        for(int i=0 ; i<maxObjects ; i++){   
        	String checkObject = objectTextFields[i].getText();
       		int checkScore = Integer.parseInt(scoreTextFields[i].getText());
            if(checkObject!=null && !"".equals(checkObject.trim())){	
            	checkObjects[i] = checkObject;
            	checkScores[i] = checkScore;
            	scoreMax += checkScore;
            }
            else{   
            	objects = new String[i];
            	scores = new int[i];
                break;
            }
        }
        if(objects==null){
        	objects = new String[maxObjects];
        	scores = new int[maxObjects];
        }
        for(int i=0 ; i<objects.length ; i++){
        	objects[i] = checkObjects[i];
        	scores[i] = checkScores[i];
        }
    }
    
    public String[] getObjects(){   
    	return objects;
    }
    
   
    public int[] getScores(){   
    	return scores;
    }
    
    public int getScoreMax(){   
    	return scoreMax;
    }
    
    public void makeGUI(){
    	paramPanel = new JPanel();
		bottomPanel = new JPanel();
        
        Box boxv = Box.createVerticalBox();
        
        Box boxh = Box.createHorizontalBox();
        boxh.add(Box.createHorizontalStrut(10));
        
        JLabel label = new JLabel("NAME");
        boxh.add(label);
        
        boxh.add(Box.createHorizontalStrut(100));
        
        label = new JLabel("VALUE");
        boxh.add(label);
        
        boxh.add(Box.createHorizontalStrut(100));
        
        label = new JLabel("SCORE");
        boxh.add(label);
        
        boxv.add(boxh);
        boxv.add(Box.createVerticalStrut(10));
        
        objectTextFields = new JTextField[maxObjects];
        scoreTextFields = new JTextField[maxObjects];
        objectLabels = new JLabel[maxObjects];
        for(int i=0 ; i<maxObjects ; i++){
            boxh = Box.createHorizontalBox();
            
            objectLabels[i] = new JLabel("Object "+(i+1));
            objectLabels[i].setPreferredSize(new Dimension(50,20));
            boxh.add(objectLabels[i]);
            
            objectTextFields[i] = new JTextField("");
            objectTextFields[i].setPreferredSize(new Dimension(200,20));
            boxh.add(objectTextFields[i]);
            
            scoreTextFields[i] = new JTextField("0");
            scoreTextFields[i].setPreferredSize(new Dimension(40,20));
            boxh.add(scoreTextFields[i]);

            boxv.add(boxh);
        }
        paramPanel.add(boxv);
        
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
        
		scrollPane = new JScrollPane(paramPanel);
    }
    
    public void makeFrame(){
    	frame = DialogFacade.newInstance(this, "");
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
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(this) && frame==null){	
			makeGUI();
			for (int i = 0 ; objects!=null &&  i < objects.length; i++){	
				objectTextFields[i].setText(objects[i]);
				scoreTextFields[i].setText(""+scores[i]);
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

