package fi.wiskopdr;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;

import fi.beans.ideas.Exercise;
import fi.beans.ideas.IdeasIF;
import fi.beans.ideas.RuleIF;

public class IdeasInstellingenButton extends WiskOpdrButton implements ActionListener
{
	private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
	private DialogFacade frame;
	
	private String[] strategieDomeinen = {
    		IdeasIF.MATH_LINEQ,
    		IdeasIF.MATH_QUADREQ,
    		IdeasIF.MATH_QUADREQ_WITH_APPROX,
    		IdeasIF.MATH_QUADREQ_NO_ABC,
    		IdeasIF.MATH_HIGHERDEGREE,
    		IdeasIF.MATH_LININEQ,
    		IdeasIF.MATH_QUADRINEQ,
    		IdeasIF.MATH_INEQHIGHERDEGREE,
    		IdeasIF.MATH_COVERUP
    };
    private JPanel basisPanelRules, basisPanelBuggyRules, basisPanelDiagnosis;
	private JScrollPane scrollPaneRules, scrollPaneBuggyRules, scrollPaneDiagnosis;
    private JPanel contentPaneRules, contentPaneBuggyRules, contentPaneDiagnosis;
    
    private JCheckBox tipOpBalkCB;
    private JCheckBox hulpOpBalkCB;
    private JCheckBox stapOpBalkCB;
    private JCheckBox solveOpBalkCB;
    private JCheckBox meerTipsCB;
    private JCheckBox tipBijFoutCB;
    private JCheckBox feedbackBijFoutCB;
    private JCheckBox hulpBijTipCB;
    
    private String[] diagnoseMessages = {"expected","correct","detour","similar","buggy","notequiv","ready"};
    
    private Hashtable changedTextFields = new Hashtable();
    private Hashtable changedTexts = new Hashtable();
    
    private JComboBox strategieDomeinKeuze;
    
	private JButton okButton; 
	private JButton cancelButton;
	
	private JPanel instellingenPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JScrollPane scrollPane;
	
	private JLabel aftrekTipLabel;
	private JLabel aftrekHulpLabel;
	private JLabel aftrekStapLabel;
	private JLabel aftrekSolveLabel;
	
	private JTextField aftrekTipTF;
	private JTextField aftrekHulpTF;
	private JTextField aftrekStapTF;
	private JTextField aftrekSolveTF;
	
	private Hashtable instellingen = new Hashtable();
	
	ButtonGroup buttonGroup;
	JRadioButton ideasTipsButton;
	JRadioButton ideasTipsDiagnoseButton;

	
	
	public IdeasInstellingenButton(){	
		super(WiskOpdr.rb.getString("ideasButtonLabel"));
		addActionListener(this);
	}
	
	public void zetInstellingen(Hashtable instellingen){   
		this.instellingen = instellingen;
	}
	
