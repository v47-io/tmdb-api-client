package io.v47.tmdb.api

import com.neovisionaries.i18n.LocaleCode
import io.v47.tmdb.http.get
import io.v47.tmdb.http.getWithLanguage
import io.v47.tmdb.http.getWithPage
import io.v47.tmdb.http.impl.HttpExecutor
import io.v47.tmdb.model.*
import io.v47.tmdb.utils.dateFormat
import java.time.LocalDate

class TvEpisodesApi internal constructor(private val httpExecutor: HttpExecutor) {
    /**
     * Get the TV episode details by id.
     *
     * Supports `appendToResponse` using the enumeration [TvEpisodeRequest] (More information
     * [here](https://developers.themoviedb.org/3/getting-started/append-to-response))
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     * @param language A language code
     * @param append Other requests to append to this call
     */
    fun details(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: LocaleCode? = null,
        vararg append: TvEpisodeRequest
    ) =
        httpExecutor.execute(
            getWithLanguage<TvEpisodeDetails>(
                "/tv/$tvId/season/$seasonNumber/episode/$episodeNumber",
                language
            ) {
                append.forEach { req ->
                    queryArg("append_to_response", req.value)
                }
            }
        )

    /**
     * Get the changes for a TV episode. By default only the last 24 hours are returned.
     *
     * You can query up to 14 days in a single query by using the `startDate` and
     * `endDate` query parameters
     *
     * @param episodeId The id of the TV episode
     * @param startDate Filter the results with a start date
     * @param endDate Filter the results with an end date
     * @param page Specify which page to query
     */
    fun changes(
        episodeId: Int,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int? = null
    ) =
        httpExecutor.execute(
            getWithPage<TvEpisodeChanges>("/tv/episode/$episodeId/changes", page) {
                startDate?.let { queryArg("start_date", it.format(dateFormat)) }
                endDate?.let { queryArg("end_date", it.format(dateFormat)) }
            }
        )

    /**
     * Get the credits (cast, crew and guest stars) for a TV episode
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     */
    fun credits(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: LocaleCode? = null
    ) =
        httpExecutor.execute(
            getWithLanguage<TvEpisodeCredits>(
                "/tv/$tvId/season/$seasonNumber/episode/$episodeNumber/credits",
                language
            )
        )

    /**
     * Get the external ids for a TV episode.
     *
     * Look [here](https://developers.themoviedb.org/3/tv-seasons/get-tv-episode-external-ids)
     * for a list of supported external sources
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     */
    fun externalIds(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) =
        httpExecutor.execute(
            get<TvEpisodeExternalIds>("/tv/$tvId/season/$seasonNumber/episode/$episodeNumber/external_ids")
        )

    /**
     * Get the images that belong to a TV episode.
     *
     * Querying images with a `language` parameter will filter the results. If you
     * want to include a fallback language (especially useful for backdrops) you
     * can use the `includeImageLanguage` parameter. This should be a comma seperated
     * value like so: `en,null`
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     * @param language A language code
     * @param includeLanguage A list of language codes to filter the results
     */
    fun images(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: LocaleCode? = null,
        vararg includeLanguage: LocaleCode?
    ) =
        httpExecutor.execute(
            getWithLanguage<TvEpisodeImages>("/tv/$tvId/season/$seasonNumber/episode/$episodeNumber/images", language) {
                includeLanguage.toSet().forEach { lang ->
                    queryArg("include_image_language", lang?.toString() ?: "null")
                }
            }
        )

    /**
     * Get the translation data for an episode
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     */
    fun translations(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) =
        httpExecutor.execute(
            get<TvEpisodeTranslations>("/tv/$tvId/season/$seasonNumber/episode/$episodeNumber/translations")
        )

    /**
     * Get the videos that have been added to a TV episode
     *
     * @param tvId The id of the TV show
     * @param seasonNumber The number of the season
     * @param episodeNumber The number of the episode
     * @param language A language code
     */
    fun videos(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: LocaleCode? = null
    ) =
        httpExecutor.execute(
            getWithLanguage<TvEpisodeVideos>("/tv/$tvId/season/$seasonNumber/episode/$episodeNumber/videos", language)
        )
}

enum class TvEpisodeRequest(internal val value: String) {
    Changes("changes"),
    Credits("credits"),
    ExternalIds("external_ids"),
    Images("images"),
    Translations("translations"),
    Videos("videos")
}
