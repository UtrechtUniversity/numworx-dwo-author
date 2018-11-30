package fi.wiskopdr;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.cbook.cbookif.LessonMode;
import org.json.fimple.JSONArray;
import org.json.fimple.JSONObject;
import org.json.fimple.JSONValue;
import org.json.fimple.parser.JSONParser;
import org.json.fimple.parser.ParseException;

import fi.beans.appletutil.AppletUtil;
import fi.beans.base64code.StringCodeObject;
import fi.beans.ideas.IdeasClient;
import fi.beans.ideas.IdeasIF;
import fi.beans.scorm.JSScormAPI;
import fi.beans.scorm.Parameter;
import fi.beans.scorm.PartialScoreIF;
import fi.beans.scorm.SCORM12APIInterface;
import fi.beans.scorm.Scorm;
import fi.beans.scorm.ScormAppletIF;
import fi.beans.scorm.ScormEditComponentIF;
import fi.beans.scorm.WNScormAPI;
import fi.beans.wnwidgets.NWButtonUI;
import fi.wiskopdr.cbook.WidgetBridge;
import fi.wiskopdr.copyright.FIButton;
import fi.wiskopdr.expressies.Functie;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.opdrnav.MyOpdrContainer;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.opdrnav.OpdrNavStructEdit;
import fi.wiskopdr.tekstobjects.Link;
import fi.wiskopdr.tekstobjects.LinkIF;
import fi.wiskopdr.tekstobjects.LinkRegel;
import fi.wiskopdr.tekstobjects.TekstImageVak;

public class WiskOpdr extends JApplet implements ScormAppletIF, ActionListener, ComponentListener, PartialScoreIF, LinkIF, Printable {
	
	private static final Logger LOG = Logger.getLogger(WiskOpdr.class.getName());
	
	private static final String CMI_COMPLETION_STATUS = "cmi.completion_status";
	private static final String CMI_CORE_LESSON_LOCATION = "cmi.core.lesson_location";
	private static final String CMI_COMMENTS_FROM_LMS_0_COMMENT = "cmi.comments_from_lms.0.comment";
	private static final String CMI_CORE_LESSON_MODE = "cmi.core.lesson_mode";
	private static final String CMI_CORE_LESSON_STATUS = "cmi.core.lesson_status";
	private static final String CMI_CORE_SCORE_RAW = "cmi.core.score.raw";
	private static final String CMI_SUSPEND_DATA = "cmi.suspend_data";
	private static final String CMI_LAUNCH_DATA = "cmi.launch_data";
	private static final String CMI_CORE_SESSION_TIME = "cmi.core.session_time";
	private static final String CMI_CORE_SCORE_MAX = "cmi.core.score.max";
	private static final String LESSON_STATUS_completed = "completed";
	private static final LessonMode LESSON_MODE_browse = LessonMode.browse;
	private static final LessonMode LESSON_MODE_normal = LessonMode.normal;
	private static final LessonMode LESSON_MODE_review = LessonMode.review;
	
	
	private static Hashtable suspendData = new Hashtable();
	private static Hashtable log = new Hashtable();
	private static Hashtable reviewData = new Hashtable();
	
	public static LookAndFeel lookAndFeel;
	public static ResourceBundle rb;
	public static Locale language = new Locale("en", "");
	
	public static Color bgcolor;
	public static Color BG_BLUE = new Color(221, 238, 255);
	public static Color bgcolorEditor;

	public static String deployVariant;
	public static boolean deployDwoGrading = true;
	
	public static Image GOEDKRUL, FOUTKRUIS, HALFKRUL;

//	public static MathematicaLink phrasebook; deleted at 20181129
	public static IdeasIF ideas;

	public static Font tekstFont = new Font("SansSerif", Font.PLAIN, 12);
	public static Font tekstFontBold = new Font("SansSerif", Font.BOLD, 12);
	public static Font tekstFontKlein = new Font("SansSerif", Font.PLAIN, 11);
	public static Font titelFont = new Font("SansSerif", Font.BOLD, 16);

	public static Font formuleFont0 = new Font("TimesRoman", Font.PLAIN, 16);
	public static Font formuleFont1 = new Font("TimesRoman", Font.PLAIN, 16);
	public static Font formuleFont2 = new Font("TimesRoman", Font.PLAIN, 14);

	public static Font formuleFont0Mac = new Font("SansSerif", Font.PLAIN, 14);
	public static Font formuleFont1Mac = new Font("SansSerif", Font.PLAIN, 14);
	public static Font formuleFont2Mac = new Font("SansSerif", Font.PLAIN, 12);

	public static WiskOpdr applet; // Wim: Volgens mij is er maar één assignment en dat is in WiskOpdr()
	public static JPanel fiButtonPanel;
			
	
	public static boolean mac;
	public static boolean zoefi;
	public static boolean formTimes = true;
	public static boolean fToets = true;
	public static boolean launchDataChanged = false;

	public static String dwo_env; // "dummy", "test", "app" of null
	public static boolean isExperimental() {
		return "test".equals(dwo_env);
	}
	public static String abo_type; // free,demo,premium,standard (null)
	public static boolean isPremium() {
	  boolean is = "premium".equals(abo_type) || abo_type == null;
      return is; // premium in Applicatie
	}
	
	public static String[][] objectives = null;
	public static String[] categorieString = null;
	public StudentModel studentModel;
	private StudentModel studentModels[];
	public static String[][] misconceptions = null;
	public static String[] mccCategorieString = null;
	
	
	private JSONArray studentModelsJSON;
	
	private void readStudentModelsJSON()
	{
		JSONParser parser = new JSONParser();
		try {
			String studentModelsMock = getParameter("studentModelContexts");
			if(studentModelsMock == null)
				studentModelsMock = "[]";
			studentModelsJSON = (JSONArray) parser.parse(studentModelsMock);
		} catch (Exception e) {
			LOG.log(Level.WARNING, "readStudentModelsJSON", e);
			studentModelsJSON = new JSONArray();
		}
	}
	
	
	static public class StudentCategory {
		public String category;
		public String[] objectives;
	}
	
	static public class StudentModel {
		public String title;
		public String id;
		public StudentCategory[] categories;
		public String toString() {
			return String.valueOf(title);
		}

		public int getMaxObjectives() {
			int max = 0;
			for(StudentCategory c: categories)
				max = Math.max(max, c.objectives.length);
			return max;
		}
	}
	
	private StudentModel[] getStudentModelsInit() {
		int size = studentModelsJSON.size();
		StudentModel[] result = new StudentModel[size+1];
		for(int i = 0; i < size; i++) {
			StudentModel model = readModel(studentModelsJSON.get(i));
			result[i+1] = model;
		}
		
		return result;
	}
 	
	public StudentModel[] getStudentModels() {
		if(studentModels == null) {
			readStudentModelsJSON();
			studentModels = getStudentModelsInit();
		}
		return studentModels;
	}
	
	
	
	private static StudentModel readModel(Object object) {
		StudentModel result = new StudentModel();
		JSONObject map = (JSONObject) object;
		JSONObject model = (JSONObject) map.get("modelStructure");
		result.title = getTitle(model);
		result.id = getId(map); // FIXME 
		result.categories = readCategories(model.get("categories"));		
		return result;
	}

	private static String getId(JSONObject map) {
		Object id = map.get("id");
		if(id instanceof Map) {
			return (String) ((Map) id).get("idString");
		}
		return null;
	}


	private static StudentCategory[] readCategories(Object object) {
		if(object == null) return new StudentCategory[0];
		JSONArray array = (JSONArray) object;
		int size = array.size();
		StudentCategory[] categories = new StudentCategory[size];
		for(int i = 0; i < size; i++)  {
			categories[i] = readStudentCategory(array.get(i));
		}
		return categories;
	}

	private static StudentCategory readStudentCategory(Object object) {
		JSONObject map = (JSONObject) object;
		StudentCategory result = new StudentCategory();
		result.category = getTitle(map);
		result.objectives = readObjectives(map.get("objectives"));
		return result;
	}

	protected static String getTitle(JSONObject map) {
		return (String) ((Map) ((Map) map.get("info")).get("title")).get(language.toString());
	}

