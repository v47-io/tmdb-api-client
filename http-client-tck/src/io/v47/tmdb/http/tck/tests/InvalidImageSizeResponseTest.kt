package io.v47.tmdb.http.tck.tests

import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.utils.TypeInfo

internal class InvalidImageSizeResponseTest : AbstractTckTest("https://image.tmdb.org/t/p") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val checkError = ErrorResponse("Image size not supported", 400)

        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/123456h/wwemzKWzjKYJFfCeiB57q3r4Bcm.svg"
        )

        val result = Flowable.fromPublisher(
            httpClient.execute(
                request,
                TypeInfo.Simple(ByteArray::class.java)
            )
        ).blockingFirst()

        return if (result.status != 400)
            TckTestResult.Failure(400, result.status)
        else if (result.body !is ErrorResponse || result.body != checkError)
            TckTestResult.Failure(checkError, result.body)
        else
            TckTestResult.Success
    }
}
