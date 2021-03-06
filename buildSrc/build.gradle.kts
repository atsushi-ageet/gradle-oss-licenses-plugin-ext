val artifactBaseName = "oss-licenses-plugin-ext"
version = "0.10.4"
group = "com.ageet.oss-licenses-plugin-ext"

plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.4.32"
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.android.tools.build:gradle:4.1.3")
    implementation("com.google.android.gms:oss-licenses-plugin:0.10.4")
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

gradlePlugin {
    plugins {
        create("ossLicensesPluginExt") {
            id = project.group.toString()
            implementationClass = "com.ageet.gradle.oss_liicenses_plugin_ext.OssLicensesPluginExt"
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            getByName<MavenPublication>("pluginMaven") {
                artifactId = artifactBaseName
                artifact(sourcesJar.get())
            }
        }
    }
}
