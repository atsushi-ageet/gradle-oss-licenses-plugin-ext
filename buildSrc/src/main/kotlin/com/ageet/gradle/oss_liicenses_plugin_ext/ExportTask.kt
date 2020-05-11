package com.ageet.gradle.oss_liicenses_plugin_ext

import com.google.gson.GsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class ExportTask : DefaultTask() {

    @InputFile
    lateinit var licensesFile: File

    @InputFile
    lateinit var licensesMetadataFile: File

    @OutputFile
    lateinit var outputFile: File

    @TaskAction
    fun action() {
        val licenses = readLicenses(licensesFile, licensesMetadataFile)
        val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()
        outputFile.writeText(gson.toJson(licenses))
    }
}
