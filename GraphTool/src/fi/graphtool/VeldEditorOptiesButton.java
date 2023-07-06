package fi.graphtool;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fi.wiskopdr.DialogFacade;

public class VeldEditorOptiesButton extends JButton implements ActionListener {	
	
	private DialogFacade frame;
	private GraphToolInteractieEditPanel graphToolInteractieEditPanel;
//	public JCheckBox functieToegestaanCB, ongelijkheidToegestaanCB, implicieteFunctieToegestaanCB, verticaleLijnToegestaanCB, parametrisatieToegestaanCB;
	
	JComboBox veldGrafiekTypeCombo, veldPijlGrootteModusCombo;
	private JTextField veldComponentHoogteTF, veldPijlGrootteTF, veldPijlSchaalTF ;
	private JLabel veldPijlGrootteLabel, veldPijlSchaalLabel;
	private JCheckBox largerGridStartPointsCB;
	
	private VeldComponent.FieldGraphType veldGrafiekType = VeldComponent.cDefault_VeldGrafiekType;
	private VeldComponent.FieldGraphArrowSizeMode veldPijlGrootteModus = VeldComponent.cDefault_VeldPijlGrootteModus;
	private int veldPijlGroottePixels = VeldComponent.cDefault_PijlGroottePixels;
	private double veldPijlSchaalfactor = VeldComponent.cDefault_PijlSchaalFactor;
	private int veldComponentHoogte = VeldComponent.cDefault_VeldComponentHoogte;
	private boolean veldLargerGridStartPoints = VeldComponent.cDefault_VeldLargerGridStartPoints;
	
	private Font theFont = new Font("SansSerif", Font.PLAIN, 12);
	
	private JButton okButton; 
	private JButton cancelButton;
	
	JPanel optiesPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JScrollPane scrollPane;
	
	public VeldEditorOptiesButton(GraphToolInteractieEditPanel gtiep)
	{	
		super(GraphTool.rb.getString("GTIEP_veldEditorOpties"));
		graphToolInteractieEditPanel = gtiep;
		addActionListener(this);
		setOptions(null);		
	}

	//soort setState-methode.
	public void setOptions (Hashtable h) {	
		VeldComponent.FieldGraphType veldGrafiekType = VeldComponent.cDefault_VeldGrafiekType;
		VeldComponent.FieldGraphArrowSizeMode veldPijlGrootteModus = VeldComponent.cDefault_VeldPijlGrootteModus;
		int veldPijlGroottePixels = VeldComponent.cDefault_PijlGroottePixels;
		double veldPijlSchaalfactor = VeldComponent.cDefault_PijlSchaalFactor;
		int veldComponentHoogte = VeldComponent.cDefault_VeldComponentHoogte;
		boolean veldLargerGridStartPoints = VeldComponent.cDefault_VeldLargerGridStartPoints;
		
		if(h!=null)
		{
			if(h.containsKey("veldGrafiekType"))
				veldGrafiekType = VeldComponent.FieldGraphType.values()[ ((Integer)h.get("veldGrafiekType")).intValue() ];
			if(h.containsKey("veldPijlGrootteModus"))
				veldPijlGrootteModus = VeldComponent.FieldGraphArrowSizeMode.values()[ ((Integer)h.get("veldPijlGrootteModus")).intValue() ];
			if(h.containsKey("veldPijlGroottePixels"))
				veldPijlGroottePixels = ((Integer)h.get("veldPijlGroottePixels")).intValue();
			if(h.containsKey("veldPijlSchaalfactor"))
				veldPijlSchaalfactor = ((Double)h.get("veldPijlSchaalfactor")).doubleValue();
			if(h.containsKey("veldLargerGridStartPoints"))
				veldLargerGridStartPoints = ((Boolean)h.get("veldLargerGridStartPoints")).booleanValue();
			if(h.containsKey("veldComponentHoogte"))
				veldComponentHoogte = ((Integer)h.get("veldComponentHoogte")).intValue();

		}
		this.veldGrafiekType = veldGrafiekType;
		this.veldPijlGrootteModus = veldPijlGrootteModus;
		this.veldPijlGroottePixels = veldPijlGroottePixels;
		this.veldPijlSchaalfactor = veldPijlSchaalfactor;
		this.veldComponentHoogte = veldComponentHoogte;
		this.veldLargerGridStartPoints = veldLargerGridStartPoints;
	}
	
