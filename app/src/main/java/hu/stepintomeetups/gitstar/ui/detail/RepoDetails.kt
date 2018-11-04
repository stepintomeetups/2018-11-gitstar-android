/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.detail

import hu.stepintomeetups.gitstar.api.entities.Commit
import hu.stepintomeetups.gitstar.api.entities.GitFile
import hu.stepintomeetups.gitstar.api.entities.Repo

data class RepoDetails(val repo: Repo, val readme: GitFile?, val commits: List<Commit>)