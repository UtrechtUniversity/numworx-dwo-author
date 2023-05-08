
package fi.javalogoweb3d;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import fi.beans.wiskopdrbeans.InteractieEditPanel;


public class JavaLogoInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener 
{

		private JavaLogoInteractiePanel interactiePanel;
		private JPanel optionsPanel;
		
		private int defaultHeight = 500;
		private int defaultIpWidth = 700;
		private int defaultOpWidth = 260;
		
		private JCheckBox uitvoerVeldCB;
		private JCheckBox transparantCB;
		private JCheckBox draadFiguurCB;
		private JCheckBox zoomCB;
		private JCheckBox programmaVeldCB;
		private JCheckBox deeltakenCB;
		private JCheckBox whileLoopCB;
		private JCheckBox keuzeCommandCB;
		//private JCheckBox printCommandsCB;
		private JCheckBox tekenCommandsCB;
		private JCheckBox traceCB;
		private JCheckBox codeIOCB;
		
		
		
		//// Einde voorbeeldcode
		
		
		public JavaLogoInteractieEditPanel() 
		{
			setLayout(null);
			interactiePanel = new JavaLogoInteractiePanel();
			interactiePanel.setBounds(0,0,defaultIpWidth,defaultHeight-50);
System.out.println("jliep " + defaultIpWidth);			
			add(interactiePanel);
			
			optionsPanel = new JPanel();
			optionsPanel.setLayout(null);
			optionsPanel.setBounds(defaultIpWidth+30,20,defaultOpWidth,defaultHeight);
			add(optionsPanel);
			
			int currentX = 20;
			int currentY = 50;
			
			uitvoerVeldCB = new JCheckBox(JavaLogoWeb3d.rb.getString("uitvoerVeldCBLabel"));
			uitvoerVeldCB.setBounds(currentX, currentY, 200, 20);
			uitvoerVeldCB.addActionListener(this);
			uitvoerVeldCB.setSelected(true);
			optionsPanel.add(uitvoerVeldCB);
			currentY += 30; 

			transparantCB = new JCheckBox(JavaLogoWeb3d.rb.getString("transparantCBLabel"));
			transparantCB.setBounds(currentX, currentY, 200, 20);
			transparantCB.addActionListener(this);
			transparantCB.setSelected(true);
			optionsPanel.add(transparantCB);
			currentY += 30; 

			draadFiguurCB = new JCheckBox(JavaLogoWeb3d.rb.getString("draadFiguurCBLabel"));
			draadFiguurCB.setBounds(currentX, currentY, 200, 20);
			draadFiguurCB.addActionListener(this);
			draadFiguurCB.setSelected(true);
			optionsPanel.add(draadFiguurCB);
			currentY += 30; 

			zoomCB = new JCheckBox(JavaLogoWeb3d.rb.getString("zoomCBLabel"));
			zoomCB.setBounds(currentX, currentY, 200, 20);
			zoomCB.addActionListener(this);
			zoomCB.setSelected(true);
			optionsPanel.add(zoomCB);
			currentY += 40; 

			programmaVeldCB = new JCheckBox(JavaLogoWeb3d.rb.getString("programmaVeldCBLabel"));
			programmaVeldCB.setBounds(currentX, currentY, 200, 20);
			programmaVeldCB.addActionListener(this);
			programmaVeldCB.setSelected(true);
			optionsPanel.add(programmaVeldCB);
			currentY += 30;
			
			deeltakenCB = new JCheckBox(JavaLogoWeb3d.rb.getString("deeltakenCBLabel"));
			deeltakenCB.setBounds(currentX, currentY, 200, 20);
			deeltakenCB.addActionListener(this);
			deeltakenCB.setSelected(true);
			optionsPanel.add(deeltakenCB);
			currentY += 30;
						
			whileLoopCB = new JCheckBox(JavaLogoWeb3d.rb.getString("whileLoopCBLabel"));
			whileLoopCB.setBounds(currentX, currentY, 200, 20);
			whileLoopCB.addActionListener(this);
			whileLoopCB.setSelected(true);
			optionsPanel.add(whileLoopCB);
			currentY += 30;
			
			keuzeCommandCB = new JCheckBox(JavaLogoWeb3d.rb.getString("keuzeComandCBLabel"));
			keuzeCommandCB.setBounds(currentX, currentY, 200, 20);
			keuzeCommandCB.addActionListener(this);
			keuzeCommandCB.setSelected(true);
			optionsPanel.add(keuzeCommandCB);
			currentY += 30;
			
			//printCommandsCB = new JCheckBox(JavaLogoWeb3d.rb.getString("printCommandsCBLabel"));
			//printCommandsCB.setBounds(20, 230, 200, 20);
			//printCommandsCB.addActionListener(this);
			//printCommandsCB.setSelected(true);
			//optionsPanel.add(printCommandsCB);
			
			tekenCommandsCB = new JCheckBox(JavaLogoWeb3d.rb.getString("tekenCommandsCBLabel"));
			tekenCommandsCB.setBounds(currentX, currentY, 200, 20);
			tekenCommandsCB.addActionListener(this);
			tekenCommandsCB.setSelected(true);
			optionsPanel.add(tekenCommandsCB);
			currentY += 30;
			
			traceCB = new JCheckBox(JavaLogoWeb3d.rb.getString("traceCBLabel"));
			traceCB.setBounds(currentX, currentY, 200, 20);
			traceCB.addActionListener(this);
			traceCB.setSelected(true);
			optionsPanel.add(traceCB);
			currentY += 30;
			
			codeIOCB = new JCheckBox(JavaLogoWeb3d.rb.getString("codeIOCBLabel"));
			codeIOCB.setBounds(currentX, currentY, 200, 20);
			codeIOCB.addActionListener(this);
			codeIOCB.setSelected(true);
			optionsPanel.add(codeIOCB);
			currentY += 30;
			
			
		}
		
