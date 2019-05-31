package io.v47.tmdb.http.tck

import io.v47.tmdb.http.HttpClientFactory

internal interface TckTest {
    fun verify(httpClientFactory: HttpClientFactory): TckTestResult
}

internal sealed class TckTestResult {
    object Success : TckTestResult()
    data class Failure(val expectedValue: Any, val actualValue: Any?) : TckTestResult()
}
