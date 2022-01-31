plugins {
    val kotlinVersion = "1.4.30"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.9.2"
}

group = "cn.transfur"
version = "0.1.1"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
}

dependencies {
    //Gson
    implementation("com.google.code.gson:gson:2.8.9")
    // https://mvnrepository.com/artifact/org.yaml/snakeyaml
    implementation("org.yaml:snakeyaml:1.30")
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    api("net.mamoe:mirai-console-terminal:2.9.2") // 自行替换版本
    api("net.mamoe:mirai-core:2.9.2")
}