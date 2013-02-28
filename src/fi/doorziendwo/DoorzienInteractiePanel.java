package fi.doorziendwo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.awt.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

import fi.beans.base64code.StringCodeObject;
 

public class DoorzienInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel
{
	ViewPanel viewPanel;
	DoorzienPanel doorzienPanel;
	
	int viewPanelOffset = 0;
	
	boolean viewerModus = false; //true;
	
	boolean rotateOption = true;
	boolean borderOption = false;
	boolean designOption = false;
	boolean resetOption = false;
	boolean foldOption = false;
	
	boolean noSetBounds;
	
	static boolean letters = false;
	
	public DoorzienInteractiePanel()
	{
		setLayout(null);
	}
	
	public void setBackground(Color c)
	{
		if (viewPanel != null)
			viewPanel.setBackground(c);
		
		if (doorzienPanel != null)
			doorzienPanel.setBackground(c);
		
		super.setBackground(c);
	}
	
/*	
// tijdelijk
public void viewerObjectNaarDoorzien()
{
	ScormedObject3D sco = viewPanel.scormedObject3D;
	if (sco != null)
	{
		Hashtable scoTable = NoSer.getScormObject3DState(sco);
		doorzienPanel.setState(scoTable);
	}
}
*/
	
	public void setViewerModus(boolean b, boolean reset)
	{
		viewerModus = b;
		if (viewerModus)
		{	viewPanel.setVisible(true);
			doorzienPanel.setVisible(false);
		}
		else
		{	if ((viewPanel.doorzienFrame != null) && viewPanel.doorzienFrame.isVisible())
			{
				viewPanel.getScormedObject3D();	        
				viewPanel.doorzienFrame.dispose();
				viewPanel.restart();
			}
			viewPanel.setVisible(false);
			doorzienPanel.setVisible(true);
		}
		
		if (reset)
			DrawConstants.reset();
		
//System.out.println("set viewerModus = " + viewerModus);		
	}
	
	public void setRotateOption(boolean b)
	{	rotateOption = b;
		if (viewerModus)
			viewPanel.setMouse(rotateOption);
		else
			doorzienPanel.setRotateOption(rotateOption);
	}

	public void setBorderOption(boolean b)
	{	borderOption = b;
		if (viewerModus)
			viewPanel.setBordered(borderOption);
		else
			doorzienPanel.setBorderOption(borderOption);
	}

	public void setDesignOption(boolean b)
	{	designOption = b;
		if (viewerModus)
			viewPanel.setDesignOption(designOption);
		else
			doorzienPanel.setDesignOption(designOption);
	}

	public void setResetOption(boolean b)
	{	resetOption = b;
		if (viewerModus)
			viewPanel.setResetOption(resetOption);
		else
			doorzienPanel.setResetOption(resetOption);
	}
	
	public void setFoldOption(boolean b)
	{	foldOption = b;
		if (viewerModus)
			viewPanel.setFoldOption(foldOption);
		else
			doorzienPanel.setFoldOption(foldOption);
	}
	
	public void zetDemo(boolean b)
	{	doorzienPanel.zetDemo(b);
	}

	public void zetFigurenMenuOptie(boolean b)
	{	doorzienPanel.zetFigurenMenuOptie(b);
	}
	
	public void zetOptiesMenuOptie(boolean b)
	{	doorzienPanel.zetOptiesMenuOptie(b);
	}

	public void zetHelpBarOptie(boolean b)
	{	doorzienPanel.zetHelpBarOptie(b);
	}
	
	public void zetLijnTekenOptie(boolean b)
	{	doorzienPanel.zetLijnTekenOptie(b);
	}
	
	public void zetLijnVerlengOptie(boolean b)
	{	doorzienPanel.zetLijnVerlengOptie(b);
	}
	
	public void zetVlakTekenOptie(boolean b)
	{	doorzienPanel.zetVlakTekenOptie(b);
	}
	
