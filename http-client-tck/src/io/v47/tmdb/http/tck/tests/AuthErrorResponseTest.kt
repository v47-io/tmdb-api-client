package io.v47.tmdb.http.tck.tests

import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.utils.tmdbTypeReference
import io.v47.tmdb.utils.toTypeInfo

internal class AuthErrorResponseTest : AbstractTckTest("https://api.themoviedb.org") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val checkResponse = ErrorResponse("Invalid API key: You must be granted a valid key.", 7)

        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/3/company/2"
        )

        val result = Flowable.fromPublisher(
            httpClient.execute(
                request,
                tmdbTypeReference<ValidSimpleResponseTest.Company>().toTypeInfo()
            )
        ).blockingFirst()

        return if (result.status != 401)
            TckTestResult.Failure(401, result.status)
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
