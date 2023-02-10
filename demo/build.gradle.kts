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
    compileSdk = 30

    defaultConfig {
        minSdk = 21
        targetSdk = 30

        applicationId = "com.ageet.gradle.plugin.oss_licenses_ext.demo"
        versionCode = 1
        versionName = "1.0"
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
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
}
