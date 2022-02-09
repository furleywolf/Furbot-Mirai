rootProject.name = "furbot-mirai"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }

    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion

        val miraiVersion: String by settings
        id("net.mamoe.mirai-console") version miraiVersion
    }
}
