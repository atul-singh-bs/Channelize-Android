# Channelize-Android
 This tutorial will walk you through building a simple Android app with the Channelize integration.
 
 first, you will need to register on Channelize: https://channelize.io/pricing

 after the successful payment at https://channelize.io/pricing you'll get the "channelize.aar" file that will contains the all
 messaging other feature of channelize.io.

 1. After getting the channelize.aar file download it on system.
 2. Once get the channelize.aar file, add this file into project. For that please follow the below steps:
 In android studio Go to File-> Project Structure-> Click + Icon at top left-> Import .jar/.aar package-> Select the channelize.aar
 file.
 3. Add below code in app level build.gradle file:
Add below code at top:

apply plugin: 'com.jakewharton.butterknife'

Add all below dependencies:

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
        maven {
            url 'https://maven.fabric.io/public'
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
        classpath 'io.fabric.tools:gradle:1.25.4'

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

    
    5. .
    6. .
    7. .