	public Hashtable getOptions() {	
		VeldComponent.FieldGraphType veldGrafiekType = this.veldGrafiekType;
		int veldComponentHoogte = this.veldComponentHoogte;
		VeldComponent.FieldGraphArrowSizeMode veldPijlGrootteModus = this.veldPijlGrootteModus;
		int veldPijlGroottePixels = this.veldPijlGroottePixels;
		double veldPijlSchaalfactor = this.veldPijlSchaalfactor;
		boolean veldLargerGridStartPoints = this.veldLargerGridStartPoints;
		
		Hashtable h = new Hashtable();
		h.put("veldGrafiekType", new Integer(veldGrafiekType.ordinal()));
		h.put("veldPijlGrootteModus", new Integer(veldPijlGrootteModus.ordinal()));
		h.put("veldPijlGroottePixels", new Integer(veldPijlGroottePixels));
		h.put("veldPijlSchaalfactor", new Double(veldPijlSchaalfactor));
		h.put("veldLargerGridStartPoints", new Boolean(veldLargerGridStartPoints));
		h.put("veldComponentHoogte", new Integer(veldComponentHoogte));
		
		return h;
		
	}
	
	private void maakOpties()
	{
		veldLargerGridStartPoints = largerGridStartPointsCB.isSelected();

		try{
			veldComponentHoogte = Integer.parseInt(veldComponentHoogteTF.getText());
		}
		catch(Exception e)
		{}
		if (veldPijlGrootteTF.isVisible()) {
			try{
				veldPijlGroottePixels = Integer.parseInt(veldPijlGrootteTF.getText());
			}
			catch(Exception e)
			{}
		}
		if (veldPijlSchaalTF.isVisible()) {
			try{
				veldPijlSchaalfactor = Double.parseDouble(veldPijlSchaalTF.getText());
			}
			catch(Exception e)
			{}
		}
	}
	
//	private JCheckBox maakCheckBox(
//			String s, //int x, int y, int b, int h, 
//			boolean selected, Box parent
//			)
//	{	JCheckBox checkbox = new JCheckBox(GraphTool.rb.getString(s));
//		checkbox.setPreferredSize(new Dimension(250, 20));
//		checkbox.setFont(theFont);
//		checkbox.setBackground(getBackground());
//		checkbox.setSelected(selected);
//		checkbox.addActionListener(this);
//		parent.add(checkbox);
//		
//		return checkbox;
//	}
	
