package com.ptzkg.erestaurant.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.adapter.SubCategoryAdapter
import com.ptzkg.erestaurant.util.SwipeController
import com.ptzkg.erestaurant.viewmodel.SubCategoryViewModel
import kotlinx.android.synthetic.main.fragment_sub_category.*

class SubCategoryFragment : Fragment(), SubCategoryDialogFragment.SubCategoryDialogListener {
    private lateinit var subCategoryAdapter: SubCategoryAdapter
    private lateinit var subCategoryViewModel: SubCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sub_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subCategoryViewModel = ViewModelProvider(this).get(SubCategoryViewModel::class.java)

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

        fab_sub_category.setOnClickListener {
            val dialogFragment = SubCategoryDialogFragment.newInstance(R.string.add, -1, "",-1)
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(requireFragmentManager(), resources.getString(R.string.sub_category))
        }

        setupAdapter()
        setupObserver()
        setupSwipeController()
    }

    override fun onResume() {
        super.onResume()
        subCategoryViewModel.loadSubCategories()
    }

    override fun onDialogPositiveClick(
        dialog: DialogFragment,
        id: Int,
        name: String,
        category: Int
    ) {
        if (id == -1) {
            subCategoryViewModel.addSubCategory(name, category)
        }
        else {
            subCategoryViewModel.updateSubCategory(id, name, category)
        }
        subCategoryViewModel.getMessage().observe(viewLifecycleOwner, Observer {
            response -> if (response != "") Snackbar.make(requireView(), response, Snackbar.LENGTH_LONG).show()
            subCategoryViewModel.loadSubCategories()
        })
    }

    fun setupAdapter() {
        subCategoryAdapter = SubCategoryAdapter()
        sub_category_recyclerview.adapter = subCategoryAdapter
        sub_category_recyclerview.layoutManager = LinearLayoutManager(activity)
    }

    fun setupObserver() {
        subCategoryViewModel.getSubCategories().observe(viewLifecycleOwner, Observer {
            subCategoryAdapter.update(it)
        })

        subCategoryViewModel.getLoading().observe(viewLifecycleOwner, Observer {
            loading -> category_loading.visibility = if (loading) View.VISIBLE else View.GONE
        })

        subCategoryViewModel.getLoadError().observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                    .setMessage(resources.getString(R.string.network_error))
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(resources.getString(R.string.reload)) { _, _ ->
                        subCategoryViewModel.loadSubCategories()
                    }.show()
            }
        })
    }

    fun setupSwipeController() {
        val swipeController = object : SwipeController(requireActivity().applicationContext) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position = viewHolder.adapterPosition
                var subCategory = subCategoryAdapter.getSubCategoryByPosition(position)

                if (direction == ItemTouchHelper.LEFT) {
                    MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                        .setMessage(resources.getString(R.string.delete_message))
                        .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                        .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            var id = subCategory.id
                            subCategoryViewModel.deleteSubCategory(id)
                            subCategoryViewModel.getMessage().observe(viewLifecycleOwner, Observer {
                                response -> if(response != "") Snackbar.make(view!!, response, Snackbar.LENGTH_LONG).show()
                                subCategoryViewModel.loadSubCategories()
                            })
                        }
                        .show()
                }
                else {
                    val dialogFragment = SubCategoryDialogFragment.newInstance(R.string.edit, subCategory.id, subCategory.name, subCategory.category.id)
                    dialogFragment.setTargetFragment(this@SubCategoryFragment, 0)
                    dialogFragment.show(requireFragmentManager(), resources.getString(R.string.sub_category))
                }
                subCategoryAdapter.notifyItemChanged(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(sub_category_recyclerview)
    }
}