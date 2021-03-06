package com.ageet.gradle.oss_liicenses_plugin_ext

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.RandomAccessFile

internal fun writeLicense(licenses: List<License>, licenseFile: File) {
    licenseFile.printWriter().use { writer ->
        licenses.forEach {
            writer.println(it.body)
        }
    }
}

internal fun writeLicenseMetadata(licenses: List<License>, licenseMetadataFile: File) {
    var offset = 0
    licenseMetadataFile.printWriter().use { writer ->
        licenses.forEach {
            val bodySizeInBytes = it.bodySizeInBytes
            writer.println("$offset:${bodySizeInBytes} ${it.name}")
            offset += bodySizeInBytes + 1
        }
    }
}

internal fun readLicenses(licenseFile: File, licenseMetadataFile: File): List<License> = RandomAccessFile(licenseFile, "r").use { file ->
    """(\d+):(\d+) (.+)""".toRegex().findAll(licenseMetadataFile.readText())
            .map { it.destructured }
            .map { (start, length, name) ->
                License(name, file.readString(start.toLong(), length.toInt()))
            }.toList()
}

internal fun readAdditionalLicenses(files: List<File>): List<License> = files.flatMap { depends ->
    Gson().fromJson(depends.inputStream().reader(), JsonArray::class.java)
            .mapNotNull { it as? JsonObject }
            .map {
                License(it.getAsJsonPrimitive("name").asString, readLicenseByPath(depends.parent, it.getAsJsonPrimitive("path").asString))
            }
}

internal fun readMappingBody(files: List<File>): Map<String, String> = files.flatMap { mapping ->
    Gson().fromJson<Map<String, String>>(mapping.inputStream().reader(), TypeToken.getParameterized(Map::class.java, String::class.java, String::class.java).type)
            .mapValues { readLicenseByPath(mapping.parent, it.value) }
            .toList()
}.toMap()

private fun readLicenseByPath(baseDir: String, path: String): String {
    val delimiterIndex = path.indexOf(':')
    if (delimiterIndex == -1) {
        throw Exception("Invalid path format")
    }
    val type = path.substring(0, delimiterIndex)
    val value = path.substring(delimiterIndex + 1)
    return when (type) {
        "built-in" -> OssLicensesPluginExt::class.java.getResource(value).readText()
        "file" -> File(baseDir, value).readText()
        "text" -> value
        else -> throw Exception("Unknown path type")
    }
}

private fun RandomAccessFile.readString(start: Long, length: Int): String {
    seek(start)
    return String(ByteArray(length).apply { read(this) })
}
