/*
 * Created by Tamás Szincsák on 2018-11-02.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.api.entities

data class Repo(val id: Long, val name: String?, val full_name: String?, val description: String?, val stargazers_count: Int, val language: String?)