plugins {
    kotlin("jvm") version "1.9.0"
    jacoco
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("org.jetbrains.dokka") version "1.9.0"
}

group = "com.virusbear"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.microutils:kotlin-logging:3.0.5")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    api("io.ktor:ktor-network:2.2.4")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(false)
    }
}

detekt {
    config.setFrom(rootDir.resolve("detekt.yaml"))
    parallel = true
}