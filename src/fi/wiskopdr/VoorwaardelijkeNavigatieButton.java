package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fi.wiskopdr.opdrnav.PlusMinKnop;

public class VoorwaardelijkeNavigatieButton extends JButton implements ActionListener, FocusListener
{
	private DialogFacade frame;
	JPanel voorwaardenPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JScrollPane scrollPane;
	Box[] boxKolom;
	
	private int[] vanPaginas, naarPaginas;
	private int[][] scorePaginas;
	private String[] scorePaginasString, verkorteScorePaginasString;
	private int[] grensScores;
	
	int[][] naarPaginasArray;
	int[][] grensScoresArray;
	int[][] scorePaginasArray;
	
	public int[][][] navVoorwaarden;
	
	private int maxVoorwaarden = 100; 
	
	private JTextField[] vanPaginaTextFields, naarPaginaTextFields, scorePaginaTextFields, grensTextFields;
	
	JButton okButton, cancelButton;
	PlusMinKnop aantalRijenKnop;
	int aantalRijen = 1;
	JButton sorteerButton;
	
	public VoorwaardelijkeNavigatieButton()
	{	super(WiskOpdr.rb.getString("CN_voorwaarden"));
		addActionListener(this);
	}
	
	public void zetNavVoorwaarden(int[][][] navVoorwaarden)
	{	this.navVoorwaarden = navVoorwaarden;
		if(navVoorwaarden == null)
			return;
		this.naarPaginasArray = navVoorwaarden[0];
		this.scorePaginasArray = navVoorwaarden[1];
		this.grensScoresArray = navVoorwaarden[2];
		
		int lengte = 0;
		if(naarPaginasArray != null)
		{	for(int i = 0; i < naarPaginasArray.length; i++)
				lengte += naarPaginasArray[i].length;
			vanPaginas = new int[lengte];
			naarPaginas = new int[lengte];
			scorePaginas = new int[lengte][];
			scorePaginasString = new String[lengte];
			grensScores = new int[lengte];
			vulPaginaRijtjes();
		}
		
	}
	
	public void vulPaginaRijtjes()
	{
		int k = 0;
		for(int i = 0; i < naarPaginasArray.length; i++)
		{	for(int j = 0; j < naarPaginasArray[i].length; j++)
			{	try{
				vanPaginas[k] = i + 1;
				naarPaginas[k] = naarPaginasArray[i][j];
				grensScores[k] = grensScoresArray[i][j];
				scorePaginas[k] = scorePaginasArray[i];
				
				scorePaginasString[k] = "";
				for(int l = 0; l < scorePaginas[k].length; l++)
					scorePaginasString[k] = scorePaginasString[k] + scorePaginas[k][l] + ",";
				scorePaginasString[k] = scorePaginasString[k].substring(0, scorePaginasString[k].length()-1);
				}
				catch(Exception e){}
				k++;
			}
		}
	}
	
	public void makeTextFields()
	{
		vanPaginaTextFields = new JTextField[maxVoorwaarden];
        naarPaginaTextFields = new JTextField[maxVoorwaarden];
        scorePaginaTextFields = new JTextField[maxVoorwaarden];
        grensTextFields = new JTextField[maxVoorwaarden];
        
        for(int i = 0; i < maxVoorwaarden; i++)
        {	vanPaginaTextFields[i] = maakTekstVeld(new Dimension(50,20));
        	naarPaginaTextFields[i] = maakTekstVeld(new Dimension(50,20));
        	scorePaginaTextFields[i] = maakTekstVeld(new Dimension(80,20));
        	grensTextFields[i] = maakTekstVeld(new Dimension(70,20));
        }
	}
	
	public JTextField maakTekstVeld(Dimension dim)
	{
		JTextField tekstVeld = new JTextField();
		tekstVeld.setPreferredSize(dim);
		tekstVeld.addFocusListener(this);
		tekstVeld.addActionListener(this);
		
		return tekstVeld;
	}
	
	public void zetTeksten()
	{	
		for(int i = 0; i < aantalRijen; i++)
        {	if(vanPaginas != null && i < vanPaginas.length)
        		vanPaginaTextFields[i].setText("" + vanPaginas[i]);
        	if(naarPaginas != null && i < naarPaginas.length)
				naarPaginaTextFields[i].setText(""+naarPaginas[i]);
        	if(scorePaginas != null && i < scorePaginas.length)
				scorePaginaTextFields[i].setText(scorePaginasString[i]);
        	if(grensScores != null && i < grensScores.length && grensScores[i] != 0)
        		grensTextFields[i].setText("" + grensScores[i]);
        }
		updateScorePaginas();
    }
	
