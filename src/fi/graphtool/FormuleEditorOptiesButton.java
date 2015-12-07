package fi.graphtool;

import java.awt.BorderLayout;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fi.wiskopdr.DialogFacade;

public class FormuleEditorOptiesButton extends JButton implements ActionListener
{	private DialogFacade frame;
	private GraphToolInteractieEditPanel graphToolInteractieEditPanel;
	public JCheckBox grafiekKleurenCB, kleurInstelbaarCB, functieBeginZichtbaarCB, functieBeginAanpasbaarCB, formeleFunctiesCB,
				domeinInstelbaarCB;
	public JCheckBox functieToegestaanCB, ongelijkheidToegestaanCB, implicieteFunctieToegestaanCB, verticaleLijnToegestaanCB, parametrisatieToegestaanCB;
	
	private JLabel hoogteLabel;
	private JTextField hoogteTF;
	
	
	private boolean grafiekKleuren, kleurInstelbaar, functieBeginZichtbaar, functieBeginAanpasbaar, formeleFuncties, domeinInstelbaar;
	private boolean functieToegestaan, ongelijkheidToegestaan, implicieteFunctieToegestaan, verticaleLijnToegestaan, parametrisatieToegestaan;
	private int formuleComponentHoogte = 120;
	
	private Font theFont = new Font("SansSerif", Font.PLAIN, 12);
	
	private JButton okButton; 
	private JButton cancelButton;
	
	JPanel optiesPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JScrollPane scrollPane;
	
	public FormuleEditorOptiesButton(GraphToolInteractieEditPanel gtiep)
	{	
		super(GraphTool.rb.getString("GTIEP_formuleEditorOpties"));
		graphToolInteractieEditPanel = gtiep;
		addActionListener(this);
		setOptions(null);
		
	}

	//soort setState-methode.
	public void setOptions (Hashtable h)
	{	boolean grafiekKleuren = true;
		boolean kleurInstelbaar = true;
		boolean functieBeginZichtbaar = true;
		boolean functieBeginAanpasbaar = true;
		boolean formeleFuncties = true;
		boolean domeinInstelbaar = false;
		int formuleComponentHoogte = 120;
		
		boolean functieToegestaan = true;
		boolean ongelijkheidToegestaan = true;
		boolean implicieteFunctieToegestaan = false;
		boolean verticaleLijnToegestaan = true;
		boolean parametrisatieToegestaan = false;
		
		if(h!=null)
		{
			if(h.containsKey("grafiekKleuren"))
				grafiekKleuren = ((Boolean)h.get("grafiekKleuren")).booleanValue();
			if(h.containsKey("kleurInstelbaar"))
				kleurInstelbaar = ((Boolean)h.get("kleurInstelbaar")).booleanValue();
			if(h.containsKey("functieBeginZichtbaar"))
				functieBeginZichtbaar = ((Boolean)h.get("functieBeginZichtbaar")).booleanValue();
			if(h.containsKey("functieBeginAanpasbaar"))
				functieBeginAanpasbaar = ((Boolean)h.get("functieBeginAanpasbaar")).booleanValue();
			if(h.containsKey("formeleFuncties"))
				formeleFuncties = ((Boolean)h.get("formeleFuncties")).booleanValue();
			if(h.containsKey("domeinInstelbaar"))
				domeinInstelbaar = ((Boolean)h.get("domeinInstelbaar")).booleanValue();
			if(h.containsKey("formuleComponentHoogte"))
				formuleComponentHoogte = ((Integer)h.get("formuleComponentHoogte")).intValue();
			
			if(h.containsKey("functieToegestaan"))
				functieToegestaan = ((Boolean)h.get("functieToegestaan")).booleanValue();
			if(h.containsKey("ongelijkheidToegestaan"))
				ongelijkheidToegestaan = ((Boolean)h.get("ongelijkheidToegestaan")).booleanValue();
			if(h.containsKey("implicieteFunctieToegestaan"))
				implicieteFunctieToegestaan = ((Boolean)h.get("implicieteFunctieToegestaan")).booleanValue();
			if(h.containsKey("verticaleLijnToegestaan"))
				verticaleLijnToegestaan = ((Boolean)h.get("verticaleLijnToegestaan")).booleanValue();
			if(h.containsKey("parametrisatieToegestaan"))
				parametrisatieToegestaan = ((Boolean)h.get("parametrisatieToegestaan")).booleanValue();
		}
		this.grafiekKleuren = grafiekKleuren;
		this.kleurInstelbaar = kleurInstelbaar;
		this.functieBeginZichtbaar = functieBeginZichtbaar;
		this.functieBeginAanpasbaar = functieBeginAanpasbaar;
		this.formeleFuncties = formeleFuncties;
		this.domeinInstelbaar = domeinInstelbaar;
		this.formuleComponentHoogte = formuleComponentHoogte;
		
		this.functieToegestaan = functieToegestaan;
		this.ongelijkheidToegestaan = ongelijkheidToegestaan;
		this.implicieteFunctieToegestaan = implicieteFunctieToegestaan;
		this.verticaleLijnToegestaan = verticaleLijnToegestaan;
		this.parametrisatieToegestaan = parametrisatieToegestaan;
		
	}
	
