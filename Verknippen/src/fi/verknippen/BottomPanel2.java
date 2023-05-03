package fi.verknippen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

import fi.verknippen.Verknippen.InputKL;
import fi.verknippen.Verknippen.OkAL;
import fi.verknippen.Verknippen.OpdrachtIL;
import fi.verknippen.Verknippen.OpnieuwAL;
import fi.verknippen.Verknippen.TextAL;
import fi.verknippen.Verknippen.TextFL;
import fi.verknippen.Verknippen.VergelijkIL;

public class BottomPanel2 extends JPanel
{	
	VerknippenInteractiePanel owner;
	
	boolean showGoed = false;
	boolean showFout = false;
	boolean showHalfGoed = false;

	int taakNummer = 1;
	
	int offSet = 10;

	JLabel opdrachtLabel, opdrachtLabel2;
	JButton opnieuwButton;
	JTextField oppervlakteTextField;
	JButton okButton;
	//Choice vergelijkChoice;
	JComboBox vergelijkChoice;
	boolean vergelijkEnabled = true;

	public BottomPanel2(VerknippenInteractiePanel o, int w, int h)
	{	owner = o;
		setLayout(null);
		setSize(w, h);
		setBackground(Verknippen.bgColor);

		opnieuwButton = new JButton(Verknippen.rb.getString("opnieuwTekst"));
		opnieuwButton.setBackground(Verknippen.buttonColor);
		opnieuwButton.setFont(owner.theFont);
		int width = owner.theFM.stringWidth(Verknippen.rb.getString("opnieuwTekst")) + 35;

		opnieuwButton.setBounds(getSize().width - offSet - width, offSet,
								width, 3 * owner.theFM.getHeight() / 2);
		add(opnieuwButton);
		opnieuwButton.addActionListener(new OpnieuwAL());	
		
		oppervlakteTextField = new JTextField("");
		oppervlakteTextField.setFont(owner.theBoldFont);		
		width = owner.theBoldFM.stringWidth("XXXXXX") + 15;
		oppervlakteTextField.setBounds(
			opnieuwButton.getLocation().x - 2 * offSet - width,
			offSet,
			width, 3 * owner.theFM.getHeight() / 2);
		oppervlakteTextField.setVisible(false);
		add(oppervlakteTextField);
		oppervlakteTextField.addFocusListener(new TextFL());
		oppervlakteTextField.addActionListener(new TextAL());
		oppervlakteTextField.addKeyListener(new InputKL());

		
		//vergelijkChoice = new Choice();
		vergelijkChoice = new JComboBox();
		vergelijkChoice.addItem(Verknippen.rb.getString("groterTekst"));
		vergelijkChoice.addItem(Verknippen.rb.getString("kleinerTekst"));
		vergelijkChoice.addItem(Verknippen.rb.getString("evengrootTekst"));
		vergelijkChoice.setFont(owner.theBoldFont);		
		vergelijkChoice.setBackground(Color.white);		
		width = Math.max(owner.theBoldFM.stringWidth(Verknippen.rb.getString("groterTekst")),
					Math.max(owner.theBoldFM.stringWidth(Verknippen.rb.getString("kleinerTekst")),
							owner.theBoldFM.stringWidth(Verknippen.rb.getString("evengrootTekst"))))
				+ 30;
		vergelijkChoice.setBounds(
			opnieuwButton.getLocation().x - 2 * offSet - width,
			offSet / 2,
			width, 3 * owner.theFM.getHeight() / 2);
		vergelijkChoice.setVisible(false);
		add(vergelijkChoice);
		vergelijkChoice.addActionListener(new VergelijkAL());	

		okButton = new JButton("ok");
		okButton.setBackground(Verknippen.buttonColor);
		okButton.setFont(owner.theFont);
		width = owner.theFM.stringWidth("ok") + 35;
		okButton.setBounds(
			vergelijkChoice.getLocation().x + vergelijkChoice.getSize().width -
			width,
			vergelijkChoice.getLocation().y + vergelijkChoice.getSize().height,
			width, 3 * owner.theFM.getHeight() / 2);
		okButton.setVisible(false);
		add(okButton);
		okButton.addActionListener(new OkAL());	
			
		opdrachtLabel = new JLabel();
		opdrachtLabel.setFont(owner.theBoldFont);
//opdrachtLabel.setOpaque(true);
//opdrachtLabel.setBackground(Color.orange);		

		opdrachtLabel2 = new JLabel();
		opdrachtLabel2.setFont(owner.theBoldFont);
//opdrachtLabel2.setOpaque(true);
//opdrachtLabel2.setBackground(Color.orange);		

		// taakNummer== 1	
		opdrachtLabel.setText(Verknippen.rb.getString("maakRechthoekTekst"));
		opdrachtLabel.setBounds(
			offSet, offSet, owner.theBoldFM.stringWidth(opdrachtLabel.getText()) + 10,
			3 * owner.theBoldFM.getHeight() / 2);
		add(opdrachtLabel);
			

		opdrachtLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		opdrachtLabel2.setText(Verknippen.rb.getString("oppervlakteGrijsTekst"));	
		opdrachtLabel2.setSize(owner.theBoldFM.stringWidth(opdrachtLabel2.getText()) + 10, 3 * owner.theBoldFM.getHeight() / 2);
		opdrachtLabel2.setLocation(offSet / 2,
			//opdrachtLabel.getLocation().x + opdrachtLabel.getSize().width -
			//opdrachtLabel2.getSize().width, 
			opdrachtLabel.getLocation().y + opdrachtLabel.getSize().height);
		opdrachtLabel2.setVisible(false);		
		add(opdrachtLabel2);
		

				
	}
	
