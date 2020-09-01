package com.ptzkg.erestaurant.fragment

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
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.adapter.KitchenMenuAdapter
import com.ptzkg.erestaurant.viewmodel.OrderViewModel
import kotlinx.android.synthetic.main.fragment_kitchen_menu.*

class KitchenMenuFragment : Fragment() {
    private lateinit var kitchenAdapter: KitchenMenuAdapter
    private lateinit var orderViewModel: OrderViewModel
    lateinit var voucher_no: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kitchen_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        voucher_no = arguments.let { KitchenMenuFragmentArgs.fromBundle(it!!) }.voucherNo
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.kitchenFragment, false)
            }
        })

        btn_back.setOnClickListener {
            findNavController().popBackStack(R.id.kitchenFragment, false)
        }

        setupAdapter()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        orderViewModel.loadOrderDetails(voucher_no)
    }

    fun setupAdapter() {
        kitchenAdapter = KitchenMenuAdapter()
        menu_recyclerview.layoutManager = LinearLayoutManager(activity)
        menu_recyclerview.adapter = kitchenAdapter
    }

    fun setupObserver() {
        orderViewModel.getOrderDetails().observe(viewLifecycleOwner, Observer {
            kitchenAdapter.update(it)
        })

        orderViewModel.getLoading().observe(viewLifecycleOwner, Observer {
            loading -> menu_loading.visibility = if (loading) View.VISIBLE else View.GONE
        })

        orderViewModel.getLoadError().observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                    .setMessage(resources.getString(R.string.network_error))
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(resources.getString(R.string.reload)) { _, _ ->
                        orderViewModel.loadOrderDetails(voucher_no)
                    }
                    .show()
            }
        })
    }
}