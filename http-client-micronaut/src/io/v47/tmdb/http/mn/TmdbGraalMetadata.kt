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
package io.v47.tmdb.http.mn

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.TypeHint
import io.micronaut.core.annotation.TypeHint.AccessType
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.model.CastMember
import io.v47.tmdb.model.Certification
import io.v47.tmdb.model.Change
import io.v47.tmdb.model.CollectionDetails
import io.v47.tmdb.model.CollectionImages
import io.v47.tmdb.model.CollectionInfo
import io.v47.tmdb.model.CollectionTranslation
import io.v47.tmdb.model.CollectionTranslationData
import io.v47.tmdb.model.CollectionTranslations
import io.v47.tmdb.model.Company
import io.v47.tmdb.model.CompanyAlternativeNameResult
import io.v47.tmdb.model.CompanyAlternativeNames
import io.v47.tmdb.model.CompanyImages
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
import io.v47.tmdb.model.Genre
import io.v47.tmdb.model.GenreList
import io.v47.tmdb.model.GuestSession
import io.v47.tmdb.model.ImageListResult
import io.v47.tmdb.model.ItemStatus
import io.v47.tmdb.model.Jobs
import io.v47.tmdb.model.Keyword
import io.v47.tmdb.model.KeywordMovies
import io.v47.tmdb.model.Language
import io.v47.tmdb.model.ListDetails
import io.v47.tmdb.model.MovieAlternativeTitles
import io.v47.tmdb.model.MovieChanges
import io.v47.tmdb.model.MovieCredits
import io.v47.tmdb.model.MovieDetails
import io.v47.tmdb.model.MovieExternalIds
import io.v47.tmdb.model.MovieImages
import io.v47.tmdb.model.MovieKeywords
import io.v47.tmdb.model.MovieListResult
import io.v47.tmdb.model.MovieLists
import io.v47.tmdb.model.MovieReleaseDates
import io.v47.tmdb.model.MovieReviews
import io.v47.tmdb.model.MovieTranslations
import io.v47.tmdb.model.MovieVideos
import io.v47.tmdb.model.Network
import io.v47.tmdb.model.NetworkAlternativeNameResult
import io.v47.tmdb.model.NetworkAlternativeNames
import io.v47.tmdb.model.NetworkImages
import io.v47.tmdb.model.PaginatedListResults
import io.v47.tmdb.model.PaginatedMovieListResultsWithDates
import io.v47.tmdb.model.PeoplePopular
import io.v47.tmdb.model.PersonChanges
import io.v47.tmdb.model.PersonCredits
import io.v47.tmdb.model.PersonDetails
import io.v47.tmdb.model.PersonExternalIds
import io.v47.tmdb.model.PersonImages
import io.v47.tmdb.model.PersonListResult
import io.v47.tmdb.model.PersonTaggedImages
import io.v47.tmdb.model.PersonTranslations
import io.v47.tmdb.model.ReviewAuthorDetails
import io.v47.tmdb.model.ReviewDetails
import io.v47.tmdb.model.Timezones
import io.v47.tmdb.model.Title
import io.v47.tmdb.model.TranslationListResult
import io.v47.tmdb.model.TvEpisodeChanges
import io.v47.tmdb.model.TvEpisodeCredits
import io.v47.tmdb.model.TvEpisodeDetails
import io.v47.tmdb.model.TvEpisodeExternalIds
import io.v47.tmdb.model.TvEpisodeGroupDetails
import io.v47.tmdb.model.TvEpisodeGroupType
import io.v47.tmdb.model.TvEpisodeImages
import io.v47.tmdb.model.TvEpisodeListResult
import io.v47.tmdb.model.TvEpisodeTranslations
import io.v47.tmdb.model.TvEpisodeVideos
import io.v47.tmdb.model.TvListResult
import io.v47.tmdb.model.TvSeasonChanges
import io.v47.tmdb.model.TvSeasonCredits
import io.v47.tmdb.model.TvSeasonDetails
import io.v47.tmdb.model.TvSeasonExternalIds
import io.v47.tmdb.model.TvSeasonImages
import io.v47.tmdb.model.TvSeasonVideos
import io.v47.tmdb.model.TvShowAlternativeTitles
import io.v47.tmdb.model.TvShowChanges
import io.v47.tmdb.model.TvShowContentRatings
import io.v47.tmdb.model.TvShowCredits
import io.v47.tmdb.model.TvShowDetails
import io.v47.tmdb.model.TvShowEpisodeGroups
import io.v47.tmdb.model.TvShowExternalIds
import io.v47.tmdb.model.TvShowImages
import io.v47.tmdb.model.TvShowKeywords
import io.v47.tmdb.model.TvShowReview
import io.v47.tmdb.model.TvShowScreenedTheatrically
import io.v47.tmdb.model.TvShowTranslations
import io.v47.tmdb.model.TvShowVideos
import io.v47.tmdb.model.VideoListResult

