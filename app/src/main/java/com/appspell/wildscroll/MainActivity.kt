package com.appspell.wildscroll

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.appspell.wildscroll.fragments.CustomLayoutFragment
import com.appspell.wildscroll.fragments.CustomProgrammaticallyFragment
import com.appspell.wildscroll.fragments.SimpleFragment

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager>(R.id.pager)
        viewPager.adapter = PagerAdapter(supportFragmentManager, this)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
    }
}

internal class PagerAdapter(fm: FragmentManager, private val context: Context) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? =
            when (position) {
                0 -> SimpleFragment()
                1 -> CustomLayoutFragment()
                2 -> CustomProgrammaticallyFragment()
//                3 -> SectionsFragment()
                else -> null
            }

    override fun getPageTitle(position: Int): CharSequence? =
            when (position) {
                0 -> context.getString(R.string.tab_simple)
                1 -> context.getString(R.string.tab_custom_layout)
                2 -> context.getString(R.string.tab_custom_programmatically)
//                3 -> context.getString(R.string.tab_sections)
                else -> null
            }

    override fun getCount(): Int {
        return 3
    }
}
