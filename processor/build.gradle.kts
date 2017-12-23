plugins {
    java
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", "1.2.10"))
    implementation("com.squareup:kotlinpoet:0.6.0")
}