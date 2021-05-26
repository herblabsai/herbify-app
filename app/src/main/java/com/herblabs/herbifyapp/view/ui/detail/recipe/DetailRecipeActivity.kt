package com.herblabs.herbifyapp.view.ui.detail.recipe

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.databinding.ActivityDetailRecipeBinding
import com.herblabs.herbifyapp.view.adapter.StepsAdapter

class DetailRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecipeBinding
    private lateinit var stepsAdapter: StepsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.elevation = 0f

        val recipe = intent.getParcelableExtra<Recipe>(EXTRA_RECIPE)

        if (recipe != null) {
            binding.tvTitle.text = recipe.name
            binding.tvDescription.text = recipe.overview
            binding.tvCredit.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.referenceUrl))
                startActivity(intent)
            }
            binding.rvStep.apply {
                stepsAdapter = StepsAdapter(recipe.steps)
                this.adapter = stepsAdapter
                this.layoutManager = LinearLayoutManager(this@DetailRecipeActivity)
                stepsAdapter.notifyDataSetChanged()
            }

            if(recipe.ingredients.isNotEmpty()){
                val builder = StringBuilder()

                for(item in recipe.ingredients.indices){
                    builder.append("\u2022 " + recipe.ingredients[item] + "\n")
                }

                binding.tvIngredients.text = builder.toString()
            }

            Glide.with(this)
                .load(recipe.imageUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                .into(binding.imgRecipe)
        }

        val collapsingToolbarLayout = binding.collapsingToolbar
        val appBarLayout = binding.appBar
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = recipe?.name
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = " " //careful there should a space between double quote otherwise it wont work
                    isShow = false
                }
            }

        })

        binding.btnBack.setOnClickListener{
            onBackPressed()
        }
    }

    companion object{
        const val EXTRA_RECIPE = "recipe"
    }
}