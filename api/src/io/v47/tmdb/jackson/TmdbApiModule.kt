/**
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@file:Suppress("MaxLineLength")

package io.v47.tmdb.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import io.v47.tmdb.jackson.deserialization.EnumDeserializerModifier
import io.v47.tmdb.jackson.mixins.CastMemberMixin
import io.v47.tmdb.jackson.mixins.CollectionDetailsMixin
import io.v47.tmdb.jackson.mixins.CollectionDetailsPartMixin
import io.v47.tmdb.jackson.mixins.CollectionInfoMixin
import io.v47.tmdb.jackson.mixins.CollectionTranslationMixin
import io.v47.tmdb.jackson.mixins.CompanyInfoMixin
import io.v47.tmdb.jackson.mixins.CompanyMixin
import io.v47.tmdb.jackson.mixins.ConfigurationImagesMixin
import io.v47.tmdb.jackson.mixins.ConfigurationMixin
import io.v47.tmdb.jackson.mixins.CountryMixin
import io.v47.tmdb.jackson.mixins.CreditListResultMixin
import io.v47.tmdb.jackson.mixins.CreditMediaMovieMixin
import io.v47.tmdb.jackson.mixins.CreditMediaTvEpisodeMixin
import io.v47.tmdb.jackson.mixins.CreditMediaTvMixin
import io.v47.tmdb.jackson.mixins.CreditMediaTvSeasonMixin
import io.v47.tmdb.jackson.mixins.CreditPersonKnownForMovieMixin
import io.v47.tmdb.jackson.mixins.CreditPersonKnownForTvMixin
import io.v47.tmdb.jackson.mixins.CreditPersonMixin
import io.v47.tmdb.jackson.mixins.CreditsMixin
import io.v47.tmdb.jackson.mixins.CrewMemberMixin
import io.v47.tmdb.jackson.mixins.FindMixin
import io.v47.tmdb.jackson.mixins.ImageListResultMixin
import io.v47.tmdb.jackson.mixins.ItemStatusMixin
import io.v47.tmdb.jackson.mixins.KeywordMoviesMixin
import io.v47.tmdb.jackson.mixins.LanguageMixin
import io.v47.tmdb.jackson.mixins.ListDetailsMixin
import io.v47.tmdb.jackson.mixins.MovieChangesChangeItemMixin
import io.v47.tmdb.jackson.mixins.MovieChangesMixin
import io.v47.tmdb.jackson.mixins.MovieDetailsMixin
import io.v47.tmdb.jackson.mixins.MovieExternalIdsMixin
import io.v47.tmdb.jackson.mixins.MovieListResultMixin
import io.v47.tmdb.jackson.mixins.MovieListsMixin
import io.v47.tmdb.jackson.mixins.MovieReleaseDatesReleaseDatesMixin
import io.v47.tmdb.jackson.mixins.MovieReleaseDatesReleaseDatesMovieReleaseInfoMixin
import io.v47.tmdb.jackson.mixins.MovieReviewsMixin
import io.v47.tmdb.jackson.mixins.NetworkMixin
import io.v47.tmdb.jackson.mixins.PaginatedListResultsMixin
import io.v47.tmdb.jackson.mixins.PaginatedMovieListResultsWithDatesMixin
import io.v47.tmdb.jackson.mixins.PaginatedMovieTvPersonListResultsMixin
import io.v47.tmdb.jackson.mixins.PeoplePopularMixin
import io.v47.tmdb.jackson.mixins.PeoplePopularPopularPersonMixin
import io.v47.tmdb.jackson.mixins.PersonChangesPersonChangePersonChangeItemMixin
import io.v47.tmdb.jackson.mixins.PersonDetailsMixin
import io.v47.tmdb.jackson.mixins.PersonExternalIdsMixin
import io.v47.tmdb.jackson.mixins.PersonListResultMixin
import io.v47.tmdb.jackson.mixins.PersonTaggedImagesMixin
import io.v47.tmdb.jackson.mixins.PersonTaggedImagesTaggedImageMixin
import io.v47.tmdb.jackson.mixins.PersonTranslationsPersonTranslationMixin
import io.v47.tmdb.jackson.mixins.ReviewAuthorDetailsMixin
import io.v47.tmdb.jackson.mixins.ReviewDetailsMixin
import io.v47.tmdb.jackson.mixins.TimezonesMixin
import io.v47.tmdb.jackson.mixins.TitleMixin
import io.v47.tmdb.jackson.mixins.TmdbTypeMixin
import io.v47.tmdb.jackson.mixins.TranslationListResultMixin
import io.v47.tmdb.jackson.mixins.TvEpisodeChangesChangeItemMixin
import io.v47.tmdb.jackson.mixins.TvEpisodeCreditsMixin
import io.v47.tmdb.jackson.mixins.TvEpisodeDetailsMixin
import io.v47.tmdb.jackson.mixins.TvEpisodeExternalIdsMixin
import io.v47.tmdb.jackson.mixins.TvEpisodeGroupDetailsMixin
import io.v47.tmdb.jackson.mixins.TvListResultMixin
import io.v47.tmdb.jackson.mixins.TvListResultTvListNetworkMixin
import io.v47.tmdb.jackson.mixins.TvListResultTvListNetworkTvListNetworkLogoMixin
import io.v47.tmdb.jackson.mixins.TvSeasonChangesChangeItemMixin
import io.v47.tmdb.jackson.mixins.TvSeasonChangesChangeValueMixin
import io.v47.tmdb.jackson.mixins.TvSeasonDetailsMixin
import io.v47.tmdb.jackson.mixins.TvSeasonExternalIdsMixin
import io.v47.tmdb.jackson.mixins.TvShowChangesChangeItemMixin
import io.v47.tmdb.jackson.mixins.TvShowContentRatingsRatingMixin
import io.v47.tmdb.jackson.mixins.TvShowDetailsMixin
import io.v47.tmdb.jackson.mixins.TvShowEpisodeGroupsTvShowEpisodeGroupMixin
import io.v47.tmdb.jackson.mixins.TvShowExternalIdsMixin
import io.v47.tmdb.jackson.mixins.TvShowReviewMixin
import io.v47.tmdb.jackson.mixins.TvShowScreenTheatricallyScreenedResultMixin
import io.v47.tmdb.jackson.mixins.VideoListResultMixin
import io.v47.tmdb.model.CastMember
import io.v47.tmdb.model.CollectionDetails
import io.v47.tmdb.model.CollectionInfo
import io.v47.tmdb.model.CollectionTranslation
import io.v47.tmdb.model.Company
import io.v47.tmdb.model.CompanyInfo
import io.v47.tmdb.model.Configuration
import io.v47.tmdb.model.Country
import io.v47.tmdb.model.CreditListResult
import io.v47.tmdb.model.CreditMediaMovie
import io.v47.tmdb.model.CreditMediaTv
import io.v47.tmdb.model.CreditMediaTvEpisode
import io.v47.tmdb.model.CreditMediaTvSeason
import io.v47.tmdb.model.CreditPerson
import io.v47.tmdb.model.CreditPersonKnownForMovie
import io.v47.tmdb.model.CreditPersonKnownForTv
import io.v47.tmdb.model.Credits
import io.v47.tmdb.model.CrewMember
import io.v47.tmdb.model.Find
import io.v47.tmdb.model.ImageListResult
import io.v47.tmdb.model.ItemStatus
import io.v47.tmdb.model.KeywordMovies
import io.v47.tmdb.model.Language
import io.v47.tmdb.model.ListDetails
import io.v47.tmdb.model.MovieChanges
import io.v47.tmdb.model.MovieDetails
import io.v47.tmdb.model.MovieExternalIds
import io.v47.tmdb.model.MovieListResult
import io.v47.tmdb.model.MovieLists
import io.v47.tmdb.model.MovieReleaseDates
import io.v47.tmdb.model.MovieReviews
import io.v47.tmdb.model.Network
import io.v47.tmdb.model.PaginatedListResults
import io.v47.tmdb.model.PaginatedMovieListResultsWithDates
import io.v47.tmdb.model.PaginatedMovieTvPersonListResults
import io.v47.tmdb.model.PeoplePopular
import io.v47.tmdb.model.PersonChanges
import io.v47.tmdb.model.PersonDetails
import io.v47.tmdb.model.PersonExternalIds
import io.v47.tmdb.model.PersonListResult
import io.v47.tmdb.model.PersonTaggedImages
import io.v47.tmdb.model.PersonTranslations
import io.v47.tmdb.model.ReviewAuthorDetails
import io.v47.tmdb.model.ReviewDetails
import io.v47.tmdb.model.Timezones
import io.v47.tmdb.model.Title
import io.v47.tmdb.model.TmdbType
import io.v47.tmdb.model.TranslationListResult
import io.v47.tmdb.model.TvEpisodeChanges
import io.v47.tmdb.model.TvEpisodeCredits
import io.v47.tmdb.model.TvEpisodeDetails
import io.v47.tmdb.model.TvEpisodeExternalIds
import io.v47.tmdb.model.TvEpisodeGroupDetails
import io.v47.tmdb.model.TvListResult
import io.v47.tmdb.model.TvSeasonChanges
import io.v47.tmdb.model.TvSeasonDetails
import io.v47.tmdb.model.TvSeasonExternalIds
import io.v47.tmdb.model.TvShowChanges
import io.v47.tmdb.model.TvShowContentRatings
import io.v47.tmdb.model.TvShowDetails
import io.v47.tmdb.model.TvShowEpisodeGroups
import io.v47.tmdb.model.TvShowExternalIds
import io.v47.tmdb.model.TvShowReview
import io.v47.tmdb.model.TvShowScreenedTheatrically
import io.v47.tmdb.model.VideoListResult

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
            ReviewAuthorDetails::class.java to ReviewAuthorDetailsMixin::class.java,

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
            TvShowReview::class.java to TvShowReviewMixin::class.java,

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
