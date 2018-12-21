package fi.wiskopdr;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

import static javafx.concurrent.Worker.State.FAILED;

//@SuppressWarnings("restriction")
public class SimpleSwingBrowser {
	final class TitleHandler implements ChangeListener<String> {
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue,
    		final String newValue) {
        if (frame != null) {
        	SwingUtilities.invokeLater(new Runnable() {
        		@Override
        		public void run() {
        			SimpleSwingBrowser.this.setTitle(newValue);
        		}
        	});
        }
    }
  }

  final class ExceptionHandler implements ChangeListener<Throwable> {
    public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
    	if (engine.getLoadWorker().getState() == FAILED) {
    		SwingUtilities.invokeLater(new Runnable() {
    			@Override
    			public void run() {
    				JOptionPane.showMessageDialog(jfxPanel,
    						(value != null) ? engine.getLocation() + "\n" + value.getMessage()
    								: engine.getLocation() + "\nUnexpected error.",
    						"Loading error...", JOptionPane.ERROR_MESSAGE);
    			}
    		});
    	}
    }
  }

  static {
		Platform.setImplicitExit(false);
	}

	private Component observer;
	
	public void setRepaintObserver(Component c) {
	  observer = c;
	}
	
	
	private final JFXPanel jfxPanel = new JFXPanel() {
	    public void repaint() {
	      super.repaint();
	      if(observer != null) {
	        observer.repaint();
	      }
	    }
	};
	private WebEngine engine;

//	private final JPanel panel = new JPanel(new BorderLayout());
//	private final JLabel lblStatus = new JLabel();

//	private final JButton btnGo = new JButton("Go");
//	private final JTextField txtURL = new JTextField();
	//private final JProgressBar progressBar = new JProgressBar();
//	private final JPanel topBar = new JPanel(new BorderLayout(5, 0));
//	private final JPanel statusBar = new JPanel(new BorderLayout(5, 0));

	public SimpleSwingBrowser() {
		super();
		initComponents();
	}

	public void setAdressFieldVisible(boolean b) {
//		if (!b)
//			panel.remove(topBar);
	}

	public void setStatusBarVisible(boolean b) {
//		if (!b)
//			panel.remove(statusBar);
	}

	private void initComponents() {
		createScene();

//		ActionListener al = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				loadURL(txtURL.getText());
//			}
//		};
//
//		btnGo.addActionListener(al);
//		txtURL.addActionListener(al);
//
//		//progressBar.setPreferredSize(new Dimension(150, 18));
//		//progressBar.setStringPainted(true);
//
//		topBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
//		topBar.add(txtURL, BorderLayout.CENTER);
//		topBar.add(btnGo, BorderLayout.EAST);
//
//		statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
//		statusBar.add(lblStatus, BorderLayout.CENTER);
//		//statusBar.add(progressBar, BorderLayout.EAST);

//		panel.add(topBar, BorderLayout.NORTH);
//		panel.add(jfxPanel, BorderLayout.CENTER);
//		panel.add(statusBar, BorderLayout.SOUTH);

		// getContentPane().add(panel);

		// setPreferredSize(new Dimension(1024, 600));
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// pack();

	}

	public JFXPanel getBrowserPanel() {
		return jfxPanel;
	}

	private void createScene() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				WebView view = new WebView();
				engine = view.getEngine();

				engine.titleProperty().addListener(new TitleHandler());

//				engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
//					@Override
//					public void handle(final WebEvent<String> event) {
//						SwingUtilities.invokeLater(new Runnable() {
//							@Override
//							public void run() {
//								lblStatus.setText(event.getData());
//							}
//						});
//					}
//				});

//				engine.locationProperty().addListener(new ChangeListener<String>() {
//					@Override
//					public void changed(ObservableValue<? extends String> ov, String oldValue, final String newValue) {
//						SwingUtilities.invokeLater(new Runnable() {
//							@Override
//							public void run() {
//								txtURL.setText(newValue);
//							}
//						});
//					}
//				});

//				engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
//					@Override
//					public void changed(ObservableValue<? extends Number> observableValue, Number oldValue,
//							final Number newValue) {
//						SwingUtilities.invokeLater(new Runnable() {
//							@Override
//							public void run() {
//								progressBar.setValue(newValue.intValue());
//							}
//						});
//					}
//				});

				engine.getLoadWorker().exceptionProperty().addListener(new ExceptionHandler());

				jfxPanel.setScene(new Scene(view));
			}
		});
	}

	void setTitle(String newValue) {
		if (frame != null) {
			frame.setTitle(newValue);
		}
	}

	public void loadURL(final String url) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String tmp = toURL(url);

				if (tmp == null) {
					tmp = toURL("http://" + url);
				}

				engine.load(tmp);
			}
		});
	}

	public void loadContent(final String content, final String type) {
		Platform.runLater(() -> {
			engine.loadContent(content, type);
		});
	}

	public void loadContent(String content) {
		loadContent(content, "text/html");
	}

	private static String toURL(String str) {
		try {
			return new URL(str).toExternalForm();
		} catch (MalformedURLException exception) {
			return null;
		}
	}

	private JFrame frame;

	private JFrame getFrame() {
		if (frame == null) {
			frame = new JFrame();
			frame.setContentPane(jfxPanel);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // for fire and forget
			frame.setPreferredSize(new Dimension(1024, 600));
			frame.pack();
		}
		return frame;
	}

	public void setSize(int width, int height) {
		getFrame().setSize(width, height);
	}

	public void setVisible(boolean b) {
		getFrame().setVisible(b);
	}

}