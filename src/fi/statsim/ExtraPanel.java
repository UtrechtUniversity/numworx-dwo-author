package fi.statsim;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
//import fi.statistiek.Statistiek;

public class ExtraPanel extends JPanel {	
		
	StatSimInteractiePanel ssip;
	ExtraPanel1 extraPanel1;
	ExtraPanel2 extraPanel2;
	
	public ExtraPanel(StatSimInteractiePanel ssip) {
		setLayout(null);
		this.ssip=ssip;
		setSize(500,360);
		
		extraPanel1 = new ExtraPanel1(ssip);
		extraPanel1.setVisible(false);
		extraPanel1.setBounds(0,0,getWidth(),getHeight());
		add(extraPanel1);
		
		extraPanel2 = new ExtraPanel2(ssip);
		extraPanel2.setVisible(false);
		extraPanel2.setBounds(0,0,getWidth(),getHeight());
		add(extraPanel2);
	}
	
	public void setPanelNr(int nr) {
		extraPanel1.setVisible(false);
		extraPanel2.setVisible(false);
		if(nr==1)
			extraPanel1.setVisible(true);
		if(nr==2)
			extraPanel2.setVisible(true);
	}
	
	public void setZichtbaar() {
		
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if(extraPanel1!=null)
			extraPanel1.setSize(getWidth(),getHeight());
		if(extraPanel2!=null)
			extraPanel2.setSize(getWidth(),getHeight());
	}
}