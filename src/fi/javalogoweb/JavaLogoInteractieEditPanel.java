
package fi.javalogoweb;


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
			
			uitvoerVeldCB = new JCheckBox(JavaLogoWeb.rb.getString("uitvoerVeldCBLabel"));
			uitvoerVeldCB.setBounds(20, 80, 200, 20);
			uitvoerVeldCB.addActionListener(this);
			uitvoerVeldCB.setSelected(true);
			optionsPanel.add(uitvoerVeldCB);
			
			programmaVeldCB = new JCheckBox(JavaLogoWeb.rb.getString("programmaVeldCBLabel"));
			programmaVeldCB.setBounds(20, 110, 200, 20);
			programmaVeldCB.addActionListener(this);
			programmaVeldCB.setSelected(true);
			optionsPanel.add(programmaVeldCB);
			
		}
		
		public Hashtable getEditState() {
			Hashtable h = new Hashtable();
			
			Hashtable state = interactiePanel.getState();
			
			h.put("state", state);
			h.put("uitvoerVeldZichtbaar",  new Boolean(uitvoerVeldCB.isSelected()));
			h.put("programmaVeldZichtbaar",  new Boolean(programmaVeldCB.isSelected()));
			
			return h;
		}
		
		
		public void setEditState(Hashtable h) {
			Hashtable state = null;
			Hashtable antwoordModel = null;
			boolean aanpasbaar = false;
			boolean nakijken = false;
			int scoreMax = 0;
			
			if(h.containsKey("state")) state = (Hashtable) h.get("state");
			if(h.containsKey("uitvoerVeldZichtbaar")) uitvoerVeldCB.setSelected((Boolean)h.get("uitvoerVeldZichtbaar"));
			if(h.containsKey("programmaVeldZichtbaar")) programmaVeldCB.setSelected((Boolean)h.get("programmaVeldZichtbaar"));
			
			interactiePanel.setState(state);
			
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
			
		}

	

}
