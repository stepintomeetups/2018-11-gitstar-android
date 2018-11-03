package hu.stepintomeetups.gitstar.api.responses

import hu.stepintomeetups.gitstar.api.entities.Repo

data class SearchRepositoriesResult(val total_count: Int, val incomplete_results: Boolean, val items: List<Repo>)