package com.herblabs.herbifyapp.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.databinding.ItemHerbsBinding
import com.herblabs.herbifyapp.view.ui.detail.herb.DetailHerbActivity
import com.herblabs.herbifyapp.view.ui.detail.herb.DetailHerbActivity.Companion.EXTRA_HERB

class HerbsAdapter(var herbsList: List<HerbsFirestore>): RecyclerView.Adapter<HerbsAdapter.RecipesViewHolder>() {
    class RecipesViewHolder(private val binding: ItemHerbsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(herbs: HerbsFirestore){
            binding.tvTitle.text = herbs.name
            binding.tvDescription.text = herbs.overview
            Glide.with(itemView.context)
                .load(herbs.imageUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                .into(binding.imgRecipe)
            itemView.setOnClickListener {
                Intent(itemView.context, DetailHerbActivity::class.java).apply{
                    this.putExtra(EXTRA_HERB, herbs)
                    itemView.context.startActivity(this)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        val view = ItemHerbsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val item = herbsList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = herbsList.size
}