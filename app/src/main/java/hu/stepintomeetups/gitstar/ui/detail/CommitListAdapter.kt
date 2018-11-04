/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.stepintomeetups.gitstar.R
import hu.stepintomeetups.gitstar.api.entities.Commit
import hu.stepintomeetups.gitstar.helpers.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_commit.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class CommitListAdapter : RecyclerView.Adapter<CommitListAdapter.ViewHolder>() {
    var items: List<Commit> by Delegates.observable(Collections.emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(items[position])
    }

    inner class ViewHolder(parent: ViewGroup, override val containerView: View = LayoutInflater.from(parent.context).inflate(R.layout.row_commit, parent, false)) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindTo(commit: Commit) {
            authorView.text = commit.author?.name ?: commit.author?.login ?: commit.commit.author?.name ?: commit.commit.author?.email ?: "Unknown"
            dateView.text = if (commit.commit.author != null) dateFormat.format(commit.commit.author.date) else "-"
            messageView.text = commit.commit.message

            if (commit.author?.avatar_url != null) {
                GlideApp.with(iconView.context)
                    .load(commit.author.avatar_url)
                    .centerCrop()
                    .into(iconView)
            } else
                iconView.setImageResource(R.drawable.ic_launcher_background)
        }
    }
}
