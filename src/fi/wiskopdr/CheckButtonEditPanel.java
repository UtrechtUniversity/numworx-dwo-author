package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import fi.beans.iconan.Iconan;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.tekstobjects.TekstImageVak;

public class CheckButtonEditPanel extends JPanel implements InteractieEditPanel, ActionListener, HelpButtonPanelIF {

	private FormuleButton knopImageButton;
	private Dialog imageDialog;
	private Iconan iconman;
	private String knopImageString = "";
	private Icon knopImage;
	
	private JCheckBox kijkNaCB;
	private JCheckBox bewaarCB;
	private JCheckBox rondAfCB;
	private JCheckBox volgendeCB;
	
	private JRadioButton kijkNaEigenVakRB;
	private JRadioButton kijkNaAllesRB;
	private JRadioButton kijkNaXWidgetRB;
	
	private Font ifFont = new Font("SansSerif",Font.PLAIN,12);
	
	
	public CheckButtonEditPanel()
	{
		setLayout(null);
		setBounds(0,0,780,480);
		
		knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setBounds(550,50,80,20);
		knopImageButton.addActionListener(this);
		add(knopImageButton);
		
		kijkNaCB = new JCheckBox(WiskOpdr.rb.getString("CB_kijkNaOptieLabel"));
		kijkNaCB.setBounds(20,50,100,20);
		kijkNaCB.setOpaque(false);
		kijkNaCB.setFont(ifFont);
		kijkNaCB.setSelected(true);
		kijkNaCB.addActionListener(this);
		add(kijkNaCB);
		
		bewaarCB = new JCheckBox(WiskOpdr.rb.getString("CB_bewaarOptieLabel"));
		bewaarCB.setBounds(20,125,100,20);
		bewaarCB.setOpaque(false);
		bewaarCB.setFont(ifFont);
		bewaarCB.addActionListener(this);
		add(bewaarCB);
		
		rondAfCB = new JCheckBox(WiskOpdr.rb.getString("CB_rondAfOptieLabel"));
		rondAfCB.setBounds(20,150,100,20);
		rondAfCB.setOpaque(false);
		rondAfCB.setFont(ifFont);
		rondAfCB.addActionListener(this);
		add(rondAfCB);
		
		volgendeCB = new JCheckBox(WiskOpdr.rb.getString("CB_nextPageOptieLabel"));
		volgendeCB.setBounds(20,175,200,20);
		volgendeCB.setOpaque(false);
		volgendeCB.setFont(ifFont);
		volgendeCB.addActionListener(this);
		add(volgendeCB);
		
		kijkNaEigenVakRB = new JRadioButton(WiskOpdr.rb.getString("CB_eigenVakOptieLabel"));
		kijkNaEigenVakRB.setBounds(130,50,200,20);
		kijkNaEigenVakRB.setOpaque(false);
		kijkNaEigenVakRB.setFont(ifFont);
		kijkNaEigenVakRB.setSelected(true);
		kijkNaEigenVakRB.addActionListener(this);
		add(kijkNaEigenVakRB);
		
		kijkNaAllesRB = new JRadioButton(WiskOpdr.rb.getString("CB_opPaginaOptieLabel"));
		kijkNaAllesRB.setBounds(130,75,200,20);
		kijkNaAllesRB.setOpaque(false);
		kijkNaAllesRB.setFont(ifFont);
		kijkNaAllesRB.addActionListener(this);
		add(kijkNaAllesRB);
		
		kijkNaXWidgetRB = new JRadioButton(WiskOpdr.rb.getString("CB_viaXWidgetOptieLabel"));
		kijkNaXWidgetRB.setBounds(130,100,200,20);
		kijkNaXWidgetRB.setOpaque(false);
		kijkNaXWidgetRB.setFont(ifFont);
		kijkNaXWidgetRB.addActionListener(this);
		add(kijkNaXWidgetRB);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(kijkNaEigenVakRB);
		buttonGroup.add(kijkNaAllesRB);
		buttonGroup.add(kijkNaXWidgetRB);
		
		
	}
	
