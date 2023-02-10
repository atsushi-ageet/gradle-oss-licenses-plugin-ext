package com.ageet.gradle.oss_liicenses_plugin_ext

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.google.android.gms.oss.licenses.plugin.DependencyTask
import com.google.android.gms.oss.licenses.plugin.LicensesTask
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logging

class OssLicensesPluginExt : Plugin<Project> {

    private val logger = Logging.getLogger(OssLicensesPluginExt::class.java)
    private lateinit var extension: OssLicensesExtension

    override fun apply(project: Project) {
        extension = project.extensions.create("ossLicenses", OssLicensesExtension::class.java)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.apply {
            onVariants(selector().all(), { variant ->
                configureVariantTasks(project, variant)
            })
        }
    }

    private fun configureVariantTasks(project: Project, variant: Variant) {
        val licenseTask = project.tasks.findByName("${variant.name}OssLicensesTask") as LicensesTask
        if (extension.skipDependenciesTask) {
            val dependenciesTask = project.tasks.findByName("${variant.name}OssDependencyTask") as DependencyTask
            logger.quiet("Skip ${dependenciesTask.name} task")
            dependenciesTask.isEnabled = false
            val generateFileTask = project.tasks.register("${variant.name}OssGenerateEmptyDependencies") { task ->
                val outputFile = dependenciesTask.dependenciesJson.asFile.get()
                val outputDir = outputFile.parentFile
                task.outputs.apply {
                    dir(outputDir)
                    file(outputFile)
                }
                task.doLast(object : Action<Task> {
                    override fun execute(t: Task) {
                        outputFile.writeText("[]")
                    }
                })
            }
            licenseTask.dependsOn(generateFileTask)
        }
        licenseTask.inputs.apply {
            files(licenseTask.inputs.files + extension.additionalLicenses + extension.mappingBody)
        }
        licenseTask.doLast(object : Action<Task> {
            override fun execute(t: Task) {
                customLicensesFile(licenseTask)
            }
        })

        project.tasks.register("${variant.name}OssLicensesExport", ExportTask::class.java) {
            it.dependsOn(licenseTask)
            it.licensesFile = licenseTask.licenses
            it.licensesMetadataFile = licenseTask.licensesMetadata
            it.outputFile = project.rootProject.file("${variant.name}OssLicenses.json")
        }
    }

    private fun customLicensesFile(licenseTask: LicensesTask) {
        val mappingBody = readMappingBody(extension.mappingBody)
        val licenses = (readLicenses(licenseTask.licenses, licenseTask.licensesMetadata) + readAdditionalLicenses(extension.additionalLicenses))
                .filterNot { license -> extension.exclude.any { it == license.name } }
                .filterNot { license -> extension.excludeRegex.any { it.containsMatchIn(license.name) } }
                .map { license ->
                    val mappedBody = mappingBody[license.body] ?: license.body
                    License(license.name, mappedBody)
                }
        writeLicenseMetadata(licenses, licenseTask.licensesMetadata)
        writeLicense(licenses, licenseTask.licenses)
    }
}
