package io.v47.tmdb.http.tck

import io.v47.tmdb.http.HttpClientFactory
import io.v47.tmdb.http.tck.tests.*

class HttpClientTck {
    private val tests = listOf<TckTest>(
        ValidSimpleResponseTest(),
        ValidComplexResponseTest(),
        AuthErrorResponseTest(),
        NotFoundErrorResponseTest(),
        ValidImageResponseTest(),
        ImageNotFoundResponseTest(),
        InvalidImageSizeResponseTest()
    )

    fun verify(httpClientFactory: HttpClientFactory): TckResult {
        val failures = mutableListOf<TckResult.Failure.FailedTest>()
        tests.forEach { test ->
            val testResult = test.verify(httpClientFactory)
            if (testResult is TckTestResult.Failure)
                failures += TckResult.Failure.FailedTest(
                    test.javaClass.canonicalName,
                    testResult.expectedValue,
                    testResult.actualValue
                )
        }

        return if (failures.isEmpty())
            TckResult.Success
        else
            TckResult.Failure(failures)
    }
}

sealed class TckResult {
    object Success : TckResult()
    data class Failure(val failedTests: List<FailedTest>) : TckResult() {
        data class FailedTest(
            val name: String,
            val expectedValue: Any,
            val actualValue: Any?
        )
    }
}
