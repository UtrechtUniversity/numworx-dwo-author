package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import fi.wiskopdr.formuleobjects.FormuleVak;

public class CellItem extends JPanel {
	
	class EditAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(CellItem.this, "Edit panel here");
		}

		EditAction() {
			super();
		}

		EditAction(String name, Icon icon) {
			super(name, icon);
		}

		EditAction(String name) {
			super(name);
		}
		
	}
	
	class VisibleAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			boolean visible = radio.isSelected();
			cell.item.setVisible(visible);
		}
	}
	
	@Override
	public Dimension getMaximumSize() {
		Dimension size = super.getMaximumSize();
		size.height = super.getPreferredSize().height;
		return size;
	}

	CELL cell;
	JRadioButton radio;
	JButton potlood;
	JComponent center;

	CellItem(CELL cell) {
		super(new BorderLayout());
		setBackground(Color.WHITE);
		this.cell = cell;
		
		potlood = new JButton( new EditAction("edit"));
		add(potlood, BorderLayout.LINE_END);
		radio = new JRadioButton();
		radio.setSelected(cell.item.isVisible());
		radio.setAction(new VisibleAction());
		add(radio, BorderLayout.LINE_START);
		add( center = createCenter(cell), BorderLayout.CENTER);
		
	}

	private JComponent createCenter(CELL cell) {
// FormuleVak		
		//return new JLabel(cell.toString());
		FormuleVak fv = new FormuleVak();
		fv.vulVak(cell.text);
		fv.setEditable(false);
		fv.zetMaat();
		return fv;
	}

	public void refresh() {
		radio.setSelected(cell.item.isVisible());
		remove(center);
		add ( center = createCenter(cell), BorderLayout.CENTER);
	}
	
	
}
