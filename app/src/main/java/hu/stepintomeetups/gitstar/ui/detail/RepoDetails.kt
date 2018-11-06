/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.detail

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import hu.stepintomeetups.gitstar.api.entities.Commit
import hu.stepintomeetups.gitstar.api.entities.GitFile
import hu.stepintomeetups.gitstar.api.entities.Repo

data class RepoDetails(val repo: MutableLiveData<Repo>, val readme: MutableLiveData<GitFile?>,
                       val commits: MutableLiveData<List<Commit>>, val isStarred: MutableLiveData<Boolean>) {
    companion object {
        @MainThread
        fun initFrom(repo: Repo, readme: GitFile?, commits: List<Commit>, isStarred: Boolean): RepoDetails {
            return RepoDetails(
                MutableLiveData<Repo>().apply {
                    value = repo
                },
                MutableLiveData<GitFile?>().apply {
                    value = readme
                },
                MutableLiveData<List<Commit>>().apply {
                    value = commits
                },
                MutableLiveData<Boolean>().apply {
                    value = isStarred
                }
            )
        }
    }
}