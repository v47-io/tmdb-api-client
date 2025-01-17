plugins {
    id("tmdb-api-client.root")

    // Cannot be in convention plugin, leads to invalid plugin exception
    id("net.researchgate.release")
}
