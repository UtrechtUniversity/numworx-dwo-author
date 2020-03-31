

package fi.beans.browser; 
 
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import fi.beans.scorm.SCORM12APIInterface;
import nl.numworx.swingbrowser.api.ConsoleEvent;
//import javafx.concurrent.Worker.State;
//import javafx.scene.control.ButtonType;
//import javafx.scene.control.Dialog;
import nl.numworx.swingbrowser.api.SwingBrowser;
import nl.numworx.swingbrowser.api.SwingBrowserFactory;
import nl.numworx.swingbrowser.api.SwingBrowserProvider;
import nl.numworx.swingbrowser.scorm.ConsoleListener;

public class SimpleSwingBrowser extends JPanel implements Status, ConsoleListener {
    public static boolean debug;
    
    static final SwingBrowserProvider PROVIDER = new SwingBrowserProvider();
    private final SwingBrowserFactory FACTORY;    
    private final SwingBrowser browser;
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JComponent jfxPanel;
 
	public JComponent getJfxPanel() {
		return jfxPanel;
	}

	public void newSession() {
	  FACTORY.newSession();
	}
	
	SCORM12APIInterface api;
	private Console console;
	private Status status = this;
//	transient private State state;
 
    public SCORM12APIInterface getApi() {
		return api;
	}

	public void setApi(SCORM12APIInterface api) {
		this.api = api;
		browser.setAPI(new Scorm2004API(getApi()));
		browser.addConsoleListener(this);
	}

	public Console getConsole() {
		return console;
	}

	public void setConsole(Console console) {
		this.console = console;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		if(status == null) status = this; // Null Pattern
		this.status = status;
	}

//	public synchronized State getState() {
//		return state;
//	}
//
//	public synchronized void setState(State state) {
//		this.state = state;
//		notifyAll();
//	}

	private final JLabel lblStatus = new JLabel();
 
	
	public SimpleSwingBrowser() {
		this(PROVIDER.getFactory());
	}
	
    public SimpleSwingBrowser(SwingBrowserFactory factory) {
        super(new BorderLayout());
        FACTORY = factory;
        browser = FACTORY.newBrowser();
        jfxPanel = browser.asComponent();
        initComponents();
        browser.addStatusListener(e -> status.showStatus(e.getStatus()));
    }

