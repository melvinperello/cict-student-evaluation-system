# CICT Student Evaluation System


*Integrated with Monosync Framework*


**Contributors**


-Melvin Perello
-Joemar de la Cruz


### JDK 9+ Migration Notice


Converted from **Ant Build** to **Maven Build**


I'm trying to run this old project from **Oracle0 JDK 8** to  **Open JDK 9+** which supports Java Modules


JDK 12+ will not work because of JFoenix library.


Currently Trying with **Open JDK 11.0.2**


for JDK 11.0.2 only because of JFoenix Library
https://stackoverflow.com/questions/55889654/illegalaccessexception-for-jfxtextfield-with-java-sdk-12


Open JDK Archive https://jdk.java.net/archive/
Open JDK 11.0.2 (build 11.0.2+9) https://download.java.net/java/ga/jdk11/openjdk-11_windows-x64_bin.zip


Open JavaFX Download Page https://gluonhq.com/products/javafx/
Open JavaFX Windows SDK 11.0.2 http://gluonhq.com/download/javafx-11-0-2-sdk-windows/


**Run with JDK 9 - JDK 11**


'''java
java --module-path "C:\dev\bin\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media -jar cictses-jar-with-dependencies.jar
'''