import korlibs.korge.gradle.*

plugins {
	alias(libs.plugins.korge)
}

korge {
	id = "com.sample.demo"
	
	targetJvm()
	targetDesktop()

	serializationJson()
    serialization()
}


dependencies {
    add("commonMainApi", project(":deps"))
}

