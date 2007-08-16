package fi.javalogoweb;

import fi.javalogoweb.schuifobjects.*;
import java.awt.*;
import java.awt.event.*;

import logotekenap.*;

public class JavaLogoSchuifVeld extends SchuifVeld implements ActionListener
{
	private CommandComponent[] commandComponents;
	private int aantalCC;
	private ProgrammaComponent programmaComponent;
	private int pcSizeHeightDefault;
	private int pcSizeWidthDefault;
	private int pcLocXDefault;
	private int pcLocYDefault;
	private ScrollSlider scrollSlider;
	//private String[] varNamen = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	private int varTeller;
	
	private VooruitCComponent actiefInvoerVak;
	
	public JavaLogoSchuifVeld(int x, int y, int b, int h)
	{	super(x,y,b,h);
	
		commandComponents = new CommandComponent[1000];
		
	}
	
	public void teken(Tekenblad tb)
	{	programmaComponent.teken(tb, new VarSet());
		
	}
	
	public void initialize()
	{	pcSizeWidthDefault = 200;
		pcSizeHeightDefault = 370;
		pcLocXDefault = 170;
		pcLocYDefault = 50;
		programmaComponent = new ProgrammaComponent(pcLocXDefault,pcLocYDefault,pcSizeWidthDefault,pcSizeHeightDefault, this);
		programmaComponent.zetVast(true);
		commandComponents[0]= programmaComponent;
		add(commandComponents[0],0);
		
		Panel pBoven = new Panel();
		pBoven.setBackground(Color.lightGray);
		pBoven.setBounds(pcLocXDefault,0,pcSizeWidthDefault,pcLocYDefault);
		add(pBoven);
		
		Panel pOnder = new Panel();
		pOnder.setBackground(Color.lightGray);
		pOnder.setBounds(pcLocXDefault,pcLocYDefault + pcSizeHeightDefault,pcSizeWidthDefault,500);
		add(pOnder);
		
		Label tekenProgrammaLabel = new Label("tekenprogramma");
		tekenProgrammaLabel.setBackground(Color.lightGray);
		tekenProgrammaLabel.setAlignment(Label.CENTER);
		tekenProgrammaLabel.setFont(new Font("SansSerif",Font.BOLD, 20));
		tekenProgrammaLabel.setBounds(pcLocXDefault,10,pcSizeWidthDefault, pcLocYDefault-10);
		add(tekenProgrammaLabel,0);
		
		int opdrSizeWidthDefault = 120;
		int opdrSizeHeightDefault = 370;
		int opdrLocXDefault = 10;
		int opdrLocYDefault = 50;
		
		Label opdrLabel = new Label("opdrachten");
		opdrLabel.setBackground(Color.lightGray);
		opdrLabel.setAlignment(Label.CENTER);
		opdrLabel.setFont(new Font("SansSerif",Font.BOLD, 20));
		opdrLabel.setBounds(opdrLocXDefault,10,opdrSizeWidthDefault, opdrLocYDefault-10);
		add(opdrLabel,0);
		
		
		
		commandComponents[1] = new PenAanCComponent(opdrLocXDefault,opdrLocYDefault,opdrSizeWidthDefault,25, this);
		add(commandComponents[1],0);
		
		commandComponents[2] = new PenUitCComponent(opdrLocXDefault,opdrLocYDefault+30,opdrSizeWidthDefault,25, this);
		add(commandComponents[2],0);
		
		commandComponents[3] = new VooruitCComponent(opdrLocXDefault,opdrLocYDefault+60,opdrSizeWidthDefault,25, this);
		add(commandComponents[3],0);
		
		commandComponents[4] = new LinksCComponent(opdrLocXDefault,opdrLocYDefault+90,opdrSizeWidthDefault,25, this);
		add(commandComponents[4],0);
		
		commandComponents[5] = new RechtsCComponent(opdrLocXDefault,opdrLocYDefault+120,opdrSizeWidthDefault,25, this);
		add(commandComponents[5],0);
		
		commandComponents[6] = new VulAanCComponent(opdrLocXDefault,opdrLocYDefault+150,opdrSizeWidthDefault,25, this);
		add(commandComponents[6],0);
		
		commandComponents[7] = new VulUitCComponent(opdrLocXDefault,opdrLocYDefault+180,opdrSizeWidthDefault,25, this);
		add(commandComponents[7],0);
		
		commandComponents[8] = new StapCComponent(opdrLocXDefault,opdrLocYDefault+210,opdrSizeWidthDefault,25, this);
		add(commandComponents[8],0);
		
		commandComponents[9] = new HerhaalCommandComponent(opdrLocXDefault,opdrLocYDefault+250,opdrSizeWidthDefault,48, this);
		add(commandComponents[9],0);
		
		commandComponents[10] = new VarCComponent(opdrLocXDefault,opdrLocYDefault+320,opdrSizeWidthDefault,25, this);
		add(commandComponents[10],0);
		
		
		
		aantalCC = 11;
		
		scrollSlider = new ScrollSlider(350,0,false);
		scrollSlider.setBackground(Color.lightGray);
		scrollSlider.zetStand(0);
		scrollSlider.setLocation(375,50);
		scrollSlider.addActionListener(this);
		super.add(scrollSlider);
		scrollSlider.setVisible(false);
		
		
		
	}
	
