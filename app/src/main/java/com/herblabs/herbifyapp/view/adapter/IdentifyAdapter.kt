package com.herblabs.herbifyapp.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.databinding.ItemIdentifyBinding
import com.herblabs.herbifyapp.view.ui.detail.herb.DetailHerbActivity
import kotlin.math.roundToInt

class IdentifyAdapter(var data: List<PredictedEntity>): RecyclerView.Adapter<IdentifyAdapter.IdentifyViewHolder>() {
    class IdentifyViewHolder(private val binding: ItemIdentifyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(response: PredictedEntity){
            binding.tvName.text = response.name
            val confident = response.confident.roundToInt()
            val toString = "$confident%"
            binding.tvConfident.text = toString
            val imageUrl = response.imageUrl.replace(" ", "%20")
            Glide.with(itemView.context)
                .load(response.imageUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                .into(binding.imageView)
            itemView.setOnClickListener {
                // TODO : GOTO HERBS DETAIL
                Intent(itemView.context, DetailHerbActivity::class.java).apply{
                    this.putExtra(DetailHerbActivity.EXTRA_PREDICTED, response)
                    itemView.context.startActivity(this)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdentifyViewHolder {
        val view = ItemIdentifyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IdentifyViewHolder(view)
    }

    override fun onBindViewHolder(holder: IdentifyViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}