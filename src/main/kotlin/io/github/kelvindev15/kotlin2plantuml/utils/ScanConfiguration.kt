package io.github.kelvindev15.kotlin2plantuml.utils

interface ScanConfiguration {

    /**
     * Add a package in which to scan classes in hierarchies.
     */
    fun addPackage(packageName: String)

    /**
     * Don't scan classes in [packageName] package when building hierarchies.
     */
    fun removePackage(packageName: String)

    /**
     * The list of packages in which to scan classes.
     */
    val scanPackages: List<String>

    /**
     * Add a path to the classpath.
     */
    fun addClasspath(classpath: String)

    /**
     * Remove the path from the classpath.
     */
    fun removeClasspath(classpath: String)

    /**
     * @return a classloader with the added paths.
     */
    val classLoader: ClassLoader
}
