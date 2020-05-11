package com.ageet.gradle.oss_liicenses_plugin_ext

import java.io.File

open class OssLicensesExtension {
    internal var additionalLicenses: List<File> = emptyList()
    internal var mappingBody: List<File> = emptyList()
    internal var skipDependenciesTask: Boolean = false

    fun additionalLicenses(vararg files: File) {
        additionalLicenses = files.toList()
    }

    fun mappingBody(vararg files: File) {
        mappingBody = files.toList()
    }

    fun skipDependenciesTask(skip: Boolean) {
        skipDependenciesTask = skip
    }
}
