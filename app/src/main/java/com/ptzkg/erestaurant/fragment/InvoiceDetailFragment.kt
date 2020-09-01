package com.ptzkg.erestaurant.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.viewmodel.OrderViewModel
import kotlinx.android.synthetic.main.fragment_invoice_detail.*

class InvoiceDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invoice_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var order = arguments.let { InvoiceDetailFragmentArgs.fromBundle(it!!) }.order
        // orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
    }
}