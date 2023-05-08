package fi.javalogoweb3d;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import fi.logotekenap3d.TraceBeheerder;
import fi.logotekenap3d.TekenApplet3D;
import fi.beans.stringutils.StringUtils;
import fi.javalogoweb3d.parameters.Identifier;
import fi.javalogoweb3d.parameters.IdentifierList;

public class DeeltaakBodyComponent extends ProgrammaComponent implements ParameterEditorListener
{
	private ParameterTextField naamEditor;
	private int separatorX = 0;
	private FontMetrics fm;
	
	private boolean isEditingName = false;
	private boolean isEditingParamName = false;
	private Identifier deeltaaknaamParam;
	private IdentifierList pmParam;
	
	public DeeltaakBodyComponent(int x, int y, int b, int h, String pn, JavaLogoSchuifVeld sv)
	{
		super(x, y, b, h, pn, sv);
		deeltaaknaamParam = new Identifier(pn);
		pmParam = new IdentifierList();
		naamEditor = new ParameterTextField(10, 4, 80, 17, this);
		fm = getFontMetrics(JavaLogoWeb3d.boldfont);
		separatorX = 10+fm.stringWidth(pn+"(");
		add(naamEditor);
		isHeightFixed = false; 			// allow height changes
	}
	
	/**
	 * Check validity of entire deeltaak header.
	 * This includes testing if name is an identifier, all parameters are identifiers
	 * and number of parameters <= maximum.
	 * 
	 * @return true if all checks are ok.
	 */
	boolean isHeaderValid()
	{
		return deeltaaknaamParam.isCorrect() && pmParam.isCorrect();
	}

	@Override
	public void parameterEdited(String text)
	{
		if ( isEditingName )
		{
			deeltaaknaamParam.setParameter(text);
			isEditingName = false;
			separatorX = 10+fm.stringWidth(deeltaaknaamParam.getParameterText()+"(");
		} 
		else if ( isEditingParamName )
		{
			pmParam.setParameter(text);
			isEditingParamName = false;
		}
		schuifveld.repaint();
	}

	public void editParameter(boolean name)
	{
		if ( name )
		{
			naamEditor.setLocation(10, 4);
			naamEditor.vulIn(deeltaaknaamParam.getParameterText());
			separatorX = naamEditor.getX()+naamEditor.getWidth()+1;
			isEditingName = true;
		}
		else
		{
			naamEditor.setLocation(separatorX+2, 4);
			naamEditor.vulIn(pmParam.getParameterText());
			isEditingParamName = true;
		}
	}

	/**
	 * Handle mouse actions tos start and stop editting parameters.
	 * Note: all resize operations are handled directly by MouseListener in ProgrammaComponent, 
	 * no need to consider them here.
	 * @see fi.javalogoweb.ParameterEditorListener#parameterComponentClicked(int, int)
	 */
	@Override
	public void parameterComponentClicked(int x, int y)
	{
		boolean newEdit;
		boolean onName = ( x < separatorX );
		if ( isEditingName )
		{
			newEdit = !onName;				// newEdit true: going from name to value
			parameterEdited(naamEditor.getText());
		}
		else if ( isEditingParamName )
		{
			newEdit = onName;				// newEdit true: going from value to name
			parameterEdited(naamEditor.getText());
		} else
		{
			newEdit = true;					// we weren't editing anything, so start
		}
		if ( newEdit && y <= headerHeight)
		{	
			editParameter(onName);
		} else
		{
			naamEditor.setVisible(false);
			naamEditor.setEnabled(false);
		}
		schuifveld.repaint();
	}
	
	/**
	 * Set name and parameter directly (ProgrammaImporter)
	 * @param s
	 */
	void setDeeltaakHeader(String n, String p)
	{
		deeltaaknaamParam.setParameter(n);
		pmParam.setParameter(p);
		// Fix PBgv, 2015-08-16: set separator, so TextFiled for params will be in the right place after importing
		separatorX = 10+fm.stringWidth(n+"(");
	}
	
	/**
	 * Gets the name of this ProgrammaComponent, regardless of its correctness
	 * Note: when printing or exporting, we also want incorrect names. in execution
	 * 
	 * @return	the given name for this deeltaak
	 */
	@Override
	public String getProgramName()
	{
		return deeltaaknaamParam.getParameterText();
	}
	
	/**
	 * Get the number of parameters of this deeltaak
	 * Note: returns the number also when one or more identifiers are incorrect!
	 * 
	 * @return	number of parameters
	 */
	int getParameterCount()
	{
		return pmParam.getIdCount();
	}
	
