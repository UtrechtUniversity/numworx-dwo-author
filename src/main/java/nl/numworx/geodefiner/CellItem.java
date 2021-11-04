package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import fi.beans.numworxlf.Constants;
import fi.beans.numworxlf.JButton;
import fi.beans.numworxlf.JOptionPane;
import javax.swing.JPanel;
import fi.beans.numworxlf.JRadioButton;
import javax.swing.TransferHandler;

import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Destroyable;
import fi.euclides.swing.AWTViewer;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import fi.wiskopdr.formuleobjects.FormuleVak;
import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.ui.UIEditor;
import nl.numworx.geodefiner.ui.UIModelFactory;

@SuppressWarnings("serial")
public class CellItem extends JPanel {
	
	private ImageIcon editImage = new ImageIcon(getClass().getResource("resources/edit.gif"));
	private ImageIcon wisImage = new ImageIcon(getClass().getResource("resources/teken_wisknop.gif"));
	private AbstractViewer viewer;
	
	class DeleteAction extends AbstractAction {
		DeleteAction() { super(null, wisImage); } // icoon 
		public void actionPerformed(ActionEvent e) {
// find model to remove
			Container container = CellItem.this.getParent();
			while (!(container instanceof DefinitionPanel)) container = container.getParent();
			DefinitionPanel parent = (DefinitionPanel) container;
			parent.remove(CellItem.this);
		}
	}
	
	
	class EditAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			UIEditor editor = getEditor();
			String name = viewer.toString(getCell().item);
// local patches: e, grid
			if("e".equals(name))
				name = "point(1,0)";
			else if ("$#@".equals(name))
				name = "grid";
			editor.setName(name);
			actionTail(editor, name);
		}

		public void actionTail(UIEditor editor, String name) {
			Icon icon = iconOf(getCell().item);
			Object defaultOption = Messages.getString("save");
			Object[] options = { defaultOption, Messages.getString("delete"), Messages.getString("cancel") };
			int ok = 
					canDelete ?
							JOptionPane.showOptionDialog(CellItem.this, editor, name, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon, options, defaultOption)
					:		JOptionPane.showConfirmDialog(CellItem.this, editor, name, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
					
			if(ok == JOptionPane.YES_OPTION) {
				if (editor.verify(viewer)) 
					editor.commit();
				else 
				{
					actionTail(editor, name); 
					return;
				}
				viewer.paint();
			} else if ( ok == JOptionPane.NO_OPTION) {
				CellItem.this.
				firePropertyChange("item", getCell().item, null);
				UIModel<?, ?> cnf = getCell().config;
				if(cnf != null) cnf.init(null);
				getCell().item.destroy();
			}
		}

		private UIEditor getEditor() {
			return getCellConfig().editor();
		}

		EditAction(Icon icon) {
			super(null, icon);
		}

	}
	
	class VisibleAction extends AbstractAction implements Observer {

		public void actionPerformed(ActionEvent e) {
			boolean visible = radio.isSelected();
			getCellConfig().setVisible(visible);
			getCell().item.setVisible(visible); // immediate mode.
		}

		@Override
		public void update(Observable observable, Object arg) {
			if(Destroyable.DESTROY == arg) {
				observable.deleteObserver(this);
			} else if(Destroyable.VISIBLE == arg) {
				boolean b = getCell().item.isVisible();
				if(radio.isSelected() != b) {
					radio.setSelected(b);
				}
			}
			
		}
	}
	
	@Override
	public Dimension getMaximumSize() {
		Dimension size = super.getMaximumSize();
		size.height = super.getPreferredSize().height;
		return size;
	}

	public Icon iconOf(Destroyable item) {
		// TODO zie fi.euclides.swing.ListRenderer
		return null;
	}

	CELL cell;
	JRadioButton radio;
	JButton potlood;
	FormuleVak center;
	boolean canDelete;
	final UIModelFactory factory;
	
	public CellItem(CELL cell, AbstractViewer viewer, int i0, UIModelFactory factory) {
		super(new BorderLayout());
		this.factory = factory;
		setBorder(new CellBorder());
		canDelete = true;
		setBackground(Color.WHITE);
		this.setCell(cell);
		this.viewer = viewer;
		boolean valid = cell.item != null;
		potlood = new JButton( valid ? new EditAction(editImage): new DeleteAction());
		potlood.setContentAreaFilled(false);
		add(potlood, BorderLayout.LINE_END);
		radio = new JRadioButton(); radio.setContentAreaFilled(false);
		radio.setEnabled(valid);
		radio.setSelected(valid && cell.item.isVisible());
		VisibleAction a = new VisibleAction();
		radio.setAction(a);
		if(cell.item != null) cell.item.addObserver(a);
		add(radio, BorderLayout.LINE_START);
		add( center = createCenter(cell), BorderLayout.CENTER);
		TransferHandler newHandler = new ListTransfer("pos");
		this.setTransferHandler(newHandler);
		addMouseListener(new MouseAdapter(){
			            public void mousePressed(MouseEvent e){
			                CellItem  button = (CellItem)e.getSource();
			                TransferHandler handle = button.getTransferHandler();
			                handle.exportAsDrag(button, e, TransferHandler.COPY);
			            }

						@Override
						public void mouseEntered(MouseEvent e) {
							setBackground(Constants.COLOR20);
							setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
						}

						@Override
						public void mouseExited(MouseEvent e) {
							setBackground(Color.WHITE);
							setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					}
			            
			        });
		setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	public String getPos() {
		return cell.var;
	}
	
	public void setPos(String var) {
		System.out.println("setPos " + var);
		if(var.equals(cell.var)) return;
		Container parent = getParent();
		int cnt = parent.getComponentCount();
		int s = cnt, d = 0;
		Component c = null;
		for(int i = 0; i < cnt; i++) {
			CellItem item = (CellItem) parent.getComponent(i);
			if(item == this) s = i;
			if(item.cell.var.equals(var)) { c = item; d = i; }
		}
		parent.remove(d);
		parent.add(c, s);
		parent.invalidate();
		parent.validate();
		parent.repaint();
	}
	
	public CellItem(CELL o, AWTViewer viewer2, UIModelFactory factory, boolean b) {
		this(o, viewer2, -1, factory);
		canDelete = b;
	}

	private FormuleVak createCenter(CELL cell) {
// FormuleVak		
		Color foreground = Color.black;
		if(cell.item == null)
			foreground = Color.lightGray;
		
		//return new JLabel(cell.toString());
		FormuleVak fv = new FormuleVak();
		fv.setFGColor(foreground);
		fv.vulVak(cell.text);
		fv.setEditable(false);
		fv.zetMaat();
		fv.setMinimumSize(new Dimension(2, fv.getHeight()));
		fv.setPreferredSize(new Dimension(40, fv.getHeight()));
		return fv;
	}

	public void refresh() {
		if(getCell().item == null) {
			radio.setSelected(false);
			radio.setEnabled(false);
			potlood.setAction(new DeleteAction());
		} else {
			if(!radio.isEnabled())
				potlood.setAction(new EditAction(editImage));
			radio.setEnabled(true);
			radio.setSelected(getCell().item.isVisible());
			getCell().item.addObserver((Observer) radio.getAction());
		}
		remove(center);
		add ( center = createCenter(getCell()), BorderLayout.CENTER);
	}

	private UIModel<?, UIEditor> getCellConfig() {
		if(getCell().config == null) 
		{
			getCell().config = factory.build(getCell().item);
		}
		return (UIModel<?, UIEditor>) getCell().config;
	}

	public CELL getCell() {
		return cell;
	}

	public void setCell(CELL cell) {
		this.cell = cell;
	}
	
	
}