	private void initComponents() {
   
        lblStatus.setText("");
        
        JPanel statusBar = new JPanel(new BorderLayout(5, 0));
        statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        statusBar.add(lblStatus, BorderLayout.CENTER);
 
        add(statusBar, BorderLayout.SOUTH);
        add(jfxPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(1024, 600));
    }
 
//    private void showAlert(String message) {
//        Dialog<Void> alert = new Dialog<>();
//        alert.getDialogPane().setContentText(message);
//        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        alert.showAndWait();
//    }
//
//    private boolean showConfirm(String message) {
//        Dialog<ButtonType> confirm = new Dialog<>();
//        confirm.getDialogPane().setContentText(message);
//        confirm.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
//        boolean result = confirm.showAndWait().filter(ButtonType.YES::equals).isPresent();
//        return result ;
//    }
	
//    private void createScene() {
// 
//        Platform.runLater(new Runnable() {
//            @Override 
//            public void run() {
//            	
//                WebView view = new WebView();
//                engine = view.getEngine();
//                install();
//                engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
//                    @Override 
//                    public void handle(final WebEvent<String> event) {
//                        SwingUtilities.invokeLater(new Runnable() {
//                            @Override 
//                            public void run() {
//                                String message = event.getData();
//                                if(message != null) getStatus().showStatus(message);
//                            }
//                        });
//                    }
//                });
//                if(debug)
//                engine.locationProperty().addListener(new ChangeListener<String>() {
//                    @Override
//                    public void changed(ObservableValue<? extends String> ov, String oldValue, final String newValue) {
//                                System.out.println("Location: " + newValue + " was " + oldValue);
//                    }
//                });
// 
//                if(debug)
//                engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
//                        SwingUtilities.invokeLater(new Runnable() {
//                            @Override 
//                            public void run() {
//                                int value = newValue.intValue();
//                                System.out.println("Progress: " + value);
//                                progressBar.setValue(value);
//								progressBar.setVisible(value != 100); // NEVER show
//                            }
//                        });
//                    }
//                });
//
//                engine.getLoadWorker()
//                        .exceptionProperty()
//                        .addListener(new ChangeListener<Throwable>() {
// 
//                            public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
//                                if (engine.getLoadWorker().getState() == State.FAILED) {
//                                    SwingUtilities.invokeLater(new Runnable() {
//                                        @Override public void run() {
//                                            JOptionPane.showMessageDialog(
//                                                    SimpleSwingBrowser.this,
//                                                    (value != null) ?
//                                                    engine.getLocation() + "\n" + value.getMessage() :
//                                                    engine.getLocation() + "\nUnexpected error.",
//                                                    "Loading error...",
//                                                    JOptionPane.ERROR_MESSAGE);
//                                        }
//                                    });
//                                }
//                            }
//                        });
//
//             // process page loading
//                engine.getLoadWorker().
//                		stateProperty().
//                		addListener(new ChangeListener<State>() {
//
//							@Override
//							public void changed(
//									ObservableValue<? extends State> ov,
//									State oldState, State newState) {
//								
//								if (newState == State.RUNNING || newState == State.SUCCEEDED)
//								{   SwingUtilities.invokeLater(
//	                                new Runnable() {
//										public void run() {
//											progressBar.setVisible(false); // NEVER show when running
//										}
//									});
//									install();
//								}
//								if(newState != oldState) 
//									setState(newState);
//							}
//						}
//                     );
// 
//                engine.setOnAlert(new EventHandler<WebEvent<String>>() {
//					
//					@Override
//					public void handle(final WebEvent<String> message) {
//						showAlert(message.getData());
//					}
//				});
//                
//                engine.setConfirmHandler(new Callback<String, Boolean>() {
//					
//					@Override
//					public Boolean call(String message) {
//						return showConfirm(message);
//					}
//				});
//                
//                jfxPanel.setScene(new Scene(view));
//            }
//
//            void install() {
//              JSObject window;
//              window = (JSObject) engine.executeScript("window");
//              if (getConsole() != null) {
//              	window.setMember("console", getConsole());
//              }
//              if (getApi() != null) {
//                  Scorm2004API api = new Scorm2004API(getApi());
//              	window.setMember("API", api);
//              	window.setMember("API_1484_11", api);
//              }
//            }
//        });
//    }
 
    public void loadURL(final String url) {
      browser.loadURL(url);
    }

//	private void removeMembers0() {
//		JSObject window;
//		window = (JSObject) engine.executeScript("window");
//		try { window.removeMember("console"); } catch (JSException e) {}
//		try { window.removeMember("API"); } catch (JSException e) {}
//        try { window.removeMember("API_1484_11"); } catch (JSException e) {}
//	}
//
	public void removeMembers() {
	  try {
        browser.close();
      } catch (IOException e) {
      // TODO Auto-generated catch block
        e.printStackTrace();
      }
//		if ( Platform.isFxApplicationThread() )
//			removeMembers0();
//		else
//			Platform.runLater(()-> { removeMembers0(); });
	}
	
	
	public void showStatus(String message) {
		lblStatus.setText(message);
	}

	private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
                return null;
        }
    }

  @Override
  public void onConsole(ConsoleEvent event) {
    Console c = console;
    if (c != null) {
      String m = event.getMessage();
      switch(event.getLevel()) {
        case LOG:
          c.log(m); break;
        case DEBUG: c.debug(m); break;
        case ERROR: c.error(m); break;
        case INFO: c.info(m); break;
        case WARN: c.warn(m); break;
      }
    }
    
  }

//    static {
//    	Platform.setImplicitExit(false);
//    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            public void run() {
//                SimpleSwingBrowser browser = new SimpleSwingBrowser();
//                browser.setVisible(true);
//                browser.loadURL("http://web-expresser.appspot.com");
//                JFrame f = new JFrame();
//                f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
//                f.setContentPane(browser);
//                f.pack();
//                f.show();
//           }     
//       });
//    }
}

	