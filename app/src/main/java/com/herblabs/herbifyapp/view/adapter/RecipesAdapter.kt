package com.herblabs.herbifyapp.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.databinding.ItemTopRecipesBinding
import com.herblabs.herbifyapp.view.ui.detail.recipe.DetailRecipeActivity

class RecipesAdapter(var recipesList: List<Recipe>): RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {
    class RecipesViewHolder(private val binding: ItemTopRecipesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(recipe: Recipe){
            binding.tvTitle.text = recipe.name
            binding.tvDescription.text = recipe.overview
            Glide.with(itemView.context)
                .load(recipe.imageUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                .into(binding.imgRecipe)
            itemView.setOnClickListener {
                Intent(itemView.context, DetailRecipeActivity::class.java).apply{
                    this.putExtra(DetailRecipeActivity.EXTRA_RECIPE, recipe)
                    itemView.context.startActivity(this)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        val view = ItemTopRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val item = recipesList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return if(recipesList.size > 5) {
            5
        }else{
            recipesList.size
        }
    }
}