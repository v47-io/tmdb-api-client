package io.v47.tmdb.utils

import io.v47.tmdb.http.TmdbRequestBuilder
import io.v47.tmdb.model.Session

internal fun <T : Any> TmdbRequestBuilder<T>.withSession(session: Session) {
    queryArg(session.property, session.id)
}
