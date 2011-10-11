package fi.javalogoweb;

import fi.javalogoweb.schuifobjects.*;
import fi.beans.stringutils.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import logotekenap.*;

public class JavaLogoSchuifVeld extends SchuifVeld implements ActionListener
{
	private CommandComponent[] commandComponents;
	private int aantalCC;
	private ProgrammaComponent programmaComponent, pcActief;
	//private DeeltaakCComponent dtc1, dtc2, dtc3;
	private DeeltaakCComponent[] deeltaakComponenten;
	private int aantalDeeltaakComponenten = 5;
	private int pcSizeHeightDefault;
	private int pcSizeWidthDefault;
	private int pcLocXDefault;
	private int pcLocYDefault;
	private int opdrSizeWidthDefault;
	private int opdrSizeHeightDefault;
	private int opdrLocXDefault;
	private int opdrLocYDefault;
	private ScrollSlider scrollSlider;
	private JLabel tekenProgrammaLabel;
	//private ImageButton editButton1, editButton2, editButton3;
	private ImageButton[] editButtons;
	private JButton runButton;
	private JButton exportButton;
	private JButton importButton;
	private Tekenblad tekenblad;
	private DeeltaakHeader deeltaakHeader;
	
	
	public JavaLogoSchuifVeld(int x, int y, int b, int h, Tekenblad tb)
	{	super(x,y,b,h);
		
		tekenblad = tb;
		commandComponents = new CommandComponent[1000];
		
	}
	
	public void teken(Tekenblad tb)
	{	programmaComponent.teken(tb, new VarSet());
		
	}
	
