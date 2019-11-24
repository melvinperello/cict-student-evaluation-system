# CICT Student Evaluation System


*Integrated with Monosync Framework*


**Contributors**

- Melvin Perello

- Joemar de la Cruz

Converted from **Ant Build** to **Maven Build**

### Java 1.9+ Migration Initiative (Cancelled)

Runtime Upgrade from **Java 1.8** to **Java 1.9 +**


| Runtime   | Issue           			 |
| --------- |-------------------------------------------------------------|
| Java 1.13 | Application started, almost all features are not functional |
| Java 1.12 | JFoenix / ControlsFX Incompatibility.    			    |
| Java 1.11 | ControlsFX Incompatibility.    					    |
| Java 1.10 | ControlsFX Incompatibility.    					    |
| Java 1.09 | ControlsFX Incompatibility.    					    |


**JFoenix Java 1.12 Incompatibility**


JFoenix will only work with JDK 11.0.2. See Link -> https://stackoverflow.com/questions/55889654/illegalaccessexception-for-jfxtextfield-with-java-sdk-12


Open JDK Archive https://jdk.java.net/archive/

Open JDK 11.0.2 (build 11.0.2+9) https://download.java.net/java/ga/jdk11/openjdk-11_windows-x64_bin.zip

Open JavaFX Download Page https://gluonhq.com/products/javafx/

Open JavaFX Windows SDK 11.0.2 http://gluonhq.com/download/javafx-11-0-2-sdk-windows/



**How to run with Modulearized Java**


```java
java --module-path "C:\dev\bin\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media -jar cictses-jar-with-dependencies.jar
```