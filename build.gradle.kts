plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.wispforest.io")
    flatDir {
        dirs("libs")
    }}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("io.wispforest:endec:0.1.9")
    implementation("io.wispforest.endec:netty:0.1.5")
    implementation("com.mojang:datafixerupper:8.0.0-SNAPSHOT")
    implementation("com.google.guava:guava:33.4.0-jre")
    implementation("io.netty:netty-all:4.1.24.Final")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("it.unimi.dsi:fastutil:8.5.15")



}