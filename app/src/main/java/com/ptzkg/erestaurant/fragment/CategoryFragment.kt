package com.ptzkg.erestaurant.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.adapter.CategoryAdapter
import com.ptzkg.erestaurant.util.SwipeController
import com.ptzkg.erestaurant.viewmodel.CategoryViewModel
import kotlinx.android.synthetic.main.dialog_single_edittext.view.*
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : Fragment() {
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var dialogView: View
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

//        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
//            .setMessage(resources.getString(R.string.swipe_instruction))
//            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
//            .show()

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        })

        btn_back.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }

        fab_category.setOnClickListener {
            dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_single_edittext, null)
            dialog = MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                .setTitle(resources.getString(R.string.add))
                .setView(dialogView)
                .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    val name = dialogView.edit_dialog.text.toString()
                    categoryViewModel.addCategory(name)
                    categoryViewModel.getMessage().observe(viewLifecycleOwner, Observer {
                        response -> if (response != "") Snackbar.make(requireView(), response, Snackbar.LENGTH_LONG).show()
                        categoryViewModel.loadCategories()
                    })
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }

        setupAdapter()
        setupObserver()
        setupSwipeController()
    }

    override fun onResume() {
        super.onResume()
        categoryViewModel.loadCategories()
    }

    fun setupAdapter() {
        categoryAdapter = CategoryAdapter()
        category_recyclerview.adapter = categoryAdapter
        category_recyclerview.layoutManager = LinearLayoutManager(activity)
    }

    fun setupObserver() {
        categoryViewModel.getCategories().observe(viewLifecycleOwner, Observer {
            categoryAdapter.update(it)
        })

        categoryViewModel.getLoading().observe(viewLifecycleOwner, Observer {
            loading -> category_loading.visibility = if (loading) View.VISIBLE else View.GONE
        })

        categoryViewModel.getLoadError().observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                    .setMessage(resources.getString(R.string.network_error))
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(resources.getString(R.string.reload)) { _, _ ->
                        categoryViewModel.loadCategories()
                    }.show()
            }
        })
    }

    fun setupSwipeController() {
        val swipeController = object : SwipeController(requireActivity().applicationContext) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position = viewHolder.adapterPosition
                var category = categoryAdapter.getCategoryByPosition(position)

                if (direction == ItemTouchHelper.LEFT) {
                    MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                        .setMessage(resources.getString(R.string.delete_message))
                        .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                        .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            var id = category.id
                            categoryViewModel.deleteCategory(id)
                            categoryViewModel.getMessage().observe(viewLifecycleOwner, Observer {
                                response -> if(response != "") Snackbar.make(view!!, response, Snackbar.LENGTH_LONG).show()
                                categoryViewModel.loadCategories()
                            })
                        }
                        .show()
                }
                else {
                    dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_single_edittext, null)
                    dialogView.edit_dialog.setText(category.name)

                    dialog = MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                        .setTitle(resources.getString(R.string.edit))
                        .setView(dialogView)
                        .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                        .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            var name = dialogView.edit_dialog.text.toString()
                            categoryViewModel.updateCategory(category.id, name)
                            categoryViewModel.getMessage().observe(viewLifecycleOwner, Observer {
                                response -> if (response != "") Snackbar.make(view!!, response, Snackbar.LENGTH_LONG).show()
                                categoryViewModel.loadCategories()
                            })
                            dialog.dismiss()
                        }
                        .create()
                    dialog.show()
                }
                categoryAdapter.notifyItemChanged(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(category_recyclerview)
    }
}