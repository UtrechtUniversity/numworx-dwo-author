package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import nl.numworx.geodefiner.ui.PointModel;
import nl.numworx.geodefiner.ui.UIEditor;
import nl.numworx.geodefiner.ui.UIModel;
import nl.numworx.geodefiner.ui.UIModelFactory;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.wiskopdr.formuleobjects.FormuleVak;

@SuppressWarnings("serial")
public class CellItem extends JPanel {
	
	private ImageIcon editImage = new ImageIcon(getClass().getResource("resources/edit.gif"));
	private AbstractViewer viewer;
	
	class EditAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			UIEditor editor = getEditor();
			int ok = JOptionPane.showConfirmDialog(CellItem.this, editor, viewer.toString(cell.item), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(ok == JOptionPane.OK_OPTION) {
				editor.commit();
				viewer.paint();
			}
		}

		private UIEditor getEditor() {
			return getCellConfig().editor();
		}

		EditAction(Icon icon) {
			super(null, icon);
		}

	}
	
	class VisibleAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			boolean visible = radio.isSelected();
			getCellConfig().setVisible(visible);
			cell.item.setVisible(visible); // immediate mode.
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

	CellItem(CELL cell, AbstractViewer viewer) {
		super(new BorderLayout());
		setBackground(Color.WHITE);
		this.cell = cell;
		this.viewer = viewer;
		potlood = new JButton( new EditAction(editImage));
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

	private UIModel<?> getCellConfig() {
		if(cell.config == null) 
		{
			cell.config = new UIModelFactory().build(cell.item);
		}
		return cell.config;
	}
	
	
}
