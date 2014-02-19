package fi.statistiek.boxplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * 
 * Component that displays the independent axis in a boxplot view
 * @author Manu Drijvers
 *
 */
public class BoxplotIndependentAxis extends JPanel {
	private BoxplotView boxplotView;
	private BoxplotModel model;
	
	private boolean verticalBoxplots;
	
	private double boxWidth;
	private boolean normalFit;
	private int maxStringLength;
	private ColumnType cType;
	private AllowedTypes type;
	private String columnName;
	
	/**
	 * Constructor
	 * @param model the model
	 * @param boxplotView the view using this
	 * @param verticalBoxplots true if using vertical boxplots, false otherwise
	 * @param columnName the name of the column represented by the boxplotview
	 */
	public BoxplotIndependentAxis(BoxplotModel model, BoxplotView boxplotView, boolean verticalBoxplots, String columnName) {
		this.model = model;
		this.boxplotView = boxplotView;
		this.verticalBoxplots = verticalBoxplots;
		this.columnName = columnName;
		
		this.setType();
		this.determineNormalFit();
	}
	
	private void setType() {
		cType = this.model.getTableModel().getColumnTypes().get(this.model.getColumnSplitIndex());
		type = cType.getType();
	}
	
	/**
	 * Determine whether the labels wont overlap when drawn horizontally
	 * and updates the preferred size
	 */
	private void determineNormalFit() {
		FontMetrics fm = super.getFontMetrics(super.getFont());
		
		maxStringLength = 0;
		
		if(type.isNumber()) {
			for(Double d : this.model.getSplitBinBoundaries()) {
				if(fm.stringWidth(d.toString()) > maxStringLength) {
					maxStringLength = fm.stringWidth(d.toString());
				}
			}
		}
		else if(type.equals(AllowedTypes.ENUM)) {
			for(String s : cType.getEnumOptions()) {
				if(fm.stringWidth(s) > maxStringLength) {
					maxStringLength = fm.stringWidth(s);
				}
			}
		}
		else {
			for(String s : this.model.getTableModel().getStringOptions(this.model.getColumnSplitIndex())) {
				if(fm.stringWidth(s) > maxStringLength) {
					maxStringLength = fm.stringWidth(s);
				}
			}
		}
		if(this.verticalBoxplots) {
			boxWidth = (double)(this.boxplotView.getWidth()-this.boxplotView.getDependentAxisWidth()-BoxplotView.KEEP_CLEAR_WIDTH)/(double)this.model.getSplitClasses();
			normalFit = maxStringLength <= boxWidth;
			
			if(normalFit) {
				Dimension newPrefSize = new Dimension(0, 35);
				if(!this.getPreferredSize().equals(newPrefSize)) {
					super.setPreferredSize(newPrefSize);
					this.invalidate(); //preferredsize has changed, invalidate
				}
			}
			else {
				System.out.println("PreferredSize 0,strlen + 20");
				Dimension newPrefSize = new Dimension(0, maxStringLength + 20);
				if(!this.getPreferredSize().equals(newPrefSize)) {
					super.setPreferredSize(new Dimension(0, maxStringLength + 20));
					this.invalidate(); //preferredsize has changed, invalidate
				}
			}
		}
		else {
			this.boxWidth = (double)(super.getHeight()-BoxplotView.KEEP_CLEAR_WIDTH-boxplotView.getDependentAxisHeight())/(double)this.model.getSplitClasses();
			Dimension newPrefSize = new Dimension(maxStringLength + fm.getHeight() + 5, 0);
			if(!super.getPreferredSize().equals(newPrefSize)) {
				super.setPreferredSize(newPrefSize);
				this.invalidate();
			}
				
		}
	}
	
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, super.getWidth(), super.getHeight());
		
		if(!this.model.getTableModel().isColumnIndexValid(this.model.getColumnSplitIndex())) {
			return;
		}
		
		this.determineNormalFit();
		g.setColor(Color.BLACK);
		
		FontMetrics fm = g.getFontMetrics();
		AffineTransform at = new AffineTransform();
		at.rotate(Math.PI*0.5);
		Font normalFont = g.getFont();
		Font rotateFont = g.getFont().deriveFont(at);	
		
		if(this.verticalBoxplots) {
			if(!normalFit) {
				g.setFont(rotateFont);
			}
			
			g.drawLine(boxplotView.getDependentAxisWidth(), 0, super.getWidth(), 0);
			
			String s;
			if(type.isNumber()) {
				for(int i = 0; i <= this.model.getSplitClasses(); i++) {
					int x1 = (int)Math.round((double)i*boxWidth) + this.boxplotView.getDependentAxisWidth();
					
					//draw marker
					g.drawLine(x1, 0, x1, 4);
					
					
					//draw text
					s = this.model.getSplitBinBoundaries().get(i).toString();

					if(normalFit) {
						int x2 = x1 - (int)(0.5*fm.stringWidth(s));
						g.drawString(s, x2, 5+fm.getHeight());
					}
					else {
						int x2 = x1 - (int)(0.5 * fm.getHeight());
						g.drawString(s, x2, 5);
					}
				}				
			}
			else {
				for(int i = 0; i < this.model.getSplitClasses(); i++) {
					int x1 = (int)Math.round((i+0.5)*boxWidth) + this.boxplotView.getDependentAxisWidth();
					
					if(type.equals(AllowedTypes.ENUM)) {
						s = cType.getEnumOptions()[i];
					}
					else {
						s = this.model.getTableModel().getStringOptions(this.model.getColumnSplitIndex()).get(i);
					}
					
					if(normalFit) {
						int x2 = x1 - (int)(0.5*fm.stringWidth(s));
						g.drawString(s, x2, 5+fm.getHeight());
					}
					else {
						int x2 = x1 - (int)(0.5 * fm.getHeight());
						g.drawString(s, x2, 5);
					}
				}
			}
			
			Font boldFont = normalFont.deriveFont(Font.BOLD);
			g.setFont(boldFont);
			g.drawString(this.columnName, super.getWidth()/2-fm.stringWidth(columnName), super.getHeight()-2);

		}
		else {
			g.drawLine(super.getWidth()-1, 0, super.getWidth()-1, super.getHeight()-boxplotView.getDependentAxisHeight());
			
			if(this.type.isNumber()) {
				for(int i = 0; i <= this.model.getSplitClasses(); i++) {
					String s = Double.toString(this.model.getSplitOptions().getBinBoundaries().get(i));
					int y = super.getHeight()-(boxplotView.getDependentAxisHeight()+(int)Math.round(i*this.boxWidth));
					g.drawLine(this.getWidth()-5, y, this.getWidth(), y);
					g.drawString(s, this.getWidth()-fm.stringWidth(s)-7, y+(int)(0.5*fm.getHeight()));
				}
			}
			else {
				String s;
				for(int i = 0; i < this.model.getSplitClasses(); i++) {
					if(type.equals(AllowedTypes.ENUM)) {
						s = this.cType.getEnumOptions()[i];
					}
					else {
						s = this.model.getTableModel().getStringOptions(this.model.getColumnSplitIndex()).get(i);
					}
					int y = super.getHeight()-(boxplotView.getDependentAxisHeight()+(int)Math.round((i+0.5)*this.boxWidth));
					//int y = (int)Math.round((this.model.getSplitClasses()-i-0.5)*this.boxWidth);
					g.drawString(s, this.getWidth()-fm.stringWidth(s)-2, y-(int)(0.5*fm.getHeight()));
				}
			}
			at = new AffineTransform();
			at.rotate(Math.PI*1.5);
			Font boldRotateFont = normalFont.deriveFont(at);
			boldRotateFont = boldRotateFont.deriveFont(Font.BOLD);
			g.setFont(boldRotateFont);
			g.drawString(this.columnName, fm.getHeight() + 2, super.getHeight()/2+fm.stringWidth(columnName));

			
			
		}
		
	
		g.setFont(normalFont);
	}	
}
