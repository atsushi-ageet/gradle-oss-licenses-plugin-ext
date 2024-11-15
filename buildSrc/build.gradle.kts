val artifactBaseName = "oss-licenses-plugin-ext"
version = "0.10.6-2"
group = "com.ageet.oss-licenses-plugin-ext"

plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.8.10"
    id("com.gradle.plugin-publish") version "1.1.0"
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.android.tools.build:gradle:7.4.2")
    implementation("com.google.android.gms:oss-licenses-plugin:0.10.6")
    implementation("com.google.code.gson:gson:2.8.9")
}

gradlePlugin {
    website.set("https://github.com/atsushi-ageet/gradle-oss-licenses-plugin-ext")
    vcsUrl.set("https://github.com/atsushi-ageet/gradle-oss-licenses-plugin-ext")
    plugins {
        create("ossLicensesPluginExt") {
            id = project.group.toString()
            displayName = "OSS Licenses Gradle Plugin Extension"
            description = "Can customize the OSS license list."
            implementationClass = "com.ageet.gradle.oss_liicenses_plugin_ext.OssLicensesPluginExt"
            tags.set(listOf("android", "license"))
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            getByName<MavenPublication>("pluginMaven") {
                artifactId = artifactBaseName
            }
        }
    }
}
