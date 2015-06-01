package fi.javalogoweb;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import fi.beans.stringutils.StringUtils;

public class DeeltaakBodyComponent extends ProgrammaComponent implements ParameterEditorListener
{
	private ParameterTextField naamEditor;
	private int separatorX = 0;
	private FontMetrics fm;
	
	private boolean isEditingName = false;
	private boolean isEditingParamName = false;
	private IdentifierParameter deeltaaknaamParam;
	private boolean hasParameter = false;
	private IdentifierParameter pmParam;
	
	public DeeltaakBodyComponent(int x, int y, int b, int h, String pn, JavaLogoSchuifVeld sv)
	{
		super(x, y, b, h, pn, sv);
		deeltaaknaamParam = new IdentifierParameter(pn);
		pmParam = new IdentifierParameter("");
		naamEditor = new ParameterTextField(10, 4, 80, 17, this);
		fm = getFontMetrics(JavaLogoWeb.boldfont);
		separatorX = 10+fm.stringWidth(pn+"(");
		add(naamEditor);
		isHeightFixed = false; 			// allow height changes
	}
	
	boolean isNameValid()
	{
		return deeltaaknaamParam.isCorrect();
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
			hasParameter = ( pmParam.isCorrect() );
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
		hasParameter = ( !p.equals("") );
		pmParam.setParameter(p);
	}
	
	/**
	 * Gets the name of this ProgrammaComponent.
	 * This is the name from the IdentifierParameter, if Valid. If not, it returns the default name
	 * 
	 * @return	the default name for this ProgrammaComponent
	 */
	@Override
	public String getProgramName()
	{
		if ( isNameValid() )
			return deeltaaknaamParam.getParameterText();
		else
			return defaultName;
	}
	
	/**
	 * Is there a parameter in this ddeltaak? 
	 * Note: returns true also when the identifier is incorrect!
	 * 
	 * @return	param == empty string
	 */
	boolean hasParameter()
	{
		return hasParameter;
	}
	
	/**
	 * Get name of the parameter. Will return empty string if there isn't one.
	 * 
	 * @return	parametername or empty string
	 */
	String getParameterName()
	{
		return pmParam.getParameterText();
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
	protected void dropComponent(int x, int y)
	{
		int newX = previousX;
		int newY = previousY;
		Rectangle r = new Rectangle(JavaLogoSchuifVeld.ppx, JavaLogoSchuifVeld.ppy, JavaLogoSchuifVeld.ppw, JavaLogoSchuifVeld.pph);
		if ( r.contains(x, y) )
		{
			// Note: the actual mouse position, given by the parameters, is irrelevant, because we want to position
			// the DBC exactly where it is now. So, just translate the current (absolute-JLSV) coordinates 
			// to the ProgrammaPanel and apply min/max rules to keep it inside.
			newX = getX()-JavaLogoSchuifVeld.ppx;
			newX = Math.max(80, Math.min(newX, JavaLogoSchuifVeld.ppw-getWidth()));
			newY = getY()-JavaLogoSchuifVeld.ppy;
			newY = Math.max(0, Math.min(newY, JavaLogoSchuifVeld.pph-getHeight()));
		}
		setLocation(newX, newY);
		schuifveld.addToProgrammaPanel(this);
		schuifveld.repaint();
	}

	@Override
	protected void paintCommand(Graphics g)
	{
		g.setFont(JavaLogoWeb.boldfont);
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

	public String getCode(String tab)
	{	
		String s = "\nDeeltaak: " + getProgramName() + "( ";
		if ( hasParameter )
		{
			s = s + getParameterName();
		}
		s = s + " )" + "\n";
		return s+ super.getCode(tab)+"\n";
	}

}
