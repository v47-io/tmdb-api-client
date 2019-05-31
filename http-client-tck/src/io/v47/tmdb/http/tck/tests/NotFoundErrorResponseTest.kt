package io.v47.tmdb.http.tck.tests

import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.utils.tmdbTypeReference
import io.v47.tmdb.utils.toTypeInfo

internal class NotFoundErrorResponseTest : AbstractTckTest("https://api.themoviedb.org") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val checkResponse = ErrorResponse("The resource you requested could not be found.", 34)

        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/3/company/0",
            mapOf("api_key" to listOf(apiKey))
        )

        val result = Flowable.fromPublisher(
            httpClient.execute(
                request,
                tmdbTypeReference<ValidSimpleResponseTest.Company>().toTypeInfo()
            )
        ).blockingFirst()

        return if (result.status != 404)
            TckTestResult.Failure(404, result.status)
        else {
            @Suppress("CAST_NEVER_SUCCEEDS")
            val errorResponse = result.body as? ErrorResponse
            if (errorResponse !is ErrorResponse || errorResponse != checkResponse)
                TckTestResult.Failure(checkResponse, errorResponse)
            else
                TckTestResult.Success
        }
    }
}