	public Hashtable getOptions()
	{	boolean grafiekKleuren = true;
		boolean kleurInstelbaar = true;
		boolean functieBeginZichtbaar = true;
		boolean functieBeginAanpasbaar = true;
		boolean formeleFuncties = true;
		boolean domeinInstelbaar = false;
		int formuleComponentHoogte = 120;
		
		boolean functieToegestaan = true;
		boolean ongelijkheidToegestaan = true;
		boolean implicieteFunctieToegestaan = false;
		boolean verticaleLijnToegestaan = true;
		boolean parametrisatieToegestaan = false;
		
		grafiekKleuren = this.grafiekKleuren;
		kleurInstelbaar = this.kleurInstelbaar;
		functieBeginZichtbaar = this.functieBeginZichtbaar;
		functieBeginAanpasbaar = this.functieBeginAanpasbaar;
		formeleFuncties = this.formeleFuncties;
		domeinInstelbaar = this.domeinInstelbaar;
		formuleComponentHoogte = this.formuleComponentHoogte;
		
		functieToegestaan = this.functieToegestaan;
		ongelijkheidToegestaan = this.ongelijkheidToegestaan;
		implicieteFunctieToegestaan = this.implicieteFunctieToegestaan;
		verticaleLijnToegestaan = this.verticaleLijnToegestaan;
		parametrisatieToegestaan = this.parametrisatieToegestaan;
		
		Hashtable h = new Hashtable();
		h.put("grafiekKleuren", new Boolean(grafiekKleuren));
		h.put("kleurInstelbaar", new Boolean(kleurInstelbaar));
		h.put("functieBeginZichtbaar", new Boolean(functieBeginZichtbaar));
		h.put("functieBeginAanpasbaar", new Boolean(functieBeginAanpasbaar));
		h.put("formeleFuncties", new Boolean(formeleFuncties));
		h.put("domeinInstelbaar", new Boolean(domeinInstelbaar));
		h.put("formuleComponentHoogte", new Integer(formuleComponentHoogte));
		
		h.put("functieToegestaan", new Boolean(functieToegestaan));
		h.put("ongelijkheidToegestaan", new Boolean(ongelijkheidToegestaan));
		h.put("implicieteFunctieToegestaan", new Boolean(implicieteFunctieToegestaan));
		h.put("verticaleLijnToegestaan", new Boolean(verticaleLijnToegestaan));
		h.put("parametrisatieToegestaan", new Boolean(parametrisatieToegestaan));
		
		return h;
		
	}
	
