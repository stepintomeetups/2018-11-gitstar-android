/*
 * Created by Tamás Szincsák on 2018-11-02.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.main

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.inSpans
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import hu.stepintomeetups.gitstar.R
import hu.stepintomeetups.gitstar.helpers.RepoNameHelper
import hu.stepintomeetups.gitstar.ui.main.my.RepositoryListFragment
import hu.stepintomeetups.gitstar.ui.main.search.RepositorySearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        tabLayout.setupWithViewPager(viewPager)

        viewPager.adapter = MainPagerAdapter()

        viewModel = ViewModelProviders.of(this@MainActivity).get(MainViewModel::class.java)

        viewModel?.userProfile?.observe(this@MainActivity, Observer {
            updateTitle()
        })

        viewModel?.avatar?.observe(this@MainActivity, Observer {
            updateTitle()
        })
    }

    override fun onStart() {
        super.onStart()

        viewModel?.refreshUserProfile()
    }

    private fun updateTitle() {
        val userProfile = viewModel?.userProfile?.value
        val avatar = viewModel?.avatar?.value

        if (userProfile != null) {
            RepoNameHelper.currentUserName = userProfile.login

            val builder = SpannableStringBuilder()
            if (avatar != null) {
                builder.inSpans(ImageSpan(toolbar.context, avatar)) {
                    builder.append(" ")
                }
                builder.append(" ")
            }
            builder.append(userProfile.name ?: ("@" + userProfile.login))
            builder.append(" - ")
            builder.append(getString(R.string.app_name))

            title = builder
        } else
            title = getString(R.string.app_name)
    }

    private inner class MainPagerAdapter : FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> RepositoryListFragment()
                1 -> RepositorySearchFragment()
                else -> throw RuntimeException("Invalid index")
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> getString(R.string.tab_my_repositories)
                1 -> getString(R.string.tab_search)
                else -> null
            }
        }
    }
}