	public void scroll(boolean adjustLocation)
	{	int extraHoogte = programmaComponent.getSize().height - pcSizeHeightDefault;
		if(extraHoogte > 0 && adjustLocation)
		{	programmaComponent.setLocation(programmaComponent.getLocation().x,   pcLocYDefault - extraHoogte);
			if(scrollSlider!=null) 
			{	scrollSlider.zetStand(extraHoogte);
				scrollSlider.setVisible(true);
			}
		}
		else if(extraHoogte > 0)
		{	if(scrollSlider!=null) 
			{	scrollSlider.setVisible(true);
			}
		}
		else 
		{	programmaComponent.setLocation(programmaComponent.getLocation().x, pcLocYDefault);
			if(scrollSlider!=null) 
			{	scrollSlider.zetStand(0);
				scrollSlider.setVisible(false);
			}
		}
		
	}
	
	public void zetStapel(CommandComponent cc)
	{	int x = cc.getLocation().x;
		int y = cc.getLocation().y;
		int b = cc.getSize().width;
		int h = cc.getSize().height;
		//if(asc instanceof InvoerSchuifComponent)
		//{ schuifcomponenten[aantalSc] = new InvoerSchuifComponent(this ,x,y,b,h);
		//}
		if(cc instanceof PenAanCComponent)
		{ 	commandComponents[aantalCC] = new PenAanCComponent(x,y,b,h, this);
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		if(cc instanceof PenUitCComponent)
		{ 	commandComponents[aantalCC] = new PenUitCComponent(x,y,b,h, this);
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		
		if(cc instanceof VooruitCComponent)
		{ 	commandComponents[aantalCC] = new VooruitCComponent(x,y,b,h, this);
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		if(cc instanceof LinksCComponent)
		{ 	commandComponents[aantalCC] = new LinksCComponent(x,y,b,h, this);
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		if(cc instanceof RechtsCComponent)
		{ 	commandComponents[aantalCC] = new RechtsCComponent(x,y,b,h, this);
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		if(cc instanceof VulAanCComponent)
		{ 	commandComponents[aantalCC] = new VulAanCComponent(x,y,b,h, this);
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		if(cc instanceof VulUitCComponent)
		{ 	commandComponents[aantalCC] = new VulUitCComponent(x,y,b,h, this);
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		if(cc instanceof StapCComponent)
		{ 	commandComponents[aantalCC] = new StapCComponent(x,y,b,h, this);
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		if(cc instanceof HerhaalCommandComponent)
		{ 	commandComponents[aantalCC] = new HerhaalCommandComponent(x,y,b,h, this);
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		if(cc instanceof VarCComponent)
		{ 	commandComponents[aantalCC] = new VarCComponent(x,y,b,h, this);
			//varTeller++;
			//commandComponents[aantalCC].setLabel(varNamen[varTeller%26]+"  =  ");
			
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		
		
	}
	
	public void verwijder(CommandComponent cc)
	{	for(int i=0 ; i<aantalCC ; i++)
		{	if(commandComponents[i]==cc)
			{	remove(cc);
				for(int j=i ; j<aantalCC ; j++)
				{	commandComponents[j] = commandComponents[j+1];
				}
				aantalCC--;
				tekenOpnieuw();
				return;
			}
		}
	}
	
	public void tekenAchtergrond(Graphics g)
	{	Dimension dd = getSize();
		g.setColor(getBackground());
		g.fillRect(0,0,dd.width,dd.height);
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, dd.width, dd.height);
	}
	
	public CommandContainer getCommandContainerAt(int x, int y)
	{	CommandContainer cc = null;
		Component c = getComponentAt(x,y);
		if(c!=this && c!=null && c instanceof CommandContainer) 
		{	cc = (CommandContainer)c;
			return cc.getCommandContainerAt(x - cc.getLocation().x,y - cc.getLocation().y);
		}
		return null;
	}
	
	public CommandComponent getCommandComponentAt(int x, int y)
	{	CommandComponent cc = null;
		Component c = getComponentAt(x,y);
		if(c!=this && c!=null && c instanceof CommandComponent) 
		{	cc = (CommandComponent)c;
			return cc.getCommandComponentAt(x - cc.getLocation().x,y - cc.getLocation().y);
		}
		return null;
	}
	
	public void losSchuiver(SchuifComponent sc)
	{	int x = sc.getLocation().x + sc.getSize().width/2;
		int y = sc.getLocation().y + sc.getSize().height/2;
		boolean terugOpVeld = true;
		Component cc = getCommandContainerAt(x,y);
		if(cc != null && cc!=sc)
		{	((CommandContainer)cc).add(sc);
			tekenOpnieuw();
			terugOpVeld = false;
		}
		if(terugOpVeld)super.losSchuiver(sc);
	}
	
	public void zetSchuiver(SchuifComponent sc)
	{	if(sc.getParent()!=this && sc.getParent().getParent()!=this && sc.getParent().getParent().getParent()!=this)
		{	int newLx = sc.getLocation().x + sc.getParent().getLocation().x + sc.getParent().getParent().getLocation().x + sc.getParent().getParent().getParent().getLocation().x;
			int newLy = sc.getLocation().y + sc.getParent().getLocation().y + sc.getParent().getParent().getLocation().y + sc.getParent().getParent().getParent().getLocation().y;
			sc.setBounds(newLx,newLy,120,sc.getSize().height);
			
		}
		else if(sc.getParent()!=this && sc.getParent().getParent()!=this)
		{	int newLx = sc.getLocation().x + sc.getParent().getLocation().x + sc.getParent().getParent().getLocation().x;
			int newLy = sc.getLocation().y + sc.getParent().getLocation().y + sc.getParent().getParent().getLocation().y;
			sc.setBounds(newLx,newLy,120,sc.getSize().height);
		}
		else if(sc.getParent()!=this)
		{	int newLx = sc.getLocation().x + sc.getParent().getLocation().x;
			int newLy = sc.getLocation().y + sc.getParent().getLocation().y;
			sc.setBounds(newLx,newLy,120,sc.getSize().height);
		}
		if(sc.getParent() instanceof CommandComponent)
		{	//((CommandComponent)sc.getParent()).kind = null;
		}
		/*for(int i=0 ; i<aantalCC; i++)
		{	if(sc==commandComponents[i])
			{	for(int j=i ; j>0 ; j--)
				{	commandComponents[j] = commandComponents[j-1];
				}
				commandComponents[0] = (CommandComponent)sc;
				break;
			}
		}*/
		super.zetSchuiver(sc);
	}
	
	public void traceComponent(CommandComponent sc)
	{	int x = sc.getLocation().x + sc.getSize().width/2;
		int y = sc.getLocation().y + sc.getSize().height/2;
		
		for(int i=0 ; i<aantalCC; i++)
		{	commandComponents[i].showCaret(x,y,false);
		}
		tekenOpnieuw();	

		CommandComponent cc = getCommandComponentAt(x,y);
		if(cc != null && cc instanceof ProgrammaComponent && cc!=sc || cc != null && cc.getParent() instanceof CommandContainer && cc!=sc && !cc.vast)
		{	cc.showCaret(x,y,true);
			tekenOpnieuw();	
		}
		
		
	}
	
	public void zetFormuleVak(VooruitCComponent usc)
	{	sluitFormuleVak();
		actiefInvoerVak = usc;
		//formuleEditor.formuleVak = usc.commandInvoerVak.formulevak1;
		//formuleEditor.formuleVak.setEditable(true);
		//formuleEditor.formuleVak.setSelectable(true);
		zetOpSchuifLaag(usc);
	}
	
	public void sluitFormuleVak()
	{	if(actiefInvoerVak!=null)
		{	this.zetTerugSchuifLaag(actiefInvoerVak);
			//actiefInvoerVak.zetInvulWaarde();
			//formuleEditor.formuleVak.setSelected(false);
			//formuleEditor.formuleVak.setEditable(false);
			//formuleEditor.formuleVak.setSelectable(false);
			//formuleEditor.formuleVak = new FormuleVak();
			actiefInvoerVak = null;
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==scrollSlider)
		{
			programmaComponent.setLocation(programmaComponent.getLocation().x, pcLocYDefault - scrollSlider.geefStand());
			tekenOpnieuw();
		}
	}
	
}
