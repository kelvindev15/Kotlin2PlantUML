package io.github.kelvindev15.kotlin2plantuml.utils

import java.net.URL
import java.net.URLClassLoader
import kotlin.io.path.Path

/**
 * A default implementation of [ScanConfiguration].
 */
class DefaultScanConfiguration private constructor() {
    companion object : ScanConfiguration {
        private val packages = mutableListOf<String>()
        private val classpath = mutableListOf<URL>()

        override fun addPackage(packageName: String) {
            packages.add(packageName)
        }

        override fun removePackage(packageName: String) {
            packages.remove(packageName)
        }

        override val scanPackages: List<String>
            get() = packages.toList()

        override fun addClasspath(classpath: String) {
            this.classpath.add(classpath.toURL())
        }

        override fun removeClasspath(classpath: String) {
            this.classpath.remove(classpath.toURL())
        }

        private fun String.toURL() = Path(this).toUri().toURL()

        override val classLoader: ClassLoader
            get() = URLClassLoader(classpath.toTypedArray())
    }
}
