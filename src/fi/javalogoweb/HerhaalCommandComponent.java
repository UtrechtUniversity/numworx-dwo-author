package fi.javalogoweb;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;

import fi.javalogoweb.expressies.*;
import fi.javalogoweb.formuleobjects.*;
import logotekenap.Uitvoerblad;

public class HerhaalCommandComponent extends CompositeCommandComponent implements ParameterEditorListener
{
	private NumericParameter loopCount;
	
	private CommandContainer loopBlock;
	public static final int blockX = 25;
	public static final int blockY = 25;

	private boolean isEditing = false;
	private ParameterTextField countEditor;
	private String naString = " keer";
	private String naStringTranslated = JavaLogoWeb.rb.getString(naString);

	public HerhaalCommandComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		commandName = "Herhaal";
		commandNameTranslated = JavaLogoWeb.rb.getString(commandName);
		loopCount = new NumericParameter();
		
		loopBlock = new CommandContainer(blockX, blockY, b-blockX, h-blockY, this);
		add(loopBlock);
		
		FontMetrics fm = getFontMetrics(JavaLogoWeb.defaultfont);
		int tfx = 10+fm.stringWidth(commandNameTranslated+" ");
		countEditor = new ParameterTextField(tfx, 4, 80, 17, this);
		add(countEditor);
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
		loopCount.setParameter(s);
	}
	
	@Override
	public void parameterEdited(String text)
	{
		loopCount.setParameter(text);
		isEditing = false;
		schuifveld.repaint();
	}

	@Override
	public void parameterComponentClicked(int x, int y)
	{
		if ( isEditing )
		{
			loopCount.setParameter(countEditor.getText());
			countEditor.setVisible(false);
			countEditor.setEditable(false);
			isEditing = false;
		} else
		{	if ( y < blockY )
			{
				isEditing = true;
				countEditor.vulIn(loopCount.getParameterText());
			}
		}
		schuifveld.repaint();
	}
	
	@Override
	protected void paintBackground(Graphics g)
	{
		g.setColor(Color.orange);
		g.fillRect(0,0,getSize().width-1,getSize().height-1);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
		g.drawRect(1,1,getSize().width-3,getSize().height-3);		
	}

	@Override
	protected void paintCommand(Graphics g)
	{
		g.setFont(JavaLogoWeb.defaultfont);
		g.setColor(Color.black);
		if ( isEditing )
		{
			g.drawString(commandNameTranslated+" ", 10, 18);
			g.drawString(naStringTranslated, countEditor.getX()+countEditor.getWidth()+1, 18);		
		} else
		{
			if ( !loopCount.isCorrect() )g.setColor(Color.RED);
			g.drawString(commandNameTranslated+" "+loopCount.getParameterText()+naStringTranslated, 10, 18);		
		}
	}
	
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	
		if ( !loopCount.isCorrect(varSet) ) return false; 
		for(int i=0 ; i<(int)loopCount.getValue() ; i++)
		{	
			for(int j=0 ; j<loopBlock.getComponentCount() ; j++)
			{	
				Component c = loopBlock.getComponent(j);
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
	{	String s = tab + "Herhaal "+loopCount.getParameterText()+naString + "\n" + tab +"{\n";
		String tabNieuw = tab + "    ";
		s= s+ loopBlock.getCode(tabNieuw);
		s = s + tab + "}\n";
		return s;
	}

}
