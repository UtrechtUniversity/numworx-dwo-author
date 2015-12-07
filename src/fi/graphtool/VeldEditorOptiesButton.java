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
	public enum FieldGraphType {QUIVER, STREAMLINE};
	
	/* component defaults & contstants */
	private final static int cDefault_VeldComponentHoogte = 120;
	private final static FieldGraphType cDefault_VeldGrafiekType = FieldGraphType.QUIVER;
	private final static String	cVeldGrafiekTypeStrings[] = { "Quiver", "Streamline" };
	
	private DialogFacade frame;
	private GraphToolInteractieEditPanel graphToolInteractieEditPanel;
//	public JCheckBox functieToegestaanCB, ongelijkheidToegestaanCB, implicieteFunctieToegestaanCB, verticaleLijnToegestaanCB, parametrisatieToegestaanCB;
	
	JComboBox veldGrafiekTypeLB;
	private JTextField veldComponentHoogteTF;
	
	private int veldComponentHoogte = cDefault_VeldComponentHoogte;
	private FieldGraphType veldGrafiekType = cDefault_VeldGrafiekType;
	
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
		FieldGraphType veldGrafiekType = FieldGraphType.QUIVER;
		int veldComponentHoogte = cDefault_VeldComponentHoogte;
		
		if(h!=null)
		{
//			if(h.containsKey("grafiekKleuren"))
//				grafiekKleuren = ((Boolean)h.get("grafiekKleuren")).booleanValue();
			if(h.containsKey("veldComponentHoogte"))
				veldComponentHoogte = ((Integer)h.get("veldComponentHoogte")).intValue();
			if(h.containsKey("veldGrafiekType"))
				veldGrafiekType = FieldGraphType.values()[ ((Integer)h.get("veldGrafiekType")).intValue() ];
		}
		this.veldComponentHoogte = veldComponentHoogte;
		this.veldGrafiekType = veldGrafiekType;
	}
	
	public Hashtable getOptions() {	
		FieldGraphType veldGrafiekType = this.veldGrafiekType;
		int veldComponentHoogte = this.veldComponentHoogte;;
		
		Hashtable h = new Hashtable();
		h.put("veldComponentHoogte", new Integer(veldComponentHoogte));
		h.put("veldGrafiekType", new Integer(veldGrafiekType.ordinal()));
		
//		h.put("functieToegestaan", new Boolean(functieToegestaan));
		
		return h;
		
	}
	
	private void maakOpties()
	{
//		grafiekKleuren = grafiekKleurenCB.isSelected();
		try{
			veldComponentHoogte = Integer.parseInt(veldComponentHoogteTF.getText());
		}
		catch(Exception e)
		{}
		
	}
	
	private JCheckBox maakCheckBox(
			String s, //int x, int y, int b, int h, 
			boolean selected, Box parent
			)
	{	JCheckBox checkbox = new JCheckBox(GraphTool.rb.getString(s));
		checkbox.setPreferredSize(new Dimension(250, 20));
		checkbox.setFont(theFont);
		checkbox.setBackground(getBackground());
		checkbox.setSelected(selected);
		checkbox.addActionListener(this);
		parent.add(checkbox);
		
		return checkbox;
	}
	
	public void makeGUI() {
		int xBase = 5, yBase = 5;
		int rowHeight = 30, columnWidth = 120; 
		int tab1 = xBase, tab2 = tab1 + columnWidth, tab3 = tab2 + columnWidth + 2*xBase;
		
	 	optiesPanel = new JPanel( );
	 	optiesPanel.setLayout(null);
		bottomPanel = new JPanel();
		optiesPanel.setSize(4 * xBase + 3 * columnWidth, 2 * yBase + 5 * rowHeight);
	        
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
        veldGrafiekTypeLB = new JComboBox(cVeldGrafiekTypeStrings);
		veldGrafiekTypeLB.setSelectedIndex(0);
		veldGrafiekTypeLB.setLocation(tab2, yBase + 1 * rowHeight);
		veldGrafiekTypeLB.setSize(veldGrafiekTypeLB.getPreferredSize());
		optiesPanel.add(veldGrafiekTypeLB);

		// Rij - Hoogte
		JLabel hoogteLabel = new JLabel(GraphTool.rb.getString("GTIEP_veldComponentHoogte"));
		hoogteLabel.setFont(theFont);
		hoogteLabel.setLocation(tab1, yBase + 2* rowHeight);
		hoogteLabel.setSize(hoogteLabel.getPreferredSize());
		optiesPanel.add(hoogteLabel);
		veldComponentHoogteTF = new JTextField("" + veldComponentHoogte);
		veldComponentHoogteTF.setLocation(tab2, yBase + 2* rowHeight);
		veldComponentHoogteTF.setSize(new Dimension(50, 20));
		veldComponentHoogteTF.setFont(theFont);
		optiesPanel.add(veldComponentHoogteTF);
			
//      functieToegestaanCB = maakCheckBox("GTIEP_functieToegestaan", functieToegestaan, boxv2);
			
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
			System.out.println("Action Source = "+ e.getSource());
			if(e.getSource().equals(this) && frame==null){	
				makeGUI();
				makeFrame();
			}
			else if(e.getSource().equals(okButton)) {   
				maakOpties();
				graphToolInteractieEditPanel.zetFormuleEditorOpties(false);
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

