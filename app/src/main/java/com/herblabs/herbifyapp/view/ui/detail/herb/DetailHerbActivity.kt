package com.herblabs.herbifyapp.view.ui.detail.herb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.databinding.ActivityDetailHerbBinding
import com.herblabs.herbifyapp.utils.HorizontalMarginItemDecoration
import com.herblabs.herbifyapp.view.adapter.RecipesAdapter
import com.herblabs.herbifyapp.view.ui.camera.CameraActivity
import com.herblabs.herbifyapp.view.ui.main.MainActivity
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

        Log.d(TAG, "Success : $herb")
        if (herb?.recipes?.isNotEmpty() == true) {
//            for(idRecipe in herb.recipes.indices){
//                detailHerbViewModel.getRecipes(db, herb.recipes[idRecipe].id)
//                detailHerbViewModel.recipe.observe({lifecycle}, {
//                    binding.rvRecipes.apply {
//                        recipesAdapter = RecipesAdapter(it)
//                        this.adapter = recipesAdapter
//                        this.layoutManager = LinearLayoutManager(this@DetailHerbActivity, LinearLayoutManager.HORIZONTAL, false)
//                        addItemDecoration(HorizontalMarginItemDecoration(24))
//                        recipesAdapter.notifyDataSetChanged()
//                        showProgressBar(false)
//                    }
//                })
//            }
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

    private fun getRecipes(listId: ArrayList<Int>) {
        detailHerbViewModel.getRecipe(listId).observe(this, { result ->
            Log.d(TAG, "Cek : ${result.data}")
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
                        Toast.makeText(this, "Error Fetching data", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        showProgressBar(false)
                        Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_LONG).show()
                    }
                }
            } else{
                Log.e(TAG, "onUploadResult: Result Null")
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
        const val TAG = "DetailHerbActivity"
    }
}