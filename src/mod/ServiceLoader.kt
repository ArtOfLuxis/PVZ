package mod

import java.io.*
import java.net.*
import java.util.jar.*

fun loadAndRunMod(jarPath: String) {
    val file = File(jarPath)
    require(file.exists()) { "Jar not found: $file" }

    println("Loading JAR mod: $jarPath")

    val jarUrl = file.toURI().toURL()
    val parent = Thread.currentThread().contextClassLoader
    val loader = URLClassLoader(arrayOf(jarUrl), parent)

    // try to find the ModInitializer object:
    val manifestMainClass = JarFile(file).use { jf ->
        jf.manifest?.mainAttributes?.getValue("Main-Class")?.trim()
    } ?: throw IllegalArgumentException("Manifest Main-Class not found in $jarPath")

    val mainClass = loader.loadClass(manifestMainClass)

    // try to find the init method:
    val initFunction = mainClass.getMethod("init")

    initFunction.invoke(null)
}
