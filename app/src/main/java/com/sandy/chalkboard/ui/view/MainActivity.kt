package com.sandy.chalkboard.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sandy.chalkboard.R
import com.sandy.chalkboard.ui.view.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(SchoolFragment(), "Schools")
        adapter.addFragment(ClassFragment(), "Classes")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

    }

}