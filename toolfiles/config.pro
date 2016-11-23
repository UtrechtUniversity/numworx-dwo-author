-injars ../output/classes
-outjars ../output/jar/wiskopdr.jar
-injars ../../GraphTool/output/classes
-outjars ../output/jar/graphtool.jar
-injars ../../BalansFruitApplet/output/classes
-outjars ../output/jar/balansfruitapplet.jar
#-injars  ../../geodefiner/target/geodefiner.jar
-injars ../lib/geodefiner.jar
-outjars ../output/jar/geodefiner.jar


-libraryjars <java.home>/lib/rt.jar
-libraryjars <java.home>/lib/ext/jfxrt.jar
-libraryjars <java.home>/lib/jce.jar
#-libraryjars ../output/jar/calendar.jar
#-libraryjars ../lib/jcommon-1.0.17.jar
-libraryjars ../lib/junit.jar
-libraryjars ../lib/mayscript.jar
-libraryjars ../lib/AppleJavaExtensions-1.4.jar
-libraryjars ../lib/classes
-libraryjars ../output/jar/geogebra.jar
-libraryjars ../output/jar/geogebra3.jar
-libraryjars ../output/jar/geogebra3_cas.jar
-libraryjars ../output/jar/geogebra3_export.jar
-libraryjars ../output/jar/geogebra3_gui.jar
-libraryjars ../output/jar/geogebra3_main.jar
-libraryjars ../output/jar/geogebra3_properties.jar
-libraryjars ../output/jar/geogebra_algos.jar
-libraryjars ../output/jar/geogebra_cas.jar
-libraryjars ../output/jar/geogebra_export.jar
-libraryjars ../output/jar/geogebra_gui.jar
-libraryjars ../output/jar/geogebra_javascript.jar
-libraryjars ../output/jar/geogebra_main.jar
-libraryjars ../output/jar/geogebra_properties.jar
-libraryjars ../output/jar/jlatexmath.jar
-libraryjars ../output/jar/jlm_cyrillic.jar
-libraryjars ../output/jar/jlm_greek.jar
-libraryjars ../output/jar/sardine.jar
#-libraryjars ..\output\jar\vecmath.jar

-target 1.8
-printmapping mapping.txt
-forceprocessing
-dontwarn

-keep class fi.wiskopdr.text.* {
    <fields>;
    <methods>;
}

-keep class fi.balansfruit.text.* {
    <fields>;
    <methods>;
}

-keep class fi.graphtool.text.* {
    <fields>;
    <methods>;
}


-keep class fi.graphtool.text.* {
    <fields>;
    <methods>;
}

-keep class fi.wiskopdr.tekstobjects.LinkIF{
    <fields>;
    <methods>;
}

-keep class fi.wiskopdr.tekstobjects.TekstArea{
    <fields>;
    <methods>;
}
-keep class fi.wiskopdr.WiskOpdrEditPanel {
    <methods>;
}
-keep class fi.wiskopdr.WiskOpdrPanel {
    <methods>;
}

-keep class fi.wiskopdr.RealPoint{
    <fields>;
    <methods>;
}

-keep class fi.beans.*.* {
    <fields>;
    <methods>;
}

-keep class fi.wiskopdr.WiskOpdr{
    <fields>;
    <methods>;
}

-keep interface fi.wiskopdr.tekstobjects.LinkIF {
	<methods>;
}

-keep class fi.graphtool.GraphTool{
    <fields>;
    <methods>;
}

-keep class nl.numworx.geodefiner.GeoDefiner {
    <fields>;
    <methods>;
}

-keep class fi.balansfruit.BalansFruitApplet{
    <fields>;
    <methods>;
}

-keep,allowshrinking class org.** {
    <fields>;
    <methods>;
}

-keep,allowshrinking class uk.** {
    <fields>;
    <methods>;
}

# Keep - Applications. Keep all application classes, along with their 'main'
# methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Keep - Applets. Keep all extensions of java.applet.Applet.
-keep public class * extends java.applet.Applet



# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Also keep - Serialization code. Keep all fields and methods that are used for
# serialization.
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Also keep - Database drivers. Keep all implementations of java.sql.Driver.
-keep class * extends java.sql.Driver

# Also keep - Swing UI L&F. Keep all extensions of javax.swing.plaf.ComponentUI,
# along with the special 'createUI' method.
-keep class * extends javax.swing.plaf.ComponentUI {
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent);
}

# Also keep - RMI interfaces. Keep all interfaces that extend the
# java.rmi.Remote interface, and their methods.
-keep interface  * extends java.rmi.Remote {
    <methods>;
}

# Also keep - RMI implementations. Keep all implementations of java.rmi.Remote,
# including any explicit or implicit implementations of Activatable, with their
# two-argument constructors.
-keep class * extends java.rmi.Remote {
    <init>(java.rmi.activation.ActivationID,java.rmi.MarshalledObject);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

# Remove - System method calls. Remove all invocations of System
# methods without side effects whose return values are not used.
-assumenosideeffects public class java.lang.System {
    public static long currentTimeMillis();
    static java.lang.Class getCallerClass();
    public static int identityHashCode(java.lang.Object);
    public static java.lang.SecurityManager getSecurityManager();
    public static java.util.Properties getProperties();
    public static java.lang.String getProperty(java.lang.String);
    public static java.lang.String getenv(java.lang.String);
    public static java.lang.String mapLibraryName(java.lang.String);
    public static java.lang.String getProperty(java.lang.String,java.lang.String);
}

# Remove - StringBuffer method calls. Remove all invocations of StringBuffer
# methods without side effects whose return values are not used.
-assumenosideeffects public class java.lang.StringBuffer {
    public java.lang.String toString();
    public char charAt(int);
    public int capacity();
    public int codePointAt(int);
    public int codePointBefore(int);
    public int indexOf(java.lang.String,int);
    public int lastIndexOf(java.lang.String);
    public int lastIndexOf(java.lang.String,int);
    public int length();
    public java.lang.String substring(int);
    public java.lang.String substring(int,int);
}

# Remove - StringBuilder method calls. Remove all invocations of StringBuilder
# methods without side effects whose return values are not used.
-assumenosideeffects public class java.lang.StringBuilder {
    public java.lang.String toString();
    public char charAt(int);
    public int capacity();
    public int codePointAt(int);
    public int codePointBefore(int);
    public int indexOf(java.lang.String,int);
    public int lastIndexOf(java.lang.String);
    public int lastIndexOf(java.lang.String,int);
    public int length();
    public java.lang.String substring(int);
    public java.lang.String substring(int,int);
}
