import org.gradle.kotlin.dsl.*

project.setProperty("archivesBaseName", "processor")

plugins {
    java
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib", "1.1.51"))
    compile("com.squareup:kotlinpoet:0.6.0")

    testCompile("junit:junit:4.12")
    testCompile("org.mockito:mockito-core:2.2.9")
    testCompile("com.nhaarman:mockito-kotlin:1.3.0")
}