package fi.doorziendwo;

import java.util.*;

/*
    a class representing a lookup table for internationalization
    note the trick: the actual lookup table and its access method are
    declared static so they can be accessed as Table.lookUp
    to fill the lookup table from the language table create one instance of
    the class in the main applet class
*/
public class Table
{   static String[][] contents =
    //   keys               nederlands              engels             spaans
    {   // start button
        {"startButtonText", "Start Doorzien 4",
                            "Start Doorzien 4",
                            ""},
        // frame title/copyright                    
        {"titelText",       "Doorzien DWO",
                            "Doorzien DWE",
                            ""},
        {"versionText",     "versie 20061104",
                            "version 20061104",
                            ""},
        {"programText",     "Programmeur: Huub Nilwik",
                            "Programmer: Huub Nilwik",
                            ""},
        {"authorText",      "auteur: ",  
                            "author: ",
                            ""},
        {"fiText",          "Freudenthal Instituut", 
                            "Freudenthal Institute", 
                            ""},
        {"developText",     "Ontwikkeld door het",  
                            "Developed by the",
                            ""},                                    
        {"forEPNText",      "voor EPN/Getal en Ruimte",  
                            "for EPN/Number and Space",
                            ""},                     
        {"copyRightText",   "\u00A9 2004-2006",  
                            "\u00A9 2004-2006",
                            ""},                     
                            
                            
        // applet menu                    
        {"appletText",      "Applet",
                            "Applet",
                            ""},
        {"stopText",        "Stop",
                            "Stop",
                            ""},
        {"infoText",        "Info",
                            "Info",
                            ""},
        // figure menu                    
        {"figureText",      "Figuren",
                            "Objects",
                            ""},
                            
        {"octahedronText",  "Achtvlak",
                            "Octahedron",
                            ""},
        {"blockText",       "Balk",
                            "Block",
                            ""},
        {"cylinderText",    "Cilinder",
                            "Cylinder",
                            ""},
        {"housesText",      "Huizen",
                            "Houses",
                            ""},
        {"pirHouseText",    "Huis met piramidedak",
                            "House with piramidal roof",
                            ""},
        {"edgeHouseText",   "Huis met schilddak",
                            "House with edged roof",
                            ""},
        {"conesText",       "Kegels",
                            "Cones",
                            ""},
        {"cone1Text",       "Kegel 1",
                            "Cone 1",
                            ""},
        {"cone2Text",       "Kegel 2",
                            "Cone 2",
                            ""},
        {"cone3Text",       "Kegel 3",
                            "Cone 3",
                            ""},
        {"cone4Text",       "Kegel 4",
                            "Cone 4",
                            ""},
        {"cubeText",        "Kubus",
                            "Cube",
                            ""},
        {"piramidsText",    "Piramides",
                            "Piramids",
                            ""},
        {"threePiramidText", "Driezijdige piramide",
                             "Three sided piramid",
                             ""},
        {"fourPiramidText",  "Vierzijdige piramide",
                             "Four sided piramid",
                             ""},
        {"fivePiramidText",  "Vijfzijdige piramide",
                             "Five sided piramid",
                             ""},
        {"sixPiramidText",   "Zeszijdige piramide",
                             "Six sided piramid",
                             ""},
        {"sevenPiramidText", "Zevenzijdige piramide",
                             "Seven sided piramid",
                             ""},
        {"eightPiramidText", "Achtzijdige piramide",
                             "Eight sided piramid",
                             ""},
                             
        {"prismsText",       "Prisma's",
                             "Prisms",
                             ""},
        {"threePrismText",   "Driezijdig prisma",
                             "Three sided prism",
                             ""},
        {"fourPrismText",    "Vierzijdig prisma",
                             "Four sided prism",
                             ""},
        {"fivePrismText",    "Vijfzijdig prisma",
                             "Five sided prism",
                             ""},
        {"sixPrismText",     "Zeszijdig prisma",
                             "Six sided prism",
                             ""},
        {"dodecahedronText", "Twaalfvlak",
                             "Dodecahedron",
                             ""},
        {"icosahedronText", "Twintigvlak",
                            "Icosahedron",
                            ""},
        {"tetrahedronText", "Viervlak",
                            "Tetrahedron",
                            ""},

        {"myFigureText",    "Mijn figuur",
                            "My figure",
                            ""},
                            

        // options menu
        {"optionsText",     "Opties",
                            "Options",
                             ""},
        {"centralProjText", "Centrale projectie",
                            "Central projection",
                             ""},
        {"parallelProjText", "Parallelprojectie",
                             "Parallel projection",
                             ""},
        {"lettersText",     "Letters",
                            "Add letters to points",
                             ""},
        {"helpPointsText",  "Hulppunten",
                            "Help points",
                             ""},
        {"noHelpPointsText", "Geen hulppunten",
                             "No help points",
                             ""},
        {"divideSidesText",  "Verdeel ribben in",
                             "Divide edges into",
                             ""},
        {"twoPartsText",     "twee delen",
                             "two parts",
                             ""},
        {"threePartsText",   "drie delen",
                             "three parts",
                             ""},
        {"fourPartsText",    "vier delen",
                             "four parts",
                             ""},
        {"fivePartsText",    "vijf delen",
                             "five parts",
                             ""},
        {"sixPartsText",     "zes delen",
                             "six parts",
                             ""},
        {"sevenPartsText",   "zeven delen",
                             "seven parts",
                             ""},
        {"eightPartsText",   "acht delen",
                             "eight parts",
                             ""},
        {"ninePartsText",    "negen delen",
                             "nine parts",
                             ""},
        {"tenPartsText",     "tien delen",
                             "ten parts",
                             ""},
                             
        
                            
        // help messages
        {"rotateText",      "Klik en sleep met de muis om de figuur te draaien",
                            "Click and drag with the mouse to rotate the object",
                            ""},
        // right toolbar                    
        {"escapeText",      "Stop aktie",
                            "Abort action",
                            ""},
        
        
        {"wireFrameText",   "Draadfiguur",
                            "Wireframe object",
                            ""},
        {"solidText",       "Massieve figuur",
                            "Solid object",
                            ""},
        {"zoomInText",      "Zoom in",
                            "Zoom in",
                            ""},
        {"zoomOutText",     "Zoom uit",
                            "Zoom out",
                            ""},
        {"conDrawText",     "Bouwplaat",
                            "Fold-out",
                            ""},
        {"conDrawSelectText", "#Klik met de muis op het vlakje dat het centrum van de bouwplaat moet worden",
                              "#Click with the mouse on the side which will be the center of the fold-out",
                              "#"},
        {"wholeFigureText", "Figuur",
                            "Object",
                            ""},
        {"flatText",        "plat",
                            "flat",
                            ""},
        {"volumeText",      "Inhoud",
                            "Volume",
                            ""},
        {"largeFigureText", "grote figuur",
                            "large object",
                            ""},
        {"smallFigureText", "kleine figuur",
                            "small object",
                            ""},
                            
                            
/*
                            
        {"dimensionsText",  "Toon afmetingen van de figuur",
                            "Show dimensions of the object",
                            ""},
        {"noDimensionsText",  "Verberg afmetingen van de figuur",
                              "Hide dimensions of the object",
                              ""},
        {"axesText",        "Toon assenstelsel en coordinaten",
                            "Show axes and coordinates",
                            ""},
        {"noAxesText",      "Verberg assenstelsel en coordinaten",
                            "Hide axes and coordinates",
                            ""},
*/        
        
        // top toolbar                    
        {"drawLineText",    "Teken lijn",
                            "Draw line",
                            ""},
        
        {"drawLinesText",   "Teken lijnen",
                            "Draw lines",
                            ""},
// niet relevant meer                            
        {"drawLineEndText", "Stop met tekenen van lijnen in de figuur",
                            "Stop drawing lines in the object",
                            ""},
// niet relevant meer                                                        
        {"drawLineCancelText", "Stop met tekenen van de lijn",
                               "Stop drawing the line",
                               ""},
                            
        {"linePoint1Text",  "#Kies het eerste punt van de lijn door met de muis op een hoekpunt of ribbe te klikken",
                            "#Choose the first point of the line by clicking with the mouse on a vertex or on an edge",
                            "#"},
        {"linePoint2Text",  "#Kies het tweede punt van de lijn door met de muis op een hoekpunt of ribbe te klikken",
                            "#Choose the second point of the line by clicking with the mouse on a vertex or on an edge",
                            "#"},
        {"deleteLineText",  "Verwijder lijn",
                            "Delete line",
                            ""},
        {"selectDeleteLineText",  "#Klik met de muis op de lijn die verwijderd moet worden",
                                  "#Click with the mouse on the line that should be deleted",                        
                                  "#"},
        {"lengLinesText",  "Verleng lijnstukken",
                           "Extend segments",
                           ""},
        {"shortLinesText", "Verkort lijnstukken",
                           "Shorten segments",
                           ""},
                                  
                                  
                                  
        {"drawPlaneText",   "Teken vlak",
                            "Draw plane",
                            ""},
                            
        {"drawPlanesText",   "Teken vlakken",
                            "Draw planes",
                            ""},
// niet relevant meer                            
        {"drawPlaneEndText", "Stop met tekenen van vlakken in de figuur",
                            "Stop drawing planes in the object",
                            ""},
// niet relevant meer                                                        
        {"drawPlaneCancelText", "Stop met tekenen van het vlak",
                                "Stop drawing the plane",
                                ""},
                            
        {"planePoint1Text", "#Kies het eerste punt van het vlak door met de muis op een hoekpunt of ribbe te klikken",
                            "#Choose the first point of the plane by clicking with the mouse on a vertex or on an edge",
                            "#"},
        {"planePoint2Text", "#Kies het tweede punt van het vlak door met de muis op een hoekpunt of ribbe te klikken",
                            "#Choose the second point of the plane by clicking with the mouse on a vertex or on an edge",
                            "#"},
        {"planePoint3Text", "#Kies het derde punt van het vlak door met de muis op een hoekpunt of ribbe te klikken",
                            "#Choose the third point of the plane by clicking with the mouse on a vertex or on an edge",
                            "#"},

        {"parPlaneText",  "Teken evenwijdig vlak",
                          "Draw parallel plane",
                          ""},
        {"selectParPlaneText",  "#Klik met de muis op een lijn van het vlak dat evenwijdig gekopieerd moet worden",
                                "#Click with the mouse on a line of the plane which should be copied",
                                "#"},
        {"parPlanePointText",   "#Kies een punt van het nieuwe evenwijdige vlak door met de muis op een hoekpunt of ribbe te klikken",
                                "#Choose a point of the new parallel plane by clicking with the mouse on a vertex or on an edge",
                                "#"},
                            
                            
        {"deletePlaneText", "Verwijder vlak",
                            "Delete plane",
                            ""},
        {"selectDeletePlaneText", "#Klik met de muis op een lijn van het vlak dat verwijderd moet worden",
                                  "#Click with the mouse on a line of the plane that should be deleted",
                                  "#"},
        {"planesFilledText", "Vul vlakken",
                             "Fill planes",
                             ""},
        {"planesEmptyText",  "Maak vlakken doorzichtig",
                             "Make planes transparent",
                             ""},
        {"rotatePlaneText", "Draai vlak",
                            "Rotate plane",
                            ""},
        {"selectRotatePlaneText", "#Klik met de muis op een lijn van het vlak. Het vlak wordt dan om deze lijn gedraaid",
                                  "#Click with the mouse on a line of the plane. The plane will be rotated around this line",
                                  "#"},
        {"translatePlaneText", "Verschuif vlak",
                               "Translate plane",
                               ""},
        {"selectTranslatePlaneText", "#Klik met de muis op een lijn van het vlak dat verschoven moet worden",
                                     "#Click with the mouse on a line of the plane that should be translated",
                                     "#"},
        {"showCutText", "Toon doorsnede",
                        "Show intersection",
                        ""},
        {"selectShowCutText", "#Klik met de muis op een lijn van het vlak waarvan de doorsnede met de figuur getoond moet worden",
                              "#Click with the mouse on a line of the plane whose intersection with the object should be shown",
                              "#"},
        {"hideCutText", "Verberg doorsnede",
                        "Hide intersection",
                        ""},
        {"cutFigureText", "Splits figuur",
                          "Split object",
                          ""},
        {"glueFigureText", "Voeg figuren samen",
                           "Paste objects together",
                           ""},
                          
                          
        {"selectCutPlaneText", "#Klik met de muis op een lijn van het vlak waarlangs de figuur doorgesneden moet worden",
                               "#Click with the mouse on a line of the plane along which the object should be cut",
                               "#"},
        {"selectCutFigureText", "#Klik met de muis op het stuk van de figuur, dat de nieuwe figuur moet worden, of gebruik de Plak-knop",
                                "#Click with the mouse to select the part of the object which should become the new object, or use the Paste-button",
                                "#"},

        {"undoText", "Herstel",
                     "Undo",
                     ""},
        {"redoText", "Maak herstelling ongedaan",
                     "Redo",
                     ""},
                                
                                
                            
        {"decSep",          ",", ".", ""},                                                        
                            
        
    };
    // make the table static so it can be accessed from the static
    // method lookUp
    public static Hashtable table = new Hashtable();
    String language;
    int languageCode;
    // constructor, find the table column number for the required language
    // and initialize the hashtable with the keys column and the
    // language column
    Table(String lang)
    {   language = lang;
        initLanguageCode();
        for (int i = 0; i < contents.length; i++)
            table.put(contents[i][0], contents[i][languageCode]);
    }
    private void initLanguageCode()
    {   if (language == null)
            languageCode = 1;
        else if (language.equals("nl"))
            languageCode = 1;
        else if (language.equals("en"))
            languageCode = 2;
// temporary: no spanish avalable
//        else if (language.equals("es"))
//            languageCode = 3;
        else languageCode = 1;
    }
    public static String lookUp(String key)
    {   if (table.containsKey(key))
            return (String) table.get(key);
        else
            return "";
    }
} // class Table
