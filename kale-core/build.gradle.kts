plugins {
    java
    kotlin("multiplatform")
    `maven-publish`
    id("org.jetbrains.kotlinx.kover")
    id("org.kodein.mock.mockmp")
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

        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

mockmp {
    usesHelper = true
}
