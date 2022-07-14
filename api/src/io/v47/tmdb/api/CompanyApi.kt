/**
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.api

import io.v47.tmdb.http.get
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Company
import io.v47.tmdb.model.CompanyAlternativeNames
import io.v47.tmdb.model.CompanyImages

class CompanyApi internal constructor(private val http: HttpExecutor) {
    /**
     * Get a companies details by id
     *
     * @param companyId The id of the company
     */
    fun details(companyId: Int) =
        http.get<Company>("/company/{companyId}") {
            pathVar("companyId", companyId)
        }


    /**
     * Get the alternative names of a company
     *
     * @param companyId The id of the company
     */
    fun alternativeNames(companyId: Int) =
        http.get<CompanyAlternativeNames>("/company/{companyId}/alternative_names") {
            pathVar("companyId", companyId)
        }


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
        http.get<CompanyImages>("/company/{companyId}/images") {
            pathVar("companyId", companyId)
        }

}