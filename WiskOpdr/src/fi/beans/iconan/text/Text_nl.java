package fi.beans.iconan.text;

import java.util.ListResourceBundle;

public class Text_nl extends ListResourceBundle {
	
	 public Object [] [] getContents() { return contents; }
     static final Object [] [] contents =
     {
         	{ Text.TITEL , "Titel" },
			{ Text.ANNULEER , "Annuleer" },
			{ Text.NIEUW , "Toevoegen" },
			{ Text.REMOVE , "Verwijderen" },
			{ Text.OK , "Ok" },
			{ Text.CLOSE , "Sluiten" },
			{ Text.FILE , "Bestand" },
			{ Text.URL , "URL" },
			{ Text.EDIT, "Bewerken" },
			{ Text.WIJZIG, "Wijzig" },
			{ Text.EDIT_URL, "URL van plaatje" }
     };
}