	public void sorteerVoorwaarden()
	{
		int npTijdelijk;
		for(int j = 0; j < grensScoresArray.length; j++)
		{	for(int i = 0; i < grensScoresArray[j].length; i++)
			{	int minIndex = i;
				int minWaarde = grensScoresArray[j][i];
				for(int k = i + 1; k < grensScoresArray[j].length; k++)
					if(grensScoresArray[j][k] < minWaarde)
					{	minWaarde = grensScoresArray[j][k];
						minIndex = k;
					}
				if(minIndex != i)
				{	npTijdelijk = naarPaginasArray[j][minIndex];
					grensScoresArray[j][minIndex] = grensScoresArray[j][i];
					naarPaginasArray[j][minIndex] = naarPaginasArray[j][i];
					grensScoresArray[j][i] = minWaarde;
					naarPaginasArray[j][i] = npTijdelijk;
				}
			}
		}	
		vulPaginaRijtjes();
	}
	
	public void updateScorePaginas()
	{	for(int k = 1; k < aantalRijen; k++)
		{	scorePaginaTextFields[k].setEditable(true);
			String checkObject = vanPaginaTextFields[k].getText();
			if(checkObject != null && !"".equals(checkObject.trim()))
				for(int j = 0; j < k; j++)
					if(checkObject.equals(vanPaginaTextFields[j].getText()))
					{	scorePaginaTextFields[k].setText(scorePaginaTextFields[j].getText());
						scorePaginaTextFields[k].setEditable(false);
					}
		}
	}
	
	public void makeGUI(int aantalRijen){
		voorwaardenPanel = new JPanel();
		bottomPanel = new JPanel();
        Box boxv = Box.createVerticalBox();
        
        Box boxh = Box.createHorizontalBox();
        boxh.add(Box.createHorizontalStrut(10));
        
        boxKolom = new Box[4];
        for(int i = 0; i < boxKolom.length; i++)
        {	boxKolom[i] = Box.createVerticalBox();
        	boxKolom[i].add(Box.createVerticalStrut(10));
        }
        JLabel vanLabel, naarLabel, scoresLabel, grensLabel;
        
        vanLabel = new JLabel(WiskOpdr.rb.getString("CN_vanPagina"));
        vanLabel.setPreferredSize(new Dimension(50,20));
        boxKolom[0].add(vanLabel);
        
        naarLabel = new JLabel(WiskOpdr.rb.getString("CN_naarPagina"));
        naarLabel.setPreferredSize(new Dimension(50,20));
        boxKolom[1].add(naarLabel);
        
        scoresLabel = new JLabel(WiskOpdr.rb.getString("CN_scorePagina"));
        scoresLabel.setPreferredSize(new Dimension(80,20));
        boxKolom[2].add(scoresLabel);
        
        grensLabel = new JLabel(WiskOpdr.rb.getString("CN_grensPercentage"));
        grensLabel.setPreferredSize(new Dimension(70,20));
        boxKolom[3].add(grensLabel);
               
        for(int i = 0; i < aantalRijen; i++)
        {	boxKolom[0].add(vanPaginaTextFields[i]);
        	boxKolom[1].add(naarPaginaTextFields[i]);
        	boxKolom[2].add(scorePaginaTextFields[i]);
        	boxKolom[3].add(grensTextFields[i]);
        }
        
        for(int i = 0; i < boxKolom.length; i++)
        	boxh.add(boxKolom[i]);
        boxv.add(boxh);
        
        boxh = Box.createHorizontalBox();
        boxh.add(Box.createHorizontalStrut(20));
        
        aantalRijenKnop = new PlusMinKnop(0, 0, 16, 20, PlusMinKnop.VERTIKAAL);
        aantalRijenKnop.setPreferredSize(new Dimension(16, 20));
        aantalRijenKnop.setSize(getPreferredSize());
        aantalRijenKnop.addActionListener(this);
        boxh.add(aantalRijenKnop);
        
        boxh.add(Box.createHorizontalStrut(50));
        
        sorteerButton = new JButton(WiskOpdr.rb.getString("CN_sorteer"));
        sorteerButton.addActionListener(this);
        boxh.add(sorteerButton);
        
        boxv.add(boxh);
        voorwaardenPanel.add(boxv);
        
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
                
		scrollPane = new JScrollPane(voorwaardenPanel);
    }
	
