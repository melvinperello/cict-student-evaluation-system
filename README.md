# CICT Student Evaluation System

The study was created with the purpose of developing a software entitled “CICT Student Evaluation System”. The proponents aim to improve the current system in a way that it adopts the new technologies available today. The enhanced system is composed of three parts: an evaluation system, a web portal, and an Android application.  These three parts combined make the transactions flow easier and smoother.
 
The developed evaluation system is going to help ease the management of evaluation per student. It also provides additional functionalities such as giving suggestions on the outstanding students, class monitoring, on-screen display of the list of subjects that can be taken within the maximum units per semester and will prompt messages if the student has reached the maximum load of units. 

Students are now able to fill up and update their student information on the web portal, which is be known as the CICT Local Web Portal. This way, the information is saved on the database and can be retrieved whenever needed. This paves a way for the start of paperless transaction for the college. Students are also able to view their grades on the web portal.

The Android Application, conveniently named CICT Linked – “Connection without lines”, is developed for the sole purpose of managing the student queues during the evaluation. This provides order and eliminates the possibility of other students cutting in line. 


### PHP7 (Web Runtime)


CICT-SES Extension projects was built using laravel.


**Laravel** is a web application framework with expressive, elegant syntax. We’ve already laid the foundation — freeing you to create without sweating the small things. https://laravel.com/


### WEB-01 CICT-SES Web Portal
https://github.com/melvinperello/cict-student-evaluation-system/tree/master/extensions/cictwebportal


**Preview (Web Portal)**


