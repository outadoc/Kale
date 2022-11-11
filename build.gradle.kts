import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.jvm.tasks.Jar

val kaleVersion = "5.0.0"

val projectTitle = "Kale"

apply {
    plugin("maven-publish")
    plugin("jacoco")
    plugin("idea")
}

plugins {
    java
    kotlin("jvm") version "1.6.21"
    kotlin("kapt") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

jacoco {
    toolVersion = "0.7.9"
}

val jacocoTestReport = project.tasks.getByName("jacocoTestReport")

jacocoTestReport.doFirst {
    (jacocoTestReport as JacocoReport).classDirectories.setFrom(
        fileTree("build/classes/main").apply {
            // Exclude well known data classes that should contain no logic
            // Remember to change values in codecov.yml too
            exclude("**/*Event.*")
            exclude("**/*State.*")
            exclude("**/*Configuration.*")
            exclude("**/*Runner.*")
        }
    )

    jacocoTestReport.reports.xml.required.set(true)
    jacocoTestReport.reports.html.required.set(true)
}

compileJava {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.xml.bind:jaxb-api:2.3.0")

    implementation("org.slf4j:slf4j-api:1.7.21")
    implementation("io.reactivex.rxjava2:rxjava:2.1.6")
    implementation("io.reactivex.rxjava2:rxkotlin:2.1.0")
    implementation("com.squareup:kotlinpoet:0.6.0")

    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.2.9")
    testImplementation("com.nhaarman:mockito-kotlin:1.3.0")
    testImplementation("ch.qos.logback:logback-classic:1.1.2")
}

test {
    testLogging.setEvents(listOf("passed", "skipped", "failed", "standardError"))
}

val buildNumberAddition = if (project.hasProperty("BUILD_NUMBER")) {
    ".${project.property("BUILD_NUMBER")}"
} else {
    ""
}

version = "$kaleVersion$buildNumberAddition"
group = "chat.willow.kale"
project.setProperty("archivesBaseName", "Kale")

shadowJar {
    mergeServiceFiles()
    relocate("kotlin", "chat.willow.kale.repack.kotlin")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
}

val sourcesTask = task<Jar>("sourcesJar") {
    dependsOn("classes")

    from(sourceSets("main").allSource)
    archiveClassifier.set("sources")
}

project.artifacts.add("archives", sourcesTask)
project.artifacts.add("archives", shadowJarTask())

configure<PublishingExtension> {
    val deployUrl = if (project.hasProperty("DEPLOY_URL")) {
        project.property("DEPLOY_URL")
    } else {
        project.buildDir.absolutePath
    }

    repositories.maven {
        setUrl("$deployUrl")
    }

    publications {
        create<MavenPublication>("mavenJava") {
            from(components.getByName("java"))
            artifact(sourcesTask)
            artifactId = projectTitle
        }
    }
}

fun Project.jar(setup: Jar.() -> Unit) = (project.tasks.getByName("jar") as Jar).setup()
fun jacoco(setup: JacocoPluginExtension.() -> Unit) = the<JacocoPluginExtension>().setup()
fun shadowJar(setup: ShadowJar.() -> Unit) = shadowJarTask().setup()
fun Project.test(setup: Test.() -> Unit) = (project.tasks.getByName("test") as Test).setup()
fun Project.compileJava(setup: JavaCompile.() -> Unit) =
    (project.tasks.getByName("compileJava") as JavaCompile).setup()

fun shadowJarTask() = (project.tasks.findByName("shadowJar") as ShadowJar)
fun sourceSets(name: String) =
    (project.property("sourceSets") as SourceSetContainer).getByName(name)
