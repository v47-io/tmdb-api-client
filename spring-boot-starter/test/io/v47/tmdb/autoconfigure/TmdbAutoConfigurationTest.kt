/**
 * Copyright 2022 The tmdb-api-client Authors
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
package io.v47.tmdb.autoconfigure

import io.reactivex.rxjava3.core.Flowable
import io.v47.tmdb.TmdbClient
import io.v47.tmdb.config.TmdbAutoConfigurationTestConfiguration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
    classes = [
        TmdbAutoConfiguration::class,
        TmdbAutoConfigurationTestConfiguration::class
    ]
)
class TmdbAutoConfigurationTest {
    @Autowired
    private lateinit var tmdbClient: TmdbClient

    @Test
    fun `TMDB Client is available`() {
        Flowable.fromPublisher(tmdbClient.configuration.system()).blockingFirst()
    }
}