	public void setSize(int b, int h)
	{
		super.setSize(b, h);
		if (opnieuwButton != null)
			opnieuwButton.setLocation(getSize().width - offSet - opnieuwButton.getSize().width, 
									  opnieuwButton.getLocation().y);
	}

	public void zetTaakNummer(int taakNum)
	{
		if ((taakNum < 0) || (taakNum > 4))
			return;

		taakNummer = taakNum;

		if (taakNummer == 0)
		{	opnieuwButton.setVisible(false);
			opdrachtLabel.setVisible(false);
			oppervlakteTextField.setVisible(false);
			vergelijkChoice.setVisible(false);
			opdrachtLabel2.setVisible(false);
			okButton.setVisible(false);
			
		}
		else if (taakNummer == 1)
		{	opnieuwButton.setVisible(true);
			opdrachtLabel.setVisible(true);
			oppervlakteTextField.setVisible(false);
			vergelijkChoice.setVisible(false);
			opdrachtLabel2.setVisible(false);
			okButton.setVisible(false);
			
			opdrachtLabel.setHorizontalAlignment(SwingConstants.LEFT);
			opdrachtLabel.setText(Verknippen.rb.getString("maakRechthoekTekst"));
			opdrachtLabel.setBounds(
				offSet, offSet, owner.theBoldFM.stringWidth(opdrachtLabel.getText()) + 10, 3 * owner.theBoldFM.getHeight() / 2);
		}
		else if ((taakNummer == 2) || (taakNummer == 3))
		{	opnieuwButton.setVisible(true);
			opdrachtLabel.setVisible(true);
			oppervlakteTextField.setVisible(true);
			vergelijkChoice.setVisible(false);
			opdrachtLabel2.setVisible(false);
			okButton.setVisible(false);
		
			opdrachtLabel.setHorizontalAlignment(SwingConstants.LEFT);
			opdrachtLabel.setText(Verknippen.rb.getString("watIsOppervlakteTekst"));
			opdrachtLabel.setBounds(
				offSet, offSet, owner.theBoldFM.stringWidth(opdrachtLabel.getText()) + 10, 3 * owner.theBoldFM.getHeight() / 2);
			oppervlakteTextField.setLocation(
				opdrachtLabel.getLocation().x + opdrachtLabel.getSize().width , offSet);
			
		}
		else if (taakNummer == 4)
		{	opnieuwButton.setVisible(true);
			opdrachtLabel.setVisible(true);
			oppervlakteTextField.setVisible(false);
			vergelijkChoice.setVisible(true);
			opdrachtLabel2.setVisible(true);
			okButton.setVisible(true);
			
			opdrachtLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			opdrachtLabel.setText(Verknippen.rb.getString("oppervlakteRoodTekst"));
			opdrachtLabel.setBounds(offSet / 2, offSet / 2 - 2, owner.theBoldFM.stringWidth(opdrachtLabel.getText()) + 10,	
									3 * owner.theBoldFM.getHeight() / 2);
			
			vergelijkChoice.setLocation(
				opdrachtLabel.getLocation().x + opdrachtLabel.getSize().width + offSet,
				vergelijkChoice.getLocation().y);
			
			opdrachtLabel2.setSize(owner.theBoldFM.stringWidth(opdrachtLabel2.getText()) + 10, 3 * owner.theBoldFM.getHeight() / 2);
			opdrachtLabel2.setLocation(offSet/2,
				//opdrachtLabel.getLocation().x + opdrachtLabel.getSize().width -
				//opdrachtLabel2.getSize().width, 
				opdrachtLabel.getLocation().y + opdrachtLabel.getSize().height);
			
			okButton.setLocation(vergelijkChoice.getLocation().x + vergelijkChoice.getSize().width -
				                 okButton.getSize().width, 
				                 vergelijkChoice.getLocation().y + vergelijkChoice.getSize().height - 2);

		}		
		
	}
	
