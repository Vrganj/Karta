plugins {
    id("java")
}

group = "me.vrganj"
version = "1.0-SNAPSHOT"

repositories {
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}