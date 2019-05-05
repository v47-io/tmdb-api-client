package io.v47.tmdb.api

import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.http.impl.get
import io.v47.tmdb.model.Company
import io.v47.tmdb.model.CompanyAlternativeNames
import io.v47.tmdb.model.CompanyImages

class CompaniesApi(private val httpExecutor: HttpExecutor) {
    /**
     * Get a companies details by id
     *
     * @param companyId The id of the company
     */
    fun getDetails(companyId: Int) =
        httpExecutor.execute(get<Company>("/company/$companyId"))

    /**
     * Get the alternative names of a company
     *
     * @param companyId The id of the company
     */
    fun getAlternativeNames(companyId: Int) =
        httpExecutor.execute(get<CompanyAlternativeNames>("/company/$companyId/alternative_names"))

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
    fun getImages(companyId: Int) =
        httpExecutor.execute(get<CompanyImages>("/company/$companyId/images"))
}
