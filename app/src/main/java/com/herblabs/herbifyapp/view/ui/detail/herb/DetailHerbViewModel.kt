package com.herblabs.herbifyapp.view.ui.detail.herb

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class DetailHerbViewModel @Inject constructor(
    private val repository: HerbsRepository
): ViewModel() {

    val TAG: String = "DetailViewModel"

    fun getRecipe(listID: List<Int>): LiveData<Resource<List<Recipe>>> {
        return repository.getRecipeByListID(listID)
    }
    fun getHerb(name: String): LiveData<Resource<List<HerbsFirestore>>> {
        return repository.getHerbByName(name)
    }


}