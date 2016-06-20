package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StyleManager extends JPanel implements ActionListener {

	private TekstVakEditPanel tvep;
	private JComboBox styleChoice;
	
	private JLabel titelLabel;
	
	private JButton addStyleButton;
	private JButton removeStyleButton;
	private JButton editStyleButton;
	private JButton saveStyleButton;
	private JButton cancelStyleButton;
	private JButton closeButton;
	
	private Font ifFont = new Font("SansSerif",Font.PLAIN,12);
	private Font titelFont = new Font("SansSerif", Font.BOLD, 14);
	
	public StyleManager(TekstVakEditPanel tvep, JComboBox styleChoice){
		this.tvep = tvep;
		this.styleChoice = styleChoice;
		
		setLayout(null);
		setOpaque(true);
		
		titelLabel = new JLabel("Manage styles");
		titelLabel.setBounds(10,5,250,25);
		titelLabel.setHorizontalAlignment(JLabel.CENTER);
		titelLabel.setFont(titelFont);
		add(titelLabel);
		
		addStyleButton = new JButton("Save settings as new style");
		addStyleButton.addActionListener(this);
		addStyleButton.setFont(ifFont);
		addStyleButton.setBounds(10,65,250,20);
		addStyleButton.setVisible(true);
		add(addStyleButton);
		
		saveStyleButton = new JButton("Save Style");
		saveStyleButton.addActionListener(this);
		saveStyleButton.setFont(ifFont);
		saveStyleButton.setBounds(10,90,120,20);
		saveStyleButton.setVisible(true);
		add(saveStyleButton);
		
		removeStyleButton = new JButton("Remove Style");
		removeStyleButton.addActionListener(this);
		removeStyleButton.setFont(ifFont);
		removeStyleButton.setBounds(140,90,120,20);
		removeStyleButton.setVisible(true);
		add(removeStyleButton);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		//closeButton.setFont(ifFont);
		closeButton.setBounds(75,120,120,20);
		closeButton.setVisible(true);
		add(closeButton);
		
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		 if(e.getSource().equals(editStyleButton))
			{	tvep.enableStyleSettings(true);
			}
		   if(e.getSource().equals(saveStyleButton))
			{	saveStyleAction();
		    }
		    if(e.getSource().equals(addStyleButton))
			{	addStyleAction();
		    }
		    if(e.getSource().equals(removeStyleButton))
			{	removeStyleAction();
		    }
		    if(e.getSource().equals(closeButton))
			{	tvep.enableStyleSettings(styleChoice.getSelectedIndex()==0);
		    	produceAction("close");
			}
		    if(e.getSource().equals(styleChoice))
			{	removeStyleButton.setEnabled(styleChoice.getSelectedIndex()>0);
				saveStyleButton.setEnabled(styleChoice.getSelectedIndex()>0);	
			}
		}
		
		public void setVisible(boolean b){
			super.setVisible(b);
			if(b) {
				removeStyleButton.setEnabled(styleChoice.getSelectedIndex()>0);
				saveStyleButton.setEnabled(styleChoice.getSelectedIndex()>0);
				styleChoice.setBounds(10,35,250,20);
				add(styleChoice);
			}
		}
		
		private void saveStyleAction() {
			Hashtable h = tvep.getStyleSettings();
			TekstVakPanel.styles.put((String)styleChoice.getSelectedItem(),h);
		}
		
		private void removeStyleAction() {
			String item = (String)styleChoice.getSelectedItem();
			TekstVakPanel.styles.remove(item);
			styleChoice.removeItem(item);
			styleChoice.setSelectedIndex(0);
		}
		
		private void addStyleAction() {
			String s = (String)JOptionPane.showInputDialog(null,null,"Give style name",JOptionPane.PLAIN_MESSAGE);
			if(s!=null && !"".equals(s))
			{	styleChoice.addItem(s);
				styleChoice.setSelectedItem(s);
				saveStyleAction();
			}
			else
			{	styleChoice.setSelectedItem(0);
			}
		}
		
		//ActionProducer
		private ActionListener actionListener = null;
		
		public void addActionListener(ActionListener l) 
	 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
	 	}
	 	
	 	public void removeActionListener(ActionListener l)
	 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
	 	}	
	 	
	 	public void produceAction(String command)
	 	{	if (actionListener != null)
	 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
	 		}
	 	}
	 	//end ActionProducer
		
		
		
	
	
}
