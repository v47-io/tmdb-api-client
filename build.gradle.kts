plugins {
    id("tmdb-api-client.root")

    // Cannot be in convention plugin, leads to invalid plugin exception
    id("net.researchgate.release")
}

tasks.checkSnapshotDependencies {
    // Disabled for now, leads to ConcurrentModificationException
    enabled = false
}
