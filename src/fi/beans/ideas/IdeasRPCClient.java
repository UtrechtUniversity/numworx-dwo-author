// Generated code, do not edit
package fi.beans.ideas;

import java.util.Vector;
import java.net.URL;
import fi.beans.xmlrpc.Client;
import org.apache.xmlrpc.applet.XmlRpcException;
import java.io.IOException;

public class IdeasRPCClient extends Client implements fi.beans.ideas.IdeasRPCIF {

	public IdeasRPCClient(URL u) {
		super(u);
	}

    public java.util.Vector getDerivation(java.util.Hashtable a, java.lang.String b) throws IOException, XmlRpcException
    {
        Vector vv = new Vector(2);
        vv.addElement(a);
        vv.addElement(b);
        Object object = invoke("getDerivation", vv);
        return (java.util.Vector)object;
    }

    public java.util.Vector getAllFirsts(java.util.Hashtable a, java.lang.String b) throws IOException, XmlRpcException
    {
        Vector vv = new Vector(2);
        vv.addElement(a);
        vv.addElement(b);
        Object object = invoke("getAllFirsts", vv);
        return (java.util.Vector)object;
    }

    public java.util.Hashtable getOneFirst(java.util.Hashtable a, java.lang.String b) throws IOException, XmlRpcException
    {
        Vector vv = new Vector(2);
        vv.addElement(a);
        vv.addElement(b);
        Object object = invoke("getOneFirst", vv);
        return (java.util.Hashtable)object;
    }

    public java.util.Vector findBuggyRules(java.util.Hashtable a, java.util.Hashtable b, java.lang.String c) throws IOException, XmlRpcException
    {
        Vector vv = new Vector(3);
        vv.addElement(a);
        vv.addElement(b);
        vv.addElement(c);
        Object object = invoke("findBuggyRules", vv);
        return (java.util.Vector)object;
    }

    public java.util.Hashtable diagnose(java.util.Hashtable a, java.util.Hashtable b, java.lang.String c) throws IOException, XmlRpcException
    {
        Vector vv = new Vector(3);
        vv.addElement(a);
        vv.addElement(b);
        vv.addElement(c);
        Object object = invoke("diagnose", vv);
        return (java.util.Hashtable)object;
    }

    public java.util.Vector getExerciseList() throws IOException, XmlRpcException
    {
        Vector vv = new Vector(0);
        Object object = invoke("getExerciseList", vv);
        return (java.util.Vector)object;
    }

    public java.util.Vector getRuleList(java.lang.String a) throws IOException, XmlRpcException
    {
        Vector vv = new Vector(1);
        vv.addElement(a);
        Object object = invoke("getRuleList", vv);
        return (java.util.Vector)object;
    }

    public java.util.Vector getRuleInfo(java.lang.String a) throws IOException, XmlRpcException
    {
        Vector vv = new Vector(1);
        vv.addElement(a);
        Object object = invoke("getRuleInfo", vv);
        return (java.util.Vector)object;
    }

    public java.util.Vector getExamples(java.lang.String a) throws IOException, XmlRpcException
    {
        Vector vv = new Vector(1);
        vv.addElement(a);
        Object object = invoke("getExamples", vv);
        return (java.util.Vector)object;
    }

    public java.util.Hashtable interpret(java.lang.String a, java.util.Vector b) throws IOException, XmlRpcException
    {
        Vector vv = new Vector(2);
        vv.addElement(a);
        vv.addElement(b);
        Object object = invoke("interpret", vv);
        return (java.util.Hashtable)object;
    }

}