	/**
	 * Get name of the parameter. Will return empty string if there isn't one.
	 * 
	 * @return	parametername or empty string
	 */
	String getParameterName(int n)
	{
		return pmParam.getIdentifier(n);
	}
	
	/**
	 * DeeltaakBodyComponents retains its width when being dragged
	 * 
	 * @see fi.javalogoweb.CommandComponent#getDragWidth()
	 */
	@Override
	int getDragWidth()
	{
		return getWidth();
	}
	
	/**
	 * Deeltaak will not be traced ( no carets while arranging the DTB's in the ProgrammaPanel)
	 * 
	 * @see fi.javalogoweb.CommandComponent#isTraceable()
	 */
	@Override
	boolean isTraceable()
	{
		return false;
	}
	
	@Override
	void containerHeightChanged(int h)
	{	
		if ( !isHeightFixed && isOpen )
		{	int newh;
			newh = Math.min(schuifveld.getHeight()-20, Math.max(pcminoh, commandBlock.getContentHeight()+headerHeight+20));
			setSize(getWidth(), newh);
			setLocation(getX(), Math.min(getY(), Math.max(0,JavaLogoSchuifVeld.pph-newh)));
		}
		
		int heightSurplus = commandBlock.getHeight()-(getHeight()-headerHeight);
		if ( heightSurplus > 0 )
		{	
			int newY = -heightSurplus;//Math.max(-heightSurplus, Math.min(0, commandBlock.getY()+8));
			commandBlock.setLocation(commandBlock.getX(), newY);
		}
		else
			commandBlock.setLocation(commandBlock.getX(), 0);
	}
	
	@Override
	protected void dropComponent(int x, int y)
	{
		// Fix PBgv, 2015-08-16: adjusted to new flexible size by always referring to programmaPanel's dimension
		int newX = previousX;
		int newY = previousY;
		Rectangle r = new Rectangle(JavaLogoSchuifVeld.ppx, JavaLogoSchuifVeld.ppy, schuifveld.getPPDimension().width, schuifveld.getPPDimension().height);
		if ( r.contains(x, y) )
		{
			// Note: the actual mouse position, given by the parameters, is irrelevant, because we want to position
			// the DBC exactly where it is now. So, just translate the current (absolute-JLSV) coordinates 
			// to the ProgrammaPanel and apply min/max rules to keep it inside.
			newX = getX()-JavaLogoSchuifVeld.ppx;
			newX = Math.max(80, Math.min(newX, schuifveld.getPPDimension().width-getWidth()));
			newY = getY()-JavaLogoSchuifVeld.ppy;
			newY = Math.max(0, Math.min(newY, schuifveld.getPPDimension().height-getHeight()));
		}
		setLocation(newX, newY);
		schuifveld.addToProgrammaPanel(this);
		schuifveld.repaint();
	}
	
	
	@Override
	protected void paintCommand(Graphics g)
	{
		g.setFont(JavaLogoWeb3d.boldfont);
		g.setColor(Color.BLACK);
		if ( isEditingName )
		{
			g.drawString("("+pmParam.getParameterText()+")", separatorX, 18);		
		}
		else if ( isEditingParamName )
		{
			g.drawString(deeltaaknaamParam.getParameterText()+"(", 10, 18);
			g.drawString(" )", separatorX+naamEditor.getWidth()+3, 18);
		} else
		{	// paint parts of the declaration in RED if they are incorrect;
			int xpos = 10;
			if ( !deeltaaknaamParam.isCorrect() ) g.setColor(Color.RED);
			g.drawString(deeltaaknaamParam.getParameterText(), 10, 18);
			xpos = xpos+fm.stringWidth(deeltaaknaamParam.getParameterText());
			g.setColor(Color.BLACK);
			g.drawString("( ", xpos, 18);
			xpos = xpos + fm.stringWidth("(");
			if ( !pmParam.isCorrect() )g.setColor(Color.RED);
			g.drawString(pmParam.getParameterText(), xpos, 18);
			xpos = xpos + fm.stringWidth(pmParam.getParameterText());
			g.setColor(Color.BLACK);
			g.drawString(")", xpos, 18);
		}
	}
	
	@Override
	public boolean execute(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{
		return executeContent(trb, ub, varSet);
	}

	public String getCode(String tab)
	{	
		//String s = "\nDeeltaak: " + getProgramName() + "( " + pmParam.getParameterText() + " )" + "\n";
		String s = "\n" + JavaLogoWeb3d.rb.getString("deeltaak1") + " " + getProgramName() + "( " + pmParam.getParameterText() + " )" + "\n";
		return s+ super.getCode(tab)+"\n";
	}

}
