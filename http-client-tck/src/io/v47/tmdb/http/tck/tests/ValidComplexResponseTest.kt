package io.v47.tmdb.http.tck.tests

import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.utils.tmdbTypeReference
import io.v47.tmdb.utils.toTypeInfo

internal class ValidComplexResponseTest : AbstractTckTest("https://api.themoviedb.org") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/3/company/2/alternative_names",
            mapOf("api_key" to listOf(apiKey))
        )

        val result = Flowable.fromPublisher(
            httpClient.execute(
                request,
                tmdbTypeReference<CompanyAlternativeNames>().toTypeInfo()
            )
        ).map { it.body }.blockingFirst()

        val checkValue = CompanyAlternativeNames(
            2,
            listOf(
                CompanyAlternativeNames.AlternativeName("Disney", ""),
                CompanyAlternativeNames.AlternativeName("월트 디즈니 픽처스", "")
            )
        )

        return if (result != checkValue)
            TckTestResult.Failure(checkValue, result)
        else
            TckTestResult.Success
    }

    data class CompanyAlternativeNames(
        val id: Int,
        val results: List<AlternativeName>
    ) {
        data class AlternativeName(val name: String, val type: String)
    }
}
