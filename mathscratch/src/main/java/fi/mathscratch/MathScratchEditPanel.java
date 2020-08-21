package fi.mathscratch;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.JPanel;

import fi.beans.numworxlf.JCheckBox;
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class MathScratchEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	private MathScratchPanel mathcratchPanel;
	private JPanel panel;
	private JPanel instellingenPanel;
	private InstellingenButton instellingenButton;
	
	private JCheckBox calculatorCB;
	
	public MathScratchEditPanel(MathScratchPanel mathcratchPanel) {
		this.mathcratchPanel = mathcratchPanel;
		mathcratchPanel.setBounds(0,0,500,450);
		
		panel = new JPanel(null);
		panel.setPreferredSize(new Dimension(500,451));
		panel.add(mathcratchPanel);
		
		instellingenPanel = new JPanel(null);
		instellingenPanel.setPreferredSize(new Dimension(200,451));
		instellingenPanel.setLayout(new BorderLayout());
		
		calculatorCB = new JCheckBox(MathScratch.rb.getString("calculatorCB"));
		instellingenButton = new InstellingenButton (MathScratch.rb.getString("inputAreasButton"));
		Box vb = Box.createVerticalBox();
		vb.add(calculatorCB);
		vb.add(Box.createVerticalStrut(10));
		vb.add(instellingenButton);
		vb.add(Box.createVerticalGlue());
		instellingenPanel.add(vb);
		
		
		
		
		setLayout(new BorderLayout());
		Box hb = Box.createHorizontalBox();
		vb = Box.createVerticalBox();
		vb.add(panel);
		vb.add(Box.createVerticalGlue());
		hb.add(vb);
		hb.add(Box.createRigidArea(new Dimension(20,0)));
		hb.add(Box.createHorizontalGlue());
		hb.add(instellingenPanel);
		add(hb);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Hashtable getEditState() {
		Hashtable h = new Hashtable();
		
		h.put("areaSettings", instellingenButton.getInstellingen());
		h.put("calculator", calculatorCB.isSelected());
		
		return h;
	}

	@Override
	public void setEditState(Hashtable h) {
		if (h.containsKey("areaSettings")) {
			Hashtable areaSettings = ((Hashtable) h.get("areaSettings"));
			instellingenButton.setInstellingen(areaSettings);
		}
		if (h.containsKey("calculator")) {
			boolean calculator = ((Boolean) h.get("calculator"));
			calculatorCB.setSelected(calculator);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		
	}

	@Override
	public void zetBreedte(int b) {
		mathcratchPanel.setBounds(0,0,b,mathcratchPanel.getHeight());
	}

	@Override
	public void zetHoogte(int h) {
		mathcratchPanel.setBounds(0,0,mathcratchPanel.getWidth(), h);
	
	}
}
