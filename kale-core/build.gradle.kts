plugins {
    java
    kotlin("multiplatform")
    kotlin("kapt")
    `maven-publish`
    id("org.jetbrains.kotlinx.kover")
}

version = "6.0.0"
group = "chat.willow.kale"

kotlin {
    ios()
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.0")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.21")
            }
        }

        val iosMain by getting

        val jvmTest by getting {
            dependencies {
                implementation("junit:junit:4.12")
                implementation("org.mockito:mockito-core:2.2.9")
                implementation("com.nhaarman:mockito-kotlin:1.3.0")
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
