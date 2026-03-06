import korlibs.korge.gradle.*

plugins {
	alias(libs.plugins.korge)
}

val groupName = "me.artofluxis.game"
group = groupName

korge {
	id = groupName
	
	targetJvm()
	targetDesktop()

	serializationJson()
    serialization()

    jvmMainClassName = "$groupName.MainKt"
}

dependencies {
    add("commonMainApi", project(":deps"))
}

