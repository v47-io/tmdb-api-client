/*
 * The Clear BSD License
 *
 * Copyright (c) 2025, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import name.remal.gradle_plugins.dsl.extensions.configure
import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.util.*
import java.util.Calendar.YEAR

private val Project.libs: LibrariesForLibs
    get() = extensions.getByType()

plugins {
    kotlin("jvm")

    id("com.github.hierynomus.license")
    id("com.gorylenko.gradle-git-properties")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")

    jacoco
}

kotlin {
    jvmToolchain(17)
}

tasks.compileKotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xno-param-assertions", "-Xno-call-assertions")
        javaParameters = true
    }
}

tasks.compileTestKotlin {
    compilerOptions {
        javaParameters = true
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junitApi)
    testRuntimeOnly(libs.junitEngine)
}

gitProperties {
    gitPropertiesName = "${rootProject.name}-${project.name}-version.properties"
}

detekt {
    source.setFrom("src/main/kotlin", "src/test/kotlin")
}

val reportMerge: ReportMergeTask = rootProject.tasks["reportMerge"] as ReportMergeTask

tasks.withType<Detekt> {
    finalizedBy(reportMerge)

    reports {
        xml.required = true
        sarif.required = true
    }

    reportMerge.configure { mergeTask ->
        mergeTask.input.from(sarifReportFile.get().asFile)
    }
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.test {
    useJUnitPlatform()

    extensions
        .getByType<JacocoTaskExtension>()
        .setDestinationFile(layout.buildDirectory.file("jacoco/test.exec").get().asFile)
}

license {
    excludes(
        setOf(
            "**/*.json",
            "**/*.properties",
            "**/META-INF/**/*"
        )
    )

    header = rootProject.file("LICENSE")
    skipExistingHeaders = true

    ext {
        set("year", Calendar.getInstance().get(YEAR))
    }
}

tasks.getByName<DokkaTaskPartial>("dokkaHtmlPartial") {
    dokkaSourceSets {
        configureEach {
            perPackageOption {
                matchingRegex = ".*\\.(impl|utils).*"
                suppress.set(true)
            }

            displayName = project.path.split(':').filterNot { it.isEmpty() }.joinToString(" - ")
            moduleName = displayName

            includes.from("$projectDir/docs.md")
        }
    }
}

tasks.getByName<DokkaTask>("dokkaJavadoc") {
    dokkaSourceSets {
        configureEach {
            perPackageOption {
                matchingRegex = ".*\\.(impl|utils).*"
                suppress = true
            }

            displayName = project.path.split(':').filterNot { it.isEmpty() }.joinToString(" - ")
            moduleName = displayName

            includes.from("$projectDir/docs.md")
        }
    }
}
