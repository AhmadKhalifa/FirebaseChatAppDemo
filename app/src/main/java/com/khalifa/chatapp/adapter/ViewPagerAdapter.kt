package com.khalifa.chatapp.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<String>()

    override fun getItem(position: Int) = fragments[position]

    override fun getPageTitle(position: Int) = titles[position]

    override fun getCount() = fragments.size

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }
}