	private void maakOpties()
	{
		grafiekKleuren = grafiekKleurenCB.isSelected();
		kleurInstelbaar = kleurInstelbaarCB.isSelected();
		functieBeginZichtbaar = functieBeginZichtbaarCB.isSelected();
		functieBeginAanpasbaar = functieBeginAanpasbaarCB.isSelected();
		formeleFuncties = formeleFunctiesCB.isSelected();
		domeinInstelbaar = domeinInstelbaarCB.isSelected();
		try{
			formuleComponentHoogte = Integer.parseInt(hoogteTF.getText());
		}
		catch(Exception e)
		{}
		
		functieToegestaan = functieToegestaanCB.isSelected();
		ongelijkheidToegestaan = ongelijkheidToegestaanCB.isSelected();
		implicieteFunctieToegestaan = implicieteFunctieToegestaanCB.isSelected();
		verticaleLijnToegestaan = verticaleLijnToegestaanCB.isSelected();
		parametrisatieToegestaan = parametrisatieToegestaanCB.isSelected();
		
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
	
	public void makeGUI(){
		 	optiesPanel = new JPanel();
			bottomPanel = new JPanel();
	        
	        Box boxv = Box.createVerticalBox();
	        
	        Box boxh = Box.createHorizontalBox();
	        Box boxv2 = Box.createVerticalBox();
	        JLabel label = new JLabel(GraphTool.rb.getString("GTIEP_formuleEditorOpties"));
	        boxv2.add(label);
	        boxv2.add(Box.createVerticalStrut(10));
	        grafiekKleurenCB = maakCheckBox("GTIEP_grafiekKleurenOptie", grafiekKleuren, boxv2);
	        kleurInstelbaarCB = maakCheckBox("GTIEP_grafiekKleurInstelbaar", kleurInstelbaar, boxv2);
	        functieBeginZichtbaarCB = maakCheckBox("GTIEP_functieBeginZichtbaar", functieBeginZichtbaar, boxv2);
	        functieBeginAanpasbaarCB = maakCheckBox("GTIEP_functieBeginAanpasbaar", functieBeginAanpasbaar, boxv2);
	        formeleFunctiesCB = maakCheckBox("GTIEP_formeleFuncties", formeleFuncties, boxv2);
			domeinInstelbaarCB = maakCheckBox("GTIEP_domeinInstelbaar", domeinInstelbaar, boxv2);
			
			Box boxh3 = Box.createHorizontalBox();
			hoogteLabel = new JLabel(GraphTool.rb.getString("GTIEP_hoogte"));
			hoogteLabel.setFont(theFont);
			boxh3.add(hoogteLabel);
			boxh3.add(Box.createHorizontalStrut(10));
			hoogteTF = new JTextField("" + formuleComponentHoogte);
			hoogteTF.setSize(new Dimension(50, 20));
			hoogteTF.setFont(theFont);
			boxh3.add(hoogteTF);
			boxv2.add(boxh3);
			boxv2.add(Box.createHorizontalGlue());
			boxh.add(boxv2);
			
			boxv2 = Box.createVerticalBox();
			
			JLabel toegestaan = new JLabel(GraphTool.rb.getString("GTIEP_toegestaneTypes"));
			boxv2.add(toegestaan);
			boxv2.add(Box.createVerticalStrut(10));
	        functieToegestaanCB = maakCheckBox("GTIEP_functieToegestaan", functieToegestaan, boxv2);
			ongelijkheidToegestaanCB = maakCheckBox("GTIEP_ongelijkheidToegestaan", ongelijkheidToegestaan, boxv2); 
			implicieteFunctieToegestaanCB = maakCheckBox("GTIEP_implicieteFunctieToegestaan", implicieteFunctieToegestaan, boxv2); 
			implicieteFunctieToegestaanCB.setEnabled(false);
			verticaleLijnToegestaanCB = maakCheckBox("GTIEP_verticaleLijnToegestaan", verticaleLijnToegestaan, boxv2); 
			parametrisatieToegestaanCB = maakCheckBox("GTIEP_parametrisatieToegestaan", parametrisatieToegestaan, boxv2);
			parametrisatieToegestaanCB.setEnabled(false);
			
			boxv2.add(Box.createHorizontalGlue());
			boxv2.add(Box.createVerticalStrut(25));
			boxh.add(boxv2);
			
			boxv.add(boxh);
			optiesPanel.add(boxv);
			
			okButton = new JButton("Ok");
	        okButton.addActionListener(this);
	        bottomPanel.add(okButton);
	        
	        cancelButton = new JButton("Cancel");
	        cancelButton.addActionListener(this);
	        bottomPanel.add(cancelButton);
	        
			scrollPane = new JScrollPane(optiesPanel);
	    }
	    
	    public void makeFrame(){
	    	frame = DialogFacade.newInstance(this, "", true);
	    	frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        frame.getContentPane().setLayout(new BorderLayout());
	        frame.getContentPane().add(scrollPane);
	        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
			frame.addWindowListener( new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent w) {
				    frame.dispose();
				    frame=null;
				}
			} );

			frame.pack();
		    frame.setVisible(true);
		    
	    }
		
		public void actionPerformed(ActionEvent e){
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
