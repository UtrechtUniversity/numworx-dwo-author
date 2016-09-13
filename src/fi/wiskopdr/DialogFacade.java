package fi.wiskopdr;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import fi.wiskopdr.opdrnav.OpdrNavStruct;

public class DialogFacade {

	protected JDialog   dialog;
	protected Container  window;
	protected RootPaneContainer rootPane;
	
	protected DialogFacade() {
		super();
		
	}
	
	
	
	
	public static DialogFacade newInstance(Component src, String title) {
		return newInstance(src, title, false);
	}
	public static DialogFacade newInstance(Component src,
			String title, boolean modal) {

		DialogFacade result;

		JDesktopPane pane = JOptionPane.getDesktopPaneForComponent(src);
		if(pane != null && !modal) // TODO? geen modal stuff voor internal frames.
		{
			final JInternalFrame frame = new JInternalFrame(title, true, true);
			result = new DialogFacade() {
				public void pack() {
					frame.pack();
				}
				public void dispose() {
					frame.dispose();
				}
				public void setResizable(boolean resizable) {
					frame.setResizable(resizable);
				}

				public void setDefaultCloseOperation(int operation) {
					frame.setDefaultCloseOperation(operation);
				}

				public void addWindowListener(final WindowListener l) {
					//frame.addWindowListener(l);
					frame.addInternalFrameListener(new InternalFrameAdapter() {
						WindowEvent stub = null; 
						//new WindowEvent(SwingUtilities.windowForComponent(e.), WindowEvent.WINDOW_STATE_CHANGED);

						public void internalFrameActivated(InternalFrameEvent e) {
							l.windowActivated(stub);
						}

						public void internalFrameClosed(InternalFrameEvent e) {
							l.windowClosed(stub);
							
						}

						public void internalFrameClosing(InternalFrameEvent e) {
							l.windowClosing(stub);
						}

						public void internalFrameDeactivated(
								InternalFrameEvent e) {
							l.windowDeactivated(stub);
						}

						public void internalFrameDeiconified(
								InternalFrameEvent e) {
							l.windowDeiconified(stub);
						}

						public void internalFrameIconified(InternalFrameEvent e) {
							l.windowIconified(stub);
						}

						public void internalFrameOpened(InternalFrameEvent e) {
							l.windowOpened(stub);
						}});
				}

				

			};
			result.rootPane = frame;
			result.window = frame;
			frame.setVisible(false);
			pane.add(frame); // voorop...
			pane.setLayer(frame, JLayeredPane.PALETTE_LAYER.intValue()); // modeless dialog layer
			frame.toFront();
			return result;
		} 

		result = new DialogFacade();
		Component window = WiskOpdr.getWindowForComponent(src);
		
		// gebruik een tablet owning layered pane t.b.v. tonen popup tablet (keyboard) in popup
		TabletOwningLayeredPane layeredPane = new TabletOwningLayeredPane(src);
		
		JDialog dialog;
		if(window instanceof Frame) {
			dialog = new JDialog( (Frame) window, title, modal);
		} else if (window instanceof Dialog )
			dialog = new JDialog( (Dialog) window, title, modal);
		else 
			dialog = new JDialog((Frame) null, title, modal); // SwingUtilities.getSharedOwnerFrame()

		// gebruik een tablet owning layered pane t.b.v. tonen popup tablet (keyboard) in popup
		dialog.setLayeredPane(layeredPane);
		
		result.dialog = dialog;
		result.window = dialog;
		result.rootPane = dialog;
		return result;
		
	}

	public Container getContentPane() {
		return rootPane.getContentPane();
	}
	public void setContentPane(Container content) {
		rootPane.setContentPane(content);
	}

	/**
	 * @param l
	 * @see java.awt.Component#addComponentListener(java.awt.event.ComponentListener)
	 */
	public void addComponentListener(ComponentListener l) {
		window.addComponentListener(l);
	}

	/**
	 * @param l
	 * @see java.awt.Window#addWindowListener(java.awt.event.WindowListener)
	 */
	public void addWindowListener(WindowListener l) {
		dialog.addWindowListener(l);
	}

	/**
	 * @return
	 * @see java.awt.Component#getSize()
	 */
	public Dimension getSize() {
		return window.getSize();
	}

	/**
	 * @return
	 * @see java.awt.Container#getInsets()
	 */
	public Insets getInsets() {
		return window.getInsets();
	}

	/**
	 * 
	 * @see java.awt.Window#pack()
	 */
	public void pack() {
		dialog.pack();
	}

	/**
	 * @param x
	 * @param y
	 * @see java.awt.Component#setLocation(int, int)
	 */
	public void setLocation(int x, int y) {
		window.setLocation(x, y);
	}

	/**
	 * @param width
	 * @param height
	 * @see java.awt.Window#setSize(int, int)
	 */
	public void setSize(int width, int height) {
		window.setSize(width, height);
	}

	/**
	 * @param b
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	public void setVisible(boolean b) {
		window.setVisible(b);
	}

	/**
	 * 
	 * @see java.awt.Window#dispose()
	 */
	public void dispose() {
		dialog.dispose();
	}

	/**
	 * @param d
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	public void setSize(Dimension d) {
		window.setSize(d);
	}

	/**
	 * @param preferredSize
	 * @see java.awt.Component#setPreferredSize(java.awt.Dimension)
	 * @since 1.5
	 */
	public void setPreferredSize(Dimension preferredSize) {
		window.setPreferredSize(preferredSize);
	}

	/**
	 * @param operation
	 * @see javax.swing.JDialog#setDefaultCloseOperation(int)
	 */
	public void setDefaultCloseOperation(int operation) {
		dialog.setDefaultCloseOperation(operation);
	}

	/**
	 * @return
	 * @see java.awt.Component#isVisible()
	 */
	public boolean isVisible() {
		return window.isVisible();
	}

	public void setResizable(boolean resizable) {
		dialog.setResizable(resizable);
	}


	
}
