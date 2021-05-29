package com.herblabs.herbifyapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.data.source.remote.response.Data
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import com.herblabs.herbifyapp.databinding.ItemIdentifyBinding
import kotlin.math.roundToInt

class IdentifyAdapter(var data: List<Data>): RecyclerView.Adapter<IdentifyAdapter.IdentifyViewHolder>() {
    class IdentifyViewHolder(private val binding: ItemIdentifyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(response: Data){
            binding.tvName.text = response.name
            val confident = response.confident.roundToInt()
            val toString = "$confident%"
            binding.tvConfident.text = toString
//            Glide.with(itemView.context)
//                .load(predicted.imageUrl)
//                .apply(
//                    RequestOptions.placeholderOf(R.drawable.ic_loading)
//                        .error(R.drawable.ic_error))
//                .into(binding.imageHerb)
            itemView.setOnClickListener {
                // TODO : GOTO HERBS DETAIL
//                Intent(itemView.context, DetailRecipeActivity::class.java).apply{
//                    this.putExtra(DetailRecipeActivity.EXTRA_RECIPE, recipe)
//                    itemView.context.startActivity(this)
//                }

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