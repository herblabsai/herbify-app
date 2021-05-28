package com.herblabs.herbifyapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.databinding.ItemCaptureBinding
import com.herblabs.herbifyapp.view.ui.identify.IdentifyActivity

class CaptureAdapter(private val activity: Activity) : PagedListAdapter<CaptureEntity, CaptureAdapter.CaptureViewHolder>(DIFF_CALLBACK) {

    inner class CaptureViewHolder(private val binding : ItemCaptureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(capture : CaptureEntity) {
            with(binding){

                Glide.with(itemView.context)
                    .load(capture.imageUri)
                    .apply(RequestOptions().override(120,280))
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .into(binding.imgCapture)

                linearLayout.setOnClickListener {
                    val intent = Intent(activity, IdentifyActivity::class.java)
                    intent.putExtra(IdentifyActivity.EXTRA_CAPTURE, capture)
                    activity.startActivity(intent)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<CaptureEntity> = object : DiffUtil.ItemCallback<CaptureEntity>() {
            override fun areItemsTheSame(
                oldItem: CaptureEntity,
                newItem: CaptureEntity
            ): Boolean {
                return oldItem.imageUri == newItem.imageUri
            }

            override fun areContentsTheSame(
                oldItem: CaptureEntity,
                newItem: CaptureEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: CaptureViewHolder, position: Int) {
        holder.bind(getItem(position) as CaptureEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaptureViewHolder {
        val binding = ItemCaptureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CaptureViewHolder(binding)
    }
}