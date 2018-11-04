/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.api.entities

import java.util.*

data class GitCommitUser(val name: String, val email: String, val date: Date)