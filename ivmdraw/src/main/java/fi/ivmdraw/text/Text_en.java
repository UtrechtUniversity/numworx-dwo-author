package fi.ivmdraw.text;

public class Text_en extends java.util.ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = { 
		{ "vaas", "Jar" }, 
		{ "geenVaas" , "No Jar" },
		{ "opdrachtLabel" , "Task" },
		{ "vaasOpdrachtLabel" , "Volume-height graph jar:" },
		{ "feedbackVisibleCBLabel", "Feedback visible" },
		{ "historyVisibleCBLabel", "History visible"},
		{ "scoreLabel" , "Maximum score" },
		{ 	"scoreLabel0" , "Correct after 1 or 2 attempts" },
    	{ 	"scoreLabel1" , "Correct after more then 2 attempts" },
    	{ 	"scoreLabel2" , "Wrong, but more then 2 attempts" },
		{ "CBA_action.correct", "Action: correct answer" }, 
		{ "CBA_action.false", "Action: incorrect answer" },
		{ "CBA_action.false_2", "Action: 2x incorrect answer" }, 
		{ "CBA_text.feedback", "feedback" } 
	};

}
