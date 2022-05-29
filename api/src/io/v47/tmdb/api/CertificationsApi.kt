/**
 * Copyright 2022 The tmdb-api-v2 Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.v47.tmdb.api

import io.v47.tmdb.http.get
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Certification

class CertificationsApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get an up to date list of the officially supported movie certifications on TMDb
     */
    fun forMovies() =
        httpExecutor.execute(get<Certification>("/certification/movie/list"))

    /**
     * Get an up to date list of the officially supported TV show certifications on TMDb
     */
    fun forTv() =
        httpExecutor.execute(get<Certification>("/certification/tv/list"))
}
