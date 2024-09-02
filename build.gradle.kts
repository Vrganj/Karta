plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.2"
}

group = "me.vrganj"
version = "1.0-SNAPSHOT"

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}

paperweight.reobfArtifactConfiguration.set(io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION)

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
