/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.api.entities

data class Commit(
    val sha: String,
    val author: User?,
    val committer: User?,
    val commit: GitCommit
)
