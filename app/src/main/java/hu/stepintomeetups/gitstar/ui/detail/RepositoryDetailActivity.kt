/*
 * Created by Tamás Szincsák on 2018-11-03.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hu.stepintomeetups.gitstar.R
import hu.stepintomeetups.gitstar.helpers.CommonHttpClient
import hu.stepintomeetups.gitstar.api.entities.Repo
import hu.stepintomeetups.gitstar.helpers.RepoNameHelper
import hu.stepintomeetups.gitstar.helpers.getFirstNLines
import hu.stepintomeetups.gitstar.ui.common.DataRequestState
import kotlinx.android.synthetic.main.activity_repository_detail.*
import kotlinx.coroutines.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.il.AsyncDrawableLoader
import ru.noties.markwon.il.NetworkSchemeHandler
import ru.noties.markwon.spans.SpannableTheme
import kotlin.coroutines.CoroutineContext

class RepositoryDetailActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var viewModel: RepositoryDetailViewModel? = null

    private var adapter: CommitListAdapter? = null

    private val markdownConfiguration by lazy {
        val drawableLoader = AsyncDrawableLoader.builder()
            .addSchemeHandler(NetworkSchemeHandler.create(CommonHttpClient.instance))
            .build()

        val theme = SpannableTheme.builderWithDefaults(readmeView.context)
            .blockQuoteColor(ContextCompat.getColor(readmeView.context, R.color.markdown_block_color))
            .codeBackgroundColor(ContextCompat.getColor(readmeView.context, R.color.markdown_block_color))
            .codeBlockBackgroundColor(ContextCompat.getColor(readmeView.context, R.color.markdown_block_color))
            .headingTextSizeMultipliers(floatArrayOf(1.5f, 1.35f, 1.2f, 1.15f, 1.1f, 1.05f))
            .build()

        SpannableConfiguration.builder(readmeView.context)
            .asyncDrawableLoader(drawableLoader)
            .theme(theme)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_repository_detail)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        commitsRecyclerView.setHasFixedSize(true)
        commitsRecyclerView.layoutManager = LinearLayoutManager(commitsRecyclerView.context)
        commitsRecyclerView.itemAnimator = null
        commitsRecyclerView.addItemDecoration(DividerItemDecoration(commitsRecyclerView.context, DividerItemDecoration.VERTICAL))

        adapter = CommitListAdapter()
        commitsRecyclerView.adapter = adapter

        starButton.setOnClickListener {
            starButton.isEnabled = false

            when (starButton.isChecked) {
                true -> viewModel?.unstarRepository()
                false -> viewModel?.starRepository()
            }
        }

        viewReadmeButton.setOnClickListener {
            Toast.makeText(this@RepositoryDetailActivity, R.string.not_implemented_yet, Toast.LENGTH_SHORT).show()
        }

        viewModel = ViewModelProviders.of(this@RepositoryDetailActivity).get(RepositoryDetailViewModel::class.java)

        viewModel?.data?.observe(this@RepositoryDetailActivity, Observer {
            when (it) {
                is DataRequestState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    mainContainer.visibility = View.GONE
                }
                is DataRequestState.Error -> {
                    progressBar.visibility = View.GONE
                    mainContainer.visibility = View.GONE
                }
                is DataRequestState.Success -> {
                    progressBar.visibility = View.GONE
                    mainContainer.visibility = View.VISIBLE

                    descriptionView.text = it.data.repo.description
                    descriptionView.visibility = if (it.data.repo.description?.isNotBlank() == true) View.VISIBLE else View.GONE

                    starsCountView.text = starsCountView.resources.getQuantityString(R.plurals.repo_stars_count_fmt, it.data.repo.stargazers_count, it.data.repo.stargazers_count)
                    watchersCountView.text = starsCountView.resources.getQuantityString(R.plurals.repo_watchers_count_fmt, it.data.repo.subscribers_count, it.data.repo.subscribers_count)
                    openIssuesCountView.text = starsCountView.resources.getQuantityString(R.plurals.repo_open_issues_count_fmt, it.data.repo.open_issues_count, it.data.repo.open_issues_count)

                    starButton.isChecked = it.data.isStarred

                    if (!it.data.repo.topics.isNullOrEmpty()) {
                        topicsContainer.removeAllViews()
                        val inflater = LayoutInflater.from(topicsContainer.context)
                        it.data.repo.topics?.forEach { topic ->
                            val view = inflater.inflate(R.layout.row_topic, topicsContainer, false) as TextView
                            view.text = topic
                            topicsContainer.addView(view)
                        }
                        topicsContainer.visibility = View.VISIBLE
                    } else
                        topicsContainer.visibility = View.GONE

                    noCommitsView.visibility = if (it.data.commits.isEmpty()) View.VISIBLE else View.GONE
                    commitsRecyclerView.visibility = if (!it.data.commits.isEmpty()) View.VISIBLE else View.GONE

                    adapter?.items = it.data.commits

                    if (it.data.readme != null) {
                        viewReadmeButton.text = viewReadmeButton.context.getString(R.string.btn_view_readme, it.data.readme?.name)
                        viewReadmeButton.visibility = View.VISIBLE

                        launch(Dispatchers.Default) {
                            val parsed = Markwon.markdown(markdownConfiguration, it.data.readme?.decodedContents ?: "")
                            val excerpt = parsed.getFirstNLines(8)

                            withContext(Dispatchers.Main) {
                                Markwon.setText(readmeView, excerpt, BetterLinkMovementMethod.newInstance())
                            }
                        }
                    } else {
                        readmeView.setText(R.string.no_readme_found)
                        viewReadmeButton.visibility = View.GONE
                    }
                }
            }
        })

        val repository = intent.getParcelableExtra<Repo>(EXTRA_REPOSITORY)

        title = RepoNameHelper.formatRepoName(toolbar.context, repository)

        viewModel?.initWithRepository(repository.owner.login, repository.name)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()

        job.cancel()
    }

    companion object {
        const val EXTRA_REPOSITORY = "repository"
    }
}
