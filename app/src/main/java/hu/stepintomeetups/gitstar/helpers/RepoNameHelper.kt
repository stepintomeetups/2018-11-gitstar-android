/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.helpers

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.content.withStyledAttributes
import androidx.core.text.inSpans
import hu.stepintomeetups.gitstar.api.entities.Repo

object RepoNameHelper {
    var currentUserName: String? = null

    fun formatRepoName(context: Context, repo: Repo): CharSequence {
        val builder = SpannableStringBuilder()

        context.withStyledAttributes(attrs = intArrayOf(android.R.attr.textColorPrimary)) {
            val color = getColor(0, 0xFF999999.toInt()).withAlpha(0x7F)

            builder.inSpans(ForegroundColorSpan(color)) {
                if (currentUserName == repo.owner.login)
                    builder.append("@me")
                else
                    builder.append(repo.owner.login)

                builder.append('/')
            }
        }

        builder.append(repo.name)

        return builder
    }
}