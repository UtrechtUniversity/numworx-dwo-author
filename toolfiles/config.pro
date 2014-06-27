-injars ..\output\classes
-outjars ..\output\jar\wiskopdr.jar

-libraryjars <java.home>\lib\rt.jar
-libraryjars ..\lib\mayscript.jar
-libraryjars ..\lib\junit.jar
-libraryjars ..\output\jar\geogebra3.jar
-libraryjars ..\output\jar\geogebra3_main.jar
-libraryjars ..\output\jar\geogebra.jar
-libraryjars ..\output\jar\geogebra_main.jar
-libraryjars ..\output\jar\geogebra_cas.jar
-libraryjars ..\lib\classes


-target 1.7

-keeppackagenames

-forceprocessing


-keep class fi.wiskopdr.formuleobjects.* {
    <fields>;
    <methods>;
}
-keep class fi.wiskopdr.expressies.* {
    <fields>;
    <methods>;
}
-keep class fi.wiskopdr.tekstobjects.TekstArea {
    <methods>;
}
-keep class fi.wiskopdr.DialogFacade {
    <methods>;
}

-keep class fi.wiskopdr.RealPoint {
 	<fields>;
    <methods>;
}
-keep class fi.wiskopdr.Grafiek* {
 	<fields>;
    <methods>;
}

-keep class fi.wiskopdr.text.* {
    <fields>;
    <methods>;
}

-keep class fi.beans.*.* {
    <fields>;
    <methods>;
}

-keep class org.*.* {
     <methods>;
     <fields>;
}

-keep class org.*.*.* {
    <fields>;
    <methods>;
}

-keep class org.*.*.*.* {
    <fields>;
    <methods>;
}

-keep class uk.*.*.*.* {
    <fields>;
    <methods>;
}

-keep class fi.wiskopdr.WiskOpdr {
   <fields>;
    <methods>;
}

-keep class fi.wiskopdr.WiskOpdrPanel {
    <methods>;
}

-keep class fi.wiskopdr.WiskOpdrEditPanel {
    <methods>;
}

-keep class fi.wiskopdr.tekstobjects.LinkIF {
    <methods>;
}

# zet in commentaar voor produktie!
-keep class fi.wiskopdr.* {
    <methods>;
}



# Keep - Applications. Keep all application classes, along with their 'main'
# methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Also keep - Database drivers. Keep all implementations of java.sql.Driver.
-keep class * extends java.sql.Driver

# Also keep - Swing UI L&F. Keep all extensions of javax.swing.plaf.ComponentUI,
# along with the special 'createUI' method.
-keep class * extends javax.swing.plaf.ComponentUI {
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent);
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
