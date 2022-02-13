import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("net.mamoe.mirai-console")
}

repositories {
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
}

dependencies {
    // yamlkt
    implementation("net.mamoe.yamlkt:yamlkt-jvm:0.10.2")

    val miraiVersion: String by project
    implementation("net.mamoe:mirai-console-terminal:$miraiVersion")
    implementation("net.mamoe:mirai-core:$miraiVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}
