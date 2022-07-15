/**
 * BSD 3-Clause License
 * <p>
 * Copyright (c) 2022, the tmdb-api-client authors All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <p>
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * <p>
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * <p>
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.quarkus.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveHierarchyBuildItem;
import io.v47.tmdb.http.quarkus.runtime.TmdbClientConfiguration;
import io.v47.tmdb.model.TmdbType;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Type;

public class TmdbProcessor {
    private static final DotName TMDB_TYPE = DotName.createSimple(TmdbType.class.getName());

    @BuildStep
    public void addDependencies(BuildProducer<IndexDependencyBuildItem> indexDependency) {
        indexDependency.produce(new IndexDependencyBuildItem("com.neovisionaries", "nv-i18n"));
        indexDependency.produce(new IndexDependencyBuildItem("io.v47.tmdb-api-client", "api"));
        indexDependency.produce(new IndexDependencyBuildItem("io.v47.tmdb-api-client", "core"));
        indexDependency.produce(new IndexDependencyBuildItem("org.jetbrains.kotlin", "kotlin-stdlib"));
    }

    @BuildStep
    public void register(CombinedIndexBuildItem combinedIndex, BuildProducer<ReflectiveHierarchyBuildItem> reflectiveHierarchyClass, BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        Type tmdbType = Type.create(TMDB_TYPE, Type.Kind.CLASS);
        reflectiveHierarchyClass.produce(new ReflectiveHierarchyBuildItem.Builder().type(tmdbType).serialization(true).build());

        combinedIndex.getIndex().getAllKnownSubclasses(TMDB_TYPE).forEach(classInfo -> {
            Type jandexType = Type.create(classInfo.name(), Type.Kind.CLASS);
            reflectiveHierarchyClass.produce(new ReflectiveHierarchyBuildItem.Builder().type(jandexType).serialization(true).build());
        });

        additionalBeans.produce(new AdditionalBeanBuildItem(TmdbClientConfiguration.class));
    }
}
