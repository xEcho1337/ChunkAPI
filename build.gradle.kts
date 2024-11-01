plugins {
    java
}

allprojects {
    group = "net.echo"
    version = "1.0.5"

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.codemc.io/repository/maven-public/")
        maven("https://repo.codemc.io/repository/nms/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/groups/public/")
    }

    tasks.withType<Jar> {
        destinationDirectory.set(file("$rootDir/build/libs"))
    }
}

tasks {
    register("shadowJarAll") {
        group = "build"
        description = "Builds both ChunkAPI-1.8 and ChunkAPI-1.21 shadow JARs"

        dependsOn(":chunkapi-1.8:shadowJar", ":chunkapi-1.21:shadowJar")
    }
}

subprojects {
    apply(plugin = "java")

    dependencies {
        implementation("de.tr7zw:item-nbt-api:2.13.2")
    }
}