	public void makeFrame(){
    	frame = DialogFacade.newInstance(this, "", true);
    	frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		frame.pack();
	    frame.setVisible(true);
	}
	
	private void makeObjects(){   
    	int[] newInts = null;
		vanPaginas = null;
		newInts = new int[maxVoorwaarden];
		for(int i = 0; i < maxVoorwaarden; i++)
		{	String checkObject = vanPaginaTextFields[i].getText();
			if(checkObject != null && !"".equals(checkObject.trim()))
				newInts[i] = Integer.parseInt(checkObject);
			else
			{	vanPaginas = new int[i];
				break;
			}
		}
		if(vanPaginas == null)
			vanPaginas = new int[maxVoorwaarden];
		for(int i = 0; i < vanPaginas.length; i++)
			vanPaginas[i] = newInts[i];
		
		naarPaginas = new int[vanPaginas.length];
		for(int i = 0; i < naarPaginas.length; i++)
			try{
				naarPaginas[i] = Integer.parseInt(naarPaginaTextFields[i].getText());
			}
			catch(Exception e)
			{
				naarPaginas[i] = vanPaginas[i] + 1;
			}
		
		scorePaginas = new int[vanPaginas.length][];
		scorePaginasString = new String[vanPaginas.length];
		for(int i = 0; i < scorePaginas.length; i++)
		{	scorePaginas[i] = geefScoreRij(scorePaginaTextFields[i].getText());
			scorePaginasString[i] = scorePaginaTextFields[i].getText();
		}
		
		grensScores = new int[vanPaginas.length];
    	for(int i = 0; i < grensScores.length; i++)
    	{	try{
    		grensScores[i] = Integer.parseInt(grensTextFields[i].getText());
    		}
    		catch(Exception e){
    			grensScores[i] = 0;
    		}
    	}
    	int maxPagina = 0;
    	for(int i = 0; i < vanPaginas.length; i ++)
    	{	if(vanPaginas[i] > maxPagina)
    			maxPagina = vanPaginas[i];
    	}
    	
    	naarPaginasArray = new int[maxPagina][];
    	grensScoresArray = new int[maxPagina][];
    	scorePaginasArray = new int[maxPagina][];
    	int[] aantalKeuzes = new int[maxPagina];
    	for(int i = 0; i < aantalKeuzes.length; i++)
    		aantalKeuzes[i] = 0;
    	for(int j = 0; j < vanPaginas.length; j++)
    		aantalKeuzes[vanPaginas[j]-1]++;
    	for(int i = 0; i < naarPaginasArray.length; i++)
    	{	naarPaginasArray[i] = new int[aantalKeuzes[i]];
    		grensScoresArray[i] = new int[aantalKeuzes[i]];
    	}
    	int[] index = new int[maxPagina];
    	for(int i = 0; i < maxPagina; i++)
    		index[i] = 0;
    	verkorteScorePaginasString = new String[maxPagina];
    	for(int i = 0; i < naarPaginas.length; i++)
    	{	naarPaginasArray[vanPaginas[i]-1][index[vanPaginas[i]-1]] = naarPaginas[i];
    		grensScoresArray[vanPaginas[i]-1][index[vanPaginas[i]-1]] = grensScores[i];
    		if(index[vanPaginas[i]-1]==0)
    		{	scorePaginasArray[vanPaginas[i]-1] = scorePaginas[i];
    			verkorteScorePaginasString[vanPaginas[i]-1] = scorePaginasString[i];
    		}
    		index[vanPaginas[i]-1]++;
    	}
    	sorteerVoorwaarden();
    	
    	navVoorwaarden = new int[3][][];
    	navVoorwaarden[0] = naarPaginasArray;
    	navVoorwaarden[1] = scorePaginasArray;
    	navVoorwaarden[2] = grensScoresArray;
    	
    }
	