	private static String[] readObjectives(Object object) {
		if (object == null) return new String[0];
		JSONArray array = (JSONArray) object;
		int size = array.size();
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = readObjective(array.get(i));
		}
		return result;
	}

	private static String readObjective(Object object) {
		JSONObject map = (JSONObject) object;
		return getTitle(map);
	}

	private static URL defaultCodeBase; // allow code injection?
	private static boolean COMPLETED = false;
	public static String defaultEditModeState = null;
			
	SCORM12APIInterface api;
	private Object window; // No reference to JSObject (ClassNotFoundException)
	private long sessionStartTime;
	private ScormEditComponentIF scormEditComponent;

	public OpdrNavStruct ons;
	private Hashtable defaultParamValues, launchData;

	private Button viewButton;

	private boolean loaded;
	private boolean toetsLocked;
	private boolean review;
	private LessonMode lessonMode;
	public static final String CAS_IDEAS = "ideas", CAS_LOCAL = "local";
	public static String doCAS;

	public static void main(String[] args) throws Exception {
		
		int width = 800;
		int height = 600;
		ScormEditMainFrame mf = new ScormEditMainFrame(new WiskOpdr(), width, height);
		mf.setTitle("WiskOpdr");
		mf.pack();
		mf.setVisible(true);
		mf.setSize(width, height);
	}
	
	/**
	 * @deprecated gebruik getWindowForComponent(Component)
	 */
	public static Frame getFrame() {
		return JOptionPane.getFrameForComponent(applet);
	}

	/**
	 * Geeft top-level Frame terug van een opgegeven component.
	 */
	public static Component getWindowForComponent(Component component) {
		Component parent = component;
		if (parent == null)
			parent = applet;
		for (int i = 0; parent != null && i < 70; i++) {
			if (parent instanceof Frame || parent instanceof Dialog) {
				break;
			} else if (parent != null) {
				parent = parent.getParent();
			}
		}
		if (parent instanceof Window)
			return parent;
		else {
			return getFrame();
		}
	}

	/**
	 * Laad image met opgegeven naam.
	 */
	public static Image loadImage(String imageName) {
		MediaTracker tr = new MediaTracker(applet);
		Image image = applet.getToolkit().getImage(WiskOpdr.class.getResource(imageName));
		tr.addImage(image, 0);
		try {
			tr.waitForAll();
		} catch (Exception e) {
		}
		;
		return image;
	}

	/**
	 * Laad images uit de map resources aan de hand van een array namen 
	 * en plaatst ze in de meegegeven hashtabel met de namen als sleutels.
	 */
	public static void loadImages(Hashtable images, String[] imageNames) {
		MediaTracker tr = new MediaTracker(applet);
		Image[] image = new Image[imageNames.length];
		for (int i = 0; i < imageNames.length; i++) {
			String filename = imageNames[i];
			URL u = WiskOpdr.class.getResource("resources/" + filename);
			image[i] = applet.getToolkit().getImage(u);
			tr.addImage(image[i], 0);
		}
		try {
			tr.waitForAll();
		} catch (Exception e) {
		}
		;
		for (int i = 0; i < imageNames.length; i++) {
			images.put(imageNames[i], image[i]);
		}
	}
	
	/**
	 * Zet de plaats van het fi-InfoButton
	 */
	public static void setLocationFiButton(int x) {
		fiButtonPanel.setLocation(x, 0);
	}

	/**
	 * Zet TimesRoman voor formules aan of uit.
	 */
	public static void setFormTimes(boolean b) {
		formTimes = b;
		if (b)
			FormuleVak.setDefaultFont((!formTimes) || mac || zoefi ? formuleFont2Mac : formuleFont2);
	}

	/**
	 * Zet de F-toetsen als sneltoets voor formules aan/uit.
	 */
	public static void setFToets(boolean b) {
		fToets = b;
	}
	
	/**
	 * Zet de maat van de standaardfonts
	 */
	public static void zetFont(String name, int size) {
		tekstFont = new Font(name, Font.PLAIN, size);
		tekstFontBold = new Font(name, Font.BOLD, size);
		tekstFontKlein = new Font(name, Font.PLAIN, size - 1);
		titelFont = new Font(name, Font.BOLD, size * 4 / 3);

		if ("TimesRoman".equals(name)) {
			formuleFont0 = new Font("TimesRoman", Font.PLAIN, size);
			formuleFont1 = new Font("TimesRoman", Font.PLAIN, size);
			formuleFont2 = new Font("TimesRoman", Font.PLAIN, 14);
		} else {
			formuleFont0 = new Font("TimesRoman", Font.PLAIN, size * 6 / 5);
			formuleFont1 = new Font("TimesRoman", Font.PLAIN, size * 6 / 5);
			formuleFont2 = new Font("TimesRoman", Font.PLAIN, 14);
		}

		formuleFont0Mac = new Font(name, Font.PLAIN, size * 8 / 7);
		formuleFont1Mac = new Font(name, Font.PLAIN, size * 8 / 7);
		formuleFont2Mac = new Font(name, Font.PLAIN, 12);

	}
	
	/**
	 * Zet een map met de loggegevens van een antwoordvak in de centrale log Hashtable.
	 * LogID is de logID van het antwoordvak
	 */
	public static void setLog(String logID, Object logMap) {
		log.put(logID, logMap);
	}

	/**
	 * Zet  review-data in de centrale reviewData Hashtable (nu nog alleen gebruikt voor
	 * het verzegelen van een toets per leerling)
	 */
	public static void setReviewData(String name, Object value) {
		reviewData.put(name, value);
	}
	
	/**
	 * Wordt aangeroepen als editten de launchData veranderd zijn, ivm het moeten opslaan of niet.
	 */
	public static void setLaunchDataChanged() {
		WiskOpdr.launchDataChanged = true;
		//System.out.println("launchDataChanged");
	}

	/**
	 * Zet de objectives (leerdoelen) waarmee de items (antwoordvakken) gelabeld kunnen worden.
	 */
	public static void setObjectives(String[][] objectives) {
		WiskOpdr.objectives = objectives;
	}
	
	public static void setCategories(String[] categorieString) {
		WiskOpdr.categorieString = categorieString;
	}
	
	/**
	 * Zet de misconceptions waarmee de items (feedback-tabbladen van antwoordvakken) gelabeld kunnen worden.
	 */
	public static void setMisconceptions(String[][] misconceptions) {
		WiskOpdr.misconceptions = misconceptions;
	}
	
	public static void setMccCategories(String[] mccCategorieString) {
		WiskOpdr.mccCategorieString = mccCategorieString;
	}
	
	/**
	 * Hiermee haalt de DWO de score per objective uit de suspenddata. 
	 */
	public static Hashtable getScoresPerObjective(String suspendData) {
		Map h = toSuspendData(suspendData);
		if (h == null)
			return null;
		Hashtable scoresPerObjective = null;
		if (h != null &&  h.containsKey("onsState")) {
			Map onsState = (Map) h.get("onsState");
			if (onsState.containsKey("scoresPerObjective"))
			{
				scoresPerObjective = new Hashtable( (Map) onsState.get("scoresPerObjective") ) ;
			}
		}
		return scoresPerObjective;
	}

	/**
	 * Hiermee haalt de DWO de loggegevens per gelogd item uit de suspenddata. 
	 */
	public static Hashtable getLog(String suspendData) {
		Map h = toSuspendData(suspendData);
		Hashtable log = new Hashtable();
		if (h != null && h.containsKey("log")) {
			log.putAll( (Map)  h.get("log") );
		}
		return log;
	}
	
	@Deprecated
	public static WiskOpdrPanel getWiskOpdrPanel(String launchDataString) {
		return getWiskOpdrPanel(launchDataString, language);
	}
	/**
	 * Hiermee vraagt de DWO een WiskOpdr-panel op voor tekst enz. op de modulepagina. 
	 */
	public static WiskOpdrPanel getWiskOpdrPanel(String launchDataString, Locale locale) {
		language = locale;
		WiskOpdr wiskOpdr = new WiskOpdr();
		Hashtable launchData = null;
		if (launchDataString != null) {
			Object o = StringCodeObject.decodeStringToObject(launchDataString);
			launchData = (Hashtable) o;
		}
		if (launchData == null)
			launchData = wiskOpdr.makeDefaultParamValues(0);
		WiskOpdrPanel wop = new WiskOpdrPanel(launchData, wiskOpdr);
		return wop;
	}

	/**
	 * Hiermee vraagt de DWO een WiskOpdrEdit-panel op voor het bewerken van tekst enz. op de modulepagina. 
	 */
	public static WiskOpdrEditPanel getWiskOpdrEditPanel(String launchDataString) {
		WiskOpdr wiskOpdr = new WiskOpdr();
		WiskOpdrEditPanel wop = new WiskOpdrEditPanel(launchDataString, wiskOpdr);
		return wop;
	}
	
	public static int getObjectSize(Object o) {
		return StringCodeObject.encodeObjectToString(o).length();
	}
	/**
	 * Hiermee kan de default codebase worden opgevraagd. 
	 */
	private static URL defaultCodeBase() {
		if (defaultCodeBase == null)
			try {
				defaultCodeBase = new URL("http://ws.fisme.science.uu.nl/javaclasses/");
			} catch (MalformedURLException e) {
				throw new RuntimeException(e.toString(), e); // should not
																// happen!
			}
		return defaultCodeBase;
	}
	
	/**
	 * Hiermee haalt de DWO de paginascores uit de suspenddata. 
	 * wordt ook aangeroepen door getScoreMapList(...)
	 */
	public static String[] geefPaginaScores(String suspendData) {
		Map h = toSuspendData(suspendData);
		if(h == null) return null;
		
		int[][] scores = null;
		boolean[][] bezocht = null;
		if (h.containsKey("onsState")) {
			Map onsState = (Map) h.get("onsState");
			if (onsState.containsKey("orScores"))
				scores = OpdrNavStruct.toIntArrayArray(onsState.get("orScores"));
			bezocht = OpdrNavStruct.toBooleanArrayArray(onsState.get("bezocht"));
		}
		if (scores == null || scores.length == 0 || scores[0].length == 0)
			return null;
		String[] log = new String[scores[0].length];
		for (int i = 0; i < scores[0].length; i++) {
			if(bezocht == null || bezocht[0][i])
				log[i] = Integer.toString( scores[0][i] );
		}
		return log;
	}
	/**
	 * Hiermee haalt de DWO de paginaCorrectiescores uit de reviewdata. 
	 * wordt ook aangeroepen door getScoreMapList(...)
	 */
	public static String[] geefPaginaScores(String suspendData, String reviewStateString) {
		Map h = toSuspendData(suspendData);
		if(h == null) return null;
		
		int[][] scores = null;
		int[] scoreCorrecties = geefPaginaCorrectieScores(reviewStateString);
		
		boolean[][] bezocht = null;
		if (h.containsKey("onsState")) {
			Map onsState = (Map) h.get("onsState");
			if (onsState.containsKey("orScores"))
				scores = OpdrNavStruct.toIntArrayArray(onsState.get("orScores"));
			bezocht = OpdrNavStruct.toBooleanArrayArray(onsState.get("bezocht"));
// situatie: bezocht = [[true,true]] en scores = null;			
			if(bezocht != null && scores == null) {
			  scores = new int[bezocht.length][];
			}
			  for (int i = 0; scores != null && bezocht != null && i < Math.min(scores.length, bezocht.length); i++) {
			    if ( bezocht[i] != null && scores[i] == null) {
			      scores[i] = new int[bezocht[i].length];
			    }
			  }
			
			
		}
		if (scores == null || scores.length == 0 || scores[0].length == 0)
			return null;
		String[] log = new String[scores[0].length];
		for (int i = 0; i < scores[0].length; i++) {
			if(bezocht == null || bezocht[0][i])
				log[i] = Integer.toString( scores[0][i] + getInt(scoreCorrecties,i) );
		}
		return log;
	}
	
	private static int getInt(int[] array, int i) {
		if(array==null) return 0;
		if(i >= array.length) return 0;
		return array[i];
	}
	
	public static int[] geefPaginaCorrectieScores(String reviewStateString) {
		Map reviewState = toHashtable(reviewStateString);
		if(reviewState == null) return null;
		
		int[] correctieScores = null;
		if (reviewState.containsKey("opdrContStates")) {
			Hashtable[][] opdrContReviewStates = OpdrNavStruct.toHashtableArrayArray(reviewState.get("opdrContStates"));
			correctieScores = new int[opdrContReviewStates[0].length];
			for(int i=0 ; i<opdrContReviewStates[0].length ; i++) {
				if(opdrContReviewStates[0][i]!=null)
					correctieScores[i] = OpdrNavStruct.getScoreCorrectiePage(opdrContReviewStates[0][i]);
			}
		}
		return correctieScores;
	}
	
	public static String[] geefPaginaIsCorrected(String reviewStateString) {
		Map reviewState = toHashtable(reviewStateString);
		if(reviewState == null) return null;
		
		boolean[] isCorrected = null;
		String[] isCorrectedString = null;
		if (reviewState.containsKey("opdrContStates")) {
			Hashtable[][] opdrContReviewStates = OpdrNavStruct.toHashtableArrayArray(reviewState.get("opdrContStates"));
			isCorrected = new boolean[opdrContReviewStates[0].length];
			isCorrectedString = new String[opdrContReviewStates[0].length];
			for(int i=0 ; i<opdrContReviewStates[0].length ; i++) {
				if(opdrContReviewStates[0][i]!=null) {
					int correctie = OpdrNavStruct.getScoreCorrectiePage(opdrContReviewStates[0][i]);
					isCorrected[i] = (correctie != 0);
					isCorrectedString[i] = Boolean.toString(isCorrected[i]);
				}
			}
		}
		return isCorrectedString;
	}

	private static Map toSuspendData(String suspendData) {
		Object o;
		if(suspendData != null && suspendData.startsWith("{"))
			o = JSONValue.parse(suspendData);
		else
			o = StringCodeObject.decodeStringToObject(suspendData);
		return (Map) o;
	}
	
	/*private static Hashtable toReviewState(String reviewStateString) {
		Object reviewStateObject = JSONValue.parse(reviewStateString);
		Hashtable reviewState = new Hashtable();
		Map reviewStateMap = null;
		if (reviewStateObject instanceof Map)
			reviewState.putAll((Map) reviewStateObject);
		return reviewState;
	}*/

	public static Hashtable toHashtable(String JSONString) {
		Object object = JSONValue.parse(JSONString);
		Hashtable h = new Hashtable();
		Map map = null;
		if (object instanceof Map)
			h.putAll((Map) object);
		return h;
	}
	
	/**
	 * Hiermee worden de paginatijden uit de suspenddata gehaald. 
	 * (hoe lang een leerling op een pagina bezig is)
	 */
	private static String[] geefPaginaTijden(String suspendData) {
		Map h = toSuspendData(suspendData);
		if(h == null) return null;
		String[][] times = null;
		if ( h.containsKey("onsState")) {
			Map onsState = (Map) h.get("onsState");
			times = OpdrNavStruct.toStringArrayArray( onsState.get("orTimes"));
		}
		if (times == null || times.length == 0 || times[0].length == 0)
			return null;
		return times[0];
	}
	
	
	public static boolean isCijfersOfLetters(String launchData) {
		Object o = StringCodeObject.decodeStringToObject(launchData);
		if (o == null) return false; // cijfers
		Map h = (Map) o;
		o = h.get("instellingen");
		if(o == null) return false;
		o = StringCodeObject.decodeStringToObject(o.toString());
		if(o != null) h = (Map)o;
		return Boolean.TRUE.equals(h.get("abcDeelOpdr"));
		
	}
	/**
	 * Hiermee worden de maximale scores uit de launchData gehaald. 
	 * 
	 */
	public static String[] geefPaginaScoresMax(String launchData) {
		Object o = StringCodeObject.decodeStringToObject(launchData);
		if (o == null)
			return null;
		Hashtable h = (Hashtable) o;

		String aantalOpdrachtenString = "0";
		if (h.containsKey("aantalOpdrachten_1"))
			aantalOpdrachtenString = (String) h.get("aantalOpdrachten_1");
		int aantalOpdrachten = Integer.parseInt(aantalOpdrachtenString);

		String[] scoresMaxString = new String[aantalOpdrachten];
		for (int i = 0; i < aantalOpdrachten; i++) {
			String opdracht = (String) h.get("opdracht_1_" + (i + 1));
			if (opdracht != null && !opdracht.equals("")) {
				Object scoresMax = null;
				Object ob = StringCodeObject.decodeStringToObject(opdracht);
				Hashtable ht = (Hashtable) ob;
				if (ht != null)
					scoresMax = ht.get("scoreMax");
				scoresMaxString[i] = scoresMax == null ? null : scoresMax.toString();
			}
		}
		return scoresMaxString;
	}
	
	/**
	 * Schrijft de scores weg naar het LMS (gebruikt binnen het LMS van de uitgevers).
	 */
	public static void setLMSScore() {
		if (!deployDwoGrading && ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))) {
			double score = ((WiskOpdr) applet).getScore();
			score = 1.0 * score / 100;
			String scoreString = new Double(score).toString();
			if (((WiskOpdr) applet).api != null) {
				((WiskOpdr) applet).api.LMSSetValue(CMI_CORE_SCORE_RAW, scoreString);
			}
		}
	}

	/**
	 * Schrijft de state (suspenddata) weg naar het LMS (gebruikt binnen het LMS van de uitgevers).
	 */
	public static void setLMSState() {
		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) {
			if (((WiskOpdr) applet).api instanceof WNScormAPI) {
				String s = ((WiskOpdr) applet).getState();
				((WiskOpdr) applet).api.LMSSetValue(CMI_SUSPEND_DATA, s);
			}
		}
	}

	/**
	 * Schrijft de variabele 'completed'  weg naar het LMS (gebruikt binnen het LMS van de uitgevers).
	 */
	public static void setCompleted(boolean b) {
		if (((WiskOpdr) applet).api instanceof WNScormAPI && !COMPLETED) {
			COMPLETED = b;
			if (COMPLETED)
				((WiskOpdr) applet).api.LMSSetValue(CMI_CORE_LESSON_STATUS, LESSON_STATUS_completed);
		}
	}

	public static String getLearner_id() {
		if( applet != null && applet.api != null) {
			return applet.api.LMSGetValue("cmi.core.student_id");
		} 
		return "learner_id";
	}

	public static String getLearnerName() {
		if( applet != null && applet.api != null) {
			return applet.api.LMSGetValue("cmi.core.student_name");
		}
		return "learner_name";
	}
	
	public static String getOAuthToken() {
		if( applet != null) {
			String token =  applet.getParameter("oauth_token");
			if(token != null) return token;
		}
		return "oauth_token";
	}
	
	
	/**
	 * Default contructor
	 */
	public WiskOpdr() {
		rb = ResourceBundle.getBundle("fi.wiskopdr.text.Text", language);
		bgcolor = BG_BLUE;
		mac = System.getProperty("os.name").equals("Mac OS X");
		if (lookAndFeel == null)
			lookAndFeel = UIManager.getLookAndFeel();
		deployVariant = "";
		applet = this;
	}
	
	/**
	 * initialisatie
	 */
	public void init() {
		applet = this;
		URL url = super.getCodeBase();
		if (url != null && url.getHost().equals("ws.fisme.science.uu.nl")) {
		}
		//else return; 

		dwo_env = getParameter("dwo_env"); // Zie Wiskopdr.isExperimental();
		abo_type = getParameter("abo_type"); // Zie WiskOpdr.isPremium();
		
		doJSON = "true".equals(getParameter("JSON"));
		doCAS  = CAS_LOCAL;
		String doCASString  = getParameter("CAS");
		if(doCASString!=null && !"".equals(doCASString))
			doCAS = doCASString;
		
		try {
			ideas = new IdeasClient(this, IdeasClient.IDEAS);
		} catch (Exception e) {
			LOG.log(Level.WARNING, "ideas", e);
		}
		
		applet = this;

		zetFont("SansSerif", 12);

		String launchDataString = super.getParameter("launchData");
		if (launchDataString != null) {
			Object o = StringCodeObject.decodeStringToObject(launchDataString);
			launchData = (Hashtable) o;
		}

		deployVariant = super.getParameter("deployVariant");
		//deployVariant = "MW";

		defaultParamValues = makeDefaultParamValues(0);

		try {
			api = Scorm.findAPI(this);
			launchDataString = api.LMSGetValue("cmi.launch_data");
			if (launchDataString != null && launchDataString.length() > 10)
				launchData = (Hashtable) StringCodeObject
						.decodeStringToObject(launchDataString);
		} catch (Exception e) {
		}
// init time: set lessonMode early
		try {
			lessonMode = LessonMode.valueOf(api.LMSGetValue(CMI_CORE_LESSON_MODE));
		} catch (Exception e1) {
			lessonMode = LESSON_MODE_normal;
		}

		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) {
			deployDwoGrading = false;
			api = new WNScormAPI(this);
		}

		String s = super.getParameter("deployVariantDWO");
		if (s != null && s.equals("MW")) {
			deployVariant = "MW";
			deployDwoGrading = true;
		}
		if (s != null && s.equals("GR")) {
			deployVariant = "GR";
			deployDwoGrading = true;
		}

		getContentPane().setLayout(null);

		setJSObjectOwner(this);

		String langArg = getParameter("language");
		if (langArg == null || langArg.equals(""))
			langArg = "nl";
		language = new Locale(langArg, "");
		
		rb = ResourceBundle.getBundle("fi.wiskopdr.text.Text", language);
		setLocale(language);
		JComponent.setDefaultLocale(language);

		bgcolor = BG_BLUE;
		bgcolorEditor = BG_BLUE;
		String kleurcode = getParameter("bgcolor");
		if (kleurcode != null)
			bgcolor = new Color(Integer.parseInt(kleurcode.substring(1), 16));
		getContentPane().setBackground(bgcolor);
		setBackground(bgcolor);

		// Fi-logo, copyright
		FIButton fiButton = new FIButton("WiskOpdr", new String[] { 
				"versie-info:20150101", 
				"auteur: Peter Boon", 
				"programmeur: Peter Boon",
				"Freudenthal Instituut", 
				"www.fisme.science.uu.nl", "" });
		fiButtonPanel = new JPanel();
		fiButtonPanel.setOpaque(false);
		fiButtonPanel.setLayout(null);
		fiButton.setBounds(0, 0, 20, 30);
		if ("GR".equals(WiskOpdr.deployVariant)) {
			fiButtonPanel.setBounds(16, getHeight() - 40, 20, 30);
		} else {
			fiButtonPanel.setBounds(getSize().width - 25, getSize().height - 50, 20, 28);
			Color c = WiskOpdr.bgcolor;
			c = new Color(c.getRed() - 20, c.getGreen() - 20, c.getBlue() - 20);
			if ("MW".equals(WiskOpdr.deployVariant))
				c = new Color(236, 245, 246);
			fiButtonPanel.setBackground(c);
		}
		fiButtonPanel.add(fiButton);
		getContentPane().add(fiButtonPanel, 0);

		AppletUtil au = new AppletUtil(this);
		loadImages(au);

		viewButton = new Button("View");
		viewButton.setBounds(getSize().width - 160, getSize().height - 80, 80, 20);
		viewButton.addActionListener(this);

		if (getParent() instanceof ScormEditMainFrame) {
			scormEditComponent = getEditComponent(defaultParamValues);// (launchData);//
			((ScormEditMainFrame) getParent()).setScormEditComponent(scormEditComponent);
			((ScormEditMainFrame) getParent()).setBackground(getBackground());
			getContentPane().add(scormEditComponent.getComponent(), 0);
			((Component) scormEditComponent).setBackground(getBackground());
			scormEditComponent.getComponent().setSize(getSize().width, getSize().height);
			getContentPane().add(viewButton, 0);
		} else {
			maakOpdrNavStruct();
		}

