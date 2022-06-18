plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.3.7"
}

group = "me.vrganj"
version = "1.0-SNAPSHOT"

dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")
}

tasks {
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(reobfJar)
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
