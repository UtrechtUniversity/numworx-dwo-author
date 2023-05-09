package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Objects;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.euclides.event.Tracker;
import fi.euclides.expr.InterpretException;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.formuleobjects.Token;
import fi.euclides.formuleobjects.TokenMgrError;
import fi.euclides.model.Label;
import fi.wiskopdr.TabletOwningLayeredPane;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleEditor;
import fi.wiskopdr.tekstobjects.FeedbackTekstArea;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.math.Expression;
import nl.tue.win.riaca.openmath.lang.OMObject;

@SuppressWarnings("serial")
public abstract class UIEditor extends TabletOwningLayeredPane implements ActionListener {

	protected static final class RenameVerifier extends InputVerifier {
			@Override
			public boolean verify(JComponent input) {
				JTextField field = (JTextField) input;
				String text = field.getText();
				return verify(text);
			}
	
			boolean verify(String text) {
				if (text.isEmpty()) return true;
				FormuleParser parser = new FormuleParser(text);
				try {
					Token t = parser.variableAt();
					return true;
				} catch(Exception e) {}
				return false;
			}
		}

	protected static final class RenameFormat extends Format {
			
			@Override
			public Object parseObject(String source, ParsePosition pos) {
				if (!verifier.verify(source)) {
					pos.setErrorIndex(0);
					return null;
				}
				pos.setIndex(source.length());
				return source;
			}
	
			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				return toAppendTo.append(Objects.toString(obj, ""));
			}
		}

	protected static final RenameVerifier verifier = new RenameVerifier();

	public abstract void commit();

	protected JPanel content;
	protected FeedbackTekstArea feedback;
	protected FormuleEditor visibilityEditor;
	protected JTextField name;
	
	public UIEditor() {
		super();
		content = new JPanel(null);
		add(content, JLayeredPane.DEFAULT_LAYER);
	}

	protected void commitFields(JFormattedTextField... fields) {
		for( JFormattedTextField field: fields) {
			try {
				field.commitEdit();
			} catch(java.text.ParseException pe) {
				field.setValue(null);
			}
		}
	}

	protected void feedback(String command, Throwable t) {
		if(t instanceof ParseException) {
			ParseException pe = (ParseException)t;
			int position = pe.currentToken.beginColumn;
			if (pe.currentToken == null || pe.currentToken.image == null) pe.currentToken = new Token(0, "start");
			command += "\nSyntax fout na " + pe.currentToken + " (positie " + position + ")"; 
		} else if (t instanceof InterpretException) {
			InterpretException ie = (InterpretException) t;
			command += "\n" + ie.getLocalizedMessage();
		} else if (t instanceof TokenMgrError) {
			TokenMgrError tme = (TokenMgrError) t;
			command += "\n" + tme.getLocalizedMessage();
			
		}
		setFeedback(command, visibilityEditor);
	}

	
    static Point getLocationOnWindow(Component f) {
        Point curLocation = f.getLocation();

        for (Container parent = f.getParent();
                parent != null && !(parent instanceof Window);
                parent = parent.getParent())
        {
            curLocation.x += parent.getX();
            curLocation.y += parent.getY();
        }

        return curLocation;
    }

	public Component add(Component c) {
		 content.add(c);
		 setSizes();
		 return c;
	}

	protected void setSizes() {
		setPreferredSize(content.getPreferredSize());
		content.setSize(getPreferredSize());
	}

	
	protected void setFeedback(String tekst, Object object) {
		if(feedback == null) {
			feedback = new FeedbackTekstArea();
			feedback.setSize(200,40);
			if(true) {	
				feedback.setBackground(new Color(255,255,200));
				if("MW".equals(WiskOpdr.deployVariant))feedback.setBackground(new Color(250,255,220));
				if("GR".equals(WiskOpdr.deployVariant))feedback.setBackground(new Color(255,255,255));
				feedback.setBorders(true);
			}
			feedback.setCloseable(true);
			feedback.addActionListener(this);
			feedback.setVisible(false);
			add(feedback, JLayeredPane.PALETTE_LAYER);
		}
		int x; int y;
		Component f = null;
		if (object instanceof Component) f = (Component) object;
		x = getLocationOnWindow(f).x + 10 - getLocationOnWindow(content).x;
		y = getLocationOnWindow(f).y - 10 - getLocationOnWindow(content).y;
		feedback.setLocation(x, y);
		feedback.setText(tekst);
		feedback.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == feedback) {
			feedback.setVisible(false);
		}	
	}

	public boolean verify(Tracker t) {
		String v = getVisibility();
		if ("$f@".equals(v))
			return true; // empty
		Randomizer r = t.adapt(Randomizer.class);
		if (r != null) v = r.randomize(v);
		FormuleParser parser = new FormuleParser(v.substring(2));
		try {
			OMObject object = parser.logic();
			Expression expr = t.adapt(Expression.class);
			Label l = new Label();
			expr.interpret(object, l, t.getMapper());
			l.destroy();
		} catch (TokenMgrError e) { // TODO zet feedback tekst
			feedback(v, e);
			return false;
		} catch (ParseException e) {
			feedback(v, e);
			return false;
		} catch (InterpretException e) {
			feedback(v, e);
			return false;
		} catch (Exception e) {
			feedback(v, e);
			return false;
		}
	
		return true;
	}

	public String getVisibility() {
		return visibilityEditor.formuleVak.toString();
	}
}
