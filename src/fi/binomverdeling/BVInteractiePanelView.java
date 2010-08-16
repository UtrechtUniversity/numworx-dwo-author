package fi.binomverdeling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BVInteractiePanelView extends JPanel implements Observer {
	private int staafbreedte;
	private JPanel centerpanel;
	private final int EDITHEIGHT = 30;
	private JTextField ntext;
	private JLabel nlabel;
	private JTextField ktext;
	private JLabel klabel;
	private JTextField ptext;
	private JLabel plabel;	
	private BVInteractiePanelModel model;
	private JLabel totalekanslabel;
	
	public BVInteractiePanelView(BVInteractiePanelModel model) {
		super();
		this.model = model;
		this.model.addObserver(this);
		
        super.setLayout(new BorderLayout());
        
        this.totalekanslabel = new JLabel("P(X <= " + this.model.getSuccessen() + ") = " + this.model.berekenKansCumulatief());
		super.add(this.totalekanslabel, BorderLayout.SOUTH);
        this.ntext = new JTextField(5);
        this.ntext.setActionCommand("ntextupdate");
        this.ntext.setText(Integer.toString(this.model.getN()));
        this.ptext = new JTextField(5);
        this.ptext.setActionCommand("ptextupdate");
        this.ptext.setText(Double.toString(this.model.getP()));
        this.ktext = new JTextField(5);
        this.ktext.setActionCommand("ktextupdate");
        this.ktext.setText(Integer.toString(this.model.getSuccessen()));
        
        this.nlabel = new JLabel("n_test");
        this.plabel = new JLabel("p");
        this.klabel = new JLabel("k");
        
        
        Container editbalk = new Container();
        LayoutManager layout = new GridLayout(0,6);
        editbalk.setLayout(layout);
        editbalk.add(this.nlabel);
        editbalk.add(this.ntext);
        editbalk.add(this.klabel);
        editbalk.add(this.ktext);
        editbalk.add(this.plabel);
        editbalk.add(this.ptext);
        super.add(editbalk, BorderLayout.NORTH);
        
        this.centerpanel = new JPanel();
        super.add(this.centerpanel, BorderLayout.CENTER);
        
        super.setVisible(true);
	}
	
	public void paintStaafje(int k) {
		Graphics g = this.centerpanel.getGraphics();
		if(k<=this.model.getSuccessen()) {
			g.setColor(Color.GRAY);
		}
		g.fillRect((2*k+1)*this.staafbreedte, this.centerpanel.getHeight() - (int)(model.berekenKansK(k)*(this.centerpanel.getHeight()-this.EDITHEIGHT)+this.EDITHEIGHT), this.staafbreedte, (int)(model.berekenKansK(k)*(this.centerpanel.getHeight()-this.EDITHEIGHT)));
	}
	public void paintStaafjeSchaal(int k,double multiplier) {
		Graphics g = super.getGraphics();
		if(k<=this.model.getSuccessen()) {
			g.setColor(Color.GRAY);
		}
		g.fillRect((2*k+1)*this.staafbreedte, this.centerpanel.getHeight() - (int)(model.berekenKansK(k)*this.centerpanel.getHeight()*multiplier), this.staafbreedte, (int)(model.berekenKansK(k)*this.centerpanel.getHeight()*multiplier));
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
        this.berekenStaafBreedte();
        g.clearRect(0, 0, this.centerpanel.getWidth(), this.centerpanel.getHeight());
		for(int k = 0; k <= this.model.getN(); k++) {
			this.paintStaafje(k);
		}
	}
	
	private void berekenStaafBreedte() {
		this.staafbreedte = this.centerpanel.getWidth()/(2*this.model.getN()+3);
	}
	
	public void update(Observable o, Object arg) {
		this.ntext.setText(Integer.toString(this.model.getN()));
		this.ptext.setText(Double.toString(this.model.getP()));
		this.ktext.setText(Integer.toString(this.model.getSuccessen()));
		this.totalekanslabel.setText("P(X <= " + this.model.getSuccessen() + ") = " + this.model.berekenKansCumulatief());
		this.repaint(200);
	}
	
	public void addNListener(ActionListener a) {
		this.ntext.addActionListener(a);
	}
	public void addPListener(ActionListener a) {
		this.ptext.addActionListener(a);
	}
	public void addKListener(ActionListener a) {
		this.ktext.addActionListener(a);
	}
	public String getNText() {
		return this.ntext.getText();
	}
	public String getPText() {
		return this.ptext.getText();
	}
	public String getKText() {
		return this.ktext.getText();
	}
	
	public JPanel getPanel() {
		return this;
	}
}
