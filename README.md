# Channelize-Android
 This tutorial will walk you through building a simple Android app with the Channelize integration.
 
 Firstly, you will need to register on Channelize: https://channelize.io/pricing

 After the successful payment at https://channelize.io/pricing you'll get the "**channelize.aar**" file that will contains the all messaging and other feature of channelize.io.

 1. After getting the **channelize.aar** file download it on your system.
 
 2. Once get the **channelize.aar** file, add this file into project. For that please follow the below steps:
 In android studio Go to File-> Project Structure-> Click + Icon at top left-> Import .jar/.aar package-> Select the
 **channelize.aar** file.
 
 3. Add below code in app level build.gradle file:
 **Add below code at top:**

    apply plugin: 'com.jakewharton.butterknife'

**Add all below dependencies:**

    implementation "com.android.support:appcompat-v7:$supportLibraryVersion"
    implementation "com.android.support:design:$supportLibraryVersion"
    implementation "com.android.support:support-v4:$supportLibraryVersion"
    implementation "com.android.support:cardview-v7:$supportLibraryVersion"
    implementation ('com.google.android.gms:play-services-places:15.0.1') {
        exclude group: 'com.android.support'
    }
    implementation 'com.squareup.okhttp3:okhttp:3.10.0'
    implementation 'com.github.bumptech.glide:glide:4.7.1'
    implementation ('com.google.firebase:firebase-messaging:17.1.0') {
        exclude group: 'com.android.support'
    }
    implementation ('com.google.firebase:firebase-core:16.0.1') {
        exclude group: 'com.android.support'
    }
    implementation 'com.makeramen:roundedimageview:2.3.0'
    implementation 'com.google.code.gson:gson:2.3'
    implementation 'com.jakewharton:disklrucache:2.0.2'
    implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0'
    implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
    implementation('com.giphy.sdk:core:1.0.2@aar') {
        transitive=true
    }
    implementation 'com.facebook.shimmer:shimmer:0.3.0@aar'
    implementation "com.jakewharton:butterknife:9.0.0-SNAPSHOT"
    annotationProcessor"com.jakewharton:butterknife-compiler:9.0.0-SNAPSHOT"
    
   4. Add below code at project level build.gradle file:
    
    buildscript {
    repositories {
        maven {
            url 'https://maven.google.com/'
            name 'Google'
        }

        // TODO remove after butterknife 9 graduates to stable
        maven { name 'Sonatype SNAPSHOTs'; url 'https://oss.sonatype.org/content/repositories/snapshots/' }

        maven {
            url "https://jcenter.bintray.com"
        }

    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.4'
        classpath 'com.google.gms:google-services:4.0.0'
        classpath 'com.jakewharton:butterknife-gradle-plugin:9.0.0-SNAPSHOT'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
    }
    allprojects {
      repositories {
    
        maven {
            url 'https://maven.google.com/'
            name 'Google'
        }
        
        jcenter()

        flatDir {
            dirs 'libs'
        }
        maven { url 'https://oss.sonatype.org/content/repositories/snapshots' }
        maven {
            url "https://repo.eclipse.org/content/repositories/paho-snapshots/"
        }
        maven {
            url  "https://giphy.bintray.com/giphy-sdk"
        }

        maven { url "https://jitpack.io" }

    }
    }
    
    
   5. Change the below variables in **Config.java** file:
    
    
     //This will be the default API calling url.
    static final String API_DEFAULT_URL = "CHANNELIZE_API_DEFAULT_URL";

    //This will be the mqtt client server url.
    static final String MQTT_SERVER_URL = "CHANNELIZE_MQTT_SERVER_URL";

    // This will be the api key.
    static final String API_KEY = "CHANNELIZE_API_KEY";

    // this will be sender id of the project
    public static final String FIREBASE_SENDER_ID = "CHANNELIZE_FIREBASE_SENDER_ID";

    // This will be the mobilesdk_app_id in google-services.json file
    public static final String FIREBASE_APPLICATION_ID = "CHANNELIZE_FIREBASE_APPLICATION_ID";

    // This will be the giphy api key.
    static final String GIPHY_API_KEY = "CHANNELIZE_GIPHY_API_KEY";

    // This will be the google places api key.
    static final String GOOGLE_PLACES_API_KEY = "CHANNELIZE_GOOGLE_PLACES_API_KEY";

    // This will be the aws api calling url.
    static final String AWS_API_URL = "CHANNELIZE_AWS_API_URL";
    
   6. Channelize requires a few permissions. Head over to **AndroidManifest.xml** and add the following: 
    
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

    <uses-feature
        android:name="android.hardware.camera"
        android:required="false" />
    <uses-feature
        android:name="android.hardware.camera.autofocus"
        android:required="false" />
    
    7. .
