plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.ixbob.thepit"
version = "0.0.1"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    implementation("com.github.retrooper:packetevents-spigot:2.7.0")
    implementation("org.mongodb:mongodb-driver-sync:5.3.1")
    testImplementation(platform("org.junit:junit-bom:5.12.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.assemble {
    dependsOn(tasks.named("reobfJar"))
}

tasks.register("moveOutputToTestServer") {
    doLast {
        val testServerDir = project.findProperty("testServerDir") ?: "$projectDir/test-server"
        val pluginsDir = file("$testServerDir/plugins")
        if (!pluginsDir.exists()) {
            pluginsDir.mkdirs()
        }
        val jarFiles = fileTree("$projectDir/build/libs").matching {
            include("*-reobf.jar")
        }
        jarFiles.forEach { jar ->
            copy {
                from(jar)
                into(pluginsDir)
            }
            println("Moved ${'$'}{jar.name} to ${'$'}pluginsDir")
        }
    }
}