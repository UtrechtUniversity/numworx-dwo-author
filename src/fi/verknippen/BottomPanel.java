package fi.verknippen;

//import java.awt.*;
//import java.applet.*;
//import java.util.*;


import javax.swing.*;

public class BottomPanel extends JPanel
{	
	Verknippen owner;
	
	public BottomPanel(Verknippen o)
	{	owner = o;
		setLayout(null);
		setBackground(owner.bgColor);
	}
}