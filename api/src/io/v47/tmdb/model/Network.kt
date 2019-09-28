/**
 * Copyright 2019 Alex Katlein <dev@vemilyus.com>
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
package io.v47.tmdb.model

import com.neovisionaries.i18n.CountryCode

data class Network(
    override val id: Int,
    val name: String?,
    val homepage: String?,
    val headquarters: String?,
    val originCountry: CountryCode?
) : TmdbType(), TmdbIntId

data class NetworkAlternativeNames(
    override val id: Int?,
    val results: List<NetworkAlternativeNameResult> = emptyList()
) : TmdbType(), TmdbIntId

data class NetworkAlternativeNameResult(
    val name: String?,
    val type: String?
) : TmdbType()

data class NetworkImages(
    override val id: Int?,
    val logos: List<ImageListResult> = emptyList()
) : TmdbType(), TmdbIntId
