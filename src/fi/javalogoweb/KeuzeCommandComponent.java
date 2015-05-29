package fi.javalogoweb;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import logotekenap.Uitvoerblad;

public class KeuzeCommandComponent extends CompositeCommandComponent implements ParameterEditorListener
{
	private BooleanParameter condition;
	
	private String jaString = "ja";
	private String neeString = "nee";
	public static final int ifBlockX = 0;
	// note: elseBlockX is variable, given by a method
	public static final int blockY = 25;
	
	private boolean inIfBlock = true;
	
	private CommandContainer ifBlock;
	private CommandContainer elseBlock;
	
	private boolean isEditing = false;
	private ParameterTextField conditionEditor;
	
	public KeuzeCommandComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		commandName = "Keuze";
		condition = new BooleanParameter();
		
		ifBlock = new CommandContainer(ifBlockX, blockY, blockWidth(), h-blockY, this);
		add(ifBlock);
		elseBlock = new CommandContainer(elseBlockX(), blockY, blockWidth(), h-blockY, this);
		add(elseBlock);
		
		conditionEditor = new ParameterTextField((getWidth()-80)/2, 4, 80, 17, this);
		// note: xpos must change, since TextField will always be centered
		add(conditionEditor);
	}
	
	/**
	 * xpos of elseBlock cannot be a constant
	 * 
	 * @return	elseBlockX
	 */
	public final int elseBlockX()
	{
		return getWidth()/2-1;
	}
		
	public final int blockWidth()
	{
		return getWidth()/2+1;
	}
		
	/**
	 * PBgv: set the boolean expression (from programImporter).
	 * 
	 * @param expression	the boolean expression as a String
	 */
	public void setBoolExpression (String expression)
	{
		condition.setParameter(expression);
	}

	/**
	 * Select a container for future adding of components. Only relevant for scripting (ProgrammaImporter)
	 * 
	 * @param inIfBlock		true/false for obvious container selection
	 */
	public void setInIfBlock(boolean inIfBlock)
	{
		this.inIfBlock = inIfBlock;
	}

	/**
	 * Add a CommandComponent from source code, for instance by the ProgarammaImporter
	 * Note: Drag&drop will add a CC directly into one of the CommandContainers
	 * 
	 * @param cc			CommandComponent to be added
	 * @param inIfBlock		in if- or elseBlock
	 */
	@Override
	void addCComponent(CommandComponent cc)
	{	
		if ( inIfBlock )
		{
			ifBlock.addCComponent(cc);					
		} else
		{	
			elseBlock.addCComponent(cc);		
		}
	}
	
	@Override
	public void setSize(int w, int h)
	{	
		super.setSize(w,h);
		ifBlock.setWidth(blockWidth());
		// also move elseBlock to the middle
		elseBlock.setLocation(elseBlockX(), blockY);
		elseBlock.setWidth(blockWidth());
	}
	
	@Override
	public void setBounds(int x, int y, int w, int h)
	{	
		super.setBounds(x,y,w,h);
		if ( ifBlock != null && elseBlock != null)		// constructor will call setBounds before containers are made
		{
			ifBlock.setWidth(blockWidth());
			elseBlock.setLocation(elseBlockX(), blockY);
			elseBlock.setWidth(blockWidth());
		}
	}
	
	@Override
	void containerHeightChanged(int h)
	{
		// call doesn't specify the contaier, so get heights of both.
		int maxh = Math.max(ifBlock.getHeight(), elseBlock.getHeight());
		// also adjust heights of containers, or one may be too short
		ifBlock.setSize(ifBlock.getWidth(), maxh);
		elseBlock.setSize(elseBlock.getWidth(), maxh);
		super.setSize(getWidth(), maxh+blockY);
		((CommandContainer)getParent()).reArrange();
	}
	
	@Override
	public void parameterEdited(String text)
	{
		condition.setParameter(text);
		isEditing = false;
		schuifveld.tekenOpnieuw();
	}

	@Override
	public void parameterComponentClicked(int x, int y)
	{
		if ( isEditing )
		{
			condition.setParameter(conditionEditor.getText());
			conditionEditor.setVisible(false);
			conditionEditor.setEditable(false);
			isEditing = false;
		} else
		{	if ( y < blockY )
			{
				isEditing = true;
				conditionEditor.vulIn(condition.getParameterText());
				conditionEditor.setLocation( (getWidth()-conditionEditor.getWidth())/2, 4);
			}
		}
		schuifveld.tekenOpnieuw();
	}

	@Override
	protected void paintBackground(Graphics g)
	{
		g.setColor(Color.orange);
		if(traceKleur)g.setColor(traceActiveColor);
		g.fillRect(0, 0, getSize().width-1, getSize().height-1);
		g.setColor(Color.black);
		g.drawRect(0, 0, getSize().width-1, getSize().height-1);
		g.drawRect(1, 1, getSize().width-3, getSize().height-3);
		g.drawLine(0, 0, blockWidth(), blockY);
		g.drawLine(0, 1, blockWidth(), blockY+1);
		g.drawLine(getSize().width, 0, blockWidth(), blockY);
		g.drawLine(getSize().width, 1, blockWidth(), blockY+1);
		g.setFont(JavaLogoWeb.defaultfont);
		g.drawString(jaString, 10, 20);
		g.drawString(neeString, getSize().width-30, 20);		
	}

	@Override
	protected void paintCommand(Graphics g)
	{
		if ( isEditing ) return;			// nothing to paint, only the TextField
		g.setFont(JavaLogoWeb.defaultfont);
		if ( condition.isCorrect() )
		{
			g.setColor(Color.black);			
		} else
		{
			g.setColor(Color.RED);
		}
		g.drawString(condition.getParameterText(),getSize().width/2-10,18);
		
	}
	
	@Override
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	
		if ( !condition.isCorrect(varSet) ) return false; 
		boolean value = condition.getValue();
		//traceKleur =ub.checkKeuze(bc.geefTekst() + "? " + (value?"ja":"nee"));
		if(traceKleur)schuifveld.tekenOpnieuw();
		
		if(value)
		{	for(int j=0 ; j<ifBlock.getComponentCount() ; j++)
			{	Component c = ifBlock.getComponent(j);
				if(c instanceof CommandComponent)
				{	boolean tracekleur = ((CommandComponent)c).execute(ub, varSet);
					if(tracekleur) return true;
				}
			}
		}
		else
		{	for(int j=0 ; j<elseBlock.getComponentCount() ; j++)
			{	Component c = elseBlock.getComponent(j);
				if(c instanceof CommandComponent)
				{	boolean tracekleur = ((CommandComponent)c).execute(ub, varSet);
					if(tracekleur) return true;
				}
			}
		}
		return false;
	}
		
	@Override
	public String getCode(String tab)
	{	String s = tab + "Keuze: Als "+condition.getParameterText()+" Dan\n" + tab +"{\n";
		String tabNieuw = tab + "    ";
		s = s + ifBlock.getCode(tabNieuw);
		s = s + tab + "}\n";
		if ( elseBlock.getComponentCount() > 0 )
		{
			s = s + tab + "Anders\n" + tab +"{\n";
			s = s + elseBlock.getCode(tabNieuw);
			s = s + tab + "}\n";
		}
		return s;
	}

}
