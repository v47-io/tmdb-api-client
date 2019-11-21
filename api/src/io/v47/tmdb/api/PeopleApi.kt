package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.get
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.getWithPage
import io.v47.tmdb.http.getWithPageAndLanguage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.*
import io.v47.tmdb.utils.dateFormat
import java.time.LocalDate

@Suppress("TooManyFunctions")
class PeopleApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get the primary person details by id.
     *
     * Supports `appendToResponse` using the enumeration [PeopleRequest] (More information
     * [here](https://developers.themoviedb.org/3/getting-started/append-to-response))
     *
     * @param personId The id of the person
     * @param language A language code
     * @param append Other requests to append to this call
     */
    fun details(personId: Int, language: LocaleCode? = null, vararg append: PeopleRequest) =
        httpExecutor.execute(
            getWithLanguage<PersonDetails>("/person/{personId}", language) {
                pathVar("personId", personId)

                append.forEach { req ->
                    queryArg("append_to_response", req.value)
                }
            }
        )

    /**
     * Get the changes for a person. By default only the last 24 hours are returned.
     *
     * You can query up to 14 days in a single query by using the `startDate` and `endDate` parameters
     *
     * @param personId The id of the person
     * @param startDate Filter the results with a start date
     * @param endDate Filter the results with a end date
     * @param page Specify which page to query
     */
    fun changes(
        personId: Int,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int? = null
    ) =
        httpExecutor.execute(
            getWithPage<PersonChanges>("/person/{personId}/changes", page) {
                pathVar("personId", personId)

                startDate?.let { queryArg("start_date", it.format(dateFormat)) }
                endDate?.let { queryArg("end_date", it.format(dateFormat)) }
            }
        )

    /**
     * Get the movie credits for a person.
     *
     * You can query for some extra details about the credit with the method [CreditApi.details]
     *
     * @param personId The id of the person
     * @param language A language code
     */
    fun movieCredits(personId: Int, language: LocaleCode? = null) =
        credits("movie", personId, language)

    /**
     * Get the TV show credits for a person.
     *
     * You can query for some extra details about the credit with the method [CreditApi.details]
     *
     * @param personId The id of the person
     * @param language A language code
     */
    fun tvCredits(personId: Int, language: LocaleCode? = null) =
        credits("tv", personId, language)

    /**
     * Get the movie and TV credits together in a single response.
     *
     * You can query for some extra details about the credit with the method [CreditApi.details]
     *
     * @param personId The id of the person
     * @param language A language code
     */
    fun combinedCredits(personId: Int, language: LocaleCode? = null) =
        credits("combined", personId, language)

    private fun credits(type: String, personId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithLanguage<PersonCredits>("/person/{personId}/${type}_credits", language) {
                pathVar("personId", personId)
            }
        )

    /**
     * Get the external ids for a person. We currently support the following external sources.
     *
     * - IMDB ID
     * - Facebook
     * - Freebase MID
     * - Freebase ID
     * - Instagram
     * - TVRage ID
     * - Twitter
     *
     * @param personId The id of the person
     */
    fun externalIds(personId: Int) =
        httpExecutor.execute(
            get<PersonExternalIds>("/person/{personId}/external_ids") {
                pathVar("personId", personId)
            }
        )

    /**
     * Get the images for a person
     *
     * @param personId The id of the person
     */
    fun images(personId: Int) =
        httpExecutor.execute(
            get<PersonImages>("/person/{personId}/images") {
                pathVar("personId", personId)
            }
        )

    /**
     * Get the images that this person has been tagged in
     *
     * @param personId The id of the person
     * @param language A language code
     * @param page Specify which page to query
     */
    fun taggedImages(personId: Int, page: Int? = null, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithPageAndLanguage<PersonTaggedImages>(
                "/person/{personId}/tagged_images",
                page,
                language
            ) {
                pathVar("personId", personId)
            }
        )

    /**
     * Get a list of translations that have been created for a person
     *
     * @param personId The id of the person
     * @param language A language code
     */
    fun translations(personId: Int, language: LocaleCode? = null) =
        httpExecutor.execute(
            getWithLanguage<PersonTranslations>("/person/{personId}/translations", language) {
                pathVar("personId", personId)
            }
        )

    /**
     * Get the most newly created person. This is a live response and will continuously change
     *
     * @param language A language code
     */
    fun latest(language: LocaleCode? = null) =
        httpExecutor.execute(getWithLanguage<PersonDetails>("/person/latest", language))

    /**
     * Get the list of popular people on TMDb. This list updates daily
     *
     * @param language A language code
     * @param page Specify which page to query
     */
    fun popular(page: Int? = null, language: LocaleCode? = null) =
        httpExecutor.execute(getWithPageAndLanguage<PeoplePopular>("/person/popular", page, language))
}

enum class PeopleRequest(internal val value: String) {
    Changes("changes"),
    MovieCredits("movie_credits"),
    TvCredits("tv_credits"),
    CombinedCredits("combined_credits"),
    ExternalIds("external_ids"),
    Images("images"),
    TaggedImages("tagged_images"),
    Translations("translations")
}
