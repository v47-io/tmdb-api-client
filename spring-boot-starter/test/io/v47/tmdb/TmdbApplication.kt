package io.v47.tmdb

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

// Needed for SpringBootTest
@SpringBootApplication
class TmdbApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TmdbApplication::class.java, *args)
        }
    }
}
