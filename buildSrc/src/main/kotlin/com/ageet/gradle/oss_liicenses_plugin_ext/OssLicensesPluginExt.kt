package com.ageet.gradle.oss_liicenses_plugin_ext

import com.android.build.gradle.AppExtension
import com.google.android.gms.oss.licenses.plugin.DependencyTask
import com.google.android.gms.oss.licenses.plugin.LicensesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logging

class OssLicensesPluginExt : Plugin<Project> {

    private val logger = Logging.getLogger(OssLicensesPluginExt::class.java)
    private lateinit var extension: OssLicensesExtension

    override fun apply(project: Project) {
        extension = project.extensions.create("ossLicenses", OssLicensesExtension::class.java)
        project.afterEvaluate {
            val licenseTask = project.tasks.findByName("generateLicenses") as LicensesTask
            if (extension.skipDependenciesTask) {
                logger.quiet("skip dependencies task")
                val dependenciesTask = project.tasks.findByName("getDependencies") as DependencyTask
                dependenciesTask.isEnabled = false
                project.tasks.create("generateEmptyDependencies") { task ->
                    licenseTask.mustRunAfter(task)
                    project.android.applicationVariants.forEach { variant ->
                        variant.preBuildProvider.configure { it.dependsOn(task) }
                    }
                    task.outputs.apply {
                        dir(dependenciesTask.outputDir)
                        file(dependenciesTask.outputFile)
                    }
                    task.doLast {
                        dependenciesTask.outputFile.writeText("[]")
                    }
                }
            }
            licenseTask.inputs.files(licenseTask.inputs.files + extension.additionalLicenses + extension.mappingBody)
            licenseTask.doLast {
                customLicensesFile(licenseTask)
            }

            project.tasks.create("exportOssLicenses", ExportTask::class.java) {
                it.dependsOn(licenseTask)
                it.licensesFile = licenseTask.licenses
                it.licensesMetadataFile = licenseTask.licensesMetadata
                it.outputFile = project.rootProject.file("ossLicenses.json")
            }
        }
    }

    private fun customLicensesFile(licenseTask: LicensesTask) {
        val mappingBody = readMappingBody(extension.mappingBody)
        val licenses = (readLicenses(licenseTask.licenses, licenseTask.licensesMetadata) + readAdditionalLicenses(extension.additionalLicenses))
                .map { license ->
                    val mappedBody = mappingBody[license.body]?.readText() ?: license.body
                    License(license.name, mappedBody)
                }
        writeLicenseMetadata(licenses, licenseTask.licensesMetadata)
        writeLicense(licenses, licenseTask.licenses)
    }

    private val Project.android: AppExtension get() = extensions.getByType(AppExtension::class.java)
}
