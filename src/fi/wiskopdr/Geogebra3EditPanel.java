package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.cbook.cbookif.rm.ResourceManager;

import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.cbook.WidgetBridge;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.*;

public class Geogebra3EditPanel extends GeogebraEditPanel
{
	private Geogebra3Panel geogebraPanel;
	protected int version() { return GeogebraParamButton.GEOGEBRA3; }

	public Geogebra3EditPanel(String id)
	{	
		super(id);
	}

	@Override
	protected void createGeogebraPanel(String id) {
		geogebraPanel = new Geogebra3Panel(true);
		geogebraPanel.setInstanceId(id);
		geogebraPanel.setFactory(WidgetBridge.getFactory(geogebraPanel));
		geogebraPanel.refreshGeogebra();
		add(geogebraPanel);
		setGeogebraBounds();
	}

	protected Hashtable getGeoGebraEditState() {
		return geogebraPanel.getEditState();
	}

	protected void setGeogebraBounds() {
		geogebraPanel.setBounds(defaultPanelBounds);
	}


	protected void setGeogebraEditState(Hashtable h) {
		geogebraPanel.setEditState(h);
	}

	public void zetBreedte(int b)
	{	geogebraPanel.setSize(b,geogebraPanel.getSize().height);
	}
	public void zetHoogte(int h)
	{	geogebraPanel.setSize(geogebraPanel.getSize().width, h);
	}

	@Override
	ResourceManager rm() {
		return geogebraPanel.rm();
	}

}