	public void zetEvenwijdigVlakOptie(boolean b)
	{	doorzienPanel.zetEvenwijdigVlakOptie(b);
	}
		
	public void zetToonDoorsnedeOptie(boolean b)
	{	doorzienPanel.zetToonDoorsnedeOptie(b);
	}
	
	public void zetSplitsFiguurOptie(boolean b)
	{	doorzienPanel.zetSplitsFiguurOptie(b);
	}
	
	public void zetBouwplaatOptie(boolean b)
	{	doorzienPanel.zetBouwplaatOptie(b);
	}
	
	public void zetPreviewOptie(boolean b)
	{	doorzienPanel.zetPreviewOptie(b);
	}
	
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
		
		boolean viewerModus = true;

		if (b.containsKey("viewerModus")) 
		{	viewerModus = ((Boolean) b.get("viewerModus")).booleanValue();
//System.out.println("dip zetOpdracht contains vm = " + viewerModus);		
		}

		if (viewerModus)
		{
			
//System.out.println("dip zetOpdracht vm = " + viewerModus);

			boolean rotateOption = true;
			boolean borderOption = false;
			boolean designOption = false;
			boolean resetOption = false;
			boolean foldOption = false;
		
			if (b.containsKey("rotateOption")) 
				rotateOption = ((Boolean) b.get("rotateOption")).booleanValue();
			if (b.containsKey("borderOption")) 
				borderOption = ((Boolean) b.get("borderOption")).booleanValue();
			if (b.containsKey("designOption")) 
				designOption = ((Boolean) b.get("designOption")).booleanValue();
			if (b.containsKey("resetOption")) 
				resetOption = ((Boolean) b.get("resetOption")).booleanValue();
			if (b.containsKey("foldOption")) 
				foldOption = ((Boolean) b.get("foldOption")).booleanValue();
	   
			this.rotateOption = rotateOption;
			this.borderOption = borderOption;
			this.designOption = designOption;
			this.resetOption = resetOption;
			this.foldOption = foldOption;
		
			viewPanel.setMouse(rotateOption);
			viewPanel.setBordered(borderOption);
			viewPanel.setDesignOption(designOption);
			viewPanel.setResetOption(resetOption);
			viewPanel.setFoldOption(foldOption);


			String startFiguurString = null;
			if (b.containsKey("startFiguurString")) 
			{	startFiguurString = (String) b.get("startFiguurString");
				viewPanel.setState(startFiguurString);
			}
			
			
/*			
			String startFiguurString = null;
			if (b.containsKey("startFiguurString")) 
			{	startFiguurString = (String) b.get("startFiguurString");
				Object o = StringCodeObject.decodeStringToObject(startFiguurString);
				ScormedObject3D sco = (ScormedObject3D) o;				
				if (sco != null)
				{	Hashtable scoTable = NoSer.getScormObject3DState(sco);
					for (Enumeration e = scoTable.keys(); e.hasMoreElements();)
					{	Object aKey = e.nextElement();
						Object aValue = scoTable.get(aKey);
						b.put(aKey, aValue);
					}
				}
			}
			boolean demo = true;
			b.put("demo", new Boolean(demo));
			viewerModus = false;
			this.viewerModus = viewerModus;
			b.put("viewerModus", new Boolean(viewerModus));

			doorzienPanel.zetOpdracht(b, randomVars, randomValues);
*/			
		}
		else
		{	
//System.out.println("dip zetOpdracht vm = " + viewerModus);

			doorzienPanel.zetOpdracht(b, randomVars, randomValues);
			
		}
			