	public void paintComponent(Graphics g)
	{	g.setColor(Verknippen.bgColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
		
		g.setColor(Color.black);
		g.drawRect(0, -1, getSize().width - 1, getSize().height - 1);
		
		int vOffSet = 0;
		int x = 0;
		
		if ((taakNummer == 2) || (taakNummer == 3))
		{	vOffSet = offSet;
			x = oppervlakteTextField.getLocation().x + oppervlakteTextField.getSize().width;
		}
		else if (taakNummer == 4)
		{	vOffSet = offSet / 2;
			x = vergelijkChoice.getLocation().x + vergelijkChoice.getSize().width;
		}
		
		//int x = vergelijkChoice.getLocation().x + vergelijkChoice.getSize().width;
		
		if (showFout)
		{	g.drawImage(owner.foutKruis, x, vOffSet, null);
		}
		if (showGoed)
		{	g.drawImage(owner.goedKrul, x, vOffSet, null);
		}
		if (showHalfGoed)
		{	g.drawImage(owner.halfKrul, x, vOffSet, null);
		}
		
		
	}
	
	class OkAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	String choice = (String) vergelijkChoice.getSelectedItem();
			owner.antwoord = vergelijkChoice.getSelectedIndex() + 1;
			boolean ok = false;
			if (choice.equals(Verknippen.rb.getString("evengrootTekst")) &&
			    (owner.oppervlakteRood == owner.oppervlakteGrijs))
			{	ok = true;    
			}
			if (choice.equals(Verknippen.rb.getString("groterTekst")) &&
			    (owner.oppervlakteRood > owner.oppervlakteGrijs))
			{	ok = true;    
			}
			if (choice.equals(Verknippen.rb.getString("kleinerTekst")) &&
			    (owner.oppervlakteRood < owner.oppervlakteGrijs))
			{	ok = true;    
			}
			if (ok && (owner.antwoordenFout == 0))
			{	owner.antwoordOK = true;
				showGoed = true;
				showFout = false;
				showHalfGoed = false;
					
//					if (ipa != null)				
//						ipa.produceAction("changed");
					
			}
			else if (ok && (owner.antwoordenFout > 0))
			{	owner.antwoordOK = true;
				showGoed = false;
				showFout = false;
				showHalfGoed = true;
					
//					if (ipa != null)				
//						ipa.produceAction("changed");
					
			}
			else
			{	owner.antwoordenFout++;
				owner.antwoordOK = false;
				showGoed = false;
				showFout = true;
				showHalfGoed = false;
					
//					if (ipa != null)				
//						ipa.produceAction("changed");
					
			}
			
			
			owner.produceAction("changed");
			
			repaint();
		}
	}	
	
	public void focusLostAction()
	{	String text = oppervlakteTextField.getText();

//System.out.println("fl = " + text);			

		String text1 = trimTrailingZeros(text);
		boolean changed1 = (text.length() != text1.length());
		String text2 = addLeadingZero(text1);
		boolean changed2 = (text1.length() != text2.length());
		if (changed1 || changed2)
		{	text = text2;
			oppervlakteTextField.setText(text);
		}
		
		boolean error = false;
		int oNum = 0;
		try
		{	oNum = Integer.parseInt(text);
		}
		catch (NumberFormatException nfe)
		{	error = true;
		}
		if (!error)
		{	owner.antwoord = oNum;
			if (owner.antwoord == owner.oppervlakteRood)
			{	owner.antwoordOK = true;
				showGoed = true;
				showFout = false;
			}
			else
			{	owner.antwoordOK = false;
				showGoed = false;
				showFout = true;
			}
//System.out.println("o = " + oNum);
			owner.produceAction("changed");

		}	
			
		repaint();

	}

	class TextFL implements FocusListener
	{	public void focusGained(FocusEvent e)
		{
		}
		public void focusLost(FocusEvent e)
		{	focusLostAction();
		}
	}
	class TextAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
		
			focusLostAction();