	/* 
	 * hier gaat een rij als 1,3..5 in. Alleen positieve gehele getallen.
	 */
	private int[] geefScoreRij(String s)
	{
		int index, linkerGrens, rechterGrens;
		String getal;
		while(s.indexOf("..") > -1)
		{
			index = s.indexOf("..");
			if(index > 1 && Character.isDigit(s.charAt(index-2)))
				linkerGrens = Integer.parseInt(s.substring(index-2,index));
			else
				linkerGrens = Integer.parseInt(s.substring(index-1,index));
			if(s.length() >= index + 4 && Character.isDigit(s.charAt(index + 3)))
				rechterGrens = Integer.parseInt(s.substring(index+2, index + 4));
			else
				rechterGrens = Integer.parseInt(s.substring(index+2, index + 3));
			s = s.substring(0, index) + "," + s.substring(index + 2);
			for(int i = rechterGrens - 1; i > linkerGrens; i--)
				s = s.substring(0, index) + "," + i + s.substring(index);
		}
		s = s.replaceAll(" ", "");
		s = s + ",";
		
		int intLengte = s.length() - s.replaceAll(",", "").length();
		int[] scoreRij = new int[intLengte];
		for(int i = 0; i < scoreRij.length; i++)
		{	getal = s.substring(0, s.indexOf(','));
			s = s.substring(s.indexOf(',') + 1);
			try
			{	scoreRij[i] = Integer.parseInt(getal);
			}
			catch(Exception e)
			{	scoreRij[i] = 0;
			}
		}
		return scoreRij;
	}
	
	public int[][][] getNavVoorwaarden()
	{
		return navVoorwaarden;
	}
	
	public void testTekstVeld(JTextField tekstVeld, int testOnder, int testBoven)
	{	int testInt = -1;
		try{
			testInt = Integer.parseInt(tekstVeld.getText());
		}
		catch(Exception ex)
		{	tekstVeld.setText("");
		}
		if(testInt <= testOnder || testInt > testBoven)
			tekstVeld.setText("");
		
	}
	
	public void actionPerformed(ActionEvent e){
	
		if(e.getSource().equals(this) && frame==null)
		{	makeTextFields();
			
			if(vanPaginas != null && vanPaginas.length > 0)
			{	makeGUI(vanPaginas.length);
				aantalRijen = vanPaginas.length;
			}
			else
			{	makeGUI(1);
				aantalRijen = 1;
			}
			zetTeksten();
			makeFrame();
		}
		else if(e.getSource() instanceof JTextField)
		{	for(int i = 0; i < aantalRijen; i++)
			{	testTekstVeld(vanPaginaTextFields[i], 0, 100);
				testTekstVeld(naarPaginaTextFields[i], -1, 100);
				testTekstVeld(grensTextFields[i], -1, 100);
			}
			updateScorePaginas();
		}
		else if(e.getSource().equals(aantalRijenKnop))
		{	if(e.getActionCommand().equals("min") && aantalRijen < 100)
			{	makeGUI(aantalRijen + 1);
				scorePaginaTextFields[aantalRijen].setEditable(true);
				aantalRijen++;
				frame.getContentPane().removeAll();
				frame.getContentPane().add(scrollPane);
				frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
				frame.pack();
			}
			else if(e.getActionCommand().equals("plus") && aantalRijen > 0)
			{	makeGUI(aantalRijen - 1);
				aantalRijen--;
				vanPaginaTextFields[aantalRijen].setText("");
				naarPaginaTextFields[aantalRijen].setText("");
				scorePaginaTextFields[aantalRijen].setText("");
				grensTextFields[aantalRijen].setText("");
		 	
				frame.getContentPane().removeAll();
				frame.getContentPane().add(scrollPane);
				frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
				frame.pack();
			}
		}
		else if(e.getSource().equals(sorteerButton))
		{	makeObjects();
			zetTeksten();
		}
		else if(e.getSource().equals(okButton)) {   
			makeObjects();
			frame.setVisible(false);
            frame.dispose();
            frame=null;
        }
		else if(e.getSource().equals(cancelButton)) {   
			frame.getContentPane().removeAll();
			frame.setVisible(false);
            frame.dispose();
            frame=null;
        }
	}

	public void focusGained(FocusEvent arg0) {
	}

	public void focusLost(FocusEvent e) 
	{	if(e.getSource() instanceof JTextField)
		{	for(int i = 0; i < aantalRijen; i++)
			{	testTekstVeld(vanPaginaTextFields[i], 0, 100);
				testTekstVeld(naarPaginaTextFields[i], -1, 100);
				testTekstVeld(grensTextFields[i], -1, 100);
			}
		}
		updateScorePaginas();
	}   
}