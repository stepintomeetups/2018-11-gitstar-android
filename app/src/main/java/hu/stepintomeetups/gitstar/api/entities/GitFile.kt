/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.api.entities

import android.util.Base64
import io.gsonfire.annotations.PostDeserialize

data class GitFile(val name: String, val content: String?) {
    // required by gson
    constructor() : this("", null)

    @Transient
    var decodedContents: String? = null

    @PostDeserialize
    fun postDeserialize() {
        decodedContents = when {
            content != null -> String(Base64.decode(content, 0), charset("utf-8"))
            else -> null
        }
    }
}