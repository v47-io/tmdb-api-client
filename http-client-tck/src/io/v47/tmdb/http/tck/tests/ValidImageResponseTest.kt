package io.v47.tmdb.http.tck.tests

import io.reactivex.Flowable
import io.v47.tmdb.http.HttpClient
import io.v47.tmdb.http.HttpMethod
import io.v47.tmdb.http.impl.HttpRequestImpl
import io.v47.tmdb.http.tck.TckTestResult
import io.v47.tmdb.utils.TypeInfo

internal class ValidImageResponseTest : AbstractTckTest("https://image.tmdb.org/t/p") {
    override fun doVerify(httpClient: HttpClient): TckTestResult {
        val request = HttpRequestImpl(
            HttpMethod.Get,
            "/original/wwemzKWzjKYJFfCeiB57q3r4Bcm.svg"
        )

        val result = Flowable.fromPublisher(
            httpClient.execute(
                request,
                TypeInfo.Simple(ByteArray::class.java)
            )
        ).blockingFirst()

        val contentLengthHeader = result.headers["Content-Length"]?.firstOrNull()?.toIntOrNull()
        return when {
            result.status != 200 -> TckTestResult.Failure(200, result.status)
            contentLengthHeader != 2114 -> TckTestResult.Failure(2114, result.status)
            else -> TckTestResult.Success
        }
    }
}
