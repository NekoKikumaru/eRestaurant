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
import com.ptzkg.erestaurant.adapter.UserAdapter
import com.ptzkg.erestaurant.util.SwipeController
import com.ptzkg.erestaurant.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment(), UserDialogFragment.UserDialogListener {
    private lateinit var userAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

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

        fab_user.setOnClickListener {
            val dialogFragment = UserDialogFragment.newInstance(R.string.add, -1, "","", "", "", "")
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(requireFragmentManager(), resources.getString(R.string.users))
        }

        setupAdapter()
        setupObserver()
        setupSwipeController()
    }

    override fun onResume() {
        super.onResume()
        userViewModel.loadUsers()
    }

    override fun onDialogPositiveClick(
        dialog: DialogFragment,
        id: Int,
        reg_no: String,
        name: String,
        email: String,
        password: String,
        role: String
    ) {
        if (id == -1) {
            userViewModel.addUser(name, email, password, role)
        }
        else {
            Snackbar.make(requireView(), R.string.incomplete_api, Snackbar.LENGTH_LONG).show()
        }
        userViewModel.getMessage().observe(viewLifecycleOwner, Observer {
            response -> if (response != "") Snackbar.make(requireView(), response, Snackbar.LENGTH_LONG).show()
            userViewModel.loadUsers()
        })
    }

    fun setupAdapter() {
        userAdapter = UserAdapter()
        user_recyclerview.layoutManager = LinearLayoutManager(activity)
        user_recyclerview.adapter = userAdapter
    }

    fun setupObserver() {
        userViewModel.getUsers().observe(viewLifecycleOwner, Observer {
            userAdapter.update(it)
        })

        userViewModel.getLoading().observe(viewLifecycleOwner, Observer {
            loading -> user_loading.visibility = if (loading) View.VISIBLE else View.GONE
        })

        userViewModel.getLoadError().observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                    .setMessage(resources.getString(R.string.network_error))
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(resources.getString(R.string.reload)) { _, _ ->
                        userViewModel.loadUsers()
                    }
                    .show()
            }
        })
    }

    fun setupSwipeController() {
        val swipeController = object : SwipeController(requireActivity().applicationContext) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position = viewHolder.adapterPosition
                var user = userAdapter.getUserByPosition(position)

                if (direction == ItemTouchHelper.LEFT) {
                    MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                        .setMessage(resources.getString(R.string.delete_message))
                        .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                        .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            var id = user.id
                            userViewModel.deleteUser(id)
                            userViewModel.getMessage().observe(viewLifecycleOwner, Observer {
                                response -> if(response != "") Snackbar.make(view!!, response, Snackbar.LENGTH_LONG).show()
                                userViewModel.loadUsers()
                            })
                        }
                        .show()
                }
                else {
                    var email = user.email
                    if (user.email == null) {
                        email = ""
                    }
                    val dialogFragment = UserDialogFragment.newInstance(R.string.edit, user.id, user.ID_NO, user.name, email, user.password, user.role)
                    dialogFragment.setTargetFragment(this@UserFragment, 0)
                    dialogFragment.show(requireFragmentManager(), resources.getString(R.string.sub_category))
                }
                userAdapter.notifyItemChanged(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(user_recyclerview)
    }
}