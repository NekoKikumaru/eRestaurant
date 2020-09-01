package com.ptzkg.erestaurant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.adapter.AuthAdapter
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : Fragment() {
    lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authAdapter =
            AuthAdapter(childFragmentManager)
        val viewpager = view_pager
        viewpager.adapter = authAdapter
        tab_layout.setupWithViewPager(viewpager)
    }
}