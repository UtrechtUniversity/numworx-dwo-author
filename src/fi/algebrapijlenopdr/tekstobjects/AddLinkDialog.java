package fi.algebrapijlenopdr.tekstobjects;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class AddLinkDialog extends Dialog implements ActionListener,
        WindowListener {

	private String url="";
    private int height;
    private int width;
     	
    private TextField urlField;
    private TextField widthField;
    private TextField heightField;
    
    private Button okButton;
    private Button cancelButton;
    
    private Font font = new Font("SansSerif",Font.PLAIN,12);
    
    boolean confirmed;

    

    public AddLinkDialog(Component owner, String windowTitle, String url, int width, int height) {
        super((owner instanceof Frame) ? (Frame) owner : new Frame(),
                windowTitle, true);
        this.setLayout(null);
        this.setBackground(new Color(230,230,230));
        this.url = url;
        this.height = height;
        this.width = width;
    
        confirmed = false;

        Label l;
        FontMetrics fm;

        /* schoolName label */
        l = new Label("URL");
        l.setForeground(Color.black);
        l.setFont(font);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 30);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* schoolName field */
        urlField = new TextField(url);
        urlField.setBounds(150, 28, 300, 20);
        this.add(urlField);
        
        /* schoolLogin label */
        l = new Label("breedte van het window");
        l.setForeground(Color.black);
        l.setFont(font);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 80);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* schoolLogin field */
        widthField = new TextField(""+width);
        widthField.setBounds(150, 78, 150, 20);
        this.add(widthField);
        
        /* studentPasswd label */
        l = new Label("Hoogte van het window");
        l.setForeground(Color.black);
        l.setFont(font);
        fm = l.getFontMetrics(l.getFont());
        l.setLocation(10, 110);
        l.setSize(fm.stringWidth(l.getText()) + 10, fm.getHeight());
        l.setVisible(false);
        this.add(l);
        l.setVisible(true);

        /* studentPasswd field */
        heightField = new TextField(""+height);
        heightField.setBounds(150, 108, 150, 20);
        this.add(heightField);
        
        
        

        this.setSize(460, 200);

        /* Register button */
        okButton = new Button("OK");
        okButton.setBounds(50,150,80,20);
        okButton.addActionListener(this);
        add(okButton);
        
        /* Reset button */
        cancelButton = new Button("Cancel");
        cancelButton.setBounds(150,150,80,20);
        cancelButton.addActionListener(this);
        add(cancelButton);
        
        this.addWindowListener(this);
    }

    public static Link addLink() {
        return addLink(null);
    }

    /**
     * @return fi.dwo.client.domain.Sco
     */
    public static Link addLink(Component owner) {
        AddLinkDialog asd = new AddLinkDialog(owner, "Link gegevens", "", 0, 0);
        asd.show();
        if (asd.isConfirmed()) {
            Link link = new Link(asd.getUrl(), asd.getWidth(), asd.getHeight());
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
    public static Link editLink(Component owner, Link link)  {
        String url = link.getUrl();
        int height = link.getHeight();
        int width = link.getWidth();
        if(url==null) url = "";
        
                
        AddLinkDialog asd = new AddLinkDialog(owner, "URLgegevens wijzigen", url, width, height);
        asd.show();
        if (asd.isConfirmed()) {
        	Link newLink = new Link(asd.getUrl(), asd.getWidth(), asd.getHeight());
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            this.setVisible(false);
        } else if (e.getSource() == okButton) {
            url = urlField.getText();
            try {width = Integer.parseInt(widthField.getText());}
            catch(Exception ex){}
            try {height = Integer.parseInt(heightField.getText());}
            catch(Exception ex){}
            confirmed = true;
            this.setVisible(false);
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
    
    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    
}