package com.ptzkg.erestaurant.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ptzkg.erestaurant.R
import kotlinx.android.synthetic.main.dialog_user.view.*

class UserDialogFragment: DialogFragment(), AdapterView.OnItemSelectedListener {
    private var userDialogListener: UserDialogListener? = null
    private lateinit var dialog: MaterialAlertDialogBuilder
    private lateinit var dialogView: View

    interface UserDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, id: Int, reg_no: String, name: String, email: String, password: String, role: String)
    }

    companion object {
        fun newInstance(title: Int, id: Int, reg_no: String, name: String, email: String, password: String, role: String): UserDialogFragment {
            val userDialogFragment = UserDialogFragment()
            val args = Bundle()

            args.putInt("dialog_title", title)
            args.putInt("user_id", id)
            args.putString("registeration_no", reg_no)
            args.putString("user_name", name)
            args.putString("user_email", email)
            args.putString("user_password", password)
            args.putString("user_role", role)

            userDialogFragment.arguments = args
            return userDialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        userDialogListener = targetFragment as UserDialogListener

        dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_user, null)

        val title = arguments?.getInt("dialog_title")
        val id = arguments?.getInt("user_id", -1)
        val reg_no = arguments?.getString("registeration_no", "")
        val name = arguments?.getString("user_name", "")
        val email = arguments?.getString("user_email", "")
        val password = arguments?.getString("user_password", "")
        val role = arguments?.getString("role", "")

        dialogView.spinner_role.setLabel(resources.getString(R.string.role))

        var roleList = ArrayList<String>()
        roleList.add("Admin")
        roleList.add("Waiter")
        roleList.add("Chef")

        var spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roleList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        if (title == R.string.edit) {
            dialogView.spinner_role.getSpinner().setSelection(roleList.indexOf(role))
        }

        dialogView.spinner_role.setAdapter(spinnerAdapter)
        dialogView.spinner_role.getSpinner().onItemSelectedListener = this

        if (title != null) {
            dialog.setTitle(resources.getString(title))
        }
        if (title == R.string.edit) {
            dialogView.edit_dialog_id.setText(reg_no)
            dialogView.edit_dialog_name.setText(name)
            dialogView.edit_dialog_email.setText(email)
            dialogView.edit_dialog_password.setText(password)
        }
        else {
            dialogView.edit_dialog_password.visibility = View.GONE
            dialogView.edit_dialog_id.visibility = View.GONE
        }

        dialog.setView(dialogView)
            .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                val name = dialogView.edit_dialog_name.text.toString()
                val email = dialogView.edit_dialog_email.text.toString()
                val password = dialogView.edit_dialog_password.text.toString()
                val role = dialogView.spinner_role.getSpinner().selectedItem.toString()
                userDialogListener?.onDialogPositiveClick(this, id!!, reg_no!!, name, email, password, role)
            }
        return dialog.create()
    }

    override fun onNothingSelected(p0: AdapterView<*>) {

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

    }
}