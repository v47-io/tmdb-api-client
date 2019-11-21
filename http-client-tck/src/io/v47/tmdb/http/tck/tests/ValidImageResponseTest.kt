package io.v47.tmdb.http.tck.tests

import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.HttpResponse
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.utils.TypeInfo

internal class ValidImageResponseTest : AbstractTckTest("https://image.tmdb.org/t/p") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/{imageSize}/{imagePath}",
            mapOf(
                "imageSize" to "original",
                "imagePath" to "wwemzKWzjKYJFfCeiB57q3r4Bcm.svg"
            )
        )

        lateinit var response: HttpResponse<*>
        val contentLength = Flowable
            .fromPublisher(
                httpClient.execute(
                    request,
                    TypeInfo.Simple(ByteArray::class.java)
                )
            )
            .reduce(0) { acc, resp ->
                response = resp
                acc + (resp.body as ByteArray).size
            }
            .blockingGet()

        return when {
            response.status != 200 -> TckTestResult.Failure(200, response.status)
            contentLength != 2114 -> TckTestResult.Failure(2114, contentLength)
            else -> TckTestResult.Success
        }
    }
}
