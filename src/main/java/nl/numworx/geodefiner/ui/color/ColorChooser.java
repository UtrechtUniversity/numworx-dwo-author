package nl.numworx.geodefiner.ui.color;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorChooser extends JPanel implements ActionListener, ChangeListener {


	JComboBox<Object> prefab;
	ColorBoard board;
	JSlider slider;
	JComponent sample;
	RecentColors recent;
	
	public ColorChooser(java.awt.Color color) {
		this();
		setValue(color.getRGB());
	}
	
	
	public ColorChooser() {
		super(null);
		BoxLayout hlayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(hlayout);
		prefab = new JComboBox<Object>(Color.values());
		prefab.setEditable(true);
		prefab.setMaximumSize(new Dimension(1000, prefab.getPreferredSize().height));
		slider = new JSlider(); slider.setValue(100);
		slider.setMajorTickSpacing(25);
		slider.setMinorTickSpacing(5);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		Dictionary<Integer,JComponent> labels = new Hashtable<Integer,JComponent>();
		labels.put(0, new JLabel("0%"));
		//labels.put(25, new JLabel("25%"));
		labels.put(50, new JLabel("50%"));
		//labels.put(75, new JLabel("75%"));
		labels.put(100, new JLabel("100%"));
		
		slider.setLabelTable(labels );
		board = new ColorBoard();
		sample = new JComponent() {

			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(java.awt.Color.white);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(java.awt.Color.black);
				g.fillRect(0, 0, getWidth()/2, getHeight()/2);
				g.fillRect(getWidth()/2, getHeight()/2, getWidth()/2, getHeight()/2);
				g.setColor(getForeground());
				g.fillRect(0, 0, getWidth(), getHeight());
			} };
		sample.setPreferredSize(new Dimension(30,30));
		sample.setMinimumSize(sample.getPreferredSize());
		sample.setMaximumSize(sample.getPreferredSize());
		sample.setSize(sample.getPreferredSize());
		sample.setBorder(BorderFactory.createEtchedBorder());
		recent = new RecentColors();
// events
		slider.addChangeListener(this);
		board.addActionListener(this);
		prefab.addActionListener(this);
		recent.addActionListener(this);
// layout
		Box hh = Box.createHorizontalBox();
		hh.add(board);
		hh.add(Box.createHorizontalStrut(10));
		Box v = Box.createVerticalBox();
		Box h = Box.createHorizontalBox();
		h.add(prefab); v.add(h); h.add(Box.createHorizontalStrut(20));
		v.add(Box.createVerticalGlue());
		v.add(sample);
		v.add(Box.createVerticalGlue());
		v.add(slider);
		hh.add(v);
		hh.add(recent);
		add(hh);
		previewPanel = new JPanel();
		add(previewPanel);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == prefab) {
			Object item = prefab.getSelectedItem();
			if(item instanceof Color) {
				int v = ((Color) item).intValue();
				setValue0(v);
			} else if(item instanceof String) {
				int v = Integer.parseInt(item.toString().substring(1), 16);
				setValue0(v);
				recent.insertColor(v);
			}
		} else if (e.getSource() == board) {
			prefab.setSelectedItem(e.getActionCommand());
			int v = Integer.parseInt(e.getActionCommand().substring(1), 16);
			setValue0(v);
			recent.insertColor(v);
		} else if (e.getSource() == recent) {
			prefab.setSelectedItem(e.getActionCommand());
			int v = Integer.parseInt(e.getActionCommand().substring(1), 16);
			setValue0(v);			
		}
	}

	public void setValue(int v) {
		int frac = v >>> 24;
		slider.setValue(frac * 100 / 255);
		v = v & 0xFFFFFF;
		setValue0(v);
		for(Color i: Color.values()) {
			if(i.intValue() == v) {
				prefab.setSelectedItem(i);
				return;
			}
		}
		String cmd = Integer.toHexString(v);
		while(cmd.length() < 6) cmd = '0' + cmd;
		prefab.setSelectedItem('#'+cmd);
	}
	
	public int getValue() {
		return value;
	}
	public java.awt.Color getColor() {
		return new java.awt.Color(value, true);
	}
	
	private void setValue0(int v) {
		int frac = 255 * slider.getValue() / 100;
		v = (frac << 24)|(v&0xFFFFFF);
		value = v;
		sample.setForeground(new java.awt.Color(v, true));
		previewPanel.setForeground(sample.getForeground());
		sample.repaint();
		previewPanel.repaint();
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		ColorChooser b = new ColorChooser();
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setContentPane(b);
		f.pack();
		f.show();
		b.setValue(0xC0FF00FF);
	}

	int value;

	@Override
	public void stateChanged(ChangeEvent e) {
		setValue0(value);
	}

	private JComponent previewPanel;
	public JComponent getPreviewPanel() {
		return previewPanel;
	}


	public void setPreviewPanel(JComponent panel) {
		if(panel == null) panel = new JPanel();
		remove(previewPanel);
		add(previewPanel = panel);
	}

}
