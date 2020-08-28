package com.ageet.gradle.oss_liicenses_plugin_ext

import java.io.File

open class OssLicensesExtension {
    internal var additionalLicenses: List<File> = emptyList()
    internal var mappingBody: List<File> = emptyList()
    internal var exclude: List<String> = emptyList()
    internal var excludeRegex: List<Regex> = emptyList()
    internal var skipDependenciesTask: Boolean = false

    fun additionalLicenses(vararg files: File) {
        additionalLicenses = additionalLicenses + files.toList()
    }

    fun mappingBody(vararg files: File) {
        mappingBody = mappingBody + files.toList()
    }

    fun exclude(vararg name: String) {
        exclude = exclude + name.toList()
    }

    fun excludeRegex(vararg regex: String) {
        excludeRegex = excludeRegex + regex.map { it.toRegex() }
    }

    fun skipDependenciesTask(skip: Boolean) {
        skipDependenciesTask = skip
    }
}
