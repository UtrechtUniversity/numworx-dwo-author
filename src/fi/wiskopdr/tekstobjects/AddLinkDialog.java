package fi.wiskopdr.tekstobjects;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URI;
import java.util.Arrays;

import javax.swing.*;

import fi.wiskopdr.WiskOpdr;


public class AddLinkDialog extends Dialog implements ActionListener,
        WindowListener {

	private String linkTekst="link";
	//private String url="http://";
    private int height = 400;
    private int width = 400;
    private LinkType embedded = LinkType.FALSE;
     	
    private TextField linkTekstField;
    private TextField urlField;
    private TextField widthField;
    private TextField heightField;
    
    private Button okButton;
    private Button cancelButton;
    
    private Font font = new Font("SansSerif",Font.PLAIN,12);
    
    boolean confirmed;
    
    boolean voorwaardelijk = false;
    private String[] urls = null;
    private int[] grensScores = null;
	
    private JCheckBox voorwaardelijkBox;
    private JComboBox<LinkType> embeddedCB;
    private VoorwaardelijkeLinkButton voorwaardelijkButton;

    
/*
    public AddLinkDialog(Component owner, String windowTitle, String linkTekst, String url, int width, int height) {
        super((owner instanceof Frame) ? (Frame) owner : new Frame(),
                windowTitle, true);
        this.setLayout(null);
        this.setBackground(new Color(230,230,230));
        this.linkTekst = linkTekst;
        this.url = url;
        this.height = height;
        this.width = width;
    
        confirmed = false;
        
    //  /*
        for(int i = 0; i < 10; i++)
    	{	urls[i] = "http://";
    	}
    	//Hier * weer invoegen

        Label l;
        FontMetrics fm;

        /* schoolName label // Hier * weer invoegen
        l = new Label(WiskOpdr.rb.getString("LEP_linkTekst"));//"Tekst"
        l.setForeground(Color.black);
        l.setFont(WiskOpdr.tekstFont);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 30);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* schoolName field // Hier * weer invoegen
        linkTekstField = new TextField(linkTekst);
        linkTekstField.setBounds(150, 28, 300, 20);
        this.add(linkTekstField);
        
        /* schoolName label // Hier * weer invoegen
        l = new Label(WiskOpdr.rb.getString("LEP_url"));//"URL"
        l.setForeground(Color.black);
        l.setFont(WiskOpdr.tekstFont);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 60);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* schoolName field // Hier * weer invoegen
        urlField = new TextField(url);
        urlField.setBounds(150, 58, 300, 20);
        this.add(urlField);
        
        /* schoolLogin label // Hier * weer invoegen
        l = new Label(WiskOpdr.rb.getString("LEP_vensterBreedte"));//"breedte van het window"
        l.setForeground(Color.black);
        l.setFont(WiskOpdr.tekstFont);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 90);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* schoolLogin field // Hier * weer invoegen
        widthField = new TextField(""+width);
        widthField.setBounds(150, 88, 150, 20);
        this.add(widthField);
        
        /* studentPasswd label // Hier * weer invoegen
        l = new Label(WiskOpdr.rb.getString("LEP_vensterHoogte"));//"Hoogte van het window"
        l.setForeground(Color.black);
        l.setFont(WiskOpdr.tekstFont);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 120);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* studentPasswd field // Hier * weer invoegen
        heightField = new TextField(""+height);
        heightField.setBounds(150, 118, 150, 20);
        this.add(heightField);
        
        voorwaardelijkBox = new JCheckBox("Voorwaardelijke link (work in progress)");//nog aanpassen!
        voorwaardelijkBox.setBounds(10, 150, 300, 20);
        voorwaardelijkBox.setSelected(voorwaardelijk);
        this.add(voorwaardelijkBox);
        voorwaardelijkBox.addActionListener(this);
        
        voorwaardelijkButton = new VoorwaardelijkeLinkButton();
        voorwaardelijkButton.setBounds(310, 150, 120, 20);
        voorwaardelijkButton.setVisible(false);
        this.add(voorwaardelijkButton);
        
        
        
        
        //Hierdoor merkt niemand iets van aanbouw voorwaardelijke link 
        //Als de voorwaardelijke checkbox verdwijnt: stuk na else behouden.
        //voorwaardelijkBox.setVisible(false);
        if(voorwaardelijkBox.isVisible())
        {	this.setSize(460, 230);

	        /* Register button // Hier * weer invoegen
	        okButton = new Button("OK");
	        okButton.setBounds(50,190,80,20);
	        okButton.addActionListener(this);
	        add(okButton);
	        
	        /* Reset button // Hier * weer invoegen
	        cancelButton = new Button("Cancel");
	        cancelButton.setBounds(150,190,80,20);
	        cancelButton.addActionListener(this);
	        add(cancelButton);
        }
        else
        {	this.setSize(460, 200);

	        /* Register button // Hier * weer invoegen
	        okButton = new Button("OK");
	        okButton.setBounds(50,160,80,20);
	        okButton.addActionListener(this);
	        add(okButton);
	        
	        /* Reset button // Hier * weer invoegen
	        cancelButton = new Button("Cancel");
	        cancelButton.setBounds(150,160,80,20);
	        cancelButton.addActionListener(this);
	        add(cancelButton);
	    }
        	
        
        this.addWindowListener(this);
    }
    
*/
    
    public static Link addLink() {
        return addLink(null);
    }

    /**
     * @return fi.dwo.client.domain.Sco
     */
   /*
    public static Link addLink(Component owner) {
        AddLinkDialog asd = new AddLinkDialog(owner, "Link gegevens", "link", "http://", 400, 400);
        asd.show();
        if (asd.isConfirmed()) {
            Link link = new Link(asd.getLinkTekst(), asd.getUrl(), asd.getWidth(), asd.getHeight());
            if(link == null) { //something went wrong, reshow the dialog
                link = addLink(owner);
            }
            return link;
        } else { //action canceled
            return null;
        }
    }
    */
    
    //Nieuwe versie 17-4
    public static Link addLink(Component owner) {
        String[] httpString = new String[] {"http://", "http://","http://", "http://",
        		"http://", "http://","http://", "http://","http://", "http://"};
    	AddLinkDialog asd = new AddLinkDialog(owner, WiskOpdr.rb.getString("linkDialogLinkGegevens"), "link", httpString, 400, 400, LinkType.FALSE, null);
        asd.show();
        if (asd.isConfirmed()) {
            Link link = new Link(asd.getLinkTekst(), asd.getUrlString(), asd.getWidth(), asd.getHeight(), asd.getEmbedded(), asd.getGrensScores());
            if(link == null) { //something went wrong, reshow the dialog
                link = addLink(owner);
            }
            return link;
        } else { //action canceled
            return null;
        }
    }

	public static Link editLink(Link link) {
        return editLink(null, link);
    }

    /**
     * @return fi.dwo.client.domain.Sco
     */
	
	/*
    public static Link editLink(Component owner, Link link)  {
        String linkTekst = link.getLinkTekst();
        String url = link.getUrl();
        int height = link.getHeight();
        int width = link.getWidth();
        if(url==null) url = "";
        
                
        AddLinkDialog asd = new AddLinkDialog(owner, "URLgegevens wijzigen", linkTekst, url, width, height);
        asd.show();
        if (asd.isConfirmed()) {
        	Link newLink = new Link(asd.getLinkTekst(), asd.getUrl(), asd.getWidth(), asd.getHeight());
            if(newLink == null) { //something went wrong, reshow the dialog
            	newLink = editLink(owner, newLink);
            }
            return newLink;
        } else { //action canceled
            return null;
        }
    }
    */
    
	//nieuwe versie 17-4
	public static Link editLink(Component owner, Link link)  {
	        String linkTekst = link.getLinkTekst();
	        String[] urls = link.getUrlString();
	        int[] grensScores = link.getGrensScores();
	        int height = link.getHeight();
	        int width = link.getWidth();
	        LinkType embedded = link.getEmbedded();
	        //dit hieronder lijkt me niet verstandig; even kijken
	        //welke foutmeldingen ik nu krijg.
	        //if(urls==null) 
	        //{	urls = new String[10];
	        //	for(int i = 0; i < 10; i++)
	        //		urls[i] = "";
	        //}
	                
	        AddLinkDialog asd = new AddLinkDialog(owner, WiskOpdr.rb.getString("linkDialogUrlGegevensWijzigen"), linkTekst, urls, width, height, embedded, grensScores);
	        asd.show();
	        if (asd.isConfirmed()) {
	        	Link newLink = new Link(asd.getLinkTekst(), asd.getUrlString(), asd.getWidth(), asd.getHeight(), asd.getEmbedded(), asd.getGrensScores());
	            if(newLink == null) { //something went wrong, reshow the dialog
	            	newLink = editLink(owner, newLink);
	            }
	            return newLink;
	        } else { //action canceled
	            return null;
	        }
	    }

   
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    /*
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            this.setVisible(false);
        } else if (e.getSource() == okButton) {
        	linkTekst = linkTekstField.getText();
        	url = urlField.getText();
            try {width = Integer.parseInt(widthField.getText());}
            catch(Exception ex){}
            try {height = Integer.parseInt(heightField.getText());}
            catch(Exception ex){}
            confirmed = true;
            this.setVisible(false);
            WiskOpdr.setLaunchDataChanged();
        }
       else if (e.getSource() == voorwaardelijkBox) {
        	voorwaardelijk = voorwaardelijkBox.isSelected();
        	voorwaardelijkButton.setVisible(voorwaardelijk);
        }

    }
    */
    
	//nieuwe versie 17-4
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            this.setVisible(false);
        } else if (e.getSource() == okButton) {
        	linkTekst = linkTekstField.getText();
        	if(voorwaardelijk)
        	{	urls = voorwaardelijkButton.getUrls();
        		grensScores = voorwaardelijkButton.getGrensScores();
        	}
        	else
        	{	urls = new String[1];
        		urls[0] = urlField.getText();
        		try {
        		  URI uri = new URI(urls[0]);
        		  String scheme = uri.getScheme();
        		  if (scheme != null) {
        		    java.util.List<String> allowed = Arrays.asList("http","https", "goto", "about");
        		    if ( ! allowed.contains(scheme)) {
        		      throw new IllegalArgumentException(scheme);
        		    }
        		  }
        		  
        		} catch(Exception oops) {
        		  urls[0] = "";
        		  urlField.setText("");
        		  return;
        		}
        		
        	}
        	//url = urlField.getText();
            try {width = Integer.parseInt(widthField.getText());}
            catch(Exception ex){}
            try {height = Integer.parseInt(heightField.getText());}
            catch(Exception ex){}
            try {embedded = embeddedCB.getItemAt(embeddedCB.getSelectedIndex());}
            catch(Exception ex){}
            confirmed = true;
            this.setVisible(false);
            WiskOpdr.setLaunchDataChanged();
        }
       else if (e.getSource() == voorwaardelijkBox) {
        	voorwaardelijk = voorwaardelijkBox.isSelected();
        	voorwaardelijkButton.setVisible(voorwaardelijk);
        	if(!voorwaardelijk)
        	{	grensScores = null;
        		voorwaardelijkButton.setGrensScores(null);
        		urls = new String[1];
        		urls[0] = urlField.getText();
        		voorwaardelijkButton.setUrls(null);
        	}
        else if (e.getSource() == voorwaardelijkButton)
        	urlField.setText(voorwaardelijkButton.getUrls()[0]);
        	//dit werkt nog niet, even kijken hoe dit wel gaat werken.
        	// wat ik wil is dat zodra het scherm van de voorwaardelijkButton
        	// weer sluit, in het url-textfield de bovenste url uit de 
        	// voorwaardelijkbutton wordt overgenomen.
        }
       

    }

    /**
     * Invoked when the window is set to be the user's active window, which
     * means the window (or one of its subcomponents) will receive keyboard
     * events.
     * 
     * @param e
     *            The WindowEvent.
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent e) {
    }

    /**
     * Invoked when a window has been closed as the result of calling dispose on
     * the window.
     * 
     * @param e
     *            The WindowEvent.
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        setVisible(false);
        dispose();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public boolean isConfirmed() {
        return confirmed;
    }
    
    public String getLinkTekst() {
        return linkTekst;
    }
    
    //public String getUrl() {
    //    return url;
    //}
    
    public String[] getUrlString()
    {
    	return urls;
    }
    
    public int[] getGrensScores()
    {	return grensScores;
    }

    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    public LinkType getEmbedded() {
        return embedded;
    }

    
    
    //Nieuw en uitproberend!!
    
    public AddLinkDialog(Component owner, String windowTitle, String linkTekst, String[] urls, int width, int height, LinkType embedded, int[] grensScores) {
        super((owner instanceof Frame) ? (Frame) owner : new Frame(),
                windowTitle, true);
        this.setLayout(null);
        this.setBackground(new Color(230,230,230));
        this.linkTekst = linkTekst;
        this.urls = urls;
        this.height = height;
        this.embedded = embedded;
        this.width = width;
        this.grensScores = grensScores;
        if(grensScores == null)
        	voorwaardelijk = false;
        else
        	voorwaardelijk = true;

    
        confirmed = false;
        
      //if(urls != null)
       // for(int i = 0; i < urls.length; i++)
    	//{	if(urls[i] == "")
    	//		urls[i] = "http://";
    	//}

        JLabel l;
        FontMetrics fm;

        /* schoolName label */
        l = new JLabel(WiskOpdr.rb.getString("LEP_linkTekst"));//"Tekst"
        l.setForeground(Color.black);
        l.setFont(font);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 30);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* schoolName field */
        linkTekstField = new TextField(linkTekst);
        linkTekstField.setBounds(150, 28, 300, 20);
        this.add(linkTekstField);
        
        /* schoolName label */
        l = new JLabel(WiskOpdr.rb.getString("LEP_url"));//"URL"
        l.setForeground(Color.black);
        l.setFont(font);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 60);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* schoolName field */
        urlField = new TextField();
        if(urls != null)
        	urlField.setText(urls[0]);
        else
        	urlField.setText("http://");
        urlField.setBounds(150, 58, 300, 20);
        this.add(urlField,0);
        
        /* schoolLogin label */
        l = new JLabel(WiskOpdr.rb.getString("LEP_vensterBreedte"));//"breedte van het window"
        l.setForeground(Color.black);
        l.setFont(font);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 90);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* schoolLogin field */
        widthField = new TextField(""+width);
        widthField.setBounds(150, 88, 150, 20);
        this.add(widthField,0);
        
        /* studentPasswd label */
        l = new JLabel(WiskOpdr.rb.getString("LEP_vensterHoogte"));//"Hoogte van het window"
        l.setForeground(Color.black);
        l.setFont(font);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 120);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* studentPasswd field */
        heightField = new TextField(""+height);
        heightField.setBounds(150, 118, 150, 20);
        this.add(heightField,0);
        
        voorwaardelijkBox = new JCheckBox("Voorwaardelijke link (work in progress)");//nog aanpassen!
        voorwaardelijkBox.setBounds(10, 150, 300, 20);
        voorwaardelijkBox.setSelected(voorwaardelijk);
        //this.add(voorwaardelijkBox);
        voorwaardelijkBox.addActionListener(this);
        
        voorwaardelijkButton = new VoorwaardelijkeLinkButton();
        voorwaardelijkButton.setBounds(310, 150, 120, 20);
        voorwaardelijkButton.setVisible(voorwaardelijk);
        //this.add(voorwaardelijkButton);
        voorwaardelijkButton.addActionListener(this);
        
        if(voorwaardelijk)
        {	voorwaardelijkButton.setUrls(urls);
        	voorwaardelijkButton.setGrensScores(grensScores);
        }
        
        embeddedCB = new JComboBox<>(LinkType.values());
        embeddedCB.setBounds(150, 150, 300, 20);
        embeddedCB.setSelectedItem(embedded);
        embeddedCB.setOpaque(false);
        add(embeddedCB,0);
        embeddedCB.addActionListener(this);
        l = new JLabel("embedding");
        l.setBounds(10,150,300,20);
        add(l);
        
        //Hierdoor merkt niemand iets van aanbouw voorwaardelijke link 
        //Als de voorwaardelijke checkbox verdwijnt: stuk na else behouden.
        voorwaardelijkBox.setVisible(false);
        if(voorwaardelijkBox.isVisible())
        {	this.setSize(460, 230);

	        /* Register button */
	        okButton = new Button("OK");
	        okButton.setBounds(50,190,80,20);
	        okButton.addActionListener(this);
	        add(okButton);
	        
	        /* Reset button */
	        cancelButton = new Button("Cancel");
	        cancelButton.setBounds(150,190,80,20);
	        cancelButton.addActionListener(this);
	        add(cancelButton);
        }
        else
        {	this.setSize(460, 230);

	        /* Register button */
	        okButton = new Button("OK");
	        okButton.setBounds(50,190,80,20);
	        okButton.addActionListener(this);
	        add(okButton);
	        
	        /* Reset button */
	        cancelButton = new Button("Cancel");
	        cancelButton.setBounds(150,190,80,20);
	        cancelButton.addActionListener(this);
	        add(cancelButton);
	    }
        	
        
        this.addWindowListener(this);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}