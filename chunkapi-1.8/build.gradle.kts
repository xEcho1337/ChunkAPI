plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
        archiveBaseName.set("chunkapi-1.8")
        archiveClassifier.set("")
        archiveVersion.set("")

        relocate("de.tr7zw", "net.echo.chunkapi.relocated.tr7zw")
    }
}