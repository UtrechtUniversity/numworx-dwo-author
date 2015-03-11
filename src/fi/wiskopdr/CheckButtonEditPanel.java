package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fi.beans.iconan.Iconan;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.tekstobjects.TekstImageVak;

public class CheckButtonEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	private FormuleButton knopImageButton;
	private Dialog imageDialog;
	private Iconan iconman;
	private String knopImageString = "";
	private Image knopImage;
	
	public CheckButtonEditPanel()
	{
		setLayout(null);
		setBounds(0,0,780,480);
		
		knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setBounds(550,50,80,20);
		knopImageButton.addActionListener(this);
		add(knopImageButton);
	}
	
	@Override
	public void setEditState(Hashtable h) {

		String knopImageString = "";
		
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		
		this.knopImageString = knopImageString;
		
		knopImageButton.setPopupButtonImage(knopImage);
	    iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
	    if(knopImageString!=null && !"".equals(knopImageString)) {
	    	knopImage = iconman.getImage(knopImageString);
	    	knopImageButton.setPopupButtonImage(knopImage);
	    }
	    else {
	    	knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
	    }

	}

	@Override
	public Hashtable getEditState() {

		String knopImageString = "";
		
		knopImageString = this.knopImageString;
		
		Hashtable h = new Hashtable();
		h.put("knopImageString", knopImageString);
		
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
        if(imageDialog == null)
        {
        	
        	Frame f = JOptionPane.getFrameForComponent(this);
			imageDialog = new Dialog(f,"title", true);
			imageDialog.setLayout(new BorderLayout());
			iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
            imageDialog.add(iconman);
            imageDialog.pack();
            iconman.addActionListener(this);
        }
        iconman.select(knopImageString);
        imageDialog.setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==knopImageButton)
	    {   editImage();
	            
	    }
	    else if(e.getSource()==iconman)
	    {
	    	String name = e.getActionCommand();
	        if(!"".equals(name))
	        {
	        	knopImageString = name;
	            this.knopImage = iconman.getImage(name);
	            knopImageButton.setPopupButtonImage(knopImage);
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

}
