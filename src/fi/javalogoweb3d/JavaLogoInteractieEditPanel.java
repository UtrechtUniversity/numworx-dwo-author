
package fi.javalogoweb3d;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import fi.beans.wiskopdrbeans.InteractieEditPanel;


	

public class JavaLogoInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

		private JavaLogoInteractiePanel interactiePanel;
		private JPanel optionsPanel;
		
		private int defaultHeight = 500;
		private int defaultIpWidth = 500;
		private int defaultOpWidth = 260;
		
		private JCheckBox uitvoerVeldCB;
		private JCheckBox programmaVeldCB;
		private JCheckBox deeltakenCB;
		private JCheckBox whileLoopCB;
		private JCheckBox keuzeCommandCB;
		private JCheckBox printCommandsCB;
		private JCheckBox tekenCommandsCB;
		private JCheckBox traceCB;
		private JCheckBox codeIOCB;
		
		
		
		//// Einde voorbeeldcode
		
		
		public JavaLogoInteractieEditPanel() {
			setLayout(null);
			interactiePanel = new JavaLogoInteractiePanel();
			interactiePanel.setBounds(0,0,defaultIpWidth,defaultHeight-50);
			add(interactiePanel);
			
			optionsPanel = new JPanel();
			optionsPanel.setLayout(null);
			optionsPanel.setBounds(defaultIpWidth+30,20,defaultOpWidth,defaultHeight);
			add(optionsPanel);
			
			uitvoerVeldCB = new JCheckBox(JavaLogoWeb3d.rb.getString("uitvoerVeldCBLabel"));
			uitvoerVeldCB.setBounds(20, 80, 200, 20);
			uitvoerVeldCB.addActionListener(this);
			uitvoerVeldCB.setSelected(true);
			optionsPanel.add(uitvoerVeldCB);
			
			programmaVeldCB = new JCheckBox(JavaLogoWeb3d.rb.getString("programmaVeldCBLabel"));
			programmaVeldCB.setBounds(20, 110, 200, 20);
			programmaVeldCB.addActionListener(this);
			programmaVeldCB.setSelected(true);
			optionsPanel.add(programmaVeldCB);
			
			deeltakenCB = new JCheckBox(JavaLogoWeb3d.rb.getString("deeltakenCBLabel"));
			deeltakenCB.setBounds(20, 140, 200, 20);
			deeltakenCB.addActionListener(this);
			deeltakenCB.setSelected(true);
			optionsPanel.add(deeltakenCB);
						
			whileLoopCB = new JCheckBox(JavaLogoWeb3d.rb.getString("whileLoopCBLabel"));
			whileLoopCB.setBounds(20, 170, 200, 20);
			whileLoopCB.addActionListener(this);
			whileLoopCB.setSelected(true);
			optionsPanel.add(whileLoopCB);
			
			keuzeCommandCB = new JCheckBox(JavaLogoWeb3d.rb.getString("keuzeComandCBLabel"));
			keuzeCommandCB.setBounds(20, 200, 200, 20);
			keuzeCommandCB.addActionListener(this);
			keuzeCommandCB.setSelected(true);
			optionsPanel.add(keuzeCommandCB);
			
			printCommandsCB = new JCheckBox(JavaLogoWeb3d.rb.getString("printCommandsCBLabel"));
			printCommandsCB.setBounds(20, 230, 200, 20);
			printCommandsCB.addActionListener(this);
			printCommandsCB.setSelected(true);
			optionsPanel.add(printCommandsCB);
			
			tekenCommandsCB = new JCheckBox(JavaLogoWeb3d.rb.getString("tekenCommandsCBLabel"));
			tekenCommandsCB.setBounds(20, 260, 200, 20);
			tekenCommandsCB.addActionListener(this);
			tekenCommandsCB.setSelected(true);
			optionsPanel.add(tekenCommandsCB);
			
			traceCB = new JCheckBox(JavaLogoWeb3d.rb.getString("traceCBLabel"));
			traceCB.setBounds(20, 290, 200, 20);
			traceCB.addActionListener(this);
			traceCB.setSelected(true);
			optionsPanel.add(traceCB);
			
			codeIOCB = new JCheckBox(JavaLogoWeb3d.rb.getString("codeIOCBLabel"));
			codeIOCB.setBounds(20, 320, 200, 20);
			codeIOCB.addActionListener(this);
			codeIOCB.setSelected(true);
			optionsPanel.add(codeIOCB);
			
			
			
		}
		
		public Hashtable getEditState() {
			Hashtable h = new Hashtable();
			
			Hashtable state = interactiePanel.getState();
			
			h.put("state", state);
			h.put("uitvoerVeldZichtbaar",  new Boolean(uitvoerVeldCB.isSelected()));
			h.put("programmaVeldZichtbaar",  new Boolean(programmaVeldCB.isSelected()));
			h.put("deeltakenZichtbaar",  new Boolean(deeltakenCB.isSelected()));
			h.put("whileLoopZichtbaar",  new Boolean(whileLoopCB.isSelected()));
			h.put("keuzeCommandZichtbaar",  new Boolean(keuzeCommandCB.isSelected()));
			h.put("printCommandsZichtbaar",  new Boolean(printCommandsCB.isSelected()));
			h.put("tekenCommandsZichtbaar",  new Boolean(tekenCommandsCB.isSelected()));
			h.put("traceZichtbaar",  new Boolean(traceCB.isSelected()));
			h.put("codeIOZichtbaar",  new Boolean(codeIOCB.isSelected()));
			
			return h;
		}
		
		
		public void setEditState(Hashtable h) {
			Hashtable state = null;
						
			if(h.containsKey("state")) state = (Hashtable) h.get("state");
			if(h.containsKey("uitvoerVeldZichtbaar")) uitvoerVeldCB.setSelected((Boolean)h.get("uitvoerVeldZichtbaar"));
			if(h.containsKey("programmaVeldZichtbaar")) programmaVeldCB.setSelected((Boolean)h.get("programmaVeldZichtbaar"));
			if(h.containsKey("deeltakenZichtbaar")) deeltakenCB.setSelected((Boolean)h.get("deeltakenZichtbaar"));
			if(h.containsKey("whileLoopZichtbaar")) whileLoopCB.setSelected((Boolean)h.get("whileLoopZichtbaar"));
			if(h.containsKey("keuzeCommandZichtbaar")) keuzeCommandCB.setSelected((Boolean)h.get("keuzeCommandZichtbaar"));
			if(h.containsKey("printCommandsZichtbaar")) printCommandsCB.setSelected((Boolean)h.get("printCommandsZichtbaar"));
			if(h.containsKey("tekenCommandsZichtbaar")) tekenCommandsCB.setSelected((Boolean)h.get("tekenCommandsZichtbaar"));
			if(h.containsKey("traceZichtbaar")) traceCB.setSelected((Boolean)h.get("traceZichtbaar"));
			if(h.containsKey("codeIOZichtbaar")) codeIOCB.setSelected((Boolean)h.get("codeIOZichtbaar"));
			
			
			interactiePanel.zetUitvoerVeldZichtbaar(uitvoerVeldCB.isSelected());
			interactiePanel.zetProgrammaVeldZichtbaar(programmaVeldCB.isSelected());
			interactiePanel.zetDeeltaken(deeltakenCB.isSelected());
			interactiePanel.zetWhileLoopZichtbaar(whileLoopCB.isSelected());
			interactiePanel.zetKeuzeCommandZichtbaar(keuzeCommandCB.isSelected());
			interactiePanel.zetPrintCommandsZichtbaar(printCommandsCB.isSelected());
			interactiePanel.zetTekenCommandsZichtbaar(tekenCommandsCB.isSelected());
			interactiePanel.zetTraceZichtbaar(traceCB.isSelected());
			interactiePanel.zetCodeIOZichtbaar(codeIOCB.isSelected());
			interactiePanel.setState(state);
			
			interactiePanel.setBounds(interactiePanel.getBounds());
			
			
		}
		
		public void setBounds(int x, int y, int b, int h) {
			super.setBounds(x,y,b,h);
			optionsPanel.setBounds(b-defaultOpWidth-10, 20, defaultOpWidth, defaultHeight);
		}
		
		
		public void zetBreedte(int b) {
			interactiePanel.setSize(b,interactiePanel.getHeight());
			
		}
		
		public void zetHoogte(int h) {
			interactiePanel.setSize(interactiePanel.getWidth(), h);
			
		}
		
		public void wis() {
				
		}
		
		public void zetMode(int mode) {
				
		}
		
		public void stop() {
				
		}
		
		public void start() {
				
		}
		
		public void addActionListener(ActionListener al) {
				
		}
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==uitvoerVeldCB) {
				interactiePanel.zetUitvoerVeldZichtbaar(uitvoerVeldCB.isSelected());
			}
			if(e.getSource()==programmaVeldCB) {
				interactiePanel.zetProgrammaVeldZichtbaar(programmaVeldCB.isSelected());
			}
			if(e.getSource()==deeltakenCB) {
				interactiePanel.zetDeeltaken(deeltakenCB.isSelected());
			}
			if(e.getSource()==whileLoopCB) {
				interactiePanel.zetWhileLoopZichtbaar(whileLoopCB.isSelected());
			}
			if(e.getSource()==keuzeCommandCB) {
				interactiePanel.zetKeuzeCommandZichtbaar(keuzeCommandCB.isSelected());
			}
			if(e.getSource()==printCommandsCB) {
				interactiePanel.zetPrintCommandsZichtbaar(printCommandsCB.isSelected());
			}
			if(e.getSource()==tekenCommandsCB) {
				interactiePanel.zetTekenCommandsZichtbaar(tekenCommandsCB.isSelected());
			}
			if(e.getSource()==traceCB) {
				interactiePanel.zetTraceZichtbaar(traceCB.isSelected());
			}
			if(e.getSource()==codeIOCB) {
				interactiePanel.zetCodeIOZichtbaar(codeIOCB.isSelected());
			}
			interactiePanel.setBounds(interactiePanel.getBounds());
		}

	

}