//		try {
//			phrasebook = new MathematicaLink(this); // doCAS?
//		} catch (MalformedURLException e) {
//			LOG.log(Level.WARNING, "MathematicaLink", e);
//		} // echte applet

		addComponentListener(this);
	}

	/**
	 * Vraag of het applet in de nakijkmodus wordt gebruikt.
	 */
	public boolean reviewMode() {
		return api != null && lessonMode == LESSON_MODE_review;
	}
	
	public boolean toetsLockedMode() {
		return toetsLocked;
	}

	/**
	 * Inladen van de basis plaatjes
	 */
	public void loadImages(AppletUtil au) {
		GOEDKRUL = null;
		if (rb.getLocale().toString().equals("nl"))
			GOEDKRUL = au.getImage("resources/goedkrul.gif");
		else 
			GOEDKRUL = au.getImage("resources/goedkrul_en.gif");
		FOUTKRUIS = au.getImage("resources/foutkruis.gif");
		HALFKRUL = au.getImage("resources/goedkrulhalf.gif");

		if ("MW".equals(deployVariant) || "GR".equals(deployVariant)) {
			GOEDKRUL = NWButtonUI.loadImage("mw_vinkje_groen.png", this);
			FOUTKRUIS = NWButtonUI.loadImage("mw_kruisje_rood.png", this);
			HALFKRUL = NWButtonUI.loadImage("mw_vinkje_geel.png", this);
		}
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(GOEDKRUL, 0);
		tr.addImage(FOUTKRUIS, 0);
		tr.addImage(HALFKRUL, 0);
		try {
			tr.waitForAll();
		} catch (Exception e) {
		}
	}
	
	/**
	 * Maakt een instantie van OpdrNavStruct en plaats het op het content pane.
	 */
	private void maakOpdrNavStruct() {
		MyOpdrContainer myOpdrContainer = new MyOpdrContainer(0, 0, getSize().width, getSize().height);
		myOpdrContainer.setBackground(getBackground());
		ons = new OpdrNavStruct(this, myOpdrContainer, 0, 0, getSize().width, getSize().height, api, null);
		ons.setBackground(getBackground());
		ons.addActionListener(this);
		if( ons.needPremium() && ! isPremium()) {
		  LOG.severe("Needs premium school");
		  needsPremium = new JLabel(rb.getString("needsPremium"));
		  needsPremium.setSize(needsPremium.getPreferredSize());
		  getContentPane().add(needsPremium);
		} else
		  getContentPane().add(ons);
	}

	/**
	 * override van getParameter van Applet.
	 * Als de parameters niet op de gewone wijze worden gevonden, 
	 * dan kunnen ze eventueel ook de aanwezige launchData dan wel defaultParamValues worden gehaald
	 */
	public String getParameter(String name) {
		String value = null;
		value = super.getParameter(name);
		if (value == null && launchData != null)
			value = (String) launchData.get(name);
		if (value == null && defaultParamValues != null) // Is soms null! (Wim)
			value = (String) defaultParamValues.get(name);
		return value;
	}

	/**
	 * Als er geen launchData zijn, dan wordt bij opstarten teruggevallen op deze basisinstellingen
	 */
	public Hashtable makeDefaultParamValues(int variant) {
		Hashtable h = new Hashtable();
		h.put("language", "nl");
		h.put("bgcolor", "#FFFFFF");

		Hashtable defaultEditModeLaunchData = new Hashtable();
		defaultEditModeLaunchData.put("titel", "Titel");
		defaultEditModeLaunchData.put("tekst", "Tekst");
		defaultEditModeLaunchData.put("randVarString", "");
		defaultEditModeLaunchData.put("antwoordString", "$f@");
		defaultEditModeLaunchData.put("herleiding", new Boolean(false));
		defaultEditModeLaunchData.put("antwoordString", "$f@");
		defaultEditModeLaunchData.put("herleiding", new Boolean(false));
		defaultEditModeLaunchData.put("exact", new Boolean(false));
		defaultEditModeLaunchData.put("soortHerleiding", new Integer(1));
		defaultEditModeLaunchData.put("puntenGelijkwaardig", new Integer(10));
		defaultEditModeLaunchData.put("puntenHerleiding", new Integer(0));
		defaultEditModeLaunchData.put("puntenExact", new Integer(0));
		defaultEditModeLaunchData.put("vergelijking", new Boolean(false));
		defaultEditModeLaunchData.put("eindOplossingNodig", new Boolean(true));
		defaultEditModeLaunchData.put("puntenEindOplossing", new Integer(10));
		defaultEditModeLaunchData.put("startString", "$f@");
		defaultEditModeLaunchData.put("bewerkingKnoppen", new Boolean(false));
		defaultEditModeLaunchData.put("abcKnop", new Boolean(false));
		defaultEditModeLaunchData.put("subKnop", new Boolean(false));
		defaultEditModeLaunchData.put("hasTitle", new Boolean(false));
		defaultEditModeLaunchData.put("hasAntwoordVak", new Boolean(false));
		defaultEditModeLaunchData.put("scheidingX", new Integer(780));

		defaultEditModeState = StringCodeObject.encodeObjectToString(defaultEditModeLaunchData);

		h.put("aantalActiviteiten", "1");
		h.put("activiteit_1", "Onderdeel 1");
		h.put("aantalOpdrachten_1", "1");
		h.put("opdracht_1_1", defaultEditModeState);
		h.put("mode", "0");
		return h;
	}

	/**
	 * Vraag het JSobject op dat gebruikt wordt in de JavaScript communicatie (bv voor html popups)
	 * implementeert interface LinkIF 
	 */
	public Object getJSObject() {
		return window;
	}

	/**
	 * Zet het JSobject op dat gebruikt wordt in de JavaScript communicatie (bv voor html popups)
	 * implementeert interface LinkIF 
	 */
	public void setJSObject(Object window) {
		this.window = window;
	}

	/**
	 * Start van het applet. Hier worden suspenddata en andere data binnegehaald
	 */
	public void start() {
		LOG.info("Start");
		log = new Hashtable();
		reviewData = new Hashtable();
		suspendData = null; // Deze moet altijd VERS zijn.....
		sessionStartTime = System.currentTimeMillis();
		if (api != null) {
			String s = api.LMSGetValue(CMI_SUSPEND_DATA);
			review = reviewMode();
			String locString = api.LMSGetValue(CMI_CORE_LESSON_LOCATION);
			if (review || !locString.isEmpty()) {
				//System.out.println("locString: " + locString);
				int loc = -1;
				try {
					loc = Integer.parseInt(locString);
				} catch (NumberFormatException e) {
				}
				ons.setReviewLocation(loc);
				//System.out.println("Review location :" + loc);
				if (s == null || s.equals(""))
					ons.setEmptyState();
			}
			if (s != null && !s.equals("")) {
				// Switch to JSON state: {...}
				if (s.charAt(0) == '{')
					setJSONState(s);
				else

					setState(s);
			}
			loaded = true;
		}
		LOG.info("State");
		Thread startDraad = new Thread() {
			public void run() {
				try {
					sleep(200);
				} catch (InterruptedException e) {
				}
				if (scormEditComponent != null)
					scormEditComponent.reset();
				else if (ons != null) {
					ons.start();
					if (("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) && !deployDwoGrading && ons.getScore() == 0) { 
						api.LMSSetValue(CMI_CORE_SCORE_RAW, "0.00");
					}
				}
			}
		};
		startDraad.start();
	}
	
	/**
	 * Wordt aangeroepen bij afsluiten in een html-omgeving (bv scormpackage) 
	 * om de gegevens al voor de echte stop van het applet weg te kunnen schrijven 
	 */
	public void stopSco() {
		if (api != null) {
			stop();
			api = null;
		}
	}

	/**
	 * Stop van het applet. Hier worden de gegevens weggeschreven 
	 */
	public void stop() {
		if (api != null && loaded) {
			
			LOG.info("opgestuurde reviewStateString :" + "dummy");
			
					ons.stop();
			//Thread stopDraad = new Thread() {
			//	public void run() {
					String s;
					if (doJSON)
						s = getJSONState();
					else
						s = getSuspendState();

					double score = getScore();
					if (!deployDwoGrading && ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)))
						score = 1.0 * score / 100;
					String d = new Double(score).toString();
					String t = getSessionTime();
					String location = "0";
					if(ons!=null ) location = "" + ons.geefOpdrachtNr(); // NPE want geen ons?

					//ons.getScoresObjectives();

					if (lessonMode == null)
						lessonMode = LESSON_MODE_normal;
					if (lessonMode == LESSON_MODE_browse) {
						WiskOpdr.deployVariant = "";
						return;
					}
					api.LMSSetValue(CMI_CORE_LESSON_LOCATION, location); // Altijd!
					if (review) {
						Hashtable reviewState = ons.getReviewStateHashtable();
						String reviewStateString = JSONValue.toJSONString(reviewState);
						LOG.info("weggeschreven reviewStateString: "+ reviewStateString);
						String old = api.LMSGetValue(CMI_COMPLETION_STATUS);
						if("completed" .equals(old) ) {
						  api.LMSSetValue(CMI_COMPLETION_STATUS, "review"); // ontzegel voor review FIXME security hack
						  api.LMSSetValue(CMI_COMMENTS_FROM_LMS_0_COMMENT, reviewStateString);
						  api.LMSSetValue(CMI_CORE_SCORE_RAW, d);
						  api.LMSSetValue(CMI_COMPLETION_STATUS, old);
					    }
//						api.LMSSetValue(CMI_CORE_LESSON_LOCATION, location); // Altijd, ook als reviewData empty is!
						if (suspendData == null || suspendData.isEmpty() || reviewData == null || reviewData.isEmpty())
							return;
						//suspendData.put("reviewData", reviewData);
						//System.out.println("bij stop: " + suspendData);
						String suspendDataString;
						if(doJSON)
							suspendDataString = JSONValue.toJSONString(suspendData);
						else
							suspendDataString = StringCodeObject.encodeObjectToString(suspendData);
						//api.LMSSetValue(CMI_SUSPEND_DATA, suspendDataString);
						Object o = reviewData.get("toetsLocked");
						if( Boolean.TRUE.equals(o))
							api.LMSSetValue(CMI_COMPLETION_STATUS, "completed");
						else if(Boolean.FALSE.equals(o))
							api.LMSSetValue(CMI_COMPLETION_STATUS,  "incomplete");
						
						
						
						return;
					}
					if (lessonMode ==LESSON_MODE_normal && toetsLocked) { // && ons.getMode()==3
						return;
					}
					
					
					api.LMSSetValue(CMI_CORE_SESSION_TIME, t);
					api.LMSSetValue(CMI_CORE_SCORE_RAW, d);
					api.LMSSetValue(CMI_CORE_SCORE_MAX, "100");

					if (s != null && !"".equals(s))
						api.LMSSetValue(CMI_SUSPEND_DATA, s);
				//}
			//};
			//stopDraad.start();
			
			
		}
	}

	/**
	 * Voor het tussentijds wegschrijven van de score en suspenddata naar de DWO
	 */
	//boolean writing = false;
	
	public void updateResults() {
		if (api instanceof JSScormAPI)
			return;
		if (api instanceof WNScormAPI)
			return;

		final String s = doJSON ? getJSONState() : getState();
		double score = getScore();
		final String d = new Double(score).toString();

		if (api != null) {
			if (lessonMode == null)
				lessonMode = LESSON_MODE_normal;
			if (lessonMode == LESSON_MODE_browse || review)
				return;
			if (lessonMode == LESSON_MODE_normal && toetsLocked) {
				return;
			}
			
			//Thread updateDraad = new Thread() {
			//	public void run() {
			//		if(!writing) {
			//			writing = true;
						api.LMSSetValue(CMI_SUSPEND_DATA, s);
						api.LMSSetValue(CMI_CORE_SCORE_RAW, d);
			//			writing = false;
			//		}
			//	}
			//};
			//updateDraad.start();
			
			
		} 
	}

	public void destroy() {
		if (ons != null)
		{
			ons.destroy();
			ons = null;
		}
		if (scormEditComponent != null)
			scormEditComponent.end();
	}

	private boolean doJSON = false;

  private JLabel needsPremium;

	public void setJSONState(String s) {
		try {
			setJSONState(s, true);
		} catch(Exception e) {
			setJSONState(s, false);
		}
	}
	
	private void setJSONState(String s, boolean allow) {

		Object o;
		o = JSONValue.parse(s);
		if (o instanceof Map) {
			Map h = (Map) o;
			Object value;
			Hashtable log = new Hashtable();
			Hashtable onsState = new Hashtable();
			Hashtable reviewData = new Hashtable();
			value = h.get("onsState");
			if (value != null)
				onsState.putAll((Map) value);
			value = h.get("log");
			if (value != null)
				log.putAll((Map) value);
			value = h.get("reviewData");
			if (value != null)
				reviewData.putAll((Map) value);
			toetsLocked = Boolean.TRUE.equals(reviewData.get("toetsLocked"));
			toetsLocked |= "completed".equals(api.LMSGetValue(CMI_COMPLETION_STATUS));

			WiskOpdr.log = log;
			WiskOpdr.reviewData = reviewData;
			suspendData = new Hashtable(h);

			ons.zetToetsLocked(toetsLocked);

		if(/*review ||*/ toetsLocked && allow) {
			String reviewStateString = api.LMSGetValue(CMI_COMMENTS_FROM_LMS_0_COMMENT);
			Hashtable reviewState = toHashtable(reviewStateString);
			
			//System.out.println("Hashtable state :" + JSONValue.toJSONString(onsState));
			//System.out.println("Hashtable reviewstate:" + JSONValue.toJSONString(reviewState));
			
			//PATCH
			Hashtable[][] opdrContStates;
			opdrContStates = OpdrNavStruct.toHashtableArrayArray(onsState.get("opdrContStates"));
			for(int i=0 ; i<opdrContStates[0].length ; i++) {
				if(opdrContStates[0][i]!=null) {
					Object object = opdrContStates[0][i].get("interactiePanelStates");
					List l = (List) object;
					if(l.size() > 5)
					{ 	
						object = l.get(5);
						Map m = (Map) object;
						object = m.get("interactiePanelStates");
						l = (List) object;
						for(int x=0; x<5; x++) l.add(0, null);
						opdrContStates[0][i].put("interactiePanelStates", object);
					}
					
				}
			}
			onsState.put("opdrContStates", opdrContStates);
			//
			
			//System.out.println("Hashtable patched state:" + JSONValue.toJSONString(onsState));
			ons.mergeReviewStateHashtable(onsState, reviewState);
			//System.out.println("Hashtable merged state:" + JSONValue.toJSONString(onsState));
			//System.out.println("Hashtable review state:" + JSONValue.toJSONString(reviewState));
			ons.setState(onsState,false);
			//ons.setJSONState(onsState, reviewState);
			//ons.setState(onsState, false);
		}
		else {
			ons.setJSONState(onsState);
		}
		if (review)
				ons.toonAantalSessies();
		}
	}

	/**
	 * De status van het applet wordt gezet met behulp de suspenddata.
	 */
	
	public void setState(String s) {
		try {
			setState(s, true);
		} catch(Exception e) {
			setState(s, false);
		}
	}
	
	private void setState(String s, boolean allow) {
		Object o = StringCodeObject.decodeStringToObject(s);
		if (o == null)
			return;
		Hashtable h = (Hashtable) o;

		Hashtable log = new Hashtable();
		Hashtable reviewData = new Hashtable();
		Hashtable onsState = new Hashtable();

		if (h.containsKey("log"))
			log = (Hashtable) h.get("log");
		if (h.containsKey("reviewData"))
			reviewData = (Hashtable) h.get("reviewData");
		if (h.containsKey("onsState"))
			onsState = (Hashtable) h.get("onsState");

		WiskOpdr.log = log;
		WiskOpdr.reviewData = reviewData;
		WiskOpdr.suspendData = h;

		if (reviewData.containsKey("toetsLocked"))
			toetsLocked = ((Boolean) reviewData.get("toetsLocked")).booleanValue();
		else
			toetsLocked = false;
		toetsLocked |= "completed".equals(api.LMSGetValue(CMI_COMPLETION_STATUS));

		ons.zetToetsLocked(toetsLocked);
		
		if(/*review ||*/ toetsLocked && allow) {
			String reviewStateString = api.LMSGetValue(CMI_COMMENTS_FROM_LMS_0_COMMENT);
			//System.out.println("opgehaalde reviewStateString :" + reviewStateString);
			Hashtable reviewState = toHashtable(reviewStateString);
			
			//System.out.println("Hashtable 1state :" + JSONValue.toJSONString(onsState));
			//System.out.println("Hashtable 1reviewstate:" + JSONValue.toJSONString(reviewState));
			
			ons.mergeReviewStateHashtable(onsState, reviewState);
			ons.setState(onsState,false);
		}
		else
			ons.setState(onsState);

		if (review) {
			ons.toonAantalSessies();
		}
	}

	public String getJSONState() {
		Hashtable onsState = null;
		Hashtable log = new Hashtable();
		Hashtable reviewData = new Hashtable();

		if (ons == null)
			return null;
		onsState = ons.getState();
		log = WiskOpdr.log;
		reviewData = WiskOpdr.reviewData;

		Hashtable h = new Hashtable();
		h.put("onsState", onsState);
		h.put("log", log);
		h.put("reviewData", reviewData);

		String s = JSONValue.toJSONString(h);

		return s;

	}

	public String getSuspendState() {
		return getState();
	}
	/**
	 * De status van het applet wordt opgevraagd.
	 */
	public String getState() {
		Hashtable onsState = null;
		Hashtable log = new Hashtable();
		Hashtable reviewData = new Hashtable();

		if (ons == null)
			return null;
		onsState = ons.getState();
		log = WiskOpdr.log;
		reviewData = WiskOpdr.reviewData;

		Hashtable h = new Hashtable();
		h.put("onsState", onsState);
		h.put("log", log);
		h.put("reviewData", reviewData);

		String s = StringCodeObject.encodeObjectToString(h);
		return s;
	}

	/**
	 * De score van het applet wordt opgevraagd.
	 */
	public double getScore() {
		if (ons == null)
			return 0;
		double score = ons.getScore();
		return score;
	}

	/**
	 * Methode voor PartialScoreIF. Gebruikt door de DWO om deelscores+tijden op te vragen
	 */
	public List getScoreMapList(SCORM12APIInterface api) {
		String launchData = api.LMSGetValue(CMI_LAUNCH_DATA);
		String suspendData = api.LMSGetValue(CMI_SUSPEND_DATA);
		String completed = api.LMSGetValue(CMI_COMPLETION_STATUS);
		String reviewStateString = 
				LESSON_STATUS_completed .equals(completed)
				? api.LMSGetValue(CMI_COMMENTS_FROM_LMS_0_COMMENT)
			    : "";
		String[] scoresMax, scoresRaw, sessions, isCorrected;
		scoresMax = geefPaginaScoresMax(launchData);
		boolean cijfersOfLetters = isCijfersOfLetters(launchData);
		if (scoresMax == null)
			return Collections.EMPTY_LIST;
		scoresRaw = geefPaginaScores(suspendData, reviewStateString);
		sessions = geefPaginaTijden(suspendData);
		isCorrected =  geefPaginaIsCorrected(reviewStateString);
		List list = new ArrayList(scoresMax.length);
		for (int i = 0; i < scoresMax.length; i++) {
			HashMap map = new HashMap();
			map.put(PartialScoreIF.SCORE_MAX, scoresMax[i]);
			
			if (scoresRaw != null && scoresRaw.length > i && scoresRaw[i] != null)
				map.put(PartialScoreIF.SCORE_RAW, scoresRaw[i]);
			else
				map.put(PartialScoreIF.SCORE_RAW, "");
			
			if (isCorrected != null && isCorrected.length > i && isCorrected[i] != null)
				map.put("isCorrected", isCorrected[i]);
			else
				map.put("isCorrected", "");
			
			map.put(PartialScoreIF.LOCATION, String.valueOf(i)); // tellen vanaf 1?

			if(cijfersOfLetters)
				map.put(PartialScoreIF.DESCRIPTION, Character.toString((char) ('a'+i)));
			else
				map.put(PartialScoreIF.DESCRIPTION, String.valueOf(i+1));
												// voor sylvia
			if (sessions != null && sessions.length > i && sessions[i] != null)
				map.put(PartialScoreIF.SESSION_TIME, sessions[i]);
			list.add(map);
		}
		return list;
	}

	public Map getScoreObjectivesMap(SCORM12APIInterface api) {
		String suspendDataStr = api.LMSGetValue(CMI_SUSPEND_DATA);
		if(suspendDataStr == null || suspendDataStr.length() == 0) return null;
		String launchDataStr = api.LMSGetValue(CMI_LAUNCH_DATA);
		Object o = StringCodeObject.decodeStringToObject(launchDataStr);
		if(! (o instanceof Hashtable)) return null;
		Hashtable launchData = (Hashtable)o;
// instellingen
		String instellingenStr = (String) launchData.get("instellingen");
		if(instellingenStr == null) return null;
		Hashtable instellingen = (Hashtable) StringCodeObject.decodeStringToObject(instellingenStr);
		if(instellingen == null) return null;
		o = instellingen.get("objectives");
		if( !(o instanceof String[][])) return null;
		String[][] objectives = (String[][]) o;
		o = instellingen.get("categorieString");
		if( !(o instanceof String[])) return null;
		String[] categorieString = (String[]) o;	
		Map h = new HashMap();
		
		int[][] totaalMaxObjectives = new int[objectives.length][];
		int[][] totaalScoreObjectives = new int[objectives.length][];
		for (int i = 0; i < objectives.length; i++) {
			int n = objectives[i].length;
			totaalMaxObjectives[i] = new int[n];
			totaalScoreObjectives[i]  = new int[n];
		}
		Hashtable suspendData = (Hashtable) StringCodeObject.decodeStringToObject(suspendDataStr);
		if(suspendData == null) return null;
		Hashtable onsState    = (Hashtable) suspendData.get("onsState");
		if(onsState == null) return null;
		int[][][][] scoresObjectives = (int[][][][]) onsState.get("scoresObjectives");
		int[][][][] scoresMaxObjectives = (int[][][][]) onsState.get("scoresMaxObjectives");

		if(scoresObjectives == null || scoresMaxObjectives == null) return null;
		sumObjectives(scoresObjectives, totaalScoreObjectives);
		sumObjectives(scoresMaxObjectives, totaalMaxObjectives);
		
		
		h.put("objectives", objectives);
		h.put("totaalMaxObjectives", totaalMaxObjectives);
		h.put("totaalScoreObjectives", totaalScoreObjectives);
		h.put("categorieString", categorieString);
		return h;
	}
	
	// FIXME assert not null?
	private void sumObjectives(int[][][][] elem,
			int[][] sum) {

		for(int i = 0; i < elem.length; i ++) {
			int[][][] elemi = elem[i];
			for(int j = 0; j < elemi.length; j++) {
				int[][] elemij = elemi[j];
				int lenk = Math.min(elemij.length, sum.length);
				for(int k = 0; k < lenk; k ++) {
					int[] elemijk = elemij[k];
					int[] sumk = sum[k];
					int lenl = Math.min(elemijk.length, sumk.length);
					for(int l = 0; l < lenl; l++) {
						sumk[l] += elemijk[l];
					}
				}
			}
		}
	}

	/**
	 * Berekend de sessietijd en geeft die als string terug.
	 */
	public String getSessionTime() {
		long sessionTime = System.currentTimeMillis() - sessionStartTime;
		String s = "";
		int hours = (int) sessionTime / 3600000;
		int minutes = (int) sessionTime / 60000 - hours * 60;
		int seconds = (int) sessionTime / 1000 - hours * 3600 - minutes * 60;
		if (hours < 10)
			s += "0";
		s += hours;
		s += ":";
		if (minutes < 10)
			s += "0";
		s += minutes;
		s += ":";
		if (seconds < 10)
			s += "0";
		s += seconds;
		return s;
	}

	/**
	 * Hier vraag de DWO of dit applet een editComponent heeft
	 */
	public boolean hasEditMode() {
		return true;
	}

	/**
	 * Hier vraag de DWO het editComponent op (voor het bewerken van een activiteit).
	 */
	public ScormEditComponentIF getEditComponent(Hashtable launchData) {
        abo_type = getParameter("abo_type"); // Zie WiskOpdr.isPremium();	  
		ScormEditComponent sec = new ScormEditComponent(launchData, this);
		launchDataChanged = false;
		return sec;
	}

	/**
	 * Hier vraag de DWO parameters op die bewerkt kunnen worden.
	 * Niet van toepassing als er een editComponent is.
	 */
	public Parameter[] getEditableParameters() {
		return null;
	}
	
	/**
	 * Hier vraag de DWO alle parameters.
	 * Niet van toepassing als er een editComponent is.
	 */
	public Parameter[] getAllParameters() {
		return null;
	}

	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == viewButton) {
			if (viewButton.getLabel().equals("View")) {
				getContentPane().remove(scormEditComponent.getComponent());
				launchData = scormEditComponent.getLaunchData();
				if (scormEditComponent != null)
					scormEditComponent.reset();
				maakOpdrNavStruct();
				viewButton.setLabel("Edit");
				ons.start();
				ons.revalidate();
			} else {
				getContentPane().remove(ons);
				if(needsPremium != null) getContentPane().remove(needsPremium);
				if (ons != null)
					ons.stop();
				getContentPane().add(scormEditComponent.getComponent());
				scormEditComponent.reset();
				viewButton.setLabel("View");
				((JComponent) scormEditComponent).revalidate();

				ons.getState();
				ons.getScoresObjectives();
				// System.out.println("score "+ ons.getScore());
				ons = null;
			}
			repaint();
		}
		// aanpassing voor selectie van bolletjes (Wim)
		// kan altijd, maar is niet efficiënt als je geen review doet.
		else if (review && api != null && e.getSource() == ons && "select".equals(e.getActionCommand())) {
			int opdrachtNr = ons.geefOpdrachtNr();
			api.LMSSetValue(CMI_CORE_LESSON_LOCATION, Integer.toString(opdrachtNr));
		}
	}

	/**
	 * aanpassingen als het applet van maat verandert.
	 */
	public void componentResized(ComponentEvent e) {
		if (ons != null)
			ons.setSize(getSize());
		if (scormEditComponent != null)
			((Component) scormEditComponent).setSize(getSize());
		fiButtonPanel.setBounds(getSize().width - 25, getSize().height - 50, 20, 28);
		if ("GR".equals(WiskOpdr.deployVariant))
			fiButtonPanel.setBounds(16, getHeight() - 40, 20, 30);
		viewButton.setLocation(getSize().width - 125, getSize().height - 20);

	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	/**
	 * Hiermee vraagt de DWO de contentpagina.
	 * Gebruikt bij volg-app voor het maken van een screenshot
	 */
	public Component getContentPage() {
		if (ons == null)
			return getContentPane();
		return ((MyOpdrContainer) ons.getOpdrContainer()).getContentPane();
	}

	/**
	 * Voor de mergeoptie voor activiteiten: Twee launchData-strings worden samengevoegd tot een.
	 */
	public String mergeLaunchData(String launchData1, String launchData2) {
		Hashtable h1 = (Hashtable) StringCodeObject.decodeStringToObject(launchData1);
		Hashtable h2 = (Hashtable) StringCodeObject.decodeStringToObject(launchData2);
		int aantal1 = Integer.parseInt(h1.get("aantalOpdrachten_1").toString());
		int aantal2 = Integer.parseInt(h2.get("aantalOpdrachten_1").toString());
		for (int i = 1; i <= aantal2; i++) {
			Object opdr = h2.get("opdracht_1_" + i);
			h1.put("opdracht_1_" + (aantal1 + i), opdr);
		}
		h1.put("aantalOpdrachten_1", Integer.toString(aantal1 + aantal2));
		Hashtable image1 = (Hashtable) StringCodeObject.decodeStringToObject((String) h1.get(TekstImageVak.IMAGE_MAP));
		Hashtable image2 = (Hashtable) StringCodeObject.decodeStringToObject((String) h2.get(TekstImageVak.IMAGE_MAP));
		if (image2 == null)
			image2 = new Hashtable();
		if (image1 != null)
			image2.putAll(image1);
		if (!image2.isEmpty())
			h1.put(TekstImageVak.IMAGE_MAP, StringCodeObject.encodeObjectToString(image2));
		return StringCodeObject.encodeObjectToString(h1);
	}

	/**
	 * Om via een link naar een andere activiteit te gaan binnen de DWO.
	 */
	public boolean gotoScoNr(String nr) {
		if(nr.startsWith("."))
		{
			try {
				int opdrnr = Integer.parseInt(nr.substring(1));
				int actnr  = ons.geefActiviteitNr();
				ons.kiesOpdracht(actnr, opdrnr-1);
				ons.stelNavigatieIn(actnr, opdrnr-1);
			} catch(Exception _) {}
			return false;
		}
		if (api != null) {
			return "true".equals(api.LMSSetValue("dwo.goto.sconr", nr));
		}
		return false;
	}

	/**
	 * Vraagt codebase op en als er niet is krijg je de default-codebase
	 */
	public URL getCodeBase() {
		URL codeBase = super.getCodeBase();
		if (codeBase == null)
			return defaultCodeBase();
		return codeBase;
	}

	public void setJSObjectOwner(LinkIF applet) {
		setJSObject(applet.getJSObject());
		LinkRegel.setJSObjectOwner(applet);
		Link.setJSObjectOwner(applet);
		WidgetBridge.setJSObjectOwner(applet);
	}
	
	public static int getPageNr() {
		if(applet == null || applet.ons == null)
			return getEditPageNr();
		return applet.ons.geefOpdrachtNr();
	}
	
	public static String getUnit_id() {
		if( applet != null) {
			String parameter = applet.getParameter("scoViewNr");
			if(parameter != null)
				return parameter; 
		}
		return "scoViewNr";
	}
	
	public static LessonMode getLessonMode() {
		if (applet != null)
			return applet.lessonMode;
		return LESSON_MODE_browse;
	}
	
	
	public static int getEditPageNr() {
		return OpdrNavStructEdit.getInstance().geefOpdrachtNr();
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		String[] score = ons.getScores();
		int opgave = pageIndex;						// FIXME 1-to-1 pageindex en opgave
		String naam;
		String id = getLearner_id();
		String name = getLearnerName();
		String klas = "";
		if(api != null) {
			klas = api.LMSGetValue("dme.team");
		}
		naam = id + " - " + name + "; " + klas;
		if(opgave < ons.geefAantalOpdrachten(0)) {
			if(opgave != ons.geefOpdrachtNr()) ons.kiesOpdracht(0, opgave);
			// put 0, 0 at start of printable image
			Graphics2D g2d = (Graphics2D)graphics;
		    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		    Box header = Box.createVerticalBox();
		    JLabel line = new JLabel(naam);
		    header.add(line);
// TODO I18N
		    header.add( line = new JLabel(ons.getOpdrachtText() +
		    		" "+ (1+opgave) + ". " + rb.getString("score") + score[opgave]));
		    header.add( line = new JLabel(new Date().toLocaleString()));
		    header.setSize(header.getPreferredSize());
		    header.doLayout();
		    header.print(g2d);
		    g2d.translate(0, header.getHeight());
		    
		    ons.opdrContainer.prepareForPrint();
			JComponent component = ons.opdrContainer.getContentPane();
			JComponent popups    = ons.opdrContainer.popups;
			if(popups.getComponentCount() > 0) {
				Dimension size = component.getSize();
				component.setPreferredSize(size);
				component.setMaximumSize(size);
				component.setMinimumSize(size);
				popups.setSize(component.getWidth(), Short.MAX_VALUE);
				popups.doLayout();
				int h = 0;
				// trim
				for(int i = 0; i < popups.getComponentCount(); i++ )
				{
					Component p = popups.getComponent(i);
					int y = p.getHeight() + p.getY();
					h = Math.max(y,h);
				}
				popups.setSize(size.width, h);
				Box box = Box.createVerticalBox();
				
				box.add(component);
				box.add(popups);
				box.setSize(size.width, h + size.height);
				box.doLayout();
				component = box;
			}
			component.setPreferredSize(component.getSize());
			Frame f = new Frame();f.add(component);f.pack();
		    double width = component.getWidth();
		    double pageWidth = pageFormat.getImageableWidth();
		    double sx = pageWidth/width; sx = Math.min(1, sx);
		    double height = component.getHeight();
		    double pageHeight = pageFormat.getImageableHeight();
		    //sx = Math.min(sx, pageHeight/height);
			g2d.scale(sx, sx);
			component.print(graphics);
			f.remove(component);
			return PAGE_EXISTS;
		}
		return NO_SUCH_PAGE;
	}
}
