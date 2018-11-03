/*
 * Created by Tamás Szincsák on 2018-11-03.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.api.responses

import hu.stepintomeetups.gitstar.api.entities.Repo

data class SearchRepositoriesResult(val total_count: Int, val incomplete_results: Boolean, val items: List<Repo>)