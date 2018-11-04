/*
 * Created by Tamás Szincsák on 2018-11-03.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.main.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.stepintomeetups.gitstar.R
import hu.stepintomeetups.gitstar.api.entities.Repo
import hu.stepintomeetups.gitstar.helpers.RepoNameHelper
import hu.stepintomeetups.gitstar.ui.common.RepositoryIconHelper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_repository.*
import java.util.*
import kotlin.properties.Delegates

class RepositoryListAdapter(var clickListener: RepositoryClickListener? = null) : RecyclerView.Adapter<RepositoryListAdapter.ViewHolder>() {
    val repositoryIconHelper = RepositoryIconHelper()

    var items: List<Repo> by Delegates.observable(Collections.emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(items[position])
    }

    inner class ViewHolder(parent: ViewGroup, override val containerView: View = LayoutInflater.from(parent.context).inflate(R.layout.row_repository, parent, false)) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        var item: Repo? = null

        init {
            containerView.setOnClickListener {
                clickListener?.onRepositoryClick(item!!)
            }
        }

        fun bindTo(repo: Repo) {
            this.item = repo

            nameView.text = RepoNameHelper.formatRepoName(nameView.context, repo)
            descriptionView.text = repo.description
            starsView.text = repo.stargazers_count.toString()
            iconView.setImageBitmap(repositoryIconHelper.getIconForRepository(iconView.context, repo))

            descriptionView.visibility = if (repo.description?.isNotBlank() == true) View.VISIBLE else View.GONE
        }
    }
}
