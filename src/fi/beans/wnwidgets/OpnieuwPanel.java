package fi.beans.wnwidgets;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OpnieuwPanel extends PopupPanel implements ActionListener {

	private static final Frame DUMMY = new Frame();
	private JButton ja, nee;
	private static Image rollOverImage;
	private String titel = "";
	
	public OpnieuwPanel(int x, int y)
	{
		this(getOpnieuwImage(), x, y,"","",new Locale("nl"));
	}
	
	private static Image getOpnieuwImage() {
		URL url = OpnieuwPanel.class.getResource("resources/opnieuw.png");		
		Image popupImg = Toolkit.getDefaultToolkit().getImage(url);
		MediaTracker tr = new MediaTracker(DUMMY);
		tr.addImage(popupImg, 0);
		try {
			tr.waitForAll();
		} catch (InterruptedException e) {
		}
		return popupImg;
	}

	public OpnieuwPanel(Image image, int x, int y, String tekst, String titel, Locale locale) {
		super(image, x, y);
		this.titel = titel;
		rollOverImage = Toolkit.getDefaultToolkit().getImage(OpnieuwPanel.class.getResource("resources/macknop_over.png"));
		MediaTracker tr = new MediaTracker(DUMMY);
		tr.addImage(rollOverImage, 0);
		try {
			tr.waitForAll();
		} catch (InterruptedException e) {
		}
		this.setLocale(locale);
		closeRect = new Rectangle(getWidth()-40, 10, 22, 27);
		tekstArea.setLineWrap(true);
		tekstArea.setLocation(21, tekstArea.getY());
		
		if(!ComponentOrientation.getOrientation(locale).isLeftToRight()) {
			tekstArea.setComponentOrientation(ComponentOrientation.getOrientation(locale));
			tekstArea.setFont(new Font("SansSerif",Font.PLAIN,13));
		}
		else {
			tekstArea.setSize(getWidth()-30, 80);
		}
		//tekstArea.setBackground(Color.red); // DEBUG
		//tekstArea.setOpaque(false);
		//setText("Je verliest je huidige score als je opnieuw begint.\n\nWeet je zeker dat je opnieuw wilt beginnen?");
		setText(tekst);
		//setSize(getWidth(), getHeight());
		String jaString = "yes";
		if(locale.equals(new Locale("nl")))jaString = "ja";
		ja = new JButton(jaString);
		ja.setUI(MacButtonUI.getInstance());
		//ja.setFont(new Font("SansSerif",Font.BOLD,13)); opgenomen in UI
		//ja.setForeground(new Color(100,100,100));
		ja.addActionListener(this);
		
		String neeString = "no";
		if(locale.equals(new Locale("nl")))neeString = "nee";
		nee = new JButton(neeString);
		nee.setUI(MacButtonUI.getInstance());
		//nee.setFont(new Font("SansSerif",Font.BOLD,13));
		//nee.setForeground(new Color(100,100,100));
		
		nee.addActionListener(this);
		JPanel c = new JPanel();
		c.setBackground(Color.white);
		//c.setOpaque(true);
		c.setLayout(new GridLayout(1, 2, 20, 20));
		c.add(ja); c.add(nee);
		c.validate();
		c.setSize(c.getPreferredSize());
		add(c);
		c.setLocation((getWidth()-c.getWidth())/2, getHeight()-c.getHeight()-25);
	}

	
	/* (non-Javadoc)
	 * @see fi.beans.wnwidgets.PopupPanel#paintBackground(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics gr) {
		
		Graphics2D g = (Graphics2D)gr;
    	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
		/*
		int h = image.getHeight(this);
		if(h <= 0) return;
		int w = image.getWidth(this);
		int H = getHeight()-30;
		int m = 40;
		g.drawImage(image, 10, 10, w, h+10, 0, 0, w, h, this);
		g.drawImage(image, 10, H-(h-m), w, H, 0, m, w, h, this);
		*/
		g.drawImage(image, 0,0, this);
		g.setColor(new Color(100,100,100));
		g.setFont(new Font("SansSerif", Font.BOLD, 18));
		g.drawString(titel,150,30);
		
		
		
	}


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		JFrame f = new JFrame("test2");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(null);
		f.setSize(600, 400);
		OpnieuwPanel p = new OpnieuwPanel( 50, 50);
		f.getContentPane().add(p);
		Image helaasImg = f.getToolkit().createImage(p.getClass().getResource("resources/helaas.png"));
		MediaTracker tr = new MediaTracker(f);
		tr.addImage(helaasImg, 0);
		tr.waitForAll();
		PopupPanel helaas = new PopupPanel(helaasImg, 20, 40);
		helaas.setText("dit is een slecht nieuws gesprek");
		f.getContentPane().add(helaas);
		
		f.show();
	}

	private boolean ok;

	public void actionPerformed(ActionEvent e) {
		ok = ja == e.getSource();
		setVisible(false);
		produceAction("close");
	}

	public boolean isOk() {
		boolean result = ok;
		ok = false;
		return result;
	}
	
}
