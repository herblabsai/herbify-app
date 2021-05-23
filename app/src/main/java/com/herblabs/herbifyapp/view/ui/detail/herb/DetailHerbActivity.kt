package com.herblabs.herbifyapp.view.ui.detail.herb

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
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
import com.herblabs.herbifyapp.view.ui.home.HomeViewModel

class DetailHerbActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHerbBinding

    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var homeViewModel: HomeViewModel

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

        // sementara menggunakan top recipes
        val db = FirebaseFirestore.getInstance()
        showProgressBar(true)
        getRecipes(db)

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

    private fun getRecipes(db: FirebaseFirestore) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        homeViewModel.getRecipes(db)
        homeViewModel.recipe.observe({lifecycle}, {
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