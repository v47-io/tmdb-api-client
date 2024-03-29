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
package io.v47.tmdb.api

import io.v47.tmdb.http.get
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.Configuration
import io.v47.tmdb.model.Country
import io.v47.tmdb.model.Jobs
import io.v47.tmdb.model.Language
import io.v47.tmdb.model.Timezones

class ConfigurationApi internal constructor(private val http: HttpExecutor) {
    /**
     * Get the system wide configuration information. Some elements of the API
     * require some knowledge of this configuration data. The purpose of this
     * is to try and keep the actual API responses as light as possible. It is
     * recommended you cache this data within your application and check for
     * updates every few days.
     *
     * This method currently holds the data relevant to building image URLs as
     * well as the change key map.
     *
     * To build an image URL, you will need 3 pieces of data. The `base_url`, `size`
     * and `file_path`. Simply combine them all and you will have a fully qualified
     * URL. Here’s an example URL:
     *
     * ```
     * https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg
     * ```
     *
     * The configuration method also contains the list of change keys which can be
     * useful if you are building an app that consumes data from the change feed
     */
    fun system() = http.get<Configuration>("/configuration")

    /**
     * Get the list of countries (ISO 3166-1 tags) used throughout TMDb
     */
    fun countries() = http.get<List<Country>>("/configuration/countries")

    /**
     * Get a list of the jobs and departments we use on TMDb
     */
    fun jobs() = http.get<List<Jobs>>("/configuration/jobs")

    /**
     * Get the list of languages (ISO 639-1 tags) used throughout TMDb
     */
    fun languages() = http.get<List<Language>>("/configuration/languages")

    /**
     * Get a list of the _officially_ supported translations on TMDb.
     *
     * While it's technically possible to add a translation in any one of the
     * [languages](https://developers.themoviedb.org/3/configuration/get-languages)
     * we have added to TMDb (we don't restrict content), the ones listed in this
     * method are the ones we also support for localizing the website with which
     * means they are what we refer to as the "primary" translations.
     *
     * These are all specified as
     * [IETF tags](https://en.wikipedia.org/wiki/IETF_language_tag) to identify the
     * languages we use on TMDb. There is one exception which is image languages.
     * They are currently only designated by a ISO-639-1 tag. This is a planned
     * upgrade for the future.
     *
     * We're always open to adding more if you think one should be added. You can ask
     * about getting a new primary translation added by posting on
     * [the forums](https://www.themoviedb.org/talk/category/5047951f760ee3318900009a).
     *
     * One more thing to mention, these are the translations that map to our website
     * translation project. You can view and contribute to that project
     * [here](https://www.localeapp.com/projects/8267)
     */
    fun primaryTranslations() = http.get<List<String>>("/configuration/primary_translations")

    /**
     * Get the list of timezones used throughout TMDb
     */
    fun timezones() = http.get<List<Timezones>>("/configuration/timezones")
}
