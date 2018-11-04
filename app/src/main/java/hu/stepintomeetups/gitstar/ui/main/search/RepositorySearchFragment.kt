/*
 * Created by Tamás Szincsák on 2018-11-03.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.main.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.stepintomeetups.gitstar.R
import hu.stepintomeetups.gitstar.api.entities.Repo
import hu.stepintomeetups.gitstar.ui.common.DataRequestState
import hu.stepintomeetups.gitstar.ui.detail.RepositoryDetailActivity
import hu.stepintomeetups.gitstar.ui.main.common.RepositoryClickListener
import hu.stepintomeetups.gitstar.ui.main.common.RepositoryListAdapter
import kotlinx.android.synthetic.main.fragment_repository_search.*

class RepositorySearchFragment : Fragment(), RepositoryClickListener {
    private lateinit var adapter: RepositoryListAdapter

    private var viewModel: RepositorySearchViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repository_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.itemAnimator = null
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

        adapter = RepositoryListAdapter(this)
        recyclerView.adapter = adapter

        floatingSearchView.setOnQueryChangeListener { _, new ->
            viewModel?.performSearch(new)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val offset = recyclerView.computeVerticalScrollOffset()

                decorationView.visibility = when {
                    offset <= 0 -> View.INVISIBLE
                    else -> View.VISIBLE
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this@RepositorySearchFragment).get(RepositorySearchViewModel::class.java)

        viewModel?.data?.observe(this@RepositorySearchFragment, Observer {
            when (it) {
                is DataRequestState.Loading -> {
                    progressBar.show()
                    recyclerView.visibility = View.GONE
                }
                is DataRequestState.Error -> {
                    progressBar.hide()
                    recyclerView.visibility = View.GONE
                }
                is DataRequestState.Success -> {
                    progressBar.hide()
                    recyclerView.visibility = View.VISIBLE
                    adapter.items = it.data.items
                }
            }
        })
    }

    override fun onRepositoryClick(repo: Repo) {
        startActivity(Intent(activity, RepositoryDetailActivity::class.java).apply {
            putExtra(RepositoryDetailActivity.EXTRA_REPOSITORY, repo)
        })
    }
}