	public void initialize()
	{	pcSizeWidthDefault = 200;
		pcSizeHeightDefault = 395;
		pcLocXDefault = 190;
		pcLocYDefault = 38;
		
		programmaComponent = new ProgrammaComponent(pcLocXDefault,pcLocYDefault,pcSizeWidthDefault,pcSizeHeightDefault, this);
		programmaComponent.zetVast(true);
		commandComponents[0]= programmaComponent;
		add(commandComponents[0],0);
		pcActief = programmaComponent;
		
		JPanel pBoven = new JPanel();
		pBoven.setBackground(getBackground());
		pBoven.setBounds(pcLocXDefault,0,pcSizeWidthDefault,pcLocYDefault);
		add(pBoven,0);
		
		JPanel pOnder = new JPanel();
		pOnder.setBackground(getBackground());
		pOnder.setBounds(pcLocXDefault,pcLocYDefault + pcSizeHeightDefault,pcSizeWidthDefault,500);
		add(pOnder,0);
		
		tekenProgrammaLabel = new JLabel("tekenalgoritme");
		tekenProgrammaLabel.setBackground(getBackground());
		//tekenProgrammaLabel.setAlignment(Label.CENTER);
		tekenProgrammaLabel.setFont(new Font("SansSerif",Font.PLAIN, 20));
		tekenProgrammaLabel.setBounds(pcLocXDefault+40,10,pcSizeWidthDefault, pcLocYDefault-10);
		add(tekenProgrammaLabel,0);
		
		opdrSizeWidthDefault = 160;
		opdrSizeHeightDefault = 370;
		opdrLocXDefault = 10;
		opdrLocYDefault = 50;
		
		JLabel opdrLabel = new JLabel("opdrachten");
		opdrLabel.setBackground(getBackground());
		//opdrLabel.setAlignment(Label.CENTER);
		opdrLabel.setFont(new Font("SansSerif",Font.PLAIN, 20));
		opdrLabel.setBounds(opdrLocXDefault+40,10,opdrSizeWidthDefault, opdrLocYDefault-22);
		add(opdrLabel,0);
		
		JLabel deeltaakLabel = new JLabel("deeltaken");
		deeltaakLabel.setBackground(getBackground());
		//deeltaakLabel.setAlignment(Label.CENTER);
		deeltaakLabel.setFont(new Font("SansSerif",Font.PLAIN, 20));
		deeltaakLabel.setBounds(opdrLocXDefault+40,340,opdrSizeWidthDefault, opdrLocYDefault-22);
		add(deeltaakLabel,0);
		
		
		
		commandComponents[1] = new PenAanCComponent(opdrLocXDefault,opdrLocYDefault,opdrSizeWidthDefault/2-5,25, this);
		add(commandComponents[1],0);
		
		commandComponents[2] = new PenUitCComponent(opdrLocXDefault+opdrSizeWidthDefault/2+5,opdrLocYDefault,opdrSizeWidthDefault/2-5,25, this);
		add(commandComponents[2],0);
		
		commandComponents[3] = new VooruitCComponent(opdrLocXDefault,opdrLocYDefault+30,opdrSizeWidthDefault/2-5,25, this);
		add(commandComponents[3],0);
		
		commandComponents[4] = new StapCComponent(opdrLocXDefault+opdrSizeWidthDefault/2+5,opdrLocYDefault+30,opdrSizeWidthDefault/2-5,25, this);
		add(commandComponents[4],0);
	
		commandComponents[5] = new LinksCComponent(opdrLocXDefault,opdrLocYDefault+60,opdrSizeWidthDefault/2-5,25, this);
		add(commandComponents[5],0);
		
		commandComponents[6] = new RechtsCComponent(opdrLocXDefault+opdrSizeWidthDefault/2+5,opdrLocYDefault+60,opdrSizeWidthDefault/2-5,25, this);
		add(commandComponents[6],0);
		
		commandComponents[7] = new VulAanCComponent(opdrLocXDefault,opdrLocYDefault+90,opdrSizeWidthDefault/2-5,25, this);
		add(commandComponents[7],0);
		
		commandComponents[8] = new VulUitCComponent(opdrLocXDefault+opdrSizeWidthDefault/2+5,opdrLocYDefault+90,opdrSizeWidthDefault/2-5,25, this);
		add(commandComponents[8],0);
		
		
		commandComponents[9] = new HerhaalCommandComponent(opdrLocXDefault,opdrLocYDefault+130,opdrSizeWidthDefault,48, this);
		add(commandComponents[9],0);
		
		commandComponents[10] = new KeuzeCommandComponent(opdrLocXDefault,opdrLocYDefault+190,opdrSizeWidthDefault,48, this);
        add(commandComponents[10],0);
        
		commandComponents[11] = new VarCComponent(opdrLocXDefault,opdrLocYDefault+250,opdrSizeWidthDefault,25, this);
		add(commandComponents[11],0);
		
		deeltaakComponenten = new DeeltaakCComponent[aantalDeeltaakComponenten];
		editButtons = new ImageButton[aantalDeeltaakComponenten];
		for(int i=0 ; i<aantalDeeltaakComponenten ; i++)
		{
			deeltaakComponenten[i] = new DeeltaakCComponent(opdrLocXDefault+30,opdrLocYDefault+330+30*i,opdrSizeWidthDefault-30,25, this);
			deeltaakComponenten[i].setCommandName("deeltaak"+i);
			add(deeltaakComponenten[i]);
			commandComponents[11+2*i] = deeltaakComponenten[i];
			
			ProgrammaComponent pc = new ProgrammaComponent(pcLocXDefault,pcLocYDefault,pcSizeWidthDefault,pcSizeHeightDefault, this);
			pc.zetVast(true);
			deeltaakComponenten[i].zetDeeltaakContainer(pc);
			commandComponents[12+2*i]= pc;
			
			editButtons[i] = new ImageButton(JavaLogoWeb.editImage);
			editButtons[i].setBounds(opdrLocXDefault,opdrLocYDefault+335+30*i,20,20);
			editButtons[i].addActionListener(this);
			add(editButtons[i],0);
		}
		
		/*dtc1 = new DeeltaakCComponent(opdrLocXDefault+30,opdrLocYDefault+270,opdrSizeWidthDefault-30,25, this);
		dtc1.setCommandName("deeltaak1");
		add(dtc1);
		commandComponents[11] = dtc1;
		
		ProgrammaComponent pc1 = new ProgrammaComponent(pcLocXDefault,pcLocYDefault,pcSizeWidthDefault,pcSizeHeightDefault, this);
		pc1.zetVast(true);
		dtc1.zetDeeltaakContainer(pc1);
		commandComponents[12]= pc1;
		
		editButton1 = new ImageButton(JavaLogoWeb.editImage);
		editButton1.setBounds(opdrLocXDefault,opdrLocYDefault+275,20,20);
		editButton1.addActionListener(this);
		add(editButton1);
		
		dtc2 = new DeeltaakCComponent(opdrLocXDefault+30,opdrLocYDefault+300,opdrSizeWidthDefault-30,25, this);
		dtc2.setCommandName("deeltaak2");
		add(dtc2);
		commandComponents[13] = dtc2;
		
		ProgrammaComponent pc2 = new ProgrammaComponent(pcLocXDefault,pcLocYDefault,pcSizeWidthDefault,pcSizeHeightDefault, this);
		pc2.zetVast(true);
		dtc2.zetDeeltaakContainer(pc2);
		commandComponents[14]= pc2;
		
		editButton2 = new ImageButton(JavaLogoWeb.editImage);
		editButton2.setBounds(opdrLocXDefault,opdrLocYDefault+305,20,20);
		editButton2.addActionListener(this);
		add(editButton2);
		
		dtc3 = new DeeltaakCComponent(opdrLocXDefault+30,opdrLocYDefault+330,opdrSizeWidthDefault-30,25, this);
		dtc3.setCommandName("deeltaak3");
		add(dtc3);
		commandComponents[15] = dtc3;
		
		ProgrammaComponent pc3 = new ProgrammaComponent(pcLocXDefault,pcLocYDefault,pcSizeWidthDefault,pcSizeHeightDefault, this);
		pc3.zetVast(true);
		dtc3.zetDeeltaakContainer(pc3);
		commandComponents[16]= pc3;
		
		editButton3 = new ImageButton(JavaLogoWeb.editImage);
		editButton3.setBounds(opdrLocXDefault,opdrLocYDefault+335,20,20);
		editButton3.addActionListener(this);
		add(editButton3);
		*/
		
		
		aantalCC = 18;
		
		scrollSlider = new ScrollSlider(pcSizeHeightDefault-25,0,false);
		scrollSlider.setBackground(getBackground());
		scrollSlider.zetStand(0);
		scrollSlider.setLocation(pcSizeWidthDefault+pcLocXDefault+5,pcLocYDefault+12);
		scrollSlider.addActionListener(this);
		super.add(scrollSlider);
		scrollSlider.setVisible(false);
		
		runButton = new JButton("Run");
		runButton.setBounds(190,430,200,25);
		runButton.setMargin(new Insets(3,5,3,5));
		runButton.addActionListener(this);
		add(runButton,0);
		
		importButton = new JButton("Import code");
		importButton.setBounds(190,460,95,25);
		importButton.setMargin(new Insets(3,5,3,5));
		importButton.addActionListener(this);
		add(importButton,0);
		
		exportButton = new JButton("Export code");
		exportButton.setBounds(295,460,95,25);
		exportButton.setMargin(new Insets(3,5,3,5));
		exportButton.addActionListener(this);
		add(exportButton,0);
		
		deeltaakHeader = new DeeltaakHeader(pcLocXDefault,pcLocYDefault-11, pcSizeWidthDefault, 25);
		//deeltaakHeader.zetDeeltaakCComponent(dtc1);
		deeltaakHeader.addActionListener(this);
		
	}
	
