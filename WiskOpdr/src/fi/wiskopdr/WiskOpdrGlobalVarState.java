package fi.wiskopdr;

import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import fi.beans.iconan.ImageCache;
import fi.beans.iconan.SimpleCache;
import fi.wiskopdr.domainmodel.StudentModel;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleTeken;
import fi.wiskopdr.opdrnav.MyOpdrEditContainer;
import fi.wiskopdr.opdrnav.OpdrNavStructEdit;
import fi.wiskopdr.tekstobjects.ShareAction;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public class WiskOpdrGlobalVarState {

	//static vars WiskOpdr
	private WiskOpdr applet = null;
	private boolean formTimes = true;
	private boolean fToets = true;
	
	private Font tekstFont = new Font("SansSerif", Font.PLAIN, 12);
	private Font tekstFontBold = new Font("SansSerif", Font.BOLD, 12);
	private Font tekstFontKlein = new Font("SansSerif", Font.PLAIN, 12 - 1);
	private Font titelFont = new Font("SansSerif", Font.BOLD, 12 * 4 / 3);

	private Font formuleFont0 = new Font("TimesRoman", Font.PLAIN, 12 * 6 / 5);
	private Font formuleFont1 = new Font("TimesRoman", Font.PLAIN, 12 * 6 / 5);
	private Font formuleFont2 = new Font("TimesRoman", Font.PLAIN, 14);
	
	private Font formuleFont0Mac = new Font("SansSerif", Font.PLAIN, 12 * 8 / 7);
	private Font formuleFont1Mac = new Font("SansSerif", Font.PLAIN, 12 * 8 / 7);
	private Font formuleFont2Mac = new Font("SansSerif", Font.PLAIN, 12);

	private String[][] objectives = null;
	private String[] categorieString = null;
	private String[] mccCategorieString = null;
	private String[][] misconceptions = null;
    private Supplier<StudentModel> studentModelSupplier;
	
	//static vars MyOpdrEditPanel
	private int defaultMarginX = 15;
	private int  defaultMarginY = 10;
	private int  defaultDocWidth = 1024;
	private int  defaultDocHeight = 450;	
	
	//static vars TekstVakPanel
	private Map<String,Map<String,Object>> styles = new Hashtable<String,Map<String,Object>>();
	private Map<String,String> templatePages = new Hashtable<String,String>();
	private Map<String,String> templateComponents = new Hashtable<String,String>();
	private ArrayList<String> templatePagesKeys = new ArrayList<String>();
	private ArrayList<String> templateComponentsKeys = new ArrayList<String>();
	private String[] layerNames;
	private boolean[] layerVisible;
	private boolean TEMPLATE_EDITOR;
	private boolean fontOvererving = false;
	
	//static vars FormuleParser
	private boolean woordFormule = false;
	private boolean tweeHoofdletterVariabele = false;
	private boolean significantie = false;
	private boolean diffOperatoren = false;
		
	//static vars FormuleTeken
	private  boolean maalteken = false;
	private  boolean diffOperatorenFT = false;
	
	//static vars Antwoordvakkenvakken
	private boolean fontOverervingForm = false;
	
	// static vars Expressie
	private boolean hoekGraden;
	
	//static vars AntwoordFormuleVakEditPanel en AntwoordVergelijkvakEditPanel
	private boolean	significantieAan = false;
	
	//static vars ShareAction
	private static String shareMapData;
	private static Hashtable<String,Object> stateMap = new Hashtable<String, Object>();
	private static boolean sharingPossible;
	
	//static vars TekstImageVak
	private Object imagemap = new Hashtable();
	private ImageCache imagecache = new SimpleCache();
	
	//static vars TekstInteractiePanelVak
	private TekstInteractiePanelVak potentialSource;
	private TekstInteractiePanelVak potentialDest;
	private Properties jarOfMap = new Properties();
	
	public void storeCurrentGlobalVars() {
		applet = WiskOpdr.applet;
		formTimes = WiskOpdr.formTimes;
		fToets = WiskOpdr.fToets;
		
		tekstFont = WiskOpdr.tekstFont;
		tekstFontBold = WiskOpdr.tekstFontBold;
		tekstFontKlein = WiskOpdr.tekstFontKlein;
		titelFont =  WiskOpdr.titelFont;

		formuleFont0 = WiskOpdr.formuleFont0;
		formuleFont1 = WiskOpdr.formuleFont1;
		formuleFont2 = WiskOpdr.formuleFont2;
		
		formuleFont0Mac = WiskOpdr.formuleFont0Mac;
		formuleFont1Mac = WiskOpdr.formuleFont1Mac;
		formuleFont2Mac = WiskOpdr.formuleFont2Mac;
		
		objectives = WiskOpdr.objectives;
		categorieString = WiskOpdr.categorieString;
		mccCategorieString = WiskOpdr.mccCategorieString;
		misconceptions = WiskOpdr.misconceptions;
		studentModelSupplier = WiskOpdr.studentModelSupplier;
		
		defaultMarginX = MyOpdrEditContainer.defaultMarginX;
		defaultMarginY = MyOpdrEditContainer.defaultMarginY;
		defaultDocWidth = MyOpdrEditContainer.defaultDocWidth;
		defaultDocHeight = MyOpdrEditContainer.defaultDocHeight;	
		
		styles = TekstVakPanel.styles;
		templatePages =  TekstVakPanel.templatePages;
		templateComponents =  TekstVakPanel.templateComponents;
		templatePagesKeys = TekstVakPanel.templatePagesKeys;
		templateComponentsKeys = TekstVakPanel.templateComponentsKeys;
		layerNames = TekstVakPanel.layerNames;
		layerVisible = TekstVakPanel.layerVisible;
		TEMPLATE_EDITOR = TekstVakPanel.TEMPLATE_EDITOR;
		fontOvererving = TekstVakPanel.fontOvererving;
		
		woordFormule = FormuleParser.isWoordFormule();
		tweeHoofdletterVariabele = FormuleParser.isTweeHoofdletterVariabele();
		significantie = FormuleParser.isSignificantie();
		diffOperatoren = FormuleParser.isDiffOperatoren();
		
		maalteken = FormuleTeken.isMaalTeken();
		diffOperatorenFT = FormuleTeken.isDiffOperator();
		
		fontOverervingForm = AntwoordFormuleVak.fontOvererving;
		
		hoekGraden = Expressie.isHoekGraden();
		
		significantieAan = AntwoordFormuleVakEditPanel.significantieAan;
		
		sharingPossible = ShareAction.getSharingPossible();
		shareMapData = ShareAction.getSharedLaunchData();
		stateMap = ShareAction.getSharedState();
		
		imagemap = TekstImageVak.getImageMap();
		imagecache = TekstImageVak.getImageCache();
		
		potentialSource = TekstInteractiePanelVak.potentialSource;
		potentialDest = TekstInteractiePanelVak.potentialDest;
		jarOfMap = TekstInteractiePanelVak.jarOfMap;
		
	}
	
	public void setStoredGlobalVars() {
		
		MyOpdrEditContainer.defaultMarginX = defaultMarginX;
		MyOpdrEditContainer.defaultMarginY = defaultMarginY;
		MyOpdrEditContainer.defaultDocWidth = defaultDocWidth;
		MyOpdrEditContainer.defaultDocHeight = defaultDocHeight;	
		
		WiskOpdr.applet = applet;
		WiskOpdr.formTimes = formTimes;
		WiskOpdr.fToets = fToets;
		
		WiskOpdr.tekstFont = tekstFont;
		WiskOpdr.tekstFontBold = tekstFontBold;
		WiskOpdr.tekstFontKlein = tekstFontKlein;
		WiskOpdr.titelFont =  titelFont;

		WiskOpdr.formuleFont0 = formuleFont0;
		WiskOpdr.formuleFont1 = formuleFont1;
		WiskOpdr.formuleFont2 = formuleFont2;
		
		WiskOpdr.formuleFont0Mac = formuleFont0Mac;
		WiskOpdr.formuleFont1Mac = formuleFont1Mac;
		WiskOpdr.formuleFont2Mac = formuleFont2Mac;
		
		WiskOpdr.objectives = objectives;
		WiskOpdr.categorieString = categorieString;
		WiskOpdr.mccCategorieString = mccCategorieString;
		WiskOpdr.misconceptions = misconceptions;
		WiskOpdr.studentModelSupplier = studentModelSupplier;
		
		
		TekstVakPanel.styles = styles;
		TekstVakPanel.templatePages =  templatePages;
		TekstVakPanel.templateComponents =  templateComponents;
		TekstVakPanel.templatePagesKeys = templatePagesKeys;
		TekstVakPanel.templateComponentsKeys = templateComponentsKeys;
		TekstVakPanel.layerNames = layerNames;
		TekstVakPanel.layerVisible = layerVisible;
		TekstVakPanel.TEMPLATE_EDITOR = TEMPLATE_EDITOR;
		TekstVakPanel.fontOvererving = fontOvererving;
		
		FormuleParser.zetWoordFormule(woordFormule);
		FormuleParser.zetTweeHoofdletterVariabele(tweeHoofdletterVariabele);
		FormuleParser.zetSignificantie(significantie);
		FormuleParser.zetDiffOperatoren(diffOperatoren);
		
		FormuleTeken.zetMaalTeken(maalteken);
		FormuleTeken.zetDiffOperatoren(diffOperatorenFT);
		
		AntwoordFormuleVak.fontOvererving = true;
		AntwoordVergelijkingVak.fontOvererving = true;
		SimpelAntwoordFormuleVak.fontOvererving = true;
		SimpelAntwoordVergelijkingVak.fontOvererving = true;
		AntwoordTekstVak.fontOvererving = true;
		
		Expressie.zetHoekGraden(hoekGraden);
		
		AntwoordFormuleVakEditPanel.significantieAan = significantieAan;
		AntwoordVergelijkingVakEditPanel.significantieAan = significantieAan;
		
		ShareAction.setSharingPossible(sharingPossible);
		ShareAction.init(shareMapData);
		ShareAction.setSharedState(stateMap);
		
		TekstImageVak.setImageMap(imagemap, imagecache);
		
		TekstInteractiePanelVak.potentialSource = potentialSource;
		TekstInteractiePanelVak.potentialDest = potentialDest;
		TekstInteractiePanelVak.jarOfMap = jarOfMap;
		
		OpdrNavStructEdit.getInstance().setFont(WiskOpdr.tekstFont);
	}
	
}
