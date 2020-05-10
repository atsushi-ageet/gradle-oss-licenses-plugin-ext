val artifactBaseName = "oss-licenses-plugin-ext"
version = "0.10.1"
group = "com.ageet.oss-licenses-plugin-ext"

plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.3.72"
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.android.tools.build:gradle:3.6.3")
    implementation("com.google.android.gms:oss-licenses-plugin:0.10.2")
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

gradlePlugin {
    isAutomatedPublishing = false
    plugins {
        create("ossLicensesPluginExt") {
            id = project.group.toString()
            implementationClass = "com.ageet.gradle.oss_liicenses_plugin_ext.OssLicensesPluginExt"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            artifactId = artifactBaseName
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}
