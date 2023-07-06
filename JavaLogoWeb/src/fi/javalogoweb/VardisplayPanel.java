package fi.javalogoweb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VardisplayPanel extends JPanel
{
	private JTextArea displayarea;
		
	public VardisplayPanel()
	{
		setLayout(null);
		setBackground(new Color(161, 255,161));
		
		displayarea = new JTextArea(24, 24);
		displayarea.setMargin(new Insets(3,5,3,5));
		displayarea.setFont(JavaLogoWeb.defaultfont);
		displayarea.setBackground(new Color(221, 255, 221));
		displayarea.setBounds(0, 25, 160, 475);
		add(displayarea, BorderLayout.CENTER);
		
		JLabel toelichting = new JLabel(JavaLogoWeb.rb.getString("variabelenLabel"));
		toelichting.setBounds(10, 0, 150, 25);
		toelichting.setFont(JavaLogoWeb.boldfont);
		add(toelichting, BorderLayout.NORTH);
	}
	
	void setContent(String s)
	{
		displayarea.setText(s);
	}
}
