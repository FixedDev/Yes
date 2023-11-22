plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.6"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.fixeddev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.10.7")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.test {
    useJUnitPlatform()
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

tasks.shadowJar {
    relocate("com.github.stefvanschie.inventoryframework", "me.fixeddev.troll.inventoryframework")
}