	public void scroll(boolean adjustLocation)
	{	int extraHoogte = pcActief.getSize().height - pcSizeHeightDefault;
		if(extraHoogte > 0 && adjustLocation)
		{	pcActief.setLocation(pcActief.getLocation().x,   pcLocYDefault - extraHoogte);
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
		{	pcActief.setLocation(pcActief.getLocation().x, pcLocYDefault);
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
		if(cc instanceof KeuzeCommandComponent)
        {   commandComponents[aantalCC] = new KeuzeCommandComponent(x,y,b,h, this);
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
		if(cc instanceof DeeltaakCComponent)
		{ 	commandComponents[aantalCC] = new DeeltaakCComponent(x,y,b,h, this);
			((DeeltaakCComponent)commandComponents[aantalCC]).setCommandName(((DeeltaakCComponent)cc).getCommandName());
			((DeeltaakCComponent)commandComponents[aantalCC]).zetDeeltaakContainer(((DeeltaakCComponent)cc).geefProgrammaComponent());
			((DeeltaakCComponent)commandComponents[aantalCC]).setDtcParent(((DeeltaakCComponent)cc).getDtcParent());
			add(commandComponents[aantalCC],0);
			aantalCC++;
		}
		
		
	}
	
	public void voegToe(CommandComponent cc)
	{	commandComponents[aantalCC] = cc;
		aantalCC++;
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
		g.setColor(getBackground());
		g.fillRect(0, 0, dd.width, dd.height);
		g.setColor(Color.black);
		//g.drawLine(dd.width-1, 0, dd.width-1, dd.height-59);
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
	{	int x = sc.getLocation().x;// + sc.getSize().width/2;
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
			sc.setBounds(newLx,newLy,opdrSizeWidthDefault/2-5,sc.getSize().height);
			//((CommandComponent)sc).zetMaat();
		}
		else if(sc.getParent()!=this && sc.getParent().getParent()!=this)
		{	int newLx = sc.getLocation().x + sc.getParent().getLocation().x + sc.getParent().getParent().getLocation().x;
			int newLy = sc.getLocation().y + sc.getParent().getLocation().y + sc.getParent().getParent().getLocation().y;
			sc.setBounds(newLx,newLy,opdrSizeWidthDefault/2-5,sc.getSize().height);
			//((CommandComponent)sc).zetMaat();
		}
		else if(sc.getParent()!=this)
		{	int newLx = sc.getLocation().x + sc.getParent().getLocation().x;
			int newLy = sc.getLocation().y + sc.getParent().getLocation().y;
			sc.setBounds(newLx,newLy,opdrSizeWidthDefault/2-5,sc.getSize().height);
			//((CommandComponent)sc).zetMaat();
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
		((CommandComponent)sc).zetMaat();
	}
	
	public void traceComponent(CommandComponent sc)
	{	int x = sc.getLocation().x;//sc.getLocation().x + sc.getSize().width/2;
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
	
	private void exportFrame(String contents) {
		final TextArea area = new TextArea(contents, 0, 0, TextArea.SCROLLBARS_NONE);
		Frame f = new Frame("Code van het algoritme");
		f.setLayout(new BorderLayout());
		f.add(area,BorderLayout.CENTER);
		f.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				area.requestFocus();
				area.setCaretPosition(0);
			}
			public void windowActivated(WindowEvent e) {
				area.selectAll();
			}
			public void windowClosing(WindowEvent e) {
					e.getWindow().dispose();
			} });
		f.pack();
		f.setVisible(true);
		f.toFront();
	}
	
	private void importFrame(String contents) {
		final TextArea area = new TextArea(contents, 0, 0, TextArea.SCROLLBARS_NONE);
		Frame f = new Frame("Code van het algoritme");
		f.setLayout(new BorderLayout());
		f.add(area,BorderLayout.CENTER);
		f.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				area.requestFocus();
				area.setCaretPosition(0);
			}
			public void windowActivated(WindowEvent e) {
				area.selectAll();
			}
			public void windowClosing(WindowEvent e) {
				setCode(area.getText());	
				e.getWindow().dispose();
			} });
		f.pack();
		f.setVisible(true);
		f.toFront();
	}
	
	public DeeltaakCComponent[] geefDtcCommands()
	{
		//DeeltaakCComponent[] dtcs = new DeeltaakCComponent[3];
		//dtcs[0] = dtc1;
		//dtcs[1] = dtc2;
		//dtcs[2] = dtc3;
		//return dtcs;
		
		return deeltaakComponenten;
	}
	
	public void setCode(String code)
	{	code += "\n";
		String[] codeParts = StringUtils.split(code,"Deeltaak:");
		for(int i=1 ; i<codeParts.length ; i++)
		{
			if(codeParts.length>i)
			{	deeltaakComponenten[i-1].setCommandName(codeParts[i].substring(0,codeParts[i].indexOf("\n")).trim());
				deeltaakComponenten[i-1].geefProgrammaComponent().removeAll();
				deeltaakComponenten[i-1].geefProgrammaComponent().setCode(codeParts[i].substring(codeParts[i].indexOf("\n")+1));
				
			}
		}
		
		/*if(codeParts.length>1)
		{	dtc1.setCommandName(codeParts[1].substring(0,codeParts[1].indexOf("\n")).trim());
			dtc1.geefProgrammaComponent().setCode(codeParts[1].substring(codeParts[1].indexOf("\n")+1));
		}
		if(codeParts.length>2)
		{	dtc2.setCommandName(codeParts[2].substring(0,codeParts[2].indexOf("\n")).trim());
			dtc2.geefProgrammaComponent().setCode(codeParts[2].substring(codeParts[2].indexOf("\n")+1));
		}
		if(codeParts.length>3)
		{	dtc3.setCommandName(codeParts[3].substring(0,codeParts[3].indexOf("\n")).trim());
			dtc3.geefProgrammaComponent().setCode(codeParts[3].substring(codeParts[3].indexOf("\n")+1));
		}*/
		programmaComponent.removeAll();
		programmaComponent.setCode(codeParts[0]);
	}
	
	public String getCode()
	{	String s0 = programmaComponent.getCode("");
		for(int i=0 ; i<aantalDeeltaakComponenten ; i++)
		{	String s = "\nDeeltaak: " + deeltaakComponenten[i].commandString + "\n" + deeltaakComponenten[i].geefProgrammaComponent().getCode("");
			s0 = s0 + s;
		}
		//String s1 = "\nDeeltaak: " + dtc1.commandString + "\n" + dtc1.geefProgrammaComponent().getCode("");
		//String s2 = "\nDeeltaak: " + dtc2.commandString + "\n" + dtc2.geefProgrammaComponent().getCode("");
		//String s3 = "\nDeeltaak: " + dtc3.commandString + "\n" + dtc3.geefProgrammaComponent().getCode("");
		//return s0+s1+s2+s3+"\n";
		return s0+"\n";
	}
	
	public void actionPerformed(ActionEvent e)
	{	for(int i=0 ; i<aantalDeeltaakComponenten ; i++)
		{	if(e.getSource()==editButtons[i])
			{	remove(pcActief);
				tekenProgrammaLabel.setVisible(false);
				runButton.setVisible(false);
				deeltaakComponenten[i].addDeeltaakContainer();
				pcActief = deeltaakComponenten[i].geefProgrammaComponent();
				deeltaakHeader.zetDeeltaakCComponent(deeltaakComponenten[i]);
				add(deeltaakHeader,0);
				scroll(true);
			}
			
		}
		if(e.getSource()==runButton)
		{	tekenblad.tekenOpnieuw();
		}
		else if(e.getSource()==importButton)
		{	importFrame("");
		}
		else if(e.getSource()==exportButton)
		{	exportFrame(getCode());
		}
		else if(e.getSource()==scrollSlider)
		{
			pcActief.setLocation(pcActief.getLocation().x, pcLocYDefault - scrollSlider.geefStand());
			tekenOpnieuw();
		}
		
		/*else if(e.getSource()==editButton1)
		{	remove(pcActief);
			tekenProgrammaLabel.setVisible(false);
			runButton.setVisible(false);
			dtc1.addDeeltaakContainer();
			pcActief = dtc1.geefProgrammaComponent();
			deeltaakHeader.zetDeeltaakCComponent(dtc1);
			add(deeltaakHeader,0);
		}
		else if(e.getSource()==editButton2)
		{	remove(pcActief);
			tekenProgrammaLabel.setVisible(false);
			runButton.setVisible(false);
			dtc2.addDeeltaakContainer();
			pcActief = dtc2.geefProgrammaComponent();
			deeltaakHeader.zetDeeltaakCComponent(dtc2);
			add(deeltaakHeader,0);
		}
		else if(e.getSource()==editButton3)
		{	remove(pcActief);
			tekenProgrammaLabel.setVisible(false);
			runButton.setVisible(false);
			dtc3.addDeeltaakContainer();
			pcActief = dtc3.geefProgrammaComponent();
			deeltaakHeader.zetDeeltaakCComponent(dtc3);
			add(deeltaakHeader,0);
		}*/
		else  if(e.getSource()==deeltaakHeader)
		{	if(e.getActionCommand().equals("close"))
			{	remove(deeltaakHeader);
				remove(pcActief);
				pcActief = programmaComponent;
				add(pcActief,0);
				tekenProgrammaLabel.setVisible(true);
				runButton.setVisible(true);
				scroll(true);
			}
			else
			{	for(int i=0 ; i<aantalCC; i++)
				{	CommandComponent cc = commandComponents[i];
					if(cc instanceof DeeltaakCComponent)
					{	DeeltaakCComponent dtc = ((DeeltaakCComponent)cc);
						if(dtc.getDtcParent().equals(deeltaakHeader.geefDeeltaakCComponent()))
						{	dtc.setCommandName(e.getActionCommand());
							
						}
					}
				}
				deeltaakHeader.geefDeeltaakCComponent().setCommandName(e.getActionCommand());
					
			}
			
		}
		tekenOpnieuw();
	
	}
	
}
