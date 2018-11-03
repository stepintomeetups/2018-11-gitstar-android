package hu.stepintomeetups.gitstar.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import hu.stepintomeetups.gitstar.R
import hu.stepintomeetups.gitstar.ui.main.my.RepositoryListFragment
import hu.stepintomeetups.gitstar.ui.main.search.RepositorySearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        tabLayout.setupWithViewPager(viewPager)

        viewPager.adapter = MainPagerAdapter()
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
