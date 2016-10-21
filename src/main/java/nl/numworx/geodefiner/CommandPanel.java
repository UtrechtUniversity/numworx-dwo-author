package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.openmath.Expression;
import fi.euclides.openmath.ParseException;
import fi.euclides.openmath.Popcorn;
import fi.euclides.swing.SwingSymbols;

class CommandPanel extends JPanel implements ActionListener {
	
	JTextField cmd = new JTextField();	

	static {
		Popcorn.map = new SwingSymbols();
	}
	
	
	CommandPanel() {
		super(new BorderLayout());
		add(new JLabel("> "), BorderLayout.WEST);
		add(cmd, BorderLayout.CENTER);
		cmd.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		try {
			OMObject object = Expression.parse(command);
			firePropertyChange("command", command, object);
			cmd.setText("");
		} catch (ParseException e1) {
			JOptionPane.showMessageDialog(this, "Error", e1.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