	private void makeInstellingen(){   
		String strategieDomein = "";
		boolean tipOpBalk = true;
        boolean hulpOpBalk = false;
        boolean stapOpBalk = false;
        boolean solveOpBalk = false;
        boolean meerTips = false;
        boolean tipBijFout = false;
        boolean feedbackBijFout = false;
        boolean hulpBijTip = false;
        Hashtable changedTexts = new Hashtable();
        boolean diagnose = false;
        int aftrekTip = 0;
        int aftrekHulp = 0;
        int aftrekStap = 0;
        int aftrekSolve = 0;
        
        strategieDomein = strategieDomeinen[strategieDomeinKeuze.getSelectedIndex()];
		tipOpBalk = tipOpBalkCB.isSelected();
        hulpOpBalk = hulpOpBalkCB.isSelected();
        stapOpBalk = stapOpBalkCB.isSelected();
        solveOpBalk = solveOpBalkCB.isSelected();
        meerTips = meerTipsCB.isSelected();
        tipBijFout = tipBijFoutCB.isSelected();
        feedbackBijFout = feedbackBijFoutCB.isSelected();
        hulpBijTip = hulpBijTipCB.isSelected();
        changedTexts = this.changedTexts;
        diagnose = ideasTipsDiagnoseButton.isSelected();
        aftrekTip = Integer.parseInt(aftrekTipTF.getText());
        aftrekHulp = Integer.parseInt(aftrekHulpTF.getText());
        aftrekStap = Integer.parseInt(aftrekStapTF.getText());
        aftrekSolve = Integer.parseInt(aftrekSolveTF.getText());

		instellingen = new Hashtable();
		instellingen.put("strategieDomein",strategieDomein);
		instellingen.put("tipOpBalk",new Boolean(tipOpBalk));
		instellingen.put("hulpOpBalk",new Boolean(hulpOpBalk));
		instellingen.put("stapOpBalk",new Boolean(stapOpBalk));
		instellingen.put("solveOpBalk",new Boolean(solveOpBalk));
		instellingen.put("meerTips",new Boolean(meerTips));
		instellingen.put("tipBijFout",new Boolean(tipBijFout));
		instellingen.put("feedbackBijFout",new Boolean(feedbackBijFout));
		instellingen.put("hulpBijTip",new Boolean(hulpBijTip));
		instellingen.put("changedTexts",changedTexts);
		instellingen.put("diagnose",new Boolean(diagnose));
		instellingen.put("aftrekTip",new Integer(aftrekTip));
		instellingen.put("aftrekHulp",new Integer(aftrekHulp));
		instellingen.put("aftrekStap",new Integer(aftrekStap));
		instellingen.put("aftrekSolve",new Integer(aftrekSolve));
    }
	
	private void instellingenNaarGUI(){   
		 String strategieDomein = "";
         boolean tipOpBalk = true;
         boolean hulpOpBalk = false;
         boolean stapOpBalk = false;
         boolean solveOpBalk = false;
         boolean meerTips = false;
         boolean tipBijFout = false;
         boolean feedbackBijFout = false;
         boolean hulpBijTip = false;
         Hashtable changedTexts = new Hashtable();
         boolean diagnose = false;
         int aftrekTip = 0;
         int aftrekHulp = 0;
         int aftrekStap = 0;
         int aftrekSolve = 0;
         
         if(instellingen.containsKey("strategieDomein")) strategieDomein = (String)instellingen.get("strategieDomein");
     	 if(instellingen.containsKey("tipOpBalk")) tipOpBalk = ((Boolean)instellingen.get("tipOpBalk")).booleanValue();
         if(instellingen.containsKey("hulpOpBalk")) hulpOpBalk = ((Boolean)instellingen.get("hulpOpBalk")).booleanValue();
         if(instellingen.containsKey("stapOpBalk")) stapOpBalk = ((Boolean)instellingen.get("stapOpBalk")).booleanValue();
         if(instellingen.containsKey("solveOpBalk")) solveOpBalk = ((Boolean)instellingen.get("solveOpBalk")).booleanValue();
         if(instellingen.containsKey("meerTips")) meerTips = ((Boolean)instellingen.get("meerTips")).booleanValue();
         if(instellingen.containsKey("tipBijFout")) tipBijFout = ((Boolean)instellingen.get("tipBijFout")).booleanValue();
         if(instellingen.containsKey("feedbackBijFout")) feedbackBijFout = ((Boolean)instellingen.get("feedbackBijFout")).booleanValue();
         if(instellingen.containsKey("hulpBijTip")) hulpBijTip = ((Boolean)instellingen.get("hulpBijTip")).booleanValue();
         if(instellingen.containsKey("changedTexts")) changedTexts = (Hashtable)instellingen.get("changedTexts");
         if(instellingen.containsKey("diagnose")) diagnose = ((Boolean)instellingen.get("diagnose")).booleanValue();
         if(instellingen.containsKey("aftrekTip")) aftrekTip = ((Integer)instellingen.get("aftrekTip")).intValue();
         if(instellingen.containsKey("aftrekHulp")) aftrekHulp = ((Integer)instellingen.get("aftrekHulp")).intValue();
         if(instellingen.containsKey("aftrekStap")) aftrekStap = ((Integer)instellingen.get("aftrekStap")).intValue();
         if(instellingen.containsKey("aftrekSolve")) aftrekSolve = ((Integer)instellingen.get("aftrekSolve")).intValue();
         
     	this.changedTexts = changedTexts;
    	strategieDomeinKeuze.setSelectedItem(strategieDomein);
    	tipOpBalkCB.setSelected(tipOpBalk);
        hulpOpBalkCB.setSelected(hulpOpBalk);
        stapOpBalkCB.setSelected(stapOpBalk);
        solveOpBalkCB.setSelected(solveOpBalk);
        meerTipsCB.setSelected(meerTips);
        tipBijFoutCB.setSelected(tipBijFout);
        feedbackBijFoutCB.setSelected(feedbackBijFout);
        hulpBijTipCB.setSelected(hulpBijTip);
        ideasTipsDiagnoseButton.setSelected(diagnose);
        aftrekTipTF.setText(""+aftrekTip);
        aftrekHulpTF.setText(""+aftrekHulp);
        aftrekStapTF.setText(""+aftrekStap);
        aftrekSolveTF.setText(""+aftrekSolve);
    }
    
