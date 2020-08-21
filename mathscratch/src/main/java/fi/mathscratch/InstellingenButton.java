package fi.mathscratch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.beans.numworxlf.JButton;
import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JOptionPane;
import fi.beans.numworxlf.JScrollPane;
import fi.beans.numworxlf.JTextField;
import fi.wiskopdr.WiskOpdr;


public class InstellingenButton extends JButton implements ActionListener {

	private JDialog frame;
	private JPanel mainPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JPanel topPanel = new JPanel();
	private JLabel titleLabel;
	private JButton okButton;
	private JButton cancelButton;
	
	private Hashtable currentInstellingen = new Hashtable();
	
	private JTextField[][] rectangleDataFields;
	private ArrayList<int[]> rectangleData;
	private int rectangleCount = 10;

	

	public InstellingenButton(String text) {
		super(text);
		addActionListener(this);
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(237,239,241));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(new Color(221,223,225));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(49,71,112));
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		
		titleLabel = new JLabel(MathScratch.rb.getString("inputAreasTitle"));
		titleLabel.setFont(new Font("SansSerif",Font.PLAIN, 24));
		titleLabel.setForeground(new Color(237,239,241));
		
		Box headerBox = Box.createHorizontalBox();		
		headerBox.add(Box.createHorizontalGlue());
		headerBox.add(titleLabel);
		headerBox.add(Box.createHorizontalGlue());
		topPanel.add(headerBox, BorderLayout.NORTH);
	

		rectangleDataFields = new JTextField[rectangleCount][4];
		rectangleData = new ArrayList<int[]>();
		for (int i = 0; i < rectangleCount; i++) {
			for (int j = 0; j < 4; j++) {
				rectangleDataFields[i][j] = new JTextField();
				rectangleDataFields[i][j].setPreferredSize(new Dimension(50,24));
			}
		}

		

	}

	public void setInstellingen(Hashtable h) {
		currentInstellingen = h;
		if (h.containsKey("rectangleData"))
			rectangleData = (ArrayList<int[]>) h.get("rectangleData");
		for (int i = 0; i < rectangleData.size(); i++) {
			for (int j = 0; j < 4; j++) {
				rectangleDataFields[i][j].setText("" + rectangleData.get(i)[j]);
			}
		}
		
	}

	public Hashtable getInstellingen() {
		return currentInstellingen;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this)) {
			JPanel paramPanel = new JPanel();
			paramPanel = new JPanel();

			Box boxv = Box.createVerticalBox();

			Box boxh = Box.createHorizontalBox();
			boxh.add(Box.createHorizontalStrut(100));

			JLabel label = new JLabel("x");
			label.setForeground(new Color(49,71,112));
			boxh.add(label);

			boxh.add(Box.createHorizontalStrut(40));

			label = new JLabel("y");
			label.setForeground(new Color(49,71,112));
			boxh.add(label);

			boxh.add(Box.createHorizontalStrut(40));

			label = new JLabel(MathScratch.rb.getString("areaWidth"));
			label.setForeground(new Color(49,71,112));
			boxh.add(label);

			boxh.add(Box.createHorizontalStrut(40));

			label = new JLabel(MathScratch.rb.getString("areaHeight"));
			label.setForeground(new Color(49,71,112));
			boxh.add(label);

			boxv.add(boxh);
			boxv.add(Box.createVerticalStrut(10));

			for (int i = 0; i < rectangleCount; i++) {
				boxh = Box.createHorizontalBox();
				boxh.add(Box.createHorizontalStrut(10));
				label = new JLabel(MathScratch.rb.getString("areaLabel") + " " + (i + 1));
				label.setForeground(new Color(49,71,112));
				boxh.add(label);
				boxh.add(Box.createHorizontalGlue());
				if(i<9)
					boxh.add(Box.createHorizontalStrut(20));
				else
					boxh.add(Box.createHorizontalStrut(13));
				for (int j = 0; j < 4; j++) {
					if (j > 0)
						boxh.add(Box.createHorizontalStrut(20));
					boxh.add(rectangleDataFields[i][j]);
				}
				// boxh.add(Box.createHorizontalStrut(10));
				boxv.add(boxh);
				boxv.add(Box.createVerticalStrut(3));
			}

			paramPanel.add(boxv);
			
			mainPanel.removeAll();
			bottomPanel.removeAll();
			mainPanel.add(paramPanel);

			
//			JPanel bottomPanel = new JPanel();
//			okButton = new JButton("Ok");
//			okButton.addActionListener(this);
//			bottomPanel.add(okButton);
//
//			cancelButton = new JButton("Cancel");
//			cancelButton.addActionListener(this);
//			bottomPanel.add(cancelButton);

			Box hb = Box.createHorizontalBox();
	        hb.add(Box.createHorizontalGlue());
	        
	        okButton = new JButton("Ok");
	        okButton.setPreferredSize(new Dimension(70,24));
			okButton.setBackground(new Color(49,71,112));
			okButton.setForeground(new Color(237,239,241));
	        okButton.addActionListener(this);
	        hb.add(okButton);
	        hb.add(Box.createHorizontalStrut(20));
	        
	        cancelButton = new JButton("Cancel");
	        cancelButton.setPreferredSize(new Dimension(70,24));
			cancelButton.setBackground(new Color(49,71,112));
			cancelButton.setForeground(new Color(237,239,241));
	        cancelButton.addActionListener(this);
	        hb.add(cancelButton);
	        
	        hb.add(Box.createHorizontalGlue());
	        bottomPanel.add(hb);
	        
			//JScrollPane scrollPane = new JScrollPane(paramPanel);

			frame = new JDialog(JOptionPane.getFrameForComponent(this), true);
			Dimension preferred = new Dimension(400, 450);
			frame.setPreferredSize(preferred);
			frame.setSize(preferred);
			frame.getContentPane().setLayout(new BorderLayout());
			//frame.getContentPane().add(scrollPane);
			frame.getContentPane().add(mainPanel);
	        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	        frame.getContentPane().add(topPanel,BorderLayout.NORTH);
	       
	       
			frame.pack();
			 Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
		      int x = (screenSize.width-frame.getSize().width)/2;
		      int y = (screenSize.height-frame.getSize().height)/2;
		      frame.setLocation(x , y);
		    frame.setVisible(true);
		} else if (e.getSource().equals(okButton)) {
			rectangleData = new ArrayList<int[]>();
			for (int i = 0; i < rectangleCount; i++) {
				int[] rectAttr = new int[4];
				boolean lastRect = false;
				for (int j = 0; j < 4; j++) {
					try {
						rectAttr[j] = Integer.parseInt(rectangleDataFields[i][j].getText());
					} catch (Exception ex) {
						rectAttr[j] = 0;
						lastRect = true;
					}
				}
				if (!lastRect)
					rectangleData.add(rectAttr);
				else
					break;
			}
			currentInstellingen.put("rectangleData", rectangleData);
			frame.setVisible(false);
			frame.dispose();
		} else if (e.getSource().equals(cancelButton)) {
			frame.setVisible(false);
			frame.dispose();
		}
	}

}
