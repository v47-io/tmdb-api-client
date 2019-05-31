package io.v47.tmdb.http.tck.tests

import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.utils.TypeInfo

internal class ImageNotFoundResponseTest : AbstractTckTest("https://image.tmdb.org/t/p") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val checkError = ErrorResponse("File not Found", 404)

        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/original/wwemzKWzjKYJ3r4cm.svg"
        )

        val result = Flowable.fromPublisher(
            httpClient.execute(
                request,
                TypeInfo.Simple(ByteArray::class.java)
            )
        ).blockingFirst()

        return if (result.status != 404)
            TckTestResult.Failure(404, result.status)
        else if (result.body !is ErrorResponse || result.body != checkError)
            TckTestResult.Failure(checkError, result.body)
        else
            TckTestResult.Success
    }
}
