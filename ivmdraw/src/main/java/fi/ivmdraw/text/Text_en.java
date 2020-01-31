package fi.ivmdraw.text;

public class Text_en extends java.util.ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = { 
		{ "vaas", "Volume-height graph vase" }, 
		{ "geenVaas" , "Open task" },
		{ "opdrachtLabel" , "Task" },
		{ "vaasOpdrachtLabel" , "Volume-height graph vase:" },
		{ "jarFeedbackVisibleCBLabel", "Show vase of drawn graph"},
		{ "feedbackVisibleCBLabel", "Feedback visible" },
	 	{	"goedFoutVisibleCBLabel", "correct/wrong visible"},
		{ "historyVisibleCBLabel", "History visible"},
		{	"checkCBLabel", "Check"},
		{ "scoreLabel" , "Score" },
		{ 	"scoreLabel0" , "Correct after 1 or 2 attempts" },
    		{ 	"scoreLabel1" , "Correct after more then 2 attempts" },
    		{ 	"scoreLabel2" , "Wrong, but more then 2 attempts" },
    		{ 	"feedbackLabel" , "Feedback" },
    		{	"defaultFeedbackCase1", "With your graph, the height of the water in the vase increases with equal steps."},
   		{	"defaultFeedbackCase2a", "With your graph, the rise of the water in the vase increases."},
     	{	"defaultFeedbackCase2b", "With your graph, the rise of the water in the vase decreases."},
     	{	"defaultFeedbackCase3a", "With your graph, the rise of water in the vase first increases and then slowly decreases."},
     	{	"defaultFeedbackCase3b", "With your graph, the rise of the water in the vase first decreases and then slowly increases."},
     	{	"defaultFeedbackCase4a", "With your graph, the rise of the water in the vase first increases, then decreases and then again increases."},
     	{	"defaultFeedbackCase4b", "With your graph, the rise of the water in the vase first decreases, then increases and then again decreases. "},
     	{	"defaultFeedbackCase5", "With your graph, the height of the water decreases while there's getting more water in the vase. That's not correct!"},
     	{	"defaultFeedbackCase6", "With your graph you indicate that the same volume represents several heights. That's not correct!"},
     	{	"defaultFeedbackCase7", "Your graph is not recognized, but it is not correct!"},
     	
		{ "CBA_action.correct", "Action: correct answer" }, 
		{ "CBA_action.false", "Action: incorrect answer" },
		{ "CBA_action.false_2", "Action: 2x incorrect answer" }, 
		{ "CBA_text.feedback", "feedback" },
		{ 	"CBA_graph", "Graph"},
	};

}
