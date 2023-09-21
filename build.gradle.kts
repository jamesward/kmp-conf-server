buildscript {
    dependencies {
        classpath(universe.jib.native.image.extension)
    }
}

plugins {
    alias(universe.plugins.kotlin.multiplatform)
    alias(universe.plugins.kotlin.plugin.serialization)
    alias(universe.plugins.jib)
}

repositories {
    mavenCentral()
}

kotlin {
    linuxX64 {
        binaries {
            executable(listOf(DEBUG, RELEASE)) {
                entryPoint = "main"
                linkerOpts("--as-needed")
                freeCompilerArgs += "-Xoverride-konan-properties=linkerGccFlags.linux_x64=-lgcc -lgcc_eh -lc"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(universe.ktor.server.core)
                implementation(universe.ktor.server.cio)
                implementation(universe.ktor.server.content.negotiation)
                implementation(universe.ktor.serialization.kotlinx.json)
                implementation(universe.arrow.kt.suspendapp)
                implementation(universe.arrow.kt.suspendapp.ktor)
            }
        }
    }

}

tasks.register<Copy>("copyBinary") {
    dependsOn(tasks.first { it.name.contains("linkReleaseExecutable") })
    from(layout.buildDirectory.file("bin/linuxX64/releaseExecutable/kmp-conf-server.kexe"))
    into(layout.buildDirectory.dir("native/nativeCompile"))
}

tasks.withType<com.google.cloud.tools.jib.gradle.JibTask> {
    dependsOn("copyBinary")
}

jib {
    from {
        image = "gcr.io/distroless/base"
    }
    pluginExtensions {
        pluginExtension {
            implementation = "com.google.cloud.tools.jib.gradle.extension.nativeimage.JibNativeImageExtension"
            properties = mapOf(Pair("imageName", "kmp-conf-server.kexe"))
        }
    }
    container {
        mainClass = "MainKt"
    }
}

sourceSets.create("main")
