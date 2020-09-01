package com.ptzkg.erestaurant.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ptzkg.erestaurant.R
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_register.setOnClickListener {
            // findNavController().navigate(R.id.action_authFragment_to_homeFragment)
            MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                .setMessage("The app is currently only available for registered user")
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                .show()
        }
    }
}