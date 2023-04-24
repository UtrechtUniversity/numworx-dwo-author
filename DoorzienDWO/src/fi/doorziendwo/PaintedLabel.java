package fi.doorziendwo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

//light weight (else does not show in browser)
//label with painted(!) strings
public class PaintedLabel extends Component
{   boolean outline = false;
 	String text = "";
 	// default constructor
 	public void setLabel(String t)
 	{   text = t;
 		repaint();
 	}    
 
 	public void update(Graphics g)
 	{   paint(g);
     
 	}    
 
	// draw offscreen
	public void paint(Graphics g)
	{   Image offscreen = createImage(getSize().width, getSize().height);
	    Graphics og = offscreen.getGraphics();
	    og.setClip(0, 0, getSize().width, getSize().height);
	    paintLabel(og);
	    g.drawImage(offscreen, 0, 0, null);
	    og.dispose();
	}
 
 
	public void paintLabel(Graphics g)
	{   
     
		g.setColor(getBackground());
		g.fillRect(0, 0, getSize().width, getSize().height);
     
		if (outline)
		{   g.setColor(Color.black);
			g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);        
		}
		// get font with metrics
		Font fo = getFont();
		FontMetrics fm = getFontMetrics(fo);
		// determine baseline bx, by
		//int hSpace = (getSize().width -
		//           	fm.stringWidth(text)) / 2;
		int bx = 3;
//     	if (hSpace > 0)
//      	   bx = hSpace;
		int vSpace = (getSize().height -
					fm.getHeight()) / 2;    
		int by = getSize().height - fm.getDescent();;
		if (vSpace > 0)
			by = vSpace + fm.getHeight() - fm.getDescent();
		g.setColor(getForeground());    
		paintLabeledString(g, text, fo, bx, by);
	}    
 
	public void paintLabeledString(Graphics g, String cs, Font f, 
									int bx, int by)
	{   String current = cs;
		String temp;
		char marker = '!';
		char labelChar;
		int markerIndex = current.indexOf(marker);
		int baseX = bx;
		Color c = Color.black; 
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics(f);        
		Font fSmall = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
		FontMetrics fmSmall = g.getFontMetrics(fSmall);        
		boolean superOn = false;
		boolean subOn = false;
		// loop through current as long as current contains a '!'
		while (markerIndex > -1)
		{   // '!' is not first character 
			if (markerIndex > 0)
			{   // get part before the marker
				temp = current.substring(0, markerIndex);
				// draw it
				g.drawString(temp, baseX, by);
				if (superOn || subOn)
					baseX += fmSmall.stringWidth(temp);
				else
					baseX += fm.stringWidth(temp);
				// throw it away
				current = current.substring(markerIndex);
				markerIndex = 0;
			}
			// first character is '!' 
			else if (markerIndex <= (current.length() - 2))
			{   // try to process label instructions
				temp = current.substring(0, 2);
				current = current.substring(2);
				labelChar = temp.charAt(1);
				switch (labelChar)
				{   // start subscript
					// end superscript
					case 's': 
					{   by += fm.getDescent();
						if (superOn)
						{   superOn = false;
							g.setFont(f);
						}
						else
						{   subOn = true;
							g.setFont(fSmall);                        
						}
                     
					}
					break;
					// end subscript
					// start superscript
					case 't': 
					{   by -= fm.getDescent();
                     	if (subOn)
                     	{   subOn = false;
                     		g.setFont(f);                        
                     	}
                     	else
                     	{   superOn = true;
                     		g.setFont(fSmall);                        
                     	}
					}
					break;
					default: // nothing
				}  
				markerIndex = current.indexOf(marker);                
			}    
		} // while
		// draw the remaining part
		g.drawString(current, baseX, by);
	} // paintLabeledString   
}  // class PaintedLabel  
