package hu.stepintomeetups.gitstar.ui.main.common

import hu.stepintomeetups.gitstar.api.entities.Repo

interface RepositoryClickListener {
    fun onRepositoryClick(repo: Repo)
}