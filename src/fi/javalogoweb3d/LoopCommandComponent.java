package fi.javalogoweb3d;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import fi.javalogoweb3d.parameters.TAParameter;
import fi.logotekenap3d.TraceBeheerder;
import fi.logotekenap3d.TekenApplet3D;

public abstract class LoopCommandComponent extends CompositeCommandComponent implements ParameterEditorListener
{
	protected TAParameter loopCondition;
	
	protected CommandContainer loopBlock;
	public static final int blockX = 25;
	public static final int blockY = 25;

	private boolean isEditing = false;
	private ParameterTextField loopEditor;
	protected String naString;
	protected String naStringTranslated;

	public LoopCommandComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		
		loopBlock = new CommandContainer(blockX, blockY, b-blockX, JavaLogoSchuifVeld.ccsh, this);
		add(loopBlock);
	}

	/**
	 * Create the textfield for editing loop count or while-condition. Must be called by subclasses
	 * after the commandName has been set
	 */
	protected void createLoopEditor()
	{	FontMetrics fm = getFontMetrics(JavaLogoWeb3d.defaultfont);
		int tfx = 10+fm.stringWidth(commandNameTranslated+" ");
		loopEditor = new ParameterTextField(tfx, 4, 80, 17, this);
		add(loopEditor);
	}

	@Override
	void addCComponent(CommandComponent cc)
	{	
		loopBlock.addCComponent(cc);
	}
	
	public void setSize(int w, int h)
	{	
		loopBlock.setWidth(w);
		super.setSize(w,h);
	}
	
	public void setBounds(int x, int y, int w, int h)
	{	
		if ( loopBlock != null )		// for constructor only: loopBlock made later on
		{
			loopBlock.setWidth(w-blockX);			
		}
		super.setBounds(x,y,w,h);
	}
	
	@Override
	void containerHeightChanged(int h)
	{
		// this is callback from CommandContainer that has been adjeusted. Just change height of this component
		super.setSize(getWidth(), h+blockY);
		((CommandContainer)getParent()).reArrange();
	}
	
	/**
	 * Set loop count directly (programmaImporter)
	 * 
	 * @param s
	 */
	void setLoopCount(String s)
	{
		loopCondition.setParameter(s);
	}
	
	@Override
	public void parameterEdited(String text)
	{
		loopCondition.setParameter(text);
		isEditing = false;
		schuifveld.repaint();
	}

	@Override
	public void parameterComponentClicked(int x, int y)
	{
		if ( isEditing )
		{
			loopCondition.setParameter(loopEditor.getText());
			loopEditor.setVisible(false);
			loopEditor.setEditable(false);
			isEditing = false;
		} else
		{	if ( y < blockY )
			{
				isEditing = true;
				loopEditor.vulIn(loopCondition.getParameterText());
			}
		}
		schuifveld.repaint();
	}
	
	@Override
	protected void paintBackground(Graphics g)
	{
		if(traceKleur)
		{
			g.setColor(traceActiveColor);
			traceKleur = false;
		} else
		{
			g.setColor(Color.orange);
		}
		g.fillRect(0,0,getSize().width-1,getSize().height-1);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
		g.drawRect(1,1,getSize().width-3,getSize().height-3);		
	}

	@Override
	protected void paintCommand(Graphics g)
	{
		g.setFont(JavaLogoWeb3d.defaultfont);
		g.setColor(Color.black);
		if ( isEditing )
		{
			g.drawString(commandNameTranslated+" ", 10, 18);
			g.drawString(naStringTranslated, loopEditor.getX()+loopEditor.getWidth()+1, 18);		
		} else
		{
			if ( !loopCondition.isCorrect() )g.setColor(Color.RED);
			g.drawString(commandNameTranslated+" "+loopCondition.getParameterText()+naStringTranslated, 10, 18);		
		}
	}
	
	public abstract boolean executeContent(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet);
	
	@Override
	public boolean execute(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{	
		varSet.increaseLevel("-- in loop", false);			// maybe specify better? Like "Herhaal 2 van 4"?
		boolean b = executeContent(trb, ub, varSet);
		varSet.decreaseLevel();
		return b;
	}
		
	@Override
	public String getCode(String tab)
	{	//String s = tab + commandName + " "+ loopCondition.getParameterText() + naString + "\n" + tab +"{\n";
		String s = tab + commandNameTranslated + " " + loopCondition.getParameterText() + naStringTranslated + "\n" + tab +"{\n";
		String tabNieuw = tab + "    ";
		s= s+ loopBlock.getCode(tabNieuw);
		s = s + tab + "}\n";
		return s;
	}

}
