-injars ../target/dependency/graphtool.jar
-outjars ../target/graphtool.jar
-injars ../target/dependency/balansfruitapplet.jar
-outjars ../target/balansfruitapplet.jar
-injars ../target/dependency/geodefiner.jar
-outjars ../target/geodefiner.jar
-injars ../target/dependency/statistiek-jar-with-dependencies.jar
-outjars ../target/statistiek.jar
-injars ../target/dependency/mathscratch.jar
-outjars ../target/mathscratch.jar
-injars ../target/dependency/geogebra3widget.jar
-outjars ../target/geogebra3widget.jar
-injars ../target/dependency/geogebra4widget.jar
-outjars ../target/geogebra4widget.jar


-target 1.8
-printmapping target/mapping.txt
-optimizations !class/merging/*
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
-keep class fi.wiskopdr.ObjectiveChoiceButton{
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

-keep class nl.numworx.geogebra3.** {
	<fields>;
	<methods>;
}
-keep class nl.numworx.geogebra4.** {
	<fields>;
	<methods>;
}

-keep class nl.numworx.geodefiner.** {
    <fields>;
    <methods>;
}
-keep class fi.euclides.** {
    <fields>;
    <methods>;
}

-keep class fi.balansfruit.BalansFruitApplet{
    <fields>;
    <methods>;
}

#keep all, tuning later.
-keep class fi.mathscratch.** {
	<fields>;
	<methods>;
}

# no allow shrinking
-keep class org.cbook.cbookif.rm.* {
	<methods>;
	<fields>;
}


-keep,allowshrinking class org.** {
    <fields>;
    <methods>;
}

-keep,allowshrinking class uk.** {
    <fields>;
    <methods>;
}

# extra's voor statistiek
-keeppackagenames fi.statistiek.**,org.knowm.**,de.**,org.apache.**

-keep class fi.statistiek.text.* {
    <fields>;
    <methods>;
}

-keep class fi.beans.*.* {
    <fields>;
    <methods>;
}

-keep class fi.statistiek.Statistiek {
    <methods>;
}

-keep class fi.statistiek.types.* {
	<methods>;
	<fields>;
}

-keep class javax.inject.Provider {
	<methods>;
}
-keep class dagger.MembersInjector {
	<methods>;
}
-keep class dagger.Lazy {
	<methods>;
}


# Keep - Applications. Keep all application classes, along with their 'main'
# methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Keep - Applets. Keep all extensions of java.applet.Applet.
-keep public class * extends java.applet.Applet

# voor export-package/import-package match
-keeppackagenames fi.beans.wiskopdrbeans, fi.wiskopdr, fi.wiskopdr.domainmodel, fi.wiskopdr.expressies
-keeppackagenames fi.wiskopdr.formuleobjects, fi.wiskopdr.opdrnav, fi.wiskopdr.tekstobjects

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
