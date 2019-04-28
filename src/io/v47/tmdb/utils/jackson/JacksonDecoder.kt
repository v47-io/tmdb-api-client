@file:Suppress("BlockingMethodInNonBlockingContext")

package io.v47.tmdb.utils.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.async.ByteArrayFeeder
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.TokenBuffer
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.io.IOException
import java.lang.reflect.ParameterizedType

internal class JacksonDecoder(private val objectMapper: ObjectMapper) {
    fun <T : Any> decode(input: Publisher<ByteArray>, resultType: TypeReference<T>): Publisher<T> {
        val parseAsArray = isIterableType(resultType)
        val tokens = tokenize(input, parseAsArray)

        val reader = objectMapper.readerFor(resultType)

        @Suppress("TooGenericExceptionCaught")
        return Flowable.fromPublisher(tokens).flatMap { tokenBuffer ->
            try {
                Flowable.just(reader.readValue(tokenBuffer.asParser(objectMapper)) as T)
            } catch (x: Throwable) {
                Flowable.error<T>(x)
            }
        }
    }

    private fun tokenize(input: Publisher<ByteArray>, parseAsArray: Boolean): Publisher<TokenBuffer> {
        val parser = objectMapper.factory.createNonBlockingByteArrayParser()
        val tokenizer = JacksonTokenizer(parser, parseAsArray)



        return Flowable.fromPublisher(input)
                .flatMap(tokenizer::tokenize, tokenizer::error, tokenizer::endOfInput)
    }

    private fun isIterableType(typeRef: TypeReference<*>): Boolean {
        val baseType = when {
            typeRef.type is Class<*> -> typeRef.type as Class<*>
            typeRef.type is ParameterizedType -> (typeRef.type as ParameterizedType).rawType as Class<*>
            else -> null
        }

        return baseType != null && java.lang.Iterable::class.java.isAssignableFrom(baseType)
    }
}

private class JacksonTokenizer(private val parser: JsonParser, private val parseAsArray: Boolean = false) {
    private var tokenBuffer = TokenBuffer(parser)
    private val inputFeeder = parser.nonBlockingInputFeeder as ByteArrayFeeder

    private var arrayDepth = 0
    private var objectDepth = 0

    fun tokenize(bytes: ByteArray): Publisher<TokenBuffer> {
        return try {
            inputFeeder.feedInput(bytes, 0, bytes.size)
            parseTokenBuffer()
        } catch (x: JsonProcessingException) {
            error(x)
        } catch (x: IOException) {
            error(x)
        }
    }

    fun endOfInput(): Publisher<TokenBuffer> {
        this.inputFeeder.endOfInput()
        return try {
            parseTokenBuffer()
        } catch (x: JsonProcessingException) {
            error(x)
        } catch (x: IOException) {
            error(x)
        }
    }

    fun error(throwable: Throwable): Publisher<TokenBuffer> =
            Flowable.error(throwable)

    private fun parseTokenBuffer(): Publisher<TokenBuffer> {
        val result = mutableListOf<TokenBuffer>()

        while (true) {
            val token = parser.nextToken()
            if (token == null || token == JsonToken.NOT_AVAILABLE)
                break

            updateDepth(token)

            processToken(token, result)
        }

        return Flowable.fromIterable(result)
    }

    private fun updateDepth(token: JsonToken) {
        when (token) {
            JsonToken.START_ARRAY -> arrayDepth++
            JsonToken.END_ARRAY -> arrayDepth--
            JsonToken.START_OBJECT -> objectDepth++
            JsonToken.END_OBJECT -> objectDepth--
            else -> Unit
        }
    }

    @Suppress("ComplexCondition")
    private fun processToken(token: JsonToken, result: MutableList<TokenBuffer>) {
        if (parseAsArray || arrayDepth != 1 || token != JsonToken.START_ARRAY)
            tokenBuffer.copyCurrentEvent(parser)

        if (
                (parseAsArray && token == JsonToken.END_ARRAY && arrayDepth == 0 && objectDepth == 0) ||
                (((!parseAsArray && token == JsonToken.END_OBJECT) || token.isScalarValue) &&
                        objectDepth == 0 && (arrayDepth == 0 || arrayDepth == 1))
        ) {
            result.add(tokenBuffer)
            tokenBuffer = TokenBuffer(parser)
        }
    }
}
