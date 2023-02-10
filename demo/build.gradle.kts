repositories {
    google()
    mavenCentral()
}

plugins {
    id("com.android.application")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.ageet.oss-licenses-plugin-ext")
}

android {
    namespace = "com.ageet.gradle.oss_liicenses_plugin_ext.demo"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        applicationId = "com.ageet.gradle.plugin.oss_licenses_ext.demo"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

ossLicenses {
//    skipDependenciesTask(true)
    additionalLicenses(file("../licenses/additionalLicenses.json"))
    mappingBody(file("../licenses/mappingBody.json"))
    exclude("absl")
    exclude("androidx.annotation:annotation")
    excludeRegex("google")
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
}
