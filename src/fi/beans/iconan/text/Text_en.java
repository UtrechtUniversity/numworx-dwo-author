package fi.beans.iconan.text;

import java.util.ListResourceBundle;

public class Text_en extends ListResourceBundle {
	
	 public Object [] [] getContents() { return contents; }
     static final Object [] [] contents =
     {
         	{ Text.TITEL , "title" },
			{ Text.ANNULEER , "cancel" },
			{ Text.NIEUW , "add" },
			{ Text.REMOVE , "remove" },
			{ Text.OK , "ok" },
			{ Text.FILE , "file" },
			{ Text.URL , "URL" },
			{ Text.EDIT , "edit"},
			{ Text.WIJZIG, "change"}, 
			{ Text.EDIT_URL, "URL of picture" },
     };
}
