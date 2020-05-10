package com.ageet.gradle.oss_liicenses_plugin_ext

data class License(val name: String, val body: String) {
    val bodySizeInBytes: Int by lazy { body.toByteArray().size }
}
