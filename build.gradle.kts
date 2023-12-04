// Top-level build file where you can add configuratio
// n options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.android.tools.build:gradle:8.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath ("com.github.dcendents:android-maven-plugin:1.2")
        classpath ("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.2")
    }
}

plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

