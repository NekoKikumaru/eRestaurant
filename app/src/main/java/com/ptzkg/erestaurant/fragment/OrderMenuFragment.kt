package com.ptzkg.erestaurant.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.adapter.OrderCategoryAdapter
import com.ptzkg.erestaurant.model.OrderDetailJson
import com.ptzkg.erestaurant.viewmodel.*
import kotlinx.android.synthetic.main.fragment_order_menu.*

class OrderMenuFragment : Fragment() {
    private lateinit var categoryAdapter: OrderCategoryAdapter
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var subCategoryViewModel: SubCategoryViewModel
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var sharedPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        var user_id = sharedPrefs.getInt("user_id", -1)
        var table = arguments.let { OrderMenuFragmentArgs.fromBundle(it!!) }.table

        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        subCategoryViewModel = ViewModelProvider(this).get(SubCategoryViewModel::class.java)
        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.orderFragment, false)
            }
        })

        btn_back.setOnClickListener {
            findNavController().popBackStack(R.id.orderFragment, false)
        }

        fab_order.setOnClickListener {
            var orders = orderViewModel.getOrderJson()
            var total = orders.map { it.total }.sum()

            var order_details = ArrayList<OrderDetailJson>()
            for (order in orders) {
                order_details.add(OrderDetailJson(order.menu_id.toString(), order.qty.toString()))
            }

            if (order_details.isNullOrEmpty()) {
                MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                    .setMessage(resources.getString(R.string.empty_order))
                    .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }.show()
            }
            else {
                var order_detail = Gson().toJson(order_details)

                orderViewModel.addOrder(user_id, table.id, total.toString(), order_detail)
                orderViewModel.getMessage().observe(viewLifecycleOwner, Observer { message ->
                    if (message != "") {
                        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                            .setMessage("The total is "+total.toString()+" MMK")
                            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                                findNavController().navigate(R.id.action_orderMenuFragment_to_orderFragment)
                            }.show()
                    }
                })
            }
        }

        setupAdapter()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        categoryViewModel.loadCategories()
        subCategoryViewModel.loadSubCategories()
        menuViewModel.loadMenus()
    }

    fun setupAdapter() {
        var orders = orderViewModel.getOrderJson()
        categoryAdapter = OrderCategoryAdapter(requireContext())
        categoryAdapter.updateViewModel(orders, orderViewModel)
        order_recyclerview.adapter = categoryAdapter
        order_recyclerview.layoutManager = LinearLayoutManager(activity)
    }

    fun setupObserver() {
        categoryViewModel.getCategories().observe(viewLifecycleOwner, Observer {
            categoryAdapter.updateCategory(it)
        })

        subCategoryViewModel.getSubCategories().observe(viewLifecycleOwner, Observer {
            categoryAdapter.updateSubCategory(it)
        })

        menuViewModel.getMenus().observe(viewLifecycleOwner, Observer {
            categoryAdapter.updateMenu(it)
        })

        categoryViewModel.getLoading().observe(viewLifecycleOwner, Observer {
            loading -> order_loading.visibility = if (loading) View.VISIBLE else View.GONE
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
}