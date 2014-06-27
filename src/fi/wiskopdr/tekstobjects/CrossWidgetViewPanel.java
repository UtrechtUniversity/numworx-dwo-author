package fi.wiskopdr.tekstobjects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class CrossWidgetViewPanel extends JPanel implements MouseListener
{
	private TekstVak tekstVak;
	
	private ArrayList<TekstInteractiePanelVak> crossWidgetContainers = new ArrayList<TekstInteractiePanelVak>();
	private ArrayList<Map> connectionInfo = new ArrayList<Map>();
	private static final String KEY_SENDER = "sender";
	private static final String KEY_LISTENER = "listener";
	private static final String KEY_COMMAND = "command";
	private static final String KEY_CLICK_AREA = "clickarea";
	
	private final int ARR_SIZE = 8;
	private final int LINE_WIDTH = 4;
	
	private JPopupMenu popup;
	
	
	public CrossWidgetViewPanel(TekstVak tekstVak)
	{	this.tekstVak = tekstVak;
		addMouseListener(this);
		
		popup = new JPopupMenu();
	}
	
	public boolean contains (int x, int y)
	{	boolean contains = false;
		for(int i=0 ; i<connectionInfo.size() ; i++)
		{	contains = contains || ((Rectangle)connectionInfo.get(i).get(KEY_CLICK_AREA)).contains(x,y);
		}
		return contains;
	}
	
	public void addCrossWidgetContainer(TekstInteractiePanelVak crossWidgetContainer)
	{
		if(!crossWidgetContainers.contains(crossWidgetContainer))
			crossWidgetContainers.add(crossWidgetContainer);
	}
	
	private Point calculatePosInTekstVak(TekstInteractiePanelVak crossWidgetContainer)
	{
		Component component = crossWidgetContainer;
		Point location = new Point(component.getX()+component.getWidth()/2, component.getY()+component.getHeight()/2);
		int y = 0;
		int zoekdiepte = 0;
		while(component.getParent()!=null && zoekdiepte<100 && tekstVak != component.getParent())
		{	zoekdiepte++;
			component = component.getParent();
			location.x += component.getX();
			location.y += component.getY();
		}
		return location;
	}
	
	private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) 
	{
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.fillRect(0, -LINE_WIDTH/2, len/2-2*ARR_SIZE, LINE_WIDTH);
        g.fillRect(len/2-LINE_WIDTH/2, -LINE_WIDTH/2, len/2, LINE_WIDTH);
        g.fillPolygon(new int[] {len/2, len/2-2*ARR_SIZE, len/2-2*ARR_SIZE, len/2},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }

    public void paintComponent(Graphics g) 
    {
        g.setColor(new Color(255,200,0,100));
        g.drawRect(0,0,getWidth()-1, getHeight()-1);
        g.drawRect(1,1,getWidth()-3, getHeight()-3);
        g.drawRect(2,2,getWidth()-5, getHeight()-5);
        
        connectionInfo.clear();
		for (int i=0 ; i<crossWidgetContainers.size() ; i++)
    	{	TekstInteractiePanelVak startVak = crossWidgetContainers.get(i);
    		if(startVak!=null)
    		{	Point start = calculatePosInTekstVak(crossWidgetContainers.get(i));
	    		List connections = crossWidgetContainers.get(i).getConnections();
	    		ArrayList<TekstInteractiePanelVak> eindVakken = new ArrayList<TekstInteractiePanelVak>();
				Iterator iter = connections.iterator();
	    		while(iter.hasNext())
				{	Map<String, String> type = (Map)iter.next();
					Map.Entry<String,String> entry = type.entrySet().iterator().next();
					TekstInteractiePanelVak eindVak = tekstVak.getWidgetContainer(entry.getValue());
					if(eindVak != null && !eindVakken.contains(eindVak))
					{	eindVakken.add(eindVak);
						Point eind = calculatePosInTekstVak(eindVak);
						drawArrow(g, start.x, start.y, eind.x, eind.y);
						double dx = eind.x-start.x;
						double dy = eind.y-start.y;
						double length = Math.sqrt(dx*dx+dy*dy);
						Point p = new Point((int)(start.x+dx/2-dx/length*1.5*ARR_SIZE), (int)(start.y+dy/2-dy/length*1.5*ARR_SIZE));
						Rectangle clickRect = new Rectangle(p.x-ARR_SIZE, p.y-ARR_SIZE, 2*ARR_SIZE, 2*ARR_SIZE);
						HashMap<String,Object> connection = new HashMap<String,Object>();
						connection.put(KEY_SENDER, startVak);
						connection.put(KEY_LISTENER, eindVak);
						connection.put(KEY_COMMAND, entry.getKey());
						connection.put(KEY_CLICK_AREA, clickRect);
						connectionInfo.add(connection);
					}
					
				}
    		}
    	}
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		boolean raak = false;
		for(int i=0 ; i<connectionInfo.size() ; i++)
		{	raak = ((Rectangle)connectionInfo.get(i).get(KEY_CLICK_AREA)).contains(e.getX(),e.getY());
			if(raak)
			{	final TekstInteractiePanelVak sender = (TekstInteractiePanelVak)connectionInfo.get(i).get(KEY_SENDER);
				final TekstInteractiePanelVak listener = (TekstInteractiePanelVak)connectionInfo.get(i).get(KEY_LISTENER);
				final String command = (String)connectionInfo.get(i).get(KEY_COMMAND);
				if(e.getModifiers()== InputEvent.BUTTON3_MASK || e.isControlDown())
				{	sender.removeConnection(listener, command);
					sender.removeActionListener(listener);
					repaint();
				}
				else
				{	popup.removeAll();
					final JCheckBoxMenuItem item = new JCheckBoxMenuItem(command);
					item.setSelected(true);
					item.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							if(!item.isSelected()){
								sender.removeConnection(listener, command);
								sender.removeActionListener(listener);
								popup.setVisible(false);
								repaint();
							}
								
						}
					});					
					popup.add(item);
					popup.addSeparator();
					final JMenuItem okItem = new JMenuItem("ok");
					okItem.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							popup.setVisible(false);	
						}
					});
					popup.add(okItem);
					popup.setVisible(true);
					popup.setLocation(getLocationOnScreen().x + e.getX(),getLocationOnScreen().y + e.getY() - popup.getHeight());
					
				}
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
