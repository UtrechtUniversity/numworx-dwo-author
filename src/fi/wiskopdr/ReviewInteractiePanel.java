package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public class ReviewInteractiePanel extends JPanel implements ActionListener, MouseListener, WindowListener, KeyListener {

	private DialogFacade frame;
	private JButton okButton; 
	private JButton cancelButton;
	private JPanel reviewPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	
	private int scoreMax;
	private int score;
	private int scoreCorrectie;
	private Color correctieColor;
	private Color heeftCorrectieKleur = new Color(255,150,0,128);
	private Color geenCorrectieKleur = new Color(150,150,150,128);
	
	JLabel scoreMaxLabel;
	JLabel scoreMaxStringLabel;
	JLabel scoreLabel;
	JLabel scoreCorrectieLabel = new JLabel("correctie score:");
	
	private JLabel scoreStringLabel;
	private JTextField scoreCorrectieTF;
	
	TekstInteractiePanelVak tipv;
	private Polygon p;
	
	public ReviewInteractiePanel(TekstInteractiePanelVak tipv) {
		this.tipv = tipv;
		
		this.scoreMax = tipv.getScoreMax();
		correctieColor = geenCorrectieKleur;
		
		
		addMouseListener(this);
		
		okButton = new JButton("Ok");
	    okButton.addActionListener(this);
	    bottomPanel.add(okButton);
	       
	    cancelButton = new JButton("Cancel");
	    cancelButton.addActionListener(this);
	    bottomPanel.add(cancelButton);
	    
	    reviewPanel = new JPanel();
		
		scoreMaxLabel = new JLabel("maximale score:");
		scoreMaxStringLabel = new JLabel(""+scoreMax);
		scoreLabel = new JLabel("score:");
		scoreStringLabel = new JLabel(""+score);
		scoreCorrectieLabel = new JLabel("correctie score:");
		
		scoreCorrectieTF = new JTextField(""+scoreCorrectie);
		scoreCorrectieTF.addKeyListener(this);
	    
	    
	}
	
	public boolean contains(int x, int y) {
		return p.contains(x,y);
	}
	
	public void makeGUI(){
		
			 
	    Box boxv = Box.createVerticalBox();
	        
	    Box boxh = Box.createHorizontalBox();
	    boxh.add(scoreMaxLabel);
	    boxh.add(Box.createHorizontalStrut(20));
	    boxh.add(scoreMaxStringLabel); 
	    boxh.add(Box.createHorizontalStrut(5));
	    boxv.add(boxh);
	    
	    boxh = Box.createHorizontalBox();
	    boxh.add(scoreLabel);
	    boxh.add(Box.createHorizontalGlue());
	    boxh.add(scoreStringLabel); 
	    boxh.add(Box.createHorizontalStrut(5));
	    boxv.add(boxh);
	    
	    boxh = Box.createHorizontalBox();
	    boxh.add(scoreCorrectieLabel);
	    boxh.add(Box.createHorizontalStrut(10));
	    boxh.add(Box.createHorizontalGlue());
	    boxh.add(scoreCorrectieTF);  
	    boxv.add(boxh);
	    
	    reviewPanel.add(boxv);
	}
	
	public void setEditable(boolean b) {
		scoreCorrectieTF.setEditable(b);
	}
	 
	public void setScore(int score) {
		this.score = score;
		scoreStringLabel.setText(""+score);
	}
	
	public void setScoreCorrectie(int scoreCorrectie) {
		this.scoreCorrectie = scoreCorrectie;
		scoreCorrectieTF.setText(""+scoreCorrectie);
		correctieColor = scoreCorrectie==0 ? geenCorrectieKleur : heeftCorrectieKleur ;
		repaint();
	}
	
	public int getScoreCorrectie() {
		return scoreCorrectie;
	}
	
	private void updateScoreCorrectie() {
		String scoreCorrectieString = scoreCorrectieTF.getText();
		this.scoreCorrectie = Integer.parseInt(scoreCorrectieString);
		correctieColor = scoreCorrectie==0 ? geenCorrectieKleur : heeftCorrectieKleur ;
		repaint();
	}
	
	   
	public void makeFrame(){
		frame = DialogFacade.newInstance(this, "", true);
	    //Dimension preferredSize = new Dimension(400,320);
		//frame.setPreferredSize(preferredSize);
	    frame.addWindowListener(this);
	    //frame.setSize(preferredSize);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.getContentPane().add(reviewPanel);
	    frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	    
	    Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
		int x = getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + getWidth()));
		int y = getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + getHeight()));
		frame.setLocation(x,y);
			
		frame.pack();
		frame.setVisible(true);
	}
	    
	public void paintComponent(Graphics g) {	 
		p = new Polygon();
		p.addPoint(getWidth()-20, getHeight());
		p.addPoint(getWidth(), getHeight()-20);
		p.addPoint(getWidth(), getHeight());
	
   		g.setColor(correctieColor);
   		g.fillPolygon(p);
   		g.setColor(new Color(255,255,255));
   		g.drawString("v", getWidth()-8, getHeight()-2);
   	}
	public boolean hitButton(int x, int y) {
		boolean hit = p.contains(x,y);
		return hit;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(p.contains(e.getX(), e.getY()) && frame==null) {	
			makeGUI();
			makeFrame();
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(okButton)) {   
			updateScoreCorrectie();
			tipv.reviewUpdate();
			closeFrame();
        }
		else if(e.getSource().equals(cancelButton)) {   
			closeFrame();
        }
	}
	
	private void closeFrame() {
		frame.setVisible(false);
        frame.dispose();
        frame=null;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		closeFrame();
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		try {
			scoreCorrectie = Integer.parseInt(scoreCorrectieTF.getText());
		}
		catch(Exception ex) {
			scoreCorrectie = 0;
			scoreCorrectieTF.setText("");
		}
		if(scoreCorrectie<0) {
			scoreCorrectie = 0;
			scoreCorrectieTF.setText(""+scoreCorrectie);
		}
		if(scoreCorrectie>scoreMax-score) {
			scoreCorrectie = scoreMax-score;
			scoreCorrectieTF.setText(""+scoreCorrectie);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}  
		
}
