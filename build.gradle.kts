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
    // Gson
    implementation("com.google.code.gson:gson:2.8.9")
    // SnakeYaml
    implementation("org.yaml:snakeyaml:1.30")
    // Okhttp 3
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    val miraiVersion: String by project
    api("net.mamoe:mirai-console-terminal:$miraiVersion")
    api("net.mamoe:mirai-core:$miraiVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}
