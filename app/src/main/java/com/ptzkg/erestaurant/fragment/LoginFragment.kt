package com.ptzkg.erestaurant.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.User
import com.ptzkg.erestaurant.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var users: List<User> = ArrayList()
        var userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        edit_user.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edit_user.text.toString() != "") {
                    error_user.visibility = View.INVISIBLE
                }
            }
        })

        edit_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edit_password.text.toString() != "") {
                    error_password.visibility = View.INVISIBLE
                }
            }
        })

        btn_login.setOnClickListener {
            if (edit_user.text.toString() == "") {
                error_user.visibility = View.VISIBLE
            }
            if (edit_password.text.toString() == "") {
                error_password.visibility = View.VISIBLE
            }

            if (edit_user.text.toString() != "" && edit_password.text.toString() != "") {
                userViewModel.loadUsers()

                var i = 0
                userViewModel.getUsers().observe(viewLifecycleOwner, Observer {
                    i += 1
                    if (i <= 1) {
                        users = it!!
                        var user = users.find { it.ID_NO == edit_user.text.toString() }

                        if (user != null) {
                            if (user.password == edit_password.text.toString()) {
                                var sharedPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
                                var editor: SharedPreferences.Editor = sharedPrefs.edit()
                                editor.putInt("user_id", user.id)
                                editor.putString("user_role", user.role)
                                editor.apply()

                                try {
                                    if (user.role == "admin") {
                                        findNavController().navigate(R.id.action_authFragment_to_homeFragment)
                                    }
                                    else if (user.role == "waiter") {
                                        findNavController().navigate(R.id.action_authFragment_to_orderFragment)
                                    }
                                    else {
                                        findNavController().navigate(R.id.action_authFragment_to_homeFragment)
                                    }
                                }
                                catch (error: IllegalArgumentException) {
                                    requireActivity().recreate()
                                }
                            }
                            else {
                                alertDialog("Your password is incorrect")
                            }
                        }
                        else {
                            alertDialog("Check your registeration number")
                        }
                    }
                })

                userViewModel.getLoading().observe(viewLifecycleOwner, Observer {
                    loading -> login_loading.visibility = if (loading) View.VISIBLE else View.GONE
                })

                userViewModel.getLoadError().observe(viewLifecycleOwner, Observer { error ->
                    if (error) {
                        alertDialog(resources.getString(R.string.network_error))
                    }
                })
            }
        }
    }

    private fun alertDialog(message: String) {
        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
            .show()
    }
}