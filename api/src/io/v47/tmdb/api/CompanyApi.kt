/**
 * Copyright 2020 The tmdb-api-v2 Authors
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
import io.v47.tmdb.model.Company
import io.v47.tmdb.model.CompanyAlternativeNames
import io.v47.tmdb.model.CompanyImages

class CompanyApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get a companies details by id
     *
     * @param companyId The id of the company
     */
    fun details(companyId: Int) =
        httpExecutor.execute(
            get<Company>("/company/{companyId}") {
                pathVar("companyId", companyId)
            }
        )

    /**
     * Get the alternative names of a company
     *
     * @param companyId The id of the company
     */
    fun alternativeNames(companyId: Int) =
        httpExecutor.execute(
            get<CompanyAlternativeNames>("/company/{companyId}/alternative_names") {
                pathVar("companyId", companyId)
            }
        )

    /**
     * Get a companies logos by id.
     *
     * There are two image formats that are supported for companies, PNG's and SVG's.
     * You can see which type the original file is by looking at the `file_type` field.
     * We prefer SVG's as they are resolution independent and as such, the width and
     * height are only there to reflect the original asset that was uploaded. An SVG
     * can be scaled properly beyond those dimensions if you call them as a PNG.
     *
     * For more information about how SVG's and PNG's can be used, take a read through
     * [this document](https://developers.themoviedb.org/3/getting-started/images)
     *
     * @param companyId The id of the company
     */
    fun images(companyId: Int) =
        httpExecutor.execute(
            get<CompanyImages>("/company/{companyId}/images") {
                pathVar("companyId", companyId)
            }
        )
}