	@Override
	public void setEditState(Hashtable h) {

		String knopImageString = "";
		boolean nakijkenVak=true;
		boolean nakijken = true;
		boolean nakijkenPagina=false;
		boolean nakijkenXWidget=false;
		boolean actieBewaren=false;
		boolean actieAfronden=false;
		boolean actionNextPage=false;
		
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		if(h.containsKey("nakijkenPagina")) nakijkenPagina = ((Boolean)h.get("nakijkenPagina")).booleanValue();
		if(h.containsKey("nakijkenVak")) nakijkenVak = ((Boolean)h.get("nakijkenVak")).booleanValue();
		if(h.containsKey("nakijken")) nakijken = ((Boolean)h.get("nakijken")).booleanValue();
		if(h.containsKey("nakijkenXWidget")) nakijkenXWidget = ((Boolean)h.get("nakijkenXWidget")).booleanValue();
		if(h.containsKey("actieBewaren")) actieBewaren = ((Boolean)h.get("actieBewaren")).booleanValue();
		if(h.containsKey("actieAfronden")) actieAfronden = ((Boolean)h.get("actieAfronden")).booleanValue();
		if(h.containsKey("actionNextPage")) actionNextPage = ((Boolean)h.get("actionNextPage")).booleanValue();
		
		this.knopImageString = knopImageString;
		kijkNaCB.setSelected(nakijken);
		kijkNaEigenVakRB.setSelected(nakijkenVak); kijkNaEigenVakRB.setEnabled(nakijken);
		kijkNaAllesRB.setSelected(nakijkenPagina); kijkNaAllesRB.setEnabled(nakijken);
		kijkNaXWidgetRB.setSelected(nakijkenXWidget); kijkNaXWidgetRB.setEnabled(nakijken);
		bewaarCB.setSelected(actieBewaren);
		rondAfCB.setSelected(actieAfronden);
		volgendeCB.setSelected(actionNextPage);
		
		knopImageButton.setPopupButtonIcon(knopImage);
	    iconman = new Iconan(WiskOpdr.applet, (Component)this, TekstImageVak.getImageMap(), TekstImageVak.getImageCache());
	    if(knopImageString!=null && !"".equals(knopImageString)) {
	    	knopImage = iconman.getIcon(knopImageString);
	    	knopImageButton.setPopupButtonIcon(knopImage);
	    }
	    else {
	    	knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
	    }

	}

	@Override
	public Hashtable getEditState() {

		String knopImageString = "";
		boolean nakijkenVak=true;
		boolean nakijkenPagina=false;
		boolean nakijkenXWidget=false;
		boolean actieBewaren=false;
		boolean actieAfronden=false;
		boolean nakijken = true;
		boolean actionNextPage = false;
		
		knopImageString = this.knopImageString;
		nakijken = kijkNaCB.isSelected();
		nakijkenVak = kijkNaEigenVakRB.isSelected();
		nakijkenPagina = kijkNaAllesRB.isSelected();
		nakijkenXWidget = kijkNaXWidgetRB.isSelected();
		actieBewaren = bewaarCB.isSelected();
		actieAfronden = rondAfCB.isSelected();
		actionNextPage = volgendeCB.isSelected();
		
		Hashtable h = new Hashtable();
		h.put("knopImageString", knopImageString);
		h.put("nakijken", new Boolean(nakijken));
		h.put("nakijkenPagina", new Boolean(nakijkenPagina));
		h.put("nakijkenVak", new Boolean(nakijkenVak));
		h.put("nakijkenXWidget", new Boolean(nakijkenXWidget));
		h.put("actieBewaren", new Boolean(actieBewaren));
		h.put("actieAfronden", new Boolean(actieAfronden));
		h.put("actionNextPage", new Boolean(actionNextPage));
		
		return h;
	}

	@Override
	public void zetBreedte(int b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void zetHoogte(int h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}
	
	public void editImage() {
		if(iconman==null)
			iconman = new Iconan(WiskOpdr.applet, this, TekstImageVak.getImageMap(), TekstImageVak.getImageCache());
		iconman.editImage(knopImageString, this, this);
		
//        if(imageDialog == null)
//        {
//        	
//        	Frame f = JOptionPane.getFrameForComponent(this);
//			imageDialog = new Dialog(f,"title", true);
//			imageDialog.setLayout(new BorderLayout());
//			iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
//            imageDialog.add(iconman);
//            imageDialog.pack();
//            iconman.addActionListener(this);
//        }
//        iconman.select(knopImageString);
//        imageDialog.setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == kijkNaCB) {
			boolean selected = kijkNaCB.isSelected();
			kijkNaEigenVakRB.setEnabled(selected);
			kijkNaAllesRB.setEnabled(selected);
			kijkNaXWidgetRB.setEnabled(selected);
		} else
		if(e.getSource()==knopImageButton)
	    {   editImage();
	            
	    }
	    else if(e.getSource()==iconman)
	    {
	    	String name = e.getActionCommand();
	        if(!"".equals(name))
	        {
	        	knopImageString = name;
	            this.knopImage = iconman.getIcon(name);
	            knopImageButton.setPopupButtonIcon(knopImage);
	            int imWidth = iconman.getWidth(knopImageString);
				int imHeight = iconman.getHeight(knopImageString);
				if(imWidth == -1) imWidth = 20;
				if(imHeight == -1) imHeight = 20;
				knopImageButton.setSize(imWidth,imHeight);
	            repaint();
	        }
	    }

	    if(imageDialog!=null)
	        imageDialog.setVisible(false);
		
	}

	@Override
	public void showHelpButtons(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String geefHelpURL() {
		// TODO Auto-generated method stub
		return null;
	}

}
