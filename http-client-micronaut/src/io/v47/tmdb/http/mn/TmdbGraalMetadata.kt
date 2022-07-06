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
package io.v47.tmdb.http.mn

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.TypeHint
import io.micronaut.core.annotation.TypeHint.AccessType
import io.v47.tmdb.http.api.ErrorResponse
import io.v47.tmdb.http.api.RawErrorResponse
import io.v47.tmdb.model.*

@TypeHint(
    value = [
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
