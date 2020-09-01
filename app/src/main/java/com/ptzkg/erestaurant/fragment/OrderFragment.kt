package com.ptzkg.erestaurant.fragment

import android.os.Bundle
import android.os.Handler
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
import com.ptzkg.erestaurant.adapter.OrderAdapter
import com.ptzkg.erestaurant.model.Table
import com.ptzkg.erestaurant.viewmodel.TableViewModel
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : Fragment(), OrderAdapter.ClickListener {
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var tableViewModel: TableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tableViewModel = ViewModelProvider(this).get(TableViewModel::class.java)

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
        tableViewModel.loadTables()
//        var handler = Handler()
//        var runnable = object : Runnable {
//            override fun run() {
//                tableViewModel.loadTables()
//                handler.postDelayed(this, 1000)
//            }
//        }
//        handler.postDelayed(runnable, 1000)
    }

    override fun onClick(table: Table) {
        var action = OrderFragmentDirections.actionOrderFragmentToOrderMenuFragment(table)
        view?.findNavController()?.navigate(action)
    }

    fun setupAdapter() {
        orderAdapter = OrderAdapter()
        orderAdapter.setOnClick(this)
        order_table_recyclerview.layoutManager = LinearLayoutManager(activity)
        order_table_recyclerview.adapter = orderAdapter
    }

    fun setupObserver() {
        tableViewModel.getTables().observe(viewLifecycleOwner, Observer {
            orderAdapter.update(it)
        })

        tableViewModel.getLoading().observe(viewLifecycleOwner, Observer {
            loading -> order_table_loading.visibility = if (loading) View.VISIBLE else View.GONE
        })

        tableViewModel.getLoadError().observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                    .setMessage(resources.getString(R.string.network_error))
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(resources.getString(R.string.reload)) { _, _ ->
                        tableViewModel.loadTables()
                    }
                    .show()
            }
        })
    }
}