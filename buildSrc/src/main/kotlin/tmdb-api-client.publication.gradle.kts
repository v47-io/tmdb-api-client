/**
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
import internal.common
import internal.ossrh

plugins {
    `java-base`
    `maven-publish`

    signing
    id("name.remal.maven-publish-ossrh")
}

val packageJavadoc = tasks.register("packageJavadoc", Jar::class.java) {
    archiveClassifier = "javadoc"

    val dokkaJavadoc = tasks.getByName("dokkaJavadoc")
    dependsOn(dokkaJavadoc)
    from(dokkaJavadoc.outputs)
}

val packageSources = tasks.register("packageSources", Jar::class.java) {
    archiveClassifier = "sources"
    from(sourceSets.main.get().allSource)
}

artifacts {
    add("archives", packageSources)
}

publishing {
    publications {
        create<MavenPublication>("tmdbApiClient") {
            groupId = "${project.group}"
            artifactId = project.name
            version = "${project.version}"

            from(components["java"])

            artifact(packageJavadoc) { classifier = "javadoc" }
            artifact(packageSources) { classifier = "sources" }

            pom {
                common(project)
            }
        }

        val ossrhUser = project.findProperty("ossrhUser")?.toString() ?: System.getenv("OSSRH_USER")
        val ossrhPass = project.findProperty("ossrhPass")?.toString() ?: System.getenv("OSSRH_PASS")

        if (ossrhUser != null && ossrhPass != null) {
            repositories {
                ossrh {
                    credentials {
                        username = ossrhUser
                        password = ossrhPass
                    }
                }
            }
        }
    }
}
