package com.ptzkg.erestaurant.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.adapter.KitchenAdapter
import com.ptzkg.erestaurant.model.Order
import com.ptzkg.erestaurant.viewmodel.OrderViewModel
import kotlinx.android.synthetic.main.fragment_kitchen.*

class KitchenFragment : Fragment(), KitchenAdapter.ClickListener {
    private lateinit var kitchenAdapter: KitchenAdapter
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kitchen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        })

        btn_back.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }

        setupAdapter()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        orderViewModel.loadOrders()
    }

    override fun onClick(order: Order) {
        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
            .setMessage(resources.getString(R.string.incomplete_api))
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
            .show()
//        var action = KitchenFragmentDirections.actionKitchenFragmentToKitchenMenuFragment(order.voucher_no)
//        view?.findNavController()?.navigate(action)
    }

    fun setupAdapter() {
        kitchenAdapter = KitchenAdapter()
        kitchenAdapter.setOnClick(this)
        kitchen_recyclerview.layoutManager = LinearLayoutManager(activity)
        kitchen_recyclerview.adapter = kitchenAdapter
    }

    fun setupObserver() {
        orderViewModel.getOrders().observe(viewLifecycleOwner, Observer {
            kitchenAdapter.update(it)
        })

        orderViewModel.getLoading().observe(viewLifecycleOwner, Observer {
            loading -> kitchen_loading.visibility = if (loading) View.VISIBLE else View.GONE
        })

        orderViewModel.getLoadError().observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                    .setMessage(resources.getString(R.string.network_error))
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(resources.getString(R.string.reload)) { _, _ ->
                        orderViewModel.loadOrders()
                    }
                    .show()
            }
        })
    }
}