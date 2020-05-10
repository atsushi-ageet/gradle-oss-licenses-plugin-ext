buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.3")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.2")
    }
}

repositories {
    google()
    jcenter()
}

plugins {
    id("com.android.application")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.ageet.oss-licenses-plugin-ext")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)

        applicationId = "com.ageet.gradle.plugin.oss_licenses_ext.demo"
        versionCode = 1
        versionName = "1.0"
    }
}

ossLicenses {
//    skipDependenciesTask(true)
    additionalLicenses(file("../licenses/depends.json"))
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
}
