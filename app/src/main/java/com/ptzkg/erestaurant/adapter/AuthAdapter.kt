package com.ptzkg.erestaurant.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ptzkg.erestaurant.fragment.LoginFragment
import com.ptzkg.erestaurant.fragment.RegisterFragment
import java.lang.IllegalStateException

class AuthAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val tabTitles = arrayOf("Login", "Register")

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> RegisterFragment()
            else -> throw IllegalStateException("position $position is invalid for this viewpager")
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}