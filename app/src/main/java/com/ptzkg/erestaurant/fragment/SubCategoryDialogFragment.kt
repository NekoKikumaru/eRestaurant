package com.ptzkg.erestaurant.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.Category
import com.ptzkg.erestaurant.viewmodel.CategoryViewModel
import kotlinx.android.synthetic.main.dialog_sub_category.view.*

class SubCategoryDialogFragment: DialogFragment(), AdapterView.OnItemSelectedListener {
    private var subCategoryDialogListener: SubCategoryDialogListener? = null
    private lateinit var dialog: MaterialAlertDialogBuilder
    private lateinit var dialogView: View
    private lateinit var categories: List<Category>

    interface SubCategoryDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, id: Int, name: String, category: Int)
    }

    companion object {
        fun newInstance(title: Int, id: Int, name: String, category: Int): SubCategoryDialogFragment {
            val subCategoryDialogFragment = SubCategoryDialogFragment()
            val args = Bundle()

            args.putInt("dialog_title", title)
            args.putInt("sub_category_id", id)
            args.putString("sub_category_name", name)
            args.putInt("category", category)

            subCategoryDialogFragment.arguments = args
            return subCategoryDialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        subCategoryDialogListener = targetFragment as SubCategoryDialogListener

        dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_sub_category, null)

        val title = arguments?.getInt("dialog_title")
        val id = arguments?.getInt("sub_category_id", -1)
        val name = arguments?.getString("sub_category_name", "")
        val category = arguments?.getInt("category", -1)

        dialogView.spinner_sub_category.setLabel(resources.getString(R.string.category))
        var categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        categoryViewModel.loadCategories()

        categoryViewModel.getCategories().observe(requireParentFragment().viewLifecycleOwner, Observer {
            categories = it
            var categoryList = categories.map { it.name }

            var spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            dialogView.spinner_sub_category.setAdapter(spinnerAdapter)
            dialogView.spinner_sub_category.getSpinner().onItemSelectedListener = this

            if (title == R.string.edit) {
                val category = categories.find { it.id == category }
                val position = categories.indexOf(category)
                dialogView.spinner_sub_category.getSpinner().setSelection(position)
            }
        })

        if (title != null) {
            dialog.setTitle(resources.getString(title))
        }
        if (title == R.string.edit) {
            dialogView.edit_dialog_sub_category.setText(name)
        }

        dialog.setView(dialogView)
            .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                val name = dialogView.edit_dialog_sub_category.text.toString()
                val position = dialogView.spinner_sub_category.getSpinner().selectedItemId.toInt()
                val category = categories[position].id
                subCategoryDialogListener?.onDialogPositiveClick(this, id!!, name, category)
            }
        return dialog.create()
    }

    override fun onNothingSelected(p0: AdapterView<*>) {

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

    }
}