package com.herblabs.herbifyapp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalMarginItemDecoration(private val spaceHeight: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect){

            // jika bukan item pertama
            if(parent.getChildAdapterPosition(view) != 0){
                left = spaceHeight
            }

            top = spaceHeight
            bottom = spaceHeight
        }
    }
}