package com.ptzkg.erestaurant.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.SubCategory
import com.ptzkg.erestaurant.viewmodel.SubCategoryViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_menu.*
import kotlinx.android.synthetic.main.dialog_menu.view.*
import kotlinx.android.synthetic.main.item_menu.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MenuDialogFragment: DialogFragment(), AdapterView.OnItemSelectedListener {
    private var menuDialogListener: MenuDialogListener? = null
    private var imgReqCode = 100
    private lateinit var dialog: MaterialAlertDialogBuilder
    private lateinit var dialogView: View
    private lateinit var subCategories: List<SubCategory>
    private lateinit var imgPath: String

    interface MenuDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, id: Int, name: String, sub_category: Int, price: String, image: String)
    }

    companion object {
        fun newInstance(title: Int, id: Int, name: String, sub_category: Int, price: String, image: String): MenuDialogFragment {
            val menuDialogFragment = MenuDialogFragment()
            val args = Bundle()

            args.putInt("dialog_title", title)
            args.putInt("menu_id", id)
            args.putString("menu_name", name)
            args.putInt("sub_category", sub_category)
            args.putString("price", price)
            args.putString("image", image)

            menuDialogFragment.arguments = args
            return menuDialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        menuDialogListener = targetFragment as MenuDialogListener

        dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_menu, null)

        val title = arguments?.getInt("dialog_title")
        val id = arguments?.getInt("menu_id", -1)
        val name = arguments?.getString("menu_name", "")
        val subcategory = arguments?.getInt("sub_category", -1)
        val price = arguments?.getString("price", "0")
        val image = arguments?.getString("image", "placeholder")

        dialogView.img_dialog_menu.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, imgReqCode)
        }

        dialogView.spinner_menu.setLabel(resources.getString(R.string.sub_category))
        var subCategoryViewModel = ViewModelProvider(this).get(SubCategoryViewModel::class.java)
        subCategoryViewModel.loadSubCategories()

        subCategoryViewModel.getSubCategories().observe(requireParentFragment().viewLifecycleOwner, Observer {
            subCategories = it
            var subCategoryList = subCategories.map { it.name }

            var spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subCategoryList)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            dialogView.spinner_menu.setAdapter(spinnerAdapter)
            dialogView.spinner_menu.getSpinner().onItemSelectedListener = this

            if (title == R.string.edit) {
                val subcategory = subCategories.find { it.id == subcategory }
                val position = subCategories.indexOf(subcategory)
                dialogView.spinner_menu.getSpinner().setSelection(position)
            }
        })

        if (title != null) {
            dialog.setTitle(resources.getString(title))
        }
        if (title == R.string.edit) {
            dialogView.edit_dialog_menu.setText(name)
            dialogView.edit_dialog_price.setText(price)
            Picasso.get().load("http://menu-order.khaingthinkyi.me/"+image).into(dialogView.img_dialog_menu) // .resize(130, 130).into(dialogView.img_dialog_menu)
        }

        dialog.setView(dialogView)
            .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                val name = dialogView.edit_dialog_menu.text.toString()
                val position = dialogView.spinner_menu.getSpinner().selectedItemId.toInt()
                val subcategory = subCategories[position].id
                val price = dialogView.edit_dialog_price.text.toString()

                if (!this::imgPath.isInitialized) {
                    imgPath = "placeholder"
                    if (title == R.string.edit) {
                        imgPath = "http://menu-order.khaingthinkyi.me/"+image
                    }
                }

//                val file = File(imgPath)
//                val requestBody = RequestBody.create(MediaType.parse("multipart/formdata"), file)
//                val image = MultipartBody.Part.createFormData("","",requestBody)
                menuDialogListener?.onDialogPositiveClick(this, id!!, name, subcategory, price, imgPath)
            }
        return dialog.create()
    }

    override fun onNothingSelected(p0: AdapterView<*>) {

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == imgReqCode) {
            val imgData: Uri? = data!!.data
            val imgProjection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = requireActivity().contentResolver.query(imgData!!, imgProjection, null, null, null)
            cursor!!.moveToFirst()
            val imgIndex: Int = cursor.getColumnIndex(imgProjection[0])
            imgPath = cursor.getString(imgIndex)
            cursor.close()

            var bitmap = BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(imgData!!))
            bitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, true)
            dialogView.img_dialog_menu.setImageBitmap(bitmap)
        }
    }
}