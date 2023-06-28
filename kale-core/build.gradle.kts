plugins {
    java
    kotlin("multiplatform")
    kotlin("kapt")
    `maven-publish`
}

version = "5.0.0"
group = "chat.willow.kale"

kotlin {
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
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.21")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("junit:junit:4.12")
                implementation("org.mockito:mockito-core:2.2.9")
                implementation("com.nhaarman:mockito-kotlin:1.3.0")
                implementation("ch.qos.logback:logback-classic:1.1.2")
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}
