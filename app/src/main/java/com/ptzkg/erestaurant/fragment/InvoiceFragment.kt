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
import com.ptzkg.erestaurant.adapter.InvoiceAdapter
import com.ptzkg.erestaurant.model.Order
import com.ptzkg.erestaurant.viewmodel.OrderViewModel
import kotlinx.android.synthetic.main.fragment_invoice.*

class InvoiceFragment : Fragment(), InvoiceAdapter.ClickListener {
    private lateinit var invoiceAdapter: InvoiceAdapter
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_invoice, container, false)
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
        var action = InvoiceFragmentDirections.actionInvoiceFragmentToInvoiceDetailFragment(order)
        view?.findNavController()?.navigate(action)
    }

    fun setupAdapter() {
        invoiceAdapter = InvoiceAdapter()
        // invoiceAdapter.setOnClick(this)
        invoice_recyclerview.layoutManager = LinearLayoutManager(activity)
        invoice_recyclerview.adapter = invoiceAdapter
    }

    fun setupObserver() {
        orderViewModel.getOrders().observe(viewLifecycleOwner, Observer {
            invoiceAdapter.update(it)
        })

        orderViewModel.getLoading().observe(viewLifecycleOwner, Observer {
            loading -> invoice_loading.visibility = if (loading) View.VISIBLE else View.GONE
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