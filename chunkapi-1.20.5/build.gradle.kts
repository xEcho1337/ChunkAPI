plugins {
    java
    id("com.gradleup.shadow") version "8.3.4"
    id("io.papermc.paperweight.userdev") version "1.7.2"
}

dependencies {
    implementation(project(":chunkapi-core"))
    paperweight.paperDevBundle("1.20.5-R0.1-SNAPSHOT")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks {
    shadowJar {
        archiveBaseName.set("chunkapi-1.20.5")
        archiveClassifier.set("")
        archiveVersion.set("")

        relocate("de.tr7zw", "net.echo.chunkapi.relocated.tr7zw")
    }
}