package fi.beans.iconan.text;

import java.util.ListResourceBundle;

public class Text_nl extends ListResourceBundle {
	
	 public Object [] [] getContents() { return contents; }
     static final Object [] [] contents =
     {
         	{ Text.TITEL , "titel" },
			{ Text.ANNULEER , "annuleer" },
			{ Text.NIEUW , "toevoegen" },
			{ Text.REMOVE , "verwijderen" },
			{ Text.OK , "ok" },
			{ Text.FILE , "bestand" },
			{ Text.URL , "URL" },
			{ Text.EDIT, "edit" },
			{ Text.WIJZIG, "wijzig" },
			{ Text.EDIT_URL, "URL van plaatje" }
     };
}
