package com.herblabs.herbifyapp.view.ui.detail.herb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.databinding.ActivityDetailHerbBinding
import com.herblabs.herbifyapp.utils.HorizontalMarginItemDecoration
import com.herblabs.herbifyapp.view.adapter.RecipesAdapter
import com.herblabs.herbifyapp.vo.StatusMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailHerbActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHerbBinding

    private lateinit var recipesAdapter: RecipesAdapter
    private val viewModel: DetailHerbViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHerbBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.elevation = 0f

        val herb = intent.getParcelableExtra<HerbsFirestore>(EXTRA_HERB)
        if (herb != null) {
            setupLayout(herb)
        } else {
            val predicted = intent.getParcelableExtra<PredictedEntity>(EXTRA_PREDICTED)
            if (predicted != null){
                viewModel.getHerb(predicted.name).observe(this, { result ->
                    if (result!=null){
                        when(result.status){
                            StatusMessage.LOADING -> {
                                showProgressBar(true)
                                Log.d(TAG, "Loading")
                            }
                            StatusMessage.SUCCESS ->{
                                result.data?.forEach {
                                    Log.d(TAG, "Success : $it")
                                }
                                result.data?.get(0)?.let { setupLayout(it) }
                            }
                            StatusMessage.ERROR -> {
                                showProgressBar(false)
                                onBackPressed()
                                Log.e(TAG, "Error")
                                Toast.makeText(this, "Error Mengambil data", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                showProgressBar(false)
                                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                })
            }
        }

        binding.btnBack.setOnClickListener{
            onBackPressed()
        }

    }

    private fun setupLayout(herb: HerbsFirestore) {
        binding.apply {
            tvTitle.text = herb.name
            tvLatinName.text = herb.latinName
            tvDescription.text = herb.overview
            Glide.with(this@DetailHerbActivity)
                .load(herb.imageUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                .into(imgHerb)
            if(herb.benefits.isNotEmpty()){
                val builder = StringBuilder()

                for(item in herb.benefits.indices){
                    builder.append("\u2022 " + herb.benefits[item] + "\n")
                }

                tvBenefits.text = builder.toString()
            }

            if(herb.dosage.isNotEmpty()){
                val builder = StringBuilder()

                for(item in herb.dosage.indices){
                    builder.append("\u2022 " + herb.dosage[item] + "\n")
                }

                tvDosage.text = builder.toString()
            }else{
                tvDosage.visibility = View.GONE
                dosage.visibility = View.GONE
            }

            if (herb.recipes.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.Main) {
                    val listId = withContext(Dispatchers.Main) {
                        val data = ArrayList<Int>()
                        for (i in herb.recipes.indices) {
                            data.add(herb.recipes[i].id)
                            Log.d(TAG, "SUCCESS ID: ${herb.recipes[i].id}")
                        }
                        data
                    }
                    Log.d(TAG, "SUCCESS : $listId")
                    getRecipes(listId)
                }
            }

            val collapsingToolbarLayout = collapsingToolbar
            val appBarLayout = appBar
            appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
                var isShow = true
                var scrollRange = -1

                override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout!!.totalScrollRange
                    }
                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbarLayout.title = herb.name
                        isShow = true
                    } else if (isShow) {
                        collapsingToolbarLayout.title = " " //careful there should a space between double quote otherwise it wont work
                        isShow = false
                    }
                }
            })
        }
    }

    private fun getRecipes(listId: ArrayList<Int>) {
        viewModel.getRecipe(listId).observe(this, { result ->
            if (result!=null){
                when(result.status){
                    StatusMessage.LOADING -> {
                        showProgressBar(true)
                        Log.d(TAG, "Loading")
                    }
                    StatusMessage.SUCCESS ->{
                        result.data?.forEach {
                            Log.d(TAG, "Success : $it")
                        }
                        binding.rvRecipes.apply {
                            recipesAdapter = RecipesAdapter(result.data as List<Recipe>)
                            this.adapter = recipesAdapter
                            this.layoutManager = LinearLayoutManager(this@DetailHerbActivity, LinearLayoutManager.HORIZONTAL, false)
                            addItemDecoration(HorizontalMarginItemDecoration(24))
                            recipesAdapter.notifyDataSetChanged()
                            showProgressBar(false)
                        }
                    }
                    StatusMessage.ERROR -> {
                        showProgressBar(false)
                        Log.e(TAG, "Error")
                        Toast.makeText(this, "Error Mengambil data", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        showProgressBar(false)
                        Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_LONG).show()
                    }
                }
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
        const val EXTRA_PREDICTED = "extra_predicted"
        const val TAG = "DetailHerbActivity"
    }
}