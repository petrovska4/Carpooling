// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false
}

buildscript {
    repositories {
        google()  // Ensure Google repository is included
        mavenCentral()
    }
    dependencies {
        // Add the Google Services plugin
        classpath("com.google.gms:google-services:4.3.15")  // Use the latest version
    }
}
