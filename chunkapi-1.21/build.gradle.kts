plugins {
    java
    id("com.gradleup.shadow") version "8.3.4"
    id("io.papermc.paperweight.userdev") version "1.7.2"
}

dependencies {
    implementation(project(":chunkapi-core"))
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks {
    shadowJar {
        archiveFileName.set("chunkapi-1.21.jar")

        relocate("de.tr7zw", "net.echo.chunkapi.relocated.tr7zw")
    }
}