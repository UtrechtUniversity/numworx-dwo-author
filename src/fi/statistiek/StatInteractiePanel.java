package fi.statistiek;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.statistiek.types.ColumnType;

/**
 * Statistiek InteractiePanel MVC Controller
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class StatInteractiePanel extends JPanel implements InteractiePanel,	ActionListener, CBookAware
{
	private StatModel model;
	private StatInteractiePanelView view;
	public static final boolean DEBUG = false;
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);	

	/**
	 * Constructor
	 */
	public StatInteractiePanel()
	{
		super(new BorderLayout());
		this.model = new StatModel();
		this.view = new StatInteractiePanelView(this.model, this);
		super.add(this.view, BorderLayout.CENTER);
	}

	public StatInteractiePanel(StatModel model)
	{
		super(new BorderLayout());
		this.model = model;
		this.view = new StatInteractiePanelView(this.model, this);
		super.add(this.view, BorderLayout.CENTER);
	}

	private void debugPrint(String s)
	{
		if (DEBUG)
		{
			System.out.println(s);
		}
	}

	public StatInteractiePanelView getView()
	{
		return view;
	}

	public void setView(StatInteractiePanelView view)
	{
		this.view = view;
	}

	public StatModel getModel()
	{
		return model;
	}

	public void setModel(StatModel model)
	{
		this.model = model;
		this.view.setModel(this.model);
	}

	public void zetOpdracht(Hashtable hashtable, String[] randomVars, Hashtable randomValues)
	{
		// Waarom randomVars en randomValues?
//		System.out.println("StatInteractiePanel.zetOpdracht(hashtable=" + hashtable
//		 + ", randomVars=" + randomVars + ", randomValues=" + randomValues);
		
		Hashtable b = deepCopy(hashtable);
		Hashtable resetHashtable = deepCopy(hashtable);

		// Deep copy the hashtable, else references will be copied and fields
		// within resetHashtable can be changed.
		this.model.setResetHashtable(resetHashtable);

		this.model.removeViewsWithoutEvent();

		if (b.containsKey("tableModel"))
		{
			this.model.getStatTableModel().setState((Hashtable) b.get("tableModel"));

			// this.view.setModel(this.model);
		}

		ArrayList<Boolean> selectionList;
		if (b.containsKey("selectionList")) // de oude manier
		{
			selectionList = (ArrayList<Boolean>) b.get("selectionList");
			this.model.getStatTableModel().setSelectionList(selectionList);
		}
		else if (b.containsKey("selectionIndices")) // de nieuwe manier
		{
			this.model.getStatTableModel().setSelectionIndices(
				(ArrayList<Number>) b.get("selectionIndices"));
		}
		else
		{
			ArrayList<Number> indicesList = new ArrayList<Number>();
			this.model.getStatTableModel().setRowOutlierIndices(indicesList);
		}

		if (b.containsKey("rowOutlierList")) // de oude manier
		{
			this.model.getStatTableModel().setRowOutlierList(
				(ArrayList<Boolean>) b.get("rowOutlierList"));
		}
		else if (b.containsKey("rowOutlierIndices")) // de nieuwe manier
		{
			this.model.getStatTableModel().setRowOutlierIndices(
				(ArrayList<Number>) b.get("rowOutlierIndices"));
		}
		else
		{
			ArrayList<Number> indicesList = new ArrayList<Number>();
			this.model.getStatTableModel().setRowOutlierIndices(indicesList);
		}

		if (b.containsKey("cellOutlierList")) // de oude manier
		{
			this.model.getStatTableModel().setCellOutlierList(
				(ArrayList<ArrayList<Boolean>>) b.get("cellOutlierList"));
		}
		else if (b.containsKey("cellOutlierIndices")) // de nieuwe manier
		{
			this.model.getStatTableModel().setCellOutlierIndices(
				(ArrayList<ArrayList<Number>>) b.get("cellOutlierIndices"));
		}
		else
		{
			ArrayList<ArrayList<Number>> indicesList = new ArrayList<ArrayList<Number>>();
			this.model.getStatTableModel().setCellOutlierIndices(indicesList);
		}

		if (b.containsKey("statistiekViewTypes")
			&& b.containsKey("statistiekViewStates"))
		{
			String[] statistiekViewTypes = (String[]) b
				.get("statistiekViewTypes");
			Object[] statistiekViewStates = (Object[]) b
				.get("statistiekViewStates");

			for (int i = 0; i < statistiekViewTypes.length; i++)
			{
				StatistiekView statistiekView = Statistiek.createView(
					statistiekViewTypes[i], "", this.model.getStatTableModel(), 0, 0, this);
				if (statistiekView != null)
				{
					statistiekView.setState(statistiekViewStates[i]);
					this.model.addView(statistiekView);
				}

			}
			// this.view.setModel(this.model);
		}

		if (b.containsKey("selectedView"))
		{
//			System.out.println("StatInteractiePanel.zetOpdracht(): selectedView in hashtable = "
//				+ ((Integer)b.get("selectedView")).intValue());
			this.view.processSelectedTab(((Integer) b.get("selectedView")).intValue());
		}
	}

	public int getSelectedView()
	{
		return this.view.getSelectedView();
	}

	public void setSelectedTab(int tab)
	{
		this.view.processSelectedTab(tab);
	}

	public Hashtable getState()
	{
		Hashtable h = new Hashtable();

		h.put("tableModel", this.model.getStatTableModel().getState());

		h.put("selectionIndices", this.model.getStatTableModel().getSelectionIndices());
		h.put("rowOutlierIndices", this.model.getStatTableModel().getRowOutlierIndices());
		h.put("cellOutlierIndices", this.model.getStatTableModel().getCellOutlierIndices());

		// statistiekViewTypes and statistiekViewStates should always be added to the state
		int noViews = this.model.getViews().size();
		String[] statistiekViewTypes = new String[noViews];
		Object[] statistiekViewStates = new Object[noViews];
		for (int i = 0; i < noViews; i++)
		{
			statistiekViewTypes[i] = this.model.getViews().get(i).getViewType();
			statistiekViewStates[i] = this.model.getViews().get(i).getState();
		}

		h.put("statistiekViewTypes", statistiekViewTypes);
		h.put("statistiekViewStates", statistiekViewStates);

		int tabInt = this.model.mainWindowIndexToGeneralIndex(this.view
			.getSelectedView());
		h.put("selectedView", new Integer(tabInt));
		System.out.println("StatInteractiePanel.getState(): this.model.mainWindowIndexToGeneralIndex(this.view.getselectedView()="
			+ this.view.getSelectedView() + ") = " + tabInt);

		return h;
	}

	public void setState(Hashtable hashtable)
	{
		//Hashtable b = deepCopy(hashtable);
		Map b = (Map) hashtable;
		
		this.model.removeViewsWithoutEvent();

		if (b.containsKey("tableModel"))
		{
			this.model.getStatTableModel().setState(new Hashtable((Map) b.get("tableModel")));
			// this.view.setModel(this.model);
		}
		if (b.containsKey("selectionList")) // de oude manier
		{
			this.model.getStatTableModel().setSelectionList((ArrayList<Boolean>) b.get("selectionList"));
		}
		else if (b.containsKey("selectionIndices")) // de nieuwe manier
		{
			this.model.getStatTableModel().setSelectionIndices(
				(ArrayList<Number>) b.get("selectionIndices"));
		}
		else
		{
			ArrayList<Number> indicesList = new ArrayList<Number>();
			this.model.getStatTableModel().setSelectionIndices(indicesList); // was setRowOutlierIndices
		}

		if (b.containsKey("rowOutlierList")) // if old version set the rowOutlierList the old fashion way
		{
			this.model.getStatTableModel().setRowOutlierList(
				(ArrayList<Boolean>) b.get("rowOutlierList"));
		}
		else if (b.containsKey("rowOutlierIndices")) // de nieuwe manier
		{
			this.model.getStatTableModel().setRowOutlierIndices(
				(ArrayList<Number>) b.get("rowOutlierIndices"));
		}
		else
		{
			List<Number> indicesList = Collections.emptyList();
			this.model.getStatTableModel().setRowOutlierIndices(indicesList);
		}

		if (b.containsKey("cellOutlierList")) // de oude manier
		{
			this.model.getStatTableModel().setCellOutlierList(
				(ArrayList<ArrayList<Boolean>>) b.get("cellOutlierList"));
		}
		else if (b.containsKey("cellOutlierIndices")) // de nieuwe manier
		{
			this.model.getStatTableModel().setCellOutlierIndices(
				(ArrayList<ArrayList<Number>>) b.get("cellOutlierIndices"));
		}
		else
		{
			ArrayList<ArrayList<Number>> indicesList = new ArrayList<ArrayList<Number>>();
			this.model.getStatTableModel().setCellOutlierIndices(indicesList);
		}

		if (b.containsKey("statistiekViewTypes")
			&& b.containsKey("statistiekViewStates"))
		{
			String[] statistiekViewTypes = toStringArray(b.get("statistiekViewTypes"));
			Object[] statistiekViewStates = toObjectArray(b.get("statistiekViewStates"));

			for (int i = 0; i < statistiekViewTypes.length; i++)
			{
				StatistiekView statistiekView = Statistiek.createView(
					statistiekViewTypes[i], "", this.model.getStatTableModel(), 0, 0, this);
				if (statistiekView != null)
				{
					statistiekView.setState(statistiekViewStates[i]);
					this.model.addView(statistiekView);
				}

			}
			// this.view.setModel(this.model);
		}

		if (b.containsKey("selectedView"))
		{
			int index = ((Number) b.get("selectedView")).intValue();
//			System.out.println("StatInteractiePanel.setState(): selectedView in hashtable = "
//				+ index);
			this.view.processSelectedTab(index);
		}
	}

	public static String[] toStringArray(Object object)
	{
		if (object == null || object instanceof String[])
			return (String[]) object;
		if (object instanceof List)
		{
			List list = (List) object;
			return (String[]) list.toArray(new String[list.size()]);
		}
		return null;
	}

	public static Object[] toObjectArray(Object object)
	{
		if (object == null || object instanceof Object[])
			return (Object[]) object;
		if (object instanceof List)
		{
			List list = (List) object;
			return list.toArray();
		}
		return null;
	}
	
	public Hashtable getEditState()
	{
		return this.getState();
	}

	public void setEditState(Hashtable b)
	{
		this.setState(b);
	}

	public InteractieEditPanel getEditPanel()
	{
		return new StatEditPanelController();
	}

	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x, y, b, h);

	}

	public void wis()
	{
		// TODO Auto-generated method stub
	}

	public void zetMaat()
	{
		// TODO Auto-generated method stub
	}

	public int geefAsHoogte()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getIpId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getScore()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getScoreMax()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Retourneert altijd true, want in de statistiekcomponent wordt
	 * niets nagekeken.
	 */
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
		// TODO Auto-generated method stub
	}

	public void zetNagekeken(boolean b)
	{
		// TODO Auto-generated method stub
	}

	public void stop()
	{
		// close all dialogs
		ArrayList<Boolean> viewInOwnWindow = new ArrayList<Boolean>();
		for (int i = 0; i < this.model.getViewInOwnWindow().size(); i++)
		{
			viewInOwnWindow.add(false);
		}
		this.model.setViewInOwnWindow(viewInOwnWindow);
	}

	public void start()
	{
		// TODO Auto-generated method stub
	}

	public void destroy()
	{
		// TODO Auto-generated method stub
	}

	public void opnieuw()
	{
		// TODO Auto-generated method stub
	}

	public void kijkNa()
	{
		// TODO Auto-generated method stub
	}

	public void kijkNa(int stapNr)
	{
		// TODO Auto-generated method stub
	}

	public void addActionListener(ActionListener al)
	{
		// TODO Auto-generated method stub
	}

	public void actionPerformed(ActionEvent e)
	{
		//System.out.println("StatInteractiePanel.actionPerformed(): " + e.getActionCommand());
		
		String ac = e.getActionCommand(); 
		
		if (ac.equals("startVarBox") || ac.equals("startVar2Box"))
		{
			// for crosstab (kruistabel) startVarBox is for choosing the rows variable
			// startVar2Box is for choosing the columns variable
			
			String s = this.view.getViewsBoxString();
			String t = null;
			if (Arrays.asList(Statistiek.VIEWS_translated).contains(s))
			{
				if (s == Statistiek.rb.getString("tableOption"))
				{
					t = Statistiek.VIEWS[0];
				}
				else if (s == Statistiek.rb.getString("histogramOption"))
				{
					t = Statistiek.VIEWS[1];
				}
				else if (s == Statistiek.rb.getString("dotplotOption"))
				{
					t = Statistiek.VIEWS[2];
				}
				else if (s == Statistiek.rb.getString("frequencytableOption"))
				{
					t = Statistiek.VIEWS[3];
				}
				else if (s == Statistiek.rb.getString("frequencypolygonOption"))
				{
					t = Statistiek.VIEWS[4];
				}
				else if (s == Statistiek.rb.getString("boxplotOption"))
				{
					t = Statistiek.VIEWS[5];
				}
				else if (s == Statistiek.rb.getString("crosstabOption"))
				{
					t = Statistiek.VIEWS[6];
				}
				else if (s == Statistiek.rb.getString("scatterplotOption"))
				{
					t = Statistiek.VIEWS[7];
				}
				else if (s == Statistiek.rb.getString("descriptivesOption"))
				{
					t = Statistiek.VIEWS[8];
				}
				else if (s == Statistiek.rb.getString("piechartOption"))
				{
					t = Statistiek.VIEWS[9];
				}
				
				// Als Tabel gekozen, dan is de actionPerformed van startVarBox niet relevant
				if (!t.equals(Statistiek.VIEWS[0]))
				{
					StatistiekView statistiekView = null;
					
					if (t.equals(Statistiek.VIEWS[6]) || t.equals(Statistiek.VIEWS[7]))
					{
						// Crosstab or scatterplot
						// Check if both varboxes are set
						if ((this.view.getStartVarBoxSelectedIndex() > 0)
							&& (this.view.getStartVar2BoxSelectedIndex() > 0))
						{
							// both variable boxes are set
							
		    				// startVarBox index -1 vanwege de eerste default 'Kies een variabele'
		    				statistiekView = Statistiek.createView(t,
		    					this.model.findUniqueViewName(s), model.getStatTableModel(),
		    					this.view.getStartVarBoxSelectedIndex()-1, 
		    					this.view.getStartVar2BoxSelectedIndex()-1, this);
							this.model.addView(statistiekView);
		    				this.view.selectLastTab();
		    				this.view.clearAddViewTab();
						}
					}
					else
					{
	    				// startVarBox index -1 vanwege de eerste default 'Kies een variabele'
	    				statistiekView = Statistiek.createView(t,
	    					this.model.findUniqueViewName(s), model.getStatTableModel(),
	    					this.view.getStartVarBoxSelectedIndex()-1, 0, this);
						this.model.addView(statistiekView);
	    				this.view.selectLastTab();
	    				this.view.clearAddViewTab();
					}
				}
			}
		} // ac = startVarBox || startVar2Box
		else if (ac.equals("viewsBox"))
		{
			// check of tabel gekozen is
			String s = this.view.getViewsBoxString();
			String t = null;
			if (Arrays.asList(Statistiek.VIEWS_translated).contains(s)
				&& (s == Statistiek.rb.getString("tableOption")))
			{
				// tabel gekozen, er is geen variabelekeuze nodig
				t = Statistiek.VIEWS[0];
				StatistiekView statistiekView = Statistiek.createView(t,
					this.model.findUniqueViewName(s), model.getStatTableModel(),
					0, 0, this);
				this.model.addView(statistiekView);
				this.view.selectLastTab();
				this.view.clearAddViewTab();
			}
			else if (Arrays.asList(Statistiek.VIEWS_translated).contains(s)
				&& (s == Statistiek.rb.getString("crosstabOption")))
			{
				// update label "Kies variabele rijen:"
				this.view.setStartVarLabel(Statistiek.rb.getString("chooseStartVarRowLabel"));
				this.view.setStartVarBox(true);
				this.view.setStartVar2Box(true);
			}
			else if (Arrays.asList(Statistiek.VIEWS_translated).contains(s)
				&& (s == Statistiek.rb.getString("scatterplotOption")))
			{
				// update label "Kies variabele x-as:"
				this.view.setStartVarLabel(Statistiek.rb.getString("chooseStartVarXLabel"));
				// update label "Kies variabele y-as:"
				this.view.setStartVar2Label(Statistiek.rb.getString("chooseStartVarYLabel"));
				this.view.setStartVarBox(true);
				this.view.setStartVar2Box(true);
			}
			else
			{
				// update startVar-box: toon alleen kolommen die van toepassing zijn, bijv. alleen van type opsomming bij cirkeldiagram
				this.view.updateStartVarBox();

				// bied variabelekeuze aan
				this.view.setStartVarLabel(Statistiek.rb.getString("chooseStartVarLabel"));
				this.view.setStartVarBox(true);
				this.view.setStartVar2Box(false);
			}
		}
		// resetbutton is now implemented in StatTable
		// else if(e.getActionCommand().equals("reset"))
		// {
		// Hashtable resetHashtable = this.model.getResetHashtable();
		//
		// // clear stringFrequencies
		// this.model.getData().clearStringFrequencies();
		//
		// //
		// System.out.println("reset clicked! this.model.getResetHashtable()="
		// // + resetHashtable);
		//
		// // Complete reset met zetOpdracht()
		// this.zetOpdracht(resetHashtable, null, null);
		// }
		else if (e.getSource() instanceof ButtonTabComponent.TabButton)
		{
			int mainTabIndex = this.view
				.indexOfTabComponent(((ButtonTabComponent.TabButton) (e
					.getSource())).getButtonTabComponent());
			int tab = this.model.mainWindowIndexToGeneralIndex(mainTabIndex);
			this.model.removeView(tab);
		}
	}

	/**
	 * deeply clones a Hashtable by serializing and deserializing.
	 */
	private Hashtable<String, Object> deepCopy(Hashtable<String, Object> original)
	{
		Object copy = null;
		try
		{
			// Write the object out to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(original);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(
				new ByteArrayInputStream(bos.toByteArray()));
			copy = in.readObject();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
		return (Hashtable) copy;
	}

	
	@Override
	public void acceptCBookEvent(CBookEvent event)
	{
		String command = event.getCommand();
		
		if (command.startsWith("text.csv"))
		{
			Map map = (Map) event.getParameters();
			
			if (map != null)
			{
				Hashtable h = this.getState();
				h.remove("selectionList");
				h.remove("rowOutlierList");
				h.remove("cellOutlierList");
				
				Hashtable tableModel = (Hashtable) h.get("tableModel");
				int columnCount = ((Integer) tableModel.get("columnCount")).intValue();
				boolean dataFitting = true;
				String dataString = (String) map.get("content");

				if ("".equals(dataString))
				{
					model.removeData();
					return;
				}

				model.getStatTableModel().clearOutlierLists();
				
				String[] regels = dataString.split("\n");
				ArrayList<ArrayList<Object>> values = new ArrayList<ArrayList<Object>>();
				for (int i = 0; i < regels.length && !"".equals(dataString); i++)
				{
					String[] waarden = regels[i].split(";");
					// System.out.println("Waardenlengte"+waarden.length);
					if ((waarden.length != columnCount) 
						&& !"".equals(dataString))
					{
						JOptionPane.showMessageDialog(this,
							"Data not fitting in number of columns");
						dataFitting = false;
						break;
					}
					values.add(new ArrayList<Object>());
					for (int j = 0; j < waarden.length; j++)
					{ // System.out.println("Waarden"+j+waarden[j]);
						if (!"".equals(waarden[j].trim()))
						{
							values.get(i).add(waarden[j]);
						}
					}
				}
				if (dataFitting)
				{
					tableModel.put("rowCount", new Integer(regels.length));
					if (regels.length > 0)
					{
						tableModel.put("values", values);
					}
					h.put("tableModel", tableModel);
					this.setState(h);
				}
			}
		}
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}

	@Override
	public String[] getSendCmds() {
		String[] commands = {"text.csv"};
		return commands;
	}

	@Override
	public String[] getAcceptedCmds()
	{
		String[] commands =
			{ "text.csv" };
		return commands;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		String localizedCmd = Statistiek.rb.getString(CBA_PREFIX + cmd);
		if(localizedCmd==null)
			return cmd;
		return localizedCmd;
	}

	public void fire(String command, String key, Object value) {
		cbookEventHandler.fire(command, key, value);
	}
}
