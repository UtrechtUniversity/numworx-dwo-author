package fi.javalogoweb3d;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ImporterFrame extends JFrame implements ActionListener
{
	private JavaLogoSchuifVeld veld;
	
	private JTextArea codearea;
	private JButton importbutton;
	private JButton cancelbutton;
		
	public ImporterFrame(String title, JavaLogoSchuifVeld sv) throws HeadlessException
	{
		super(title);
		veld = sv;
		//setLayout(new BorderLayout());
		
		codearea = new JTextArea(16, 48);
		codearea.setMargin(new Insets(3,5,3,5));
		codearea.setFont(JavaLogoWeb3d.defaultfont);
		JScrollPane scroller = new JScrollPane(codearea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroller, BorderLayout.CENTER);
		
		JPanel bottom = new JPanel();
		add(bottom, BorderLayout.SOUTH);
		importbutton = new JButton("Importeer");
		importbutton.addActionListener(this);
		bottom.add(importbutton);
		cancelbutton = new JButton("Annuleer");
		cancelbutton.addActionListener(this);
		bottom.add(cancelbutton);
		
		JLabel toelichting = new JLabel("Plak of type de code van het algoritme:");
		toelichting.setFont(JavaLogoWeb3d.boldfont);
		add(toelichting, BorderLayout.NORTH);
		
		codearea.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == importbutton )
		{
			veld.importeer(codearea.getText());
			dispose();
		} else
		{
			dispose();
		}
		
	}


}
