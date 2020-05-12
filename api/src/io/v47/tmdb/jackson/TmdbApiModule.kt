/**
 * Copyright 2020 The tmdb-api-v2 Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("MaxLineLength")

package io.v47.tmdb.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import io.v47.tmdb.jackson.deserialization.EnumDeserializerModifier
import io.v47.tmdb.jackson.mixins.*
import io.v47.tmdb.model.*

class TmdbApiModule : SimpleModule("TmdbApiModule") {
    init {
        doInit()
    }

    @Suppress("LongMethod")
    private fun doInit() {
        setDeserializerModifier(EnumDeserializerModifier())

        mapOf<Class<out TmdbType>, Class<*>>(
            CollectionDetails::class.java to CollectionDetailsMixin::class.java,
            CollectionDetails.Part::class.java to CollectionDetailsPartMixin::class.java,
            CollectionTranslation::class.java to CollectionTranslationMixin::class.java,

            Company::class.java to CompanyMixin::class.java,

            Configuration::class.java to ConfigurationMixin::class.java,
            Configuration.Images::class.java to ConfigurationImagesMixin::class.java,
            Country::class.java to CountryMixin::class.java,
            Language::class.java to LanguageMixin::class.java,
            Timezones::class.java to TimezonesMixin::class.java,

            Credits::class.java to CreditsMixin::class.java,
            CreditMediaMovie::class.java to CreditMediaMovieMixin::class.java,
            CreditMediaTv::class.java to CreditMediaTvMixin::class.java,
            CreditMediaTvEpisode::class.java to CreditMediaTvEpisodeMixin::class.java,
            CreditMediaTvSeason::class.java to CreditMediaTvSeasonMixin::class.java,
            CreditPerson::class.java to CreditPersonMixin::class.java,
            CreditPersonKnownForMovie::class.java to CreditPersonKnownForMovieMixin::class.java,
            CreditPersonKnownForTv::class.java to CreditPersonKnownForTvMixin::class.java,

            Find::class.java to FindMixin::class.java,

            KeywordMovies::class.java to KeywordMoviesMixin::class.java,

            ListDetails::class.java to ListDetailsMixin::class.java,
            ItemStatus::class.java to ItemStatusMixin::class.java,

            MovieDetails::class.java to MovieDetailsMixin::class.java,
            MovieChanges::class.java to MovieChangesMixin::class.java,
            MovieChanges.ChangeItem::class.java to MovieChangesChangeItemMixin::class.java,
            MovieExternalIds::class.java to MovieExternalIdsMixin::class.java,
            MovieReleaseDates.ReleaseDates::class.java to MovieReleaseDatesReleaseDatesMixin::class.java,
            MovieReleaseDates.ReleaseDates.MovieReleaseInfo::class.java to MovieReleaseDatesReleaseDatesMovieReleaseInfoMixin::class.java,
            MovieReviews::class.java to MovieReviewsMixin::class.java,
            MovieLists::class.java to MovieListsMixin::class.java,

            Network::class.java to NetworkMixin::class.java,

            PaginatedMovieTvPersonListResults::class.java to PaginatedMovieTvPersonListResultsMixin::class.java,

            PersonDetails::class.java to PersonDetailsMixin::class.java,
            PersonExternalIds::class.java to PersonExternalIdsMixin::class.java,
            PersonTaggedImages::class.java to PersonTaggedImagesMixin::class.java,
            PersonTaggedImages.TaggedImage::class.java to PersonTaggedImagesTaggedImageMixin::class.java,
            PersonTranslations.PersonTranslation::class.java to PersonTranslationsPersonTranslationMixin::class.java,
            PersonChanges.PersonChange.PersonChangeItem::class.java to PersonChangesPersonChangePersonChangeItemMixin::class.java,
            PeoplePopular::class.java to PeoplePopularMixin::class.java,
            PeoplePopular.PopularPerson::class.java to PeoplePopularPopularPersonMixin::class.java,

            ReviewDetails::class.java to ReviewDetailsMixin::class.java,

            MovieListResult::class.java to MovieListResultMixin::class.java,
            TvListResult::class.java to TvListResultMixin::class.java,
            TvListResult.TvListNetwork::class.java to TvListResultTvListNetworkMixin::class.java,
            TvListResult.TvListNetwork.TvListNetworkLogo::class.java to TvListResultTvListNetworkTvListNetworkLogoMixin::class.java,
            Title::class.java to TitleMixin::class.java,
            PersonListResult::class.java to PersonListResultMixin::class.java,
            CollectionInfo::class.java to CollectionInfoMixin::class.java,
            CompanyInfo::class.java to CompanyInfoMixin::class.java,
            CreditListResult::class.java to CreditListResultMixin::class.java,
            ImageListResult::class.java to ImageListResultMixin::class.java,
            VideoListResult::class.java to VideoListResultMixin::class.java,
            TranslationListResult::class.java to TranslationListResultMixin::class.java,
            PaginatedListResults::class.java to PaginatedListResultsMixin::class.java,
            PaginatedMovieListResultsWithDates::class.java to PaginatedMovieListResultsWithDatesMixin::class.java,
            CastMember::class.java to CastMemberMixin::class.java,
            CrewMember::class.java to CrewMemberMixin::class.java,

            TmdbType::class.java to TmdbTypeMixin::class.java,

            TvShowDetails::class.java to TvShowDetailsMixin::class.java,
            TvShowChanges.ChangeItem::class.java to TvShowChangesChangeItemMixin::class.java,
            TvShowContentRatings.Rating::class.java to TvShowContentRatingsRatingMixin::class.java,
            TvShowEpisodeGroups.TvShowEpisodeGroup::class.java to TvShowEpisodeGroupsTvShowEpisodeGroupMixin::class.java,
            TvShowExternalIds::class.java to TvShowExternalIdsMixin::class.java,
            TvShowScreenedTheatrically.ScreenedResult::class.java to TvShowScreenTheatricallyScreenedResultMixin::class.java,

            TvEpisodeGroupDetails::class.java to TvEpisodeGroupDetailsMixin::class.java,

            TvEpisodeDetails::class.java to TvEpisodeDetailsMixin::class.java,
            TvEpisodeChanges.ChangeItem::class.java to TvEpisodeChangesChangeItemMixin::class.java,
            TvEpisodeCredits::class.java to TvEpisodeCreditsMixin::class.java,
            TvEpisodeExternalIds::class.java to TvEpisodeExternalIdsMixin::class.java,

            TvSeasonDetails::class.java to TvSeasonDetailsMixin::class.java,
            TvSeasonChanges.ChangeItem::class.java to TvSeasonChangesChangeItemMixin::class.java,
            TvSeasonChanges.ChangeValue::class.java to TvSeasonChangesChangeValueMixin::class.java,
            TvSeasonExternalIds::class.java to TvSeasonExternalIdsMixin::class.java
        ).forEach { (c, m) -> setMixInAnnotation(c, m) }
    }
}

@SinceKotlin("1.3")
fun ObjectMapper.registerTmdbApiModule() {
    registerModule(TmdbApiModule())
}
