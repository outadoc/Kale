import org.gradle.kotlin.dsl.*

plugins {
    java
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", "1.1.51"))
    implementation("com.squareup:kotlinpoet:0.6.0")

    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.2.9")
    testImplementation("com.nhaarman:mockito-kotlin:1.3.0")
}