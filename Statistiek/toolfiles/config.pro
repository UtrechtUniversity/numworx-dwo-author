#-injars ..\output\classes
#-outjars ..\output\jar\statistiek.jar

#-libraryjars 'C:\Program Files\Java\jdk1.7.0_51\jre\lib\rt.jar'
#-libraryjars ..\lib\mayscript.jar
#-libraryjars ..\lib\cbookif-1.0.2.jar
#-libraryjars ..\lib\xchart-3.5.0.jar
#-libraryjars ..\lib\xchart-demo-3.5.0.jar

-target 1.6

-keeppackagenames

-forceprocessing

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
