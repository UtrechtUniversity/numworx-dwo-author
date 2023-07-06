package fi.beans.iconan.text;

import java.util.ListResourceBundle;

public class Text_en extends ListResourceBundle {
	
	 public Object [] [] getContents() { return contents; }
     static final Object [] [] contents =
     {
         	{ Text.TITEL , "Title" },
			{ Text.ANNULEER , "Cancel" },
			{ Text.NIEUW , "Add" },
			{ Text.REMOVE , "Remove" },
			{ Text.OK , "Ok" },
			{ Text.CLOSE , "Close" },
			{ Text.FILE , "file" },
			{ Text.URL , "URL" },
			{ Text.EDIT , "Edit"},
			{ Text.WIJZIG, "Change"}, 
			{ Text.EDIT_URL, "URL of picture" },
     };
}
