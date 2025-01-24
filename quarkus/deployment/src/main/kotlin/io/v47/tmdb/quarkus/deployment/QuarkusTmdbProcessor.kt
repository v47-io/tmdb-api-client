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
package io.v47.tmdb.quarkus.deployment

import io.quarkus.arc.deployment.IgnoreSplitPackageBuildItem
import io.quarkus.bootstrap.classloading.QuarkusClassLoader
import io.quarkus.deployment.annotations.BuildProducer
import io.quarkus.deployment.annotations.BuildStep
import io.quarkus.deployment.builditem.CombinedIndexBuildItem
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem
import io.quarkus.deployment.builditem.FeatureBuildItem
import io.quarkus.deployment.builditem.IndexDependencyBuildItem
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem
import io.quarkus.jackson.spi.ClassPathJacksonModuleBuildItem
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.jackson.TmdbApiModule
import io.v47.tmdb.model.TmdbType
import java.util.function.BooleanSupplier

private const val FEATURE = "tmdb-api-client"

internal class QuarkusTmdbProcessor {
    @BuildStep
    fun feature() = FeatureBuildItem(FEATURE)

    @BuildStep
    fun enableSslNativeSupport() =
        ExtensionSslNativeSupportBuildItem(FEATURE)

    @BuildStep
    fun registerJacksonModules(jacksonModules: BuildProducer<ClassPathJacksonModuleBuildItem>) {
        jacksonModules.produce(ClassPathJacksonModuleBuildItem(TmdbApiModule::class.qualifiedName))
    }

    @BuildStep
    fun indexDependencies(index: BuildProducer<IndexDependencyBuildItem>) {
        index.produce(IndexDependencyBuildItem("io.v47.tmdb-api-client", "api"))
        index.produce(IndexDependencyBuildItem("io.v47.tmdb-api-client", "core"))
    }

    @BuildStep
    fun registerTmdbTypes(
        index: CombinedIndexBuildItem,
        reflection: BuildProducer<ReflectiveClassBuildItem>
    ) {
        reflection.produce(
            ReflectiveClassBuildItem
                .builder(RawErrorResponse::class.java)
                .methods(true)
                .build()
        )

        reflection.produce(
            ReflectiveClassBuildItem
                .builder(TmdbType::class.java)
                .methods(true)
                .build()
        )

        index.index
            .getAllKnownSubclasses(TmdbType::class.java)
            .forEach { classInfo ->
                reflection.produce(
                    ReflectiveClassBuildItem.builder(classInfo.name().toString())
                        .methods(true)
                        .build()
                )
            }

        index.index
            .knownClasses
            .asSequence()
            .filter { it.name().packagePrefix() == "io.v47.tmdb.jackson.mixins" }
            .forEach { classInfo ->
                ReflectiveClassBuildItem.builder(classInfo.name().toString())
                    .methods(true)
                    .build()
            }
    }

    @BuildStep
    fun ignoreSplitPackages() =
        IgnoreSplitPackageBuildItem(
            listOf(
                "io.v47.tmdb.http",
                "io.v47.tmdb.http.impl",
                "io.v47.tmdb.utils",
                "io.v47.tmdb.jackson",
                "io.v47.tmdb.jackson.mixins",
                "io.v47.tmdb"
            )
        )

    @BuildStep(onlyIf = [IsTckActive::class])
    fun indexTckDependencies(index: BuildProducer<IndexDependencyBuildItem>) {
        index.produce(IndexDependencyBuildItem("io.v47.tmdb-api-client", "http-client-tck"))
    }

    @BuildStep(onlyIf = [IsTckActive::class])
    fun registerTckClasses(reflection: BuildProducer<ReflectiveClassBuildItem>) {
        val tckClasses = listOf(
            "io.v47.tmdb.http.tck.tests.AbstractTckTest",
            "io.v47.tmdb.http.tck.tests.ValidComplexResponseTest",
            "io.v47.tmdb.http.tck.tests.ValidSimpleResponseTest"
        )

        val tckSerializableClasses = listOf(
            "io.v47.tmdb.http.tck.TckResult",
            "io.v47.tmdb.http.tck.TckResult\$Success",
            "io.v47.tmdb.http.tck.TckResult\$Failure",
            "io.v47.tmdb.http.tck.tests.ValidComplexResponseTest\$CompanyAlternativeNames",
            "io.v47.tmdb.http.tck.tests.ValidComplexResponseTest\$CompanyAlternativeNames\$AlternativeName",
            "io.v47.tmdb.http.tck.tests.ValidSimpleResponseTest\$Company"
        )

        tckClasses.forEach { name ->
            reflection.produce(
                ReflectiveClassBuildItem.builder(name)
                    .constructors(true)
                    .fields(true)
                    .build()
            )
        }

        tckSerializableClasses.forEach { name ->
            reflection.produce(
                ReflectiveClassBuildItem.builder(name)
                    .methods(true)
                    .build()
            )
        }
    }

    @BuildStep(onlyIf = [IsTckActive::class])
    fun ignoreTckSplitPackages() =
        IgnoreSplitPackageBuildItem(listOf("io.v47.tmdb.http.tck"))

    class IsTckActive : BooleanSupplier {
        override fun getAsBoolean(): Boolean {
            return QuarkusClassLoader.isClassPresentAtRuntime("io.v47.tmdb.http.tck.HttpClientTck")
        }
    }
}