/*		
			String text = oppervlakteTextField.getText();
			
			String text1 = trimTrailingZeros(text);
			boolean changed1 = (text.length() != text1.length());
			String text2 = addLeadingZero(text1);
			boolean changed2 = (text1.length() != text2.length());
			if (changed1 || changed2)
			{	text = text2;
				oppervlakteTextField.setText(text);
			}
			
			boolean error = false;
			int oNum = 0;
			try
			{	oNum = Integer.parseInt(text);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
			{	owner.antwoord = oNum;
				if (currentOpdracht.antwoord == currentOpdracht.oppervlakte)
				{	selector.setState(currentNum, selector.GREEN);
					currentOpdracht.antwoordOK = true;
					if (isDWOComponent)
					{	bottomPanel.showGoed = true;
						bottomPanel.showFout = false;
						
						if (ipa != null)				
							ipa.produceAction("changed");
						
					}	
				}
				else
				{	selector.setState(currentNum, selector.RED);			
					currentOpdracht.antwoordOK = false;
					if (isDWOComponent)
					{	bottomPanel.showGoed = false;
						bottomPanel.showFout = true;
						
						if (ipa != null)				
							ipa.produceAction("changed");
						
					}	

				}
			}	
			
			repaint();
*/
		}
	}

	public String trimTrailingZeros(String s)
	{	String txt = new String(s);
		if (txt.indexOf('.') < 0)
			return txt;
		char c = txt.charAt(txt.length() - 1);
		while (c == '0')
		{	txt = removeCharAt(txt, txt.length() - 1);
			c = txt.charAt(txt.length() - 1);
		}	
		c = txt.charAt(txt.length() - 1);
		if (c == '.')
			txt = removeCharAt(txt, txt.length() - 1);
		return txt;		
	}				
		
	public String addLeadingZero(String s)
	{	String txt = new String(s);
		// met minteken
		if ((txt.length() >= 2) && (txt.charAt(0) == '-') &&
			(txt.charAt(1) == '.'))
		{	txt = "-0" + txt.substring(1);
		}	
		// zonder minteken
		if ((txt.length() >= 1) && (txt.charAt(0) == '.'))
		{	txt = "0" + txt;
		}
		return txt;
	}

	public String removeCharAt(String s, int index)
	{	String txt = new String(s);
		// eerste
		if (index == 0)
			txt = txt.substring(1);
		// laatste	
		else if (index == (txt.length() - 1))
			txt = txt.substring(0, txt.length() - 1);
		// middenin	
		else
		{	String txt1 = txt.substring(0, index);
			String txt2 = txt.substring(index + 1);
			txt = txt1 + txt2;
		}
		return txt;
	}		
	
	class InputKL extends KeyAdapter
	{	public void keyReleased(KeyEvent e)
		{	
			String txt = oppervlakteTextField.getText();
			boolean corrected = false;
			// kijk of txt illegale characters bevat
			// dit zou er maximaal 1 moeten zijn
			int index = -1;
			for (int cCnt = 0; cCnt < txt.length(); cCnt++)
			{	char c = txt.charAt(cCnt);
				if (!isLegal(c))
					index = cCnt;
			}
			// verwijder illegaal karakter
			if (index >= 0)
			{	txt = removeCharAt(txt, index);
				corrected = true;
			}
			// leading zeros, leiden niet tot een NumberFormatException
			// geen minteken
			if (//(txt.indexOf('-') < 0) && 
				(txt.length() >= 2) &&
				(txt.charAt(0) == '0') && Character.isDigit(txt.charAt(1)))
			{	txt = removeCharAt(txt, 0);
				corrected = true;
			}
			
// trailing zeros na(!) decimale punt oplossen 
// bij actionPerformed of focusLost			
			
			if (corrected)
				oppervlakteTextField.setText(txt);
			
		}
		
		public boolean isLegal(char c)
		{	return Character.isDigit(c); 
		}
	}
	
	class OpnieuwAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{
			owner.opnieuwAction();
			
			owner.produceAction("changed");
			
		}
	}
	
	class VergelijkAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
			if (!vergelijkEnabled)
				return;
		
			owner.antwoord = 0;
			owner.antwoordOK = false;
			showGoed = false;
			showFout = false;

			
			repaint();

		}
	}
}
