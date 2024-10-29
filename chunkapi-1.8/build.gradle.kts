plugins {
    java
    id("com.gradleup.shadow") version "8.3.4"
}

dependencies {
    implementation(project(":chunkapi-core"))
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    shadowJar {
        archiveFileName.set("chunkapi-1.8.jar")

        relocate("de.tr7zw", "net.echo.chunkapi.relocated.tr7zw")
    }
}