@TypeHint(
    value = [
        GuestSession::class,
        Certification::class,
        Change::class,
        CollectionDetails::class,
        CollectionDetails.Part::class,
        CollectionImages::class,
        CollectionTranslations::class,
        CollectionTranslation::class,
        CollectionTranslationData::class,
        Company::class,
        CompanyAlternativeNames::class,
        CompanyAlternativeNameResult::class,
        CompanyImages::class,
        Configuration::class,
        Configuration.Images::class,
        Country::class,
        Jobs::class,
        Language::class,
        Timezones::class,
        Credits::class,
        CreditMediaMovie::class,
        CreditMediaTv::class,
        CreditMediaTvEpisode::class,
        CreditMediaTvSeason::class,
        CreditPerson::class,
        CreditPersonKnownForMovie::class,
        CreditPersonKnownForTv::class,
        Find::class,
        GenreList::class,
        Keyword::class,
        KeywordMovies::class,
        ListDetails::class,
        ItemStatus::class,
        MovieDetails::class,
        MovieAlternativeTitles::class,
        MovieChanges::class,
        MovieChanges.Change::class,
        MovieChanges.ChangeItem::class,
        MovieCredits::class,
        MovieExternalIds::class,
        MovieImages::class,
        MovieKeywords::class,
        MovieReleaseDates::class,
        MovieReleaseDates.ReleaseDates::class,
        MovieReleaseDates.ReleaseDates.MovieReleaseInfo::class,
        MovieVideos::class,
        MovieTranslations::class,
        MovieReviews::class,
        MovieLists::class,
        Network::class,
        NetworkAlternativeNames::class,
        NetworkAlternativeNameResult::class,
        NetworkImages::class,
        PaginatedMovieListResultsWithDates::class,
        PersonDetails::class,
        PersonCredits::class,
        PersonChanges::class,
        PersonExternalIds::class,
        PersonImages::class,
        PersonTaggedImages::class,
        PersonTaggedImages.TaggedImage::class,
        PersonTranslations::class,
        PersonTranslations.PersonTranslation::class,
        PersonTranslations.PersonTranslation.PersonTranslationData::class,
        PersonChanges::class,
        PersonChanges.PersonChange::class,
        PersonChanges.PersonChange.PersonChangeItem::class,
        PeoplePopular::class,
        PeoplePopular.PopularPerson::class,
        ReviewDetails::class,
        MovieListResult::class,
        TvListResult::class,
        TvListResult.TvListNetwork::class,
        TvListResult.TvListNetwork.TvListNetworkLogo::class,
        Title::class,
        PersonListResult::class,
        Genre::class,
        CollectionInfo::class,
        CompanyInfo::class,
        CreditListResult::class,
        ImageListResult::class,
        VideoListResult::class,
        TranslationListResult::class,
        TranslationListResult.TranslationData::class,
        PaginatedListResults::class,
        PaginatedMovieListResultsWithDates::class,
        PaginatedMovieListResultsWithDates.Dates::class,
        CastMember::class,
        CrewMember::class,
        ReviewAuthorDetails::class,
        TvShowDetails::class,
        TvShowAlternativeTitles::class,
        TvShowChanges::class,
        TvShowChanges.Change::class,
        TvShowChanges.ChangeItem::class,
        TvShowContentRatings::class,
        TvShowContentRatings.Rating::class,
        TvShowCredits::class,
        TvShowEpisodeGroups::class,
        TvShowEpisodeGroups.TvShowEpisodeGroup::class,
        TvShowExternalIds::class,
        TvShowImages::class,
        TvShowKeywords::class,
        TvShowReview::class,
        TvShowScreenedTheatrically::class,
        TvShowScreenedTheatrically.ScreenedResult::class,
        TvShowTranslations::class,
        TvShowVideos::class,
        TvEpisodeGroupDetails::class,
        TvEpisodeGroupDetails.TvEpisodeGroup::class,
        TvEpisodeGroupType::class,
        TvEpisodeDetails::class,
        TvEpisodeChanges::class,
        TvEpisodeChanges.Change::class,
        TvEpisodeChanges.ChangeItem::class,
        TvEpisodeCredits::class,
        TvEpisodeExternalIds::class,
        TvEpisodeImages::class,
        TvEpisodeTranslations::class,
        TvEpisodeVideos::class,
        TvEpisodeListResult::class,
        TvSeasonDetails::class,
        TvSeasonChanges::class,
        TvSeasonChanges.Change::class,
        TvSeasonChanges.ChangeItem::class,
        TvSeasonChanges.ChangeValue::class,
        TvSeasonCredits::class,
        TvSeasonExternalIds::class,
        TvSeasonImages::class,
        TvSeasonVideos::class,
        RawErrorResponse::class,
        ErrorResponse::class,
    ],
    accessType = [AccessType.ALL_DECLARED_CONSTRUCTORS]
)
@Introspected
internal class TmdbGraalMetadata
