package fi.wiskopdr;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

public class ScoresObjectivesPanel extends JPanel implements MouseListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2942694616353969444L;
	private boolean kleurNeutraal;
	
	public static void main(String[] args ) {
		
		JFrame f = new JFrame("testing");
		Hashtable h = new Hashtable();
		String[][] objectives  = { { "fietsen", "lopen", "zwemmen" }, { "nogwat" } };
		int[][] totaalMaxObjectives = {{ 100,100,100 }, { 100 } };
		int[][] totaalScoreObjectives = {{ 20,50,90 }, { 10 } };
		String[] categorieString = { "sport" , "extra"};
		h.put("objectives", objectives);
		h.put("totaalMaxObjectives", totaalMaxObjectives);
		h.put("totaalScoreObjectives", totaalScoreObjectives);
		h.put("categorieString", categorieString);
		
		ScoresObjectivesPanel p = new ScoresObjectivesPanel(h);
		f.setContentPane(new JScrollPane(p));
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Dimension getPreferredSize() {
		Dimension d = new Dimension(200,175);
		for(int i = 0; i < mpX.length; i++) {
			d.width = Math.max(mpX[i], d.width);
			d.height = Math.max(mpY[i], d.height);
		}
		d.width += 200;
		d.height += 175;
		return d;
	}
	
	
	private String[][] objectives;
	private String[] categorieString;
	private String scoreText = "Score"; //TODO: in text opnemen 
	private String categorieText = "Categorie"; //TODO: in text opnemen

	
	private String[][] objectivesForDiagram;
	private String[] categorieStringForDiagram;
	private int[][] totaalScoresForDiagram;
	private int[][] totaalMaxForDiagram;
	private boolean[] categorieUitgeklapt;
	
	int straal = 100;
	int marge = 5;
	int[] mpX;
	int[] mpY;
	//tbv nieuwe weergave:
	//int tekstKolomBreedte, marge, regelHoogte, indent;
	
	int[][] totaalScoreObjectives;
	int[][] totaalMaxObjectives;
	double[][] scoresPercObjectives;
	double[] categorieScoresPercObjectives;
	int[] totaalMax;
	double[][] hoek, cumHoek, labelHoek; 
	double[][] hoekGraden, cumHoekGraden;
	int[][] eindPuntX, eindPuntY, labelEindPuntX, labelEindPuntY;
	int[][] straalRij;
	
	JTextArea[][] objectivesTextAreas;
	JLabel[] categorieLabels;
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	Color[][] kleurRij;
	Color[] categorieKleurRij;
	
	public ScoresObjectivesPanel(Map map)
	{
		setLayout(null);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		objectives = (String[][]) map.get("objectives");
		totaalScoreObjectives = (int[][]) map.get("totaalScoreObjectives");
		totaalMaxObjectives = (int[][]) map.get("totaalMaxObjectives");
		categorieString = (String[]) map.get("categorieString");
		
		/* Makkelijk voor testen (deel hierboven afschermen)
		objectives = new String[][] {{"Procenten/ verhoudingen", "Meten/ meetkunde", "Verbanden", "Getallen"}, {"Nogwat"}};
		totaalScoreObjectives = new int[][] {{30, 40, 80, 10}, {12}};
		totaalMaxObjectives = new int[][] {{110, 70, 90, 15}, {100}};
		categorieString = new String[] {"Categorie", "Extra"};
		*/
		
		totaalMax = new int[objectives.length];
		for(int j = 0; j<objectives.length; j++)
		{	totaalMax[j] = 0;
			for(int i = 0; i<objectives[j].length; i++)
				totaalMax[j] += totaalMaxObjectives[j][i];
		}	
		int nietNulTeller = 0;
		for(int j = 0; j < totaalMax.length; j++)
		{	
			if(totaalMax[j] != 0)
				nietNulTeller++;
		}
		
		objectivesForDiagram = new String[nietNulTeller][];
		categorieStringForDiagram = new String[nietNulTeller];
		totaalScoresForDiagram = new int[nietNulTeller][];
		totaalMaxForDiagram = new int[nietNulTeller][];
		categorieUitgeklapt = new boolean[nietNulTeller];
		
		nietNulTeller = 0;
		for(int j = 0; j < objectives.length; j++)
			if(totaalMax[j] != 0)
			{	objectivesForDiagram[nietNulTeller] = objectives[j];
				categorieStringForDiagram[nietNulTeller] = categorieString[j];
				totaalScoresForDiagram[nietNulTeller] = totaalScoreObjectives[j];
				totaalMaxForDiagram[nietNulTeller] = totaalMaxObjectives[j];
				totaalMax[nietNulTeller] = totaalMax[j];
				categorieUitgeklapt[nietNulTeller] = false;
				nietNulTeller++;
			}
		
		scoresPercObjectives = new double[objectivesForDiagram.length][];
		categorieScoresPercObjectives = new double[objectivesForDiagram.length];
		hoek = new double[objectivesForDiagram.length][];
		cumHoek = new double[objectivesForDiagram.length][];
		hoekGraden = new double[objectivesForDiagram.length][];
		cumHoekGraden = new double[objectivesForDiagram.length][];
		labelHoek = new double[objectivesForDiagram.length][];
		
		eindPuntX = new int[objectivesForDiagram.length][];
		eindPuntY = new int[objectivesForDiagram.length][];
		labelEindPuntX = new int[objectivesForDiagram.length][];
		labelEindPuntY = new int[objectivesForDiagram.length][];
		mpX = new int[objectivesForDiagram.length];
		mpY = new int[objectivesForDiagram.length];
		
		for(int i = 0; i<objectivesForDiagram.length; i++)
		{
			scoresPercObjectives[i] = new double[objectivesForDiagram[i].length];
			hoek[i] = new double[objectivesForDiagram[i].length];
			cumHoek[i] = new double[objectivesForDiagram[i].length];
			hoekGraden[i] = new double[objectivesForDiagram[i].length];
			cumHoekGraden[i] = new double[objectivesForDiagram[i].length];
			labelHoek[i] = new double[objectivesForDiagram[i].length];
			
			eindPuntX[i] = new int[objectivesForDiagram[i].length];
			eindPuntY[i] = new int[objectivesForDiagram[i].length];
			labelEindPuntX[i] = new int[objectivesForDiagram[i].length];
			labelEindPuntY[i] = new int[objectivesForDiagram[i].length];
			if(i<3)
			{	mpX[i] = 200 + i*400;
				mpY[i] = 175;
			}
			else 
			{	mpX[i] = 200 + (i-3)*400;
				mpY[i] = 525;
			}
			
		}
		
		for(int j = 0; j < objectivesForDiagram.length; j++)
		{	for(int i = 0; i<objectivesForDiagram[j].length; i++)
			{	hoek[j][i] = totaalMaxForDiagram[j][i] * 2 * Math.PI / totaalMax[j];
				hoekGraden[j][i] = totaalMaxForDiagram[j][i] * 360 / totaalMax[j];
			}
		
			cumHoek[j][0] = hoek[j][0];
			cumHoekGraden[j][0] = hoekGraden[j][0];
			for(int i = 1; i<objectivesForDiagram[j].length; i++)
			{	cumHoek[j][i] = cumHoek[j][i-1] + hoek[j][i];
				cumHoekGraden[j][i] = (cumHoekGraden[j][i-1] + hoekGraden[j][i]);
			}
			
			for(int i = 0; i<objectivesForDiagram[j].length; i++)
			{	
				eindPuntX[j][i] = (int) Math.round(straal * Math.sin(cumHoek[j][i]) + mpX[j]);
				eindPuntY[j][i] = (int) Math.round(- straal * Math.cos(cumHoek[j][i]) + mpY[j]);
			}
			for(int i = 0; i<objectivesForDiagram[j].length; i++)
			{
				labelHoek[j][i] = cumHoek[j][i] - hoek[j][i]/2;
				labelEindPuntX[j][i] = (int) ((straal + marge) * Math.sin(labelHoek[j][i]) + mpX[j]);
				labelEindPuntY[j][i] = (int) (- (straal + marge) * Math.cos(labelHoek[j][i]) + mpY[j]);
			}
			int somScoresPerc = 0;
			for(int k=0 ; k<objectivesForDiagram[j].length; k++)
		   	{	if(totaalMaxForDiagram[j][k]==0) scoresPercObjectives[j][k] = 0;
		   		else scoresPercObjectives[j][k] = Math.round(100.0*totaalScoresForDiagram[j][k]/totaalMaxForDiagram[j][k]);
		   		somScoresPerc += scoresPercObjectives[j][k];
		   	}
			categorieScoresPercObjectives[j] = Math.round(somScoresPerc/objectivesForDiagram[j].length); 
		}
		
		
		
		/* tbv nieuwe weergave
		categorieLabels = new JLabel[objectivesForDiagram.length];
		marge = 10;
		regelHoogte = theFM.getHeight() + marge;
		tekstKolomBreedte = 0;
		indent = 20;
		for(int j = 0; j < objectivesForDiagram.length; j++)
		{
			tekstKolomBreedte = Math.max(theFM. stringWidth(categorieStringForDiagram[j]) + marge, tekstKolomBreedte);
			for(int i = 0; i < objectivesForDiagram[j].length; i++ )
			{
				tekstKolomBreedte = Math.max(theFM.stringWidth(objectivesForDiagram[j][i]) + marge + indent, tekstKolomBreedte);
			}
		}
		
		int categorieX = marge + 5;
		int categorieY = regelHoogte + 5;
		for(int j = 0; j < objectivesForDiagram.length; j++)
		{
			categorieY += regelHoogte;
			categorieLabels[j] = new JLabel(categorieStringForDiagram[j]);
			categorieLabels[j].setFont(theFont);
			categorieLabels[j].setBounds(categorieX, categorieY, tekstKolomBreedte, regelHoogte);
			add(categorieLabels[j]);
			categorieLabels[j].addMouseListener(this);
			
		}
		*/
		
		straalRij = new int[objectivesForDiagram.length][];
		objectivesTextAreas = new JTextArea[objectivesForDiagram.length][];
		
		for(int j = 0; j<objectivesForDiagram.length; j++)
		{	straalRij[j] = new int[objectivesForDiagram[j].length];
			objectivesTextAreas[j] = new JTextArea[objectivesForDiagram[j].length];
			for(int i = 0; i<objectivesForDiagram[j].length; i++)
				straalRij[j][i] = (int) Math.round(straal * scoresPercObjectives[j][i]/100);
			for(int i = 0; i < objectivesForDiagram[j].length; i++)
			{	objectivesTextAreas[j][i] = new JTextArea();
				objectivesTextAreas[j][i].setFont(theFont);
				objectivesTextAreas[j][i].setLineWrap(true);
				objectivesTextAreas[j][i].setWrapStyleWord(true);
				
				if(labelEindPuntX[j][i] >= mpX[j]) //label staat 'rechts' van cirkel
				{	if(labelEindPuntY[j][i] > mpY[j] + straal)
						objectivesTextAreas[j][i].setBounds(labelEindPuntX[j][i] - breedteLabel(i,j)/2, labelEindPuntY[j][i], breedteLabel(i,j), hoogteLabel(i,j));
					else if(labelEindPuntY[j][i] >= mpY[j])
						objectivesTextAreas[j][i].setBounds(labelEindPuntX[j][i], labelEindPuntY[j][i], breedteLabel(i,j), hoogteLabel(i,j));
					else if(labelEindPuntY[j][i] <= mpY[j] - straal)
						objectivesTextAreas[j][i].setBounds(labelEindPuntX[j][i] - breedteLabel(i,j)/2, labelEindPuntY[j][i] - hoogteLabel(i,j), breedteLabel(i,j), hoogteLabel(i,j));
					else
						objectivesTextAreas[j][i].setBounds(labelEindPuntX[j][i], labelEindPuntY[j][i] - hoogteLabel(i,j), breedteLabel(i,j), hoogteLabel(i,j));
				}
				else //label staat 'links' van cirkel
				{	if(labelEindPuntY[j][i] > mpY[j] + straal)
						objectivesTextAreas[j][i].setBounds(labelEindPuntX[j][i] - breedteLabel(i,j)/2, labelEindPuntY[j][i], breedteLabel(i,j), hoogteLabel(i,j));
					else if(labelEindPuntY[j][i] >= mpY[j])
						objectivesTextAreas[j][i].setBounds(labelEindPuntX[j][i] - breedteLabel(i,j), labelEindPuntY[j][i], breedteLabel(i,j), hoogteLabel(i,j));
					else if(labelEindPuntY[j][i] <= mpY[j] - straal)
						objectivesTextAreas[j][i].setBounds(labelEindPuntX[j][i] - breedteLabel(i,j)/2, labelEindPuntY[j][i] - hoogteLabel(i,j), breedteLabel(i,j), hoogteLabel(i,j));
					else
						objectivesTextAreas[j][i].setBounds(labelEindPuntX[j][i] - breedteLabel(i,j), labelEindPuntY[j][i] - hoogteLabel(i,j), breedteLabel(i,j), hoogteLabel(i,j));
				}	
				objectivesTextAreas[j][i].setText(objectivesForDiagram[j][i] + ": " + (int) scoresPercObjectives[j][i]+"%");
				add(objectivesTextAreas[j][i]);
			}
		}
		
	}
	
	public int breedteLabel(int i, int j)
	{
		int breedte = Math.min(mpX[0] - straal - marge, 
				theFM.stringWidth(objectivesForDiagram[j][i] + ": " + (int) scoresPercObjectives[j][i]+"%"));
		
		return breedte;	
	}
	
	
	public int hoogteLabel(int i, int j)
	{
		int hoogte;
		
		if(theFM.stringWidth(objectivesForDiagram[j][i] + ": " + (int) scoresPercObjectives[j][i] + "%") > mpX[0] - straal - marge)
			hoogte = 2 * theFM.getHeight();
		else 
			hoogte = theFM.getHeight();			
		
		return hoogte;
	}
	
	public void zetKleurNeutraal()
	{
		kleurNeutraal = true;
	}
	
	public void zetKleuren()
	{	categorieKleurRij = new Color[objectivesForDiagram.length];
		kleurRij = new Color[objectivesForDiagram.length][];
		for(int j = 0; j < objectivesForDiagram.length; j++)
		{	
			int red = 255; 
			int green = 255;
			if(categorieScoresPercObjectives[j] < 50)
				green = (int) (green * categorieScoresPercObjectives[j] / 50);
			else 
				red -= (int) (red * (categorieScoresPercObjectives[j] - 50)/50);
			categorieKleurRij[j] = new Color(red, green, 0);
			kleurRij[j] = new Color[objectivesForDiagram[j].length];
			for(int i = 0; i < objectivesForDiagram[j].length; i++)
			{	red = 255; 
				green = 255;
				if(scoresPercObjectives[j][i] < 50)
					green = (int) (green * scoresPercObjectives[j][i] / 50);
				else 
					red -= (int) (red * (scoresPercObjectives[j][i] - 50)/50);
				kleurRij[j][i] = new Color(red, green, 0);
			}	
		}
	}
	
//	public void paintComponentNieuw(Graphics g)
//	{
//		super.paintComponent(g);
//		this.setBackground(Color.WHITE);
//		Graphics2D g2 = (Graphics2D) g;
//		Line2D.Double line;
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//		        RenderingHints.VALUE_ANTIALIAS_ON);
//		
//		zetKleuren();
//		
//		if(objectivesForDiagram.length == 1)//maar één categorie, later andere mogelijkheid verder uitwerken
//		{
//			int scoreBreedte = theFM.stringWidth(scoreText) + marge;
//			int labelX = marge + 5;
//			int scoreX = labelX + tekstKolomBreedte;
//			int kleurX = marge + tekstKolomBreedte;
//			int kleurY = marge;
//			int tekstY = regelHoogte + 2;
//			g.setFont(theBoldFont);
//			g.drawString(categorieStringForDiagram[0], labelX, tekstY);
//			g.drawString(scoreText, scoreX, tekstY);
//			g.setFont(theFont);
//			
//			for(int i = 0; i < objectivesForDiagram[0].length; i++)
//			{
//				tekstY += regelHoogte;
//				kleurY += regelHoogte;
//				g.setColor(kleurRij[0][i]);
//				g.fillRect(kleurX, kleurY, scoreBreedte, regelHoogte);
//				g.setColor(Color.BLACK);
//				g.drawString(objectivesForDiagram[0][i], labelX, tekstY);
//				g.drawString((int) scoresPercObjectives[0][i]+"%", scoreX, tekstY);
//			}
//			int lijnHoogte = marge;
//			g2.setColor(Color.BLACK);
//			for(int i = 0; i < objectivesForDiagram[0].length + 2; i++)
//			{	line = new Line2D.Double(marge, lijnHoogte, tekstKolomBreedte + scoreBreedte + marge, lijnHoogte);
//				g2.draw(line);
//				lijnHoogte += regelHoogte;
//			}
//			lijnHoogte -= regelHoogte;
//			line = new Line2D.Double(marge, marge, marge, lijnHoogte);
//			g2.draw(line);
//			line = new Line2D.Double(marge + tekstKolomBreedte, marge, marge + tekstKolomBreedte, lijnHoogte);
//			g2. draw(line);
//			line = new Line2D.Double(marge + tekstKolomBreedte + scoreBreedte, marge, marge + tekstKolomBreedte + scoreBreedte, lijnHoogte);
//			g2.draw(line);
//			
//			
//		}
//		else
//		{	int categorieX = marge + 5;
//			int scoreBreedte = theFM.stringWidth(scoreText) + marge;
//			int labelX = marge + indent + 5;
//			int scoreX = categorieX + tekstKolomBreedte;
//			int kleurX = marge + tekstKolomBreedte;
//			int lijnHoogte = marge;
//			
//			//int tekstY = regelHoogte + 2;
//			int tekstVerschil = regelHoogte - marge + 2;
//			int tussenRuimte = 5;
//			g.setFont(theBoldFont);
//			g.drawString(categorieText, categorieX, lijnHoogte + tekstVerschil);
//			g.drawString(scoreText, scoreX, lijnHoogte + tekstVerschil);
//			g.setFont(theFont);
//			tekstVerschil += 3;
//			line = new Line2D.Double(marge, lijnHoogte, tekstKolomBreedte + scoreBreedte + marge, lijnHoogte);
//			g2.draw(line);
//			lijnHoogte += regelHoogte;			
//			
//			for(int j = 0; j < objectivesForDiagram.length; j++)
//			{
//				//dubbel lijntje boven elke categorie.
//				line = new Line2D.Double(marge, lijnHoogte, tekstKolomBreedte + scoreBreedte + marge, lijnHoogte);
//				g2.draw(line);
//				lijnHoogte += tussenRuimte;
//				g.setColor(categorieKleurRij[j]);
//				g.fillRect(kleurX, lijnHoogte, scoreBreedte, regelHoogte);
//				g.setColor(Color.BLACK);
//				line = new Line2D.Double(marge, lijnHoogte, tekstKolomBreedte + scoreBreedte + marge, lijnHoogte);
//				g2.draw(line);
//				
//				//categorienaam en score invullen
//				tekstVerschil += 5 - regelHoogte;
//				//kleurY += regelHoogte;
//				//g.setColor(Color.BLACK);
//				categorieLabels[j].setLocation(categorieX, lijnHoogte + tekstVerschil);
//				tekstVerschil += regelHoogte - 8;  //label-location en drawString hebben verschillende y nodig.
//				g.drawString((int) categorieScoresPercObjectives[j]+"%", scoreX, lijnHoogte + tekstVerschil);
//			
//				if(categorieUitgeklapt[j])
//				{
//					for(int i = 0; i < objectivesForDiagram[j].length; i++)
//					{
//						lijnHoogte += regelHoogte;
//						//kleurY += regelHoogte;
//						g.setColor(kleurRij[j][i]);
//						g.fillRect(kleurX, lijnHoogte, scoreBreedte, regelHoogte);
//						g.setColor(Color.BLACK);
//						g.drawString(objectivesForDiagram[j][i], labelX, lijnHoogte + tekstVerschil);
//						g.drawString((int) scoresPercObjectives[j][i]+"%", scoreX, lijnHoogte + tekstVerschil);
//						line = new Line2D.Double(marge, lijnHoogte, tekstKolomBreedte + scoreBreedte + marge, lijnHoogte);
//						g2.draw(line);
//						
//					}
//				}
//				tekstVerschil += 3;
//				lijnHoogte += regelHoogte;
//			}
//			line = new Line2D.Double(marge, lijnHoogte, tekstKolomBreedte + scoreBreedte + marge, lijnHoogte);
//			g2.draw(line);
//			
//			//tabellijnen tekenen
//			line = new Line2D.Double(marge, marge, marge, lijnHoogte);
//			g2.draw(line);
//			line = new Line2D.Double(marge + tekstKolomBreedte, marge, marge + tekstKolomBreedte, lijnHoogte);
//			g2. draw(line);
//			line = new Line2D.Double(marge + tekstKolomBreedte + scoreBreedte, marge, marge + tekstKolomBreedte + scoreBreedte, lijnHoogte);
//			g2.draw(line);
//			
//		}
//		
//		
//		
//	}
	
	public void paintComponent(Graphics g)
	{	super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		Graphics2D g2 = (Graphics2D) g;
		Arc2D.Double arc;
		Ellipse2D.Double ellipse;
		Line2D.Double line;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);	
		
		zetKleuren();
		for(int j = 0; j < objectivesForDiagram.length; j++)
		{	arc = new Arc2D.Double(mpX[j] - straalRij[j][0], mpY[j] - straalRij[j][0], 2 * straalRij[j][0], 2 * straalRij[j][0], 90, (int) - hoekGraden[j][0],Arc2D.PIE);
			if(!kleurNeutraal)
				g2.setColor(kleurRij[j][0]);
			else
				g2.setColor(new Color(202,222,255));
			g2.fill(arc);
			g2.setColor(Color.BLACK);
			g2.draw(arc);
			
			for(int i = 1; i < objectivesForDiagram[j].length; i++)
			{
				arc = new Arc2D.Double(mpX[j] - straalRij[j][i], mpY[j] - straalRij[j][i], 2 * straalRij[j][i], 2 * straalRij[j][i], 90 - cumHoekGraden[j][i-1], (int) - hoekGraden[j][i], Arc2D.PIE);
				if(!kleurNeutraal)
					g2.setColor(kleurRij[j][i]);
				else
					g2.setColor(new Color(202,222,255));
				g2.fill(arc);
				g2.setColor(Color.BLACK);
				g2.draw(arc);
				
			}
			ellipse = new Ellipse2D.Double(mpX[j] - straal, mpY[j]-straal, 2 * straal, 2 * straal);
			g2.setColor(Color.BLACK);
			g2.draw(ellipse);
			for(int i = 0; i < objectivesForDiagram[j].length; i++)
			{	line = new Line2D.Double(mpX[j], mpY[j], eindPuntX[j][i], eindPuntY[j][i]);
				g2.draw(line);
			}
			g.setFont(theBoldFont);
			if(j<3)
				g.drawString(categorieStringForDiagram[j], mpX[j] - theBoldFM.stringWidth(categorieStringForDiagram[j])/2, 20);
			else
				g.drawString(categorieStringForDiagram[j], mpX[j] - theBoldFM.stringWidth(categorieStringForDiagram[j])/2, 370);
		}
	}

	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		for(int j = 0; j < objectivesForDiagram.length; j++)
		{
			if(e.getSource() == categorieLabels[j])
			{
				categorieUitgeklapt[j] = !categorieUitgeklapt[j];
				repaint();
				break;
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}	
}