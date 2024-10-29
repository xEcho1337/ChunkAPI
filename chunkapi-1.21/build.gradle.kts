plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
        archiveBaseName.set("chunkapi-1.21")
        archiveClassifier.set("")
        archiveVersion.set("")

        relocate("de.tr7zw", "net.echo.chunkapi.relocated.tr7zw")
    }
}