    public Hashtable geefInstellingen(){   
    	return instellingen;
    }
    
   
    public void makeGUI(){
    	instellingenPanel = new JPanel();
    	instellingenPanel.setLayout(null);
    	
		bottomPanel = new JPanel();
        
		tipOpBalkCB = makeCheckBox(400,35,200,20,"Tipknop op balk",true,true);
        hulpOpBalkCB = makeCheckBox(400,60,200,20,"Hulpknop op balk",false,true);
        stapOpBalkCB = makeCheckBox(400,85,200,20,"Stapknop op balk",false,true);
        solveOpBalkCB = makeCheckBox(400,110,200,20,"Uitwerkingknop balk",false,true);
        meerTipsCB = makeCheckBox(200,35,200,20,"Meer dan 1 tip",false,true);
        tipBijFoutCB = makeCheckBox(200,60,200,20,"Tip bij fout",false,true);
        feedbackBijFoutCB = makeCheckBox(200,85,200,20,"feedback bij fout (buggy rule)",true,true);
        hulpBijTipCB = makeCheckBox(200,110,200,20,"Hulpknop bij tip",false,true);
        
        buttonGroup = new ButtonGroup();
    	
        ideasTipsButton = new JRadioButton("Alleen IDEAS tips etc.");
    	ideasTipsButton.addActionListener(this);
    	ideasTipsButton.setFont(font);
    	ideasTipsButton.setBounds(20,35,180,20);
    	instellingenPanel.add(ideasTipsButton);
    	ideasTipsButton.setSelected(true);
    	buttonGroup.add(ideasTipsButton);
    	
    	ideasTipsDiagnoseButton = new JRadioButton("IDEAS tips + diagnose");
    	ideasTipsDiagnoseButton.addActionListener(this);
    	ideasTipsDiagnoseButton.setFont(font);
    	ideasTipsDiagnoseButton.setBounds(20,60,180,20);
    	instellingenPanel.add(ideasTipsDiagnoseButton);
    	buttonGroup.add(ideasTipsDiagnoseButton);
    	
    	aftrekTipLabel = makeLabel(600,35,100,20,"Aftrek tip",true);
    	aftrekHulpLabel = makeLabel(600,60,100,20,"Aftrek hulp",true);
    	aftrekStapLabel = makeLabel(600,85,100,20,"Aftrek stap",true);
    	aftrekSolveLabel = makeLabel(600,110,100,20,"Aftrek uitw.",true);
    	
    	aftrekTipTF = makeTextField(700,35,40,20,"0",true);
    	aftrekHulpTF = makeTextField(700,60,40,20,"0",true);
    	aftrekStapTF = makeTextField(700,85,40,20,"0",true);
    	aftrekSolveTF = makeTextField(700,110,40,20,"0",true);
    	
    	
        
        Exercise[] strategies = WiskOpdr.ideas.getExerciseList();
		Vector v =  new Vector(strategies.length);
		for (int i = 0; i < strategies.length; i++) {
			Exercise exercise = strategies[i];
			//if("math".equals(exercise.getDomain()))
				 // alles of niets?
			v.add(exercise.getID());
		}
		strategieDomeinen = new String[v.size()];
		for(int i=0 ; i< strategieDomeinen.length ; i++)
        {	strategieDomeinen[i] = (String)v.elementAt(i);
        }
        
        strategieDomeinKeuze = new JComboBox(v);
        
        strategieDomeinKeuze = new JComboBox();
        for(int i=0 ; i< strategieDomeinen.length ; i++)
        {	strategieDomeinKeuze.addItem(strategieDomeinen[i]);
        }
        strategieDomeinKeuze.setBounds(110,5,350,20);
        strategieDomeinKeuze.addActionListener(this);
        instellingenPanel.add(strategieDomeinKeuze,0);
        
        JLabel diagnosisLabel = new JLabel("Diagnose messages");
        diagnosisLabel.setFont(font);
        diagnosisLabel.setBounds(20,135,150,20);
        instellingenPanel.add(diagnosisLabel);
        
        contentPaneDiagnosis = new JPanel();
        contentPaneDiagnosis.setBounds(getBounds());
        contentPaneDiagnosis.setLayout(null);
        contentPaneDiagnosis.setBackground(getBackground());

        basisPanelDiagnosis = new JPanel();
        basisPanelDiagnosis.setBounds(20,155,760, 90);
        basisPanelDiagnosis.setLayout(new BorderLayout());
        basisPanelDiagnosis.setBackground(getBackground());
        instellingenPanel.add(basisPanelDiagnosis);
		
		scrollPaneDiagnosis = new JScrollPane(contentPaneDiagnosis,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneDiagnosis.setBackground(getBackground());
		scrollPaneDiagnosis.setBorder(BorderFactory.createLineBorder(Color.gray));
		basisPanelDiagnosis.add(scrollPaneDiagnosis);
		
		
		
		
		JLabel rulesLabel = new JLabel("Rules");
        rulesLabel.setFont(font);
        rulesLabel.setBounds(0,245,150,20);
        //add(rulesLabel);
        
        contentPaneRules = new JPanel();
        contentPaneRules.setBounds(getBounds());
        contentPaneRules.setLayout(null);
        contentPaneRules.setBackground(getBackground());

        basisPanelRules = new JPanel();
		basisPanelRules.setBounds(20,265,760, 120);
		basisPanelRules.setLayout(new BorderLayout());
		basisPanelRules.setBackground(getBackground());
		instellingenPanel.add(basisPanelRules);
		
		scrollPaneRules = new JScrollPane(contentPaneRules,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneRules.setBackground(getBackground());
		scrollPaneRules.setBorder(BorderFactory.createLineBorder(Color.gray));
		basisPanelRules.add(scrollPaneRules);
				
		JLabel buggyRulesLabel = new JLabel("Buggy rules");
		buggyRulesLabel.setFont(font);
		buggyRulesLabel.setBounds(20,385,150,20);
		instellingenPanel.add(buggyRulesLabel);
        
        contentPaneBuggyRules = new JPanel();
        contentPaneBuggyRules.setBounds(getBounds());
        contentPaneBuggyRules.setLayout(null);
        contentPaneBuggyRules.setBackground(getBackground());

        basisPanelBuggyRules = new JPanel();
        basisPanelBuggyRules.setBounds(20,405,760, 100);
        basisPanelBuggyRules.setLayout(new BorderLayout());
        basisPanelBuggyRules.setBackground(getBackground());
        instellingenPanel.add(basisPanelBuggyRules);
		
		scrollPaneBuggyRules = new JScrollPane(contentPaneBuggyRules,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneBuggyRules.setBackground(getBackground());
		scrollPaneBuggyRules.setBorder(BorderFactory.createLineBorder(Color.gray));
		basisPanelBuggyRules.add(scrollPaneBuggyRules);
   
        
        
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
        
		scrollPane = new JScrollPane(instellingenPanel);
    }
    
    public JCheckBox makeCheckBox(int x, int y, int b, int h, String text, boolean selected, boolean visible)
	{	JCheckBox checkbox = new JCheckBox(text);
		checkbox.setBounds(x,y,b,h);
		checkbox.setFont(font);
		checkbox.setOpaque(false);
		checkbox.addActionListener(this);
		checkbox.setSelected(selected);
		checkbox.setVisible(visible);
		instellingenPanel.add(checkbox,0);
		return checkbox;
	}
	
	public JLabel makeLabel(int x, int y, int b, int h, String text, boolean visible)
	{	JLabel label = new JLabel(text);
		label.setBounds(x,y,b,h);
		label.setFont(font);
		label.setVisible(visible);
		instellingenPanel.add(label,0);
		return label;
	}
	
	public JTextField makeTextField(int x, int y, int b, int h, String text, boolean visible)
	{	JTextField textField = new JTextField(text);
		textField.setBounds(x,y,b,h);
		textField.setFont(font);
		textField.addActionListener(this);
		textField.setVisible(visible);
		instellingenPanel.add(textField,0);
		return textField;
	}
	
	public void setNewScrollSize()
	{	
		int maxb = 0;
		int maxh = 0; //scrollPane.getSize().height-20;
		for(int i=0 ; i<contentPaneRules.getComponentCount() ; i++)
		{	Component c = contentPaneRules.getComponent(i);
			int b = c.getLocation().x + c.getSize().width-15;
			int h = c.getLocation().y + c.getSize().height;
			if(b>maxb) maxb = b;
			if(h>maxh) maxh = h;
		}
		//contentPane.setPreferredSize(new Dimension(contentPane.getSize().width, maxh));
		contentPaneRules.setPreferredSize(new Dimension(maxb, maxh));
		contentPaneRules.scrollRectToVisible(new Rectangle(0,0, 10, maxh));
		contentPaneRules.revalidate();
		contentPaneRules.doLayout();
		
		maxb = 0;
		maxh = 0; //scrollPane.getSize().height-20;
		for(int i=0 ; i<contentPaneBuggyRules.getComponentCount() ; i++)
		{	Component c = contentPaneBuggyRules.getComponent(i);
			int b = c.getLocation().x + c.getSize().width-15;
			int h = c.getLocation().y + c.getSize().height;
			if(b>maxb) maxb = b;
			if(h>maxh) maxh = h;
		}
		//contentPane.setPreferredSize(new Dimension(contentPane.getSize().width, maxh));
		contentPaneBuggyRules.setPreferredSize(new Dimension(maxb, maxh));
		contentPaneBuggyRules.scrollRectToVisible(new Rectangle(0,0, 10, maxh));
		contentPaneBuggyRules.revalidate();
		contentPaneBuggyRules.doLayout();
		
		maxb = 0;
		maxh = 0; //scrollPane.getSize().height-20;
		for(int i=0 ; i<contentPaneDiagnosis.getComponentCount() ; i++)
		{	Component c = contentPaneDiagnosis.getComponent(i);
			int b = c.getLocation().x + c.getSize().width-15;
			int h = c.getLocation().y + c.getSize().height;
			if(b>maxb) maxb = b;
			if(h>maxh) maxh = h;
		}
		//contentPane.setPreferredSize(new Dimension(contentPane.getSize().width, maxh));
		contentPaneDiagnosis.setPreferredSize(new Dimension(maxb, maxh));
		contentPaneDiagnosis.scrollRectToVisible(new Rectangle(0,0, 10, maxh));
		contentPaneDiagnosis.revalidate();
		contentPaneDiagnosis.doLayout();
	}
	
	private void presentRules()
    {
    	presentDiagnoseMessages();
    	
    	contentPaneRules.removeAll();
    	contentPaneBuggyRules.removeAll();
    	String strategieDomein = strategieDomeinen[strategieDomeinKeuze.getSelectedIndex()];
    	if(WiskOpdr.ideas==null)
		{	JOptionPane.showMessageDialog(this,"Feedbackservice not available");
			return;
		}
    	RuleIF[] rules = WiskOpdr.ideas.getRuleList(strategieDomein);
    	if(rules==null)
		{	JOptionPane.showMessageDialog(this,"Feedbackservice not available");
			return;
		}
    	int yRulesLocation = 0;
    	int yBuggyRulesLocation = 0;
		for(int i=0 ; i<rules.length ; i++)
    	{	
    		{	
	    		System.out.println(rules[i].getPrefix() + "   " + rules[i].getId());
	    		System.out.println(rules[i].getContext());
	    		
	    		JLabel label = new JLabel(rules[i].getId());
	    		label.setFont(font);
	    		
	    		JTextField field = new JTextField(AntwoordVergelijkingVak.translateRuleToStandard(rules[i].getId()));
	    		field.addActionListener(this);
	    		changedTextFields.put(field,rules[i].getId());
	    		if(changedTexts.containsKey(rules[i].getId()) && !AntwoordVergelijkingVak.translateRuleToStandard(rules[i].getId()).equals((String)changedTexts.get(rules[i].getId()))) 
	    		{	field.setText((String)changedTexts.get(rules[i].getId()));
	    			field.setForeground(Color.red);
	    		}
	    		field.setFont(font);
	    		
	    		if ("false" .equals(rules[i].getContext().get("buggy")))
	    		{	label.setBounds(5,yRulesLocation,300,20);
	    			field.setBounds(305,yRulesLocation,450,20);
	    			contentPaneRules.add(label);
	    			contentPaneRules.add(field);
	    			yRulesLocation += 25;
	    		}
	    		else 
	    		{	label.setBounds(5,yBuggyRulesLocation,300,20);
	    			field.setBounds(305,yBuggyRulesLocation,450,20);
	    			contentPaneBuggyRules.add(label);
	    			contentPaneBuggyRules.add(field);
	    			yBuggyRulesLocation += 25;
	    		}
	    	}
    	}
    	
    	setNewScrollSize();
		repaint();
    }
    
    private void presentDiagnoseMessages()
    {
    	contentPaneDiagnosis.removeAll();
    	contentPaneDiagnosis.removeAll();
    	String strategieDomein = strategieDomeinen[strategieDomeinKeuze.getSelectedIndex()];
    	
    	int yLocation = 0;
    	for(int i=0 ; i<diagnoseMessages.length ; i++)
    	{	JLabel label = new JLabel(diagnoseMessages[i]);
	    	label.setFont(font);
	    	
	    	JTextField field = new JTextField(AntwoordVergelijkingVak.translateRuleToStandard(diagnoseMessages[i]));
	    	field.addActionListener(this);
	    	changedTextFields.put(field,diagnoseMessages[i]);
	    	field.setFont(font);
	    	if(changedTexts.containsKey(diagnoseMessages[i]) && !(AntwoordVergelijkingVak.translateRuleToStandard(diagnoseMessages[i]).equals((String)changedTexts.get(diagnoseMessages[i])))) 
    		{	field.setText((String)changedTexts.get(diagnoseMessages[i]));
    			field.setForeground(Color.red);
    		}
	    	
    		
    		
	    	label.setBounds(5,yLocation,300,20);
	    	field.setBounds(305,yLocation,450,20);
	    	contentPaneDiagnosis.add(label);
	    	contentPaneDiagnosis.add(field);
	    	yLocation += 25;
	    }
    	
    	setNewScrollSize();
		repaint();
    }
    
    public void makeFrame(){
     	Dimension preferred = new Dimension(800,600);
        frame = DialogFacade.newInstance(this, "");
        frame.setPreferredSize(preferred);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(preferred);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		frame.pack();
	    frame.setVisible(true);
	    
    }
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(this) && frame==null){	
			makeGUI();
			presentRules();
			instellingenNaarGUI();
			makeFrame();
		}
		else if(e.getSource().equals(okButton)) {   
			makeInstellingen();
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
		else if(e.getSource()==strategieDomeinKeuze)
        {	presentRules();
        }
        else if(changedTextFields.containsKey(e.getSource()))
        {
        	String textToChange = (String)changedTextFields.get(e.getSource());
        	String changedText = ((JTextField)e.getSource()).getText();
        	((JTextField)e.getSource()).setForeground(Color.red);
        	changedTexts.put(textToChange,changedText);
        }
	}   
}

