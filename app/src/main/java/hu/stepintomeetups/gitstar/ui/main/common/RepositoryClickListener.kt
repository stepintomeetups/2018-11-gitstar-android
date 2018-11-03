/*
 * Created by Tamás Szincsák on 2018-11-03.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.main.common

import hu.stepintomeetups.gitstar.api.entities.Repo

interface RepositoryClickListener {
    fun onRepositoryClick(repo: Repo)
}