	public void makeGUI() {
		int xBase = 5, yBase = 5;
		int rowHeight = 30, columnWidth = 140; 
		int tab1 = xBase, tab2 = tab1 + columnWidth, tab3 = tab2 + columnWidth + 2*xBase;
		
	 	optiesPanel = new JPanel( );
	 	optiesPanel.setLayout(null);
		bottomPanel = new JPanel();
		optiesPanel.setSize(4 * xBase + 3 * columnWidth, 2 * yBase + 7 * rowHeight);
	        
		// Header
		JLabel OptiesLabel = new JLabel(GraphTool.rb.getString("GTIEP_veldEditorOpties"));
        OptiesLabel.setLocation(tab1, yBase /* + 0 * rowHeight */);
        OptiesLabel.setSize(OptiesLabel.getPreferredSize());
        optiesPanel.add(OptiesLabel);

        // Rij - Type
        JLabel veldTypeLabel = new JLabel(GraphTool.rb.getString("GTIEP_veldGrafiekType"));
        veldTypeLabel.setLocation(tab1, yBase  + 1 * rowHeight );
        veldTypeLabel.setSize(veldTypeLabel.getPreferredSize());
        veldTypeLabel.setFont(theFont);
        optiesPanel.add(veldTypeLabel);
        veldGrafiekTypeCombo = new JComboBox();
        for (int i=0; i<VeldComponent.cVeldGrafiekTypeStrings.size(); i++) {
        	String addStr = VeldComponent.cVeldGrafiekTypeStrings.get(i);
        	veldGrafiekTypeCombo.addItem(addStr);
        }
		veldGrafiekTypeCombo.setLocation(tab2, yBase + 1 * rowHeight);
		veldGrafiekTypeCombo.setSize(veldGrafiekTypeCombo.getPreferredSize());
		veldGrafiekTypeCombo.addActionListener(this);
		optiesPanel.add(veldGrafiekTypeCombo);

		// Rij - Pijl Grootte Modus
        JLabel veldPijlGrootteModusLabel = new JLabel(GraphTool.rb.getString("GTIEP_veldGrafiek_PijlGrootteModus"));
        veldPijlGrootteModusLabel.setLocation(tab1, yBase  + 2 * rowHeight );
        veldPijlGrootteModusLabel.setSize(veldPijlGrootteModusLabel.getPreferredSize());
        veldPijlGrootteModusLabel.setFont(theFont);
        optiesPanel.add(veldPijlGrootteModusLabel);
		veldPijlGrootteModusCombo = new JComboBox();
        for (int i=0; i<VeldComponent.cVeldGrafiekPijlGrootteModusStrings.size(); i++) {
        	veldPijlGrootteModusCombo.addItem(VeldComponent.cVeldGrafiekPijlGrootteModusStrings.get(i));
        }
		veldPijlGrootteModusCombo.setLocation(tab2, yBase + 2 * rowHeight);
		veldPijlGrootteModusCombo.setSize(veldPijlGrootteModusCombo.getPreferredSize());
		veldPijlGrootteModusCombo.addActionListener(this);
		optiesPanel.add(veldPijlGrootteModusCombo);
		
		// Rij - Pijl Grootte (fixed size)
		veldPijlGrootteLabel = new JLabel(GraphTool.rb.getString("GTIEP_veldGrafiek_PijlGrootte"));
		veldPijlGrootteLabel.setFont(theFont);
		veldPijlGrootteLabel.setLocation(tab1, yBase + 3* rowHeight);
		veldPijlGrootteLabel.setSize(veldPijlGrootteLabel.getPreferredSize());
		veldPijlGrootteLabel.setVisible(false);
		optiesPanel.add(veldPijlGrootteLabel);
		veldPijlGrootteTF = new JTextField("" + veldPijlGroottePixels);
		veldPijlGrootteTF.setLocation(tab2, yBase + 3 * rowHeight);
		veldPijlGrootteTF.setSize(new Dimension(50, 20));
		veldPijlGrootteTF.setFont(theFont);
		veldPijlGrootteTF.setVisible(false);
		optiesPanel.add(veldPijlGrootteTF);
		
		// Rij - Pijl Schaal
		veldPijlSchaalLabel = new JLabel(GraphTool.rb.getString("GTIEP_veldGrafiek_PijlSchaalFactor"));
		veldPijlSchaalLabel.setFont(theFont);
		veldPijlSchaalLabel.setLocation(tab1, yBase + 3* rowHeight);
		veldPijlSchaalLabel.setSize(veldPijlSchaalLabel.getPreferredSize());		
		veldPijlSchaalLabel.setVisible(false);
		optiesPanel.add(veldPijlSchaalLabel);
		veldPijlSchaalTF = new JTextField("" + veldPijlSchaalfactor);
		veldPijlSchaalTF.setLocation(tab2, yBase + 3 * rowHeight);
		veldPijlSchaalTF.setSize(new Dimension(50, 20));
		veldPijlSchaalTF.setFont(theFont);
		veldPijlSchaalTF.setVisible(false);
		optiesPanel.add(veldPijlSchaalTF);

		// Rij Larger Grid StartPoints
		largerGridStartPointsCB = new JCheckBox(GraphTool.rb.getString("GTIEP_veldGrafiek_GroterGridStartPunten"));
		largerGridStartPointsCB.setLocation(tab1, yBase + 4 * rowHeight);
		largerGridStartPointsCB.setSize(new Dimension(250, 20));
		largerGridStartPointsCB.setFont(theFont);
		largerGridStartPointsCB.setSelected(veldLargerGridStartPoints);
		optiesPanel.add(largerGridStartPointsCB);
		
		
		// Rij - Hoogte
		JLabel hoogteLabel = new JLabel(GraphTool.rb.getString("GTIEP_veldComponentHoogte"));
		hoogteLabel.setFont(theFont);
		hoogteLabel.setLocation(tab1, yBase + 6* rowHeight);
		hoogteLabel.setSize(hoogteLabel.getPreferredSize());
		optiesPanel.add(hoogteLabel);
		veldComponentHoogteTF = new JTextField("" + veldComponentHoogte);
		veldComponentHoogteTF.setLocation(tab2, yBase + 6 * rowHeight);
		veldComponentHoogteTF.setSize(new Dimension(50, 20));
		veldComponentHoogteTF.setFont(theFont);
		optiesPanel.add(veldComponentHoogteTF);
			
		okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
	        
		scrollPane = new JScrollPane(optiesPanel);
		scrollPane.setPreferredSize(optiesPanel.getSize());
		scrollPane.setBounds(0, 0, 2*xBase + 3 * columnWidth, 2 * yBase+ 5 * rowHeight);
		
//		System.out.println("2   - scrollPane - Height = " + scrollPane.getHeight());
		// Set values		
		veldGrafiekTypeCombo.setSelectedIndex(veldGrafiekType.ordinal());
		veldPijlGrootteModusCombo.setSelectedIndex(veldPijlGrootteModus.ordinal());
	}
	    