[![Image](https://raw.githubusercontent.com/melvinperello/cict-student-evaluation-system/master/preview-img/cict-webportal.PNG)](https://raw.githubusercontent.com/melvinperello/cict-student-evaluation-system/master/preview-img/cict-webportal.PNG)


### WEB-02 CICT-SES Mobile Linked PHP API
https://github.com/melvinperello/cict-student-evaluation-system/tree/master/extensions/linked-php-api


**Preview (Android App API)**

```php
<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

// chage to post
//Route::get('linked/login/{username}/{password}/{imei}/{is_skipped}', "Authenticator@login");
Route::post('linked/login', 'Authenticator@login');
Route::get('linked/is_used/{imei}', "Authenticator@is_used");
// chage to post
//Route::get('linked/logout/{account_id}/{type}', "Authenticator@logout");
Route::post('linked/logout', "Authenticator@logout");

Route::get('linked/search/{student_number}', "Marshall@search");
Route::post('linked/add_student', "Marshall@add_student");
Route::post('linked/create_student', "Marshall@create_new_student");

//
// cict_id param
Route::get('linked/check/{id}', "Marshall@check");
Route::post('linked/entrance', 'Marshall@entrance_pass');


// Student
// cict_id param
Route::get('linked/check_number/{cict_id}', 'Student@check_number');
Route::post('linked/cancel_reference', 'Student@cancel_reference');

// Mono Form
Route::post('linked/monoforms/profile', 'MonoForms@mono_profile');

//---------------------------------------------------------
// settings at config/filesystems.php
Route::get('linked/photo/{photo}', 'Media@get_photo');
//----------------------------------------------------------
#-----------------------------------------------------------
# New Routes since 11/13/2017
Route::post('linked/student/update', 'Marshall@update_student_info');
Route::get('linked/student/update/fetch/{cict_id}', 'Marshall@fetch_update_information');

```


### WEB-03 CICT-SES GSM Server API
https://github.com/melvinperello/cict-student-evaluation-system/tree/master/extensions/smsserver


### WEB 04 CICT-SES Monoforms (Static Site QR Generator)
https://github.com/melvinperello/cict-student-evaluation-system/tree/master/extensions/cictmonoforms


[![Image](https://raw.githubusercontent.com/melvinperello/cict-student-evaluation-system/master/preview-img/cict-monoforms.PNG)](https://raw.githubusercontent.com/melvinperello/cict-student-evaluation-system/master/preview-img/cict-monoforms.PNG)


### Docker
The following extension projects has been integrated with docker.

- WEB-01 CICT-SES Web Portal
- WEB-02 CICT-SES Mobile Linked PHP API
- WEB 04 CICT-SES Monoforms (Static Site QR Generator)

```batch
git clone https://github.com/melvinperello/cict-student-evaluation-system.git
cd cict-student-evaluation-system\docker

# build artifacts
build.bat

# build image
docker build -t melvinperello/cictses:1.0 .

# run image
# you need to add your local machine ip as host with name windows.local in order for the container to connect to your local database running on port 3306
docker run --add-host=windows.local:<local-ip> -p 8000:8080 -p 8001:8081 -p 8002:8082 melvinperello/cictses:1.0
```


### Java 1.8 (Stable Runtime)

- This application was built with Oracle **Java 1.8 update 40** (2017)

- Tested on **Java 1.8 update 232** and was running fine. (2019)


### JAVA-01 Monosync Framework Integration
https://github.com/melvinperello/cict-student-evaluation-system/tree/master/extensions/mono-fw


I created this for my final project at Bulacan State University College of Information and Communications Technology This project is the compilation of all the tools that I have used in the said project.

This framework is necessary to allow reusability of code through out the development scope across multiple developers.

**Hibernate ORM Wrapper Classes**

- Hibernate Wrapper and Code Generation

- Creates a simplified abstractions and configuration for Hibernate

- Simple to use ORM Methods

- [Visit Hibernate Website For More Information About Hibernate ORM](http://hibernate.org/orm/)

**Java FX Initialization Wrapper**

- Easy to use FXML loader

- Highly Maintanable code with FXML


### JAVA-02 CICT-SES Application (Main/Desktop)


*CICT-SES Desktop Application* contains the following modules:

- Evaluation

- Adding / Changing of Subjects

- Faculty Hub

- Account Settings

- Reports

- Academic Term Configuration

- Academic Programs

- Student Management

- Section Management

- Faculty Management

- Access Controls

- Linked System (Control Panel)

This is the main application in the CICT Student Evaluation System.


**Preview (Home)**


[![Image](https://raw.githubusercontent.com/melvinperello/cict-student-evaluation-system/master/preview-img/cict-ses-home.PNG)](https://raw.githubusercontent.com/melvinperello/cict-student-evaluation-system/master/preview-img/cict-ses-home.PNG)


**Updates**

- Converted from Ant to Maven dependency management.

**Build Instructions**

```java
git clone https://github.com/melvinperello/cict-student-evaluation-system.git
cd cict-student-evaluation-system
mvn -U clean install
cd target
java -jar cictses-jar-with-dependencies.jar
```

### JAVA-APP-03 CICT-SES Linked System (Auto-Caller)
https://github.com/melvinperello/cict-student-evaluation-system/tree/master/extensions/linked-auto-caller


### JAVA-04 CICT-SES Linked System (TV Display)
https://github.com/melvinperello/cict-student-evaluation-system/tree/master/extensions/linked-tv-display


### JAVA-05 CICT-SES Linked System (Android)
https://github.com/melvinperello/cict-student-evaluation-system/tree/master/extensions/Linked


### JAVA-06 CICT-SES GSM Server
https://github.com/melvinperello/cict-student-evaluation-system/tree/master/extensions/messaging-server


### JAVA-07 CICT-SES Profile Uploader (ETL Extract Transform Load from Excel)
https://github.com/melvinperello/cict-student-evaluation-system/tree/master/extensions/profile-uploader


## Java 1.9+ Migration Initiative (Cancelled)

Runtime Upgrade from **Java 1.8** to **Java 1.9 +**

| Runtime   | Issue           			 |
| --------- |-------------------------------------------------------------|
| Java 1.13 | Application started, almost all features are not functional |
| Java 1.12 | JFoenix / ControlsFX Incompatibility.    			    |
| Java 1.11 | ControlsFX Incompatibility.    					    |
| Java 1.10 | ControlsFX Incompatibility.    					    |
| Java 1.09 | ControlsFX Incompatibility.    					    |

**JFoenix Java 1.12 Incompatibility**

> JFoenix will only work with JDK 11.0.2. See Link -> https://stackoverflow.com/questions/55889654/illegalaccessexception-for-jfxtextfield-with-java-sdk-12


**JDK 9+ Links**

- Open JDK Archive https://jdk.java.net/archive/

- Open JDK 11.0.2 (build 11.0.2+9) https://download.java.net/java/ga/jdk11/openjdk-11_windows-x64_bin.zip

- Open JavaFX Download Page https://gluonhq.com/products/javafx/

- Open JavaFX Windows SDK 11.0.2 http://gluonhq.com/download/javafx-11-0-2-sdk-windows/

**How to run with Modulearized Java**

```java
java --module-path "C:\dev\bin\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media -jar cictses-jar-with-dependencies.jar
```

Cheers,
Melvin