plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.6"
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