		setViewerModus(viewerModus, false);
		
	}
	
	public void setState(Hashtable b)
	{
//System.out.println("dip setState");		
		
		boolean viewerModus = true;
		
		if (b.containsKey("viewerModus")) 
		{	viewerModus = ((Boolean) b.get("viewerModus")).booleanValue();
//System.out.println("dip setState contains vm");		
		}
		
		if (viewerModus)
		{
			
//System.out.println("dip setState vm = " + viewerModus);

			String startFiguurString = null;
			if (b.containsKey("startFiguurString")) 
			{	startFiguurString = (String) b.get("startFiguurString");
				viewPanel.setState(startFiguurString);
				
//System.out.println("dip setState contains sfs");				
			}
			


/*
			String startFiguurString = null;
			if (b.containsKey("startFiguurString")) 
			{	
				
System.out.println("dip setState contains sfs");

				startFiguurString = (String) b.get("startFiguurString");
				Object o = StringCodeObject.decodeStringToObject(startFiguurString);
				ScormedObject3D sco = (ScormedObject3D) o;				
				if (sco != null)
				{	Hashtable scoTable = NoSer.getScormObject3DState(sco);
					for (Enumeration e = scoTable.keys(); e.hasMoreElements();)
					{	Object aKey = e.nextElement();
						Object aValue = scoTable.get(aKey);
						b.put(aKey, aValue);
					}
				}
			}
			boolean demo = true;
			b.put("demo", new Boolean(demo));
			viewerModus = false;
			b.put("viewerModus", new Boolean(viewerModus));
			
			doorzienPanel.setState(b);
*/
		}
		else
		{
//System.out.println("dip setState vm = " + viewerModus);			
			doorzienPanel.setState(b);
		}
		
		setViewerModus(viewerModus, false);
	}
	
	public void setEditState(Hashtable b)
	{
		
//System.out.println("dip setEditState");

		boolean viewerModus = true;

		if (b.containsKey("viewerModus")) 
		{	viewerModus = ((Boolean) b.get("viewerModus")).booleanValue();
//System.out.println("dip setEditState contains + vm = " + viewerModus);		
		}
		
		if (viewerModus)
		{
//System.out.println("dip setEditState + vm = " + viewerModus);			
		
			boolean rotateOption = true;
			boolean borderOption = false;
			boolean designOption = false;
			boolean resetOption = false;
			boolean foldOption = false;
			
			if (b.containsKey("rotateOption")) 
				rotateOption = ((Boolean) b.get("rotateOption")).booleanValue();
			if (b.containsKey("borderOption")) 
				borderOption = ((Boolean) b.get("borderOption")).booleanValue();
			if (b.containsKey("designOption")) 
				designOption = ((Boolean) b.get("designOption")).booleanValue();
			if (b.containsKey("resetOption")) 
				resetOption = ((Boolean) b.get("resetOption")).booleanValue();
			if (b.containsKey("foldOption")) 
				foldOption = ((Boolean) b.get("foldOption")).booleanValue();
	   
			this.rotateOption = rotateOption;
			this.borderOption = borderOption;
			this.designOption = designOption;
			this.resetOption = resetOption;
			this.foldOption = foldOption;
		
			viewPanel.setMouse(rotateOption);
			viewPanel.setBordered(borderOption);
			viewPanel.setDesignOption(designOption);
			viewPanel.setResetOption(resetOption);
			viewPanel.setFoldOption(foldOption);
			
			String startFiguurString = null;
			if (b.containsKey("startFiguurString")) 
			{	startFiguurString = (String) b.get("startFiguurString");
				Object o = StringCodeObject.decodeStringToObject(startFiguurString);
				ScormedObject3D sco = (ScormedObject3D) o;				
				if (sco != null)
				{	Hashtable scoTable = NoSer.getScormObject3DState(sco);
					for (Enumeration e = scoTable.keys(); e.hasMoreElements();)
					{	Object aKey = e.nextElement();
						Object aValue = scoTable.get(aKey);
						b.put(aKey, aValue);
					}
				}
			}
			boolean demo = true;
			b.put("demo", new Boolean(demo));
//			viewerModus = false;
//			this.viewerModus = viewerModus;
//			b.put("viewerModus", new Boolean(viewerModus));
			
			doorzienPanel.setEditState(b);
			
		}
		else
		{	doorzienPanel.setEditState(b);

		}
		
		//setViewerModus(viewerModus, false);
		setViewerModus(false, false);

	}
	
	public Hashtable getState()
	{
		
//System.out.println("dip getState");

		if (viewerModus)
		{

//System.out.println("dip getState vm = " + viewerModus);

			Hashtable h = viewPanel.getState();
			h.put("viewerModus", new Boolean(viewerModus));
		
			return h;
		}
		else
		{
//System.out.println("dip getState vm = " + viewerModus);			
			Hashtable h = doorzienPanel.getState();
			h.put("viewerModus", new Boolean(viewerModus));
			
			return h;
		}
	}
	
	public Hashtable getEditState()
	{
		
//System.out.println("dip getEditState");

		boolean viewerModus = false;

		viewerModus = this.viewerModus;


		if (viewerModus)
		{
//System.out.println("dip getEditState + vm " + viewerModus);			
			
			boolean rotateOption;
			boolean borderOption;
			boolean designOption;
			boolean resetOption;
			boolean foldOption;
		
			String startFiguurString = null;
		
			viewerModus = this.viewerModus;
		
			rotateOption = this.rotateOption;
			borderOption = this.borderOption;
			designOption = this.designOption;
			resetOption = this.resetOption;
			foldOption = this.foldOption;
		
			startFiguurString = viewPanel.getStateString();

			Hashtable h = new Hashtable();
	    
		    h.put("viewerModus", new Boolean(viewerModus));
		    
		    h.put("rotateOption", new Boolean(rotateOption));
		    h.put("borderOption", new Boolean(borderOption));
		    h.put("designOption", new Boolean(designOption));
		    h.put("resetOption", new Boolean(resetOption));
		    h.put("foldOption", new Boolean(foldOption));

		    h.put("startFiguurString", startFiguurString);
//System.out.println("put viewerModus = " + viewerModus);		

			return h;
		}
		else
		{
//System.out.println("dip getEditState + vm " + viewerModus);

			Hashtable h = doorzienPanel.getEditState();
			
			h.put("viewerModus", new Boolean(viewerModus));

			return h;
		}
		
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new DoorzienInteractieEditPanel();
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
		
		if (h == 1)
			return;
		
		if ((getLocation().x == x) && (getLocation().y == y) &&
			(getSize().width == b) && (getSize().height == h))
			return;
			
		super.setBounds(x, y, b, h);
//System.out.println("dzip set bounds " + b + " " + h);
		
		if (viewPanel == null)
		{
			
//System.out.println("Panels created");
//System.out.println("viewerModus " + viewerModus);

			viewPanel = new ViewPanel(viewPanelOffset, viewPanelOffset,
					                  b - 2 * viewPanelOffset, h - 2 * viewPanelOffset);
			viewPanel.setVisible(viewerModus);
			add(viewPanel);
			
			viewPanel.setBackground(Color.white);
			
			viewPanel.setBordered(borderOption);
			viewPanel.setMouse(rotateOption);
			viewPanel.setDesignOption(designOption);
			viewPanel.setResetOption(resetOption);
			viewPanel.setFoldOption(foldOption);
			
			String editModeState = "H4sIAAAAAAAAAK1YDWwT5xn+7MRx/gohW/lrFlGgf7TENOFnqbfZECc0lSEpztJgr1su9iU+ON9dzuc4NiplQmo1mo31Zz9sk9BGWdU0LRObtqxodaZ2IKqu6yat0FYTqFW3MVagQtvYisbe97vvLndnA8nak+7z973f3/u+z/O+932e+IB40ippHBSaErKs5gVeSmTlpkhcVlN8omtgGx/XWkI/C6bOkcKhA26yKEQ8nDQk8lFSNSgMZVS+LaNFScWgIIp8AoUip2m8xCdCpFbkpSEt2cHFNVmNEq/IQ4+a7iTlKTnBd5JKKZMKCxIPkiqodosc1GEJWUx0sOXmQl2X6xIYqXCCpPXkFNCgVrF1VSuqjPoKshQl1emknO2U0kKCD5HqvCyndD3CpCwFGpJF4UHBZzHZt4nTVGG0JeQPkwrcVU4Pk53EFWa7tCVlOc1LGlnonEj1o/PmaUl0h+61jaqcUTTS6Bxu6aWT5sKkDtiwK6MLcdeyMKmfFveoPL9FlkHpJc7FLCM2g09huTkwz7IFW+0GkEY0TtXACYBCsRFUTvWpHZHFTApGqwKgR+rD27gRzgc2Dvl0mX9UITN5ymjpCpzZh8/3gGULHCwzXH5XMHdj/fuPT7iJN0Qq0nFO5PuMylajEkVcVGEI0C8Gr5dHbKn+5aqcvRvNrtLrzZZ6C62PKsH+3UsOXzr97hT7NdoldDRWTp7Y82h6ndTgJmUh4hqFNwdvHtZK9VNDg9xpm/mUPjWBs8d++/2bzw4ETr38rcnHLj4c4Dd0Z4PN/Xpv4cjmxTsDhWzgWOt/Wi5NPBE4f2/+wJn7x9ncU56fTp17Jlg4u6Jw6M3EfwOvXnjKX133noIPqNrgUNXGrMj2qlUPjx2Nuok7Sm5Iy6oWyQysV1Uulw4Tr0yHpk14M5ogMif6R4udYOQB/58mnxzzXnzETeZBnMYhHHg1wkP8exMCl4JaiFRijQe5mRQgZjHexTYa9BjplGwQ9XOh3surGj8a5gZ4ESQ1TCLEMRNUyhlNhAQBicA7IqSFAZEHEujbUiRjsAddCygRuzqly2VgGovmCoVTYYESHDKNjJFqTTW00MjiopVNtsUgWiz6a+RTseJwiZHKEbYY6nAjhk/go5/n/9oXHjfIUgFvJQ0XF8Oekcn5m1F1S23wGJbuW/rPJ33/8t/jJoTGaAWQZH7psct+t87bvXzRuJssjBIPn1K0nAWweUkhkeClLt39m0zgumWIvtLAQJjmezkxAwhVD3BqzoJSmHjisggMJHW6d7is5mtDCfimKqFy2R4hvh28545FQcAnhnjkChV0Ahi6AIaDZF6seAmvICUM7zZCQhXSW3jI2XE+BTp0DaLYA8BL8EnjRMYb/PbQXdmkWmYRXRNlDcAthZpLQQO9AMHtNIcyQaUu4BWzrXZPTwA3ZCRB22zdtEYnCzWO7uvgD8rmW7Jrucs11dPqfmBiez/G+xy0vAksb6Jaug4u2F23tyXjJp4OjANRSXKdxDOig+COg7dumnYWRUB3WUQB36DrB9WhATocPd2BwURbqMYyXY0rV64QSDbAOsDmgb1LWj89+eC7Br3KGSEBpU0v9t868rfDB21d1geGMeimDfB/9pa3X3r9l/E/mrOoB26mG8OLWc4quGIXvOkccccPWlFXFNzGdHCz75AXo0EfW2OoFLjgUNE60fZgx51gweKiyDMzwcEtLz939Ov3nbSbUmPs4dzr2pqU7jV+S69s/FIrmlEDxXBGiZa+RnBHrL72xeNtweENS184Of/LU5vaxTl3HXmJ9Q7feunZiYMfBhVp/7k9Nb+f6v9J6nJTn+ro/SLXcP7yv5Wprd+4qL138ahj5b5Dt324ZtkPp/xta+eM7K5iur9TuzK77ruRwivLL/v4sYHC3/c+/4Zv2cqrIwDOZ6m1CVNrE0utz/+h9y9nPrNjo+l1jbja4A3Bey+8G+l+N5nLYEC1vTr+fur+KRq1y+mOtxg7XpsBK4q68ReZVcaYVbjgnGfB5BrMcsBSehEnLYzHAB4712KxZlYUSB5/5897Uvmgunpt2eFfLAzagDJ7v5TNhPb/+GtBRhDs/DwWn2OGf0Byq1aeGCocPr5j8rVTrYW3f/TRW+2vTV0DUOxZzyBbD287CmgRKgFa81LpdOOxXZ8EaJgOMC24lNL+LFgDaSagYWUzdTwWq+3hWdLu62KDlQj1LxZ+BsbIPm9vJDo3mPZOvkJ+/XTQFnEmHc7vfmzsqw0LCkfvWbyzJj02Ewiw0kddjwUG0IZiDIIrVj3x7Z5ffWwM8PUQ/cjjvQoGgf8Hg67SnrfGxzoaJDPHoMfh+a/8Zlf7iTeOBFkmxM4v0Fiwm1FgGARmicFW8HwHVmjRXgKD9U+LJxPjHzt5uYgeA4iBR5kBY2eKAUdDwHT18MxdTU8dftOjGaYUSywBllgCs0gsWBEooU2X5kq4NPKP22N5of+TSC3oVg81z8GHWZ40ilML9eswZfksMwj1a8ZC5gL7+AbYxzdw/Y9vcaKgfs1RztLbqAuOp3X084w3SXYseus7D8V2+Q+5ykgZXCniHBw7BS3XKcVVejLvJLW8SGttckbS4HjMmiFO4xxXKf1uNv3Pg8t5IGADvvl63/669B2icSCoBr3qHWe2DfLoqTufDSb3dR5wk2q4tyR5YSgJV9cK/f+qEPFkhYSWxIsFvS2tQjsbzebd9mazvdlib662N9cYzRHriR/Levul0MiQeCmsvO6lEHsX4QysLMFiIxZRLLZhkcfiUcrxBSZJKu0k8ZsfnB6TM3p2MwlQqbDHvsY0Ram51Mguk7F60r3aeRU7H8QihcUeLB7CwofFfdM726lHQ5p+rbaaTNSTpqGhMqoo/wMeVbsO3RQAAA==";
			viewPanel.setState(editModeState);

			doorzienPanel = new DoorzienPanel(x, y, b, h);
			doorzienPanel.setVisible(!viewerModus);
			add(doorzienPanel);
			
		}
		else
		{
//System.out.println("Panels setBounds");
			
			viewPanel.setBounds(x, y, b, h);
			doorzienPanel.setBounds(x, y, b, h);
			
		}

	}
	
	public void wis()
	{
		
	}
	
	public void zetMaat()
	{
		
	}
	
	public int geefAsHoogte()
	{
		return 0;
	}
	
	public int getIpId()
	{
		return 0;
	}
	
	public String getIpExpString()
	{
		return null;
	}
	
	public int getScore()
	{ 
		return 0;
	}
	
	public int getScoreMax()
	{
		return 10;
	}
	
	public boolean isCorrect()
	{
		return true;
	}
	
	public boolean isFout()
	{
		return false;
	}
	
	public void zetMode(int mode)
	{
		
	}
	
	public void zetNagekeken(boolean b)
	{
		
	}
	
    public void stop()
    {
    	
    }
    
    public void start()
    {
    	
    }
    
    public void destroy()
    {
    	
    }
    
    public void opnieuw()
    {
    	
    }
    
    public void kijkNa()
    {
    	
    }
    
    public void kijkNa(int stapNr)
    {
    	
    }
    
    public void addActionListener(ActionListener al)
    {
    	
    }
    
	public void actionPerformed(ActionEvent e)
	{
		
	}

	
	public void zetBreedte(int b)
	{
		
	}
	
	public void zetHoogte(int h)
	{
		
	}
	

    

	
    
}
