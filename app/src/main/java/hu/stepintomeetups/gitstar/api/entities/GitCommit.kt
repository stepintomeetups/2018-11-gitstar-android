/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.api.entities

data class GitCommit(val message: String, val author: GitCommitUser?, val committer: GitCommitUser?)