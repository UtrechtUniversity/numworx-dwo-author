package fi.javalogoweb3d;

import java.awt.BorderLayout;
//import java.awt.GraphicsConfiguration;
//import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ExporterFrame extends JPanel //JFrame 
						   implements ActionListener
{
	private JavaLogoSchuifVeld veld;
	
	private JTextArea codearea;
	private JButton closebutton;
	//private JButton cancelbutton;
		
	public ExporterFrame(String code, JavaLogoSchuifVeld sv) //throws HeadlessException
	{
//		super(title);
		veld = sv;
//toegevoegd		
		setLayout(new BorderLayout());
		
		codearea = new JTextArea(code, 16, 48);
		codearea.setMargin(new Insets(3,5,3,5));
		codearea.setFont(JavaLogoWeb3d.defaultfont);
		JScrollPane scroller = new JScrollPane(codearea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroller, BorderLayout.CENTER);
		
		JPanel bottom = new JPanel();
		add(bottom, BorderLayout.SOUTH);
		closebutton = new JButton(JavaLogoWeb3d.rb.getString("sluitLabel"));
		closebutton.addActionListener(this);
		bottom.add(closebutton);
		//cancelbutton = new JButton("Annuleer");
		//cancelbutton.addActionListener(this);
		//bottom.add(cancelbutton);
		
		JLabel toelichting = new JLabel("Code van het algoritme:");
		toelichting.setFont(JavaLogoWeb3d.boldfont);
		add(toelichting, BorderLayout.NORTH);
		
		codearea.requestFocus();
		
//na setBounds elders		
		//validate();
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == closebutton )
		{
//			dispose();
			setVisible(false);
			veld.remove(this);
			veld.exf  = null;
		}
		
//		else
//		{
//			dispose();
//			setVisible(false);
//			veld.remove(this);
//			veld.imf  = null;
//		}
		
	}


}
