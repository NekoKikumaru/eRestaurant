package com.ptzkg.erestaurant.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R

abstract class SwipeController(context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private var context = context
    private val background = ColorDrawable()
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val icon: Int = if (dX > 0) R.drawable.ic_baseline_edit else R.drawable.ic_baseline_delete
        val iconDrawable = ContextCompat.getDrawable(context, icon)!!
        val intrinsicWidth = iconDrawable.intrinsicWidth
        val intrinsicHeight = iconDrawable.intrinsicHeight

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            if (dX > 0) clearCanvas(c, itemView.left.toFloat(), itemView.top.toFloat(), itemView.left+dX, itemView.bottom.toFloat()) else clearCanvas(c, itemView.right+dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        if (dX > 0) {
            background.color = Color.parseColor("#00EA9C")
            background.setBounds(itemView.left, itemView.top, itemView.left+dX.toInt(), itemView.bottom)
        }
        else {
            background.color = Color.parseColor("#FC4205")
            background.setBounds(itemView.right+dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        }

        background.draw(c)

        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val iconBottom = iconTop + intrinsicHeight

        if (dX > 0) {
            val iconLeft = iconMargin
            val iconRight = iconMargin + intrinsicWidth
            iconDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        }
        else {
            val iconLeft = itemView.right - iconMargin - intrinsicWidth
            val iconRight = itemView.right - iconMargin
            iconDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        }

        iconDrawable.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, clearPaint)
    }
}