		public Hashtable getEditState() 
		{
			Hashtable h = new Hashtable();
			
			Hashtable state = interactiePanel.getState();
			
			h.put("state", state);
			h.put("uitvoerVeldZichtbaar",  new Boolean(uitvoerVeldCB.isSelected()));
			h.put("transparantOptie",  new Boolean(transparantCB.isSelected()));
			h.put("draadFiguurOptie",  new Boolean(draadFiguurCB.isSelected()));
			h.put("zoomOptie",  new Boolean(zoomCB.isSelected()));
			h.put("programmaVeldZichtbaar",  new Boolean(programmaVeldCB.isSelected()));
			h.put("deeltakenZichtbaar",  new Boolean(deeltakenCB.isSelected()));
			h.put("whileLoopZichtbaar",  new Boolean(whileLoopCB.isSelected()));
			h.put("keuzeCommandZichtbaar",  new Boolean(keuzeCommandCB.isSelected()));
			//h.put("printCommandsZichtbaar",  new Boolean(printCommandsCB.isSelected()));
			h.put("tekenCommandsZichtbaar",  new Boolean(tekenCommandsCB.isSelected()));
			h.put("traceZichtbaar",  new Boolean(traceCB.isSelected()));
			h.put("codeIOZichtbaar",  new Boolean(codeIOCB.isSelected()));
			
			return h;
		}
		
		
		public void setEditState(Hashtable h) 
		{
			Hashtable state = null;
						
			if(h.containsKey("state")) state = (Hashtable) h.get("state");
			if(h.containsKey("uitvoerVeldZichtbaar")) uitvoerVeldCB.setSelected((Boolean)h.get("uitvoerVeldZichtbaar"));
			if(h.containsKey("transparantOptie")) 
				transparantCB.setSelected((Boolean)h.get("transparantOptie"));
			if(h.containsKey("draadFiguurOptie")) 
				draadFiguurCB.setSelected((Boolean)h.get("draadFiguurOptie"));
			if(h.containsKey("zoomOptie")) 
				zoomCB.setSelected((Boolean)h.get("zoomOptie"));
			if(h.containsKey("programmaVeldZichtbaar")) programmaVeldCB.setSelected((Boolean)h.get("programmaVeldZichtbaar"));
			if(h.containsKey("deeltakenZichtbaar")) deeltakenCB.setSelected((Boolean)h.get("deeltakenZichtbaar"));
			if(h.containsKey("whileLoopZichtbaar")) whileLoopCB.setSelected((Boolean)h.get("whileLoopZichtbaar"));
			if(h.containsKey("keuzeCommandZichtbaar")) keuzeCommandCB.setSelected((Boolean)h.get("keuzeCommandZichtbaar"));
			//if(h.containsKey("printCommandsZichtbaar")) printCommandsCB.setSelected((Boolean)h.get("printCommandsZichtbaar"));
			if(h.containsKey("tekenCommandsZichtbaar")) tekenCommandsCB.setSelected((Boolean)h.get("tekenCommandsZichtbaar"));
			if(h.containsKey("traceZichtbaar")) traceCB.setSelected((Boolean)h.get("traceZichtbaar"));
			if(h.containsKey("codeIOZichtbaar")) codeIOCB.setSelected((Boolean)h.get("codeIOZichtbaar"));
			
			
			interactiePanel.zetUitvoerVeldZichtbaar(uitvoerVeldCB.isSelected());
			interactiePanel.zetTransparantOptie(transparantCB.isSelected());
			interactiePanel.zetDraadFiguurOptie(draadFiguurCB.isSelected());
			interactiePanel.zetZoomOptie(zoomCB.isSelected());
			interactiePanel.zetProgrammaVeldZichtbaar(programmaVeldCB.isSelected());
			interactiePanel.zetDeeltaken(deeltakenCB.isSelected());
			interactiePanel.zetWhileLoopZichtbaar(whileLoopCB.isSelected());
			interactiePanel.zetKeuzeCommandZichtbaar(keuzeCommandCB.isSelected());
			//interactiePanel.zetPrintCommandsZichtbaar(printCommandsCB.isSelected());
			interactiePanel.zetTekenCommandsZichtbaar(tekenCommandsCB.isSelected());
			interactiePanel.zetTraceZichtbaar(traceCB.isSelected());
			interactiePanel.zetCodeIOZichtbaar(codeIOCB.isSelected());
			interactiePanel.setState(state);
			
			interactiePanel.setBounds(interactiePanel.getBounds());
			
			
		}
		
		public void setBounds(int x, int y, int b, int h) 
		{
			super.setBounds(x,y,b,h);
			optionsPanel.setBounds(b-defaultOpWidth-10, 20, defaultOpWidth, defaultHeight);
			//interactiePanel.setBounds(0,0,b-defaultOpWidth, h);
		}
		
		
		public void zetBreedte(int b) 
		{
			interactiePanel.setSize(b,interactiePanel.getHeight());
			
		}
		
		public void zetHoogte(int h) 
		{
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
		
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource()==uitvoerVeldCB) {
				interactiePanel.zetUitvoerVeldZichtbaar(uitvoerVeldCB.isSelected());
			}
			if(e.getSource()==transparantCB) {
				interactiePanel.zetTransparantOptie(transparantCB.isSelected());
			}
			if(e.getSource()==draadFiguurCB) {
				interactiePanel.zetDraadFiguurOptie(draadFiguurCB.isSelected());
			}
			if(e.getSource()==zoomCB) {
				interactiePanel.zetZoomOptie(zoomCB.isSelected());
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
			//if(e.getSource()==printCommandsCB) {
			//	interactiePanel.zetPrintCommandsZichtbaar(printCommandsCB.isSelected());
			//}
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
