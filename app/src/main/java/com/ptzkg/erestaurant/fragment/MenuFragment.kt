package com.ptzkg.erestaurant.fragment

import android.os.Bundle
import android.util.Log
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
import com.ptzkg.erestaurant.adapter.MenuAdapter
import com.ptzkg.erestaurant.util.SwipeController
import com.ptzkg.erestaurant.viewmodel.MenuViewModel
import kotlinx.android.synthetic.main.fragment_menu.*
import okhttp3.MultipartBody

class MenuFragment : Fragment(), MenuDialogFragment.MenuDialogListener {
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var menuViewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

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

        fab_menu.setOnClickListener {
            val dialogFragment = MenuDialogFragment.newInstance(R.string.add, -1, "", -1, "0", "placeholder")
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(requireFragmentManager(), resources.getString(R.string.menu))
        }

        setupAdapter()
        setupObserver()
        setupSwipeController()
    }

    override fun onResume() {
        super.onResume()
        menuViewModel.loadMenus()
    }

    override fun onDialogPositiveClick(
        dialog: DialogFragment,
        id: Int,
        name: String,
        sub_category: Int,
        price: String,
        image: String
    ) {
        if (id == -1) {
            menuViewModel.addMenu(name, sub_category, price, image)
        }
        else {
            menuViewModel.updateMenu(id, name, sub_category, price, image)
        }
        menuViewModel.getMessage().observe(viewLifecycleOwner, Observer {
            response -> if (response != "") Snackbar.make(requireView(), response, Snackbar.LENGTH_LONG).show()
            menuViewModel.loadMenus()
        })
    }

    fun setupAdapter() {
        menuAdapter = MenuAdapter()
        menu_recyclerview.adapter = menuAdapter
        menu_recyclerview.layoutManager = LinearLayoutManager(activity)
    }

    fun setupObserver() {
        menuViewModel.getMenus().observe(viewLifecycleOwner, Observer {
            menuAdapter.update(it)
        })

        menuViewModel.getLoading().observe(viewLifecycleOwner, Observer {
            loading -> menu_loading.visibility = if (loading) View.VISIBLE else View.GONE
        })

        menuViewModel.getLoadError().observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                    .setMessage(resources.getString(R.string.network_error))
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(resources.getString(R.string.reload)) { _, _ ->
                        menuViewModel.loadMenus()
                    }.show()
            }
        })
    }

    fun setupSwipeController() {
        val swipeController = object : SwipeController(requireActivity().applicationContext) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position = viewHolder.adapterPosition
                var menu = menuAdapter.getMenuByPosition(position)

                if (direction == ItemTouchHelper.LEFT) {
                    MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                        .setMessage(resources.getString(R.string.delete_message))
                        .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                        .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            var id = menu.id
                            menuViewModel.deleteMenu(id)
                            menuViewModel.getMessage().observe(viewLifecycleOwner, Observer {
                                response -> if(response != "") Snackbar.make(view!!, response, Snackbar.LENGTH_LONG).show()
                                menuViewModel.loadMenus()
                            })
                        }
                        .show()
                }
                else {
                    val dialogFragment = MenuDialogFragment.newInstance(R.string.edit, menu.id, menu.name, menu.subCategory.id, menu.price, menu.image)
                    dialogFragment.setTargetFragment(this@MenuFragment, 0)
                    dialogFragment.show(requireFragmentManager(), resources.getString(R.string.menu))
                }
                menuAdapter.notifyItemChanged(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(menu_recyclerview)
    }
}