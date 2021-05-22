plugins {
    kotlin("jvm") version "1.5.0"
}

repositories {
    jcenter()
    maven ("https://papermc.io/repo/repository/maven-public/")
    maven ("https://mvnrepository.com/artifact/io.netty/netty-all")
    maven ("https://libraries.minecraft.net/")
    maven ("https://jitpack.io/")
}

dependencies {
    implementation(kotlin("stdlib"))

    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.mojang", "authlib", "1.5.21")

    implementation("io.netty","netty-all", "4.1.65.Final")

    implementation("com.github.rqbik", "bukkt", "1.0.3")
}

tasks {
    compileKotlin { kotlinOptions.jvmTarget = "15" }
    compileJava { options.encoding = "UTF-8" }
}