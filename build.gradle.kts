import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.versioning.VersioningPlugin
import org.jetbrains.dokka.versioning.VersioningConfiguration

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.dokka:versioning-plugin:1.9.0")
    }
}

plugins {
    kotlin("jvm") version "1.9.0"
    jacoco
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("org.jetbrains.dokka") version "1.9.0"
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

group = "com.virusbear"
version = "1.1.0"

apply(plugin = "com.dipien.semantic-version")

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

    dokkaHtmlPlugin("org.jetbrains.dokka:versioning-plugin:1.9.0")
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

tasks.getByName<DokkaTask>("dokkaHtml") {
    val docVersionsDir = projectDir.resolve("javadoc/version")
    val currentVersion = project.version.toString()
    val currentDocsDir = docVersionsDir.resolve(currentVersion)
    outputDirectory.set(currentDocsDir)

    pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
        olderVersionsDir = docVersionsDir
        version = currentVersion
        renderVersionsNavigationOnAllPages = true

        olderVersions = docVersionsDir.list()?.map { File(docVersionsDir, it) }?.filter { it.isDirectory }
        versionsOrdering = olderVersions?.map { it.relativeTo(docVersionsDir).name }
    }

    doLast {
        val latestDir = file("javadoc/latest")
        latestDir.deleteRecursively()
        currentDocsDir.copyRecursively(latestDir)
        currentDocsDir.resolve("older").deleteRecursively()
    }
}

val dokkaHtmlJar = tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-docs")
}

val dokkaJavadocJar = tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

val jar = tasks.named<Jar>("jar")
val sourcesJar = tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

artifacts {
    archives(sourcesJar)
    archives(dokkaHtmlJar)
    archives(dokkaJavadocJar)
    archives(jar)
}

object Meta {
    const val desc = "Async kotlin client for beanstalkd work queue"
    const val license = "Apache-2.0"
    const val licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0"
    const val githubRepo = "virusbear/beanstalkt"
    const val release = "https://s01.oss.sonatype.org/service/local/"
    const val snapshot = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["dokkaHtmlJar"])
            artifact(tasks["dokkaJavadocJar"])

            pom {
                name.set(project.name)
                description.set(Meta.desc)
                url.set("https://github.com/${Meta.githubRepo}")

                licenses {
                    license {
                        name.set(Meta.license)
                        url.set(Meta.licenseUrl)
                    }
                }

                developers {
                    developer {
                        id.set("virusbear")
                        name.set("Fabian Fahrenholz")
                    }
                }

                scm {
                    connection.set("scm:https://github.com/${Meta.githubRepo}.git")
                    developerConnection.set("scm:git@github.com/${Meta.githubRepo}.git")
                    url.set("https://github.com/${Meta.githubRepo}")
                }

                issueManagement {
                    url.set("https://github.com/${Meta.githubRepo}/issues")
                }
            }
        }
    }
}

signing {
    val signingKey = providers.environmentVariable("GPG_SIGNING_KEY")
    val signingPassphrase = providers.environmentVariable("GPG_SIGNING_PASSPHRASE")

    if (signingKey.isPresent && signingPassphrase.isPresent) {
        useInMemoryPgpKeys(file(signingKey.get()).readText(), signingPassphrase.get())
        val extension = extensions.getByName("publishing") as PublishingExtension
        sign(extension.publications)
    }
}

nexusPublishing {
    this.repositories {
        sonatype {
            nexusUrl.set(uri(Meta.release))
            snapshotRepositoryUrl.set(uri(Meta.snapshot))

            val ossrhUsername = providers.environmentVariable("OSSRH_USERNAME")
            val ossrhPassword = providers.environmentVariable("OSSRH_PASSWORD")
            if (ossrhUsername.isPresent && ossrhPassword.isPresent) {
                username.set(ossrhUsername.get())
                password.set(ossrhPassword.get())
            }
        }
    }
}
