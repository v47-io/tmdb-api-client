/**
 * The Clear BSD License
 *
 * Copyright (c) 2023, the tmdb-api-client authors
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
package io.v47.tmdb.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import io.v47.tmdb.jackson.deserialization.CreditMediaDeserializer
import io.v47.tmdb.jackson.deserialization.CreditPersonKnownForDeserializer
import io.v47.tmdb.jackson.deserialization.ImageSizeDeserializer
import io.v47.tmdb.jackson.deserialization.MovieTvPersonListResultDeserializer
import io.v47.tmdb.jackson.deserialization.TmdbDeserializerModifier
import io.v47.tmdb.jackson.serialization.ImageSizeSerializer
import io.v47.tmdb.model.CreditMedia
import io.v47.tmdb.model.CreditPersonKnownFor
import io.v47.tmdb.model.ImageSize
import io.v47.tmdb.model.MovieTvPersonListResult

class TmdbApiModule : SimpleModule("TmdbApiModule") {
    init {
        setDeserializerModifier(TmdbDeserializerModifier())

        addDeserializer(CreditMedia::class.java, CreditMediaDeserializer())
        addDeserializer(CreditPersonKnownFor::class.java, CreditPersonKnownForDeserializer())
        addDeserializer(ImageSize::class.java, ImageSizeDeserializer())
        addDeserializer(MovieTvPersonListResult::class.java, MovieTvPersonListResultDeserializer())

        addSerializer(ImageSizeSerializer())
    }

}

@SinceKotlin("1.3")
fun ObjectMapper.registerTmdbApiModule() {
    registerModule(TmdbApiModule())
}
