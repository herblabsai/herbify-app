package com.herblabs.herbifyapp.view.ui.detail.herb

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.firebase.model.ListIdRecipe
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.view.ui.main.MainActivity
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


}