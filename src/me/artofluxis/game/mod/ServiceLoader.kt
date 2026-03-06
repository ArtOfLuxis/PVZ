package me.artofluxis.game.mod

import java.io.*
import java.net.*
import java.util.jar.*

fun loadAndRunMod(modFile: File) {
    require(modFile.exists()) { "Jar not found: $modFile" }

    println("Loading JAR mod: $modFile")

    val jarUrl = modFile.toURI().toURL()
    val parent = Thread.currentThread().contextClassLoader
    val loader = URLClassLoader(arrayOf(jarUrl), parent)

    // try to find the ModInitializer object:
    val manifestMainClass = JarFile(modFile).use { jf ->
        jf.manifest?.mainAttributes?.getValue("Main-Class")?.trim()
    } ?: throw IllegalArgumentException("Manifest Main-Class not found in $modFile")

    val mainClass = loader.loadClass(manifestMainClass)

    // try to find the init method:
    val initFunction = mainClass.getMethod("init")

    initFunction.invoke(null)
}
