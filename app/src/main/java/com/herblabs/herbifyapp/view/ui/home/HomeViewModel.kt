package com.herblabs.herbifyapp.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HerbsRepository
): ViewModel() {

    fun getAllHerbs(): LiveData<Resource<List<HerbsFirestore>>> =
        repository.getHerbs()

    fun getRecipes(): LiveData<Resource<List<Recipe>>> =
        repository.getRecipesWherePopular()
}