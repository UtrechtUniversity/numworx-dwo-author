package fi.beans.ideas;

import java.util.Hashtable;
import java.util.Vector;
/**
 * @xmlrpc.generate
 */
public interface IdeasRPCIF {
	public Vector getDerivation(Hashtable expr, String strategie) throws Exception;
	public Vector getAllFirsts(Hashtable expr, String strategie) throws Exception;
	public Hashtable getOneFirst(Hashtable expr, String strategie) throws Exception;
	public Vector findBuggyRules(Hashtable expr, Hashtable input, String strategie) throws Exception;
	public Hashtable diagnose(Hashtable expr, Hashtable input,String strategie) throws Exception;
	public Vector getExerciseList() throws Exception;
	public Vector getRuleList(String strategie) throws Exception;
	public Vector getRuleInfo(String strategie) throws Exception;
	public Vector getExamples(String strategie) throws Exception;
	public Hashtable interpret(String how, Vector args) throws Exception;
}