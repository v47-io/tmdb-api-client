/**
 * The Clear BSD License
 *
 * Copyright (c) 2022 the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.utils

import io.v47.tmdb.http.TypeInfo
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

inline fun <reified T : Any> tmdbTypeReference() = object : TmdbTypeReference<T>() {}

abstract class TmdbTypeReference<T : Any> protected constructor() :
    Comparable<TmdbTypeReference<T>> {

    val type: Type

    init {
        val superClass = javaClass.genericSuperclass
        require(superClass !is Class<*>) { "Internal error: TypeReference constructed without actual type information" }

        type = (superClass as ParameterizedType).actualTypeArguments[0]
    }

    override fun compareTo(other: TmdbTypeReference<T>) = 0
}

fun TmdbTypeReference<*>.toTypeInfo() = type.toTypeInfo()

private fun Type.toTypeInfo(): TypeInfo =
    when (this) {
        is Class<*> -> toTypeInfo()
        is ParameterizedType -> toTypeInfo()
        is WildcardType -> when {
            upperBounds.size == 1 -> upperBounds[0].toTypeInfo()
            lowerBounds.size == 1 -> lowerBounds[0].toTypeInfo()
            else -> throw IllegalArgumentException("Cannot convert wildcard type without singular bound!")
        }

        else -> throw IllegalArgumentException("Cannot convert ${javaClass.canonicalName} into TypeInfo!")
    }

private fun ParameterizedType.toTypeInfo(): TypeInfo {
    val typeArgumentInfos = actualTypeArguments.map { it.toTypeInfo() }

    return if (typeArgumentInfos.isNotEmpty())
        TypeInfo.Generic(
            rawType as Class<*>,
            typeArgumentInfos,
            this
        )
    else
        TypeInfo.Simple(rawType as Class<*>)
}

private fun Class<*>.toTypeInfo(): TypeInfo = TypeInfo.Simple(this)
