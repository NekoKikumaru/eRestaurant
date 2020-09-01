package com.ptzkg.erestaurant.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.ptzkg.erestaurant.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private var isFirstBackPressed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isFirstBackPressed == false) {
                    isFirstBackPressed = true
                    Toast.makeText(context, "Press back again to exit", Toast.LENGTH_LONG).show()
                }
                else {
                   requireActivity().finish()
               }
            }
        })

        category_cardview.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        }

        sub_category_cardview.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_subCategoryFragment)
        }

        menu_cardview.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }

        table_cardview.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_tableFragment)
        }

        order_cardview.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_orderFragment)
        }

        kitchen_cardview.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_kitchenFragment)
        }

        user_cardview.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userFragment)
        }

        invoice_cardview.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_invoiceFragment)
        }

        btn_log_out.setOnClickListener {
            var sharedPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
            var editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.clear()
            editor.apply()
            findNavController().navigate(R.id.action_homeFragment_to_authFragment)
        }
    }
}