package fi.statistiek;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import fi.statistiek.Statistiek;
/**
 * Dialog used for changing the name of a view
 * @author Manu Drijvers
 *
 */
public class ChangeViewNameDialog extends JDialog implements ActionListener {
	private JTextField textField;
	private JLabel label;
	private StatModel model;
	private int viewIndex;
	private StatInteractiePanelView view;
	
	/**
	 * Constructor
	 * @param owner the dialog owner
	 * @param model the model
	 * @param viewIndex the index of the view 
	 * @param view the view component
	 * @param location the initial location of the dialog
	 */
	public ChangeViewNameDialog(Dialog owner, StatModel model, int viewIndex, StatInteractiePanelView view, Point location) {
		super(owner, Statistiek.rb.getString("changeviewnameDialog"), true);
		super.setLocation(location);
		this.viewIndex = viewIndex;
		this.model = model;
		this.view = view;
		this.setUp();
		
	}
	
	/**
	 * Constructor
	 * @param owner the dialog owner
	 * @param model the model
	 * @param viewIndex the index of the view 
	 * @param view the view component
	 * @param location the initial location of the dialog
	 */
	public ChangeViewNameDialog(Frame owner, StatModel model, int viewIndex, StatInteractiePanelView view, Point location) {
		super(owner, Statistiek.rb.getString("changeviewnameDialog"), true);
		super.setLocation(location);
		this.viewIndex = viewIndex;
		this.model = model;
		this.view = view;
		this.setUp();
	}
	
	/**
	 * Initialize GUI
	 */
	private void setUp() {
		super.setSize(400, 100);
		super.setLayout(new GridLayout(2,1));
		this.label = new JLabel(Statistiek.rb.getString("enternameLabel"));
		super.add(this.label);
		
		this.textField = new JTextField();
		this.textField.addActionListener(this);
		super.add(this.textField);
		this.validate();
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getSource() == this.textField)
		{
			boolean b = true;
			for (int i = 0; b && i < this.model.getViews().size(); i++)
			{
				b = i == this.viewIndex
					|| !this.model.getViews().get(i).getViewName()
						.equals(this.textField.getText());
			}
			if (b)
			{
				this.model.getViews().get(this.viewIndex)
					.setViewName(this.textField.getText());

				this.view.update(null, null);
				// set the currently selected view
				// syl: tabPane is not showing correctly, 
				// while the field selectedView is properly updated (e.g., change page to check this)  
				this.view.processSelectedView(this.viewIndex);

				super.setVisible(false);
			}
			else
			{
				this.label.setText(Statistiek.rb
					.getString("namealreadyinuseLabel"));
			}
		}
	}
	
}
