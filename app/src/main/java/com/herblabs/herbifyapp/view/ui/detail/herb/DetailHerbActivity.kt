package com.herblabs.herbifyapp.view.ui.detail.herb

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.databinding.ActivityDetailHerbBinding
import com.herblabs.herbifyapp.utils.HorizontalMarginItemDecoration
import com.herblabs.herbifyapp.view.adapter.RecipesAdapter

class DetailHerbActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHerbBinding

    private lateinit var recipesAdapter: RecipesAdapter
    private val detailHerbViewModel: DetailHerbViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHerbBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.elevation = 0f

        val herb = intent.getParcelableExtra<HerbsFirestore>(EXTRA_HERB)
        binding.tvTitle.text = herb?.name
        val latinName = "(${herb?.latinName})"
        binding.tvLatinName.text = latinName
        binding.tvDescription.text = herb?.overview
        Glide.with(this)
            .load(herb?.imageUrl)
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_error))
            .into(binding.imgHerb)

        if(herb?.benefits?.isNotEmpty() == true){
            val builder = StringBuilder()

            for(item in herb.benefits.indices){
                builder.append("\u2022 " + herb.benefits[item] + "\n")
            }

            binding.tvBenefits.text = builder.toString()
        }

        if(herb?.dosage?.isNotEmpty() == true){
            val builder = StringBuilder()

            for(item in herb.dosage.indices){
                builder.append("\u2022 " + herb.dosage[item] + "\n")
            }

            binding.tvDosage.text = builder.toString()
        }else{
            binding.tvDosage.visibility = View.GONE
            binding.dosage.visibility = View.GONE
        }

        val db = FirebaseFirestore.getInstance()
        showProgressBar(true)

        if (herb?.recipes?.isNotEmpty() == true) {
            for(idRecipe in herb.recipes.indices){
                detailHerbViewModel.getRecipes(db, herb.recipes[idRecipe].id)
                detailHerbViewModel.recipe.observe({lifecycle}, {
                    binding.rvRecipes.apply {
                        recipesAdapter = RecipesAdapter(it)
                        this.adapter = recipesAdapter
                        this.layoutManager = LinearLayoutManager(this@DetailHerbActivity, LinearLayoutManager.HORIZONTAL, false)
                        addItemDecoration(HorizontalMarginItemDecoration(24))
                        recipesAdapter.notifyDataSetChanged()
                        showProgressBar(false)
                    }
                })
            }
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
                    collapsingToolbarLayout.title = herb?.name
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

    private fun showProgressBar(state: Boolean){
        if(state){
            binding.progressBarHerbs.visibility = View.VISIBLE
        }else{
            binding.progressBarHerbs.visibility = View.GONE
        }
    }

    companion object{
        const val EXTRA_HERB = "extra_herb"
    }
}