package io.v47.tmdb.autoconfigure

import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

internal class TmdbAutoConfigurationCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val apiKeyProp = context.environment.getProperty("tmdb-client.api-key")
        val envVar = context.environment.getProperty("TMDB_API_KEY")

        return !apiKeyProp.isNullOrBlank() || !envVar.isNullOrBlank()
    }
}
