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
import com.ptzkg.erestaurant.adapter.TableAdapter
import com.ptzkg.erestaurant.util.SwipeController
import com.ptzkg.erestaurant.viewmodel.TableViewModel
import kotlinx.android.synthetic.main.dialog_single_edittext.view.*
import kotlinx.android.synthetic.main.fragment_table.*

class TableFragment : Fragment() {
    private lateinit var tableAdapter: TableAdapter
    private lateinit var tableViewModel: TableViewModel
    private lateinit var dialogView: View
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tableViewModel = ViewModelProvider(this).get(TableViewModel::class.java)

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

        fab_table.setOnClickListener {
            dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_single_edittext, null)
            dialogView.input_dialog.hint = resources.getString(R.string.table_no)

            dialog = MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                .setTitle(resources.getString(R.string.add))
                .setView(dialogView)
                .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    val table_no = dialogView.edit_dialog.text.toString()
                    tableViewModel.addTable(table_no)
                    tableViewModel.getMessage().observe(viewLifecycleOwner, Observer {
                        response -> if (response != "") Snackbar.make(requireView(), response, Snackbar.LENGTH_LONG).show()
                        tableViewModel.loadTables()
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
        tableViewModel.loadTables()
    }

    fun setupAdapter() {
        tableAdapter = TableAdapter()
        table_recyclerview.layoutManager = LinearLayoutManager(activity)
        table_recyclerview.adapter = tableAdapter
    }

    fun setupObserver() {
        tableViewModel.getTables().observe(viewLifecycleOwner, Observer {
            tableAdapter.update(it)
        })

        tableViewModel.getLoading().observe(viewLifecycleOwner, Observer {
            loading -> table_loading.visibility = if (loading) View.VISIBLE else View.GONE
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

    fun setupSwipeController() {
        val swipeController = object : SwipeController(requireActivity().applicationContext) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position = viewHolder.adapterPosition
                var table = tableAdapter.getTableByPosition(position)

                if (direction == ItemTouchHelper.LEFT) {
                    MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                        .setMessage(resources.getString(R.string.delete_message))
                        .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                        .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            var id = table.id
                            tableViewModel.deleteTable(id)
                            tableViewModel.getMessage().observe(viewLifecycleOwner, Observer {
                                response -> if(response != "") Snackbar.make(view!!, response, Snackbar.LENGTH_LONG).show()
                                tableViewModel.loadTables()
                            })
                        }
                        .show()
                }
                else {
                    dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_single_edittext, null)
                    dialogView.edit_dialog.setText(table.table_no)
                    dialogView.input_dialog.hint = resources.getString(R.string.table_no)

                    dialog = MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                        .setTitle(resources.getString(R.string.edit))
                        .setView(dialogView)
                        .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                        .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            var table_no = dialogView.edit_dialog.text.toString()
                            tableAdapter.notifyDataSetChanged()
                            tableViewModel.updateTable(table.id, table_no)
                            tableViewModel.getMessage().observe(viewLifecycleOwner, Observer {
                                response -> if (response != "") Snackbar.make(view!!, response, Snackbar.LENGTH_LONG).show()
                                tableViewModel.loadTables()
                            })
                            dialog.dismiss()
                        }
                        .create()
                    dialog.show()
                }
                tableAdapter.notifyItemChanged(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(table_recyclerview)
    }
}