    public void makeFrame(){
    	frame = DialogFacade.newInstance(this, "", true);
    	frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent w) {
			    frame=null;
			}
		} );
	    frame.setVisible(true);
		    
    }
		
		public void actionPerformed(ActionEvent e){
//			System.out.println("Action Source = "+ e.getSource());
			if(e.getSource().equals(this) && frame==null){	
				makeGUI();
				makeFrame();
			} 
			else if(e.getSource().equals(veldPijlGrootteModusCombo)) {
				
				veldPijlGrootteModus = VeldComponent.FieldGraphArrowSizeMode.values()[veldPijlGrootteModusCombo.getSelectedIndex()];
				
				if (veldPijlGrootteModus == VeldComponent.FieldGraphArrowSizeMode.FIXEDSIZE ) { // Fixed size (pixels)
					veldPijlGrootteTF.setVisible(true);
					veldPijlGrootteLabel.setVisible(true);
				} else {
					veldPijlGrootteTF.setVisible(false);					
					veldPijlGrootteLabel.setVisible(false);
				}
				if (veldPijlGrootteModus == VeldComponent.FieldGraphArrowSizeMode.SCALEDSIZE) { // Scaled size (factor)
					veldPijlSchaalTF.setVisible(true);
					veldPijlSchaalLabel.setVisible(true);
				} else {
					veldPijlSchaalTF.setVisible(false);					
					veldPijlSchaalLabel.setVisible(false);
				}
			}
			else if(e.getSource().equals(veldGrafiekTypeCombo)) {
				veldGrafiekType = VeldComponent.FieldGraphType.values()[veldGrafiekTypeCombo.getSelectedIndex()];
			}
			else if(e.getSource().equals(okButton)) {   
				maakOpties();
				graphToolInteractieEditPanel.zetVeldEditorOpties(false);
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

